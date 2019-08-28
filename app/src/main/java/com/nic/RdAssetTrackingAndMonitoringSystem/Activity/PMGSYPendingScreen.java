package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.nic.RdAssetTrackingAndMonitoringSystem.Adapter.PMGSYPendingAdapter;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class PMGSYPendingScreen extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener {
    private RecyclerView pendingRecycler;
    public dbData dbData = new dbData(this);
    ArrayList<RoadListValue> pendingList = new ArrayList<>();
    private PMGSYPendingAdapter pmgsyPendingAdapter;
    private ImageView back_img, home_img;
    JSONObject datasetHabitation = new JSONObject();
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private ProgressHUD progressHUD;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pmgsy_pending_screen);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
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
        new fetchHabitationtask().execute();


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
            pendingList = dbData.getSavedHabitation("0");
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

    public class toUploadHabitation extends AsyncTask<RoadListValue, Void,
            JSONObject> {
        @Override
        protected JSONObject doInBackground(RoadListValue... roadListValues) {
            dbData.open();
            JSONArray habitation = new JSONArray();
            ArrayList<RoadListValue> Habitation = dbData.getPendingSavedHabitation(roadListValues[0].getPmgsyDcode(), roadListValues[0].getPmgsyBcode(), roadListValues[0].getPmgsyPvcode(), roadListValues[0].getPmgsyHabcode());

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
                        jsonObject.put(AppConstant.KEY_IMAGE_REMARK,Habitation.get(i).getRemark());

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
    public void syncHabitation() {
        try {
            new ApiService(this).makeJSONObjectRequest("saveHabitation", Api.Method.POST, UrlGenerator.getRoadListUrl(), habitaionSavedJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getPMGSYHabitation() {
        try {
            new ApiService(this).makeJSONObjectRequest("PMGSYHabitationList", Api.Method.POST, UrlGenerator.getRoadListUrl(), pmgsyHabitationListJsonParams(), "not cache", this);
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

    public JSONObject pmgsyHabitationListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.pmgsyHabitationListJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("pmgsyHabitationList", "" + authKey);
        return dataSet;
    }
    @Override
    public void OnMyResponse(ServerResponse serverResponse) {

        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();
            if ("saveHabitation".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, "Synchronized PMGSY Habitation Data to the server");
                    dbData.open();
                    db.delete(DBHelper.SAVE_IMAGE_HABITATION_TABLE, "pmgsy_dcode = ? and pmgsy_bcode = ? and pmgsy_pvcode = ? and pmgsy_hab_code = ?", new String[]{prefManager.getKeyPmgsyDcode(), prefManager.getKeyPmgsyBcode(), prefManager.getKeyPmgsyPvcode(), prefManager.getKeyPmgsyHabcode()});
                    dbData.deletePmgsyHabitationTable();
                    pmgsyPendingAdapter.deletePending(Integer.valueOf(prefManager.getKeyPmgsyDeleteId()));
                    pmgsyPendingAdapter.notifyDataSetChanged();
                    getPMGSYHabitation();
                    datasetHabitation = new JSONObject();
                }else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("ERROR")) {
                    db.delete(DBHelper.SAVE_IMAGE_HABITATION_TABLE, "pmgsy_dcode = ? and pmgsy_bcode = ? and pmgsy_pvcode = ? and pmgsy_hab_code = ?", new String[]{prefManager.getKeyPmgsyDcode(), prefManager.getKeyPmgsyBcode(), prefManager.getKeyPmgsyPvcode(), prefManager.getKeyPmgsyHabcode()});
                    pmgsyPendingAdapter.deletePending(Integer.valueOf(prefManager.getKeyPmgsyDeleteId()));
                    pmgsyPendingAdapter.notifyDataSetChanged();
                    Utils.showAlert(this, jsonObject.getString("MESSAGE"));
                }
                Log.d("savedHabitation", "" + responseDecryptedBlockKey);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class InsertPMGSYHabitationListTask extends AsyncTask<JSONObject, Void, Void> {

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
                            pmgsyHabitation.setImageAvailable(jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE_AVAILABLE));

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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressHUD != null) {
                progressHUD.cancel();
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHUD = ProgressHUD.show(PMGSYPendingScreen.this, "Downloading", true, false, null);
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
