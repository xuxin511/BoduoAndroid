package com.liansu.boduowms.base;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @ Des: 数据层基类
 * @ Created by yangyiqing on 2020/6/27.
 */
public abstract class BaseFragmentModel {
    protected MyHandler<BaseFragment>              mHandler;
    protected Context                          mContext;
    protected Map<String, NetCallBackListener> mNetMap = new HashMap<>();

    protected abstract void onHandleMessage(Message msg);

    public BaseFragmentModel(Context context, MyHandler<BaseFragment> handler) {
        mContext = context;
        mHandler = handler;
    }
}
