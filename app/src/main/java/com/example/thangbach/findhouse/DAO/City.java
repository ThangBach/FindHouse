package com.example.thangbach.findhouse.DAO;

/**
 * Created by THANG_BACH on 10/14/16.
 */

public class City {

    String cityID;
    String cityName;

    public City() {
    }

    public City(String cityID, String cityName) {
        this.cityID = cityID;
        this.cityName = cityName;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
