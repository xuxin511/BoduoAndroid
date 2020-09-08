package com.liansu.boduowms.modules.qualityInspection.bill;

import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public interface IQualityInspectionBillView {
    void sumBillCount(int billCount);
    void onFilterContentFocus();
    void bindListView(List<QualityHeaderInfo> receiptModels);
    void onReset();
    void stopRefreshProgress();
    void startRefreshProgress();
    String getErpVoucherNo();
}
