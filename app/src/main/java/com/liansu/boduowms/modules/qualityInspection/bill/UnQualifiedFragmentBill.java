package com.liansu.boduowms.modules.qualityInspection.bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseFragment;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
import com.liansu.boduowms.modules.qualityInspection.qualityInspectionProcessing.QualityInspectionProcessingScan;
import com.liansu.boduowms.ui.adapter.quality_inspection.QualityInspectionBillItemAdapter;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/19.
 */

@ContentView(R.layout.activity_quality_inspection_bill_choice)
public class UnQualifiedFragmentBill extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IQualityInspectionBillView {
    Context                mContext;
    UnQualifiedBillPresenter mPresenter;
    @ViewInject(R.id.lsvChoiceReceipt)
    ListView           mListView;
    @ViewInject(R.id.mSwipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewInject(R.id.edt_filterContent)
    EditText           mEdtfilterContent;
    @ViewInject(R.id.txt_receipt_sumrow)
    TextView           mSumBillCount;
    QualityInspectionBillItemAdapter mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        // 在界面onAttach之后就触发初始化Presenter
        mPresenter = new UnQualifiedBillPresenter(context, this, mHandler);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
    }
    @Override
    public void onStart() {
        super.onStart();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
//            mPresenter.onReset();
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


    void StartScanIntent(QualityHeaderInfo headerInfo, ArrayList<OutBarcodeInfo> barCodeInfo) {
        Intent intent = new Intent(mContext, QualityInspectionProcessingScan.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("QUALITY_INSPECTION", headerInfo);
        bundle.putString("QUALITY_TYPE","UNQUALIFIED");
        intent.putExtras(bundle);
        mContext.startActivity(intent);
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
        mAdapter = new QualityInspectionBillItemAdapter(mContext, receiptModels);
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
}
