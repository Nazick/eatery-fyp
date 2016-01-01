package model;

import javax.persistence.*;

/**
 * Created by bruntha on 12/23/15.
 */
@Entity
@Table(name = "business", schema = "", catalog = "eatery")
public class BusinessEntity {

    @Id
    @Column(name = "business_id")
    private String businessId;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "type")
    private String type;

    @Basic
    @Column(name = "city")
    private String city;

    @Basic
    @Column(name = "state")
    private String state;

    @Basic
    @Column(name = "stars")
    private Float stars;

    public BusinessEntity() {
    }

    public BusinessEntity(String businessId, String name, String type, String city, String state, Float stars) {
        this.businessId = businessId;
        this.name = name;
        this.type = type;
        this.city = city;
        this.state = state;
        this.stars = stars;
    }

    public BusinessEntity(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Float getStars() {
        return stars;
    }

    public void setStars(Float stars) {
        this.stars = stars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusinessEntity that = (BusinessEntity) o;

        if (businessId != null ? !businessId.equals(that.businessId) : that.businessId != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (stars != null ? !stars.equals(that.stars) : that.stars != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = businessId != null ? businessId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (stars != null ? stars.hashCode() : 0);
        return result;
    }
}
