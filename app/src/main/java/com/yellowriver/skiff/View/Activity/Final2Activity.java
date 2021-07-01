package com.yellowriver.skiff.View.Activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gargoylesoftware.htmlunit.html.xpath.XPathUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.yellowriver.skiff.Bean.DataBaseBean.FavoriteEntity;
import com.yellowriver.skiff.Bean.HomeBean.NowRuleBean;
import com.yellowriver.skiff.DataUtils.LocalUtils.SharedPreferencesUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.AnalysisUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.NetUtils;
import com.yellowriver.skiff.Help.LogUtil;
import com.yellowriver.skiff.Help.SnackbarUtil;
import com.yellowriver.skiff.Model.SQLModel;
import com.yellowriver.skiff.R;
import com.yellowriver.skiff.View.Fragment.Home.HomeDataViewFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.seimicrawler.xpath.JXDocument;

import java.util.InputMismatchException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import skin.support.design.widget.SkinMaterialAppBarLayout;
import skin.support.design.widget.SkinMaterialCollapsingToolbarLayout;

public class Final2Activity extends AppCompatActivity {


    @BindView(R.id.toolbar_layout)
    SkinMaterialCollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    SkinMaterialAppBarLayout appBar;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_summary)
    TextView tvSummary;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.tv_toolbartitle)
    TextView tvToolbartitle;
    @BindView(R.id.imgview)
    LinearLayout imgview;
    @BindView(R.id.imageview2)
    LinearLayout imageview2;

    /**
     * 源分组
     */
    private String qzGroupName;
    /**
     * 标题
     */
    private String qzTitle;
    /**
     * 源名称
     */
    private String qzSourceName;
    /**
     * 链接
     */
    private String qzLink;
    private String qzStep;
    /**
     * 首页还是搜索
     */
    private String qzSoucesType;
    /**
     * 搜索关键字
     */
    private String qzQuery;
    /**
     * 当前所在分组下标
     */
    private int qzindex;

    //获取图片和简介的json
    private String finalSummary;
    private String qzCharset;
    /**
     * 用于收藏
     */
    private String qzCover;
    private String qzSummary;
    private String qzDate;
    private int readIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final2);
        ButterKnife.bind(this);
        bindData();
        bindView();


    }

    private void bindData() {
        //源分组
        qzGroupName = getIntent().getStringExtra("qzGroupName");
        //标题
        qzTitle = getIntent().getStringExtra("qzTitle");
        //源名称
        qzSourceName = getIntent().getStringExtra("qzSourceName");
        //链接
        qzLink = getIntent().getStringExtra("qzLink");
        qzStep = getIntent().getStringExtra("qzStep");
        //搜索还是首页
        qzSoucesType = getIntent().getStringExtra("qzSoucesType");
        //搜索关键字  第二步一般不需要
        qzQuery = getIntent().getStringExtra("qzQuery");
        //当前所在分组下标
        qzindex = getIntent().getIntExtra("qzindex", 0);
        qzCover = getIntent().getStringExtra("qzCover");
        qzSummary = getIntent().getStringExtra("qzSummary");
        qzDate = getIntent().getStringExtra("qzDate");
        finalSummary = getIntent().getStringExtra("qzFinalSummary");
        qzCharset =  getIntent().getStringExtra("qzCharset");
        readIndex = getIntent().getIntExtra("readIndex", 0);
    }

    @SuppressLint("ResourceAsColor")
    private void bindView() {
        ButterKnife.bind(this);

        //标题
        //toolbar.setTitle(qzTitle);
        //toolbar.setTitleTextColor(Color.YELLOW);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert_black_24dp));
        //这样共用一个fragment 和同一个activity
        switch (qzStep) {

            case "1":
                showFragment(HomeDataViewFragment.getInstance(qzGroupName, qzSourceName, "2", qzLink, qzSoucesType, qzQuery, qzindex, qzTitle, readIndex));
                break;
            case "2":
                showFragment(HomeDataViewFragment.getInstance(qzGroupName, qzSourceName, "3", qzLink, qzSoucesType, qzQuery, qzindex, qzTitle, readIndex));

                break;
            case "3":

                break;
            default:
                break;
        }

        if (qzCover != null) {
            GlideUrl glideUrl = null;
            if (qzCover.indexOf("{QZ}") != -1) {
                String[] sourceStrArray2 = qzCover.split("\\{QZ\\}");
                if (sourceStrArray2.length == 2) {
                    String imgurl = sourceStrArray2[0];
                    String imgref = sourceStrArray2[1];
                    try {
                        glideUrl = new GlideUrl(imgurl, new LazyHeaders.Builder()
                                .addHeader("Referer", imgref)
                                .build());
                    } catch (IllegalArgumentException e) {

                    }

                } else {
                    try {
                        glideUrl = new GlideUrl(qzCover, new LazyHeaders.Builder()
                                .build());
                    } catch (IllegalArgumentException e) {

                    }
                }
            } else {

                try {
                    glideUrl = new GlideUrl(qzCover, new LazyHeaders.Builder()
                            .build());
                } catch (IllegalArgumentException e) {

                }
            }
            if (glideUrl != null) {
                Glide.with(this)
                        .load(glideUrl)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                //helper.getView(R.id.iv_cover).setVisibility(View.VISIBLE);
                                return false;
                            }

                        })
                        .dontAnimate()
                        .into(ivCover);
            }

        }else{
            if(finalSummary!=null)
            {
                if (!finalSummary.isEmpty()){
                    //处理图片和简介
                    loadFinalSummary();
                }else {
                    imageview2.setVisibility(View.INVISIBLE);
                }
            }else {
                imageview2.setVisibility(View.INVISIBLE);
            }
        }

        if(finalSummary!=null)
        {
            LogUtil.info("详情信息",finalSummary);
            if (!finalSummary.isEmpty()){
                //处理图片和简介
                LogUtil.info("进入详情信息","");
                loadFinalSummary();
            }else {
                LogUtil.info("没进入详情信息","");
                if (qzSummary != null) {
                    tvSummary.setText(qzSummary);
                }
            }
        }else{
            LogUtil.info("没进入详情信息","xx");
            if (qzSummary != null) {
                tvSummary.setText(qzSummary);
            }
        }

        if (qzDate != null) {
            tvDate.setText(qzDate);
        }

        tvTitle.setText(qzTitle);
        tvToolbartitle.setText(qzTitle);
        appBar.addOnOffsetChangedListener(new AppBarLayoutStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                switch (state) {
                    case EXPANDED:
                        //Toast.makeText(ScrollingActivity.this, "展开", Toast.LENGTH_SHORT).show();
                        tvToolbartitle.setVisibility(View.INVISIBLE);
                        imgview.setVisibility(View.VISIBLE);
                        //toolbar.setTitle("");
                        break;
                    case COLLAPSED:
                        //Toast.makeText(ScrollingActivity.this, "折叠", Toast.LENGTH_SHORT).show();
                        tvToolbartitle.setVisibility(View.VISIBLE);
                        imgview.setVisibility(View.INVISIBLE);
                        //toolbar.setVisibility(View.VISIBLE);
                        //toolbar.setTitle(qzTitle);
                        break;
                    case INTERMEDIATE:
                        //Toast.makeText(ScrollingActivity.this, "中间状态", Toast.LENGTH_SHORT).show();
                        tvToolbartitle.setVisibility(View.INVISIBLE);
                        imgview.setVisibility(View.INVISIBLE);
                        // toolbar.setVisibility(View.INVISIBLE);
                        //toolbar.setTitle("");
                        break;
                }
            }
        });
    }

    //这里要改成替换 先删掉旧在添加新的
    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }


    private static final int MENU_CONFIRM = 17;
    MenuItem item;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        item = menu.add(Menu.NONE, MENU_CONFIRM, 0, "收藏");
        //主要是这句话
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //添加监听事件
        item.setOnMenuItemClickListener(listener);
        if (isAdd()) {
            item.setIcon(R.drawable.ic_star_black_24dp);
        } else {
            item.setIcon(R.drawable.ic_star_border_black_24dp);
        }
        return true;
    }

    MenuItem.OnMenuItemClickListener listener = item -> {
        switch (item.getItemId()) {
            case MENU_CONFIRM:
                addFavorite();
                break;
            default:
                break;
        }
        return false;
    };


    /**
     * 添加收藏
     */
    private void addFavorite() {
        FavoriteEntity favoriteEntity = new FavoriteEntity();
        favoriteEntity.setGrouping(qzGroupName);
        favoriteEntity.setSourcesName(qzSourceName);
        favoriteEntity.setType(qzSoucesType);
        favoriteEntity.setStep(qzStep);
        favoriteEntity.setTitle(qzTitle);
        favoriteEntity.setSummary(qzSummary);
        favoriteEntity.setCover(qzCover);
        favoriteEntity.setDate(qzDate);
        favoriteEntity.setLink(qzLink);
        favoriteEntity.setReadIndex(0);
        favoriteEntity.setSpinnerSel(qzindex);
        boolean isAdd = SQLModel.getInstance().addFavorite(favoriteEntity);
        if (isAdd) {
            SnackbarUtil.ShortSnackbar(coordinator, "收藏成功！", SnackbarUtil.Confirm).show();

            SharedPreferencesUtils.FavoriteChange(true, getApplicationContext());
            item.setIcon(R.drawable.ic_star_black_24dp);
        } else {
            SnackbarUtil.ShortSnackbar(coordinator, "已经收藏过了！", SnackbarUtil.Warning).show();


        }
    }


    private boolean isAdd() {
        boolean isadd = false;
        try {
            List<FavoriteEntity> favoriteEntityList = SQLModel.getInstance().getFavoritebyTitle(qzTitle, qzSourceName);
            if (favoriteEntityList.isEmpty()) {
                isadd = false;
            } else {
                isadd = true;
            }
        } catch (IllegalArgumentException e) {

        }
        return isadd;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public abstract static class AppBarLayoutStateChangeListener implements SkinMaterialAppBarLayout.OnOffsetChangedListener {

        enum State {
            EXPANDED,//展开
            COLLAPSED,//折叠
            INTERMEDIATE//中间状态
        }

        private State mCurrentState = State.INTERMEDIATE;

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (verticalOffset == 0) {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED);
                }
                mCurrentState = State.EXPANDED;
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED);
                }
                mCurrentState = State.COLLAPSED;
            } else {
                if (mCurrentState != State.INTERMEDIATE) {
                    onStateChanged(appBarLayout, State.INTERMEDIATE);
                }
                mCurrentState = State.INTERMEDIATE;
            }
        }

        public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
    }

    private void loadFinalSummary()
    {
        if(finalSummary.startsWith("[")) {
            JSONArray jsonArray = null;
            try {
                jsonArray = JSON.parseArray(finalSummary);
            }catch (JSONException e)
            {

            }

            if (jsonArray != null) {
                String imageXpath = null;
                String summaryXpath = null;
                String dateXpath = null;
                String dateProcessing = null;
                String imageProcessing = null;
                String summaryProcessing = null;
                for (int i = 0; i < jsonArray.size(); i++) {
                    imageXpath = jsonArray.getJSONObject(i).getString("Image");
                    summaryXpath = jsonArray.getJSONObject(i).getString("Summary");
                    dateXpath = jsonArray.getJSONObject(i).getString("Date");
                    dateProcessing = jsonArray.getJSONObject(i).getString("DateProcessing");
                    imageProcessing = jsonArray.getJSONObject(i).getString("ImageProcessing");
                    summaryProcessing = jsonArray.getJSONObject(i).getString("SummaryProcessing");

                }

                if (imageXpath != null || summaryXpath != null || dateXpath != null) {


                    String finalImageXpath = imageXpath;
                    String finalImageProcessing = imageProcessing;
                    String finalDateXpath = dateXpath;
                    String finalDateProcessing = dateProcessing;
                    String finalSummaryXpath = summaryXpath;
                    String finalSummaryProcessing = summaryProcessing;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String html = NetUtils.getInstance().getRequest(qzLink, null, qzCharset);
                            if (null != html) {
                                Document doc = Jsoup.parse(html);
                                //将DOM转为jx
                                JXDocument jxDocument = JXDocument.create(doc.toString());
                                String summary = "";
                                String imageurl = "";
                                String date = "";
                                //通过列表xpath获取的列表的dom
                                if (finalImageXpath.startsWith("//*")) {

                                    try {
                                        imageurl = getValue(jxDocument, finalImageXpath, finalImageProcessing);
                                    } catch (InputMismatchException e) {

                                    }
                                }
                                if (finalDateXpath.startsWith("//*")) {

                                    try {
                                        date = getValue(jxDocument, finalDateXpath, finalDateProcessing);
                                    } catch (InputMismatchException e) {

                                    }
                                }

                                if (finalSummaryXpath.startsWith("//*")) {

                                    try {
                                        summary = getValue(jxDocument, finalSummaryXpath, finalSummaryProcessing);
                                    } catch (InputMismatchException e) {

                                    }
                                }
                                String finalImageurl = imageurl;
                                if(finalImageurl!=null) {
                                    if (!finalImageurl.startsWith("http")) {
                                        finalImageurl = getHost(qzLink) + finalImageurl;
                                    }
                                }
                                String finalDate = date;
                                String finalSummary1 = summary;
                                String finalImageurl1 = finalImageurl;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (finalImageurl1 != null&&qzCover==null) {

                                            if (finalImageurl1.startsWith("http")) {

                                                GlideUrl glideUrl = null;
                                                if (finalImageurl1.indexOf("{QZ}") != -1) {
                                                    String[] sourceStrArray2 = finalImageurl1.split("\\{QZ\\}");
                                                    if (sourceStrArray2.length == 2) {
                                                        String imgurl = sourceStrArray2[0];
                                                        String imgref = sourceStrArray2[1];
                                                        try {
                                                            glideUrl = new GlideUrl(imgurl, new LazyHeaders.Builder()
                                                                    .addHeader("Referer", imgref)
                                                                    .build());
                                                        } catch (IllegalArgumentException e) {

                                                        }

                                                    } else {
                                                        try {
                                                            glideUrl = new GlideUrl(finalImageurl1, new LazyHeaders.Builder()
                                                                    .build());
                                                        } catch (IllegalArgumentException e) {

                                                        }
                                                    }
                                                } else {

                                                    try {
                                                        glideUrl = new GlideUrl(finalImageurl1, new LazyHeaders.Builder()
                                                                .build());
                                                    } catch (IllegalArgumentException e) {

                                                    }
                                                }
                                                if (glideUrl != null) {
                                                    Glide.with(getApplicationContext())
                                                            .load(glideUrl)
                                                            .listener(new RequestListener<Drawable>() {
                                                                @Override
                                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                                                    return false;
                                                                }

                                                                @Override
                                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                                                    //helper.getView(R.id.iv_cover).setVisibility(View.VISIBLE);
                                                                    return false;
                                                                }

                                                            })
                                                            .dontAnimate()
                                                            .into(ivCover);
                                                }

                                            }
                                        }

                                        if (finalDate != null) {
                                            if (!finalDate.equals("")) {
                                                tvDate.setText(finalDate);
                                            }
                                        }
                                        if (finalSummary1 != null) {
                                            if (!finalSummary1.equals("")) {
                                                tvSummary.setText(finalSummary1);
                                            }else{
                                                if(qzSummary!=null){
                                                    if(!qzSummary.equals("")){
                                                        tvSummary.setText(qzSummary);
                                                    }
                                                }

                                            }
                                        }else{
                                            if(qzSummary!=null){
                                                if(!qzSummary.equals("")){
                                                    tvSummary.setText(qzSummary);
                                                }
                                            }

                                        }
                                    }
                                });
                            }

                        }
                    }).start();
                }


            }else{
                tvSummary.setText(qzSummary);
            }
        }
    }

    private String getValue(JXDocument jxDocument, String xpath,String Processing) {
        String title = null;
        //标题xpaht不为空
        if (!"".equals(xpath)) {
            //获取列表中所有标题的值

            List<Object> titles = jxDocument.sel(xpath);
            if (titles.size() != 0) {

                LogUtil.info("轻舟调试详情","处理前"+titles.get(0).toString());
                //获取标题
                title = titles.get(0).toString();
                if (!"".equals(Processing)) {
                    //第一步处理 处理图片 如果需要处理的值不为空  （一般情况下使用左拼接将图片拼接完整）
                    title = AnalysisUtils.getInstance().processingValue(title, Processing, qzLink, 0);
                }



                LogUtil.info("轻舟调试详情","处理后"+title);


            } else {

                LogUtil.info("轻舟调试警告","没有解析到时显示html："+titles.toString());
            }
        }
        return title;
    }

    public static String getHost(String url){
        if(url==null||url.trim().equals("")){
            return "";
        }
        String host = "";
        Pattern p =  Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
        Matcher matcher = p.matcher(url);
        if(matcher.find()){
            host = matcher.group();
        }
        if(url.startsWith("http://")){
            host = "http://" + host;
        }else if(url.startsWith("https://")){
            host = "https://" + host;
        }
        return host;
    }

}
