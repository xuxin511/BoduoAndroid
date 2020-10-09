package com.liansu.boduowms.modules.setting.newPrint;

import android.widget.EditText;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/29.
 */
 public interface INewSettingPrintView {
    int getInStockPrintType();
    String getLaserPrinterAddress();
    int getOutStockPrintType();
    String getDesktopPrintAddress();
    String getBluetoothPrinterMacAddress();
    void createPrintAddressListDialog(List<String> list, EditText editText);
   int getOutStockPackingBoxPrintType();
}
