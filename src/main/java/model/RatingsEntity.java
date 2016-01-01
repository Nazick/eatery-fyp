package model;

import AggregatingModel.LBNCI;

import javax.persistence.*;

/**
 * Created by bruntha on 12/24/15.
 */
@Entity
@Table(name = "ratings", schema = "", catalog = "eatery")
public class RatingsEntity {

    @Id @GeneratedValue
    @Column(name = "rating_id")
    private Integer ratingId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id",referencedColumnName = "business_id")
    private BusinessEntity restaurant;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aspect_tag", referencedColumnName = "aspect_tag")
    private AspectEntity aspect;

    @Column(name = "score")
    private Double score;

    @Column(name = "no_of_occurance")
    private Integer noOfoccurance;

    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public BusinessEntity getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(BusinessEntity restaurant) {
        this.restaurant = restaurant;
    }

    public AspectEntity getAspect() {
        return aspect;
    }

    public void setAspect(AspectEntity aspect) {
        this.aspect = aspect;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getNoOfoccurance() {
        return noOfoccurance;
    }

    public void setNoOfoccurance(Integer noOfoccurance) {
        this.noOfoccurance = noOfoccurance;
    }

    public void increaseNoOfOccurance(){
        if(this.noOfoccurance == null){
            this.noOfoccurance = 0;
        }
        this.noOfoccurance +=1;
    }

    public void addScore(Integer score){
        LBNCI lbnci = new LBNCI();
        if(this.score == null){
            this.score = 0.0;
            this.noOfoccurance = 0;
        }
        this.score = lbnci.calculateLBNCI(this.score,noOfoccurance,score.doubleValue());
    }
}
