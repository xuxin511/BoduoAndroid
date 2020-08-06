package com.liansu.boduowms.modules.outstock.packingScan;

import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/28.
 */
public interface IPackingScanView {
    void bindListView(List<OutStockOrderDetailInfo> list);
    void onErpVoucherNoFocus();
    void onBarcodeNoFocus();
    void onQtyFocus();
    void createBatchNoListDialog(List<String> batchNoList);
    void setQtyViewStatus(boolean isVisibility);
    float getQty();
}
