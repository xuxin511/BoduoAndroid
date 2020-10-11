package com.liansu.boduowms.modules.instock.replenishment;

import com.liansu.boduowms.bean.stock.StockInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/10/10.
 */
 public interface IInStockHouseReplenishmentView {
     void  onOutPalletNoFocus();
     void  onOutPalletQtyFocus();
     void  onInPalletNoFocus();
     void  onInPalletAreaNoFocus();
     void  setInPalletAreaNoEnable(boolean enable);
     void  setInPalletAreaNo(String areaNo);
     void  onReset();
    void bindListView(List<StockInfo> list);
    void setOutPalletQty(float qty);
    List<StockInfo>  getSelectedMaterialItems();
    float getInPalletQty();

}
