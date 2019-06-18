package com.nic.RdAssetTrackingAndMonitoringSystem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nic.RdAssetTrackingAndMonitoringSystem.Activity.AssetListScreen;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;

import java.util.List;

public class AssetListAdapter extends RecyclerView.Adapter<AssetListAdapter.MyViewHolder>{

    private final dbData dbData;
    private Context context;
    private List<RoadListValue> assetListValues;
    private PrefManager prefManager;

    public AssetListAdapter(Context context, List<RoadListValue> assetListValues, dbData dbData) {
        this.context = context;
        this.assetListValues = assetListValues;
        this.dbData = dbData;
        prefManager = new PrefManager(context);
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
                opensubGroup(position);
            }
        });
    }

    public void opensubGroup(int pos) {
        Integer road_id =  assetListValues.get(pos).getRoadID();
        Integer loc_grp = assetListValues.get(pos).getLocGroup();
        Integer loc_ID = assetListValues.get(pos).getLocID();
        Activity activity = (Activity) context;
        Intent intent = new Intent(context, AssetListScreen.class);
        intent.putExtra(AppConstant.KEY_ROAD_ID,String.valueOf(road_id));
        intent.putExtra(AppConstant.KEY_LOCATION_GROUP,String.valueOf(loc_grp));
        prefManager.setLocationGroup(String.valueOf(loc_grp));

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }

    @Override
    public int getItemCount() {
        return assetListValues.size();
    }

}
