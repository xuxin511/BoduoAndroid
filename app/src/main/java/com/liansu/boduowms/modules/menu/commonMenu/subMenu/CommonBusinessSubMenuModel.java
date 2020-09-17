package com.liansu.boduowms.modules.menu.commonMenu.subMenu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.menu.MenuChildrenInfo;
import com.liansu.boduowms.bean.menu.MenuType;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.outstock.Model.MenuOutStockModel;
import com.liansu.boduowms.modules.outstock.SalesOutstock.OutstockOrderColse;
import com.liansu.boduowms.modules.outstock.SalesOutstock.OutstockSalesConfig;
import com.liansu.boduowms.modules.outstock.SalesOutstock.SalesOutStockBox;
import com.liansu.boduowms.modules.outstock.SalesOutstock.SalesOutstock;
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
            if (voucherType == OrderType.IN_HOUSE_STOCK_ORDER_TYPE_BATCH_PRINT_VALUE) {
                icon = R.drawable.b_batch_print;
            } else if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE) {

            }
            //29 销售出库
            else if (voucherType==OrderType.OUT_STOCK_ORDER_TYPE_SALES_OUT_STOCK_VALUE){
                if (moduleType.trim().equals(MenuType.MENU_MODULE_TYPE_OUT_STOCK_OFF_SHELF)){
                    icon=R.drawable.b_out_stock_off_shelf;
                }else if (moduleType.trim().equals(MenuType.MENU_MODULE_TYPE_OUT_STOCK_LCL)){
                    icon=R.drawable.b_set_tray_and_remove_tray;
                }else if (moduleType.trim().equals(MenuType.MENU_MODULE_TYPE_OUT_STOCK_LOADING_TRUCK)){
                    icon=R.drawable.b_loading_truck;
                }else if (moduleType.trim().equals(MenuType.MENU_MODULE_TYPE_OUT_STOCK_SHIPMENT_CLOSED)){
                    icon=R.drawable.b_shipment_approval;
                }
            }
//            switch (voucherType) {
//                //41
//                case OrderType.IN_HOUSE_STOCK_ORDER_TYPE_BATCH_PRINT_VALUE:
//                    icon = R.drawable.b_batch_print;
//                    break;
//                //22
//                case OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE:
//                    icon = R.drawable.b_purchase_storage_icon;
//                    break;
//                //47
//                case OrderType.IN_STOCK_ORDER_TYPE_RANDOM_INSPECTION_STORAGE_VALUE:
//                    icon = R.drawable.b_spot_check_icon;
//                    break;
//                //45
//                case OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_VALUE:
//                    icon = R.drawable.b_instock;
//                    break;
//                //-1
//                case OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_PRINT_VALUE:
//                    icon = R.drawable.b_batch_print;
//                    break;
//



//            }
            if (icon != -1) {
                item.setIcon(icon);
            }

        }
    }
    /**
     * @desc: 载入二级菜单信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/5 16:42
     */
    public List<Map<String, Object>> loadBusinessSubMenu(String businessType) {
//        mItemIconList.clear();
//        mItemNamesList.clear();
//        mMenuList.clear();
//        // "采购验退"   ||采购退货
//        if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION) || businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_RETURN)) {
//            mItemIconList.add(R.drawable.b_out_stock_off_shelf);
//            mItemNamesList.add(mContext.getString(R.string.main_menu_item_off_shelf_scan));
//            mItemIconList.add(R.drawable.b_loading_truck);
//            mItemNamesList.add(mContext.getString(R.string.main_menu_item_loading_truck));
//        } else if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_SALES_OUTSOTCK)) {
//            mItemIconList.add(R.drawable.b_out_stock_off_shelf);
//            mItemNamesList.add(mContext.getString(R.string.main_menu_item_off_shelf_scan));
//            mItemIconList.add(R.drawable.b_set_tray_and_remove_tray);
//            mItemNamesList.add(mContext.getString(R.string.main_menu_item_delivery_lcl));
//            mItemIconList.add(R.drawable.b_loading_truck);
//            mItemNamesList.add(mContext.getString(R.string.main_menu_item_loading_truck));
//            mItemIconList.add(R.drawable.b_shipment_approval);
//            mItemNamesList.add(mContext.getString(R.string.main_menu_item_outstock_lock));
//
//        } else if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_RWMATERIAL_OUTSOTCK)) {
//            mItemIconList.add(R.drawable.b_out_stock_off_shelf);
//            mItemNamesList.add(mContext.getString(R.string.main_menu_item_off_shelf_scan));
//        } else if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_OUTSOURC_OUTSOTCK)) {//委外发料  二级菜单加载
//            mItemIconList.add(R.drawable.b_out_stock_off_shelf);
//            mItemNamesList.add(mContext.getString(R.string.main_menu_item_off_shelf_scan));
//        } else if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_SENDCARSORDER)) {
//            mItemIconList.add(R.drawable.b_out_stock_off_shelf);
//            mItemNamesList.add(mContext.getString(R.string.main_menu_item_off_shelf_scan));
//            mItemIconList.add(R.drawable.b_loading_truck);
//            mItemNamesList.add(mContext.getString(R.string.main_menu_item_other_loading_truck));
//        } else if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_ALLOCATION)) {
//            mItemIconList.add(R.drawable.b_out_stock_off_shelf);
//            mItemNamesList.add(mContext.getString(R.string.main_menu_item_off_shelf_scan));
//        } else if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_REALLOCATION)) {
//            mItemIconList.add(R.drawable.b_out_stock_off_shelf);
//            mItemNamesList.add(mContext.getString(R.string.main_menu_item_off_shelf_scan));
//            mItemIconList.add(R.drawable.b_loading_truck);
//            mItemNamesList.add(mContext.getString(R.string.main_menu_item_other_loading_truck_callback));
//
//        }
//
//        for (int i = 0; i < mItemIconList.size(); i++) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("image", mItemIconList.get(i));
//            map.put("text", mItemNamesList.get(i));
//            mMenuList.add(map);
//        }
//        return mMenuList;
        return null;
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
        String path = item.getPath();
        String title=item.getTitle()!=null?item.getTitle():"";
        int voucherType = Integer.parseInt(path);
        String moduleType=item.getComponent()!=null?item.getComponent():"";
        //29 销售出库
             if (voucherType==OrderType.OUT_STOCK_ORDER_TYPE_SALES_OUT_STOCK_VALUE){
            if (moduleType.trim().equals(MenuType.MENU_MODULE_TYPE_OUT_STOCK_OFF_SHELF)){
                //销售出库下架
                MenuOutStockModel model=new MenuOutStockModel();
                model.Title=title;
                model.VoucherType="29";
                String json = GsonUtil.parseModelToJson(model);
                Uri data = Uri.parse(json);
                intent.setData(data);
                intent.setClass(mContext, SalesOutstock.class);
            }else if (moduleType.trim().equals(MenuType.MENU_MODULE_TYPE_OUT_STOCK_LCL)){
                MenuOutStockModel model = new MenuOutStockModel();
                model.Title = title;
                model.VoucherType = "29";
                String json = GsonUtil.parseModelToJson(model);
                Uri data = Uri.parse(json);
                intent.setData(data);
                intent.setClass(mContext, SalesOutStockBox.class);
            }else if (moduleType.trim().equals(MenuType.MENU_MODULE_TYPE_OUT_STOCK_LOADING_TRUCK)){
                Uri data = Uri.parse("29");
                intent.setData(data);
                intent.setClass(mContext, OutstockSalesConfig.class);
            }else if (moduleType.trim().equals(MenuType.MENU_MODULE_TYPE_OUT_STOCK_SHIPMENT_CLOSED)){
                MenuOutStockModel model = new MenuOutStockModel();
                model.Title = title;
                model.VoucherType = "29";
                String json = GsonUtil.parseModelToJson(model);
                Uri data = Uri.parse(json);
                intent.setData(data);
                intent.setClass(mContext, OutstockOrderColse.class);

            }
        }
//        if (moduleName.equals(mContext.getString(R.string.main_menu_item_off_shelf_scan))) { //下架扫描
//            if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION)) {
//                MenuOutStockModel model = new MenuOutStockModel();
//                model.Title = "采购验退扫描";
//                model.VoucherType = "28";
//                String json = GsonUtil.parseModelToJson(model);
//                Uri data = Uri.parse(json);
//                intent.setData(data);
//                intent.setClass(mContext, PurchaseInspectionBill.class); //验退单
//            } else if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_RETURN)) {
//                // intent.setClass(mContext, PurchaseReturnOffScan.class);//仓退
//                MenuOutStockModel model = new MenuOutStockModel();
//                model.Title = "采购退货下架";
//                model.VoucherType = "27";
//                String json = GsonUtil.parseModelToJson(model);
//                Uri data = Uri.parse(json);
//                intent.setData(data);
//                intent.setClass(mContext, OutstockRawmaterialActivity.class);
//            } else if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_SALES_OUTSOTCK)) {
//                //销售出库下架
//                MenuOutStockModel model = new MenuOutStockModel();
//                model.Title = "销售出库下架";
//                model.VoucherType = "29";
//                String json = GsonUtil.parseModelToJson(model);
//                Uri data = Uri.parse(json);
//                intent.setData(data);
//                intent.setClass(mContext, SalesOutstock.class);
//            } else if (businessType.equals((OrderType.OUT_STOCK_ORDER_TYPE_SENDCARSORDER))) {
//                //派车单
//                MenuOutStockModel model = new MenuOutStockModel();
//                model.Title = "派车单下架";
//                model.VoucherType = "36";
//                String json = GsonUtil.parseModelToJson(model);
//                Uri data = Uri.parse(json);
//                intent.setData(data);
//                intent.setClass(mContext, SalesOutstock.class);
////                Uri data = Uri.parse("46");
////                intent.setData(data);
////                intent.setClass(mContext, OutstockRawmaterialActivity.class);
//            } else if (businessType.equals((OrderType.OUT_STOCK_ORDER_TYPE_REALLOCATION))) {
//
//                //委外发料
////                Uri data = Uri.parse("57");
////                intent.setData(data);
////                intent.setClass(mContext, SalesOutstock.class);
//            }
//            intent.putExtra("BusinessType", businessType);
//        } else if (moduleName.equals(mContext.getString(R.string.main_menu_item_other_loading_truck))) {
//            //市内装车
//            //  intent = intent.setClass(mContext, BaseReviewScan.class);
//            if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_SALES_OUTSOTCK)) {
//                //销售出库复核PackingScanAdapter
//                MenuOutStockModel model = new MenuOutStockModel();
//                model.Title = "销售出库复核";
//                model.VoucherType = "29";
//                String json = GsonUtil.parseModelToJson(model);
//                Uri data = Uri.parse(json);
//                intent.setData(data);
//                intent.setClass(mContext, SalesOutReview.class);
//
//            } else if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_SENDCARSORDER)) {
//                //派车单复核
//                MenuOutStockModel model = new MenuOutStockModel();
//                model.Title = "派车单复核";
//                model.VoucherType = "36";
//                String json = GsonUtil.parseModelToJson(model);
//                Uri data = Uri.parse(json);
//                intent.setData(data);
//                intent.setClass(mContext, SalesOutReview.class);
//            }
//            intent.putExtra("BusinessType", businessType);
//        }
//        //拼箱
//        else if (moduleName.equals((mContext.getString((R.string.main_menu_item_delivery_lcl))))) {
//            //       intent = intent.setClass(mContext, BaseReviewScan.class);
//            if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_SALES_OUTSOTCK)) {
//                //销售出库拼箱
//                MenuOutStockModel model = new MenuOutStockModel();
//                model.Title = "销售出库拼箱";
//                model.VoucherType = "29";
//                String json = GsonUtil.parseModelToJson(model);
//                Uri data = Uri.parse(json);
//                intent.setData(data);
//                intent.setClass(mContext, SalesOutStockBox.class);
//            }
//            intent.putExtra("BusinessType", businessType);
//        } else if (moduleName.equals(mContext.getString(R.string.main_menu_item_off_shelf_scan))) {
//            intent = intent.setClass(mContext, BaseReviewScan.class);
//            intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION);
//
//        } else if (moduleName.equals((mContext.getString(((R.string.main_menu_item_loading_truck)))))) {
//            //发货装车
//            //销售出库装车
//            if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_SALES_OUTSOTCK)) {
//                // MenuOutStockModel model = new MenuOutStockModel();
//                // model.Title = "销售出库复核";
//                //   model.VoucherType = "29";
//                //    String json = GsonUtil.parseModelToJson(model);
//                Uri data = Uri.parse("29");
//                intent.setData(data);
//                intent.setClass(mContext, OutstockSalesConfig.class);
//            } else if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION)) {
//                //采购验退
////                intent = intent.setClass(mContext, BaseReviewScan.class);
////                intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION);
//                MenuOutStockModel model = new MenuOutStockModel();
//                model.Title = "采购验退复核";
//                model.VoucherType = "28";
//                String json = GsonUtil.parseModelToJson(model);
//                Uri data = Uri.parse(json);
//                intent.setData(data);
//                intent.setClass(mContext, SalesOutReview.class);
//            } else if (businessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_RETURN)) {
//                //仓退复核
//                MenuOutStockModel model = new MenuOutStockModel();
//                model.Title = "采购退货复核";
//                model.VoucherType = "27";
//                String json = GsonUtil.parseModelToJson(model);
//                Uri data = Uri.parse(json);
//                intent.setData(data);
//                intent.setClass(mContext, SalesOutReview.class);
//            }
//            intent.putExtra("BusinessType", businessType);
//        } else if (moduleName.equals(mContext.getString(R.string.main_menu_item_other_loading_truck_callback))) {
//            //二阶段回调
//            if (businessType.equals((OrderType.OUT_STOCK_ORDER_TYPE_REALLOCATION))) {
//                MenuOutStockModel model = new MenuOutStockModel();
//                model.Title = "二阶段回调";
//                model.VoucherType = "30";
//                String json = GsonUtil.parseModelToJson(model);
//                Uri data = Uri.parse(json);
//                intent.setData(data);
//                intent.setClass(mContext, SalesOutStockCallback.class);
//                //委外发料
////                Uri data = Uri.parse("57");
////                intent.setData(data);
////                intent.setClass(mContext, SalesOutstock.class);
//            }
//            intent.putExtra("BusinessType", businessType);
//        } else if (moduleName.equals(mContext.getString((R.string.main_menu_item_outstock_lock)))) {
//            MenuOutStockModel model = new MenuOutStockModel();
//            model.Title = "发货结案";
//            model.VoucherType = "29";
//            String json = GsonUtil.parseModelToJson(model);
//            Uri data = Uri.parse(json);
//            intent.setData(data);
//            intent.setClass(mContext, OutstockOrderColse.class);
//            intent.putExtra("BusinessType", businessType);
//        } else {
//            intent = null;
//        }
        return intent;
    }


}
