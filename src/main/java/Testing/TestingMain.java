package Testing;

import AggregatingModel.LBNCI;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bruntha on 1/2/16.
 */
public class TestingMain {
    HashMap<String, ArrayList<Double>> businesses = new HashMap<>();

    public static void main(String[] args) {
        TestingMain testingMain = new TestingMain();
        testingMain.calculateRating();
    }

    public void calculateRating() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File reviewJson = new File(classLoader.getResource("top2000Business.json").getFile());

            readJson(reviewJson);
            calculateAggregatingRating();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void calculateAggregatingRating() {
        LBNCI lbnci = new LBNCI();
        for (Map.Entry<String, ArrayList<Double>> entry : businesses.entrySet()) {
            String businessID = entry.getKey();

            double score = lbnci.calculateAvgRating(toArray(entry.getValue()));
            System.out.println(businessID + " " + score);
        }
    }

    public double[] toArray(ArrayList<Double> arrayList) {
        double array[] = new double[arrayList.size()];
        for (int j = 0; j < arrayList.size(); j++) {
            array[j] = arrayList.get(j);
        }
        return array;
    }

    public void readJson(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            splitJson(line);
        }
        br.close();
        fr.close();
    }

    public void splitJson(String json) {
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        try {
            Object obj = parser.parse(json);
            JSONObject jsonObject = (JSONObject) obj;

            String businessID = (String) jsonObject.get("business_id");    // get review text from json
            int stars = Integer.parseInt(jsonObject.get("stars").toString());    // get review text from json

            if (businesses.containsKey(businessID)) {
                ArrayList<Double> starsList = businesses.get(businessID);
                starsList.add(stars * 1.0);
                businesses.put(businessID, starsList);
            } else {
                ArrayList<Double> starsList = new ArrayList<>();
                starsList.add(stars * 1.0);
                businesses.put(businessID, starsList);
            }

        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }
}
