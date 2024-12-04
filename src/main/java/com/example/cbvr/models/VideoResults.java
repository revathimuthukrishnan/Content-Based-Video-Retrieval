package com.example.cbvr.models;

public class VideoResults { 

    private String caption;
    private String base64Image;

    public VideoResults(String caption, String base64Image) {
        this.caption = caption;
        this.base64Image = base64Image;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
