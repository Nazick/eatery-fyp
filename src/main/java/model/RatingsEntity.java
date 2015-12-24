package model;

import javax.persistence.*;

/**
 * Created by bruntha on 12/24/15.
 */
@Entity
@Table(name = "ratings", schema = "", catalog = "eatery")
public class RatingsEntity {
    private String restaurantId;
    private String aspect;
    private String parentAspect;
    private double rating;
    private int noOfoccurance;
    private int level;
    private double weight;

    @Id
    @Column(name = "restaurantID")
    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Basic
    @Column(name = "aspect")
    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    @Basic
    @Column(name = "parentAspect")
    public String getParentAspect() {
        return parentAspect;
    }

    public void setParentAspect(String parentAspect) {
        this.parentAspect = parentAspect;
    }

    @Basic
    @Column(name = "rating")
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Basic
    @Column(name = "noOfoccurance")
    public int getNoOfoccurance() {
        return noOfoccurance;
    }

    public void setNoOfoccurance(int noOfoccurance) {
        this.noOfoccurance = noOfoccurance;
    }

    @Basic
    @Column(name = "level")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Basic
    @Column(name = "weight")
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RatingsEntity that = (RatingsEntity) o;

        if (level != that.level) return false;
        if (noOfoccurance != that.noOfoccurance) return false;
        if (Double.compare(that.rating, rating) != 0) return false;
        if (Double.compare(that.weight, weight) != 0) return false;
        if (aspect != null ? !aspect.equals(that.aspect) : that.aspect != null) return false;
        if (parentAspect != null ? !parentAspect.equals(that.parentAspect) : that.parentAspect != null) return false;
        if (restaurantId != null ? !restaurantId.equals(that.restaurantId) : that.restaurantId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = restaurantId != null ? restaurantId.hashCode() : 0;
        result = 31 * result + (aspect != null ? aspect.hashCode() : 0);
        result = 31 * result + (parentAspect != null ? parentAspect.hashCode() : 0);
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + noOfoccurance;
        result = 31 * result + level;
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
