package com.eatery.utils;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nazick on 11/29/15.
 */
public class Sentence {
    private String line;
    private String[] tokens;
    private Map<Integer, WordTag> tags;
    private Map<Integer, WordTag> implicitTags;
    private String formattedText;

    public Sentence(String line) {
        this.line = line;
        this.tokenizeSentence();
    }

    public static void main(String[] args){
        Sentence sentence = new Sentence("Overall, the food is tasty enough- it wasn't great, but it wasn't bad.");
        sentence.getTokens();
    }

    public void tokenizeSentence(){
        InputStream tokenModel = null;

        try {
            tokenModel = new FileInputStream("src/hibernate/resources/opennlp/en-token.bin");
            TokenizerModel model = new TokenizerModel(tokenModel);

            Tokenizer tokenizer = new TokenizerME(model);
            this.tokens = tokenizer.tokenize(this.line);
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
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String[] getTokens() {
        return tokens;
    }

    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

    public void setTags(Map<Integer, WordTag> tags) {
        this.tags = tags;
    }

    public Map<Integer, WordTag> getTags() {

        if(this.tags == null){
            this.tags = new HashMap<>();
        }
        return tags;
    }

    public String getFormattedText() {
        return formattedText;
    }

    public void setFormattedText(String formattedText) {
        this.formattedText = formattedText;
    }

    public Map<Integer, WordTag> getImplicitTags() {
        if(this.implicitTags == null){
            this.implicitTags = new HashMap<>();
        }
        return implicitTags;
    }

    public void setImplicitTags(Map<Integer, WordTag> implicitTags) {
        this.implicitTags = implicitTags;
    }

}
