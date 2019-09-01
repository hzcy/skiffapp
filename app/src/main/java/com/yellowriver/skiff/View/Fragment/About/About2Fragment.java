package com.yellowriver.skiff.View.Fragment.About;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.tencent.bugly.beta.Beta;
import com.yellowriver.skiff.Help.SmallUtils;
import com.yellowriver.skiff.Help.SnackbarUtil;
import com.yellowriver.skiff.R;
import com.yellowriver.skiff.View.Activity.Other.AboutWebViewActivity;
import com.yellowriver.skiff.View.Activity.Other.LicenseActivity;

import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 关于
 *
 * @author huang
 */
public class About2Fragment extends Fragment {
    private static final String TAG = "About2Fragment";
    @BindView(R.id.update)
    LinearLayout update;
    @BindView(R.id.changelog)
    LinearLayout changelog;
    @BindView(R.id.homepage)
    LinearLayout homepage;
    @BindView(R.id.share)
    LinearLayout share;
    @BindView(R.id.license)
    LinearLayout license;
    @BindView(R.id.qqgroup)
    LinearLayout qqgroup;
    @BindView(R.id.weixingroup)
    LinearLayout weixingroup;
    @BindView(R.id.weixingzh)
    LinearLayout weixingzh;
    @BindView(R.id.mail)
    LinearLayout mail;
    @BindView(R.id.alipayhb)
    LinearLayout alipayhb;
    @BindView(R.id.alipayjz)
    LinearLayout alipayjz;
    @BindView(R.id.qqjz)
    LinearLayout qqjz;
    @BindView(R.id.weixinjz)
    LinearLayout weixinjz;
    @BindView(R.id.disclaimer)
    LinearLayout disclaimer;
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.main)
    CoordinatorLayout main;


    public About2Fragment() {
        // Required empty public constructor
    }

    View mRootView;
    /**
     * 绑定控件
     */
    private Unbinder bind;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "测试-->onCreateView: ");

        if (mRootView == null) {

            Log.d(TAG, "测试-->新加载view");
            mRootView = inflater.inflate(R.layout.fragment_about2, container, false);
            bind = ButterKnife.bind(this, mRootView);

            //加载视图
            bindView();

        } else {
            Log.d(TAG, "测试-->使用旧view");
        }

        return mRootView;
    }

    private void bindView() {
        version.setText(getPackageInfo(getContext()).versionName);
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

    /**
     * 销毁.
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "测试-->onDestroy");
        super.onDestroy();
        if (bind!=null) {
            //解除绑定
            bind.unbind();
        }

    }

    @Override
    public void onStop() {
        Log.d(TAG, "测试-->onStop");
        super.onStop();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "测试-->onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "测试-->onResume");
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "测试-->onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "测试-->onDestroyView");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "测试-->onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }


    @OnClick(R.id.license)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), LicenseActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.update, R.id.changelog, R.id.homepage, R.id.share, R.id.qqgroup, R.id.weixingroup, R.id.weixingzh, R.id.mail, R.id.alipayhb, R.id.alipayjz, R.id.qqjz, R.id.weixinjz, R.id.disclaimer})
    public void onViewClicked(View view) {
        String qzLink = "";
        String qzTitle = "";
        switch (view.getId()) {
            case R.id.update:
                Beta.checkUpgrade();
                break;
            case R.id.changelog:
                qzLink = "http://hege.gitee.io/page/changelog.html";
                qzTitle = "更新日志";
                break;
            case R.id.homepage:
                qzLink = "http://hege.gitee.io/index.html";
                qzTitle = "轻舟";
                break;
            case R.id.share:
                //分享软件
                qzLink = "http://hege.gitee.io/page/share.html";
                qzTitle = "分享海报";
                break;
            case R.id.qqgroup:
                qzLink = "http://hege.gitee.io/page/qqGroup.html";
                qzTitle = "QQ群";
                break;
            case R.id.weixingroup:
                qzLink = "http://hege.gitee.io/page/weixinGroup.html";
                qzTitle = "微信群";
                break;
            case R.id.weixingzh:
                //复制公众号到剪贴板
                SmallUtils.getInstance().toCopy(getContext(), "轻舟软件");
                //SnackbarUtil.ShortSnackbar(getView(), "公众号名称已复制到剪贴板", SnackbarUtil.Confirm).show();
                Toast.makeText(getContext(), "公众号名称已复制到剪贴板", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mail:
                Intent data = new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:skiffapp@163.com"));
                startActivity(data);
                break;
            case R.id.alipayhb:
                String qrcode = "9GAZc1x07989uzufesr4jfzu2f85Vq";
                String url = "intent://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + qrcode + "#Intent;scheme=alipays;package=com.eg.android.AlipayGphone;end";
                startIntentUrl(getActivity(), url);
                break;
            case R.id.alipayjz:
                //String code = "com.eg.android.AlipayGphone";
                String INTENT_URL_FORMAT = "intent://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2FFKX099391S5VJELYIYEUA1%3F_s%3Dweb-other#Intent;scheme=alipays;package=com.eg.android.AlipayGphone;end";
                startIntentUrl(getActivity(), INTENT_URL_FORMAT);
                break;
            case R.id.qqjz:
                qzLink = "http://hege.gitee.io/page/qqjz.html";
                qzTitle = "QQ捐赠";
                break;
            case R.id.weixinjz:
                qzLink = "http://hege.gitee.io/page/weixinjz.html";
                qzTitle = "微信捐赠";
                break;
            case R.id.disclaimer:
                //免责声明
                qzLink = "http://hege.gitee.io/page/disclaimer.html";
                qzTitle = "免责声明";
                break;
            default:
                break;
        }
        if (!"".equals(qzLink) && !"".equals(qzTitle)) {
            Intent intent = new Intent(getActivity(), AboutWebViewActivity.class);
            intent.putExtra("qzLink", qzLink);
            intent.putExtra("qzTitle", qzTitle);
            startActivity(intent);
        }
    }

    public static boolean startIntentUrl(Activity activity, String intentFullUrl) {
        try {
            @SuppressLint("WrongConstant") Intent e = Intent.parseUri(intentFullUrl, 1);
            activity.startActivity(e);
            return true;
        } catch (URISyntaxException var3) {
            var3.printStackTrace();
            return false;
        } catch (ActivityNotFoundException var4) {
            var4.printStackTrace();
            return false;
        }
    }
}
