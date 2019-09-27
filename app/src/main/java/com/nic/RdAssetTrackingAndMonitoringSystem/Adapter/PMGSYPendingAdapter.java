package com.nic.RdAssetTrackingAndMonitoringSystem.Adapter;

import android.annotation.SuppressLint;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nic.RdAssetTrackingAndMonitoringSystem.Activity.PMGSYPendingScreen;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.DBHelper;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.Utils;

import java.util.List;

public class PMGSYPendingAdapter extends PagedListAdapter<RoadListValue,PMGSYPendingAdapter.MyViewHolder> {

    private dbData dbData;
    private Context context;
    private List<RoadListValue> pmgsyListValues;
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;

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

    public PMGSYPendingAdapter(Context context, List<RoadListValue> pmgsyListValues, dbData dbData) {
        super(DIFF_CALLBACK);
        try {
            dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        private TextView habitation_tv, village_tv,delete;
        private RelativeLayout upload;

        public MyViewHolder(View itemView) {
            super(itemView);
            habitation_tv = (TextView) itemView.findViewById(R.id.hab_name);
            village_tv = (TextView) itemView.findViewById(R.id.village_name);
            upload = (RelativeLayout) itemView.findViewById(R.id.upload);
            delete = (TextView) itemView.findViewById(R.id.delete);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.habitation_tv.setText(String.valueOf(pmgsyListValues.get(position).getPmgsyHabName()));
        holder.village_tv.setText(String.valueOf(pmgsyListValues.get(position).getPmgsyPvname()));
        final int pmgsy_dcode = pmgsyListValues.get(position).getPmgsyDcode();
        final int pmgsy_bcode = pmgsyListValues.get(position).getPmgsyBcode();
        final int pmgsy_pvcode = pmgsyListValues.get(position).getPmgsyPvcode();
        final int pmgsy_hab_code = pmgsyListValues.get(position).getPmgsyHabcode();
        holder.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isOnline()) {
                    RoadListValue roadListValue = new RoadListValue();
                    roadListValue.setPmgsyDcode(pmgsy_dcode);
                    roadListValue.setPmgsyBcode(pmgsy_bcode);
                    roadListValue.setPmgsyPvcode(pmgsy_pvcode);
                    roadListValue.setPmgsyHabcode(pmgsy_hab_code);
                    prefManager.setKeyPmgsyDcode(String.valueOf(pmgsy_dcode));
                    prefManager.setKeyPmgsyBcode(String.valueOf(pmgsy_bcode));
                    prefManager.setKeyPmgsyPvcode(String.valueOf(pmgsy_pvcode));
                    prefManager.setKeyPmgsyHabcode(String.valueOf(pmgsy_hab_code));
                    prefManager.setKeyPmgsyDeleteId(String.valueOf(position));
                    ((PMGSYPendingScreen) context).new toUploadHabitation().execute(roadListValue);
                } else {
                    Utils.showAlert(context, "Turn On Mobile Data To Synchronize!");
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deletePending(position);
            }
        });
    }

    public void deletePending(int position) {
        int pmgsy_dcode = pmgsyListValues.get(position).getPmgsyDcode();
        int pmgsy_bcode = pmgsyListValues.get(position).getPmgsyBcode();
        int pmgsy_pvcode = pmgsyListValues.get(position).getPmgsyPvcode();
        int pmgsy_hab_code = pmgsyListValues.get(position).getPmgsyHabcode();
        int sdsm = db.delete(DBHelper.SAVE_IMAGE_HABITATION_TABLE, "pmgsy_dcode = ? and pmgsy_bcode = ? and pmgsy_pvcode = ? and pmgsy_hab_code = ?", new String[]{String.valueOf(pmgsy_dcode), String.valueOf(pmgsy_bcode), String.valueOf(pmgsy_pvcode), String.valueOf(pmgsy_hab_code)});
        pmgsyListValues.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, pmgsyListValues.size());
    }

    @Override
    public int getItemCount() {
        return pmgsyListValues.size();
    }

}

