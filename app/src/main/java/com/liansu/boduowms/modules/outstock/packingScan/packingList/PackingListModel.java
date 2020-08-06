package com.liansu.boduowms.modules.outstock.packingScan.packingList;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/28.
 */
class PackingListModel extends BaseModel {
    private OutStockOrderHeaderInfo       mOrderInfo;
    private List<OutStockOrderDetailInfo> mPackingList = new ArrayList<>();

    public PackingListModel(Context context, MyHandler<BaseActivity> handler) {
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
//        super.onHandleMessage(msg);
    }

    public void setPackingList(List<OutStockOrderDetailInfo> list) {
        mPackingList.clear();
        if (list != null && list.size() > 0) {
            mPackingList.addAll(list);
        }
    }

    public List<OutStockOrderDetailInfo> getPackingList() {
        return mPackingList;
    }

    public void setOrderInfo(OutStockOrderHeaderInfo orderInfo){
        mOrderInfo=orderInfo;
    }

    public OutStockOrderHeaderInfo getOrderInfo(){
        return  mOrderInfo;
    }

    /**
     * @desc: 获取拼箱列表
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestPackingList(OutStockOrderHeaderInfo orderHeaderInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_PurchaseOrderListADFAsync", callBackListener);
        String modelJson = parseModelToJson(orderHeaderInfo);
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PurchaseOrderListADFAsync, mContext.getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), mContext, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null, UrlInfo.getUrl().GetT_PurchaseOrderListADFAsync, modelJson, null);
    }

    /**
     * @desc: 拆箱
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestDeletePackingInfo(OutStockOrderDetailInfo detailInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_PurchaseOrderListADFAsync", callBackListener);
        String modelJson = parseModelToJson(detailInfo);
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PurchaseOrderListADFAsync, mContext.getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), mContext, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null, UrlInfo.getUrl().GetT_PurchaseOrderListADFAsync, modelJson, null);
    }
}
