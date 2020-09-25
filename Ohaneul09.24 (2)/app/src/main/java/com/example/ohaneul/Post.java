package com.example.ohaneul;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class Post {

    private String docID;
    private String uid;
    private String title;
    private String local;
    private Date date;
    private String content;
    private String image;
    private String thumbnail;

    public Post() {    }

    public Post(String title, String local, Date date, String content, String image,
                String docID, String uid){
        this.title = title;
        this.local = local;
        this.date = date;
        this.content = content;
        this.image = image;
        this.docID = docID;
        this.uid = uid;
    }

    public  Post(String thumbnail, String title, String docID, String uid){
        this.thumbnail = thumbnail;
        this.title = title;
        this.docID = docID;
        this.uid = uid;
    }

    public Post(String image, String docID, String uid){
        this.image = image;
        this.docID = docID;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
