package com.example.afinal.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    protected Toast toast;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    // full screen
    protected void fullScreenConfig() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // implement sub thread to show Toast
    protected void showToastInThread(Context context, String msg) {
        runOnUiThread(() -> {
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            toast.show();
        });
    }
}
