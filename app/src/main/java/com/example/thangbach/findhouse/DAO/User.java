package com.example.thangbach.findhouse.DAO;

import java.io.Serializable;

/**
 * Created by THANG_BACH on 09/22/16.
 */

public class User implements Serializable{
    public String id;
    public String fullName;
    public String userName;
    public String passWord;
    public String urlImg;
    public String email;

    public User() {
    }

    public User(String id, String fullName, String userName, String passWord, String urlImg, String email) {
        this.id = id;
        this.fullName = fullName;
        this.userName = userName;
        this.passWord = passWord;
        this.urlImg = urlImg;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
