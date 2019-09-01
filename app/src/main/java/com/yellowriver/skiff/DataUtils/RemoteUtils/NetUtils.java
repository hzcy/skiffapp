package com.yellowriver.skiff.DataUtils.RemoteUtils;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by hzcy on 2016/12/24 0024.
 * @author huang
 */
public class NetUtils {

    private static final String TAG = "NetUtils";
    private static OkHttpClient buildHttpClient() {
        return new OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(20, TimeUnit.SECONDS).build();
    }

    private static final NetUtils INSTANCE = new NetUtils();

    private NetUtils() {
    }

    public static NetUtils getInstance() {
        return INSTANCE;
    }

    /**
     * Get请求
     *
     * @param path
     * @return
     */
    public String getRequest(String path, String Charset) {
        OkHttpClient client = buildHttpClient();
        final Request request = new Request.Builder()
                .get()
                .url(path)
                .addHeader("Connection", "close")
                .build();

        String result = null;
        Response response;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                switch (Charset) {
                    //utf-8
                    case "1":
                        result = Objects.requireNonNull(response.body()).string();
                        break;
                    case "2":
                        //获取数据的bytes
                        byte[] b = Objects.requireNonNull(response.body()).bytes();
                        result = new String(b, "GB2312");
                        break;
                    default:
                        result = Objects.requireNonNull(response.body()).string();
                        break;
                }


            } else {
                //throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return result;

    }

    /**
     * GET
     *
     * @param actionUrl get url
     * @param headerMap get header
     * @return
     */
    public String getRequest(String actionUrl, HashMap<String, String> headerMap, String Charset) {
        //Log.d("getRequest", "getRequest: getRequest");
        OkHttpClient client = buildHttpClient();

        Request.Builder builder2 = new Request.Builder()
                .get()
                .url(actionUrl);


        //GET请求-4添加请求头信息
        if (headerMap != null && !headerMap.isEmpty()) {
            for (String key : headerMap.keySet()) {
                builder2.addHeader(key, Objects.requireNonNull(headerMap.get(key)));
            }
        }else
        {
            builder2.addHeader("Connection", "close");
        }

        Request request = builder2.build();
        String result = null;
        Response response;

        try {
            response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                switch (Charset) {
                    //utf-8
                    case "1":
                        result = response.body().string();
                        break;
                    case "2":
                        //获取数据的bytes
                        byte[] b = response.body().bytes();
                        result = new String(b, "GB2312");
                        break;
                    default:
                        result = response.body().string();
                        break;
                }

            } else {
                //throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }

        return result;

    }


    /**
     * POST
     *
     * @param actionUrl post url
     * @param paramsMap post data
     * @param headerMap post header
     * @return
     */
    public String requestPost(String actionUrl, HashMap<String, String> paramsMap, HashMap<String, String> headerMap, String Charset) {
        OkHttpClient client = buildHttpClient();
        Log.d("requestPost", "requestPost: 正在使用requestPost");
        //创建一个FormBody.Builder
        FormBody.Builder builder = new FormBody.Builder();
        if (paramsMap != null && !paramsMap.isEmpty()) {
            for (String key : paramsMap.keySet()) {
                //追加表单信息
                Log.d(TAG, "requestPost: "+paramsMap.get(key));
                builder.add(key, Objects.requireNonNull(paramsMap.get(key)));
            }
        }
        //生成表单实体对象
        RequestBody formBody = builder.build();
        Request.Builder builder2 = new Request.Builder()

                .url(actionUrl)
                .post(formBody);

        //GET请求-4添加请求头信息
        if (headerMap != null && !headerMap.isEmpty()) {
            for (String key : headerMap.keySet()) {
                builder2.addHeader(key, Objects.requireNonNull(headerMap.get(key)));
            }
        }else
        {
            builder2.addHeader("Connection", "close");
        }

        Request request = builder2.build();

        String result = null;
        Response response;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                switch (Charset) {
                    //utf-8
                    case "1":
                        result = Objects.requireNonNull(response.body()).string();
                        break;
                    case "2":
                        byte[] b = Objects.requireNonNull(response.body()).bytes();
                        result = new String(b, "GB2312");
                        break;
                    default:
                        result = Objects.requireNonNull(response.body()).string();
                        break;
                }


            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return result;

    }





    /**
     * 获取重定向地址
     *
     * @param path
     * @return
     */
    public static String getRedirectUrl(String path) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(path)
                    .openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(conn).setInstanceFollowRedirects(false);
        conn.setConnectTimeout(5000);
        return conn.getHeaderField("Location");
    }


}
