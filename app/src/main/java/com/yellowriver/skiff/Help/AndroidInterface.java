package com.yellowriver.skiff.Help;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.just.agentweb.AgentWeb;

public class AndroidInterface {

    private Handler deliver = new Handler(Looper.getMainLooper());
    private AgentWeb agent;
    private Context context;

    public AndroidInterface(AgentWeb agent, Context context) {
        this.agent = agent;
        this.context = context;
    }



    @JavascriptInterface
    public void callAndroid(final String key) {

        Log.d("call", "run: "+key);

        deliver.post(new Runnable() {
            @Override
            public void run() {
                Log.d("call", "run: "+key);
                joinQQGroup(key);

            }
        });


        Log.i("Info", "Thread:" + Thread.currentThread());

    }

    /****************
     *
     * 发起添加群流程。群号：轻舟一群(883483362) 的 key 为： TtFCZ32h_iq5yYGV2HPGX8pvOaTlW2wg
     * 调用 joinQQGroup(TtFCZ32h_iq5yYGV2HPGX8pvOaTlW2wg) 即可发起手Q客户端申请加群 轻舟一群(883483362)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

}
