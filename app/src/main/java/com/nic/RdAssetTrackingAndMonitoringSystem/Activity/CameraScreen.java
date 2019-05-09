package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nic.RdAssetTrackingAndMonitoringSystem.R;

public class CameraScreen extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_with_description);
    }

    @Override
    public void onClick(View v) {

    }
}
