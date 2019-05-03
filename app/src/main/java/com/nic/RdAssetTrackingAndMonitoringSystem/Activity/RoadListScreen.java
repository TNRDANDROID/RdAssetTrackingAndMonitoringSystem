package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nic.RdAssetTrackingAndMonitoringSystem.Adapter.RoadListAdapter;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;


import java.util.ArrayList;



public class RoadListScreen extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    public dbData dbData = new dbData(this);
    private RoadListAdapter roadListAdapter;
    private ArrayList<RoadListValue> roadLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.road_list_activity);
        intializeUI();
    }


    public void intializeUI() {
     //   prefManager = new PrefManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.road_list);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);

        loadVPR();
    }

    public void loadVPR() {
        String code = getIntent().getExtras().getString(AppConstant.KEY_ROAD_CATEGORY_CODE);
        new fetchRoadtask().execute(code);
    }

    public class fetchRoadtask extends AsyncTask<String, Void,
            ArrayList<RoadListValue>> {
        @Override
        protected ArrayList<RoadListValue> doInBackground(String... params) {
            dbData.open();
            roadLists = dbData.getAll(params[0]);
//            if (roadLists.size() > 0) {
//                for (int i = 0; i < roadLists.size(); i++) {
//                    RoadListValue card = new RoadListValue();
//                    card.setRoadName(roadLists.get(i).getRoadName());
//                    card.setRoadID(roadLists.get(i).getRoadID());
//                    card.setRoadCode(roadLists.get(i).getRoadCode());
//                    card.setRoadVillage(roadLists.get(i).getRoadVillage());
//                    roadListValues.add(card);
//                }
//            }
            return roadLists;
        }

        @Override
        protected void onPostExecute(ArrayList<RoadListValue> roadList) {
            super.onPostExecute(roadList);
            roadListAdapter = new RoadListAdapter(RoadListScreen.this,
                    roadList, dbData);
            recyclerView.setAdapter(roadListAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
    public void dashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

}
