package com.liansu.boduowms.modules.menu.outboundBusiness;

import android.content.Context;
import android.content.Intent;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.menu.MenuChildrenInfo;
import com.liansu.boduowms.bean.menu.MenuInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.debug.DebugModuleData;
import com.liansu.boduowms.modules.menu.IMenuPresenter;
import com.liansu.boduowms.modules.menu.IMenuView;
import com.liansu.boduowms.modules.menu.outboundBusiness.subMenu.OutboundBusinessSubMenu;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.baseReviewScan.BaseReviewScan;
import com.liansu.boduowms.utils.function.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/25.
 */
public class OutboundBusinessMenuPresenter extends IMenuPresenter {


    IMenuView mMenuView;
    Context   mContext;

    public OutboundBusinessMenuPresenter(IMenuView menuView, Context context) {
        mMenuView = menuView;
        mContext = context;
    }

    @Override
    protected void loadMenuList(List<MenuInfo> menuInfos) {
        mMenuList = new ArrayList<Map<String, Object>>();
        ArrayList<Integer> itemIconList = new ArrayList<>();
        ArrayList<String> itemNamesList = new ArrayList<>();
        List<MenuChildrenInfo> menuChildrenInfos = getMenuChildrenList(menuInfos);
        if (menuChildrenInfos != null) {
            for (int i = 0; i < menuChildrenInfos.size(); i++) {
                String nodUrl = menuChildrenInfos.get(i).getPath();
                if (!CommonUtil.isNumeric(nodUrl)) continue;
                int Node = Integer.parseInt(nodUrl);
                switch (Node) {
//                    case 25:
//                        itemIconList.add(R.drawable.transfer_out_of_warehouse);
//                        itemNamesList.add(mContext.getString(R.string.main_menu_item_transfer_out_of_warehouse));
//                        break;
//                    case 27:
//                        itemIconList.add(R.drawable.purchase_returns);
//                        itemNamesList.add(mContext.getString(R.string.main_menu_item_off_shelf_scan));
//                        break;
////                    case 30:
////                        itemIconList.add(R.drawable.sales_out_of_stock);
////                        itemNamesList.add(mContext.getString(R.string.main_menu_item_sales_out_of_stock));
////                        break;
//                    case 30:
//                        itemIconList.add(R.drawable.sales_out_of_stock);
//                        itemNamesList.add(mContext.getString(R.string.main_menu_item_sales_out_stock));
//                        break;
                }
            }    //cion和iconName的长度是相同的，这里任选其一都可以

            if (DebugModuleData.isDebugDataStatusOffline()) {
                DebugModuleData.loadOutboundBusinessMenuList(mContext, itemIconList, itemNamesList);
            }
            for (int i = 0; i < itemIconList.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("image", itemIconList.get(i));
                map.put("text", itemNamesList.get(i));
                mMenuList.add(map);
            }
        }

        mMenuView.bindMenuList(mMenuList);
    }

    @Override
    protected void loadBusiness(String moduleName) {
        Intent intent = new Intent();
      if (moduleName.equals(mContext.getString(R.string.main_menu_item_delivery_lcl))) {
//          intent.setClass(mContext, PackingScan.class);
        } else if (moduleName.equals(mContext.getString(R.string.main_menu_item_loading_truck))) {
            intent.setClass(mContext, BaseReviewScan.class);
            intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION);
        }else if (moduleName.equals(mContext.getString(R.string.main_menu_item_purchase_inspection))) {
            intent.setClass(mContext, OutboundBusinessSubMenu.class);
            intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION);
        }else if (moduleName.equals(mContext.getString(R.string.main_menu_item_purchase_return))) {
            intent.setClass(mContext, OutboundBusinessSubMenu.class);
            intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_RETURN);
        }else if (moduleName.equals(mContext.getString(R.string.main_menu_item_other_loading_truck))) {
            intent = intent.setClass(mContext, BaseReviewScan.class);
            intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION);
        }else if(moduleName.equals(mContext.getString(R.string.main_menu_item_sales_out_stock))) {
          intent.setClass(mContext, OutboundBusinessSubMenu.class);
          intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_SALES_OUTSOTCK);
//            intent = intent.setClass(mContext, SalesOutstock.class);
//            intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_SALES_OUTSOTCK);
        }


        if (intent != null) {
            mMenuView.loadBusiness(intent);
        }

    }

    /**
     * @desc: 获取菜单入库模块
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/9 9:09
     */
    public List<MenuChildrenInfo> getMenuChildrenList(List<MenuInfo> list) {
        if (list != null && list.size() > 0) {
            for (MenuInfo menuInfo : list) {
                if (menuInfo.getTitle().equals("PDA出库")) {
                    List<MenuChildrenInfo> menuChildrenInfos = menuInfo.getChildren();
                    return menuChildrenInfos;
                }
            }
        }
        return null;
    }
}


