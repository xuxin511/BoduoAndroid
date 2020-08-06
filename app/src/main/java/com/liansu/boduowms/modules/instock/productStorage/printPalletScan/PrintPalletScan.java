package com.liansu.boduowms.modules.instock.productStorage.printPalletScan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.paroductStorage.ProductDetailInfo;
import com.liansu.boduowms.modules.instock.productStorage.scan.ProductStorageScan;
import com.liansu.boduowms.ui.adapter.instock.productStorage.ProductScanDetailAdapter;
import com.liansu.boduowms.ui.dialog.MaterialInfoDialogActivity;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ Des: 产品入库托盘标签打印
 * @ Created by yangyiqing on 2020/7/15.
 */

@ContentView(R.layout.activity_product_storage_print_scan)
public class PrintPalletScan extends BaseActivity implements IPrintPalletScanView {
    @ViewInject(R.id.product_storage_print_out_barcode)
    EditText     mBarcode;
    @ViewInject(R.id.txt_VoucherNo)
    EditText     mErpVoucherNo;
    @ViewInject(R.id.btn_refer)
    Button       mJumpButton;
    @ViewInject(R.id.lsv_ReceiptScan)
    RecyclerView mRecyclerView;
    PrintPalletScanPresenter mPresenter;
    Context                  mContext = PrintPalletScan.this;
    /*业务类型 */
    protected    String mBusinessType   = "";
    public final int    REQUEST_CODE_OK = 1;
    ProductScanDetailAdapter mAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(mContext.getResources().getString(R.string.appbar_title_product_storage_pallet_label_print)+"-"+BaseApplication.mCurrentWareHouseInfo.getWarehousename(), true);
        BaseApplication.isCloseActivity = false;
        x.view().inject(this);
//        closeKeyBoard(mBarcode);

    }

    @Override
    public void onHandleMessage(Message msg) {
        if (mPresenter != null) {
            mPresenter.onHandleMessage(msg);
        }
    }

    @Override
    protected void initData() {
//        OrderHeaderInfo headerInfo = getIntent().getParcelableExtra("OrderHeaderInfo");
        mBusinessType = getIntent().getStringExtra("BusinessType").toString();
        if (mPresenter == null) {
            mPresenter = new PrintPalletScanPresenter(mContext, this, mHandler, null, mBusinessType);

        }

    }


    @Event(R.id.btn_refer)
    private void btnCombinePalletClick(View view) {
        if (mPresenter != null) {
            Intent intent = new Intent(PrintPalletScan.this, ProductStorageScan.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("OrderHeaderInfo", mPresenter.getModel().getOrderHeaderInfo());
            intent.putExtra("BusinessType", mPresenter.getModel().getBusinessType());
            intent.putExtras(bundle);
            startActivityLeft(intent);
        }

    }



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mPresenter != null) {
//            mPresenter.getOrderDetailInfoList(mPresenter.getModel().getOrderHeaderInfo());
//        }
    }

    @Override
    public void bindListView(List<ProductDetailInfo> detailInfos) {
        mAdapter = new ProductScanDetailAdapter(mContext, "", detailInfos);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    /**
     * @desc: 单据号扫描
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/7 12:45
     */
    @Event(value = R.id.txt_VoucherNo, type = View.OnKeyListener.class)
    private boolean outErpVoucherNoScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String erpVoucherNo = mErpVoucherNo.getText().toString().trim();
            if (mPresenter != null) {
                OrderHeaderInfo orderHeaderInfo=new OrderHeaderInfo();
                orderHeaderInfo.setErpvoucherno(erpVoucherNo);
                mPresenter.getOrderDetailInfoList(orderHeaderInfo);
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
    @Event(value = R.id.product_storage_print_out_barcode, type = View.OnKeyListener.class)
    private boolean outBarcodeScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String barcode = mBarcode.getText().toString().trim();
            if (mPresenter != null) {
                mPresenter.scanBarcode(barcode);
            }
        }

        return false;
    }

    @Override
    public void onReset() {

    }

    @Override
    public void onBarcodeFocus() {
        CommonUtil.setEditFocus(mBarcode);
    }

    @Override
    public void onErpVoucherNo() {
        CommonUtil.setEditFocus(mErpVoucherNo);
    }

    @Override
    public void setErpVoucherNo(String erpVoucherNo) {
        mErpVoucherNo.setText(erpVoucherNo);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_CODE_OK: //返回的结果是来自于Activity B
                    if (resultCode == Activity.RESULT_OK) {
                        OutBarcodeInfo info = data.getParcelableExtra("resultInfo");
                        if (info != null) {
                            mPresenter.onCombinePalletRefer(info);
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

}
