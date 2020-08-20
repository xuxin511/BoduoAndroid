package com.liansu.boduowms.modules.instock.batchPrint.order;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.modules.instock.batchPrint.print.BaseOrderLabelPrint;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.ui.adapter.instock.baseScanStorage.BaseOrderLabelPrintDetailAdapter;
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
 * @ Des: 选择物料
 * @ Created by yangyiqing on 2020/8/13.
 */
@ContentView(R.layout.base_order_label_print_select)
public class BaseOrderLabelPrintSelect extends BaseActivity implements IBaseOrderLabelPrintSelectView, RadioGroup.OnCheckedChangeListener, IUserSettingView {
    @ViewInject(R.id.base_order_label_print_select_erp_voucher_no)
    EditText     mErpVoucherNo;
    @ViewInject(R.id.base_order_label_print_select_list_view)
    RecyclerView mRecyclerView;
    @ViewInject(R.id.base_order_label_print_select_order_type)
    Spinner      mVoucherTypeNameSpinner;
    @ViewInject(R.id.base_order_label_print_select_radio_group)
    RadioGroup   mRadioGroup;
    @ViewInject(R.id.base_order_label_print_select_material_no)
    EditText     mMaterialNo;
    @ViewInject(R.id.base_order_label_print_select_erp_voucher_no_desc)
    TextView     mErpVoucherNoDesc;
    @ViewInject(R.id.base_order_label_print_select_order_type_desc)
    TextView     mOrderTypeDesc;
    @ViewInject(R.id.base_order_label_print_select_type_pallet_no)
    RadioButton  mPalletNoType;
    @ViewInject(R.id.base_order_label_print_select_type_outer_box)
    RadioButton  mOuterBoxType;
    Context                            mContext      = BaseOrderLabelPrintSelect.this;
    String                             mBusinessType = "";
    BaseOrderLabelPrintSelectPresenter mPresenter;
    BaseOrderLabelPrintDetailAdapter   mAdapter;
    ArrayAdapter                       mVoucherTypeNameArrayAdapter;
    protected UserSettingPresenter mUserSettingPresenter;
    @Override
    public void onHandleMessage(Message msg) {
        if (mPresenter != null) {
            mPresenter.onHandleMessage(msg);
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(mContext.getResources().getString(R.string.main_menu_item_batch_printing) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename(), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        initViewStatus(getPrintType());
        mRadioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
        mUserSettingPresenter=new UserSettingPresenter(mContext,this);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.mednu_sale_return, menu);
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.menu_sale_return_icon) {
//            if (DoubleClickCheck.isFastDoubleClick(mContext)) {
//                return false;
//            }
//            Intent intent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("OrderHeaderInfo", null);
//            bundle.putParcelableArrayList("barCodeInfo", null);
//            intent.putExtra("BusinessType", mBusinessType);
//            intent.putExtras(bundle);
//            intent.setClass(BaseOrderLabelPrintSelect.this, BaseOrderLabelPrint.class);
//            startActivityLeft(intent);
//
//
//        }
//        return false;
//    }

    @Override
    public void onErpVoucherNoFocus() {
        CommonUtil.setEditFocus(mErpVoucherNo);
    }

    @Override
    public void onMaterialFocus() {
        CommonUtil.setEditFocus(mMaterialNo);
    }

    @Override
    public int getPrintType() {
        String value = "";
        int type = PrintBusinessModel.PRINTER_LABEL_TYPE_NONE;
        int count = mRadioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton rb = (RadioButton) mRadioGroup.getChildAt(i);
            if (rb.isChecked()) {
                value = rb.getText().toString().trim();
                break;
            }
        }
        if (value.equals(PrintBusinessModel.PRINTER_LABEL_NAME_PALLET_NO)) {
            type = PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO;
        } else if (value.equals(PrintBusinessModel.PRINTER_LABEL_NAME_OUTER_BOX)) {
            type = PrintBusinessModel.PRINTER_LABEL_TYPE_OUTER_BOX;
        }

        return type;
    }

    @Override
    public String getVoucherTypeName() {
        return mVoucherTypeNameSpinner.getSelectedItem().toString();
    }

    @Override
    public void onReset(boolean isReset) {
        if (isReset){
            initViewStatus(getPrintType());
            if (mPresenter!=null){
                bindListView(mPresenter.getModel().getOrderDetailList());
            }
        }else {
            onMaterialFocus();
        }


    }




    @Override
    public void bindListView(List<OrderDetailInfo> orderDetailInfos) {
        if (mAdapter == null) {
            mAdapter = new BaseOrderLabelPrintDetailAdapter(mContext, "", orderDetailInfos);
            mAdapter.setRecyclerView(mRecyclerView);
            mAdapter.setOnItemClickListener(new BaseOrderLabelPrintDetailAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView parent, View view, int position, OrderDetailInfo data) {
                    if (data!=null){
                        StartScanIntent(data);
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


    /**
     * @desc: 订单号
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/7 12:45
     */
    @Event(value = {R.id.base_order_label_print_select_erp_voucher_no, R.id.base_order_label_print_select_material_no}, type = View.OnKeyListener.class)
    private boolean onScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            switch (v.getId()) {
                case R.id.base_order_label_print_select_erp_voucher_no:
                    if (mPresenter != null) {
                        String voucherTypeName = getVoucherTypeName();
                        String erpVoucherNo = mErpVoucherNo.getText().toString().trim();
                        mPresenter.getOrderDetailInfoList(voucherTypeName, erpVoucherNo);
                    }
                    break;
                case R.id.base_order_label_print_select_material_no:
                    if (mPresenter != null) {
                        String materialNo = mMaterialNo.getText().toString().trim();
                        mPresenter.onScan(materialNo, getPrintType());

                    }
                    break;
            }

        }

        return false;
    }


    @Override
    public void setSpinnerData(List<String> list) {
        if (list == null || list.size() == 0) {
            if (mVoucherTypeNameSpinner.getVisibility() != View.GONE) {
                mVoucherTypeNameSpinner.setVisibility(View.GONE);
            }
            return;
        } else {
            if (mVoucherTypeNameSpinner.getVisibility() != View.VISIBLE) {
                mVoucherTypeNameSpinner.setVisibility(View.VISIBLE);
            }

            mVoucherTypeNameArrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            mVoucherTypeNameArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉风格
            mVoucherTypeNameSpinner.setAdapter(mVoucherTypeNameArrayAdapter); // 将adapter 添加到spinner中
            mVoucherTypeNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(
            ) {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       if (mPresenter!=null){
                           mPresenter.onReset(true);
                       }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });// 添加监听
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter==null){
            mPresenter = new BaseOrderLabelPrintSelectPresenter(mContext, this, mHandler);
            mPresenter.getVoucherTypeList();
            initViewStatus(getPrintType());
        }else {
            mPresenter.onReset(false);
        }



    }

    @Override
    public void StartScanIntent(OrderDetailInfo orderDetailInfo) {
        Intent intent = new Intent();
        intent.setClass(BaseOrderLabelPrintSelect.this, BaseOrderLabelPrint.class);
        Bundle bundle = new Bundle();
        bundle.putInt("PRINT_TYPE", getPrintType());
        bundle.putParcelable("ORDER_DETAIL_INFO", orderDetailInfo);
        intent.putExtras(bundle);
        startActivityLeft(intent);
    }

    @Override
    public void initViewStatus(int printType) {
        if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_OUTER_BOX) {
            if (mErpVoucherNoDesc.getVisibility() != View.GONE) {
                mErpVoucherNoDesc.setVisibility(View.GONE);
                mErpVoucherNo.setVisibility(View.GONE);
                mOrderTypeDesc.setVisibility(View.GONE);
                mVoucherTypeNameSpinner.setVisibility(View.GONE);
            }
            mErpVoucherNo.setText("");
            mMaterialNo.setText("");
            onMaterialFocus();
        } else if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO) {
            if (mErpVoucherNoDesc.getVisibility() != View.VISIBLE) {
                mErpVoucherNoDesc.setVisibility(View.VISIBLE);
                mErpVoucherNo.setVisibility(View.VISIBLE);
                mOrderTypeDesc.setVisibility(View.VISIBLE);
                mVoucherTypeNameSpinner.setVisibility(View.VISIBLE);
            }
            mErpVoucherNo.setText("");
            mMaterialNo.setText("");
            onErpVoucherNoFocus();

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (mPresenter!=null){
            mPresenter.onReset(true);
        }

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
