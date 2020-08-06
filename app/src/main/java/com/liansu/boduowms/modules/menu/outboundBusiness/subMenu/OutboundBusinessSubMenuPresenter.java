package com.liansu.boduowms.modules.menu.outboundBusiness.subMenu;

import android.content.Context;
import android.content.Intent;

import com.liansu.boduowms.modules.menu.IMenuView;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/5.
 */
public class OutboundBusinessSubMenuPresenter {
    OutboundBusinessSubMenuModel mModel;
    IMenuView                    mMenuView;

    public OutboundBusinessSubMenuPresenter(Context context, IMenuView view) {
        mModel = new OutboundBusinessSubMenuModel(context);
        mMenuView = view;
    }

    /**
     * @desc: 加载二级菜单 图标和名称
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/5 16:20
     */
    public void loadMenuList(String businessType) {
        mMenuView.bindMenuList(mModel.loadBusinessSubMenu(businessType));
    }

    /**
     * @desc: 调整二级菜单
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/5 16:27
     */
    public void loadBusiness(String moduleName,String businessType) {
      Intent intent= mModel.loadSubBusiness(moduleName,businessType);
        if (intent != null) {
            mMenuView.loadBusiness(intent);
        }
    }
}
