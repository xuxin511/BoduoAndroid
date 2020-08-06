package com.liansu.boduowms.modules.outstock.purchaseInspection.bill;

import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public interface IPurchaseInspectionBillView {
    void sumBillCount(int billCount);
    void onFilterContentFocus();
    void bindListView(List<OutStockOrderHeaderInfo> receiptModels);
    void onReset();
    void stopRefreshProgress();
    void startRefreshProgress();
}
