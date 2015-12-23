/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implicitaspects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Ruba
 */
public class ImplicitAspectsV1 {
    static String reviewFile;
    static String review;
    static String annFile;
    
    static Set<String> implicitFeatures = new LinkedHashSet<String>() {};
    static List<String> implicitFeatureList;
    static List<String>[] model;
    static List<String> formattedSentences = new ArrayList<>();
    
    static void createImplicitFeatureList() throws FileNotFoundException {
        Scanner annData = new Scanner(new File(annFile));
        while (annData.hasNext()) {
            String tagData[] = annData.nextLine().split("\t");
            
            if(tagData[1].contains("P_")){
            implicitFeatures.add(tagData[2].toLowerCase().replaceAll(" ", "_").replaceAll("[^a-zA-Z_]", ""));
                
            }                  
        }
        System.out.println("Current no of implicit features identified: "+implicitFeatures.size());
    }
    
    static void saveImplicitFeatureList() throws FileNotFoundException{
        PrintWriter features = new PrintWriter(new File("src/Model/implicitFeatures.txt"));
        for (String implicitFeature : implicitFeatures) {
            System.out.println(implicitFeature);
            features.write(implicitFeature+"\n");
        }
        
        features.close();
        implicitFeatureList = new ArrayList<String>(implicitFeatures);
    }
    
    static void processReviews() throws FileNotFoundException{
    
    review = new Scanner(new File(reviewFile)).useDelimiter("\\Z").next();
    review = review.toLowerCase();
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

    int fullstop = review.indexOf('.');
    while(fullstop>=0){
        String sentence = review.substring(0,fullstop).trim();
        if(sentence.length()>1)
            formattedSentences.add(sentence);
        review = review.substring(fullstop+1, review.length());
        fullstop = review.indexOf('.');
    }
        //System.out.println("No of lines : "+formattedSentences.size());
    }
    
    static void saveFormattedSentences() throws FileNotFoundException{
        PrintWriter out = new PrintWriter(new File("src/Model/formattedSentences.txt"));
        for (String formattedSentence : formattedSentences) {
            out.write(formattedSentence+"\n");
        }
        
        out.close();
    }
    
    static void createModel(){
    
        model = new ArrayList[implicitFeatures.size()];
        for (int i =0 ; i< model.length; i++) {
            model[i] = new ArrayList<String>();
        }
        
        for (String formattedSentence : formattedSentences) {
            if(formattedSentence.indexOf("P_")>0){
                String[] words = formattedSentence.split(" ");
                for (int i =0; i< words.length; i++) {
                    if(words[i].contains("P_")){
                        
                        String feature = words[i].substring(words[i].indexOf("/")+1,words[i].length());
                        //feature = feature.replaceAll("_", " ");
                        feature = feature.replaceAll("[^a-zA-Z_]","");
                        //System.out.println("feature: "+feature);
                        String tag = words[i].substring(0,words[i].indexOf("/"));
                        int position = implicitFeatureList.indexOf(feature);
                        if(position>=0)
                        model[position].add(tag +" ::: "+formattedSentence);
                    }
                }
            }    
        }
    }
    
    static void saveModel() throws FileNotFoundException{
        PrintWriter out = new PrintWriter(new File("src/Model/model.txt"));
        for (int i =0; i< implicitFeatureList.size(); i++) {
            out.write("***"+implicitFeatureList.get(i)+"_FEATURE"+"***\n");
            for(String sentence : model[i]){
            out.write(sentence+"\n");
            }
            out.write("\n");
        }
        
        out.close();
    }
    
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        annFile = "src/reviews/u_12 Ann_Sorted.txt";
        createImplicitFeatureList();
        annFile = "src/reviews/A Ann_Sorted.txt";
        createImplicitFeatureList();
        annFile = "src/reviews/u_5 Ann_Sorted.txt";
        createImplicitFeatureList();
        annFile = "src/reviews/u_13 Ann_Sorted.txt";
        createImplicitFeatureList();
        annFile = "src/reviews/u_1 Ann_Sorted.txt";
        createImplicitFeatureList();
        annFile = "src/reviews/u_2 Ann_Sorted.txt";
        createImplicitFeatureList();
        annFile = "src/reviews/u_11 Ann_Sorted.txt";
        createImplicitFeatureList();
        annFile = "src/reviews/u_3 Ann_Sorted.txt";
        createImplicitFeatureList();
        annFile = "src/reviews/u_4 Ann_Sorted.txt";
        createImplicitFeatureList();
        annFile = "src/reviews/u_9 Ann_Sorted.txt";
        createImplicitFeatureList();
        annFile = "src/reviews/u_14 Ann_Sorted.txt";
        createImplicitFeatureList();
        
        saveImplicitFeatureList();
        
        reviewFile = "src/reviews/u_12.txt";
        annFile = "src/reviews/u_12 Ann_Sorted.txt";
        processReviews();
        reviewFile = "src/reviews/u_5.txt";
        annFile = "src/reviews/u_5 Ann_Sorted.txt";
        processReviews();
        reviewFile = "src/reviews/A.txt";
        annFile = "src/reviews/A Ann_Sorted.txt";
        processReviews();
        reviewFile = "src/reviews/u_13.txt";
        annFile = "src/reviews/u_13 Ann_Sorted.txt";
        processReviews();
        reviewFile = "src/reviews/u_1.txt";
        annFile = "src/reviews/u_1 Ann_Sorted.txt";
        processReviews();
        reviewFile = "src/reviews/u_2.txt";
        annFile = "src/reviews/u_2 Ann_Sorted.txt";
        processReviews();
        reviewFile = "src/reviews/u_11.txt";
        annFile = "src/reviews/u_11 Ann_Sorted.txt";
        processReviews();
        reviewFile = "src/reviews/u_3.txt";
        annFile = "src/reviews/u_3 Ann_Sorted.txt";
        processReviews();
        reviewFile = "src/reviews/u_4.txt";
        annFile = "src/reviews/u_4 Ann_Sorted.txt";
        processReviews();
        reviewFile = "src/reviews/u_9.txt";
        annFile = "src/reviews/u_9 Ann_Sorted.txt";
        processReviews();
        reviewFile = "src/reviews/u_14.txt";
        annFile = "src/reviews/u_14 Ann_Sorted.txt";
        processReviews();
        
        saveFormattedSentences();
        
        createModel();
        saveModel();
                
    }
    
}
