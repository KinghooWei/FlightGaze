package com.xwlab.blacklist;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.xwlab.util.Logger;
import com.xwlab.util.PermissionUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SettingActivity extends AppCompatActivity {
    MyApplication myApplication;
    private static final String TAG = "SettingActivity";
    private final static int REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        myApplication = MyApplication.getInstance();
        if (PermissionUtil.checkMultiPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.SYSTEM_ALERT_WINDOW}, REQUEST_CODE)) {
            try {
                copyBigDataToSD("det1.bin");
                copyBigDataToSD("det2.bin");
                copyBigDataToSD("det3.bin");
                copyBigDataToSD("det1.param");
                copyBigDataToSD("det2.param");
                copyBigDataToSD("det3.param");
                copyBigDataToSD("recognition.bin");
                copyBigDataToSD("recognition.param");
            } catch (IOException e) {
                e.printStackTrace();
            }
            work();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        copyBigDataToSD("det1.bin");
                        copyBigDataToSD("det2.bin");
                        copyBigDataToSD("det3.bin");
                        copyBigDataToSD("det1.param");
                        copyBigDataToSD("det2.param");
                        copyBigDataToSD("det3.param");
                        copyBigDataToSD("recognition.bin");
                        copyBigDataToSD("recognition.param");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    work();
                } else {
                    new AlertDialog.Builder(this).setTitle("提示").setMessage("需先开启相机权限").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).create().show();
                }
                break;
        }
    }

    private void work() {
        myApplication.getFaceDatabase().loadDatabase();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SettingActivity.this, WorkActivity.class);
                startActivity(intent);
            }
        }, 5000);
    }

    private void copyBigDataToSD(String strOutFileName) throws IOException {
        Logger.i(TAG, "start copy file " + strOutFileName);

        File file = new File(MyApplication.SD_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        String tmpFile = MyApplication.SD_PATH + strOutFileName;
        File f = new File(tmpFile);
        if (f.exists()) {
            Logger.i(TAG, "file exists " + strOutFileName);
            return;
        }
        InputStream myInput;
        java.io.OutputStream myOutput = new FileOutputStream(MyApplication.SD_PATH + strOutFileName);
        myInput = this.getAssets().open(strOutFileName);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }
        myOutput.flush();
        myInput.close();
        myOutput.close();
        Logger.i(TAG, "end copy file " + strOutFileName);
    }

}