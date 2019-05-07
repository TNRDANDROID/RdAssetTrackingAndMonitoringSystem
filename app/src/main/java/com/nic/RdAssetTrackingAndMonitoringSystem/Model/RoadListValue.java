package com.nic.RdAssetTrackingAndMonitoringSystem.Model;

import android.graphics.Bitmap;

/**
 * Created by AchanthiSundar on 01-11-2017.
 */

public class RoadListValue {
public Integer roadCategoryCode;
public String roadCategory;
public Integer roadID;
public Integer roadCode;
public String roadName;
public String roadVillage;
private ItemType type;

public  Integer locGroup;
public  Integer locID;
public  String groupName;
public  String subgroupName;
public String colLabel;

    public String getColLabel() {
        return colLabel;
    }

    public void setColLabel(String colLabel) {
        this.colLabel = colLabel;
    }

    public Integer getLocGroup() {
        return locGroup;
    }

    public void setLocGroup(Integer locGroup) {
        this.locGroup = locGroup;
    }

    public Integer getLocID() {
        return locID;
    }

    public void setLocID(Integer locID) {
        this.locID = locID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSubgroupName() {
        return subgroupName;
    }

    public void setSubgroupName(String subgroupName) {
        this.subgroupName = subgroupName;
    }

    public enum ItemType {
        ONE_ITEM, TWO_ITEM;
    }
    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }
    public Integer getRoadCode() {
        return roadCode;
    }

    public void setRoadCode(Integer roadCode) {
        this.roadCode = roadCode;
    }

    public String getRoadVillage() {
        return roadVillage;
    }

    public void setRoadVillage(String roadVillage) {
        this.roadVillage = roadVillage;
    }

    public Integer getRoadCategoryCode() {
        return roadCategoryCode;
    }

    public void setRoadCategoryCode(Integer roadCategoryCode) {
        this.roadCategoryCode = roadCategoryCode;
    }

    public String getRoadCategory() {
        return roadCategory;
    }

    public void setRoadCategory(String roadCategory) {
        this.roadCategory = roadCategory;
    }

    public Integer getRoadID() {
        return roadID;
    }

    public void setRoadID(Integer roadID) {
        this.roadID = roadID;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }
}