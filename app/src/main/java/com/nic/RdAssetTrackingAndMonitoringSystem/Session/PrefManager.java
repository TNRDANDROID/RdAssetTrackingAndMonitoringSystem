package com.nic.RdAssetTrackingAndMonitoringSystem.Session;

import android.content.Context;
import android.content.SharedPreferences;

import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;

import org.json.JSONArray;

import java.util.List;


/**
 * Created by AchanthiSudan on 11/01/19.
 */
public class PrefManager {

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String APP_KEY = "AppKey";

    private static final String KEY_USER_PASS_KEY = "pass_key";
    private static final String KEY_ENCRYPT_PASS = "pass";
    private static final String KEY_USER_NAME = "UserName";
    private static final String KEY_USER_PASSWORD = "UserPassword";
    private static final String KEY_DECRYPT_KEY = "Decrypt_Key";
    private static final String KEY_DISTRICT_CODE = "District_Code";
    private static final String KEY_BLOCK_CODE = "Block_Code";
    private static final String KEY_PV_CODE = "Pv_Code";
    private static final String KEY_DISTRICT_NAME = "District_Name";
    private static final String KEY_BLOCK_NAME = "Block_Name";
    private static final String KEY_PV_NAME = "Pv_Name";
    private static final String KEY_LEVELS = "Levels";
    private static final String KEY_ROAD_CATEGOTY = "road_category";
    private static final String KEY_ROAD_ID = "road_id";
    private static final String KEY_LOCATION_GROUP = "location_group";
    private static final String KEY_PMGSY_DELETE_ID = "pmgsy_Delete_id";
    private static final String KEY_DELETE_ID = "deleteId";
    private static final String KEY_PMGSY_DCODE = "pmgsy_dcode";
    private static final String KEY_PMGSY_BCODE = "pmgsy_bcode";
    private static final String KEY_PMGSY_PVCODE = "pmgsy_pvcode";
    private static final String KEY_PMGSY_HABCODE = "pmgsy_habcode";
    private static final String KEY_CLICKED_POSITION = "clickedPositionId";
    private static final String KEY_CULVERT_ID_ARRAY = "culvertIdArray";


    private static final String IMEI = "imei";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(AppConstant.PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public String getIMEI() {
        return pref.getString(IMEI,null);
    }

    public void setImei(String imei) {
        editor.putString(IMEI,imei);
        editor.commit();
    }

    public void setAppKey(String appKey) {
        editor.putString(APP_KEY, appKey);
        editor.commit();
    }

    public String getAppKey() {
        return pref.getString(APP_KEY, null);
    }


    public void clearSession() {
        editor.clear();
        editor.commit();
    }


    public void setUserPassKey(String userPassKey) {
        editor.putString(KEY_USER_PASS_KEY, userPassKey);
        editor.commit();
    }

    public String getUserPassKey() {
        return pref.getString(KEY_USER_PASS_KEY, null);
    }


    public void setUserName(String userName) {
        editor.putString(KEY_USER_NAME, userName);
        editor.commit();
    }

    public String   getUserName() { return pref.getString(KEY_USER_NAME, null); }

    public void setUserPassword(String userPassword) {
        editor.putString(KEY_USER_PASSWORD, userPassword);
        editor.commit();
    }

    public String   getUserPassword() { return pref.getString(KEY_USER_PASSWORD, null); }


    public void setEncryptPass(String pass) {
        editor.putString(KEY_ENCRYPT_PASS, pass);
        editor.commit();
    }

    public String getEncryptPass() {
        return pref.getString(KEY_ENCRYPT_PASS, null);
    }

    public Object setDistrictCode(Object key) {
        editor.putString(KEY_DISTRICT_CODE, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getDistrictCode() {
        return pref.getString(KEY_DISTRICT_CODE, null);
    }


    public Object setBlockCode(Object key) {
        editor.putString(KEY_BLOCK_CODE, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getBlockCode() {
        return pref.getString(KEY_BLOCK_CODE, null);
    }



    public Object setPvCode(Object key) {
        editor.putString(KEY_PV_CODE, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getPvCode() {
        return pref.getString(KEY_PV_CODE, null);
    }




    public Object setDistrictName(Object key) {
        editor.putString(KEY_DISTRICT_NAME, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getDistrictName() {
        return pref.getString(KEY_DISTRICT_NAME, null);
    }

    public Object setBlockName(Object key) {
        editor.putString(KEY_BLOCK_NAME, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getBlockName() {
        return pref.getString(KEY_BLOCK_NAME, null);
    }


    public Object setPvName(Object key) {
        editor.putString(KEY_PV_NAME, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getPvName() {
        return pref.getString(KEY_PV_NAME, null);
    }


    public Object setLevels(Object key) {
        editor.putString(KEY_LEVELS, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getLevels() {
        return pref.getString(KEY_LEVELS, null);
    }

    public String getRoadCategoty() {
        return pref.getString(KEY_ROAD_CATEGOTY,null);
    }

    public void setRoadCategoty(String RoadCategoty) {
        editor.putString(KEY_ROAD_CATEGOTY,RoadCategoty);
        editor.commit();
    }

    public String getRoadId() {
        return pref.getString(KEY_ROAD_ID,null);
    }

    public void setRoadId(String RoadId) {
        editor.putString(KEY_ROAD_ID,RoadId);
        editor.commit();
    }

    public String getLocationGroup() {
        return pref.getString(KEY_LOCATION_GROUP,null);
    }

    public void setLocationGroup(String LocationGroup) {
        editor.putString(KEY_LOCATION_GROUP,LocationGroup);
        editor.commit();
    }

    public String getKeyPmgsyDeleteId() {
        return pref.getString(KEY_PMGSY_DELETE_ID,null);
    }

    public void setKeyPmgsyDeleteId(String LocationId) {
        editor.putString(KEY_PMGSY_DELETE_ID,LocationId);
        editor.commit();
    }

    public String getKeyDeleteId() {
        return pref.getString(KEY_DELETE_ID,null);
    }

    public void setKeyDeleteId(String deleteId) {
        editor.putString(KEY_DELETE_ID,deleteId);
        editor.commit();
    }

    public String getKeyPmgsyDcode() {
        return pref.getString(KEY_PMGSY_DCODE,null);
    }

    public void setKeyPmgsyDcode(String pmgsyDcode) {
        editor.putString(KEY_PMGSY_DCODE,pmgsyDcode);
        editor.commit();
    }

    public String getKeyPmgsyBcode() {
        return pref.getString(KEY_PMGSY_BCODE,null);
    }

    public void setKeyPmgsyBcode(String pmgsyBcode) {
        editor.putString(KEY_PMGSY_BCODE,pmgsyBcode);
        editor.commit();
    }

    public String getKeyPmgsyPvcode() {
        return pref.getString(KEY_PMGSY_PVCODE,null);
    }

    public void setKeyPmgsyPvcode(String pmgsyPvcode) {
        editor.putString(KEY_PMGSY_PVCODE,pmgsyPvcode);
        editor.commit();
    }

    public String getKeyPmgsyHabcode() {
        return pref.getString(KEY_PMGSY_HABCODE,null);
    }

    public void setKeyPmgsyHabcode(String pmgsyHabcode) {
        editor.putString(KEY_PMGSY_HABCODE,pmgsyHabcode);
        editor.commit();
    }

    public String getKeyClickedPosition() {
        return pref.getString(KEY_CLICKED_POSITION,null);
    }

    public void setKeyClickedPosition(String clickedPosition) {
        editor.putString(KEY_CLICKED_POSITION,clickedPosition);
        editor.commit();
    }

    public void setLocalSaveCulvertIdJsonList(JSONArray jsonarray) {
        editor.putString(KEY_CULVERT_ID_ARRAY, jsonarray.toString());
        editor.commit();
    }

    public String getLocalSaveJsonList() {
        return pref.getString(KEY_CULVERT_ID_ARRAY, null);
    }

    public JSONArray getLocalSaveCulvertIdJson() {
        JSONArray jsonData = null;
        String strJson = getLocalSaveJsonList();//second parameter is necessary ie.,Value to return if this preference does not exist.
        try {
            if (strJson != null) {
                jsonData = new JSONArray(strJson);
            }
        } catch (Exception e) {

        }
        return jsonData;
    }
}
