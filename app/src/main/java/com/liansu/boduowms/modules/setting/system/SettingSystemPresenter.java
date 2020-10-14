package com.liansu.boduowms.modules.setting.system;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseFragment;
import com.liansu.boduowms.modules.setting.print.SettingModel;
import com.liansu.boduowms.utils.hander.MyHandler;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class SettingSystemPresenter {
    private Context            mContext;
    private SettingModel       mModel;
    private ISettingSystemView mView;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public SettingSystemPresenter(Context context, ISettingSystemView view, MyHandler<BaseFragment> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new SettingModel(context, handler);
    }


}
