package com.liansu.boduowms.base;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.liansu.boduowms.bean.menu.MenuInfo;
import com.liansu.boduowms.bean.user.UserInfo;
import com.liansu.boduowms.bean.warehouse.WareHouseInfo;

import org.xutils.x;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import androidx.multidex.MultiDex;

/**
 * 指定自定义Application类，用于存放全局变量
 * 在 AndroidManifest.xml中指定android:name="com.example.demo.application.BaseApplication" 来启用
 */

public class BaseApplication extends Application {
    public static BaseApplication instance;

    public static Context context;  //activity中context对象

    public static String DialogShowText;

    public static boolean isCloseActivity = true;

    public static ToolBarTitle toolBarTitle;

    public static UserInfo       mCurrentUserInfo;
    public static WareHouseInfo  mCurrentWareHouseInfo;
    public static List<MenuInfo> mCurrentMenuList;
    private       RequestQueue   mRequestQueue;

    public static int       mDebugDataStatus = -1;
    //取消Volley 断线重连
    static        HurlStack mStack           = new HurlStack() {
        @Override
        protected HttpURLConnection createConnection(URL url) throws IOException {
            HttpURLConnection con = super.createConnection(url);
            //主要是这行代码, 貌似是因为HttpClient的bug
            con.setChunkedStreamingMode(0);
            return con;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //Stetho.initializeWithDefaults(this); //调试工具
        x.Ext.init(this);
//        OkHttpClient okHttpClient = new OkHttpClient();
//        okHttpClient.networkInterceptors().add(new StethoInterceptor());
//        mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack(okHttpClient));

        mRequestQueue = Volley.newRequestQueue(this, mStack);

        MultiDex.install(this);
        // ErrorCodeParser.init();
    }


    public static BaseApplication getInstance() {
        return (BaseApplication) instance;
    }

    public static RequestQueue getRequestQueue() {
        return instance.mRequestQueue;
    }


}
