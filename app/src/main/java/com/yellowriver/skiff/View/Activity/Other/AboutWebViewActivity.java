package com.yellowriver.skiff.View.Activity.Other;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.Extra;
import com.download.library.ResourceRequest;
import com.google.android.material.appbar.AppBarLayout;

import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultDownloadImpl;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.IWebLayout;
import com.just.agentweb.MiddlewareWebChromeBase;
import com.just.agentweb.MiddlewareWebClientBase;
import com.just.agentweb.NestedScrollAgentWebView;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.WebViewClient;
import com.yellowriver.skiff.Help.AndroidInterface;
import com.yellowriver.skiff.Help.MiddlewareChromeClient;
import com.yellowriver.skiff.Help.MiddlewareWebViewClient;
import com.yellowriver.skiff.Help.WebLayout;
import com.yellowriver.skiff.R;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 内置浏览器
 *
 * @author huang
 */
public class AboutWebViewActivity extends AppCompatActivity {
    private static String TAG = "WebViewActivity";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.main)
    CoordinatorLayout main;
    @BindView(R.id.drawer_layout)
    CoordinatorLayout drawerLayout;
    private AgentWeb mAgentWeb;



    private String link;

    private String fromTitle;

    private MiddlewareWebClientBase mMiddleWareWebClient;
    private MiddlewareWebChromeBase mMiddleWareWebChrome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutwebview);
        ButterKnife.bind(this);

        initView();
    }

    NestedScrollAgentWebView webView;

    private void initView() {


        //获取连接地址
        link = getIntent().getStringExtra("qzLink");
        fromTitle = getIntent().getStringExtra("qzTitle");

        if ("分享海报".equals(fromTitle)){
            hideStausbar(true);
            appbar.setVisibility(View.GONE);
        }else {
            hideStausbar(false);
            appbar.setVisibility(View.VISIBLE);
            mToolbar.setTitle(fromTitle);
            setSupportActionBar(mToolbar);
        }



        //滑动隐藏AppBarLayout
        webView = new NestedScrollAgentWebView(this);

        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(-1, -1);

        lp.setBehavior(new AppBarLayout.ScrollingViewBehavior());

        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(main, 1, lp)//lp记得设置behavior属性
                //.setAgentWebParent((LinearLayout) view, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
                .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setWebLayout(getWebLayout())
                .useMiddlewareWebChrome(getMiddlewareWebChrome())
                .useMiddlewareWebClient(getMiddlewareWebClient())
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                //.setAgentWebUIController(new UIController(getActivity())) //自定义UI  AgentWeb3.0.0 加入。
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                .setWebView(webView)
                .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                .createAgentWeb()//创建AgentWeb。
                .ready()//设置 WebSettings。
                .go(link); //WebView载入该url地址的页面并显示。


        if (mAgentWeb != null) {
            mAgentWeb.getUrlLoader().reload(); // 刷新
        }
        mAgentWeb.getWebCreator().getWebView().getSettings().setTextZoom(100);

        if(mAgentWeb!=null){
            //注入对象
            mAgentWeb.getJsInterfaceHolder().addJavaObject("android",new AndroidInterface(this));
        }

        //添加下拉背景  轻舟logo
        addBgChild(mAgentWeb.getWebCreator().getWebParentLayout());
        // AgentWeb 没有把WebView的功能全面覆盖 ，所以某些设置 AgentWeb 没有提供 ， 请从WebView方面入手设置。
        mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
    }

    /**
     * @return IAgentWebSettings
     */
    public IAgentWebSettings getSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;

            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            /**
             * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
             * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.download.library:Downloader:4.1.1' ，
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
            @Override
            public WebListenerManager setDownloader(WebView webView, android.webkit.DownloadListener downloadListener) {
                return super.setDownloader(webView,
                        new DefaultDownloadImpl((Activity) webView.getContext(),
                                webView,
                                this.mAgentWeb.getPermissionInterceptor()) {

                            @Override
                            protected ResourceRequest createResourceRequest(String url) {
                                return DownloadImpl.getInstance(getApplicationContext())
                                        .with(getApplicationContext())
                                        .url(url)
                                        .quickProgress()
                                        .addHeader("", "")
                                        .setEnableIndicator(true)
                                        .autoOpenIgnoreMD5()
                                        .setRetry(5)
                                        .setBlockMaxTime(100000L);
                            }

                            @Override
                            protected void taskEnqueue(ResourceRequest resourceRequest) {
                                resourceRequest.enqueue(new DownloadListenerAdapter() {
                                    @Override
                                    public void onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, Extra extra) {
                                        super.onStart(url, userAgent, contentDisposition, mimetype, contentLength, extra);
                                    }

                                    @MainThread
                                    @Override
                                    public void onProgress(String url, long downloaded, long length, long usedTime) {
                                        super.onProgress(url, downloaded, length, usedTime);
                                    }

                                    @Override
                                    public boolean onResult(Throwable throwable, Uri path, String url, Extra extra) {
                                        return super.onResult(throwable, path, url, extra);
                                    }
                                });
                            }
                        });
            }
        };
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
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {


            return super.shouldOverrideUrlLoading(view, request);


        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("tencent://")) {
                return false;
            }
            return super.shouldOverrideUrlLoading(view, url);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("Info", "onResult:" + requestCode + " onResult:" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
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


    private void hideStausbar(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
        } else {
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
        }
    }



    /**
     * 列举正常情况下能正常加载的网页url
     * @param url
     * @return
     */
    private boolean urlCanLoad(String url)
    {
        return url.startsWith("http://") || url.startsWith("https://") ||
                url.startsWith("ftp://") || url.startsWith("file://");
    }

    /**
     * 打开第三方app。如果没安装则跳转到应用市场
     * @param url
     */
    private void startThirdpartyApp(String url)
    {
        try {
            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); // 注释1
            if (getPackageManager().resolveActivity(intent, 0) == null)
            {  // 如果手机还没安装app，则跳转到应用市场
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=" + intent.getPackage())); // 注释2
            }
            startActivity(intent);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
    }


    /**
     * MiddlewareWebClientBase 是 AgentWeb 3.0.0 提供一个强大的功能，
     * 如果用户需要使用 AgentWeb 提供的功能， 不想重写 WebClientView方
     * 法覆盖AgentWeb提供的功能，那么 MiddlewareWebClientBase 是一个
     * 不错的选择 。
     *
     * @return
     */
    protected MiddlewareWebClientBase getMiddlewareWebClient() {
        return this.mMiddleWareWebClient = new MiddlewareWebViewClient() {
            /**
             *
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "MiddlewareWebClientBase#shouldOverrideUrlLoading url:" + url);
				/*if (url.startsWith("agentweb")) { // 拦截 url，不执行 DefaultWebClient#shouldOverrideUrlLoading
					Log.i(TAG, "agentweb scheme ~");
					return true;
				}*/

                if (super.shouldOverrideUrlLoading(view, url)) { // 执行 DefaultWebClient#shouldOverrideUrlLoading
                    return true;
                }
                // do you work
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e(TAG, "MiddlewareWebClientBase#shouldOverrideUrlLoading request url:" + request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }
        };
    }

    protected MiddlewareWebChromeBase getMiddlewareWebChrome() {
        return this.mMiddleWareWebChrome = new MiddlewareChromeClient() {
        };
    }

}
