package com.bio.espalet.model;

import android.graphics.Bitmap;

import java.util.Date;

import okhttp3.HttpUrl;

public class Snapshot {

    private HttpUrl url;
    private Bitmap image;
    private Date date;

    public Snapshot(HttpUrl url) {
        this.url = url;
    }

    public HttpUrl getUrl() {
        return url;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
