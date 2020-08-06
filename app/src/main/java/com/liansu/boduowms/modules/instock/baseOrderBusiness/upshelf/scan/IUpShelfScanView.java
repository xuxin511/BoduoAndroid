package com.liansu.boduowms.modules.instock.baseOrderBusiness.upshelf.scan;

import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;

import java.util.ArrayList;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public interface IUpShelfScanView {
    void bindListView(ArrayList<OrderDetailInfo> receiptDetailModels);
    void onReset();
    void onBarcodeFocus();
    void onAreaNoFocus();
    void setReceiptHeaderInfo(OrderHeaderInfo receipt_model);
}
