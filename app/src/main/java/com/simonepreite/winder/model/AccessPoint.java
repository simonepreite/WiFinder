package com.simonepreite.winder.model;

/**
 * Created by smogarch on 08/05/17.
 */

public class AccessPoint {
    private String bssid;
    private String ssid;

    public AccessPoint() {
    }

    public AccessPoint(String name, String mac) {
        bssid = mac;
        ssid = name;
    }

    public String getMac(){
        return this.bssid;
    }
}
