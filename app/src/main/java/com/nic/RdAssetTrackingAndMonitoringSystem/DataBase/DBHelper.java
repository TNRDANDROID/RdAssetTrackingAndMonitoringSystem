package com.nic.RdAssetTrackingAndMonitoringSystem.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RdAssetTracking";
    private static final int DATABASE_VERSION = 1;
    public static final String ROAD_LIST_TABLE = "RoadList";
    public static final String ASSET_LIST_TABLE = "AssetList";
    public static final String SAVE_LAT_LONG_TABLE = "LatLongTable";
    public static final String SAVE_IMAGE_LAT_LONG_TABLE = "ImageLatLongTable";


    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    //creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ROAD_LIST_TABLE + " ("
                + "road_category_code INTEGER," +
                "road_id INTEGER," +
                "road_code INTEGER," +
                "tot_asset INTEGER," +
                "asset_cap_cnt INTEGER," +
                "tot_start_point INTEGER," +
                "tot_mid_point INTEGER," +
                "tot_end_point INTEGER," +
                "road_category TEXT," +
                "pvname TEXT," +
                "road_name TEXT)");

        db.execSQL("CREATE TABLE " + ASSET_LIST_TABLE + " ("
                + "road_id INTEGER," +
                "loc_grp INTEGER," +
                "loc INTEGER," +
                "group_name TEXT," +
                "location_sub_group_name TEXT," +
                "col_label TEXT," +
                "location_details TEXT)");

        db.execSQL("CREATE TABLE " + SAVE_LAT_LONG_TABLE + " ("
                + "road_category TEXT," +
                "road_id TEXT," +
                "point_type TEXT," +
                "road_lat TEXT," +
                "road_long TEXT, " +
                "server_flag  INTEGER DEFAULT 0," +
                "created_date TEXT)");

        db.execSQL("CREATE TABLE " + SAVE_IMAGE_LAT_LONG_TABLE + " ("
                + "road_category TEXT," +
                "road_id TEXT," +
                "asset_id TEXT," +
                "road_lat TEXT," +
                "road_long TEXT," +
                "images blob," +
                "server_flag  INTEGER DEFAULT 0," +
                "created_date TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            //drop table if already exists
            db.execSQL("DROP TABLE IF EXISTS " + ROAD_LIST_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ASSET_LIST_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_LAT_LONG_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_IMAGE_LAT_LONG_TABLE);
            onCreate(db);
        }
    }


}
