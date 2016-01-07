package PreProcess;

import org.json.simple.JSONObject;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by bruntha on 7/9/15.
 */
public class SeparateReviews {
    final static String filePathRead = "/home/bruntha/Documents/FYP/Data/Samples/" +
            "review_100_C.json";
    final static String filePathWrite = "/home/bruntha/Documents/FYP/Data/Samples/" +
            "u_16.txt";
    final static String filePathEnglishReviews = "/home/bruntha/Documents/FYP/Data/English Review/" +
            "englishReviews.json";
//    final static String filePathFrequency = "/home/bruntha/Documents/FYP/Data/English Review/" +
//            "Frequency";
    final static String filePathFrequencySorted = "/home/bruntha/Documents/FYP/Data/English Review/" +
            "FrequencySorted";
    final static String filePathTop100BusinessReviews = "/home/bruntha/Documents/FYP/Data/English Review/" +
            "top2000Business.json";
    ArrayList<Integer> randomIntegers = new ArrayList<>();
//    Hashtable<String, Integer> frequency = new Hashtable<>();
    ArrayList<String> top100Bussiness = new ArrayList<>();
    ArrayList<String> topBussiness = new ArrayList<>();
    int topReviews = 0;
    int count = 0;
    int written = 0;

    public static void main(String[] args) {
        SeparateReviews separateReviews = new SeparateReviews();
        separateReviews.seperateTopReviews();

    }

    public void separateReviews() {
        try {
            readLinesUsingFileReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void seperateTopReviews()
    {
        try {
            Hashtable<String, Integer> freq=getFrequency(filePathEnglishReviews, "2011-01-01", "2011-03-31");
            separateTopBussiessReview(2000,freq,"2011-01-01", "2011-03-31");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void separateTopBussiessReview(int noOfRevies,Hashtable<String, Integer> hashtable,String dateStart, String dateEnd) {
        try {
            sortHashtable(hashtable, noOfRevies);
//            loadTopBusiness();
            readLinesUsingFileReaderB(dateStart,dateEnd);
            System.out.println("Total reviews = " + topReviews);
            System.out.println("Total restaurnats = " + topBussiness.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTopBusiness() throws IOException {
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

    public void getRandomReviews(int count) {
        for (int i = 0; i < count; i++) {
            int rand = getRandInt(1, 980528);
            if (randomIntegers.contains(rand)) {
                i--;
            } else {
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
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand = new Random();

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

    private void getCount(String filePath) throws IOException {
        File file = new File(filePath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            count++;
        }
        br.close();
        fr.close();
        System.out.println("Count = " + count);
    }

    private void readLinesUsingFileReaderB(String dateStart,String dateEnd) throws IOException {
        File file = new File(filePathEnglishReviews);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            splitJsonB(line,dateStart,dateEnd);    //splitting each json into ...
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
        System.out.println(written + " no of reviews written to file from " + count + " reviews");
    }

    private void writePrintStream(String line, String path) {
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
            String reviewWONewLine = review.replace("\n", "").replace("\r", "");
            System.out.println("R   " + reviewWONewLine);
            writePrintStream(reviewWONewLine, filePathWrite); //write review as json if it is english

        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    private void splitJsonB(String json,String dateStart,String dateEnd) {
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();

        try {

            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            String business_id = (String) jsonObject.get("business_id");    // get review text from json
            String date = (String) jsonObject.get("date");    // get review text from json
            if (topBussiness.contains(business_id)) {
//                System.out.println(json);
                if (topReviews < 2000 && isDateInRange(date, dateStart, dateEnd)) {
                    topReviews++;
                    writePrintStream(json, filePathTop100BusinessReviews); //write review as json if it is english
                }
            }

        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    private void splitJsonC(String json, String dateStart, String dateEnd, Hashtable<String, Integer> hashtable) {
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();

        try {
            Object obj = parser.parse(json);
            JSONObject jsonObject = (JSONObject) obj;

            String business_id = (String) jsonObject.get("business_id");    // get review text from json
            String date = (String) jsonObject.get("date");    // get review text from json
            if (isDateInRange(date, dateStart, dateEnd)) {
                if (hashtable.containsKey(business_id))
                    hashtable.put(business_id, hashtable.get(business_id) + 1);
                else
                    hashtable.put(business_id, 1);
            }

        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean isDateInRange(String date, String dateStart, String dateEnd) {
        boolean result = false;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateD = sdf.parse(date);
            Date dateStartD = sdf.parse(dateStart);
            Date dateEndD = sdf.parse(dateEnd);

            if (dateD.compareTo(dateStartD) > 0 && dateD.compareTo(dateEndD) < 0) {
                result = true;
            } else {
                result = false;
            }

        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Hashtable<String, Integer> getFrequency(String filePath, String dateStart, String dateEnd) throws IOException {
        Hashtable<String, Integer> frequency = new Hashtable<>();

        File file = new File(filePath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            splitJsonC(line, dateStart, dateEnd, frequency);
        }
        br.close();
        fr.close();

        return frequency;
    }

//    public void sortBusiness(int noOfReview,Hashtable<String, Integer> hashtable) throws IOException {
//        File file = new File(filePathFrequency);
//        FileReader fr = new FileReader(file);
//        BufferedReader br = new BufferedReader(fr);
//        String line;
//
//        while ((line = br.readLine()) != null) {
//            String[] strings = line.split(" ");
//            hashtable.put(strings[0], Integer.parseInt(strings[2]));
//        }
//        br.close();
//        fr.close();
//
//        sortHashtable(hashtable, noOfReview);
//    }

    public void sortHashtable(Hashtable<?, Integer> t, int noOfReview) {

        //Transfer as List and sort it
        ArrayList<Map.Entry<?, Integer>> l = new ArrayList(t.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<?, Integer>>() {

            public int compare(Map.Entry<?, Integer> o1, Map.Entry<?, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        int count = 0;
        for (int i = l.size() - 1; i > l.size() - 100 - 1; i--) {
            System.out.println(l.get(i).getKey().toString() + " " + l.get(i).getValue());
            writePrintStream(l.get(i).getKey().toString(), filePathFrequencySorted);
            if (count < noOfReview) {
                topBussiness.add(l.get(i).getKey().toString());
                count += l.get(i).getValue();
            }
        }
    }

}

