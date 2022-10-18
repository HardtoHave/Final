package com.example.afinal.ui.store;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.afinal.R;
import com.example.afinal.db.DBUser;
import com.example.afinal.ui.focus.ChooseDialog;
import com.example.afinal.ui.focus.FocusFragment;


public class StoreFragment extends Fragment {

    private Button mario, kabi, koala, wukong;
    private int m_coinNum;
    private final DBUser dbHelper= (DBUser) DBUser.getInstance(getContext());

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_store, container,false);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        ChooseDialog chooseDialog=new ChooseDialog(requireActivity());
        super.onViewCreated(view, savedInstanceState);
        mario=view.findViewById(R.id.mario);
        kabi=view.findViewById(R.id.kabi);
        koala=view.findViewById(R.id.koala);
        wukong=view.findViewById(R.id.wukong);
        SharedPreferences preferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
        m_coinNum = preferences.getInt("coinSum",0);

        mario.setOnClickListener(v -> {
            if(mario.getText().equals("$2000")&&m_coinNum-10>=0){
                m_coinNum-=10;
                editor.putInt("coinSum",m_coinNum);
                editor.apply();
                mario.setText(R.string.equip);
                FocusFragment.passDataInterface.passData(m_coinNum+"");
                ContentValues values=new ContentValues();
                values.put("decoration","mario");
                values.put("isEquip",true);
                //db.insert("theme",null,values);
                Toast.makeText(requireActivity(), "charge success ٩( 'ω' )و ", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(requireActivity(), "Your coins not enough (○ﾟεﾟ○)", Toast.LENGTH_SHORT).show();
            }

        });
        kabi.setOnClickListener(v->{
            if(kabi.getText().equals("$2000")&&m_coinNum-2000>=0){
                m_coinNum-=2000;
                editor.putInt("coinSum",m_coinNum);
                editor.apply();
                kabi.setText(R.string.equip);
                ContentValues values=new ContentValues();
                values.put("decoration","kabi");
                values.put("isEquip",true);
                //db.insert("theme",null,values);
                Toast.makeText(requireActivity(), "charge success ٩( 'ω' )و ", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(requireActivity(), "Your coins not enough (○ﾟεﾟ○)", Toast.LENGTH_SHORT).show();
            }

        });
        koala.setOnClickListener(v->{
            if(koala.getText().equals("$2000")&&m_coinNum-2000>=0){
                m_coinNum-=2000;
                editor.putInt("coinSum",m_coinNum);
                editor.apply();
                koala.setText(R.string.equip);
                ContentValues values=new ContentValues();
                values.put("decoration","koala");
                values.put("isEquip",true);
                //db.insert("theme",null,values);
                Toast.makeText(requireActivity(), "charge success ٩( 'ω' )و ", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(requireActivity(), "Your coins not enough (○ﾟεﾟ○)", Toast.LENGTH_SHORT).show();
            }

        });
        wukong.setOnClickListener(v->{
            if(wukong.getText().equals("$2000")&&m_coinNum-2000>=0){
                m_coinNum-=2000;
                editor.putInt("coinSum",m_coinNum);
                editor.apply();
                wukong.setText(R.string.equip);
                ContentValues values=new ContentValues();
                values.put("decoration","wukong");
                values.put("isEquip",true);
                //db.insert("theme",null,values);
                Toast.makeText(requireActivity(), "charge success ٩( 'ω' )و ", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(requireActivity(), "Your coins not enough (○ﾟεﾟ○)", Toast.LENGTH_SHORT).show();
            }
        });
    }


}