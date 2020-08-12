package com.liansu.boduowms.modules.instock.productionReturnsStorage.print;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/17.
 */
public interface IProductionReturnsStorageView {
    void onErpVoucherNoFocus();
    void createDialog(OutBarcodeInfo info);
   void  bindListView(List<OrderDetailInfo> orderDetailInfos);
    void onReset();




}
