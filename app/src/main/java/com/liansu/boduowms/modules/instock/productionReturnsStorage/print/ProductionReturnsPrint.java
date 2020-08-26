package com.liansu.boduowms.modules.instock.productionReturnsStorage.print;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.liansu.boduowms.modules.instock.batchPrint.print.BaseOrderLabelPrint;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.ui.adapter.instock.baseScanStorage.BaseOrderLabelPrintDetailAdapter;
import com.liansu.boduowms.ui.adapter.instock.productStorage.ProductScanDetailAdapter;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;

/**
 * @ Des: 生产退 外箱标签打印
 * @ Created by yangyiqing on 2020/7/15.
 */

@ContentView(R.layout.activity_product_storage_print_scan)
public class ProductionReturnsPrint extends BaseActivity implements IProductionReturnsStorageView, IUserSettingView {
    @ViewInject(R.id.product_storage_print_out_barcode)
    EditText     mBarcode;
    @ViewInject(R.id.product_storage_print_voucher_no_desc)
    TextView     mErpVoucherNoDesc;
    @ViewInject(R.id.txt_VoucherNo)
    EditText     mErpVoucherNo;
    @ViewInject(R.id.lsv_ReceiptScan)
    RecyclerView mRecyclerView;
    ProductionReturnsPresenter mPresenter;
    Context                    mContext = ProductionReturnsPrint.this;
    /*业务类型 */
    protected    String mBusinessType   = "";
    public final int    REQUEST_CODE_OK = 1;
    ProductScanDetailAdapter mAdapter;
    protected UserSettingPresenter mUserSettingPresenter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(mContext.getResources().getString(R.string.appbar_title_production_returns_print) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename(), true);
        BaseApplication.isCloseActivity = false;
        x.view().inject(this);
        mUserSettingPresenter = new UserSettingPresenter(mContext, this);
//        closeKeyBoard(mBarcode);
//        mErpVoucherNoDesc.setVisibility(View.GONE);
//        mErpVoucherNo.setVisibility(View.GONE);
    }

    @Override
    public void onHandleMessage(Message msg) {
        if (mPresenter != null) {
            mPresenter.onHandleMessage(msg);
        }
    }

    @Override
    protected void initData() {
        mBusinessType = getIntent().getStringExtra("BusinessType").toString();
        if (mPresenter == null) {
            mPresenter = new ProductionReturnsPresenter(mContext, this, mHandler);

        }

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            if (mPresenter.getModel().getOrderHeaderInfo() != null) {
                onBarcodeFocus();
            } else {
                onErpVoucherNo();
            }
        }

    }

    @Override
    public void bindListView(List<OrderDetailInfo> detailInfos) {
        if (mAdapter == null) {
            mAdapter = new ProductScanDetailAdapter(mContext, "", detailInfos);
            mAdapter.setRecyclerView(mRecyclerView);
            mAdapter.setOnItemClickListener(new BaseOrderLabelPrintDetailAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView parent, View view, int position, OrderDetailInfo data) {
                    if (data != null) {
                        OutBarcodeInfo scanQRCode = null;
                        BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(mBarcode.getText().toString());
                        if (resultInfo.getHeaderStatus()) {
                            scanQRCode = resultInfo.getInfo();
                        } else {
                            MessageBox.Show(mContext, resultInfo.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onBarcodeFocus();
                                }
                            });
                            return;
                        }
                        if (scanQRCode != null && scanQRCode.getBatchno() != null && scanQRCode.getMaterialno() != null) {

                            data.setBatchno(scanQRCode.getBatchno());
                        }
                        createDialog(data);
                    }

                }
            });
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            mAdapter.notifyDataSetChanged();
        }
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
                mPresenter.getOrderDetailInfoList(erpVoucherNo);
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
                mPresenter.onScan(barcode);
            }
        }

        return false;
    }

    @Override
    public void onReset() {
        mErpVoucherNo.setText("");
        mBarcode.setText("");
        onErpVoucherNo();
        if (mPresenter != null) {
            bindListView(mPresenter.getModel().getOrderDetailsList());
        }

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
    public void createDialog(OrderDetailInfo orderDetailInfo) {
        Intent intent = new Intent();
        intent.setClass(ProductionReturnsPrint.this, BaseOrderLabelPrint.class);
        Bundle bundle = new Bundle();
        bundle.putInt("PRINT_TYPE", PrintBusinessModel.PRINTER_LABEL_TYPE_OUTER_BOX);
        bundle.putParcelable("ORDER_DETAIL_INFO", orderDetailInfo);
        intent.putExtras(bundle);
        startActivityLeft(intent);

    }

    @Override
    public void selectWareHouse(List<String> list) {
        if (list != null && list.size() > 0) {
            final String[] items = list.toArray(new String[0]);
            new AlertDialog.Builder(mContext).setTitle(getResources().getString(R.string.activity_login_WareHousChoice))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setCancelable(false)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法存根
                            String select_item = items[which].toString();
                            if (mUserSettingPresenter != null) {
                                mUserSettingPresenter.saveCurrentWareHouse(select_item);
                            }
                            if (mPresenter != null) {
                                mPresenter.onReset();
                            }
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public void setTitle() {
        if (mPresenter != null) {
            getToolBarHelper().getToolBar().setTitle(mPresenter.getTitle());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_setting_warehouse_select) {
            selectWareHouse(mUserSettingPresenter.getModel().getWareHouseNameList());
        }
        return false;
    }
}
