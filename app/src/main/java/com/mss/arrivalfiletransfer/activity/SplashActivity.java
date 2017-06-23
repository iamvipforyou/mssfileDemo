package com.mss.arrivalfiletransfer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.mss.arrivalfiletransfer.R;
import com.mss.arrivalfiletransfer.Utils.Util;

import java.util.Calendar;
import java.util.TimeZone;

public class SplashActivity extends Activity {

    private static final int REQUEST_READ_PHONE_STATE_PERMISSION = 225;
    private static final int REQUEST_READ_EXTERNALSTORAGE_PERMISSION = 226;
    private static final int REQUEST_WRITE_EXTERNALSTORAGE_PERMISSION = 227;

    private String TAG = SplashActivity.class.getSimpleName();
    private SplashActivity ctx;

    final int REQUEST_Write_PERMISSION = 113;
    final int REQUEST_CAMERA_PERMISSION = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();
        Util.setStatusBarColor(SplashActivity.this);
        ctx = SplashActivity.this;
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        } else {
            startActivity();

        }
    }

    private void initUI() {


    }


    private void startActivity() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, AllFilesPicker.class));
                //   startActivity(new Intent(SplashActivity.this, NavMainActivity.class));

                finish();
            }
        }, 2000);
    }

    public void checkPermission() {

        //get all required elements
        int READ_EXTERNALSTORAGE_PERMISSION = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNALSTORAGE_PERMISSION = ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);


        //Check permissions if permission already granted run if blog if not granted then run else blog
        if (READ_EXTERNALSTORAGE_PERMISSION == PackageManager.PERMISSION_GRANTED && WRITE_EXTERNALSTORAGE_PERMISSION == PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, " Permission Already given ");
            startActivity();

        } else {
            Log.d(TAG, "Current app does not have permission");

            // Marshmallow+
            ActivityCompat.requestPermissions(ctx, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNALSTORAGE_PERMISSION);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e(TAG, "Permission request" + requestCode);


        switch (requestCode) {

            case REQUEST_READ_PHONE_STATE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "Permission Granted" + grantResults.length);
                    //Proceed to next steps
                    startActivity();

                } else {
                    Log.e(TAG, "Permission Denied");
                }
                return;
            }
            case REQUEST_READ_EXTERNALSTORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "Permission Granted");
                    //Proceed to next steps
                    startActivity();

                } else {
                    Log.e(TAG, "Permission Denied");
                }
                return;
            }
            case REQUEST_WRITE_EXTERNALSTORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "Permission Granted");
                    //Proceed to next steps
                    startActivity();


                } else {
                    Log.e(TAG, "Permission Denied");
                }
                return;
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            //	GCMRegistrar.unregister(SplashActivity.this);
            //	GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
        }
    }
}
