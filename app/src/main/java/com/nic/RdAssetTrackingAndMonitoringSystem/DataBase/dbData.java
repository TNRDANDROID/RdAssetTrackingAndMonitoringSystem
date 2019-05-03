package com.nic.RdAssetTrackingAndMonitoringSystem.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;

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

    public RoadListValue create(RoadListValue roadListValue) {

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

    public ArrayList<RoadListValue> getAll(String code1) {

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


}
