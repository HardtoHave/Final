package com.example.afinal.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.afinal.activity.MainActivity;

public class notificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent toMainActivityIntent=new Intent(context, MainActivity.class);
        toMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(toMainActivityIntent);
    }
}
