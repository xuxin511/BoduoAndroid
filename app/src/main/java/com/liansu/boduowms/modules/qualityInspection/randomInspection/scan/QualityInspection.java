package com.liansu.boduowms.modules.qualityInspection.randomInspection.scan;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


/**
 * @ Des: 抽检扫描
 * @ Created by yangyiqing on 2020/6/28.
 */
@ContentView(R.layout.activity_quality_inspection2)
public class QualityInspection extends BaseActivity implements IQualityInspectionView {
    @ViewInject(R.id.quality_inspection_erp_voucher_no)
    TextView mErpVoucherNo;
    @ViewInject(R.id.quality_inspection_area_no)
    EditText mAreaNo;
    @ViewInject(R.id.quality_inspection_barcode_no)
    EditText mBarcode;
    @ViewInject(R.id.quality_inspection_material_select)
    Button   mMaterialSelect;
    @ViewInject(R.id.quality_inspection_qty)
    EditText mQty;
    @ViewInject(R.id.outbarcode_info_material_no)
    TextView mMaterialNo;
    @ViewInject(R.id.outbarcode_info_batch_no)
    TextView mBatchNo;
    @ViewInject(R.id.quality_inspection_qty_refer)
    Button   mRefer;
    @ViewInject(R.id.outbarcode_info_random_inspection_qty_desc)
    TextView mRandomInspectionQtyDesc;
    @ViewInject(R.id.outbarcode_info_random_inspection_qty)
    TextView mRandomInspectionQty;
    @ViewInject(R.id.outbarcode_info_material_name)
    TextView mMaterialDesc;
    @ViewInject(R.id.quality_inspection_purchase_order_no)
    TextView mPurchaseOrderNo;
    @ViewInject(R.id.quality_inspection_arr_voucher_no_order)
    TextView mArrVoucherNo;
    @ViewInject(R.id.random_inspection_voucher_qty)
    TextView mVoucherQty;
    @ViewInject(R.id.random_inspection_sampqty_qty)
    TextView mSampQty;
    @ViewInject(R.id.quality_inspection_erp_voucher_name)
    TextView mErpVoucherName;
    @ViewInject(R.id.quality_inspection_purchase_order_no_desc)
    TextView mPurchaseOrderNoDesc;
    @ViewInject(R.id.quality_inspection_arr_voucher_no_order_desc)
    TextView mArrVoucherNoDesc;
    QualityInspectionPresenter mPresenter;
    Context                    mContext = QualityInspection.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.context = QualityInspection.this;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.quality_inspection_title) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno(), true);
        x.view().inject(this);
        mRandomInspectionQty.setVisibility(View.VISIBLE);
        mRandomInspectionQtyDesc.setVisibility(View.VISIBLE);
        mRandomInspectionQtyDesc.setText("送检数:");
        mRandomInspectionQtyDesc.setVisibility(View.GONE);
        mRandomInspectionQty.setVisibility(View.GONE);
        closeKeyBoard(mAreaNo,mBarcode,mQty);
    }

    @Override
    public void onHandleMessage(Message msg) {
        mPresenter.onHandleMessage(msg);
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        qualityInspection
        QualityHeaderInfo info = getIntent().getParcelableExtra("qualityInspection");
        mPresenter = new QualityInspectionPresenter(mContext, this, mHandler, info);
        mPresenter.getOrderDetailInfo();
    }

    @Event(value = {R.id.quality_inspection_erp_voucher_no, R.id.quality_inspection_area_no, R.id.quality_inspection_barcode_no, R.id.quality_inspection_qty}, type = View.OnKeyListener.class)
    private boolean onScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
//            keyBoardCancle();
            switch (v.getId()) {
                case R.id.quality_inspection_area_no:
                    String areaNo = mAreaNo.getText().toString().trim();
                    mPresenter.getAreaInfo(areaNo);
                    break;
                case R.id.quality_inspection_barcode_no:
                    String barcode = mBarcode.getText().toString().trim();
                    mPresenter.scanBarcode(barcode);
                    break;

                case R.id.quality_inspection_qty:
                    mPresenter.setBarcodeQty();
                    break;
            }
        }

        return false;
    }

    @Event(value = {R.id.quality_inspection_material_select, R.id.quality_inspection_qty_refer}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.quality_inspection_material_select:

                break;
            case R.id.quality_inspection_qty_refer:
                mPresenter.onOrderRefer();
                break;
        }


    }

    @Override
    public void onErpVoucherNoFocus() {

    }

    @Override
    public void onAreaNoFocus() {
        CommonUtil.setEditFocus(mAreaNo);
    }

    @Override
    public void onBarcodeFocus() {
        CommonUtil.setEditFocus(mBarcode);

    }

    @Override
    public void onQtyFocus() {
        CommonUtil.setEditFocus(mQty);
    }


    @Override
    public void setOrderInfo(QualityHeaderInfo info) {
        if (info != null) {
            if (info.getVouchertype() == 47) {
                mPurchaseOrderNoDesc.setText("采购单号:");
                mArrVoucherNoDesc.setText("到货单号:");
            } else if (info.getVouchertype() == 48) {
                mPurchaseOrderNoDesc.setText("工单号:");
                mArrVoucherNoDesc.setText("完工单号:");
            }
            mErpVoucherNo.setText(info.getQualityno());
            mErpVoucherNo.setTextColor(getResources().getColor(R.color.colorPrimary));
            mMaterialNo.setText(info.getMaterialno());
            mMaterialNo.setTextColor(getResources().getColor(R.color.mediumseagreen));
            mBatchNo.setText(info.getBatchno());
            mRandomInspectionQty.setText("0/" + info.getVoucherqty());
            mMaterialDesc.setText(info.getMaterialdesc());
            mMaterialDesc.setTextColor(getResources().getColor(R.color.colorPrimary));
            mPurchaseOrderNo.setText(info.getErpvoucherno());
            mPurchaseOrderNo.setTextColor(getResources().getColor(R.color.colorPrimary));
            mArrVoucherNo.setText(info.getArrvoucherno());
            mArrVoucherNo.setTextColor(getResources().getColor(R.color.peru));
            mVoucherQty.setText(info.getVoucherqty() + "");
            mSampQty.setText(info.getSampqty() + "");
            mSampQty.setTextColor(getResources().getColor(R.color.red));
            mErpVoucherName.setText(info.getErpvoucherdesc() + "  " + info.getErpstatuscodedesc());
            mErpVoucherName.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            mErpVoucherNo.setText(info.getQualityno());
            mMaterialNo.setText(info.getMaterialno());
            mBatchNo.setText(info.getBatchno());
            mRandomInspectionQty.setText("" + info.getVoucherqty() + "");
            mMaterialDesc.setText(info.getMaterialdesc());
            mPurchaseOrderNo.setText(info.getErpvoucherno());
            mArrVoucherNo.setText(info.getArrvoucherno());
            mVoucherQty.setText("0");
            mSampQty.setText("0");
            mErpVoucherName.setText(info.getErpvoucherdesc());
        }


    }

    @Override
    public void onActivityFinish() {
        closeActivity();
    }

    @Override
    public void onReset() {
        mBarcode.setText("");
        mQty.setText("0");
        onBarcodeFocus();
    }


    @Override
    public void setQty(String qty) {
        mQty.setText(qty);
    }

    @Override
    public float getQty() {
        String qty = mQty.getText().toString().trim();
        if (qty.equals("")) qty = "0";
        return Float.parseFloat(qty);
    }

    @Override
    public void setScannedQty(String qty) {
        mRandomInspectionQty.setText(qty);
    }


}
