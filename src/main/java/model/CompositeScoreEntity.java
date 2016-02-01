package model;

import javax.persistence.*;

/**
 * Created by bruntha on 1/31/16.
 */
@Entity
@Table(name = "composite_score", schema = "", catalog = "eatery")
@IdClass(CompositeScoreEntityPK.class)
public class CompositeScoreEntity {
    private String businessId;
    private int aspectId;
    private double compositeScore;

    public CompositeScoreEntity(String businessId, int aspectId, double compositeScore) {
        this.businessId = businessId;
        this.aspectId = aspectId;
        this.compositeScore = compositeScore;
    }

    public CompositeScoreEntity() {
    }

    @Id
    @Column(name = "business_id")
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Id
    @Column(name = "aspect_id")
    public int getAspectId() {
        return aspectId;
    }

    public void setAspectId(int aspectId) {
        this.aspectId = aspectId;
    }

    @Basic
    @Column(name = "composite_score")
    public double getCompositeScore() {
        return compositeScore;
    }

    public void setCompositeScore(double compositeScore) {
        this.compositeScore = compositeScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompositeScoreEntity that = (CompositeScoreEntity) o;

        if (aspectId != that.aspectId) return false;
        if (Double.compare(that.compositeScore, compositeScore) != 0) return false;
        if (businessId != null ? !businessId.equals(that.businessId) : that.businessId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = businessId != null ? businessId.hashCode() : 0;
        result = 31 * result + aspectId;
        temp = Double.doubleToLongBits(compositeScore);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
