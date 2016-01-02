package com.eatery.preprocessing;

import org.gauner.jSpellCorrect.ToySpellingCorrector;

/**
 * Created by nazick on 11/28/15.
 */
public class SpellCorrector {
    ToySpellingCorrector sc = new ToySpellingCorrector();

    public SpellCorrector() {
        sc.trainFile("src/main/resources/preprocessing/newDictWords.txt");
    }

    public String correct(String word){
        return sc.correct(word);
    }

    public static void main(String args[]){

        ToySpellingCorrector sc = new ToySpellingCorrector();
        sc.trainFile("src/main/resources/preprocessing/newDictWords.txt");
        System.out.println(sc.correct("overdone"));

    }
}
