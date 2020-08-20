package com.liansu.boduowms.modules.instock.salesReturn.print;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.modules.instock.salesReturn.scan.SalesReturnStorageScan;
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.DoubleClickCheck;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ Des: 销售退货打印  打印托盘标签和外箱标签
 * @ Created by yangyiqing on 2020/7/17.
 */
@ContentView(R.layout.return_sales_print)
public class SalesReturnPrint extends BaseActivity implements ISalesReturnPrintView , IUserSettingView {
    Context mContext = SalesReturnPrint.this;
    @ViewInject(R.id.return_sales_print_customer_code)
    EditText  mCustomerCode;
    @ViewInject(R.id.return_storage_scan_print_query_start_date_time)
    EditText mStartDateTime;
    @ViewInject(R.id.return_storage_scan_print_query_end_date_time)
    EditText  mEndDateTime;
    @ViewInject(R.id.return_storage_scan_print_query_date_time_select)
    ImageView mStartDateTimeSelect;
    @ViewInject(R.id.return_storage_scan_print_query_end_date_time_select)
    ImageView mEndDateTimeSelect;
    @ViewInject(R.id.return_storage_scan_print_material_no)
    EditText mMaterialNo;
    @ViewInject(R.id.return_storage_scan_print_material_name)
    TextView mMaterialName;
    @ViewInject(R.id.return_storage_scan_print_batch_no)
    Spinner  mBatchNoSpinner;
    @ViewInject(R.id.return_storage_scan_print_pack_qty)
    EditText mPackQty;
    @ViewInject(R.id.return_storage_scan_print_qty)
    EditText mSumPalletQty;
    @ViewInject(R.id.return_storage_scan_print_button)
    Button   mPrintButton;
    ArrayAdapter              mBatchNoArrayAdapter;
    SalesReturnPrintPresenter mPresenter;
    TimePickerView pvCustomLunar;
    protected UserSettingPresenter mUserSettingPresenter;
    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(mContext.getResources().getString(R.string.appbar_title_sales_return_storage_print) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename(), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        onReset();

    }


    @Override
    protected void initData() {
        super.initData();
        mPresenter = new SalesReturnPrintPresenter(mContext, this, mHandler);
        mUserSettingPresenter=new UserSettingPresenter(mContext,this);

    }

    @Override
    public void onHandleMessage(Message msg) {
        mPresenter.onHandleMessage(msg);
    }

    @Override
    public void onCustomerNoFocus() {
        CommonUtil.setEditFocus(mCustomerCode);
    }

    @Override
    public void onStartTimeFocus() {
        CommonUtil.setEditFocus(mStartDateTime);
    }

    @Override
    public void onEndTimeFocus() {
        CommonUtil.setEditFocus(mEndDateTime);
    }

    @Override
    public void onMaterialNoFocus() {
        CommonUtil.setEditFocus(mMaterialNo);
    }

    @Override
    public void onPackQtyFocus() {
        CommonUtil.setEditFocus(mPackQty);
    }

    @Override
    public void onPrintQtyFocus() {
        CommonUtil.setEditFocus(mSumPalletQty);
    }

    @Override
    public void setMaterialInfo(OrderDetailInfo orderDetailInfo) {
        if (orderDetailInfo != null && orderDetailInfo.getMaterialdesc() != null) {
            mMaterialName.setText(orderDetailInfo.getMaterialdesc());
            mPackQty.setText(orderDetailInfo.getPackQty() + "");
            if (orderDetailInfo.getPackQty() > 0) {
                setPackQtyEnable(false);
            } else {
                setPackQtyEnable(true);
            }
        } else {
            mMaterialName.setText("");
        }
    }

    @Override
    public void setSpinnerData(List<String> list) {
        if (list == null || list.size() == 0) {
            if (mBatchNoSpinner.getVisibility() != View.GONE) {
                mBatchNoSpinner.setVisibility(View.GONE);
            }
            return;
        } else {
            if (mBatchNoSpinner.getVisibility() != View.VISIBLE) {
                mBatchNoSpinner.setVisibility(View.VISIBLE);
            }

            // 设置spinner，不用管什么作用
            mBatchNoArrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            mBatchNoArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉风格
            mBatchNoSpinner.setAdapter(mBatchNoArrayAdapter); // 将adapter 添加到spinner中
            mBatchNoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(
            ) {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });// 添加监听
        }

    }

    @Event(value = {R.id.return_sales_print_customer_code, R.id.return_storage_scan_print_query_start_date_time, R.id.return_storage_scan_print_query_end_date_time, R.id.return_storage_scan_print_material_no}, type = View.OnKeyListener.class)
    private boolean edtStockScanClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {


            switch (v.getId()) {
                case R.id.return_sales_print_customer_code:
                    String customerCode = mCustomerCode.getText().toString().trim();
                    if (!customerCode.equals("")) {
                        onStartTimeFocus();
                    } else {
                        onCustomerNoFocus();
                    }

                    break;
                case R.id.return_storage_scan_print_query_start_date_time:
                    String startDateTime = mStartDateTime.getText().toString().trim();
                    if (!startDateTime.equals("")) {
                        onEndTimeFocus();
                    } else {
                        onStartTimeFocus();
                    }
                    break;
                case R.id.return_storage_scan_print_query_end_date_time:
                    String endDateTime = mEndDateTime.getText().toString().trim();
                    if (!endDateTime.equals("")) {
                        onMaterialNoFocus();
                    } else {
                        onEndTimeFocus();
                    }
                    break;
                case R.id.return_storage_scan_print_material_no:
                    String materialNo = mMaterialNo.getText().toString().trim();
                    String customerCode2 = mCustomerCode.getText().toString().trim();
                    String endDateTime2 = mEndDateTime.getText().toString().trim();
                    String startDateTime2 = mStartDateTime.getText().toString().trim();
                    if (!materialNo.equals("")) {
                        mPresenter.getMaterialNoBatchList(materialNo, startDateTime2, endDateTime2, customerCode2);
                    } else {
                        onMaterialNoFocus();
                    }
                    break;

            }

        }
        return false;
    }

    @Override
    public void createCalendarDialog(final EditText editText) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2069, 2, 28);
        //时间选择器 ，自定义布局
        TimePickerView pvCustomLunar = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                editText.setText(getTime(date));
//                Toast.makeText(SalesReturnPrint.this, getTime(date), Toast.LENGTH_SHORT).show();
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate).setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.RED).build();

        pvCustomLunar.show();
    }

    @Override
    public void setPackQty(float packQty) {
        mPackQty.setText(packQty + "");
    }

    @Override
    public void setPackQtyEnable(boolean isEnable) {
        if (isEnable) {
            mPackQty.setEnabled(true);
        } else {
            mPackQty.setEnabled(false);
        }
    }

    @Override
    public void onReset() {
        mCustomerCode.setText("");
        mStartDateTime.setText("");
        mEndDateTime.setText("");
        setSpinnerData(null);
        mMaterialNo.setText("");
        mMaterialName.setText("");
        setPackQty(0);
        mSumPalletQty.setText(0 + "");


    }



    @Event(R.id.return_storage_scan_print_button)
    private void btnCombinePalletClick(View view) {
        if (mPresenter != null) {
            String materialNo = mMaterialNo.getText().toString().trim();
            String materialName = mMaterialName.getText().toString().trim();
            String batchNo = "";
            if (mBatchNoArrayAdapter != null) {
                batchNo = mBatchNoSpinner.getSelectedItem().toString();
            }
            float packQty = Float.parseFloat(mPackQty.getText().toString().trim());
            float sumPalletQty = Float.parseFloat(mSumPalletQty.getText().toString().trim());
            mPresenter.onPrint(materialNo, materialName, batchNo, packQty, sumPalletQty);
        }
    }

    @Event(value ={R.id.return_storage_scan_print_query_date_time_select,R.id.return_storage_scan_print_query_end_date_time_select})
    private void onTimePickerViewClick(View view) {
        switch (view.getId()){
            case R.id.return_storage_scan_print_query_date_time_select:
                createCalendarDialog(mStartDateTime);
                break;
            case R.id.return_storage_scan_print_query_end_date_time_select:
                createCalendarDialog(mEndDateTime);
                break;
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sale_return_icon) {
            if (DoubleClickCheck.isFastDoubleClick(mContext)) {
                return false;
            }

            Intent intent = new Intent();
            intent.setClass(SalesReturnPrint.this, SalesReturnStorageScan.class);
            startActivityLeft(intent);


        }
        if (item.getItemId() == R.id.user_setting_warehouse_select) {
            selectWareHouse(mUserSettingPresenter.getModel().getWareHouseNameList());
        }
        return false;
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
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
        getMenuInflater().inflate(R.menu.mednu_sale_return, menu);
        return true;
    }




}
