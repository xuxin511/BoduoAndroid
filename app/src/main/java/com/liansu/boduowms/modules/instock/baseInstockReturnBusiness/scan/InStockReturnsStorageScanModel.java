package com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScanModel;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.modules.print.linkos.PrintType;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelListToJsonArray;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class InStockReturnsStorageScanModel extends BaseOrderScanModel {
    public String TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC     = "InStockReturnsStorageScanModel_TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC";  // 获取工单明细
    public String TAG_SAVE_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC = "ProductionReturnsStorageScanModel_TAG_SaveT_WorkOrderReturnDetailADFAsync";
    String TAG_POST_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC = "ProductionReturnsStorageScanModel_TAG_POST_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC";
    private final       int            RESULT_TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC     = 121;
    private final       int            RESULT_TAG_SAVE_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC = 122;
    private final       int            RESULT_TAG_POST_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC = 123;
    public final static int            IN_STOCK_RETURN_TYPE_NONE                            = -1; //没有选择退货类型
    public final static int            IN_STOCK_RETURN_TYPE_ACTIVE                          = 1; //有源退货
    public final static int            IN_STOCK_RETURN_TYPE_NO_SOURCE                       = 2;//无源退货
    public final static int            RETURN_PALLET_TYPE_NONE                              = -1;//未选择托盘类型
    public final static int            RETURN_PALLET_TYPE_OLD_PALLET                        = 1;//现有托盘
    public final static int            RETURN_PALLET_TYPE_NEW_PALLET                        = 2;//新托盘
    public final static String         RETURN_PALLET_TYPE_OLD_PALLET_NAME                   = "现有托盘";
    public final static String         RETURN_PALLET_TYPE_NEW_PALLET_NAME                   = "新托盘";
    protected           int            mBusinessType                                        = IN_STOCK_RETURN_TYPE_NONE;
    protected           OutBarcodeInfo mCurrentScanInfo                                     = null;

    public InStockReturnsStorageScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler, OrderType.IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE_VALUE);
    }

    public InStockReturnsStorageScanModel(Context context, MyHandler<BaseActivity> handler, int voucherType, int businessType) {
        super(context, handler, OrderType.IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE_VALUE);
        setVoucherType(voucherType);
        setBusinessType(businessType);
    }

    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC");
                break;
            case RESULT_TAG_SAVE_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC:
                listener = mNetMap.get("TAG_SAVE_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC");
                break;
            case RESULT_TAG_POST_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC:
                listener = mNetMap.get("TAG_POST_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC");
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
        super.onHandleMessage(msg);
    }

    /**
     * @desc: 设置入库类型
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/20 20:43
     */
    public void setBusinessType(int businessType) {
        mBusinessType = businessType;
    }

    public int getBusinessType() {
        return mBusinessType;
    }

    /**
     * @desc: 获取当前已扫描的条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/20 23:11
     */
    public void setCurrentScanInfo(OutBarcodeInfo info) {
        mCurrentScanInfo = info;
    }

    public OutBarcodeInfo getCurrentScanInfo() {
        return mCurrentScanInfo;
    }

    /**
     * @desc: 获取订单表体明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestOrderDetail(OrderRequestInfo orderRequestInfo, NetCallBackListener<String> callBackListener) {
//        mNetMap.put("TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC", callBackListener);
//        String modelJson = parseModelToJson(orderRequestInfo);
//        LogUtil.WriteLog(InStockReturnStorageScan.class, TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC, mContext.getString(R.string.message_request_production_return_detail), mContext, mHandler, RESULT_TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC, null, UrlInfo.getUrl().GetT_WorkOrderDetailListADFAsync, modelJson, null);
    }


    /**
     * @desc: 组托并提交入库
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestCombineAndReferPallet(OrderDetailInfo info, NetCallBackListener<String> callBackListener) {
    }

    /**
     * @desc: 组托并提交入库
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    @Override
    public void requestCombineAndReferPallet(List<OrderDetailInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_SAVE_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
        LogUtil.WriteLog(InStockReturnStorageScan.class, TAG_SAVE_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SAVE_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC, mContext.getString(R.string.message_request_refer_barcode_info), mContext, mHandler, RESULT_TAG_SAVE_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().SaveT_WorkOrderReturnDetailADFAsync, modelJson, null);
    }

    /**
     * @desc: 过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 18:46
     */
    public void requestOrderRefer(List<OrderDetailInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_POST_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
        LogUtil.WriteLog(InStockReturnStorageScan.class, TAG_POST_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_POST_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC, mContext.getString(R.string.Msg_order_refer), mContext, mHandler, RESULT_TAG_POST_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().PostT_WorkOrderReturnDetailADFAsync, modelJson, null);

    }

    /**
     * @desc: 过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 18:46
     */
    public void requestNoSourceOrderRefer(List<OutBarcodeInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC, mContext.getString(R.string.message_request_refer_barcode_info), mContext, mHandler, RESULT_TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().Post_SaleReturnDetailADFasync, modelJson, null);

    }

    @Override
    protected PrintInfo getPrintModel(OutBarcodeInfo outBarcodeInfo) {
        PrintInfo printInfo = null;
        if (outBarcodeInfo != null) {
            printInfo = new PrintInfo();
            String materialNo = outBarcodeInfo.getMaterialno() != null ? outBarcodeInfo.getMaterialno() : "";
            String batchNo = outBarcodeInfo.getBatchno() != null ? outBarcodeInfo.getBatchno() : "";
            int barcodeQty = (int) outBarcodeInfo.getQty();
            String QRBarcode = materialNo + "%" + batchNo + "%" + outBarcodeInfo.getPackqty() + "%" + 2;
            printInfo.setMaterialNo(materialNo);
            printInfo.setMaterialDesc(outBarcodeInfo.getMaterialdesc());
            printInfo.setBatchNo(batchNo);
            printInfo.setQty(barcodeQty);
            printInfo.setQRCode(QRBarcode);
            printInfo.setSignatory(BaseApplication.mCurrentUserInfo.getUsername());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            //获取当前时间
            Date date = new Date(System.currentTimeMillis());
            String arrivalTime = simpleDateFormat.format(date);
            printInfo.setArrivalTime(arrivalTime);
            printInfo.setPrintType(PrintType.PRINT_TYPE_PALLET_STYLE);
        }
        return printInfo;


    }

    @Override
    public void onReset() {
        mHeaderInfo = null;
        setOrderRequestInfo(null);
        setAreaInfo(null);
        mCurrentScanInfo = null;
        mOrderDetailList.clear();
//        if (mBusinessType==InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_ACTIVE){
//
//        }
    }
}
