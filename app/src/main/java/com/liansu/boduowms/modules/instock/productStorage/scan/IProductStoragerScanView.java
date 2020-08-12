package com.liansu.boduowms.modules.instock.productStorage.scan;

import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.IBaseOrderScanView;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/16.
 */
public interface IProductStoragerScanView extends IBaseOrderScanView {
    void setTransferSubmissionStatus();

    void onErpVoucherNoFocus();
}
