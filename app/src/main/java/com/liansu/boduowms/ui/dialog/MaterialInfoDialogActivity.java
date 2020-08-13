package com.liansu.boduowms.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.DoubleClickCheck;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/2.
 */
@ContentView(R.layout.dialog_material_info)
public class MaterialInfoDialogActivity extends AppCompatActivity {
    Context mContext = MaterialInfoDialogActivity.this;
    @ViewInject(R.id.dialog_material_info_title)
    TextView mTitle;
    @ViewInject(R.id.dialog_material_info_material_no)
    EditText mMaterialNo;
    @ViewInject(R.id.dialog_material_info_batch_no)
    EditText mBatchNo;
    @ViewInject(R.id.dialog_material_info_qty)
    EditText mQty;
    @ViewInject(R.id.dialog_material_info_positiveTextView)
    TextView mPositiveTextView;
    @ViewInject(R.id.dialog_material_info_negativeTextView)
    TextView mNegativeTextView;
    @ViewInject(R.id.dialog_material_info_material_name)
    EditText mMaterialName;
    @ViewInject(R.id.dialog_material_info_outer_barcode_qty)
    EditText mOuterBoxQty;
    @ViewInject(R.id.dialog_material_info_pack_count)
    EditText mPackCount;
    @ViewInject(R.id.dialog_material_info_material_bulk_volume)
    EditText mBulkVolume;
    @ViewInject(R.id.dialog_material_info_outer_barcode_qty_desc)
    TextView mOutBoxQtyDesc;
    @ViewInject(R.id.dialog_material_info_material_pack_count_desc)
    TextView mPackCountDesc;
    @ViewInject(R.id.dialog_material_info_material_bulk_volume_desc)
    TextView mBulkVolumeDesc;
    OutBarcodeInfo mOutBarcode;
    public static final int MATERIAL_INFO_TYPE_RAW_MATERIAL     = 1;
    public static final int MATERIAL_INFO_TYPE_FINISHED_PRODUCT = 2;
    private             int mMaterialType                       = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //隐藏输入法
        x.view().inject(this);
        try {
            mOutBarcode = getIntent().getParcelableExtra("info");
            if (mOutBarcode != null) {
                initData(mOutBarcode, -1);
                setViewStatus(mMaterialType);
                mPositiveTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveData();
                    }
                });

                mNegativeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "出现意料之外的异常:" + e.getMessage());
        }
    }

    @Event(value = {R.id.dialog_material_info_material_no, R.id.dialog_material_info_batch_no, R.id.dialog_material_info_outer_barcode_qty, R.id.dialog_material_info_pack_count, R.id.dialog_material_info_material_bulk_volume, R.id.dialog_material_info_qty}, type = View.OnKeyListener.class)
    private boolean barcodeScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();

            switch (v.getId()) {
                case R.id.dialog_material_info_material_no:
                    requestBatchNoFocus();
                    break;
                case R.id.dialog_material_info_batch_no:
                    if (mMaterialType == MATERIAL_INFO_TYPE_FINISHED_PRODUCT) {
                        requestOuterBoxQtyFocus();
                    } else {

                    }
                    break;
                case R.id.dialog_material_info_outer_barcode_qty:
                    float pcakcount = Float.parseFloat(mPackCount.getText().toString().trim());
                    if (pcakcount == 0) {
                        requestPackCountFocus();
                    } else {
                        requestBulkVolumeFocus();
                    }
                    ;
                    sumQtyOfFinishedProduct();
                    break;
                case R.id.dialog_material_info_pack_count:
                    requestBulkVolumeFocus();
                    sumQtyOfFinishedProduct();
                    break;
                case R.id.dialog_material_info_material_bulk_volume:
                    sumQtyOfFinishedProduct();
                    requestQtyFocus();
                case R.id.dialog_material_info_qty:
                    if (DoubleClickCheck.isFastDoubleClick(mContext, 200)) {
                        return false;
                    }
                    saveData();
                    break;

            }

            return true;
        }
        return false;
    }

    /**
     * @desc:
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/2 17:32
     */
    public void initData(OutBarcodeInfo info, int materialType) {
        mTitle.setText("添加物料信息");
        mMaterialType = materialType;
        if (info != null) {
            float packQty = info.getPackQty();
            if (packQty != 0) {
                mMaterialType = MATERIAL_INFO_TYPE_FINISHED_PRODUCT;
            } else {
                mMaterialType = MATERIAL_INFO_TYPE_RAW_MATERIAL;
            }
            String materialNo = info.getMaterialno() != null ? info.getMaterialno() : "";
            String materialName = info.getMaterialdesc() != null ? info.getMaterialdesc() : "";
            String batchNo = info.getBatchno() != null ? info.getBatchno() : "";
            float packCount = info.getPackQty();
            mMaterialName.setText(materialName);
            mMaterialName.setEnabled(false);
            mMaterialNo.setText(materialNo);
            mBatchNo.setText(info.getBatchno());
            mQty.setText(info.getQty() + "");
            mPackCount.setText(packQty + "");
            mBulkVolume.setText("0");
            mOuterBoxQty.setText("0");
            if (materialNo.equals("")) {
                mMaterialNo.setEnabled(true);
                requestMaterialNoFocus();
            } else {
                mMaterialNo.setEnabled(false);

            }
            if (batchNo.equals("") && !materialNo.equals("")) {
                requestBatchNoFocus();
            }
            if (info.getPackQty() != 0) {
                mQty.setText("0");
                mQty.setEnabled(false);
            }
            requestBatchNoFocus();
        }


    }

    public void sumQtyOfFinishedProduct() {
        float outerBoxQty = Float.parseFloat(mOuterBoxQty.getText().toString().trim());
        float packCount = Float.parseFloat(mPackCount.getText().toString().trim());
        float bulkVolume = Float.parseFloat(mBulkVolume.getText().toString().trim());

        float sumOuterBoxQty = ArithUtil.mul(outerBoxQty, packCount);
        float sumQty = ArithUtil.add(sumOuterBoxQty, bulkVolume);  //外箱数*包装量+散装数量
        mQty.setText(sumQty + "");
    }

    /**
     * @desc: 保存物料数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/2 17:32
     */
    public void saveData() {
        String batchNo = mBatchNo.getText().toString().trim();
        String materialNo = mMaterialNo.getText().toString().trim();
        int packQty = Integer.parseInt(mPackCount.getText().toString().trim());
        float qty = Float.parseFloat(mQty.getText().toString());
        if (batchNo.equals("")) {
            MessageBox.Show(mContext, "批次不能为空");
            return;
        }
        if (qty <= 0) {
            MessageBox.Show(mContext, "数量必须大于0");
            return;
        }
        if (materialNo.equals("")) {
            MessageBox.Show(mContext, "物料编号不能为空");
            return;
        }

        if (packQty != mOutBarcode.getPackQty()) {
            mOutBarcode.setPackQty(packQty);
        }
        mOutBarcode.setMaterialno(mMaterialNo.getText().toString().trim());
        mOutBarcode.setBatchno(batchNo);
        mOutBarcode.setQty(qty);

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("resultInfo", mOutBarcode);
        intent.putExtras(bundle);
        // 设置返回码和返回携带的数据
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void setViewStatus(int materialType) {
        if (materialType == MATERIAL_INFO_TYPE_FINISHED_PRODUCT) {
            mOuterBoxQty.setVisibility(View.VISIBLE);
            mOutBoxQtyDesc.setVisibility(View.VISIBLE);
            mPackCount.setVisibility(View.VISIBLE);
            mPackCountDesc.setVisibility(View.VISIBLE);
            mBulkVolume.setVisibility(View.VISIBLE);
            mBulkVolumeDesc.setVisibility(View.VISIBLE);
            mQty.setEnabled(false);
        } else if (materialType == MATERIAL_INFO_TYPE_RAW_MATERIAL) {
            mOuterBoxQty.setVisibility(View.GONE);
            mOutBoxQtyDesc.setVisibility(View.GONE);
            mPackCount.setVisibility(View.GONE);
            mPackCountDesc.setVisibility(View.GONE);
            mBulkVolume.setVisibility(View.GONE);
            mBulkVolumeDesc.setVisibility(View.GONE);
            mQty.setEnabled(true);
        }
    }

    public void requestMaterialNoFocus() {
        CommonUtil.setEditFocus(mMaterialNo);
    }

    public void requestBatchNoFocus() {
        CommonUtil.setEditFocus(mBatchNo);
    }

    public void requestOuterBoxQtyFocus() {
        CommonUtil.setEditFocus(mOuterBoxQty);
    }

    public void requestPackCountFocus() {
        CommonUtil.setEditFocus(mPackCount);
    }

    public void requestBulkVolumeFocus() {
        CommonUtil.setEditFocus(mBulkVolume);
    }

    public void requestQtyFocus() {
        CommonUtil.setEditFocus(mQty);
    }

    /**
     * 隐藏键盘
     */
    public void keyBoardCancle() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
