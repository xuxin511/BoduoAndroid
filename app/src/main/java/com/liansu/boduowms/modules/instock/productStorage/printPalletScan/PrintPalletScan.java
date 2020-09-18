package com.liansu.boduowms.modules.instock.productStorage.printPalletScan;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.menu.MenuType;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.batchPrint.print.BaseOrderLabelPrint;
import com.liansu.boduowms.modules.instock.productStorage.scan.ProductStorageScan;
import com.liansu.boduowms.modules.menu.commonMenu.MenuModel;
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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;

/**
 * @ Des: 产品入库托盘标签打印
 * @ Created by yangyiqing on 2020/7/15.
 */

@ContentView(R.layout.activity_product_storage_print_scan)
public class PrintPalletScan extends BaseActivity implements IPrintPalletScanView, IUserSettingView {
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
    protected UserSettingPresenter mUserSettingPresenter;
    @Override
    protected void initViews() {
        super.initViews();
        mUserSettingPresenter=new UserSettingPresenter(mContext,this);
        BaseApplication.context = mContext;
        if (getTitleString().trim().equals("")){
            setToolBarTitle(MenuModel.getSecondaryMenuModuleTitle(MenuType.MENU_TYPE_IN_STOCK, OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_PRINT_VALUE));
        }
        BaseApplication.toolBarTitle = new ToolBarTitle(getToolBarTitle(), true);
        BaseApplication.isCloseActivity = false;
        x.view().inject(this);
        closeKeyBoard(mBarcode,mErpVoucherNo);

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
            intent.putExtra("Title", MenuModel.getSecondaryMenuModuleTitle(MenuType.MENU_TYPE_IN_STOCK,OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_VALUE));
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
        if (mPresenter!=null){
            if (mPresenter.getModel().getProductInfo()!=null){
                onBarcodeFocus();
            }else {
                onErpVoucherNo();
            }
        }
//        if (mPresenter != null) {
//            mPresenter.getOrderDetailInfoList(mPresenter.getModel().getOrderHeaderInfo());
//        }
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
                OrderHeaderInfo orderHeaderInfo = new OrderHeaderInfo();
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
         mBarcode.setText("");
         mErpVoucherNo.setText("");
         onErpVoucherNo();
         bindListView(mPresenter.getModel().getOrderDetailList());
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
        intent.setClass(PrintPalletScan.this, BaseOrderLabelPrint.class);
        Bundle bundle = new Bundle();
        bundle.putInt("PRINT_TYPE", PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO);
        bundle.putParcelable("ORDER_DETAIL_INFO", orderDetailInfo);
        intent.putExtras(bundle);
        startActivityLeft(intent);
//        Intent intent = new Intent();
//        intent.setClass(mContext, MaterialInfoDialogActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("info", info);
//        intent.putExtras(bundle);
//        startActivityForResult(intent, REQUEST_CODE_OK);
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
            MessageBox.Show(mContext, "从物料界面传递数据给入库扫描界面出现异常" + e.getMessage());
        }
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
                            if (mPresenter!=null){
                                mPresenter.onReset();
                            }
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public void setTitle() {
        if (mPresenter!=null){
            getToolBarHelper().getToolBar().setTitle(getToolBarTitle());
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
            selectWareHouse(mUserSettingPresenter.getModel().getWareHouseNoList());
        }
        return false;
    }

}
