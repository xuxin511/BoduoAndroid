package com.liansu.boduowms.modules.instock.salesReturn.bill;

import android.content.Intent;
import android.os.Bundle;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoice;
import com.liansu.boduowms.modules.instock.salesReturn.scan.SalesReturnStorageScan;

import java.util.ArrayList;

/**
 * @ Des: 销售退货列表
 * @ Created by yangyiqing on 2020/7/15.
 */

public class SalesReturnBillChoice extends BaseOrderBillChoice {



    @Override
    public void StartScanIntent(OrderHeaderInfo orderHeaderInfo, ArrayList<OutBarcodeInfo> barCodeInfo) {
        Intent intent = new Intent(mContext, SalesReturnStorageScan.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("OrderHeaderInfo", orderHeaderInfo);
        bundle.putParcelableArrayList("barCodeInfo", barCodeInfo);
        intent.putExtra("BusinessType",mBusinessType);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }
}
