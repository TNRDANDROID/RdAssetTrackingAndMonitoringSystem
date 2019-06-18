package com.nic.RdAssetTrackingAndMonitoringSystem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nic.RdAssetTrackingAndMonitoringSystem.Activity.CameraScreen;
import com.nic.RdAssetTrackingAndMonitoringSystem.Activity.ViewImageScreen;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.texy.treeview.TreeNode;
import me.texy.treeview.base.ClickableNodeViewBinder;


/**
 * Created by zxy on 17/4/23.
 */

public class ThirdLevelNodeViewBinder extends ClickableNodeViewBinder {
    MyCustomTextView third_level_tv,loc_id,view_offline_image;
    TextView view_image_tv;
    View viewTop,viewBottom;
    ImageView take_photo;
    private Context appContext;
    private dbData dbData;
    private PrefManager prefManager;
    public ThirdLevelNodeViewBinder(Context context,View itemView) {
        super(context,itemView);
        third_level_tv = (MyCustomTextView) itemView.findViewById(R.id.third_level_tv);
        view_image_tv = (TextView) itemView.findViewById(R.id.view_image_tv);
        viewTop = (View) itemView.findViewById(R.id.viewTop);
        viewBottom = (View) itemView.findViewById(R.id.viewBottom);
        take_photo = (ImageView) itemView.findViewById(R.id.take_photo);
        view_offline_image = (MyCustomTextView) itemView.findViewById(R.id.view_offline_image);
        loc_id = (MyCustomTextView) itemView.findViewById(R.id.loc_id);
        this.appContext = context;

        dbData  = new dbData(context);
        prefManager = new PrefManager(context);

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
              //  String loc_id = clicked_id.getString("id");
               // Log.d("Nodetreeid",""+ loc_id);
               // cameraActivity(loc_id);
                cameraActivity(clicked_id);
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
                viewImageScreen(clicked_id,"online");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onOfflineTextClickView(TreeNode treeNode, boolean clicked) {
        if (clicked) {
            try {
                JSONObject clicked_id = new JSONObject(String.valueOf(treeNode.getValue()));
                viewImageScreen(clicked_id,"offline");
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        String type = "";
        try {
            jsonObject = new JSONObject(String.valueOf(treeNode.getValue()));
            type =  jsonObject.getString("type");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(type.equalsIgnoreCase("assetScreen")) {

            try {
                visibleOfflinebutton(jsonObject.getString("id"));

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
        }
        else if(type.equalsIgnoreCase("bridgeScreen")) {
            try {

                visibleOfflineBridge(jsonObject.getString(AppConstant.KEY_CULVERT_ID));
               // visibleOnlineBridge(jsonObject.getString(AppConstant.KEY_CULVERT_ID));

                String text = jsonObject.getString(AppConstant.KEY_CULVERT_NAME)+"\n"
                        +"CHAINAGE:"+jsonObject.getString(AppConstant.KEY_CHAINAGE)+"\n"
                        +"WIDTH:"+jsonObject.getString(AppConstant.KEY_WIDTH)+"\n"
                        +"LENGTH:"+jsonObject.getString(AppConstant.KEY_LENGTH)+"\n"
                        +"VENT HEIGHT:"+jsonObject.getString(AppConstant.KEY_VENT_HEIGHT)+"\n"
                        +"SPAN:"+jsonObject.getString(AppConstant.KEY_SPAN);

                third_level_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                third_level_tv.setText(text);

                image_available =  jsonObject.getString(AppConstant.KEY_IMAGE_AVAILABLE);
                if(image_available.equalsIgnoreCase("Y")) {
                    view_image_tv.setVisibility(View.VISIBLE);
                }
                else if(image_available.equalsIgnoreCase("N")) {
                    view_image_tv.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        // third_level_tv.setText(treeNode.getValue().toString());
    }

    public void visibleOfflinebutton(String asset_id){
        String road_id = prefManager.getRoadId();
        String road_category = prefManager.getRoadCategoty();
        dbData.open();
        ArrayList<RoadListValue> assets = dbData.selectImage(road_id,road_category,asset_id,"0");

        if (assets.size() > 0) {
            view_offline_image.setVisibility(View.VISIBLE);
            viewTop.setVisibility(View.VISIBLE);
        }
        else {
            view_offline_image.setVisibility(View.GONE);
            viewTop.setVisibility(View.GONE);
        }
    }

    public void visibleOfflineBridge(String culvert_id){
        String road_id = prefManager.getRoadId();
        dbData.open();
        ArrayList<RoadListValue> image = dbData.selectBridgeImage(road_id,culvert_id,"0");

        if (image.size() > 0) {
            view_offline_image.setVisibility(View.VISIBLE);
            viewTop.setVisibility(View.VISIBLE);
        }
        else {
            view_offline_image.setVisibility(View.GONE);
            viewTop.setVisibility(View.GONE);
        }
    }

    public void visibleOnlineBridge(String culvert_id){
        String road_id = prefManager.getRoadId();
        dbData.open();
        ArrayList<RoadListValue> image = dbData.selectBridgeImage(road_id,culvert_id,"1");

        if (image.size() > 0) {
            view_image_tv.setVisibility(View.VISIBLE);
        }
        else {
            view_image_tv.setVisibility(View.GONE);
        }
    }


    public void cameraActivity(JSONObject clicked_id){
        String loc_id = null;
        String type = null;
        try {
            type = clicked_id.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(type.equalsIgnoreCase("assetScreen")){
            try {
                loc_id = clicked_id.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Activity activity = (Activity) appContext;
            Intent intent = new Intent( appContext, CameraScreen.class);
            intent.putExtra("loc_id",loc_id);
            intent.putExtra(AppConstant.KEY_SCREEN_TYPE,"thirdLevelNode");
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
        else if(type.equalsIgnoreCase("bridgeScreen")){
            String culvert_id = null;
            try {
                culvert_id = clicked_id.getString(AppConstant.KEY_CULVERT_ID);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Activity activity = (Activity) appContext;
            Intent intent = new Intent( appContext, CameraScreen.class);
            intent.putExtra(AppConstant.KEY_CULVERT_ID,culvert_id);
            intent.putExtra(AppConstant.KEY_SCREEN_TYPE,"bridgeLevel");
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }

    }

    public void viewImageScreen(JSONObject jsonObject,String  OffOntype) {
        String type = "";
        String culvert_id = "";
        String data_type = "";
        try {
            type = jsonObject.getString("type");
            culvert_id = jsonObject.getString(AppConstant.KEY_CULVERT_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Activity activity = (Activity) appContext;
        Intent intent = new Intent(appContext, ViewImageScreen.class);

        if(type.equalsIgnoreCase("assetScreen")) {
            if(OffOntype.equalsIgnoreCase("online")) {
                if(!Utils.isOnline()) {
                    Utils.showAlert(this.appContext,"Your Internet seems to be Offline.Images can be viewed only in Online mode.");
                    return;
                }
                else {
                    try {
                        data_type = jsonObject.getString("data_type");
                         Integer loc_id = jsonObject.getInt("loc_id");
                         Integer asset_id = jsonObject.getInt("id");
                        intent.putExtra(AppConstant.KEY_ROAD_CATEGORY,data_type);
                        intent.putExtra(AppConstant.KEY_LOCATION_ID,String.valueOf(loc_id));
                        intent.putExtra("asset_id",String.valueOf(asset_id));
                        intent.putExtra(AppConstant.KEY_LOCATION_GROUP,String.valueOf(jsonObject.getInt(AppConstant.KEY_LOCATION_GROUP)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            intent.putExtra("imageData", String.valueOf(jsonObject));
            intent.putExtra("OffOntype",OffOntype);
            intent.putExtra(AppConstant.KEY_SCREEN_TYPE,"thirdLevelNode");
        }
        else if(type.equalsIgnoreCase("bridgeScreen")) {
            if(OffOntype.equalsIgnoreCase("online")) {
                if(!Utils.isOnline()) {
                    Utils.showAlert(this.appContext,"Your Internet seems to be Offline.Images can be viewed only in Online mode.");
                    return;
                }
                else {
                    try {
                        intent.putExtra(AppConstant.KEY_LOCATION_GROUP,jsonObject.getString(AppConstant.KEY_LOCATION_GROUP));
                        intent.putExtra(AppConstant.KEY_LOCATION_ID,jsonObject.getString(AppConstant.KEY_LOCATION_ID));
                        intent.putExtra(AppConstant.KEY_CULVERT_TYPE,jsonObject.getString(AppConstant.KEY_CULVERT_TYPE));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            intent.putExtra(AppConstant.KEY_CULVERT_ID,culvert_id);
            intent.putExtra(AppConstant.KEY_ROAD_ID,prefManager.getRoadId());
            intent.putExtra("OffOntype",OffOntype);
            intent.putExtra(AppConstant.KEY_SCREEN_TYPE,"bridgeScreen");
        }

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
