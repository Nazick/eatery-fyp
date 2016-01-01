package com.eatery.preprocessing;

import org.json.simple.JSONObject;

import java.io.*;
import java.util.*;

/**
 * Created by bruntha on 7/9/15.
 */
public class SeparateReviews {
    public static void main(String[] args) {
        SeparateReviews separateReviews = new SeparateReviews();
        separateReviews.getRandomReviews(100);

    }


    final static String filePathRead = "/home/bruntha/Documents/FYP/Data/Samples/" +
            "review_100_C.json";
    final static String filePathWrite = "/home/bruntha/Documents/FYP/Data/Samples/" +
            "u_16.txt";
    final static String filePathEnglishReviews = "/home/bruntha/Documents/FYP/Data/English Review/" +
            "englishReviews.json";
    final static String filePathFrequency = "/home/bruntha/Documents/FYP/Data/English Review/" +
            "Frequency";
    final static String filePathFrequencySorted = "/home/bruntha/Documents/FYP/Data/English Review/" +
            "FrequencySorted";
    final static String filePathTop100BusinessReviews = "/home/bruntha/Documents/FYP/Data/English Review/" +
            "top100Business.json";

    ArrayList<Integer> randomIntegers=new ArrayList<>();
    Hashtable<String,Integer> frequency=new Hashtable<>();
    ArrayList<String> top100Bussiness=new ArrayList<>();

    int count=0;
    int written=0;



    public void separateReviews() {
        try {
            readLinesUsingFileReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void separateTop100BussiessReview() {
        try {
            loadTop100Business();
            readLinesUsingFileReaderB();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTop100Business() throws IOException {
        File file = new File(filePathFrequencySorted);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            top100Bussiness.add(line);
        }
        br.close();
        fr.close();

    }

    public void getRandomReviews(int count){
        for (int i = 0; i < count; i++) {
            int rand=getRandInt(1,980528);
            if (randomIntegers.contains(rand)) {
                i--;
            }else {
                randomIntegers.add(rand);
            }
        }
        try {
            readJSONUsingFileReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getRandInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the hibernate scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand=new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    private void readLinesUsingFileReader() throws IOException {
        File file = new File(filePathRead);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            splitJson(line);    //splitting each json into ...
        }
        br.close();
        fr.close();
        System.out.println("Done...");
    }

    private void readLinesUsingFileReaderB() throws IOException {
        File file = new File(filePathEnglishReviews);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            splitJsonB(line);    //splitting each json into ...
        }
        br.close();
        fr.close();
        System.out.println("Done...");
    }

    private void readJSONUsingFileReader() throws IOException {
        File file = new File(filePathEnglishReviews);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            count++;
            if (randomIntegers.contains(count)) {
                written++;
                splitJson(line);
            }
        }
        br.close();
        fr.close();
        System.out.println(written+" no of reviews written to file from "+count+" reviews");
    }

    private void writePrintStream(String line,String path) {
        PrintStream fileStream = null;
        File file = new File(path);

        try {
            fileStream = new PrintStream(new FileOutputStream(file, true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        fileStream.println(line);
//        fileStream.println();
        fileStream.close();


    }

    private void splitJson(String json) {
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();

        try {

            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            String review = (String) jsonObject.get("text");    // get review text from json
            String reviewWONewLine=review.replace("\n", "").replace("\r", "");
            System.out.println("R   "+reviewWONewLine);
            writePrintStream(reviewWONewLine,filePathWrite); //write review as json if it is english

        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    private void splitJsonB(String json) {
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();

        try {

            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            String business_id = (String) jsonObject.get("business_id");    // get review text from json
            if (top100Bussiness.contains(business_id)) {
                System.out.println(json);
                writePrintStream(json,filePathTop100BusinessReviews); //write review as json if it is english
            }

        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    public void sortBusiness() throws IOException {
        File file = new File(filePathFrequency);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            String[] strings=line.split(" ");
            frequency.put(strings[0],Integer.parseInt(strings[2]));
        }
        br.close();
        fr.close();

        sortHashtable(frequency);
    }

    public void sortHashtable(Hashtable<?, Integer> t){

        //Transfer as List and sort it
        ArrayList<Map.Entry<?, Integer>> l = new ArrayList(t.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<?, Integer>>(){

            public int compare(Map.Entry<?, Integer> o1, Map.Entry<?, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }});

//        System.out.println(l);
        for (int i = l.size()-1; i > l.size()-100-1 ; i--) {
            System.out.println(l.get(i).getKey().toString()+" "+l.get(i).getValue());
            writePrintStream(l.get(i).getKey().toString(),filePathFrequencySorted);
        }
    }

}