package com.liansu.boduowms.modules.inHouseStock.inventoryMovement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.ui.adapter.instock.InventoryMovementScanDetailAdapter;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.DoubleClickCheck;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * @desc: 移库
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/6/25 22:00
 */
@ContentView(R.layout.activity_inner_move_scan)
public class InventoryMovementScan extends BaseActivity implements IInventoryMovementView , IUserSettingView {
    @ViewInject(R.id.inner_move_barcode_scan)
    EditText     mBarcode;
    @ViewInject(R.id.inner_move_in_stock_scan)
    EditText     mMoveInAreaNo;
    @ViewInject(R.id.inner_move_out_stock_area_no)
    EditText     mMoveOutAreaNo;
    @ViewInject(R.id.inventory_movement_qty)
    EditText     mQty;
    @ViewInject(R.id.txt_Company)
    TextView     mStrongHoldName;
    @ViewInject(R.id.txt_Batch)
    TextView     mBatchNo;
    @ViewInject(R.id.txt_Status)
    TextView     mStatus;
    @ViewInject(R.id.txt_MaterialName)
    TextView     mMaterialName;
    @ViewInject(R.id.inventory_movement_refer)
    Button       mRefer;
    @ViewInject(R.id.inventory_movement_list_view)
    RecyclerView mRecyclerView;
    Context                            mContext = InventoryMovementScan.this;
    InventoryMovementPresenter         mPresenter;
    InventoryMovementScanDetailAdapter mAdapter;
    protected UserSettingPresenter  mUserSettingPresenter;
    @Override
    public void onHandleMessage(Message msg) {
        super.onHandleMessage(msg);
        if (mPresenter != null) {
            mPresenter.onHandleMessage(msg);
        }

    }


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.activity_inventory_movement_scan) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno(), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        mPresenter = new InventoryMovementPresenter(InventoryMovementScan.this, this, mHandler);
        mUserSettingPresenter = new UserSettingPresenter(mContext, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        onClear();
    }

    @Event(value = {R.id.inner_move_barcode_scan, R.id.inner_move_in_stock_scan}, type = View.OnKeyListener.class)
    private boolean barcodeScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            switch (v.getId()) {
                case R.id.inner_move_barcode_scan:
                    String barcode = mBarcode.getText().toString().trim();
                    mPresenter.scanBarcode(barcode);
                    break;
                case R.id.inner_move_in_stock_scan:
                    String moveInAreaNo = mMoveInAreaNo.getText().toString().trim();
                    mPresenter.scanMoveInAreaInfo(moveInAreaNo);
                    break;
                case R.id.inventory_movement_qty:
                    break;
            }

            return true;
        }
        return false;
    }


    @Event(R.id.inventory_movement_refer)
    private void onRefer(View v) {
        if (DoubleClickCheck.isFastDoubleClick(mContext)) {
            return;
        }
        if (mPresenter != null) {
            mPresenter.onRefer();
        }
    }


    @Override
    public void requestBarcodeFocus() {
        CommonUtil.setEditFocus(mBarcode);
    }

    @Override
    public void requestMoveInAreaNoFocus() {
        CommonUtil.setEditFocus(mMoveInAreaNo);
    }

    @Override
    public void requestMoveOutAreaNoFocus() {
        CommonUtil.setEditFocus(mMoveOutAreaNo);
    }

    @Override
    public void requestQtyFocus() {
        CommonUtil.setEditFocus(mQty);
    }


    @Override
    public String getMoveInAreaNo() {
        return mMoveInAreaNo.getText().toString().trim();
    }

    @Override
    public String getMoveOutAreaNo() {
        return mMoveOutAreaNo.getText().toString().trim();
    }

    @Override
    public float getQty() {
        float qty = 0;
        try {
            qty = Float.parseFloat(mQty.getText().toString());

        } catch (Exception e) {
            MessageBox.Show(InventoryMovementScan.this, e.getMessage());
        }
        return qty;
    }

    @Override
    public void onClear() {
        mBarcode.setText("");
        mMoveInAreaNo.setText("");
        mMoveOutAreaNo.setText("");
        mQty.setText(0 + "");
        requestBarcodeFocus();
    }

    @Override
    public void bindListView(List<StockInfo> itemList) {
        if (mAdapter == null) {
            mAdapter = new InventoryMovementScanDetailAdapter(mContext, itemList);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            mAdapter.notifyDataSetChanged();
        }
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
                            if (mPresenter!=null){
                                mPresenter.onClear();
                            }
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public void setTitle() {
        getToolBarHelper().getToolBar().setTitle(mContext.getString(R.string.activity_inventory_movement_scan) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno());

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
