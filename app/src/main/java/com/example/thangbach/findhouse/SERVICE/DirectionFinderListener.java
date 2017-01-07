package com.example.thangbach.findhouse.SERVICE;

import com.example.thangbach.findhouse.DAO.Route;

import java.util.List;

/**
 * Created by ThangBach on 10/25/2016.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
