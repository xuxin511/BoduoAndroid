package com.liansu.boduowms.modules.instock.noSourceOtherStorage.scan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.ui.adapter.instock.NoSourceScanDetailAdapter;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ Des: 无源杂入
 * @ Created by yangyiqing on 2020/7/15.
 */

@ContentView(R.layout.activity_no_source_scan)
public class NoSourceOtherScan extends BaseActivity implements INoSourceOtherScanView {
    @ViewInject(R.id.no_source_scan_instock_type_spinner)
    Spinner      mInstockTypeSpinner;
    @ViewInject(R.id.edt_area_no)
    EditText     mAreaNo;
    @ViewInject(R.id.no_source_scan_out_barcode)
    EditText     mBarcode;
    @ViewInject(R.id.no_source_scan_list)
    RecyclerView mRecyclerView;
    @ViewInject(R.id.outbarcode_info_material_no)
    TextView     mMaterialNo;
    @ViewInject(R.id.outbarcode_info_batch_no)
    TextView     mBatchNo;
    NoSourceScanDetailAdapter  mAdapter;
    NoSourceOtherScanPresenter mPresenter;
    ArrayAdapter               mInstockTypeArrayAdapter;
    Context                    mContext;
    public final int REQUEST_CODE_OK = 1;

    @Override
    protected void initViews() {
        super.initViews();
        mContext = NoSourceOtherScan.this;
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(mContext.getResources().getString(R.string.appbar_title_no_source_scan), true);
        BaseApplication.isCloseActivity = false;
        x.view().inject(this);

    }


    @Override
    protected void initData() {
        super.initData();
        mPresenter=new NoSourceOtherScanPresenter(mContext,this,mHandler);
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

    @Event(value = R.id.edt_area_no, type = View.OnKeyListener.class)
    private boolean edtStockScanClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            String areaNo = mAreaNo.getText().toString().trim();
            if (TextUtils.isEmpty(areaNo)) {
                CommonUtil.setEditFocus(mAreaNo);
                return true;
            }
            mPresenter.getAreaInfo(areaNo);
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
    @Event(value = R.id.no_source_scan_out_barcode, type = View.OnKeyListener.class)
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
    public void onReset() {
        setSpinnerData();
        setCurrentBarcodeInfo(null);
        bindListView(null);
        onBarcodeFocus();
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
    public void setCurrentBarcodeInfo(OutBarcodeInfo info) {
        if (info != null) {
            mMaterialNo.setText(info.getMaterialno());
            mBatchNo.setText(info.getBatchno());
        } else {
            mMaterialNo.setText("物料");
            mBatchNo.setText("批次");
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
    public String getAreaNo() {
        return mAreaNo.getText().toString().trim();
    }

    @Override
    public void setSpinnerData() {
        String var1 = mContext.getResources().getString(R.string.no_source_scan_instock_qualified);
        String var2 = mContext.getResources().getString(R.string.no_source_scan_instock_un_qualified);
        List<String> list = new ArrayList<>();
        list.add(var1);
        list.add(var2);
        // 设置spinner，不用管什么作用
        mInstockTypeArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        mInstockTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉风格
        mInstockTypeSpinner.setAdapter(mInstockTypeArrayAdapter); // 将adapter 添加到spinner中
        mInstockTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(
        ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });// 添加监听
        mInstockTypeSpinner.setVisibility(View.VISIBLE);// 设置默认值
    }

    @Override
    public String getSpinnerItemValue() {
        return mInstockTypeSpinner.getSelectedItem().toString();
    }
}
