package com.example.afinal.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TimeyDbHelper extends SQLiteOpenHelper {

    private static SQLiteOpenHelper mInstance;
    public static  synchronized SQLiteOpenHelper getInstance(Context context){
        if (mInstance == null){
            mInstance = new TimeyDbHelper(context, "timey.db", null, 1);
        }
        return mInstance;
    }

    private TimeyDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Initialize database
    @Override
    public void onCreate(SQLiteDatabase db) {

        String users = "create table users(_id integer primary key autoincrement, telephone text, username text, email text, password text)";
        String records = "create table records(_id integer primary key autoincrement, telephone text, start_time text, end_time text, duration text)";
        String total = "create table total(_id integer primary key autoincrement, telephone text, total_time text)";
        String shop = "create table shop(_id integer primary key autoincrement, telephone text, coins integer)";

        db.execSQL(users);
        db.execSQL(records);
        db.execSQL(total);
        db.execSQL(shop);


    }

    // Upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
