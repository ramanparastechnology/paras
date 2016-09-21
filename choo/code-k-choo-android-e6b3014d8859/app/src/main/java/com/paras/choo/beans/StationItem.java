package com.paras.choo.beans;

/**
 * Created by paras on 14-07-2015.
 */
public class StationItem {
    String stationName = "";
    public StationItem(String stationName){
        this.stationName = stationName;
    }

    public String getStationName() {
        return this.stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
