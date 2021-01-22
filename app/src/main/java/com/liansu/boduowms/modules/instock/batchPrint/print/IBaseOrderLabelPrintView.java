package com.liansu.boduowms.modules.instock.batchPrint.print;

import com.liansu.boduowms.bean.order.OrderDetailInfo;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/14.
 */
public interface IBaseOrderLabelPrintView {
    void onBatchNoFocus();
    void onOrderRemainQtyFocus();
    void onPackQtyFocus();
    void onPalletQtyFocus();
    void onPrintCountFocus();
    void onReset();
    void setPrintInfoData(OrderDetailInfo printInfoData,int printType);
    void setViewStatus(int printType);
    String getBatchNo();
    float getRemainQty();
    float getPackQty();
    float getPalletQty();
    int  getPrintCount();
    boolean  checkBatchNo(String batchNo);
    float getOriginalRemainQty();
    boolean checkRemainQty();
}
