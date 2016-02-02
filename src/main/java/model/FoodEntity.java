package model;

import javax.persistence.*;

/**
 * Created by bruntha on 2/2/16.
 */
@Entity
@Table(name = "food", schema = "", catalog = "eatery")
@IdClass(FoodEntityPK.class)
public class FoodEntity {
    private String restaurantId;
    private String foodItem;
    private String clusterHead;
    private Double score;

    public FoodEntity() {
    }

    public FoodEntity(String restaurantId, String foodItem, String clusterHead, Double score) {

        this.restaurantId = restaurantId;
        this.foodItem = foodItem;
        this.clusterHead = clusterHead;
        this.score = score;
    }

    @Id
    @Column(name = "restaurant_id")
    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Id
    @Column(name = "food_item")
    public String getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    @Id
    @Column(name = "cluster_head")
    public String getClusterHead() {
        return clusterHead;
    }

    public void setClusterHead(String clusterHead) {
        this.clusterHead = clusterHead;
    }

    @Basic
    @Column(name = "score")
    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodEntity that = (FoodEntity) o;

        if (clusterHead != null ? !clusterHead.equals(that.clusterHead) : that.clusterHead != null) return false;
        if (foodItem != null ? !foodItem.equals(that.foodItem) : that.foodItem != null) return false;
        if (restaurantId != null ? !restaurantId.equals(that.restaurantId) : that.restaurantId != null) return false;
        if (score != null ? !score.equals(that.score) : that.score != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = restaurantId != null ? restaurantId.hashCode() : 0;
        result = 31 * result + (foodItem != null ? foodItem.hashCode() : 0);
        result = 31 * result + (clusterHead != null ? clusterHead.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        return result;
    }
}
