package com.liansu.boduowms.modules.outstock.logisticsLoading.setting;

import android.content.Context;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.debug.DebugModuleData;
import com.liansu.boduowms.ui.adapter.outstock.offscan.OffShelfScanDetailAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.ArrayList;

/**
 * @ Des: 发货装车设置
 * @ Created by yangyiqing on 2020/6/28.
 */
@ContentView(R.layout.logistics_loading_setting)
public class LogisticsLoadingSetting extends BaseActivity implements ILogisticsLoadingSettingView {
    Context mContext = this;

    OffShelfScanDetailAdapter mAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.appbar_title_logistics_loading_setting), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (DebugModuleData.isDebugDataStatusOffline()) {
//            ArrayList<OutStockTaskDetailsInfo_Model> list = DebugModuleData.loadDistributionOffShelfList();
//            bindListView(list);
//            setSumScanQty(9,50);
        }
    }

    @Override
    public void bindListView(ArrayList<OrderDetailInfo> list) {
        mAdapter = new OffShelfScanDetailAdapter(mContext, list);

    }

    @Override
    public void setErpVoucherNoInfo(OrderDetailInfo model) {

    }

    @Override
    public void setSumScanQty(int outBoxQty, int EANQty) {

    }
}
