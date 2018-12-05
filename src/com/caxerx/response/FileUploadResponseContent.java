package com.caxerx.response;

import java.io.Serializable;

public class FileUploadResponseContent implements Serializable {
    private int imageId;


    public FileUploadResponseContent() {
    }

    public FileUploadResponseContent(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
