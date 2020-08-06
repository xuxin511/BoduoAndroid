package com.liansu.boduowms.modules.instock.noSourceOtherStorage.scan;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/17.
 */
interface INoSourceOtherScanView {
    void bindListView(List<OutBarcodeInfo> materialItemList);

    void onReset();

    void onBarcodeFocus();

    void onAreaNoFocus();
    void setCurrentBarcodeInfo(OutBarcodeInfo info);

    void createDialog(OutBarcodeInfo info);

    String getAreaNo();

    void setSpinnerData();
    String getSpinnerItemValue();


}
