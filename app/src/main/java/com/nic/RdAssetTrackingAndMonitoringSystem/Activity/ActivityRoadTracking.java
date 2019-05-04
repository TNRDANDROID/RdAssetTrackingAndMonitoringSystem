package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import Database.PlaceDataSQL;
import Util.ApiCrypter;
import Util.AsynchPostGet;
import Util.Util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nic.RdAssetTrackingAndMonitoringSystem.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ActivityRoadTracking extends Activity implements LocationListener {
    private String bcode;
    boolean bl_buttonStatus = false;
    private boolean bl_gisUpload = false;
    private boolean bl_rdUpload = false;
    protected Button clearButton;
    private String dcode;
    private String flag = "";
    Handler hand = new C00241();
    InputStream is = null;
    double latitude = 0.0d;
    protected LocationManager locationManager;
    protected Button pauseButton;
    PlaceDataSQL pl;
    private String pvCode;
    String res;
    private String roadCode;
    private String roadId;
    private String roadLength;
    private String roadName;
    protected Button startButton;
    protected Button stopButton;
    private TextView tv_roadCode;
    private TextView tv_roadLength;
    private TextView tv_roadName;
    private TextView tv_roadTrackingStatus;
    protected Button uploadButton;
    private String user;
    String xml;

    /* renamed from: com.nic.ruralroadworks.ActivityRoadTracking$1 */
    class C00241 extends Handler {
        C00241() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (ActivityRoadTracking.this.bl_gisUpload) {
                        Log.i("res", ActivityRoadTracking.this.res);
                        Util.progressDialog.dismiss();
                        try {
                            if (ActivityRoadTracking.this.res.contains("<Feature><project><roadcode>" + ActivityRoadTracking.this.roadCode + "</roadcode><roadname>" + ActivityRoadTracking.this.roadName + "</roadname><response>Yes</response></project></Feature>")) {
                                ActivityRoadTracking.this.createJsonAndEncodeJson();
                            } else if (ActivityRoadTracking.this.res.contains("<Feature><project><roadcode>" + ActivityRoadTracking.this.roadCode + "</roadcode><roadname>" + ActivityRoadTracking.this.roadName + "</roadname><response>No</response></project></Feature>")) {
                                Toast.makeText(ActivityRoadTracking.this, "Not Uploaded", 1).show();
                            } else if (ActivityRoadTracking.this.res.contains("<Feature>The Xml values are not valid</Feature>")) {
                                Toast.makeText(ActivityRoadTracking.this, "Already Exists", 1).show();
                            } else {
                                Toast.makeText(ActivityRoadTracking.this, "Error In Uploading", 1).show();
                            }
                        } catch (Exception e) {
                            ActivityRoadTracking.this.hand.sendEmptyMessage(1);
                            e.printStackTrace();
                        }
                    }
                    if (ActivityRoadTracking.this.bl_rdUpload) {
                        try {
                            if (ActivityRoadTracking.this.res.contains("yes")) {
                                Toast.makeText(ActivityRoadTracking.this, "Uploaded Successfully", 1).show();
                                return;
                            } else if (ActivityRoadTracking.this.res.contains("no")) {
                                Toast.makeText(ActivityRoadTracking.this, "Not Uploaded", 1).show();
                                return;
                            } else {
                                return;
                            }
                        } catch (Exception e2) {
                            ActivityRoadTracking.this.hand.sendEmptyMessage(1);
                            e2.printStackTrace();
                            return;
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.nic.ruralroadworks.ActivityRoadTracking$2 */
    class C00252 implements OnClickListener {
        C00252() {
        }

        public void onClick(View v) {
            try {
                ActivityRoadTracking.this.uploadTrackedRecords();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.nic.ruralroadworks.ActivityRoadTracking$3 */
    class C00263 implements OnClickListener {
        C00263() {
        }

        public void onClick(View v) {
            PlaceDataSQL.tableName = "roadTrackingRepository";
            if (ActivityRoadTracking.this.pl.deleteRoadTrackingTable(ActivityRoadTracking.this.roadId).longValue() > 0) {
                Toast.makeText(ActivityRoadTracking.this, "Cleared", 1).show();
                ActivityRoadTracking.this.tv_roadTrackingStatus.setText("Cleared");
                ActivityRoadTracking.this.startButton.setText("Start Tracking at begining of the Road");
                ActivityRoadTracking.this.startButton.setEnabled(true);
                ActivityRoadTracking.this.pauseButton.setEnabled(false);
                ActivityRoadTracking.this.stopButton.setEnabled(false);
                ActivityRoadTracking.this.uploadButton.setEnabled(false);
                ActivityRoadTracking.this.clearButton.setEnabled(false);
                return;
            }
            Toast.makeText(ActivityRoadTracking.this, "Not Cleared", 1).show();
        }
    }

    @SuppressLint("WrongConstant")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_tracking);
        Intent in = getIntent();
        this.roadId = in.getStringExtra("road_id").toString();
        this.roadCode = in.getStringExtra("road_code").toString();
        this.roadName = in.getStringExtra("road_name").toString().trim();
        this.roadLength = in.getStringExtra("road_length").toString();
        this.pvCode = in.getStringExtra("pv").toString();
        this.pl = new PlaceDataSQL(this);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("pin_prefname", 0);
        this.dcode = pref.getString("dcode", "");
        this.bcode = pref.getString("bcode", "");
        this.user = pref.getString("user", "");
        ((TextView) findViewById(R.id.headtext)).setText("GIS Survey For Roads - Road Tracking");
        this.startButton = (Button) findViewById(R.id.startButton);
        this.pauseButton = (Button) findViewById(R.id.pauseButton);
        this.stopButton = (Button) findViewById(R.id.stopButton);
        this.uploadButton = (Button) findViewById(R.id.uploadTrackingRecords);
        this.clearButton = (Button) findViewById(R.id.clearTrackedRecord);
        this.pauseButton.setEnabled(false);
        this.stopButton.setEnabled(false);
        this.uploadButton.setEnabled(false);
        this.uploadButton.setEnabled(false);
        this.clearButton.setEnabled(false);
        this.tv_roadCode = (TextView) findViewById(R.id.vRoadCode);
        this.tv_roadCode.setText(this.roadCode);
        this.tv_roadName = (TextView) findViewById(R.id.vRoadNmae);
        this.tv_roadName.setText(this.roadName);
        this.tv_roadLength = (TextView) findViewById(R.id.vRoadLength);
        this.tv_roadLength.setText(this.roadLength);
        this.tv_roadTrackingStatus = (TextView) findViewById(R.id.vRoadTrackingStatus);
        try {
            PlaceDataSQL.tableName = "roadTrackingRepository";
            Cursor cur = this.pl.fetchRoadTrackingRepository(this.roadId);
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                String status = cur.getString(4).toString();
                if (status.equals("paused")) {
                    this.tv_roadTrackingStatus.setText("Paused");
                    this.startButton.setText("Resume Tracking at Last Location of the Road");
                } else if (status.equals("stopped")) {
                    this.tv_roadTrackingStatus.setText("Stopped");
                    this.startButton.setEnabled(false);
                    this.clearButton.setEnabled(true);
                    this.uploadButton.setEnabled(true);
                }
                this.locationManager = (LocationManager) getSystemService("location");
                this.uploadButton.setOnClickListener(new C00252());
                this.clearButton.setOnClickListener(new C00263());
            }
            this.tv_roadTrackingStatus.setText("Not Started Yet");
            this.locationManager = (LocationManager) getSystemService("location");
            this.uploadButton.setOnClickListener(new C00252());
            this.clearButton.setOnClickListener(new C00263());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    protected void uploadTrackedRecords() throws IOException {
        ParserConfigurationException pce;
        Writer writer;
        AsynchPostGet asynchPostGet;
        Timer timer;
        AsynchPostGet asynchPostGet2;
        TransformerException tfe;
        this.bl_gisUpload = true;
        StringWriter writer2 = null;
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element rootElement = doc.createElement("Feature");
            doc.appendChild(rootElement);
            Element project = doc.createElement("project");
            rootElement.appendChild(project);
            project.setAttribute("type", "Line");
            Element element = project;
            element.setAttribute("pvillage", this.pvCode);
            element = project;
            element.setAttribute("block_code", this.bcode);
            element = project;
            element.setAttribute("district_code", this.dcode);
            project.setAttribute("state_code", "29");
            element = project;
            element.setAttribute("roadname", this.roadName);
            element = project;
            element.setAttribute("road_code", this.roadCode);
            Element survey = doc.createElement("Survey");
            rootElement.appendChild(survey);
            Cursor cursors1 = new PlaceDataSQL(this).fetchRoadTrackingRepository(this.roadId);
            while (cursors1.moveToNext()) {
                String primaryKey = cursors1.getString(0).toString();
                String roadId = cursors1.getString(1).toString();
                String lat = cursors1.getString(2).toString();
                String lon = cursors1.getString(3).toString();
                String status = cursors1.getString(4).toString();
                Element firstname = doc.createElement("Sequence");
                firstname.setAttribute("lat", lat);
                firstname.setAttribute("lon", lon);
                firstname.setAttribute("seq", primaryKey);
                survey.appendChild(firstname);
            }
            cursors1.close();
            project.appendChild(survey);
            DOMSource dOMSource = new DOMSource(doc);
            Writer writer3 = new StringWriter();
            try {
                StreamResult streamResult = new StreamResult(writer3);
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty("indent", "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(dOMSource, streamResult);
                writer3.flush();
                writer2 = (StringWriter) writer3;
            } catch (TransformerException e2) {
                tfe = e2;
                writer = writer3;
                tfe.printStackTrace();
                this.xml = writer2.toString();
                Log.i("XML", this.xml);
                PostParams.clear();
                PostParams.al_param.add("testurl");
                PostParams.al_param_value.add(this.xml);
                if (Util.isNetworkAvailable(this)) {
                    Util.showProgressBar(null, "Uploading data...", this);
                    asynchPostGet = new AsynchPostGet(this, "POST");
                    asynchPostGet.execute(new String[]{"http://tangis.tn.nic.in/rd_new/xml/rd_line_xml.php"});
                    timer = new Timer();
                    asynchPostGet2 = asynchPostGet;
                    timer.schedule(null, 1000, 1000);
                    return;
                }
                Toast.makeText(this, "Network Connections are not available.", 1).show();
            }
        } catch (ParserConfigurationException e3) {
            pce = e3;
        }
        this.xml = writer2.toString();
        Log.i("XML", this.xml);
        PostParams.clear();
        PostParams.al_param.add("testurl");
        PostParams.al_param_value.add(this.xml);
        if (Util.isNetworkAvailable(this)) {
            Util.showProgressBar(null, "Uploading data...", this);
            asynchPostGet = new AsynchPostGet(this, "POST");
            asynchPostGet.execute(new String[]{"http://tangis.tn.nic.in/rd_new/xml/rd_line_xml.php"});
            timer = new Timer();
            asynchPostGet2 = asynchPostGet;
            timer.schedule(null,1000, 1000);
            return;
        }
        Toast.makeText(this, "Network Connections are not available.", 1).show();
    }

    public void onStartButtonClicked(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.locationManager.requestLocationUpdates("gps", 0, 1.0f, this);
        if (this.latitude > 0.0d) {
            this.startButton.setEnabled(false);
            this.pauseButton.setEnabled(true);
            this.stopButton.setEnabled(true);
            this.startButton.setText("Started");
            if (!this.flag.equals("paused")) {
                this.flag = "started";
                PlaceDataSQL.tableName = "roadTrackingRepository";
                this.pl.deleteRoadTrackingTable(this.roadId);
                return;
            } else if (this.flag.equals("stopped")) {
                this.flag = "started";
                return;
            } else {
                this.flag = "stopped";
                return;
            }
        }
        Util.showProgressBar(null, "Waiting for Location...", this);
    }

    protected void createJsonAndEncodeJson() {
        this.bl_gisUpload = false;
        this.bl_rdUpload = true;
        Util u = new Util();
        String ivParam = u.createIVParam(this, this.user);
        String secretKey = u.createSecretKey(this, ivParam);
        String[] arr_info = u.getInfo(this, this.user);
        ApiCrypter cry = new ApiCrypter(ivParam, secretKey);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("road_id", ApiCrypter.bytesToHex(cry.encrypt(this.roadId)));
            jsonObj.put("imei", ApiCrypter.bytesToHex(cry.encrypt(arr_info[0])));
            jsonObj.put("imsi", ApiCrypter.bytesToHex(cry.encrypt(arr_info[1])));
            jsonObj.put("serial", ApiCrypter.bytesToHex(cry.encrypt(arr_info[2])));
            jsonObj.put("user", ApiCrypter.bytesToHex(cry.encrypt(arr_info[3])));
            jsonObj.put("time", ApiCrypter.bytesToHex(cry.encrypt(arr_info[4])));
            jsonObj.put("iv", ivParam);
            jsonObj.put("sec", secretKey);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        JSONObject jsonValue = new JSONObject();
        try {
            jsonValue.put("upload", jsonObj);
        } catch (JSONException e3) {
            e3.printStackTrace();
        }
        String jsonStr = jsonValue.toString();
        Log.d("JSON", jsonStr);
        String encodedJson = "";
        try {
            Log.i("encodedJson", ApiCrypter.bytesToHex(new ApiCrypter().encrypt(jsonStr)));
        } catch (Exception e22) {
            e22.printStackTrace();
        }
        Util.progressDialog.dismiss();
    }

    public void onPauseButtonClicked(View v) {
        this.startButton.setEnabled(true);
        this.pauseButton.setEnabled(false);
        this.startButton.setText("Resume Tracking at Last Location of the Road");
        PlaceDataSQL.tableName = "roadTrackingRepository";
        this.flag = "paused";
        if (this.pl.updateRoadTrackingRepositoryStatus(this.flag, this.roadId)) {
            this.locationManager.removeUpdates(this);
        } else {
            Toast.makeText(this, "Not Paused", 1).show();
        }
    }

    public void onStopButtonClicked(View v) throws Exception {
        this.flag = "stopped";
        this.startButton.setText("Stopped");
        PlaceDataSQL.tableName = "roadTrackingRepository";
        if (this.pl.updateRoadTrackingRepositoryStatus(this.flag, this.roadId)) {
            this.locationManager.removeUpdates(this);
        } else {
            Toast.makeText(this, "Not Stopped", 1).show();
        }
        this.startButton.setText("Start Tracking at begining of the Road");
        this.startButton.setEnabled(false);
        this.pauseButton.setEnabled(false);
        this.stopButton.setEnabled(false);
        this.uploadButton.setEnabled(true);
        this.clearButton.setEnabled(true);
    }

    @SuppressLint("WrongConstant")
    public void onLocationChanged(Location location) {
        this.latitude = location.getLatitude();
        if (this.latitude > 0.0d) {
            if (!this.bl_buttonStatus) {
                Util.progressDialog.dismiss();
                this.startButton.setEnabled(false);
                this.pauseButton.setEnabled(true);
                this.stopButton.setEnabled(true);
                Toast.makeText(this, "Start Move on the Road", 1).show();
            }
            this.bl_buttonStatus = true;
            PlaceDataSQL.clear();
            PlaceDataSQL.al_param.add(PlaceDataSQL.TAG_ROAD_IDD);
            PlaceDataSQL.al_param.add(PlaceDataSQL.TAG_LAT);
            PlaceDataSQL.al_param.add(PlaceDataSQL.TAG_LON);
            PlaceDataSQL.al_param.add(PlaceDataSQL.TAG_TRACKING_STATUS);
            PlaceDataSQL.al_value.add(this.roadId);
            PlaceDataSQL.al_value.add(new StringBuilder(String.valueOf(location.getLatitude())).toString());
            PlaceDataSQL.al_value.add(new StringBuilder(String.valueOf(location.getLongitude())).toString());
            PlaceDataSQL.al_value.add(this.flag);
            PlaceDataSQL.tableName = "roadTrackingRepository";
            long row = this.pl.insertIntoTable().longValue();
            if (row > 0) {
                Toast.makeText(this, "Inserted " + row, 1).show();
            } else {
                Toast.makeText(this, "Not Inserted " + row, 1).show();
            }
        }
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            this.locationManager.removeUpdates(this);
        }
        return super.onKeyDown(keyCode, event);
    }
}
