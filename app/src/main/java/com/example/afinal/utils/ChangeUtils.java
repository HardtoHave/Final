package com.example.afinal.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.afinal.db.TimeyDbHelper;

public class ChangeUtils {
    private TimeyDbHelper dbHelper;

    private void ChangeEmail(String telephone, String email_old, String email_new) throws Exception {
        // non-empty validate
        if (TextUtils.isEmpty(telephone) || TextUtils.isEmpty(email_old) || TextUtils.isEmpty(email_new)) {
            throw new Exception("Change failed due to the presence of an empty input");
        }

        // email validate
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(true,"users",new String[]{"email"},"telephone = ?",new String[]{email_old},null,null,null,null);
        if (cursor.moveToFirst()){
            String email_check = cursor.getString(0);
            if (!TextUtils.equals(email_old, email_check)) {
                throw new Exception("The email you entered does not match your email");
            }
            cursor.close();
        }

        // change email
        if(db.isOpen()){
            // check if the user had been registered
            ContentValues values = new ContentValues();
            values.put("email", email_new);
            db.update("users",values,"telephone = ?", new String[]{telephone});

            db.close();
        }
    }

    private void ChangePassword(String telephone, String password_old, String password_new) throws Exception {
        // non-empty validate
        if (TextUtils.isEmpty(telephone) || TextUtils.isEmpty(password_old) || TextUtils.isEmpty(password_new)) {
            throw new Exception("Change failed due to the presence of an empty input");
        }

        // password validate
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(true,"users",new String[]{"password"},"telephone = ?",new String[]{password_old},null,null,null,null);
        if (cursor.moveToFirst()){
            String password_check = cursor.getString(0);
            if (!TextUtils.equals(password_old, password_check)) {
                throw new Exception("The password you entered does not match your email");
            }
            cursor.close();
        }

        // change password
        if(db.isOpen()){
            // check if the user had been registered
            ContentValues values = new ContentValues();
            values.put("password", password_new);
            db.update("users",values,"telephone = ?", new String[]{telephone});

            db.close();
        }
    }

    private void ChangeUsername(String telephone, String username) throws Exception {
        // non-empty validate
        if (TextUtils.isEmpty(telephone) || TextUtils.isEmpty(username)) {
            throw new Exception("Change failed due to the presence of an empty input");
        }

        // change username
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            // check if the user had been registered
            ContentValues values = new ContentValues();
            values.put("username", username);
            db.update("users",values,"telephone = ?", new String[]{telephone});

            db.close();
        }
    }
}
