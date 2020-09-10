package com.liansu.boduowms.modules.instock.baseOrderBusiness.scan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.BasePresenterFactory;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.debug.DebugModuleData;
import com.liansu.boduowms.modules.instock.batchPrint.order.BaseOrderLabelPrintSelect;
import com.liansu.boduowms.modules.instock.combinePallet.InstockCombinePallet;
import com.liansu.boduowms.modules.setting.SettingMainActivity;
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.modules.stockRollBack.StockRollBack;
import com.liansu.boduowms.ui.adapter.instock.baseScanStorage.BaseScanDetailAdapter;
import com.liansu.boduowms.ui.dialog.MaterialInfoDialogActivity;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.DoubleClickCheck;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.liansu.boduowms.modules.instock.combinePallet.InstockCombinePalletModel.COMBINE_PALLET_TYPE_RECEIPTION;


@ContentView(R.layout.activity_receiption_scan)
public class BaseOrderScan extends BaseActivity implements IBaseOrderScanView, IUserSettingView {
    protected Context      mContext = BaseOrderScan.this;
    @ViewInject(R.id.lsv_ReceiptScan)
    protected RecyclerView mRecyclerView;
    @ViewInject(R.id.edt_RecScanBarcode)
    protected EditText     mPalletBarcode;
    @ViewInject(R.id.textView10)
    protected TextView     mPalletBarcodeDesc;
    @ViewInject(R.id.txt_VoucherNo)
    protected EditText     mOrderNo;
    @ViewInject(R.id.txt_Company)
    protected TextView     txtCompany;
    @ViewInject(R.id.txt_Batch)
    protected TextView     txtBatch;
    @ViewInject(R.id.txt_Status)
    protected TextView     txtStatus;
    @ViewInject(R.id.txt_EDate)
    protected TextView     txtEDate;
    @ViewInject(R.id.txt_MaterialName)
    protected TextView     txtMaterialName;
    @ViewInject(R.id.edt_area_no)
    protected EditText     mAreaNo;
    @ViewInject(R.id.textView33)
    protected TextView     mAreaName;
    @ViewInject(R.id.receiption_scan_supplier_name)
    protected TextView     mSupplierName;
    @ViewInject(R.id.receiption_scan_out_barcode_desc)
    protected TextView     mOutBarcodeDesc;
    @ViewInject(R.id.receiption_scan_out_barcode)
    protected EditText     mOutBarcode;
    @ViewInject(R.id.receiption_scan_module_type)
    protected Switch       mOperationType;
    @ViewInject(R.id.textView5)
    protected TextView     mOrderName;
    @ViewInject(R.id.btn_refer)
    protected Button       mRefer;
    @ViewInject(R.id.txt_receiption_scan_supplier_name)
    TextView mSupplierNameDesc;
    BaseScanDetailAdapter mAdapter;
    private      BaseOrderScanPresenter mPresenter;
    public final int                    REQUEST_CODE_OK = 1;
    /*业务类型 */
    protected    String                 mBusinessType   = "";
    protected    UserSettingPresenter   mUserSettingPresenter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        closeKeyBoard(mOrderNo,mOutBarcode,mAreaNo,mPalletBarcode);
        initListener();
        onAreaNoFocus();
        mUserSettingPresenter = new UserSettingPresenter(mContext, this);
//        setToolbarTitleViewTextSize((AppCompatActivity) mContext,toolbar);
    }

    @Override
    public void onHandleMessage(Message msg) {
        if (mPresenter != null) {
            mPresenter.onHandleMessage(msg);
        }
    }


    @Override
    protected void initData() {
        super.initData();

        OrderHeaderInfo headerInfo = getIntent().getParcelableExtra("OrderHeaderInfo");
        List<OutBarcodeInfo> barCodeInfos = getIntent().getParcelableArrayListExtra("barCodeInfo");
        mBusinessType = getIntent().getStringExtra("BusinessType").toString();
        if (mPresenter == null) {
            mPresenter = BasePresenterFactory.getBaseOrderScanPresenter(mContext, getIView(), mHandler, headerInfo, barCodeInfos, mBusinessType);
        }


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
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }


        getToolBarHelper().getToolBar().setTitle(mPresenter.getTitle());
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

    @Event(value = R.id.txt_VoucherNo, type = View.OnKeyListener.class)
    private boolean scanVoucherNo(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String erpVoucherNo = mOrderNo.getText().toString().trim();
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


    @Event(value = R.id.lsv_ReceiptScan, type = AdapterView.OnItemClickListener.class)
    private boolean lsv_ReceiptScanItemClick(AdapterView<?> parent, View view, int position,
                                             long id) {
//        if (id >= 0) {
//            ReceiptDetail_Model receiptDetailModel = receiptDetailModels.get(position);
//            try {
//                if (receiptDetailModel.getLstBarCode() != null && receiptDetailModel.getLstBarCode().size() != 0) {
////                        Intent intent = new Intent(context, ReceiptionBillDetail.class);
////                        Bundle bundle = new Bundle();
////                        bundle.putParcelable("receiptDetailModel", receiptDetailModel);
////                        intent.putExtras(bundle);
////                        startActivityLeft(intent);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_bill, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_order_filter) {
            if (DoubleClickCheck.isFastDoubleClick(mContext)) {
                return false;
            }
            Intent intent = new Intent();
            intent.setClass(BaseOrderScan.this, InstockCombinePallet.class);
            Bundle bundle = new Bundle();
            bundle.putInt("inStockType", COMBINE_PALLET_TYPE_RECEIPTION);
            bundle.putParcelable("orderHeader", mPresenter.getModel().getOrderHeaderInfo());
            bundle.putParcelableArrayList("orderDetailList", (ArrayList<? extends Parcelable>) DebugModuleData.loadReceiptScanDetailList());
//        bundle.putParcelableArrayList("orderDetailList", mPresenter.getModel().getReceiptDetailModels());
            intent.putExtras(bundle);
            startActivityLeft(intent);
        } else if (item.getItemId() == R.id.menu_order_reprint) {
            Intent intent = new Intent();
            intent.setClass(BaseOrderScan.this, BaseOrderLabelPrintSelect.class);
            if (mPresenter!=null){
                intent.putExtra("VOUCHER_TYPE",mPresenter.getModel().getVoucherType());
            }

//            Bundle bundle = new Bundle();
//            bundle.putInt("inStockType", COMBINE_PALLET_TYPE_RECEIPTION);
//            bundle.putParcelable("orderHeader", mPresenter.getModel().getOrderHeaderInfo());
//            bundle.putParcelableArrayList("orderDetailList", (ArrayList<? extends Parcelable>) DebugModuleData.loadReceiptScanDetailList());
////        bundle.putParcelableArrayList("orderDetailList", mPresenter.getModel().getReceiptDetailModels());
//            intent.putExtras(bundle);
            startActivityLeft(intent);
        } else if (item.getItemId() == R.id.menu_setting) {
            startActivityLeft(new Intent(mContext, SettingMainActivity.class));
        } else if (item.getItemId() == R.id.user_setting_warehouse_select) {
            selectWareHouse(mUserSettingPresenter.getModel().getWareHouseNameList());
        }
        return false;
    }


    @Override
    public void bindListView(List<OrderDetailInfo> receiptDetailModels) {
        if (mAdapter == null) {
            mAdapter = new BaseScanDetailAdapter(mContext, "采购收货", receiptDetailModels);
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
                             startRollBackActivity(orderHeaderInfo.getErpvoucherno(),orderHeaderInfo.getVouchertype(),mPresenter.getTitle());
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
        mOrderNo.setText("");
        mSupplierName.setText("");
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
    public void onErpVoucherNoFocus() {
        CommonUtil.setEditFocus(mOrderNo);
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
            mOrderNo.setText(info.getErpvoucherno());
            mSupplierName.setText(info.getSuppliername());
        }

    }

    @Override
    public void setSecondLineInfo(String desc, String name, boolean isVisibility) {
        if (isVisibility) {
            mSupplierName.setVisibility(View.VISIBLE);
            mSupplierNameDesc.setVisibility(View.VISIBLE);
            if (desc != null && name != null) {
                mSupplierNameDesc.setText(desc);
                mSupplierName.setText(name);
            }
        } else {
            mSupplierName.setVisibility(View.GONE);
            mSupplierNameDesc.setVisibility(View.GONE);
        }
    }

    @Override
    public String getErpVoucherNo() {
        return mOrderNo.getText().toString().trim();
    }

    @Override
    public void onActivityFinish(String message) {
        new AlertDialog.Builder(BaseApplication.context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage(message + " 是否返回上一页面？")
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
    public IBaseOrderScanView getIView() {
        return this;
    }

    ;

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
        if (mPresenter != null) {
            getToolBarHelper().getToolBar().setTitle(mPresenter.getTitle());
        }

    }




}
