package com.liansu.boduowms.modules.outstock.baseOutStockBusiness.baseReviewScan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.BasePresenterFactory;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.outstock.logisticsLoading.setting.LogisticsLoadingSetting;
import com.liansu.boduowms.ui.adapter.outstock.offscan.OtherLoadingDetailAdapter;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * @ Des: 发货装车扫描基类
 * @ Created by yangyiqing on 2020/6/28.
 */
@ContentView(R.layout.activity_review_scan)
public class BaseReviewScan extends BaseActivity implements IBaseReviewScanView {
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
    @ViewInject(R.id.review_scan_receiver_desc)
    TextView  mReceiverDesc;
    @ViewInject(R.id.review_scan_barcode)
    EditText mOutBarcode;
    @ViewInject(R.id.review_refer)
    Button   mRefer;
    @ViewInject(R.id.review_scan_qty_desc)
    TextView mQtyDesc;
    @ViewInject(R.id.review_scan_qty)
    EditText mQty;
    OtherLoadingDetailAdapter mAdapter;
    String                    mBusinessType;
    @ViewInject(R.id.review_receive_logistics_company)
    EditText mLogisticsCompany;
    @ViewInject(R.id.review_receive_logistics_company_desc)
    TextView mLogisticsCompanyDesc;
    protected BaseReviewScanPresenter mPresenter;

    @Override
    public void onHandleMessage(Message msg) {
        if (mPresenter != null) {
            mPresenter.onHandleMessage(msg);
        }

    }

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        initTitle();
        initPresenter();
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
       setQtyViewStatus(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        if (mPresenter!=null){
            mPresenter.onResume();
        }
    }

    /**
     * @desc: 过账提交
     * @param:
     * @return:
     * @author:
     * @time 2020/4/20 17:16
     */
    @Event(value = {R.id.review_refer}, type = CompoundButton.OnClickListener.class)
    private void onRefer(View view) {
        mPresenter.onOrderRefer();
    }

    @Override
    public void bindListView(List<OutStockOrderDetailInfo> list) {
        mAdapter = new OtherLoadingDetailAdapter(mContext, list);
        mList.setAdapter(mAdapter);
    }


    @Override
    public void setErpVoucherNoInfo(OutStockOrderHeaderInfo model) {
        mReceiver.setText(model.getContacts());
        mReceiveAddress.setText("收货地址:" + (model.getAddress() == null ? "" : model.getAddress()));
    }

    @Override
    public void setSumScanQty(float outBoxQty, float EANQty) {
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
        CommonUtil.setEditFocus(mQty);
    }

    @Override
    public void setQty(float qty) {
        mQty.setText(qty + "");
    }

    @Override
    public void setQtyViewStatus(boolean isVisibility) {
        if (isVisibility) {
            if (mQty.getVisibility() != View.VISIBLE) {
                mQty.setVisibility(View.VISIBLE);
                mQtyDesc.setVisibility(View.VISIBLE);
            }
        } else {
            if (mQty.getVisibility() != View.GONE) {
                mQty.setVisibility(View.GONE);
                mQtyDesc.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setLogisticsCompanyStatus(boolean isVisibility) {
        if (isVisibility) {
            if (mLogisticsCompany.getVisibility() != View.VISIBLE) {
                mLogisticsCompany.setVisibility(View.VISIBLE);
                mLogisticsCompanyDesc.setVisibility(View.VISIBLE);
            }
        } else {
            if (mLogisticsCompany.getVisibility() != View.GONE) {
                mLogisticsCompany.setVisibility(View.GONE);
                mLogisticsCompanyDesc.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setReceiverStatus(boolean isVisibility) {
        if (isVisibility) {
            if (mReceiver.getVisibility() != View.VISIBLE) {
                mReceiver.setVisibility(View.VISIBLE);
                mReceiverDesc.setVisibility(View.VISIBLE);
                mReceiveAddress.setVisibility(View.VISIBLE);
            }
        } else {
            if (mReceiver.getVisibility() != View.GONE) {
                mReceiver.setVisibility(View.GONE);
                mReceiverDesc.setVisibility(View.GONE);
                mReceiveAddress.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public String getLogisticsCompany() {
        return mLogisticsCompany.getText().toString();
    }

    @Override
    public void onActivityFinish(String title) {
        new AlertDialog.Builder(BaseApplication.context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage(title+"是否返回上一页面？")
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receiptbillchoice, menu);
        MenuItem setting = menu.findItem(R.id.action_filter);
        setting.setTitle("设置");
        setting.setVisible(false);
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

    protected void initPresenter() {
        mPresenter = BasePresenterFactory.getBaseReviewScanPresenter(mContext, this, mHandler, mBusinessType);
    }

    @Event(value = {R.id.review_erp_voucher_no, R.id.review_driver, R.id.review_scan_barcode, R.id.review_scan_qty}, type = View.OnKeyListener.class)
    private boolean onScanClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            switch (v.getId()) {
                case R.id.review_erp_voucher_no:
                    String erpVoucherNo = mErpVoucherNo.getText().toString().trim();
                    mPresenter.getOrderDetailInfoList(erpVoucherNo);
                    break;
                case R.id.review_scan_barcode:
                    String barcode = mOutBarcode.getText().toString().trim();
                    mPresenter.onScan(barcode);
                    break;
                case R.id.review_scan_qty:
                    float qty=Float.parseFloat(mQty.getText().toString().trim());
                    OutBarcodeInfo info=new OutBarcodeInfo();
                    info.setBarcode(mOutBarcode.getText().toString().trim());
                    info.setQty(qty);
                    mPresenter.onSparePartsBarcodeScan(info);
                    break;
            }

        }
        return false;
    }


}
