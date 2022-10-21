package com.example.afinal.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.afinal.R;
import com.example.afinal.databinding.ActivityRegisterBinding;
import com.example.afinal.db.TimeyDbHelper;
import com.example.afinal.utility.ValidUtils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    String account = "";
    private ActivityRegisterBinding registerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        // get login page data
        Intent intent = getIntent();
        account = intent.getStringExtra("account");
        registerBinding.etTelephone.setText(account);
        // set listener
        setOnClickListener();
        //same as function in login activity
        setOnFocusChangeErrMsg(registerBinding.etTelephone, "phone", "Incorrect mobile number format");
        setOnFocusChangeErrMsg(registerBinding.etPassword, "password", "Password must be at least 6 digits");
        setOnFocusChangeErrMsg(registerBinding.etGender, "email", "Incorrect email format");
    }

    /*
    validate account and password
     */
    private void setOnFocusChangeErrMsg(EditText editText, String inputType, String errMsg) {
        editText.setOnFocusChangeListener(
                (v, hasFocus) -> {
                    String inputStr = editText.getText().toString();
                    // lost focus
                    if (!hasFocus) {
                        switch (inputType) {
                            case "phone":
                                if (!ValidUtils.isPhoneValid(inputStr)) {
                                    editText.setError(errMsg);
                                }
                                break;

                            case "password":
                                if (!ValidUtils.isPasswordValid(inputStr)) {
                                    editText.setError(errMsg);
                                }
                                break;

                            case "email":
                                if (!ValidUtils.isEmailValid(inputStr)) {
                                    editText.setError(errMsg);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
        );
    }

    private void setOnClickListener() {
        registerBinding.btSubmitRegister.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        String telephone = registerBinding.etTelephone.getText().toString();
        String username = registerBinding.etUsername.getText().toString();
        String gender = registerBinding.etGender.getText().toString();
        String password1 = registerBinding.etPassword.getText().toString();
        String password2 = registerBinding.etPassword2.getText().toString();

        if (view.getId() == R.id.bt_submit_register) {
            //insert data
            newRegister(telephone, username, gender, password1, password2);
        }
    }

    private void newRegister(String telephone , String username, String email, String password1, String password2) {
        // non-empty validate
        if (TextUtils.isEmpty(telephone)  || TextUtils.isEmpty(username)
                || TextUtils.isEmpty(email) || TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)) {
            Toast.makeText(RegisterActivity.this, "Registration failed due to the presence of an empty input", Toast.LENGTH_SHORT).show();
            return;
        }

        // password validate
        if (!TextUtils.equals(password1, password2)) {
            Toast.makeText(RegisterActivity.this, "Inconsistent passwords", Toast.LENGTH_SHORT).show();
            return;
        }

        // register
        SQLiteOpenHelper database = TimeyDbHelper.getInstance(this);
        SQLiteDatabase db = database.getWritableDatabase();

        if(db.isOpen()){
            // check if the user had been registered
            Cursor cursor = db.rawQuery("select * from users where telephone = " + telephone, null);

            if (cursor.getCount() == 0) {
                // register
                ContentValues values = new ContentValues();
                values.put("telephone",telephone);
                values.put("username",username);
                values.put("email",email);
                values.put("password",password1);
                db.insert("users",null,values);
                Intent intentToMain = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intentToMain);
            } else {
                Toast.makeText(RegisterActivity.this, "The phone number is already registered", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
            db.close();
        }
    }
}