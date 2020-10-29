package com.liansu.boduowms.modules.inHouseStock.reprintLabel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.ui.adapter.inHouseStock.ReprintLabelAdapter;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.DateUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ Des:  托盘标签补打
 * @ Created by yangyiqing on 2020/8/23.
 */
@ContentView(R.layout.activity_reprint_pallet_label)
public class ReprintPalletLabel extends BaseActivity implements IReprintPalletLabelView, IUserSettingView {
    Context mContext = ReprintPalletLabel.this;
    @ViewInject(R.id.activity_query_stock_content)
    EditText     mBarcode;
    @ViewInject(R.id.reprint_pallet_label_material_no)
    EditText     mMaterialNo;
    @ViewInject(R.id.reprint_pallet_label_batch_no)
    EditText     mBatchNo;
    @ViewInject(R.id.reprint_pallet_label_area_no)
    EditText     mAreaNo;
    @ViewInject(R.id.activity_query_stock_recyclerView)
    RecyclerView mRecyclerView;
    @ViewInject(R.id.activity_query_stock_query_button)
    Button       mQueryButton;
    ReprintLabelAdapter mAdapter;
    protected UserSettingPresenter        mUserSettingPresenter;
    private   ReprintPalletLabelPresenter mPresenter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(getToolBarTitle(), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        mUserSettingPresenter = new UserSettingPresenter(mContext, this);
        mQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    mPresenter.onQueryInfoRefer();
                }
            }
        });
    }


    @Override
    protected void initData() {
        super.initData();
        if (mPresenter == null) {
            mPresenter = new ReprintPalletLabelPresenter(mContext, this, mHandler);
        }
        onMaterialNoFocus();
    }


    @Override
    public void onHandleMessage(Message msg) {
        if (mPresenter!=null){
            mPresenter.onHandleMessage(msg);
        }
    }

    @Override
    public void bindListView(List<StockInfo> list) {
        if (mAdapter == null) {
            mAdapter = new ReprintLabelAdapter(mContext, list);
            mAdapter.setRecyclerView(mRecyclerView);
            mAdapter.setOnItemClickListener(new ReprintLabelAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView parent, View view, int position, StockInfo data) {
                    if (mPresenter != null) {
                        mPresenter.onPrint(data);
                    }
                }
            });
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onBarcodeFocus() {
        CommonUtil.setEditFocus(mBarcode);
    }

    @Override
    public void onBatchNoFocus() {
        CommonUtil.setEditFocus(mBatchNo);
    }

    @Override
    public void onAreaNoFocus() {
        CommonUtil.setEditFocus(mAreaNo);
    }

    @Override
    public void onMaterialNoFocus() {
        CommonUtil.setEditFocus(mMaterialNo);
    }

    @Override
    public void setBatchNo(String batchNo) {
        mBatchNo.setText("" + batchNo);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Event(value = R.id.reprint_pallet_label_area_no, type = View.OnKeyListener.class)
    private boolean edtAreaNoScanClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            String areaNo = mAreaNo.getText().toString().trim();
            if (TextUtils.isEmpty(areaNo)) {
                CommonUtil.setEditFocus(mAreaNo);
                return true;
            }
            if (mPresenter != null) {
                mPresenter.scanAreaInfo(areaNo);
            }

        }
        return false;
    }

    /**
     * @desc:
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/7 12:45
     */
    @Event(value = {R.id.activity_query_stock_content, R.id.reprint_pallet_label_material_no, R.id.reprint_pallet_label_batch_no}, type = View.OnKeyListener.class)
    private boolean onScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            switch (v.getId()) {
                case R.id.activity_query_stock_content:
                    if (mPresenter != null) {
                        mPresenter.scanPalletBarcode(mBarcode.getText().toString().trim());
                    }
                    break;
                case R.id.reprint_pallet_label_material_no:
                    if (mPresenter != null) {
                        mPresenter.scanMaterialNo(mMaterialNo.getText().toString().trim());
                    }
                    break;
                case R.id.reprint_pallet_label_batch_no:
                    if (checkBatchNo(mBatchNo.getText().toString().trim())) {
                        onAreaNoFocus();
                    }
                    break;
            }

        }

        return false;
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
        getToolBarHelper().getToolBar().setTitle(getToolBarTitle());
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


    /**
     * @desc: 重置方法
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/23 22:23
     */
    @Override
    public void onReset() {
        mBarcode.setText("");
        mBatchNo.setText("");
        mMaterialNo.setText("");
        mAreaNo.setText("");
        if (mPresenter != null) {
            mPresenter.getModel().getStockInfoList().clear();
            bindListView(mPresenter.getModel().getStockInfoList());
        }
        onMaterialNoFocus();
    }

    @Override
    public String getBarcode() {
        return mBarcode.getText().toString().trim();
    }

    @Override
    public String getBatchNo() {
        return mBatchNo.getText().toString().trim();
    }


    @Override
    public boolean checkBatchNo(String batchNo) {
        if (!DateUtil.isBeforeOrCompareToday(batchNo.trim(), "yyyyMMdd")) {
            MessageBox.Show(mContext, "校验日期格式失败:[" + batchNo + "]" + "日期格式不正确或日期大于今天", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBatchNoFocus();
                }
            });
            return false;
        }
        return true;
    }
}
