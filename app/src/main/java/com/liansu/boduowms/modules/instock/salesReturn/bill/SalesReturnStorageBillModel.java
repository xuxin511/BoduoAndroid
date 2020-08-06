package com.liansu.boduowms.modules.instock.salesReturn.bill;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoice;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoiceModel;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

/**
 * @ Des: 销售退货
 * @ Created by yangyiqing on 2020/7/13.
 */
public class SalesReturnStorageBillModel extends BaseOrderBillChoiceModel {
    public        String TAG_GetT_InStockList    = "ReceiptBillChoice_GetT_PurchaseListADFasync";
    private final int    RESULT_GetT_InStockList = 101;

    public SalesReturnStorageBillModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }


    @Override
    protected void onHandleMessage(Message msg) {
        super.onHandleMessage(msg);
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_GetT_InStockList:
                listener = mNetMap.get("TAG_GetT_InStockList");
                break;

        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    /**
     * @desc:   获取销售退货list
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 17:52
     */
    public void requestReturnStorageList(OrderRequestInfo orderHeaderInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_InStockList", callBackListener);
        String ModelJson = GsonUtil.parseModelToJson(orderHeaderInfo);
        LogUtil.WriteLog(BaseOrderBillChoice.class, TAG_GetT_InStockList, ModelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InStockList, mContext.getString(R.string.Msg_GetT_InStockListADF), mContext, mHandler, RESULT_GetT_InStockList, null, UrlInfo.getUrl().GetT_PurchaseListADFasync, ModelJson, null);
    }
}
