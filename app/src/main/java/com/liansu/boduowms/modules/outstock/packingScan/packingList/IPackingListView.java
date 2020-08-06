package com.liansu.boduowms.modules.outstock.packingScan.packingList;

import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/28.
 */
 public interface IPackingListView {
    void bindListView(List<OutStockOrderDetailInfo> list);
    void deletePackingInfo(OutStockOrderDetailInfo outStockOrderDetailInfo);
}
