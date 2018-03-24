package com.bs.entity;

import javax.persistence.*;

@Entity
@Table(name="collection")

public class Collection {
    @Id
    @GeneratedValue

    private Long id;
    @Column
    private Long userId;
    @Column
    private Long musicId;
    @Column
    private Long playCount;
    @Column
    private Integer love;


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

    public Long getMusicId() {
        return musicId;
    }

    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }

    public Integer getLove() {
        return love;
    }

    public void setLove(Integer love) {
        this.love = love;
    }

    public Long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Long playCount) {
        this.playCount = playCount;
    }
}
