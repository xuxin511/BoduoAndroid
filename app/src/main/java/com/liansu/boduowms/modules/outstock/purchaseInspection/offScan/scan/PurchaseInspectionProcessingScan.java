package com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.ui.adapter.quality_inspection.QualityInspectionScanAdapter;
import com.liansu.boduowms.ui.dialog.MaterialInfoDialogActivity;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * @desc: 验退扫描
 * @return:
 * @author: Nietzsche
 * @time 2020/7/12 14:14
 */
@ContentView(R.layout.activity_quality_inspection_processing_scan)
public class PurchaseInspectionProcessingScan extends BaseActivity implements IPurchaseInspectionProcessingView {
    Context context = PurchaseInspectionProcessingScan.this;
    @ViewInject(R.id.quality_inspection_scan_stronghold_no)
    TextView     mStrongHoldNo;
    @ViewInject(R.id.quality_inspection_scan_receipt_voucher_no)
    TextView     mReceiptVoucherNo;
    @ViewInject(R.id.quality_inspection_erp_voucher_no)
    TextView     mErpVoucherNo;
    @ViewInject(R.id.quality_inspection_scan_material_no)
    TextView     mMaterialNo;
    @ViewInject(R.id.quality_inspection_scan_batch_no)
    TextView     mBatchNo;
    @ViewInject(R.id.quality_inspection_scan_qualified_qty)
    TextView     mQualifiedQty;
    @ViewInject(R.id.quality_inspection_scan_un_qualified_qty_desc)
    TextView     mUnQualifiedQty;
    @ViewInject(R.id.quality_inspection_scan_un_qualified_scan_qty)
    TextView     mUnQualifiedSumQty; //已扫描数量合计
    @ViewInject(R.id.quality_inspection_scan_area_no)
    EditText     mAreaNo;
    @ViewInject(R.id.quality_inspection_scan_area_no_desc)
    TextView     mAreaNoDesc;
    @ViewInject(R.id.quality_inspection_scan_un_qualified_barcode)
    EditText     mBarcode;
    @ViewInject(R.id.quality_inspection_scan_barcode)
    TextView     mBarcodeDesc;
    @ViewInject(R.id.quality_inspection_scan_un_qualified_barcode_qty_desc)
    TextView     mQtyDesc;
    @ViewInject(R.id.quality_inspection_scan_un_qualified_barcode_qty)
    EditText     mQty;//质检扫描数量
    @ViewInject(R.id.quality_inspection_box_type)
    ToggleButton mOperationType;
    @ViewInject(R.id.quality_inspection_qty_refer)
    Button       mPrint;
    @ViewInject(R.id.quality_inspection_scan_un_qualified_father_barcode)
    EditText     mPalletNo;
    @ViewInject(R.id.quality_inspection_scan_qualified_qty_desc)
    TextView  mQualifiedQtyDesc;
    @ViewInject(R.id.quality_inspection_scan_batch_no_desc)
    TextView mBatchNoDesc;
    QualityInspectionScanAdapter          mAdapter;
    PurchaseInspectionProcessingPresenter mPresenter;
    public final int REQUEST_CODE_OK = 1;
    String mQualityType = "";

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.purchase_inspection_processing_scan), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;


    }

    @Override
    protected void initData() {
        super.initData();
        try {
            OutStockOrderHeaderInfo headerInfo = getIntent().getParcelableExtra("QUALITY_INSPECTION");
            mQualityType = "UNQUALIFIED";
            if (mPresenter == null) {
                mPresenter = new PurchaseInspectionProcessingPresenter(context, this, mHandler, headerInfo, mQualityType);
            }
            setViewStatus();
            initViewStatus();
        } catch (Exception e) {
            MessageBox.Show(context, e.getMessage());
        }


    }

    @Override
    public void onHandleMessage(Message msg) {
        mPresenter.onHandleMessage(msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.getQualityInspectionDetailList(mPresenter.getModel().getOrderHeaderInfo().getErpvoucherno());
        }

    }

    /**
     * @desc: 拆零操作条码类型
     * @param:
     * @return:
     * @author:
     * @time 2020/4/20 17:16
     */
    @Event(value = {R.id.quality_inspection_box_type}, type = CompoundButton.OnClickListener.class)
    private void onClickListener(View view) {
        initViewStatus();


    }

    @Event(value = {R.id.quality_inspection_scan_un_qualified_father_barcode, R.id.quality_inspection_scan_un_qualified_barcode, R.id.quality_inspection_scan_un_qualified_barcode_qty}, type = View.OnKeyListener.class)
    private boolean onScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            switch (v.getId()) {
                case R.id.quality_inspection_scan_un_qualified_father_barcode:
                    String palletNo=mPalletNo.getText().toString().trim();
                    BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(palletNo);
                    if (resultInfo.getHeaderStatus()){
                        OutBarcodeInfo info=resultInfo.getInfo();
                        if (info.getBarcodetype()!=2){
                            MessageBox.Show(context,"解析父级条码失败:请扫描托盘码");
                            return false ;
                        }
                    }else {
                        MessageBox.Show(context,"解析父级条码失败:"+ resultInfo.getMessage());
                        return false ;
                    }

                    if (getOperationType() == PurchaseInspectionProcessingModel.SCAN_TYPE_PALLET_NO) {
                        mPresenter.onBarCodeScan(mPalletNo.getText().toString().trim(), "", getOperationType(), 0);
                    } else if (getOperationType() == PurchaseInspectionProcessingModel.SCAN_TYPE_SUB_BARCODE) {
                        onBarcodeFocus();
                    }
                    break;
                case R.id.quality_inspection_scan_un_qualified_barcode:
                    String fatherBarcode2 = mPalletNo.getText().toString().trim();
                    String subBarcode2 = mBarcode.getText().toString().trim();
                    mPresenter.onBarCodeScan(fatherBarcode2, subBarcode2, getOperationType(), 0);
                    break;
                case R.id.quality_inspection_scan_un_qualified_barcode_qty:
                    String fatherBarcode = mPalletNo.getText().toString().trim();
                    String subBarcode = mBarcode.getText().toString().trim();
                    float scanQty = Float.parseFloat(mQty.getText().toString());
                    mPresenter.scanBulkBarcode(fatherBarcode, subBarcode, scanQty);
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
    @Event(value = {R.id.quality_inspection_qty_refer}, type = CompoundButton.OnClickListener.class)
    private void onRefer(View view) {
        mPresenter.onOrderPrint();
    }





    @Override
    public void onReset() {

    }

    @Override
    public void onBarcodeFocus() {
        CommonUtil.setEditFocus(mBarcode);

    }

    @Override
    public void onAreaNoFocus() {
        CommonUtil.setEditFocus(mAreaNo);
    }

    @Override
    public void onPalletFocus() {
        CommonUtil.setEditFocus(mPalletNo);

    }

    @Override
    public void onQtyFocus() {
        CommonUtil.setEditFocus(mQty);
    }

    @Override
    public void setOrderInfo(OutStockOrderHeaderInfo headerInfo) {
        if (headerInfo != null) {
            mStrongHoldNo.setText(headerInfo.getStrongholdcode() + "");
            mReceiptVoucherNo.setText(headerInfo.getArrvoucherno() + "");
            mErpVoucherNo.setText(headerInfo.getErpvoucherno() + "");
//            mQualifiedQty.setText(headerInfo.getQualityqty() + "");
        } else {
            mStrongHoldNo.setText("");
            mReceiptVoucherNo.setText("");
            mErpVoucherNo.setText("");
//            mQualifiedQty.setText("");
        }
    }

    @Override
    public void setOrderDetailInfo(OutStockOrderDetailInfo detailInfo) {
        if (detailInfo != null) {
            mMaterialNo.setText(detailInfo.getMaterialno());
            mBatchNo.setText(detailInfo.getBatchno());
            mUnQualifiedQty.setText("剩余数量:"+detailInfo.getRemainqty() + "");
//            mQualifiedQty.setText(detailInfo.getQualityqty()+"");
        } else {
            mMaterialNo.setText("");
            mBatchNo.setText("");
            mUnQualifiedQty.setText("0");
            mQualifiedQty.setText("");
        }
        setScanQty(detailInfo.getScanqty());
    }

    @Override
    public int getOperationType() {
        int type = -1;
        if (!mOperationType.isChecked()) {
            type = PurchaseInspectionProcessingModel.SCAN_TYPE_PALLET_NO;
        } else {
            type = PurchaseInspectionProcessingModel.SCAN_TYPE_SUB_BARCODE;
        }
        return type;
    }

    @Override
    public void setScanQty(float qty) {
        mUnQualifiedSumQty.setText("已扫数:"+qty + "");
    }


    @Override
    public void createDialog(OutBarcodeInfo info) {
        Intent intent = new Intent();
        intent.setClass(context, MaterialInfoDialogActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("info", info);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE_OK);
    }

    @Override
    public void setViewStatus() {
        if (mQualityType.equals("QUALIFIED")) {
            mUnQualifiedQty.setVisibility(View.GONE);
            mUnQualifiedSumQty.setVisibility(View.GONE);
            mBarcode.setVisibility(View.GONE);
            mBarcodeDesc.setVisibility(View.GONE);
            mQtyDesc.setVisibility(View.GONE);
            mQty.setVisibility(View.GONE);
        } else if (mQualityType.equals("UNQUALIFIED")) {
            mQualifiedQty.setVisibility(View.GONE);
            mQualifiedQtyDesc.setVisibility(View.GONE);
            mBatchNo.setVisibility(View.GONE);
            mBatchNoDesc.setVisibility(View.GONE);
            mUnQualifiedQty.setVisibility(View.VISIBLE);
            mUnQualifiedSumQty.setVisibility(View.VISIBLE);
            mBarcode.setVisibility(View.VISIBLE);
            mBarcodeDesc.setVisibility(View.VISIBLE);
            mQtyDesc.setVisibility(View.VISIBLE);
            mQty.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public String getAreaNo() {
        return mAreaNo.getText().toString().trim();
    }

    @Override
    public void onActivityFinish() {
        closeActivity();
    }

    @Override
    public void setQtyViewStatus(boolean isStatus) {
        if (isStatus) {
            if (mQty.getVisibility() != View.VISIBLE) {
                mQty.setVisibility(View.VISIBLE);
                mQtyDesc.setVisibility(View.VISIBLE);
            }

        } else {
            if (mQty.getVisibility() != View.GONE) {
                mQty.setVisibility(View.GONE);
                mQtyDesc.setVisibility(View.GONE);
            }
        }
    }


    public void initViewStatus() {
        if (getOperationType() == PurchaseInspectionProcessingModel.SCAN_TYPE_PALLET_NO) {
            if (mBarcode.getVisibility() != View.GONE) {
                mBarcode.setVisibility(View.GONE);
                mBarcodeDesc.setVisibility(View.GONE);
                mQtyDesc.setVisibility(View.GONE);
                mQty.setVisibility(View.GONE);
            }
        } else if (getOperationType() == PurchaseInspectionProcessingModel.SCAN_TYPE_SUB_BARCODE) {
            if (mBarcode.getVisibility() != View.VISIBLE) {
                mBarcode.setVisibility(View.VISIBLE);
                mBarcodeDesc.setVisibility(View.VISIBLE);
            }
        }
    }

}
