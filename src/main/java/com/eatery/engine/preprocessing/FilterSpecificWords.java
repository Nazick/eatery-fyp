package com.eatery.engine.preprocessing;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import com.eatery.engine.opennlp.OpennlpTagger;

/**
 * Created by nazick on 11/28/15.
 */
public class FilterSpecificWords {
    public static void main(String args[]) {
        try {
            ArrayList storeWordList = new ArrayList();
            FileInputStream fstream = new FileInputStream("src/main/resources/preprocessing/dictWords.txt");
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                storeWordList.add(strLine);
            }
            //Close the input stream
            in.close();

            HashSet<String> uniqueWords = new HashSet<>();
            for (Iterator iter = storeWordList.iterator(); iter.hasNext();) {
                String temp = ((String) iter.next()).toLowerCase();
                String[] uni = OpennlpTagger.tokenizeSentence(temp);
                for(String word:uni) {
                    word = word.replaceAll("[-+.^:,\"*#!?]","");
                    if (!uniqueWords.contains(word)) {
                        uniqueWords.add(word);
                        System.out.println(word);
                    }
                }
            }
            Writer output = null;
            File file = new File("src/main/resources/preprocessing/newDictWords.txt");
            output = new BufferedWriter(new FileWriter(file));

            for (String word: uniqueWords) {
                output.write(word);
                output.write(" ");
            }
            output.close();
            System.out.println("Your file has been written");

        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
}
