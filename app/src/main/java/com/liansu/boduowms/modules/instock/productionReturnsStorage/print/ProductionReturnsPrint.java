package com.liansu.boduowms.modules.instock.productionReturnsStorage.print;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.productionReturnsStorage.scan.ProductionReturnStorageScan;
import com.liansu.boduowms.ui.adapter.instock.productReturn.ProductReturnAdapter;
import com.liansu.boduowms.ui.dialog.MaterialInfoDialogActivity;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.DoubleClickCheck;

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
 * @ Des: 成品退货打印  打印成品标签
 * @ Created by yangyiqing on 2020/7/17.
 */
@ContentView(R.layout.activity_product_return_storage_scan)
public class ProductionReturnsPrint extends BaseActivity implements IProductionReturnsStorageView {
    private static final int REQUEST_CODE_OK = 1;
    Context mContext = ProductionReturnsPrint.this;
    @ViewInject(R.id.txt_VoucherNo)
    EditText     mErpVoucherNo;
    @ViewInject(R.id.list_recycle_view)
    RecyclerView mRecyclerView;
    ProductionReturnsPresenter mPresenter;
    ProductReturnAdapter       mAdapter;


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(mContext.getResources().getString(R.string.appbar_title_production_returns_print) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename(), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        onReset();

    }


    @Override
    protected void initData() {
        super.initData();
        mPresenter = new ProductionReturnsPresenter(mContext, this, mHandler);


    }

    @Override
    public void onHandleMessage(Message msg) {
        mPresenter.onHandleMessage(msg);
    }


    @Event(value = {R.id.txt_VoucherNo}, type = View.OnKeyListener.class)
    private boolean edtStockScanClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            switch (v.getId()) {
                case R.id.txt_VoucherNo:
                    String erpVoucherNo=mErpVoucherNo.getText().toString().trim();
                   mPresenter.getOrderDetailInfoList(erpVoucherNo);
                    break;

            }

        }
        return false;
    }


    @Override
    public void onErpVoucherNoFocus() {
        CommonUtil.setEditFocus(mErpVoucherNo);
    }


    @Override
    public void bindListView(List<OrderDetailInfo> orderDetailInfos) {
        if (mAdapter == null) {
            mAdapter = new ProductReturnAdapter(mContext, orderDetailInfos);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onReset() {


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mednu_sale_return, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sale_return_icon) {
            if (DoubleClickCheck.isFastDoubleClick(mContext)) {
                return false;
            }

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable("OrderHeaderInfo", null);
            bundle.putParcelableArrayList("barCodeInfo",null );
            intent.putExtra("BusinessType", OrderType.IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE);
            intent.putExtras(bundle);
            intent.setClass(ProductionReturnsPrint.this, ProductionReturnStorageScan.class);
            startActivityLeft(intent);


        }
        return false;
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
                            mPresenter.onPrint(info);
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
}
