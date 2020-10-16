package com.liansu.boduowms.bean.menu;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/9/16.
 */
public class MenuType {
    public static final int                    MENU_TYPE_NONE           = -1; //未选择菜单类型
    public static final int                    MENU_TYPE_IN_STOCK       = 1; //入库类型
    public static final int                    MENU_TYPE_OUT_STOCK      = 2; //出库类型
    public static final int                    MENU_TYPE_IN_HOUSE_STOCK = 3; //库内类型
    public static final int                    MENU_STYLE_TYPE_SECONDARY_MENU=2 ; //二级菜单
    public static final int                    MENU_STYLE_TYPE_THREE_LEVEL_MENU=3 ; //三级菜单
    //出库功能标识
    public static final String                 MENU_MODULE_TYPE_OUT_STOCK_OFF_SHELF = "OUT_STOCK_OFF_SHELF"; //下架扫描
    public static final String                 MENU_MODULE_TYPE_OUT_STOCK_LCL = "OUT_STOCK_LCL"; //发货拼箱
    public static final String                 MENU_MODULE_TYPE_OUT_STOCK_LOADING_TRUCK = "OUT_STOCK_LOADING_TRUCK"; //发货装车
    public static final String                 MENU_MODULE_TYPE_OUT_STOCK_SHIPMENT_CLOSED = "OUT_STOCK_SHIPMENT_CLOSED"; //发货结案
    public static final String                 MENU_MODULE_TYPE_OUT_STOCK_INDOOR_LOADING_TRUCK = "OUT_STOCK_INDOOR_LOADING_TRUCK"; //室内装车
    public static final String                 MENU_MODULE_TYPE_OUT_STOCK_OFF_SHELF_TWO  = "OUT_STOCK_OFF_SHELF_TWO"; //下架（ 整托）
    public static final String                 MENU_MODULE_TYPE_NONE  = "MENU_MODULE_TYPE_NONE"; //
    public static final String                 MENU_MODULE_TYPE_OUT_STOCK_ONE_REVIEW  = "OUT_STOCK_ONE_REVIEW"; //一键复核
    public static final String                 MENU_MODULE_TYPE_OUT_STOCK_CUSTOMER_MENTION  = "OUT_STOCK_CUSTOMER_MENTION "; //客户自提

    //库内功能标识
    public static final String                 MENU_MODULE_TYPE_IN_HOUSE_STOCK_INVENTORY_LIST  = "IN_HOUSE_STOCK_INVENTORY_LIST"; //盘点列表
    public static final String                 MENU_MODULE_TYPE_IN_HOUSE_STOCK_INVENTORY_SCAN  = "IN_HOUSE_STOCK_INVENTORY_SCAN"; //盘点扫描
    public static final String                 MENU_MODULE_TYPE_IN_HOUSE_STOCK_OPEN_INVENTORY_LIST  = "IN_HOUSE_STOCK_OPEN_INVENTORY_LIST"; //明盘列表
    public static final String                 MENU_MODULE_TYPE_IN_HOUSE_STOCK_OPEN_INVENTORY_SCAN  = "IN_HOUSE_STOCK_OPEN_INVENTORY_SCAN"; //明盘扫描
    public static final String                 MENU_MODULE_TYPE_IN_HOUSE_STOCK_INVENTORY_PALLET_PRINT  = "IN_HOUSE_STOCK_INVENTORY_PALLET_PRINT"; //托盘蓝牙打印


}
