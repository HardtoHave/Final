package com.example.afinal.ui.setting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.afinal.R;
import com.example.afinal.activity.LoginActivity;
import com.example.afinal.activity.MainActivity;
import com.example.afinal.db.TimeyDbHelper;



public class SettingFragment extends Fragment {
    Button okay_text, cancel_text,okay_text2, cancel_text2;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_setting, container,false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){

        TextView cEmail = view.findViewById(R.id.tv_change_email);
        TextView cPassword = view.findViewById(R.id.tv_change_password);
        String account = ((MainActivity) requireActivity()).getAccount();
        Button logout = view.findViewById(R.id.bt_logout);

        cEmail.setOnClickListener(v->{
            Toast.makeText(requireActivity(), "success", Toast.LENGTH_SHORT).show();
            Dialog dialog = new Dialog(requireActivity());
            dialog.setContentView(R.layout.dialog_changemail);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
            okay_text = dialog.findViewById(R.id.bt_change_email);
            cancel_text = dialog.findViewById(R.id.cancel_change_email);
            okay_text.setOnClickListener(v1 -> {
                EditText emailOldEditText = view.findViewById(R.id.et_email_old);
                String emailOld = emailOldEditText.getText().toString();

                EditText emailNewEditText = view.findViewById(R.id.et_email_new);
                String emailNew = emailNewEditText.getText().toString();

                EditText emailNew2EditText = view.findViewById(R.id.et_email_new2);
                String emailNew2 = emailNew2EditText.getText().toString();

                // non-empty validate
                if (TextUtils.isEmpty(emailOld) || TextUtils.isEmpty(emailNew) || TextUtils.isEmpty(emailNew2)) {
                    Toast.makeText(requireActivity(), "Change failed due to the presence of an empty input", Toast.LENGTH_SHORT).show();
                    return;
                }

                // email validate
                if (!TextUtils.equals(emailNew, emailNew2)) {
                    Toast.makeText(requireActivity(), "Inconsistent emails", Toast.LENGTH_SHORT).show();
                    return;
                }

                // change email
                SQLiteOpenHelper database = TimeyDbHelper.getInstance(getContext());
                SQLiteDatabase db = database.getWritableDatabase();

                if(db.isOpen()){
                    // check if the user had been registered
                    Cursor cursor = db.query("users",null,"email=? and telephone=?", new String[]{emailOld,account},null,null,null);
                    @SuppressLint("Recycle") Cursor cursor2 = db.query("users",null,"email=? and telephone=?", new String[]{emailNew,account},null,null,null);

                    if (cursor.getCount() == 0) {
                        // did not find email
                        Toast.makeText(requireActivity(), "Wrong email", Toast.LENGTH_SHORT).show();
                    } else {
                        if (cursor2.getCount() == 0) {
                            String sql = "update users set email=? where email=?";
                            db.execSQL(sql, new Object[]{emailNew, emailOld});
                            Intent intentToForget = new Intent(requireActivity(), MainActivity.class);
                            startActivity(intentToForget);
                            Toast.makeText(requireActivity(), "The email was successfully changed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireActivity(), "This email is already in use", Toast.LENGTH_SHORT).show();
                        }
                    }
                    cursor.close();
                    db.close();
                }
                dialog.dismiss();
            });

            cancel_text.setOnClickListener(v12 -> dialog.dismiss());
            dialog.show();
        });
        // change email function
//        changeEmail.setOnClickListener(v -> {
//            EditText emailOldEditText = view.findViewById(R.id.et_email_old);
//            String emailOld = emailOldEditText.getText().toString();
//
//            EditText emailNewEditText = view.findViewById(R.id.et_email_new);
//            String emailNew = emailNewEditText.getText().toString();
//
//            EditText emailNew2EditText = view.findViewById(R.id.et_email_new2);
//            String emailNew2 = emailNew2EditText.getText().toString();
//
//            // non-empty validate
//            if (TextUtils.isEmpty(emailOld) || TextUtils.isEmpty(emailNew) || TextUtils.isEmpty(emailNew2)) {
//                Toast.makeText(requireActivity(), "Change failed due to the presence of an empty input", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // email validate
//            if (!TextUtils.equals(emailNew, emailNew2)) {
//                Toast.makeText(requireActivity(), "Inconsistent emails", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // change email
//            SQLiteOpenHelper database = TimeyDbHelper.getInstance(getContext());
//            SQLiteDatabase db = database.getWritableDatabase();
//
//            if(db.isOpen()){
//                // check if the user had been registered
//                Cursor cursor = db.query("users",null,"email=? and telephone=?", new String[]{emailOld,account},null,null,null);
//                Cursor cursor2 = db.query("users",null,"email=? and telephone=?", new String[]{emailNew,account},null,null,null);
//
//                if (cursor.getCount() == 0) {
//                    // did not find email
//                    Toast.makeText(requireActivity(), "Wrong email", Toast.LENGTH_SHORT).show();
//                } else {
//                    if (cursor2.getCount() == 0) {
//                        String sql = "update users set email=? where email=?";
//                        db.execSQL(sql, new Object[]{emailNew, emailOld});
//                        Intent intentToForget = new Intent(requireActivity(), MainActivity.class);
//                        startActivity(intentToForget);
//                        Toast.makeText(requireActivity(), "The email was successfully changed", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(requireActivity(), "This email is already in use", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                cursor.close();
//                db.close();
//            }
//
//        });

        // change password function
        cPassword.setOnClickListener(v->{
            Dialog dialog = new Dialog(requireActivity());
            dialog.setContentView(R.layout.dialog_changepassword);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
            okay_text2 = dialog.findViewById(R.id.bt_change_password);
            cancel_text2 = dialog.findViewById(R.id.cancel_change_password);
            okay_text2.setOnClickListener(v3->{
                EditText passwordOldEditText = view.findViewById(R.id.et_password_old);
                String passwordOld = passwordOldEditText.getText().toString();

                EditText passwordNewEditText = view.findViewById(R.id.et_password_new);
                String passwordNew = passwordNewEditText.getText().toString();

                EditText passwordNew2EditText = view.findViewById(R.id.et_password_new2);
                String passwordNew2 = passwordNew2EditText.getText().toString();

                // non-empty validate
                if (TextUtils.isEmpty(passwordOld) || TextUtils.isEmpty(passwordNew) || TextUtils.isEmpty(passwordNew2)) {
                    Toast.makeText(requireActivity(), "Change failed due to the presence of an empty input", Toast.LENGTH_SHORT).show();
                    return;
                }

                // password validate
                if (!TextUtils.equals(passwordNew, passwordNew2)) {
                    Toast.makeText(requireActivity(), "Inconsistent passwords", Toast.LENGTH_SHORT).show();
                    return;
                }

                // change password
                SQLiteOpenHelper database = TimeyDbHelper.getInstance(getContext());
                SQLiteDatabase db = database.getWritableDatabase();

                if(db.isOpen()){
                    // check if the user had been registered
                    Cursor cursor = db.query("users",null,"password=? and telephone=?", new String[]{passwordOld,account},null,null,null);

                    if (cursor.getCount() == 0) {
                        // did not find password
                        Toast.makeText(requireActivity(), "Wrong password", Toast.LENGTH_SHORT).show();
                    } else {
                        String sql = "update users set password=? where password=?";
                        db.execSQL(sql, new Object[]{passwordNew, passwordOld});
                        Intent intentToForget = new Intent(requireActivity(), MainActivity.class);
                        startActivity(intentToForget);
                        Toast.makeText(requireActivity(), "The password was successfully changed", Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                    db.close();
                }
                dialog.dismiss();
            });
            cancel_text2.setOnClickListener(v12 -> dialog.dismiss());
            dialog.show();
        });
//        changePassword.setOnClickListener(v -> {
//            EditText passwordOldEditText = view.findViewById(R.id.et_password_old);
//            String passwordOld = passwordOldEditText.getText().toString();
//
//            EditText passwordNewEditText = view.findViewById(R.id.et_password_new);
//            String passwordNew = passwordNewEditText.getText().toString();
//
//            EditText passwordNew2EditText = view.findViewById(R.id.et_password_new2);
//            String passwordNew2 = passwordNew2EditText.getText().toString();
//
//            // non-empty validate
//            if (TextUtils.isEmpty(passwordOld) || TextUtils.isEmpty(passwordNew) || TextUtils.isEmpty(passwordNew2)) {
//                Toast.makeText(requireActivity(), "Change failed due to the presence of an empty input", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // password validate
//            if (!TextUtils.equals(passwordNew, passwordNew2)) {
//                Toast.makeText(requireActivity(), "Inconsistent passwords", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // change password
//            SQLiteOpenHelper database = TimeyDbHelper.getInstance(getContext());
//            SQLiteDatabase db = database.getWritableDatabase();
//
//            if(db.isOpen()){
//                // check if the user had been registered
//                Cursor cursor = db.query("users",null,"password=? and telephone=?", new String[]{passwordOld,account},null,null,null);
//
//                if (cursor.getCount() == 0) {
//                    // did not find password
//                    Toast.makeText(requireActivity(), "Wrong password", Toast.LENGTH_SHORT).show();
//                } else {
//                    String sql = "update users set password=? where password=?";
//                    db.execSQL(sql, new Object[]{passwordNew, passwordOld});
//                    Intent intentToForget = new Intent(requireActivity(), MainActivity.class);
//                    startActivity(intentToForget);
//                    Toast.makeText(requireActivity(), "The password was successfully changed", Toast.LENGTH_SHORT).show();
//                }
//                cursor.close();
//                db.close();
//            }
//
//        });

        // logout function
        logout.setOnClickListener(v -> logoutActivity());

    }
    // log out function
    public void logoutActivity() {

        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        //下面2个flags ,可以将原有任务栈清空
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

    }

}