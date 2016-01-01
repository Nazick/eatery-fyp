package com.eatery.system;

import com.eatery.implicit.ImplicitAspects;
import com.eatery.opennlp.OpennlpTagger;
import com.eatery.preprocessing.LanguageDetect;
import com.eatery.preprocessing.SpellCorrector;
import com.eatery.sentimentAnalysis.MyWord;
import com.eatery.sentimentAnalysis.TypedDependencyEngine;
import com.eatery.utils.JsonData;
import com.eatery.utils.Sentence;
import com.eatery.utils.WordTag;
import opennlp.tools.util.Span;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nazick on 11/29/15.
 */
public class EateryMain {

    final static String processedFilePath = "src/main/resources/" +
   //         "processedFile.json";
            "review_100_A.json";

    final static String filePathRead = "src/main/resources/" +
            "review_100_A.json";


    public static void main(String[] args){
        EateryMain eateryMain = new EateryMain();
        //eateryMain.preProcessData();
        eateryMain.process();
    }

    public void process(){
        LanguageDetect languageDetect = new LanguageDetect();
        ImplicitAspects implicitAspects = new ImplicitAspects();

        try {
            File file = new File(processedFilePath);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            int count = 0;
            long startTime = System.currentTimeMillis();
            while ((line = br.readLine()) != null) {
                count++;
                String restaurentId;
                String review;
                JsonData jsonData = this.splitJson(line);

                if(!jsonData.equals(null)){
                    review = jsonData.getReview();
                    restaurentId = jsonData.getRestaurentId();
                    List<Sentence> sentencesInReview = new ArrayList<>();
                    List<Sentence> scoredSentences = new ArrayList<>();

                    System.out.println("Restaurent ID : " + restaurentId);

                    if(languageDetect.isReviewInEnglish(review)){

                        TypedDependencyEngine typedDependencyEngine = new TypedDependencyEngine();
                        String[] sentences = OpennlpTagger.detectSentence(review);

                        //Tagging explicit aspects using OpenNLP
                        for(String sentence: sentences){
                            Sentence savedSentence = this.saveSentenceTags(OpennlpTagger.tag(sentence), sentence);
                            sentencesInReview.add(savedSentence);
                        }

                        //Tagging implicit Aspects
                        sentencesInReview = implicitAspects.find(sentencesInReview);

                        //Sentiment analysis per sentence
                        for(Sentence sentence : sentencesInReview){
                            scoredSentences.add(this.getSentimentScore(sentence, typedDependencyEngine));
                        }

                    }else{
                        System.out.println("Review not in English");
                    }
                }
            }
            System.out.println(System.currentTimeMillis() - startTime);
            br.close();
            fr.close();
            System.out.println("Done...");

        }catch(Exception e){
            e.printStackTrace();
            //System.out.println(e.toString());
        }
    }

    public void preProcessData(){
        EateryMain eateryMain = new EateryMain();
        org.json.simple.JSONObject jsonObject;
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        LanguageDetect languageDetect = new LanguageDetect();

        try {
            File inputFile = new File(filePathRead);
            File processedFile = new File(processedFilePath);

            FileReader fr = new FileReader(inputFile);
            BufferedReader br = new BufferedReader(fr);

            Writer output = new BufferedWriter(new FileWriter(processedFile,false));

            String line;

            long startTime = System.currentTimeMillis();

            while ((line = br.readLine()) != null) {
                String review;
                String tempReview = "";

                Object obj = parser.parse(line);
                jsonObject = (JSONObject) obj;


                if(!jsonObject.equals(null)){
                    review = (String) jsonObject.get("text");
                    if(languageDetect.isReviewInEnglish(review)){
                        String[] sentences = OpennlpTagger.detectSentence(review);

                        for(String sentence: sentences){
                            tempReview += eateryMain.spellCorrect(sentence);
                        }
                        jsonObject.put("text",tempReview);
                        output.write(jsonObject.toJSONString());
                        output.write("\n");
                    }else{
                        System.out.println("Review not in English");
                    }
                }
            }

            br.close();
            fr.close();
            output.close();

            System.out.println(System.currentTimeMillis() - startTime);
            System.out.println("Done...");

        }catch(Exception e){
            e.getStackTrace();
            //System.out.println(e.toString());
        }
    }

    public Sentence getSentimentScore(Sentence sentence, TypedDependencyEngine typedDependencyEngine){
        List<List<MyWord>> sentences = typedDependencyEngine.sentiTyped(sentence.getLine());

        for(List<MyWord> words: sentences){
            String temp = typedDependencyEngine.generateSentence(words);
            Integer score = typedDependencyEngine.findSentimentScore(temp);

            for(MyWord myWord:words){
                if(sentence.getTags().containsKey(myWord.getIndex() -1)){
                    sentence.getTags().get(myWord.getIndex() -1).setScore(score);
                }else if(sentence.getImplicitTags().containsKey(myWord.getIndex()-1)){
                    sentence.getImplicitTags().get(myWord.getIndex()-1).setScore(score);
                }
            }
        }

        if(sentences.size() == 0){
            Integer score = typedDependencyEngine.findSentimentScore(sentence.getLine());

            for(WordTag wordTag: sentence.getTags().values()){
                wordTag.setScore(score);
            }

            for(WordTag wordTag: sentence.getImplicitTags().values()){
                wordTag.setScore(score);
            }
        }

        return sentence;
    }

    public BufferedReader readFile(String filePath){

        BufferedReader bufferedReader = null;
        try {
            FileInputStream fstream = new FileInputStream(filePath);
            DataInputStream in = new DataInputStream(fstream);
            bufferedReader = new BufferedReader(new InputStreamReader(in));
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filePath + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + filePath + "'");
        }

        return bufferedReader;
    }

    public String detokenize(String[] tokens){
        String line = new String();
        for(String word:tokens){
            if(line.isEmpty()){
                line += word;
            }else{
               line += " "+word;
            }
        }
        return line;
    }

    private String removeSymbols(String review){
        String[] tokens = OpennlpTagger.tokenizeSentence(review);

        for(int i = 0; i<tokens.length;i++){
            tokens[i] = tokens[i].replaceAll("[-+^:.\"*#!?()]","");
        }
        return this.detokenize(tokens);
    }

    private String spellCorrect(String review){
        SpellCorrector spellCorrector = new SpellCorrector();

        String[] tokens = OpennlpTagger.tokenizeSentence(review);
        String[] newTokens = new String[tokens.length];
        int count = 0;
        for(int i = 0; i<tokens.length;i++){
            tokens[i] = tokens[i].replaceAll("[-+^:,\"*#!?()]","");
        }
        for(int i = 0; i<tokens.length;i++){
            if(tokens[i].length() == 1){
                newTokens[i] = tokens[i];
            }else if(!tokens[i].isEmpty()) {
                newTokens[i] = spellCorrector.correct(tokens[i]);
            }else{
                newTokens[i] = tokens[i];
            }
            if(!tokens[i].equals(newTokens[i])){
                count++;
            }
        }
        //System.out.print(count + "   ");
        review = this.detokenize(newTokens);

        return review;
    }

    private JsonData splitJson(String json) {
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        JsonData jsonData = new JsonData();
        try {
            Object obj = parser.parse(json);
            JSONObject jsonObject = (JSONObject) obj;

            jsonData.setRestaurentId((String) jsonObject.get("business_id"));

            String review = (String) jsonObject.get("text");
            String formattedReview=review.replace("\n", "").replace("\r", "");
            jsonData.setReview(formattedReview);                // get review text from json

            jsonData.setReviewId((String) jsonObject.get("review_id"));

        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }finally {
            return jsonData;
        }
    }

    private Sentence saveSentenceTags(Span[] tags, String line){
        Sentence sentence = new Sentence(line);
        Map<Integer, WordTag> tagsMap = new HashMap<>();

        for(Span tagSpan: tags){
            WordTag tag = new WordTag();

            tag.setTag(tagSpan.getType());
            tag.setWord(sentence.getTokens()[tagSpan.getStart()]);
            tag.setWordIndex(tagSpan.getStart());

            tagsMap.put(tagSpan.getStart(), tag);
        }

        sentence.setTags(tagsMap);

        return sentence;
    }

}
