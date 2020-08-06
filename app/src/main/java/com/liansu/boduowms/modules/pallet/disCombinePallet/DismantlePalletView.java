package com.liansu.boduowms.modules.pallet.disCombinePallet;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2019/11/14.
 */
public interface DismantlePalletView {

   void  requestBarcodeFocus();
   void  setCurrentBarcodeInfo(OutBarcodeInfo info);
   void  setPalletNo(String palletNo);
   void bindListView(List<OutBarcodeInfo> list);
   void onClear();
    int getCombinPalletType();
    void  showPalletScan(boolean isCheck);
    void   setSwitchButton(boolean isCheck);
}
