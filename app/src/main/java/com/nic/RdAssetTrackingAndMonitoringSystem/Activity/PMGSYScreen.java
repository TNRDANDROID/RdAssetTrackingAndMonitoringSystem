package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.Api;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ServerResponse;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;

public class PMGSYScreen extends AppCompatActivity implements Api.ServerResponseListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pmgsy_layout);
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {

    }

    @Override
    public void OnError(VolleyError volleyError) {

    }
}
