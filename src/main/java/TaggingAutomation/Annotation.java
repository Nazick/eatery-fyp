package TaggingAutomation;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bruntha on 9/10/15.
 */
public class Annotation {
//    final String filePathDictionary = "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/" +
//            "dictionary.txt";

    final String filePathReview = "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/" +
            "u_12.txt";  //the file path that need to be annotated

    final String filePathAnnDestination = "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/" +
            "u_12_A.ann";  //annotation of the file that need to be annotated

    final String filePathAnnSource = "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/" +
            "A.ann";  //manually tagged ann file

    final String filePathDictionaryAuto = "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/" +
            "dictionaryAuto.txt";   //the file having related aspect and word

    final String filePathNonDictionaryAuto = "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/" +
            "nonDictionaryAuto.txt";    //the file having non-related aspect and word

    final String filePathExternalFile = "/home/bruntha/Documents/Softwares/brat-v1.3_Crunchy_Frog/data/Eatery/" +
            "nonDictionaryAuto.txt";    //the files like foodlist

    Hashtable<String, String> dictionaryTerms = new Hashtable<>(); //<key=word,value=aspect>
    ArrayList<String> nonDictionaryTerms = new ArrayList<>();
    int characterCount = 0;
    int characterCountTemp=0;
    StanfordLemmatizer stanfordLemmatizer = new StanfordLemmatizer();
    int tagCount = 0;
    int test = 0;
    Hashtable<String, String> taggedItems = new Hashtable<>();
    private int noOfTagsAlready;
    private int noOfTagsAlreadyMax;


    public static void main(String args[]) {
        Annotation annotation = new Annotation();
        annotation.automate();
    }

    public void automate() {
        try {
            dictionary(false);
            loadOldTags();
            tagCount = noOfTagsAlreadyMax;
            readReviews();
//            readReviews2();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done..");
    }

    public void automateFoodVertical() {
        try {
            loadOldTags();
            tagCount = noOfTagsAlreadyMax;
            readReviewsFoodVertical();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done..");
    }

    //related works to words and aspect pairs
    private void dictionary(boolean updateDictionary) {
        try {
            loadDictionary(filePathDictionaryAuto);
            loadNonDictionaryTerms(filePathNonDictionaryAuto);
            if (updateDictionary) {
                upgradeDictionary();
                populateDictionary();
                writeToDictionary();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // load the word and aspect for future processing
    private void loadDictionary(String pathDictionary) throws IOException {
        File fileAnnotation = new File(pathDictionary);
        if (!fileAnnotation.exists()) {
            fileAnnotation.createNewFile();
        }
        FileReader fr;
        fr = new FileReader(fileAnnotation);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            String[] dic = line.split("[ \t]");
            String word = line.substring(line.indexOf(" ") + 1);
            dictionaryTerms.put(word.toLowerCase(), dic[0]);
            System.out.println("Loading Dictionary Terms: Word: " + word + "\tTag: " + dic[0]);
        }
        br.close();
        fr.close();
        System.out.println("Dictionary Terms Loaded");
    }

    // load non related word for future processing
    private void loadNonDictionaryTerms(String filePathNonDictionaryAuto) throws IOException {
        File fileAnnotation = new File(filePathNonDictionaryAuto);
        if (!fileAnnotation.exists()) {
            fileAnnotation.createNewFile();
        }
        FileReader fr;
        fr = new FileReader(fileAnnotation);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {

            nonDictionaryTerms.add(line.trim().toLowerCase());
            System.out.println("Loading Non Dictionary Terms: " + line);
        }
        br.close();
        fr.close();
        System.out.println("Non Dictionary Terms Loaded");
    }

    // update the dictionary in memory( word and aspect pair) with newly identified pair of word and aspect
    private void upgradeDictionary() throws IOException {
        File fileAnnotation = new File(filePathAnnSource);
        FileReader fr = new FileReader(fileAnnotation);
        BufferedReader br = new BufferedReader(fr);
        String line;

        if (fileAnnotation.exists()) {
            while ((line = br.readLine()) != null) {
                String[] annotations = line.split("[ \t]");
                String word = line.substring(line.lastIndexOf("\t") + 1);

                if (!nonDictionaryTerms.contains(word.toLowerCase())) {
                    if (dictionaryTerms.containsKey(word.toLowerCase())) {
                        if (!dictionaryTerms.get(word.toLowerCase()).equalsIgnoreCase(annotations[1])) {
                            System.out.println("Conflict: word:: " + word + " Dictionary Tag: " + dictionaryTerms.get(word.toLowerCase()) + " New Tag: " + annotations[1]);
                            nonDictionaryTerms.add(word.toLowerCase());
//                        removeLineFile(dictionaryTerms.get(word.toLowerCase())+" "+word.toLowerCase(),filePathDictionaryAuto);
                            dictionaryTerms.remove(word.toLowerCase());
                            writePrintStream(word, filePathNonDictionaryAuto);
                        }
                    } else {
                        System.out.println("Updating Dictionary:: Word: " + word + "\tTag: " + annotations[1]);
                        dictionaryTerms.put(word.toLowerCase(), annotations[1]);
                        writePrintStream(annotations[1] + " " + word.toLowerCase(), filePathDictionaryAuto);
                    }
                }

//            if (dictionaryTerms.containsKey(word.toLowerCase())) {
//                if (!dictionaryTerms.get(word.toLowerCase()).equalsIgnoreCase(annotations[1])) {
//                    System.out.println("Conflict: word: " + word + " Dictionary Tag: " + dictionaryTerms.get(word.toLowerCase()) + " New Tag: " + annotations[1]);
//                    nonDictionaryTerms.add(word.toLowerCase());
//
//                }
//            } else {
//                dictionary.add(word.toLowerCase());
//                dictionaryTerms.put(word.toLowerCase(), annotations[1]);
//                System.out.println("Updating Dictionary: Word: " + word + "\tTag: " + annotations[1]);
//            }
            }
        }


        br.close();
        fr.close();
    }

    // populate the words using lemmatizer and synonyms
    private void populateDictionary() throws IOException {
        populateDictionaryByLemmatizer();
//        populateDictionaryBySynonyms();
    }

    // update the dictionary in file( word and aspect pair) with newly identified pair of word and aspect
    private void writeToDictionary() throws IOException {

        File file = new File(filePathDictionaryAuto);

        if (file.delete()) {
            file.createNewFile();
            Enumeration e = dictionaryTerms.keys();
            while (e.hasMoreElements()) {
                String word = (String) e.nextElement();
                if (word.length() != 1) {
                    writePrintStream(dictionaryTerms.get(word) + " " + word, filePathDictionaryAuto);
                }
            }
        } else {
            System.out.println("Error in dictionary file");
        }
        System.out.println("Dictionary updated");

    }

//    public void loadDictionaryManual() throws IOException {
//        File fileAnnotation = new File(filePathDictionary);
//        FileReader fr = new FileReader(fileAnnotation);
//        BufferedReader br = new BufferedReader(fr);
//        String line;
//
//        while ((line = br.readLine()) != null) {
//            String[] dic = line.split("[ \t]");
//            String word = line.substring(line.indexOf(" ") + 1);
//            dictionaryTerms.put(word.toLowerCase(), dic[0]);
//            System.out.println("Loading dictionary : Word: " + word + "\tAspect: " + dic[0]);
//        }
//        br.close();
//        fr.close();
//        System.out.println("Dictionary Loaded");
//    }

    // this method reads reviews one by one and pass reviews for automation
    public void readReviews() throws IOException {
        File fileAnnotation = new File(filePathReview);
        FileReader fr = new FileReader(fileAnnotation);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
//            System.out.println(line);
            characterCountTemp=characterCount;
            analyzeReview(line);
            characterCount=characterCountTemp;
            analyzeReviewStemming(line);
        }
        br.close();
        fr.close();
    }
    public void readReviews2() throws IOException {
        File fileAnnotation = new File(filePathReview);
        FileReader fr = new FileReader(fileAnnotation);
        BufferedReader br = new BufferedReader(fr);
        String line;
//        characterCount=0;
        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//            characterCountTemp=characterCount;
//            analyzeReview(line);
//            characterCount=characterCountTemp;
            analyzeReviewStemming(line);
        }
        br.close();
        fr.close();
    }

    //need to customize this according to the input file and pass aspect, word pair to annotate method
    public void readReviewsFoodVertical() throws IOException {
        File fileAnnotation = new File(filePathExternalFile);
        FileReader fr = new FileReader(fileAnnotation);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            annotate("aspect","item");
        }
        br.close();
        fr.close();
    }

//    public void analyzeReview(String review) {
//        String[] reviewWords = review.split(" ");
//
//        for (int i = 0; i < reviewWords.length; i++) {
//            if (dictionaryTerms.containsKey(reviewWords[i])) {
//                System.out.println(reviewWords[i] + " " + dictionaryTerms.get(reviewWords[i]) + " " + (characterCount + characterCount) + " " + (characterCount + characterCount + reviewWords[i].length()));
//            }
//            characterCount += reviewWords[i].length();
//            characterCount++;
//        }
//    }

    //this method works in a way that it tokenize the review and lemmatize each token and search in dictionary for a match
    public void analyzeReviewStemming(String review) {
        String[] reviewWords = new String[0];
        try {
            reviewWords = tokenize(review);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int localCount = -1;
        for (int i = 0; i < reviewWords.length; i++) {
            List<String> words = stanfordLemmatizer.lemmatize(reviewWords[i]);

            for (int j = 0; j < words.size(); j++) {
                if (dictionaryTerms.containsKey(words.get(j)) && !(reviewWords[i].contains(" ") ||reviewWords[i].contains("+")||reviewWords[i].contains("\"") ||reviewWords[i].contains("'")||reviewWords[i].contains(".") ||reviewWords[i].contains(":") || reviewWords[i].contains("(") || reviewWords[i].contains(")") ||reviewWords[i].contains("-") || reviewWords[i].contains(";"))) {
                    localCount = review.indexOf(reviewWords[i], localCount + 1);
                    if (!taggedItems.containsKey((characterCount + localCount) + "-" + (characterCount + localCount + reviewWords[i].length()))) {
                        String newAnnotation = "T" + ++tagCount + "\t" + dictionaryTerms.get(words.get(j)) + " " + (localCount + characterCount) + " " + (localCount + characterCount + reviewWords[i].length()) + "\t" + reviewWords[i];

                        try {
                            taggedItems.put((localCount + characterCount) + "-" + (localCount + characterCount + reviewWords[i].length()), dictionaryTerms.get(words.get(j)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                        }


                        System.out.println("ARS\t"+newAnnotation);
                        writePrintStream(newAnnotation, filePathAnnDestination);
//                        System.out.println(reviewWords[i] + " " + dictionaryTerms.get(words.get(j)) + " " + (localCount + characterCount) + " " + (localCount + characterCount + reviewWords[i].length()));
                    }
                }

            }
        }
        characterCount += review.length();
        characterCount++;
    }

    public void analyzeReviewSynonyms(String review) {
        String[] reviewWords = new String[0];
        try {
            reviewWords = tokenize(review);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int localCount = 0;
        for (int i = 0; i < reviewWords.length; i++) {
            ArrayList<String> words = PyDictionary.findSynonyms(reviewWords[i]);

            for (int j = 0; j < words.size(); j++) {
//                System.out.println(reviewWords[i]+" "+words.get(j));
                if (dictionaryTerms.containsKey(words.get(j))) {
                    localCount = review.indexOf(reviewWords[i], localCount + 1);
                    if (!taggedItems.containsKey((characterCount + localCount) + "-" + (characterCount + localCount + reviewWords[i].length()))) {
                        String newAnnotation = "T" + ++tagCount + "\t" + dictionaryTerms.get(words.get(j)) + " " + (localCount + characterCount) + " " + (localCount + characterCount + reviewWords[i].length()) + "\t" + reviewWords[i];
                        taggedItems.put((localCount + characterCount) + "-" + (localCount + characterCount + reviewWords[i].length()), dictionaryTerms.get(words.get(j)));
                        System.out.println(newAnnotation);
                        //                        writePrintStream(newAnnotation,filePathAnnDestination);
                    }
                }
            }
        }
        characterCount += review.length();
        characterCount++;
    }


    // tokenize the reviews to use in lemmatization process
    public String[] tokenize(String review) throws IOException {
        InputStream is = new FileInputStream("en-token.bin");

        TokenizerModel model = new TokenizerModel(is);

        TokenizerME tokenizer = new TokenizerME(model);

        String tokens[] = tokenizer.tokenize(review);

        is.close();
        return tokens;
    }

    //this method used to load the already tagged tags in the file that need to be annotated
    private void loadOldTags() throws IOException {
        File fileAnnotation = new File(filePathAnnDestination);
        if (!fileAnnotation.exists()) {
            fileAnnotation.createNewFile();
        }
        FileReader fr = new FileReader(fileAnnotation);




        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            String[] annotations = line.split("[ \t]");
            String word = line.substring(line.lastIndexOf('\t') + 1);
            String[] words = word.split(" ");

            int start = Integer.parseInt(annotations[2]);
            if (words.length != 0) {
                for (int i = 0; i < words.length; i++) {
                    int end = start + words[i].length();
                    taggedItems.put(start + "-" + end, annotations[1]);
                    System.out.println("Loading old tags: " + start + "-" + end + "\t" + annotations[1]);
                    start = end + 1;
                }
            }
            taggedItems.put(annotations[2] + "-" + annotations[3], annotations[1]);

            noOfTagsAlready = Integer.parseInt(annotations[0].substring(1));
            if (noOfTagsAlreadyMax < noOfTagsAlready)
                noOfTagsAlreadyMax = noOfTagsAlready;
        }
        br.close();
        fr.close();
        System.out.println("Loaded tags");
    }

    /*public void annotateNormal() throws IOException {
        File fileAnnotation = new File(filePathDictionary);
        FileReader fr = new FileReader(fileAnnotation);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            String[] annotations = line.split("[ \t]");
            String word = line.substring(line.indexOf(" ") + 1);
            annotate(annotations[0], word.toLowerCase());
        }
        br.close();
        fr.close();
        System.out.println("Done");
    }*/

    // the annotation process happens here
    public void annotate(String aspect, String item) throws IOException {
        File fileAnnotation = new File(filePathReview);
        FileReader fr = new FileReader(fileAnnotation);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int indexTotal = 0;

        if (!(item.contains(" ") ||item.contains("+") ||item.contains("\"") ||item.contains("'")||item.contains(".") ||item.contains(":") || item.contains("(") || item.contains(")") ||item.contains("-")|| item.contains(";")))
        while ((line = br.readLine()) != null) {
            Pattern pattern = Pattern.compile("\\b(" + item + ")\\b");
            Matcher matcher = pattern.matcher(line.toLowerCase());
            while (matcher.find()) {

                int currentIndex = matcher.start();

                if (!alreadyAnnotated((currentIndex + indexTotal) + "-" + (currentIndex + indexTotal + item.length()), aspect, line.substring(currentIndex, matcher.end()))) {
                    String newAnnotation = "T" + ++tagCount + "\t" + aspect + " " + (currentIndex + indexTotal) + " " + (currentIndex + indexTotal + item.length()) + "\t" + line.substring(currentIndex, matcher.end());

                    String word = line.substring(currentIndex, matcher.end());
                    String[] words = word.split(" ");
                    int start = (currentIndex + indexTotal);

                    if (words.length != 0) {
                        for (int i = 0; i < words.length; i++) {
                            int end = start + words[i].length();
                            taggedItems.put(start + "-" + end, aspect);
                            start = end + 1;
                        }
                    }
                    taggedItems.put((currentIndex + indexTotal) + "-" + (currentIndex + indexTotal + item.length()), aspect);
                    System.out.println("ANN\t" + newAnnotation);
                    writePrintStream(newAnnotation, filePathAnnDestination);
                }
            }
            indexTotal += line.length() + 1;
            test++;
        }
        br.close();
        fr.close();
    }

    public void analyzeReview(String review) throws IOException {
        

        Set set = dictionaryTerms.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String aspect = entry.getValue().toString();
            String item = entry.getKey().toString();


            if (!(item.contains(" ") ||item.contains("+")||item.contains("\"") ||item.contains("'")||item.contains(".") ||item.contains(":") || item.contains("(") || item.contains(")") ||item.contains("-")|| item.contains(";"))) {
                Pattern pattern = Pattern.compile("\\b(" + item + ")\\b");
                Matcher matcher = pattern.matcher(review.toLowerCase());
                while (matcher.find()) {

                    int currentIndex = matcher.start();

                    if (!alreadyAnnotated((currentIndex + characterCount) + "-" + (currentIndex + characterCount + item.length()), aspect, review.substring(currentIndex, matcher.end()))) {
                        String newAnnotation = "T" + ++tagCount + "\t" + aspect + " " + (currentIndex + characterCount) + " " + (currentIndex + characterCount + item.length()) + "\t" + review.substring(currentIndex, matcher.end());

                        String word = review.substring(currentIndex, matcher.end());
                        String[] words = word.split(" ");
                        int start = (currentIndex + characterCount);

                        if (words.length != 0) {
                            for (int i = 0; i < words.length; i++) {
                                int end = start + words[i].length();
                                taggedItems.put(start + "-" + end, aspect);
                                start = end + 1;
                            }
                        }

                        taggedItems.put((currentIndex + characterCount) + "-" + (currentIndex + characterCount + item.length()), aspect);


                        System.out.println("AR\t"+newAnnotation);
                        writePrintStream(newAnnotation, filePathAnnDestination);
                    }
                }
            }


            test++;
        }
        characterCount += review.length();
        characterCount++;
    }

    // this method used to check whether a word already tagged or not
    public boolean alreadyAnnotated(String indeces, String tag, String word) {
        if (taggedItems.containsKey(indeces)) {
            if (!tag.equals(taggedItems.get(indeces)))
                System.out.println("Conflict : " + word + " Old tag: " + taggedItems.get(indeces) + " New tag: " + tag);
            return true;
        } else
            return false;
    }

    //write word and aspect to the .ann file
    public void writePrintStream(String line, String path) {
        PrintStream fileStream = null;
        File file = new File(path);

        try {
            fileStream = new PrintStream(new FileOutputStream(file, true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        fileStream.println(line);
        fileStream.close();
    }

    // update the dictionary in memory( word and aspect pair) using its root words
    private void populateDictionaryByLemmatizer() throws IOException {

        Enumeration e = dictionaryTerms.keys();
        while (e.hasMoreElements()) {
            String word = (String) e.nextElement();
            String tag = dictionaryTerms.get(word);

            if (!(word.contains(" ")||word.contains("+") ||word.contains("\"") ||word.contains("'")||word.contains(".") ||word.contains(":") || word.contains("(") || word.contains(")") ||word.contains("-")|| word.contains(";"))) {
                String tokenizedWords[] = tokenize(word);

                if (tokenizedWords.length == 1) {
                    List<String> stemmings = stanfordLemmatizer.lemmatize(word);

                    for (int i = 0; i < stemmings.size(); i++) {
                        if (!dictionaryTerms.containsKey(stemmings.get(i))) {
                            dictionaryTerms.put(stemmings.get(i), tag);
                            System.out.println("Stemming: " + word + "\t" + stemmings.get(i));
                        }
                    }
                }
            }
        }
    }

    // update the dictionary in memory( word and aspect pair) using its synonyms
    private void populateDictionaryBySynonyms() {

        Enumeration e = dictionaryTerms.keys();
        while (e.hasMoreElements()) {
            String word = (String) e.nextElement();
            String tag = dictionaryTerms.get(word);

            ArrayList<String> synonyms = PyDictionary.findSynonyms(word);

            for (int i = 0; i < synonyms.size(); i++) {
                if (!dictionaryTerms.containsKey(synonyms.get(i))) {
                    dictionaryTerms.put(synonyms.get(i), tag);
                }
            }
        }
    }
}
