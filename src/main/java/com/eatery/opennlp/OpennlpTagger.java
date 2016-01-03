package com.eatery.opennlp;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by nazick on 7/24/15.
 */
public class OpennlpTagger {

    public static void main(String args[]) {
        String review = " The chips and salsa rock my world! The entree's are delicious! The staff is friendly and efficient";
        Span[] taggedTokens = null;
        OpennlpTagger opennlpTagger = new OpennlpTagger();
        String[] sentences = opennlpTagger.detectSentence(review);
        if (sentences != null) {
            for (String sentence : sentences) {
                String[] tokens = opennlpTagger.tokenizeSentence(sentence);
//                if (tokens != null) {
//                    taggedTokens = opennlpTagger.tagger(tokens);
//                    if (taggedTokens != null) {
//                        opennlpTagger.printTags(sentence, taggedTokens, tokens);
//                    }
//                }
                tokens = opennlpTagger.tokenizeSentence(opennlpTagger.detokenizeTokens(tokens));
            }
        }

    }

    //@params tokens String array
    public Span[] tag(String[] tokens){
        Span[] taggedTokens = null;
        if (tokens != null) {
            taggedTokens = this.tagger(tokens);
        }
        return taggedTokens;
    }

    //@params review String
    public static Span[] tag(String review){

        Span[] taggedTokens = null;
        OpennlpTagger opennlpTagger = new OpennlpTagger();
        String[] sentences = opennlpTagger.detectSentence(review);
        if (sentences != null) {
            for (String sentence : sentences) {
                String[] tokens = opennlpTagger.tokenizeSentence(sentence);
                if (tokens != null) {
                    taggedTokens = opennlpTagger.tagger(tokens);
//                    if (taggedTokens != null) {
//                        opennlpTagger.printTags(sentence, taggedTokens, tokens);
//                    }
                }
            }
        }
        return taggedTokens;
    }

    public Span[] tagger(String[] tokens) {
        InputStream eateryModel = null;
        Span[] taggedTokens = null;
        try {
            eateryModel = new FileInputStream("src/main/resources/opennlp/evaluation/models/reviews_1500.bin");
            TokenNameFinderModel model = new TokenNameFinderModel(eateryModel);

            NameFinderME nameFinder = new NameFinderME(model);
            taggedTokens = nameFinder.find(tokens);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (eateryModel != null) {
                try {
                    eateryModel.close();
                } catch (IOException e) {
                }
            }
        }

        return taggedTokens;
    }

    public static String[] detectSentence(String text) {
        InputStream sentenceModel = null;
        String[] sentences = null;
        try {
            sentenceModel = new FileInputStream("src/main/resources/opennlp/en-sent.bin");
            SentenceModel model = new SentenceModel(sentenceModel);

            SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
            sentences = sentenceDetector.sentDetect(text);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sentenceModel != null) {
                try {
                    sentenceModel.close();
                } catch (IOException e) {
                }
            }
        }

        return sentences;
    }

    public static String[] tokenizeSentence(String sentence) {
        InputStream tokenModel = null;
        String[] tokens = null;

        try {
            tokenModel = new FileInputStream("src/main/resources/opennlp/en-token.bin");
            TokenizerModel model = new TokenizerModel(tokenModel);

            Tokenizer tokenizer = new TokenizerME(model);
            tokens = tokenizer.tokenize(sentence);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (tokenModel != null) {
                try {
                    tokenModel.close();
                } catch (IOException e) {
                }
            }
        }

        return tokens;
    }

    public static String detokenizeTokens(String[] tokensArray) {
        //Define list of punctuation characters that should NOT have spaces before or after
        List<String> noSpaceBefore = new LinkedList<String>(Arrays.asList(",", ".", ";", ":", ")", "}", "]","'"));
        List<String> noSpaceAfter = new LinkedList<String>(Arrays.asList("(", "[","{", "\"","","'"));

        StringBuilder sentence = new StringBuilder();
        List<String> tokens = new ArrayList<>(Arrays.asList(tokensArray));

        tokens.add(0, "");  //Add an empty token at the beginning because loop checks as position-1 and "" is in noSpaceAfter
        for (int i = 1; i < tokens.size(); i++) {
            if (noSpaceBefore.contains(tokens.get(i))
                    || noSpaceAfter.contains(tokens.get(i - 1))) {
                sentence.append(tokens.get(i));
            } else {
                sentence.append(" " + tokens.get(i));
            }

            // Assumption that opening double quotes are always followed by matching closing double quotes
            // This block switches the " to the other set after each occurrence
            // ie The first double quotes should have no space after, then the 2nd double quotes should have no space before
            if ("\"".equals(tokens.get(i - 1))) {
                if (noSpaceAfter.contains("\"")) {
                    noSpaceAfter.remove("\"");
                    noSpaceBefore.add("\"");
                } else {
                    noSpaceAfter.add("\"");
                    noSpaceBefore.remove("\"");
                }
            }
        }
        return sentence.toString();
    }

    public void printTags(String sentence, Span[] tags, String[] tokens) {
        System.out.println(sentence);
        int i =0;
        for (Span tag : tags) {
            System.out.print(tag.toString() +" - "+tokens[tags[i].getStart()]);
            System.out.println();
            i ++;
        }
        System.out.println();
    }


}
