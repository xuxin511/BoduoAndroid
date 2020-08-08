package com.liansu.boduowms.bean.order;

/**
 * @ Des: 订单类型
 * @ Created by yangyiqing on 2020/7/13.
 */
public class OrderType {
    public final static String IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE           = "采购入库";
    public final static String IN_STOCK_ORDER_TYPE_OUTSOURCING_STORAGE        = "委外入库";
    public final static String IN_STOCK_ORDER_TYPE_TRANSFER_TO_STORAGE        = "调拨入库";
    public final static String IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE       = "有源杂入";
    public final static String IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE            = "产品入库";
    public final static String IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE       = "销售退货";
    public final static String IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE = "生产退货";
    public final static String OUT_STOCK_ORDER_TYPE_QUALITY_INSPECTION        = "质检";
    public final static String OUT_STOCK_ORDER_TYPE_PICKING_OFF_THE_SHELF     = "拣货下架";
    public final static String OUT_STOCK_ORDER_TYPE_PICKING_OFF_THE_SHELF_ONLY_TRAY  = "拣货下架_组托";
    public final static String OUT_STOCK_ORDER_TYPE_CITY_DELIVERY_AND_LOADING  = "市区发货装车";
    public final static String OUT_STOCK_ORDER_TYPE_OTHER_DELIVERY_AND_LOADING  = "其它发货装车";
    public final static String OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION  = "采购验退";
    public final static String OUT_STOCK_ORDER_TYPE_PURCHASE_RETURN  = "采购退货";
    public final static int IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE = 22;
    public final static int IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_VALUE  = 45;
    public final static int OUT_STOCK_ORDER_TYPE_QUALITY_INSPECTION_VALUE  = 47;
    public final static int IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE_VALUE  = 44;
    public final static int IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE_VALUE  = 26;
    public final static int OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION_VALUE  = 28;
    public final static int OUT_STOCK_ORDER_TYPE_OTHER_DELIVERY_AND_LOADING_VALUE  = 1;


    public final static String OUT_STOCK_ORDER_TYPE_SALES_OUTSOTCK  = "销售出库";


}
