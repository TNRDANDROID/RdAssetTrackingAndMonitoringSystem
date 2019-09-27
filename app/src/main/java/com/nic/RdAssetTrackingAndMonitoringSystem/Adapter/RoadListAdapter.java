package com.nic.RdAssetTrackingAndMonitoringSystem.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nic.RdAssetTrackingAndMonitoringSystem.Activity.AssetTrackingScreen;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;

import java.util.ArrayList;
import java.util.List;

public class RoadListAdapter extends PagedListAdapter<RoadListValue,RecyclerView.ViewHolder> implements Filterable {
    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;

    private final dbData dbData;
    private static Context context;
    private List<RoadListValue> roadListValues;
    private List<RoadListValue> roadListValuesFiltered;
    private PrefManager prefManager;
    private static  RoadListAdapterListener listener;
    private static DiffUtil.ItemCallback<RoadListValue> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<RoadListValue>() {
                @Override
                public boolean areItemsTheSame(RoadListValue oldItem, RoadListValue newItem) {
                    return oldItem.getId() == newItem.getId();
                }
                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(RoadListValue oldItem, RoadListValue newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public RoadListAdapter(Context context, List<RoadListValue> roadListValues,dbData dbData) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.roadListValues = roadListValues;
        this.dbData = dbData;
        prefManager = new PrefManager(context);
        this.roadListValuesFiltered = roadListValues;
    }

    // determine which layout to use for the row
    @Override
    public int getItemViewType(int position) {
        RoadListValue item = roadListValuesFiltered.get(position);
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

    private void initLayoutOne(ViewHolderOne holder, final int position) {

        String string = String.valueOf(roadListValuesFiltered.get(position).getRoadCategory());
        String code = roadListValuesFiltered.get(position).getRoadCode();
        String village_code = String.valueOf(roadListValuesFiltered.get(position).getRoadCategoryCode());

        holder.road_code.setText("R"+code);
        holder.road_name.setText(roadListValuesFiltered.get(position).getRoadName());

        if (roadListValuesFiltered.get(position).getState().equalsIgnoreCase("completed")) {
            holder.imageView.setBackgroundResource(R.mipmap.tick_mark);
        } else if (roadListValuesFiltered.get(position).getState().equalsIgnoreCase("Not_Started")) {
            holder.imageView.setBackgroundResource(R.mipmap.error);
        } else {
            holder.imageView.setBackgroundResource(R.mipmap.warning);
        }

        if(village_code.equalsIgnoreCase("2")) {
            holder.road_village_name.setVisibility(View.VISIBLE);
            holder.road_village_name.setText(roadListValuesFiltered.get(position).getRoadVillage());
        }
        else {
            holder.road_village_name.setVisibility(View.GONE);
        }

        holder.road_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<RoadListValue> roadListLocal = dbData.getParticularRoadTrackInfo(String.valueOf(roadListValuesFiltered.get(position).getRoadID()));

                if(roadListLocal.size() < 1) {
                    openAssetList(position);
                }
                else {
                   showAlert(position);
                }

            }
        });


    }

    public void showAlert(final int position) {
        String Alert = "This road has been already tracked and data found in local database. Do you want to continue?";

        new AlertDialog.Builder(context)
                .setTitle("WARNING")
                .setMessage(Alert)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        openAssetList(position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                }).show();

    }

    private void initLayoutTwo(ViewHolderTwo holder, final int position) {

        String string = String.valueOf(roadListValuesFiltered.get(position).getRoadCategory());
        String code = roadListValuesFiltered.get(position).getRoadCode();
        String village_code = String.valueOf(roadListValuesFiltered.get(position).getRoadCategoryCode());

        holder.road_code.setText("R"+code);
        holder.road_name.setText(roadListValuesFiltered.get(position).getRoadName());
        if (roadListValuesFiltered.get(position).getState().equalsIgnoreCase("completed")) {
            holder.imageView.setBackgroundResource(R.mipmap.tick_mark);
        } else if (roadListValuesFiltered.get(position).getState().equalsIgnoreCase("Not_Started")) {
            holder.imageView.setBackgroundResource(R.mipmap.error);
        } else {
            holder.imageView.setBackgroundResource(R.mipmap.warning);
        }
        if(village_code.equalsIgnoreCase("2")) {
            holder.road_village_name.setVisibility(View.VISIBLE);
            holder.road_village_name.setText(roadListValuesFiltered.get(position).getRoadVillage());
        }
        else {
            holder.road_village_name.setVisibility(View.GONE);
        }

        holder.road_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAssetList(position);
            }
        });
    }

    // Static inner class to initialize the views of rows
    static class ViewHolderOne extends RecyclerView.ViewHolder {

        public MyCustomTextView road_code, road_name, road_village_name;
        RelativeLayout road_layout;
        private ImageView imageView;

        public ViewHolderOne(View itemView) {
            super(itemView);
            road_code = (MyCustomTextView) itemView.findViewById(R.id.road_code);
            road_name = (MyCustomTextView) itemView.findViewById(R.id.road_name);
            road_village_name = (MyCustomTextView) itemView.findViewById(R.id.road_village_name);
            road_layout = (RelativeLayout) itemView.findViewById(R.id.road_layout);
            imageView = (ImageView)itemView. findViewById(R.id.imageView);

        }
    }

    static class ViewHolderTwo extends RecyclerView.ViewHolder {
        public MyCustomTextView road_code, road_name, road_village_name;
        RelativeLayout road_layout;
        private ImageView imageView;

        public ViewHolderTwo(View itemView) {
            super(itemView);
            road_code = (MyCustomTextView) itemView.findViewById(R.id.road_code);
            road_name = (MyCustomTextView) itemView.findViewById(R.id.road_name);
            road_village_name = (MyCustomTextView) itemView.findViewById(R.id.road_village_name);
            road_layout = (RelativeLayout) itemView.findViewById(R.id.road_layout);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    @Override
    public int getItemCount() {
       // return roadListValues.size();
        return roadListValuesFiltered == null ? 0 : roadListValuesFiltered.size();
    }

    public void openAssetList(int pos) {
        Integer road_id =  roadListValuesFiltered.get(pos).getRoadID();
        String road_name = roadListValuesFiltered.get(pos).getRoadName();
        String road_category = roadListValuesFiltered.get(pos).getRoadCategory();
        String road_state = roadListValuesFiltered.get(pos).getState();
        Activity activity = (Activity) context;
        Intent intent = new Intent(context, AssetTrackingScreen.class);
        intent.putExtra(AppConstant.KEY_ROAD_ID,String.valueOf(road_id));
        intent.putExtra(AppConstant.KEY_ROAD_NAME,road_name);
        intent.putExtra(AppConstant.KEY_ROAD_CATEGORY,road_category);
        prefManager.setRoadId(String.valueOf(road_id));
        prefManager.setRoadCategoty(road_category);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    roadListValuesFiltered = roadListValues;
                } else {
                    List<RoadListValue> filteredList = new ArrayList<>();
                    for (RoadListValue row : roadListValues) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getRoadName().toLowerCase().contains(charString.toLowerCase()) || row.getRoadCode().contains(charString)) {
                            filteredList.add(row);
                        }
                    }

                    roadListValuesFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = roadListValuesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                roadListValuesFiltered = (ArrayList<RoadListValue>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface RoadListAdapterListener {
        void onContactSelected(RoadListValue contact);
    }
}
