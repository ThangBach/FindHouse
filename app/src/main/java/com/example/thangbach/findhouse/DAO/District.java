package com.example.thangbach.findhouse.DAO;

/**
 * Created by THANG_BACH on 10/14/16.
 */

public class District {
    String districtID;
    String districtName;
    String cityID;

    public District() {
    }

    public District(String districtID, String districtName, String cityID) {
        this.districtID = districtID;
        this.districtName = districtName;
        this.cityID = cityID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }
}
