package com.liansu.boduowms.modules.instock.baseOrderBusiness.bill;

import android.widget.EditText;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;

import java.util.ArrayList;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public interface IBaseOrderBillChoiceView {
    void sumBillCount(int billCount);
    void onFilterContentFocus();
    void bindListView(ArrayList<OrderHeaderInfo> receiptModels);
    void setViewStatus(EditText view,boolean isShow);
    void onReset();
    void StartScanIntent(OrderHeaderInfo orderHeaderInfo, ArrayList<OutBarcodeInfo> barCodeInfo);
    EditText getEditSupplierName();
}
