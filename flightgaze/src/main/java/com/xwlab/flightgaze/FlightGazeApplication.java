package com.xwlab.flightgaze;

import android.app.Application;
import android.os.Environment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightGazeApplication extends Application {
    private static FlightGazeApplication instance;
    public static final String SD_PATH = Environment.getExternalStorageDirectory().toString() + "/FlightGaze/";
    private FaceDatabase faceDatabase;
    private String flight;
    private String from;
    private String to;
    private String date;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        faceDatabase = new FaceDatabase(instance);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        date = df.format(new Date());
    }

    public String getDate() {
        return date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public FaceDatabase getFaceDatabase() {
        return faceDatabase;
    }

    public static FlightGazeApplication getInstance() {
        return instance;
    }
}
