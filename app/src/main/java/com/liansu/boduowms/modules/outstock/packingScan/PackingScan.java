package com.liansu.boduowms.modules.outstock.packingScan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.modules.outstock.offScan.BalconyInfoDialogActivity;
import com.liansu.boduowms.modules.outstock.packingScan.packingList.PackingList;
import com.liansu.boduowms.ui.adapter.outstock.packing.PackingScanAdapter;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @ Des: 拼箱扫描
 * @ Created by yangyiqing on 2020/6/28.
 */
@ContentView(R.layout.activity_packing_scan)
public class PackingScan extends BaseActivity implements IPackingScanView {
    private static final int REQUEST_CODE_OK = 1;
    Context mContext = this;
    @ViewInject(R.id.packing_scan_list_view)
    protected ListView             mList;
    @ViewInject(R.id.offscan_voucher_no)
    protected EditText             mErpVoucherNo;
    @ViewInject(R.id.packing_scan_barcode)
    protected EditText             mBarcode;
    @ViewInject(R.id.packing_scan_print)
    protected Button               mPrintButton;
    @ViewInject(R.id.packing_scan_qty_desc)
    protected TextView             mQtyDesc;
    @ViewInject(R.id.packing_scan_qty)
    protected EditText             mQty;
    @ViewInject(R.id.packing_platform)
    protected Button               mPlatform;
    @ViewInject(R.id.packing_scan_unstock_packing)
    protected Button               mUnStockPackingPrint;
    protected PackingScanAdapter   mAdapter;
    protected PackingScanPresenter mPresenter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.appbar_title_packing), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;

    }


    @Override
    protected void initData() {
        super.initData();
        mPresenter = new PackingScanPresenter(mContext, this, mHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void bindListView(List<OutStockOrderDetailInfo> list) {
        if (list != null && list.size() > 0) {
            mList.setVisibility(View.VISIBLE);
            mAdapter = new PackingScanAdapter(mContext, list);
            mList.setAdapter(mAdapter);
        } else {
            mList.setVisibility(View.GONE);
        }

    }

    @Override
    public void onErpVoucherNoFocus() {
        CommonUtil.setEditFocus(mErpVoucherNo);
    }

    @Override
    public void onBarcodeNoFocus() {
        CommonUtil.setEditFocus(mBarcode);
    }

    @Override
    public void onQtyFocus() {
        CommonUtil.setEditFocus(mQty);
    }

    /**
     * @desc: 拆零操作条码类型
     * @param:
     * @return:
     * @author:
     * @time 2020/4/20 17:16
     */
    @Event(value = {R.id.packing_platform, R.id.packing_scan_unstock_packing}, type = CompoundButton.OnClickListener.class)
    private void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.packing_platform:
                Intent intent = new Intent();
                intent.setClass(PackingScan.this, BalconyInfoDialogActivity.class);
                Bundle bundle = new Bundle();
//            bundle.putString("ORDER_NO",mPresenter.getModel().getOrderDetailList().get(0).getArrVoucherNo());
                bundle.putString("ORDER_NO", "20200726");
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_OK);
                break;
            case R.id.packing_scan_unstock_packing:
                mPresenter.printLCLLabel();
                break;
        }


    }

    @Event(value = {R.id.offscan_voucher_no, R.id.packing_scan_barcode, R.id.packing_scan_qty}, type = View.OnKeyListener.class)
    private boolean onScanClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            switch (v.getId()) {
                case R.id.offscan_voucher_no:
                    String erpVoucherNo = mErpVoucherNo.getText().toString().trim();
                    mPresenter.getOrderDetailInfoList(erpVoucherNo);
                    break;
                case R.id.packing_scan_barcode:
                    String barcode = mBarcode.getText().toString().trim();
                    if (barcode.contains("%")) {  //外箱码直接扫描提交
                        setQtyViewStatus(false);
                        mPresenter.onScan(barcode);
                    } else {
                        //散件调到数量输入数量
                        setQtyViewStatus(true);
                        onQtyFocus();
                    }

                    break;
                case R.id.packing_scan_qty:
//                    OutBarcodeInfo outBarcodeInfo = mPresenter.getModel().getCurrentBarcodeInfo();
//                    outBarcodeInfo.setQty(getQty());
//                    if (outBarcodeInfo != null) {
//                        mPresenter.requestBarcodeInfo(outBarcodeInfo);
//                    } else {
//                        MessageBox.Show(mContext, "当前扫描的数据不能为空");
//                    }
                    String sparePartsBarcode = mBarcode.getText().toString().trim();
                    mPresenter.onScan(sparePartsBarcode);
                    break;

            }

        }
        return false;
    }

    @Override
    public void createBatchNoListDialog(List<String> batchNoList) {
        if (batchNoList != null && batchNoList.size() > 0) {
            final String[] items = batchNoList.toArray(new String[0]);
            new AlertDialog.Builder(mContext).setTitle(getResources().getString(R.string.activity_login_WareHousChoice))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setCancelable(false)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String batchNo = items[which].toString();
                            String barcode = mBarcode.getText().toString().trim();
                            OutBarcodeInfo outBarcodeInfo = mPresenter.getModel().getCurrentBarcodeInfo();
                            if (outBarcodeInfo != null) {
                                outBarcodeInfo.setBatchno(batchNo);
                                mPresenter.requestBarcodeInfo(outBarcodeInfo);
                            }

                            dialog.dismiss();
                        }
                    }).show();
        } else {
            MessageBox.Show(mContext, "地址列表为空" );
        }
    }

    @Override
    public void setQtyViewStatus(boolean isVisibility) {
        if (isVisibility) {
            if (mQtyDesc.getVisibility() != View.VISIBLE) {
                mQty.setVisibility(View.VISIBLE);
                mQtyDesc.setVisibility(View.VISIBLE);
            }

        } else {
            if (mQtyDesc.getVisibility() != View.GONE) {
                mQty.setVisibility(View.GONE);
                mQtyDesc.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public float getQty() {
        try {
            return Float.parseFloat(mQty.getText().toString().trim());
        } catch (Exception e) {
            MessageBox.Show(mContext, "转化数量类型出现异常:" + e.getMessage() );
            return -1;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_CODE_OK: //返回的结果是来自于Activity B
                    if (resultCode == Activity.RESULT_OK) {
                        String balconyDesc = data.getStringExtra("BALCONY_DESC");
                        String orderNo = data.getStringExtra("ORDER_NO");
                        if (balconyDesc != null && orderNo != null) {
                            mPresenter.setBalconyInfo(orderNo, balconyDesc);
                        }

                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "从物料界面传递数据给入库扫描界面出现异常" + e.getMessage() );
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_packing, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_packing_list) {
            Intent intent = new Intent();
            intent.setClass(PackingScan.this, PackingList.class);
            Bundle bundle = new Bundle();
//            bundle.putString("ORDER_NO",mPresenter.getModel().getOrderDetailList().get(0).getArrVoucherNo());
            bundle.putString("ORDER_NO", "20200726");
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE_OK);
        }

        return false;
    }

}
