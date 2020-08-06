package com.liansu.boduowms.modules.outstock.packingScan.packingList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.ui.adapter.outstock.packing.PackingListAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * @ Des:  拼箱列表
 * @ Created by yangyiqing on 2020/7/28.
 */
@ContentView(R.layout.activity_packing_list)
public class PackingList extends BaseActivity implements IPackingListView {
    @ViewInject(R.id.packing_list)
    ListView mListView;
    Context              mContext = PackingList.this;
    PackingListAdapter   mAdapter;
    PackingListPresenter mPresenter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.packing_list), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;

    }


    @Override
    protected void initData() {
        super.initData();
        mPresenter = new PackingListPresenter(mContext, this, mHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        if (mPresenter != null) {
            mPresenter.getOrderDetailList();
        }
    }


    @Override
    public void bindListView(List<OutStockOrderDetailInfo> list) {
        if (list != null && list.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            mAdapter = new PackingListAdapter(mContext, list);
            mListView.setAdapter(mAdapter);
        } else {
            mListView.setVisibility(View.GONE);
        }

    }

    @Override
    public void deletePackingInfo(final OutStockOrderDetailInfo orderDetailInfo) {
        new AlertDialog.Builder(mContext).setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除拼箱号为:" + orderDetailInfo.getErpvoucherno() + "的数据")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deletePackingNoInfo(orderDetailInfo);
                    }
                }).setNegativeButton("取消", null).show();
    }


    @Event(value = R.id.packing_list, type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OutStockOrderDetailInfo outStockTaskDetailsInfoModel = (OutStockOrderDetailInfo) mAdapter.getItem(position);
        deletePackingInfo(outStockTaskDetailsInfoModel);
    }

}
