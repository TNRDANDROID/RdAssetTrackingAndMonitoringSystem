package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.Api;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ServerResponse;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyEditTextView;

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
            habitationImage();
        }
        else if(screen_type.equalsIgnoreCase("thirdLevelNode")) {
            description_layout.setVisibility(View.GONE);
            thirdLevelNodeImage();
        }
        else if(screen_type.equalsIgnoreCase("bridgeScreen")) {
            description_layout.setVisibility(View.GONE);
            bridgeImage();
        }
    }

    public void thirdLevelNodeImage() {
        String image = getIntent().getStringExtra("imageData");
        String OffOntype = getIntent().getStringExtra("OffOntype");

        try {
            jsonObject = new JSONObject(image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (OffOntype.equalsIgnoreCase("online")) {
            String imagestr = "";
            try {
                imagestr = jsonObject.getString("image");
                String image_available = jsonObject.getString("image_available");

                if (imagestr != "") {
                    byte[] decodedString = Base64.decode(jsonObject.getString("image"), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image_view.setImageBitmap(decodedByte);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (OffOntype.equalsIgnoreCase("offline")) {
            try {
                String asset_id = jsonObject.getString("id");
                String road_id = prefManager.getRoadId();
                String road_category = prefManager.getRoadCategoty();

                dbData.open();
                ArrayList<RoadListValue> assets = dbData.selectImage(road_id, road_category, asset_id);

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
    }

    public void habitationImage() {

        String pmgsy_habcode = getIntent().getStringExtra(AppConstant.KEY_PMGSY_HAB_CODE);
        String OffOntype = getIntent().getStringExtra("OffOntype");
        Log.d("pmgsy_habcode",pmgsy_habcode);
        String server_flag = null;

        if (OffOntype.equalsIgnoreCase("online")) {
           server_flag = "1";
        } else if (OffOntype.equalsIgnoreCase("offline")) {
            server_flag = "0";
        }


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

    public void bridgeImage() {

        String culvert_id = getIntent().getStringExtra(AppConstant.KEY_CULVERT_ID);
        String road_id = getIntent().getStringExtra(AppConstant.KEY_ROAD_ID);
        String OffOntype = getIntent().getStringExtra("OffOntype");
        Log.d("culvert_id",culvert_id);
        String image_flag = null;

        if (OffOntype.equalsIgnoreCase("online")) {
            image_flag = "1";
        } else if (OffOntype.equalsIgnoreCase("offline")) {
            image_flag = "0";
        }


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

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {

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
