package model;

import javax.persistence.*;

/**
 * Created by bruntha on 12/24/15.
 */
@Entity
@Table(name = "weights", schema = "", catalog = "eatery")
@IdClass(WeightsEntityPK.class)
public class WeightsEntity {
    private String aspect;
    private int level;
    private String parentAspect;
    private Double weight;

    @Override
    public String toString() {
        return "WeightsEntity{" +
                "aspect='" + aspect + '\'' +
                ", level=" + level +
                ", parentAspect='" + parentAspect + '\'' +
                ", weight=" + weight +
                '}';
    }

    public WeightsEntity() {
    }

    public WeightsEntity(String aspect, int level, String parentAspect, Double weight) {
        this.aspect = aspect;
        this.level = level;
        this.parentAspect = parentAspect;
        this.weight = weight;
    }

    @Id
    @Column(name = "aspect")
    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    @Id
    @Column(name = "level")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
    @Column(name = "weight")
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeightsEntity that = (WeightsEntity) o;

        if (level != that.level) return false;
        if (aspect != null ? !aspect.equals(that.aspect) : that.aspect != null) return false;
        if (parentAspect != null ? !parentAspect.equals(that.parentAspect) : that.parentAspect != null) return false;
        if (weight != null ? !weight.equals(that.weight) : that.weight != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = aspect != null ? aspect.hashCode() : 0;
        result = 31 * result + level;
        result = 31 * result + (parentAspect != null ? parentAspect.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        return result;
    }
}
