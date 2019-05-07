package com.nic.RdAssetTrackingAndMonitoringSystem.Adapter;

import android.view.View;
import android.widget.TextView;

import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;

import me.texy.treeview.TreeNode;
import me.texy.treeview.base.BaseNodeViewBinder;
import me.texy.treeview.base.CheckableNodeViewBinder;


/**
 * Created by zxy on 17/4/23.
 */

public class ThirdLevelNodeViewBinder extends BaseNodeViewBinder {
    MyCustomTextView third_level_tv;
    public ThirdLevelNodeViewBinder(View itemView) {
        super(itemView);
        third_level_tv = (MyCustomTextView) itemView.findViewById(R.id.third_level_tv);
    }


    @Override
    public int getLayoutId() {
        return R.layout.third_level;
    }

    @Override
    public void bindView(TreeNode treeNode) {
        third_level_tv.setText(treeNode.getValue().toString());
    }
}
