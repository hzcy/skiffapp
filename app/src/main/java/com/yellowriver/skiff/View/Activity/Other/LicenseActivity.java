package com.yellowriver.skiff.View.Activity.Other;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;


import com.yellowriver.skiff.R;
import com.yellowriver.skiff.View.Fragment.About.About2Fragment;
import com.yellowriver.skiff.View.Fragment.About.LicenseFragment;

import java.util.Objects;

/**
 * 开源许可
 * @author huang
 */
public class LicenseActivity extends AppCompatActivity {

    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        mToolbar = findViewById(R.id.toolbar);

        mToolbar.setTitle("开源许可");


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LicenseFragment())
                .commit();
    }
}
