package model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by bruntha on 2/2/16.
 */
public class FoodEntityPK implements Serializable {
    private String restaurantId;
    private String foodItem;
    private String clusterHead;

    @Column(name = "restaurant_id")
    @Id
    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Column(name = "food_item")
    @Id
    public String getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    @Column(name = "cluster_head")
    @Id
    public String getClusterHead() {
        return clusterHead;
    }

    public void setClusterHead(String clusterHead) {
        this.clusterHead = clusterHead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodEntityPK that = (FoodEntityPK) o;

        if (clusterHead != null ? !clusterHead.equals(that.clusterHead) : that.clusterHead != null) return false;
        if (foodItem != null ? !foodItem.equals(that.foodItem) : that.foodItem != null) return false;
        if (restaurantId != null ? !restaurantId.equals(that.restaurantId) : that.restaurantId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = restaurantId != null ? restaurantId.hashCode() : 0;
        result = 31 * result + (foodItem != null ? foodItem.hashCode() : 0);
        result = 31 * result + (clusterHead != null ? clusterHead.hashCode() : 0);
        return result;
    }
}
