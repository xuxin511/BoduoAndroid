package com.liansu.boduowms.bean.order;

/**
 * @ Des: 订单类型
 * @ Created by yangyiqing on 2020/7/13.
 */
public class OrderType {
    public final static String ORDER_TYPE_NONE                                          = "为选择单据类型";
    public final static String IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE                     = "采购入库";
    public final static String IN_STOCK_ORDER_TYPE_OUTSOURCING_STORAGE                  = "委外入库";
    public final static String IN_STOCK_ORDER_TYPE_TRANSFER_TO_STORAGE                  = "调拨入库";
    public final static String IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE                 = "有源杂入";
    public final static String IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE                      = "产品入库";
    public final static String IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE                 = "销售退货";
    public final static String IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE           = "工单退料";
    public final static String IN_STOCK_ORDER_TYPE_BATCH_PRINTING                       = "批量打印";
    public final static String OUT_STOCK_ORDER_TYPE_QUALITY_INSPECTION                  = "质检";
    public final static String OUT_STOCK_ORDER_TYPE_PICKING_OFF_THE_SHELF               = "拣货下架";
    public final static String OUT_STOCK_ORDER_TYPE_PICKING_OFF_THE_SHELF_ONLY_TRAY     = "拣货下架_组托";
    public final static String OUT_STOCK_ORDER_TYPE_CITY_DELIVERY_AND_LOADING           = "市区发货装车";
    public final static String OUT_STOCK_ORDER_TYPE_OTHER_DELIVERY_AND_LOADING          = "其它发货装车";
    public final static String OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION                 = "采购验退";
    public final static String OUT_STOCK_ORDER_TYPE_PURCHASE_PRODUCT                    = "成品验退";
    public final static String OUT_STOCK_ORDER_TYPE_PURCHASE_SALE                       = "销售验退";
    public final static String OUT_STOCK_ORDER_TYPE_PURCHASE_RETURN                     = "采购退货";
    public final static String IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_NAME                = "采购订单";
    public final static String IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_NAME                 = "工单";
    public final static String IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE_NAME            = "杂入单";
    public final static String IN_STOCK_ORDER_TYPE_TRANSFER_TO_STORAGE_NAME             = "调拨入";
    public final static String OUT_STOCK_ORDER_TYPE_SALES_OUTSTOCK                      = "销售出库";
    public final static String OUT_STOCK_ORDER_TYPE_RWMATERIAL_OUTSOTCK                 = "原材料发货";
    public final static String OUT_STOCK_ORDER_TYPE_OUTSOURC_OUTSOTCK                   = "委外发料";
    public final static String OUT_STOCK_ORDER_TYPE_SENDCARSORDER                       = "派车单";
    public final static String OUT_STOCK_ORDER_TYPE_ALLOCATION                          = "一阶段调拨出";
    public final static String OUT_STOCK_ORDER_TYPE_REALLOCATION                        = "二阶段调拨出";
    public final static String OUT_STOCK_ORDER_TYPE_OTHEROUT                            = "杂出";
    //Sendcarsorder
    public final static String IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY_ADJUSTMENT           = "库存调整";
    public final static int    IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE               = 22;
    public final static int    IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_VALUE                = 45;
    public final static int    IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_PRINT_VALUE          = 68; //成品入库打印
    public final static int    OUT_STOCK_ORDER_TYPE_QUALITY_INSPECTION_VALUE            = 47;
    public final static int    IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE_VALUE           = 44;
    public final static int    IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE_VALUE           = 26;
    public final static int    IN_STOCK_ORDER_TYPE_NO_SOURCE_SALES_RETURN_STORAGE_VALUE = 69; //无源销售退货
    //    public final static int    IN_STOCK_ORDER_TYPE_TRANSFER_TO_STORAGE_VALUE         = 24;
    public final static int    IN_STOCK_ORDER_TYPE_BATCH_PRINTING_VALUE                 = 33;
    public final static int    IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY_ADJUSTMENT_VALUE     = 35;
    public final static int    IN_STOCK_ORDER_TYPE_TWO_STAGE_TRANSFER_TO_STORAGE_VALUE  = 24;  //二阶段调拨入库
    public final static int    IN_STOCK_ORDER_TYPE_ONE_STAGE_TRANSFER_TO_STORAGE_VALUE  = 58;  //一阶段调拨入库
    public final static int    IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE_VALUE     = 52;  //工单退料入库
    public final static int    IN_STOCK_ORDER_TYPE_NO_SOURCE_OTHER_STORAGE_VALUE        = 66;  //无源入库
    public final static int    IN_STOCK_ORDER_TYPE_RANDOM_INSPECTION_STORAGE_VALUE      = 47;  //到货抽检
    public final static int    IN_STOCK_ORDER_TYPE_OUTSOURCING_STORAGE_VALUE            = -1;  //委外入库
    public final static int    IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY_MOVEMENT_VALUE       = 34;  //移库
    public final static int    IN_HOUSE_STOCK_ORDER_TYPE_BATCH_PRINT_VALUE              = 41;//批量打印
    public final static int    IN_STOCK_ORDER_TYPE_QUALITY_INSPECTION_VALUE             = 70; //质检合格
    public final static int    IN_HOUSE_STOCK_ORDER_TYPE_COMBINE_PALLET_VALUE           = 71;//拼托
    public final static int    IN_STOCK_ORDER_TYPE_SALES_RETURN_PRINT_STORAGE_VALUE     = 72;//销售退货打印
    public final static int    OUT_STOCK_ORDER_TYPE_SALES_OUT_STOCK_VALUE               = 29;//销售出库
    public final static int    OUT_STOCK_ORDER_TYPE_PURCHASE_INSPECTION_VALUE           = 28;
    public final static int    OUT_STOCK_ORDER_TYPE_PURCHASE_PRODUCT_VALUE              = 61;
    public final static int    OUT_STOCK_ORDER_TYPE_PURCHASE_RETURN_VALUE               = 27;
    public final static int    OUT_STOCK_ORDER_TYPE_PURCHASE_SALERETURN_VALUE           = 62;
    public final static int    OUT_STOCK_ORDER_TYPE_PURCHASE_ROWMATERIAL_VALUE          = 46;
    public final static int    OUT_STOCK_ORDER_TYPE_PURCHASE_OUTSOURC_VALUE             = 57;
    public final static int    OUT_STOCK_ORDER_TYPE_PURCHASE_SENDCARSORDER_VALUE        = 36;
    public final static int    OUT_STOCK_ORDER_TYPE_PURCHASE_ALLOCATION_VALUE           = 25;
    public final static int OUT_STOCK_ORDER_TYPE_CONSIGNMENT_PLUS_BILL           = 76;   //托运加单
    public final static int OUT_STOCK_ORDER_TYPE_OUTSTOCK_ORDERSELECT          = 79;   //发货通知单查询


    public final static int OUT_STOCK_ORDER_TYPE_OTHER_DELIVERY_AND_LOADING_VALUE = 1;

    public final static int IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY_MOVEMENT  = 34;//移库
    public final static int IN_HOUSE_STOCK_ORDER_TYPE_ADJUST_STOCK        = 35;//库存调整
    public final static int IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY_INQUIRY   = 38;//库存查询
    public final static int IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY           = 67;//盘点
    public final static int IN_HOUSE_STOCK_ORDER_TYPE_OPEN_INVENTORY      = 73;//明盘
    public final static int IN_HOUSE_STOCK_ORDER_TYPE_BEGINNING_INVENTORY = 74;//期初盘点
    public final static int IN_HOUSE_STOCK_ORDER_TYPE_REPLENISHMENT       = 75;//补货
    public final static int IN_HOUSE_STOCK_ORDER_TYPE_REPRINT_PALLET_LABEL       = 77;//标签补打
    public final static int ORDER_TYPE_NONE_VALUE                         = -1;//未选择单据类型


}
