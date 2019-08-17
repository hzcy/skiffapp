package com.yellowriver.skiff.View.Activity.Other;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;


import com.yellowriver.skiff.R;
import com.yellowriver.skiff.View.Fragment.About.LicenseFragment;
import com.yellowriver.skiff.View.Fragment.About.SettingsFragment;

/**
 * 设置界面
 * @author huang
 */
public class SettingsActivity extends AppCompatActivity {


    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = findViewById(R.id.toolbar);

        mToolbar.setTitle("设置");


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
                .commit();
    }
}
