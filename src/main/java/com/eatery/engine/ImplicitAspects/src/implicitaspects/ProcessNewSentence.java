/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implicitaspects;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 *
 * @author Ruba
 */
public class ProcessNewSentence {

public static void main(String args[]) throws FileNotFoundException{

ArrayList<String> features = new ArrayList<String>();
String sentence = "Pizza is very small and restaurant is good";
System.out.println("Sentence is : "+sentence+"\n");
Scanner in1 = new Scanner(new File("src/Model/implicitFeatures.txt")); 

String m = new Scanner(new File("src/Model/model.txt")).useDelimiter("\\Z").next();
String m1[] = m.split("\\*\\*\\*");
ArrayList<String> model = new ArrayList<>(Arrays.asList(m1));

    while (in1.hasNext()) {        
        features.add(in1.nextLine());
    }

    String words[] = sentence.split(" ");

    for (String word : words) {
        if(features.contains(word.toLowerCase().replaceAll("[^a-zA-Z ] ", ""))){
            int index = model.indexOf(word.toLowerCase()+"_FEATURE");
            if(index > 0 && model.size()>= index+2) {
                System.out.println(word+":");
                Set<String> candidateAspect = new HashSet<String>();
                ArrayList<String> filteredSentence = new ArrayList<String>();
                String[] sentences = model.get(index+1).split("\n");
                
                for (String sentence1 : sentences) {
                    if(sentence1.indexOf(":::")>0)
                        candidateAspect.add(sentence1.substring(0,sentence1.indexOf(":::")-1));
                        filteredSentence.add(sentence1);
                }
                System.out.println("There are "+candidateAspect.size()+" candidate entities");

                System.out.println("");
            if(candidateAspect.size() == 1)
                    System.out.println(word +" : "+ candidateAspect.toArray()[0]);
            if(candidateAspect.size() > 1){
                List<String> candidateAspectN = new ArrayList<String>(candidateAspect);
                double[] score = new double[candidateAspect.size()];
                
                
                for(String wordN : words){
                    for(int i=0; i<filteredSentence.size(); i++){
                    
                    String temp = filteredSentence.get(i);
                    int[] availability = new int[score.length];
                    
                    if(temp.indexOf(":::")<0)
                        continue;
                    
                    String aspect = temp.substring(0,temp.indexOf(":::")-1);
                    temp = temp.substring(temp.indexOf(":::")+1, temp.length());
                    int position = candidateAspectN.indexOf(aspect);
                        
                    while(temp.indexOf(word)>=0){
                        availability[position]++;
                        temp = temp.replace(word, "");
                    }
                    int sum = IntStream.of(availability).sum();
                    if(sum > 0)
                    {
                    score[position] += availability[position]*1.0/sum;
                    }
                }
                }
                
                for (int k =0; k<candidateAspectN.size(); k++) {
                    System.out.println(candidateAspectN.get(k)+" : "+score[k]);
                }

            }
            }
        }
    }
}
}
