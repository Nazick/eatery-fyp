package com.eatery.engine.sentimentAnalysis;

import com.eatery.engine.sentimentAnalysis.Parser.Parse;
import edu.stanford.nlp.ling.BasicDocument;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class TypedDependencyEngine {

    private static ArrayList<String> relationList = new ArrayList<String>(); // list
    // for
    // relationship
    // related
    // to
    // opinion
    private static StanfordCoreNLP pipeline;
    private static StanfordCoreNLP tokenizer;

    public TypedDependencyEngine() {
        // initialization
        Properties pipelineProps = new Properties();
        Properties tokenizerProps = new Properties();
        pipelineProps.setProperty("sentiment.model", "model.ser.gz");
        pipelineProps.setProperty("annotators", "parse, sentiment");
        pipelineProps.setProperty("enforceRequirements", "false");
        tokenizerProps.setProperty("annotators", "tokenize, ssplit");
        pipeline = new StanfordCoreNLP(pipelineProps);
        tokenizer = new StanfordCoreNLP(tokenizerProps);
        Parser.init();
        Analyzer.init();
        // relationship related to opinion
        relationList.add("amod");
        relationList.add("nsubj");
        relationList.add("advmod");
    }

    // main method
    public static void main(String args[]) throws Exception {
        new TypedDependencyEngine();
        String sentence = "The food was excellent, the service was excellent, and the drinks were flowing";
        List<List<MyWord>> sentences = sentiTyped(sentence);
        //System.out.println(sentence);
        SentimentAnalyzer eaterySentimentAnalyzer = new SentimentAnalyzer();
        for (List<MyWord> list : sentences) {
            String temp = generateSentence(list);
            System.out.println(temp + " - " + eaterySentimentAnalyzer.findSentimentScore(temp) + " (" + eaterySentimentAnalyzer.findSentimentState(temp) + ")");
        }


    }

    /**
     * sentiment analysis using stanfordNLP
     *
     * @param review
     * @return
     */
    public static ArrayList<String> sentimentAnalysis(String review) {
        ArrayList<String> sentiments = new ArrayList<String>();
        Annotation annotation = tokenizer.process(review);
        pipeline.annotate(annotation);
        for (CoreMap sentence : annotation
                .get(CoreAnnotations.SentencesAnnotation.class)) {
            String polarity = sentence
                    .get(SentimentCoreAnnotations.ClassName.class);
            sentiments.add(polarity);
        }
        return sentiments;
    }

    /**
     * get the sentence by dividing based on opinion
     *
     * @param inText
     * @return list of sentence
     */
    public static List<List<MyWord>> sentiTyped(String inText) {
        StringBuffer sb = new StringBuffer();

        sb.append(inText).append("\n");
        List<List<Word>> theseSentences = segmentText(inText);
        List<ArrayList<TypedDependency>> tdLists = generateTypedDependency(theseSentences);
        ArrayList<TypedDependency> tdList = tdLists.get(0);
        List<List<MyWord>> myWordLists = new ArrayList<List<MyWord>>();
        List<TypedDependency> discoveredTdList = new ArrayList<TypedDependency>();
        for (TypedDependency typedDependency : tdList) {
            if (discoveredTdList.contains(typedDependency)) {
                continue;
            }
            String grString = typedDependency.reln().toString();
            IndexedWord gov = typedDependency.gov();
            IndexedWord dep = typedDependency.dep();
            // nsubj relationship which has adjective
            if (grString.equals("nsubj")
                    && gov.tag().length() >= 2
                    && (gov.tag().substring(0, 2).equals("JJ") || dep.tag()
                    .substring(0, 2).equals("JJ"))) {
                ArrayList<MyWord> myWordList = new ArrayList<MyWord>();
                MyWord govWord = new MyWord(gov);
                MyWord depWord = new MyWord(dep);
                myWordList.add(depWord);
                myWordList.add(govWord);
                // add the words connected identified two words
                for (TypedDependency td : tdList) {
                    if (td.reln().toString().equalsIgnoreCase("root")
                            || td.reln().toString().equalsIgnoreCase("conj")) {
                        continue;
                    }
                    if (td.gov().equals(gov) || td.gov().equals(dep)) {
                        MyWord newWord = new MyWord(td.dep());
                        if (!myWordList.contains(newWord)) {
                            myWordList.add(newWord);
                        }
                        if (relationList.contains(td.reln().toString())) {
                            discoveredTdList.add(td);
                        }
                    } else if (td.dep().equals(gov) || td.dep().equals(dep)) {
                        MyWord newWord = new MyWord(td.gov());
                        if (!myWordList.contains(newWord)) {
                            myWordList.add(newWord);
                        }
                        if (relationList.contains(td.reln().toString())) {
                            discoveredTdList.add(td);
                        }
                    }
                }
                myWordLists.add(myWordList);
            }
            // amod relationship
            else if (grString.equals("amod")) {
                ArrayList<MyWord> myWordList = new ArrayList<MyWord>();
                MyWord govWord = new MyWord(gov);
                MyWord depWord = new MyWord(dep);
                myWordList.add(depWord);
                myWordList.add(govWord);
                // add the words connected identified two words
                for (TypedDependency td : tdList) {
                    if (td.reln().toString().equalsIgnoreCase("root")) {
                        continue;
                    }
                    if (td.gov().equals(gov) || td.gov().equals(dep)) {
                        MyWord newWord = new MyWord(td.dep());
                        if (!myWordList.contains(newWord)) {
                            myWordList.add(newWord);
                        }
                        if (relationList.contains(td.reln().toString())) {
                            discoveredTdList.add(td);
                        }
                    } else if (td.dep().equals(gov) || td.dep().equals(dep)) {
                        MyWord newWord = new MyWord(td.gov());
                        if (!myWordList.contains(newWord)) {
                            myWordList.add(newWord);
                        }
                        if (relationList.contains(td.reln().toString())) {
                            discoveredTdList.add(td);
                        }
                    }
                }
                myWordLists.add(myWordList);
            }
            // advmod relationship
            else if (grString.equals("advmod")) {
                ArrayList<MyWord> myWordList = new ArrayList<MyWord>();
                MyWord govWord = new MyWord(gov);
                MyWord depWord = new MyWord(dep);
                myWordList.add(depWord);
                myWordList.add(govWord);
                // add the words connected identified two words
                for (TypedDependency td : tdList) {
                    if (td.reln().toString().equalsIgnoreCase("root")) {
                        continue;
                    }
                    if (td.gov().equals(gov) || td.gov().equals(dep)) {
                        MyWord newWord = new MyWord(td.dep());
                        if (!myWordList.contains(newWord)) {
                            myWordList.add(newWord);
                        }
                        if (relationList.contains(td.reln().toString())) {
                            discoveredTdList.add(td);
                        }
                    } else if (td.dep().equals(gov) || td.dep().equals(dep)) {
                        MyWord newWord = new MyWord(td.gov());
                        if (!myWordList.contains(newWord)) {
                            myWordList.add(newWord);
                        }
                        if (relationList.contains(td.reln().toString())) {
                            discoveredTdList.add(td);
                        }
                    }
                }
                myWordLists.add(myWordList);
            }
        }
        if (myWordLists.isEmpty()) {
            ArrayList<String> sentiment = sentimentAnalysis(inText);
            // System.out.println(sentiment);
            sb.append(sentiment.get(0)).append("\n");
        }
        for (List<MyWord> list : myWordLists) {
            String generatedSentence = generateSentence(list);
            // System.out.println(generatedSentence);
            sb.append(generatedSentence).append("\n");
            ArrayList<String> sentiment = sentimentAnalysis(generatedSentence);
            // System.out.println(sentiment);
            sb.append(sentiment.get(0)).append("\n");
        }
        return myWordLists;
    }

    /**
     * generating typed dependency relation for list of sentence
     *
     * @param theseSentences
     * @return
     */
    public static List<ArrayList<TypedDependency>> generateTypedDependency(
            List<List<Word>> theseSentences) {
        List<ArrayList<TypedDependency>> tdLists = new ArrayList<ArrayList<TypedDependency>>();
        for (List<Word> sentence : theseSentences) {
            Parse parse = Parser.parse(sentence);
            ArrayList<TypedDependency> tdList = (ArrayList<TypedDependency>) parse.second;
            tdLists.add(tdList);
        }
        return tdLists;
    }

    /**
     * breaking the sentence and words inside sentence
     *
     * @param text
     * @return
     */
    public static List<List<Word>> segmentText(String text) {
        BasicDocument<Word> basicDocument = BasicDocument.init(text);
        Pair<List<String>, List<List<Word>>> thisResult = Segmenter
                .getSentences(basicDocument);
        return thisResult.second;
    }

    /**
     * create the sentence from list of word
     *
     * @param myWordList
     * @return
     */
    public static String generateSentence(List<MyWord> myWordList) {
        Collections.sort(myWordList);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < myWordList.size(); i++) {
            if (i == 0) {
                sb.append(myWordList.get(i).getValue());
            } else {
                sb.append(" ").append(myWordList.get(i).getValue());
            }
        }
        return sb.toString();
    }
}
