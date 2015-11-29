package com.eatery.engine.system;

import java.io.*;
import com.eatery.engine.opennlp.OpennlpTagger;
import com.eatery.engine.preprocessing.SpellCorrector;
import com.sun.xml.internal.bind.v2.TODO;
import opennlp.tools.tokenize.DictionaryDetokenizer;

/**
 * Created by nazick on 11/29/15.
 */
public class EateryMain {

    public static String TEMPFILEPATH = "src/main/resources/tempFile.txt";

    public static void main(String[] args){
        EateryMain eateryMain = new EateryMain();
        SpellCorrector spellCorrector = new SpellCorrector();

        BufferedReader bufferedReader = eateryMain.readFile("src/main/resources/TestReviews.txt");

        try {
            String strLine = null;

            //todo : need to change once other components integrated
            Writer output = null;
            File file = new File(TEMPFILEPATH);
            output = new BufferedWriter(new FileWriter(file,false));

            while ((strLine = bufferedReader.readLine()) != null) {

                String[] tokens = OpennlpTagger.tokenizeSentence(strLine);
                String[] newTokens = new String[tokens.length];
                for(int i = 0; i<tokens.length;i++){
                    tokens[i] = tokens[i].replaceAll("[-+^:,\"*#!?()]","");
                    newTokens[i] = spellCorrector.correct(tokens[i]);
                    System.out.println("..");
                }
                strLine = eateryMain.detokenize(newTokens);
                output.write(strLine);
            }
            output.close();
            System.out.println("File written");

        }catch(Exception e){
            e.getStackTrace();
            //System.out.println(e.toString());
        }
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

}
