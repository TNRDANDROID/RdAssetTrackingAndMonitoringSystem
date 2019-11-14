package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.Api;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ApiService;
import com.nic.RdAssetTrackingAndMonitoringSystem.Api.ServerResponse;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyEditTextView;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.FontCache;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.UrlGenerator;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AchanthiSundar on 28-12-2018.
 */

public class LoginScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {

    private Button login_btn;
    private String name, pass, randString;
    private TextView tv_version_number;

    private MyEditTextView userName;

    private MyEditTextView passwordEditText;
    private ImageView redEye;

    JSONObject jsonObject;
    private int setPType;

    String sb;
    private PrefManager prefManager;
    Handler myHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.login_screen);

        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        login_btn = (Button) findViewById(R.id.btn_sign_in);
        userName = (MyEditTextView) findViewById(R.id.user_name);
        tv_version_number = (TextView) findViewById(R.id.tv_version_number);
        redEye = (ImageView) findViewById(R.id.red_eye);
        passwordEditText = (MyEditTextView) findViewById(R.id.password);


        login_btn.setOnClickListener(this);
        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        login_btn.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.MEDIUM));


        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    checkLoginScreen();
                }
                return false;
            }
        });
        try {
            String versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
            tv_version_number.setText("Version" + " " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        passwordEditText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Avenir-Roman.ttf"));
        randString = Utils.randomChar();
        setPType = 1;
        redEye.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                checkLoginScreen();
                break;
            case R.id.red_eye:
                showPassword();
                break;
        }
    }

    public void showPassword() {
        if (setPType == 1) {
            setPType = 0;
            passwordEditText.setTransformationMethod(null);
            if (passwordEditText.getText().length() > 0) {
                passwordEditText.setSelection(passwordEditText.getText().length());
                redEye.setBackgroundResource(R.drawable.red_eye);
            }
        } else {
            setPType = 1;
            passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
            if (passwordEditText.getText().length() > 0) {
                passwordEditText.setSelection(passwordEditText.getText().length());
                redEye.setBackgroundResource(R.drawable.light_gray_eye);
            }
        }

    }
    public boolean validate() {
        boolean valid = true;
        String username = userName.getText().toString().trim();
        prefManager.setUserName(username);
        String password = passwordEditText.getText().toString().trim();


        if (username.isEmpty()) {
            valid = false;
            Utils.showAlert(this, "Please enter the username");
        } else if (password.isEmpty()) {
            valid = false;
            Utils.showAlert(this, "Please enter the password");
        }
        return valid;
    }

    private void checkLoginScreen() {
        if ((Utils.isOnline())) {


        }
        final String username = userName.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        prefManager.setUserPassword(password);

        if (Utils.isOnline()) {
            if (!validate())
                return;
            else if (prefManager.getUserName().length() > 0 && password.length() > 0) {
                new ApiService(this).makeRequest("LoginScreen", Api.Method.POST, UrlGenerator.getLoginUrl(), loginParams(), "not cache", this);
            } else {
                Utils.showAlert(this, "Please enter your username and password!");
            }
        } else {
            //Utils.showAlert(this, getResources().getString(R.string.no_internet));
            AlertDialog.Builder ab = new AlertDialog.Builder(
                    LoginScreen.this);
            ab.setMessage("Internet Connection is not avaliable..Please Turn ON Network Connection OR Continue With Off-line Mode..");
            ab.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            Intent I = new Intent(
                                    android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(I);
                        }
                    });
            ab.setNegativeButton("Continue With Off-Line",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            offline_mode(username, password);
                        }
                    });
            ab.show();
        }
    }


    public Map<String, String> loginParams() {
        Map<String, String> params = new HashMap<>();
        params.put(AppConstant.KEY_SERVICE_ID, "login");


        String random = Utils.randomChar();

        params.put(AppConstant.USER_LOGIN_KEY, random);
        Log.d("randchar", "" + random);

        params.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        params.put(AppConstant.KEY_APP_CODE, "A");
        Log.d("user", "" + userName.getText().toString().trim());

        String encryptUserPass = Utils.md5(passwordEditText.getText().toString().trim());
        prefManager.setEncryptPass(encryptUserPass);
        Log.d("md5", "" + encryptUserPass);

        String userPass = encryptUserPass.concat(random);
        Log.d("userpass", "" + userPass);
        String sha256 = Utils.getSHA(userPass);
        Log.d("sha", "" + sha256);

        params.put(AppConstant.KEY_USER_PASSWORD, sha256);


        Log.d("user", "" + userName.getText().toString().trim());
        Log.d("params", "" + params);


        return params;
    }

//    private void callSampleApi() {
//
//        new ApiService(this).makeRequest("sample", Api.Method.POST, "https://www.tnrd.gov.in/project/webservices_forms/login_service/login_services.php", loginParams(), "not cache", this);
//
//    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject loginResponse = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status = loginResponse.getString(AppConstant.KEY_STATUS);
            String response = loginResponse.getString(AppConstant.KEY_RESPONSE);
            String message = loginResponse.getString(AppConstant.KEY_MESSAGE);
            if ("LoginScreen".equals(urlType)) {
                if (status.equalsIgnoreCase("OK")) {
                    if (response.equals("LOGIN_SUCCESS")) {
                        String key = loginResponse.getString(AppConstant.KEY_USER);
                        String user_data = loginResponse.getString(AppConstant.USER_DATA);
                        String decryptedKey = Utils.decrypt(prefManager.getEncryptPass(), key);
                        Log.d("loginkey",""+key);
                        String userDataDecrypt = Utils.decrypt(prefManager.getEncryptPass(), user_data);
                        Log.d("userdatadecry", "" + userDataDecrypt);
                        jsonObject = new JSONObject(userDataDecrypt);
                        prefManager.setDistrictCode(jsonObject.get(AppConstant.DISTRICT_CODE));
                        prefManager.setBlockCode(jsonObject.get(AppConstant.BLOCK_CODE));
                        prefManager.setPvCode(jsonObject.get(AppConstant.PV_CODE));
                        prefManager.setDistrictName(jsonObject.get(AppConstant.DISTRICT_NAME));
                        prefManager.setBlockName(jsonObject.get(AppConstant.BLOCK_NAME));
                        prefManager.setPvName(jsonObject.get(AppConstant.PV_NAME));
                        prefManager.setLevels(jsonObject.get(AppConstant.LEVELS));
                        prefManager.setUserPassKey(decryptedKey);
                        showHomeScreen();
                    } else {
                        if (response.equals("LOGIN_FAILED") && (!message.equals("User Locked by State Level Admin"))) {
                            Utils.showAlert(this, "Invalid UserName Or Password");
                        }
                        else {
                            Utils.showAlert(this, message);
                        }
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void OnError(VolleyError volleyError) {
        Utils.showAlert(this, "Login Again");
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        showHomeScreen();
//    }

    private void showHomeScreen() {
        Intent intent = new Intent(LoginScreen.this, Dashboard.class);
        intent.putExtra("Home", "Login");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void offline_mode(String name, String pass) {
        String userName = prefManager.getUserName();
        String password = prefManager.getUserPassword();
        if (name.equals(userName) && pass.equals(password)) {
            showHomeScreen();
        } else {
            Utils.showAlert(this, "No data available for offline. Please Turn On Your Network");
        }
    }

}
