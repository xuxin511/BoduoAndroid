package com.liansu.boduowms.modules.setting.user;

import android.content.Context;

import com.liansu.boduowms.ui.dialog.MessageBox;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/19.
 */
public class UserSettingPresenter {
    Context          mContext;
    UserSettingModel mModel;
    IUserSettingView     mView;

    public UserSettingPresenter(Context context, IUserSettingView view) {
        mModel = new UserSettingModel(context);
        mView = view;
        mContext = context;
    }

    public UserSettingModel getModel() {
        return mModel;
    }

    /**
     * @desc: 保存当前仓库信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/19 13:24
     */
    public void saveCurrentWareHouse(String wareHouseName) {
        if (mModel.saveCurrentWarehouseInfo(wareHouseName)) {
            mView.setTitle();
        } else {
            //不可能出现 做个校验
            MessageBox.Show(mContext, "选择仓库信息失败:出现预期之外的异常，选择的仓库名称在当前用户的仓库信息列表中未找到");
        }
    }
}
