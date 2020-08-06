package com.liansu.boduowms.modules.outstock.purchaseReturn.purchaseInspection.scan;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public interface IPurchaseInspectionProcessingView {
    void onReset();
    void onBarcodeFocus();
    void onAreaNoFocus();
    void onPalletFocus();
    void onQtyFocus();
    void setOrderInfo(OutStockOrderHeaderInfo headerInfo);
    void setOrderDetailInfo(OutStockOrderDetailInfo detailInfo);
    int getOperationType();
    void setScanQty(float qty);
    void createDialog(OutBarcodeInfo info);
    void setViewStatus();
    String getAreaNo();
    void  onActivityFinish();
    void setQtyViewStatus(boolean isStatus);



}
