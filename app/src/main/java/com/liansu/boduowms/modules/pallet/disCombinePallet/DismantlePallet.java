package com.liansu.boduowms.modules.pallet.disCombinePallet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;

import com.liansu.boduowms.ui.adapter.pallet.PalletItemAdapter;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.DoubleClickCheck;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;

@ContentView(R.layout.activity_dismantle_pallet)
public class DismantlePallet extends BaseActivity implements DismantlePalletView, CompoundButton.OnCheckedChangeListener {
    @ViewInject(R.id.conLay_DisPallet)
    ConstraintLayout conLayDIsPallet;
    @ViewInject(R.id.SW_DisPallet)
    Switch           SWPallet;
    @ViewInject(R.id.txt_Company)
    TextView         mStrongHoldCode;
    @ViewInject(R.id.txt_Batch)
    TextView         mBatchNo;
    @ViewInject(R.id.txt_Status)
    TextView         mStatus;
    @ViewInject(R.id.txt_MaterialName)
    TextView         mMaterialName;
    @ViewInject(R.id.txt_EDate)
    TextView         txtEDate;
    @ViewInject(R.id.textView2201)
    EditText         mPalletNo;
    @ViewInject(R.id.edt_Barcode)
    EditText         mBarcode;
    @ViewInject(R.id.lsv_DisPalletDetail)
    ListView         mListView;
    @ViewInject(R.id.btn_Config)
    Button           btnConfig;
    Context                  mContext = DismantlePallet.this;
    PalletItemAdapter        mAdapter;
    DismantlePalletPresenter mPresenter;

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
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.DisPallet_scan), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        SWPallet.setOnCheckedChangeListener(this);
        mPresenter = new DismantlePalletPresenter(mContext, this, mHandler);
    }

    @Override
    protected void initData() {
        super.initData();
        if (mPresenter != null) {
            mPresenter.changeModuleType(SWPallet.isChecked());
        }
        setCurrentBarcodeInfo(null);
    }


    @Event(value = R.id.edt_Barcode, type = View.OnKeyListener.class)
    private boolean edtBarcodeonKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String barcode = mBarcode.getText().toString().trim();
            if (mPresenter != null) {
                mPresenter.scanBarcode(barcode);
                return true;
            }
        }
        return false;
    }


    /*
    提交
     */
    @Event(R.id.btn_Config)
    private void btnConfigClick(View v) {
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
    public void setCurrentBarcodeInfo(OutBarcodeInfo info) {
        if (info != null) {
            mStrongHoldCode.setText(info.getStrongholdcode() + "");
            mBatchNo.setText(info.getBatchno() + "");
            mMaterialName.setText(info.getMaterialno() + "");
        } else {
            mStrongHoldCode.setText("物料编码");
            mBatchNo.setText("批次");
            mMaterialName.setText("物料名称");
            mStatus.setText("数量");
        }
    }

    @Override
    public void setPalletNo(String palletNo) {
       mPalletNo.setText(palletNo+"");
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
        mAdapter = new PalletItemAdapter(mContext, list);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClear() {
        mBarcode.setText("");
        txtEDate.setText("");
        setCurrentBarcodeInfo(null);
        mPalletNo.setText("");
        bindListView(null);
        CommonUtil.setEditFocus(mBarcode);
    }

    @Override
    public int getCombinPalletType() {
        int type = 1;
        if (!SWPallet.isChecked()) {
            type = 1;
        } else {
            type = 2;
        }
        return type;
    }

    @Override
    public void showPalletScan(boolean isCheck) {
        if (!isCheck) {
            conLayDIsPallet.setVisibility(View.VISIBLE);
            // mBarcode.setHint(R.string.Hit_ScanBarcode);
        } else {
            conLayDIsPallet.setVisibility(View.GONE);
            //  mBarcode.setHint(R.string.Hit_ScanPallet);
        }
    }

    @Override
    public void setSwitchButton(boolean isCheck) {
        SWPallet.setOnCheckedChangeListener(null);
        SWPallet.setChecked(isCheck);
        SWPallet.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mPresenter != null) {
            mPresenter.changeModuleType(isChecked);
        }
    }

    /*
   长按删除物料
    */
    @Event(value = R.id.lsv_DisPalletDetail, type = AdapterView.OnItemLongClickListener.class)
    private boolean lsvPalletDetailonLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (id >= 0) {
            OutBarcodeInfo delBarcode = (OutBarcodeInfo) mAdapter.getItem(position);
            final String barcode = delBarcode.getBarcode();
            new AlertDialog.Builder(mContext).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除物料数据？\n条码：" + barcode)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法
                            mPresenter.onDelete(position);
                        }
                    }).setNegativeButton("取消", null).show();
        }
        return true;
    }
}
