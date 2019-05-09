package com.nic.RdAssetTrackingAndMonitoringSystem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nic.RdAssetTrackingAndMonitoringSystem.Activity.CameraScreen;
import com.nic.RdAssetTrackingAndMonitoringSystem.Activity.RoadListScreen;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;

import me.texy.treeview.TreeNode;
import me.texy.treeview.base.BaseNodeViewBinder;
import me.texy.treeview.base.CheckableNodeViewBinder;
import me.texy.treeview.base.ClickableNodeViewBinder;


/**
 * Created by zxy on 17/4/23.
 */

public class ThirdLevelNodeViewBinder extends ClickableNodeViewBinder {
    MyCustomTextView third_level_tv;
    private ImageView take_photo;
    private Context appContext;
    public ThirdLevelNodeViewBinder(Context context,View itemView) {
        super(context,itemView);
        third_level_tv = (MyCustomTextView) itemView.findViewById(R.id.third_level_tv);


        this.appContext = context;

    }

    @Override
    public int getClickableViewId() {
        return R.id.take_photo;
    }


    @Override
    public void onClickView(String s ,boolean clicked) {

        if(clicked) {
            Log.d("Nodetreeid",""+s);
            cameraActivity();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.third_level;
    }

    @Override
    public void bindView(TreeNode treeNode) {
        third_level_tv.setText(treeNode.getValue().toString());
    }


    public void cameraActivity(){
        Activity activity = (Activity) appContext;
        Intent intent = new Intent( appContext, CameraScreen.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
