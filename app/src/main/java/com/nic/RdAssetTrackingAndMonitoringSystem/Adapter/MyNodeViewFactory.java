package com.nic.RdAssetTrackingAndMonitoringSystem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import me.texy.treeview.base.BaseNodeViewBinder;
import me.texy.treeview.base.BaseNodeViewFactory;


/**
 * Created by zxy on 17/4/23.
 */

public class MyNodeViewFactory extends BaseNodeViewFactory {
private Activity activity;
    @Override
    public BaseNodeViewBinder getNodeViewBinder(Context context,View view, int level) {
        switch (level) {
         //   case 0:
//                return new FirstLevelNodeViewBinder(view);
            case 0:
                return new SecondLevelNodeViewBinder(context,view);
            case 1:
                return new ThirdLevelNodeViewBinder(context,view);
            default:
                return null;
        }
    }
}
