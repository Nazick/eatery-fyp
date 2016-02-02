package AggregatingModel;

import model.FoodRating;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by bruntha on 2/1/16.
 */
public class FoodAggregation {
    LBNCI lbnci = new LBNCI();

    public static void main(String[] args) {
        FoodAggregation foodAggregation = new FoodAggregation();
        foodAggregation.storeAggregateRating();
    }

    public void storeAggregateRating() {
        try {
            HashMap<String, FoodRating> foodRatingHashMap = readFoodFile("FoodRatings");

            Set set = foodRatingHashMap.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();

                FoodRating foodRating= (FoodRating) entry.getValue();
                writePrintStream(foodRating.getRestaurantID()+"\t"+foodRating.getFoodName()+"\t"+foodRating.getScore(),"Data/FoodRatingAggregated.txt");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, FoodRating> readFoodFile(String filePath) throws IOException {
        HashMap<String, FoodRating> foodRatingHashMap = new HashMap<>();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filePath).getFile());
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            String[] tmp = line.split("[ \t]");
            if (!foodRatingHashMap.containsKey(tmp[0] + " " + tmp[1])) {
                foodRatingHashMap.put(tmp[0] + " " + tmp[1], new FoodRating(tmp[0], tmp[1], Double.parseDouble(tmp[2]), 1));
            } else {
                FoodRating foodRating = foodRatingHashMap.get(tmp[0] + " " + tmp[1]);
                foodRating.setScore(lbnci.calculateLBNCI(foodRating.getScore(), foodRating.getNoOfOccurance(), Double.parseDouble(tmp[2])));
                foodRating.setNoOfOccurance(foodRating.getNoOfOccurance() + 1);
                foodRatingHashMap.put(tmp[0] + " " + tmp[1], foodRating);
            }
        }
        br.close();
        fr.close();
        return foodRatingHashMap;
    }

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
}
