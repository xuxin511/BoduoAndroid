package com.liansu.boduowms.modules.outstock.reviewScan;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des: 发货装车扫描
 * @ Created by yangyiqing on 2020/6/28.
 */
public class ReviewScanModel extends BaseModel {
    public  final String TAG_GET_T_CHECK_OUT_STOCK_DETAIL_LIST_ADF_ASYNC="ReviewScanModel_GetT_CheckOutStockDetailListADFAsync";
    protected List<OutStockOrderDetailInfo> mOrderDetailList = new ArrayList<>(); //订单表体
    protected OutStockOrderHeaderInfo       mOrderHeaderInfo;
    private final int    RESULT_TAG_GET_T_CHECK_OUT_STOCK_DETAIL_LIST_ADF_ASYNC      = 301;

    public ReviewScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    protected void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
//            case RESULT_Msg_GetT_InStockDetailListByHeaderIDADF:
//                listener = mNetMap.get("TAG_GetT_PurchaseOrderListADFAsync");
//                break;
//            case RESULT_Msg_SaveT_PurchaseDetailADFAsync:
//                listener = mNetMap.get("TAG_SaveT_PurchaseDetailADFAsync");
//                break;
//            case RESULT_Msg_PostT_PurchaseDetailADFAsync:
//                listener = mNetMap.get("TAG_PostT_PurchaseDetailADFAsync");
//                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    /**
     * @desc: 保存表体明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/30 18:09
     */
    public void setOrderDetailList(List<OutStockOrderDetailInfo> list) {
        mOrderDetailList.clear();
        if (list != null && list.size() > 0) {
            mOrderDetailList.addAll(list);
        }
    }

    public List<OutStockOrderDetailInfo> getOrderDetailList() {
        return mOrderDetailList;
    }


    /**
     * @desc: 保存表头信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/30 18:09
     */
    public void setOrderHeaderInfo(OutStockOrderHeaderInfo orderHeaderInfo) {
        mOrderHeaderInfo = orderHeaderInfo;
    }

    public OutStockOrderHeaderInfo getOrderHeaderInfo() {
        return mOrderHeaderInfo;
    }

    /**
     * @desc: 获取单据明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestOrderDetail(OrderRequestInfo receiptModel, NetCallBackListener<String> callBackListener) {
        receiptModel.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE);
        mNetMap.put("TAG_GetT_PurchaseOrderListADFAsync", callBackListener);
        String modelJson = parseModelToJson(receiptModel);
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PurchaseOrderListADFAsync, mContext.getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), mContext, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null, UrlInfo.getUrl().GetT_PurchaseOrderListADFAsync, modelJson, null);
    }


}
