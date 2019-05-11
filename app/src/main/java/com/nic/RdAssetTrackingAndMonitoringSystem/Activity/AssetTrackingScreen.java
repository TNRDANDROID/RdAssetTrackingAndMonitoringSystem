package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nic.RdAssetTrackingAndMonitoringSystem.Adapter.AssetListAdapter;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.Api;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ApiService;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ServerResponse;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyLocationListener;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.UrlGenerator;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class AssetTrackingScreen extends AppCompatActivity implements LocationListener, View.OnClickListener, Api.ServerResponseListener {
private PrefManager prefManager;
    private ImageView back_img;
    private LinearLayout district_user_layout, block_user_layout;
    private MyCustomTextView district_tv, block_tv, start_lat_long_tv, midd_lat_long_tv, end_lat_long_tv, road_name;
    private Button start_lat_long_click_view, stop_lat_long_click_view, end_lat_long_click_view, save_btn;
    private RecyclerView recyclerView;
    Handler myHandler = new Handler();
    LocationManager mlocManager = null;
    LocationListener mlocListener;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private AssetListAdapter assetListAdapter;
    private ArrayList<RoadListValue> assetLists = new ArrayList<>();
    private ArrayList<RoadListValue> saveLatLongLists = new ArrayList<>();
    public dbData dbData = new dbData(this);
    Double offlatTextValue, offlongTextValue;
    double latitude = 0.0d;
    double longitude = 0.0d;
    private static DecimalFormat df2 = new DecimalFormat("##.##");
    private String pointType;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private JSONObject saveLatLongData;
    private JSONObject latLongData;
    private JSONArray saveLatLongArray;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_layout);
        intializeUI();
    }

    public void intializeUI(){
        prefManager = new PrefManager(this);
        district_user_layout = (LinearLayout) findViewById(R.id.district_user_layout);
        block_user_layout = (LinearLayout) findViewById(R.id.block_user_layout);
        district_tv = (MyCustomTextView) findViewById(R.id.district_tv);
        block_tv = (MyCustomTextView) findViewById(R.id.block_tv);
        start_lat_long_click_view = (Button) findViewById(R.id.start_lat_long_click_view);
        stop_lat_long_click_view = (Button) findViewById(R.id.stop_lat_long_click_view);
        end_lat_long_click_view = (Button) findViewById(R.id.end_lat_long_click_view);
        save_btn = (Button) findViewById(R.id.save_btn);
        start_lat_long_tv = (MyCustomTextView) findViewById(R.id.start_lat_long_tv);
        midd_lat_long_tv = (MyCustomTextView) findViewById(R.id.midd_lat_long_tv);
        end_lat_long_tv = (MyCustomTextView) findViewById(R.id.end_lat_long_tv);
        road_name = (MyCustomTextView)findViewById(R.id.road_name);

        road_name.setText(getIntent().getStringExtra(AppConstant.KEY_ROAD_NAME));
        recyclerView = (RecyclerView) findViewById(R.id.road_list);
        back_img = (ImageView) findViewById(R.id.back_img);



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        assetListAdapter = new AssetListAdapter(this, assetLists, dbData);
        recyclerView.setAdapter(assetListAdapter);
        back_img.setOnClickListener(this);
        start_lat_long_click_view.setOnClickListener(this);
        stop_lat_long_click_view.setOnClickListener(this);
        end_lat_long_click_view.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        block_user_layout.setAlpha(0);
        final Runnable block = new Runnable() {
            @Override
            public void run() {
                block_user_layout.setAlpha(1);
                block_user_layout.startAnimation(AnimationUtils.loadAnimation(AssetTrackingScreen.this, R.anim.text_view_move));

            }
        };
        myHandler.postDelayed(block, 800);
        district_user_layout.setAlpha(0);
        final Runnable district = new Runnable() {
            @Override
            public void run() {
                district_user_layout.setAlpha(1);
                district_user_layout.startAnimation(AnimationUtils.loadAnimation(AssetTrackingScreen.this, R.anim.text_view_move));

            }
        };
        myHandler.postDelayed(district, 1000);


        district_tv.setText(prefManager.getDistrictName());
        block_tv.setText(prefManager.getBlockName());
        loadAssets();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                onBackPress();
                break;
            case R.id.start_lat_long_click_view:
                pointType = "1";
                getLocationPermissionWithLatLong();
                break;
            case R.id.stop_lat_long_click_view:
                pointType = "2";
                getLocationPermissionWithLatLong();
                break;
            case R.id.end_lat_long_click_view:
                pointType = "3";
                getLocationPermissionWithLatLong();
                break;
            case R.id.save_btn:
                saveLatLongData();
                break;

        }
    }

    private void getLocationPermissionWithLatLong() {
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();


        // permission was granted, yay! Do the
        // location-related task you need to do.
        if (ContextCompat.checkSelfPermission(AssetTrackingScreen.this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //Request location updates:
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        }

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(AssetTrackingScreen.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AssetTrackingScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{CAMERA, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                if (ActivityCompat.checkSelfPermission(AssetTrackingScreen.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AssetTrackingScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AssetTrackingScreen.this, new String[]{ACCESS_FINE_LOCATION}, 1);

                }
            }
            if (MyLocationListener.latitude > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (MyLocationListener.latitude > 0) {
                        offlatTextValue = MyLocationListener.latitude;
                        offlongTextValue = MyLocationListener.longitude;
//                        start_lat_long_tv.setText("Lat:"+df2.format(offlatTextValue)+","+"\n"+"Long:"+df2.format(offlanTextValue));
                    }
//                            checkPermissionForCamera();
                } else {
//                    captureImage();
                }
            } else {
                Utils.showAlert(AssetTrackingScreen.this, "Satellite communication not available to get GPS Co-ordination Please Capture Photo in Open Area..");
            }
        } else {
            Utils.showAlert(AssetTrackingScreen.this, "GPS is not turned on...");
        }

        if (!pointType.equalsIgnoreCase("") && offlatTextValue != null) {

            RoadListValue roadListValue = new RoadListValue();
            String dateOfSaveLatLong = sdf.format(new Date());
            String road_Category = getIntent().getStringExtra(AppConstant.KEY_ROAD_CATEGORY);

            String roadId = getIntent().getStringExtra(AppConstant.KEY_ROAD_ID);
            roadListValue.setRoadCategory(road_Category);
            roadListValue.setRoadID(Integer.parseInt(roadId));

            if (pointType.equalsIgnoreCase("1")) {
                roadListValue.setPointType(pointType);
            } else if (pointType.equalsIgnoreCase("2")) {
                roadListValue.setPointType(pointType);
            } else {
                roadListValue.setPointType(pointType);
            }

            roadListValue.setRoadLat(offlatTextValue.toString());
            roadListValue.setRoadLong(offlongTextValue.toString());
            roadListValue.setCreatedDate(dateOfSaveLatLong);

            dbData.saveLatLong(roadListValue);
        }
    }


    public void loadAssets() {
        String road_code = getIntent().getStringExtra(AppConstant.KEY_ROAD_ID);
        JSONObject jsonArray = new JSONObject();
        try {
            jsonArray.put(AppConstant.KEY_ROAD_ID,road_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new fetchAssettask().execute(jsonArray);
    }

    public class fetchAssettask extends AsyncTask<JSONObject, Void,
            ArrayList<RoadListValue>> {
        @Override
        protected ArrayList<RoadListValue> doInBackground(JSONObject... params) {
            dbData.open();
            assetLists = new ArrayList<>();
            assetLists = dbData.select_Asset(params[0],"firstLevel");
            return assetLists;
        }

        @Override
        protected void onPostExecute(ArrayList<RoadListValue> roadList) {
            super.onPostExecute(roadList);
            assetListAdapter = new AssetListAdapter(AssetTrackingScreen.this,
                               roadList, dbData);
            recyclerView.setAdapter(assetListAdapter);
        }
    }

    @Override
    public void onLocationChanged(final Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Toast.makeText(this, "Start Move on the Road", Toast.LENGTH_LONG).show();
//        if (this.latitude > 0.0d) {
//            if (!this.bl_buttonStatus) {
//                Util.progressDialog.dismiss();
//                this.startButton.setEnabled(false);
//                this.pauseButton.setEnabled(true);
//                this.stopButton.setEnabled(true);
//                Toast.makeText(this, "Start Move on the Road", 1).show();
//            }
//            this.bl_buttonStatus = true;
//            PlaceDataSQL.clear();
//            PlaceDataSQL.al_param.add(PlaceDataSQL.TAG_ROAD_IDD);
//            PlaceDataSQL.al_param.add(PlaceDataSQL.TAG_LAT);
//            PlaceDataSQL.al_param.add(PlaceDataSQL.TAG_LON);
//            PlaceDataSQL.al_param.add(PlaceDataSQL.TAG_TRACKING_STATUS);
//            PlaceDataSQL.al_value.add(this.roadId);
//            PlaceDataSQL.al_value.add(new StringBuilder(String.valueOf(location.getLatitude())).toString());
//            PlaceDataSQL.al_value.add(new StringBuilder(String.valueOf(location.getLongitude())).toString());
//            PlaceDataSQL.al_value.add(this.flag);
//            PlaceDataSQL.tableName = "roadTrackingRepository";
//            long row = this.pl.insertIntoTable().longValue();
//            if (row > 0) {
//                Toast.makeText(this, "Inserted " + row, 1).show();
//            } else {
//                Toast.makeText(this, "Not Inserted " + row, 1).show();
//            }
//        }
    }


//    public void Latlong(){
//        if (MyLocationListener.latitude > 0) {
//
//            offlatTextValue = MyLocationListener.latitude;
//            offlanTextValue =  MyLocationListener.longitude;
////            end_lat_long_tv.setText("Lat:"+df2.format(offlatTextValue)+","+"\n"+"Long:"+df2.format(offlanTextValue));
//        }
//    }

    public void saveLatLongData() {
        if(Utils.isOnline()) {
            new fetchDBData().execute();
        }else{
            Utils.showAlert(this,"Turn On Mobile Data To Save");
        }
    }

    public class fetchDBData extends AsyncTask<Void, Void,
            Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                dbData.open();
                saveLatLongLists = dbData.sendPostLatLong();
                saveLatLongArray = new JSONArray();
                latLongData = new JSONObject();
                if (saveLatLongLists.size() > 0) {
                    for (int i = 0; i < saveLatLongLists.size(); i++) {
                        latLongData.put(AppConstant.KEY_ROAD_CATEGORY, saveLatLongLists.get(i).getRoadCategory());
                        latLongData.put(AppConstant.KEY_ROAD_ID, saveLatLongLists.get(i).getRoadID());
                        latLongData.put(AppConstant.KEY_POINT_TYPE, saveLatLongLists.get(i).getPointType());
                        latLongData.put(AppConstant.KEY_ROAD_LAT, saveLatLongLists.get(i).getRoadLat());
                        latLongData.put(AppConstant.KEY_ROAD_LONG, saveLatLongLists.get(i).getRoadLong());
                        latLongData.put(AppConstant.KEY_CREATED_DATE, saveLatLongLists.get(i).getCreatedDate());
                    }
                }
                saveLatLongArray.put(latLongData);

                    saveLatLongData = new JSONObject();
                    try {
                        saveLatLongData.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_ROAD_TRACK_SAVE);
                        saveLatLongData.put(AppConstant.KEY_TRACK_DATA, saveLatLongArray);
                        String authKey = saveLatLongData.toString();
                        int maxLogSize = 2000;
                        for (int i = 0; i <= authKey.length() / maxLogSize; i++) {
                            int start = i * maxLogSize;
                            int end = (i + 1) * maxLogSize;
                            end = end > authKey.length() ? authKey.length() : end;
                            Log.v("to_send+_plain", authKey.substring(start, end));
                        }

                        String authKey1 = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), saveLatLongData.toString());

                        for(int i = 0; i <= authKey1.length() / maxLogSize; i++) {
                            int start = i * maxLogSize;
                            int end = (i+1) * maxLogSize;
                            end = end > authKey.length() ? authKey1.length() : end;
                            Log.v("to_send_encryt", authKey1.substring(start, end));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                }
                saveLatLongList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public void saveLatLongList() {
        try {
            new ApiService(this).makeJSONObjectRequest("saveLatLongList", Api.Method.POST, UrlGenerator.getRoadListUrl(), saveLatLongListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject saveLatLongListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), saveLatLongData.toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("saveLatLongList", "" + authKey);
        return dataSet;
    }
//    Runnable runLocation = new Runnable(){
//        @Override
//        public void run() {
//            if (MyLocationListener.latitude > 0) {
//
//                 offlatTextValue = MyLocationListener.latitude;
//                offlanTextValue =  MyLocationListener.longitude;
//                midd_lat_long_tv.setText("Lat:"+df2.format(offlatTextValue)+","+"\n"+"Long:"+df2.format(offlanTextValue));
//            }
//
//            AssetTrackingScreen.this.myHandler.postDelayed(AssetTrackingScreen.this.runLocation, 2000);
//        }
//    };

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();
            if ("saveLatLongList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, "Saved");
                }
                Log.d("saved_response", "" + responseDecryptedBlockKey);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void OnError(VolleyError volleyError) {

    }
}
