package com.liansu.boduowms.modules.instock.productStorage.scan;

import android.app.Activity;
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
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScanPresenter;
import com.liansu.boduowms.modules.instock.productStorage.printPalletScan.PrintPalletScan;
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.modules.stockRollBack.StockRollBack;
import com.liansu.boduowms.ui.adapter.instock.baseScanStorage.BaseScanDetailAdapter;
import com.liansu.boduowms.ui.dialog.MaterialInfoDialogActivity;
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

/**
 * @ Des: 产品入库
 * @ Created by yangyiqing on 2020/7/15.
 */
@ContentView(R.layout.activity_product_storage_scan)
public class ProductStorageScan extends BaseActivity implements IProductStoragerScanView, IUserSettingView {
    protected Context mContext = ProductStorageScan.this;
    @ViewInject(R.id.btn_transfer_submission)
    Button mTransferSubmission;
    @ViewInject(R.id.lsv_ReceiptScan)
    protected RecyclerView mRecyclerView;
    @ViewInject(R.id.edt_RecScanBarcode)
    protected EditText     mPalletBarcode;
    @ViewInject(R.id.txt_VoucherNo)
    protected EditText     mErpVoucherNo;
    @ViewInject(R.id.txt_Company)
    protected TextView     txtCompany;
    @ViewInject(R.id.edt_area_no)
    protected EditText     mAreaNo;
    @ViewInject(R.id.receiption_scan_out_barcode)
    protected EditText     mOutBarcode;
    @ViewInject(R.id.btn_refer)
    protected Button       mRefer;
    BaseScanDetailAdapter mAdapter;
    public final int    REQUEST_CODE_OK = 1;
    /*业务类型 */
    protected    String mBusinessType   = "";
    private      int    IS_START        = 1;

    ProductStorageScanPresenter mPresenter;
    protected UserSettingPresenter mUserSettingPresenter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        initTitle();
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
//        closeKeyBoard(mOutBarcode);
//        closeKeyBoard(mAreaNo);
//        closeKeyBoard(mOutBarcode);
//        closeKeyBoard(mAreaNo);
        initListener();
        setTransferSubmissionStatus();
        closeKeyBoard(mPalletBarcode,mErpVoucherNo,mAreaNo,mOutBarcode);
    }

    @Override
    public IProductStoragerScanView getIView() {
        return this;
    }

    @Event(R.id.btn_transfer_submission)
    private void onclick(View view) {
        if (mPresenter != null) {
            mPresenter.onTransferSubmissionRefer();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter = getPresenter();
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
    public void setTransferSubmissionStatus() {
        mTransferSubmission.setVisibility(View.GONE);
    }

    @Override
    public String getErpVoucherNo() {
        return mErpVoucherNo.getText().toString().trim();
    }

    @Override
    public void onErpVoucherNoFocus() {
        CommonUtil.setEditFocus(mErpVoucherNo);
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
                OrderHeaderInfo orderHeaderInfo = new OrderHeaderInfo();
                orderHeaderInfo.setErpvoucherno(erpVoucherNo);
                orderHeaderInfo.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_VALUE);
                mPresenter.getOrderDetailInfoList(orderHeaderInfo);
            }
        }

        return false;
    }


    @Override
    protected void initData() {
        super.initData();
//        OrderHeaderInfo headerInfo = getIntent().getParcelableExtra("OrderHeaderInfo");
//        List<OutBarcodeInfo> barCodeInfos = getIntent().getParcelableArrayListExtra("barCodeInfo");
        mBusinessType = getIntent().getStringExtra("BusinessType").toString();
        if (mPresenter == null) {
            mPresenter = new ProductStorageScanPresenter(mContext, this, mHandler, null, null);
        }
        mUserSettingPresenter = new UserSettingPresenter(mContext, this);
    }

    protected void initListener() {
        mRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    mPresenter.onOrderRefer();
                }
            }
        });
    }

    /**
     * @desc: 获取当前业务类
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/16 14:22
     */
    public <T extends BaseOrderScanPresenter> T getPresenter() {
        return (T) mPresenter;
    }


    @Override
    protected void onResume() {
        super.onResume();
        BaseApplication.isCloseActivity = false;//关闭界面 再次提示
        String erpVoucherNo = mErpVoucherNo.getText().toString().trim();
        if (mPresenter != null && !erpVoucherNo.equals("")) {
            OrderHeaderInfo orderHeaderInfo = new OrderHeaderInfo();
            orderHeaderInfo.setErpvoucherno(erpVoucherNo);
            orderHeaderInfo.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_VALUE);
            mPresenter.getOrderDetailInfoList(orderHeaderInfo);
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
    @Event(value = R.id.receiption_scan_out_barcode, type = View.OnKeyListener.class)
    private boolean outBarcodeScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String barcode = mOutBarcode.getText().toString().trim();
            if (mPresenter != null) {
                mPresenter.scanBarcode(barcode);
            }
        }

        return false;
    }


    @Event(value = {R.id.btn_refer}, type = View.OnKeyListener.class)
    private boolean onRefer(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            switch (v.getId()) {
                case R.id.btn_refer:
                    mPresenter.onOrderRefer();
                    break;
            }
        }
        return false;
    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_order_bill, menu);
//        return true;
//    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.menu_order_filter) {
//            if (DoubleClickCheck.isFastDoubleClick(mContext)) {
//                return false;
//            }
//            Intent intent = new Intent();
//            intent.setClass(ProductStorageScan.this, InstockCombinePallet.class);
//            Bundle bundle = new Bundle();
//            bundle.putInt("inStockType", COMBINE_PALLET_TYPE_RECEIPTION);
//            bundle.putParcelable("orderHeader", mPresenter.getModel().getOrderHeaderInfo());
//            bundle.putParcelableArrayList("orderDetailList", (ArrayList<? extends Parcelable>) DebugModuleData.loadReceiptScanDetailList());
////        bundle.putParcelableArrayList("orderDetailList", mPresenter.getModel().getReceiptDetailModels());
//            intent.putExtras(bundle);
//            startActivityLeft(intent);
//        } else if (item.getItemId() == R.id.menu_order_reprint) {
//            Intent intent = new Intent();
//            intent.setClass(ProductStorageScan.this, LabelReprintScan.class);
//            Bundle bundle = new Bundle();
//            bundle.putInt("inStockType", COMBINE_PALLET_TYPE_RECEIPTION);
//            bundle.putParcelable("orderHeader", mPresenter.getModel().getOrderHeaderInfo());
//            bundle.putParcelableArrayList("orderDetailList", (ArrayList<? extends Parcelable>) DebugModuleData.loadReceiptScanDetailList());
////        bundle.putParcelableArrayList("orderDetailList", mPresenter.getModel().getReceiptDetailModels());
//            intent.putExtras(bundle);
//            startActivityLeft(intent);
//        }
//
//        return false;
//    }


    @Override
    public void bindListView(List<OrderDetailInfo> receiptDetailModels) {
        if (mAdapter == null) {
            mAdapter = new BaseScanDetailAdapter(mContext, "", receiptDetailModels);
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
                    if (data!=null){
                        OrderHeaderInfo  orderHeaderInfo=mPresenter.getModel().getOrderHeaderInfo();
                        if (orderHeaderInfo!=null){
                            startRollBackActivity(orderHeaderInfo.getErpvoucherno(),orderHeaderInfo.getVouchertype(),getToolBarTitle());
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
    public void onReset() {
         mErpVoucherNo.setText("");
        mAreaNo.setText("");
        mOutBarcode.setText("");
        onErpVoucherNoFocus();
        if (mPresenter != null) {
            bindListView(mPresenter.getModel().getOrderDetailList());
        }
    }

    @Override
    public void onBarcodeFocus() {
        CommonUtil.setEditFocus(mOutBarcode);

    }

    @Override
    public void onAreaNoFocus() {
        CommonUtil.setEditFocus(mAreaNo);
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
    public void setOrderHeaderInfo(OrderHeaderInfo info) {
        if (info != null) {
            mErpVoucherNo.setText(info.getErpvoucherno());
        ;
        }

    }

    @Override
    public void setSecondLineInfo(String desc, String name, boolean isVisibility) {

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
        Intent intent = new Intent(mContext, StockRollBack.class);
        Bundle bundle = new Bundle();
        intent.putExtra("ErpVoucherNo", erpVoucherNo);
        intent.putExtra("VoucherType", voucherType);
        intent.putExtra("Title", title);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }

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
        if (mPresenter!=null){
            getToolBarHelper().getToolBar().setTitle(getToolBarTitle());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        MenuItem menuItem=menu.findItem(R.id.menu_order_reprint);
        if (menuItem!=null){
            menuItem.setVisible(true);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_setting_warehouse_select) {
            selectWareHouse(mUserSettingPresenter.getModel().getWareHouseNoList());
        }else if (item.getItemId() == R.id.menu_order_reprint){
            Intent intent = new Intent();
            intent.setClass(ProductStorageScan.this, PrintPalletScan.class);
            intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE);
            startActivityLeft(intent);
        }
        return false;
    }


}
