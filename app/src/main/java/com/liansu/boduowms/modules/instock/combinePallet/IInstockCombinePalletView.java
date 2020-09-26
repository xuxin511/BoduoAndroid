package com.liansu.boduowms.modules.instock.combinePallet;

import com.liansu.boduowms.bean.stock.StockInfo;

import java.util.List;

/**
 * @ Des:  入库组托
 * @ Created by yangyiqing on 2019/11/14.
 */
public interface IInstockCombinePalletView {

    void requestPalletOneFocus();
    void requestPalletTwoFocus();

    void requestPalletFocus(int palletType);
    void bindListView(List<StockInfo> list);

    void onReset();

    int getCombinePalletType();
    void initViewStatus(int printType);


}
