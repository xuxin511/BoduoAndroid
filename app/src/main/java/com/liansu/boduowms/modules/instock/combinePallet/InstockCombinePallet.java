package com.liansu.boduowms.modules.instock.combinePallet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.ui.adapter.pallet.InstockPalletItemAdapter;
import com.liansu.boduowms.ui.dialog.MaterialInfoDialogActivity;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.DoubleClickCheck;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;


/**
 * @desc: 入库组托
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/6/25 22:00
 */
@ContentView(R.layout.activity_instock_combin_pallet)
public class InstockCombinePallet extends BaseActivity implements IInstockCombinePalletView, CompoundButton.OnCheckedChangeListener {
    @ViewInject(R.id.SW_Pallet)
    Switch           mPalletType;
    @ViewInject(R.id.txt_Pallet)
    TextView         txtPallet;
    @ViewInject(R.id.txt_Company)
    TextView         mStrongHoldCode;
    @ViewInject(R.id.txt_Batch)
    TextView         mBatchNo;
    @ViewInject(R.id.txt_Status)
    TextView         mStatus;
    @ViewInject(R.id.txt_EDate)
    TextView         txtEDate;
    @ViewInject(R.id.txt_MaterialName)
    TextView         mMaterialDesc;
    @ViewInject(R.id.txt_CartonNum)
    TextView         mNumber;
    @ViewInject(R.id.edt_Pallet)
    EditText         mPalletNo;
    @ViewInject(R.id.edt_Barcode)
    EditText         mBarcodeNo;
    @ViewInject(R.id.textView47)
    TextView         mBarCodeDesc;
    @ViewInject(R.id.lsv_PalletDetail)
    ListView         mListView;
    @ViewInject(R.id.btn_PrintPalletLabel)
    Button           mRefer;
    @ViewInject(R.id.combin_pallet_no_button)
    Button           mPalletNoButton;
    @ViewInject(R.id.combin_root)
    ConstraintLayout mRoot;
    @ViewInject(R.id.instock_combine_pallet_erp_voucher_no_desc)
    TextView         mErpVoucherNoDesc;
    @ViewInject(R.id.instock_combine_pallet_erp_voucher_no)
    EditText         mErpVoucherNo;
    InstockPalletItemAdapter      mAdapter;
    Context                       mContext = InstockCombinePallet.this;
    InstockCombinePalletPresenter mPresenter;
    public final int REQUEST_CODE_OK = 1;

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
        BaseApplication.toolBarTitle = new ToolBarTitle(getToolBarTitle(), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        mPalletNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPresenter != null) {
                    mPresenter.onPalletListRefer();
                }
            }
        });
//        showPalletScan(false);
        mPalletType.setClickable(false);
        mPalletType.setOnCheckedChangeListener(this);
        setSwitchButton(false);
    }

    @Override
    protected void initData() {
        super.initData();
        setCurrentBarcodeInfo(null);
        mPresenter = new InstockCombinePalletPresenter(mContext, this, mHandler);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    /*
        长按删除物料
         */
    @Event(value = R.id.lsv_PalletDetail, type = AdapterView.OnItemLongClickListener.class)
    private boolean lsvPalletDetailonLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (id >= 0) {
            OutBarcodeInfo delBarcode = (OutBarcodeInfo) mAdapter.getItem(position);
            String materialNo = delBarcode.getMaterialno();
            String batchNo = delBarcode.getBatchno();
            new AlertDialog.Builder(mContext).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除物料编号:" + materialNo + ",批次为:" + batchNo + "的物料行？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法
//                            mPresenter.onDelete(position);
                        }
                    }).setNegativeButton("取消", null).show();
        }
        return true;
    }

    @Event(value = R.id.edt_Barcode, type = View.OnKeyListener.class)
    private boolean edtBarcodeonKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String barcode = mBarcodeNo.getText().toString().trim();
            if (mPresenter != null) {
                mPresenter.scanBarcode(barcode);
                return true;
            }
        }
      return false;
    }


    @Event(R.id.btn_PrintPalletLabel)
    private void btnPrintPalletLabelClick(View v) {
        if (DoubleClickCheck.isFastDoubleClick(mContext)) {
            return;
        }

        if (mPresenter != null) {
            mPresenter.onPalletListRefer();
        }
    }


    @Override
    public void requestPalletBarcodeFocus() {
        CommonUtil.setEditFocus(mPalletNo);
    }

    @Override
    public void requestBarcodeFocus() {
        CommonUtil.setEditFocus(mBarcodeNo);
    }

    @Override
    public void setCurrentBarcodeInfo(OutBarcodeInfo info) {
        mMaterialDesc.setVisibility(View.GONE);
        if (info != null) {
            mStrongHoldCode.setText(info.getMaterialno() + "");
            mBatchNo.setText(info.getBatchno() + "");
            mMaterialDesc.setText(info.getMaterialno() + "");
            mStatus.setText(info.getQty() + "");
        } else {
            mStrongHoldCode.setText("物料编码");
            mBatchNo.setText("批次");
            mMaterialDesc.setText("物料名称");
            mStatus.setText("数量");
        }
    }

    @Override
    public void setSumCountInfo(String sumCountString) {
        mNumber.setText(sumCountString);
    }

    @Override
    public void setPalletNo(String palletNo) {
        mPalletNo.setText(palletNo);
    }

    @Override
    public void setBarcode(String barcode) {
        mBarcodeNo.setText(barcode);
    }

    @Override
    public void bindListView(List<OutBarcodeInfo> list) {
        if (mListView.getVisibility() == View.GONE) {
            mListView.setVisibility(View.VISIBLE);
        }
        if (list == null || list.size() == 0) {
            mListView.setVisibility(View.GONE);
            return;
        } else {
            mListView.setVisibility(View.VISIBLE);
        }
        mAdapter = new InstockPalletItemAdapter(mContext, list);
        mListView.setAdapter(mAdapter);
    }


    @Override
    public void onClear() {
        bindListView(null);
        mPalletNo.setEnabled(true);
        mBarcodeNo.setText("");
        mPalletNo.setText("");
        txtEDate.setText("");
        setCurrentBarcodeInfo(null);
        mNumber.setText("0 / 0");
        if (getCombinPalletType() == 2) {
            requestPalletBarcodeFocus();
        }
    }

    @Override
    public void setBottomText(String text) {
        Snackbar.make(mRoot, text, Snackbar.LENGTH_LONG).show();
    }

    /**
     * @desc: 1  新建组托   2.更新组托
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/2/13 14:34
     */
    @Override
    public int getCombinPalletType() {
        int type = 1;
        if (!mPalletType.isChecked()) {
            type = 1;
        } else {
            type = 2;
        }
        return type;
    }

    @Override
    public void showPalletScan(boolean isCheck) {
        mErpVoucherNoDesc.setVisibility(View.VISIBLE);
        txtPallet.setEnabled(true);
        if (!isCheck) {
            mPalletNo.setHint(R.string.combine_pallet_tray_no_scan);
            mPalletNoButton.setVisibility(View.GONE);
            mPalletNo.setEnabled(false);
        } else {
            mPalletNo.setHint(R.string.combine_pallet_outer_barcode_no_scan);
            mPalletNoButton.setVisibility(View.GONE);
            mPalletNo.setEnabled(true);
            txtPallet.setVisibility(View.VISIBLE);
            mPalletNo.setVisibility(View.VISIBLE);
            mBarcodeNo.setVisibility(View.GONE);
            mBarCodeDesc.setVisibility(View.GONE);
            CommonUtil.setEditFocus(mPalletNo);
        }
    }

    @Override
    public void setSwitchButton(boolean isCheck) {
        mPalletType.setOnCheckedChangeListener(null);
        mPalletType.setChecked(isCheck);
        mPalletType.setOnCheckedChangeListener(this);
    }

    @Override
    public void createDialog(OutBarcodeInfo info) {
        Intent intent = new Intent();
        intent.setClass(mContext, MaterialInfoDialogActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("info", info);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE_OK);
    }

    @Override
    public void setErpVoucherNo(String erpVoucherNo) {
        mErpVoucherNo.setText(erpVoucherNo);
    }

    /**
     * @desc: 切换组托模式
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/2/13 19:31
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

}
