package com.liansu.boduowms.modules.instock.baseOrderBusiness.bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.BasePresenterFactory;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.debug.DebugModuleData;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.ui.adapter.instock.baseScanStorage.OrderBillChioceItemAdapter;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


@ContentView(R.layout.activity_receipt_bill_choice)
public class BaseOrderBillChoice extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, IBaseOrderBillChoiceView {

    /*业务类型 */
    protected     String                       mBusinessType        = "";
    protected     Context                      mContext             = BaseOrderBillChoice.this;
    protected     boolean                      isCancelFilterButton = false; //供应商筛选标志
    protected     BaseOrderBillChoicePresenter mPresenter;
    private final int                          supplierRequestCode  = 1001;

    @Override
    public void onHandleMessage(Message msg) {
        mSwipeLayout.setRefreshing(false);
        if (mPresenter != null) {
            mPresenter.onHandleMessage(msg);
        }
    }


    @ViewInject(R.id.lsvChoiceReceipt)
    protected ListView                   lsvChoiceReceipt;
    @ViewInject(R.id.mSwipeLayout)
    protected SwipeRefreshLayout         mSwipeLayout;
    @ViewInject(R.id.edt_filterContent)
    protected EditText                   mEdtfilterContent;
    @ViewInject(R.id.txt_Suppliername)
    protected TextView                   txtSuppliername;
    protected MenuItem                   gMenuItem    = null;
    @ViewInject(R.id.txt_receipt_sumrow)
    protected TextView                   mSumBillCount;
    @ViewInject(R.id.edt_supplier_no)
    protected EditText                   mSupplierNoFilter;
    @ViewInject(R.id.query)
    protected Button                     mQuery;
    protected ArrayList<OrderHeaderInfo> receiptModels;//单据信息
    protected List<Map<String, String>>  SupplierList = new ArrayList<Map<String, String>>();//供应商列表
    protected OrderBillChioceItemAdapter mAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        mBusinessType = getIntent().getStringExtra("BusinessType").toString();
        if (mBusinessType != null) {
            mPresenter = BasePresenterFactory.getBaseOrderBillChoicePresenter(mContext, this, mHandler, mBusinessType);
            BaseApplication.toolBarTitle = new ToolBarTitle(mPresenter.getTitle(), false);
        }
        x.view().inject(this);
        initListener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isCancelFilterButton) {
            onRefresh();
        }
        if (DebugModuleData.isDebugDataStatusOffline()) {
            mPresenter.loadDebugData();
        }
        mPresenter.onResume();

    }

    @Override
    protected void initData() {
        super.initData();
        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
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
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    protected void initListener() {
        lsvChoiceReceipt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderHeaderInfo receiptModel = (OrderHeaderInfo) mAdapter.getItem(position);

                try {
                    StartScanIntent(receiptModel, null);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Event(value = {R.id.edt_filterContent, R.id.edt_supplier_no}, type = View.OnKeyListener.class)
    private boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            switch (v.getId()) {

                case R.id.query:
                    String code = mEdtfilterContent.getText().toString().trim();
                    String supplierNo = mSupplierNoFilter.getText().toString().trim();
//                    if (code.equals("")) {
//                        return true;
//                    }
                    if (code.length() > 0) {
                        OrderRequestInfo receiptModel = new OrderRequestInfo();
                        receiptModel.setErpvoucherno(code);
                        if (!supplierNo.equals("")) {
                            receiptModel.setSupplierno(supplierNo);
                        }


                        if (mPresenter != null) {
                            mPresenter.getOrderHeaderList(receiptModel);
                        }

                    } else {

                    }
                    break;

                case R.id.edt_filterContent:
                    String sErpVoucherNo = mEdtfilterContent.getText().toString().trim();
                    if (sErpVoucherNo.length() > 0) {
                        OrderRequestInfo receiptModel = new OrderRequestInfo();
                        if (!sErpVoucherNo.equals("")) {
                            receiptModel.setErpvoucherno(sErpVoucherNo);
                        }
                        if (mPresenter != null) {
                            mPresenter.getOrderHeaderList(receiptModel);
                        }
                        break;
                    }
                case R.id.edt_supplier_no:
                    String sSupplierNo = mSupplierNoFilter.getText().toString().trim();
                    if (sSupplierNo.length() > 0) {
                        OrderRequestInfo receiptModel2 = new OrderRequestInfo();
                        if (!sSupplierNo.equals("")) {
                            receiptModel2.setSupplierno(sSupplierNo);
                        }
                        if (mPresenter != null) {
                            mPresenter.getOrderHeaderList(receiptModel2);
                        }
                        break;

                    }

            }

        }
        return false;
    }

    @Event(value = R.id.query)
    private void onKey(View v) {

        String code = mEdtfilterContent.getText().toString().trim();
        String supplierNo = mSupplierNoFilter.getText().toString().trim();
//                    if (code.equals("")) {

        OrderRequestInfo receiptModel = new OrderRequestInfo();

        if (!code.equals("")) {
            receiptModel.setErpvoucherno(code);
        }

        if (!supplierNo.equals("")) {
            receiptModel.setSupplierno(supplierNo);
        }


        if (mPresenter != null) {
            mPresenter.getOrderHeaderList(receiptModel);
        }
        return;

    }

    @Override
    public void StartScanIntent(OrderHeaderInfo orderHeaderInfo, ArrayList<OutBarcodeInfo> barCodeInfo) {
        Intent intent = new Intent(mContext, BaseOrderScan.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("OrderHeaderInfo", orderHeaderInfo);
        bundle.putParcelableArrayList("barCodeInfo", barCodeInfo);
        intent.putExtra("BusinessType", mBusinessType);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }

    @Override
    public EditText getEditSupplierName() {
        return mSupplierNoFilter;
    }


    @Override
    public void sumBillCount(int billCount) {
        mSumBillCount.setText("合计:" + billCount);
    }

    @Override
    public void onFilterContentFocus() {
        CommonUtil.setEditFocus(mEdtfilterContent);
    }

    @Override
    public void bindListView(ArrayList<OrderHeaderInfo> receiptModels) {
        mAdapter = new OrderBillChioceItemAdapter(mContext, receiptModels);
        mAdapter.notifyDataSetChanged();
        lsvChoiceReceipt.setAdapter(mAdapter);
    }

    @Override
    public void setViewStatus(EditText view, boolean isShow) {
        if (isShow) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
        onFilterContentFocus();
    }

    @Override
    public void onReset() {
        mEdtfilterContent.setText("");
    }
}

