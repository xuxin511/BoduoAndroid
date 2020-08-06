package com.liansu.boduowms.modules.instock.activeOtherStorage.bill;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoice;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoiceModel;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des: 有源杂入列表
 * @ Created by yangyiqing on 2020/7/13.
 */
public class ActiveOtherBillModel extends BaseOrderBillChoiceModel {
    public        String TAG_GET_T_OTHER_HEAD_LIST_ADF_ASYNC        = "ActiveOtherBillModel_GetT_OtherHeadListADFAsync"; //获取表头数据
    private final int    RESULT_TAG_GET_T_OTHER_HEAD_LIST_ADF_ASYNC = 124;

    public ActiveOtherBillModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }


    @Override
    protected void onHandleMessage(Message msg) {
        super.onHandleMessage(msg);
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_T_OTHER_HEAD_LIST_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_OTHER_HEAD_LIST_ADF_ASYNC");
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    /**
     * @desc: 获取有源表头列表数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 17:52
     */
    public void requestInStockList(OrderRequestInfo orderHeaderInfo, NetCallBackListener<String> callBackListener) {
        orderHeaderInfo.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE_VALUE);
        mNetMap.put("TAG_GET_T_OTHER_HEAD_LIST_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(orderHeaderInfo);
        LogUtil.WriteLog(BaseOrderBillChoice.class, TAG_GET_T_OTHER_HEAD_LIST_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_OTHER_HEAD_LIST_ADF_ASYNC, mContext.getString(R.string.message_request_get_order_header_info), mContext, mHandler, RESULT_TAG_GET_T_OTHER_HEAD_LIST_ADF_ASYNC, null, UrlInfo.getUrl().GetT_OtherHeadListADFAsync, modelJson, null);

    }
}
