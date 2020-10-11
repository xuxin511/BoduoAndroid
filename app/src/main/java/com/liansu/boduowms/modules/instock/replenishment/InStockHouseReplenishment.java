package com.liansu.boduowms.modules.instock.replenishment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.ui.adapter.inHouseStock.InStockHouseReplenishmentItemAdapter;
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

/**
 * @ Des:  补货界面
 * @ Created by yangyiqing on 2020/10/10.
 */
@ContentView(R.layout.activity_in_stock_house_replenishment)
public class InStockHouseReplenishment extends BaseActivity implements IInStockHouseReplenishmentView {
    protected Context mContext = InStockHouseReplenishment.this;
    @ViewInject(R.id.replenishment_out_tray)
    EditText     mOutPalletNo;
    @ViewInject(R.id.replenishment_out_tray_qty)
    EditText     mOutPalletQty;
    @ViewInject(R.id.replenishment_in_tray)
    EditText     mInPalletNo;
    @ViewInject(R.id.replenishment_move_in_tray_area_no)
    EditText     mInPalletAreaNo;
    @ViewInject(R.id.replenishment_recycle_view)
    RecyclerView mRecyclerView;
    @ViewInject(R.id.replenishment_refer_button)
    Button       mReferButton;
    InStockHouseReplenishmentItemAdapter mAdapter;
    InStockHouseReplenishmentPresenter   mPresenter;


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.isCloseActivity = false;
        BaseApplication.toolBarTitle = new ToolBarTitle(getToolBarTitle(), true);
        x.view().inject(this);
        closeKeyBoard(mOutPalletNo, mOutPalletQty, mInPalletNo, mInPalletAreaNo);
//        initListener();
        onOutPalletNoFocus();
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
        if (mPresenter == null) {
            mPresenter = new InStockHouseReplenishmentPresenter(mContext, this, mHandler);
        }


    }


    @Event(value = R.id.replenishment_move_in_tray_area_no, type = View.OnKeyListener.class)
    private boolean scanAreaNo(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            String areaNo = mInPalletAreaNo.getText().toString().trim();
            if (TextUtils.isEmpty(areaNo)) {
                CommonUtil.setEditFocus(mInPalletAreaNo);
                return true;
            }
            mPresenter.getAreaInfo(areaNo);
        }
        return false;
    }


    /**
     * @desc: 扫描托盘码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/7 12:45
     */
    @Event(value = {R.id.replenishment_out_tray, R.id.replenishment_out_tray_qty, R.id.replenishment_in_tray}, type = View.OnKeyListener.class)
    private boolean outBarcodeScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            switch (v.getId()) {
                case R.id.replenishment_out_tray:
                    String outPalletNo = mOutPalletNo.getText().toString().trim();
                    if (mPresenter != null) {
                        mPresenter.onOutPalletScan(outPalletNo);
                    }
                    break;
                case R.id.replenishment_out_tray_qty:
                    try {
                        float qty = Float.parseFloat(mOutPalletQty.getText().toString().trim());
                        if (mAdapter != null) {
                            if (mPresenter != null) {
                                mPresenter.checkOutPalletQty(qty);
                            }
                        } else {
                            MessageBox.Show(mContext, "请先扫描移出托盘", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onOutPalletNoFocus();
                                }
                            });
                        }
                    } catch (Exception e) {
                        MessageBox.Show(mContext, "输入数量格式不正确!"+e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onOutPalletNoFocus();
                            }
                        });
                    }


                    break;
                case R.id.replenishment_in_tray:
                    break;
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
//                    mPresenter.onOrderRefer();
                    break;
            }
        }
        return false;
    }

    @Override
    public void onOutPalletNoFocus() {
        CommonUtil.setEditFocus(mOutPalletNo);

    }

    @Override
    public void onOutPalletQtyFocus() {
        CommonUtil.setEditFocus(mOutPalletQty);
    }

    @Override
    public void onInPalletNoFocus() {
        CommonUtil.setEditFocus(mInPalletNo);
    }

    @Override
    public void onInPalletAreaNoFocus() {
        CommonUtil.setEditFocus(mInPalletAreaNo);
    }

    @Override
    public void setInPalletAreaNoEnable(boolean enable) {
        if (mOutPalletQty.isEnabled() != enable) {
            mOutPalletQty.setEnabled(enable);
        }

    }

    @Override
    public void setInPalletAreaNo(String areaNo) {

    }

    @Override
    public void onReset() {

    }

    @Override
    public void bindListView(List<StockInfo> list) {
        try {
            if (mAdapter == null || list.size() == 0) {
                mAdapter = new InStockHouseReplenishmentItemAdapter(mContext, list);
                mAdapter.setOnItemClickListener(new InStockHouseReplenishmentItemAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView parent, View view, int position, StockInfo data) {
                        if (data != null) {
                            try {
                                mAdapter.setSingleCheckedStatus(view, position);
                            } catch (Exception e) {
                                MessageBox.Show(context, "点击列表出现预期之外的异常:" + e.getMessage());
                            }
                        }

                    }


                });
//            mAdapter.setOnItemLongClickListener(new InStockPalletItemAdapter.OnItemLongClickListener() {
//                @Override
//                public void onItemLongClick(RecyclerView parent, View view, int position, OutBarcodeInfo data) {
//                    if (data!=null){
//                        new AlertDialog.Builder(mContext).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除物料编号:" + materialNo + ",批次为:" + batchNo + "的物料行？")
//                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // TODO 自动生成的方法
////                            mPresenter.onDelete(position);
//                                    }
//                                }).setNegativeButton("取消", null).show();
//                        OrderHeaderInfo orderHeaderInfo=mPresenter.getModel().getOrderHeaderInfo();
//                        if (orderHeaderInfo!=null){
//
//                        }
//
//
//
//                    }
//                }
//            });
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else {
                mAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            MessageBox.Show(context, "显示列表出现预期之外的异常:" + e.getMessage());
        }

    }

    @Override
    public void setOutPalletQty(float qty) {
        mOutPalletQty.setText(qty + "");
    }

    @Override
    public List<StockInfo> getSelectedMaterialItems() {
        if (mAdapter != null) {
            return mAdapter.getSelectedData();
        } else {
            return null;
        }

    }


}
