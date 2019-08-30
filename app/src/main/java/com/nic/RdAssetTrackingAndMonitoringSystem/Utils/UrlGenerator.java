package com.nic.RdAssetTrackingAndMonitoringSystem.Utils;


import com.nic.RdAssetTrackingAndMonitoringSystem.Application.NICApplication;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;

/**
 * Created by Achanthi Sundar  on 21/03/16.
 */
public class UrlGenerator {

    public static String getLoginUrl() {
        return NICApplication.getAppString(R.string.LOGIN_URL);
    }

    public static String getRoadListUrl() {
        return NICApplication.getAppString(R.string.APP_MAIN_SERVICES_URL);
    }

    public static String getTnrdHostName() {
        return NICApplication.getAppString(R.string.TNRD_HOST_NAME);
    }
}
