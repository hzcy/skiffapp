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
 * ???????????????
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
        //??????????????????
        link = getIntent().getStringExtra("qzLink");
        fromTitle = getIntent().getStringExtra("qzTitle");
        readHost = getIntent().getStringExtra("qzReadHost");
        contentXpath = getIntent().getStringExtra("qzReadXpath");
        readCharset = getIntent().getStringExtra("qzCharset");
        content = getIntent().getStringExtra("qzContent");

        SharedPreferencesUtils.readmodeWrite(getApplicationContext(),0);
        //????????????
        fontSize = SharedPreferencesUtils.fontSizeRead(getApplicationContext());
        FontColor = SharedPreferencesUtils.fontColorRead(getApplicationContext());
        BGColor = SharedPreferencesUtils.bgColorRead(getApplicationContext());
    }

    private void initView() {

        mToolbar.setTitle(fromTitle);
        //?????????????????????????????????
        mToolbar.setNavigationIcon(R.drawable.ic_close_writer_24dp);
        setSupportActionBar(mToolbar);
        mToolbar.setOverflowIcon(getResources().getDrawable(ic_more_vert_black_24dp));



        loadWeb();



        //??????????????????  ??????logo
        addBgChild(mAgentWeb.getWebCreator().getWebParentLayout());
        // AgentWeb ?????????WebView????????????????????? ????????????????????? AgentWeb ???????????? ??? ??????WebView?????????????????????
        mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);


        fontColor = Integer.toHexString(FontColor);
        if (fontColor!=null) {
            fontColor = fontColor.substring(2);
        }
        getCSS_STYLE(fontSize,fontColor,BGColor);


    }

    private void loadWeb()
    {
        //????????????AppBarLayout
        webView = new NestedScrollAgentWebView(this);
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(-1, -1);
        lp.setBehavior(new AppBarLayout.ScrollingViewBehavior());
//        mAgentWeb = AgentWeb.with(this)//
//                .setAgentWebParent(main, 1, lp)//lp????????????behavior??????
//                //.setAgentWebParent((LinearLayout) view, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//??????AgentWeb???????????????
//                .useDefaultIndicator(-1, 3)//?????????????????????????????????-1????????????????????????2????????????dp???
//                .setAgentWebWebSettings(getSettings())//?????? IAgentWebSettings???
//                .setWebViewClient(mWebViewClient)//WebViewClient ??? ??? WebView ???????????? ?????????????????????WebView??????setWebViewClient(xx)?????????,?????????AgentWeb DefaultWebClient,???????????????????????????????????????
//                .setWebChromeClient(mWebChromeClient) //WebChromeClient
//                .setPermissionInterceptor(mPermissionInterceptor) //???????????? 2.0.0 ?????????
//                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //???????????? Android 4.2.2 ??????????????????????????? ?????????AgentWebView????????????
//                //.setAgentWebUIController(new UIController(getActivity())) //?????????UI  AgentWeb3.0.0 ?????????
//                .setMainFrameErrorView(R.layout.agentweb_error_page, -1) //??????1?????????????????????????????????2??????????????????ID -1???????????????????????????????????? AgentWeb 3.0.0 ?????????
//                .useMiddlewareWebChrome(getMiddlewareWebChrome()) //??????WebChromeClient????????????????????????WebChromeClient???AgentWeb 3.0.0 ?????????
//                //.additionalHttpHeader(getUrl(), "cookie", "41bc7ddf04a26b91803f6b11817a5a1c")
//                .useMiddlewareWebClient(getMiddlewareWebClient()) //??????WebViewClient????????????????????????WebViewClient??? AgentWeb 3.0.0 ?????????
//                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//???????????????????????????????????????????????????????????? AgentWeb 3.0.0 ?????????
//                .interceptUnkownUrl() //??????????????????????????????Url AgentWeb 3.0.0 ?????????
//                .createAgentWeb()//??????AgentWeb???
//                .ready()//?????? WebSettings???
//                .go(link); //WebView?????????url???????????????????????????

        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(main, 1, lp)//lp????????????behavior??????
                //.setAgentWebParent((LinearLayout) view, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//??????AgentWeb???????????????
                .useDefaultIndicator(-1, 3)//?????????????????????????????????-1????????????????????????2????????????dp???
                .setAgentWebWebSettings(getSettings())//?????? IAgentWebSettings???
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setWebLayout(getWebLayout())
                .setPermissionInterceptor(mPermissionInterceptor)
                .useMiddlewareWebChrome(getMiddlewareWebChrome())
                .useMiddlewareWebClient(getMiddlewareWebClient())
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //???????????? Android 4.2.2 ??????????????????????????? ?????????AgentWebView????????????
                //.setAgentWebUIController(new UIController(getActivity())) //?????????UI  AgentWeb3.0.0 ?????????
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1) //??????1?????????????????????????????????2??????????????????ID -1???????????????????????????????????? AgentWeb 3.0.0 ?????????
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//???????????????????????????????????????????????????????????? AgentWeb 3.0.0 ?????????
                .setWebView(webView)
                .interceptUnkownUrl() //??????????????????????????????Url AgentWeb 3.0.0 ?????????
                .createAgentWeb()//??????AgentWeb???
                .ready()//?????? WebSettings???
                .go(link); //WebView?????????url???????????????????????????


    }

    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {

        /**
         * PermissionInterceptor ????????? url1 ??????????????? url2 ????????????????????????
         * @param url
         * @param permissions
         * @param action
         * @return true ???Url???????????????????????????????????? ???false ??????????????????
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
             * AgentWeb 4.0.0 ??????????????? DownloadListener ?????? ???????????????API ?????? Download ??????????????????????????????????????????
             * ????????????????????? AgentWeb Download ?????? ??? ???????????? compile 'com.download.library:Downloader:4.1.1' ???
             * ???????????????????????????????????????????????? AgentWebSetting ??? New ??? DefaultDownloadImpl
             * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? setDownloader ????????????????????????
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
                        //???????????????
                        content = content != null ? content.replace("<img/", "<img style=\"max-width:100%;height:auto") : null;


                        content = getNewContent(content);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (goTop) {
                                    String template = FileUtil.getFileContent(getApplicationContext(), "code.html");
                                    // ????????????
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
                    Log.d("?????????", "initView: " + content);
                    content = content != null ? content.replace("<img/", "<img style=\"max-width:100%;height:auto") : null;

                    content = getNewContent(content);
                    if (goTop) {
                        String template = FileUtil.getFileContent(getApplicationContext(), "code.html");
                        // ????????????
                        String html = template.replace("{{code}}", content);
                        mAgentWeb.getWebCreator().getWebView().loadDataWithBaseURL("file:///android_asset/", CSS_STYLE + html, "text/html", "utf-8", null);

                    } else {
                        mAgentWeb.getWebCreator().getWebView().loadDataWithBaseURL(null, CSS_STYLE + content, "text/html", "utf-8", null);

                    }
                }
            }

        } else if (content != null) {
            if (!"".equals(content)) {
                Log.d("?????????", "initView: " + content);
                content = content != null ? content.replace("<img/", "<img style=\"max-width:100%;height:auto") : null;

                content = getNewContent(content);
                if (goTop) {
                    String template = FileUtil.getFileContent(getApplicationContext(), "code.html");
                    // ????????????
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
     * ???html?????????????????????img??????????????????????????????????????????????????????????????????????????????
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

    //??????????????????
    private void addBgChild(FrameLayout frameLayout) {

        ImageView mImageView = new ImageView(frameLayout.getContext());
        mImageView.setImageResource(R.mipmap.skifflogo_round);

        TextView mTextView = new TextView(frameLayout.getContext());
        mTextView.setText("??????");
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
        item = menu.add(Menu.NONE, MENU_CONFIRM, 0, "????????????");
        //?????????????????? ????????????
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
                    //??????????????????
                    SnackbarUtil.ShortSnackbar(main, "??????????????????", SnackbarUtil.Confirm).show();
                    mAgentWeb.getWebCreator().getWebView().loadUrl(link);
                    SharedPreferencesUtils.readmodeWrite(getApplicationContext(), 0);

                }else {
                    if (!contentXpath.isEmpty() || content != null) {

                        loadData(true);
                        SnackbarUtil.ShortSnackbar(main, "??????????????????", SnackbarUtil.Confirm).show();
                        SharedPreferencesUtils.readmodeWrite(getApplicationContext(), 1);
                    } else {
                        SnackbarUtil.ShortSnackbar(main, "???????????????????????????", SnackbarUtil.Confirm).show();

                    }
                }
                return true;
            case R.id.refresh:
                if (mAgentWeb != null) {
                    mAgentWeb.getUrlLoader().reload(); // ??????
                }
                return true;

            case R.id.copy:
                if (mAgentWeb != null) {
                    //??????????????????
                    String newurl = "";
                    if (voidurl.equals(mAgentWeb.getWebCreator().getWebView().getUrl()) || voidurl2.equals(mAgentWeb.getWebCreator().getWebView().getUrl())) {
                        newurl = link;
                    } else {
                        newurl = mAgentWeb.getWebCreator().getWebView().getUrl();
                    }
                    SmallUtils.getInstance().toCopy(this, newurl);
                    SnackbarUtil.ShortSnackbar(main,"?????????????????????",SnackbarUtil.Confirm).show();

                }
                return true;
            case R.id.default_browser:
                if (mAgentWeb != null) {
                    //???????????????????????????
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
        //????????????????????????
        EventBus.getDefault().removeAllStickyEvents();
        //????????????
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
     * MiddlewareWebClientBase ??? AgentWeb 3.0.0 ??????????????????????????????
     * ???????????????????????? AgentWeb ?????????????????? ???????????? WebClientView???
     * ?????????AgentWeb???????????????????????? MiddlewareWebClientBase ?????????
     * ??????????????? ???
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
				/*if (url.startsWith("agentweb")) { // ?????? url???????????? DefaultWebClient#shouldOverrideUrlLoading
					Log.i(TAG, "agentweb scheme ~");
					return true;
				}*/

                if (super.shouldOverrideUrlLoading(view, url)) { // ?????? DefaultWebClient#shouldOverrideUrlLoading
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
