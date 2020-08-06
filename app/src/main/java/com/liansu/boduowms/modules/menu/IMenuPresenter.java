package com.liansu.boduowms.modules.menu;


import com.liansu.boduowms.bean.menu.MenuInfo;

import java.util.List;
import java.util.Map;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/25.
 */
public abstract class IMenuPresenter {
    protected List<Map<String, Object>> mMenuList;
    protected abstract void loadMenuList(List<MenuInfo> list);
    protected  List<Map<String, Object>>  getMenuList(){
        return  mMenuList;
    }
    protected  abstract  void  loadBusiness(String moduleName);

}
