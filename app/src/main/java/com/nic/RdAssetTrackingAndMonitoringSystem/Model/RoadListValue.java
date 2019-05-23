package com.nic.RdAssetTrackingAndMonitoringSystem.Model;

import android.graphics.Bitmap;

import java.sql.Blob;

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
private Integer totalAsset;
private Integer assetCapturedCount;
private Integer totalStartPoint;
private Integer totalMidPoint;
private Integer totalEndPoint;
private String state;

public  Integer locGroup;
public  Integer locID;
public  String groupName;
public  String subgroupName;
public String colLabel;
public String locationDetails;
public String levelType;
public String pointType;
public String roadLat;
public String roadLong;
public String assetId;
public Bitmap Image;

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public String getRoadLat() {
        return roadLat;
    }

    public void setRoadLat(String roadLat) {
        this.roadLat = roadLat;
    }

    public String getRoadLong() {
        return roadLong;
    }

    public void setRoadLong(String roadLong) {
        this.roadLong = roadLong;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String createdDate;

    public String getLevelType() {
        return levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

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

    public Integer getTotalAsset() {
        return totalAsset;
    }

    public void setTotalAsset(Integer totalAsset) {
        this.totalAsset = totalAsset;
    }

    public Integer getAssetCapturedCount() {
        return assetCapturedCount;
    }

    public void setAssetCapturedCount(Integer assetCapturedCount) {
        this.assetCapturedCount = assetCapturedCount;
    }

    public Integer getTotalMidPoint() {
        return totalMidPoint;
    }

    public void setTotalMidPoint(Integer totalMidPoint) {
        this.totalMidPoint = totalMidPoint;
    }

    public Integer getTotalEndPoint() {
        return totalEndPoint;
    }

    public void setTotalEndPoint(Integer totalEndPoint) {
        this.totalEndPoint = totalEndPoint;
    }

    public Integer getTotalStartPoint() {
        return totalStartPoint;
    }

    public void setTotalStartPoint(Integer totalStartPoint) {
        this.totalStartPoint = totalStartPoint;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}