package model;

/**
 * Created by bruntha on 2/1/16.
 */
public class FoodRating {
    String restaurantID;
    String foodName;
    double score;
    int noOfOccurance;

    public FoodRating(String restaurantID, String foodName, double score, int noOfOccurance) {
        this.restaurantID = restaurantID;
        this.foodName = foodName;
        this.score = score;
        this.noOfOccurance = noOfOccurance;
    }

    public FoodRating() {

    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getNoOfOccurance() {
        return noOfOccurance;
    }

    public void setNoOfOccurance(int noOfOccurance) {
        this.noOfOccurance = noOfOccurance;
    }
}
