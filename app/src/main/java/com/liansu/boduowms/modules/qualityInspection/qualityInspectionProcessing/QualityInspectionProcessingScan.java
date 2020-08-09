package com.liansu.boduowms.modules.qualityInspection.qualityInspectionProcessing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
import com.liansu.boduowms.ui.adapter.quality_inspection.QualityInspectionScanAdapter;
import com.liansu.boduowms.ui.dialog.MaterialInfoDialogActivity;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @desc: 质检扫描
 * @param: if (DoubleClickCheck.isFastDoubleClick(context)) {
 * return false;
 * }
 * @return:
 * @author: Nietzsche
 * @time 2020/7/12 14:14
 */
@ContentView(R.layout.activity_quality_inspection_processing_scan)
public class QualityInspectionProcessingScan extends BaseActivity implements IQualityInspectionProcessingView {
    Context context = QualityInspectionProcessingScan.this;
    @ViewInject(R.id.quality_inspection_scan_stronghold_no)
    TextView mStrongHoldNo;
    @ViewInject(R.id.quality_inspection_scan_receipt_voucher_no)
    TextView mReceiptVoucherNo;
    @ViewInject(R.id.quality_inspection_erp_voucher_no)
    TextView mErpVoucherNo;
    @ViewInject(R.id.quality_inspection_scan_material_no)
    TextView mMaterialNo;
    @ViewInject(R.id.quality_inspection_scan_batch_no)
    TextView mBatchNo;
    @ViewInject(R.id.quality_inspection_scan_qualified_qty)
    TextView mQualifiedQty;
    @ViewInject(R.id.quality_inspection_scan_un_qualified_qty_desc)
    TextView mUnQualifiedQty;
    @ViewInject(R.id.quality_inspection_scan_un_qualified_scan_qty)
    TextView mUnQualifiedSumQty; //已扫描数量合计
    @ViewInject(R.id.quality_inspection_scan_area_no)
    EditText mAreaNo;
    @ViewInject(R.id.quality_inspection_scan_area_no_desc)
    TextView mAreaNoDesc;
    @ViewInject(R.id.quality_inspection_scan_un_qualified_barcode)
    EditText mBarcode;
    @ViewInject(R.id.quality_inspection_scan_barcode)
    TextView mBarcodeDesc;
    @ViewInject(R.id.quality_inspection_scan_un_qualified_barcode_qty_desc)
    TextView mQtyDesc;
    @ViewInject(R.id.quality_inspection_scan_un_qualified_barcode_qty)
    EditText     mQty;//质检扫描数量
    @ViewInject(R.id.quality_inspection_box_type)
    ToggleButton mOperationType;
    @ViewInject(R.id.quality_inspection_qty_refer)
    Button       mRefer;
    @ViewInject(R.id.quality_inspection_scan_father_barcode)
    TextView mFatherBarcodeDesc;
    @ViewInject(R.id.quality_inspection_scan_un_qualified_father_barcode)
    EditText mFatherBarcode;
    QualityInspectionScanAdapter         mAdapter;
    QualityInspectionProcessingPresenter mPresenter;
    public final int REQUEST_CODE_OK = 1;
    String mQualityType = "";

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.quality_inspection_processing_scan_title), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        mRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    mPresenter.onRefer();
                }
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        try {
            QualityHeaderInfo headerInfo = getIntent().getParcelableExtra("QUALITY_INSPECTION");
            mQualityType = getIntent().getStringExtra("QUALITY_TYPE");
            if (mPresenter == null) {
                mPresenter = new QualityInspectionProcessingPresenter(context, this, mHandler, headerInfo, mQualityType);
            }
            setViewStatus();
        } catch (Exception e) {
            MessageBox.Show(context, e.getMessage() );
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
            mPresenter.getQualityInspectionDetailList();
        }

    }

    @Event(value = {R.id.quality_inspection_scan_area_no, R.id.quality_inspection_scan_un_qualified_barcode, R.id.quality_inspection_scan_un_qualified_barcode_qty}, type = View.OnKeyListener.class)
    private boolean onScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            switch (v.getId()) {
                case R.id.quality_inspection_scan_area_no:
                    String areaNo = mAreaNo.getText().toString().trim();
//                    mPresenter.getAreaInfo(areaNo);
                    break;
                case R.id.quality_inspection_scan_un_qualified_barcode:
                    String barcode = mBarcode.getText().toString().trim();
                    mPresenter.onBarCodeScan(barcode, getOperationType());
                    break;
                case R.id.quality_inspection_scan_un_qualified_barcode_qty:
                    break;
            }

        }
        return false;
    }


    /**
     * @desc: 外箱码扫描
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/7 12:45
     */
    @Event(value = R.id.receiption_scan_out_barcode, type = View.OnKeyListener.class)
    private boolean outBarcodeScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String barcode = mBarcode.getText().toString().trim();
            if (mPresenter != null) {
                mPresenter.scanBarcode(barcode);
            }
        }

        return false;
    }


//    @Event(R.id.btn_ReceiptDetail)
//    private void btnCombinePalletClick(View view) {
//        Intent intent = new Intent();
//        intent.setClass(QualityInspectionProcessingScan.this, InstockCombinePallet.class);
//        Bundle bundle = new Bundle();
////        bundle.putInt("inStockType", COMBINE_PALLET_TYPE_RECEIPTION);
////        bundle.putParcelable("orderHeader", mPresenter.getModel().getReceiptModel());
////        bundle.putParcelableArrayList("orderDetailList", (ArrayList<? extends Parcelable>) DebugModuleData.loadReceiptScanDetailList());
////        bundle.putParcelableArrayList("orderDetailList", mPresenter.getModel().getReceiptDetailModels());
//        intent.putExtras(bundle);
//        startActivityLeft(intent);
//    }


    @Event(value = R.id.lsv_ReceiptScan, type = AdapterView.OnItemClickListener.class)
    private boolean lsv_ReceiptScanItemClick(AdapterView<?> parent, View view, int position,
                                             long id) {
//        if (id >= 0) {
//            ReceiptDetail_Model receiptDetailModel = receiptDetailModels.get(position);
//            try {
//                if (receiptDetailModel.getLstBarCode() != null && receiptDetailModel.getLstBarCode().size() != 0) {
////                        Intent intent = new Intent(context, ReceiptionBillDetail.class);
////                        Bundle bundle = new Bundle();
////                        bundle.putParcelable("receiptDetailModel", receiptDetailModel);
////                        intent.putExtras(bundle);
////                        startActivityLeft(intent);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
        return true;
    }


    @Override
    public void bindListView(List<QualityHeaderInfo> qualityDetailInfos) {
//        mAdapter = new QualityInspectionScanAdapter(context, "质检处理", qualityDetailInfos);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
    public void onQtyFocus() {
        CommonUtil.setEditFocus(mQty);
    }

    @Override
    public void setOrderInfo(QualityHeaderInfo headerInfo) {
        if (headerInfo != null) {
            mStrongHoldNo.setText(headerInfo.getStrongholdcode() + "");
            mReceiptVoucherNo.setText(headerInfo.getVoucherno() + "");
            mErpVoucherNo.setText(headerInfo.getErpvoucherno() + "");
            mQualifiedQty.setText(headerInfo.getQualityqty()+"");
        } else {
            mStrongHoldNo.setText("");
            mReceiptVoucherNo.setText("");
            mErpVoucherNo.setText("");
            mQualifiedQty.setText("");
        }
    }

    @Override
    public void setOrderDetailInfo(QualityHeaderInfo detailInfo) {
        if (detailInfo != null) {
            mMaterialNo.setText(detailInfo.getMaterialno());
            mBatchNo.setText(detailInfo.getBatchno());
            mUnQualifiedQty.setText(detailInfo.getUnqualityqty()+"");
            mQualifiedQty.setText(detailInfo.getQualityqty()+"");
        } else {
            mMaterialNo.setText("");
            mBatchNo.setText("");
            mUnQualifiedQty.setText("");
            mQualifiedQty.setText("");
        }
    }

    @Override
    public int getOperationType() {
        int type = -1;
        if (!mOperationType.isChecked()) {
            type = 1;
        } else {
            type = 2;
        }
        return type;
    }

    @Override
    public void setScanQty(float qty) {
        mUnQualifiedSumQty.setText(qty + "");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_CODE_OK: //返回的结果是来自于Activity B
                    if (resultCode == Activity.RESULT_OK) {
                        OutBarcodeInfo info = data.getParcelableExtra("resultInfo");
                        if (info != null) {
                            mPresenter.onBarcodeRefer(info);
                        }

                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            MessageBox.Show(context, "从物料界面传递数据给入库扫描界面出现异常" + e.getMessage() );
        }


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
            mOperationType.setVisibility(View.GONE);
            mFatherBarcodeDesc.setVisibility(View.GONE);
            mFatherBarcode.setVisibility(View.GONE);
            mRefer.setText("提交");
        } else if (mQualityType.equals("UNQUALIFIED")) {
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
    public void onActivityFinish(String title) {
        new AlertDialog.Builder(BaseApplication.context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage(title+" 是否返回上一页面？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 自动生成的方法
                        closeActivity();
                    }
                }).setNegativeButton("取消", null).show();
    }


}
