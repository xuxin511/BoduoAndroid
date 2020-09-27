package com.liansu.boduowms.modules.instock.combinePallet;

import android.content.Context;
import android.os.Message;
import android.view.KeyEvent;
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
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.ui.adapter.inHouseStock.InStockPalletItemAdapter;
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
 * @desc: 入库拼托
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/6/25 22:00
 */
@ContentView(R.layout.activity_instock_combin_pallet)
public class InstockCombinePallet extends BaseActivity implements IInstockCombinePalletView, RadioGroup.OnCheckedChangeListener {
    @ViewInject(R.id.instock_combine_pallet_one)
    EditText     mFirstPallet;
    @ViewInject(R.id.instock_combine_pallet_two)
    EditText     mSecondPallet;
    @ViewInject(R.id.instock_combine_pallet_two_desc)
    TextView     mSecondPalletDesc;
    @ViewInject(R.id.list_recycle_view)
    RecyclerView mRecyclerView;
    @ViewInject(R.id.btn_refer)
    Button       mRefer;
    @ViewInject(R.id.instock_combine_radio_group)
    RadioGroup   mRadioGroup;
    InStockPalletItemAdapter      mAdapter;
    Context                       mContext = InstockCombinePallet.this;
    InstockCombinePalletPresenter mPresenter;
    public final int REQUEST_CODE_OK = 1;

    @Override
    public void onHandleMessage(Message msg) {
        super.onHandleMessage(msg);
        if (mPresenter != null) {
            mPresenter.onHandleMessage(msg);
        }

    }


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(getToolBarTitle(), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        mRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPresenter != null) {
                    if (getCombinePalletType()==InstockCombinePalletModel.COMBINE_PALLET_TYPE_DIS_COMBINE_PALLET){
                        mPresenter.onDisCombinePalletListRefer(mAdapter.getSelectedData());
                    }else {
                        mPresenter.onCombinePalletListRefer();
                    }

                }
            }
        });

        mRadioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter = new InstockCombinePalletPresenter(mContext, this, mHandler);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Event(value = {R.id.instock_combine_pallet_one, R.id.instock_combine_pallet_two}, type = View.OnKeyListener.class)
    private boolean scanPalletNo(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            switch (v.getId()) {
                case R.id.instock_combine_pallet_one:
                    String firstPalletNo = mFirstPallet.getText().toString().trim();
                    if (mPresenter != null) {
                        mPresenter.scanPalletInfo(firstPalletNo, InstockCombinePalletModel.PALLET_TYPE_FIRST_PALLET);
                        return true;
                    }
                    break;
                case R.id.instock_combine_pallet_two:
                    String secondPalletNo = mSecondPallet.getText().toString().trim();
                    if (mPresenter != null) {
                        mPresenter.scanPalletInfo(secondPalletNo, InstockCombinePalletModel.PALLET_TYPE_SECOND_PALLET);
                        return true;
                    }
                    break;
            }

        }
        return false;
    }



    @Override
    public void requestPalletOneFocus() {
        CommonUtil.setEditFocus(mFirstPallet);
    }

    @Override
    public void requestPalletTwoFocus() {
        CommonUtil.setEditFocus(mSecondPallet);
    }

    @Override
    public void requestPalletFocus(int palletType) {
        if (palletType == InstockCombinePalletModel.PALLET_TYPE_FIRST_PALLET) {
            requestPalletOneFocus();
        } else if (palletType == InstockCombinePalletModel.PALLET_TYPE_SECOND_PALLET) {
            requestPalletTwoFocus();
        }
    }

    @Override
    public void bindListView(List<StockInfo> list)  {
        try {
//            if (mAdapter == null ||list.size()==0) {
                mAdapter = new InStockPalletItemAdapter(mContext, list);
                mAdapter.setOnItemClickListener(new InStockPalletItemAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView parent, View view, int position, StockInfo data) {
                        if (data != null) {
                            if (getCombinePalletType()==InstockCombinePalletModel.COMBINE_PALLET_TYPE_DIS_COMBINE_PALLET){
                                try {
                                    mAdapter.setCheckedStatus(view,position);
                                } catch (Exception e) {
                                    MessageBox.Show(context,"点击列表出现预期之外的异常:"+e.getMessage());
                                }
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
//            } else {
//                mAdapter.notifyDataSetChanged();
//            }
        }catch (Exception e){
            MessageBox.Show(context,"显示列表出现预期之外的异常:"+e.getMessage());
        }

    }


    @Override
    public void onReset() {
        mFirstPallet.setText("");
        mSecondPallet.setText("");
        if (mPresenter!=null){
            mAdapter=null;
            bindListView(mPresenter.getModel().getShowList());
        }
        requestPalletOneFocus();
    }


    /**
     * @desc: 1  新建组托   2.添加组托
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/2/13 14:34
     */
    @Override
    public int getCombinePalletType() {
        String value = "";
        int type = InstockCombinePalletModel.COMBINE_PALLET_TYPE_NONE;
        int count = mRadioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton rb = (RadioButton) mRadioGroup.getChildAt(i);
            if (rb.isChecked()) {
                value = rb.getText().toString().trim();
                break;
            }
        }
        if (value.equals(InstockCombinePalletModel.COMBINE_PALLET_TYPE_OLD_PALLET_NAME)) {
            type = InstockCombinePalletModel.COMBINE_PALLET_TYPE_OLD_PALLET;
        } else if (value.equals(InstockCombinePalletModel.COMBINE_PALLET_TYPE_NEW_PALLET_NAME)) {
            type = InstockCombinePalletModel.COMBINE_PALLET_TYPE_NEW_PALLET;
        } else if (value.equals(InstockCombinePalletModel.COMBINE_PALLET_TYPE_DIS_COMBINE_PALLET_NAME)) {
            type = InstockCombinePalletModel.COMBINE_PALLET_TYPE_DIS_COMBINE_PALLET;
        }

        return type;
    }

    @Override
    public void initViewStatus(int printType) {
        if (printType == InstockCombinePalletModel.COMBINE_PALLET_TYPE_OLD_PALLET || printType == InstockCombinePalletModel.COMBINE_PALLET_TYPE_NEW_PALLET) {
            if (mSecondPalletDesc.getVisibility()!=View.VISIBLE){
                mSecondPalletDesc.setVisibility(View.VISIBLE);
                mSecondPallet.setVisibility(View.VISIBLE);
            }
        } else if (printType == InstockCombinePalletModel.COMBINE_PALLET_TYPE_DIS_COMBINE_PALLET) {
            if (mSecondPalletDesc.getVisibility()!=View.GONE){
                mSecondPalletDesc.setVisibility(View.GONE);
                mSecondPallet.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        initViewStatus(getCombinePalletType());
        if (mPresenter!=null){
            mPresenter.onReset();
        }
    }
}
