package com.liansu.boduowms.modules.instock.baseOrderBusiness.scan;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public interface IBaseOrderScanView {
    void bindListView(List<OrderDetailInfo> receiptDetailModels);

    void onReset();

    void onBarcodeFocus();

    void onAreaNoFocus();

 

    void createDialog(OutBarcodeInfo info);

    String getAreaNo();
   void  setOrderHeaderInfo(OrderHeaderInfo info);
     <T extends IBaseOrderScanView > T getIView();
     void setSecondLineInfo(String desc,String name,boolean isVisibility);

     void onActivityFinish(String title) ;
}
