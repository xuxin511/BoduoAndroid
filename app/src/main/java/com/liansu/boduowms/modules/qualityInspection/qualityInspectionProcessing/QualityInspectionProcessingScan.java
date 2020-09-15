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
import com.liansu.boduowms.bean.warehouse.WareHouseInfo;
import com.liansu.boduowms.ui.adapter.quality_inspection.QualityInspectionScanAdapter;
import com.liansu.boduowms.ui.dialog.MaterialInfoDialogActivity;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * @desc: 质检合格扫描
 * @param: if (DoubleClickCheck.isFastDoubleClick(context)) {
 * return false;
 * }
 * @return:
 * @author: Nietzsche
 * @time 2020/7/12 14:14
 */
@ContentView(R.layout.activity_quality_inspection_processing_scan2)
public class QualityInspectionProcessingScan extends BaseActivity implements IQualityInspectionProcessingView {
    Context context = QualityInspectionProcessingScan.this;
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
    Button       mRefer;
    @ViewInject(R.id.quality_transfer_refer)
    Button       mTransferRefer;
    @ViewInject(R.id.quality_inspection_scan_father_barcode)
    TextView     mFatherBarcodeDesc;
    @ViewInject(R.id.quality_inspection_scan_un_qualified_father_barcode)
    EditText     mFatherBarcode;
    @ViewInject(R.id.quality_inspection_voucher_no_desc_name)
    TextView     mVoucherNoDesc;  //单据性质
    @ViewInject(R.id.quality_inspection_purchase_no_desc)
    TextView     mPurchaseOrderNoDesc;
    @ViewInject(R.id.quality_inspection_purchase_no)
    TextView     mPurchaseNo;
    @ViewInject(R.id.quality_inspection_scan_receipt_voucher_no_desc)
    TextView     mArrVoucherNoDesc;
    @ViewInject(R.id.quality_inspection_voucher_no_desc_name)
    TextView     mErpVoucherName;
    @ViewInject(R.id.quality_inspection_scan_material_desc)
    TextView     mMaterialDesc;
    @ViewInject(R.id.quality_inspection_scan_voucher_qty)
    TextView     mVoucherQty;
    @ViewInject(R.id.quality_inspection_scan_qualified_qty)
    TextView     mQualifiedQty;
    @ViewInject(R.id.quality_inspection_scan_sampqty_qty)
    TextView     mSampQty;
    @ViewInject(R.id.quality_inspection_scan_erp_voucher_no)
    TextView  mErpVoucherNoDesc;
    QualityInspectionScanAdapter         mAdapter;
    QualityInspectionProcessingPresenter mPresenter;
    public final int REQUEST_CODE_OK = 1;
    String mQualityType = "";

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.quality_inspection_processing_scan_title) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno(), true);
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
        mTransferRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> wareHouseInfos = getWareHouseNameList();
                if (wareHouseInfos != null && wareHouseInfos.size() > 0) {
                    selectWareHouse(wareHouseInfos);
                }

            }
        });
        closeKeyBoard(mBarcode, mQty, mFatherBarcode);
        mArrVoucherNoDesc.setText("创建人:");
        mErpVoucherNoDesc.setText("创建日期:");
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
        WareHouseInfo info = BaseApplication.mCurrentWareHouseInfo;
        if (info != null) {
            if (info.getIstransfer() == 2) {
                mTransferRefer.setVisibility(View.VISIBLE);
            } else {
                mTransferRefer.setVisibility(View.GONE);
            }
        }
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

                    break;
                case R.id.quality_inspection_scan_un_qualified_barcode:
                    String barcode = mBarcode.getText().toString().trim();
                    mPresenter.onBarCodeScan(barcode, getOperationType());
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
    }

    @Override
    public void onQtyFocus() {
        CommonUtil.setEditFocus(mQty);
    }

    @Override
    public void setOrderInfo(QualityHeaderInfo headerInfo) {
        if (headerInfo != null) {
            mStrongHoldNo.setText(headerInfo.getStrongholdcode() + "");
//            mReceiptVoucherNo.setText(headerInfo.getArrvoucherno() + "");
            mReceiptVoucherNo.setText(headerInfo.getCreater() + "");
            mErpVoucherNo.setText(headerInfo.getStrcreatetime() + "");
//            mErpVoucherNo.setText(headerInfo.getQualityno() + "");
            mPurchaseNo.setText(headerInfo.getErpvoucherno());
            mMaterialDesc.setText(headerInfo.getMaterialdesc());
            mVoucherQty.setText(headerInfo.getVoucherqty() + "");
            mQualifiedQty.setText(headerInfo.getQualityqty() + "");
            mSampQty.setText(headerInfo.getSampqty() + "");
            if (headerInfo.getVouchertype() == 47) {
                mPurchaseOrderNoDesc.setText("采购单号:");
//                mArrVoucherNoDesc.setText("到货单号:");
            } else if (headerInfo.getVouchertype() == 48) {
                mPurchaseOrderNoDesc.setText("工单号:");
//                mArrVoucherNoDesc.setText("完工单号:");
            }

            mVoucherNoDesc.setText(headerInfo.getErpvoucherdesc() + "  " + headerInfo.getErpstatuscodedesc());
            mErpVoucherName.setTextColor(getResources().getColor(R.color.colorPrimary));
            mPurchaseNo.setTextColor(getResources().getColor(R.color.colorPrimary));
            mReceiptVoucherNo.setTextColor(getResources().getColor(R.color.peru));
            mErpVoucherNo.setTextColor(getResources().getColor(R.color.colorPrimary));
            mMaterialDesc.setTextColor(getResources().getColor(R.color.colorPrimary));
            mMaterialNo.setTextColor(getResources().getColor(R.color.mediumseagreen));

        } else {
            mErpVoucherName.setText("");
            mStrongHoldNo.setText("");
            mReceiptVoucherNo.setText("");
            mErpVoucherNo.setText("");
            mQualifiedQty.setText("0");
        }
    }

    @Override
    public void setOrderDetailInfo(QualityHeaderInfo detailInfo) {
        if (detailInfo != null) {
            mMaterialNo.setText(detailInfo.getMaterialno());
            mBatchNo.setText(detailInfo.getBatchno());

        } else {
            mMaterialNo.setText("");
            mBatchNo.setText("");

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
            MessageBox.Show(context, "从物料界面传递数据给入库扫描界面出现异常" + e.getMessage());
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
            mOperationType.setVisibility(View.GONE);
            mRefer.setText("提交");
        } else if (mQualityType.equals("UNQUALIFIED")) {

        }


    }

    @Override
    public String getAreaNo() {
        return null;
    }


    @Override
    public void onActivityFinish(String title) {
        new AlertDialog.Builder(BaseApplication.context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage(title + " 是否返回上一页面？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 自动生成的方法
                        closeActivity();
                    }
                }).setNegativeButton("取消", null).show();
    }

    /**
     * @desc: 选择调拨仓库
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/8 15:29
     */
    public void selectWareHouse(List<String> list) {
        if (list != null && list.size() > 0) {
            final String[] items = list.toArray(new String[0]);
            new AlertDialog.Builder(context).setTitle(getResources().getString(R.string.activity_select_transfer_wareHouse_choice))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setCancelable(true)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法存根
                            String select_item = items[which].toString();
                            if (mPresenter != null) {
                                WareHouseInfo info = getWareHouseInfo(select_item);
                                if (info != null) {
                                    mPresenter.onTransferRefer(info);
                                }

                            }

                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    /**
     * @desc: 获取当前选择的仓库信息类
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/9 18:38
     */
    public WareHouseInfo getWareHouseInfo(String name) {
        if (BaseApplication.mCurrentUserInfo != null && name != null) {
            List<WareHouseInfo> wareHouseInfos = BaseApplication.mCurrentUserInfo.getModelListWarehouse();
            if (wareHouseInfos != null && wareHouseInfos.size() > 0) {
                for (WareHouseInfo wareHouseInfo : wareHouseInfos) {
                    String wareHouseName = wareHouseInfo.getWarehousename() != null ? wareHouseInfo.getWarehousename() : "";
                    if (name.equals(wareHouseName)) {
                        return wareHouseInfo;
                    }

                }
            }
        }
        return null;
    }

    /**
     * @desc: 获取仓库名称集合
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/9 7:21
     */
    public List<String> getWareHouseNameList() {
        List<String> sWareHouseNameList = new ArrayList<String>();
        if (BaseApplication.mCurrentUserInfo != null) {
            List<WareHouseInfo> wareHouseInfos = BaseApplication.mCurrentUserInfo.getModelListWarehouse();
            for (WareHouseInfo warehouse : wareHouseInfos) {
                if (warehouse.getWarehousename() != null && !warehouse.getWarehousename().equals("")) {
                    sWareHouseNameList.add(warehouse.getWarehousename());
                }
            }
        }
        return sWareHouseNameList;
    }
}
