package com.liansu.boduowms.modules.menu.commonMenu.subMenu;

import android.content.Context;
import android.content.Intent;

import com.liansu.boduowms.bean.menu.MenuChildrenInfo;
import com.liansu.boduowms.modules.menu.commonMenu.ICommonMenuView;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/5.
 */
public class CommonBusinessSubMenuPresenter {
    CommonBusinessSubMenuModel mModel;
    ICommonMenuView mMenuView;

    public CommonBusinessSubMenuPresenter(Context context, ICommonMenuView view, List<MenuChildrenInfo> list) {
        mModel = new CommonBusinessSubMenuModel(context,list);
        mMenuView = view;
    }

    /**
     * @desc: 加载二级菜单 图标和名称
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/5 16:20
     */
    public void loadMenuList() {
        if (mModel.getMenuChildrenInfoList() != null && mModel.getMenuChildrenInfoList().size() > 0) {
            for (MenuChildrenInfo info : mModel.getMenuChildrenInfoList()) {
                mModel.loadMenuData(info);
            }
            mMenuView.bindMenuList(mModel.getMenuChildrenInfoList());
        }
    }

    /**
     * @desc: 调整二级菜单
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/5 16:27
     */
    public void loadBusiness(MenuChildrenInfo item) {
      Intent intent= mModel.loadSubBusiness(item);
        if (intent != null) {
            mMenuView.loadBusiness(intent);
        }
    }




}
