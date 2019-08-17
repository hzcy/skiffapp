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
     * @return
     * @throws IOException
     */
    @SuppressLint("SetJavaScriptEnabled")
    public static String HtmlUtilGET(String actionUrl, HashMap<String, String> paramsMap, HashMap<String, String> headerMap, String Charset) throws IOException {

        Log.d("HtmlUtil", "HtmlUtil: 正在使用htmlutil");
         WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
        //等待js时间
        webClient.waitForBackgroundJavaScript(600*1000);//异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束
        //不跟踪抓取
        webClient.getOptions().setDoNotTrackEnabled(false);


        WebRequest request=new WebRequest(new URL(actionUrl), HttpMethod.GET);
        if (paramsMap != null && !paramsMap.isEmpty()) {
            for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                request.getRequestParameters().add(new NameValuePair(param.getKey(), param.getValue()));
            }
        }
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
        }finally {
            webClient.close();
        }




        return pageXml;

    }

    /**
     * 获取动态生成的数据
     * @return
     * @throws IOException
     */
    @SuppressLint("SetJavaScriptEnabled")
    public static String HtmlUtilPOST(String actionUrl, HashMap<String, String>  paramsMap, HashMap<String, String> headerMap, String Charset) throws Exception{
        String result;
        Log.d("HtmlUtil", "HtmlUtil: 正在使用htmlutil");
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
        //等待js时间
        webClient.waitForBackgroundJavaScript(600*1000);//异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束
        //不跟踪抓取
        webClient.getOptions().setDoNotTrackEnabled(false);


        WebRequest request=new WebRequest(new URL(actionUrl), HttpMethod.POST);

        if (paramsMap != null && !paramsMap.isEmpty()) {
            if ("on".equals(Objects.requireNonNull(paramsMap.get("type"))))
            {

            }else {
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    request.getRequestParameters().add(new NameValuePair(param.getKey(), param.getValue()));
                }
            }
        }
        if (headerMap != null && !headerMap.isEmpty()) {
            request.setAdditionalHeaders(headerMap);
        }

        HtmlPage page = null;

        try {
            if (paramsMap != null && !paramsMap.isEmpty())
            {
                if ("on".equals(Objects.requireNonNull(paramsMap.get("type"))))
                {
                    Log.d(TAG, "HtmlUtilPOST: 获取ON");
                    HtmlInput inputs;
                    HtmlInput btn;
                    page = webClient.getPage(actionUrl);
                    inputs = (HtmlInput) page.getElementById(paramsMap.get("input"));
                    btn = (HtmlInput) page.getElementById(paramsMap.get("button"));
                    inputs.setValueAttribute(Objects.requireNonNull(paramsMap.get("query")));
                    Log.d(TAG, "HtmlUtilPOST: "+paramsMap.get("query"));
                    try {
                        page = btn.click();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }else {
                page = webClient.getPage(request);//尝试加载上面图片例子给出的网页
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            webClient.close();
        }

        String pageXml = Objects.requireNonNull(page).asXml();//直接将加载完成的页面转换成xml格式的字符串
        System.out.println(pageXml);
        switch (Charset) {
            //utf-8
            case "1":
                result = pageXml;
                break;
            case "2":
                result = pageXml;
                break;
            default:
                result = pageXml;
                break;
        }

        return result;

    }
    @SuppressLint("SetJavaScriptEnabled")
    public static String HtmlUtil(String actionUrl, HashMap<String, String>  paramsMap) {
        WebClient webclient = new WebClient();
        HtmlPage htmlPage;
        HtmlInput inputs;

        String html;
        HtmlPage tempPage = null;

        if (paramsMap != null && !paramsMap.isEmpty())
        {
            if ("1".equals(Objects.requireNonNull(paramsMap.get("isAjax"))))
            {
                webclient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
                webclient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
                webclient.getOptions().setActiveXNative(false);
                webclient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
                webclient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
                webclient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
                //等待js时间
                webclient.waitForBackgroundJavaScript(600*1000);//异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束

            }else
            {
                webclient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
                webclient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
                webclient.getOptions().setActiveXNative(false);
                webclient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
                webclient.getOptions().setJavaScriptEnabled(false); //很重要，启用JS

            }
        }

        try {
            if (paramsMap != null && !paramsMap.isEmpty()) {

                htmlPage = webclient.getPage(actionUrl);
                inputs = (HtmlInput) htmlPage.getElementById(paramsMap.get("input"));//btn = (HtmlInput) htmlPage.getElementById(paramsMap.get("button"));
                inputs.setValueAttribute(Objects.requireNonNull(paramsMap.get("query")));

                try {
                    tempPage = htmlPage.getElementById(paramsMap.get("button")).click();
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
        html = Objects.requireNonNull(tempPage).asXml();
        System.out.println(html);

        return html;
    }


}
