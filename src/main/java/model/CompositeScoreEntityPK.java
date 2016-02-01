package model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by bruntha on 1/31/16.
 */
public class CompositeScoreEntityPK implements Serializable {
    private String businessId;
    private int aspectId;

    @Column(name = "business_id")
    @Id
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Column(name = "aspect_id")
    @Id
    public int getAspectId() {
        return aspectId;
    }

    public void setAspectId(int aspectId) {
        this.aspectId = aspectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompositeScoreEntityPK that = (CompositeScoreEntityPK) o;

        if (aspectId != that.aspectId) return false;
        if (businessId != null ? !businessId.equals(that.businessId) : that.businessId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = businessId != null ? businessId.hashCode() : 0;
        result = 31 * result + aspectId;
        return result;
    }
}
