package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nic.RdAssetTrackingAndMonitoringSystem.Adapter.MyNodeViewFactory;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.Api;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ApiService;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ServerResponse;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.DBHelper;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyEditTextView;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyLocationListener;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.CameraUtils;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.UrlGenerator;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import me.texy.treeview.TreeNode;
import me.texy.treeview.TreeView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class CameraScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2500;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static String imageStoragePath;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    ImageView imageView, image_view_preview;
    LocationManager mlocManager = null;
    LocationListener mlocListener;
    Double offlatTextValue, offlongTextValue;
    private PrefManager prefManager;
    private ImageView back_img;
    private JSONObject roadTracksaveImageList;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    public dbData dbData = new dbData(this);
    String loc_id;
    String screen_type = "";
    Button assetSave;

    public static DBHelper dbHelper;
    public static SQLiteDatabase db;

    private TreeNode root;
    private TreeView treeView;
    private LinearLayout description_layout;
    private MyEditTextView description;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_with_description);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            screen_type = bundle.getString(AppConstant.KEY_SCREEN_TYPE);
            Log.d("ScreenType",""+screen_type);
        }

        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        imageView = (ImageView) findViewById(R.id.image_view);
        image_view_preview = (ImageView) findViewById(R.id.image_view_preview);
        description_layout = (LinearLayout) findViewById(R.id.description_layout);
        description = (MyEditTextView ) findViewById(R.id.description);
        assetSave = (Button) findViewById(R.id.asset_save);
        back_img = (ImageView) findViewById(R.id.back_img);
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        image_view_preview.setOnClickListener(this);
        imageView.setOnClickListener(this);
        back_img.setOnClickListener(this);
        assetSave.setOnClickListener(this);
        root = TreeNode.root();
        treeView = new TreeView(root, this, new MyNodeViewFactory());
        if(screen_type.equalsIgnoreCase("thirdLevelNode")){
            loc_id = getIntent().getStringExtra("loc_id");
             assetSave.setText("SAVE ASSET PHOTO");
        }
        else  if(screen_type.equalsIgnoreCase("Habitation")){
            description_layout.setVisibility(View.VISIBLE);
            assetSave.setText("SAVE HABITATION PHOTO");
        }
        else  if(screen_type.equalsIgnoreCase("bridgeLevel")){
            assetSave.setText("SAVE PHOTO");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view_preview:
                getLatLong();
                break;
            case R.id.image_view:
                getLatLong();
                break;
            case R.id.back_img:
                onBackPress();
                break;
            case R.id.asset_save:
                if(screen_type.equalsIgnoreCase("Habitation")){
                    saveImage_habitation();
                }
                else if(screen_type.equalsIgnoreCase("thirdLevelNode")) {
                    saveImage();
                }
                else if(screen_type.equalsIgnoreCase("bridgeLevel")) {
                    saveImageBridges();
                }
                break;
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(this, file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        if (MyLocationListener.latitude > 0) {
            offlatTextValue =   MyLocationListener.latitude;
            offlongTextValue = MyLocationListener.longitude;
        }
    }

    private void getLatLong() {
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();


        // permission was granted, yay! Do the
        // location-related task you need to do.
        if (ContextCompat.checkSelfPermission(CameraScreen.this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //Request location updates:
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        }

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(CameraScreen.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CameraScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{CAMERA, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                if (ActivityCompat.checkSelfPermission(CameraScreen.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CameraScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CameraScreen.this, new String[]{ACCESS_FINE_LOCATION}, 1);

                }
            }
            if (MyLocationListener.latitude > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (CameraUtils.checkPermissions(CameraScreen.this)) {
                        captureImage();
                    } else {
                        requestCameraPermission(MEDIA_TYPE_IMAGE);
                    }
//                            checkPermissionForCamera();
                } else {
                    captureImage();
                }
            } else {
                Utils.showAlert(CameraScreen.this, "Satellite communication not available to get GPS Co-ordination Please Capture Photo in Open Area..");
            }
        } else {
            Utils.showAlert(CameraScreen.this, "GPS is not turned on...");
        }
    }

    private void requestCameraPermission(final int type) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type == MEDIA_TYPE_IMAGE) {
                                // capture picture
                                captureImage();
                            } else {
//                                captureVideo();
                            }

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(CameraScreen.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public void previewCapturedImage() {
        try {
            // hide video preview
            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
            image_view_preview.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(imageStoragePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            imageView.setImageBitmap(rotatedBitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // video successfully recorded
                // preview the recorded video
//                previewVideo();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void saveImage() {
        dbData.open();
        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        byte[] imageInByte = new byte[0];
        String image_str = "";
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            imageInByte = baos.toByteArray();
            image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

            ContentValues values = new ContentValues();
            values.put(AppConstant.KEY_ROAD_CATEGORY, prefManager.getRoadCategoty());
            values.put(AppConstant.KEY_ROAD_ID, Integer.parseInt(prefManager.getRoadId()));
            values.put(AppConstant.KEY_ASSET_ID, String.valueOf(loc_id));
            values.put(AppConstant.KEY_ROAD_LAT, offlatTextValue.toString());
            values.put(AppConstant.KEY_ROAD_LONG, offlongTextValue.toString());
            values.put(AppConstant.KEY_IMAGES,image_str.trim());
            values.put(AppConstant.KEY_CREATED_DATE,sdf.format(new Date()));
            long id = db.insert(DBHelper.SAVE_IMAGE_LAT_LONG_TABLE, null, values);

            if(id > 0){
                Toasty.success(this, "Success!", Toast.LENGTH_LONG, true).show();
                treeView.refreshTreeView();
            }
            Log.d("insIdsaveImageLatLong", String.valueOf(id));

        } catch (Exception e) {
            Utils.showAlert(CameraScreen.this, "Atleast Capture one Photo");
            //e.printStackTrace();
        }
    }

    public void saveImage_habitation() {
        dbData.open();
        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        byte[] imageInByte = new byte[0];
        String image_str = "";
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            imageInByte = baos.toByteArray();
            image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

            ContentValues values = new ContentValues();
            values.put(AppConstant.KEY_DCODE,getIntent().getStringExtra(AppConstant.KEY_DCODE) );
            values.put(AppConstant.KEY_BCODE, getIntent().getStringExtra(AppConstant.KEY_BCODE));
            values.put(AppConstant.KEY_PVCODE,getIntent().getStringExtra(AppConstant.KEY_PVCODE) );
            values.put(AppConstant.KEY_PMGSY_DCODE,getIntent().getStringExtra(AppConstant.KEY_PMGSY_DCODE) );
            values.put(AppConstant.KEY_PMGSY_BCODE,getIntent().getStringExtra(AppConstant.KEY_PMGSY_BCODE) );
            values.put(AppConstant.KEY_PMGSY_PVCODE,getIntent().getStringExtra(AppConstant.KEY_PMGSY_PVCODE) );
            values.put(AppConstant.KEY_HABCODE,getIntent().getStringExtra(AppConstant.KEY_HABCODE) );
            values.put(AppConstant.KEY_PMGSY_HAB_CODE,getIntent().getStringExtra(AppConstant.KEY_PMGSY_HAB_CODE) );
            values.put(AppConstant.KEY_ROAD_LAT, offlatTextValue.toString());
            values.put(AppConstant.KEY_ROAD_LONG, offlongTextValue.toString());
            values.put(AppConstant.KEY_IMAGES,image_str.trim());
            values.put(AppConstant.KEY_IMAGE_REMARK,description.getText().toString());

            long id = db.insert(DBHelper.SAVE_IMAGE_HABITATION_TABLE, null, values);

            if(id > 0){
                Toasty.success(this, "Success!", Toast.LENGTH_LONG, true).show();
                treeView.refreshTreeView();
            }
            Log.d("insIdsaveHabitation", String.valueOf(id));

        } catch (Exception e) {
            Utils.showAlert(CameraScreen.this, "Atleast Capture one Photo");
            //e.printStackTrace();
        }
    }

    public void saveImageBridges() {
        dbData.open();
        String culvert_id = getIntent().getStringExtra(AppConstant.KEY_CULVERT_ID);
        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        byte[] imageInByte = new byte[0];
        String image_str = "";
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            imageInByte = baos.toByteArray();
            image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

            ContentValues values = new ContentValues();
            values.put(AppConstant.KEY_START_LAT, offlatTextValue.toString());
            values.put(AppConstant.KEY_START_LONG,offlongTextValue.toString());
            values.put(AppConstant.KEY_IMAGES,image_str.trim());
            values.put(AppConstant.KEY_IMAGE_FLAG,"0");

            long id = db.update(DBHelper.BRIDGES_CULVERT,values,"culvert_id = ? and road_id = ?",new  String[]{culvert_id,prefManager.getRoadId()});
            if(id > 0){
                Toasty.success(this, "Success!", Toast.LENGTH_LONG, true).show();
                treeView.refreshTreeView();
            }
            Log.d("updated_id_Bridges",String.valueOf(id));

        } catch (Exception e) {
            Utils.showAlert(CameraScreen.this, "Atleast Capture one Photo");
            //e.printStackTrace();
        }
    }

    public void saveLatLongList() {
        try {
            new ApiService(this).makeJSONObjectRequest("roadTracksaveImageList", Api.Method.POST, UrlGenerator.getRoadListUrl(), roadTracksaveImageListListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject roadTracksaveImageListListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), roadTracksaveImageList.toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("saveLatLongList", "" + authKey);
        return dataSet;
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {

    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}
