package com.example.thangbach.findhouse.DAO;

/**
 * Created by THANG_BACH on 10/05/16.
 */

public class MenuItem {
    int image;
    String title;

    public MenuItem() {
    }

    public MenuItem(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
