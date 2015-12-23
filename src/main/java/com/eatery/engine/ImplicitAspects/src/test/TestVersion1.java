/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

/**
 *
 * @author Ruba
 */
public class TestVersion1 {
    static String review, reviewFile,annFile;
    static List<String> formattedSentences = new ArrayList<>();
    static List<String> stopWords = new ArrayList<>(Arrays.asList("a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the"));
    static List<String> entities = new ArrayList<>(Arrays.asList("Food","F_Appetizer","F_Dessert","F_Drinks","F_FoodItem","F_FI_Taste","F_FI_Price","F_FI_Quality","F_FI_Healthy","F_FI_CookingLevel","F_FI_Size","F_FI_Religious","F_Ingredients","Service","S_Menu","S_OpenHours","S_Parking","S_Staff","S_Stf_Behavior","S_Stf_Experience","S_Stf_Availability","S_Stf_Appearance","S_Gift","S_PetsAllowed","S_Accessibility","S_Wifi","S_Delivery","S_Del_Time","S_Del_OrderingMethod","S_Cutlery","S_Seating","Ambience","A_Decor","A_Furniture","A_Fur_Table","A_Fur_Door","A_Entertainment","A_Ent_Music","A_Ent_LiveShow","A_Ent_Tv","A_Environment","A_Env_Size","A_Env_Type","A_Env_AC","A_Places","A_Plc_Bathroom","A_Plc_SmokingArea","A_Plc_Buffet","A_Plc_Bar","A_Plc_Patio","A_Plc_DiningRoom","A_Plc_RestRoom","A_Plc_Kitchen","A_OutsideView","A_LocatedArea","Offers","Worthiness","W_Price","W_Waiting","Others","O_Reservation","O_Payment","O_Pay_Method","O_Pay_Price","O_Experience","O_Exp_StarsByCus","Restaurant"));
    static Map <String,String> parent = new HashMap <String,String>() {{
    put("F_FI_Taste", "F_FoodItem");
    put("F_FI_Price", "F_FoodItem");
    put("F_FI_Quality", "F_FoodItem");
    put("F_FI_Healthy", "F_FoodItem");
    put("F_FI_CookingLevel", "F_FoodItem");
    put("F_FI_Size", "F_FoodItem");
    put("F_FI_Religious", "F_FoodItem");
    put("S_Stf_Behavior", "S_Staff");
    put("S_Stf_Experience", "S_Staff");
    put("S_Stf_Availability", "S_Staff");
    put("S_Stf_Appearance", "S_Staff");
    put("A_Env_Size", "A_Environment");
    put("A_Env_Type", "A_Environment");
    put("P_A_Entertainment", "A_Environment");
    put("S_Del_Time", "S_Delivery");
}};
    static void processReviews() throws FileNotFoundException{
    
    review = new Scanner(new File(reviewFile)).useDelimiter("\\Z").next();
    Scanner annData = new Scanner(new File(annFile));
    int diff = 0;
    while (annData.hasNext()) {
            String tagData[] = annData.nextLine().split("\t");
            String tagAndIndex[] = tagData[1].split(" ");
            
            int startIndex = Integer.parseInt(tagAndIndex[1]);
            int endIndex = Integer.parseInt(tagAndIndex[2]);
            if(tagData[1].contains("P_")){
            review = review.substring(0,startIndex+diff) + tagAndIndex[0] +"/"+ review.substring(startIndex+diff,endIndex + diff).toLowerCase().replaceAll(" ", "_")+review.substring(endIndex+diff,review.length());
            diff+= tagAndIndex[0].length() + 1 ;
            }
            else{
            review = review.substring(0,startIndex+diff) + tagAndIndex[0] +review.substring(endIndex+diff,review.length());
            diff+= tagAndIndex[0].length() - review.substring(startIndex+diff,endIndex + diff).length();
            }
        }

    int fullstop =review.indexOf('.');
    while(fullstop>=0){
        String sentence = review.substring(0,fullstop).trim();
        if(sentence.length()>1)
            formattedSentences.add(sentence);
        review = review.substring(fullstop+1, review.length());
        fullstop = review.indexOf('.');
    }
        System.out.println("No of lines : "+formattedSentences.size());
    }
 
    public static void findImplicitAspets() throws FileNotFoundException{
    int totalAspects = 0;
    int predicted =0;
    int recall = 0;
    String aspectTag = ""; 
    String winningAspect = null;
    boolean anAspect = false;
    ArrayList<String> features = new ArrayList<String>();
    Scanner in1 = new Scanner(new File("src/Model/implicitFeatures.txt")); 
    
    String m = new Scanner(new File("src/Model/model.txt")).useDelimiter("\\Z").next();
    String m1[] = m.split("\\*\\*\\*");
    ArrayList<String> model = new ArrayList<>(Arrays.asList(m1));

    while (in1.hasNext()) {        
        features.add(in1.nextLine());
    }
    
for(String sentence : formattedSentences) {
    
    boolean correct = checkSentence(sentence);
    String sentenceN = null;
    int additionalLength = 0;
    int pos = formattedSentences.indexOf(sentence);
    while(!correct && pos >=1){
        
    if(sentenceN == null){
        sentenceN = formattedSentences.get(pos - 1);
    }
    else{
        sentenceN += " "+formattedSentences.get(pos - 1);
    }
    pos --;
    correct = checkSentence(formattedSentences.get(pos));
    }
    
    String wordsActual[] = sentence.split(" ");
    
    if(sentenceN != null){
        sentence = sentenceN+" "+sentence;
        additionalLength = sentenceN.split(" ").length;
    }
    
    String words[] = sentence.split(" ");
    
    for (int n=0; n< wordsActual.length; n++) {
        String word = wordsActual[n];
        
        winningAspect = null;
        anAspect = false;
        if(word.contains("P_")){
        totalAspects++;
        aspectTag = word.substring(0, word.indexOf("/"));
        anAspect = true;
        word = word.substring(word.indexOf("/")+1, word.length());
        }
        //else if(word.contains("_"))
        //    continue;
        
        if(features.contains(word.toLowerCase().replaceAll("[^a-zA-Z_]", ""))){
            int index = model.indexOf(word.toLowerCase().replaceAll("[^a-zA-Z_]", "")+"_FEATURE");
            if(index > 0 && model.size()>= index+2) {
                
                Set<String> candidateAspect = new HashSet<String>();
                ArrayList<String> filteredSentence = new ArrayList<String>();
                String[] sentences = model.get(index+1).split("\n");
                
                for (String sentence1 : sentences) {
                    if(sentence1.indexOf(":::")>0)
                        candidateAspect.add(sentence1.substring(0,sentence1.indexOf(":::")-1));
                        filteredSentence.add(sentence1);
                }
            if(candidateAspect.size() >= 1){
                
                List<String> candidateAspectN = new ArrayList<String>(candidateAspect);
                double[] score = new double[candidateAspect.size()];
                
                //only 1 candidate
                if(candidateAspect.size() == 1){
                winningAspect= candidateAspectN.get(0);
               
                }
                else{
                for(int l=0; l<words.length; l++){
                    words[l] = words[l].replaceAll("[^a-zA-Z_/]", "");
                    String wordN = words[l];
                    int enitityFactor=1;//parameter 1
                    if(wordN.length() == 0)
                        continue;
                    else if(stopWords.contains(wordN.toLowerCase()))//remove stop words
                        continue;
                    if(entities.contains(wordN))
                        enitityFactor=2;
                    
                    double[] availability = new double[score.length];
                    
                    for(int i=0; i<filteredSentence.size(); i++){
                    
                    String temp = filteredSentence.get(i);
                    
                    if(temp.indexOf(":::")<0)
                        continue;
                    
                    String aspect = temp.substring(0,temp.indexOf(":::")-1);
                    temp = temp.substring(temp.indexOf(":::")+1, temp.length());
                    int position = candidateAspectN.indexOf(aspect);
                        
                    while(temp.indexOf(wordN)>=0){
                        //System.out.println(candidateAspectN.size()+" "+position+" "+enitityFactor);
                        if(l != n+additionalLength)
                        availability[position]+= enitityFactor*(1/Math.abs(l - n - additionalLength));
                        else
                        availability[position]+= enitityFactor;
                        
                    temp = temp.replace(wordN, "");
                    }
                    
                }
                    double sum = 0;
                    
                    for (double val : availability) {
                        sum+= val;
                    }
                    
                    if(sum > 0)
                        for (int x = 0; x < score.length; x++) {
                            score[x] += availability[x]*1.0/sum;
                        }
                }
                double max= score[0];
                int maxIndex =0;
                
                for(int p=1; p<score.length; p++)
                    if(max<score[p]){
                    max = score[p];
                    maxIndex = p;
                    }
                //parameter 2
                if(max < 0.9){//control minimum threshhol score
                    continue;
            }
                winningAspect = candidateAspectN.get(maxIndex);
                }
                
                /*boolean success = checkParent(words,n+additionalLength,winningAspect);
                    if(!success){
                        //if(anAspect)
                            //System.out.println(word+" "+anAspect+" "+winningAspect);
                       continue;
                    }*/
                    
                predicted++;
                /*if(anAspect && winningAspect != null && winningAspect.equals(aspectTag))
                System.out.println(predicted +" "+word+" "+winningAspect+" - correct");
                else
                System.out.println(predicted +" "+word+" "+winningAspect);*/
                
                //missed to tag manually
                if(!anAspect)
                   System.out.println(word+" "+anAspect+" "+winningAspect);
            }
            
            //check correctness
            if(anAspect && winningAspect != null && winningAspect.equals(aspectTag))
                recall++;
            }
         
        }
        //else if(anAspect){
        //    System.out.println(aspectTag+" : "+word.toLowerCase().replaceAll("[^a-zA-Z_]", "") + " is not available in model");
        //}
    
    }
    
    }
        
        System.out.println("Total tags: "+totalAspects);
        System.out.println("Predicted tags: "+predicted);
        System.out.println("Correct: "+recall);
    }
    
    public static boolean checkParent(String[] words,int n,String winningAspect){
        int windowSizeR = 5; //parameter 3
        int windowSizeL = 5;
        
        String childTag = winningAspect.substring(2,winningAspect.length());
        if(!parent.containsKey(childTag))
            return true;
        
        String parentTag = parent.get(childTag);
        String grandParent = null;
        String grandGrandParent = null;
        
        if(parentTag.equals("F_FoodItem"))
           grandParent = "Food";
        if(parentTag.equals("A_Environment")){
            grandGrandParent = "Restaurant";
            grandParent = "Ambience";
        }
        
        int i=n+1,j=n-1;
        while(i<=n+windowSizeR && i<words.length){
            //if(stopWords.contains(words[i]))
            //    windowSizeR++;
            String word = words[i].replaceAll("[^a-zA-Z_/]", " ");
            
            if(word.contains(parentTag) || (grandParent != null && word.contains(grandParent)) || (grandGrandParent != null && word.contains(grandGrandParent)))
                return true;
            else if (word.contains("P_")){
                String tag = word.substring(0,words[i].indexOf("/"));
                if(parent.get(tag) != null && parent.get(tag).equals(parentTag)){
                    int k = i+1;
                    while(k<=i+windowSizeR && k<words.length){
                        if(word.contains(parentTag) || (grandParent != null && word.contains(grandParent)) || (grandGrandParent != null && word.contains(grandGrandParent)))
                        return true;
                        
                        k++;
                    }
            }
            
            }
                
        i++;
        }
        
        while(j>=n-windowSizeL && j>=0){
            //if(stopWords.contains(words[j]))
            //    windowSizeL++;
            String word = words[j].replaceAll("[^a-zA-Z_/]", " ");
            //System.out.println(word);
            if(word.contains(parentTag) || (grandParent != null && word.contains(grandParent)) || (grandGrandParent != null && word.contains(grandGrandParent)))
                return true;
            else if (word.contains(winningAspect)){
            int k = j+1;
                    while(k>= j-windowSizeL && k>=0){
                        if(word.contains(parentTag) || (grandParent != null && word.contains(grandParent)) || (grandGrandParent != null && word.contains(grandGrandParent)))
                        return true;
                        k--;
                    }
            }
        j--;
        }
        return false;
    }
    
    public static void main(String args[]) throws FileNotFoundException{
    reviewFile = "src/reviews/u_14.txt";
    annFile = "src/reviews/u_14 Ann_Sorted.txt";
    processReviews();
    
    findImplicitAspets();
    }

    public static boolean checkSentence(String sentence) {
        
        String words[] = sentence.split(" ");
        
        for (String word : words) {
            if(entities.contains(word))
                return true;
        }
        
        return false;
    }
}
