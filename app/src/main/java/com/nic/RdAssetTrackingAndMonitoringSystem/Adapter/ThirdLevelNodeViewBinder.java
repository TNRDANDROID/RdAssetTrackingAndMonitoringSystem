package com.nic.RdAssetTrackingAndMonitoringSystem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nic.RdAssetTrackingAndMonitoringSystem.Activity.CameraScreen;
import com.nic.RdAssetTrackingAndMonitoringSystem.Activity.ViewImageScreen;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import me.texy.treeview.TreeNode;
import me.texy.treeview.base.ClickableNodeViewBinder;


/**
 * Created by zxy on 17/4/23.
 */

public class ThirdLevelNodeViewBinder extends ClickableNodeViewBinder {
    MyCustomTextView third_level_tv,loc_id,view_offline_image;
    TextView view_image_tv;
    private Context appContext;
    public ThirdLevelNodeViewBinder(Context context,View itemView) {
        super(context,itemView);
        third_level_tv = (MyCustomTextView) itemView.findViewById(R.id.third_level_tv);
        view_image_tv = (TextView) itemView.findViewById(R.id.view_image_tv);
        loc_id = (MyCustomTextView) itemView.findViewById(R.id.loc_id);
        this.appContext = context;

    }

    @Override
    public int getClickableViewId() {
        return R.id.take_photo;
    }

    @Override
    public int getTextViewClickable() {
        return R.id.view_image_tv;
    }

    @Override
    public int getOfflineTextViewClickable() {
        return R.id.view_offline_image;
    }


    @Override
    public void onClickView(TreeNode treeNode ,boolean clicked) {

        if(clicked) {
            try {
                JSONObject clicked_id = new JSONObject(String.valueOf(treeNode.getValue()));
                String loc_id = clicked_id.getString("id");
                Log.d("Nodetreeid",""+ loc_id);
                cameraActivity(loc_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTextClickView(TreeNode treeNode, boolean clicked) {
        if (clicked) {
            try {
                JSONObject clicked_id = new JSONObject(String.valueOf(treeNode.getValue()));
              //  String image = clicked_id.getString("image");
              //  String image_available = clicked_id.getString("image_available");
                viewImageScreen(clicked_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onOfflineTextClickView(TreeNode treeNode, boolean clicked) {
        if (clicked) {

        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.third_level;
    }

    @Override
    public void bindView(TreeNode treeNode) {
        JSONObject jsonObject = new JSONObject();
        String image_available = "";
        try {
            jsonObject = new JSONObject(String.valueOf(treeNode.getValue()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            third_level_tv.setText(jsonObject.getString("display"));
            image_available =  jsonObject.getString("image_available");
            if(image_available.equalsIgnoreCase("Y")) {
                view_image_tv.setVisibility(View.VISIBLE);
            }
            else {
                view_image_tv.setVisibility(View.GONE);
            }
            loc_id.setTag(loc_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       // third_level_tv.setText(treeNode.getValue().toString());
    }


    public void cameraActivity(String id){
        String loc_id = id;
        Activity activity = (Activity) appContext;
        Intent intent = new Intent( appContext, CameraScreen.class);
        intent.putExtra("loc_id",loc_id);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void viewImageScreen(JSONObject jsonObject) {
        Activity activity = (Activity) appContext;
        Intent intent = new Intent(appContext, ViewImageScreen.class);
        intent.putExtra("imageData", String.valueOf(jsonObject));
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
