package com.liansu.boduowms.modules.inHouseStock.print;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.modules.setting.SettingMainActivity;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.DateUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * @ Des:   条码打印基类  打印数据  蓝牙托盘打印
 * @ Created by yangyiqing on 2020/10/9.
 */
@ContentView(R.layout.base_instock_house_label_print)
public class BaseInStockHouseLabelPrint extends BaseActivity implements BaseInStockHouseLabelPrintView {
    @ViewInject(R.id.base_order_label_print_erp_voucher_no_desc)
    TextView mErpVoucherNoDesc;
    @ViewInject(R.id.base_order_label_print_erp_voucher_no)
    TextView mErpVoucherNo;
    @ViewInject(R.id.base_order_label_print_material_no)
    EditText mMaterialNo;
    @ViewInject(R.id.base_order_label_print_material_name_desc)
    TextView mMaterialDesc;
    @ViewInject(R.id.base_order_label_print_batch_no)
    EditText mBatchNo;
    @ViewInject(R.id.base_order_label_print_remain_qty_desc)
    TextView mRemainQtyDesc;
    @ViewInject(R.id.base_order_label_print_remain_qty)
    EditText mRemainQty;
    @ViewInject(R.id.base_order_label_print_pack_qty_desc)
    TextView mPackQtyDesc;
    @ViewInject(R.id.base_order_label_print_pack_qty)
    EditText mPackQty;
    @ViewInject(R.id.base_order_label_print_pallet_no_desc)
    TextView mPalletNoDesc;
    @ViewInject(R.id.base_order_label_print_pallet_no)
    EditText mPalletQty;
    @ViewInject(R.id.base_order_label_print_button)
    Button   mPrint;
    @ViewInject(R.id.base_order_label_print_print_count_desc)
    TextView mPrintCountDesc;
    @ViewInject(R.id.base_order_label_print_outer_box_print_count)
    EditText mPrintCount;
    Context                             mContext   = BaseInStockHouseLabelPrint.this;
    BaseInStockHouseLabelPrintPresenter mPresenter;
    int                                 mPrintType = PrintBusinessModel.PRINTER_LABEL_TYPE_NONE;


    @Override
    public void onHandleMessage(Message msg) {
        if (mPresenter != null) {
            mPresenter.onHandleMessage(msg);
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle("", true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
//        closeKeyBoard(mBatchNo);
//        closeKeyBoard(mRemainQty);
//        closeKeyBoard(mPackQty);
//        closeKeyBoard(mPalletQty);
//        closeKeyBoard(mPrintCount);
//        closeKeyBoard(mBatchNo, mRemainQty, mPackQty, mPalletQty, mPrintCount);

    }

    @Override
    protected void initData() {
        super.initData();


    }

    @Event(R.id.base_order_label_print_button)
    private void onPrint(View view) {
        if (mPresenter != null) {
            mPresenter.onPrint();
        }
    }

    /**
     * @desc: 扫描物料编号
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/7 12:45
     */
    @Event(value = {R.id.base_order_label_print_material_no}, type = View.OnKeyListener.class)
    private boolean onMaterialNoScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            switch (v.getId()) {
                case R.id.base_order_label_print_material_no:
                    if (mPresenter != null) {
                        String materialNo = mMaterialNo.getText().toString().trim();
                        mPresenter.onScan(materialNo, mPrintType);

                    }
                    break;
            }

        }

        return false;
    }

    @Event(value = {R.id.base_order_label_print_batch_no, R.id.base_order_label_print_pack_qty, R.id.base_order_label_print_pallet_no}, type = View.OnKeyListener.class)
    private boolean onScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            switch (v.getId()) {
                case R.id.base_order_label_print_batch_no:
                    if (mPresenter != null) {
                        if (mPresenter.getModel().getMaterialInfo()==null){
                            MessageBox.Show(mContext, "物料信息不能为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onMaterialFocus();

                                }
                            });
                            return false;
                        }
                        String batchNo = mBatchNo.getText().toString().trim();
                        if (batchNo.equals("")) {
                            MessageBox.Show(mContext, "批次不能为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onBatchNoFocus();

                                }
                            });
                            return false;
                        }
                        if (batchNo.contains("%")) {
                            BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(batchNo);
                            if (resultInfo.getHeaderStatus()) {
                                batchNo = resultInfo.getInfo().getBatchno();
                                mBatchNo.setText(batchNo);
                            }
                        }

                        if (!DateUtil.isBeforeOrCompareToday(batchNo.trim(), "yyyyMMdd")) {
                            MessageBox.Show(mContext, "校验日期格式失败:" + "日期格式不正确或日期大于今天", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onBatchNoFocus();
                                }
                            });
                            return false;
                        }
                        if (mPresenter.getModel().getPrintType() == PrintBusinessModel.PRINTER_LABEL_TYPE_OUTER_BOX) {
                            if (mPresenter.getModel().getCurrentPrintInfo().getPackQty() != 0) {
                                onPrintCountFocus();
                            } else {
                                onPackQtyFocus();
                            }

                        }

                    }
                    break;
                case R.id.base_order_label_print_pack_qty:
                    break;
                case R.id.base_order_label_print_pallet_no:
                    if (mPresenter != null) {
                        mPresenter.onPrint();
                    }
                    break;

                case R.id.base_order_label_print_outer_box_print_count:
                    if (mPresenter != null) {
                        mPresenter.onPrint();
                    }

                    break;

            }

        }

        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            int voucherType=getIntent().getIntExtra("VOUCHER_TYPE", OrderType.ORDER_TYPE_NONE_VALUE);
            int printType = getIntent().getIntExtra("PRINT_TYPE", PrintBusinessModel.PRINTER_LABEL_TYPE_NONE);
            mPrintType = printType;
            OrderDetailInfo orderDetailInfo = getIntent().getParcelableExtra("ORDER_DETAIL_INFO");
            if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO_BY_BLUETOOTH) {
                getToolBarHelper().getToolBar().setTitle(getToolBarTitle());
            }
            setViewStatus(printType);
            mPresenter = new BaseInStockHouseLabelPrintPresenter(mContext, this, mHandler);
            mPresenter.getModel().setPrintType(printType);
            mPresenter.getModel().setCurrentPrintInfo(orderDetailInfo);
            mPresenter.getModel().setVoucherType(voucherType);
           if (mPresenter!=null){
               mPresenter.onReset();
           }
        } catch (Exception e) {
            MessageBox.Show(mContext, "获取打印数据出现预期之外的异常:" + e.getMessage());
        }

    }

    @Override
    public void onMaterialFocus() {
        CommonUtil.setEditFocus(mMaterialNo);
    }

    @Override
    public void onBatchNoFocus() {
        CommonUtil.setEditFocus(mBatchNo);
    }

    @Override
    public void onOrderRemainQtyFocus() {
        CommonUtil.setEditFocus(mRemainQty);
    }

    @Override
    public void onPackQtyFocus() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtil.setEditFocus(mPackQty);
            }
        }, 200);

    }

    @Override
    public void onPalletQtyFocus() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtil.setEditFocus(mPalletQty);
            }
        }, 200);

    }

    @Override
    public void onPrintCountFocus() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtil.setEditFocus(mPrintCount);
            }
        }, 200);

    }

    @Override
    public void onReset() {
        try {
            int printType = -1;
            if (mPresenter != null) {
                printType = mPresenter.getModel().getPrintType();
            }
            mMaterialNo.setText("");
            mMaterialDesc.setText("");
            mBatchNo.setText("");
            mRemainQty.setText("0");
            if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_OUTER_BOX) {
                if (mPresenter.getModel().getCurrentPrintInfo().getPackQty() != 0) {
                    mPackQty.setText(mPresenter.getModel().getCurrentPrintInfo().getPackQty() + "");
                } else {
                    mPackQty.setText("0");
                }

                mPrintCount.setText("0");
            } else if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO || printType == PrintBusinessModel.PRINTER_LABEL_TYPE_NO_SOURCE_PALLET_NO || printType == PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO_BY_BLUETOOTH) {
                mPalletQty.setText("0");
            }
            onMaterialFocus();
        } catch (Exception e) {
            MessageBox.Show(mContext, "出现预期之外的异常:" + e.getMessage());
        }

    }

    @Override
    public void setPrintInfoData(OrderDetailInfo printInfoData, int printType) {
        if (printInfoData != null) {
            mMaterialNo.setText(printInfoData.getMaterialno());
            mMaterialDesc.setText(printInfoData.getMaterialdesc());
            mBatchNo.setText(printInfoData.getBatchno());
            mRemainQty.setText(printInfoData.getRemainqty() + "");
            mErpVoucherNo.setText(printInfoData.getErpvoucherno());
            if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_OUTER_BOX) {
                if (printInfoData.getPackQty() > 0) {
                    mPackQty.setEnabled(false);
                } else {
                    mPackQty.setEnabled(true);
                }
                mPackQty.setText(printInfoData.getPackQty() + "");
                mPrintCount.setText("0");
            } else if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO || printType == PrintBusinessModel.PRINTER_LABEL_TYPE_NO_SOURCE_PALLET_NO || printType == PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO_BY_BLUETOOTH) {
//                mErpVoucherNo.setText(printInfoData.getErpvoucherno());
                mPalletQty.setText("0");
            }
            onBatchNoFocus();
        } else {
            mMaterialNo.setText("");
            mMaterialDesc.setText("");
            mBatchNo.setText("");
            mRemainQty.setText("0");
            mPackQty.setText("0");
            mPrintCount.setText("0");
            mErpVoucherNo.setText("");
        }


    }


    @Override
    public void setViewStatus(int printType) {
        if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_OUTER_BOX) {
//            mErpVoucherNoDesc.setVisibility(View.GONE);
//            mErpVoucherNo.setVisibility(View.GONE);
            mErpVoucherNoDesc.setVisibility(View.VISIBLE);
            mErpVoucherNo.setVisibility(View.VISIBLE);
            mPackQtyDesc.setVisibility(View.VISIBLE);
            mPackQty.setVisibility(View.VISIBLE);
            mPalletNoDesc.setVisibility(View.GONE);
            mPalletQty.setVisibility(View.GONE);
            mPrintCountDesc.setVisibility(View.VISIBLE);
            mPrintCount.setVisibility(View.VISIBLE);
            mRemainQtyDesc.setVisibility(View.GONE);
            mRemainQty.setVisibility(View.GONE);
        } else if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO || printType == PrintBusinessModel.PRINTER_LABEL_TYPE_NO_SOURCE_PALLET_NO || printType == PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO_BY_BLUETOOTH) {
//            mErpVoucherNoDesc.setVisibility(View.VISIBLE);
//            mErpVoucherNo.setVisibility(View.VISIBLE);
            mPackQtyDesc.setVisibility(View.GONE);
            mPackQty.setVisibility(View.GONE);
            mPalletNoDesc.setVisibility(View.VISIBLE);
            mPalletQty.setVisibility(View.VISIBLE);
            mPrintCountDesc.setVisibility(View.GONE);
            mPrintCount.setVisibility(View.GONE);
            mRemainQtyDesc.setVisibility(View.VISIBLE);
            mRemainQty.setVisibility(View.VISIBLE);
        }
        onBatchNoFocus();
    }

    @Override
    public String getBatchNo() {
        return mBatchNo.getText().toString().trim();
    }

    @Override
    public float getRemainQty() {
        return Float.parseFloat(mRemainQty.getText().toString().trim());
    }

    @Override
    public float getPackQty() {
        return Float.parseFloat(mPackQty.getText().toString().trim());
    }

    @Override
    public float getPalletQty() {
        return Float.parseFloat(mPalletQty.getText().toString().trim());
    }

    @Override
    public int getPrintCount() {
        return Integer.parseInt(mPrintCount.getText().toString().trim());
    }

    @Override
    public boolean checkBatchNo(String batchNo) {
        if (!DateUtil.isBeforeOrCompareToday(batchNo.trim(), "yyyyMMdd")) {
            MessageBox.Show(mContext, "校验日期格式失败:[" + batchNo + "]" + "日期格式不正确或日期大于今天", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBatchNoFocus();
                }
            });
            return false;
        }
        return true;
    }

    @Override
    public int getPrintType() {
        return mPrintType;
    }

    @Override
    public void setMaterialDesc(String materialDesc) {
        if (materialDesc==null || materialDesc.equals("")){
            mMaterialDesc.setText("");
        }else {
            mMaterialDesc.setText(materialDesc);
        }
    }

    @Override
    public String getMaterialNo() {
        return mMaterialNo.getText().toString().trim();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            startActivityLeft(new Intent(mContext, SettingMainActivity.class));
        }
        return false;
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mPresenter!=null){
            mPresenter.onStop();
        }
    }
}
