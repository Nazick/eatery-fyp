package model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by bruntha on 12/24/15.
 */
public class WeightsEntityPK implements Serializable {
    private String aspect;
    private int level;

    @Column(name = "aspect")
    @Id
    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    @Column(name = "level")
    @Id
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeightsEntityPK that = (WeightsEntityPK) o;

        if (level != that.level) return false;
        if (aspect != null ? !aspect.equals(that.aspect) : that.aspect != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = aspect != null ? aspect.hashCode() : 0;
        result = 31 * result + level;
        return result;
    }
}
