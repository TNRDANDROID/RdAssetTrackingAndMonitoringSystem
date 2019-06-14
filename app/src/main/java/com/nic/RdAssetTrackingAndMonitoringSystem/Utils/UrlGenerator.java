package com.nic.RdAssetTrackingAndMonitoringSystem.Utils;


/**
 * Created by Achanthi Sundar  on 21/03/16.
 */
public class UrlGenerator {

    public static String getLoginUrl() {
       // return "http://10.163.19.140:81/rdweb/project/webservices_forms/login_service/login_services.php";
        return "https://tnrd.gov.in/project/webservices_forms/login_service/login_services.php";
    }

    public static String getRoadListUrl() {
        return "https://tnrd.gov.in/project/webservices_forms/road_asset/road_asset_services_test.php";
    }

    public static String getTnrdHostName() {
        return "tnrd.gov.in";
    }
}
