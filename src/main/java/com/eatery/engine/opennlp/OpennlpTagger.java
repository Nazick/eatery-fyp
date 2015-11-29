package com.eatery.engine.opennlp;

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


/**
 * Created by nazick on 7/24/15.
 */
public class OpennlpTagger {

    public static void main(String args[]) {
        String review = "I love Mexican food and I always look for new places. Although I prefer the more formal restaurants, some of the faster-service places are pretty good.  Someone recommended Fausto's to me so I gave it a try.  Let me tell you, you get a lot of food for your money!  There's enough for two people in those entrees. And there are many entrees to choose from.  Service can be a tad slow, and the place was hot inside the day I was there.  \\n\\nThe restaurant is kind of small but it's big enough for a large group to get together when several tables are pulled together (as was happening the day I was there). There's not a lot of room by the counter to wait for your order if you are doing takeout, though. Overall, the food is tasty enough- it wasn't great, but it wasn't bad.";
        Span[] taggedTokens = null;
        OpennlpTagger opennlpTagger = new OpennlpTagger();
        String[] sentences = opennlpTagger.detectSentence(review);
        if (sentences != null) {
            for (String sentence : sentences) {
                String[] tokens = opennlpTagger.tokenizeSentence(sentence);
                if (tokens != null) {
                    taggedTokens = opennlpTagger.tagger(tokens);
                    if (taggedTokens != null) {
                        opennlpTagger.printTags(sentence, taggedTokens, tokens);
                    }
                }
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
    public Span[] tag(String review){

        Span[] taggedTokens = null;
        OpennlpTagger opennlpTagger = new OpennlpTagger();
        String[] sentences = opennlpTagger.detectSentence(review);
        if (sentences != null) {
            for (String sentence : sentences) {
                String[] tokens = opennlpTagger.tokenizeSentence(sentence);
                if (tokens != null) {
                    taggedTokens = opennlpTagger.tagger(tokens);
                    if (taggedTokens != null) {
                        opennlpTagger.printTags(sentence, taggedTokens, tokens);
                    }
                }
            }
        }
        return taggedTokens;
    }

    public Span[] tagger(String[] tokens) {
        InputStream eateryModel = null;
        Span[] taggedTokens = null;
        try {
            eateryModel = new FileInputStream("src/main/resources/opennlp/eval/Test4.bin");
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

    public String[] detectSentence(String text) {
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
