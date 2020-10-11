package com.liansu.boduowms.modules.qualityInspection.bill;

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
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.BaseFragment;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.menu.MenuType;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
import com.liansu.boduowms.modules.menu.commonMenu.MenuModel;
import com.liansu.boduowms.modules.qualityInspection.qualityInspectionProcessing.QualityInspectionProcessingScan;
import com.liansu.boduowms.ui.adapter.quality_inspection.QualityInspectionBillItemAdapter;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @ Des:  质检合格列表
 * @ Created by yangyiqing on 2020/7/19.
 */
@ContentView(R.layout.activity_quality_inspection_bill_choice)
public class QualifiedFragmentBill extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IQualityInspectionBillView {
    Context                mContext;
    QualifiedBillPresenter mPresenter;
    @ViewInject(R.id.lsvChoiceReceipt)
    ListView           mListView;
    @ViewInject(R.id.inspection_bill_swipeLayout)
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
        mPresenter = new QualifiedBillPresenter(context, this, mHandler);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
    }

    @Override
    public void onStart() {
        super.onStart();
        onRefresh(false);
//        closeKeyBoard(mEdtfilterContent);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        onRefresh(true);
    }

    @Override
    public void onHandleMessage(Message message) {
        stopRefreshProgress();
        mPresenter.onHandleMessage(message);
    }

    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvChoiceReceipt, type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            QualityHeaderInfo receiptModel = (QualityHeaderInfo) mAdapter.getItem(position);
            StartScanIntent(receiptModel, null);
        } catch (Exception e) {
            MessageBox.Show(mContext, e.getMessage());
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
            if (code.length() < 25) {
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

    public QualifiedBillPresenter getPresenter() {
        return mPresenter;
    }


    void StartScanIntent(QualityHeaderInfo headerInfo, ArrayList<OutBarcodeInfo> barCodeInfo) {
        Intent intent = new Intent(mContext, QualityInspectionProcessingScan.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("QUALITY_INSPECTION", headerInfo);
        bundle.putString("QUALITY_TYPE", "QUALIFIED");
        intent.putExtra("Title", MenuModel.getSecondaryMenuModuleTitle(MenuType.MENU_TYPE_IN_STOCK, OrderType.IN_STOCK_ORDER_TYPE_QUALITY_INSPECTION_VALUE));
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
        mListView.setEnabled(true);
        mSwipeLayout.post(new Runnable() {//刷新完成
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void startRefreshProgress() {
        mListView.setEnabled(false);
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public String getErpVoucherNo() {
        return mEdtfilterContent.getText().toString().trim();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)// 如果为Enter键
        {

            String code = mEdtfilterContent.getText().toString().trim();
            if (code.equals("")) {
                return true;
            }
            if (code.length() < 25) {
                QualityHeaderInfo qualityHeaderInfo = new QualityHeaderInfo();
//                receiptModel.setStatus(1);
                qualityHeaderInfo.setErpvoucherno(code);
                if (mPresenter != null) {
                    mPresenter.getQualityInsHeaderList(qualityHeaderInfo);
                }

            } else {
                MessageBox.Show(mContext, "校验单据长度失败:" + "请扫描单据号");
            }

        }
        return false;
    }


    public void onRefresh(boolean isClearContent) {
        if (mPresenter != null) {
            if (isClearContent) {
                mPresenter.onReset();
            }
            final String content = mEdtfilterContent.getText().toString().trim();
            QualityHeaderInfo qualityHeaderInfo = new QualityHeaderInfo();
            qualityHeaderInfo.setLinestatus(0);
            qualityHeaderInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
            qualityHeaderInfo.setErpvoucherno(content);
            mPresenter.getQualityInsHeaderList(qualityHeaderInfo);
        }
    }
}
