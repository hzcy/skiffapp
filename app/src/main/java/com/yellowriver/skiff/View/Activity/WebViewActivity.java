package com.yellowriver.skiff.View.Activity;
import	java.util.Iterator;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.Extra;
import com.download.library.ResourceRequest;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultDownloadImpl;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.IWebLayout;
import com.just.agentweb.MiddlewareWebChromeBase;
import com.just.agentweb.MiddlewareWebClientBase;
import com.just.agentweb.NestedScrollAgentWebView;
import com.just.agentweb.PermissionInterceptor;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.WebViewClient;
import com.yellowriver.skiff.DataUtils.LocalUtils.SharedPreferencesUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.ReadModeUtils;
import com.yellowriver.skiff.Help.FileUtil;
import com.yellowriver.skiff.Help.MiddlewareChromeClient;
import com.yellowriver.skiff.Help.MiddlewareWebViewClient;
import com.yellowriver.skiff.Help.SmallUtils;
import com.yellowriver.skiff.Help.SnackbarUtil;
import com.yellowriver.skiff.Help.WebLayout;
import com.yellowriver.skiff.R;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yellowriver.skiff.R.drawable.ic_more_vert_black_24dp;

/**
 * 内置浏览器
 */
public class WebViewActivity extends AppCompatActivity {
    private static String TAG = "WebViewActivity";


    private static String voidurl = "data:text/html;charset=utf-8;base64,";
    private static String voidurl2 = "about:blank";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.main)
    CoordinatorLayout main;
    @BindView(R.id.drawer_layout)
    CoordinatorLayout drawerLayout;


    private String link;
    private String readHost;
    private String fromTitle;
    private String contentXpath;
    private String readCharset;
    private String content;

    private AgentWeb mAgentWeb;
    private MiddlewareWebClientBase mMiddleWareWebClient;
    private MiddlewareWebChromeBase mMiddleWareWebChrome;

    private int readMode = 0;
    NestedScrollAgentWebView webView;
    int FontColor;
    int BGColor;
    int fontSize = 18;
    String fontColor = "39867f";
    public String CSS_STYLE = "<style>* {font-size:" + fontSize + "px;color:#" + fontColor + ";} </style>";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);


        initData();
        initView();
        bindEvent();
    }


    private void initData() {
        //获取连接地址
        link = getIntent().getStringExtra("qzLink");
        fromTitle = getIntent().getStringExtra("qzTitle");
        readHost = getIntent().getStringExtra("qzReadHost");
        contentXpath = getIntent().getStringExtra("qzReadXpath");
        readCharset = getIntent().getStringExtra("qzCharset");
        content = getIntent().getStringExtra("qzContent");

        SharedPreferencesUtils.readmodeWrite(getApplicationContext(),0);
        //取出数据
        fontSize = SharedPreferencesUtils.fontSizeRead(getApplicationContext());
        FontColor = SharedPreferencesUtils.fontColorRead(getApplicationContext());
        BGColor = SharedPreferencesUtils.bgColorRead(getApplicationContext());
    }

    private void initView() {

        mToolbar.setTitle(fromTitle);
        //添加关闭浏览器界面按钮
        mToolbar.setNavigationIcon(R.drawable.ic_close_writer_24dp);
        setSupportActionBar(mToolbar);
        mToolbar.setOverflowIcon(getResources().getDrawable(ic_more_vert_black_24dp));



        loadWeb();



        //添加下拉背景  轻舟logo
        addBgChild(mAgentWeb.getWebCreator().getWebParentLayout());
        // AgentWeb 没有把WebView的功能全面覆盖 ，所以某些设置 AgentWeb 没有提供 ， 请从WebView方面入手设置。
        mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);


        fontColor = Integer.toHexString(FontColor);
        if (fontColor!=null) {
            fontColor = fontColor.substring(2);
        }
        getCSS_STYLE(fontSize,fontColor,BGColor);


    }

    private void loadWeb()
    {
        //滑动隐藏AppBarLayout
        webView = new NestedScrollAgentWebView(this);
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(-1, -1);
        lp.setBehavior(new AppBarLayout.ScrollingViewBehavior());
//        mAgentWeb = AgentWeb.with(this)//
//                .setAgentWebParent(main, 1, lp)//lp记得设置behavior属性
//                //.setAgentWebParent((LinearLayout) view, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
//                .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
//                .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
//                .setWebViewClient(mWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
//                .setWebChromeClient(mWebChromeClient) //WebChromeClient
//                .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
//                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
//                //.setAgentWebUIController(new UIController(getActivity())) //自定义UI  AgentWeb3.0.0 加入。
//                .setMainFrameErrorView(R.layout.agentweb_error_page, -1) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
//                .useMiddlewareWebChrome(getMiddlewareWebChrome()) //设置WebChromeClient中间件，支持多个WebChromeClient，AgentWeb 3.0.0 加入。
//                //.additionalHttpHeader(getUrl(), "cookie", "41bc7ddf04a26b91803f6b11817a5a1c")
//                .useMiddlewareWebClient(getMiddlewareWebClient()) //设置WebViewClient中间件，支持多个WebViewClient， AgentWeb 3.0.0 加入。
//                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
//                .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
//                .createAgentWeb()//创建AgentWeb。
//                .ready()//设置 WebSettings。
//                .go(link); //WebView载入该url地址的页面并显示。

        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(main, 1, lp)//lp记得设置behavior属性
                //.setAgentWebParent((LinearLayout) view, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
                .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setWebLayout(getWebLayout())
                .setPermissionInterceptor(mPermissionInterceptor)
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


    }

    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {

        /**
         * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
         * @param url
         * @param permissions
         * @param action
         * @return true 该Url对应页面请求权限进行拦截 ，false 表示不拦截。
         */
        @Override
        public boolean intercept(String url, String[] permissions, String action) {
            // Log.i(TAG, "mUrl:" + url + "  permission:" + mGson.toJson(permissions) + " action:" + action);
            return false;
        }
    };

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
                                return DownloadImpl.getInstance()
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

    private void bindEvent()
    {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void loadData(boolean goTop) {
        if (contentXpath != null) {

            if (!"".equals(contentXpath)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        content = ReadModeUtils.getContext(link, contentXpath, readCharset, readHost);
                        //点击的标题
                        content = content != null ? content.replace("<img/", "<img style=\"max-width:100%;height:auto") : null;


                        content = getNewContent(content);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (goTop) {
                                    String template = FileUtil.getFileContent(getApplicationContext(), "code.html");
                                    // 生成结果
                                    String html = template.replace("{{code}}", content);
                                    mAgentWeb.getWebCreator().getWebView().loadDataWithBaseURL("file:///android_asset/", CSS_STYLE + html, "text/html", "utf-8", null);
                                    mAgentWeb.getWebCreator().getWebView().getSettings().setTextZoom(120);
                                } else {
                                    mAgentWeb.getWebCreator().getWebView().loadDataWithBaseURL(null, CSS_STYLE + content, "text/html", "utf-8", null);

                                }
                            }
                        });

                    }
                }).start();
            }else if (content != null) {
                if (!"".equals(content)) {
                    Log.d("浏览器", "initView: " + content);
                    content = content != null ? content.replace("<img/", "<img style=\"max-width:100%;height:auto") : null;

                    content = getNewContent(content);
                    if (goTop) {
                        String template = FileUtil.getFileContent(getApplicationContext(), "code.html");
                        // 生成结果
                        String html = template.replace("{{code}}", content);
                        mAgentWeb.getWebCreator().getWebView().loadDataWithBaseURL("file:///android_asset/", CSS_STYLE + html, "text/html", "utf-8", null);

                    } else {
                        mAgentWeb.getWebCreator().getWebView().loadDataWithBaseURL(null, CSS_STYLE + content, "text/html", "utf-8", null);

                    }
                }
            }

        } else if (content != null) {
            if (!"".equals(content)) {
                Log.d("浏览器", "initView: " + content);
                content = content != null ? content.replace("<img/", "<img style=\"max-width:100%;height:auto") : null;

                content = getNewContent(content);
                if (goTop) {
                    String template = FileUtil.getFileContent(getApplicationContext(), "code.html");
                    // 生成结果
                    String html = template.replace("{{code}}", content);
                    mAgentWeb.getWebCreator().getWebView().loadDataWithBaseURL("file:///android_asset/", CSS_STYLE + html, "text/html", "utf-8", null);

                } else {
                    mAgentWeb.getWebCreator().getWebView().loadDataWithBaseURL(null, CSS_STYLE + content, "text/html", "utf-8", null);

                }
            }
        }
        mToolbar.setTitle(fromTitle);
        mAgentWeb.getWebCreator().getWebView().setBackgroundColor(BGColor);

    }

    /**
     * 将html文本内容中包含img标签的图片，宽度变为屏幕宽度，高度根据宽度比例自适应
     **/
    private static String getNewContent(String htmltext) {
        try {
            Document doc = Jsoup.parse(htmltext);
            Elements elements = doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("width", "100%").attr("height", "auto");
            }

            return doc.toString();
        } catch (Exception e) {
            return htmltext;
        }
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
        if (voidurl.equals(mAgentWeb.getWebCreator().getWebView().getUrl()) || voidurl2.equals(mAgentWeb.getWebCreator().getWebView().getUrl())) {
            super.onKeyDown(keyCode, event);

        } else {
            if (mAgentWeb.handleKeyEvent(keyCode, event)) {
                return true;
            }
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

            if (voidurl.equals(mAgentWeb.getWebCreator().getWebView().getUrl()) || voidurl2.equals(mAgentWeb.getWebCreator().getWebView().getUrl())) {

            } else {
                mToolbar.setTitle(title);
            }


        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.i("CommonWebChromeClient", "onProgressChanged:" + newProgress + "  view:" + view);
        }
    };

    private static final int MENU_CONFIRM = 18;
    MenuItem item;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webmenu, menu);
        item = menu.add(Menu.NONE, MENU_CONFIRM, 0, "阅读模式");
        //主要是这句话 显示图标
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(R.drawable.ic_chrome_reader_mode_black_24dp);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case MENU_CONFIRM:
                readMode = SharedPreferencesUtils.readmodeRead(getApplicationContext());
                if (readMode==1){
                    //退出阅读模式
                    SnackbarUtil.ShortSnackbar(main, "退出阅读模式", SnackbarUtil.Confirm).show();
                    mAgentWeb.getWebCreator().getWebView().loadUrl(link);
                    SharedPreferencesUtils.readmodeWrite(getApplicationContext(), 0);

                }else {
                    if (!contentXpath.isEmpty() || content != null) {

                        loadData(true);
                        SnackbarUtil.ShortSnackbar(main, "进入阅读模式", SnackbarUtil.Confirm).show();
                        SharedPreferencesUtils.readmodeWrite(getApplicationContext(), 1);
                    } else {
                        SnackbarUtil.ShortSnackbar(main, "该页面暂无阅读模式", SnackbarUtil.Confirm).show();

                    }
                }
                return true;
            case R.id.refresh:
                if (mAgentWeb != null) {
                    mAgentWeb.getUrlLoader().reload(); // 刷新
                }
                return true;

            case R.id.copy:
                if (mAgentWeb != null) {
                    //复制链接地址
                    String newurl = "";
                    if (voidurl.equals(mAgentWeb.getWebCreator().getWebView().getUrl()) || voidurl2.equals(mAgentWeb.getWebCreator().getWebView().getUrl())) {
                        newurl = link;
                    } else {
                        newurl = mAgentWeb.getWebCreator().getWebView().getUrl();
                    }
                    SmallUtils.getInstance().toCopy(this, newurl);
                    SnackbarUtil.ShortSnackbar(main,"已复制到剪贴板",SnackbarUtil.Confirm).show();

                }
                return true;
            case R.id.default_browser:
                if (mAgentWeb != null) {
                    //调用系统浏览器打开
                    String newurl = "";
                    if (voidurl.equals(mAgentWeb.getWebCreator().getWebView().getUrl()) || voidurl2.equals(mAgentWeb.getWebCreator().getWebView().getUrl())) {
                        newurl = link;
                    } else {
                        newurl = mAgentWeb.getWebCreator().getWebView().getUrl();
                    }
                    SmallUtils.getInstance().openBrowser(newurl, getApplicationContext());
                }
                return true;


            default:
                return false;
        }

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
        //mAgentWeb.destroy();
        //mAgentWeb.getWebLifeCycle().onDestroy();
        //移除全部粘性事件
        EventBus.getDefault().removeAllStickyEvents();
        //解绑事件
        EventBus.getDefault().unregister(this);
    }






    private void getCSS_STYLE(int fontSize, String fontColor,int BGColor) {
        String BGColorstr = Integer.toHexString(BGColor);
        if (BGColorstr!=null) {
            BGColorstr = BGColorstr.substring(2, BGColorstr.length());

            CSS_STYLE = "<style>* {font-size:" + fontSize + "px;} p{color:#" + fontColor + ";} body{background-color:#" + BGColorstr + ";}</style>";

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
