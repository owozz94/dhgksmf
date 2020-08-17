package com.example.ohaneul;

import android.widget.ImageView;
import android.widget.TextView;

public class Data {
    private int profileImage;
    private String profileName;
    private String title;
    private String content;
    private int favoriteImage;
    private String favoriteText;
    private int image;

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) { this.profileName = profileName; }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFavoriteImage() {
        return favoriteImage;
    }

    public ImageView setFavoriteImage(int favoriteImage) {
        this.favoriteImage = favoriteImage;
        return null;
    }

    public String getFavoriteText() {
        return favoriteText;
    }

    public TextView setFavoriteText(String favoriteText) {
        this.favoriteText = favoriteText;
        return null;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
