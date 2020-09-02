package com.liansu.boduowms.modules.setting.print;

import android.widget.EditText;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/29.
 */
 public interface ISettingPrintView {
    int getInStockPrintType();
    String getLaserPrinterAddress();
    int getOutStockPrintType();
    String getDesktopPrintAddress();
    String getBluetoothPrinterMacAddress();
    void createPrintAddressListDialog(List<String> list, EditText editText);
   int getOutStockPackingBoxPrintType();
}
