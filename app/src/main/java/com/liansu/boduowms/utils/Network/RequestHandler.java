package com.liansu.boduowms.utils.Network;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.ui.dialog.LoadingDialog;
import com.liansu.boduowms.utils.log.LogUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import static com.liansu.boduowms.base.BaseApplication.context;
import static com.liansu.boduowms.base.BaseApplication.getRequestQueue;


/**
 * Created by GHOST on 2017/3/13.
 */

public class RequestHandler {

    public static int SOCKET_TIMEOUT = 90000;




    private static void addRequest(
            int method, String tag,
            final Handler handler, final int what,
            final Bundle bundle, final String url, final String params, final Map<String, String> header,
            final NetWorkRequestListener listener) {
        final String uuid= UUID.randomUUID().toString()+tag;
        listener.onPreRequest();
        LogUtil.WriteLog(RequestHandler.class, uuid, "请求数据:"+url+"\n\r"+params);
        JsonStringRequest JsonRequest = new JsonStringRequest(method, url, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtil.WriteLog(RequestHandler.class, uuid, "返回数据:"+response);
                onVolleyResponse(response, handler, what, bundle);
                listener.onResponse();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.WriteLog(RequestHandler.class, uuid, "返回数据:"+VolleyErrorHelper.getMessage(volleyError, context));
                onVolleyErrorResponse(volleyError, listener, handler, bundle);
            }
        });
        //清除缓存
        getRequestQueue().getCache().get(url);
        getRequestQueue().getCache().remove(url);
        getRequestQueue().getCache().clear();
        // 清除请求队列中的tag标记请求
        getRequestQueue().cancelAll(uuid);
        // 为当前请求添加标记
        JsonRequest.setTag(uuid);
        //设置超时时间
        JsonRequest.setRetryPolicy(getRetryPolicy());
        getRequestQueue().add(JsonRequest);
    }



    public static void addRequestWithDialog(
            final int method, final String tag, final String LoadText, Context context, final Handler handler, final int what, final Bundle bundle,
            final String url, final String params, final Map<String, String> header) {
        addRequest(method, tag, handler, what, bundle, url, params, header, new DefaultDialogRequestListener(context, LoadText) {        });
    }

    private static void addRequest(
            int method, String tag,
            final Handler handler, final int what,
            final Bundle bundle, String url, final Map<String, String> params, final Map<String, String> header,
            final NetWorkRequestListener listener) {
        if (method == Request.Method.GET) {
            url = NetworkHelper.getUrlWithParams(url, params);
        }
        listener.onPreRequest();
        final String uuid= UUID.randomUUID().toString()+tag;
        LogUtil.WriteLog(RequestHandler.class, uuid, "请求数据:"+url+"\n\r"+params);
        String para = (new org.json.JSONObject(params)).toString();
        try {
            JsonStringRequest JsonRequest = new JsonStringRequest(method, url, para, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    LogUtil.WriteLog(RequestHandler.class, uuid, "返回数据:"+response);
                    onVolleyResponse(response, handler, what, bundle);
                    listener.onResponse();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    LogUtil.WriteLog(RequestHandler.class, uuid, "返回数据:"+VolleyErrorHelper.getMessage(volleyError, context));
                    onVolleyErrorResponse(volleyError, listener, handler, bundle);
                }
            });

            //清理缓存
            getRequestQueue().getCache().get(url); //获取单个缓存
            getRequestQueue().getCache().remove(url);  //清除单个缓存
            getRequestQueue().getCache().clear();
            // 清除请求队列中的tag标记请求
            getRequestQueue().cancelAll(uuid);
            // 为当前请求添加标记
            JsonRequest.setTag(uuid);
            //设置超时时间

            JsonRequest.setRetryPolicy(getRetryPolicy());
            getRequestQueue().add(JsonRequest);
        } catch (Exception ex) {
            LogUtil.WriteLog(RequestHandler.class, "error", ex.getMessage());
        }
    }

    /**
     * @desc: 使用Object 传参  ，GET请求使用前请测试，里面的{URLEncoder.encode}方法是String类型的，虽然改成了Object+"",但没有试过，效果不明，修改代码参照 {addRequest} 方法
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/9 11:14
     */
    private static void addObjectRequest(
            int method, String tag,
            final Handler handler, final int what,
            final Bundle bundle, String url, final Map<String, Object> params, final Map<String, String> header,
            final NetWorkRequestListener listener) {
        if (method == Request.Method.GET) {
            url = NetworkHelper.getUrlWithObjectParams(url, params);
        }
        listener.onPreRequest();
        final String uuid= UUID.randomUUID().toString()+tag;
        String para = (new org.json.JSONObject(params)).toString();
        LogUtil.WriteLog(RequestHandler.class, uuid, "请求数据:"+url+"\n\r"+params);
        try {
            JsonStringRequest JsonRequest = new JsonStringRequest(method, url, para, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    LogUtil.WriteLog(RequestHandler.class, uuid, "返回数据:"+response);
                    onVolleyResponse(response, handler, what, bundle);
                    listener.onResponse();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    LogUtil.WriteLog(RequestHandler.class, uuid, "返回数据:"+VolleyErrorHelper.getMessage(volleyError, context));
                    onVolleyErrorResponse(volleyError, listener, handler, bundle);
                }
            });

            //清理缓存
            getRequestQueue().getCache().get(url); //获取单个缓存
            getRequestQueue().getCache().remove(url);  //清除单个缓存
            getRequestQueue().getCache().clear();
            // 清除请求队列中的tag标记请求
            getRequestQueue().cancelAll(uuid);
            // 为当前请求添加标记
            JsonRequest.setTag(uuid);
            //设置超时时间

            JsonRequest.setRetryPolicy(getRetryPolicy());
            getRequestQueue().add(JsonRequest);
        } catch (Exception ex) {
            LogUtil.WriteLog(RequestHandler.class, "error", ex.getMessage());
        }
    }

    private static void onVolleyErrorResponse(VolleyError volleyError, NetWorkRequestListener listener, Handler handler, Bundle bundle) {
//        if (listener.retry()) {
//            listener.onFailed();
//        }
        Message msg = handler.obtainMessage(NetworkError.NET_ERROR_CUSTOM, VolleyErrorHelper.getMessage(volleyError, context));
        msg.setData(bundle);
        handler.sendMessage(msg);
        listener.onFailed();
    }

    private static void onVolleyResponse(String response, Handler handler, int what, Bundle bundle) {
        Message msg = handler.obtainMessage(what, response);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    /**
     * @param method  Request.Method.GET 或 Request.Method.POST
     * @param handler 请求结束后将结果作为Message.obj发送到该Handler
     * @param what    请求结束后发送的Message.what
     * @param bundle  不参与网络请求，仅携带参数
     *                （请求结束后，通过Message.setData设置到Message对象，数据原样返回）
     * @param url     请求地址
     * @param params  请求参数
     * @param header  请求头
     */
    public static void addRequest(
            final int method, final String tag, final Handler handler, final int what, final Bundle bundle,
            final String url, final Map<String, String> params, final Map<String, String> header) {
        addRequest(method, tag, handler, what, bundle, url, params, header, new DefaultRequestListener() {
//            @Override
//            public boolean retry() {
//                addRequest(method, tag, handler, what, bundle, url, params, header,
//                        retryTimer++ >= MAX_RETRY_TIME ? new DefaultRequestListener() : this);
//                return true;
//            }
        });
    }

    public static void addRequest(
            final int method, final String tag, final Handler handler, final int what, final Bundle bundle,
            final String url, final String params, final Map<String, String> header) {
        addRequest(method, tag, handler, what, bundle, url, params, header, new DefaultRequestListener() {
//            @Override
//            public boolean retry() {
//                addRequest(method, tag, handler, what, bundle, url, params, header,
//                        retryTimer++ >= MAX_RETRY_TIME ? new DefaultRequestListener() : this);
//                return true;
//            }
        });
    }

    public static void addRequestWithDialog(
            final int method, final String tag, final String LoadText, Context context, final Handler handler, final int what, final Bundle bundle,
            final String url, final Map<String, String> params, final Map<String, String> header) {
        addRequest(method, tag, handler, what, bundle, url, params, header, new DefaultDialogRequestListener(context, LoadText) {
//            @Override
//            public boolean retry() {
////                addRequest(method, tag, handler, what, bundle, url, params, header,
////                        retryTimer++ >= MAX_RETRY_TIME ? new DefaultDialogRequestListener(context, LoadText) : this);
////                return true;
//                return false;
//            }
        });
    }

//    public static void addRequestWithDialog(
//            final int method, final String tag, final String LoadText, Context context, final Handler handler, final int what, final Bundle bundle,
//            final String url, final String params, final Map<String, String> header) {
//        addRequest(method, tag, handler, what, bundle, url, params, header, new DefaultDialogRequestListener(context, LoadText) {
//
//        });
//    }

    public static void addObjectRequestWithDialog(
            final int method, final String tag, final String LoadText, Context context, final Handler handler, final int what, final Bundle bundle,
            final String url, final Map<String, Object> params, final Map<String, String> header) {
        addObjectRequest(method, tag, handler, what, bundle, url, params, header, new DefaultDialogRequestListener(context, LoadText) {
        });
    }

    static RetryPolicy getRetryPolicy() {
//         RetryPolicy retryPolicy = new DefaultRetryPolicy(SOCKET_TIMEOUT, DefaultDialogRequestListener.MAX_RETRY_TIME, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        RetryPolicy retryPolicy = new DefaultRetryPolicy(SOCKET_TIMEOUT, DEFAULT_MAX_RETRIES, 0f);
        RetryPolicy retryPolicy = new DefaultRetryPolicy(SOCKET_TIMEOUT,  DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return retryPolicy;
    }

    /**
     * 请求过程中显示加载对话框，且自动处理其生命周期
     */
    private static class DefaultDialogRequestListener extends DefaultRequestListener {

        Context       context;
        LoadingDialog dialog;

        public DefaultDialogRequestListener(Context context, String LoadText) {
            this.context = context;
            BaseApplication.DialogShowText = LoadText;
            dialog = new LoadingDialog(context);
        }

        @Override
        public void onPreRequest() {
            dialog.show();
        }

        @Override
        public void onResponse() {
            dialog.dismiss();
        }

        @Override
        public void onFailed() {
            dialog.dismiss();
        }
    }

    private static class DefaultRequestListener implements NetWorkRequestListener {

        int retryTimer;

        static final int MAX_RETRY_TIME = -1;

        @Override
        public void onPreRequest() {

        }

        @Override
        public void onResponse() {

        }

        @Override
        public void onFailed() {

        }

//        @Override
//         public boolean retry() {
//            return false;
//        }
    }

    /**
     * 用于所有网络请求，在不同时机回调的接口
     */
    private static interface NetWorkRequestListener {
        void onPreRequest();

        void onResponse();

        void onFailed();

        // boolean retry();
    }

    // StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                onVolleyResponse(response, handler, what, bundle);
//                listener.onResponse();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                onVolleyErrorResponse(volleyError, listener, handler, bundle);
//            }
//        })
//            {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> map = header;
//                if (map == null) {
//                    map = new HashMap<>();
//                }
//                // 在此统一添加header
//               // map.put("versionName", BuildConfig.VERSION_NAME);
//                return map;
//            }
//
////            /**
////             * Volley仅在post的情况下会回调该方法，获取form表单参数
////             * @return
////             * @throws AuthFailureError
////             */
////            @Override
////            protected Map<String, String> getParams() throws AuthFailureError {
////                return params;
////            }
    //   };

}
