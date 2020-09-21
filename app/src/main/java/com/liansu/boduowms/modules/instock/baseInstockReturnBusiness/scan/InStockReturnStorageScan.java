package com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.batchPrint.order.BaseOrderLabelPrintSelect;
import com.liansu.boduowms.modules.instock.productionReturnsStorage.returnScan.ProductionReturnsStoragePresenter2;
import com.liansu.boduowms.modules.instock.salesReturn.activeScan.SalesReturnsStoragePresenter2;
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.modules.stockRollBack.StockRollBack;
import com.liansu.boduowms.ui.adapter.instock.baseScanStorage.BaseScanDetailAdapter;
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

/**
 * @ Des: 工单退货，有源销退，无源销退
 * @ Created by yangyiqing on 2020/7/15.
 */
@ContentView(R.layout.activity_product_return_scan_storage_scan)
public class InStockReturnStorageScan extends BaseActivity implements IInStockReturnStorageScanView, RadioGroup.OnCheckedChangeListener, IUserSettingView {
    protected Context      mContext = InStockReturnStorageScan.this;
    @ViewInject(R.id.lsv_ReceiptScan)
    protected RecyclerView mRecyclerView;
    @ViewInject(R.id.product_return_scan_pallet_no)
    protected EditText     mPalletBarcode;
    @ViewInject(R.id.txt_VoucherNo)
    protected EditText     mErpVoucherNo;
    @ViewInject(R.id.product_return_erp_voucher_desc)
    protected TextView     mErpVoucherDesc;
    @ViewInject(R.id.edt_area_no)
    protected EditText     mAreaNo;
    @ViewInject(R.id.product_return_scan_outer_box)
    protected EditText     mOuterBoxBarcode;
    @ViewInject(R.id.product_return_scan_outer_box_desc)
    protected TextView     mOuterBoxBarcodeDesc;
    @ViewInject(R.id.btn_refer)
    protected Button       mRefer;
    @ViewInject(R.id.product_return_radio_group)
    protected RadioGroup   mRadioGroup;
    BaseScanDetailAdapter              mAdapter;
    InStockReturnsStorageScanPresenter mPresenter;
    protected UserSettingPresenter mUserSettingPresenter;
    protected int                  mVoucherType  = OrderType.ORDER_TYPE_NONE_VALUE;
    protected int                  mBusinessType = InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_NONE;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        initTitle();
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        initListener();
        closeKeyBoard(mPalletBarcode, mErpVoucherNo, mAreaNo, mOuterBoxBarcode);

    }

    private void initPresenter() {
        if (mVoucherType == OrderType.IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE_VALUE) {
            mPresenter = new ProductionReturnsStoragePresenter2(mContext, this, mHandler);
        } else if (mVoucherType == OrderType.IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE_VALUE) {
            mPresenter = new SalesReturnsStoragePresenter2(mContext, this, mHandler);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mUserSettingPresenter = new UserSettingPresenter(mContext, this);
    }


    @Override
    public void onErpVoucherNoFocus() {
        CommonUtil.setEditFocus(mErpVoucherNo);
    }

    @Override
    public void onPalletNoFocus() {
        CommonUtil.setEditFocus(mPalletBarcode);
    }


    protected void initTitle() {
        BaseApplication.toolBarTitle = new ToolBarTitle(getToolBarTitle(), true);
    }

    @Override
    public void onHandleMessage(Message msg) {
        if (mPresenter != null) {
            mPresenter.onHandleMessage(msg);
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


    @Override
    protected void initData() {
        super.initData();
        mBusinessType = getIntent().getIntExtra("BusinessType", InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_NONE);
        mVoucherType = getIntent().getIntExtra("VoucherType", OrderType.ORDER_TYPE_NONE_VALUE);
        initPresenter();
        initViewStatus(mBusinessType);
    }

    protected void initListener() {
        mRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    if (mBusinessType == InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_ACTIVE) {
                        mPresenter.onActiveOrderRefer();
                    } else if (mBusinessType == InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_NO_SOURCE) {
                        mPresenter.onNoSourceOrderRefer();
                    }

                }
            }
        });
        mRadioGroup.setOnCheckedChangeListener(this);

    }


    public InStockReturnsStorageScanPresenter getPresenter() {
        return mPresenter;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Event(value = R.id.edt_area_no, type = View.OnKeyListener.class)
    private boolean edtStockScanClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            String areaNo = mAreaNo.getText().toString().trim();
            mPalletBarcode.setText("");
            if (TextUtils.isEmpty(areaNo)) {
                CommonUtil.setEditFocus(mAreaNo);
                return true;
            }
            if (mPresenter != null) {
                mPresenter.getAreaInfo(areaNo);
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
    @Event(value = {R.id.product_return_scan_pallet_no, R.id.product_return_scan_outer_box}, type = View.OnKeyListener.class)
    private boolean outBarcodeScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            switch (v.getId()) {
                case R.id.product_return_scan_pallet_no:
                    String palletNo = mPalletBarcode.getText().toString().trim();
                    if (mPresenter != null) {
                        mPresenter.scanPalletBarcode(palletNo);
                    }
                    break;
                case R.id.product_return_scan_outer_box:
                    String barcode = mOuterBoxBarcode.getText().toString().trim();
                    if (mPresenter != null) {
                        mPresenter.scanOuterBoxBarcode(barcode);
                    }
                    break;
            }


        }

        return false;
    }


    @Override
    public void bindListView(List<OrderDetailInfo> list) {
        if (mAdapter == null) {
            mAdapter = new BaseScanDetailAdapter(mContext, "", list);
//            mAdapter.setRecyclerView(mRecyclerView);
            mAdapter.setOnItemClickListener(new BaseScanDetailAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView parent, View view, int position, OrderDetailInfo data) {
                    if (data != null) {
                    }

                }
            });
            mAdapter.setOnItemLongClickListener(new BaseScanDetailAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(RecyclerView parent, View view, int position, OrderDetailInfo data) {
                    if (data != null) {
                        OrderHeaderInfo orderHeaderInfo = mPresenter.getModel().getOrderHeaderInfo();
                        if (orderHeaderInfo != null) {
                            startRollBackActivity(orderHeaderInfo.getErpvoucherno(), orderHeaderInfo.getVouchertype(), getTitleString());
                        }


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

    @Override
    public void bindNoSourceListView(List<OutBarcodeInfo> list) {

    }

    @Override
    public void onReset() {
        mErpVoucherNo.setText("");
        mPalletBarcode.setText("");
        mOuterBoxBarcode.setText("");
        mAreaNo.setText("");
        initPalletChangedViewStatus(getPalletType());
        if (mBusinessType == InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_NO_SOURCE) {
            bindListView(mPresenter.getModel().getOrderDetailList());
        }
    }

    @Override
    public void onBarcodeFocus() {
        CommonUtil.setEditFocus(mPalletBarcode);

    }

    @Override
    public void onAreaNoFocus() {
        CommonUtil.setEditFocus(mAreaNo);
    }

    @Override
    public int getPalletType() {
        String value = "";
        int type = InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_NONE;
        int count = mRadioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton rb = (RadioButton) mRadioGroup.getChildAt(i);
            if (rb.isChecked()) {
                value = rb.getText().toString().trim();
                break;
            }
        }
        if (value.equals(InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_OLD_PALLET_NAME)) {
            type = InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_OLD_PALLET;
        } else if (value.equals(InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_NEW_PALLET_NAME)) {
            type = InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_NEW_PALLET;
        }

        return type;
    }

    @Override
    public void initViewStatus(int businessType) {
        if (mBusinessType == InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_NO_SOURCE) {
            if (mErpVoucherDesc.getVisibility() != View.GONE) {
                mErpVoucherDesc.setVisibility(View.GONE);
                mErpVoucherNo.setVisibility(View.GONE);
            }

        } else if (mBusinessType == InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_ACTIVE) {
            if (mErpVoucherDesc.getVisibility() != View.VISIBLE) {
                mErpVoucherDesc.setVisibility(View.VISIBLE);
                mErpVoucherNo.setVisibility(View.VISIBLE);
            }
            onErpVoucherNoFocus();
        }
        initPalletChangedViewStatus(getPalletType());
    }

    @Override
    public void initPalletChangedViewStatus(int palletType) {
        if (palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_NEW_PALLET) {
            if (mOuterBoxBarcodeDesc.getVisibility() != View.GONE) {
                mOuterBoxBarcodeDesc.setVisibility(View.GONE);
                mOuterBoxBarcode.setVisibility(View.GONE);
            }
            if (mAreaNo.isEnabled() != true) {
                mAreaNo.setEnabled(true);
            }

        } else if (palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_OLD_PALLET) {
            if (mOuterBoxBarcodeDesc.getVisibility() != View.VISIBLE) {
                mOuterBoxBarcodeDesc.setVisibility(View.VISIBLE);
                mOuterBoxBarcode.setVisibility(View.VISIBLE);
            }
            if (mAreaNo.isEnabled() != false) {
                mAreaNo.setEnabled(false);
            }
        }

        if (mBusinessType == InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_NO_SOURCE) {
            if (palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_NEW_PALLET) {
                onAreaNoFocus();
            } else if (palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_OLD_PALLET) {
                onPalletNoFocus();
            }
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onActivityFinish(String title) {
        new AlertDialog.Builder(BaseApplication.context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage(title + " 是否返回上一页面？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 自动生成的方法
                        closeActivity();
                    }
                }).setNegativeButton("取消", null).show();
    }

    @Override
    public void startRollBackActivity(String erpVoucherNo, int voucherType, String title) {
        if (mVoucherType != OrderType.ORDER_TYPE_NONE_VALUE) {
            Intent intent = new Intent(mContext, StockRollBack.class);
            Bundle bundle = new Bundle();
            intent.putExtra("ErpVoucherNo", erpVoucherNo);
            intent.putExtra("VoucherType", voucherType);
            intent.putExtra("Title", title);
            intent.putExtras(bundle);
            startActivityLeft(intent);
        }

    }

    @Override
    public String getErpVoucherNo() {
        return mErpVoucherNo.getText().toString().trim();
    }

    @Override
    public void setAreaNo(String areaNo) {
        mAreaNo.setText(areaNo + "");
    }

    @Override
    public String getAreaNo() {
        return mAreaNo.getText().toString().trim();
    }

    @Override
    public void setOrderHeaderInfo(OrderHeaderInfo orderHeaderInfo) {
        if (orderHeaderInfo != null) {
            mErpVoucherNo.setText(orderHeaderInfo.getErpvoucherno());
        }
    }


    ;

    @Override
    public void selectWareHouse(List<String> list) {
        if (list != null && list.size() > 0) {
            final String[] items = list.toArray(new String[0]);
            new AlertDialog.Builder(mContext).setTitle(getResources().getString(R.string.activity_login_WareHousChoice))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setCancelable(true)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法存根
                            String select_item = items[which].toString();
                            if (mUserSettingPresenter != null) {
                                mUserSettingPresenter.saveCurrentWareHouse(select_item);
                            }

                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public void setTitle() {
        getToolBarHelper().getToolBar().setTitle(getToolBarTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_order_reprint);
        if (menuItem != null) {
            menuItem.setVisible(true);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_setting_warehouse_select) {
            selectWareHouse(mUserSettingPresenter.getModel().getWareHouseNoList());
        } else if (item.getItemId() == R.id.menu_order_reprint) {
            Intent intent = new Intent();
            intent.setClass(InStockReturnStorageScan.this, BaseOrderLabelPrintSelect.class);
            intent.putExtra("VOUCHER_TYPE", mPresenter.getModel().getVoucherType());
            startActivityLeft(intent);
        }
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (mPresenter != null) {
            mPresenter.changePalletType();
        }

    }
}
