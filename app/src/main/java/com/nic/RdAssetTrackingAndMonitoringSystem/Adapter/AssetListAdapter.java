package com.nic.RdAssetTrackingAndMonitoringSystem.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;

import java.util.List;

public class AssetListAdapter extends RecyclerView.Adapter<AssetListAdapter.MyViewHolder>{

    private final dbData dbData;
    private Context context;
 //   private List<RoadListValue> assetListValues;
    private List<String> ListValues;
  //  private PrefManager prefManager;

    public AssetListAdapter(Context context, List<String> assetListValues, dbData dbData) {
        this.context = context;
        this.ListValues = assetListValues;
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

        public MyViewHolder(View itemView) {
            super(itemView);
            asset_groupName = (MyCustomTextView) itemView.findViewById(R.id.asset_groupName);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.asset_groupName.setText(ListValues.get(position));
    }

    @Override
    public int getItemCount() {
        return ListValues.size();
    }

}
