package com.yellowriver.skiff.View.Activity.Other;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IWebLayout;
import com.just.agentweb.NestedScrollAgentWebView;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;
import com.yellowriver.skiff.Help.WebLayout;
import com.yellowriver.skiff.R;

import org.jetbrains.annotations.NotNull;

/**
 * 内置浏览器
 * @author huang
 */
public class AboutWebViewActivity extends AppCompatActivity {
    private static String TAG = "WebViewActivity";
    private AgentWeb mAgentWeb;
    private CoordinatorLayout main;


    private String link;

    private String fromTitle;

    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutwebview);

        initView();
    }

    NestedScrollAgentWebView webView;

    private void initView() {
        //设置状态栏全透明  //设置白底黑字
        main = findViewById(R.id.main);
        mToolbar = findViewById(R.id.toolbar);

        //获取连接地址
        link = getIntent().getStringExtra("qzLink");

        fromTitle = getIntent().getStringExtra("qzTitle");

        mToolbar.setTitle(fromTitle);
        setSupportActionBar(mToolbar);


        //滑动隐藏AppBarLayout
       webView = new NestedScrollAgentWebView(this);

        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(-1, -1);

        lp.setBehavior(new AppBarLayout.ScrollingViewBehavior());

        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(main, 1, lp)//lp记得设置behavior属性
                //.setAgentWebParent((LinearLayout) view, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
                .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                //.setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setWebLayout(getWebLayout())
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                //.setAgentWebUIController(new UIController(getActivity())) //自定义UI  AgentWeb3.0.0 加入。
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                //.setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                .setWebView(webView)
                .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                .createAgentWeb()//创建AgentWeb。
                .ready()//设置 WebSettings。
                .go(link); //WebView载入该url地址的页面并显示。


        mAgentWeb.getWebCreator().getWebView().getSettings().setTextZoom(100);

        //添加下拉背景  轻舟logo
        addBgChild(mAgentWeb.getWebCreator().getWebParentLayout());
        // AgentWeb 没有把WebView的功能全面覆盖 ，所以某些设置 AgentWeb 没有提供 ， 请从WebView方面入手设置。
        mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
    }






    private IWebLayout getWebLayout() {
        return new WebLayout(this);
    }

    //添加下拉背景
    private void addBgChild(FrameLayout frameLayout) {

        ImageView mImageView = new ImageView(frameLayout.getContext());
        mImageView.setImageResource(R.mipmap.skifflogo_round);

        TextView mTextView = new TextView(frameLayout.getContext());
        mTextView.setText("轻舟");
        mTextView.setTextSize(18);
        mTextView.setTextColor(Color.parseColor("#FFFFFF"));
        frameLayout.setBackgroundColor(Color.parseColor("#272b2d"));
        FrameLayout.LayoutParams mFlp = new FrameLayout.LayoutParams(-2, -2);

        mFlp.gravity = Gravity.CENTER_HORIZONTAL;
        final float scale = frameLayout.getContext().getResources().getDisplayMetrics().density;
        mFlp.topMargin = (int) (15 * scale + 0.5f);

        FrameLayout.LayoutParams mFlp2 = new FrameLayout.LayoutParams(-2, -2);

        mFlp2.gravity = Gravity.CENTER_HORIZONTAL;
        final float scale2 = frameLayout.getContext().getResources().getDisplayMetrics().density;
        mFlp2.topMargin = (int) (92 * scale2 + 0.5f);

        frameLayout.addView(mImageView, 0, mFlp);
        frameLayout.addView(mTextView, 0, mFlp2);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

            if (mAgentWeb.handleKeyEvent(keyCode, event)) {
                return true;
            }

        return super.onKeyDown(keyCode, event);
    }

    private final WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {


            return super.shouldOverrideUrlLoading(view, request);


        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            Log.i("Info", "BaseWebActivity onPageStarted");
        }
    };
    private final WebChromeClient mWebChromeClient = new WebChromeClient() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);


                mToolbar.setTitle(title);



        }
    };


    private static final int MENU_CONFIRM = 18;
    MenuItem item;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        item = menu.add(Menu.NONE, MENU_CONFIRM, 0, "关闭");
        //主要是这句话
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //添加监听事件
        //item.setOnMenuItemClickListener(listener);

        item.setIcon(R.drawable.ic_close_writer_24dp);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case MENU_CONFIRM:
                finish();
                break;

            default:
                break;
        }
        return false;
    }







    @Override
    public void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mAgentWeb.destroy();
        mAgentWeb.getWebLifeCycle().onDestroy();

    }



}
