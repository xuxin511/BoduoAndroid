package com.liansu.boduowms.modules.instock.transferToStorage.scan;

import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.IBaseOrderScanView;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/16.
 */
public interface TransferToStorageScanView extends IBaseOrderScanView {
    void setTransferSubmissionStatus();
    void onErpVoucherNoFocus();
}
