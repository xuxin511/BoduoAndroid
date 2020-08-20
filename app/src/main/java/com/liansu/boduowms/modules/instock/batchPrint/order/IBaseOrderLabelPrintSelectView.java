package com.liansu.boduowms.modules.instock.batchPrint.order;

import com.liansu.boduowms.bean.order.OrderDetailInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/14.
 */
public interface IBaseOrderLabelPrintSelectView {
    void onErpVoucherNoFocus();

    void onMaterialFocus();

    int getPrintType();

    String getVoucherTypeName();

    void onReset(boolean isReset);

    void bindListView(List<OrderDetailInfo> orderDetailInfos);

    void setSpinnerData(List<String> list);

    void StartScanIntent(OrderDetailInfo orderDetailInfo);

    void initViewStatus(int printType);
}
