package com.liansu.boduowms.modules.outstock.offScan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.BaseOutStockBusiness;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.DoubleClickCheck;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import androidx.annotation.Nullable;

/**
 * @ Des: 配货下架
 * @ Created by yangyiqing on 2020/6/28.
 */
@ContentView(R.layout.activity_offshelf_scan)
public class DistributionOffShelf extends BaseOutStockBusiness<DistributionOffShelfPresenter> implements IDistributionOffShelfView {
    Context mContext = this;
    final int REQUEST_CODE_OK = 1;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.appbar_title_offscan), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;

    }

    @Override
    protected void initData() {
        super.initData();
        mBusinessType = getIntent().getStringExtra("BusinessType").toString();
        initViewStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_offscan, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_off_scan_boxing) {
            if (DoubleClickCheck.isFastDoubleClick(mContext)) {
                return false;
            }
//            Intent intent = new Intent();
//            intent.setClass(DistributionOffShelf.this, InstockCombinePallet.class);
//            Bundle bundle = new Bundle();
//            bundle.putInt("inStockType", COMBINE_PALLET_TYPE_RECEIPTION);
//            bundle.putParcelable("orderHeader", mPresenter.getModel().getOrderHeaderInfo());
//            bundle.putParcelableArrayList("orderDetailList", (ArrayList<? extends Parcelable>) DebugModuleData.loadReceiptScanDetailList());
//            intent.putExtras(bundle);
//            startActivityLeft(intent);
        } else if (item.getItemId() == R.id.menu_off_scan_balcony) {
            Intent intent = new Intent();
            intent.setClass(DistributionOffShelf.this, BalconyInfoDialogActivity.class);
            Bundle bundle = new Bundle();
//            bundle.putString("ORDER_NO",mPresenter.getModel().getOrderDetailList().get(0).getArrVoucherNo());
            bundle.putString("ORDER_NO", "20200726");
            intent.putExtras(bundle);
            startActivityForResult(intent,REQUEST_CODE_OK);
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
                        String balconyDesc = data.getStringExtra("BALCONY_DESC");
                        String orderNo = data.getStringExtra("ORDER_NO");
                        if (balconyDesc != null && orderNo != null) {
                            mPresenter.setBalconyInfo(orderNo, balconyDesc);
                        }

                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "从物料界面传递数据给入库扫描界面出现异常" + e.getMessage(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


    }


    @Override
    protected void initPresenter() {
         mPresenter = new DistributionOffShelfPresenter(mContext, this, mHandler);

    }


}
