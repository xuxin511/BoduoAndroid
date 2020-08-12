package com.liansu.boduowms.modules.instock.productionReturnsStorage.scan;

import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.IBaseOrderScanView;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/16.
 */
public interface IProductReturnStorageScanView extends IBaseOrderScanView {
    void onErpVoucherNoFocus();
}
