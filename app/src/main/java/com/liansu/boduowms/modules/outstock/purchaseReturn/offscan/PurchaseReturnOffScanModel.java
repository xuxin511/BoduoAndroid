package com.liansu.boduowms.modules.outstock.purchaseReturn.offscan;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.BaseOutStockBusinessModel;
import com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan.PurchaseInspectionProcessingScan;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/6.
 */
public class PurchaseReturnOffScanModel extends BaseOutStockBusinessModel {
    String TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC = "PurchaseReturnOffScanModel_GetT_OutStockDetailListADFAsync";
    String TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC     = "PurchaseReturnOffScanModel_SaveT_OutStockDetailADFAsync";  //实时提交
    String TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC     = "PurchaseReturnOffScanModel_PostT_OutStockDetailADFAsync"; //单据提交
    String TAG_INSPEC_RETURN_PRINT_INSPEC_RETURN     = "PurchaseReturnOffScanModel_PrintInspecReturn"; //单据打印
    private final int RESULT_TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC = 221;
    private final int RESULT_TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC     = 222;
    private final int RESULT_TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC     = 223;
    private final int RESULT_TAG_RETURN_PRINT_INSPEC_RETURN     = 224;

    public PurchaseReturnOffScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    protected void onHandleMessage(Message msg) {
        super.onHandleMessage(msg);
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC");
                break;
            case RESULT_TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC:
                listener = mNetMap.get("TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC");
                break;
            case RESULT_TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC:
                listener = mNetMap.get("TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC");
                break;
//            case RESULT_TAG_SELECT_MATERIAL:
//                listener = mNetMap.get("TAG_SELECT_MATERIAL");
//                break;
//            case RESULT_TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC:
//                listener = mNetMap.get("TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC");
//                break;
//            case RESULT_TAG_INSPEC_RETURN_PRINT_INSPEC_RETURN:
//                listener = mNetMap.get("TAG_INSPEC_RETURN_PRINT_INSPEC_RETURN");
//                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;

        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    /**
     * @desc: 获取质检表体
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestOrderDetail(OrderRequestInfo headerInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(headerInfo);
        LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, mContext.getString(R.string.message_request_purchase_inspection_detail), mContext, mHandler, RESULT_TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, null, UrlInfo.getUrl().PurchaseReturn_GetT_OutStockDetailListADFAsync, modelJson, null);
    }

    /**
     * @desc: 提交条码信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestBarcodeInfoRefer(OutStockOrderDetailInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(PurchaseReturnOffScan.class, TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC, mContext.getString(R.string.message_request_barcode_refer), mContext, mHandler, RESULT_TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().PurchaseReturn_SaveT_OutStockDetailADFAsync, modelJson, null);

    }

    /**
     * @desc: 单据提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestRefer(OrderRequestInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC, parseModelToJson(info));
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC, mContext.getString(R.string.message_request_quality_un_inspection_refer), mContext, mHandler, RESULT_TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().PurchaseReturn_PostT_OutStockDetailADFAsync, modelJson, null);

    }

    /**
     * @desc: 订单打印
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/6 11:01
     */
    public void requestOrderPrint(OutStockOrderDetailInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_INSPEC_RETURN_PRINT_INSPEC_RETURN", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, TAG_INSPEC_RETURN_PRINT_INSPEC_RETURN, parseModelToJson(info));
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_INSPEC_RETURN_PRINT_INSPEC_RETURN, mContext.getString(R.string.request_print_order_purchase_return), mContext, mHandler, RESULT_TAG_RETURN_PRINT_INSPEC_RETURN, null, UrlInfo.getUrl().PurchaseReturn_PrintInspecReturn, modelJson, null);

    }

}
