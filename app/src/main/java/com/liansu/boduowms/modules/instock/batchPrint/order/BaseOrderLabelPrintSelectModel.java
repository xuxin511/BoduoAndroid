package com.liansu.boduowms.modules.instock.batchPrint.order;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.bean.order.VoucherTypeInfo;
import com.liansu.boduowms.modules.instock.activeOtherStorage.scan.ActiveOtherScanModel;
import com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan.InStockReturnsStorageScanModel;
import com.liansu.boduowms.modules.instock.productStorage.printPalletScan.PrintPalletScanModel;
import com.liansu.boduowms.modules.instock.productionReturnsStorage.print.ProductionReturnsModel;
import com.liansu.boduowms.modules.instock.purchaseStorage.scan.PurchaseStorageScanModel;
import com.liansu.boduowms.modules.instock.salesReturn.activeScan.SalesReturnsStorageModel2;
import com.liansu.boduowms.modules.instock.transferToStorage.scan.TransferToStorageScanModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.function.ListUtils;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.liansu.boduowms.bean.order.OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_VALUE;
import static com.liansu.boduowms.bean.order.OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/14.
 */
public class BaseOrderLabelPrintSelectModel extends BaseModel {
    Map<String, Integer> mVoucherTypeMap = new HashMap<>();
    public final String TAG_GET_ORDER_DETAIL_INFO_LIST_SUB = "BaseOrderLabelPrintSelectModel_TAG_GET_ORDER_DETAIL_INFO_LIST_SUB";
    public final String TAG_GET_T_PARAMETER_LIST           = "BaseOrderLabelPrintSelectModel_GetT_ParameterList";
    String TAG_SELECT_MATERIAL = "BaseOrderLabelPrintSelectModel_SelectMaterial";  //获取物料信息
    private final int                        RESULT_TAG_SELECT_MATERIAL      = 10001;
    private final int                        RESULT_TAG_GET_T_PARAMETER_LIST = 10002;
    protected     ArrayList<OrderDetailInfo> mOrderDetailList                = new ArrayList<>();
    protected     OrderHeaderInfo            mHeaderInfo                     = null;
    protected     OrderDetailInfo            mCurrentDetailInfo              = null;
    BaseModel mBaseModel; //所有订单类型的model 类基类
    public final String                ORDER_TYPE_NONE   = "请选择";
    private      List<VoucherTypeInfo> mVoucherTypeList  = new ArrayList<>();
    public final int                   VOUCHER_TYPE_NONE = -1; //未选择单据类型

    public BaseOrderLabelPrintSelectModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    public void onHandleMessage(Message msg) {
        if (mBaseModel != null) {
            mBaseModel.onHandleMessage(msg);
        }
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_SELECT_MATERIAL:
                listener = mNetMap.get("TAG_SELECT_MATERIAL");
                break;
            case RESULT_TAG_GET_T_PARAMETER_LIST:
                listener = mNetMap.get("TAG_GET_T_PARAMETER_LIST");
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                MessageBox.Show(mContext, "获取请求失败_____" + msg.obj);
                break;

        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }


    public void setOrderDetailList(List<OrderDetailInfo> orderDetailList) {
        mOrderDetailList.clear();
        if (orderDetailList != null && orderDetailList.size() > 0) {
            mOrderDetailList.addAll(orderDetailList);
        }

    }

    public ArrayList<OrderDetailInfo> getOrderDetailList() {
        return mOrderDetailList;
    }

    /**
     * @desc: 保存当前物料数据  托盘方式根据物料明细行获取
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 15:06
     */
    public void setCurrentDetailInfo(OrderDetailInfo orderDetailInfo) {
        mCurrentDetailInfo = orderDetailInfo;
    }


    public OrderDetailInfo getCurrentDetailInfo() {
        return mCurrentDetailInfo;
    }


    public void setOrderHeaderInfo(OrderHeaderInfo orderHeaderInfo) {
        mHeaderInfo = orderHeaderInfo;
    }

    public OrderHeaderInfo getHeaderInfo() {
        return mHeaderInfo;
    }


    public void setVoucherTypeList(List<VoucherTypeInfo> voucherTypeList) {
        mVoucherTypeList.clear();
        mVoucherTypeMap.clear();
        if (voucherTypeList != null && voucherTypeList.size() > 0) {
            mVoucherTypeList.addAll(voucherTypeList);
            setVoucherTypeMap(mVoucherTypeList);
        }

    }

    /**
     * @desc: 根据不同单据获取数据层
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 10:29
     */
    public void getOrderDetailInfoList(int voucherType, OrderRequestInfo orderRequestInfo, NetCallBackListener<String> callBackListener) {
        mBaseModel = null;
        switch (voucherType) {
            case IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE:
                PurchaseStorageScanModel purchaseStorageScanModel = new PurchaseStorageScanModel(mContext, mHandler);
                mBaseModel = purchaseStorageScanModel;
                purchaseStorageScanModel.requestReceiptDetail(orderRequestInfo, callBackListener);
                break;
            case IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_VALUE:
                PrintPalletScanModel printPalletScanModel = new PrintPalletScanModel(mContext, mHandler);
                mBaseModel = printPalletScanModel;
                OrderHeaderInfo postInfo = new OrderHeaderInfo();
                postInfo.setErpvoucherno(orderRequestInfo.getErpvoucherno());
                postInfo.setVouchertype(voucherType);
                postInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
                printPalletScanModel.requestOrderDetail(postInfo, callBackListener);
                break;
            case OrderType.IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE_VALUE:
                ActiveOtherScanModel activeOtherScanModel = new ActiveOtherScanModel(mContext, mHandler);
                mBaseModel = activeOtherScanModel;
                OrderRequestInfo postActiveOtherScanInfo = new OrderRequestInfo();
                postActiveOtherScanInfo.setErpvoucherno(orderRequestInfo.getErpvoucherno());
                postActiveOtherScanInfo.setVouchertype(voucherType);
                postActiveOtherScanInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
                postActiveOtherScanInfo.setStrongholdcode(BaseApplication.mCurrentWareHouseInfo.getStrongholdcode());
                postActiveOtherScanInfo.setStrongholdName(BaseApplication.mCurrentWareHouseInfo.getStrongholdname());
                activeOtherScanModel.requestActiveOtherDetail(postActiveOtherScanInfo, callBackListener);
                break;
            case OrderType.IN_STOCK_ORDER_TYPE_TWO_STAGE_TRANSFER_TO_STORAGE_VALUE:
                TransferToStorageScanModel transferToStorageScanModel = new TransferToStorageScanModel(mContext, mHandler);
                mBaseModel = transferToStorageScanModel;
                OrderRequestInfo postTransferToStorageScanModelScanInfo = new OrderRequestInfo();
                postTransferToStorageScanModelScanInfo.setErpvoucherno(orderRequestInfo.getErpvoucherno());
                postTransferToStorageScanModelScanInfo.setVouchertype(voucherType);
                postTransferToStorageScanModelScanInfo.setStrongholdcode(BaseApplication.mCurrentWareHouseInfo.getStrongholdcode());
                postTransferToStorageScanModelScanInfo.setStrongholdName(BaseApplication.mCurrentWareHouseInfo.getStrongholdname());
                postTransferToStorageScanModelScanInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
                transferToStorageScanModel.requestOrderDetail(postTransferToStorageScanModelScanInfo, callBackListener);
                break;

            case OrderType.IN_STOCK_ORDER_TYPE_ONE_STAGE_TRANSFER_TO_STORAGE_VALUE:
                TransferToStorageScanModel transferToStorageScanModel2 = new TransferToStorageScanModel(mContext, mHandler);
                mBaseModel = transferToStorageScanModel2;
                OrderRequestInfo postTransferToStorageScanModelScanInfo2 = new OrderRequestInfo();
                postTransferToStorageScanModelScanInfo2.setErpvoucherno(orderRequestInfo.getErpvoucherno());
                postTransferToStorageScanModelScanInfo2.setVouchertype(voucherType);
                postTransferToStorageScanModelScanInfo2.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
                postTransferToStorageScanModelScanInfo2.setStrongholdcode(BaseApplication.mCurrentWareHouseInfo.getStrongholdcode());
                postTransferToStorageScanModelScanInfo2.setStrongholdName(BaseApplication.mCurrentWareHouseInfo.getStrongholdname());
                transferToStorageScanModel2.requestOrderDetail(postTransferToStorageScanModelScanInfo2, callBackListener);
                break;
            case OrderType.IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE_VALUE:
                ProductionReturnsModel productionReturnsModel = new ProductionReturnsModel(mContext, mHandler);
                mBaseModel = productionReturnsModel;
                OrderRequestInfo productionReturnsScanInfo = new OrderRequestInfo();
                productionReturnsScanInfo.setErpvoucherno(orderRequestInfo.getErpvoucherno());
                productionReturnsScanInfo.setVouchertype(voucherType);
                productionReturnsScanInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
                productionReturnsScanInfo.setStrongholdcode(BaseApplication.mCurrentWareHouseInfo.getStrongholdcode());
                productionReturnsScanInfo.setStrongholdName(BaseApplication.mCurrentWareHouseInfo.getStrongholdname());
                productionReturnsModel.requestOrderDetail(productionReturnsScanInfo, callBackListener);
                break;
            case OrderType.IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE_VALUE:
                SalesReturnsStorageModel2 salesReturnModel = new SalesReturnsStorageModel2(mContext, mHandler, voucherType, InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_ACTIVE);
                mBaseModel = salesReturnModel;
                OrderRequestInfo salesReturnInfo = new OrderRequestInfo();
                salesReturnInfo.setErpvoucherno(orderRequestInfo.getErpvoucherno());
                salesReturnInfo.setVouchertype(voucherType);
                salesReturnInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
                salesReturnInfo.setStrongholdcode(BaseApplication.mCurrentWareHouseInfo.getStrongholdcode());
                salesReturnInfo.setStrongholdName(BaseApplication.mCurrentWareHouseInfo.getStrongholdname());
                salesReturnModel.requestOrderDetail(salesReturnInfo, callBackListener);
                break;
        }
    }

    /**
     * @desc: 查询物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestMaterialInfoQuery(String material, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_SELECT_MATERIAL", callBackListener);
        String modelJson = parseModelToJson(material);
        LogUtil.WriteLog(BaseOrderLabelPrintSelect.class, TAG_SELECT_MATERIAL, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SELECT_MATERIAL, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_TAG_SELECT_MATERIAL, null, UrlInfo.getUrl().SelectMaterial, modelJson, null);

    }

    /**
     * @desc: 获取订单类型
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 10:51
     */
    public int getVoucherType(String voucherName) {
        return mVoucherTypeMap.get(voucherName);
    }

    public void setVoucherTypeMap(List<VoucherTypeInfo> voucherTypeList) {
        for (VoucherTypeInfo info : voucherTypeList) {
            mVoucherTypeMap.put(info.getParametername(), info.getParameterid());
        }
//        mVoucherTypeMap.put(OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_NAME, OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE);
//        mVoucherTypeMap.put(OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_NAME, OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_VALUE);
//        mVoucherTypeMap.put(OrderType.IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE_NAME, OrderType.IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE_VALUE);
    }

    /**
     * @desc: 正在获取订单类型
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/17 13:38
     */
    public void requestVoucherInfoQuery(NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_PARAMETER_LIST", callBackListener);
        String modelJson = "{\"Groupname\":\"VoucherRecPrt_Name\"}";
        LogUtil.WriteLog(BaseOrderLabelPrintSelect.class, TAG_GET_T_PARAMETER_LIST, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_PARAMETER_LIST, mContext.getString(R.string.message_request_voucher_info_query), mContext, mHandler, RESULT_TAG_GET_T_PARAMETER_LIST, null, UrlInfo.getUrl().GetT_ParameterList, modelJson, null);
    }


    /**
     * @desc: 获取扫描的条码相对应的订单物料明细行信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/16 14:16
     */
    public BaseMultiResultInfo<Boolean, List<OrderDetailInfo>> findOutBarcodeInfoFromMaterial(OutBarcodeInfo info) {
        BaseMultiResultInfo<Boolean, List<OrderDetailInfo>> resultInfo = new BaseMultiResultInfo<>();
        List<OrderDetailInfo> sMaterialInfoList = new ArrayList<>();
        String barcodeMaterialNo = info.getMaterialno() != null ? info.getMaterialno() : "";
        String barcodeBatchNo = info.getBatchno() != null ? info.getBatchno() : "";
        try {


            for (int i = 0; i < mOrderDetailList.size(); i++) {
                OrderDetailInfo materialInfo = mOrderDetailList.get(i);
                if (materialInfo != null) {
                    String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
                    if (materialNo.trim().equals(barcodeMaterialNo.trim())) {
//                        if (materialInfo.getRemainqty() != 0) {
                        sMaterialInfoList.add(materialInfo);
//                            info.setUnit(sMaterialInfo.getUnit());
//                            info.setMaterialno(sMaterialInfo.getMaterialno());
//                            info.setMaterialdesc(sMaterialInfo.getMaterialdesc());
//                            info.setRowno(sMaterialInfo.getRowno());
//                            info.setRownodel(sMaterialInfo.getRownodel());
//                            info.setErpvoucherno(sMaterialInfo.getErpvoucherno());
//                            info.setStrongholdcode(sMaterialInfo.getStrongholdcode());
//                            info.setStrongholdname(sMaterialInfo.getStrongholdname());
//                            info.setPackQty(sMaterialInfo.getPackQty());
//                            info.setSpec(sMaterialInfo.getSpec());

//                        }


                    }
                }
            }

            if (sMaterialInfoList != null && sMaterialInfoList.size() > 0) {
                resultInfo.setHeaderStatus(true);
                resultInfo.setInfo(sMaterialInfoList);
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("物料号:[" + barcodeMaterialNo + "]不在订单：" + mHeaderInfo.getErpvoucherno() + "中,不能组托");
                return resultInfo;
            }


        } catch (Exception e) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("获取物料信息出现意料之外的异常:" + e.getMessage());
        }

        return resultInfo;

    }

    /**
     * @desc: 获取托盘名称
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 13:59
     */
    public List<String> getVoucherTypeNameList(int voucherType) {
        List<String> list = new ArrayList<>();
//        list.add(ORDER_TYPE_NONE);
        for (String key : mVoucherTypeMap.keySet()) {
            if (!list.contains(key)) {
                if (mVoucherTypeMap.get(key) == voucherType || voucherType == VOUCHER_TYPE_NONE) {
                    if (voucherType != VOUCHER_TYPE_NONE) {
                        list.add(0, key);
                    } else {
                        if (mVoucherTypeMap.get(key) == IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE) {
                            list.add(0, key);
                        } else {
                            list.add(key);
                        }
                    }
                } else {
                    list.add(0,key);
                }
            }

        }
        return list;
    }

    /**
     * @desc: 重置所有数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/16 15:11
     */
    public void onReset(boolean isReset) {
        if (isReset) {
            mOrderDetailList.clear();
            mHeaderInfo = null;
            mCurrentDetailInfo = null;
            mBaseModel = null;
        } else {
            mBaseModel = null;
            mCurrentDetailInfo = null;
        }


    }

    /**
     * @desc: 排序
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/18 16:09
     */
    public void sortDetailList(String materialNo) {
        String[] sortNameArr = {"Materialno", "Remainqty"};
        boolean[] isAscArr = {true, true};
        ListUtils.sort(mOrderDetailList, sortNameArr, isAscArr);
        List<OrderDetailInfo> taskDoneList = new ArrayList<>();
        List<OrderDetailInfo> currentMaterialList = new ArrayList<>();
        Iterator<OrderDetailInfo> it = mOrderDetailList.iterator();
        while (it.hasNext()) {
            OrderDetailInfo item = it.next();
            String sMaterialNo = item.getMaterialno() != null ? item.getMaterialno() : "";
            if (materialNo != null && materialNo.equals(sMaterialNo)) {
                if (item.getRemainqty() > 0 && ArithUtil.sub(item.getVoucherqty(), item.getRemainqty()) > 0) {
                    it.remove();
                    currentMaterialList.add(item);
                    continue;
                }
            }
            //变绿的沉底
            if (item.getRemainqty() == 0) {
                it.remove();
                taskDoneList.add(item);

            }

        }
        //扫描完毕的沉底
        if (taskDoneList.size() > 0) {
            mOrderDetailList.addAll(taskDoneList);

        }
        //正在扫描的物料放在最前面
        if (currentMaterialList.size() > 0) {
            mOrderDetailList.addAll(0, currentMaterialList);
        }

    }
}
