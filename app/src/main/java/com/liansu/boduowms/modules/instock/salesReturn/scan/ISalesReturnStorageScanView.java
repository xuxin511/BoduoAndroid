package com.liansu.boduowms.modules.instock.salesReturn.scan;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/1.
 */
  public interface ISalesReturnStorageScanView {
      void onPalletNoFocus();
      void onAreaNoFocus();
      void bindListView(List<OutBarcodeInfo> list);
      void setPalletNoInfo(OutBarcodeInfo info);
      void onReset();
}
