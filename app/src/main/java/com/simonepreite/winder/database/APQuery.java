package com.simonepreite.winder.database;

import static com.simonepreite.winder.database.APAuxdb.APBaseColums.*;

/**
 * Created by smog on 7/12/17.
 */

public class APQuery {
    private APQuery(){}

    //CREATE TABLE

    public static String SQL_CREATE_AP_TABLE =
            "CREATE TABLE " + TABLE_AP + "(" +
                    COLUMN_MAC_ADDRESS + " STRING PRIMARY KEY," +
                    COLUMN_SSID + " STRING NOT NULL," +
                    CAPABILITIES + " STRING NOT NULL" +
                    ")" ;

    public static String SQL_CREATE_MISURATION_TABLE =
            "CREATE TABLE " + TABLE_MEASURATION + "(" +
                    COLUMN_MAC_ADDRESS + " STRING NOT NULL," +
                    DB + " INT NOT NULL," +
                    LATITUDE+" DOUBLE,"+
                    LONGITUDE+" DOUBLE, "+
                    "PRIMARY KEY(" + COLUMN_MAC_ADDRESS + ", " + DB +  ")" +
                    ")";

    //DELETE TABLE

    public static String SQL_DELETE_AP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_AP;

    public static String SQL_DELETE_MISURATION_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_AP;

    //QUERIES

    public static String GET_AP_POSITION = "select " + DB + ", " + LATITUDE + ", " + LONGITUDE +
            " from " + TABLE_MEASURATION + " where "
            + COLUMN_MAC_ADDRESS
            + " =? order by " + DB + " desc limit 1";

    public static String GET_AP_COVERAGE = "select " + DB + ", " + LATITUDE + ", " + LONGITUDE +
            " from " + TABLE_MEASURATION + " where "
            + COLUMN_MAC_ADDRESS
            + " =? order by " + DB + " asc limit 1";

    public static String GET_ALL_ENTRIES = "select * from " + TABLE_AP;

    public static String GET_ROW_BY_MAC = "select * from "+ TABLE_AP +" where "+ COLUMN_MAC_ADDRESS + "=?";
}
