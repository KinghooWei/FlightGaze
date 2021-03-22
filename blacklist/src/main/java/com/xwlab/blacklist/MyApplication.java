package com.xwlab.blacklist;

import android.app.Application;
import android.os.Environment;

public class MyApplication extends Application {
    private static MyApplication instance;
    public static final String SD_PATH = Environment.getExternalStorageDirectory().toString() + "/FlightGaze/";
    private FaceDatabase faceDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        faceDatabase = new FaceDatabase(instance);
    }


    public FaceDatabase getFaceDatabase() {
        return faceDatabase;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
