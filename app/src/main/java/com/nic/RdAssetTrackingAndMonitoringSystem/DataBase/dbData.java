package com.nic.RdAssetTrackingAndMonitoringSystem.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

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
        Log.d("Inserted_id_road",String.valueOf(id));
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

    public ArrayList<RoadListValue> getSavedAsset() {

        ArrayList<RoadListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
          //  cursor = db.rawQuery("select * from "+DBHelper.SAVE_IMAGE_LAT_LONG_TABLE,null);
             cursor = db.query(DBHelper.SAVE_IMAGE_LAT_LONG_TABLE,
                     new String[]{"*"}, "server_flag = ?", new String[]{"0"}, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.KEY_IMAGES));
                    byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    RoadListValue card = new RoadListValue();
                    card.setRoadID(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_ID)));
                    card.setRoadCategory(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_CATEGORY)));
                    card.setAssetId(cursor.getString(cursor
                            .getColumnIndex(AppConstant.KEY_ASSET_ID)));
                    card.setRoadLat(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_LAT)));
                    card.setRoadLong(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_LONG)));
                    card.setImage(decodedByte);
                    card.setCreatedDate(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CREATED_DATE)));

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

    public ArrayList<RoadListValue> selectImage(String road_id,String road_category,String asset_id) {

        ArrayList<RoadListValue> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = "road_id = ? and road_category = ? and asset_id = ? and server_flag = ?";
        String[] selectionArgs = new String[]{road_id,road_category,asset_id,"0"};

        try {
            //  cursor = db.rawQuery("select * from "+DBHelper.SAVE_IMAGE_LAT_LONG_TABLE,null);
            cursor = db.query(DBHelper.SAVE_IMAGE_LAT_LONG_TABLE,
                    new String[]{"*"}, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.KEY_IMAGES));
                    byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    RoadListValue card = new RoadListValue();
                    card.setRoadID(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_ID)));
                    card.setRoadCategory(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_CATEGORY)));
                    card.setAssetId(cursor.getString(cursor
                            .getColumnIndex(AppConstant.KEY_ASSET_ID)));
                    card.setRoadLat(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_LAT)));
                    card.setRoadLong(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_LONG)));
                    card.setImage(decodedByte);
                    card.setCreatedDate(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CREATED_DATE)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
             Log.d("Exception" , e.toString());
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<RoadListValue> select_Asset(JSONObject code, String type) {

        ArrayList<RoadListValue> assets = new ArrayList<>();
        Cursor cursor = null;
        String condition = "";
        String road_id ="";
        String loc_grp ="";

        String[] columns = new String[0];
        String selection = null;
        String[] selectionArgs = new String[0];

        if (type.equalsIgnoreCase("firstLevel"))
        {
            try {
                road_id = String.valueOf(code.get(AppConstant.KEY_ROAD_ID));
                columns = new String[]{"distinct road_id,loc_grp,group_name"};
                selection = "road_id = ?";
                selectionArgs = new String[]{road_id};
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(type.equalsIgnoreCase("secondLevel")) {
            try {
                road_id = String.valueOf(code.get(AppConstant.KEY_ROAD_ID));
                loc_grp = String.valueOf(code.get(AppConstant.KEY_LOCATION_GROUP));
                columns = new String[]{"*"};
                selection = "road_id = ? and loc_grp = ?";
                selectionArgs = new String[]{road_id,loc_grp};
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
             cursor = db.query(DBHelper.ASSET_LIST_TABLE,
                     columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RoadListValue asset = new RoadListValue();
                    asset.setRoadID(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_ID)));
                    asset.setLocGroup(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LOCATION_GROUP)));
                    asset.setGroupName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GROUP_NAME)));
                    asset.setLevelType(type);

                    if (type.equalsIgnoreCase("secondLevel")) {
                        asset.setSubgroupName(cursor.getString(cursor
                                .getColumnIndexOrThrow(AppConstant.KEY_SUB_GROUP_NAME)));
                        asset.setColLabel(cursor.getString(cursor
                                .getColumnIndexOrThrow(AppConstant.KEY_COLUMN_LABEL)));
                        asset.setLocationDetails(cursor.getString(cursor
                                .getColumnIndexOrThrow(AppConstant.KEY_LOCATION_DETAILS)));
                    }
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

    public RoadListValue saveLatLong(RoadListValue saveLatLongValue) {
        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_ROAD_CATEGORY, saveLatLongValue.getRoadCategory());
        values.put(AppConstant.KEY_ROAD_ID, saveLatLongValue.getRoadID());
        String pointtype = saveLatLongValue.getPointType();
        values.put(AppConstant.KEY_POINT_TYPE, pointtype);
        values.put(AppConstant.KEY_ROAD_LAT, saveLatLongValue.getRoadLat());
        values.put(AppConstant.KEY_ROAD_LONG, saveLatLongValue.getRoadLong());
        values.put(AppConstant.KEY_CREATED_DATE, saveLatLongValue.getCreatedDate());
        long id = db.insert(DBHelper.SAVE_LAT_LONG_TABLE, null, values);
        if (id > 0) {
            if(pointtype.equalsIgnoreCase("1")) {
                Toasty.success(context, "Start Point Inserted", Toast.LENGTH_SHORT, true).show();
            } else
            if(pointtype.equalsIgnoreCase("2")) {
                Toasty.success(context, "Middle Point Inserted", Toast.LENGTH_SHORT, true).show();
            } else
                if(pointtype.equalsIgnoreCase("3")) {
                Toasty.success(context, "End Point Inserted", Toast.LENGTH_SHORT, true).show();
            }

        }
        Log.d("Inserted_id_saveLatLong", String.valueOf(id));

        return saveLatLongValue;
    }

    public ArrayList<RoadListValue> getSavedTrack() {

        ArrayList<RoadListValue> sendPostLatLong = new ArrayList<>();
        Cursor cursor = null;

        try {
           // cursor = db.rawQuery("select * from " + DBHelper.SAVE_LAT_LONG_TABLE, null);
            cursor = db.query(DBHelper.SAVE_LAT_LONG_TABLE,
                    new String[]{"*"}, "server_flag = ?", new String[]{"0"}, null, null, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RoadListValue postLatLong = new RoadListValue();

                    postLatLong.setRoadCategory(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_ROAD_CATEGORY)));
                    postLatLong.setRoadID(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.KEY_ROAD_ID)));
                    postLatLong.setPointType(cursor.getString(cursor.getColumnIndex(AppConstant.KEY_POINT_TYPE)));
                    postLatLong.setRoadLat(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_ROAD_LAT)));
                    postLatLong.setRoadLong(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_ROAD_LONG)));
                    postLatLong.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CREATED_DATE)));

                    sendPostLatLong.add(postLatLong);
                }
            }
        } catch (Exception e) {
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return sendPostLatLong;
    }

    public void update_image() {
        String whereClause = "server_flag = server_flag";
        Log.d("Update id is " ,"id");
        ContentValues values = new ContentValues();
        values.put("server_flag",1);
        db.update(DBHelper.SAVE_IMAGE_LAT_LONG_TABLE, values, whereClause, null);
    }

    public void update_Track() {
        String whereClause = "server_flag = server_flag";
        Log.d("Update id is " ,"id");
        ContentValues values = new ContentValues();
        values.put("server_flag",1);
        db.update(DBHelper.SAVE_LAT_LONG_TABLE, values, whereClause, null);
    }

    public void deleteAssetTable() {
        db.execSQL("delete from "+ DBHelper.ASSET_LIST_TABLE);
    }


}
