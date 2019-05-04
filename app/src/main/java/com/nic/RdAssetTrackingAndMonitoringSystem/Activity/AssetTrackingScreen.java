package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;

public class AssetTrackingScreen extends AppCompatActivity implements LocationListener,View.OnClickListener {
private PrefManager prefManager;
    private ImageView back_img;
    private LinearLayout district_user_layout, block_user_layout;
    private MyCustomTextView district_tv, block_tv,start_lat_long_click_view,stop_lat_long_click_view,end_lat_long_click_view,start_lat_long_tv,midd_lat_long_tv,end_lat_long_tv;
    private RecyclerView recyclerView;
    Handler myHandler = new Handler();
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
        start_lat_long_click_view = (MyCustomTextView) findViewById(R.id.start_lat_long_click_view);
        stop_lat_long_click_view = (MyCustomTextView) findViewById(R.id.stop_lat_long_click_view);
        end_lat_long_click_view = (MyCustomTextView) findViewById(R.id.end_lat_long_click_view);
        recyclerView = (RecyclerView) findViewById(R.id.road_list);
        back_img = (ImageView) findViewById(R.id.back_img);



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        back_img.setOnClickListener(this);
        start_lat_long_click_view.setOnClickListener(this);
        stop_lat_long_click_view.setOnClickListener(this);
        end_lat_long_click_view.setOnClickListener(this);
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
    public void onLocationChanged(Location location) {

    }

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
}
