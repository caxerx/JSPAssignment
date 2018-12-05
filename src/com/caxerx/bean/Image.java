package com.caxerx.bean;

import java.io.Serializable;
import java.sql.Blob;

public class Image implements Serializable {
    private int id;
    private Blob image;
    private String type;

    public Image() {
    }

    public Image(int id, Blob image, String type) {
        this.id = id;
        this.image = image;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
