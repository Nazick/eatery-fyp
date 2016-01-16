package domain;

import java.util.ArrayList;

/**
 * Created by bruntha on 1/12/16.
 */
public class Cluster {
    String clusterHead;
    ArrayList<String> foodList;

    public Cluster(String clusterHead, ArrayList<String> foodList) {
        this.clusterHead = clusterHead;
        this.foodList = foodList;
    }

    public String getClusterHead() {
        return clusterHead;
    }

    public void setClusterHead(String clusterHead) {
        this.clusterHead = clusterHead;
    }

    public ArrayList<String> getFoodList() {
        return foodList;
    }

    public void setFoodList(ArrayList<String> foodList) {
        this.foodList = foodList;
    }

    public void addNewElement(String foodName) {
        foodList.add(foodName);
    }
}
