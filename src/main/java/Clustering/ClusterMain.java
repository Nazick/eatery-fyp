package Clustering;

import domain.Cluster;
import hibernate.HibernateMain;
import model.FoodEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bruntha on 1/12/16.
 */
public class ClusterMain {
    HashMap<String, Cluster> clusterHashMap = new HashMap<>();
    HashMap<String, String> clusterHashMap2 = new HashMap<>();   //key-> food item    value-> cluster head

    ArrayList<String> foodList = new ArrayList<>();
    HibernateMain hibernateMain = new HibernateMain();

    public static void main(String[] args) {
        ClusterMain clusterMain = new ClusterMain();
        clusterMain.storeScore();
    }

    public void storeScore() {
        buildCluster();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("FoodRatingAggregated.txt").getFile());
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {
                String[] tmp = line.split("[ \t]");
                System.out.println(tmp[0] + " " + tmp[1] + " " + clusterHashMap2.get(tmp[1]) + " " + Double.parseDouble(tmp[2]));
                if (clusterHashMap2.get(tmp[1]) != null) {
                    hibernateMain.insertFoodScores(new FoodEntity(tmp[0], tmp[1], clusterHashMap2.get(tmp[1]), Double.parseDouble(tmp[2])));

                }

            }
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buildCluster() {
        try {
            readFile("Clusters.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewFoodToCluster(String foodName) {
        if (!foodList.contains(foodName)) {
            String[] foodNames = foodName.split(" ");

            for (int i = 0; i < foodNames.length; i++) {
                if (clusterHashMap.containsKey(foodNames[i])) {
                    Cluster cluster = clusterHashMap.get(foodNames[i]);
                    cluster.addNewElement(foodName);
                    clusterHashMap.put(foodNames[i], cluster);
                }
            }
        }
    }

//    private boolean isFoodClustered(String foodName) {
//        for (int i = 0; i < ; i++) {
//
//        }
//        return false;
//    }

    private void readFile(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        FileReader fr = new FileReader(new File(classLoader.getResource(fileName).getFile()));
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            String foodListString = br.readLine();
            String clusterHeadString = br.readLine();
//            br.readLine();
//            br.readLine();

            String foodListString1 = foodListString.replace("[", "");
            String foodListString2 = foodListString1.replace("[", "");
            String[] foodNames = foodListString2.split(",");

            String clusterHeadString1 = clusterHeadString.replaceAll("\\(", "");
            String[] clusterHeads = clusterHeadString1.split(",");
            String clusterHeadName = clusterHeads[0].replace("'", "");

            ArrayList<String> foodListArrayList = new ArrayList<>();
            for (int i = 0; i < foodNames.length; i++) {
                String foodName = foodNames[i].replace("'", "");
                foodListArrayList.add(foodName);
                foodList.add(foodName);
                clusterHashMap2.put(foodName, clusterHeadName);

            }

            if (clusterHashMap.containsKey(clusterHeadName))
                System.out.println(clusterHeadName);

            clusterHashMap.put(clusterHeadName, new Cluster(clusterHeadName, foodListArrayList));

        }
        br.close();
        fr.close();
    }
}
