package com.yellowriver.skiff.View.Activity.Other;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;


import com.yellowriver.skiff.R;
import com.yellowriver.skiff.View.Fragment.About.LicenseFragment;
import com.yellowriver.skiff.View.Fragment.About.SettingsBetaFragment;
import com.yellowriver.skiff.View.Fragment.About.SettingsFragment;

/**
 * 设置界面
 * @author huang
 */
public class SettingsActivity extends AppCompatActivity {


    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        return SkinAppCompatDelegateImpl.get(this, this);
    }

    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = findViewById(R.id.toolbar);

        mToolbar.setTitle("设置");
        setSupportActionBar(mToolbar);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
                .commit();
//        String versionname = getPackageInfo(getApplicationContext()).versionName;
//
//        String[] sourceStrArray2 = versionname.split("\\.");
//        if(sourceStrArray2!=null) {
//            if (sourceStrArray2.length <= 3) {
//                Log.d("dd", "onCreate: "+sourceStrArray2.length);
//                //正式版  1.0.2 1.0.3 1.0 1.1
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, new SettingsFragment())
//                        .commit();
//            }else{
//                Log.d("dd", "onCreate: "+sourceStrArray2.length);
//                //测试版 1.0.2.1 1.0.2.5
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, new SettingsBetaFragment())
//                        .commit();
//
//            }
//        }


    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pInfo = null;

        try {
            //通过PackageManager可以得到PackageInfo
            PackageManager pManager = context.getPackageManager();
            pInfo = pManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pInfo;
    }
}
