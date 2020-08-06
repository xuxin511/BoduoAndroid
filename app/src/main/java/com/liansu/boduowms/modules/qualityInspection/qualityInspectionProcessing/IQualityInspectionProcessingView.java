package com.liansu.boduowms.modules.qualityInspection.qualityInspectionProcessing;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public interface IQualityInspectionProcessingView {
    void bindListView(List<QualityHeaderInfo> detailInfos);
    void onReset();
    void onBarcodeFocus();
    void onAreaNoFocus();
    void onQtyFocus();
    void setOrderInfo(QualityHeaderInfo headerInfo);
    void setOrderDetailInfo(QualityHeaderInfo detailInfo);
    int getOperationType();
    void setScanQty(float qty);
    void createDialog(OutBarcodeInfo info);
    void setViewStatus();
    String getAreaNo();



}
