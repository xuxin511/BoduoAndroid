package com.liansu.boduowms.modules.instock.productStorage.printPalletScan;

import com.liansu.boduowms.bean.order.OrderDetailInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/16.
 */
public interface IPrintPalletScanView  {
    void bindListView(List<OrderDetailInfo> receiptDetailModels);

    void onReset();

    void onBarcodeFocus();
    void onErpVoucherNo();
    void  setErpVoucherNo(String erpVoucherNo);
    void createDialog(OrderDetailInfo info);

}
