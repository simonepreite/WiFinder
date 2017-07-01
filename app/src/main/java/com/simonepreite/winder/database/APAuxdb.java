package com.simonepreite.winder.database;

import android.provider.BaseColumns;

/**
 * Created by smogarch on 01/07/17.
 */

public class APAuxdb {
    private APAuxdb(){}

    public static class APBaseColums implements BaseColumns{
        public static final String TABLE_NAME = "APLIST";
        public static final String COLUMN_SSID = "SSID";
        public static final String COLUMN_MAC_ADDRESS = "BSSID";
        public static final String DB = "level";
        public static final String CAPABILITIES = "security";
        public static final String POSITION = "gps_cordinates";
    }


}
