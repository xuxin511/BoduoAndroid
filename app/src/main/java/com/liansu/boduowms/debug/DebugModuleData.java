package com.liansu.boduowms.debug;

import android.content.Context;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.IBaseOrderScanView;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.modules.print.linkos.PrintType;
import com.liansu.boduowms.utils.SharePreferUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/25.
 */
public class DebugModuleData {
    public final static int DEBUG_DATA_STATUS_OFFLINE = 0; //服务器离线数据展示
    public final static int DEBUG_DATA_STATUS_ONLINE  = 1;  //服务器在线数据展示

    public static void setDebugDataStatus(Context context, int debugLevel) {
        SharePreferUtil.SetDebugDataStatusShare(context, debugLevel);
    }

    public static boolean isDebugDataStatusOffline() {
        int status = BaseApplication.mDebugDataStatus;
        if (status == DEBUG_DATA_STATUS_OFFLINE) {
            return true;
        } else {
            return false;
        }

    }


    /**
     * @desc: 离线加载入库业务菜单
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/25 19:50
     */
    public static void loadStorageBusinessMenuList(Context context, ArrayList<Integer> itemIconList, ArrayList<String> itemNamesList) {
        itemIconList.add(R.drawable.b_batch_print);
        itemNamesList.add(context.getString(R.string.main_menu_item_batch_printing));
        itemIconList.add(R.drawable.b_purchase_storage_icon);
        itemNamesList.add(context.getString(R.string.main_menu_item_purchase_storage));
        itemIconList.add(R.drawable.b_spot_check_icon);
        itemNamesList.add(context.getString(R.string.main_menu_item_spot_check));
        itemIconList.add(R.drawable.b_instock);
        itemNamesList.add(context.getString(R.string.main_menu_item_production_storage));
        itemIconList.add(R.drawable.b_batch_print);
        itemNamesList.add(context.getString(R.string.main_menu_item_production_storage_pallet_no_print));
        itemIconList.add(R.drawable.b_work_order_return);
        itemNamesList.add(context.getString(R.string.main_menu_item_production_returns));
        itemIconList.add(R.drawable.b_transfer_to_storage);
        itemNamesList.add(context.getString(R.string.main_menu_item_two_stage_transfer_to_storage));
        itemIconList.add(R.drawable.b_transfer_to_storage);
        itemNamesList.add(context.getString(R.string.main_menu_item_one_stage_transfer_to_storage));
        itemIconList.add(R.drawable.b_outsourcing);
        itemNamesList.add(context.getString(R.string.main_menu_item_outsourcing));
        itemIconList.add(R.drawable.b_instock);
        itemNamesList.add(context.getString(R.string.main_menu_item_active_other_storage));
        itemIconList.add(R.drawable.b_scan_other_storage);
        itemNamesList.add(context.getString(R.string.main_menu_item_active_scan_other_storage));
        itemIconList.add(R.drawable.b_sales_return);
        itemNamesList.add(context.getString(R.string.main_menu_item_active_sales_return));
        itemIconList.add(R.drawable.b_qualified);
        itemNamesList.add(context.getString(R.string.main_menu_item_quality_inspection));
//        itemIconList.add(R.drawable.production_storage);
//        itemNamesList.add(context.getString(R.string.main_menu_item_product_return_print_storage));


    }

    /**
     * @desc: 离线加载库内业务菜单
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/25 19:50
     */
    public static void loadInHouseManagementMenuList(Context context, ArrayList<Integer> itemIconList, ArrayList<String> itemNamesList) {
        itemIconList.add(R.drawable.b_mobile_warehouse_inventory);
        itemNamesList.add(context.getString(R.string.main_menu_item_mobile_warehouse_inventory));
//        itemIconList.add(R.drawable.set_tray_and_remove_tray);
//        itemNamesList.add(context.getString(R.string.main_menu_item_set_tray));
//        itemIconList.add(R.drawable.set_tray_and_remove_tray);
//        itemNamesList.add(context.getString(R.string.main_menu_item_remove_tray));
        itemIconList.add(R.drawable.b_inventory_adjustment);
        itemNamesList.add(context.getString(R.string.main_menu_item_inventory_adjustment));
        itemIconList.add(R.drawable.b_inventory_scan);
        itemNamesList.add(context.getString(R.string.main_menu_item_inventory_scan));
        itemIconList.add(R.drawable.b_inventory_inquiry);
        itemNamesList.add(context.getString(R.string.main_menu_item_inventory_inquiry));

    }

    /**
     * @desc: 离线加载出库业务菜单
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/25 19:50
     */
    public static void loadOutboundBusinessMenuList(Context context, ArrayList<Integer> itemIconList, ArrayList<String> itemNamesList) {
//        itemIconList.add(R.drawable.pick_up_and_out_of_warehouse);
//        itemNamesList.add(context.getString(R.string.main_menu_item_pick_up_and_out_of_warehouse));

//        itemIconList.add(R.drawable.administrative_use);
//        itemNamesList.add(context.getString(R.string.main_menu_item_administrative_use));
//        itemIconList.add(R.drawable.purchase_returns);
//        itemNamesList.add(context.getString(R.string.main_menu_item_off_shelf_scan));
//        itemIconList.add(R.drawable.other_outbound);
//        itemNamesList.add(context.getString(R.string.main_menu_item_other_outbound));
//        itemIconList.add(R.drawable.other_outbound);
        itemNamesList.add(context.getString(R.string.main_menu_item_purchase_inspection));
        itemIconList.add(R.drawable.b_outstock_return);
        itemNamesList.add(context.getString(R.string.main_menu_item_purchase_sale));
        itemIconList.add(R.drawable.b_outstock_return);
        itemNamesList.add(context.getString(R.string.main_menu_item_purchase_product));
        itemIconList.add(R.drawable.b_outstock_return);
        itemNamesList.add(context.getString(R.string.main_menu_item_purchase_return));
        itemIconList.add(R.drawable.b_outsourcing_dispatch);
//        itemNamesList.add(context.getString(R.string.main_menu_item_outsourcing_dispatch));
//        itemIconList.add(R.drawable.transfer_out_of_warehouse);
//        itemNamesList.add(context.getString(R.string.main_menu_item_transfer_out_of_warehouse));
//        itemIconList.add(R.drawable.loading_truck);
//        itemNamesList.add(context.getString(R.string.main_menu_item_loading_truck));
//        itemIconList.add(R.drawable.sales_out_of_stock);
//        itemNamesList.add(context.getString(R.string.main_menu_item_sales_out_of_stock));
//        itemIconList.add(R.drawable.loading_truck);
//        itemNamesList.add(context.getString(R.string.main_menu_item_other_loading_truck));
//        itemIconList.add(R.drawable.delivery_lcl);
//        itemNamesList.add(context.getString(R.string.main_menu_item_delivery_lcl));
        //原材料出库
        itemIconList.add(R.drawable.b_stock_out);
        itemNamesList.add(context.getString(R.string.main_menu_item_rowmaterial_lcl));
        itemIconList.add(R.drawable.b_stock_out);
        itemNamesList.add(context.getString(R.string.main_menu_item_outsourc_lcl));
        itemIconList.add(R.drawable.b_stock_out);
        itemNamesList.add(context.getString(R.string.main_menu_item_outsotck_Sendcarsorder));
        itemIconList.add(R.drawable.b_transfer_out_to_storage);
        itemNamesList.add(context.getString(R.string.main_menu_item_outsotck_allocation));
        itemIconList.add(R.drawable.b_transfer_out_to_storage);
        itemNamesList.add(context.getString(R.string.main_menu_item_outsotck_reallocation));
        itemIconList.add(R.drawable.b_other_out);
        itemNamesList.add(context.getString(R.string.main_menu_item_outsotck_otherout));
        itemIconList.add(R.drawable.b_allocation_approval);
        itemNamesList.add(context.getString(R.string.main_menu_item_outsotck_reallocation_callback));
        itemIconList.add(R.drawable.b_administrative_requisition);
        itemNamesList.add(context.getString(R.string.main_menu_item_outsotck_adcollection));
    }

    /**
     * @desc: 获取采购订单离线表头数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 18:51
     */
    public static ArrayList<OrderHeaderInfo> loadReceiptHeaderList(String type) {
        String erpvoucherNo = "";
        if (type.equals(OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE)) {
            erpvoucherNo = "采购订单";
        } else if (type.equals(OrderType.IN_STOCK_ORDER_TYPE_OUTSOURCING_STORAGE)) {
            erpvoucherNo = "委外入库";
        } else if (type.equals(OrderType.IN_STOCK_ORDER_TYPE_TRANSFER_TO_STORAGE)) {
            erpvoucherNo = "调拨入库";
        } else if (type.equals(OrderType.IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE)) {
            erpvoucherNo = "有源杂入";
        }
        ArrayList<OrderHeaderInfo> list = new ArrayList<>();
        OrderHeaderInfo bean1 = new OrderHeaderInfo();
        bean1.setErpvoucherno("CG20200627001");
        bean1.setErpvouchertype(erpvoucherNo);
        bean1.setStrongholdName("组织1");
        bean1.setSuppliername("供应商1");
        list.add(bean1);
        OrderHeaderInfo bean2 = new OrderHeaderInfo();
        bean2.setErpvoucherno("CG20200627002");
        bean2.setErpvouchertype(erpvoucherNo);
        bean2.setStrongholdName("组织1");
        bean2.setSuppliername("供应商1");
        list.add(bean2);
        OrderHeaderInfo bean3 = new OrderHeaderInfo();
        bean3.setErpvoucherno("CG20200627003");
        bean3.setErpvouchertype(erpvoucherNo);
        bean3.setStrongholdName("组织1");
        bean3.setSuppliername("供应商1");
        list.add(bean3);
        OrderHeaderInfo bean4 = new OrderHeaderInfo();
        bean4.setErpvoucherno("CG20200627004");
        bean4.setErpvouchertype(erpvoucherNo);
        bean4.setStrongholdName("组织1");
        bean4.setSuppliername("供应商1");
        list.add(bean4);
        OrderHeaderInfo bean5 = new OrderHeaderInfo();
        bean5.setErpvoucherno("CG20200627005");
        bean5.setErpvouchertype(erpvoucherNo);
        bean5.setStrongholdName("组织1");
        bean5.setSuppliername("供应商1");
        list.add(bean5);
//
        return list;
    }

    /**
     * @desc: 获取采购订单离线表体数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 22:52
     */
    public static ArrayList<OrderDetailInfo> loadReceiptScanDetailList(IBaseOrderScanView view, OrderHeaderInfo receipt_model) {
        ArrayList<OrderDetailInfo> list = new ArrayList<>();
        OrderDetailInfo receiptDetailModel = new OrderDetailInfo();
        receiptDetailModel.setMaterialno("2020010012");
        receiptDetailModel.setBatchno("20200702");
        receiptDetailModel.setMaterialdesc("物料名称1");
        receiptDetailModel.setScanqty(0);
        receiptDetailModel.setRemainqty(100);
        receiptDetailModel.setRowno("1");
        receiptDetailModel.setVoucherqty(200);
        receiptDetailModel.setStrongholdname("1234");
        receiptDetailModel.setPackQty(50);

        list.add(receiptDetailModel);
        OrderDetailInfo receiptDetailModel2 = new OrderDetailInfo();
        receiptDetailModel2.setMaterialno("2020010013");
        receiptDetailModel2.setBatchno("20200703");
        receiptDetailModel2.setMaterialdesc("物料名称2");
        receiptDetailModel2.setScanqty(0);
        receiptDetailModel2.setRemainqty(180);
        receiptDetailModel2.setRowno("2");
        receiptDetailModel2.setVoucherqty(230);
        receiptDetailModel2.setStrongholdname("1234");
        receiptDetailModel2.setPackQty(0);
        list.add(receiptDetailModel2);
        OrderDetailInfo receiptDetailModel3 = new OrderDetailInfo();
        receiptDetailModel3.setMaterialno("物料编码3");
        receiptDetailModel3.setMaterialdesc("物料名称3");
        receiptDetailModel3.setScanqty(0);
        receiptDetailModel3.setRemainqty(50);
        list.add(receiptDetailModel3);
        view.bindListView(list);
        view.setOrderHeaderInfo(receipt_model);
        return list;
    }

    /**
     * @desc: 获取采购订单离线表体数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 22:52
     */
    public static List<OrderDetailInfo> loadReceiptScanDetailList() {
        ArrayList<OrderDetailInfo> list = new ArrayList<>();
        OrderDetailInfo receiptDetailModel = new OrderDetailInfo();
        receiptDetailModel.setMaterialno("物料编码1");
        receiptDetailModel.setMaterialdesc("物料名称1");
        receiptDetailModel.setScanqty(100);
        receiptDetailModel.setRemainqty(100);
        list.add(receiptDetailModel);
        OrderDetailInfo receiptDetailModel2 = new OrderDetailInfo();
        receiptDetailModel2.setMaterialno("物料编码2");
        receiptDetailModel2.setMaterialdesc("物料名称2");
        receiptDetailModel2.setScanqty(50);
        receiptDetailModel2.setRemainqty(180);
        list.add(receiptDetailModel2);
        OrderDetailInfo receiptDetailModel3 = new OrderDetailInfo();
        receiptDetailModel3.setMaterialno("物料编码3");
        receiptDetailModel3.setMaterialdesc("物料名称3");
        receiptDetailModel3.setScanqty(0);
        receiptDetailModel3.setRemainqty(50);
        list.add(receiptDetailModel3);
        return list;
    }

    /**
     * @desc: 获取上架任务表头 离线数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 18:51
     */
    public static ArrayList<OrderHeaderInfo> loadUpshelfHeaderList() {
        ArrayList<OrderHeaderInfo> list = new ArrayList<>();
        OrderHeaderInfo inStockTaskInfoModel = new OrderHeaderInfo();
        inStockTaskInfoModel.setErpvoucherno("20200627001");
        inStockTaskInfoModel.setVoucherno("TASK20200627001");
        inStockTaskInfoModel.setErpvouchertype("");
        inStockTaskInfoModel.setStrongholdName("组织1");
        inStockTaskInfoModel.setDepartmentname("部门1");
        list.add(inStockTaskInfoModel);
        OrderHeaderInfo inStockTaskInfoModel2 = new OrderHeaderInfo();
        inStockTaskInfoModel2.setErpvoucherno("20200627002");
        inStockTaskInfoModel2.setVoucherno("TASK20200627002");
        inStockTaskInfoModel2.setErpvouchertype("");
        inStockTaskInfoModel2.setStrongholdName("组织1");
        inStockTaskInfoModel2.setDepartmentname("部门1");
        list.add(inStockTaskInfoModel2);
        OrderHeaderInfo inStockTaskInfoModel3 = new OrderHeaderInfo();
        inStockTaskInfoModel3.setErpvoucherno("2020062703");
        inStockTaskInfoModel3.setVoucherno("TASK20200627003");
        inStockTaskInfoModel3.setErpvouchertype("");
        inStockTaskInfoModel3.setStrongholdName("组织2");
        inStockTaskInfoModel3.setDepartmentname("部门2");
        list.add(inStockTaskInfoModel3);
        OrderHeaderInfo inStockTaskInfoModel4 = new OrderHeaderInfo();
        inStockTaskInfoModel4.setErpvoucherno("2020062704");
        inStockTaskInfoModel4.setVoucherno("TASK20200627004");
        inStockTaskInfoModel4.setErpvouchertype("");
        inStockTaskInfoModel4.setStrongholdName("组织4");
        inStockTaskInfoModel4.setDepartmentname("部门4");
        list.add(inStockTaskInfoModel4);
        OrderHeaderInfo inStockTaskInfoModel5 = new OrderHeaderInfo();
        inStockTaskInfoModel5.setErpvoucherno("2020062705");
        inStockTaskInfoModel5.setVoucherno("TASK20200627005");
        inStockTaskInfoModel5.setErpvouchertype("");
        inStockTaskInfoModel5.setStrongholdName("组织5");
        inStockTaskInfoModel5.setDepartmentname("部门5");
        list.add(inStockTaskInfoModel5);
        return list;
    }

    /**
     * @desc: 获取上架任务表体 离线数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 18:51
     */
    public static ArrayList<OrderDetailInfo> loadUpshelfDetailList() {
        ArrayList<OrderDetailInfo> list = new ArrayList<>();

        OrderDetailInfo inStockTaskDetailsInfo_model = new OrderDetailInfo();
        inStockTaskDetailsInfo_model.setMaterialno("物料编码1");
        inStockTaskDetailsInfo_model.setMaterialdesc("物料名称1");
        inStockTaskDetailsInfo_model.setScanqty((float) 100);
        inStockTaskDetailsInfo_model.setRemainqty((float) 0);
        inStockTaskDetailsInfo_model.setReceiveqty((float) 100);
        list.add(inStockTaskDetailsInfo_model);
        OrderDetailInfo inStockTaskDetailsInfo_model2 = new OrderDetailInfo();
        inStockTaskDetailsInfo_model2.setMaterialno("物料编码2");
        inStockTaskDetailsInfo_model2.setMaterialdesc("物料名称2");
        inStockTaskDetailsInfo_model2.setReceiveqty((float) 50);
        inStockTaskDetailsInfo_model2.setRemainqty((float) 180);
        inStockTaskDetailsInfo_model2.setVoucherqty((float) 230);
        list.add(inStockTaskDetailsInfo_model2);
        OrderDetailInfo inStockTaskDetailsInfo_model3 = new OrderDetailInfo();
        inStockTaskDetailsInfo_model3.setMaterialno("物料编码3");
        inStockTaskDetailsInfo_model3.setMaterialdesc("物料名称3");
        inStockTaskDetailsInfo_model3.setScanqty((float) 0);
        inStockTaskDetailsInfo_model3.setRemainqty((float) 50);
        inStockTaskDetailsInfo_model3.setReceiveqty((float) 0);
        list.add(inStockTaskDetailsInfo_model3);

        return list;
    }


    /**
     * @desc: 下架扫描明细 离线数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/28 22:27
     */
    public static ArrayList<OrderDetailInfo> loadDistributionOffShelfList() {
        ArrayList<OrderDetailInfo> list = new ArrayList<>();
        final OrderDetailInfo outStockTaskDetailsInfoModel = new OrderDetailInfo();
        outStockTaskDetailsInfoModel.setMaterialno("物料1");
        outStockTaskDetailsInfoModel.setScanqty((float) 100);
        outStockTaskDetailsInfoModel.setRemainqty((float) 100);
        outStockTaskDetailsInfoModel.setBatchno("批次1");
//        outStockTaskDetailsInfoModel("推荐库位1,推荐库位2,推荐库位3");
        outStockTaskDetailsInfoModel.setMaterialdesc("物料描述1");

        list.add(outStockTaskDetailsInfoModel);
        final OrderDetailInfo outStockTaskDetailsInfoModel2 = new OrderDetailInfo();
//        outStockTaskDetailsInfoModel2.setMaterialno("物料2");
//        outStockTaskDetailsInfoModel2.setScanQty((float) 50);
//        outStockTaskDetailsInfoModel2.setRemainQty((float) 100);
//        outStockTaskDetailsInfoModel2.setBatchNo("批次2");
//        outStockTaskDetailsInfoModel2.setAreaNo("推荐库位1,推荐库位2,推荐库位3");
//        outStockTaskDetailsInfoModel2.setMaterialDesc("物料描述2");
        list.add(outStockTaskDetailsInfoModel2);
        final OrderDetailInfo outStockTaskDetailsInfoModel3 = new OrderDetailInfo();
        outStockTaskDetailsInfoModel3.setMaterialno("物料3");
//        outStockTaskDetailsInfoModel3.setScanQty((float) 800);
//        outStockTaskDetailsInfoModel3.setRemainQty((float) 1000);
//        outStockTaskDetailsInfoModel3.setBatchNo("批次3");
//        outStockTaskDetailsInfoModel3.setAreaNo("推荐库位1,推荐库位2,推荐库位3");
//        outStockTaskDetailsInfoModel3.setMaterialDesc("物料描述3");
        list.add(outStockTaskDetailsInfoModel3);

        final OrderDetailInfo outStockTaskDetailsInfoModel4 = new OrderDetailInfo();
//        outStockTaskDetailsInfoModel4.setMaterialNo("物料4");
//        outStockTaskDetailsInfoModel4.setScanQty((float) 0);
//        outStockTaskDetailsInfoModel4.setRemainQty((float) 120);
//        outStockTaskDetailsInfoModel4.setBatchNo("批次4");
//        outStockTaskDetailsInfoModel4.setAreaNo("推荐库位1,推荐库位2,推荐库位3");
//        outStockTaskDetailsInfoModel4.setMaterialDesc("物料描述4");
        list.add(outStockTaskDetailsInfoModel4);
        return list;
    }

    /**
     * @desc: 拼箱扫描明细 离线数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/28 22:27
     */
    public static ArrayList<OrderDetailInfo> loadPackScanList() {
        ArrayList<OrderDetailInfo> list = new ArrayList<>();
        final OrderDetailInfo outStockTaskDetailsInfoModel = new OrderDetailInfo();
        outStockTaskDetailsInfoModel.setMaterialno("物料1");
//        outStockTaskDetailsInfoModel.setScanQty((float) 100);
//        outStockTaskDetailsInfoModel.setRemainQty((float) 100);
//        outStockTaskDetailsInfoModel.setBatchNo("批次1");
//        outStockTaskDetailsInfoModel.setAreaNo("推荐库位1,推荐库位2,推荐库位3");
//        outStockTaskDetailsInfoModel.setMaterialDesc("物料描述1");
//        outStockTaskDetailsInfoModel.setPickFinish(true);
        list.add(outStockTaskDetailsInfoModel);
        final OrderDetailInfo outStockTaskDetailsInfoModel2 = new OrderDetailInfo();
        outStockTaskDetailsInfoModel2.setMaterialno("物料2");
//        outStockTaskDetailsInfoModel2.setScanQty((float) 50);
//        outStockTaskDetailsInfoModel2.setRemainQty((float) 100);
//        outStockTaskDetailsInfoModel2.setBatchNo("批次2");
//        outStockTaskDetailsInfoModel2.setAreaNo("推荐库位1,推荐库位2,推荐库位3");
//        outStockTaskDetailsInfoModel2.setMaterialDesc("物料描述2");
        list.add(outStockTaskDetailsInfoModel2);
        final OrderDetailInfo outStockTaskDetailsInfoModel3 = new OrderDetailInfo();
        outStockTaskDetailsInfoModel3.setMaterialno("物料3");
//        outStockTaskDetailsInfoModel3.setScanQty((float) 80);
//        outStockTaskDetailsInfoModel3.setRemainQty((float) 1000);
//        outStockTaskDetailsInfoModel3.setBatchNo("批次3");
//        outStockTaskDetailsInfoModel3.setAreaNo("推荐库位1,推荐库位2,推荐库位3");
//        outStockTaskDetailsInfoModel3.setMaterialDesc("物料描述3");
        list.add(outStockTaskDetailsInfoModel3);

        final OrderDetailInfo outStockTaskDetailsInfoModel4 = new OrderDetailInfo();
        outStockTaskDetailsInfoModel4.setMaterialno("物料4");
//        outStockTaskDetailsInfoModel4.setScanQty((float) 100);
//        outStockTaskDetailsInfoModel4.setRemainQty((float) 120);
//        outStockTaskDetailsInfoModel4.setBatchNo("批次4");
//        outStockTaskDetailsInfoModel4.setAreaNo("推荐库位1,推荐库位2,推荐库位3");
//        outStockTaskDetailsInfoModel4.setMaterialDesc("物料描述4");
        list.add(outStockTaskDetailsInfoModel4);
        return list;
    }

    /**
     * @desc: 获取采购订单离线表头数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 18:51
     */
    public static ArrayList<QualityHeaderInfo> loadQualityHeaderList() {
        ArrayList<QualityHeaderInfo> list = new ArrayList<>();
        QualityHeaderInfo bean1 = new QualityHeaderInfo();
//        bean1.setErpvoucherno("CG20200627001");
//        bean1.setErpvouchertype("检验单");
//        bean1.setStrongholdName("组织1");
//        bean1.setSuppliername("供应商1");
//        list.add(bean1);
//        QualityHeaderInfo bean2 = new QualityHeaderInfo();
//        bean2.setErpvoucherno("CG20200627002");
//        bean2.setErpvouchertype("检验单");
//        bean2.setStrongholdName("组织1");
//        bean2.setSuppliername("供应商1");
//        list.add(bean2);
//        QualityHeaderInfo bean3 = new QualityHeaderInfo();
//        bean3.setErpvoucherno("CG20200627003");
//        bean3.setErpvouchertype("检验单");
//        bean3.setStrongholdName("组织1");
//        bean3.setSuppliername("供应商1");
//        list.add(bean3);
//        QualityHeaderInfo bean4 = new QualityHeaderInfo();
//        bean4.setErpvoucherno("CG20200627004");
//        bean4.setErpvouchertype("检验单");
//        bean4.setStrongholdName("组织1");
//        bean4.setSuppliername("供应商1");
//        list.add(bean4);
//        QualityHeaderInfo bean5 = new QualityHeaderInfo();
//        bean5.setErpvoucherno("CG20200627005");
//        bean5.setErpvouchertype("检验单");
//        bean5.setStrongholdName("组织1");
//        bean5.setSuppliername("供应商1");
//        list.add(bean5);
//
        return list;
    }

    /**
     * @desc: 原料标签
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/17 10:35
     */
    public static PrintInfo getRawMaterialLabel() {
        PrintInfo printInfo = new PrintInfo();
        printInfo.setMaterialNo("130100002-测试数据");
        printInfo.setMaterialDesc("百香果风味饮料（博多家园）-测试数据");
        printInfo.setBatchNo("20200717-测试数据");
        printInfo.setQty(88);
        printInfo.setQRCode("130100001%20200717%88%1-测试数据");
        printInfo.setSpec("8瓶*2升-测试数据");
        printInfo.setPrintType(PrintType.PRINT_TYPE_RAW_MATERIAL_STYLE);
        return printInfo;
    }


      public  static  List<PrintInfo>  getRawMaterialLabelList(){
        List<PrintInfo>  list=new ArrayList<>();
          for (int i=0;i<1;i++){
              PrintInfo printInfo = new PrintInfo();
              printInfo.setErpVoucherNo("2102-M500-2009100002");
              printInfo.setMaterialNo("130100002-测试数据"+i);
              printInfo.setMaterialDesc("百香果风味饮料（博多家园）-测试数据"+i);
              printInfo.setBatchNo("20200717-测试数据"+i);
              printInfo.setPackQty(88+i);
              printInfo.setQRCode("130100001%20200717%88%1-测试数据"+i);
              printInfo.setSpec("8瓶*2升-测试数据"+i);
              printInfo.setPrintType(PrintType.PRINT_TYPE_RAW_MATERIAL_STYLE);
              list.add(printInfo);
          }
          return list;

      }
    /**
     * @desc: 托盘标签
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/17 10:36
     */
    public static PrintInfo getPalletLabel() {
        PrintInfo printInfo = new PrintInfo();
        printInfo.setMaterialNo("130100002");
        printInfo.setQty(888);
        printInfo.setBatchNo("20200715");
        printInfo.setArrivalTime("2020-07-15");
        printInfo.setSignatory("博多工贸有限公司");
        printInfo.setQRCode("130100001%20200715%888%2");
        printInfo.setPrintType(PrintType.PRINT_TYPE_PALLET_STYLE);
        return printInfo;
    }
}
