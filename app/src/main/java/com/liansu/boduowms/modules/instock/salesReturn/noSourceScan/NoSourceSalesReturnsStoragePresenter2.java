package com.liansu.boduowms.modules.instock.salesReturn.noSourceScan;

import android.content.Context;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan.IInStockReturnStorageScanView;
import com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan.InStockReturnsStorageScanModel;
import com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan.InStockReturnsStorageScanPresenter;
import com.liansu.boduowms.utils.hander.MyHandler;

/**
 * @ Des: 无源销退业务
 * @ Created by yangyiqing on 2020/9/20.
 */
public class NoSourceSalesReturnsStoragePresenter2 extends InStockReturnsStorageScanPresenter<IInStockReturnStorageScanView, NoSourceSalesReturnsStorageModel2> {
    public NoSourceSalesReturnsStoragePresenter2(Context context, IInStockReturnStorageScanView view, MyHandler<BaseActivity> handler, NoSourceSalesReturnsStorageModel2 model) {
        super(context, view, handler, model);
    }

    public NoSourceSalesReturnsStoragePresenter2(Context context, IInStockReturnStorageScanView view, MyHandler<BaseActivity> handler) {
        super(context, view, handler, new NoSourceSalesReturnsStorageModel2(context,handler, OrderType.IN_STOCK_ORDER_TYPE_NO_SOURCE_SALES_RETURN_STORAGE_VALUE, InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_NO_SOURCE));
    }


    @Override
    public void scanPalletBarcode(String scanBarcode) {
        super.scanPalletBarcode(scanBarcode);
    }

    @Override
    public void scanOuterBoxBarcode(String scanBarcode) {
        super.scanOuterBoxBarcode(scanBarcode);
    }



    @Override
    public void onResume() {
        super.onResume();
        String erpVoucherNo=mView.getErpVoucherNo();
        if (erpVoucherNo!=null && !erpVoucherNo.equals("")){
            getOrderDetailInfoList(erpVoucherNo);
        }else {
            mView.onErpVoucherNoFocus();
        }
    }
}
