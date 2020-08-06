package com.liansu.boduowms.modules.instock.productStorage.bill;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoice;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoicePresenter;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.IBaseOrderBillChoiceView;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

/**
 * @ Des:  产品入库列表
 * @ Created by yangyiqing on 2020/7/13.
 */
public class ProductStorageBillPresenter extends BaseOrderBillChoicePresenter<IBaseOrderBillChoiceView, ProductStorageBillModel> {

    public ProductStorageBillPresenter(Context context, IBaseOrderBillChoiceView view, MyHandler<BaseActivity> handler, String businssType) {
        super(context, view, new ProductStorageBillModel(context, handler));
    }


    @Override
    protected String getTitle() {
        return mContext.getResources().getString(R.string.appbar_title_product_storage_bill);
    }

    @Override
    protected void loadDebugData() {
//        ArrayList<OrderHeaderInfo> list = DebugModuleData.loadReceiptHeaderList(OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE);
//        mView.bindListView(list);
    }

    @Override
    public void getOrderHeaderList(OrderRequestInfo orderHeaderInfo) {
        orderHeaderInfo.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_VALUE);
        mModel.requestProductStorageOrderList(orderHeaderInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(BaseOrderBillChoice.class, mModel.TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC, result);
                    BaseResultInfo<List<OrderHeaderInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OrderHeaderInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == BaseResultInfo.RESULT_TYPE_OK) {
                        mModel.setOrderInfoList(returnMsgModel.getData());
//                        if (DebugModuleData.isDebugDataStatusOffline()) {
//                            mModel.setOrderInfoList(DebugModuleData.loadReceiptHeaderList(OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE));
//                        }
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
