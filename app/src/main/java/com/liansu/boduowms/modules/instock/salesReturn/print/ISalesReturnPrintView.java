package com.liansu.boduowms.modules.instock.salesReturn.print;

import android.widget.EditText;

import com.liansu.boduowms.bean.order.OrderDetailInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/17.
 */
public interface ISalesReturnPrintView {
    void onCustomerNoFocus();
    void onStartTimeFocus();
    void onEndTimeFocus();
    void onMaterialNoFocus();
    void onPackQtyFocus();
    void onPrintCountFocus();
    void onRemainQtyFocus();
    void onPalletQtyFocus();
    void setMaterialInfo(OrderDetailInfo orderDetailInfo);
    void setSpinnerData(List<String> list);
    void createCalendarDialog(EditText editText);
    void setPackQty(float packQty);
    void setPackQtyEnable(boolean isEnable);
    void onReset();
    float getPalletRemainQty();
    float getPalletQty();
    String getStartTime();
    String getEndTime();


}
