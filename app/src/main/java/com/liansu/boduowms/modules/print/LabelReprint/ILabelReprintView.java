package com.liansu.boduowms.modules.print.LabelReprint;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/17.
 */
interface ILabelReprintView {

    void onReset();

    void onErpVoucherNoFocus();

    void onMaterialNoFocus();

    void onBatchNoFocus();

    void onProductTimeFocus();

    void onQtyFocus();

    void onPrintCountFocus();


    void createDialog(OutBarcodeInfo info,int printType);


    void setSpinnerData();



    String getProductTimeValue();

    String getBatchNo();

    void setBatchNoList(List<String> batchNoList);
    String getPrintLabelType();
    void  setMaterialInfo(OutBarcodeInfo outBarcodeInfo);


}
