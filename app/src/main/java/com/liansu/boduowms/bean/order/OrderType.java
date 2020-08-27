package com.liansu.boduowms.bean.order;

/**
 * @ Des: 订单类型
 * @ Created by yangyiqing on 2020/7/13.
 */
public class OrderType {
    public final static String IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE                  = "采购入库";
    public final static String IN_STOCK_ORDER_TYPE_OUTSOURCING_STORAGE               = "委外入库";
    public final static String IN_STOCK_ORDER_TYPE_TRANSFER_TO_STORAGE               = "调拨入库";
    public final static String IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE              = "有源杂入";
    public final static String IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE                   = "产品入库";
    public final static String IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE              = "销售退货";
    public final static String IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE        = "生产退货";
    public final static String IN_STOCK_ORDER_TYPE_BATCH_PRINTING                    = "批量打印";
    public final static String OUT_STOCK_ORDER_TYPE_QUALITY_INSPECTION               = "质检";
    public final static String OUT_STOCK_ORDER_TYPE_PICKING_OFF_THE_SHELF            = "拣货下架";
    public final static String OUT_STOCK_ORDER_TYPE_PICKING_OFF_THE_SHELF_ONLY_TRAY  = "拣货下架_组托";
    public final static String OUT_STOCK_ORDER_TYPE_CITY_DELIVERY_AND_LOADING        = "市区发货装车";
    public final static String OUT_STOCK_ORDER_TYPE_OTHER_DELIVERY_AND_LOADING       = "其它发货装车";
    public final static String OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION              = "采购验退";
    public final static String OUT_STOCK_ORDER_TYPE_PURCHASE_RETURN                  = "采购退货";
    public final static int    IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE            = 22;
    public final static int    IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_VALUE             = 45;
    public final static int    OUT_STOCK_ORDER_TYPE_QUALITY_INSPECTION_VALUE         = 47;
    public final static int    IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE_VALUE        = 44;
    public final static int    IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE_VALUE        = 26;
//    public final static int    IN_STOCK_ORDER_TYPE_TRANSFER_TO_STORAGE_VALUE         = 24;
    public final static int    IN_STOCK_ORDER_TYPE_BATCH_PRINTING_VALUE              = 33;
    public final static int    OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION_VALUE        = 28;
    public final static int    OUT_STOCK_ORDER_TYPE_OTHER_DELIVERY_AND_LOADING_VALUE = 1;
    public final static String IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_NAME             = "采购订单";
    public final static String IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_NAME              = "工单";
    public final static String IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE_NAME         = "杂入单";
    public final static String IN_STOCK_ORDER_TYPE_TRANSFER_TO_STORAGE_NAME          = "调拨入";

    public final static String OUT_STOCK_ORDER_TYPE_SALES_OUTSOTCK = "销售出库";

    public final static String OUT_STOCK_ORDER_TYPE_RWMATERIAL_OUTSOTCK                = "原材料发货";
    public final static String OUT_STOCK_ORDER_TYPE_OUTSOURC_OUTSOTCK                  = "委外发料";
    public final static String OUT_STOCK_ORDER_TYPE_SENDCARSORDER                      = "派车单";
    public final static String OUT_STOCK_ORDER_TYPE_ALLOCATION                         = "一阶段调拨出";
    public final static String OUT_STOCK_ORDER_TYPE_REALLOCATION                       = "二阶段调拨出";
    public final static String OUT_STOCK_ORDER_TYPE_OTHEROUT                           = "杂出";
    //Sendcarsorder
    public final static String IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY_ADJUSTMENT          = "库存调整";
    public final static int    IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY_ADJUSTMENT_VALUE    = 35;
    public final static int    IN_STOCK_ORDER_TYPE_TWO_STAGE_TRANSFER_TO_STORAGE_VALUE = 24;  //二阶段调拨入库
    public final static int    IN_STOCK_ORDER_TYPE_ONE_STAGE_TRANSFER_TO_STORAGE_VALUE = 58;  //一阶段调拨入库

}
