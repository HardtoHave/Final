package com.example.afinal.ui.focus;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.afinal.R;
import com.example.afinal.db.DBUser;


public class ChooseDialog extends Dialog {

    private String title;
    private View dialogView;
    public ImageView mario,kabi,koala,wukong;
    private marioOnClickListener marioClick;
    private kabiOnClickListener kabiClick;
    private koalaOnClickListener koalaClick;
    private wukongOnClickListener wukongClick;

    public ChooseDialog(Context context){
        super(context, R.style.Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose);
        Window window = getWindow();
        window.setWindowAnimations(R.style.chooseDialogAnim);
        setCanceledOnTouchOutside(true);

        mario=findViewById(R.id.theme_mario);
        kabi = findViewById(R.id.starBurst);
        koala = findViewById(R.id.time);
        wukong = findViewById(R.id.star);
        mario.setVisibility(View.GONE);
        kabi.setVisibility(View.GONE);
        koala.setVisibility(View.GONE);
        wukong.setVisibility(View.GONE);
        DBUser dbHelper= (DBUser) DBUser.getInstance(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] projection={"decoration"};
        String selection="isEquip";
        String[] selectionArgs={String.valueOf(true)};
        @SuppressLint("Recycle") Cursor cursor=db.query("theme",projection,selection,selectionArgs,null,null,null);
        while (cursor.moveToNext()){
            for (int i = 0; i < 4; i++) {
                String name=cursor.getString(i);
                switch (name){
                    case "mario":
                        mario.setVisibility(View.VISIBLE);
                        break;
                    case "kabi":
                        kabi.setVisibility(View.VISIBLE);
                        break;
                    case "koala":
                        koala.setVisibility(View.VISIBLE);
                        break;
                    case "wukong":
                        wukong.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
        initClick();
    }

    private void initClick(){
        mario.setOnClickListener(v->marioClick.onClick());
        kabi.setOnClickListener(v -> kabiClick.onClick());
        koala.setOnClickListener( v -> koalaClick.onClick());
        wukong.setOnClickListener(v -> wukongClick.onClick());
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
    public interface wukongOnClickListener{
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
    public void setWukongClickListener(wukongOnClickListener wukongOnClickListener){
        this.wukongClick = wukongOnClickListener;
    }
}


