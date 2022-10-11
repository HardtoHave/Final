package com.example.afinal.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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

import com.example.afinal.R;
import com.example.afinal.databinding.ActivityRegisterBinding;
import com.example.afinal.db.TimeyDbHelper;
import com.example.afinal.utils.ValidUtils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    // Log打印的通用Tag
    private final String TAG = "RegisterActivity";
    String account = "";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private ActivityRegisterBinding registerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        // 接收用户在登录界面输入的数据，简化用户操作（如果输入过了就不用再输入了）
        // 注意接收上一个页面 Intent 的信息，需要 getIntent() 即可，而非重新 new 一个 Intent
        Intent intent = getIntent();
        account = intent.getStringExtra("account");
        // 把对应的 account 设置到 telephone 输入框
        registerBinding.etTelephone.setText(account);
        // 为点击事件设置监听器
        setOnClickListener();

         /*
            设置当输入框焦点失去时提示错误信息
            第一个参数：输入框对象
            第二个参数：输入数据类型
            第三个参数：输入不合法时提示信息
         */
        setOnFocusChangeErrMsg(registerBinding.etTelephone, "phone", "Incorrect mobile number format");
        setOnFocusChangeErrMsg(registerBinding.etPassword, "password", "Password must be at least 6 digits");
        setOnFocusChangeErrMsg(registerBinding.etGender, "email", "Incorrect email format");
    }

    /*
    当输入账号FocusChange时，校验账号
    当输入密码FocusChange时，校验密码
     */
    private void setOnFocusChangeErrMsg(EditText editText, String inputType, String errMsg) {
        editText.setOnFocusChangeListener(
                (v, hasFocus) -> {
                    String inputStr = editText.getText().toString();
                    // 失去焦点
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

        switch (view.getId()) {
            case R.id.bt_submit_register:
                newRegister(telephone, username, gender, password1, password2);
                // 点击提交注册按钮响应事件
                // 尽管后端进行了判空，但Android端依然需要判空
                break;
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