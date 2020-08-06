package com.liansu.boduowms.modules.outstock.logisticsLoading.setting;

import com.liansu.boduowms.bean.order.OrderDetailInfo;

import java.util.ArrayList;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/28.
 */
public interface ILogisticsLoadingSettingView {
    void bindListView(ArrayList<OrderDetailInfo> list);
    void setErpVoucherNoInfo(OrderDetailInfo model);
    void setSumScanQty(int outBoxQty, int EANQty);

}
