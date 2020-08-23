package com.liansu.boduowms.modules.qualityInspection.randomInspection.bill;

import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public interface IRandomInspectionBillView {
    void sumBillCount(int billCount);
    void onFilterContentFocus();
    void bindListView(List<QualityHeaderInfo> receiptModels);
    void onReset();
    void stopRefreshProgress();
    void startRefreshProgress();
}
