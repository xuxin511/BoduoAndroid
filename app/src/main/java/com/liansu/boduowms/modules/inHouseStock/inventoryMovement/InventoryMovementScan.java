package com.liansu.boduowms.modules.inHouseStock.inventoryMovement;

import android.content.Context;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.ui.adapter.instock.NoSourceScanDetailAdapter;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.DoubleClickCheck;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

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
public class InventoryMovementScan extends BaseActivity implements IInventoryMovementView {
    @ViewInject(R.id.inner_move_barcode_scan)
    EditText     mBarcode;
    @ViewInject(R.id.inner_move_in_stock_scan)
    EditText mMoveInAreaNo;
    @ViewInject(R.id.inner_move_out_stock_area_no)
    EditText mMoveOutAreaNo;
    @ViewInject(R.id.inventory_movement_qty)
    EditText mQty;
    @ViewInject(R.id.txt_Company)
    TextView mStrongHoldName;
    @ViewInject(R.id.txt_Batch)
    TextView mBatchNo;
    @ViewInject(R.id.txt_Status)
    TextView mStatus;
    @ViewInject(R.id.txt_MaterialName)
    TextView mMaterialName;
    @ViewInject(R.id.inventory_movement_refer)
    Button       mRefer;
    @ViewInject(R.id.inventory_movement_list_view)
    RecyclerView mRecyclerView;
    Context                    mContext = InventoryMovementScan.this;
    InventoryMovementPresenter mPresenter;
    NoSourceScanDetailAdapter  mAdapter;
    @Override
    public void onHandleMessage(Message msg) {
        super.onHandleMessage(msg);
        if (mPresenter != null) {
            mPresenter.onHandleMessage(msg);
        }

    }

    public void bindListView(List<OutBarcodeInfo> materialItemList) {
        if (materialItemList!=null && materialItemList.size()>0){
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new NoSourceScanDetailAdapter(mContext, materialItemList);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }else {
            mRecyclerView.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.activity_inventory_movement_scan), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        mPresenter=new InventoryMovementPresenter(InventoryMovementScan.this,this,mHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onClear();
    }

    @Event(value = {R.id.inner_move_barcode_scan, R.id.inner_move_in_stock_scan, R.id.inner_move_out_stock_area_no, R.id.inventory_movement_qty}, type = View.OnKeyListener.class)
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
                case R.id.inner_move_out_stock_area_no:
                    String moveOutAreaNo = mMoveOutAreaNo.getText().toString().trim();
                    mPresenter.scanMoveOutAreaInfo(moveOutAreaNo);
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
    public void setBarcodeInfo(StockInfo stockInfo) {
        if (stockInfo != null) {
            mStrongHoldName.setText(stockInfo.getStrongholdname());
            mBatchNo.setText(stockInfo.getBatchno());
            mStatus.setText(stockInfo.getQty() + "");
            mMaterialName.setText(stockInfo.getMaterialdesc());
        } else {
            mStrongHoldName.setText("组织");
            mBatchNo.setText("批次");
            mStatus.setText("数量");
            mMaterialName.setText("物料名称");
        }

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
        setBarcodeInfo(null);
        mBarcode.setText("");
        mMoveInAreaNo.setText("");
        mMoveOutAreaNo.setText("");
        mQty.setText(0 + "");
        requestBarcodeFocus();
    }
}
