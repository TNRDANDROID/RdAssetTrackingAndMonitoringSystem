package com.nic.RdAssetTrackingAndMonitoringSystem.Model;

import android.graphics.Bitmap;

/**
 * Created by AchanthiSundar on 01-11-2017.
 */

public class RoadListValue {
public String roadCategoryCode;
public String roadCategory;
public String roadID;
public String roadName;

    public String getRoadCategoryCode() {
        return roadCategoryCode;
    }

    public void setRoadCategoryCode(String roadCategoryCode) {
        this.roadCategoryCode = roadCategoryCode;
    }

    public String getRoadCategory() {
        return roadCategory;
    }

    public void setRoadCategory(String roadCategory) {
        this.roadCategory = roadCategory;
    }

    public String getRoadID() {
        return roadID;
    }

    public void setRoadID(String roadID) {
        this.roadID = roadID;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }
}