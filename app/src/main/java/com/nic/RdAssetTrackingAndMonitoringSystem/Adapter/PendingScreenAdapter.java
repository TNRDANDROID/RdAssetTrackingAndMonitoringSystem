package com.nic.RdAssetTrackingAndMonitoringSystem.Adapter;

import android.annotation.SuppressLint;
import android.arch.paging.PagedListAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nic.RdAssetTrackingAndMonitoringSystem.Activity.PendingScreen;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.DBHelper;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PendingScreenAdapter extends PagedListAdapter<RoadListValue, PendingScreenAdapter.MyViewHolder> {

    private final dbData dbData;
    private Context context;
    private List<RoadListValue> pendingListValues;
    JSONObject datasetTrack = new JSONObject();
    JSONObject datasetAsset = new JSONObject();
    JSONObject datasetBridges = new JSONObject();
    JSONArray prefCulvertidArray = new JSONArray();
    static PrefManager prefManager;

    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
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

    public PendingScreenAdapter(Context context, List<RoadListValue> pendingListValues, dbData dbData) {
        super(DIFF_CALLBACK);
        this.context = context;
        prefManager = new PrefManager(context);
        this.pendingListValues = pendingListValues;
        this.dbData = dbData;
        try {
            dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //      prefManager = new PrefManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_screen_adpater, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView road_name, road_code, village_name,sync_track_info,sync_asset_info,sync_culverts_info;
        private TextView del_track_info,del_asset_info,Del_culverts_info;
        private LinearLayout third_layout_village;
        private RelativeLayout sync_delete_track_layout,sync_del_asset_layout,sync_delete_bridges_layout;
        private View village_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            road_name = (TextView) itemView.findViewById(R.id.road_name);
            road_code = (TextView) itemView.findViewById(R.id.road_code);
            village_name = (TextView) itemView.findViewById(R.id.village_name);
            sync_track_info = (TextView) itemView.findViewById(R.id.sync_track_info);
            sync_asset_info = (TextView) itemView.findViewById(R.id.sync_asset_info);
            sync_culverts_info = (TextView) itemView.findViewById(R.id.sync_culverts_info);

            del_track_info = (TextView) itemView.findViewById(R.id.del_track_info);
            del_asset_info = (TextView) itemView.findViewById(R.id.del_asset_info);
            Del_culverts_info = (TextView) itemView.findViewById(R.id.Del_culverts_info);

            sync_delete_track_layout = (RelativeLayout) itemView.findViewById(R.id.sync_delete_track_layout);
            sync_del_asset_layout = (RelativeLayout) itemView.findViewById(R.id.sync_del_asset_layout);
            sync_delete_bridges_layout = (RelativeLayout) itemView.findViewById(R.id.sync_delete_bridges_layout);
            third_layout_village = (LinearLayout) itemView.findViewById(R.id.third_layout_village);
            village_view = (View) itemView.findViewById(R.id.village_view);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        dbData.open();
        if(!pendingListValues.get(position).getRoadVillage().equals("")) {
            holder.third_layout_village.setVisibility(View.VISIBLE);
            holder.village_view.setVisibility(View.VISIBLE);
            holder.village_name.setText(pendingListValues.get(position).getRoadVillage());
        }else {
            holder.third_layout_village.setVisibility(View.GONE);
            holder.village_view.setVisibility(View.GONE);
        }

       holder.road_code.setText("R"+pendingListValues.get(position).getRoadCode());
       holder.road_name.setText(pendingListValues.get(position).getRoadName());

       final Integer road_id = pendingListValues.get(position).getRoadID();

        ArrayList<RoadListValue> roadListLocal = dbData.getParticularRoadTrackInfo(String.valueOf(road_id));

        if(roadListLocal.size() < 1) {
            holder.sync_delete_track_layout.setVisibility(View.GONE);
        }
        else {
            holder.sync_delete_track_layout.setVisibility(View.VISIBLE);
        }

        ArrayList<RoadListValue> assetListLocal = dbData.getParticularAssetInfo(String.valueOf(road_id));

        if(assetListLocal.size() < 1) {
            holder.sync_del_asset_layout.setVisibility(View.GONE);
        }
        else {
            holder.sync_del_asset_layout.setVisibility(View.VISIBLE);
        }

        ArrayList<RoadListValue> bridgesListLocal = dbData.getParticularBridgesInfo(String.valueOf(road_id),"0","upload");

        if(bridgesListLocal.size() < 1) {
            holder.sync_delete_bridges_layout.setVisibility(View.GONE);
        }
        else {
            holder.sync_delete_bridges_layout.setVisibility(View.VISIBLE);
        }

        holder.sync_track_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isOnline()) {
                    new toUploadTrackTask().execute(road_id);
                    prefManager.setKeyDeleteId(String.valueOf(road_id));
                    prefManager.setKeyClickedPosition(String.valueOf(position));
                } else {
                    Utils.showAlert(context, "Turn On Mobile Data To Synchronize!");
                }

            }
        });
        holder.sync_asset_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isOnline()) {
                    new toUploadAssetTask().execute(road_id);
                    prefManager.setKeyDeleteId(String.valueOf(road_id));
                    prefManager.setKeyClickedPosition(String.valueOf(position));
                } else {
                    Utils.showAlert(context, "Turn On Mobile Data To Synchronize!");
                }
            }
        });
        holder.sync_culverts_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isOnline()) {
                    new toUploadBridges().execute(road_id);
                    prefManager.setKeyDeleteId(String.valueOf(road_id));
                    prefManager.setKeyClickedPosition(String.valueOf(position));
                } else {
                    Utils.showAlert(context, "Turn On Mobile Data To Synchronize!");
                }
            }
        });

        holder.del_track_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTrack(road_id,position);
            }
        });
        holder.del_asset_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAsset(road_id,position);
            }
        });
        holder.Del_culverts_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               deleteCulverts(road_id,position);
            }
        });
    }

    public class toUploadTrackTask extends AsyncTask<Integer, Void,
            JSONObject> {
        @Override
        protected JSONObject doInBackground(Integer... params) {
            try {
                dbData.open();
                ArrayList<RoadListValue> saveLatLongLists = dbData.getParticularRoadTrackInfo(String.valueOf(params[0]));
                JSONArray saveLatLongArray = new JSONArray();
                if (saveLatLongLists.size() > 0) {
                    for (int i = 0; i < saveLatLongLists.size(); i++) {
                        JSONObject latLongData = new JSONObject();
                        latLongData.put(AppConstant.KEY_ROAD_CATEGORY, saveLatLongLists.get(i).getRoadCategory());
                        latLongData.put(AppConstant.KEY_ROAD_ID, saveLatLongLists.get(i).getRoadID());
                        latLongData.put(AppConstant.KEY_POINT_TYPE, saveLatLongLists.get(i).getPointType());
                        latLongData.put(AppConstant.KEY_ROAD_LAT, saveLatLongLists.get(i).getRoadLat());
                        latLongData.put(AppConstant.KEY_ROAD_LONG, saveLatLongLists.get(i).getRoadLong());
                        latLongData.put(AppConstant.KEY_CREATED_DATE, saveLatLongLists.get(i).getCreatedDate());

                        saveLatLongArray.put(latLongData);
                    }
                }

                datasetTrack = new JSONObject();
                try {
                    datasetTrack.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_ROAD_TRACK_SAVE);
                    datasetTrack.put(AppConstant.KEY_TRACK_DATA, saveLatLongArray);

//                    String authKey = datasetTrack.toString();
//                    int maxLogSize = 2000;
//                    for (int i = 0; i <= authKey.length() / maxLogSize; i++) {
//                        int start = i * maxLogSize;
//                        int end = (i + 1) * maxLogSize;
//                        end = end > authKey.length() ? authKey.length() : end;
//                        Log.v("to_send_plain", authKey.substring(start, end));
//                    }
//
//                    String authKey1 = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), saveLatLongData.toString());
//
//                    for(int i = 0; i <= authKey1.length() / maxLogSize; i++) {
//                        int start = i * maxLogSize;
//                        int end = (i+1) * maxLogSize;
//                        end = end > authKey.length() ? authKey1.length() : end;
//                        Log.v("to_send_encryt", authKey1.substring(start, end));
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return datasetTrack;
        }

        protected void onPostExecute(JSONObject dataset) {
            super.onPostExecute(dataset);
            ((PendingScreen)context).syncData_Track(dataset);
        }
    }

    public class toUploadAssetTask extends AsyncTask<Integer, Void,
            JSONObject> {
        @Override
        protected JSONObject doInBackground(Integer... params) {
            dbData.open();
            JSONArray track_data = new JSONArray();
            ArrayList<RoadListValue> assets = dbData.getParticularAssetInfo(String.valueOf(params[0]));

            if (assets.size() > 0) {
                for (int i = 0; i < assets.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(AppConstant.KEY_ROAD_CATEGORY,assets.get(i).getRoadCategory());
                        jsonObject.put(AppConstant.KEY_ROAD_ID,assets.get(i).getRoadID());
                        jsonObject.put(AppConstant.KEY_ASSET_ID,assets.get(i).getAssetId());
                        jsonObject.put(AppConstant.KEY_ROAD_LAT,assets.get(i).getRoadLat());
                        jsonObject.put(AppConstant.KEY_ROAD_LONG,assets.get(i).getRoadLong());

                        Bitmap bitmap = assets.get(i).getImage();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                        byte[] imageInByte = baos.toByteArray();
                        String image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

                        jsonObject.put(AppConstant.KEY_IMAGES,image_str);

                        track_data.put(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                datasetAsset = new JSONObject();

                try {
                    datasetAsset.put(AppConstant.KEY_SERVICE_ID,AppConstant.KEY_ROAD_TRACK_ASSET_SAVE);
                    datasetAsset.put(AppConstant.KEY_TRACK_DATA,track_data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return datasetAsset;
        }

        @Override
        protected void onPostExecute(JSONObject dataset) {
            super.onPostExecute(dataset);
           ((PendingScreen)context).syncData_Asset(dataset);
        }
    }

    public class toUploadBridges extends AsyncTask<Integer, Void,
            JSONObject> {
        @Override
        protected JSONObject doInBackground(Integer... params) {
            dbData.open();
            JSONArray BridgeArray = new JSONArray();
            prefCulvertidArray = new JSONArray();
            ArrayList<RoadListValue> Bridges = dbData.getParticularBridgesInfo(String.valueOf(params[0]),"0","upload");

            if (Bridges.size() > 0) {
                for (int i = 0; i < Bridges.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(AppConstant.KEY_DATA_TYPE, Bridges.get(i).getDataType());
                        jsonObject.put(AppConstant.KEY_LOCATION_GROUP, Bridges.get(i).getLocGroup());
                        jsonObject.put(AppConstant.KEY_LOCATION_ID, Bridges.get(i).getLocID());
                        jsonObject.put(AppConstant.KEY_DCODE, Bridges.get(i).getdCode());
                        jsonObject.put(AppConstant.KEY_BCODE, Bridges.get(i).getbCode());
                        jsonObject.put(AppConstant.KEY_PVCODE, Bridges.get(i).getPvCode());
                        jsonObject.put(AppConstant.KEY_ROAD_ID, Bridges.get(i).getRoadID());
                        jsonObject.put(AppConstant.KEY_CULVERT_TYPE, Bridges.get(i).getCulvertType());
                        jsonObject.put(AppConstant.KEY_CULVERT_TYPE_NAME, Bridges.get(i).getCulvertTypeName());
                        jsonObject.put(AppConstant.KEY_CHAINAGE, Bridges.get(i).getChainage());
                        jsonObject.put(AppConstant.KEY_CULVERT_NAME, Bridges.get(i).getCulvertName());
                        jsonObject.put(AppConstant.KEY_SPAN, Bridges.get(i).getSpan());
                        jsonObject.put(AppConstant.KEY_NO_OF_SPAN, Bridges.get(i).getNoOfSpan());
                        jsonObject.put(AppConstant.KEY_WIDTH, Bridges.get(i).getWidth());
                        jsonObject.put(AppConstant.KEY_VENT_HEIGHT, Bridges.get(i).getVentHeight());
                        jsonObject.put(AppConstant.KEY_LENGTH, Bridges.get(i).getLength());
                        jsonObject.put(AppConstant.KEY_CULVERT_ID, Bridges.get(i).getCulvertId());
                        jsonObject.put(AppConstant.KEY_ROAD_LAT, Bridges.get(i).getStartLat());
                        jsonObject.put(AppConstant.KEY_ROAD_LONG, Bridges.get(i).getStartLong());

                        Bitmap bitmap = Bridges.get(i).getImage();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                        byte[] imageInByte = baos.toByteArray();
                        String image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

                        jsonObject.put(AppConstant.KEY_IMAGES, image_str);
                        BridgeArray.put(jsonObject);

                        prefCulvertidArray.put(Bridges.get(i).getCulvertId());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                datasetBridges = new JSONObject();

                try {
                    datasetBridges.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_BRIDGES_CULVERT_SAVE);
                    datasetBridges.put(AppConstant.KEY_TRACK_DATA, BridgeArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return datasetBridges;
        }

        @Override
        protected void onPostExecute(JSONObject dataset) {
            super.onPostExecute(dataset);
           ((PendingScreen)context).syncDataBridges(dataset);
           prefManager.setLocalSaveCulvertIdJsonList(prefCulvertidArray);

        }
    }


    public void deleteTrack(int road_id,int position){

        long id = db.delete(DBHelper.SAVE_LAT_LONG_TABLE,"road_id=?",new String[] {String.valueOf(road_id)});

        if(id > 0){
            Utils.showAlert(context,"Deleted");
            notifyDataSetChanged();
            itemRemove(road_id,position);
        }
    }
    public void deleteAsset(int road_id,int position){
        long id = db.delete(DBHelper.SAVE_IMAGE_LAT_LONG_TABLE,"road_id=?",new String[] {String.valueOf(road_id)});

        if(id > 0){
            Utils.showAlert(context,"Deleted");
            notifyDataSetChanged();
            itemRemove(road_id,position);
        }
    }
    public void deleteCulverts(int road_id,int position){
        ContentValues values = new ContentValues();
        values.put("image_flag",1);
        long id = db.update(DBHelper.BRIDGES_CULVERT,values,"road_id=? and image_flag = ?",new String[] {String.valueOf(road_id),"0"});

        if(id > 0){
            Utils.showAlert(context,"Deleted");
            notifyDataSetChanged();
            itemRemove(road_id,position);
        }
    }

    public void itemRemove(int road_id,int position){
        dbData.open();
        ArrayList<RoadListValue> bridgesListLocal = dbData.getParticularBridgesInfo(String.valueOf(road_id),"0","upload");
        ArrayList<RoadListValue> assetListLocal = dbData.getParticularAssetInfo(String.valueOf(road_id));
        ArrayList<RoadListValue> roadListLocal = dbData.getParticularRoadTrackInfo(String.valueOf(road_id));


        if(roadListLocal.size() == 0 && assetListLocal.size() == 0 && bridgesListLocal.size() == 0 ){
            pendingListValues.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
            notifyItemChanged(position,pendingListValues.size());
        }

    }

    @Override
    public int getItemCount() {
        return pendingListValues.size();
    }
}
