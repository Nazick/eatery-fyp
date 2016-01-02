package model;

import AggregatingModel.LBNCI;

import javax.persistence.*;

/**
 * Created by bruntha on 1/1/16.
 */
@Entity
@Table(name = "ratings", schema = "", catalog = "eatery")
public class RatingsEntity {
    private int ratingId;
    private String aspectTag;
    private String restaurantId;
    private double score;
    private int noOfOccurance;

    public RatingsEntity() {
    }

    public RatingsEntity(String aspectTag, String restaurantId, double score, int noOfOccurance) {
        this.aspectTag = aspectTag;
        this.restaurantId = restaurantId;
        this.score = score;
        this.noOfOccurance = noOfOccurance;
    }

    @Id
    @Column(name = "rating_id")
    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    @Basic
    @Column(name = "aspect_tag")
    public String getAspectTag() {
        return aspectTag;
    }

    public void setAspectTag(String aspectTag) {
        this.aspectTag = aspectTag;
    }

    @Basic
    @Column(name = "restaurant_id")
    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Basic
    @Column(name = "score")
    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Basic
    @Column(name = "no_of_occurance")
    public int getNoOfOccurance() {
        return noOfOccurance;
    }

    public void setNoOfOccurance(int noOfOccurance) {
        this.noOfOccurance = noOfOccurance;
    }

    public void addScore(Integer score){
        LBNCI lbnci = new LBNCI();
        this.score = lbnci.calculateLBNCI(this.score,noOfOccurance,this.score);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RatingsEntity that = (RatingsEntity) o;

        if (noOfOccurance != that.noOfOccurance) return false;
        if (ratingId != that.ratingId) return false;
        if (Double.compare(that.score, score) != 0) return false;
        if (aspectTag != null ? !aspectTag.equals(that.aspectTag) : that.aspectTag != null) return false;
        if (restaurantId != null ? !restaurantId.equals(that.restaurantId) : that.restaurantId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = ratingId;
        result = 31 * result + (aspectTag != null ? aspectTag.hashCode() : 0);
        result = 31 * result + (restaurantId != null ? restaurantId.hashCode() : 0);
        temp = Double.doubleToLongBits(score);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + noOfOccurance;
        return result;
    }
}
