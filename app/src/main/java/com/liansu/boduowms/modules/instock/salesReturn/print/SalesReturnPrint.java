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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.DateUtil;
import com.liansu.boduowms.utils.function.DoubleClickCheck;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ Des: 销售退货打印  打印托盘标签和外箱标签
 * @ Created by yangyiqing on 2020/7/17.
 */
@ContentView(R.layout.return_sales_print)
public class SalesReturnPrint extends BaseActivity implements ISalesReturnPrintView, IUserSettingView {
    Context mContext = SalesReturnPrint.this;
    @ViewInject(R.id.return_sales_print_customer_code)
    EditText  mCustomerCode;
    @ViewInject(R.id.return_storage_scan_print_query_start_date_time)
    EditText  mStartDateTime;
    @ViewInject(R.id.return_storage_scan_print_query_end_date_time)
    EditText  mEndDateTime;
    @ViewInject(R.id.return_storage_scan_print_query_date_time_select)
    ImageView mStartDateTimeSelect;
    @ViewInject(R.id.return_storage_scan_print_query_end_date_time_select)
    ImageView mEndDateTimeSelect;
    @ViewInject(R.id.return_storage_scan_print_material_no)
    EditText  mMaterialNo;
    @ViewInject(R.id.return_storage_scan_print_material_name)
    TextView  mMaterialName;
    @ViewInject(R.id.return_storage_scan_print_batch_no)
    EditText  mBatchNoSpinner;
    @ViewInject(R.id.return_storage_scan_print_pack_qty)
    EditText  mPackQty;
    @ViewInject(R.id.return_storage_scan_print_count)
    EditText  mOuterBoxPrintCount;
    @ViewInject(R.id.return_storage_scan_print_qty)
    EditText  mPalletRemainQty;
    @ViewInject(R.id.return_storage_scan_print_pallet_qty)
    EditText  mPalletQty;
    @ViewInject(R.id.return_storage_scan_print_button)
    Button    mPrintButton;
    @ViewInject(R.id.return_storage_scan_batch_no_select)
    Button    mBatchNoSelect;
    ArrayAdapter              mBatchNoArrayAdapter;
    SalesReturnPrintPresenter mPresenter;
    TimePickerView            pvCustomLunar;
    protected UserSettingPresenter mUserSettingPresenter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(getToolBarTitle(), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        onReset();
        closeKeyBoard(mCustomerCode, mStartDateTime, mEndDateTime, mMaterialNo, mPackQty, mOuterBoxPrintCount, mPalletRemainQty, mPalletQty);
        mBatchNoSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSpinnerData(mPresenter.getModel().getCurrentBatchNoList());
            }
        });
//        case R.id.return_storage_scan_print_pack_qty:
//
//        break;
//        case R.id.return_storage_scan_print_count:
//        onRemainQtyFocus();
//        break;
//        case R.id.return_storage_scan_print_qty:
//        onPalletQtyFocus();
//        break;
//        case R.id.return_storage_scan_print_pallet_qty:
//        break;
        mPackQty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onPrintCountFocus();
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        mOuterBoxPrintCount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onRemainQtyFocus();
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

        mPalletRemainQty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onPalletQtyFocus();
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        mBatchNoSpinner.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String batchNo = mBatchNoSpinner.getText().toString();
                if (!batchNo.trim().equals("")) {
                    if (!DateUtil.isValidDate(batchNo.trim(), "yyyyMMdd") && !batchNo.equals("")) {
                        MessageBox.Show(mContext, "校验日期格式失败:" + "日期格式不正确", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBatchNoFocus();

                            }
                        });
                    } else {
                        onPackQtyFocus();
                    }
                } else {
                    onPackQtyFocus();
                }
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

    }


    @Override
    protected void initData() {
        super.initData();
        mPresenter = new SalesReturnPrintPresenter(mContext, this, mHandler);
        mUserSettingPresenter = new UserSettingPresenter(mContext, this);

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
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtil.setEditFocus(mEndDateTime);
            }
        }, 200);


    }

    @Override
    public void onMaterialNoFocus() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtil.setEditFocus(mMaterialNo);
            }
        }, 200);

    }

    @Override
    public void onPackQtyFocus() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                float packQty = Float.parseFloat(mPackQty.getText().toString().trim());
                if (packQty > 0) {
                    onPrintCountFocus();
                } else {
                    CommonUtil.setEditFocus(mPackQty);
                }

            }
        }, 200);


    }

    @Override
    public void onPrintCountFocus() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtil.setEditFocus(mOuterBoxPrintCount);
            }
        }, 200);

    }

    @Override
    public void onRemainQtyFocus() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtil.setEditFocus(mPalletRemainQty);
            }
        }, 200);


    }

    @Override
    public void onPalletQtyFocus() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtil.setEditFocus(mPalletQty);
            }
        }, 200);


    }

    @Override
    public void onBatchNoFocus() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtil.setEditFocus(mBatchNoSpinner);
            }
        }, 200);

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
            setPackQtyEnable(true);
            mMaterialName.setText("");
        }
    }

    @Override
    public void setSpinnerData(List<String> list) {
        if (list == null || list.size() == 0) {
//            list = new ArrayList<>();
//            list.add(" ");
            return;
        }

        final String[] items = list.toArray(new String[0]);
        new AlertDialog.Builder(mContext).setTitle(getResources().getString(R.string.return_storage_scan_print_batch_no_select_title))// 设置对话框标题
                .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                .setCancelable(false)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 自动生成的方法存根
                        String select_item = items[which].toString();
                        mBatchNoSpinner.setText(select_item);
                        if (!select_item.trim().equals("")) {
                            if (!DateUtil.isValidDate(select_item.trim(), "yyyyMMdd")) {
                                MessageBox.Show(mContext, "校验日期格式失败:" + "日期格式不正确", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                            } else {
                                onBatchNoFocus();
                            }
                        } else {
                            onBatchNoFocus();
                        }

                        dialog.dismiss();
                    }
                }).show();
//            if (mBatchNoSpinner.getVisibility() != View.GONE) {
//                mBatchNoSpinner.setVisibility(View.GONE);
//            }
//            return;
//        } else {
//            if (mBatchNoSpinner.getVisibility() != View.VISIBLE) {
//                mBatchNoSpinner.setVisibility(View.VISIBLE);
//            }

        // 设置spinner，不用管什么作用
//            mBatchNoArrayAdapter = new ArrayAdapter<String>(this,
//                    android.R.layout.simple_spinner_item, list);
//            mBatchNoArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉风格
//            mBatchNoSpinner.setAdapter(mBatchNoArrayAdapter); // 将adapter 添加到spinner中
//            mBatchNoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(
//            ) {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });// 添加监听


    }

    @Event(value = {R.id.return_storage_scan_print_batch_no, R.id.return_sales_print_customer_code, R.id.return_storage_scan_print_query_start_date_time, R.id.return_storage_scan_print_query_end_date_time, R.id.return_storage_scan_print_material_no}, type = View.OnKeyListener.class)
//    @Event(value = {R.id.return_storage_scan_print_batch_no, R.id.return_sales_print_customer_code, R.id.return_storage_scan_print_query_start_date_time, R.id.return_storage_scan_print_query_end_date_time, R.id.return_storage_scan_print_material_no, R.id.return_storage_scan_print_pack_qty, R.id.return_storage_scan_print_count, R.id.return_storage_scan_print_qty, R.id.return_storage_scan_print_pallet_qty}, type = View.OnKeyListener.class)
    private boolean edtStockScanClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {


            switch (v.getId()) {
                case R.id.return_sales_print_customer_code:
                    String customerCode = mCustomerCode.getText().toString().trim();
                    if (!customerCode.trim().equals("")) {
                        onStartTimeFocus();
                    } else {
                        onCustomerNoFocus();
                    }

                    break;
                case R.id.return_storage_scan_print_query_start_date_time:
                    String startDateTime = mStartDateTime.getText().toString().trim();
                    if (!startDateTime.trim().equals("")) {
                        if (!DateUtil.isValidDate(startDateTime.trim(), "yyyy-MM-dd")) {
                            MessageBox.Show(mContext, "校验日期格式失败:" + "日期格式不正确", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onStartTimeFocus();
                                }
                            });
                            return false;
                        }
                        onEndTimeFocus();
                    } else {
                        onStartTimeFocus();
                    }
                    break;
                case R.id.return_storage_scan_print_query_end_date_time:
                    String endDateTime = mEndDateTime.getText().toString().trim();
                    if (!endDateTime.trim().equals("")) {
                        if (!DateUtil.isValidDate(endDateTime.trim(), "yyyy-MM-dd")) {
                            MessageBox.Show(mContext, "校验日期格式失败:" + "日期格式不正确", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onEndTimeFocus();
                                }
                            });
                            return false;
                        }
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
                    if (!materialNo.trim().equals("")) {
                        mPresenter.getMaterialNoBatchList(materialNo, startDateTime2, endDateTime2, customerCode2);
                    } else {
                        onMaterialNoFocus();
                    }
                    break;
//                case R.id.return_storage_scan_print_batch_no:
//
//                    String batchNo = mBatchNoSpinner.getText().toString();
//                    if (!batchNo.trim().equals("")) {
//                        if (!DateUtil.isValidDate(batchNo.trim(), "yyyyMMdd") && !batchNo.equals("")) {
//                            MessageBox.Show(mContext, "校验日期格式失败:" + "日期格式不正确", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    onBatchNoFocus();
//
//                                }
//                            });
//                        } else {
//                            onPackQtyFocus();
//                        }
//                    } else {
//                        onPackQtyFocus();
//                    }
//                    break;
//                case R.id.return_storage_scan_print_pack_qty:
//                    onPrintCountFocus();
//                    break;
//                case R.id.return_storage_scan_print_count:
//                    onRemainQtyFocus();
//                    break;
//                case R.id.return_storage_scan_print_qty:
//                    onPalletQtyFocus();
//                    break;
//                case R.id.return_storage_scan_print_pallet_qty:
//                    break;
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
                if (editText.getId() == R.id.return_storage_scan_print_query_start_date_time) {
                    onEndTimeFocus();
                } else if (editText.getId() == R.id.return_storage_scan_print_query_end_date_time) {
                    try {
                        if (!DateUtil.isStartTimeBeforeAndEqualsEndTime(getStartTime(), getEndTime())) {
                            MessageBox.Show(mContext, "校验时间失败:开始时间[" + getStartTime() + "]必须小于结束时间[" + getEndTime() + "]", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onEndTimeFocus();
                                }
                            });
                            return;
                        } else {
                            onMaterialNoFocus();
                        }
                    } catch (ParseException e) {
                        MessageBox.Show(mContext, "校验日期出现预期之外的异常:" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onStartTimeFocus();
                            }
                        });
                        return;
                    }

                }
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
        mBatchNoSpinner.setText("");
        setMaterialInfo(null);
        mCustomerCode.setText("");
        mStartDateTime.setText("");
        mEndDateTime.setText("");
        setSpinnerData(null);
        mMaterialNo.setText("");
        mMaterialName.setText("");
        setPackQty(0);
        mPalletRemainQty.setText(0 + "");
        mPackQty.setText("0");
        mOuterBoxPrintCount.setText("0");
        mPalletRemainQty.setText("0");
        mPalletQty.setText("0");
        onCustomerNoFocus();
        List<String> dateList = DateUtil.getDateStringFromSpecifyMonthsAgoOrAfter(-3, "yyyy-MM-dd");
        for (int i=0;i<dateList.size();i++){
            String date=dateList.get(i);
            if (date!=null){
                if (i==0){
                    mEndDateTime.setText(date);
                }else if (i==1){
                    mStartDateTime.setText(date);
                }
            }

        }
    }

    @Override
    public float getPalletRemainQty() {
        return Float.parseFloat(mPalletRemainQty.getText().toString().trim());
    }

    @Override
    public float getPalletQty() {
        return Float.parseFloat(mPalletQty.getText().toString().trim());
    }

    @Override
    public String getCustomerNo() {
        return mCustomerCode.getText().toString().trim();
    }

    @Override
    public String getStartTime() {
        return mStartDateTime.getText().toString().trim();
    }

    @Override
    public String getEndTime() {
        return mEndDateTime.getText().toString().trim();
    }


    @Event(R.id.return_storage_scan_print_button)
    private void btnCombinePalletClick(View view) {
        if (mPresenter != null) {
            String materialNo = mMaterialNo.getText().toString().trim();
            String materialName = mMaterialName.getText().toString().trim();
            String batchNo = "";
//            if (mBatchNoArrayAdapter != null) {
////                batchNo = mBatchNoSpinner.getSelectedItem().toString();
//                batchNo = mBatchNoSpinner.getText().toString();
//            }
            batchNo = mBatchNoSpinner.getText().toString();
            float packQty = Float.parseFloat(mPackQty.getText().toString().trim());
            float packCount = Float.parseFloat(mOuterBoxPrintCount.getText().toString().trim());
            mPresenter.onPrint(materialNo, materialName, batchNo, packQty, packCount);
        }
    }

    @Event(value = {R.id.return_storage_scan_print_query_date_time_select, R.id.return_storage_scan_print_query_end_date_time_select})
    private void onTimePickerViewClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.return_storage_scan_print_query_date_time_select:
                    createCalendarDialog(mStartDateTime);
                    break;
                case R.id.return_storage_scan_print_query_end_date_time_select:
                    createCalendarDialog(mEndDateTime);
                    break;
            }

        } catch (Exception e) {
            MessageBox.Show(mContext, "选择日期出现预期之外的异常," + e.getMessage());
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
            selectWareHouse(mUserSettingPresenter.getModel().getWareHouseNoList());
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
        if (mPresenter != null) {
            getToolBarHelper().getToolBar().setTitle(getToolBarTitle());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mednu_sale_return, menu);
        return true;
    }


}
