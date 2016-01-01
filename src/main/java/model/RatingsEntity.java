package model;

import javax.persistence.*;
import java.util.DoubleSummaryStatistics;

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
    private BusinessEntity restaurantId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aspect_id", referencedColumnName = "aspect_id")
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

    public BusinessEntity getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(BusinessEntity restaurantId) {
        this.restaurantId = restaurantId;
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
}
