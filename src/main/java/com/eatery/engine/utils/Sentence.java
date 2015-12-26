package com.eatery.engine.utils;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nazick on 11/29/15.
 */
public class Sentence {
    private String line;
    private String[] tokens;
    private List<WordTag> tags;
    private List<WordTag> implicitTags;
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
            tokenModel = new FileInputStream("src/main/resources/opennlp/en-token.bin");
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

    public void setTags(ArrayList<WordTag> tags) {
        this.tags = tags;
    }

    public List<WordTag> getTags() {

        if(this.tags == null){
            this.tags = new ArrayList<>();
        }
        return tags;
    }

    public String getFormattedText() {
        return formattedText;
    }

    public void setFormattedText(String formattedText) {
        this.formattedText = formattedText;
    }

    public List<WordTag> getImplicitTags() {
        if(this.implicitTags == null){
            this.implicitTags = new ArrayList<>();
        }
        return implicitTags;
    }

    public void setImplicitTags(List<WordTag> implicitTags) {
        this.implicitTags = implicitTags;
    }

}
