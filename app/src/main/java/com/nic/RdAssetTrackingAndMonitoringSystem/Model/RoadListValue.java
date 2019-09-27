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
public String roadCode;
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

public Integer pmgsyDcode;
public Integer pmgsyBcode;
public Integer pmgsyPvcode;
public String pmgsyPvname;
public Integer pmgsyHabcode;
public String pmgsyHabName;

public Integer dCode;
public Integer bCode;
public Integer pvCode;
public Integer habCode;
private String remark;
private String serverFlag;

private String dataType;
private Integer culvertType;
private String  culvertTypeName;
private Integer chainage;
private String culvertName;
private Integer span;
private Integer noOfSpan;
private Integer width;
private Integer ventHeight;
private Integer length;
private Integer culvertId;
private String startLat;
private String startLong;
private String imageAvailable;

    public String getImageAvailable() {
        return imageAvailable;
    }

    public void setImageAvailable(String imageAvailable) {
        this.imageAvailable = imageAvailable;
    }

    public String getCulvertTypeName() {
        return culvertTypeName;
    }

    public void setCulvertTypeName(String culvertTypeName) {
        this.culvertTypeName = culvertTypeName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getCulvertType() {
        return culvertType;
    }

    public void setCulvertType(Integer culvertType) {
        this.culvertType = culvertType;
    }

    public Integer getChainage() {
        return chainage;
    }

    public void setChainage(Integer chainage) {
        this.chainage = chainage;
    }

    public String getCulvertName() {
        return culvertName;
    }

    public void setCulvertName(String culvertName) {
        this.culvertName = culvertName;
    }

    public Integer getSpan() {
        return span;
    }

    public void setSpan(Integer span) {
        this.span = span;
    }

    public Integer getNoOfSpan() {
        return noOfSpan;
    }

    public void setNoOfSpan(Integer noOfSpan) {
        this.noOfSpan = noOfSpan;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getVentHeight() {
        return ventHeight;
    }

    public void setVentHeight(Integer ventHeight) {
        this.ventHeight = ventHeight;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getCulvertId() {
        return culvertId;
    }

    public void setCulvertId(Integer culvertId) {
        this.culvertId = culvertId;
    }

    public String getStartLat() {
        return startLat;
    }

    public void setStartLat(String startLat) {
        this.startLat = startLat;
    }

    public String getStartLong() {
        return startLong;
    }

    public void setStartLong(String startLong) {
        this.startLong = startLong;
    }

    public String getServerFlag() {
        return serverFlag;
    }

    public void setServerFlag(String serverFlag) {
        this.serverFlag = serverFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String imageDescription;

    public Integer getPmgsyDcode() {
        return pmgsyDcode;
    }

    public void setPmgsyDcode(Integer pmgsyDcode) {
        this.pmgsyDcode = pmgsyDcode;
    }

    public Integer getPmgsyBcode() {
        return pmgsyBcode;
    }

    public void setPmgsyBcode(Integer pmgsyBcode) {
        this.pmgsyBcode = pmgsyBcode;
    }

    public Integer getPmgsyPvcode() {
        return pmgsyPvcode;
    }

    public void setPmgsyPvcode(Integer pmgsyPvcode) {
        this.pmgsyPvcode = pmgsyPvcode;
    }

    public String getPmgsyPvname() {
        return pmgsyPvname;
    }

    public void setPmgsyPvname(String pmgsyPvname) {
        this.pmgsyPvname = pmgsyPvname;
    }

    public Integer getPmgsyHabcode() {
        return pmgsyHabcode;
    }

    public void setPmgsyHabcode(Integer pmgsyHabcode) {
        this.pmgsyHabcode = pmgsyHabcode;
    }

    public String getPmgsyHabName() {
        return pmgsyHabName;
    }

    public void setPmgsyHabName(String pmgsyHabName) {
        this.pmgsyHabName = pmgsyHabName;
    }

    public Integer getdCode() {
        return dCode;
    }

    public void setdCode(Integer dCode) {
        this.dCode = dCode;
    }

    public Integer getbCode() {
        return bCode;
    }

    public void setbCode(Integer bCode) {
        this.bCode = bCode;
    }

    public Integer getPvCode() {
        return pvCode;
    }

    public void setPvCode(Integer pvCode) {
        this.pvCode = pvCode;
    }

    public Integer getHabCode() {
        return habCode;
    }

    public void setHabCode(Integer habCode) {
        this.habCode = habCode;
    }

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
    public String getRoadCode() {
        return roadCode;
    }

    public void setRoadCode(String roadCode) {
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

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }
    public Integer getId() {
        return id;
    }
    private Integer id;
}