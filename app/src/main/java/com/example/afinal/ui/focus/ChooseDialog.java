package com.example.afinal.ui.focus;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.afinal.R;


public class ChooseDialog extends Dialog {

    private String title;
    private View dialogView;
    public ImageView mario,kabi,koala;
    private marioOnClickListener marioClick;
    private kabiOnClickListener kabiClick;
    private koalaOnClickListener koalaClick;

    public ChooseDialog(Context context){
        super(context, R.style.Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose);
        Window window = getWindow();
        window.setWindowAnimations(R.style.chooseDialogAnim);
        //cancel animation when click outside
        setCanceledOnTouchOutside(true);
        //dialog to change theme
        mario=findViewById(R.id.theme_mario);
        kabi = findViewById(R.id.starBurst);
        koala = findViewById(R.id.time);
        initClick();
    }

    private void initClick(){
        mario.setOnClickListener(v->marioClick.onClick());
        kabi.setOnClickListener(v -> kabiClick.onClick());
        koala.setOnClickListener( v -> koalaClick.onClick());
    }

    public interface marioOnClickListener{
        void onClick();
    }
    public interface kabiOnClickListener{
        void onClick();
    }
    public interface koalaOnClickListener{
        void onClick();
    }
    public void setMarioOnClickListener(marioOnClickListener marioOnClickListener){
        this.marioClick=marioOnClickListener;
    }
    public void setKabiOnClickListener(kabiOnClickListener kabiOnClickListener){
        this.kabiClick = kabiOnClickListener;
    }
    public void setKoalaOnClickListener(koalaOnClickListener koalaOnClickListener){
        this.koalaClick = koalaOnClickListener;
    }
}


