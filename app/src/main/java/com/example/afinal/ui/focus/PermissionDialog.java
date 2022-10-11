package com.example.afinal.ui.focus;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.afinal.R;


public class PermissionDialog extends Dialog {
    private Context context;
    private TextView jumpButton;
    private jumpClickListener jumpListener;

    public PermissionDialog(Context context){
        super(context, R.style.Dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_permission);
        setCanceledOnTouchOutside(true);
        jumpButton = findViewById(R.id.jump);
        jumpButton.setOnClickListener(view -> {
            try {
                jumpListener.onClick();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public interface jumpClickListener{
        void onClick() throws ClassNotFoundException;
    }

    public void setJumpClickListener(jumpClickListener jumpClickListener){
        this.jumpListener = jumpClickListener;
    }
}
