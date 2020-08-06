package com.liansu.boduowms.modules.outstock.reviewScan;

import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;

import java.util.ArrayList;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/28.
 */
public interface IReviewScanView {
    void bindListView(ArrayList<OrderDetailInfo> list);
    void setErpVoucherNoInfo(OrderHeaderInfo model);
    void setSumScanQty(int outBoxQty,int EANQty);
    String  getDriverInfo();

}
