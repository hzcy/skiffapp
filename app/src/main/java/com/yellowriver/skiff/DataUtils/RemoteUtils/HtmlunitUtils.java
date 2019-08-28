package com.yellowriver.skiff.DataUtils.RemoteUtils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class HtmlunitUtils {
    private static final String TAG = "HtmlunitUtils";
    private static final HtmlunitUtils INSTANCE = new HtmlunitUtils();

    private HtmlunitUtils() {
    }

    public static HtmlunitUtils getInstance() {
        return INSTANCE;
    }

    /**
     * 获取动态生成的数据
     *
     * @return
     * @throws IOException
     */
    @SuppressLint("SetJavaScriptEnabled")
    public static String HtmlUtilGET(String actionUrl,HashMap<String, String> headerMap){

        Log.d("HtmlUtil", "HtmlUtil: 正在使用htmlutil");
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
        //等待js时间
        webClient.waitForBackgroundJavaScript(600 * 1000);//异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束
        //不跟踪抓取
        webClient.getOptions().setDoNotTrackEnabled(false);


        Log.d(TAG, "HtmlUtilGET: "+actionUrl);
        WebRequest request = null;
        try {
            request = new WebRequest(new URL(actionUrl), HttpMethod.GET);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
//        if (paramsMap != null && !paramsMap.isEmpty()) {
//            for (Map.Entry<String, String> param : paramsMap.entrySet()) {
//                request.getRequestParameters().add(new NameValuePair(param.getKey(), param.getValue()));
//            }
//        }
        if (headerMap != null && !headerMap.isEmpty()) {
            request.setAdditionalHeaders(headerMap);
        }

        HtmlPage page = null;
        String pageXml = null;
        try {
            page = webClient.getPage(request);//尝试加载上面图片例子给出的网页
            pageXml = page.asXml();//直接将加载完成的页面转换成xml格式的字符串
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webClient.close();
        }


        return pageXml;

    }



    @SuppressLint("SetJavaScriptEnabled")
    public static String HtmlUtil(String actionUrl, HashMap<String, String> paramsMap) {
        WebClient webclient = new WebClient();
        HtmlPage htmlPage = null;
        HtmlInput inputs;

        String html;
        if (paramsMap != null && !paramsMap.isEmpty()) {
            if ("1".equals(Objects.requireNonNull(paramsMap.get("isAjax")))) {
                webclient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
                webclient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
                webclient.getOptions().setActiveXNative(false);
                webclient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
                webclient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
                webclient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
                //等待js时间
                webclient.waitForBackgroundJavaScript(600 * 1000);//异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束

            } else {
                webclient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
                webclient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
                webclient.getOptions().setActiveXNative(false);
                webclient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
                webclient.getOptions().setJavaScriptEnabled(false); //很重要，启用JS

            }

        }
        try {


            htmlPage = webclient.getPage(actionUrl);
            if (paramsMap != null && !paramsMap.isEmpty()) {
                inputs = (HtmlInput) htmlPage.getElementById(paramsMap.get("input"));//btn = (HtmlInput) htmlPage.getElementById(paramsMap.get("button"));
                inputs.setValueAttribute(Objects.requireNonNull(paramsMap.get("query")));
                try {
                    htmlPage = htmlPage.getElementById(paramsMap.get("button")).click();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            


        } catch (FailingHttpStatusCodeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        html = Objects.requireNonNull(htmlPage).asXml();
        System.out.println(html);

        return html;
    }


}
