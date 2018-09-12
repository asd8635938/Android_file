package com.ahanyujia.yoga.manager;

import android.app.Application;
import com.alibaba.fastjson.JSON;
import com.ahanyujia.yoga.listener.onNetCallbackListener;
import com.ahanyujia.yoga.response.Response;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import static org.xutils.x.http;

/**
 * 网络请求框架
 */

public class NetManager {

    public static int Net_TimeOut = 15000;

    private static Application sApp;

    public static void init(Application mApplication) {
        sApp = mApplication;
    }

    /**
     * 统一请求框架最基础方法，更换第三方可以修改这里
     *
     * @param url              请求网址
     * @param head             请求头
     * @param body             请求体
     * @param callbackListener 请求回掉
     * @return
     */
    public static void http_post(String url, String head, Object body, final onNetCallbackListener callbackListener) {
        String string = "";
        if (body instanceof String) {
            string = (String) body;
        }
        RequestParams requestParams = new RequestParams(url);
        requestParams.addHeader("", head);
        requestParams.setBodyContent(string);
        requestParams.setConnectTimeout(Net_TimeOut);
        requestParams.setReadTimeout(Net_TimeOut);
        requestParams.setAsJsonContent(true);
        final String finalString = string;
        http().post(requestParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(final String s) {
                Response mResponse = JSON.parseObject(s, Response.class);
                if (callbackListener != null) {
                    callbackListener.onSuccess(finalString, s);
                }
            }

            @Override
            public void onError(final Throwable throwable, final boolean b) {
                if (callbackListener != null) {
                    callbackListener.onError(throwable);
                }
            }

            @Override
            public void onCancelled(final CancelledException e) {
                if (callbackListener != null) {
                    callbackListener.onCancelled();
                }
            }

            @Override
            public void onFinished() {
                if (callbackListener != null) {
                    callbackListener.onFinished();
                }
            }
        });
    }

}
