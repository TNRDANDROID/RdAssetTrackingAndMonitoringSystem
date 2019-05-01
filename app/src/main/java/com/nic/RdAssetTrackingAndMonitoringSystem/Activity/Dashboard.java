package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.Api;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ApiService;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ServerResponse;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.Dialog.MyDialog;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.UrlGenerator;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity implements View.OnClickListener, MyDialog.myOnClickListener, Api.ServerResponseListener {

    private MyCustomTextView on_road_tv, district_tv, block_tv;
    private LinearLayout district_user_layout, block_user_layout;
    private ImageView logout_tv;
    Handler myHandler = new Handler();
    private List<RoadListValue> roadListValue = new ArrayList<>();
    private PrefManager prefManager;

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
        district_tv = (MyCustomTextView) findViewById(R.id.district_tv);
        block_tv = (MyCustomTextView) findViewById(R.id.block_tv);
        on_road_tv = (MyCustomTextView) findViewById(R.id.on_road_tv);
        logout_tv = (ImageView) findViewById(R.id.logout_tv);

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


        district_tv.setText(prefManager.getDistrictName());
        block_tv.setText(prefManager.getBlockName());
        logout_tv.setOnClickListener(this);
        getRoadList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_tv:
                closeApplication();
                break;
        }
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

    public JSONObject roadListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.roadListJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("roadlist", "" + authKey);
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
//                        loadBlockList(jsonObject.getJSONArray(AppConstant.JSON_DATA));
                    }
                    Log.d("RoadList", "" + responseDecryptedBlockKey);
                }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadBlockList(JSONArray jsonArray) {


    }

    @Override
    public void OnError(VolleyError volleyError) {
    }
}
