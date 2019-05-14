package me.texy.treeview.base;

import android.content.Context;
import android.view.View;

public abstract class ClickableNodeViewBinder extends BaseNodeViewBinder {


    public ClickableNodeViewBinder(Context context, View itemView) {
        super(context, itemView);
    }

    public abstract int getClickableViewId();

    public abstract int getTextViewClickable();

    public abstract int getOfflineTextViewClickable();
}
