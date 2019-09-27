package com.nic.RdAssetTrackingAndMonitoringSystem.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nic.RdAssetTrackingAndMonitoringSystem.Activity.CameraScreen;
import com.nic.RdAssetTrackingAndMonitoringSystem.Activity.ViewImageScreen;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PMGSYListAdapter extends PagedListAdapter<RoadListValue, PMGSYListAdapter.MyViewHolder> {

    private final dbData dbData;
    private Context context;
    private List<RoadListValue> pmgsyListValues;
  //  private PrefManager prefManager;

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
    public PMGSYListAdapter(Context context, List<RoadListValue> pmgsyListValues, dbData dbData) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.pmgsyListValues = pmgsyListValues;
        this.dbData = dbData;
  //      prefManager = new PrefManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.habitation_list, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyCustomTextView habitation_tv, view_online_image_tv, view_offline_image_tv;
        private ImageView take_photo;

        public MyViewHolder(View itemView) {
            super(itemView);
            habitation_tv = (MyCustomTextView) itemView.findViewById(R.id.habitation_tv);
            view_online_image_tv = (MyCustomTextView) itemView.findViewById(R.id.view_online_image_tv);
            view_offline_image_tv = (MyCustomTextView) itemView.findViewById(R.id.view_offline_image_tv);
            take_photo = (ImageView) itemView.findViewById(R.id.take_photo);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.habitation_tv.setText(pmgsyListValues.get(position).getPmgsyHabName());

        String image_available = pmgsyListValues.get(position).getImageAvailable();

        if (image_available.equalsIgnoreCase("Y")){
            holder.view_online_image_tv.setVisibility(View.VISIBLE);
        }
        else if (image_available.equalsIgnoreCase("N")){
            holder.view_online_image_tv.setVisibility(View.GONE);
        }

        holder.take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openphotocapture(position);
            }
        });

//        visibleOfflinebutton(String.valueOf(pmgsyListValues.get(position).getPmgsyHabcode()));

        dbData.open();
        ArrayList<RoadListValue> habitation_imageOffline = dbData.selectImage_Habitation(String.valueOf(pmgsyListValues.get(position).getPmgsyHabcode()),"0");

        if (habitation_imageOffline.size() > 0) {
           holder.view_offline_image_tv.setVisibility(View.VISIBLE);
        }
        else {
            holder.view_offline_image_tv.setVisibility(View.GONE);
        }

//        dbData.open();
//        ArrayList<RoadListValue> habitation_imageOnline = dbData.selectImage_Habitation(String.valueOf(pmgsyListValues.get(position).getPmgsyHabcode()),"1");
//
//        if (habitation_imageOnline.size() > 0) {
//            holder.view_online_image_tv.setVisibility(View.VISIBLE);
//        }
//        else {
//            holder.view_online_image_tv.setVisibility(View.GONE);
//        }

        holder.view_online_image_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isOnline()) {
                    viewImageScreen("Online", position);
                }
                else{
                    Utils.showAlert(context,"Your Internet seems to be Offline.Images can be viewed only in Online mode.");
                }
            }
        });

        holder.view_offline_image_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewImageScreen("Offline",position);
            }
        });
    }

    public void openphotocapture(int pos) {
        Integer dcode =  pmgsyListValues.get(pos).getdCode();
        Integer bcode =  pmgsyListValues.get(pos).getbCode();
        Integer pvcode =  pmgsyListValues.get(pos).getPvCode();
        Integer habcode =  pmgsyListValues.get(pos).getHabCode();
        Integer pmgsy_dcode =  pmgsyListValues.get(pos).getPmgsyDcode();
        Integer pmgsy_bcode =  pmgsyListValues.get(pos).getPmgsyBcode();
        Integer pmgsy_pvcode =  pmgsyListValues.get(pos).getPmgsyPvcode();
        Integer pmgsy_hab_code =  pmgsyListValues.get(pos).getPmgsyHabcode();

        Activity activity = (Activity) context;
        Intent intent = new Intent(context, CameraScreen.class);
        intent.putExtra(AppConstant.KEY_DCODE,String.valueOf(dcode));
        intent.putExtra(AppConstant.KEY_BCODE,String.valueOf(bcode));
        intent.putExtra(AppConstant.KEY_PVCODE,String.valueOf(pvcode));
        intent.putExtra(AppConstant.KEY_HABCODE,String.valueOf(habcode));
        intent.putExtra(AppConstant.KEY_PMGSY_DCODE,String.valueOf(pmgsy_dcode));
        intent.putExtra(AppConstant.KEY_PMGSY_BCODE,String.valueOf(pmgsy_bcode));
        intent.putExtra(AppConstant.KEY_PMGSY_PVCODE,String.valueOf(pmgsy_pvcode));
        intent.putExtra(AppConstant.KEY_PMGSY_HAB_CODE,String.valueOf(pmgsy_hab_code));
        intent.putExtra(AppConstant.KEY_SCREEN_TYPE, "Habitation");
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }

    public void viewImageScreen(String OffOntype,Integer pos) {
        Integer pmgsy_dcode =  pmgsyListValues.get(pos).getPmgsyDcode();
        Integer pmgsy_bcode =  pmgsyListValues.get(pos).getPmgsyBcode();
        Integer pmgsy_pvcode =  pmgsyListValues.get(pos).getPmgsyPvcode();
        Integer pmgsy_hab_code =  pmgsyListValues.get(pos).getPmgsyHabcode();

        Activity activity = (Activity) context;
        Intent intent = new Intent(context, ViewImageScreen.class);
        intent.putExtra(AppConstant.KEY_PMGSY_DCODE,String.valueOf(pmgsy_dcode));
        intent.putExtra(AppConstant.KEY_PMGSY_BCODE,String.valueOf(pmgsy_bcode));
        intent.putExtra(AppConstant.KEY_PMGSY_PVCODE,String.valueOf(pmgsy_pvcode));
        intent.putExtra(AppConstant.KEY_PMGSY_HAB_CODE,String.valueOf(pmgsy_hab_code));
        intent.putExtra("OffOntype", OffOntype);
        intent.putExtra(AppConstant.KEY_SCREEN_TYPE,"Habitation");
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public int getItemCount() {
        return pmgsyListValues.size();
    }

}
