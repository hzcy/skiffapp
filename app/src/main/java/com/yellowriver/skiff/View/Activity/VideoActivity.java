package com.yellowriver.skiff.View.Activity;


import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import com.yellowriver.skiff.R;


/**
 * 简单视频播放器
 * @author huang
 */
public class VideoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        String videourl = getIntent().getStringExtra("videourl");
        String title = getIntent().getStringExtra("title");
        String cover = getIntent().getStringExtra("cover");
        JzvdStd mJzvd = findViewById(R.id.videoplayer);
        mJzvd.setUp(videourl, title);

        Log.d("video", "onCreate: "+ title + videourl);
        if (cover !=null) {
            if (!"".equals(cover)) {
                Glide.with(this).load(cover).into(mJzvd.thumbImageView);

            }
        }

    }


    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //home back
        Jzvd.goOnPlayOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();


        Jzvd.goOnPlayOnPause();

    }

}
