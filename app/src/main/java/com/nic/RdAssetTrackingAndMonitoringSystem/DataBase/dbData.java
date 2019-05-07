package com.nic.RdAssetTrackingAndMonitoringSystem.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class dbData {
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private Context context;

    public dbData(Context context){
        this.dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if(dbHelper != null) {
            dbHelper.close();
        }
    }

    /****** ROAD LIST *****/
    public RoadListValue create_newRoad(RoadListValue roadListValue) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_ROAD_ID,roadListValue.getRoadID());
        values.put(AppConstant.KEY_ROAD_CODE,roadListValue.getRoadCode());
        values.put(AppConstant.KEY_ROAD_CATEGORY_CODE,roadListValue.getRoadCategoryCode());
        values.put(AppConstant.KEY_ROAD_CATEGORY,roadListValue.getRoadCategory());
        values.put(AppConstant.KEY_ROAD_NAME,roadListValue.getRoadName());
        values.put(AppConstant.KEY_ROAD_VILLAGE_NAME,roadListValue.getRoadVillage());

        long id = db.insert(DBHelper.ROAD_LIST_TABLE,null,values);
        Log.d("Inserted_id",String.valueOf(id));
        return roadListValue;
    }

    public ArrayList<RoadListValue> getAll_Road(String code1) {

        ArrayList<RoadListValue> cards = new ArrayList<>();
        Cursor cursor = null;
        String condition = "";
        String code = code1;

        if(code.equalsIgnoreCase("0")){

        }else
            if (code.equalsIgnoreCase("4")) {
            condition = " where road_category_code != 3 ";
        }
        else
            {
                condition = " where road_category_code="+code;
            }

        try {
            cursor = db.rawQuery("select * from "+DBHelper.ROAD_LIST_TABLE + condition,null);
           // cursor = db.query(CardsDBHelper.TABLE_CARDS,
             //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RoadListValue card = new RoadListValue();
                    card.setRoadID(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_ID)));
                    card.setRoadCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_CODE)));
                    card.setRoadCategoryCode(cursor
                            .getInt(cursor.getColumnIndex(AppConstant.KEY_ROAD_CATEGORY_CODE)));
                    card.setRoadName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_NAME)));
                    card.setRoadCategory(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_CATEGORY)));
                    card.setRoadVillage(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_VILLAGE_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
         //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    /**** ASSET LIST ***/

    public RoadListValue create_newAsset(RoadListValue assetListValue) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_ROAD_ID,assetListValue.getRoadID());
        values.put(AppConstant.KEY_LOCATION_GROUP,assetListValue.getLocGroup());
        values.put(AppConstant.KEY_LOCATION_ID,assetListValue.getLocID());
        values.put(AppConstant.KEY_GROUP_NAME,assetListValue.getGroupName());
        values.put(AppConstant.KEY_SUB_GROUP_NAME,assetListValue.getSubgroupName());
        values.put(AppConstant.KEY_COLUMN_LABEL,assetListValue.getColLabel());
        values.put(AppConstant.KEY_LOCATION_DETAILS,assetListValue.getLocationDetails());

        long id = db.insert(DBHelper.ASSET_LIST_TABLE,null,values);
        Log.d("Inserted_id_Asset",String.valueOf(id));
        return assetListValue;
    }

    public ArrayList<RoadListValue> getAll_Asset() {

        ArrayList<RoadListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.ASSET_LIST_TABLE,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RoadListValue card = new RoadListValue();
                    card.setRoadID(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_ID)));
                    card.setLocGroup(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LOCATION_GROUP)));
                    card.setLocID(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_LOCATION_ID)));
                    card.setGroupName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GROUP_NAME)));
                    card.setSubgroupName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_SUB_GROUP_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<RoadListValue> select_Asset(JSONObject code1, String type) {

        ArrayList<RoadListValue> assets = new ArrayList<>();
        Cursor cursor = null;
        String condition = "";
        JSONObject code = code1;
        String code2 ="";

        if (type.equalsIgnoreCase("firstLevel"))
        {
            try {
                code2 = String.valueOf(code.get(AppConstant.KEY_ROAD_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(type.equalsIgnoreCase("secondLevel")) {

        }


//        if(code.equalsIgnoreCase("0")){
//
//        }else
//        if (code.equalsIgnoreCase("4")) {
//            condition = " where road_category_code != 3 ";
//        }
//        else
//        {
//            condition = " where road_category_code="+code;
//        }

        try {
            //cursor = db.rawQuery("select * from "+DBHelper.ASSET_LIST_TABLE +" where road_id="+code,null);
             cursor = db.query(DBHelper.ASSET_LIST_TABLE,
                     new String[]{"distinct road_id,loc_grp,group_name"}, " road_id = ? ", new String[]{code2}, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RoadListValue asset = new RoadListValue();
                    asset.setRoadID(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_ID)));
                    asset.setLocGroup(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LOCATION_GROUP)));
                    asset.setGroupName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GROUP_NAME)));

                    assets.add(asset);
                }
            }
        } catch (Exception e){
              Log.d( "Exception", String.valueOf(e));
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return assets;
    }



}
