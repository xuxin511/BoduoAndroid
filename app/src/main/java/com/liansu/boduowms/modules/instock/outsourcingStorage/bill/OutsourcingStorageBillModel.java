package com.liansu.boduowms.modules.instock.outsourcingStorage.bill;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoiceModel;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/13.
 */
public class OutsourcingStorageBillModel extends BaseOrderBillChoiceModel {
    public        String TAG_GET_OUTSOURCING_STORAGE_LIST    = "ReceiptBillChoice_GetT_InStockList";
    private final int    RESULT_GET_OUTSOURCING_STORAGE_LIST = 111;
    public OutsourcingStorageBillModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }


    @Override
    protected void onHandleMessage(Message msg) {
        super.onHandleMessage(msg);
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_GET_OUTSOURCING_STORAGE_LIST:
                listener = mNetMap.get("TAG_GET_OUTSOURCING_STORAGE_LIST");
                break;

        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    /**
     * @desc: 获取委外订单
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 17:52
     */
    public void requestOutsourcingStorageList(OrderRequestInfo orderHeaderInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_OUTSOURCING_STORAGE_LIST", callBackListener);
//        receiptModel.setPcOrPda("0");
//        String ModelJson = GsonUtil.parseModelToJson(receiptModel);
        Map<String, String> params = new HashMap<>();
//        params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.mCurrentUserInfo));
//        params.put("ModelJson", ModelJson);
//        LogUtil.WriteLog(BaseOrderBillChoice.class, TAG_GetT_InStockList, ModelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InStockList, mContext.getString(R.string.Msg_GetT_InStockListADF), mContext, mHandler, RESULT_GET_OUTSOURCING_STORAGE_LIST, null, URLModel.GetURL().GetT_InStockListADF, params, null);

    }
}
