package com.example.thangbach.findhouse.DAO;

/**
 * Created by ThangBach on 10/19/2016.
 */

public class Address {

    private String cityName;
    private String districtName;
    private String wayName;

    public Address() {
    }

    public Address(String cityName, String districtName, String wayName) {
        this.cityName = cityName;
        this.districtName = districtName;
        this.wayName = wayName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getWayName() {
        return wayName;
    }

    public void setWayName(String wayName) {
        this.wayName = wayName;
    }
}
