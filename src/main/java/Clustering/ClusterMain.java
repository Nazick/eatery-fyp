package Clustering;

import domain.Cluster;

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
    ArrayList<String> foodList=new ArrayList<>();

    public static void main(String[] args) {
        ClusterMain clusterMain = new ClusterMain();
        clusterMain.buildCluster();
    }

    public void buildCluster() {
        try {
            readFile("Clustersample.txt");
            addNewFoodToCluster("lassi pairing");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewFoodToCluster(String foodName) {
        if (!foodList.contains(foodName)) {
            String[] foodNames = foodName.split(" ");

            for (int i = 0; i < foodNames.length; i++) {
                if(clusterHashMap.containsKey(foodNames[i])){
                    Cluster cluster=clusterHashMap.get(foodNames[i]);
                    cluster.addNewElement(foodName);
                    clusterHashMap.put(foodNames[i],cluster);
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
            br.readLine();
            br.readLine();

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
            }

            if (clusterHashMap.containsKey(clusterHeadName))
                System.out.println(clusterHeadName);

            clusterHashMap.put(clusterHeadName, new Cluster(clusterHeadName, foodListArrayList));

        }
        br.close();
        fr.close();
    }
}
