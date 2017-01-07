package com.example.thangbach.findhouse.DAO;

import java.io.Serializable;

/**
 * Created by ThangBach on 10/19/2016.
 */

public class Post implements Serializable {
    String postID;
    String postImage1;
    String postImage2;
    String postImage3;
    String postImage4;
    String postAddress;
    String postDescFast;
    String postDescDetail;
    String postAcreage;
    String postPhone;
    String postPrice;
    String postDate;
    String userID;
    String districtID;
    String cityID;

    public Post() {
    }

    public Post(String postID, String postImage1, String postImage2, String postImage3, String postImage4, String postAddress, String postDescFast, String postDescDetail, String postAcreage, String postPhone, String postPrice, String postDate, String userID, String districtID, String cityID) {
        this.postID = postID;
        this.postImage1 = postImage1;
        this.postImage2 = postImage2;
        this.postImage3 = postImage3;
        this.postImage4 = postImage4;
        this.postAddress = postAddress;
        this.postDescFast = postDescFast;
        this.postDescDetail = postDescDetail;
        this.postAcreage = postAcreage;
        this.postPhone = postPhone;
        this.postPrice = postPrice;
        this.postDate = postDate;
        this.userID = userID;
        this.districtID = districtID;
        this.cityID = cityID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostImage1() {
        return postImage1;
    }

    public void setPostImage1(String postImage1) {
        this.postImage1 = postImage1;
    }

    public String getPostImage2() {
        return postImage2;
    }

    public void setPostImage2(String postImage2) {
        this.postImage2 = postImage2;
    }

    public String getPostImage3() {
        return postImage3;
    }

    public void setPostImage3(String postImage3) {
        this.postImage3 = postImage3;
    }

    public String getPostImage4() {
        return postImage4;
    }

    public void setPostImage4(String postImage4) {
        this.postImage4 = postImage4;
    }

    public String getPostAddress() {
        return postAddress;
    }

    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress;
    }

    public String getPostDescFast() {
        return postDescFast;
    }

    public void setPostDescFast(String postDescFast) {
        this.postDescFast = postDescFast;
    }

    public String getPostDescDetail() {
        return postDescDetail;
    }

    public void setPostDescDetail(String postDescDetail) {
        this.postDescDetail = postDescDetail;
    }

    public String getPostAcreage() {
        return postAcreage;
    }

    public void setPostAcreage(String postAcreage) {
        this.postAcreage = postAcreage;
    }

    public String getPostPhone() {
        return postPhone;
    }

    public void setPostPhone(String postPhone) {
        this.postPhone = postPhone;
    }

    public String getPostPrice() {
        return postPrice;
    }

    public void setPostPrice(String postPrice) {
        this.postPrice = postPrice;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }
}
