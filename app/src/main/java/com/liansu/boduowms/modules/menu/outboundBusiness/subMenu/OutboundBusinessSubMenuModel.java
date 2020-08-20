package com.liansu.boduowms.modules.menu.outboundBusiness.subMenu;

import android.content.Context;
import android.content.Intent;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.outstock.SalesOutstock.SalesOutReview;
import com.liansu.boduowms.modules.outstock.SalesOutstock.SalesOutStockBox;
import com.liansu.boduowms.modules.outstock.SalesOutstock.SalesOutstock;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.baseReviewScan.BaseReviewScan;
import com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.bill.PurchaseInspectionBill;
import com.liansu.boduowms.modules.outstock.purchaseReturn.offscan.PurchaseReturnOffScan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/5.
 */
public class OutboundBusinessSubMenuModel {
    Context                   mContext;
    List<Integer>             mItemIconList  = new ArrayList<>();
    List<String>              mItemNamesList = new ArrayList<>();
    List<Map<String, Object>> mMenuList      = new ArrayList<>();

    public OutboundBusinessSubMenuModel(@NonNull Context context) {
        mContext = context;
    }

    ;

    /**
     * @desc: 载入二级菜单信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/5 16:42
     */
    public List<Map<String, Object>> loadBusinessSubMenu(String businessType) {
        mItemIconList.clear();
        mItemNamesList.clear();
        mMenuList.clear();
        // "采购验退"   ||采购退货
        if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION) || businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_RETURN)){
            mItemIconList.add(R.drawable.other_outbound);
            mItemNamesList.add(mContext.getString(R.string.main_menu_item_off_shelf_scan));
            mItemIconList.add(R.drawable.loading_truck);
            mItemNamesList.add(mContext.getString(R.string.main_menu_item_other_loading_truck));
        }else if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_SALES_OUTSOTCK)){
            mItemIconList.add(R.drawable.other_outbound);
            mItemNamesList.add(mContext.getString(R.string.main_menu_item_off_shelf_scan));
            mItemIconList.add(R.drawable.loading_truck);
            mItemNamesList.add(mContext.getString(R.string.main_menu_item_other_loading_truck));
            mItemIconList.add(R.drawable.set_tray_and_remove_tray);
            mItemNamesList.add(mContext.getString(R.string.main_menu_item_delivery_lcl));
        }
        for (int i = 0; i < mItemIconList.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", mItemIconList.get(i));
            map.put("text", mItemNamesList.get(i));
            mMenuList.add(map);
        }
        return mMenuList;
    }


    /**
     * @desc:  跳转二级菜单 Intent
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/5 16:42
     */
    protected Intent loadSubBusiness(String moduleName, String businessType) {
        Intent intent = new Intent();
        if (moduleName.equals(mContext.getString(R.string.main_menu_item_off_shelf_scan))) { //下架扫描
            if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION)) {
                intent.setClass(mContext, PurchaseInspectionBill.class);
            } else if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_RETURN)){
                intent.setClass(mContext, PurchaseReturnOffScan.class);
            }else if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_SALES_OUTSOTCK)){
                intent.setClass(mContext, SalesOutstock.class);
            }
            intent.putExtra("BusinessType", businessType);
        } else if (moduleName.equals(mContext.getString(R.string.main_menu_item_other_loading_truck))) {
          //复核
          //  intent = intent.setClass(mContext, BaseReviewScan.class);
            if  (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION)) {
                intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION);
            }else if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_SALES_OUTSOTCK)){
                //销售出库复核PackingScanAdapter
                intent.setClass(mContext, SalesOutReview.class);
            }
            intent.putExtra("BusinessType", businessType);
        }
        //拼箱
        else if(moduleName.equals((mContext.getString((R.string.main_menu_item_delivery_lcl))))){
     //       intent = intent.setClass(mContext, BaseReviewScan.class);
            if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_SALES_OUTSOTCK)){
                //销售出库拼箱
                intent.setClass(mContext, SalesOutStockBox.class);
            }
            intent.putExtra("BusinessType", businessType);
        }
        else if (moduleName.equals(mContext.getString(R.string.main_menu_item_off_shelf_scan))) {
            intent = intent.setClass(mContext, BaseReviewScan.class);
            intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION);
        } else {
            intent = null;
        }
       return intent;
    }


}
