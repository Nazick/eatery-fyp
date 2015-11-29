package com.eatery.engine.utils;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by nazick on 11/29/15.
 */
public class Sentence {
    private String line;
    private String[] tokens;

    public Sentence(String line) {
        this.line = line;
        this.tokenizeSentence();
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
}
