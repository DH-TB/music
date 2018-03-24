package com.bs.entity;

import javax.persistence.*;

@Entity
@Table(name = "signer")
public class Signer {
    @Id
    @GeneratedValue

    private Long id;
    private String tag;
    private String signer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
