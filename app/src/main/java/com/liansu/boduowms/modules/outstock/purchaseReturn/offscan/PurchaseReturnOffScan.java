package com.liansu.boduowms.modules.outstock.purchaseReturn.offscan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.BasePresenterFactory;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.BaseOutStockBusinessModel;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.BaseOutStockBusinessPresenter;
import com.liansu.boduowms.modules.outstock.packingScan.PackingScan;
import com.liansu.boduowms.ui.adapter.outstock.offscan.BaseOffShelfScanDetailAdapter;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * @ Des: 仓库采购退货
 * @ Created by yangyiqing on 2020/8/5.
 */
@ContentView(R.layout.activity_purchase_return_scan)
public class PurchaseReturnOffScan extends BaseActivity implements IPurchaseReturnOffScanView {
    protected Context      mContext = this;
    @ViewInject(R.id.offscan_list_view)
    protected ListView     mListView;
    @ViewInject(R.id.offscan_voucher_no)
    protected EditText     mErpVoucherNo;
    @ViewInject(R.id.offscan_receive_supplier_desc)
    protected TextView     mSupplierDesc;
    @ViewInject(R.id.offscan_receive_supplier)
    protected EditText     mSupplier;
    @ViewInject(R.id.offscan_barcode)
    protected EditText     mFatherBarcode;
    @ViewInject(R.id.offscan_sub_barcode)
    protected EditText     mSubBarcode;
    @ViewInject(R.id.offscan_batch_no)
    protected EditText     mBatchNo;
    @ViewInject(R.id.offscan_qty)
    protected EditText     mQty;
    @ViewInject(R.id.offscan_box_type_pallet)
    protected ToggleButton mPalletType;
    @ViewInject(R.id.offscan_box_type_box)
    protected ToggleButton mOuterBoxType;
    @ViewInject(R.id.offscan_box_type_spare_parts)
    protected ToggleButton mSparePartsType;
    @ViewInject(R.id.offscan_box_type_combine_trays)
    protected ToggleButton mCombineTraysType;
    @ViewInject(R.id.offscan_sub_barcode_desc)
    protected TextView     mSubBarcodeDesc;
    @ViewInject(R.id.offscan_qty_desc)
    protected TextView     mQtyDesc;
    @ViewInject(R.id.offscan_refer)
    Button mOrderRefer;
    @ViewInject(R.id.offscan_print)
    Button mOrderPrint;
    protected String                        mBusinessType = "";  //业务类型
    protected BaseOutStockBusinessPresenter mPresenter;
    protected BaseOffShelfScanDetailAdapter mAdapter;

    @Override
    public void onHandleMessage(Message msg) {
        if (mPresenter != null) {
            mPresenter.onHandleMessage(msg);
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        initTitle();
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        initViewStatus();

    }

    @Override
    protected void initData() {
        super.initData();
        initPresenter();
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
        mAdapter = new BaseOffShelfScanDetailAdapter(mContext, list);
        mListView.setAdapter(mAdapter);
    }


    /**
     * @desc: 拆零操作条码类型
     * @param:
     * @return:
     * @author:
     * @time 2020/4/20 17:16
     */
    @Event(value = {R.id.offscan_box_type_pallet, R.id.offscan_box_type_box, R.id.offscan_box_type_spare_parts, R.id.offscan_box_type_combine_trays, R.id.offscan_address_select}, type = CompoundButton.OnClickListener.class)
    private void onClickListener(View view) {
        if (view.getId() == R.id.offscan_address_select) {
            createOrderAddressListDialog(mPresenter.getModel().getAddressList());
        } else {
            ToggleButton button = (ToggleButton) view;
            selectScanType(button, button.isChecked());
        }


    }

    /**
     * @desc: 扫描条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 8:19
     */
    @Event(value = {R.id.offscan_voucher_no, R.id.offscan_barcode, R.id.offscan_sub_barcode, R.id.offscan_qty}, type = View.OnKeyListener.class)
    private boolean onScanClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            int scanType = getScanType();
            String fatherBarcode = mFatherBarcode.getText().toString().trim();
            String subBarcode = mSubBarcode.getText().toString().trim();
            switch (v.getId()) {
                case R.id.offscan_voucher_no:
                    String erpVoucherNo = mErpVoucherNo.getText().toString().trim();
                    mPresenter.getOrderDetailInfoList(erpVoucherNo);
                    break;
                case R.id.offscan_barcode:
                    mPresenter.onPalletScan(fatherBarcode, scanType);
                    break;
                case R.id.offscan_sub_barcode:
                    if (scanType == BaseOutStockBusinessModel.OUT_STOCK_SCAN_TYPE_OUTER_BOX) {
                        mPresenter.onOuterBoxScan(fatherBarcode, subBarcode, scanType);
                    } else if (scanType == BaseOutStockBusinessModel.OUT_STOCK_SCAN_TYPE_SPARE_PARTS) {
                        mPresenter.onSparePartsScan(fatherBarcode, subBarcode, scanType);
                    }
                    break;
                case R.id.offscan_qty:
                    if (scanType == BaseOutStockBusinessModel.OUT_STOCK_SCAN_TYPE_SPARE_PARTS) {
                        mPresenter.onSparePartsInfoRefer(fatherBarcode, subBarcode, scanType);
                    }
                    break;
            }

        }
        return false;
    }

    /**
     * @desc: 过账提交
     * @param:
     * @return:
     * @author:
     * @time 2020/4/20 17:16
     */
    @Event(value = {R.id.offscan_refer}, type = CompoundButton.OnClickListener.class)
    private void onRefer(View view) {
        mPresenter.onOrderRefer();
    }
    /**
     * @desc: 打印
     * @param:
     * @return:
     * @author:
     * @time 2020/4/20 17:16
     */
    @Event(value = {R.id.offscan_print}, type = CompoundButton.OnClickListener.class)
    private void onpPrint(View view) {
        mPresenter.onOrderPrint();
    }

    @Override
    public void setOrderHeaderInfo(OutStockOrderHeaderInfo info) {
        if (info != null) {
//             mSupplier.setText(info.);
        }
    }

    @Override
    public void onErpVoucherNoFocus() {
        CommonUtil.setEditFocus(mErpVoucherNo);
    }

    @Override
    public void onOrderAddressFocus() {

    }


    @Override
    public void onFatherBarcodeFocus() {
        CommonUtil.setEditFocus(mFatherBarcode);
    }

    @Override
    public void onSubBarcodeFocus() {
        CommonUtil.setEditFocus(mSubBarcode);
    }

    @Override
    public void onQtyFocus() {
        CommonUtil.setEditFocus(mQty);
    }

    @Override
    public void setQty(float qty) {
        mQty.setText(qty + "");
    }

    @Override
    public void createOrderAddressListDialog(List<String> addressList) {

    }

    @Override
    public void createMultipleBatchesSelectDialog(List<String> batches) {
        if (batches != null && batches.size() > 0) {
            final String[] items = batches.toArray(new String[0]);
            new AlertDialog.Builder(mContext).setTitle(getResources().getString(R.string.activity_login_WareHousChoice))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setCancelable(false)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String select_item = items[which].toString();
                            if (mPresenter.getModel().getCurrentSubInfo() != null) {
                                mPresenter.getModel().getCurrentSubInfo().setBatchno(select_item);
                                mPresenter.onSparePartsInfoRefer(mFatherBarcode.getText().toString().trim(), mSubBarcode.getText().toString().trim(), getScanQty());
                            } else {
                                MessageBox.Show(mContext, "获取散件信息为空!请先扫描散件");
                            }

                            dialog.dismiss();
                        }
                    }).show();
        } else {
            MessageBox.Show(mContext, "地址列表为空");
        }
    }

    @Override
    public int getScanType() {
        if (mPalletType.isChecked()) {
            return BaseOutStockBusinessModel.OUT_STOCK_SCAN_TYPE_TRAY;
        } else if (mOuterBoxType.isChecked()) {
            return BaseOutStockBusinessModel.OUT_STOCK_SCAN_TYPE_OUTER_BOX;
        } else if (mSparePartsType.isChecked()) {
            return BaseOutStockBusinessModel.OUT_STOCK_SCAN_TYPE_SPARE_PARTS;
        } else if (mCombineTraysType.isChecked()) {
            return BaseOutStockBusinessModel.OUT_STOCK_SCAN_TYPE_COMBINE_TRAYS;
        }
        return BaseOutStockBusinessModel.OUT_STOCK_SCAN_TYPE_NONE;
    }

    /**
     * @desc: 初始化 扫描类型的控件状态
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/26 14:44
     */
    @Override
    public void initViewStatus() {
        if (mBusinessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_PICKING_OFF_THE_SHELF)) {
            mPalletType.setVisibility(View.VISIBLE);
            selectScanType(mPalletType, true);
//            mPalletType.setVisibility(View.GONE);
//            selectScanType(mOuterBoxType, true);
        } else if (mBusinessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_PICKING_OFF_THE_SHELF_ONLY_TRAY)) {
            mPalletType.setVisibility(View.VISIBLE);
            selectScanType(mPalletType, true);
        }else if(mBusinessType.equals(OrderType.OUT_STOCK_ORDER_TYPE_PURCHASE_RETURN)){
            mPalletType.setVisibility(View.VISIBLE);
            selectScanType(mPalletType, true);
            mOrderRefer.setVisibility(View.GONE);
            mOrderPrint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onReset() {
        mFatherBarcode.setText("");
        mSubBarcode.setText("");
        mQty.setText("0");
        onFatherBarcodeFocus();
    }

    /**
     * @desc: 设置扫描状态，只能有一种类型为选中状态
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/26 14:43
     */
    @Override
    public void selectScanType(ToggleButton view, boolean viewStatus) {
        int viewId = view.getId();
        view.setChecked(viewStatus);
        if (viewId == R.id.offscan_box_type_pallet) {
            mOuterBoxType.setChecked(false);
            mSparePartsType.setChecked(false);
            mCombineTraysType.setChecked(false);
            mSubBarcode.setVisibility(View.GONE);
            mQty.setVisibility(View.GONE);
            mSubBarcodeDesc.setVisibility(View.GONE);
            mQtyDesc.setVisibility(View.GONE);
        } else if (viewId == R.id.offscan_box_type_box) {
            mPalletType.setChecked(false);
            mSparePartsType.setChecked(false);
            mCombineTraysType.setChecked(false);
            mSubBarcode.setVisibility(View.VISIBLE);
            mQty.setVisibility(View.GONE);
            mSubBarcodeDesc.setVisibility(View.VISIBLE);
            mQtyDesc.setVisibility(View.GONE);
        } else if (viewId == R.id.offscan_box_type_spare_parts) {
            mPalletType.setChecked(false);
            mOuterBoxType.setChecked(false);
            mCombineTraysType.setChecked(false);
            mSubBarcode.setVisibility(View.VISIBLE);
            mQty.setVisibility(View.VISIBLE);
            mSubBarcodeDesc.setVisibility(View.VISIBLE);
            mQtyDesc.setVisibility(View.VISIBLE);
        } else if (viewId == R.id.offscan_box_type_combine_trays) {
            mPalletType.setChecked(false);
            mOuterBoxType.setChecked(false);
            mCombineTraysType.setChecked(false);
            mSubBarcode.setVisibility(View.VISIBLE);
            mQty.setVisibility(View.VISIBLE);
            mSubBarcodeDesc.setVisibility(View.VISIBLE);
            mQtyDesc.setVisibility(View.VISIBLE);
        }
    }


    @Event(R.id.offscan_packing_box)
    private void btnStartPickingClick(View view) {
        Intent intent = new Intent();
        intent.setClass(PurchaseReturnOffScan.this, PackingScan.class);
        startActivityLeft(intent);
    }

    /**
     * @desc: 设置标题
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/24 14:54
     */
    protected void initTitle() {
        mBusinessType = getIntent().getStringExtra("BusinessType").toString();
        if (mBusinessType!=null){ BaseApplication.toolBarTitle = new ToolBarTitle(mBusinessType + getString(R.string.offScan) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno(), false);

        }

    }


    /**
     * @desc: 创建业务子类
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/26 15:24
     */
    protected void initPresenter() {
        mPresenter = BasePresenterFactory.getBaseOutStockBusinessPresenter(mContext, this, mHandler, mBusinessType);
    }

    ;


    @Override
    public float getScanQty() {
        try {
            return Float.parseFloat(mQty.getText().toString().trim());
        } catch (Exception e) {
            MessageBox.Show(mContext, "转换数量出现异常:" + e.getMessage());
            return -1;
        }

    }


}
