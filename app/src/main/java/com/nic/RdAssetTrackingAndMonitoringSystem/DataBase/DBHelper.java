package com.nic.RdAssetTrackingAndMonitoringSystem.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RdAssetTracking";
    private static final int DATABASE_VERSION = 1;
    public static final String ROAD_LIST_TABLE = "RoadList";
    public static final String ASSET_LIST_TABLE = "AssetList";


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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            //drop table if already exists
            db.execSQL("DROP TABLE IF EXISTS " + ROAD_LIST_TABLE);
            onCreate(db);
        }
    }


}
