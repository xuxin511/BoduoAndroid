package com.liansu.boduowms.modules.qualityInspection.randomInspection.scan;

import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/28.
 */
public interface IQualityInspectionView {
    void onErpVoucherNoFocus();
    void onAreaNoFocus();
    void onBarcodeFocus();
    void onQtyFocus();
    void setQty(String qty);
    float getQty();
    void setScannedQty(String qty);
    void setOrderInfo(QualityHeaderInfo info);
    void onActivityFinish();

}
