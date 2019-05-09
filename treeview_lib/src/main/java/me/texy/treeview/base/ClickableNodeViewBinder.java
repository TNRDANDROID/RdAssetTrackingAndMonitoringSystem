package me.texy.treeview.base;

import android.content.Context;
import android.view.View;

import me.texy.treeview.TreeNode;

public abstract class ClickableNodeViewBinder extends BaseNodeViewBinder {


    public ClickableNodeViewBinder(Context context, View itemView) {
        super(context, itemView);
    }

    public abstract int getClickableViewId();
}
