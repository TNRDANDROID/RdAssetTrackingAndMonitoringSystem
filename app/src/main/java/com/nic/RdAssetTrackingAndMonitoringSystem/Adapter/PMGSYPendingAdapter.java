package com.nic.RdAssetTrackingAndMonitoringSystem.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;

import java.util.List;

public class PMGSYPendingAdapter extends RecyclerView.Adapter<PMGSYPendingAdapter.MyViewHolder> {

    private dbData dbData;
    private Context context;
    private List<RoadListValue> pmgsyListValues;
    private PrefManager prefManager;

    public PMGSYPendingAdapter(Context context, List<RoadListValue> pmgsyListValues, dbData dbData) {
        this.context = context;
        this.pmgsyListValues = pmgsyListValues;
        this.dbData = dbData;
        prefManager = new PrefManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pmgsy_pending_screen_adapter, parent, false);
        return new PMGSYPendingAdapter.MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView habitation_tv, village_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            habitation_tv = (TextView) itemView.findViewById(R.id.hab_name);
            village_tv = (TextView) itemView.findViewById(R.id.village_name);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
    }


    @Override
    public int getItemCount() {
        return 20;
    }

}

