package com.liansu.boduowms.modules.pallet.combinePallet;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2019/11/14.
 */
public interface ICombinPalletView {

    void requestPalletBarcodeFocus();

    void requestBarcodeFocus();

    void setCurrentBarcodeInfo(OutBarcodeInfo info);

    void setSumCountInfo(String sumCountString);

    void setPalletNo(String palletNo);

    void setBarcode(String barcode);

    void bindListView(List<OutBarcodeInfo> list);

    void onClear();

    void setBottomText(String text);

    int getCombinPalletType();

    void showPalletScan(boolean isCheck);

    void setSwitchButton(boolean isCheck);
    void createDialog(OutBarcodeInfo info);
}
