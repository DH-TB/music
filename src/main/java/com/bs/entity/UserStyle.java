package com.bs.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "userStyle")
public class UserStyle {

    @Id
    @GeneratedValue

    private Long id;
    private Long userId;
    private Integer jazz;
    private Integer rock;
    private Integer hiphop;
    private Integer light;
    private Integer blues;
    private Integer classical;
    private Integer pop;
    private Integer heavyMetal;
    private Integer rap;
    private Integer folk;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getJazz() {
        return jazz;
    }

    public void setJazz(Integer jazz) {
        this.jazz = jazz;
    }

    public Integer getRock() {
        return rock;
    }

    public void setRock(Integer rock) {
        this.rock = rock;
    }

    public Integer getHiphop() {
        return hiphop;
    }

    public void setHiphop(Integer hiphop) {
        this.hiphop = hiphop;
    }

    public Integer getLight() {
        return light;
    }

    public void setLight(Integer light) {
        this.light = light;
    }

    public Integer getBlues() {
        return blues;
    }

    public void setBlues(Integer blues) {
        this.blues = blues;
    }

    public Integer getClassical() {
        return classical;
    }

    public void setClassical(Integer classical) {
        this.classical = classical;
    }

    public Integer getPop() {
        return pop;
    }

    public void setPop(Integer pop) {
        this.pop = pop;
    }

    public Integer getHeavyMetal() {
        return heavyMetal;
    }

    public void setHeavyMetal(Integer heavyMetal) {
        this.heavyMetal = heavyMetal;
    }

    public Integer getRap() {
        return rap;
    }

    public void setRap(Integer rap) {
        this.rap = rap;
    }

    public Integer getFolk() {
        return folk;
    }

    public void setFolk(Integer folk) {
        this.folk = folk;
    }
}
