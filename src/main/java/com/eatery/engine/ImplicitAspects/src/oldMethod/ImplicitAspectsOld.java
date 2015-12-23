/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oldMethod;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

/**
 *
 * @author Ruba
 */
public class ImplicitAspectsOld {
    static String review, reviewFile,annFile;
    static List<String> stopWords = new ArrayList<>(Arrays.asList("a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the"));
    static List<String> entities = new ArrayList<>(Arrays.asList("Food","F_Appetizer","F_Dessert","F_Drinks","F_FoodItem","F_FI_Taste","F_FI_Price","F_FI_Quality","F_FI_Healthy","F_FI_CookingLevel","F_FI_Size","F_FI_Religious","F_Ingredients","Service","S_Menu","S_OpenHours","S_Parking","S_Staff","S_Stf_Behavior","S_Stf_Experience","S_Stf_Availability","S_Stf_Appearance","S_Gift","S_PetsAllowed","S_Accessibility","S_Wifi","S_Delivery","S_Del_Time","S_Del_OrderingMethod","S_Cutlery","S_Seating","Ambience","A_Decor","A_Furniture","A_Fur_Table","A_Fur_Door","A_Entertainment","A_Ent_Music","A_Ent_LiveShow","A_Ent_Tv","A_Environment","A_Env_Size","A_Env_Type","A_Env_AC","A_Places","A_Plc_Bathroom","A_Plc_SmokingArea","A_Plc_Buffet","A_Plc_Bar","A_Plc_Patio","A_Plc_DiningRoom","A_Plc_RestRoom","A_Plc_Kitchen","A_OutsideView","A_LocatedArea","Offers","Worthiness","W_Price","W_Waiting","Others","O_Reservation","O_Payment","O_Pay_Method","O_Pay_Price","O_Experience","O_Exp_StarsByCus","Restaurant"));
    static List<String> formattedSentences = new ArrayList<>();
    static ArrayList<String> features = new ArrayList<String>();
    static Set<String> allWords = new HashSet<String>();
    static int[][] cooccurence;
    static int[] frequencies;
    static List<String> allWordsList;
    
    public static void main(String args[]) throws FileNotFoundException{
       reviewFile = "src/400 reviews/u_1.txt";
       annFile = "src/400 reviews/u_1 Ann_Sorted.txt";
       processReviews(); 
       
       createMatrix();
       
       reviewFile = "src/testFile/review_100_A_Review.txt";
       annFile = "src/testFile/review_100_A_Review_AnnFile_Sorted.txt";
       findImplicitAspets();
    }
    
    public static void readFeatures() throws FileNotFoundException{
    Scanner in1 = new Scanner(new File("src/Model/implicitFeatures.txt")); 
    
    while (in1.hasNext()) {        
        features.add(in1.nextLine());
    }
    }
    
    public static void processReviews() throws FileNotFoundException{
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
        if(sentence.length()>1){
            if(sentence.contains("P_"))
            formattedSentences.add(sentence);
        }
        review = review.substring(fullstop+1, review.length());
        fullstop = review.indexOf('.');
    }   
    }
    
    public static void createMatrix(){
        
        //identify words
        for (String sentence : formattedSentences) {
            String[] words = sentence.split(" ");
                for (int i =0; i< words.length; i++) {
                    String word = words[i].replaceAll("[^a-zA-Z_/]","");
                    if(word.contains("P_")){
                        String feature = word.substring(word.indexOf("/")+1,word.length());
                        feature = feature.replaceAll("[^a-zA-Z_]","");
                        allWords.add(feature.toLowerCase());
                    }
                    else if (!stopWords.contains(word.toLowerCase()))
                        allWords.add(word.toLowerCase());
                }        
        }
        allWordsList = new ArrayList<String>(allWords);
        //System.out.println(allWords);
        //update matrix
        cooccurence = new int[entities.size()][allWords.size()];
        
        for (String sentence : formattedSentences) {
           String[] words = sentence.split(" ");
           
           for(String word : words) {
               word = word.replaceAll("[^a-zA-Z_/]","");
                   if(word.contains("P_")){
                   //System.out.println("came");
                   String tag = word.substring(2,word.indexOf("/"));
                   int positionX = entities.indexOf(tag);
                       
                   if(positionX>=0)
                       for (int i = 0; i < words.length; i++) {
                           String str = words[i].replaceAll("[^a-zA-Z_/]","");
                           if(str.contains("P_")){
                               String feature = word.substring(word.indexOf("/")+1,word.length());
                               int positionY = allWordsList.indexOf(feature);
                               
                               if(positionY >=0){
                                   cooccurence[positionX][positionY]++;
                               }
                           }
                           else{
                               int positionY = allWordsList.indexOf(str);
                               
                               if(positionY >=0){
                                   cooccurence[positionX][positionY]++;
                               }
                           }
                       }
                   }
                       
                }
            
        }
        
        frequencies = new int[allWordsList.size()];

        for (int i = 0; i < frequencies.length; i++) {
            for (int j = 0; j < entities.size(); j++) {
                frequencies[i] += cooccurence[j][i];
            }
        }
    }
    
    public static void findImplicitAspets() throws FileNotFoundException{
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
    
    List<String> sentences = new ArrayList<>();
    
    int fullstop = review.indexOf('.');
    while(fullstop>=0){
        String sentence = review.substring(0,fullstop).trim();
        if(sentence.length()>1)
            sentences.add(sentence);
        review = review.substring(fullstop+1, review.length());
        fullstop = review.indexOf('.');
    }
    
    
    int predicted =0;
    int available =0;
        for (String sentence : sentences) {
            double[] scores = new double[entities.size()];
            
            for (int i =0; i< scores.length; i++) {
                String wrds[] = sentence.split(" ");
                
                for (String wrd : wrds) {
                    int position;
                    if(wrd.contains("P_")){
                    String feature = wrd.substring(wrd.indexOf("/")+1,wrd.length());
                    position= allWordsList.indexOf(feature);
                    }
                    else{
                    position = allWordsList.indexOf(wrd.toLowerCase().replaceAll("[^a-zA-Z]",""));
                    }
                    
                    if(position >= 0 && frequencies[position] != 0){
                        scores[i] += (cooccurence[i][position]*1.0)/frequencies[position];
                        
                    }
                }     
            }
            
            //choose winning one
                double max= scores[0];
                int maxIndex =0;
                
                for(int p=1; p<scores.length; p++)
                    if(max<scores[p]){
                    max = scores[p];
                    maxIndex = p;
                    }
                
                if(max>0.8){
                    predicted++;
                    System.out.println(predicted+" : "+entities.get(maxIndex)+" "+max);
                    
                    if(sentence.contains("P_"+entities.get(maxIndex)))
                        available++;
                }
        }
        System.out.println("No of lines : "+sentences.size());
        System.out.println("Available tags: "+available);
    }
}
