package com.eatery.engine.implicit;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.tregex.ParseException;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.util.CoreMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ruba
 */
public class FindImplicitAspects {
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
    static StanfordCoreNLP pipeline;
    static TregexPattern npPattern;
    static TreebankLanguagePack tlp;
    static GrammaticalStructureFactory gsf;
    static Map<String, String> results = new HashMap<String,String>();

    static void processReviews() throws FileNotFoundException{

        //CHANGE: remove entire while loop and change the code such that it eplace words with tags
        review = new Scanner(new File(reviewFile)).useDelimiter("\\Z").next();
        Scanner annData = new Scanner(new File(annFile));
        int diff = 0;
        while (annData.hasNext()) {
            String tagData[] = annData.nextLine().split("\t");
            String tagAndIndex[] = tagData[1].split(" ");

            int startIndex = Integer.parseInt(tagAndIndex[1]);
            int endIndex = Integer.parseInt(tagAndIndex[2]);
            if(tagData[1].contains("P_")){//this is not need. I just added for tesing purpose
            }
            else{//here is where I replace words with tags
                review = review.substring(0,startIndex+diff) + tagAndIndex[0] +review.substring(endIndex+diff,review.length());
                diff+= tagAndIndex[0].length() - review.substring(startIndex+diff,endIndex + diff).length();
            }
        }

        //loop breaks reviews into sentences
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

    public static Map<String, String> findImplicitAspets() throws FileNotFoundException{

        String winningAspect = null;
        ArrayList<String> features = new ArrayList<String>();

        //CHANGE: read this list of implicit features & model ealier in the project. Do not read again & again
        Scanner in1 = new Scanner(new File("src/Model/implicitFeatures.txt"));

        String m = new Scanner(new File("src/Model/model.txt")).useDelimiter("\\Z").next();
        String m1[] = m.split("\\*\\*\\*");
        ArrayList<String> model = new ArrayList<>(Arrays.asList(m1));
        while (in1.hasNext()) {
            features.add(in1.nextLine());
        }


        for(String sentence : formattedSentences) {
            boolean correct = true;//checkSentence(sentence);
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
                winningAspect = null;

                String word = wordsActual[n];

                if(n<wordsActual.length-1){
                    String wordTemp = word+" "+wordsActual[n+1];
                    if(features.contains(wordTemp.toLowerCase().replaceAll("[^a-zA-Z_]", ""))){
                        word = wordTemp;
                        n++;
                    }
                }

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


                            if(candidateAspect.size() == 1){
                                winningAspect= candidateAspectN.get(0);
                            }
                            else{
                                for(int l=0; l<words.length; l++){
                                    words[l] = words[l].replaceAll("[^a-zA-Z_/]", "");
                                    String wordN = words[l
                                            ];
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

                            boolean success = checkParent(words,n+additionalLength,winningAspect);
                            if(!success){
                                continue;
                            }


                            //CHANGE: here is the output
                            results.put(word,winningAspect);
                            System.out.println(word +" "+winningAspect);

                        }
                    }

                }
            }

        }

        return results;
    }


    public static boolean checkParent(String[] words,int n,String winningAspect){
        String childTag = winningAspect.substring(2,winningAspect.length());
        if(!parent.containsKey(childTag))
            return true;


        String str="";
        for (String word : words) {
            if(word.contains("P_")){
                word = word.substring(word.indexOf("/")+1,word.length());

            }
            str+=" "+word;
        }

        Annotation document = new Annotation(str);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {

            Tree sentenceTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);

            // Output dependency tree
            GrammaticalStructure gs = gsf.newGrammaticalStructure(sentenceTree);
            Collection<TypedDependency> tdl = gs.typedDependenciesCollapsed();

            if(checkRule(tdl,words,n,childTag))
                return true;
        }
        return false;
    }

    public static boolean checkRule(Collection<TypedDependency> tdl,String[] words,int n,String childTag){
        String parentTag = parent.get(childTag);
        String grandParent = null;
        String grandGrandParent = null;

        if(parentTag.equals("F_FoodItem"))
            grandParent = "Food";
        if(parentTag.equals("A_Environment")){
            grandGrandParent = "Restaurant";
            grandParent = "Ambience";
        }

        String opinion = words[n];
        if(opinion.contains("P_"))
            opinion = opinion.substring(opinion.indexOf("/")+1,opinion.length());

        opinion.replaceAll("[^a-zA-Z]", "");

        for(TypedDependency td: tdl){
            String str1 = td.gov().value().replaceAll("[^a-zA-Z_]", "");
            String str2 = td.dep().value().replaceAll("[^a-zA-Z_]", "");

            if(opinion.contains(str1)){
                //System.out.println(td +" 1 "+parentTag);
                if( td.reln().getShortName().equals("nsubj") || td.reln().getShortName().equals("dep") || td.reln().getShortName().equals("nn") || td.reln().getShortName().equals("nsubjpass") || td.reln().getShortName().equals("conj_and") || td.reln().getShortName().equals("dobj"))
                    if(str2.equals(parentTag) || (grandParent!= null && str2.equals(grandParent)) || (grandGrandParent!= null && str2.equals(grandGrandParent)))
                    {

                        return true;
                    }
                    else{
                        String subj = str2;

                        for(TypedDependency tdTemp: tdl){
                            str1 = tdTemp.gov().value().replaceAll("[^a-zA-Z_]", "");
                            str2 = tdTemp.dep().value().replaceAll("[^a-zA-Z_]", "");
                            if((tdTemp.reln().getShortName().equals("nsubj") || td.reln().getShortName().equals("nsubjpass")) && (str2.equals(parentTag) || (grandParent!= null && str2.equals(grandParent)) || (grandGrandParent!= null && str2.equals(grandGrandParent))))
                                if (str1.equals(subj))
                                    return true;
                        }

                    }
            }
            else if(opinion.contains(str2)){
                //System.out.println(td +" 2 "+parentTag);
                if(td.reln().getShortName().equals("amod") || td.reln().getShortName().equals("nn") || td.reln().getShortName().equals("dep") || td.reln().getShortName().equals("rcmod") || td.reln().getShortName().equals("amod") || td.reln().getShortName().equals("advmod") || td.reln().getShortName().equals("nn") || td.reln().getShortName().equals("nsubjpass") || td.reln().getShortName().equals("conj_and"))
                    if(str1.equals(parentTag) || (grandParent!= null && str1.equals(grandParent)) || (grandGrandParent!= null && str1.equals(grandGrandParent)))
                    {
                        return true;
                    }
                String sub = str1;
                for(TypedDependency tdTemp: tdl){
                    str1 = tdTemp.gov().value().replaceAll("[^a-zA-Z_]", "");
                    str2 = tdTemp.dep().value().replaceAll("[^a-zA-Z_]", "");
                    if(tdTemp.reln().getShortName().equals("nsubj") || td.reln().getShortName().equals("nsubjpass")){
                        if (str1.equals(sub) && (str2.equals(parentTag) || (grandParent!= null && str2.equals(grandParent))))
                            return true;
                    }
                }
            }
            else if(entities.contains(str1) && (str1.equals(parentTag) || (grandParent!= null && str1.equals(grandParent)) || (grandGrandParent!= null && str1.equals(grandGrandParent)))){
                //System.out.println(td+" 3");
                if(td.reln().getShortName().equals("amod") || td.reln().getShortName().equals("rcmod") || td.reln().getShortName().equals("prep") ){
                    String subj = str2;

                    for(TypedDependency tdTemp: tdl){
                        if(tdTemp.reln().getShortName().equals("conj_and") || tdTemp.reln().getShortName().equals("conj_or") || td.reln().getShortName().equals("pcomp") ){
                            str1 = tdTemp.gov().value().replaceAll("[^a-zA-Z_]", "");
                            str2 = tdTemp.dep().value().replaceAll("[^a-zA-Z_]", "");

                            if(opinion.contains(str1) && str2.equals(subj))
                                return true;
                            else if(opinion.contains(str2) && str1.equals(subj))
                                return true;
                        }
                    }
                }
            }
            else if(entities.contains(str2) && (str2.equals(parentTag) || (grandParent!= null && str2.equals(grandParent)) || (grandGrandParent!= null && str2.equals(grandGrandParent)))){
                //System.out.println(td+" 4");
                if(td.reln().getShortName().equals("nsubj") || td.reln().getShortName().equals("nsubjpass") || td.reln().getShortName().equals("prep")){
                    String subj = str1;

                    for(TypedDependency tdTemp: tdl){
                        if(tdTemp.reln().getShortName().equals("conj_and") || tdTemp.reln().getShortName().equals("conj_or") || tdTemp.reln().getShortName().equals("acomp") || tdTemp.reln().getShortName().equals("pcomp") || tdTemp.reln().getShortName().equals("amod")){

                            str1 = tdTemp.gov().value().replaceAll("[^a-zA-Z_]", "");
                            str2 = tdTemp.dep().value().replaceAll("[^a-zA-Z_]", "");

                            if(opinion.contains(str1) && str2.equals(subj))
                                return true;
                            else if(opinion.contains(str2) && str1.equals(subj))
                                return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    public static void main(String args[]) throws FileNotFoundException{

        //CHANGE: should be loaded initially
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        pipeline = new StanfordCoreNLP(props);
        npPattern = null;
        npPattern = TregexPattern.compile("@NP");
        tlp = new PennTreebankLanguagePack();
        gsf = tlp.grammaticalStructureFactory();

        //CHANGE: this is the input. You can remove this and update in the place where it reads the file
        reviewFile = "src/reviews/u_14.txt";
        annFile = "src/reviews/u_14 Ann_Sorted.txt";

        //CHANGE: call these two methods
        processReviews();
        Map results = findImplicitAspets();//change the results format if you want
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