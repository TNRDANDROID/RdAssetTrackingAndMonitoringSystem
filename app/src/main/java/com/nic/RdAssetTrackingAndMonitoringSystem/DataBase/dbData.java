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
        values.put(AppConstant.KEY_TOTAL_ASSET,roadListValue.getTotalAsset());
        values.put(AppConstant.KEY_ASSET_CAPTURED_COUNT,roadListValue.getAssetCapturedCount());
        values.put(AppConstant.KEY_TOTAL_START_POINT,roadListValue.getTotalStartPoint());
        values.put(AppConstant.KEY_TOTAL_MID_POINT,roadListValue.getTotalMidPoint());
        values.put(AppConstant.KEY_TOTAL_END_POINT,roadListValue.getTotalEndPoint());

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
                    card.setRoadCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_CODE)));
                    card.setRoadCategoryCode(cursor
                            .getInt(cursor.getColumnIndex(AppConstant.KEY_ROAD_CATEGORY_CODE)));
                    card.setRoadName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_NAME)));
                    card.setRoadCategory(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_CATEGORY)));
                    card.setRoadVillage(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_VILLAGE_NAME)));

                    Integer tot_asset = cursor.getInt(cursor.getColumnIndex(AppConstant.KEY_TOTAL_ASSET));
                    Integer asset_cap_cnt = cursor.getInt(cursor.getColumnIndex(AppConstant.KEY_ASSET_CAPTURED_COUNT));
                    Integer tot_start_point = cursor.getInt(cursor.getColumnIndex(AppConstant.KEY_TOTAL_START_POINT));
                    Integer tot_mid_point = cursor.getInt(cursor.getColumnIndex(AppConstant.KEY_TOTAL_MID_POINT));
                    Integer tot_end_point = cursor.getInt(cursor.getColumnIndex(AppConstant.KEY_TOTAL_END_POINT));

                    String state = null;


                    if (tot_asset == 0) {

                        if ((tot_start_point != 0) && (tot_mid_point != 0) && (tot_end_point != 0)) {
                            state = "completed";
                        } else if ((asset_cap_cnt == 0) && (tot_start_point == 0) && (tot_mid_point == 0) && (tot_end_point == 0)) {
                            state = "Not_Started";
                        }
                        else {
                            state = "partial";
                        }
                    } else {
                        if (tot_asset == asset_cap_cnt) {

                            if ((tot_start_point != 0) && (tot_mid_point != 0) && (tot_end_point != 0)) {
                                state = "completed";
                            } else {
                                state = "partial";
                            }
                        } else if (tot_asset != asset_cap_cnt) {

                            if ((asset_cap_cnt == 0) && (tot_start_point == 0) && (tot_mid_point == 0) && (tot_end_point == 0)) {
                                state = "Not_Started";
                            } else {
                                state = "partial";
                            }
                        }
                    }

                    card.setState(state);
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

    public void deleteRoadListTable() {
        db.execSQL("delete from "+ DBHelper.ROAD_LIST_TABLE);
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

    public ArrayList<RoadListValue> getParticularAssetInfo(String road_id) {

        ArrayList<RoadListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            //  cursor = db.rawQuery("select * from "+DBHelper.SAVE_IMAGE_LAT_LONG_TABLE,null);
            cursor = db.query(DBHelper.SAVE_IMAGE_LAT_LONG_TABLE,
                    new String[]{"*"}, "server_flag = ? and road_id = ?", new String[]{"0",road_id}, null, null, null);
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

    public ArrayList<RoadListValue> selectImage(String road_id,String road_category,String asset_id,String server_flag) {

        ArrayList<RoadListValue> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = "road_id = ? and road_category = ? and asset_id = ? and server_flag = ?";
        String[] selectionArgs = new String[]{road_id,road_category,asset_id,server_flag};

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
                        asset.setLocID(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LOCATION_ID)));
                        asset.setLocGroup(cursor.getInt(cursor
                                .getColumnIndexOrThrow(AppConstant.KEY_LOCATION_GROUP)));
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

    public long saveLatLong(RoadListValue saveLatLongValue) {
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

        }else{
            Toasty.error(context, "Same Lat Long are Detected! Please change your Position", Toast.LENGTH_SHORT, true).show();
        }
        Log.d("Inserted_id_saveLatLong", String.valueOf(id));

        return id;
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

    public ArrayList<RoadListValue> getParticularRoadTrackInfo(String road_id) {

        ArrayList<RoadListValue> sendPostLatLong = new ArrayList<>();
        Cursor cursor = null;

        try {
            // cursor = db.rawQuery("select * from " + DBHelper.SAVE_LAT_LONG_TABLE, null);
            cursor = db.query(DBHelper.SAVE_LAT_LONG_TABLE,
                    new String[]{"*"}, "server_flag = ? and road_id = ?", new String[]{"0",road_id}, null, null, null);

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
        String whereClause = "server_flag = server_flag ";
        Log.d("Update id is " ,"id");
        ContentValues values = new ContentValues();
        values.put("server_flag",1);
        db.update(DBHelper.SAVE_LAT_LONG_TABLE, values, whereClause, null);
    }

    public void updateBridges(Integer road_id) {
        String whereClause = "image_flag = image_flag and road_id ="+road_id;
        Log.d("Update id is " ,"id");
        ContentValues values = new ContentValues();
        values.put("image_flag",1);
        db.update(DBHelper.SAVE_LAT_LONG_TABLE, values, whereClause, null);
    }

    public void deleteAssetTable() {
        db.execSQL("delete from "+ DBHelper.ASSET_LIST_TABLE);
    }

    /****** PMGSY VILLAGE LIST *******/

    public RoadListValue insert_newPMGSYVillage(RoadListValue pmgsyvillageValue) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_PMGSY_DCODE,pmgsyvillageValue.getPmgsyDcode());
        values.put(AppConstant.KEY_PMGSY_BCODE,pmgsyvillageValue.getPmgsyBcode());
        values.put(AppConstant.KEY_PMGSY_PVCODE,pmgsyvillageValue.getPmgsyPvcode());
        values.put(AppConstant.KEY_PMGSY_PVNAME,pmgsyvillageValue.getPmgsyPvname());

        long id = db.insert(DBHelper.PMGSY_VILLAGE_LIST_TABLE,null,values);
        Log.d("Inserted_id_PMGSYVil",String.valueOf(id));
        return pmgsyvillageValue;
    }

    public ArrayList<RoadListValue> getAll_PMGSYVillage() {

        ArrayList<RoadListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.PMGSY_VILLAGE_LIST_TABLE,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RoadListValue card = new RoadListValue();
                    card.setPmgsyDcode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PMGSY_DCODE)));
                    card.setPmgsyBcode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PMGSY_BCODE)));
                    card.setPmgsyPvcode(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_PMGSY_PVCODE)));
                    card.setPmgsyPvname(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PMGSY_PVNAME)));

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

    /****** PMGSY VILLAGE LIST *******/

    public RoadListValue insert_newPMGSYHabitation(RoadListValue pmgsyhabitationValue) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_DCODE,pmgsyhabitationValue.getdCode());
        values.put(AppConstant.KEY_BCODE,pmgsyhabitationValue.getbCode());
        values.put(AppConstant.KEY_PVCODE,pmgsyhabitationValue.getPvCode());
        values.put(AppConstant.KEY_HABCODE,pmgsyhabitationValue.getHabCode());
        values.put(AppConstant.KEY_PMGSY_DCODE,pmgsyhabitationValue.getPmgsyDcode());
        values.put(AppConstant.KEY_PMGSY_BCODE,pmgsyhabitationValue.getPmgsyBcode());
        values.put(AppConstant.KEY_PMGSY_PVCODE,pmgsyhabitationValue.getPmgsyPvcode());
        values.put(AppConstant.KEY_PMGSY_HAB_CODE,pmgsyhabitationValue.getPmgsyHabcode());
        values.put(AppConstant.KEY_PMGSY_HAB_NAME,pmgsyhabitationValue.getPmgsyHabName());
        values.put(AppConstant.KEY_IMAGE_AVAILABLE,pmgsyhabitationValue.getImageAvailable());


        long id = db.insert(DBHelper.PMGSY_HABITATION_LIST_TABLE,null,values);
        Log.d("Inserted_id_PMGSYHab",String.valueOf(id));
        return pmgsyhabitationValue;
    }

    public ArrayList<RoadListValue> getAll_PMGSYHabitation() {

        ArrayList<RoadListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.PMGSY_HABITATION_LIST_TABLE,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RoadListValue card = new RoadListValue();
                    card.setdCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_DCODE)));
                    card.setbCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_BCODE)));
                    card.setPvCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PVCODE)));
                    card.setHabCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_HABCODE)));
                    card.setPmgsyDcode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PMGSY_DCODE)));
                    card.setPmgsyBcode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PMGSY_BCODE)));
                    card.setPmgsyPvcode(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_PMGSY_PVCODE)));
                    card.setPmgsyHabcode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PMGSY_HAB_CODE)));
                    card.setPmgsyHabName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PMGSY_HAB_NAME)));

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

    public ArrayList<RoadListValue> select_Habitation(JSONObject data) {

        ArrayList<RoadListValue> habits = new ArrayList<>();
        Cursor cursor = null;
        String selection = "pmgsy_dcode = ? and pmgsy_bcode = ? and pmgsy_pvcode = ?";
        String[] selectionArgs = new String[0];

        try {
            selectionArgs = new String[]{String.valueOf(data.get(AppConstant.KEY_PMGSY_DCODE)), String.valueOf(data.get(AppConstant.KEY_PMGSY_BCODE)), String.valueOf(data.get(AppConstant.KEY_PMGSY_PVCODE))};
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            cursor = db.query(DBHelper.PMGSY_HABITATION_LIST_TABLE,
                    new String[]{"*"}, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RoadListValue habitation = new RoadListValue();
                    habitation.setdCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_DCODE)));
                    habitation.setbCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_BCODE)));
                    habitation.setPvCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PVCODE)));
                    habitation.setHabCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_HABCODE)));
                    habitation.setPmgsyDcode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PMGSY_DCODE)));
                    habitation.setPmgsyBcode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PMGSY_BCODE)));
                    habitation.setPmgsyPvcode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PMGSY_PVCODE)));
                    habitation.setPmgsyHabcode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PMGSY_HAB_CODE)));
                    habitation.setPmgsyHabName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PMGSY_HAB_NAME)));
                    habitation.setImageAvailable(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_IMAGE_AVAILABLE)));


                    habits.add(habitation);
                }
            }
        } catch (Exception e){
            Log.d( "Exception", String.valueOf(e));
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return habits;
    }

    public ArrayList<RoadListValue> getSavedHabitation(String flag_code) {

        ArrayList<RoadListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select a.*,b.pmgsy_habname as pmgsy_habname,c.pmgsy_pvname as  pmgsy_pvname from (select * from ImageHabitationTable where server_flag = "+flag_code+")a left join (select * from PMGSYHabitationList) b on a.pmgsy_dcode= b.pmgsy_dcode\n" +
                    "and a.pmgsy_bcode = b.pmgsy_bcode\n" +
                    "and a.pmgsy_pvcode = b.pmgsy_pvcode\n" +
                    "and a.pmgsy_hab_code = b.pmgsy_hab_code \n" +
                    "left join (select * from PMGSYVillageList)c on a.pmgsy_dcode = c.pmgsy_dcode and a.pmgsy_bcode = c.pmgsy_bcode\n" +
                    "and a.pmgsy_pvcode = c.pmgsy_pvcode",null);
//            cursor = db.query(DBHelper.SAVE_IMAGE_HABITATION_TABLE,
//                    new String[]{"*"}, "server_flag = ?", new String[]{flag_code}, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.KEY_IMAGES));
                    byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    RoadListValue card = new RoadListValue();
                    card.setdCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_DCODE)));
                    card.setbCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_BCODE)));
                    card.setPvCode(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_PVCODE)));
                    card.setHabCode(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_HABCODE)));
                    card.setPmgsyDcode(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_PMGSY_DCODE)));
                    card.setPmgsyBcode(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_PMGSY_BCODE)));
                    card.setPmgsyPvcode(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_PMGSY_PVCODE)));
                    card.setPmgsyHabcode(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_PMGSY_HAB_CODE)));
                    card.setRoadLat(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_LAT)));
                    card.setRoadLong(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_LONG)));
                    card.setImage(decodedByte);
                    card.setRemark(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_IMAGE_REMARK)));
                    card.setPmgsyHabName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PMGSY_HAB_NAME)));
                    card.setPmgsyPvname(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PMGSY_PVNAME)));
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

    public ArrayList<RoadListValue> getPendingSavedHabitation(Integer pmgsy_dcode,Integer pmgsy_bcode, Integer pmgsy_pvcode,Integer pmgsy_hab_code) {

        ArrayList<RoadListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
// cursor = db.rawQuery("select * from "+DBHelper.SAVE_IMAGE_LAT_LONG_TABLE,null);
            cursor = db.query(DBHelper.SAVE_IMAGE_HABITATION_TABLE,
                    new String[]{"*"}, "server_flag = ? and pmgsy_dcode = ? and pmgsy_bcode = ? and pmgsy_pvcode = ? and pmgsy_hab_code = ?", new String[]{"0",String.valueOf(pmgsy_dcode), String.valueOf(pmgsy_bcode), String.valueOf(pmgsy_pvcode), String.valueOf(pmgsy_hab_code)}, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.KEY_IMAGES));
                    byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    RoadListValue card = new RoadListValue();
                    card.setdCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_DCODE)));
                    card.setbCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_BCODE)));
                    card.setPvCode(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_PVCODE)));
                    card.setHabCode(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_HABCODE)));
                    card.setPmgsyDcode(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_PMGSY_DCODE)));
                    card.setPmgsyBcode(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_PMGSY_BCODE)));
                    card.setPmgsyPvcode(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_PMGSY_PVCODE)));
                    card.setPmgsyHabcode(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_PMGSY_HAB_CODE)));
                    card.setRoadLat(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_LAT)));
                    card.setRoadLong(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_LONG)));
                    card.setImage(decodedByte);
                    card.setRemark(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_IMAGE_REMARK)));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            // Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
        }

    public RoadListValue insert_newPMGSYImages(RoadListValue pmgsyImageValue) {


        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_DCODE, pmgsyImageValue.getdCode());
        values.put(AppConstant.KEY_BCODE, pmgsyImageValue.getbCode());
        values.put(AppConstant.KEY_PVCODE,pmgsyImageValue.getPvCode() );
        values.put(AppConstant.KEY_HABCODE,pmgsyImageValue.getHabCode() );
        values.put(AppConstant.KEY_PMGSY_DCODE,pmgsyImageValue.getPmgsyDcode() );
        values.put(AppConstant.KEY_PMGSY_BCODE,pmgsyImageValue.getPmgsyBcode() );
        values.put(AppConstant.KEY_PMGSY_PVCODE,pmgsyImageValue.getPmgsyPvcode() );
        values.put(AppConstant.KEY_PMGSY_HAB_CODE,pmgsyImageValue.getPmgsyHabcode() );
        values.put(AppConstant.KEY_ROAD_LAT, pmgsyImageValue.getRoadLat());
        values.put(AppConstant.KEY_ROAD_LONG, pmgsyImageValue.getRoadLong());
        values.put(AppConstant.KEY_IMAGE_REMARK,pmgsyImageValue.getRemark());
        values.put("server_flag",pmgsyImageValue.getServerFlag());

        Bitmap bitmap = pmgsyImageValue.getImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageInByte = baos.toByteArray();
        String image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

        values.put(AppConstant.KEY_IMAGES,image_str);


        long id = db.insert(DBHelper.SAVE_IMAGE_HABITATION_TABLE,null,values);
        Log.d("Inserted_id_HabImage",String.valueOf(id));
        return pmgsyImageValue;
    }

    public ArrayList<RoadListValue> selectImage_Habitation(String hab_code,String server_flag) {

        ArrayList<RoadListValue> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = "pmgsy_hab_code = ? and server_flag = ?";
        String[] selectionArgs = new String[]{hab_code,server_flag};

        try {
            //  cursor = db.rawQuery("select * from "+DBHelper.SAVE_IMAGE_LAT_LONG_TABLE,null);
            cursor = db.query(DBHelper.SAVE_IMAGE_HABITATION_TABLE,
                    new String[]{"*"}, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.KEY_IMAGES));
                    byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    RoadListValue card = new RoadListValue();
                    card.setPmgsyDcode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PMGSY_DCODE)));
                    card.setPmgsyBcode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PMGSY_BCODE)));
                    card.setPmgsyHabcode(cursor.getInt(cursor
                            .getColumnIndex(AppConstant.KEY_PMGSY_HAB_CODE)));
                    card.setRemark(cursor.getString(cursor
                            .getColumnIndex(AppConstant.KEY_IMAGE_REMARK)));
                    card.setImage(decodedByte);

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

    public void deleteImageHabitationTable() {
        db.execSQL("delete from "+ DBHelper.SAVE_IMAGE_HABITATION_TABLE);
    }
    public void deletePmgsyHabitationTable() {
        db.execSQL("delete from "+ DBHelper.PMGSY_HABITATION_LIST_TABLE);
    }

    /********************** BRIDGES AND CULVERT ****************************/

    public RoadListValue insert_newBridges(RoadListValue bridgesValue) {


        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_DATA_TYPE, bridgesValue.getDataType());
        values.put(AppConstant.KEY_LOCATION_GROUP, bridgesValue.getLocGroup());
        values.put(AppConstant.KEY_LOCATION_ID, bridgesValue.getLocID());
        values.put(AppConstant.KEY_DCODE, bridgesValue.getdCode());
        values.put(AppConstant.KEY_BCODE, bridgesValue.getbCode());
        values.put(AppConstant.KEY_PVCODE,bridgesValue.getPvCode() );
        values.put(AppConstant.KEY_ROAD_ID,bridgesValue.getRoadID() );
        values.put(AppConstant.KEY_CULVERT_TYPE,bridgesValue.getCulvertType() );
        values.put(AppConstant.KEY_CULVERT_TYPE_NAME,bridgesValue.getCulvertTypeName() );
        values.put(AppConstant.KEY_CHAINAGE,bridgesValue.getChainage() );
        values.put(AppConstant.KEY_CULVERT_NAME,bridgesValue.getCulvertName() );
        values.put(AppConstant.KEY_SPAN,bridgesValue.getSpan() );
        values.put(AppConstant.KEY_NO_OF_SPAN, bridgesValue.getNoOfSpan());
        values.put(AppConstant.KEY_WIDTH, bridgesValue.getWidth());
        values.put(AppConstant.KEY_VENT_HEIGHT,bridgesValue.getVentHeight());
        values.put(AppConstant.KEY_LENGTH,bridgesValue.getLength());
        values.put(AppConstant.KEY_CULVERT_ID,bridgesValue.getCulvertId());
        values.put(AppConstant.KEY_START_LAT,bridgesValue.getStartLat());
        values.put(AppConstant.KEY_START_LONG,bridgesValue.getStartLong());
        values.put(AppConstant.KEY_IMAGE_AVAILABLE,bridgesValue.getImageAvailable());
        values.put(AppConstant.KEY_IMAGE_FLAG,"1");

//        Bitmap bitmap = bridgesValue.getImage();
//        if(bitmap != null){
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
//            byte[] imageInByte = baos.toByteArray();
//            String image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);
//
//            values.put(AppConstant.KEY_ONLINE_IMAGES,image_str.trim());
//            values.put(AppConstant.KEY_IMAGE_FLAG,"1");
//        }else {
//            values.put(AppConstant.KEY_ONLINE_IMAGES,"");
//            values.put(AppConstant.KEY_IMAGE_FLAG,"");
//        }

        values.put(AppConstant.KEY_SERVER_FLAG,bridgesValue.getServerFlag());

        long id = db.insert(DBHelper.BRIDGES_CULVERT,null,values);
        Log.d("Inserted_id_Bridges",String.valueOf(id));
        return bridgesValue;
    }

    public ArrayList<RoadListValue> getAllBridges(String flagCode,String purpose) {

        ArrayList<RoadListValue> bridges = new ArrayList<>();
        Cursor cursor = null;
        String selection = "server_flag = ?";
        if(purpose.equalsIgnoreCase("upload")){
            selection = "image_flag = ?";
        }

        try {
            cursor = db.query(DBHelper.BRIDGES_CULVERT,
                    new String[]{"*"},selection , new String[]{flagCode}, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RoadListValue bridge = new RoadListValue();
                    bridge.setDataType(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_DATA_TYPE)));
                    bridge.setLocGroup(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LOCATION_GROUP)));
                    bridge.setLocID(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LOCATION_ID)));
                    bridge.setdCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_DCODE)));
                    bridge.setbCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_BCODE)));
                    bridge.setPvCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PVCODE)));
                    bridge.setRoadID(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_ID)));
                    bridge.setCulvertType(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_TYPE)));
                    bridge.setCulvertTypeName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_TYPE_NAME)));
                    bridge.setChainage(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CHAINAGE)));
                    bridge.setCulvertName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_NAME)));
                    bridge.setSpan(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_SPAN)));
                    bridge.setNoOfSpan(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_NO_OF_SPAN)));
                    bridge.setWidth(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_WIDTH)));
                    bridge.setVentHeight(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_VENT_HEIGHT)));
                    bridge.setLength(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LENGTH)));
                    bridge.setCulvertId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_ID)));
                    bridge.setStartLat(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_START_LAT)));
                    bridge.setStartLong(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_START_LONG)));
                    if(purpose.equalsIgnoreCase("upload"))
                    {
                        byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.KEY_IMAGES));
                        byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        bridge.setImage(decodedByte);
                    }


                    bridges.add(bridge);
                }
            }
        } catch (Exception e){
            Log.d( "Exception", String.valueOf(e));
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return bridges;
    }

    public ArrayList<RoadListValue> getParticularBridgesInfo(String road_id,String flagCode,String purpose) {

        ArrayList<RoadListValue> bridges = new ArrayList<>();
        Cursor cursor = null;
        String selection = "server_flag = ?";
        if(purpose.equalsIgnoreCase("upload")){
            selection = "image_flag = ? and road_id = ?";
        }

        try {
            cursor = db.query(DBHelper.BRIDGES_CULVERT,
                    new String[]{"*"},selection , new String[]{flagCode,road_id}, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RoadListValue bridge = new RoadListValue();
                    bridge.setDataType(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_DATA_TYPE)));
                    bridge.setLocGroup(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LOCATION_GROUP)));
                    bridge.setLocID(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LOCATION_ID)));
                    bridge.setdCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_DCODE)));
                    bridge.setbCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_BCODE)));
                    bridge.setPvCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PVCODE)));
                    bridge.setRoadID(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_ID)));
                    bridge.setCulvertType(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_TYPE)));
                    bridge.setCulvertTypeName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_TYPE_NAME)));
                    bridge.setChainage(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CHAINAGE)));
                    bridge.setCulvertName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_NAME)));
                    bridge.setSpan(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_SPAN)));
                    bridge.setNoOfSpan(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_NO_OF_SPAN)));
                    bridge.setWidth(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_WIDTH)));
                    bridge.setVentHeight(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_VENT_HEIGHT)));
                    bridge.setLength(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LENGTH)));
                    bridge.setCulvertId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_ID)));
                    bridge.setStartLat(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_START_LAT)));
                    bridge.setStartLong(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_START_LONG)));
                    if(purpose.equalsIgnoreCase("upload"))
                    {
                        byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.KEY_IMAGES));
                        byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        bridge.setImage(decodedByte);
                    }


                    bridges.add(bridge);
                }
            }
        } catch (Exception e){
            Log.d( "Exception", String.valueOf(e));
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return bridges;
    }

    public ArrayList<RoadListValue> selectBridges(JSONObject code, String type) {

        ArrayList<RoadListValue> bridges = new ArrayList<>();
        Cursor cursor = null;
        String condition = "";
        String road_id ="";
        String loc_grp ="";
        String loc ="";

        String[] columns = new String[0];
        String selection = null;
        String[] selectionArgs = new String[0];

        if (type.equalsIgnoreCase("firstLevel"))
        {
            try {
                road_id = String.valueOf(code.get(AppConstant.KEY_ROAD_ID));
                columns = new String[]{"distinct road_id,loc_grp"};
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
                columns = new String[]{"distinct road_id,loc_grp,loc,culvert_type,culvert_type_name"};
                selection = "road_id = ? and loc_grp = ?";
                selectionArgs = new String[]{road_id,loc_grp};
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(type.equalsIgnoreCase("thirdLevel")) {
            try {
                road_id = String.valueOf(code.get(AppConstant.KEY_ROAD_ID));
                loc_grp = String.valueOf(code.get(AppConstant.KEY_LOCATION_GROUP));
                loc = String.valueOf(code.get(AppConstant.KEY_LOCATION_ID));
                columns = new String[]{"*"};
                selection = "road_id = ? and loc_grp = ? and loc = ?";
                selectionArgs = new String[]{road_id,loc_grp,loc};
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            cursor = db.query(DBHelper.BRIDGES_CULVERT,
                    columns, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RoadListValue bridge = new RoadListValue();
                    bridge.setRoadID(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_ID)));
                    Integer locgrp = cursor.getInt(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_LOCATION_GROUP));
                    bridge.setLocGroup(locgrp);

                    if(locgrp == 7){
                        bridge.setGroupName("Bridges");
                    }else if(locgrp == 8){
                        bridge.setGroupName("Culverts");
                    }

                    bridge.setLevelType(type);

                    if (type.equalsIgnoreCase("secondLevel")) {

                        bridge.setCulvertType(cursor.getInt(cursor
                                .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_TYPE)));
                        bridge.setCulvertTypeName(cursor.getString(cursor
                                .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_TYPE_NAME)));
                        bridge.setLocID(cursor.getInt(cursor
                                .getColumnIndexOrThrow(AppConstant.KEY_LOCATION_ID)));
                    }else
                        if(type.equalsIgnoreCase("thirdLevel")){

                            bridge.setDataType(cursor.getString(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_DATA_TYPE)));
                            bridge.setLocGroup(cursor.getInt(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_LOCATION_GROUP)));
                            bridge.setRoadID(cursor.getInt(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_ROAD_ID)));
                            bridge.setLocID(cursor.getInt(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_LOCATION_ID)));
                            bridge.setdCode(cursor.getInt(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_DCODE)));
                            bridge.setbCode(cursor.getInt(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_BCODE)));
                            bridge.setPvCode(cursor.getInt(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_PVCODE)));
                            bridge.setCulvertType(cursor.getInt(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_TYPE)));
                            bridge.setCulvertTypeName(cursor.getString(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_TYPE_NAME)));
                            bridge.setChainage(cursor.getInt(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_CHAINAGE)));
                            bridge.setCulvertName(cursor.getString(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_NAME)));
                            bridge.setSpan(cursor.getInt(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_SPAN)));
                            bridge.setNoOfSpan(cursor.getInt(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_NO_OF_SPAN)));
                            bridge.setWidth(cursor.getInt(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_WIDTH)));
                            bridge.setVentHeight(cursor.getInt(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_VENT_HEIGHT)));
                            bridge.setLength(cursor.getInt(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_LENGTH)));
                            bridge.setCulvertId(cursor.getInt(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_ID)));
                            bridge.setStartLat(cursor.getString(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_START_LAT)));
                            bridge.setStartLong(cursor.getString(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_START_LONG)));
                            bridge.setImageAvailable(cursor.getString(cursor
                                    .getColumnIndexOrThrow(AppConstant.KEY_IMAGE_AVAILABLE)));
                    }

                    bridges.add(bridge);
                }
            }
        } catch (Exception e){
            Log.d( "Exception", String.valueOf(e));
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return bridges;
    }


    public ArrayList<RoadListValue> selectBridgeImage(String road_id,String culvert_id,String image_flag ) {

        ArrayList<RoadListValue> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = "road_id = ? and culvert_id = ? and image_flag = ? ";
        String[] selectionArgs = new String[]{road_id,culvert_id,image_flag};

        try {
            //  cursor = db.rawQuery("select * from "+DBHelper.SAVE_IMAGE_LAT_LONG_TABLE,null);
            cursor = db.query(DBHelper.BRIDGES_CULVERT,
                    new String[]{"*"}, selection, selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    Bitmap decodedByte = null;

                  //  byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.KEY_IMAGES));
                    if(image_flag.equalsIgnoreCase("0")){
                        byte[] decodedString = Base64.decode(cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.KEY_IMAGES)), Base64.DEFAULT);
                        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    }
                    else if(image_flag.equalsIgnoreCase("1")) {
                        byte[] decodedString = Base64.decode(cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.KEY_ONLINE_IMAGES)), Base64.DEFAULT);
                        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    }


                    RoadListValue bridge = new RoadListValue();

                    bridge.setDataType(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_DATA_TYPE)));
                    bridge.setLocID(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LOCATION_ID)));
                    bridge.setdCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_DCODE)));
                    bridge.setbCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_BCODE)));
                    bridge.setPvCode(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PVCODE)));
                    bridge.setCulvertType(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_TYPE)));
                    bridge.setCulvertTypeName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_TYPE_NAME)));
                    bridge.setChainage(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CHAINAGE)));
                    bridge.setCulvertName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_NAME)));
                    bridge.setSpan(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_SPAN)));
                    bridge.setNoOfSpan(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_NO_OF_SPAN)));
                    bridge.setWidth(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_WIDTH)));
                    bridge.setVentHeight(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_VENT_HEIGHT)));
                    bridge.setLength(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LENGTH)));
                    bridge.setCulvertId(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CULVERT_ID)));
                    bridge.setStartLat(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_START_LAT)));
                    bridge.setStartLong(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_START_LONG)));
                    bridge.setImage(decodedByte);

                    cards.add(bridge);
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

    public void deleteBridgesTable() {
        db.execSQL("delete from "+ DBHelper.BRIDGES_CULVERT);
    }

    public ArrayList<RoadListValue> getPendingList() {

        ArrayList<RoadListValue> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select distinct b.* from (SELECT road_id FROM LatLongTable\n" +
                    "UNION\n" +
                    "SELECT road_id FROM ImageLatLongTable\n" +
                    "UNION\n" +
                    "SELECT road_id FROM BridgesAndCulvert where image_flag = 0) a inner join (select * from RoadList) b on a.road_id = b.road_id",null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    RoadListValue card = new RoadListValue();
                    card.setRoadID(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ROAD_ID)));
                    card.setRoadCode(cursor.getString(cursor
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

    public void refreshTable(){
        db.execSQL("delete from "+ DBHelper.ASSET_LIST_TABLE);
        db.execSQL("delete from "+ DBHelper.ROAD_LIST_TABLE);
        db.execSQL("delete from "+ DBHelper.PMGSY_VILLAGE_LIST_TABLE);
        db.execSQL("delete from "+ DBHelper.PMGSY_HABITATION_LIST_TABLE);
    }

    /************** DELETE ALL TABLE *************/
    public  void deleteAll(){
        db.execSQL("delete from "+ DBHelper.ASSET_LIST_TABLE);
        db.execSQL("delete from "+ DBHelper.ROAD_LIST_TABLE);
        db.execSQL("delete from "+ DBHelper.PMGSY_VILLAGE_LIST_TABLE);
        db.execSQL("delete from "+ DBHelper.PMGSY_HABITATION_LIST_TABLE);
        db.execSQL("delete from "+ DBHelper.BRIDGES_CULVERT +" WHERE image_flag = 1 ");
        db.execSQL("delete from "+ DBHelper.SAVE_IMAGE_HABITATION_TABLE +" WHERE server_flag = 1 ");
    }

}
