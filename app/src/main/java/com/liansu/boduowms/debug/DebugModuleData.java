package com.liansu.boduowms.debug;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.menu.MenuChildrenInfo;
import com.liansu.boduowms.bean.menu.MenuInfo;
import com.liansu.boduowms.bean.menu.MenuMetaInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.IBaseOrderScanView;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.modules.print.linkos.PrintType;
import com.liansu.boduowms.utils.SharePreferUtil;
import com.liansu.boduowms.utils.function.GsonUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelListToJsonArray;

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
        itemIconList.add(R.drawable.b_combine_pallet);
        itemNamesList.add(context.getString(R.string.main_menu_item_in_stock_combine_pallet));
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


    public static List<PrintInfo> getRawMaterialLabelList() {
        List<PrintInfo> list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            PrintInfo printInfo = new PrintInfo();
            printInfo.setErpVoucherNo("2102-M500-2009100002");
            printInfo.setMaterialNo("130100002-测试数据" + i);
            printInfo.setMaterialDesc("百香果风味饮料（博多家园）-测试数据" + i);
            printInfo.setBatchNo("20200717-测试数据" + i);
            printInfo.setPackQty(88 + i);
            printInfo.setQRCode("130100001%20200717%88%1-测试数据" + i);
            printInfo.setSpec("8瓶*2升-测试数据" + i);
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

    /**
     * @desc: 获取主菜单数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/16 10:06
     */
    public static void getMenuData() {
//        String json = "{\"Result\":1,\"ResultValue\":\"\",\"Data\":[{\"Id\":13,\"Menuno\":\"1013\",\"title\":\"PDA入库\",\"Menutype\":1,\"path\":\"1\",\"component\":\"1\",\"Nodelevel\":\"1\",\"Nodesort\":\"1\",\"Parentid\":13,\"children\":[{\"Id\":14,\"path\":\"22\",\"component\":\"1\",\"title\":\"采购收货\",\"meta\":{\"Id\":14,\"keepalive\":false,\"internalOrExternal\":false,\"title\":\"采购收货\"}},{\"Id\":15,\"path\":\"26\",\"component\":\"1\",\"title\":\"销售退货\",\"meta\":{\"Id\":15,\"keepalive\":false,\"internalOrExternal\":false,\"title\":\"销售退货\"}},{\"Id\":16,\"path\":\"24\",\"component\":\"1\",\"title\":\"调拨入库\",\"meta\":{\"Id\":16,\"keepalive\":false,\"internalOrExternal\":false,\"title\":\"调拨入库\"}},{\"Id\":32,\"path\":\"42\",\"component\":\"1\",\"title\":\"快速入库新\",\"meta\":{\"Id\":32,\"keepalive\":false,\"internalOrExternal\":false,\"title\":\"快速入库新\"}}],\"meta\":{\"Id\":13,\"keepalive\":false,\"internalOrExternal\":false,\"title\":\"PDA入库\"}},{\"Id\":28,\"Menuno\":\"1028\",\"title\":\"PDA出库\",\"Menutype\":1,\"path\":\"2\",\"component\":\"2\",\"Nodelevel\":\"1\",\"Nodesort\":\"1\",\"Parentid\":28,\"children\":[{\"Id\":17,\"path\":\"30\",\"component\":\"2\",\"title\":\"销售出库\",\"meta\":{\"Id\":17,\"keepalive\":false,\"internalOrExternal\":false,\"title\":\"销售出库\"}},{\"Id\":18,\"path\":\"27\",\"component\":\"2\",\"title\":\"采购退货\",\"meta\":{\"Id\":18,\"keepalive\":false,\"internalOrExternal\":false,\"title\":\"采购退货\"}},{\"Id\":19,\"path\":\"29\",\"component\":\"2\",\"title\":\"交货发货\",\"meta\":{\"Id\":19,\"keepalive\":false,\"internalOrExternal\":false,\"title\":\"交货发货\"}},{\"Id\":20,\"path\":\"25\",\"component\":\"2\",\"title\":\"调拨出库\",\"meta\":{\"Id\":20,\"keepalive\":false,\"internalOrExternal\":false,\"title\":\"调拨出库\"}}],\"meta\":{\"Id\":28,\"keepalive\":false,\"internalOrExternal\":false,\"title\":\"PDA出库\"}},{\"Id\":29,\"Menuno\":\"1029\",\"title\":\"PDA库存\",\"Menutype\":1,\"path\":\"3\",\"component\":\"3\",\"Nodelevel\":\"1\",\"Nodesort\":\"1\",\"Parentid\":29,\"children\":[{\"Id\":22,\"path\":\"34\",\"component\":\"3\",\"title\":\"移库\",\"meta\":{\"Id\":22,\"keepalive\":false,\"internalOrExternal\":false,\"title\":\"移库\"}},{\"Id\":23,\"path\":\"35\",\"component\":\"3\",\"title\":\"库存调整\",\"meta\":{\"Id\":23,\"keepalive\":false,\"internalOrExternal\":false,\"title\":\"库存调整\"}},{\"Id\":24,\"path\":\"38\",\"component\":\"3\",\"title\":\"库存查询\",\"meta\":{\"Id\":24,\"keepalive\":false,\"internalOrExternal\":false,\"title\":\"库存查询\"}},{\"Id\":25,\"path\":\"33\",\"component\":\"3\",\"title\":\"加解锁\",\"meta\":{\"Id\":25,\"keepalive\":false,\"internalOrExternal\":false,\"title\":\"加解锁\"}}],\"meta\":{\"Id\":29,\"keepalive\":false,\"internalOrExternal\":false,\"title\":\"PDA库存\"}}]}";
        String json = "{\n" +
                "  \"Result\": 1,\n" +
                "  \"ResultValue\": \"\",\n" +
                "  \"Data\": [\n" +
                "    {\n" +
                "      \"Id\": 13,\n" +
                "      \"Menuno\": \"1013\",\n" +
                "      \"title\": \"PDA入库\",\n" +
                "      \"Menutype\": 1,\n" +
                "      \"path\": \"1\",\n" +
                "      \"component\": \"1\",\n" +
                "      \"Nodelevel\": \"1\",\n" +
                "      \"Nodesort\": \"1\",\n" +
                "      \"Parentid\": 13,\n" +
                "      \"children\": [\n" +
                "        {\n" +
                "          \"Id\": 14,\n" +
                "          \"path\": \"22\",\n" +
                "          \"component\": \"1\",\n" +
                "          \"title\": \"采购收货\",\n" +
                "          \"meta\": {\n" +
                "            \"Id\": 14,\n" +
                "            \"keepalive\": false,\n" +
                "            \"internalOrExternal\": false,\n" +
                "            \"title\": \"采购收货\"\n" +
                "          },\n" +
                "          \"children\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"Id\": 15,\n" +
                "          \"path\": \"26\",\n" +
                "          \"component\": \"1\",\n" +
                "          \"title\": \"销售退货\",\n" +
                "          \"meta\": {\n" +
                "            \"Id\": 15,\n" +
                "            \"keepalive\": false,\n" +
                "            \"internalOrExternal\": false,\n" +
                "            \"title\": \"销售退货\"\n" +
                "          },\n" +
                "          \"children\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"Id\": 16,\n" +
                "          \"path\": \"24\",\n" +
                "          \"component\": \"1\",\n" +
                "          \"title\": \"调拨入库\",\n" +
                "          \"meta\": {\n" +
                "            \"Id\": 16,\n" +
                "            \"keepalive\": false,\n" +
                "            \"internalOrExternal\": false,\n" +
                "            \"title\": \"调拨入库\"\n" +
                "          },\n" +
                "          \"children\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"Id\": 32,\n" +
                "          \"path\": \"42\",\n" +
                "          \"component\": \"1\",\n" +
                "          \"title\": \"快速入库新\",\n" +
                "          \"meta\": {\n" +
                "            \"Id\": 32,\n" +
                "            \"keepalive\": false,\n" +
                "            \"internalOrExternal\": false,\n" +
                "            \"title\": \"快速入库新\"\n" +
                "          },\n" +
                "          \"children\": []\n" +
                "        }\n" +
                "      ],\n" +
                "      \"meta\": {\n" +
                "        \"Id\": 13,\n" +
                "        \"keepalive\": false,\n" +
                "        \"internalOrExternal\": false,\n" +
                "        \"title\": \"PDA入库\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"Id\": 28,\n" +
                "      \"Menuno\": \"1028\",\n" +
                "      \"title\": \"PDA出库\",\n" +
                "      \"Menutype\": 1,\n" +
                "      \"path\": \"2\",\n" +
                "      \"component\": \"2\",\n" +
                "      \"Nodelevel\": \"1\",\n" +
                "      \"Nodesort\": \"1\",\n" +
                "      \"Parentid\": 28,\n" +
                "      \"children\": [\n" +
                "        {\n" +
                "          \"Id\": 17,\n" +
                "          \"path\": \"30\",\n" +
                "          \"component\": \"2\",\n" +
                "          \"title\": \"销售出库\",\n" +
                "          \"meta\": {\n" +
                "            \"Id\": 17,\n" +
                "            \"keepalive\": false,\n" +
                "            \"internalOrExternal\": false,\n" +
                "            \"title\": \"销售出库\"\n" +
                "          },\n" +
                "          \"children\": [\n" +
                "            {\n" +
                "              \"Id\": 1066,\n" +
                "              \"path\": \"/test\",\n" +
                "              \"component\": \"/test\",\n" +
                "              \"title\": \"销售下架\",\n" +
                "              \"meta\": {\n" +
                "                \"Id\": 1066,\n" +
                "                \"keepalive\": false,\n" +
                "                \"internalOrExternal\": false,\n" +
                "                \"title\": \"销售下架\"\n" +
                "              },\n" +
                "              \"children\": []\n" +
                "            },\n" +
                "            {\n" +
                "              \"Id\": 1067,\n" +
                "              \"path\": \"/test\",\n" +
                "              \"component\": \"/test\",\n" +
                "              \"title\": \"销售拼箱\",\n" +
                "              \"meta\": {\n" +
                "                \"Id\": 1067,\n" +
                "                \"keepalive\": false,\n" +
                "                \"internalOrExternal\": false,\n" +
                "                \"title\": \"销售拼箱\"\n" +
                "              },\n" +
                "              \"children\": []\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"Id\": 18,\n" +
                "          \"path\": \"27\",\n" +
                "          \"component\": \"2\",\n" +
                "          \"title\": \"采购退货\",\n" +
                "          \"meta\": {\n" +
                "            \"Id\": 18,\n" +
                "            \"keepalive\": false,\n" +
                "            \"internalOrExternal\": false,\n" +
                "            \"title\": \"采购退货\"\n" +
                "          },\n" +
                "          \"children\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"Id\": 19,\n" +
                "          \"path\": \"29\",\n" +
                "          \"component\": \"2\",\n" +
                "          \"title\": \"交货发货\",\n" +
                "          \"meta\": {\n" +
                "            \"Id\": 19,\n" +
                "            \"keepalive\": false,\n" +
                "            \"internalOrExternal\": false,\n" +
                "            \"title\": \"交货发货\"\n" +
                "          },\n" +
                "          \"children\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"Id\": 20,\n" +
                "          \"path\": \"25\",\n" +
                "          \"component\": \"2\",\n" +
                "          \"title\": \"调拨出库\",\n" +
                "          \"meta\": {\n" +
                "            \"Id\": 20,\n" +
                "            \"keepalive\": false,\n" +
                "            \"internalOrExternal\": false,\n" +
                "            \"title\": \"调拨出库\"\n" +
                "          },\n" +
                "          \"children\": []\n" +
                "        }\n" +
                "      ],\n" +
                "      \"meta\": {\n" +
                "        \"Id\": 28,\n" +
                "        \"keepalive\": false,\n" +
                "        \"internalOrExternal\": false,\n" +
                "        \"title\": \"PDA出库\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"Id\": 29,\n" +
                "      \"Menuno\": \"1029\",\n" +
                "      \"title\": \"PDA库存\",\n" +
                "      \"Menutype\": 1,\n" +
                "      \"path\": \"3\",\n" +
                "      \"component\": \"3\",\n" +
                "      \"Nodelevel\": \"1\",\n" +
                "      \"Nodesort\": \"1\",\n" +
                "      \"Parentid\": 29,\n" +
                "      \"children\": [\n" +
                "        {\n" +
                "          \"Id\": 22,\n" +
                "          \"path\": \"34\",\n" +
                "          \"component\": \"3\",\n" +
                "          \"title\": \"移库\",\n" +
                "          \"meta\": {\n" +
                "            \"Id\": 22,\n" +
                "            \"keepalive\": false,\n" +
                "            \"internalOrExternal\": false,\n" +
                "            \"title\": \"移库\"\n" +
                "          },\n" +
                "          \"children\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"Id\": 23,\n" +
                "          \"path\": \"35\",\n" +
                "          \"component\": \"3\",\n" +
                "          \"title\": \"库存调整\",\n" +
                "          \"meta\": {\n" +
                "            \"Id\": 23,\n" +
                "            \"keepalive\": false,\n" +
                "            \"internalOrExternal\": false,\n" +
                "            \"title\": \"库存调整\"\n" +
                "          },\n" +
                "          \"children\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"Id\": 24,\n" +
                "          \"path\": \"38\",\n" +
                "          \"component\": \"3\",\n" +
                "          \"title\": \"库存查询\",\n" +
                "          \"meta\": {\n" +
                "            \"Id\": 24,\n" +
                "            \"keepalive\": false,\n" +
                "            \"internalOrExternal\": false,\n" +
                "            \"title\": \"库存查询\"\n" +
                "          },\n" +
                "          \"children\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"Id\": 25,\n" +
                "          \"path\": \"33\",\n" +
                "          \"component\": \"3\",\n" +
                "          \"title\": \"加解锁\",\n" +
                "          \"meta\": {\n" +
                "            \"Id\": 25,\n" +
                "            \"keepalive\": false,\n" +
                "            \"internalOrExternal\": false,\n" +
                "            \"title\": \"加解锁\"\n" +
                "          },\n" +
                "          \"children\": []\n" +
                "        }\n" +
                "      ],\n" +
                "      \"meta\": {\n" +
                "        \"Id\": 29,\n" +
                "        \"keepalive\": false,\n" +
                "        \"internalOrExternal\": false,\n" +
                "        \"title\": \"PDA库存\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        BaseResultInfo<List<MenuInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(json, new TypeToken<BaseResultInfo<List<MenuInfo>>>() {
        }.getType());
        List<MenuChildrenInfo> subList = new ArrayList<>();
        List<MenuInfo> list = returnMsgModel.getData();
        if (list != null && list.size() > 0) {
            for (MenuInfo info : list) {
                if (info.getComponent().equals("2")) {
                    for (MenuChildrenInfo menuChildrenInfo : info.getChildren()) {
                        if (menuChildrenInfo.getPath().equals("30")) {
                            MenuChildrenInfo childrenInfo = new MenuChildrenInfo();
                            childrenInfo.setId(1701);
                            childrenInfo.setComponent("OFF_SHELF");
                            childrenInfo.setPath("30");
                            childrenInfo.setTitle("销售下架");
                            MenuMetaInfo metaInfo = new MenuMetaInfo();
                            metaInfo.setId(1701);
                            metaInfo.setTitle("销售下架");
                            childrenInfo.setMeta(metaInfo);
                            subList.add(childrenInfo);
                            MenuChildrenInfo childrenInfo1 = new MenuChildrenInfo();
                            childrenInfo1.setId(1702);
                            childrenInfo1.setComponent("LCL");
                            childrenInfo1.setPath("30");
                            childrenInfo1.setTitle("销售拼箱");
                            MenuMetaInfo metaInfo1 = new MenuMetaInfo();
                            metaInfo1.setId(1702);
                            metaInfo1.setTitle("销售拼箱");
                            childrenInfo1.setMeta(metaInfo1);
                            subList.add(childrenInfo1);
                            menuChildrenInfo.setChildren(subList);
                        }
                    }
                }


                Log.v("MENU_DATA", info.toString());
                Log.v("MENU_DATA", "------------------------------------------------");

            }

        }

        String resultJson = parseModelListToJsonArray(list);
        Log.v("MENU_DATA", "-------------------------------------------------------------------------------");
        Log.v("MENU_DATA", resultJson);

    }
}
