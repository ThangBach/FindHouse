package com.example.thangbach.findhouse.DAO;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by ThangBach on 10/25/2016.
 */

public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
