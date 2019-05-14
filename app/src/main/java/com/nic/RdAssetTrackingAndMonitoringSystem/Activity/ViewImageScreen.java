package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.Api;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ServerResponse;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ViewImageScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {
    private ImageView back_img,image_view;
    JSONObject jsonObject = new JSONObject();
    public dbData dbData = new dbData(this);
    PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image);
        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        image_view = (ImageView)findViewById(R.id.image_view);

        String image = getIntent().getStringExtra("imageData");
        String type = getIntent().getStringExtra("type");

        try {
            jsonObject = new JSONObject(image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(type.equalsIgnoreCase("online")) {
            String imagestr="";
            try {
                imagestr = jsonObject.getString("image");
                String image_available = jsonObject.getString("image_available");

                if(imagestr != "") {
                    byte[] decodedString = Base64.decode(jsonObject.getString("image"), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image_view.setImageBitmap(decodedByte);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            if(type.equalsIgnoreCase("offline")) {
                try {
                    String asset_id = jsonObject.getString("id");
                    String  road_id = prefManager.getRoadId();
                    String  road_category = prefManager.getRoadCategoty();

                    dbData.open();
                    ArrayList<RoadListValue> assets = dbData.selectImage(road_id,road_category,asset_id);

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


        back_img = (ImageView) findViewById(R.id.back_img);
        back_img.setOnClickListener(this);


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
