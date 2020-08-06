package com.liansu.boduowms.modules.outstock.purchaseInspection.bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.outstock.purchaseInspection.scan.PurchaseInspectionProcessingScan;
import com.liansu.boduowms.ui.adapter.outstock.purchaseInspection.PurchaseInspectionBillItemAdapter;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @desc: 采购验退列表
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/7/19 15:40
 */
@ContentView(R.layout.activity_quality_inspection_bill_choice)
public class PurchaseInspectionBill extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, IPurchaseInspectionBillView {
    /*业务类型 */
    String                 businesType = "";
    Context                context     = PurchaseInspectionBill.this;
    PurchaseInspectionBillPresenter mPresenter;

    @Override
    public void onHandleMessage(Message msg) {
        mSwipeLayout.setRefreshing(false);
        if (mPresenter != null) {
            mPresenter.onHandleMessage(msg);
        }
    }

    @ViewInject(R.id.lsvChoiceReceipt)
    ListView           mListView;
    @ViewInject(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewInject(R.id.edt_filterContent)
    EditText           mEdtfilterContent;
    @ViewInject(R.id.txt_receipt_sumrow)
    TextView           mSumBillCount;
    PurchaseInspectionBillItemAdapter mAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        initTitle();

        x.view().inject(this);
        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter == null) {
            mPresenter = new PurchaseInspectionBillPresenter(context, this, mHandler);
        }
        onRefresh();


    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void initData() {
        super.initData();
        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
    }




    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            mPresenter.onReset();
            OutStockOrderHeaderInfo qualityHeaderInfo = new OutStockOrderHeaderInfo();
            mPresenter.getQualityInsHeaderList(qualityHeaderInfo);
        }
    }


    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvChoiceReceipt, type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OutStockOrderHeaderInfo receiptModel = (OutStockOrderHeaderInfo) mAdapter.getItem(position);

        try {
            StartScanIntent(receiptModel, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Event(value = R.id.edt_filterContent, type = View.OnKeyListener.class)
    private boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            String code = mEdtfilterContent.getText().toString().trim();
            if (code.equals("")) {
                return true;
            }
            if (code.length() < 20) {
                OutStockOrderHeaderInfo qualityHeaderInfo = new OutStockOrderHeaderInfo();
//                receiptModel.setStatus(1);
//                receiptModel.setErpVoucherNo(code);
                if (mPresenter != null) {
                    mPresenter.getQualityInsHeaderList(qualityHeaderInfo);
                }

            } else {
//                GetT_ErpVoucherNo(code);
            }

        }
        return false;
    }


    void StartScanIntent(OutStockOrderHeaderInfo headerInfo, ArrayList<OutBarcodeInfo> barCodeInfo) {
        Intent intent = new Intent(context, PurchaseInspectionProcessingScan.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("QUALITY_INSPECTION", headerInfo);
        intent.putExtras(bundle);
        startActivityLeft(intent);
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
    public void bindListView(List<OutStockOrderHeaderInfo> receiptModels) {
        mAdapter = new PurchaseInspectionBillItemAdapter(context, receiptModels);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onReset() {
        mEdtfilterContent.setText("");
    }

    @Override
    public void stopRefreshProgress() {
        //处理完业务后记得关闭，这里也得用post
        mSwipeLayout.post(new Runnable() {//刷新完成
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void startRefreshProgress() {
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });
    }

    public void initTitle(){
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.quality_inspection_processing_title), false);
    }
}
