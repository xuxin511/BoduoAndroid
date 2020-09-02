package com.liansu.boduowms.modules.instock.salesReturn.scan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.ui.adapter.instock.salesReturnStorage.SalesReturnStorageAdapter;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * @ Des: 销售退货
 * @ Created by yangyiqing on 2020/7/15.
 */

@ContentView(R.layout.activity_sales_return_scan)
public class SalesReturnStorageScan extends BaseActivity implements ISalesReturnStorageScanView, IUserSettingView {
    Context                         mContext = SalesReturnStorageScan.this;
    SalesReturnStorageScanPresenter mPresenter;
    @ViewInject(R.id.sales_return_scan_pallet_no)
    EditText mPalletNo;
    @ViewInject(R.id.sales_return_scan_area_no)
    EditText mAreaNo;
    @ViewInject(R.id.sales_return_scan_list_view)
    ListView mListView;
    @ViewInject(R.id.sales_return_scan_print_pallet_label)
    Button   mPrintButton;
    @ViewInject(R.id.txt_material_no)
    TextView mMaterialNo;
    @ViewInject(R.id.txt_batch_no)
    TextView mBatchNo;
    @ViewInject(R.id.txt_material_desc)
    TextView mMaterialDesc;
    @ViewInject(R.id.sales_return_scan_print_pallet_label)
    Button   mReferButton;
    SalesReturnStorageAdapter mAdapter;
    protected UserSettingPresenter mUserSettingPresenter;
    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(mContext.getResources().getString(R.string.appbar_title_sales_return_storage_scan) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename(), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        onAreaNoFocus();
        closeKeyBoard(mPalletNo,mAreaNo);

    }

    @Override
    public void onHandleMessage(Message msg) {
        mPresenter.onHandleMessage(msg);
    }


    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_order_bill, menu);
//        MenuItem item = menu.findItem(R.id.menu_order_setting);
//        if (item != null) {
//            item.setVisible(true);
//        }
//        return true;
//    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter = new SalesReturnStorageScanPresenter(SalesReturnStorageScan.this, this, mHandler);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mUserSettingPresenter=new UserSettingPresenter(mContext,this);
        //每次界面启动时刷新实时数据
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.menu_order_setting) {
//            Intent intent = new Intent();
//            intent.setClass(SalesReturnStorageScan.this, SalesReturnPrint.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE);
////            bundle.putParcelable("orderHeader", mPresenter.getModel().getOrderHeaderInfo());
////            bundle.putParcelableArrayList("orderDetailList", (ArrayList<? extends Parcelable>) DebugModuleData.loadReceiptScanDetailList());
//            intent.putExtras(bundle);
//            startActivityLeft(intent);
//        }
//
//        return false;
//    }

    @Event(R.id.sales_return_scan_print_pallet_label)
    private void btnRefer(View view) {
        if (mPresenter != null) {
          mPresenter.onOrderRefer();
        }

    }


    @Override
    public void onPalletNoFocus() {
        CommonUtil.setEditFocus(mPalletNo);
    }

    @Override
    public void onAreaNoFocus() {
        CommonUtil.setEditFocus(mAreaNo);
    }


    @Event(value = {R.id.sales_return_scan_area_no, R.id.sales_return_scan_pallet_no}, type = View.OnKeyListener.class)
    private boolean edtStockScanClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {


            switch (v.getId()) {
                case R.id.sales_return_scan_area_no:
                    String areaNo = mAreaNo.getText().toString().trim();
                    mPresenter.getAreaNo(areaNo);
                    break;
                case R.id.sales_return_scan_pallet_no:
                    String outBarcode = mPalletNo.getText().toString().trim();
                    mPresenter.scanBarcode(outBarcode);
                    break;
            }

        }
        return false;
    }

    @Override
    public void bindListView(List<OutBarcodeInfo> list) {
        if (list != null && list.size() > 0) {
            if (mListView.getVisibility() != View.VISIBLE) {
                mListView.setVisibility(View.VISIBLE);
            }
            mAdapter = new SalesReturnStorageAdapter(mContext, list);
            mAdapter.notifyDataSetChanged();
            mListView.setAdapter(mAdapter);
        } else {
            if (mListView.getVisibility() != View.GONE) {
                mListView.setVisibility(View.GONE);
            }
            if (mAdapter!=null){
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void setPalletNoInfo(OutBarcodeInfo info) {
        if (info != null) {
            mMaterialNo.setText(info.getMaterialno());
            mMaterialDesc.setText(info.getMaterialdesc());
            mBatchNo.setText(info.getBatchno());
        } else {
            mMaterialNo.setText("物料编码");
            mMaterialDesc.setText("物料名称");
            mBatchNo.setText("批次");
        }

    }

    @Override
    public void onReset() {
        mPalletNo.setText("");
        mAreaNo.setText("");
        onAreaNoFocus();
        setPalletNoInfo(null);
        if (mPresenter!=null){
            bindListView(mPresenter.getModel().getList());
        }
       onAreaNoFocus();
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
