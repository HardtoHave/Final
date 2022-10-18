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
    // 声明SharedPreferences对象,保存应用数据
    SharedPreferences sp;
    // 声明SharedPreferences编辑器对象，修改sp的值
    SharedPreferences.Editor editor;

    // Log打印的通用Tag
    private final String TAG = "LoginActivity";


    private ActivityLoginBinding loginBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定activity_login.xml
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

//        Uri uri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.login_video);
//        VideoView mVideoView = (VideoView)findViewById(R.id.back_video);
//        mVideoView.setVideoURI(uri);
//        mVideoView.start();
//        mVideoView.setOnPreparedListener(mediaPlayer -> {
//            mediaPlayer.setLooping(true);
//        });
        //设置监听事件
        setOnClickListener();

         /*
            当输入框焦点失去时,检验输入数据，提示错误信息
            第一个参数：输入框对象
            第二个参数：输入数据类型
            第三个参数：输入不合法时提示信息
         */
        setOnFocusChangeErrMsg(loginBinding.etAccount, "phone", "Incorrect mobile number format");
        setOnFocusChangeErrMsg(loginBinding.etPassword, "password", "Password must be at least 6 digits");

        SQLiteOpenHelper database = TimeyDbHelper.getInstance(this);
        database.getReadableDatabase();
    }
    /*
    当账号输入框失去焦点时，校验账号
    当密码输入框失去焦点时，校验密码
    如有错误，提示错误信息
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
        // 获取用户输入的账号和密码以进行验证
        String account = loginBinding.etAccount.getText().toString();
        String password = loginBinding.etPassword.getText().toString();

        switch (view.getId()) {
            // 登录按钮 响应事件
            case R.id.bt_login:
                // 让密码输入框失去焦点,触发setOnFocusChangeErrMsg方法
                loginBinding.etPassword.clearFocus();
                // 发送URL请求之前,先进行校验
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
            // 注册用户 响应事件
            case R.id.tv_to_register:
                /*
                  关于这里传参说明：给用户一个良好的体验，
                  如果在登录界面填写过的，就不需要再填了
                  所以Intent把填写过的数据传递给注册界面
                 */
                Intent intentToRegister = new Intent(this, RegisterActivity.class);
                intentToRegister.putExtra("account", account);
                startActivity(intentToRegister);
                break;
            // 忘记密码响应事件
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

    // 为点击事件的UI对象设置监听器
    private void setOnClickListener() {
        loginBinding.btLogin.setOnClickListener(this); // 登录按钮
        loginBinding.tvToRegister.setOnClickListener(this); // 注册文字
        loginBinding.tvForgetPassword.setOnClickListener(this); //忘记密码
    }
}