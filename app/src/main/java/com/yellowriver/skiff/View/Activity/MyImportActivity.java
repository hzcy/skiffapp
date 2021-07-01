package com.yellowriver.skiff.View.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.yellowriver.skiff.Adapter.ViewPageAdapter.FragmentAdapter;
import com.yellowriver.skiff.Help.BuyerLiveGoodsDialog;
import com.yellowriver.skiff.Help.LogUtil;
import com.yellowriver.skiff.Help.SupperViewPager;
import com.yellowriver.skiff.R;
import com.yellowriver.skiff.View.Fragment.ListSaveFragment;
import com.yellowriver.skiff.View.Fragment.NavMyImpFragment;

import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.exception.XpathSyntaxErrorException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import skin.support.design.widget.SkinMaterialFloatingActionButton;
import skin.support.flycotablayout.widget.SkinSlidingTabLayout;

import static com.yellowriver.skiff.R.drawable.ic_gps_fixed_black_24dp;
import static com.yellowriver.skiff.R.drawable.ic_gps_off_black_24dp;

public class MyImportActivity extends AppCompatActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.fab)
    SkinMaterialFloatingActionButton fab;
    @BindView(R.id.naView1)
    NavigationView naView1;
    @BindView(R.id.main)
    CoordinatorLayout main;
    @BindView(R.id.bb)
    TextView bb;
    @BindView(R.id.myimporttablayout)
    SkinSlidingTabLayout myimporttablayout;
    @BindView(R.id.myimportview_pager)
    SupperViewPager myimportviewPager;
    private Toolbar toolbar;
    private TextView loggr;
    private EditText jsinput;
    private ListView netw;
    private NavigationView xhrslide, netlogslide;
    private List<String> netdata = new ArrayList<String>();
    private WebView web;
    private ArrayAdapter<String> adapter;
    private SearchView urlView;
    private ImageView urldw;
    private DrawerLayout drawr;
    private MenuItem clearmenu;
    private View homs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_import);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //添加关闭浏览器界面按钮
        toolbar.setNavigationIcon(R.drawable.ic_close_writer_24dp);

        homs = findViewById(R.id.homs);
        drawr = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);

        web = (WebView) findViewById(R.id.webv);
        fab.setVisibility(View.INVISIBLE);
        webinit();

        navinit();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    FragmentAdapter contentAdapter;
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;

    private void navinit() {
        tabIndicators = new ArrayList<>();
        tabIndicators.add("第一步");
        tabIndicators.add("第二步");
        tabIndicators.add("第三步");
        tabFragments = new ArrayList<>();
        for (String title : tabIndicators) {
            //第一步
            tabFragments.add(NavMyImpFragment.getInstance(title));
        }
        try {
            contentAdapter = new FragmentAdapter(getSupportFragmentManager(), tabIndicators, tabFragments);

        }catch (IllegalStateException e){
            e.printStackTrace();

        }
        // tabLayout.setLayoutMode(View);
        //BuyerLiveGoodsPageAdapter pageAdapter = new BuyerLiveGoodsPageAdapter(getChildFragmentManager());
        myimportviewPager.setAdapter(contentAdapter);
        myimporttablayout.setViewPager(myimportviewPager);

    }
    private void webinit() {
        // atur webview agar support js
        web.getSettings().setJavaScriptEnabled(true);
        // agar bisa di inspect melalui chrome di pc
        web.setWebContentsDebuggingEnabled(true);
        // register fungsi javascript
        web.addJavascriptInterface(new MyJavaScriptInterface(), "$$");
        // navigation callback
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                setTitle("加载中...");
                super.onPageStarted(view, url, favicon);
            }

            // saat halaman selesai di load, inject js
            @Override
            public void onPageFinished(WebView view, String url) {
                setTitle(view.getTitle());

                String jsID = "function readXPathID(element) {\n" +
                        "    if(element.tagName !== \"BODY\"){" +
                        "    if (element.id !== \"\") {" +
                        "        return '//*[@id=\\\"' + element.id + '\\\"]';\n" +
                        "    }\n" +
                        "    " +
                        "    if (element == document.body) {" +
                        "        return '/html/' + element.tagName.toLowerCase();\n" +
                        "    }\n" +
                        "    var ix = 1," +
                        "         siblings = element.parentNode.childNodes;" +
                        "\n" +
                        "    for (var i = 0, l = siblings.length; i < l; i++) {\n" +
                        "        var sibling = siblings[i];\n" +
                        "       " +
                        "        if (sibling == element) {\n" +
                        "            return arguments.callee(element.parentNode) + '/' + element.tagName.toLowerCase() ;\n" +
                        "            " +
                        "        } else if (sibling.nodeType == 1 && sibling.tagName == element.tagName) {\n" +
                        "            ix++;\n" +
                        "        }\n" +
                        "    }\n" +
                        "    }\n" +
                        "};";


                String jsCLASS = "function readXPathCLASS(element) {\n" +
                        "    if(element.tagName !== \"BODY\"){" +
                        "    if (element.className !== \"\") {" +
                        "        return '//*[@class=\\\"' + element.className + '\\\"]';\n" +
                        "    }\n" +
                        "    " +
                        "    if (element == document.body) {" +
                        "        return '/html/' + element.tagName.toLowerCase();\n" +
                        "    }\n" +
                        "    var ix = 1," +
                        "         siblings = element.parentNode.childNodes;" +
                        "\n" +
                        "    for (var i = 0, l = siblings.length; i < l; i++) {\n" +
                        "        var sibling = siblings[i];\n" +
                        "       " +
                        "        if (sibling == element) {\n" +
                        "            return arguments.callee(element.parentNode) + '/' + element.tagName.toLowerCase() ;\n" +
                        "            " +
                        "        } else if (sibling.nodeType == 1 && sibling.tagName == element.tagName) {\n" +
                        "            ix++;\n" +
                        "        }\n" +
                        "    }\n" +
                        "    }\n" +
                        "};";


                String jsgetLink = "function getLink(element) {\n" +
                        "\n" +
                        "    if (element.tagName.toLowerCase() === \"body\") {\n" +
                        "        return element;\n" +
                        "    }else {\n" +
                        "        if(element.tagName.toLowerCase() == \"a\"){\n" +
                        "            return element;\n" +
                        "        }else {\n" +
                        "            return getLink(element.parentElement);\n" +
                        "        }\n" +
                        "    }\n" +
                        "};";

                String jsgetLink2 = "function getLink2(element) {\n" +
                        "\n" +
                        "    if (element.tagName.toLowerCase() === \"body\") {\n" +
                        "        return element;\n" +
                        "    }else {\n" +
                        "        if(element.tagName.toLowerCase() == \"a\"){\n" +
                        "            return element.attributes[\"href\"].value;\n" +
                        "        }else {\n" +
                        "            return getLink2(element.parentElement);\n" +
                        "        }\n" +
                        "    }\n" +
                        "};";

                String jsgetImg = "function getImg(element) {\n" +
                        "\n" +
                        "    var imgs = element.getElementsByTagName(\"img\");\n" +
                        "\n" +
                        "    if(imgs.length>0){\n" +
                        "        return imgs[0];\n" +
                        "    }else {\n" +
                        "        if (element.tagName.toLowerCase() === \"body\") {\n" +
                        "            return element;\n" +
                        "        }else {\n" +
                        "            if(element.tagName.toLowerCase() == \"img\"){\n" +
                        "                return element;\n" +
                        "            }else {\n" +
                        "                return getImg(element.parentElement);\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n" +
                        "\n" +
                        "    \n" +
                        "};";

                // String js5 = "var tt = $(t.tagName).parents();";
                String jsCode = loadJs("real_library/jquery-3.1.1.min.js");
                jsCode += "; ";
//                String js8 = "var xpath1 = readXPathID(t);";
//                String js9 = "var xpath2 = readXPathCLASS(t);";
                String js6 = "var html = document.getElementsByTagName('html')[0].innerHTML;";

//                String js7 = "var result2 = document.selectNodes(xpath2);";
                //String js3 = "$(t).css(\"box-shadow\",\"1px 1px 1px 1px #95B8E7\");";
                String js3 = "$(t).css(\"border\",\"2px solid #95B8E7\");";
                view.loadUrl("javascript:" + jsCode);
                view.loadUrl("javascript:function injek3(){window.hasdir=1;window.dir=function(n){var r=[];for(var t in n)'function'==typeof n[t]&&r.push(t);return r}};if(window.hasdir!=1){injek3();}");
                view.loadUrl("javascript:" + jsID + jsCLASS + jsgetLink + jsgetLink2 + jsgetImg + "function injek2(){window.touchblock=0,window.dummy1=1,document.addEventListener('click',function(n){if(1==window.touchblock){n.preventDefault();n.stopPropagation();var t=document.elementFromPoint(n.clientX,n.clientY);" + js3 + js6 + "window.ganti=function(n){t.outerHTML=n},window.gantiparent=function(n){t.parentElement.outerHTML=n},$$.print(html,t.textContent,getLink2(t), readXPathCLASS(getLink(t)),readXPathID(getLink(t)),readXPathCLASS(getImg(t)),readXPathID(getImg(t)), readXPathCLASS(t), readXPathID(t))}},!0)}1!=window.dummy1&&injek2();");
                view.loadUrl("javascript:function injek(){window.hasovrde=1;var e=XMLHttpRequest.prototype.open;XMLHttpRequest.prototype.open=function(ee,nn,aa){this.addEventListener('load',function(){$$.log(this.responseText, nn, JSON.stringify(arguments))}),e.apply(this,arguments)}};if(window.hasovrde!=1){injek();}");
                super.onPageFinished(view, url);
            }
        });

    }


    @Override
    public void onBackPressed() {
        // browser bisa di back, lakukan back. jika tidak maka lakukan back pada aplikasi (keluar)
        if (web.canGoBack()) {
            web.goBack();
        } else {
            super.onBackPressed();
        }
    }

    MenuItem urlmenudw;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_myimport, menu);
        // menu url input
        final MenuItem urlmenu = menu.findItem(R.id.goto_url);

        urlView = (SearchView) urlmenu.getActionView();

        if (urlView != null) {
            //改变默认的搜索图标

            //urlView.setIconifiedByDefault(false);

            SearchView.SearchAutoComplete textView = (SearchView.SearchAutoComplete) urlView.findViewById(R.id.search_src_text);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setHintTextColor(getResources().getColor(R.color.white));


            View view = urlView.findViewById(R.id.search_plate);
            view.setBackgroundColor(Color.TRANSPARENT);


            ImageView closeViewIcon = (ImageView) urlView.findViewById(R.id.search_close_btn);
            closeViewIcon.setImageDrawable(ContextCompat
                    .getDrawable(this, R.drawable.ic_close_writer_24dp));


        }
        //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
        urlView.setQueryHint("输入网址");
        // saat icon > (right chevron) di klik maka set url di url input sesuai url dari webview
        urlView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                urlView.setQuery(web.getUrl(), false);
            }
        });
        urlView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("WrongConstant")
            @Override
            // saat teken enter di input url
            public boolean onQueryTextSubmit(String urlinput) {
                // ada validasi url menggunakan regex, agar teks yang di input benar-benar url yang valid
                if (!urlinput.trim().matches("https?://.*")) {
                    urlinput = "http://" + urlinput.trim();
                }
                if (Patterns.WEB_URL.matcher(urlinput).matches()) {
                    web.loadUrl(urlinput);
                    MenuItemCompat.collapseActionView(urlmenu);
                    homs.setVisibility(View.GONE);
                    web.setVisibility(View.VISIBLE);
                } else Toast.makeText(MyImportActivity.this, "网址不正确", 0).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String p1) {
                return false;
            }
        });

        urlmenudw = menu.findItem(R.id.menu_touchinspect);


        return super.onCreateOptionsMenu(menu);
    }

    // menu click handler
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_touchinspect:
                // saat menu Touch Inscpector di klik, maka inject js yang sudah diatur
                web.loadUrl("javascript:window.touchblock=!window.touchblock;setTimeout(function(){$$.blocktoggle(window.touchblock)}, 100);");
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    // dialog source code view/edit
    private void showSourceDialog(final String html, final String text, final String link, final String classlink, final String idlink, final String classimg, final String idimg, final String classxpath, String idxpath) {
        View v = getLayoutInflater().inflate(R.layout.source, null);

        LogUtil.info("轻舟自动获取xpath", "xpath:CLASS" + classxpath + "-----ID:" + idxpath);
        int classsize = getXpathSize(html, classxpath);
        int idsize = getXpathSize(html, idxpath);
        LogUtil.info("轻舟自动获取xpath", "xpath:CLASS的大小" + classsize + "-----ID的大小:" + idsize);


        LogUtil.info("轻舟自动获取xpath", "xpath:classimg" + classimg + "dfd" + idimg);
        final TextView tvType = (TextView) v.findViewById(R.id.textviewtype);
        final EditText ed = (EditText) v.findViewById(R.id.sourceEditText1);
        final TextView tv = (TextView) v.findViewById(R.id.valuttv);
        final TextView sizetv = (TextView) v.findViewById(R.id.valutsizetv);

        final TextView jianxiao = (TextView) v.findViewById(R.id.jianxiao);
        final TextView zengjia = (TextView) v.findViewById(R.id.zengjia);

        String finalxpath = "";
        String finalLinkxpath = "";
        String finalImgxpath = "";
        int size = 0;
        if (classsize >= idsize) {
            finalxpath = classxpath + "/text()";
            if (classlink.equals("undefined")) {
                finalLinkxpath = "当前点击下没有链接，点击其他位置试试";
            } else {
                finalLinkxpath = classlink + "/@href";
            }
            if (classimg.equals("undefined")) {
                finalImgxpath = "当前点击下没有图片，点击其他位置试试";
            } else {
                finalImgxpath = classimg + "";
            }
            size = classsize;
        } else {
            if (idsize == classsize * 2) {
                finalxpath = classxpath + "/text()";
                if (classlink.equals("undefined")) {
                    finalLinkxpath = "当前点击下没有链接，点击其他位置试试";
                } else {
                    finalLinkxpath = classlink + "/@href";
                }
                if (classimg.equals("undefined")) {
                    finalImgxpath = "当前点击下没有图片，点击其他位置试试";
                } else {
                    finalImgxpath = classimg + "";
                }
                size = classsize;
            } else {

                finalxpath = idxpath + "/text()";
                ;
                if (idlink.equals("undefined")) {
                    finalLinkxpath = "当前点击下没有链接，点击其他位置试试";
                } else {
                    finalLinkxpath = idlink + "/@href";
                }
                if (idimg.equals("undefined")) {
                    finalImgxpath = "当前点击下没有图片，点击其他位置试试";
                } else {
                    finalImgxpath = idimg + "";
                }
                size = idsize;

            }
        }

        ed.setText(finalxpath);

        tvType.setText("文本类型:");

        if (text.equals("")) {
            sizetv.setText("没有获取到文本");
        } else {
            tv.setText("值:" + text);
            if (size > 1) {
                sizetv.setText("相同元素共有" + size + "个，推荐使用");
            } else {
                sizetv.setText("相同元素共有" + size + "个，不推荐使用");
            }
        }

        ed.setTag(false);
        AlertDialog.Builder dl = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER);
        // title.setBackgroundResource(R.drawable.list);
        title.setText("");
        dl.setCustomTitle(title);
        // dl.setTitle("定位元素");
        dl.setView(v);
        String finalXpath = finalxpath;
        dl.setPositiveButton("保存为", null);
        dl.setNeutralButton("检查图片", null);
        dl.setNegativeButton("检查链接", null);
        AlertDialog dlg = dl.show();

        //图片按钮
        final Button prntBtn = dlg.getButton(AlertDialog.BUTTON_NEUTRAL);
        //链接按钮
        final Button prntBtn2 = dlg.getButton(AlertDialog.BUTTON_NEGATIVE);
        //保存按钮
        final Button prntBtn3 = dlg.getButton(AlertDialog.BUTTON_POSITIVE);
        prntBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv.getText().equals("")) {

                    Toast.makeText(MyImportActivity.this, "没有获取到值，请重新选择", Toast.LENGTH_SHORT).show();
                    //LogUtil.info("轻舟自动获取xpath", "没有获取到值，请重新选择");
                    return;
                } else {
                    String xpath = ed.getText().toString();
                    if ((boolean) prntBtn2.getTag()) {
                        //LogUtil.info("轻舟自动获取xpath", "保存链接或下一页" + ed.getText());
                        showBottomSheetDialogSaveLink(xpath, dlg);
                    } else if ((boolean) prntBtn.getTag()) {
                        //LogUtil.info("轻舟自动获取xpath", "保存图片" + ed.getText());
                        showBottomSheetDialogSaveImg(xpath, dlg);
                    } else {
                        //LogUtil.info("轻舟自动获取xpath", "保存标题简介或日期" + ed.getText());
                        showBottomSheetDialogSaveText2(xpath, dlg);
                    }

                }
            }
        });

        prntBtn.setTag(false);
        prntBtn2.setTag(false);
        String finalImgxpath1 = finalImgxpath;
        prntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                tvType.setText("图片类型:");
                ed.setTag(true);
                prntBtn.setTag(true);
                prntBtn2.setTag(false);
                if (finalImgxpath1.equals("当前点击下没有图片，点击其他位置试试")) {
                    ed.setText(finalImgxpath1);
                    tv.setText("值为空");
                    sizetv.setText("");
                } else {
                    String[] result = getXpathImg(html, finalImgxpath1);
                    int size = getXpathSize(html, result[1]);
                    if (size > 1) {
                        sizetv.setText("相同元素共有" + size + "个，推荐使用");
                    } else {
                        sizetv.setText("相同元素共有" + size + "个，不推荐使用");
                    }

                    ed.setText(result[1]);
                    tv.setText("值:" + result[0]);
                }

            }
        });

        String finalLinkxpath1 = finalLinkxpath;
        prntBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                tvType.setText("链接类型:");
                ed.setTag(true);
                prntBtn2.setTag(true);
                prntBtn.setTag(false);
                if (finalLinkxpath1.equals("当前点击下没有链接，点击其他位置试试")) {
                    ed.setText(finalLinkxpath1);
                    tv.setText("值为空");
                    sizetv.setText("");
                } else {
                    ed.setText(finalLinkxpath1);
                    int size = getXpathSize(html, finalLinkxpath1);
                    if (size > 1) {
                        sizetv.setText("相同元素共有" + size + "个，推荐使用");
                    } else {
                        sizetv.setText("相同元素共有" + size + "个，不推荐使用");
                    }
                    tv.setText("值:" + link);

                }

            }
        });

        //使用较小的xpath
        jianxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = 0;
                String newxpath = "";
                String newtext = "";
                if (classsize >= idsize) {
                    if ((boolean) prntBtn.getTag()) {
                        //检查图片
                        if (idimg.equals("undefined")) {
                            newxpath = "当前点击下没有图片，点击其他位置试试";
                            size = 0;
                        } else {
                            String[] result = getXpathImg(html, idimg);
                            size = getXpathSize(html, result[1]);
                            newxpath = result[1];
                            newtext = result[0];
                            ;
                        }
                    } else if ((boolean) prntBtn2.getTag()) {
                        //检查链接
                        if (idlink.equals("undefined")) {
                            newxpath = "当前点击下没有链接，点击其他位置试试";
                            size = 0;
                        } else {
                            newxpath = idlink + "/@href";
                            size = getXpathSize(html, idlink);
                            newtext = link;
                        }

                    } else {
                        //默认text
                        size = getXpathSize(html, idxpath);
                        newxpath = idxpath + "/text()";
                        newtext = text;
                    }

                } else {
                    if ((boolean) prntBtn.getTag()) {
                        //检查图片
                        if (classimg.equals("undefined")) {
                            newxpath = "当前点击下没有图片，点击其他位置试试";
                            size = 0;
                        } else {
                            String[] result = getXpathImg(html, classimg);
                            size = getXpathSize(html, result[1]);
                            newxpath = result[1];
                            newtext = result[0];
                            ;
                        }
                    } else if ((boolean) prntBtn2.getTag()) {
                        //检查链接
                        if (classlink.equals("undefined")) {
                            newxpath = "当前点击下没有链接，点击其他位置试试";
                            size = 0;
                        } else {
                            newxpath = classlink + "/@href";
                            size = getXpathSize(html, classlink);
                            newtext = link;
                        }

                    } else {
                        //默认text
                        size = getXpathSize(html, classxpath);
                        newxpath = classxpath + "/text()";
                        newtext = text;
                    }
                }

                ed.setText(newxpath);
                if (newxpath.indexOf("点击其他位置试试") != -1) {
                    tv.setText("值为空");
                    sizetv.setText("");
                } else {
                    if (newtext.equals("")) {
                        sizetv.setText("没有获取到文本");
                    } else {
                        tv.setText("值:" + newtext);
                        if (size > 1) {
                            sizetv.setText("相同元素共有" + size + "个，推荐使用");
                        } else {
                            sizetv.setText("相同元素共有" + size + "个，不推荐使用");
                        }
                    }
                }
            }
        });

        //使用较大的xpath
        zengjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = 0;
                String newxpath = "";
                String newtext = "";
                if (classsize >= idsize) {
                    if ((boolean) prntBtn.getTag()) {
                        //检查图片
                        if (classimg.equals("undefined")) {
                            newxpath = "当前点击下没有图片，点击其他位置试试";
                            size = 0;
                        } else {
                            String[] result = getXpathImg(html, classimg);
                            size = getXpathSize(html, result[1]);
                            newxpath = result[1];
                            newtext = result[0];
                            ;
                        }
                    } else if ((boolean) prntBtn2.getTag()) {
                        //检查链接
                        if (classlink.equals("undefined")) {
                            newxpath = "当前点击下没有链接，点击其他位置试试";
                            size = 0;
                        } else {
                            newxpath = classlink + "/@href";
                            size = getXpathSize(html, classlink);
                            newtext = link;
                        }

                    } else {
                        //默认text
                        size = getXpathSize(html, classxpath);
                        newxpath = classxpath + "/text()";
                        newtext = text;
                    }

                } else {
                    if ((boolean) prntBtn.getTag()) {
                        //检查图片
                        if (idimg.equals("undefined")) {
                            newxpath = "当前点击下没有图片，点击其他位置试试";
                            size = 0;
                        } else {
                            String[] result = getXpathImg(html, idimg);
                            size = getXpathSize(html, result[1]);
                            newxpath = result[1];
                            newtext = result[0];
                            ;
                        }
                    } else if ((boolean) prntBtn2.getTag()) {
                        //检查链接
                        if (idlink.equals("undefined")) {
                            newxpath = "当前点击下没有链接，点击其他位置试试";
                            size = 0;
                        } else {
                            newxpath = idlink + "/@href";
                            size = getXpathSize(html, idlink);
                            newtext = link;
                        }

                    } else {
                        //默认text
                        size = getXpathSize(html, idxpath);
                        newxpath = idxpath + "/text()";
                        newtext = text;
                    }
                }

                ed.setText(newxpath);
                if (newxpath.indexOf("点击其他位置试试") != -1) {
                    tv.setText("值为空");
                    sizetv.setText("");
                } else {
                    if (newtext.equals("")) {
                        sizetv.setText("没有获取到文本");
                    } else {
                        tv.setText("值:" + newtext);
                        if (size > 1) {
                            sizetv.setText("相同元素共有" + size + "个，推荐使用");
                        } else {
                            sizetv.setText("相同元素共有" + size + "个，不推荐使用");
                        }
                    }
                }
            }
        });
    }

    @OnClick({R.id.fab, R.id.bb})
    public void onViewClicked(View view) {
        //drawerLayout.openDrawer();
        switch (view.getId()) {
            case R.id.fab:
                drawerLayout.openDrawer(naView1);
                break;
            case R.id.bb:
                web.loadUrl("http://www.baidu.com");
                homs.setVisibility(View.GONE);
                web.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }


    // js interface
    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void log(final String content, final String url, final String arg) {
            web.post(new Runnable() {
                @Override
                public void run() {
                    //loggr.append(String.format("REQ: %s\nARG: %s\nRESP: %s\n--------------------\n",url,arg, content));
                }
            });
        }

        @JavascriptInterface
        @SuppressWarnings("unused")
        public void print(final String html, final String text, final String link, final String classlink, final String idlink, final String classimg, final String idimg, final String classxpath, final String idxpath) {
            web.post(new Runnable() {
                @Override
                public void run() {
                    showSourceDialog(html, text, link, classlink, idlink, classimg, idimg, classxpath, idxpath);
                }
            });
        }

        @JavascriptInterface
        @SuppressWarnings("unused")
        public void blocktoggle(final String val) {
            web.post(new Runnable() {
                @SuppressLint("WrongConstant")
                @Override
                public void run() {
                    if (val.matches("(1|true)")) {

                        urlmenudw.setIcon(ic_gps_fixed_black_24dp);
                        fab.setVisibility(View.VISIBLE);
                    } else {
                        urlmenudw.setIcon(ic_gps_off_black_24dp);
                        fab.setVisibility(View.INVISIBLE);
                    }

                    Toast.makeText(MyImportActivity.this, val.matches("(1|true)") ? "定位元素开启" : "定位元素关闭", 1).show();
                }
            });

        }
    }

    private Scanner scanner;

    private String loadJs(String fileName) {
        try {
            return ReadFile(fileName);
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    protected String ReadFile(String fileName) throws IOException {
        final AssetManager am = getAssets();
        final InputStream inputStream = am.open(fileName);

        scanner = new Scanner(inputStream, "UTF-8");
        return scanner.useDelimiter("\\A").next();
    }

    public static int getXpathSize(String html, String xpath) {
        int resultsize = 0;
        // String result = "";
        JXDocument jxDocument = JXDocument.create(html);
        List<Object> results = null;
        try {
            results = jxDocument.sel(xpath);
        } catch (XpathSyntaxErrorException e) {

        }
        if (results != null) {
            resultsize = results.size();
        }
        return resultsize;
    }

    public static String getXpathImg2(String html, String xpath) {
        String result = "";
        JXDocument jxDocument = JXDocument.create(html);
        List<Object> results = null;

        try {
            results = jxDocument.sel(xpath);
            if (results != null) {

                if (results.size() != 0) {
                    result = results.get(0).toString();
                } else {
                }
            } else {

            }
        } catch (XpathSyntaxErrorException e) {

        }
        return result;
    }

    public static String[] getXpathImg(String html, String xpath) {
        //int resultsize = 0;
        String result = "";

        String resultxpath = "";
        String yuanxpath = xpath;
        xpath = yuanxpath + "/@data-original";
        LogUtil.info("轻舟自动获取图片", "加载图片@data-original");
        result = getXpathImg2(html, xpath);
        if (result.equals("")) {
            xpath = yuanxpath + "/@data-src";
            LogUtil.info("轻舟自动获取图片", "加载图片@data-src");
            result = getXpathImg2(html, xpath);
        }
        if (result.equals("")) {
            xpath = yuanxpath + "/@src";
            LogUtil.info("轻舟自动获取图片", "加载图片@src");
            result = getXpathImg2(html, xpath);
        }
        if (result.equals("")) {
            xpath = yuanxpath + "/@_src";
            LogUtil.info("轻舟自动获取图片", "加载图片@_src");
            result = getXpathImg2(html, xpath);
        }
        if (result.equals("")) {
            xpath = yuanxpath + "/@_Src";
            LogUtil.info("轻舟自动获取图片", "加载图片@_Src");
            result = getXpathImg2(html, xpath);
        }
        String[] results = {result, xpath};
        return results;
    }


    //保存为链接类型  链接 下一页
    private void showBottomSheetDialogSaveLink(String xpath, AlertDialog dlg) {
        showDialog(getSupportFragmentManager(), "链接", xpath, dlg);
    }

    //保存为图片类型 图片
    private void showBottomSheetDialogSaveImg(String xpath, AlertDialog dlg) {
        showDialog(getSupportFragmentManager(), "图片", xpath, dlg);
    }

    //保存为文本类型 标题 简介 日期
    private void showBottomSheetDialogSaveText2(String xpath, AlertDialog dlg) {
        showDialog(getSupportFragmentManager(), "文本", xpath, dlg);
    }

    public void showDialog(FragmentManager fragmentManager, String type, String xpath, AlertDialog dlg) {
        BuyerLiveGoodsDialog dialog = new BuyerLiveGoodsDialog(type, xpath, dlg);
        dialog.show(fragmentManager, "savetag");
    }


}