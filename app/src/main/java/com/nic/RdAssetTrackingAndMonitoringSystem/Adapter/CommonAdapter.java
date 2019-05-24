package com.nic.RdAssetTrackingAndMonitoringSystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;

import java.util.List;

public class CommonAdapter extends BaseAdapter {
    private List<RoadListValue> roadListValues;
    private Context mContext;
    private String type;


    public CommonAdapter(Context mContext, List<RoadListValue> roadListValues, String type) {
        this.roadListValues = roadListValues;
        this.mContext = mContext;
        this.type = type;
    }


    public int getCount() {
        return roadListValues.size();
    }


    public Object getItem(int position) {
        return position;
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.spinner_value, parent, false);

        TextView tv_type = (TextView) view.findViewById(R.id.spinner_list_value);

        RoadListValue roadList = roadListValues.get(position);
        if(type.equalsIgnoreCase("PMGSYVillage")) {
            tv_type.setText(roadList.getPmgsyPvname());
        }
        return view;
    }
}
