package com.liansu.boduowms.modules.instock.salesReturn.activeScan;

import android.content.Context;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan.InStockReturnsStorageScanModel;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.hander.MyHandler;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/9/20.
 */
 public class SalesReturnsStorageModel2 extends InStockReturnsStorageScanModel {
    public SalesReturnsStorageModel2(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }
    public SalesReturnsStorageModel2(Context context, MyHandler<BaseActivity> handler, int voucherType, int businessType) {
        super(context, handler,voucherType,businessType);
    }

    @Override
    public void requestOrderDetail(OrderRequestInfo orderRequestInfo, NetCallBackListener<String> callBackListener) {
//        super.requestOrderDetail(orderRequestInfo, callBackListener);
    }
}