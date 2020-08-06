package com.liansu.boduowms.modules.qualityInspection.randomInspection.bill;

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
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
import com.liansu.boduowms.modules.qualityInspection.randomInspection.scan.QualityInspection;
import com.liansu.boduowms.ui.adapter.quality_inspection.RandomInspectionBillItemAdapter;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @desc: 抽检单列表  activity 格式
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/7/19 15:40
 */
@ContentView(R.layout.activity_quality_inspection_bill_choice)
public class RandomInspectionBill extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, IRandomInspectionBillView {
    /*业务类型 */
    String                        businesType = "";
    Context                       context     = RandomInspectionBill.this;
    RandomInspectionBillPresenter mPresenter;

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
    RandomInspectionBillItemAdapter mAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.quality_inspection_title_list_name) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename(), false);
        x.view().inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter == null) {
            mPresenter = new RandomInspectionBillPresenter(context, this, mHandler);
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
//        businesType = getIntent().getStringExtra("BusinesType").toString();
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            mPresenter.onReset();
            QualityHeaderInfo qualityHeaderInfo = new QualityHeaderInfo();
            mPresenter.getQualityInsHeaderList(qualityHeaderInfo);
        }
    }


    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvChoiceReceipt, type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        QualityHeaderInfo receiptModel = (QualityHeaderInfo) mAdapter.getItem(position);

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
                QualityHeaderInfo qualityHeaderInfo = new QualityHeaderInfo();
//                receiptModel.setStatus(1);
                qualityHeaderInfo.setErpvoucherno(code);
                if (mPresenter != null) {
                    mPresenter.getQualityInsHeaderList(qualityHeaderInfo);
                }

            } else {
//                GetT_ErpVoucherNo(code);
            }

        }
        return false;
    }


    void StartScanIntent(QualityHeaderInfo headerInfo, ArrayList<OutBarcodeInfo> barCodeInfo) {
        Intent intent = new Intent(context, QualityInspection.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("qualityInspection", headerInfo);
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
    public void bindListView(List<QualityHeaderInfo> receiptModels) {
        mAdapter = new RandomInspectionBillItemAdapter(context, receiptModels);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onReset() {
        mEdtfilterContent.setText("");
    }
}

