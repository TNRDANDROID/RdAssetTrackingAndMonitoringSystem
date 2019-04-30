package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.VolleyError;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.Api;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ServerResponse;

public class VPRScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {


    @Override
    public void onClick(View v) {

    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {

    }

    @Override
    public void OnError(VolleyError volleyError) {

    }
}
