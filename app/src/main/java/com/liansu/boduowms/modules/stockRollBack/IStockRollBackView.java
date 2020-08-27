package com.liansu.boduowms.modules.stockRollBack;

import com.liansu.boduowms.bean.stock.VoucherDetailSubInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/26.
 */
 public interface IStockRollBackView {
  void onBarcodeFocus();
  void setErpVoucherNo(String erpVoucherNo);
  void setTitle(String title);
  void bindRecycleView(List<VoucherDetailSubInfo> list);

}
