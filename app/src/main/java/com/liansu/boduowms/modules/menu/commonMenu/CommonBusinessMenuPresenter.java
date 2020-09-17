package com.liansu.boduowms.modules.menu.commonMenu;

import android.content.Context;
import android.content.Intent;

import com.liansu.boduowms.bean.menu.MenuChildrenInfo;
import com.liansu.boduowms.bean.menu.MenuType;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/25.
 */
public class CommonBusinessMenuPresenter {

    ICommonMenuView mMenuView;
    Context         mContext;
    MenuModel       mModel;

    public CommonBusinessMenuPresenter(ICommonMenuView menuView, Context context, int menuType) {
        mMenuView = menuView;
        mContext = context;
        mModel = new MenuModel(context, menuType);
    }

    /**
     * @desc: 加载菜单数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/16 14:47
     */
    protected void loadMenuList() {
        if (mModel.getMenuChildrenInfoList() != null && mModel.getMenuChildrenInfoList().size() > 0) {
            for (MenuChildrenInfo info : mModel.getMenuChildrenInfoList()) {
                if (mModel.getMenuType() == MenuType.MENU_TYPE_IN_STOCK) {
                    mModel.loadInStockMenuData(info);
                } else if (mModel.getMenuType() == MenuType.MENU_TYPE_OUT_STOCK) {
                    mModel.loadOutStockMenuData(info);
                }else if (mModel.getMenuType() == MenuType.MENU_TYPE_IN_HOUSE_STOCK) {
                    mModel.loadInHouseStockMenuData(info);
                }

            }
            mMenuView.bindMenuList(mModel.getMenuChildrenInfoList());
        }
    }


    /**
     * @desc: 加载菜单对应的业务或者 加载二级菜单
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/16 14:47
     */
    protected void loadBusiness(MenuChildrenInfo item) {
        Intent intent = null;
        String path = item.getPath();
        int voucherType = Integer.parseInt(path);
        if (mModel.getMenuType() == MenuType.MENU_TYPE_IN_STOCK) {
            intent = mModel.loadInStockBusiness(item,voucherType);
        } else if (mModel.getMenuType() == MenuType.MENU_TYPE_OUT_STOCK) {
            intent = mModel.loadOutStockBusiness(item,voucherType);
        } else if (mModel.getMenuType() == MenuType.MENU_TYPE_IN_HOUSE_STOCK) {
            intent = mModel.loadInHouseStockBusiness(item,voucherType);
        }

        if (intent != null) {
            mMenuView.loadBusiness(intent);
        }

    }





}


