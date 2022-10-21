package com.example.afinal.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.afinal.R;
import com.example.afinal.databinding.ActivityLoginBinding;
import com.example.afinal.db.TimeyDbHelper;
import com.example.afinal.utility.ValidUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    // declare SharedPreferences to save data
    SharedPreferences sp;
    // declare SharedPreferencese editor to change sp
    SharedPreferences.Editor editor;


    private ActivityLoginBinding loginBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //bind activity_login.xml
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setOnClickListener();

         /*
            when lost inout focus, check data and remind error mesg
            1st param：input object
            2nd param：input data type
            3rd param：illegal information
         */
        setOnFocusChangeErrMsg(loginBinding.etAccount, "phone", "Incorrect mobile number format");
        setOnFocusChangeErrMsg(loginBinding.etPassword, "password", "Password must be at least 6 digits");

        SQLiteOpenHelper database = TimeyDbHelper.getInstance(this);
        database.getReadableDatabase();
    }
    /*
    show error mesg
     */
    private void setOnFocusChangeErrMsg(EditText editText, String inputType, String errMsg) {
        editText.setOnFocusChangeListener(
                (v, hasFocus) -> {
                    String inputStr = editText.getText().toString();
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
                            default:
                                break;
                        }
                    }
                }
        );
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        // validate account and password
        String account = loginBinding.etAccount.getText().toString();
        String password = loginBinding.etPassword.getText().toString();

        switch (view.getId()) {
            case R.id.bt_login:
                // trigger setOnFocusChangeErrMsg
                loginBinding.etPassword.clearFocus();
                // validate before sending url
                if (!(ValidUtils.isPhoneValid(account) && ValidUtils.isPasswordValid(password))) {
                    Toast.makeText(this, "Wrong account or password format", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    SQLiteOpenHelper database = TimeyDbHelper.getInstance(this);
                    SQLiteDatabase db = database.getWritableDatabase();

                    if(db.isOpen()){
                        // check if the user had been registered
                        Cursor cursor = db.query("users",null,"telephone=? and password=?", new String[]{account,password},null,null,null);

                        if (cursor.getCount() == 0) {
                            // did not find account
                            Toast.makeText(this, "Wrong password or account", Toast.LENGTH_SHORT).show();

                        } else {
                            Intent intentToMain = new Intent(this, MainActivity.class);
                            intentToMain.putExtra("phoneNumber",account);
                            startActivity(intentToMain);
                        }

                        cursor.close();
                        db.close();
                    }
                }
                break;
            // register user
            case R.id.tv_to_register:
                /*
                  intent passing data to register page
                 */
                Intent intentToRegister = new Intent(this, RegisterActivity.class);
                intentToRegister.putExtra("account", account);
                startActivity(intentToRegister);
                break;
            // forget password event
            case R.id.tv_forget_password:
                Intent intentToForget = new Intent(this, ForgetPasswordActivity.class);
                startActivity(intentToForget);
                break;

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    // set click listener
    private void setOnClickListener() {
        loginBinding.btLogin.setOnClickListener(this); // login
        loginBinding.tvToRegister.setOnClickListener(this); // register
        loginBinding.tvForgetPassword.setOnClickListener(this); //forget password
    }
}