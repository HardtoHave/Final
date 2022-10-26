package com.example.afinal.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.afinal.R;
import com.example.afinal.databinding.ActivityMainBinding;
import com.example.afinal.ui.focus.FocusFragment;
import com.example.afinal.ui.store.StoreFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements FocusFragment.PassDataInterface,StoreFragment.PassThemeInterface {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    TextView coin;
    public String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            account = extras.getString("phoneNumber");
        }
        //bind bar and drawer
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        coin= Objects.requireNonNull(binding.appBarMain.coinSum);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                 R.id.nav_focus,R.id.nav_timeLine, R.id.nav_store,R.id.nav_setting)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        Bundle bundle = new Bundle();
        bundle.putString("account", account);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //nav controller
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    //interface implementation
    @Override
    public void passData(String data) {
        binding.appBarMain.coinSum.setText(data);
    }

    public String getAccount(){
        return account;
    }

    @Override
    public void PassThemeData(String data) {
//        FragmentManager fragmentManager=getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//        FocusFragment fFragment=new FocusFragment();
        switch (data) {
            case "mario":
                setTheme(R.style.Theme_ChangeMario);
//                androidx.appcompat.widget.Toolbar toolbar=findViewById(R.id.toolbar);
//                toolbar.setBackground(ResourcesCompat.getDrawable(getResources(),R.color.red,null));
                break;
            case "kabi":
                setTheme(R.style.Theme_ChangeKabi);
                break;
            case "koala":
                setTheme(R.style.Theme_ChangeKoala);
                break;
            case "wukong":
                setTheme(R.style.Theme_ChangeWukong);
                break;
        }
    }
}