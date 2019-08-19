package com.yellowriver.skiff.View.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.yellowriver.skiff.Adapter.RecyclerViewAdapter.MenuAdapter;
import com.yellowriver.skiff.Adapter.RecyclerViewAdapter.ReadAdapter;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.HomeBean.MsgEvent;
import com.yellowriver.skiff.Bean.SimpleBean;
import com.yellowriver.skiff.DataUtils.LocalUtils.SharedPreferencesUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.ReadModeUtils;
import com.yellowriver.skiff.Help.CustomLoadMoreView;
import com.yellowriver.skiff.Help.MyLinearLayoutManager;
import com.yellowriver.skiff.Help.SmallUtils;
import com.yellowriver.skiff.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.yellowriver.skiff.R.drawable.ic_more_vert_black_24dp;

/**
 * 阅读界面 只显示文字
 *
 * @author huang
 * @date 2019/7/29
 */
public class ReadActivity extends AppCompatActivity implements ColorPickerDialogListener {

    private static final String TAG = "ReadActivity";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.results_list)
    RecyclerView mReadRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.list_title)
    TextView mupTitle;
    @BindView(R.id.sort)
    ImageView sortImageView;
    @BindView(R.id.menu_list)
    RecyclerView mMenuRecyclerView;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private static final String POSITIVE = "正";
    private static final String NEGATIVE = "反";
    private int selindex = 0;
    private int fromindex = 0;
    private ReadAdapter mReadAdapter;
    private boolean isLoading;
    /**
     * 目录列表适配器
     */
    private MenuAdapter mMenuAdapter;
    /**
     * 阅读数据
     */
    private List<SimpleBean> data;
    /*
     * 目录列表数据
     */
    private Vector<DataEntity> mMenuDataEntity;

    /**
     * 用着解析url
     */
    private String url;
    private String contentXpath;
    private String readHost;
    private String readCharset;
    private String content;
    private String upTitle;

    String title;
    /**
     * 默认字体大小 和 背景颜色
     */
    int fontSize = 18;
    int BGColor;
    int fontColor;


    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMsgEvent(MsgEvent event) {
        if (null != event) {
            //赋值
            this.mMenuDataEntity = event.getContent();
            this.selindex = event.getSelIndex();
            this.fromindex = event.getSelIndex();
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        ButterKnife.bind(this);

        //注册绑定事件即可接收相关数据
        EventBus.getDefault().register(this);
        bindData();
        bindView();
        bindEvent();
    }


    /**
     *
     */
    private void bindData() {
        title = getIntent().getStringExtra("qzTitle");
        readHost = getIntent().getStringExtra("qzReadHost");
        readCharset = getIntent().getStringExtra("qzCharset");
        contentXpath = getIntent().getStringExtra("qzReadXpath");
        url = getIntent().getStringExtra("qzLink");
        upTitle = getIntent().getStringExtra("qzUpTitle");
        content = getIntent().getStringExtra("qzContent");
        //取出数据
        BGColor = SharedPreferencesUtils.bgColorRead(getApplicationContext());
        fontSize = SharedPreferencesUtils.fontSizeRead(getApplicationContext());
        fontColor = SharedPreferencesUtils.fontColorRead(getApplicationContext());
    }

    MyLinearLayoutManager myLinearLayoutManagerRead;
    @SuppressLint("ResourceAsColor")
    private void bindView() {
        ButterKnife.bind(this);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        mToolbar.setOverflowIcon(getResources().getDrawable(ic_more_vert_black_24dp));
        mToolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(navView);
                if (mMenuAdapter != null) {
                    mMenuRecyclerView.scrollToPosition(selindex);
                }
            }
        });

        //下拉刷新颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorLogo1, R.color.colorLogo2, R.color.colorLogo3, R.color.colorLogo4);
        //垂直显示

        myLinearLayoutManagerRead = new MyLinearLayoutManager(this);
        mReadRecyclerView.setLayoutManager(myLinearLayoutManagerRead);
        //阅读 RecyclerView的基本设置
        mReadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mReadRecyclerView.setAdapter(mReadAdapter = new ReadAdapter(R.layout.read_item));
        mReadRecyclerView.setBackgroundColor(BGColor);

        //目录
        MyLinearLayoutManager myLinearLayoutManagerMenu;
        myLinearLayoutManagerMenu = new MyLinearLayoutManager(this);
        mMenuRecyclerView.setLayoutManager(myLinearLayoutManagerMenu);
        mMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mMenuRecyclerView.setAdapter(mMenuAdapter = new MenuAdapter(R.layout.menu_item));

    }


    private void bindEvent() {
        //下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.postDelayed(() -> {
            //开启下拉刷新
            mSwipeRefreshLayout.setRefreshing(true);

            //开启线程加载数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (fromindex!=-1&&mMenuDataEntity!=null)
                    {
                        url = mMenuDataEntity.get(fromindex).getLink();
                    }
                    data = ReadModeUtils.getContent(url, contentXpath, readHost, readCharset, content);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mReadAdapter.setNewData(data);

                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }).start();


        }, 1000));
        //进入界面开始加载数据
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    data = ReadModeUtils.getContent(url, contentXpath, readHost, readCharset, content);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mReadAdapter.setNewData(data);

                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }).start();

        });
        if (null != mMenuDataEntity) {
            mMenuAdapter.setNewData(mMenuDataEntity);
            mupTitle.setText(upTitle);
            sortImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Collections.reverse(mMenuAdapter.getData());
                    mMenuAdapter.notifyDataSetChanged();
                    mMenuRecyclerView.scrollToPosition(0);
                    //保存数据的状态
                    if (POSITIVE.equals(SharedPreferencesUtils.readDataSort(Objects.requireNonNull(getApplicationContext())))) {
                        SharedPreferencesUtils.writeDataSort("反", getApplicationContext());

                    } else if (NEGATIVE.equals(SharedPreferencesUtils.readDataSort(getApplicationContext()))) {
                        SharedPreferencesUtils.writeDataSort("正", getApplicationContext());
                    }
                }
            });
            mMenuRecyclerView.scrollToPosition(selindex);
        }


        mMenuAdapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d(TAG, "onItemClick: ");
            final DataEntity dataEntity = (DataEntity) adapter.getData().get(position);
            drawerLayout.closeDrawer(navView);
            mSwipeRefreshLayout.setRefreshing(true);
            url = dataEntity.getLink();
            title = dataEntity.getTitle();
            selindex = position;
            fromindex = position;
            //开启线程加载数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    data = ReadModeUtils.getContent(url, contentXpath, readHost, readCharset, content);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mReadAdapter.setNewData(data);
                            mSwipeRefreshLayout.setRefreshing(false);
                            mToolbar.setTitle(title);
                            mReadRecyclerView.scrollToPosition(0);
                        }
                    });
                }
            }).start();

        });
        mReadRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i(TAG, "--------------------------------------");
                int mFirstVisibleItem = myLinearLayoutManagerRead.findFirstVisibleItemPosition();

                if (data!=null&&mFirstVisibleItem!=-1)
                {
                    title = mMenuDataEntity.get(mFirstVisibleItem + fromindex).getTitle();

                    mToolbar.setTitle(title);

                }
            }
        });



            //下拉加载更多

            mReadAdapter.setOnLoadMoreListener(() -> {

                //mReadAdapter.removeAllFooterView();


                if (mMenuDataEntity.size() <= 1) {

                } else {

                    selindex++;
                    if (selindex < 0 || selindex >= mMenuDataEntity.size()) {
                        mReadAdapter.setFooterView(LayoutInflater.from(getApplicationContext()).inflate(R.layout.footer_loadover, mReadRecyclerView, false));
                        mReadAdapter.loadMoreEnd(true);
                    } else {
                        url = mMenuDataEntity.get(selindex).getLink();
                        title = mMenuDataEntity.get(selindex).getTitle();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                data = ReadModeUtils.getContent(url, contentXpath, readHost, readCharset, content);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (data != null) {
                                            mReadAdapter.addData(data);
                                            mReadAdapter.loadMoreComplete();


                                        } else {
                                            mReadAdapter.setFooterView(LayoutInflater.from(getApplicationContext()).inflate(R.layout.footer_loadover, mReadRecyclerView, false));
                                            mReadAdapter.loadMoreEnd(true);
                                        }

                                    }
                                });
                            }
                        }).start();
                    }
                }


            }, mReadRecyclerView);

        mReadAdapter.setLoadMoreView(new CustomLoadMoreView());
        mReadAdapter.disableLoadMoreIfNotFullPage();


    }




    @Override
    public void onColorSelected(int dialogId, int color) {
        if (dialogId == 0) {
            SharedPreferencesUtils.fontColorWrite(getApplicationContext(), color);
            textcolor.setBackgroundColor(color);
            mReadAdapter.notifyDataSetChanged();
        } else {
            SharedPreferencesUtils.bgColorWrite(getApplicationContext(), color);
            bgcolor.setBackgroundColor(color);
            mReadAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

    Button textcolor;
    Button bgcolor;

    private void showBottomSheetDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = View.inflate(this, R.layout.fontsize_dialog, null);
        //文字大小
        TextView tv_text = view.findViewById(R.id.textsize);
        ImageView ib_add = view.findViewById(R.id.add);
        ImageView ib_abate = view.findViewById(R.id.abate);
        //行高
        TextView textlineheight = view.findViewById(R.id.textlineheight);
        ImageView heightadd = view.findViewById(R.id.heightadd);
        ImageView heightabate = view.findViewById(R.id.heightabate);
        //文字间距
        TextView textletterspa = view.findViewById(R.id.textletterspa);
        ImageView letterspaadd = view.findViewById(R.id.letterspaadd);
        ImageView letterspaabate = view.findViewById(R.id.letterspaabate);
        //文字间距
        TextView textmargin = view.findViewById(R.id.textmargin);
        ImageView marginaadd = view.findViewById(R.id.marginaadd);
        ImageView marginabate = view.findViewById(R.id.marginabate);
        //颜色
        textcolor = view.findViewById(R.id.textcolor);
        bgcolor = view.findViewById(R.id.bgcolor);
        tv_text.setText(fontSize + "");
        textlineheight.setText(SharedPreferencesUtils.fontLineheightRead(getApplicationContext()) + "");
        textletterspa.setText(SharedPreferencesUtils.fontLetterspaRead(getApplicationContext()) + "");
        textmargin.setText(SharedPreferencesUtils.fontMarginRead(getApplicationContext()) + "");

        Log.d(TAG, "showBottomSheetDialog: " + fontColor);
        textcolor.setBackgroundColor(fontColor);
        bgcolor.setBackgroundColor(BGColor);
        textcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int fontcolorstr = SharedPreferencesUtils.fontColorRead(getApplicationContext());
                ColorPickerDialog.newBuilder().setColor(fontcolorstr).setDialogTitle(R.string.fontcolor).setDialogId(0).show(ReadActivity.this);

            }
        });
        bgcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int bgcolorstr = SharedPreferencesUtils.bgColorRead(getApplicationContext());
                ColorPickerDialog.newBuilder().setColor(bgcolorstr).setDialogTitle(R.string.bgcolor).setDialogId(1).show(ReadActivity.this);

            }
        });

        ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = Integer.parseInt(tv_text.getText().toString());
                Log.d(TAG, "onClick: " + size);
                tv_text.setText(size + 1 + "");
                fontSize = Integer.parseInt(tv_text.getText().toString());
                SharedPreferencesUtils.fontSizeWrite(getApplicationContext(), fontSize);
                mReadAdapter.notifyDataSetChanged();

            }
        });
        ib_abate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = Integer.parseInt(tv_text.getText().toString());
                tv_text.setText(size - 1 + "");
                fontSize = Integer.parseInt(tv_text.getText().toString());
                SharedPreferencesUtils.fontSizeWrite(getApplicationContext(), fontSize);
                mReadAdapter.notifyDataSetChanged();
            }
        });

        heightadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = Integer.parseInt(textlineheight.getText().toString());
                Log.d(TAG, "onClick: " + size);
                textlineheight.setText(size + 1 + "");
                int fontheigh = Integer.parseInt(textlineheight.getText().toString());
                SharedPreferencesUtils.fontLineheightWrite(getApplicationContext(), fontheigh);
                mReadAdapter.notifyDataSetChanged();

            }
        });
        heightabate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = Integer.parseInt(textlineheight.getText().toString());
                textlineheight.setText(size - 1 + "");
                int fontheigh = Integer.parseInt(textlineheight.getText().toString());
                SharedPreferencesUtils.fontLineheightWrite(getApplicationContext(), fontheigh);
                mReadAdapter.notifyDataSetChanged();
            }
        });

        letterspaadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = Integer.parseInt(textletterspa.getText().toString());
                Log.d(TAG, "onClick: " + size);
                textletterspa.setText(size + 1 + "");
                int letterspa = Integer.parseInt(textletterspa.getText().toString());
                SharedPreferencesUtils.fontLetterspaWrite(getApplicationContext(), letterspa);
                mReadAdapter.notifyDataSetChanged();

            }
        });
        letterspaabate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = Integer.parseInt(textletterspa.getText().toString());
                textletterspa.setText(size - 1 + "");
                int letterspa = Integer.parseInt(textletterspa.getText().toString());
                SharedPreferencesUtils.fontLetterspaWrite(getApplicationContext(), letterspa);
                mReadAdapter.notifyDataSetChanged();
            }
        });

        marginaadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = Integer.parseInt(textmargin.getText().toString());
                Log.d(TAG, "onClick: " + size);
                textmargin.setText(size + 1 + "");
                int margin = Integer.parseInt(textmargin.getText().toString());
                SharedPreferencesUtils.fontMarginWrite(getApplicationContext(), margin);
                mReadAdapter.notifyDataSetChanged();

            }
        });
        marginabate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = Integer.parseInt(textmargin.getText().toString());
                textmargin.setText(size - 1 + "");
                int margin = Integer.parseInt(textmargin.getText().toString());
                SharedPreferencesUtils.fontMarginWrite(getApplicationContext(), margin);
                mReadAdapter.notifyDataSetChanged();
            }
        });


        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.show();
    }

    private static final int MENU_CONFIRM = 18;
    MenuItem item;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.readmenu, menu);
        item = menu.add(Menu.NONE, MENU_CONFIRM, 0, "关闭");
        //主要是这句话 显示图标
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(R.drawable.ic_font_download_black_24dp);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case MENU_CONFIRM:
                showBottomSheetDialog();
                break;
            case R.id.default_browser:
                SmallUtils.getInstance().openBrowser(url, getApplicationContext());
                break;
            default:
                break;
        }
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //移除全部粘性事件
        EventBus.getDefault().removeAllStickyEvents();
        //解绑事件
        EventBus.getDefault().unregister(this);
    }


}