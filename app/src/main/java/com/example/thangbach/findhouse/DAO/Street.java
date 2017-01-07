package com.example.thangbach.findhouse.DAO;

/**
 * Created by THANG_BACH on 10/14/16.
 */

public class Street {

    String streetID;
    String streetName;
    String districtID;
    String cityID;

    public Street() {
    }

    public Street(String streetID, String streetName, String districtID, String cityID) {
        this.streetID = streetID;
        this.streetName = streetName;
        this.districtID = districtID;
        this.cityID = cityID;
    }

    public String getStreetID() {
        return streetID;
    }

    public void setStreetID(String streetID) {
        this.streetID = streetID;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
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
