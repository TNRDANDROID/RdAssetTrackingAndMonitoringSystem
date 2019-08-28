package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.nic.RdAssetTrackingAndMonitoringSystem.Adapter.PendingScreenAdapter;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.Api;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ApiService;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ServerResponse;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.DBHelper;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.ProgressHUD;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.UrlGenerator;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PendingScreen extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener {

    private RecyclerView pendingRecycler;
    public dbData dbData = new dbData(this);
    ArrayList<RoadListValue> pendingList = new ArrayList<>();
    private PendingScreenAdapter pendingScreenAdapter;
    private ImageView back_img, home_img;
    private LinearLayout sync_pmgsy_data;
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private ProgressHUD progressHUD;
    Handler myHandler = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_screen);
        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        pendingRecycler = (RecyclerView) findViewById(R.id.road_list);
        back_img = (ImageView) findViewById(R.id.back_img);
        home_img = (ImageView) findViewById(R.id.home_img);
        sync_pmgsy_data = (LinearLayout) findViewById(R.id.sync_pmgsy_data);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        pendingRecycler.setLayoutManager(mLayoutManager);
        pendingRecycler.setItemAnimator(new DefaultItemAnimator());
        pendingRecycler.setHasFixedSize(true);
        pendingRecycler.setNestedScrollingEnabled(false);
        pendingRecycler.setFocusable(false);
        pendingScreenAdapter = new PendingScreenAdapter(this, pendingList, dbData);
        pendingRecycler.setAdapter(pendingScreenAdapter);

        back_img.setOnClickListener(this);
        home_img.setOnClickListener(this);
        sync_pmgsy_data.setOnClickListener(this);
        pendingPmgsyVisibility();
        new fetchpendingtask().execute();
//        pmgsy_village_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
////                    sp_village.setClickable(false);
////                    sp_village.setVisibility(View.GONE);
//                } else {
//                  //  loadHabitationRecycler(pmgsyVillageList.get(position).getPmgsyHabcode());
//                    JSONObject jsonObject = new JSONObject();
//                    try {
//                        jsonObject.put(AppConstant.KEY_PMGSY_DCODE,pmgsyVillageList.get(position).getPmgsyDcode());
//                        jsonObject.put(AppConstant.KEY_PMGSY_BCODE,pmgsyVillageList.get(position).getPmgsyBcode());
//                        jsonObject.put(AppConstant.KEY_PMGSY_PVCODE,pmgsyVillageList.get(position).getPmgsyPvcode());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    new fetchHabitationtask().execute(jsonObject);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

       // loadVillageSpinner();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                onBackPress();
                break;
            case R.id.home_img:
                dashboard();
                break;
            case R.id.sync_pmgsy_data:
                PMGSYPendingScreen();
                break;
        }
    }

    public void pendingPmgsyVisibility() {
        dbData.open();
        ArrayList<RoadListValue> habitationCount = dbData.getSavedHabitation("0");
        if ((habitationCount.size() > 0)) {
            sync_pmgsy_data.setAlpha(0);
            final Runnable pmgsy = new Runnable() {
                @Override
                public void run() {
                    sync_pmgsy_data.setAlpha(1);
                    sync_pmgsy_data.startAnimation(AnimationUtils.loadAnimation(PendingScreen.this, R.anim.text_view_move_right));

                }
            };
            myHandler.postDelayed(pmgsy, 800);

            sync_pmgsy_data.setVisibility(View.VISIBLE);
        } else {
            sync_pmgsy_data.setVisibility(View.GONE);
        }
    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void dashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Home", "Home");
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void PMGSYPendingScreen() {
        Intent intent = new Intent(this, PMGSYPendingScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public class fetchpendingtask extends AsyncTask<JSONObject, Void,
            ArrayList<RoadListValue>> {
        @Override
        protected ArrayList<RoadListValue> doInBackground(JSONObject... params) {
            dbData.open();
            pendingList = new ArrayList<>();
            pendingList = dbData.getPendingList();
            return pendingList;
        }

        @Override
        protected void onPostExecute(ArrayList<RoadListValue> pendingList) {
            super.onPostExecute(pendingList);
            pendingScreenAdapter = new PendingScreenAdapter(PendingScreen.this,
                    pendingList, dbData);
            pendingRecycler.setAdapter(pendingScreenAdapter);
        }
    }

//    public void loadVillageSpinner() {
//
//        dbData.open();
//        ArrayList<RoadListValue> PMGSY_Village = dbData.getAll_PMGSYVillage();
//
//            RoadListValue roadListValue = new RoadListValue();
//            roadListValue.setPmgsyPvname("Select Village");
//            pmgsyVillageList.add(roadListValue);
//
//            for (int i=0;i<PMGSY_Village.size();i++){
//                RoadListValue villageList = new RoadListValue();
//
//                Integer pmgsydcode = PMGSY_Village.get(i).getPmgsyDcode();
//                Integer pmgsybcode = PMGSY_Village.get(i).getPmgsyBcode();
//                Integer pmgsypvcode = PMGSY_Village.get(i).getPmgsyPvcode();
//                String pmgsypvname = PMGSY_Village.get(i).getPmgsyPvname();
//
//
//                villageList.setPmgsyDcode(pmgsydcode);
//                villageList.setPmgsyBcode(pmgsybcode);
//                villageList.setPmgsyPvcode(pmgsypvcode);
//                villageList.setPmgsyPvname(pmgsypvname);
//                pmgsyVillageList.add(villageList);
//            }
//
//        pmgsy_village_sp.setAdapter(new CommonAdapter(this, pmgsyVillageList, "PMGSYVillage"));
//    }

    public void getRoadList(){
        try {
            new ApiService(this).makeJSONObjectRequest("RoadList", Api.Method.POST, UrlGenerator.getRoadListUrl(), roadListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAssetList(){
        try {
            new ApiService(this).makeJSONObjectRequest("AssetList", Api.Method.POST, UrlGenerator.getRoadListUrl(), assetListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getBridges(){
        try {
            new ApiService(this).makeJSONObjectRequest("Bridges", Api.Method.POST, UrlGenerator.getRoadListUrl(), bridgesJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject roadListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.roadListJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("roadlist", "" + authKey);
        return dataSet;
    }

    public JSONObject assetListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.assetListJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("assetlist", "" + authKey);
        return dataSet;
    }

    public JSONObject bridgesJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.bridgesListJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("Bridges", "" + authKey);
        return dataSet;
    }


    public JSONObject syncData_Track(JSONObject dataset) {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(),getResources().getString(R.string.init_vector),dataset.toString());
        JSONObject savedDataSet = new JSONObject();
        try {
            savedDataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            savedDataSet.put(AppConstant.DATA_CONTENT, authKey);

            new ApiService(this).makeJSONObjectRequest("saveLatLongList", Api.Method.POST, UrlGenerator.getRoadListUrl(), savedDataSet, "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return savedDataSet;
    }

    public JSONObject syncData_Asset(JSONObject dataset) {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(),getResources().getString(R.string.init_vector),dataset.toString());
        JSONObject savedDataSet = new JSONObject();
        try {
            savedDataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            savedDataSet.put(AppConstant.DATA_CONTENT, authKey);

            new ApiService(this).makeJSONObjectRequest("save_dataAsset", Api.Method.POST, UrlGenerator.getRoadListUrl(), savedDataSet, "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return savedDataSet;
    }

    public JSONObject syncDataBridges(JSONObject dataset) {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(),getResources().getString(R.string.init_vector),dataset.toString());
        JSONObject savedDataSet = new JSONObject();
        try {
            savedDataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            savedDataSet.put(AppConstant.DATA_CONTENT, authKey);

            new ApiService(this).makeJSONObjectRequest("saveBridgesList", Api.Method.POST, UrlGenerator.getRoadListUrl(), savedDataSet, "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return savedDataSet;
    }



    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            Runtime.getRuntime().gc();
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();
            if ("RoadList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertRoadListTask().execute(jsonObject);
                }
                Log.d("response_RoadList", "" + responseDecryptedBlockKey);
            }
            if ("AssetList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertAssetListTask().execute(jsonObject);
                }
                Log.d("response_AssetList", "" + responseDecryptedBlockKey);
            }

            if ("Bridges".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertBridgesTask().execute(jsonObject);
                }
                Log.d("resp_bridges", "" + responseDecryptedBlockKey);
            }
            if ("save_dataAsset".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    dbData.open();
                    dbData.deleteRoadListTable();
                    dbData.deleteAssetTable();
                    db.delete(DBHelper.SAVE_IMAGE_LAT_LONG_TABLE,"road_id=?",new String[] {prefManager.getKeyDeleteId()});
                    pendingScreenAdapter.itemRemove(Integer.valueOf(prefManager.getKeyDeleteId()),Integer.valueOf(prefManager.getKeyClickedPosition()));
                    pendingScreenAdapter.notifyDataSetChanged();
                    getAssetList();
                    getRoadList();
                    Utils.showAlert(this,"Synchronized Asset Data to the server");
                }
                else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("ERROR")) {
                    db.delete(DBHelper.SAVE_IMAGE_LAT_LONG_TABLE,"road_id=?",new String[] {prefManager.getKeyDeleteId()});
                    pendingScreenAdapter.itemRemove(Integer.valueOf(prefManager.getKeyDeleteId()),Integer.valueOf(prefManager.getKeyClickedPosition()));
                    pendingScreenAdapter.notifyDataSetChanged();
                    Utils.showAlert(this, jsonObject.getString("MESSAGE"));
                }
                Log.d("saved_Asset", "" + responseDecryptedBlockKey);
            }
            if ("saveLatLongList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    dbData.open();
                    db.delete(DBHelper.SAVE_LAT_LONG_TABLE,"road_id=?",new String[] {prefManager.getKeyDeleteId()});
                    pendingScreenAdapter.itemRemove(Integer.valueOf(prefManager.getKeyDeleteId()),Integer.valueOf(prefManager.getKeyClickedPosition()));
                    pendingScreenAdapter.notifyDataSetChanged();
                    dbData.deleteRoadListTable();
                    getRoadList();
                    // getAssetList();
                    Utils.showAlert(this, "Synchronized Track Data to the server");
                }else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("ERROR")) {
                    dbData.open();
                    db.delete(DBHelper.SAVE_LAT_LONG_TABLE,"road_id=?",new String[] {prefManager.getKeyDeleteId()});
                    pendingScreenAdapter.itemRemove(Integer.valueOf(prefManager.getKeyDeleteId()),Integer.valueOf(prefManager.getKeyClickedPosition()));
                    pendingScreenAdapter.notifyDataSetChanged();
                    Utils.showAlert(this, jsonObject.getString("MESSAGE"));
                }
                Log.d("saved_Track", "" + responseDecryptedBlockKey);
            }
            if ("saveBridgesList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, "Synchronized Bridges and Culverts Data to the server");

                    JSONArray jsonArray = prefManager.getLocalSaveCulvertIdJson();
                    for(int i = 0;i < jsonArray.length();i++ ){
                        ContentValues values = new ContentValues();
                        values.put("image_flag",1);
                        values.put("image_available","Y");
                        long id = db.update(DBHelper.BRIDGES_CULVERT,values,"road_id=? and image_flag = ? and culvert_id = ?",new String[] {prefManager.getKeyDeleteId(),"0", String.valueOf(jsonArray.get(i))});
                        Log.d("culvert_id",String.valueOf(jsonArray.get(i)));
                    }
                    //long id = db.update(DBHelper.BRIDGES_CULVERT,values,"road_id=? and image_flag = ?",new String[] {prefManager.getKeyDeleteId(),"0"});
                    pendingScreenAdapter.itemRemove(Integer.valueOf(prefManager.getKeyDeleteId()),Integer.valueOf(prefManager.getKeyClickedPosition()));
                    pendingScreenAdapter.notifyDataSetChanged();

                }
                else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("ERROR")) {
                    ContentValues values = new ContentValues();
                    values.put("image_flag",0);
                    long id = db.update(DBHelper.BRIDGES_CULVERT,values,"road_id=? and image_flag = ?",new String[] {prefManager.getKeyDeleteId(),"0"});
                    pendingScreenAdapter.itemRemove(Integer.valueOf(prefManager.getKeyDeleteId()),Integer.valueOf(prefManager.getKeyClickedPosition()));
                    pendingScreenAdapter.notifyDataSetChanged();
                    Utils.showAlert(this, jsonObject.getString("MESSAGE"));
                }
                Log.d("saveBridgesList", "" + responseDecryptedBlockKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class InsertRoadListTask extends AsyncTask<JSONObject ,Void ,Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<RoadListValue> roadlist_count = dbData.getAll_Road("0");
            if (roadlist_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        RoadListValue roadListValue = new RoadListValue();
                        try {
                            roadListValue.setRoadID(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_ROAD_ID));
                            roadListValue.setRoadCode(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ROAD_CODE));
                            roadListValue.setRoadCategoryCode(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_ROAD_CATEGORY_CODE));
                            roadListValue.setRoadCategory(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ROAD_CATEGORY));
                            roadListValue.setRoadName(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ROAD_NAME));
                            roadListValue.setRoadVillage(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ROAD_VILLAGE_NAME));
                            roadListValue.setTotalAsset(Integer.valueOf(jsonArray.getJSONObject(i).getString(AppConstant.KEY_TOTAL_ASSET)));
                            roadListValue.setAssetCapturedCount(Integer.valueOf(jsonArray.getJSONObject(i).getString(AppConstant.KEY_ASSET_CAPTURED_COUNT)));
                            roadListValue.setTotalStartPoint(Integer.valueOf(jsonArray.getJSONObject(i).getString(AppConstant.KEY_TOTAL_START_POINT)));
                            roadListValue.setTotalMidPoint(Integer.valueOf(jsonArray.getJSONObject(i).getString(AppConstant.KEY_TOTAL_MID_POINT)));
                            roadListValue.setTotalEndPoint(Integer.valueOf(jsonArray.getJSONObject(i).getString(AppConstant.KEY_TOTAL_END_POINT)));
                            dbData.create_newRoad(roadListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }
    }

    public class InsertAssetListTask extends AsyncTask<JSONObject ,Void ,Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<RoadListValue> assetlist_count = dbData.getAll_Asset();
            if (assetlist_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        RoadListValue assetListValue = new RoadListValue();
                        try {
                            assetListValue.setRoadID(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_ROAD_ID));
                            assetListValue.setLocGroup(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_LOCATION_GROUP));
                            assetListValue.setLocID(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_LOCATION_ID));
                            assetListValue.setGroupName(jsonArray.getJSONObject(i).getString(AppConstant.KEY_GROUP_NAME));
                            assetListValue.setSubgroupName(jsonArray.getJSONObject(i).getString(AppConstant.KEY_SUB_GROUP_NAME));
                            assetListValue.setColLabel(jsonArray.getJSONObject(i).getString(AppConstant.KEY_COLUMN_LABEL));
                            assetListValue.setLocationDetails(jsonArray.getJSONObject(i).getString(AppConstant.KEY_LOCATION_DETAILS));
                            dbData.create_newAsset(assetListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }
    }

    public class InsertBridgesTask extends AsyncTask<JSONObject ,Void ,Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<RoadListValue> bridges_count = dbData.getAllBridges("1","insert");
            if (bridges_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        RoadListValue bridges = new RoadListValue();
                        try {
                            bridges.setDataType(jsonArray.getJSONObject(i).getString(AppConstant.KEY_DATA_TYPE));
                            bridges.setLocGroup(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_LOCATION_GROUP));
                            bridges.setLocID(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_LOCATION_ID));
                            bridges.setdCode(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_DCODE));
                            bridges.setbCode(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_BCODE));
                            bridges.setPvCode(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_PVCODE));
                            bridges.setRoadID(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_ROAD_ID));
                            bridges.setCulvertType(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_CULVERT_TYPE));
                            bridges.setCulvertTypeName(jsonArray.getJSONObject(i).getString(AppConstant.KEY_CULVERT_TYPE_NAME));
                            bridges.setChainage(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_CHAINAGE));
                            bridges.setCulvertName(jsonArray.getJSONObject(i).getString(AppConstant.KEY_CULVERT_NAME));
                            bridges.setSpan(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_SPAN));
                            bridges.setNoOfSpan(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_NO_OF_SPAN));
                            bridges.setWidth(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_WIDTH));
                            bridges.setVentHeight(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_VENT_HEIGHT));
                            bridges.setLength(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_LENGTH));
                            bridges.setCulvertId(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_CULVERT_ID));
                            bridges.setStartLat(jsonArray.getJSONObject(i).getString(AppConstant.KEY_START_LAT));
                            bridges.setStartLong(jsonArray.getJSONObject(i).getString(AppConstant.KEY_START_LONG));
                            bridges.setImageAvailable(jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE_AVAILABLE));
                            bridges.setServerFlag("1");

                            dbData.insert_newBridges(bridges);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressHUD = ProgressHUD.show(Dashboard.this, "Downloading", true, false, null);
//        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressHUD != null) {
                progressHUD.cancel();
            }

        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        pendingPmgsyVisibility();
    }
}
