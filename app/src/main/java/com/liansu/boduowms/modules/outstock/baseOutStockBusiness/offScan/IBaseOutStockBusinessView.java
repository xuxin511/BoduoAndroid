package com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan;

import android.widget.ToggleButton;

import com.liansu.boduowms.base.IBaseView;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/28.
 */
public interface IBaseOutStockBusinessView extends IBaseView {
    void bindListView(List<OutStockOrderDetailInfo> list);

    void setOrderHeaderInfo(OutStockOrderHeaderInfo model);

    void onErpVoucherNoFocus();

    void onOrderAddressFocus();


    void onFatherBarcodeFocus();

    void onSubBarcodeFocus();

    void onQtyFocus();

    void setQty(float qty);

    void createOrderAddressListDialog(List<String> addressList);
    void createMultipleBatchesSelectDialog(List<String> batches);
    int getScanType();
   float getScanQty();
    void initViewStatus();
   void onReset();
    void selectScanType(ToggleButton view, boolean viewStatus);


}
