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

public class RoadListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;

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

    // determine which layout to use for the row
    @Override
    public int getItemViewType(int position) {
        RoadListValue item = roadListValues.get(position);
        if (item.getType() == RoadListValue.ItemType.ONE_ITEM) {
            return TYPE_ONE;
        } else if (item.getType() == RoadListValue.ItemType.TWO_ITEM) {
            return TYPE_TWO;
        } else {
            return -1;
        }
    }

    // specify the row layout file and click for each row
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ONE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vpr_screen, parent, false);
            return new ViewHolderOne(view);
        } else if (viewType == TYPE_TWO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vpr_screen_blue, parent, false);
            return new ViewHolderTwo(view);
        } else {
            throw new RuntimeException("The type has to be ONE or TWO");
        }
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int listPosition) {
        switch (holder.getItemViewType()) {
            case TYPE_ONE:
                initLayoutOne((ViewHolderOne)holder, listPosition);
                break;
            case TYPE_TWO:
                initLayoutTwo((ViewHolderTwo) holder, listPosition);
                break;
            default:
                break;
        }
    }

    private void initLayoutOne(ViewHolderOne holder, int position) {

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

    private void initLayoutTwo(ViewHolderTwo holder, int position) {

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

    // Static inner class to initialize the views of rows
    static class ViewHolderOne extends RecyclerView.ViewHolder {

        public MyCustomTextView road_code, road_name, road_village_name;

        public ViewHolderOne(View itemView) {
            super(itemView);
            road_code = (MyCustomTextView) itemView.findViewById(R.id.road_code);
            road_name = (MyCustomTextView) itemView.findViewById(R.id.road_name);
            road_village_name = (MyCustomTextView) itemView.findViewById(R.id.road_village_name);
        }
    }

    static class ViewHolderTwo extends RecyclerView.ViewHolder {
        public MyCustomTextView road_code, road_name, road_village_name;

        public ViewHolderTwo(View itemView) {
            super(itemView);
            road_code = (MyCustomTextView) itemView.findViewById(R.id.road_code);
            road_name = (MyCustomTextView) itemView.findViewById(R.id.road_name);
            road_village_name = (MyCustomTextView) itemView.findViewById(R.id.road_village_name);
        }
    }

    @Override
    public int getItemCount() {
       // return roadListValues.size();
        return roadListValues == null ? 0 : roadListValues.size();
    }



//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vpr_screen, parent, false);
//        return new MyViewHolder(itemView);
//    }

//    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private MyCustomTextView road_code, road_name, road_village_name;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            road_code = (MyCustomTextView) itemView.findViewById(R.id.road_code);
//            road_name = (MyCustomTextView) itemView.findViewById(R.id.road_name);
//            road_village_name = (MyCustomTextView) itemView.findViewById(R.id.road_village_name);
//        }
//
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//
//            }
//        }
//    }

//    @Override
//    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//
//        String string = String.valueOf(roadListValues.get(position).getRoadCategory());
//        String code = String.valueOf(roadListValues.get(position).getRoadCode());
//        String village_code = String.valueOf(roadListValues.get(position).getRoadCategoryCode());
//
//        holder.road_code.setText("R"+code);
//        holder.road_name.setText(roadListValues.get(position).getRoadName());
//
//        if(village_code.equalsIgnoreCase("2")) {
//            holder.road_village_name.setVisibility(View.VISIBLE);
//            holder.road_village_name.setText(roadListValues.get(position).getRoadVillage());
//        }
//        else {
//            holder.road_village_name.setVisibility(View.GONE);
//        }
//
//    }


}
