package com.liansu.boduowms.modules.menu;

import com.liansu.boduowms.bean.menu.MenuChildrenInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/18.
 */
public class MenuModel {
    private List<MenuChildrenInfo> mMenuChildrenInfo = new ArrayList<>();

    public void setMenuChildrenInfoList(List<MenuChildrenInfo> list) {
        mMenuChildrenInfo.clear();
        if (list != null && list.size() > 0) {
            mMenuChildrenInfo.addAll(list);
        }

    }


    public List<MenuChildrenInfo> getMenuChildrenInfo() {
        return mMenuChildrenInfo;
    }

    /**
     * @desc:  获取订单类型
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/18 23:25
     */
    public int getVoucherType(String moduleName){
        int voucherType=-1;
        for (MenuChildrenInfo info:mMenuChildrenInfo){
            if (moduleName.equals(info.getTitle())){
                voucherType = Integer.parseInt(info.getPath());
                break;
            }
        }

        return voucherType;
    }
}
