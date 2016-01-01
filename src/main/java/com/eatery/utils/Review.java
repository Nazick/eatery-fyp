package com.eatery.utils;

import java.util.ArrayList;

/**
 * Created by nazick on 12/26/15.
 */
public class Review {

    ArrayList<Sentence> sentences = new ArrayList<>();

    public ArrayList<Sentence> getSentences() {
        if(this.sentences == null){
            this.sentences = new ArrayList<>();
        }
        return sentences;
    }

    public void setSentences(ArrayList<Sentence> sentences) {
        this.sentences = sentences;
    }
}
