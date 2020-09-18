package com.liansu.boduowms.modules.qualityInspection.randomInspection.bill;

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
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.ui.adapter.quality_inspection.RandomInspectionBillItemAdapter;
import com.liansu.boduowms.ui.dialog.MessageBox;
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
public class RandomInspectionBill extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, IRandomInspectionBillView, IUserSettingView {
    /*业务类型 */
    String                        businesType = "";
    Context                       mContext    = RandomInspectionBill.this;
    RandomInspectionBillPresenter mPresenter;
    protected UserSettingPresenter mUserSettingPresenter;

    @Override
    public void onHandleMessage(Message msg) {
        stopRefreshProgress();
        if (mPresenter != null) {
            mPresenter.onHandleMessage(msg);
        }
    }

    @ViewInject(R.id.lsvChoiceReceipt)
    ListView           mListView;
    @ViewInject(R.id.inspection_bill_swipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewInject(R.id.edt_filterContent)
    EditText           mEdtfilterContent;
    @ViewInject(R.id.txt_receipt_sumrow)
    TextView           mSumBillCount;
    RandomInspectionBillItemAdapter mAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(getToolBarTitle(), false);
        x.view().inject(this);
        mUserSettingPresenter = new UserSettingPresenter(mContext, this);
        closeKeyBoard(mEdtfilterContent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter == null) {
            mPresenter = new RandomInspectionBillPresenter(mContext, this, mHandler);
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
          final   String erpVoucherNo=mEdtfilterContent.getText().toString().trim();
            mPresenter.onReset();
            QualityHeaderInfo qualityHeaderInfo = new QualityHeaderInfo();
            qualityHeaderInfo.setErpvoucherno(erpVoucherNo);
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
            if (code.length() <= 25) {
                QualityHeaderInfo qualityHeaderInfo = new QualityHeaderInfo();
//                receiptModel.setStatus(1);
                qualityHeaderInfo.setErpvoucherno(code);
                if (mPresenter != null) {
                    mPresenter.getQualityInsHeaderList(qualityHeaderInfo);
                }

            } else {
                MessageBox.Show(mContext, "检验订单长度失败，请输入订单号", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onFilterContentFocus();
                    }
                });
//                GetT_ErpVoucherNo(code);
            }

        }
        return false;
    }


    void StartScanIntent(QualityHeaderInfo headerInfo, ArrayList<OutBarcodeInfo> barCodeInfo) {
        Intent intent = new Intent(mContext, QualityInspection.class);
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
        if (mAdapter == null) {
            mAdapter = new RandomInspectionBillItemAdapter(mContext, receiptModels);
            mAdapter.notifyDataSetChanged();
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onReset() {
//        mEdtfilterContent.setText("");
        onFilterContentFocus();
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
    public void selectWareHouse(List<String> list) {
        if (list != null && list.size() > 0) {
            final String[] items = list.toArray(new String[0]);
            new AlertDialog.Builder(mContext).setTitle(getResources().getString(R.string.activity_login_WareHousChoice))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setCancelable(false)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法存根
                            String select_item = items[which].toString();
                            if (mUserSettingPresenter != null) {
                                mUserSettingPresenter.saveCurrentWareHouse(select_item);
                            }
                            mEdtfilterContent.setText("");
                            onRefresh();
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public void setTitle() {
        if (mPresenter != null) {
            getToolBarHelper().getToolBar().setTitle(getToolBarTitle());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_setting_warehouse_select) {
            selectWareHouse(mUserSettingPresenter.getModel().getWareHouseNoList());
        }
        return false;
    }
}

