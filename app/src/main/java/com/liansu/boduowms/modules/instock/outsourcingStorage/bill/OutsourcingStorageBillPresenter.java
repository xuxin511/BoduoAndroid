package com.liansu.boduowms.modules.instock.outsourcingStorage.bill;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.debug.DebugModuleData;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoice;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoicePresenter;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.IBaseOrderBillChoiceView;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/13.
 */
public class OutsourcingStorageBillPresenter extends BaseOrderBillChoicePresenter<IBaseOrderBillChoiceView, OutsourcingStorageBillModel> {

    public OutsourcingStorageBillPresenter(Context context, IBaseOrderBillChoiceView view, MyHandler<BaseActivity> handler, String businssType) {
        super(context, view, new OutsourcingStorageBillModel(context, handler));
    }


    @Override
    protected String getTitle() {
        return mContext.getResources().getString(R.string.appbar_title_outsourcing_orders);
    }


    @Override
    protected void loadDebugData() {
        ArrayList<OrderHeaderInfo> list = DebugModuleData.loadReceiptHeaderList(OrderType.IN_STOCK_ORDER_TYPE_OUTSOURCING_STORAGE);
        mView.bindListView(list);
    }

    @Override
    public void getOrderHeaderList(OrderRequestInfo orderHeaderInfo) {
        mModel.requestOutsourcingStorageList(orderHeaderInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(BaseOrderBillChoice.class, mModel.TAG_GetT_InStockList, result);
                    BaseResultInfo<List<OrderHeaderInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OrderHeaderInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult()==1) {
                        mModel.setOrderInfoList(returnMsgModel.getData());
                        if (DebugModuleData.isDebugDataStatusOffline()) {
                            mModel.setOrderInfoList(DebugModuleData.loadReceiptHeaderList(OrderType.IN_STOCK_ORDER_TYPE_OUTSOURCING_STORAGE));
                        }
                        if (mModel.getOrderHeaderInfotList().size() != 0 && mModel.getBarCodeList().size() != 0) {
                        } else {
                            mView.sumBillCount(mModel.getOrderHeaderInfotList().size());
                            mView.bindListView(mModel.getOrderHeaderInfotList());
                        }

                    } else {
                        ToastUtil.show(returnMsgModel.getResultValue());
                    }
                } catch (Exception ex) {
                    ToastUtil.show(ex.getMessage());
                }
                mView.onFilterContentFocus();
            }
        });
    }
}
