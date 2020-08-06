package com.liansu.boduowms.modules.outstock.reviewScan;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.debug.DebugModuleData;
import com.liansu.boduowms.modules.outstock.logisticsLoading.setting.LogisticsLoadingSetting;
import com.liansu.boduowms.ui.adapter.outstock.offscan.OffShelfScanDetailAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * @ Des: 市区和其他发货装车扫描
 * @ Created by yangyiqing on 2020/6/28.
 */
@ContentView(R.layout.activity_review_scan)
public class ReviewScan extends BaseActivity implements IReviewScanView {
    Context mContext = this;
    @ViewInject(R.id.review_scan_list_view)
    ListView mList;
    @ViewInject(R.id.review_erp_voucher_no)
    EditText mErpVoucherNo;
    @ViewInject(R.id.review_scan_sum_qty)
    TextView mSumQty;
    @ViewInject(R.id.review_driver)
    EditText mDriverDesc;
    OffShelfScanDetailAdapter mAdapter;
    String mBusinessType ;
    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        initTitle();
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (DebugModuleData.isDebugDataStatusOffline()) {
            ArrayList<OrderDetailInfo> list = DebugModuleData.loadDistributionOffShelfList();
            bindListView(list);
            setSumScanQty(9, 50);
        }
    }

    @Override
    public void bindListView(ArrayList<OrderDetailInfo> list) {
        mAdapter = new OffShelfScanDetailAdapter(mContext, list);
        mList.setAdapter(mAdapter);
    }

    @Override
    public void setErpVoucherNoInfo(OrderHeaderInfo model) {
//        mVoucherNo.setText(model.getErpVoucherNo());
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
        if (mBusinessType!=null){
            BaseApplication.toolBarTitle = new ToolBarTitle(mBusinessType, false);
        }
    }
}
