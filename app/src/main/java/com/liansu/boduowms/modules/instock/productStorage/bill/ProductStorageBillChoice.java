package com.liansu.boduowms.modules.instock.productStorage.bill;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoice;
import com.liansu.boduowms.modules.instock.productStorage.scan.ProductStorageScan;
import com.liansu.boduowms.ui.adapter.instock.baseScanStorage.OrderBillChioceItemAdapter;

import java.util.ArrayList;

/**
 * @ Des: 生产订单列表
 * @ Created by yangyiqing on 2020/7/15.
 */

public class ProductStorageBillChoice extends BaseOrderBillChoice {
    OrderBillChioceItemAdapter mBillAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        setViewStatus(mSupplierNoFilter, false);
    }

    @Override
    public void StartScanIntent(OrderHeaderInfo orderHeaderInfo, ArrayList<OutBarcodeInfo> barCodeInfo) {
        Intent intent = new Intent(mContext, ProductStorageScan.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("OrderHeaderInfo", orderHeaderInfo);
        bundle.putParcelableArrayList("barCodeInfo", barCodeInfo);
        intent.putExtra("BusinessType", mBusinessType);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }

    @Override
    public void bindListView(ArrayList<OrderHeaderInfo> receiptModels) {
        mBillAdapter = new OrderBillChioceItemAdapter(mContext, receiptModels);
        mBillAdapter.notifyDataSetChanged();
        lsvChoiceReceipt.setAdapter(mBillAdapter);

    }

    @Override
    protected void initListener() {
        lsvChoiceReceipt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderHeaderInfo receiptModel = (OrderHeaderInfo) mBillAdapter.getItem(position);

                try {
                    StartScanIntent(receiptModel, null);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (isCancelFilterButton) {
            isCancelFilterButton = false;
            gMenuItem.setTitle(getResources().getString(R.string.filter));
            txtSuppliername.setText(getResources().getString(R.string.supplierNoFilter));
        }

        if (mPresenter != null) {
            mPresenter.onReset();
            if (mBillAdapter != null) {
                mBillAdapter.notifyDataSetChanged();
            }
        }
    }
}
