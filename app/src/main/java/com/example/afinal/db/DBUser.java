package com.example.afinal.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBUser extends SQLiteOpenHelper {
    private static SQLiteOpenHelper mInstance;
    public static  synchronized SQLiteOpenHelper getInstance(Context context){
        if (mInstance == null){
            mInstance = new DBUser(context, "theme.db", null, 1);
        }
        return mInstance;
    }
    public DBUser(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String db = "create table theme (decoration text, isEquip bool)";
        sqLiteDatabase.execSQL(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
