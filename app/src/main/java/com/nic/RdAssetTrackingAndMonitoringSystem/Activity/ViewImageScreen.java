package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.Api;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ApiService;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ServerResponse;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyEditTextView;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.UrlGenerator;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewImageScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {
    private ImageView back_img,image_view;
    JSONObject jsonObject = new JSONObject();
    public dbData dbData = new dbData(this);
    PrefManager prefManager;
    private String screen_type;
    private LinearLayout description_layout;
    private MyEditTextView description_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            screen_type = bundle.getString(AppConstant.KEY_SCREEN_TYPE);
            Log.d("ScreenType", "" + screen_type);
        }
        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        image_view = (ImageView)findViewById(R.id.image_view);
        back_img = (ImageView) findViewById(R.id.back_img);
        description_layout = (LinearLayout) findViewById(R.id.description_layout);
        description_tv = (MyEditTextView) findViewById(R.id.description_tv);
        back_img.setOnClickListener(this);

        if (screen_type.equalsIgnoreCase("Habitation")) {
            description_layout.setVisibility(View.VISIBLE);
            String OffOntype = getIntent().getStringExtra("OffOntype");
            if (OffOntype.equalsIgnoreCase("online")) {
                getPMGSYOnlineImage();
            } else if (OffOntype.equalsIgnoreCase("offline")) {
                habitationImage("0");
            }
        }
        else if(screen_type.equalsIgnoreCase("thirdLevelNode")) {
            description_layout.setVisibility(View.GONE);
            String OffOntype = getIntent().getStringExtra("OffOntype");

            if (OffOntype.equalsIgnoreCase("online")) {
                if (Utils.isOnline()) {
                    getAssetOnlineImage();
                }
            } else if (OffOntype.equalsIgnoreCase("offline")) {
                thirdLevelNodeImage("0");
            }
        }
        else if(screen_type.equalsIgnoreCase("bridgeScreen")) {
            description_layout.setVisibility(View.GONE);
            String OffOntype = getIntent().getStringExtra("OffOntype");

            if (OffOntype.equalsIgnoreCase("online")) {
                if(Utils.isOnline()){
                    getBridgesOnlineImage();
                }
            } else if (OffOntype.equalsIgnoreCase("offline")) {
                bridgeImage("0");
            }

        }
    }

    public void thirdLevelNodeImage(String server_flag) {
        String image = getIntent().getStringExtra("imageData");
        String OffOntype = getIntent().getStringExtra("OffOntype");

        try {
                jsonObject = new JSONObject(image);
                String asset_id = jsonObject.getString("id");
                String road_id = prefManager.getRoadId();
                String road_category = prefManager.getRoadCategoty();

                dbData.open();
                ArrayList<RoadListValue> assets = dbData.selectImage(road_id, road_category, asset_id,server_flag);

                if (assets.size() > 0) {
                    for (int i = 0; i < assets.size(); i++) {
                        Bitmap bitmap = assets.get(i).getImage();
                        image_view.setImageBitmap(bitmap);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

    }

    public void habitationImage(String server_flag) {

        String pmgsy_habcode = getIntent().getStringExtra(AppConstant.KEY_PMGSY_HAB_CODE);

        Log.d("pmgsy_habcode",pmgsy_habcode);

            dbData.open();
            ArrayList<RoadListValue> habitation_image = dbData.selectImage_Habitation(pmgsy_habcode,server_flag);

            if (habitation_image.size() > 0) {
                for (int i = 0; i < habitation_image.size(); i++) {
                    Bitmap bitmap = habitation_image.get(i).getImage();
                    image_view.setImageBitmap(bitmap);
                    description_tv.setText(habitation_image.get(i).getRemark());
                    description_tv.setEnabled(false);
                }
            }
    }

    public void bridgeImage(String image_flag) {

        String culvert_id = getIntent().getStringExtra(AppConstant.KEY_CULVERT_ID);
        String road_id = getIntent().getStringExtra(AppConstant.KEY_ROAD_ID);
        Log.d("culvert_id",culvert_id);

        dbData.open();
        ArrayList<RoadListValue> habitation_image = dbData.selectBridgeImage(road_id,culvert_id,image_flag);

        if (habitation_image.size() > 0) {
            for (int i = 0; i < habitation_image.size(); i++) {
                Bitmap bitmap = habitation_image.get(i).getImage();
                image_view.setImageBitmap(bitmap);
                description_tv.setText(habitation_image.get(i).getRemark());
                description_tv.setEnabled(false);
            }
        }
    }
    public void getBridgesOnlineImage(){
        try {
            new ApiService(this).makeJSONObjectRequest("BridgesImage", Api.Method.POST, UrlGenerator.getRoadListUrl(), bridgesJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject bridgesJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), bridgesListJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("BridgesImage", "" + authKey);
        return dataSet;
    }
    public  JSONObject bridgesListJsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_BRIDGES_CULVERT_IMAGES);
        dataSet.put(AppConstant.KEY_LOCATION_GROUP, getIntent().getStringExtra(AppConstant.KEY_LOCATION_GROUP));
        dataSet.put(AppConstant.KEY_LOCATION_ID, getIntent().getStringExtra(AppConstant.KEY_LOCATION_ID));
        dataSet.put(AppConstant.KEY_ROAD_ID,prefManager.getRoadId());
        dataSet.put(AppConstant.KEY_CULVERT_TYPE, getIntent().getStringExtra(AppConstant.KEY_CULVERT_TYPE));
        dataSet.put(AppConstant.KEY_CULVERT_ID, getIntent().getStringExtra(AppConstant.KEY_CULVERT_ID));
        Log.d("utils_bridgesImage", "" + dataSet);
        return dataSet;
    }
    public void getPMGSYOnlineImage(){
        try {
            new ApiService(this).makeJSONObjectRequest("PMGSYImage", Api.Method.POST, UrlGenerator.getRoadListUrl(), pmgsyImagesJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject pmgsyImagesJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), pmgsyImagesListJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("pmgsyImagesList", "" + authKey);
        return dataSet;
    }
    public  JSONObject pmgsyImagesListJsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_PMGSY_ONLINE_IMAGES);
        dataSet.put(AppConstant.KEY_PMGSY_DCODE, getIntent().getStringExtra(AppConstant.KEY_PMGSY_DCODE));
        dataSet.put(AppConstant.KEY_PMGSY_BCODE, getIntent().getStringExtra(AppConstant.KEY_PMGSY_BCODE));
        dataSet.put(AppConstant.KEY_PMGSY_PVCODE,getIntent().getStringExtra(AppConstant.KEY_PMGSY_PVCODE));
        dataSet.put(AppConstant.KEY_PMGSY_HAB_CODE, getIntent().getStringExtra(AppConstant.KEY_PMGSY_HAB_CODE));
        Log.d("utils_pmgsyImages", "" + dataSet);
        return dataSet;
    }

    public void getAssetOnlineImage() {
        try {
            new ApiService(this).makeJSONObjectRequest("AssetImage", Api.Method.POST, UrlGenerator.getRoadListUrl(), assetImagesJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject assetImagesJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), assetImagesListJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("utils_AssetEncrydataSet", "" + authKey);
        return dataSet;
    }

    public JSONObject assetImagesListJsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_ASSET_ONLINE_IMAGES);
        dataSet.put(AppConstant.KEY_ROAD_CATEGORY, getIntent().getStringExtra(AppConstant.KEY_ROAD_CATEGORY));
        dataSet.put(AppConstant.KEY_ROAD_ID, prefManager.getRoadId());
        dataSet.put(AppConstant.KEY_LOCATION_GROUP,prefManager.getLocationGroup());
        dataSet.put(AppConstant.KEY_LOCATION_ID, getIntent().getStringExtra(AppConstant.KEY_LOCATION_ID));
        dataSet.put("asset_id", getIntent().getStringExtra("asset_id"));
        dataSet.put(AppConstant.KEY_LOCATION_GROUP, getIntent().getStringExtra(AppConstant.KEY_LOCATION_GROUP));
        Log.d("utils_AssetDataset", "" + dataSet);
        return dataSet;
    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();
            if ("BridgesImage".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray(AppConstant.JSON_DATA);
                    byte[] decodedString = Base64.decode(jsonArray.getJSONObject(0).getString("image"), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image_view.setImageBitmap(decodedByte);
                }
                Log.d("resp_bridgesImage", "" + responseDecryptedBlockKey);
            }
            if ("PMGSYImage".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray(AppConstant.JSON_DATA);
                    String remark = jsonArray.getJSONObject(0).getString("remark");
                    byte[] decodedString = Base64.decode(jsonArray.getJSONObject(0).getString("image"), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    if(remark.equalsIgnoreCase("")) {
                        description_tv.setVisibility(View.GONE);
                    }
                    else{
                        description_tv.setVisibility(View.VISIBLE);
                        description_tv.setText(remark);
                        description_tv.setFocusable(false);
                        description_tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/circular-bold.ttf"));
                        description_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    }
                    image_view.setImageBitmap(decodedByte);
                }
                Log.d("resp_PMGSYImage", "" + responseDecryptedBlockKey);
            }
            if ("AssetImage".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray(AppConstant.JSON_DATA);
                    byte[] decodedString = Base64.decode(jsonArray.getJSONObject(0).getString("image"), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image_view.setImageBitmap(decodedByte);
                }
                Log.d("resp_AssetImage", "" + responseDecryptedBlockKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                onBackPress();
                break;
        }
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
}
