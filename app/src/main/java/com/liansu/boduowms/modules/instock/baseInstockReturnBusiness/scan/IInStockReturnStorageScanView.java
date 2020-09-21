package com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/16.
 */
public interface IInStockReturnStorageScanView {
    void bindListView(List<OrderDetailInfo> list);

    void bindNoSourceListView(List<OutBarcodeInfo> list);

    void onErpVoucherNoFocus();

    void onPalletNoFocus();

    void onBarcodeFocus();

    void onAreaNoFocus();

    int getPalletType();

    void initViewStatus(int businessType);
    void initPalletChangedViewStatus(int palletType);

    void onReset();

    void onActivityFinish(String title);

    void startRollBackActivity(String erpVoucherNo, int voucherType, String title);

    String getErpVoucherNo();
    void setAreaNo(String areaNo);
    String getAreaNo();
    void setOrderHeaderInfo(OrderHeaderInfo orderHeaderInfo);


}
