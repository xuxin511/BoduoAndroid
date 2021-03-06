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
import com.liansu.boduowms.modules.instock.salesReturn.noSourceScan.NoSourceSalesReturnsStoragePresenter2;
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.modules.stockRollBack.StockRollBack;
import com.liansu.boduowms.ui.adapter.instock.baseScanStorage.BaseScanDetailAdapter;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.DateUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import androidx.annotation.NonNull;
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
    @ViewInject(R.id.product_return_scan_outer_box_qty)
    protected EditText     mOutBoxQty;
    @ViewInject(R.id.product_return_scan_outer_box_qty_desc)
    protected TextView     mOutBoxQtyDesc;
    @ViewInject(R.id.product_return_scan_outer_box_batch)
    protected  EditText mOutBoxBatchNo;
    @ViewInject(R.id.product_return_scan_outer_box_batch_desc)
    protected  TextView mOutBoxBatchNoDesc;
    @ViewInject(R.id.product_return_scan_stock_qty_desc)
    TextView mPalletStockQty;
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
        }else if (mVoucherType==OrderType.IN_STOCK_ORDER_TYPE_NO_SOURCE_SALES_RETURN_STORAGE_VALUE){
            mPresenter=new NoSourceSalesReturnsStoragePresenter2(mContext,this,mHandler);
        }
    }

    @Override
    public  boolean  ReturnActivity(){
        if (mPresenter!=null &&mPresenter.getGUIDHelper()!=null){
            if(!mPresenter.getGUIDHelper().isReturn()){
                // CommonUtil.setEditFocus(receiption_scan_out_barcode);
                MessageBox.Show(mContext, "过账异常不允许退出，请继续提交");
            }
            return mPresenter.getGUIDHelper().isReturn();
        }
        return  true;
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
        BaseApplication.isCloseActivity = false;//关闭界面 再次提示
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
    @Event(value = {R.id.product_return_scan_pallet_no, R.id.product_return_scan_outer_box,R.id.product_return_scan_outer_box_batch, R.id.product_return_scan_outer_box_qty}, type = View.OnKeyListener.class)
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
                case R.id.product_return_scan_outer_box_batch:
                    String batchNo=mOutBoxBatchNo.getText().toString().trim();
                    boolean isChecked=checkBatchNo(batchNo);
                    if (isChecked){
                        onOuterBoxQtyFocus();
                    }else {
                        MessageBox.Show(mContext, "校验日期格式失败:" + "日期格式不正确", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onOuterBoxBatchNoFocus();

                            }
                        });
                    }
                    setOuterBoxBatchNo(batchNo.trim());
                    break;
                case R.id.product_return_scan_outer_box_qty:
                    try {
                        float qty = Float.parseFloat(mOutBoxQty.getText().toString().trim());
                        if (mBusinessType == InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_ACTIVE) {
                            mPresenter.onActiveCombineOldPalletRefer(qty,getOuterBoxBatchNo());
                        }

                    } catch (Exception e) {
                        MessageBox.Show(mContext, "请输入正确的数字! message=" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onOuterBoxQtyFocus();
                            }
                        });
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
                            startRollBackActivity(orderHeaderInfo.getErpvoucherno(), orderHeaderInfo.getVouchertype(), getToolBarTitle());
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
    public void onReset(boolean isAllReset) {
        if (isAllReset) {
            mErpVoucherNo.setText("");
        } else {

        }
        mPalletBarcode.setText("");
        mOuterBoxBarcode.setText("");
        mAreaNo.setText("");
        mOutBoxBatchNo.setText("");
        mOutBoxQty.setText("0");
        initPalletChangedViewStatus(getPalletType());
        if (mBusinessType == InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_ACTIVE) {
            bindListView(mPresenter.getModel().getOrderDetailList());
        }
        setStockInfo(null);
    }

    @Override
    public void onBarcodeFocus() {
        CommonUtil.setEditFocus(mOuterBoxBarcode);

    }

    @Override
    public void onAreaNoFocus() {
        CommonUtil.setEditFocus(mAreaNo);
    }

    @Override
    public void onOuterBoxQtyFocus() {
        CommonUtil.setEditFocus(mOutBoxQty);
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
                mOutBoxQtyDesc.setVisibility(View.GONE);
                mOutBoxQty.setVisibility(View.GONE);
                mOutBoxBatchNo.setVisibility(View.GONE);
                mOutBoxBatchNoDesc.setVisibility(View.GONE);
                mPalletStockQty.setVisibility(View.GONE);
            }
            if (mAreaNo.isEnabled() != true) {
                mAreaNo.setEnabled(true);
            }

        } else if (palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_OLD_PALLET) {
            if (mOuterBoxBarcodeDesc.getVisibility() != View.VISIBLE) {
                mOuterBoxBarcodeDesc.setVisibility(View.VISIBLE);
                mOuterBoxBarcode.setVisibility(View.VISIBLE);
                mOutBoxQtyDesc.setVisibility(View.VISIBLE);
                mOutBoxQty.setVisibility(View.VISIBLE);
                mOutBoxBatchNo.setVisibility(View.VISIBLE);
                mOutBoxBatchNoDesc.setVisibility(View.VISIBLE);
                mPalletStockQty.setVisibility(View.VISIBLE);
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

    @Override
    public String getOuterBoxQty() {
        return mOutBoxQty.getText().toString().trim();
    }

    @Override
    public void onOuterBoxBatchNoFocus() {
        CommonUtil.setEditFocus(mOutBoxBatchNo);
    }

    @Override
    public void setOuterBoxBatchNo(String batchNo) {
        mOutBoxBatchNo.setText(batchNo+"");
    }

    @Override
    public String getOuterBoxBatchNo() {
        return mOutBoxBatchNo.getText().toString().trim();
    }

    @Override
    public boolean checkBatchNo(@NonNull String batchNo) {
        boolean IS_VERIFIED=false;
            if (batchNo.equals("")||!DateUtil.isValidDate(batchNo.trim(), "yyyyMMdd")  ) {
                IS_VERIFIED=false;
            } else {
                IS_VERIFIED=true;
            }

        return IS_VERIFIED;
    }

    @Override
    public void setStockInfo(OutBarcodeInfo info) {
        float stockQty=0f;
        if (info!=null){
             stockQty=info.getQty();
        }
        mPalletStockQty.setText("库存:"+stockQty);
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
