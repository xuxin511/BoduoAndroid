package com.liansu.boduowms.modules.menu.outboundBusiness;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.menu.MenuChildrenInfo;
import com.liansu.boduowms.bean.menu.MenuInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.debug.DebugModuleData;
import com.liansu.boduowms.modules.menu.IMenuPresenter;
import com.liansu.boduowms.modules.menu.IMenuView;
import com.liansu.boduowms.modules.menu.outboundBusiness.subMenu.OutboundBusinessSubMenu;
import com.liansu.boduowms.modules.outstock.Model.MenuOutStockModel;
import com.liansu.boduowms.modules.outstock.SalesOutstock.OutstockRawmaterialActivity;
import com.liansu.boduowms.modules.outstock.SalesOutstock.SalesOutstock;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.baseReviewScan.BaseReviewScan;
import com.liansu.boduowms.modules.outstock.offScan.DistributionOffShelf;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.GsonUtil;

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
//                    case 30:
//                        itemIconList.add(R.drawable.sales_out_of_stock);
//                        itemNamesList.add(mContext.getString(R.string.main_menu_item_sales_out_of_stock));
//                        break;
                    case 30:
                        itemIconList.add(R.drawable.sales_out_of_stock);
                        itemNamesList.add(mContext.getString(R.string.main_menu_item_sales_out_stock));
                        break;
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
      }
          else if(moduleName.equals(mContext.getString(R.string.main_menu_item_rowmaterial_lcl))) {//原材料出库
          MenuOutStockModel model=new MenuOutStockModel();
          model.Title="原材料出库";
          model.VoucherType="57";
          String json = GsonUtil.parseModelToJson(model);
          Uri data = Uri.parse(json);
          intent.setData(data);
          intent.setClass(mContext, SalesOutstock.class);
      }else if(moduleName.equals(mContext.getString(R.string.main_menu_item_outsourc_lcl))) {//委外装车发料
          MenuOutStockModel model=new MenuOutStockModel();
          model.Title="委外发料下架";
          model.VoucherType="46";
          String json = GsonUtil.parseModelToJson(model);
          Uri data = Uri.parse(json);
          intent.setData(data);
          intent.setClass(mContext, OutstockRawmaterialActivity.class);
          intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_OUTSOURC_OUTSOTCK);
      }
      else if(moduleName.equals(mContext.getString(R.string.main_menu_item_outsotck_Sendcarsorder))) {//派车单
          intent.setClass(mContext, OutboundBusinessSubMenu.class);
          intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_SENDCARSORDER);
       //   Uri data = Uri.parse("46");
//          intent.setData(data);
//          intent.setClass(mContext, OutstockRawmaterialActivity.class);
//          intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_OUTSOURC_OUTSOTCK);
      }else if(moduleName.equals(mContext.getString(R.string.main_menu_item_outsotck_allocation))){
          //一阶段调拨
          MenuOutStockModel model=new MenuOutStockModel();
          model.Title="一阶段调拨下架";
          model.VoucherType="25";
          String json = GsonUtil.parseModelToJson(model);
          Uri data = Uri.parse(json);
          intent.setData(data);
          intent.setClass(mContext, OutstockRawmaterialActivity.class);
          intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_ALLOCATION);
//          intent.setClass(mContext, OutboundBusinessSubMenu.class);
//          intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_ALLOCATION);
      }else if(moduleName.equals(mContext.getString(R.string.main_menu_item_outsotck_reallocation))){
          //二阶段调
          MenuOutStockModel model=new MenuOutStockModel();
          model.Title="二阶段调拨下架";
          model.VoucherType="30";
          String json = GsonUtil.parseModelToJson(model);
          Uri data = Uri.parse(json);
          intent.setData(data);
          intent.setClass(mContext, OutstockRawmaterialActivity.class);
          intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_ALLOCATION);
       //   intent.setClass(mContext, OutboundBusinessSubMenu.class);
         // intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_REALLOCATION);
      }else if(moduleName.equals(mContext.getString(R.string.main_menu_item_outsotck_allocation))) {
          //杂出
//          intent.setClass(mContext, OutboundBusinessSubMenu.class);
//          intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_OTHEROUT);
          MenuOutStockModel model=new MenuOutStockModel();
          model.Title="杂出下架";
          model.VoucherType="55";
          String json = GsonUtil.parseModelToJson(model);
          Uri data = Uri.parse(json);
          intent.setData(data);
          intent.setClass(mContext, OutstockRawmaterialActivity.class);
          intent.putExtra("BusinessType", OrderType.OUT_STOCK_ORDER_TYPE_ALLOCATION);
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


