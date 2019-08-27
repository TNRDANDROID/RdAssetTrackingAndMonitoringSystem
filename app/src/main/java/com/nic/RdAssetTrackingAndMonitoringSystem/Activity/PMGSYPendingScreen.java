package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.nic.RdAssetTrackingAndMonitoringSystem.Adapter.PMGSYPendingAdapter;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.Api;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ServerResponse;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class PMGSYPendingScreen extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener {
    private RecyclerView pendingRecycler;
    public dbData dbData = new dbData(this);
    ArrayList<RoadListValue> pendingList = new ArrayList<>();
    private PMGSYPendingAdapter pmgsyPendingAdapter;
    private ImageView back_img, home_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pmgsy_pending_screen);
        intializeUI();
    }

    public void intializeUI() {

        pendingRecycler = (RecyclerView) findViewById(R.id.pmgsy_pending_list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        pendingRecycler.setLayoutManager(mLayoutManager);
        pendingRecycler.setItemAnimator(new DefaultItemAnimator());
        pendingRecycler.setHasFixedSize(true);
        pendingRecycler.setNestedScrollingEnabled(false);
        pendingRecycler.setFocusable(false);
        pmgsyPendingAdapter = new PMGSYPendingAdapter(this, pendingList, dbData);
        pendingRecycler.setAdapter(pmgsyPendingAdapter);
        back_img = (ImageView) findViewById(R.id.back_img);
        home_img = (ImageView) findViewById(R.id.home_img);
        back_img.setOnClickListener(this);
        home_img.setOnClickListener(this);

        //  new fetchHabitationtask().execute();


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public class fetchHabitationtask extends AsyncTask<JSONObject, Void,
            ArrayList<RoadListValue>> {
        @Override
        protected ArrayList<RoadListValue> doInBackground(JSONObject... params) {
            dbData.open();
            pendingList = new ArrayList<>();
            pendingList = dbData.select_Habitation(params[0]);
            return pendingList;
        }

        @Override
        protected void onPostExecute(ArrayList<RoadListValue> pendingList) {
            super.onPostExecute(pendingList);
            pmgsyPendingAdapter = new PMGSYPendingAdapter(PMGSYPendingScreen.this,
                    pendingList, dbData);
            pendingRecycler.setAdapter(pmgsyPendingAdapter);
        }
    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {

    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
