package com.liansu.boduowms.modules.instock.baseOrderBusiness.bill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import com.liansu.boduowms.modules.instock.batchPrint.order.BaseOrderLabelPrintSelect;
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
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
public class BaseOrderBillChoice extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, IBaseOrderBillChoiceView , IUserSettingView {

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
    protected UserSettingPresenter        mUserSettingPresenter;


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        mBusinessType = getIntent().getStringExtra("BusinessType");
        if (mBusinessType != null) {
            mPresenter = BasePresenterFactory.getBaseOrderBillChoicePresenter(mContext, this, mHandler, mBusinessType);
            BaseApplication.toolBarTitle = new ToolBarTitle(getToolBarTitle(), false);
        }
        mUserSettingPresenter=new UserSettingPresenter(mContext,this);
        x.view().inject(this);
        initListener();
        mSwipeLayout.setEnabled(false);
        mEdtfilterContent.setImeOptions(EditorInfo.IME_ACTION_UNSPECIFIED);
        mSupplierNoFilter.setImeOptions(EditorInfo.IME_ACTION_UNSPECIFIED);
        closeKeyBoard(mEdtfilterContent,mSupplierNoFilter);
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
        setTitle();
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
                            receiptModel2.setErpvoucherno("");//erpvoucherno 传空
                        }
                        if (mPresenter != null) {
                            mPresenter.getOrderHeaderList2(receiptModel2,mSupplierNoFilter);
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
        intent.putExtra("Title",getTitleString());
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
        mSupplierNoFilter.setText("");
    }

    @Override
    public void selectWareHouse(List<String> list) {
        if (list != null && list.size() > 0) {
            final String[] items = list.toArray(new String[0]);
            new AlertDialog.Builder(mContext).setTitle(getResources().getString(R.string.activity_login_WareHousChoice))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setCancelable(true)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法存根
                            String select_item = items[which].toString();
                            if (mUserSettingPresenter != null) {
                                mUserSettingPresenter.saveCurrentWareHouse(select_item);
                            }

                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public void setTitle() {
        if (mPresenter!=null){
            getToolBarHelper().getToolBar().setTitle(getToolBarTitle());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        MenuItem menuItem=menu.findItem(R.id.menu_order_reprint);
        menuItem.setVisible(true);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_setting_warehouse_select) {
            selectWareHouse(mUserSettingPresenter.getModel().getWareHouseNoList());
        }else if (item.getItemId() == R.id.menu_order_reprint) {
            Intent intent = new Intent();
            intent.setClass(BaseOrderBillChoice.this, BaseOrderLabelPrintSelect.class);
            if (mPresenter!=null){
                intent.putExtra("VOUCHER_TYPE",getIntent().getIntExtra("VoucherType",-1));
            }

//            Bundle bundle = new Bundle();
//            bundle.putInt("inStockType", COMBINE_PALLET_TYPE_RECEIPTION);
//            bundle.putParcelable("orderHeader", mPresenter.getModel().getOrderHeaderInfo());
//            bundle.putParcelableArrayList("orderDetailList", (ArrayList<? extends Parcelable>) DebugModuleData.loadReceiptScanDetailList());
////        bundle.putParcelableArrayList("orderDetailList", mPresenter.getModel().getReceiptDetailModels());
//            intent.putExtras(bundle);
            startActivityLeft(intent);
        }
        return false;
    }

}

