package com.liansu.boduowms.modules.instock.productionReturnsStorage.returnScan;

import android.content.Context;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan.InStockReturnStorageScan;
import com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan.InStockReturnsStorageScanModel;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelListToJsonArray;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/9/20.
 */
 public class ProductionReturnsStorageModel2 extends InStockReturnsStorageScanModel {
    public ProductionReturnsStorageModel2(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }
    public ProductionReturnsStorageModel2(Context context, MyHandler<BaseActivity> handler, int voucherType, int businessType) {
        super(context, handler,voucherType,businessType);
    }



    @Override
    public void requestOrderDetail(OrderRequestInfo orderRequestInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(orderRequestInfo);
        LogUtil.WriteLog(InStockReturnStorageScan.class, TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC, mContext.getString(R.string.message_request_production_return_detail), mContext, mHandler, RESULT_TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC, null, UrlInfo.getUrl().GetT_WorkOrderDetailListADFAsync, modelJson, null);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC, mContext.getString(R.string.message_request_production_return_detail), mContext, mHandler, RESULT_TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC, null, UrlInfo.getUrl().GetT_WorkOrderReturnDetailListADFAsync, modelJson, null);

    }

    @Override
    public void requestCombineAndReferPallet(List<OrderDetailInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_SAVE_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
        LogUtil.WriteLog(InStockReturnStorageScan.class, TAG_SAVE_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SAVE_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC, mContext.getString(R.string.message_request_refer_barcode_info), mContext, mHandler, RESULT_TAG_SAVE_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().SaveT_WorkOrderReturnDetailADFAsync, modelJson, null);
    }

    /**
     * @desc: 过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 18:46
     */
    @Override
    public void requestOrderRefer(List<OrderDetailInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_POST_T_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
        LogUtil.WriteLog(InStockReturnStorageScan.class, TAG_POST_T_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_POST_T_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC, mContext.getString(R.string.Msg_order_refer), mContext, mHandler, RESULT_TAG_POST_T_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().PostT_WorkOrderReturnDetailADFAsync, modelJson, null);
    }
}
