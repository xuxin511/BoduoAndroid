package com.liansu.boduowms.modules.instock.productionReturnsStorage.bill;

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
 * @ Des:
 * @ Created by yangyiqing on 2020/7/13.
 */
public class ProductionReturnsStorageBillModel extends BaseOrderBillChoiceModel {
    public        String TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC = "ProductionReturnsStorageBillModel_GetT_WorkOrderHeadListADFAsync";
    private final int    RESULT_TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC = 122;

    public ProductionReturnsStorageBillModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }


    @Override
    public void onHandleMessage(Message msg) {
        super.onHandleMessage(msg);
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC");
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
    public void requestOrderDetailList(OrderRequestInfo orderHeaderInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC", callBackListener);
        String ModelJson = GsonUtil.parseModelToJson(orderHeaderInfo);
        LogUtil.WriteLog(BaseOrderBillChoice.class, TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC, ModelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC, mContext.getString(R.string.MSG_GET_FINISHED_PRODUCT), mContext, mHandler, RESULT_TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC, null, UrlInfo.getUrl().GetT_WorkOrderHeadListADFAsync, ModelJson, null);

    }
}
