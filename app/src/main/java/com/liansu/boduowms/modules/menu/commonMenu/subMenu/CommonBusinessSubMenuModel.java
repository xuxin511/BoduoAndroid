package com.liansu.boduowms.modules.menu.commonMenu.subMenu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.menu.MenuChildrenInfo;
import com.liansu.boduowms.bean.menu.MenuType;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.outstock.Model.MenuOutStockModel;
import com.liansu.boduowms.modules.outstock.SalesOutstock.OutstockOneReview;
import com.liansu.boduowms.modules.outstock.SalesOutstock.OutstockOrderColse;
import com.liansu.boduowms.modules.outstock.SalesOutstock.OutstockRawmaterialActivity;
import com.liansu.boduowms.modules.outstock.SalesOutstock.OutstockSalesConfig;
import com.liansu.boduowms.modules.outstock.SalesOutstock.SalesOutReview;
import com.liansu.boduowms.modules.outstock.SalesOutstock.SalesOutStockBox;
import com.liansu.boduowms.modules.outstock.SalesOutstock.SalesOutstock;
import com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.bill.PurchaseInspectionBill;
import com.liansu.boduowms.utils.function.GsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/5.
 */
public class CommonBusinessSubMenuModel {
    Context mContext;
    private List<MenuChildrenInfo> mMenuChildrenInfoList = new ArrayList<>();

    public CommonBusinessSubMenuModel() {
    }

    public CommonBusinessSubMenuModel(@NonNull Context context, List<MenuChildrenInfo> list) {
        mContext = context;
        setMenuChildrenInfoList(list);
    }


    public void setMenuChildrenInfoList(List<MenuChildrenInfo> list) {
        mMenuChildrenInfoList.clear();
        if (list != null && list.size() > 0) {
            mMenuChildrenInfoList.addAll(list);
        }

    }


    public List<MenuChildrenInfo> getMenuChildrenInfoList() {
        return mMenuChildrenInfoList;
    }
    public void loadMenuData(MenuChildrenInfo item) {
        if (item != null) {
            String path = item.getPath();
            int voucherType = Integer.parseInt(path);
            String moduleType=item.getComponent()!=null?item.getComponent():"";
            int icon = -1;
           switch (moduleType.trim()){
               case MenuType.MENU_MODULE_TYPE_OUT_STOCK_OFF_SHELF:
               case MenuType.MENU_MODULE_TYPE_OUT_STOCK_OFF_SHELF_TWO:
                   icon=R.drawable.b_out_stock_off_shelf;
                   break;
               case MenuType.MENU_MODULE_TYPE_OUT_STOCK_LCL:
                   icon=R.drawable.b_set_tray_and_remove_tray;
                   break;
               case MenuType.MENU_MODULE_TYPE_OUT_STOCK_LOADING_TRUCK:
               case MenuType.MENU_MODULE_TYPE_OUT_STOCK_INDOOR_LOADING_TRUCK:
                   icon=R.drawable.b_loading_truck;
                   break;
               case MenuType.MENU_MODULE_TYPE_OUT_STOCK_SHIPMENT_CLOSED:
               case MenuType.MENU_MODULE_TYPE_OUT_STOCK_ONE_REVIEW:
                   icon=R.drawable.b_shipment_approval;
                   break;
           }
            if (icon != -1) {
                item.setIcon(icon);
            }
        }
    }

    /**
     * @desc: 跳转二级菜单 Intent
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/5 16:42
     */
    protected Intent loadSubBusiness(MenuChildrenInfo item) {
        Intent intent = new Intent();
        intent=  loadmenu(item);
        return intent;
    }


     private  Intent  loadmenu(MenuChildrenInfo item) {
         Intent intent = new Intent();
         String path = item.getPath();
         int voucherType = Integer.parseInt(path);
         String title = item.getTitle() != null ? item.getTitle() : "";
         String moduleType = item.getComponent() != null ? item.getComponent() : "";
         MenuOutStockModel model = new MenuOutStockModel();
         model.Title = title;
         model.VoucherType = String.valueOf(voucherType);
         String json = GsonUtil.parseModelToJson(model);
         Uri data = Uri.parse(json);

         switch (moduleType.trim()){
             case MenuType.MENU_MODULE_TYPE_OUT_STOCK_OFF_SHELF:
                 intent.setData(data);
                 intent.setClass(mContext, SalesOutstock.class);
                 break;
             case MenuType.MENU_MODULE_TYPE_OUT_STOCK_LCL:
                 intent.setData(data);
                 intent.setClass(mContext, SalesOutStockBox.class);
                 break;
             case MenuType.MENU_MODULE_TYPE_OUT_STOCK_LOADING_TRUCK:
              //   Uri data1 = Uri.parse(String.valueOf(voucherType));
                 intent.setData(data);
                 intent.setClass(mContext, OutstockSalesConfig.class);
                 break;
             case MenuType.MENU_MODULE_TYPE_OUT_STOCK_SHIPMENT_CLOSED:
                 intent.setData(data);
                 intent.setClass(mContext, OutstockOrderColse.class);
                 break;
             case MenuType.MENU_MODULE_TYPE_OUT_STOCK_INDOOR_LOADING_TRUCK:
                 intent.setData(data);
                 intent.setClass(mContext, SalesOutReview.class);
                 break;
             case MenuType.MENU_MODULE_TYPE_OUT_STOCK_OFF_SHELF_TWO:
                 //下架需要列表的
                 if( voucherType==OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION_VALUE){
                     intent.setData(data);
                     intent.setClass(mContext, PurchaseInspectionBill.class);
                 }else
                 {
                     intent.setData(data);
                     intent.setClass(mContext, OutstockRawmaterialActivity.class);
                 }
                 break;
             case MenuType.MENU_MODULE_TYPE_OUT_STOCK_ONE_REVIEW:
                intent.setData(data);
                intent.setClass(mContext, OutstockOneReview.class);
                 break;
         }
//         if (moduleType.trim().equals(MenuType.MENU_MODULE_TYPE_OUT_STOCK_OFF_SHELF)) {
//             //销售出库下架
//             intent.setData(data);
//             intent.setClass(mContext, SalesOutstock.class);
//         } else if (moduleType.trim().equals(MenuType.MENU_MODULE_TYPE_OUT_STOCK_LCL)) {
//             intent.setData(data);
//             intent.setClass(mContext, SalesOutStockBox.class);
//         } else if (moduleType.trim().equals(MenuType.MENU_MODULE_TYPE_OUT_STOCK_LOADING_TRUCK)) {
//             Uri data1 = Uri.parse(String.valueOf(voucherType));
//             intent.setData(data1);
//             intent.setClass(mContext, OutstockSalesConfig.class);
//         } else if (moduleType.trim().equals(MenuType.MENU_MODULE_TYPE_OUT_STOCK_SHIPMENT_CLOSED)) {
//             intent.setData(data);
//             intent.setClass(mContext, OutstockOrderColse.class);
//         } else if (moduleType.trim().equals(MenuType.MENU_MODULE_TYPE_OUT_STOCK_INDOOR_LOADING_TRUCK)) {
//             intent.setData(data);
//             intent.setClass(mContext, SalesOutReview.class);
//         } else if (moduleType.trim().equals(MenuType.MENU_MODULE_TYPE_OUT_STOCK_OFF_SHELF_TWO)) {
//             intent.setData(data);
//             intent.setClass(mContext, OutstockRawmaterialActivity.class);
//         }else if(moduleType.trim().equals(MenuType.MENU_MODULE_TYPE_OUT_STOCK_ONE_REVIEW)) {
//             //一件复核
//         }
         return intent;
     }





}
