package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.nic.RdAssetTrackingAndMonitoringSystem.Adapter.CommonAdapter;
import com.nic.RdAssetTrackingAndMonitoringSystem.Adapter.PMGSYListAdapter;
import com.nic.RdAssetTrackingAndMonitoringSystem.Adapter.PendingScreenAdapter;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.Api;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ServerResponse;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PendingScreen extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener {

    private RecyclerView pendingRecycler;
    public dbData dbData = new dbData(this);
    ArrayList<RoadListValue> pendingList = new ArrayList<>();
    private PendingScreenAdapter pendingScreenAdapter;
    private ImageView back_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_screen);
        intializeUI();
    }

    public void intializeUI() {

        pendingRecycler = (RecyclerView) findViewById(R.id.road_list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        pendingRecycler.setLayoutManager(mLayoutManager);
        pendingRecycler.setItemAnimator(new DefaultItemAnimator());
        pendingRecycler.setHasFixedSize(true);
        pendingRecycler.setNestedScrollingEnabled(false);
        pendingRecycler.setFocusable(false);
        pendingScreenAdapter = new PendingScreenAdapter(this, pendingList, dbData);
        pendingRecycler.setAdapter(pendingScreenAdapter);

      //  new fetchHabitationtask().execute();

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

        }
    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
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

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {

    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        pendingScreenAdapter.notifyDataSetChanged();
    }
}
