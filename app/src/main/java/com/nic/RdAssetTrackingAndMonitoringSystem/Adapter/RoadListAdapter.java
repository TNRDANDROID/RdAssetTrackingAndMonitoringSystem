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

public class RoadListAdapter extends RecyclerView.Adapter<RoadListAdapter.MyViewHolder>{
    private final dbData dbData;
    private Context context;
    private List<RoadListValue> roadListValues;
  //  private PrefManager prefManager;

    public RoadListAdapter(Context context, List<RoadListValue> roadListValues,dbData dbData) {
        this.context = context;
        this.roadListValues = roadListValues;
        this.dbData = dbData;
  //      prefManager = new PrefManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vpr_screen, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyCustomTextView road_code, road_name, road_village_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            road_code = (MyCustomTextView) itemView.findViewById(R.id.road_code);
            road_name = (MyCustomTextView) itemView.findViewById(R.id.road_name);
            road_village_name = (MyCustomTextView) itemView.findViewById(R.id.road_village_name);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        String string = String.valueOf(roadListValues.get(position).getRoadCategory());
        String code = String.valueOf(roadListValues.get(position).getRoadCode());
        String village_code = String.valueOf(roadListValues.get(position).getRoadCategoryCode());

        holder.road_code.setText("R"+code);
        holder.road_name.setText(roadListValues.get(position).getRoadName());

        if(village_code.equalsIgnoreCase("2")) {
            holder.road_village_name.setVisibility(View.VISIBLE);
            holder.road_village_name.setText(roadListValues.get(position).getRoadVillage());
        }
        else {
            holder.road_village_name.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return roadListValues.size();
    }

}
