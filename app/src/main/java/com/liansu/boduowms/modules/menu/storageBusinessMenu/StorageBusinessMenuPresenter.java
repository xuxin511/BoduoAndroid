package com.liansu.boduowms.modules.menu.storageBusinessMenu;

import android.content.Context;
import android.content.Intent;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.menu.MenuChildrenInfo;
import com.liansu.boduowms.bean.menu.MenuInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.debug.DebugModuleData;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoice;
import com.liansu.boduowms.modules.instock.noSourceOtherStorage.scan.NoSourceOtherScan;
import com.liansu.boduowms.modules.instock.productStorage.printPalletScan.PrintPalletScan;
import com.liansu.boduowms.modules.instock.productStorage.scan.ProductStorageScan;
import com.liansu.boduowms.modules.instock.productionReturnsStorage.print.ProductionReturnsPrint;
import com.liansu.boduowms.modules.instock.productionReturnsStorage.scan.ProductionReturnStorageScan;
import com.liansu.boduowms.modules.instock.salesReturn.print.SalesReturnPrint;
import com.liansu.boduowms.modules.menu.IMenuPresenter;
import com.liansu.boduowms.modules.menu.IMenuView;
import com.liansu.boduowms.modules.qualityInspection.bill.QualityInspectionMainActivity;
import com.liansu.boduowms.modules.qualityInspection.randomInspection.bill.RandomInspectionBill;
import com.liansu.boduowms.utils.function.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/25.
 */
public class StorageBusinessMenuPresenter extends IMenuPresenter {

    IMenuView mMenuView;
    Context   mContext;

    public StorageBusinessMenuPresenter(IMenuView menuView, Context context) {
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
                String path = menuChildrenInfos.get(i).getPath();
                if (!CommonUtil.isNumeric(path)) continue;
                int Node = Integer.parseInt(path);
                switch (Node) {
                    case 22:
                        itemIconList.add(R.drawable.purchase_storage_icon);  //到货入库
                        itemNamesList.add(mContext.getString(R.string.main_menu_item_purchase_storage));
                        break;
                    case 24:
                        itemIconList.add(R.drawable.transfer_to_storage);  //调拨入库
                        itemNamesList.add(mContext.getString(R.string.main_menu_item_transfer_to_storage));
                        break;
                    case 26:
                        itemIconList.add(R.drawable.sales_return);  //销售退货
                        itemNamesList.add(mContext.getString(R.string.main_menu_item_active_sales_return));
                        break;


                }
            }
//
            if (DebugModuleData.isDebugDataStatusOffline()) {
                DebugModuleData.loadStorageBusinessMenuList(mContext, itemIconList, itemNamesList);
            }
            //cion和iconName的长度是相同的，这里任选其一都可以

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
        if (moduleName.equals(mContext.getString(R.string.main_menu_item_purchase_storage))) {
            intent.setClass(mContext, BaseOrderBillChoice.class);
            intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE);
        } else if (moduleName.equals(mContext.getString(R.string.main_menu_item_spot_check))) {
            intent.setClass(mContext, RandomInspectionBill.class);
        } else if (moduleName.equals(mContext.getString(R.string.main_menu_item_quality_inspection))) {
            intent.setClass(mContext, QualityInspectionMainActivity.class);
        } else if (moduleName.equals(mContext.getString(R.string.main_menu_item_outsourcing))) {
            intent.setClass(mContext, BaseOrderBillChoice.class);
            intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_OUTSOURCING_STORAGE);
        } else if (moduleName.equals(mContext.getString(R.string.main_menu_item_transfer_to_storage))) {
            intent.setClass(mContext, BaseOrderBillChoice.class);
            intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_TRANSFER_TO_STORAGE);
        } else if (moduleName.equals(mContext.getString(R.string.main_menu_item_active_other_storage))) {
            intent.setClass(mContext, BaseOrderBillChoice.class);
            intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE);
        }else if (moduleName.equals(mContext.getString(R.string.main_menu_item_production_storage))){
            intent.setClass(mContext, ProductStorageScan.class);
            intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE);
        }
        else if (moduleName.equals(mContext.getString(R.string.main_menu_item_production_storage_pallet_no_print))){
            intent.setClass(mContext, PrintPalletScan.class);
            intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE);
        }
        else if (moduleName.equals(mContext.getString(R.string.main_menu_item_active_sales_return))){
            intent.setClass(mContext, SalesReturnPrint.class);
            intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE);
        }else if (moduleName.equals(mContext.getString(R.string.main_menu_item_production_returns))){
            intent.setClass(mContext, ProductionReturnStorageScan.class);
            intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE);
        }else if (moduleName.equals(mContext.getString(R.string.main_menu_item_active_scan_other_storage))){
            intent.setClass(mContext, NoSourceOtherScan.class);
        }else if (moduleName.equals(mContext.getString(R.string.main_menu_item_product_return_print_storage))){
            intent.setClass(mContext, ProductionReturnsPrint.class);
            intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE);


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
                if (menuInfo.getTitle().equals("PDA入库")) {
                    List<MenuChildrenInfo> menuChildrenInfos = menuInfo.getChildren();
                    return menuChildrenInfos;
                }
            }
        }
        return null;
    }

}


