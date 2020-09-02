package com.liansu.boduowms.modules.setting.print;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseFragment;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.modules.print.DeviceListActivity;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.ui.adapter.quality_inspection.QualityInspectionBillItemAdapter;
import com.liansu.boduowms.ui.dialog.MessageBox;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import androidx.annotation.Nullable;

import static com.liansu.boduowms.modules.print.DeviceListActivity.EXTRA_DEVICE_ADDRESS;
import static com.liansu.boduowms.utils.SharePreferUtil.setBluetoothPrinterMacAddressShare;

/**
 * @desc: 打印设置
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/7/30 16:02
 */
@ContentView(R.layout.activity_setting_print)
public class SettingPrintingFragment extends BaseFragment implements ISettingPrintView {
    private static final int REQUEST_CODE_OK = 1;
    Context mContext;
    @ViewInject(R.id.setting_print_laser_printer)
    EditText    mLaserPrinter;
    @ViewInject(R.id.setting_print_laser_printer_select)
    Button      mLaserPrinterListSelectedButton;
    @ViewInject(R.id.setting_print_desktop_printer)
    EditText    mDesktopPrinter;
    @ViewInject(R.id.setting_print_desktop_printer_select)
    Button      mDesktopPrinterListSelectedButton;
    @ViewInject(R.id.setting_print_bluetooth_printer)
    EditText    mBluetoothPrinter;
    @ViewInject(R.id.setting_print_bluetooth_printer_select)
    Button      mBluetoothPrinterListSelectedButton;
    @ViewInject(R.id.setting_print_bluetooth_printer_test)
    Button      mBluetoothPrinterTestButton;
    @ViewInject(R.id.setting_print_in_stock_radio_group)
    RadioGroup  mInStockRadioGroup;
    @ViewInject(R.id.setting_print_out_stock_radio_group)
    RadioGroup  mOutStockRadioGroup;
    @ViewInject(R.id.setting_print_out_stock_packing_box_radio_group)
    RadioGroup  mOutStockPackingRadioGroup;
    @ViewInject(R.id.setting_print_in_stock_select_laser_printer)
    RadioButton mInStockSelectLaserPrinter;
    @ViewInject(R.id.setting_print_in_stock_select_desktop_printer)
    RadioButton mInStockSelectDesktopPrinter;
    @ViewInject(R.id.setting_print_out_stock_select_laser_printer)
    RadioButton mOutStockSelectLaserPrLaserPrinter;
    @ViewInject(R.id.setting_print_out_stock_select_desktop_printer)
    RadioButton mOutStockSelectDesktopPrLaserPrinter;
    @ViewInject(R.id.setting_print_out_stock_packing_box_select_laser_printer)
    RadioButton mOutStockPackingBoxSelectLaserPrLaserPrinter;
    @ViewInject(R.id.setting_print_out_stock_packing_box_select_desktop_printer)
    RadioButton mOutStockPackingBoxSelectDesktopPrLaserPrinter;
    QualityInspectionBillItemAdapter mAdapter;
    SettingPrintingPresenter         mPresenter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        // 在界面onAttach之后就触发初始化Presenter
        mPresenter = new SettingPrintingPresenter(context, this, mHandler);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=super.onCreateView(inflater, container, savedInstanceState);
        initData();
        closeKeyBoard(mLaserPrinter,mDesktopPrinter,mBluetoothPrinter);
        return rootView;

    }


    @Override
    public void onHandleMessage(Message message) {
        mPresenter.onHandleMessage(message);
    }


    @Event(R.id.setting_print_bluetooth_printer_select)
    private void btnSelectBluetoothAddress(View view) {
        Intent serverIntent = new Intent(mContext, DeviceListActivity.class);
        startActivityForResult(serverIntent, 1);
    }

    @Event({R.id.setting_print_laser_printer_select, R.id.setting_print_desktop_printer_select})
    private void btnSelectPrinterAddress(View view) {
        switch (view.getId()) {
            case R.id.setting_print_laser_printer_select:
                createPrintAddressListDialog(mPresenter.getModel().getLaserPrinterAddressList(), mLaserPrinter);
                break;
            case R.id.setting_print_desktop_printer_select:
                createPrintAddressListDialog(mPresenter.getModel().getDesktopPrinterAddressList(), mDesktopPrinter);
                break;
        }
    }


    @Event(R.id.setting_print_bluetooth_printer_test)
    private void printLabelTest(View view) {
        mPresenter.onLabelTest();

    }

    @Event(R.id.setting_print_save_setting)
    private void onSave(View view) {
        mPresenter.onSave();
    }



    @Override
    public int getInStockPrintType() {
        return getPrintType(mInStockRadioGroup);
    }

    @Override
    public String getLaserPrinterAddress() {
        return mLaserPrinter.getText().toString().trim();
    }


    @Override
    public int getOutStockPrintType() {
        return getPrintType(mOutStockRadioGroup);
    }

    @Override
    public String getDesktopPrintAddress() {
        return mDesktopPrinter.getText().toString().trim();
    }

    @Override
    public String getBluetoothPrinterMacAddress() {
        return mBluetoothPrinter.getText().toString().trim();
    }


    @Override
    public void createPrintAddressListDialog(List<String> list, final EditText editText) {
        if (list != null && list.size() > 0) {
            final String[] items = list.toArray(new String[0]);
            new AlertDialog.Builder(mContext).setTitle(getResources().getString(R.string.activity_login_WareHousChoice))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setCancelable(false)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法存根
                            String address = items[which].toString();
                            editText.setText(address);
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public int getOutStockPackingBoxPrintType() {
        return  getPrintType(mOutStockPackingRadioGroup);
    }

    /**
     * @desc: 获取打印类型
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/30 13:58
     */
    public int getPrintType(RadioGroup mRadioGroup) {
        String value = "";
        int type = PrintBusinessModel.PRINTER_TYPE_NONE;
        int count = mRadioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton rb = (RadioButton) mRadioGroup.getChildAt(i);
            if (rb.isChecked()) {
                value = rb.getText().toString().trim();
                break;
            }
        }
        if (value.equals(mContext.getResources().getString(R.string.setting_print_laser_printer))) {
            type = PrintBusinessModel.PRINTER_TYPE_LASER;
        } else if (value.equals(mContext.getResources().getString(R.string.setting_print_desktop_printer))) {
            type = PrintBusinessModel.PRINTER_TYPE_DESKTOP;
        }

        return type;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.getPrinterAddressList();

        }

    }

    /**
     * @desc: 设置缓存数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/30 15:15
     */
    public void initData() {
        mLaserPrinter.setText(UrlInfo.mLaserPrinterAddress);
        mDesktopPrinter.setText(UrlInfo.mDesktopPrintAddress);
        mBluetoothPrinter.setText(UrlInfo.mBluetoothPrinterMacAddress);
        //入库方式赋值
        if (UrlInfo.mInStockPrintType == PrintBusinessModel.PRINTER_TYPE_LASER) {
            mInStockSelectLaserPrinter.setChecked(true);
            mInStockSelectDesktopPrinter.setChecked(false);
        } else if (UrlInfo.mInStockPrintType == PrintBusinessModel.PRINTER_TYPE_DESKTOP) {
            mInStockSelectLaserPrinter.setChecked(false);
            mInStockSelectDesktopPrinter.setChecked(true);

        }
        // 出库方式赋值
        if (UrlInfo.mOutStockPrintType == PrintBusinessModel.PRINTER_TYPE_LASER) {
            mOutStockSelectLaserPrLaserPrinter.setChecked(true);
            mOutStockSelectDesktopPrLaserPrinter.setChecked(false);
        } else if (UrlInfo.mOutStockPrintType == PrintBusinessModel.PRINTER_TYPE_DESKTOP) {
            mOutStockSelectLaserPrLaserPrinter.setChecked(false);
            mOutStockSelectDesktopPrLaserPrinter.setChecked(true);
        }
        // 出库拼箱扫描方式赋值
        if (UrlInfo.mOutStockPackingBoxPrintType == PrintBusinessModel.PRINTER_TYPE_LASER) {
            mOutStockPackingBoxSelectLaserPrLaserPrinter.setChecked(true);
            mOutStockPackingBoxSelectDesktopPrLaserPrinter.setChecked(false);
        } else if (UrlInfo.mOutStockPackingBoxPrintType == PrintBusinessModel.PRINTER_TYPE_DESKTOP) {
            mOutStockPackingBoxSelectLaserPrLaserPrinter.setChecked(false);
            mOutStockPackingBoxSelectDesktopPrLaserPrinter.setChecked(true);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_CODE_OK:
                    if (resultCode == Activity.RESULT_OK) {
                        String macAddress = data.getStringExtra(EXTRA_DEVICE_ADDRESS);
                        if (macAddress != null) {
                            mBluetoothPrinter.setText(macAddress);
                            setBluetoothPrinterMacAddressShare(mContext, macAddress);
                        }

                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "从蓝牙地址界面传递数据出现异常" + e.getMessage() );
        }
    }
}
