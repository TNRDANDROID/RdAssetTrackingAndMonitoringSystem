package com.nic.RdAssetTrackingAndMonitoringSystem.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RdAssetTracking";
    private static final int DATABASE_VERSION = 1;
    public static final String ROAD_LIST_TABLE = "RoadList";
    public static final String ASSET_LIST_TABLE = "AssetList";
    public static final String PMGSY_VILLAGE_LIST_TABLE = "PMGSYVillageList";
    public static final String PMGSY_HABITATION_LIST_TABLE = "PMGSYHabitationList";
    public static final String SAVE_LAT_LONG_TABLE = "LatLongTable";
    public static final String SAVE_IMAGE_LAT_LONG_TABLE = "ImageLatLongTable";
    public static final String SAVE_IMAGE_HABITATION_TABLE = "ImageHabitationTable";
    public static final String BRIDGES_CULVERT = "BridgesAndCulvert";


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
                "road_code TEXT," +
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

        db.execSQL("CREATE TABLE " + PMGSY_VILLAGE_LIST_TABLE + " ("
                + "pmgsy_dcode INTEGER," +
                "pmgsy_bcode INTEGER," +
                "pmgsy_pvcode INTEGER," +
                "pmgsy_pvname TEXT)");

        db.execSQL("CREATE TABLE " + PMGSY_HABITATION_LIST_TABLE + " ("
                + "dcode INTEGER," +
                "bcode INTEGER," +
                "pvcode INTEGER," +
                "habcode INTEGER," +
                "pmgsy_dcode INTEGER," +
                "pmgsy_bcode INTEGER," +
                "pmgsy_pvcode INTEGER," +
                "pmgsy_hab_code INTEGER," +
                "image_available TEXT,"+
                "image_flag TEXT,"+
                "server_flag TEXT," +
                "pmgsy_habname TEXT)");

        db.execSQL("CREATE TABLE " + SAVE_LAT_LONG_TABLE + " ("
                + "road_category TEXT," +
                "road_id TEXT," +
                "point_type TEXT," +
                "road_lat TEXT UNIQUE," +
                "road_long TEXT UNIQUE, " +
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

        db.execSQL("CREATE TABLE " + SAVE_IMAGE_HABITATION_TABLE + " ("
                + "dcode INTEGER," +
                "bcode INTEGER," +
                "pvcode INTEGER," +
                "habcode INTEGER," +
                "pmgsy_dcode INTEGER," +
                "pmgsy_bcode INTEGER," +
                "pmgsy_pvcode INTEGER," +
                "pmgsy_hab_code INTEGER," +
                "images blob," +
                "remark TEXT," +
                "road_lat TEXT," +
                "road_long TEXT," +
                "server_flag  INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE " + BRIDGES_CULVERT + " ("
                + "data_type TEXT," +
                "loc_grp INTEGER," +
                "loc INTEGER," +
                "dcode INTEGER," +
                "bcode INTEGER," +
                "pvcode INTEGER," +
                "road_id INTEGER," +
                "culvert_type INTEGER," +
                "culvert_type_name TEXT," +
                "chainage INTEGER," +
                "culvet_name TEXT," +
                "span INTEGER," +
                "no_of_span INTEGER," +
                "width INTEGER," +
                "vent_height INTEGER," +
                "length INTEGER," +
                "culvert_id INTEGER," +
                "start_lat TEXT," +
                "start_long TEXT," +
                "server_flag TEXT," +
                "image_flag TEXT," +
                "online_images blob," +
                "image_available TEXT," +
                "images blob)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            //drop table if already exists
            db.execSQL("DROP TABLE IF EXISTS " + ROAD_LIST_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ASSET_LIST_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PMGSY_VILLAGE_LIST_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PMGSY_HABITATION_LIST_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_LAT_LONG_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_IMAGE_LAT_LONG_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_IMAGE_HABITATION_TABLE);
            onCreate(db);
        }
    }


}
