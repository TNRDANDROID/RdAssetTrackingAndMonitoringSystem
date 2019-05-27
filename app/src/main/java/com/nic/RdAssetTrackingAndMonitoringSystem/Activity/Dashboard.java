package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.nic.RdAssetTrackingAndMonitoringSystem.Adapter.RoadListAdapter;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.Api;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ApiService;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ServerResponse;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Dialog.MyDialog;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.ProgressHUD;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.UrlGenerator;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements View.OnClickListener, MyDialog.myOnClickListener, Api.ServerResponseListener {

    private MyCustomTextView on_road_tv, district_tv, block_tv , sync;
    private LinearLayout district_user_layout, block_user_layout,pmgsy_layout;
    private ImageView logout_tv;
    Handler myHandler = new Handler();
    private PrefManager prefManager;
    public dbData dbData = new dbData(this);
    RelativeLayout vpr_layout,pur_layout,vpr_pur_layout,highway_layout;
    RoadListAdapter roadListAdapter;
    RecyclerView recyclerView;
    JSONObject datasetAsset = new JSONObject();
    JSONObject datasetTrack = new JSONObject();
    JSONObject datasetHabitation = new JSONObject();
    private ProgressHUD progressHUD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        intializeUI();

    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        district_user_layout = (LinearLayout) findViewById(R.id.district_user_layout);
        block_user_layout = (LinearLayout) findViewById(R.id.block_user_layout);
        pmgsy_layout = (LinearLayout) findViewById(R.id.pmgsy_layout);
        district_tv = (MyCustomTextView) findViewById(R.id.district_tv);
        block_tv = (MyCustomTextView) findViewById(R.id.block_tv);
        on_road_tv = (MyCustomTextView) findViewById(R.id.on_road_tv);
        logout_tv = (ImageView) findViewById(R.id.logout_tv);
        vpr_layout = (RelativeLayout) findViewById(R.id.VPR_Layout);
        pur_layout= (RelativeLayout) findViewById(R.id.PUR_Layout);
        vpr_pur_layout= (RelativeLayout) findViewById(R.id.VPR_PUR_Layout);
        highway_layout= (RelativeLayout) findViewById(R.id.Highway_Road_layout);
        sync = (MyCustomTextView) findViewById(R.id.sync);

        vpr_layout.setOnClickListener(this);
        pur_layout.setOnClickListener(this);
        vpr_pur_layout.setOnClickListener(this);
        highway_layout.setOnClickListener(this);
        sync.setOnClickListener(this);
        pmgsy_layout.setOnClickListener(this);

        on_road_tv.setAlpha(0);
        final Runnable onroad = new Runnable() {
            @Override
            public void run() {

                on_road_tv.startAnimation(AnimationUtils.loadAnimation(Dashboard.this, R.anim.text_view_move));
                on_road_tv.setAlpha(1);
            }
        };
        myHandler.postDelayed(onroad, 700);
        block_user_layout.setAlpha(0);
        final Runnable block = new Runnable() {
            @Override
            public void run() {
                block_user_layout.setAlpha(1);
                block_user_layout.startAnimation(AnimationUtils.loadAnimation(Dashboard.this, R.anim.text_view_move));

            }
        };
        myHandler.postDelayed(block, 1000);
        district_user_layout.setAlpha(0);
        final Runnable district = new Runnable() {
            @Override
            public void run() {
                district_user_layout.setAlpha(1);
                district_user_layout.startAnimation(AnimationUtils.loadAnimation(Dashboard.this, R.anim.text_view_move));

            }
        };
        myHandler.postDelayed(district, 1200);
        pmgsy_layout.setAlpha(0);
        final Runnable pmgsy = new Runnable() {
            @Override
            public void run() {
                pmgsy_layout.setAlpha(1);
                pmgsy_layout.startAnimation(AnimationUtils.loadAnimation(Dashboard.this, R.anim.text_view_move_right));

            }
        };
        myHandler.postDelayed(pmgsy, 1500);

        district_tv.setText(prefManager.getDistrictName());
        block_tv.setText(prefManager.getBlockName());
        logout_tv.setOnClickListener(this);
        getRoadList();
        getAssetList();
        getPMGSYVillage();
        getPMGSYHabitation();

        syncButtonVisibility();
    }

    public void syncButtonVisibility() {
        dbData.open();
        ArrayList<RoadListValue> assetsCount = dbData.getSavedAsset();
        ArrayList<RoadListValue> trackCount = dbData.getSavedTrack();
        ArrayList<RoadListValue> habitationCount = dbData.getSavedHabitation();

        if (assetsCount.size() > 0 || trackCount.size() > 0 || habitationCount.size() > 0) {
            sync.setVisibility(View.VISIBLE);
        }else {
            sync.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_tv:
                closeApplication();
                break;
            case R.id.VPR_Layout:
                roadlistScreen("2");
                break;
            case R.id.PUR_Layout:
                roadlistScreen("1");
                break;
            case R.id.VPR_PUR_Layout:
                roadlistScreen("4");
                break;
            case R.id.Highway_Road_layout:
                roadlistScreen("3");
                break;
            case R.id.sync:
                toUpload();
                break;
            case R.id.pmgsy_layout:
                pmgsyScreen();
                break;
        }
    }

    public void pmgsyScreen() {
        Intent intent = new Intent(this,PMGSYScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void toUpload() {
        if(Utils.isOnline()) {
            new toUploadAssetTask().execute();
            new toUploadTrackTask().execute();
            new toUploadHabitation().execute();
        }
        else {
            Utils.showAlert(this,"Please Turn on Your Mobile Data to Upload");
        }

    }


    public void roadlistScreen(String code) {
        Intent intent = new Intent(this,RoadListScreen.class);
        intent.putExtra(AppConstant.KEY_ROAD_CATEGORY_CODE,code);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    private void closeApplication() {
        new MyDialog(Dashboard.this).exitDialog(Dashboard.this, "Are you sure you want to Logout?", "Logout");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                new MyDialog(this).exitDialog(this, "Are you sure you want to exit ?", "Exit");
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onButtonClick(AlertDialog alertDialog, String type) {
        alertDialog.dismiss();
        if ("Exit".equalsIgnoreCase(type)) {
            onBackPressed();
        } else {

            Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("EXIT", false);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }

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

    public void getPMGSYVillage(){
        try {
            new ApiService(this).makeJSONObjectRequest("PMGSYVillageList", Api.Method.POST, UrlGenerator.getRoadListUrl(), pmgsyVillageListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getPMGSYHabitation(){
        try {
            new ApiService(this).makeJSONObjectRequest("PMGSYHabitationList", Api.Method.POST, UrlGenerator.getRoadListUrl(), pmgsyHabitationListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getPMGSYImages(){
        try {
            new ApiService(this).makeJSONObjectRequest("PMGSYImages", Api.Method.POST, UrlGenerator.getRoadListUrl(), pmgsyImagesJsonParams(), "not cache", this);
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

    public JSONObject pmgsyVillageListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.pmgsyVillageListJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("pmgsyVillageList", "" + authKey);
        return dataSet;
    }

    public JSONObject pmgsyHabitationListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.pmgsyHabitationListJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("pmgsyHabitationList", "" + authKey);
        return dataSet;
    }

    public JSONObject pmgsyImagesJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.pmgsyImagesListJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("pmgsyImagesList", "" + authKey);
        return dataSet;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
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
            if ("PMGSYVillageList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                  new InsertPMGSYVillageListTask().execute(jsonObject);
                }
                Log.d("response_pmgsyVillage", "" + responseDecryptedBlockKey);
            }
            if ("PMGSYHabitationList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                   new InsertPMGSYHabitationListTask().execute(jsonObject);
                }
                Log.d("resp_pmgsyHabitation", "" + responseDecryptedBlockKey);
            }
            if ("save_dataAsset".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    dbData.open();
                    dbData.deleteRoadListTable();
                    dbData.deleteAssetTable();
                    dbData.update_image();
                    datasetAsset = new JSONObject();
                    getAssetList();
                    getRoadList();
                    Utils.showAlert(this,"Asset Saved");
                    syncButtonVisibility();
                }
                Log.d("saved_Asset", "" + responseDecryptedBlockKey);
            }
            if ("saveLatLongList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    dbData.open();
                    dbData.update_Track();
                    dbData.deleteRoadListTable();
                    datasetTrack = new JSONObject();
                    getRoadList();
                   // getAssetList();
                    Utils.showAlert(this, "Lat Long Saved");
                    syncButtonVisibility();
                }
                Log.d("saved_Track", "" + responseDecryptedBlockKey);
            }
            if ("saveHabitation".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, "PMGSY Habitation Saved");
                    dbData.open();
                    dbData.deleteImageHabitationTable();
                    datasetHabitation = new JSONObject();
                    syncButtonVisibility();
                }
                Log.d("savedHabitation", "" + responseDecryptedBlockKey);
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
                            roadListValue.setRoadCode(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_ROAD_CODE));
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

    public class InsertPMGSYVillageListTask extends AsyncTask<JSONObject ,Void ,Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<RoadListValue> pmgsyVillage_count = dbData.getAll_PMGSYVillage();
            if (pmgsyVillage_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        RoadListValue pmgsyVillage = new RoadListValue();
                        try {
                            pmgsyVillage.setPmgsyDcode(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_PMGSY_DCODE));
                            pmgsyVillage.setPmgsyBcode(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_PMGSY_BCODE));
                            pmgsyVillage.setPmgsyPvcode(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_PMGSY_PVCODE));
                            pmgsyVillage.setPmgsyPvname(jsonArray.getJSONObject(i).getString(AppConstant.KEY_PMGSY_PVNAME));

                            dbData.insert_newPMGSYVillage(pmgsyVillage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }
    }

    public class InsertPMGSYHabitationListTask extends AsyncTask<JSONObject ,Void ,Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<RoadListValue> pmgsyHabitation_count = dbData.getAll_PMGSYHabitation();
            if (pmgsyHabitation_count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        RoadListValue pmgsyHabitation = new RoadListValue();
                        try {
                            pmgsyHabitation.setdCode(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_DCODE));
                            pmgsyHabitation.setbCode(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_BCODE));
                            pmgsyHabitation.setPvCode(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_PVCODE));
                            pmgsyHabitation.setHabCode(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_HABCODE));
                            pmgsyHabitation.setPmgsyDcode(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_PMGSY_DCODE));
                            pmgsyHabitation.setPmgsyBcode(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_PMGSY_BCODE));
                            pmgsyHabitation.setPmgsyPvcode(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_PMGSY_PVCODE));
                            pmgsyHabitation.setPmgsyHabcode(jsonArray.getJSONObject(i).getInt(AppConstant.KEY_PMGSY_HAB_CODE));
                            pmgsyHabitation.setPmgsyHabName(jsonArray.getJSONObject(i).getString(AppConstant.KEY_PMGSY_HAB_NAME));

                            dbData.insert_newPMGSYHabitation(pmgsyHabitation);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHUD = ProgressHUD.show(Dashboard.this, "Downloading", true, false, null);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressHUD != null) {
                progressHUD.cancel();
            }

        }
    }


    public class toUploadAssetTask extends AsyncTask<Void, Void,
            JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... voids) {
            dbData.open();
            JSONArray track_data = new JSONArray();
            ArrayList<RoadListValue> assets = dbData.getSavedAsset();

            if (assets.size() > 0) {
                for (int i = 0; i < assets.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(AppConstant.KEY_ROAD_CATEGORY,assets.get(i).getRoadCategory());
                        jsonObject.put(AppConstant.KEY_ROAD_ID,assets.get(i).getRoadID());
                        jsonObject.put(AppConstant.KEY_ASSET_ID,assets.get(i).getAssetId());
                        jsonObject.put(AppConstant.KEY_ROAD_LAT,assets.get(i).getRoadLat());
                        jsonObject.put(AppConstant.KEY_ROAD_LONG,assets.get(i).getRoadLong());

                        Bitmap bitmap = assets.get(i).getImage();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                        byte[] imageInByte = baos.toByteArray();
                        String image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

                        jsonObject.put(AppConstant.KEY_IMAGES,image_str);

                        track_data.put(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                datasetAsset = new JSONObject();

                try {
                    datasetAsset.put(AppConstant.KEY_SERVICE_ID,AppConstant.KEY_ROAD_TRACK_ASSET_SAVE);
                    datasetAsset.put(AppConstant.KEY_TRACK_DATA,track_data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return datasetAsset;
        }

        @Override
        protected void onPostExecute(JSONObject dataset) {
            super.onPostExecute(dataset);
            syncData_Asset();
        }
    }

    public class toUploadTrackTask extends AsyncTask<Void, Void,
            JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                dbData.open();
                ArrayList<RoadListValue> saveLatLongLists = dbData.getSavedTrack();
                JSONArray saveLatLongArray = new JSONArray();
                if (saveLatLongLists.size() > 0) {
                    for (int i = 0; i < saveLatLongLists.size(); i++) {
                        JSONObject latLongData = new JSONObject();
                        latLongData.put(AppConstant.KEY_ROAD_CATEGORY, saveLatLongLists.get(i).getRoadCategory());
                        latLongData.put(AppConstant.KEY_ROAD_ID, saveLatLongLists.get(i).getRoadID());
                        latLongData.put(AppConstant.KEY_POINT_TYPE, saveLatLongLists.get(i).getPointType());
                        latLongData.put(AppConstant.KEY_ROAD_LAT, saveLatLongLists.get(i).getRoadLat());
                        latLongData.put(AppConstant.KEY_ROAD_LONG, saveLatLongLists.get(i).getRoadLong());
                        latLongData.put(AppConstant.KEY_CREATED_DATE, saveLatLongLists.get(i).getCreatedDate());

                        saveLatLongArray.put(latLongData);
                    }
                }

                datasetTrack = new JSONObject();
                try {
                    datasetTrack.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_ROAD_TRACK_SAVE);
                    datasetTrack.put(AppConstant.KEY_TRACK_DATA, saveLatLongArray);

//                    String authKey = datasetTrack.toString();
//                    int maxLogSize = 2000;
//                    for (int i = 0; i <= authKey.length() / maxLogSize; i++) {
//                        int start = i * maxLogSize;
//                        int end = (i + 1) * maxLogSize;
//                        end = end > authKey.length() ? authKey.length() : end;
//                        Log.v("to_send_plain", authKey.substring(start, end));
//                    }
//
//                    String authKey1 = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), saveLatLongData.toString());
//
//                    for(int i = 0; i <= authKey1.length() / maxLogSize; i++) {
//                        int start = i * maxLogSize;
//                        int end = (i+1) * maxLogSize;
//                        end = end > authKey.length() ? authKey1.length() : end;
//                        Log.v("to_send_encryt", authKey1.substring(start, end));
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return datasetTrack;
        }

        protected void onPostExecute(JSONObject dataset) {
            super.onPostExecute(dataset);
            syncData_Track();
        }
    }

    public class toUploadHabitation extends AsyncTask<Void, Void,
            JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... voids) {
            dbData.open();
            JSONArray habitation = new JSONArray();
            ArrayList<RoadListValue> Habitation = dbData.getSavedHabitation();

            if (Habitation.size() > 0) {
                for (int i = 0; i < Habitation.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(AppConstant.KEY_DCODE, Habitation.get(i).getdCode());
                        jsonObject.put(AppConstant.KEY_BCODE, Habitation.get(i).getbCode());
                        jsonObject.put(AppConstant.KEY_PVCODE, Habitation.get(i).getPvCode());
                        jsonObject.put(AppConstant.KEY_HABCODE, Habitation.get(i).getHabCode());
                        jsonObject.put(AppConstant.KEY_PMGSY_DCODE, Habitation.get(i).getPmgsyDcode());
                        jsonObject.put(AppConstant.KEY_PMGSY_BCODE, Habitation.get(i).getPmgsyBcode());
                        jsonObject.put(AppConstant.KEY_PMGSY_PVCODE, Habitation.get(i).getPmgsyPvcode());
                        jsonObject.put(AppConstant.KEY_PMGSY_HAB_CODE, Habitation.get(i).getPmgsyHabcode());
                        jsonObject.put(AppConstant.KEY_ROAD_LAT, Habitation.get(i).getRoadLat());
                        jsonObject.put(AppConstant.KEY_ROAD_LONG, Habitation.get(i).getRoadLong());

                        Bitmap bitmap = Habitation.get(i).getImage();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                        byte[] imageInByte = baos.toByteArray();
                        String image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

                        jsonObject.put(AppConstant.KEY_IMAGES, image_str);

                        habitation.put(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                datasetHabitation = new JSONObject();

                try {
                    datasetHabitation.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_PMGSY_HABITATION_SAVE);
                    datasetHabitation.put(AppConstant.KEY_TRACK_DATA, habitation);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return datasetHabitation;
        }

        @Override
        protected void onPostExecute(JSONObject dataset) {
            super.onPostExecute(dataset);
            syncHabitation();
        }
    }


    public void syncData_Asset() {
        try {
            new ApiService(this).makeJSONObjectRequest("save_dataAsset", Api.Method.POST, UrlGenerator.getRoadListUrl(), dataTobeSavedJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject dataTobeSavedJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), datasetAsset.toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("saving", "" + authKey);
        return dataSet;
    }

    public void syncData_Track() {
        try {
            new ApiService(this).makeJSONObjectRequest("saveLatLongList", Api.Method.POST, UrlGenerator.getRoadListUrl(), saveLatLongListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject saveLatLongListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), datasetTrack.toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("saveLatLongList", "" + authKey);
        return dataSet;
    }

    public void syncHabitation() {
        try {
            new ApiService(this).makeJSONObjectRequest("saveHabitation", Api.Method.POST, UrlGenerator.getRoadListUrl(), habitaionSavedJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject habitaionSavedJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), datasetHabitation.toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("Habitation", "" + authKey);
        return dataSet;
    }

    @Override
    public void OnError(VolleyError volleyError) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncButtonVisibility();
    }
}
