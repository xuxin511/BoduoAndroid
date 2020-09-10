package com.liansu.boduowms.modules.instock.outsourcingStorage.scan;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScanModel;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelListToJsonArray;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class OutsourcingStorageScanModel extends BaseOrderScanModel {
    String TAG_GetT_InStockDetailListByHeaderIDADF = "ReceiptionScan_GetT_InStockDetailListByHeaderIDADF";
    String TAG_GetT_PalletDetailByBarCodeADF       = "ReceiptionScan_GetT_PalletDetailByBarCodeADF";
    String TAG_GetT_PurchaseOrderListADFAsync      = "OutsourcingStorageScanModel_GetT_PurchaseOrderListADFAsync";
    String TAG_GetAreaModelADF                     = "ReceiptionScan_GetAreaModelADF";
    private final int RESULT_Msg_GetT_InStockDetailListByHeaderIDADF = 101;
    private final int RESULT_Msg_GetT_PalletDetailByBarCode          = 102;
    private final int RESULT_Msg_SaveT_InStockDetailADF              = 103;
    private final int RESULT_Msg_GetAreaModelADF                     = 104;

    public OutsourcingStorageScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler, OrderType.IN_STOCK_ORDER_TYPE_OUTSOURCING_STORAGE_VALUE);
    }

    @Override
    public void onHandleMessage(Message msg) {

        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_Msg_GetT_InStockDetailListByHeaderIDADF:
                listener = mNetMap.get("TAG_GetT_InStockDetailListByHeaderIDADF");
                break;
            case RESULT_Msg_GetT_PalletDetailByBarCode:

                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
        super.onHandleMessage(msg);
    }

    /**
     * @desc: 获取委婉订单明细接口
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 23:22
     */
    public void requestOutSourcingStorageDetail(OrderRequestInfo receiptModel, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_InStockDetailListByHeaderIDADF", callBackListener);
//        final ReceiptDetail_Model receiptDetailModel = new ReceiptDetail_Model();
//        receiptDetailModel.setHeaderID(receiptModel.getId());
//        receiptDetailModel.setErpVoucherNo(receiptModel.getErpvoucherno());
//        receiptDetailModel.setVoucherType(receiptModel.getVouchertype());
//        final Map<String, String> params = new HashMap<String, String>();
//        params.put("ModelDetailJson", parseModelToJson(receiptDetailModel));
//        String para = (new JSONObject(params)).toString();
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, para);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InStockDetailListByHeaderIDADF, mContext.getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), mContext, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null, URLModel.GetURL().GetT_InStockDetailListByHeaderIDADF, params, null);
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
     * @desc: 过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 18:46
     */

    public void requestOrderRefer(List<OrderDetailInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_POST_T_WORK_ORDER_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_POST_T_WORK_ORDER_DETAIL_ADF_ASYNC, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_POST_T_WORK_ORDER_DETAIL_ADF_ASYNC, mContext.getString(R.string.Msg_order_refer), mContext, mHandler, RESULT_TAG_POST_T_WORK_ORDER_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().PostT_WorkOrderDetailADFAsync, modelJson, null);

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
        mNetMap.put("TAG_SAVE_IN_STOCK_WORK_ORDER_LIST_TO_DB", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_SAVE_IN_STOCK_WORK_ORDER_LIST_TO_DB, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SAVE_IN_STOCK_WORK_ORDER_LIST_TO_DB, mContext.getString(R.string.message_request_barcode_refer), mContext, mHandler, RESULT_TAG_SAVE_IN_STOCK_WORK_ORDER_LIST_TO_DB, null, UrlInfo.getUrl().Save_InStockWorkOrderListToDB, modelJson, null);
    }

    @Override
    protected PrintInfo getPrintModel(OutBarcodeInfo outBarcodeInfo) {
        return null;
    }




}
