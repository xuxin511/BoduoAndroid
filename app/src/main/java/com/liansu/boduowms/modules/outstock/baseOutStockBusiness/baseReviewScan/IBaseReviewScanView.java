package com.liansu.boduowms.modules.outstock.baseOutStockBusiness.baseReviewScan;

import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/28.
 */
public interface IBaseReviewScanView {
    void bindListView(List<OutStockOrderDetailInfo> list);
    void setErpVoucherNoInfo(OutStockOrderHeaderInfo model);
    void setSumScanQty(int outBoxQty,int EANQty);
    String  getDriverInfo();
    void onErpVoucherFocus();
    void  onDriverInfoFocus();
    void onBarcodeFocus();
    void onQtyFocus();
    void setQty(float qty);
    void setQtyViewStatus(boolean isVisibility);
    void setLogisticsCompanyStatus(boolean isVisibility);
    void setReceiverStatus(boolean isVisibility);
    String getLogisticsCompany();
    void  onActivityFinish(String title);
    void onReset();

}
