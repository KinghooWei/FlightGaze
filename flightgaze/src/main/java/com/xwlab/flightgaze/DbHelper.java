package com.xwlab.flightgaze;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(@Nullable Context context, @Nullable String name, int version) {
        super(context, name, null, version);
    }

    private static final String CREATE_PASSENGER = "create table IF NOT EXISTS Passenger (" +
            "flight text, " +
            "passenger_id text, " +
            "name text, " +
            "face text," +
            "feature text," +
            "feature_with_mask text," +
            "city_from text," +
            "city_to text, " +
            "flight_class text," +
            "seat text," +
            "gate integer," +
            "PRIMARY KEY(flight, passenger_id)"
            + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PASSENGER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
