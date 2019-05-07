package com.nic.RdAssetTrackingAndMonitoringSystem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nic.RdAssetTrackingAndMonitoringSystem.Activity.AssetListScreen;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;

import java.util.List;

public class AssetListAdapter extends RecyclerView.Adapter<AssetListAdapter.MyViewHolder>{

    private final dbData dbData;
    private Context context;
    private List<RoadListValue> assetListValues;
  //  private PrefManager prefManager;

    public AssetListAdapter(Context context, List<RoadListValue> assetListValues, dbData dbData) {
        this.context = context;
        this.assetListValues = assetListValues;
        this.dbData = dbData;
  //      prefManager = new PrefManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inner_asset_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyCustomTextView asset_groupName;
        LinearLayout grpName_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            asset_groupName = (MyCustomTextView) itemView.findViewById(R.id.asset_groupName);
            grpName_layout = (LinearLayout) itemView.findViewById(R.id.grpName_layout);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.asset_groupName.setText(assetListValues.get(position).getGroupName());

        holder.grpName_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(context,AssetListScreen.class);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assetListValues.size();
    }

}
