package com.liansu.boduowms.modules.menu.inHouseManagementMenu;

import android.content.Context;
import android.content.Intent;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.menu.MenuChildrenInfo;
import com.liansu.boduowms.bean.menu.MenuInfo;
import com.liansu.boduowms.debug.DebugModuleData;
import com.liansu.boduowms.modules.inHouseStock.adjustStock.AdjustStock;
import com.liansu.boduowms.modules.inHouseStock.inventory.InventoryHead;
import com.liansu.boduowms.modules.inHouseStock.inventoryMovement.InventoryMovementScan;
import com.liansu.boduowms.modules.inHouseStock.query.QueryStock;
import com.liansu.boduowms.modules.menu.IMenuPresenter;
import com.liansu.boduowms.modules.menu.IMenuView;
import com.liansu.boduowms.modules.pallet.combinePallet.CombinPallet;
import com.liansu.boduowms.modules.pallet.disCombinePallet.DismantlePallet;
import com.liansu.boduowms.utils.function.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/25.
 */
public class InHouseManagementMenuPresenter extends IMenuPresenter {

    IMenuView mMenuView;
    Context   mContext;

    public InHouseManagementMenuPresenter(IMenuView menuView, Context context) {
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
//                    case 34:
//                        itemIconList.add(R.drawable.mobile_warehouse_inventory);
//                        itemNamesList.add(mContext.getString(R.string.main_menu_item_mobile_warehouse_inventory));
//                        break;

                }
            }    //cion和iconName的长度是相同的，这里任选其一都可以


            if (DebugModuleData.isDebugDataStatusOffline()) {
                DebugModuleData.loadInHouseManagementMenuList(mContext, itemIconList, itemNamesList);
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
        if (moduleName.equals(mContext.getString(R.string.main_menu_item_set_tray))) { //组托
            intent.setClass(mContext, CombinPallet.class);
        } else if (moduleName.equals(mContext.getString(R.string.main_menu_item_remove_tray))) { //拆托
            intent.setClass(mContext, DismantlePallet.class);
        } else if (moduleName.equals(mContext.getString(R.string.main_menu_item_mobile_warehouse_inventory))) {
            intent.setClass(mContext, InventoryMovementScan.class);
        } else if (moduleName.equals(mContext.getString(R.string.main_menu_item_inventory_adjustment))) {
            intent.setClass(mContext, AdjustStock.class);
        }else if (moduleName.equals(mContext.getString(R.string.main_menu_item_inventory_inquiry))){
            intent.setClass(mContext, QueryStock.class);
        }else if(moduleName.equals(mContext.getString(R.string.main_menu_item_inventory_scan))){
           //盘点
            intent.setClass(mContext, InventoryHead.class);
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
                if (menuInfo.getTitle().equals("PDA库存")) {
                    List<MenuChildrenInfo> menuChildrenInfos = menuInfo.getChildren();
                    return menuChildrenInfos;
                }
            }
        }
        return null;
    }
}


