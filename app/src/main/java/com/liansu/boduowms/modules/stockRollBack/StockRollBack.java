package com.liansu.boduowms.modules.stockRollBack;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.stock.VoucherDetailSubInfo;
import com.liansu.boduowms.ui.adapter.stockRollBack.StockRollBackAdapter;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ Des:  入库，出库通用条码回退
 * @ Created by yangyiqing on 2020/8/26.
 */
@ContentView(R.layout.activity_stock_roll_back)
public class StockRollBack extends BaseActivity implements IStockRollBackView {
    @ViewInject(R.id.stock_roll_back_erp_voucher_no)
    TextView     mTxtErpVoucherNo;
    @ViewInject(R.id.stock_roll_back_pallet_no)
    EditText     mPalletNo;
    @ViewInject(R.id.stock_roll_back_recycle_view)
    RecyclerView mRecyclerView;
    Context                mContext      = StockRollBack.this;
    StockRollBackPresenter mPresenter;
    int                    mVoucherType  = -1;
    String                 mErpVoucherNo = "";
    String                 mTitle        = "回退";
    StockRollBackAdapter   mAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;

//        closeKeyBoard(mOutBarcode);
//        closeKeyBoard(mAreaNo);
    }


    /**
     * @desc: 外箱码扫描
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/7 12:45
     */
    @Event(value = R.id.stock_roll_back_pallet_no, type = View.OnKeyListener.class)
    private boolean outBarcodeScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            String barcode = mPalletNo.getText().toString().trim();
            if (mPresenter != null) {
                mPresenter.onScan(barcode);
            }
        }

        return false;
    }


    @Override
    protected void initData() {
        super.initData();
        mErpVoucherNo = getIntent().getStringExtra("ErpVoucherNo");
        mVoucherType = getIntent().getIntExtra("VoucherType", -1);
        mTitle = getIntent().getStringExtra("Title");
        mPresenter=new StockRollBackPresenter(mContext,this,mHandler,mErpVoucherNo,mVoucherType,mTitle);
        if (mPresenter!=null){
            setTitle(mPresenter.getTitle());
        }
    }


    @Override
    public void onBarcodeFocus() {
        CommonUtil.setEditFocus(mPalletNo);
    }

    @Override
    public void setErpVoucherNo(String erpVoucherNo) {
        mTxtErpVoucherNo.setText(erpVoucherNo);
    }

    @Override
    public void setTitle(String title) {
        if (mPresenter != null) {
            getToolBarHelper().getToolBar().setTitle(mPresenter.getTitle());
        }

    }

    @Override
    public void bindRecycleView(List<VoucherDetailSubInfo> list) {
        if (mAdapter == null) {
            mAdapter = new StockRollBackAdapter(mContext, list);
//            mAdapter.setRecyclerView(mRecyclerView);
            mAdapter.setOnItemClickListener(new StockRollBackAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView parent, View view, int position, VoucherDetailSubInfo data) {
                    if (data != null) {
                        mPresenter.onDeleteTemporaryDataRefer(data);
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
    protected void onResume() {
        super.onResume();

        if (mPresenter != null) {
            mPresenter.getTemporaryDetailList();
        }
    }
}
