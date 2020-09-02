package com.liansu.boduowms.modules.instock.noSourceOtherStorage.scan;

import android.content.Context;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.modules.instock.salesReturn.scan.SalesReturnStorageScanModel;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class NoSourceOtherStorageScanModel extends SalesReturnStorageScanModel {

    public NoSourceOtherStorageScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    public void requestOrderRefer(List<OutBarcodeInfo> list, NetCallBackListener<String> callBackListener) {
        super.requestOrderRefer(list, callBackListener);
    }
}
