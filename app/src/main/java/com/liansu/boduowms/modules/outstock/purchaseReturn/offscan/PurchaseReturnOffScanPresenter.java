package com.liansu.boduowms.modules.outstock.purchaseReturn.offscan;

import android.content.Context;

import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.BaseOutStockBusinessPresenter;
import com.liansu.boduowms.utils.hander.MyHandler;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/6.
 */
public class PurchaseReturnOffScanPresenter extends BaseOutStockBusinessPresenter<IPurchaseReturnOffScanView, PurchaseReturnOffScanModel> {
    public PurchaseReturnOffScanPresenter(Context context, IPurchaseReturnOffScanView view, MyHandler handler) {
        super(context, view, handler, new PurchaseReturnOffScanModel(context, handler));
    }


}
