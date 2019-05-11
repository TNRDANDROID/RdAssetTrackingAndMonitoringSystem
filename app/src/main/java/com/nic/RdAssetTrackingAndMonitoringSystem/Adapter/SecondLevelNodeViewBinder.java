package com.nic.RdAssetTrackingAndMonitoringSystem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import me.texy.treeview.TreeNode;
import me.texy.treeview.base.BaseNodeViewBinder;


/**
 * Created by zxy on 17/4/23.
 */

public class SecondLevelNodeViewBinder extends BaseNodeViewBinder {

    MyCustomTextView second_level_tv;
    ImageView imageView;
    public SecondLevelNodeViewBinder(Context context,View itemView) {
        super(context,itemView);
        second_level_tv = (MyCustomTextView) itemView.findViewById(R.id.second_level_tv);
        imageView = (ImageView) itemView.findViewById(R.id.arrow_img);
    }



    @Override
    public int getLayoutId() {
        return R.layout.second_level;
    }

    @Override
    public void bindView(final TreeNode treeNode) {
        second_level_tv.setText(treeNode.getValue().toString());
        imageView.setRotation(treeNode.isExpanded() ? 90 : 0);
    }

    @Override
    public void onNodeToggled(TreeNode treeNode, boolean expand) {
        if (expand) {
            imageView.animate().rotation(90).setDuration(200).start();
        } else {
            imageView.animate().rotation(0).setDuration(200).start();
        }
    }
}
