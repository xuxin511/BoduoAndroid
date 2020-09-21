package com.liansu.boduowms.modules.instock.productionReturnsStorage.returnScan;

import android.content.Context;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan.IInStockReturnStorageScanView;
import com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan.InStockReturnsStorageScanModel;
import com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan.InStockReturnsStorageScanPresenter;
import com.liansu.boduowms.utils.hander.MyHandler;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/9/20.
 */
public class ProductionReturnsStoragePresenter2 extends InStockReturnsStorageScanPresenter<IInStockReturnStorageScanView,ProductionReturnsStorageModel2> {
    public ProductionReturnsStoragePresenter2(Context context, IInStockReturnStorageScanView view, MyHandler<BaseActivity> handler, ProductionReturnsStorageModel2 model) {
        super(context, view, handler, model);
    }

    public ProductionReturnsStoragePresenter2(Context context, IInStockReturnStorageScanView view, MyHandler<BaseActivity> handler) {
        super(context, view, handler, new ProductionReturnsStorageModel2(context,handler, OrderType.IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE_VALUE, InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_ACTIVE));
    }

    @Override
    public void getAreaInfo(String areaNo) {
        super.getAreaInfo(areaNo);
    }

    @Override
    protected void getOrderDetailInfoList(String erpVoucherNo) {
        super.getOrderDetailInfoList(erpVoucherNo);
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
    public void onActiveCombinePalletRefer(OutBarcodeInfo palletInfo) {
        super.onActiveCombinePalletRefer(palletInfo);
    }
}
