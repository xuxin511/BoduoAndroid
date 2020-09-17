package com.liansu.boduowms.modules.menu.commonMenu;

import android.content.Intent;

import com.liansu.boduowms.bean.menu.MenuChildrenInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/9/16.
 */
public interface ICommonMenuView {
    void bindMenuList(List<MenuChildrenInfo> menuList);
    void loadBusiness(Intent intent);
}
