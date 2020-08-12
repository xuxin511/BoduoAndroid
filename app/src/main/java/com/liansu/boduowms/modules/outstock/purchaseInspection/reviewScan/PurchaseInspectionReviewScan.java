package com.liansu.boduowms.modules.outstock.purchaseInspection.reviewScan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.outstock.logisticsLoading.setting.LogisticsLoadingSetting;
import com.liansu.boduowms.ui.adapter.outstock.offscan.OtherLoadingDetailAdapter;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * @ Des: 市区和其他发货装车扫描
 * @ Created by yangyiqing on 2020/6/28.
 */
@ContentView(R.layout.activity_review_scan)
public class PurchaseInspectionReviewScan extends BaseActivity implements IPurchaseInspectionReviewScanView {
    Context mContext = this;
    @ViewInject(R.id.review_scan_list_view)
    ListView mList;
    @ViewInject(R.id.review_erp_voucher_no)
    EditText mErpVoucherNo;
    @ViewInject(R.id.review_scan_sum_qty)
    TextView mSumQty;
    @ViewInject(R.id.review_driver)
    EditText mDriverDesc;
    @ViewInject(R.id.review_scan_receiver)
    TextView mReceiver;
    @ViewInject(R.id.review_scan_receive_address)
    TextView mReceiveAddress;
    @ViewInject(R.id.review_scan_barcode)
    EditText mOutBarcode;
    @ViewInject(R.id.review_receive_logistics_company)
    EditText mLogisticsCompany;
    @ViewInject(R.id.review_receive_logistics_company_desc)
    TextView mLogisticsCompanyDesc;
    OtherLoadingDetailAdapter mAdapter;
    String                    mBusinessType;

    @Override
    public void onHandleMessage(Message msg) {

    }

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        initTitle();
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        onDriverInfoFocus();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void bindListView(List<OutStockOrderDetailInfo> list) {
        mAdapter = new OtherLoadingDetailAdapter(mContext, list);
        mList.setAdapter(mAdapter);
    }


    @Override
    public void setErpVoucherNoInfo(OutStockOrderHeaderInfo model) {
        mReceiver.setText(model.getContacts());
        mReceiveAddress.setText("收货地址:" + model.getAddress());
    }

    @Override
    public void setSumScanQty(int outBoxQty, int EANQty) {
        mSumQty.setText("外箱数:" + outBoxQty + "     散件数:" + EANQty);
    }

    @Override
    public String getDriverInfo() {
        return mDriverDesc.getText().toString().trim();
    }

    @Override
    public void onErpVoucherFocus() {
        CommonUtil.setEditFocus(mErpVoucherNo);
    }

    @Override
    public void onDriverInfoFocus() {
        CommonUtil.setEditFocus(mDriverDesc);
    }

    @Override
    public void onBarcodeFocus() {
        CommonUtil.setEditFocus(mOutBarcode);
    }

    @Override
    public void onQtyFocus() {

    }

    @Override
    public void setQty(float qty) {

    }

    @Override
    public void setQtyViewStatus(boolean isVisibility) {

    }

    @Override
    public void setLogisticsCompanyStatus(boolean isVisibility) {

    }

    @Override
    public void setReceiverStatus(boolean isVisibility) {

    }

    @Override
    public String getLogisticsCompany() {
        return null;
    }

    @Override
    public void onActivityFinish(String title) {
        new AlertDialog.Builder(BaseApplication.context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage(title + "是否返回上一页面？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 自动生成的方法
                        closeActivity();
                    }
                }).setNegativeButton("取消", null).show();
    }

    @Override
    public void onReset() {
        mDriverDesc.setText("");
        mErpVoucherNo.setText("");
        mLogisticsCompany.setText("");
        mOutBarcode.setText("");
        setSumScanQty(0,0);
        onDriverInfoFocus();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receiptbillchoice, menu);
        MenuItem setting = menu.findItem(R.id.action_filter);
        setting.setTitle("设置");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            Intent intent = new Intent(mContext, LogisticsLoadingSetting.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initTitle() {
        mBusinessType = getIntent().getStringExtra("BusinessType").toString();
        if (mBusinessType != null) {
            BaseApplication.toolBarTitle = new ToolBarTitle(mBusinessType, false);
        }
    }


}
