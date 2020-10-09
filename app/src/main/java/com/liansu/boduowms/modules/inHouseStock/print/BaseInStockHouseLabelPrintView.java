package com.liansu.boduowms.modules.inHouseStock.print;

import com.liansu.boduowms.bean.order.OrderDetailInfo;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/14.
 */
public interface BaseInStockHouseLabelPrintView {
    void onMaterialFocus();
    void onBatchNoFocus();
    void onOrderRemainQtyFocus();
    void onPackQtyFocus();
    void onPalletQtyFocus();
    void onPrintCountFocus();
    void onReset();
    void setPrintInfoData(OrderDetailInfo printInfoData, int printType);
    void setViewStatus(int printType);
    String getBatchNo();
    float getRemainQty();
    float getPackQty();
    float getPalletQty();
    int  getPrintCount();
    boolean checkBatchNo(String batchNo);
    int getPrintType();
    void setMaterialDesc(String materialDesc);
    String getMaterialNo();
}
