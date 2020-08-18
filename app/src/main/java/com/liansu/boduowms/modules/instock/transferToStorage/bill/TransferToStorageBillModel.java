package com.liansu.boduowms.modules.instock.transferToStorage.bill;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoiceModel;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.hander.MyHandler;

/**
 * @desc: 调拨入库列表
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/7/13 23:46
 */
public class TransferToStorageBillModel extends BaseOrderBillChoiceModel {
    public        String TAG_GetT_InStockList    = "ReceiptBillChoice_GetT_InStockList";
    private final int    RESULT_GetT_InStockList = 101;

    public TransferToStorageBillModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }


    @Override
    public void onHandleMessage(Message msg) {
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
     * @desc: 获取采购订单列表数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 17:52
     */
    public void requestTransferToStorageList(OrderRequestInfo orderHeaderInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_InStockList", callBackListener);
//        receiptModel.setPcOrPda("0");
//        String ModelJson = GsonUtil.parseModelToJson(receiptModel);
//        Map<String, String> params = new HashMap<>();
////        params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.mCurrentUserInfo));
//        params.put("ModelJson", ModelJson);
//        LogUtil.WriteLog(BaseOrderBillChoice.class, TAG_GetT_InStockList, ModelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InStockList, mContext.getString(R.string.Msg_GetT_InStockListADF), mContext, mHandler, RESULT_GetT_InStockList, null, URLModel.GetURL().GetT_InStockListADF, params, null);

    }
}
