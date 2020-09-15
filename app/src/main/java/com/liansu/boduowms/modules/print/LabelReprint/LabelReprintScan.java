package com.liansu.boduowms.modules.print.LabelReprint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.modules.print.linkos.PrintType;
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
 * @ Des: 标签打印  已废弃  ，用 批量打印{@link com.liansu.boduowms.modules.instock.batchPrint.print.BaseOrderLabelPrint }类代替
 * @ Created by yangyiqing on 2020/7/15.
 */

@ContentView(R.layout.activity_label_reprint)
public class LabelReprintScan extends BaseActivity implements ILabelReprintView {
    @ViewInject(R.id.label_reprint_type_spinner)
    Spinner  mPrintTypeSpinner;
    @ViewInject(R.id.label_reprint_erp_voucher_no)
    EditText mErpVoucherNo;
    @ViewInject(R.id.label_reprint_material_no)
    EditText mMaterialNo;
    @ViewInject(R.id.label_reprint_material_name)
    EditText mMaterialDesc;
    @ViewInject(R.id.label_reprint_batch_no)
    EditText mBatchNo;
    @ViewInject(R.id.label_reprint_batch_no_select)
    Button   mBatchNoSelect;
    @ViewInject(R.id.label_reprint_product_time)
    EditText mProductTime;
    @ViewInject(R.id.label_reprint_product_time_select)
    Button   mProductTimeSelect;
    @ViewInject(R.id.label_reprint_print)
    Button   mPrint;
    @ViewInject(R.id.label_reprint_qty)
    EditText mQty;
    @ViewInject(R.id.label_reprint_print_count)
    EditText mPrintCount;
    LabelReprintPresenter mPresenter;
    ArrayAdapter          mPrintTypeArrayAdapter;
    Context               mContext;
    public final int REQUEST_CODE_OK = 1;

    @Override
    protected void initViews() {
        super.initViews();
        mContext = LabelReprintScan.this;
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(mContext.getResources().getString(R.string.appbar_title_label_reprint), true);
        BaseApplication.isCloseActivity = false;
        x.view().inject(this);
        mQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getQty();
            }
        });

    }


    @Override
    protected void initData() {
        super.initData();
        mPresenter = new LabelReprintPresenter(mContext, this, mHandler);
        onReset();

    }

    @Override
    public void onHandleMessage(Message msg) {
        if (mPresenter != null) {
            mPresenter.onHandleMessage(msg);
        }
    }

    @Event(R.id.btn_refer)
    private void onclick(View view) {
        if (mPresenter != null) {
            mPresenter.onOrderRefer();
        }
    }


    /**
     * @desc: 获取订单
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/7 12:45
     */
    @Event(value = {R.id.label_reprint_erp_voucher_no, R.id.label_reprint_material_no, R.id.label_reprint_batch_no, R.id.label_reprint_product_time, R.id.label_reprint_qty, R.id.label_reprint_print_count}, type = View.OnKeyListener.class)
    private boolean outErpVoucherNoScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            switch (v.getId()) {
                case R.id.label_reprint_erp_voucher_no:
                    String erpVoucherNo = mErpVoucherNo.getText().toString().trim();
                    mPresenter.getOrderDetailInfoList(erpVoucherNo);
                    break;
                case R.id.label_reprint_material_no:
                    String outBarcode = mMaterialNo.getText().toString().trim();
                    mPresenter.scanBarcode(outBarcode);
                    break;
                case R.id.label_reprint_batch_no:
                    onProductTimeFocus();
                    break;
                case R.id.label_reprint_product_time:
                    onQtyFocus();
                    break;
                case R.id.label_reprint_qty:

                    break;
                case R.id.label_reprint_print_count:
                    break;

            }


        }

        return false;
    }


    @Override
    public void onReset() {
        setSpinnerData();

    }

    @Override
    public void onErpVoucherNoFocus() {
        CommonUtil.setEditFocus(mErpVoucherNo);
    }

    @Override
    public void onMaterialNoFocus() {
        CommonUtil.setEditFocus(mMaterialNo);
    }

    @Override
    public void onBatchNoFocus() {
        CommonUtil.setEditFocus(mBatchNo);
    }

    @Override
    public void onProductTimeFocus() {
        CommonUtil.setEditFocus(mProductTime);
    }

    @Override
    public void onQtyFocus() {
        CommonUtil.setEditFocus(mQty);
    }

    @Override
    public void onPrintCountFocus() {

        CommonUtil.setEditFocus(mPrintCount);
    }


    @Override
    public void createDialog(OutBarcodeInfo info, int printType) {
        Intent intent = new Intent();
        intent.setClass(mContext, MaterialInfoDialogActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("info", info);
        bundle.putInt("printType", printType);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE_OK);
    }


    @Override
    public void setSpinnerData() {
        String var1 = PrintType.PRINT_TYPE_PALLET_STYLE;
        String var2 = PrintType.PRINT_TYPE_RAW_MATERIAL_STYLE;
        String var3 = PrintType.PRINT_TYPE_FINISHED_PRODUCT_STYLE;
        List<String> list = new ArrayList<>();
        list.add(var1);
        list.add(var2);
        list.add(var3);
        // 设置spinner，不用管什么作用
        mPrintTypeArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        mPrintTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉风格
        mPrintTypeSpinner.setAdapter(mPrintTypeArrayAdapter); // 将adapter 添加到spinner中
        mPrintTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(
        ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });// 添加监听
        mPrintTypeSpinner.setVisibility(View.VISIBLE);// 设置默认值
    }


    @Override
    public String getProductTimeValue() {
        return mProductTime.getText().toString().trim();
    }

    @Override
    public String getBatchNo() {
        return mBatchNo.getText().toString().trim();
    }

    @Override
    public void setBatchNoList(List<String> batchNoList) {
        final String[] items = batchNoList.toArray(new String[0]);
        new AlertDialog.Builder(mContext).setTitle(getResources().getString(R.string.label_reprint_batch_no_list_desc))// 设置对话框标题
                .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                .setCancelable(false)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 自动生成的方法存根
                        String batchNo = items[which].toString();
                        mBatchNo.setText(batchNo);

                        dialog.dismiss();
                    }
                }).show();

    }

    @Override
    public String getPrintLabelType() {
        return mPrintTypeSpinner.getSelectedItem().toString();
    }

    @Override
    public void setMaterialInfo(OutBarcodeInfo outBarcodeInfo) {
        if (outBarcodeInfo != null) {
            String materialNo = outBarcodeInfo.getMaterialno();
            String materialDesc = outBarcodeInfo.getMaterialdesc();
            mMaterialNo.setText(materialNo);
            mMaterialDesc.setText(materialDesc);
        } else {
            mMaterialNo.setText("");
            mMaterialDesc.setText("");
        }
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
                            mPresenter.onScan(info);
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


    @Event({R.id.label_reprint_batch_no_select, R.id.label_reprint_product_time, R.id.label_reprint_product_time_select})
    private void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.label_reprint_batch_no_select:
                setBatchNoList(mPresenter.getModel().getBatchNoList());
                break;
            case R.id.label_reprint_product_time:
                break;
            case R.id.label_reprint_print:
                String materialNo = mMaterialNo.getText().toString().trim();
                String materialName = mMaterialDesc.getText().toString().trim();
                String batchNo = mBatchNo.getText().toString().trim();
                String productTime = mProductTime.getText().toString().trim();
                String qty = mQty.getText().toString().trim();
                String printCount = mPrintCount.getText().toString().trim();
                OutBarcodeInfo info = new OutBarcodeInfo();
                info.setMaterialno(materialNo);
                info.setMaterialdesc(materialName);
                info.setBatchno(batchNo);
                info.setProdate(productTime);
                info.setQty(Float.parseFloat(qty));
                mPresenter.onPrint(info, Integer.parseInt(printCount));
                break;
        }


    }
}
