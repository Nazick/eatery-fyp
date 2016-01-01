package model;

import javax.persistence.*;

/**
 * Created by nazick on 1/1/16.
 */

@Entity
@Table(name = "aspect", schema = "", catalog = "eatery")
public class AspectEntity {

    @Id @GeneratedValue
    @Column(name = "aspect_id")
    private Integer aspectId;

    @Column(name = "aspect_name")
    private String aspectName;

    @Column(name = "aspect_tag")
    private String aspectTag;

    @Column(name = "level")
    private Integer level;

    @Column(name = "parent_aspect")
    private Integer parentAspectId;



    public Integer getAspectId() {
        return aspectId;
    }

    public void setAspectId(Integer aspectId) {
        this.aspectId = aspectId;
    }

    public String getAspectName() {
        return aspectName;
    }

    public void setAspectName(String aspectName) {
        this.aspectName = aspectName;
    }

    public String getAspectTag() {
        return aspectTag;
    }

    public void setAspectTag(String aspectTag) {
        this.aspectTag = aspectTag;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getParentAspectId() {
        return parentAspectId;
    }

    public void setParentAspectId(Integer parentAspectId) {
        this.parentAspectId = parentAspectId;
    }

}
