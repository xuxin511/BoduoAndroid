package com.liansu.boduowms.modules.inHouseStock.adjustStock;

import com.liansu.boduowms.bean.stock.StockInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/24.
 */
public interface IAdjustStockView {
    void onBarcodeFocus();

    void onBatchNoFocus();

    void onQtyFocus();

    void setStockInfo(StockInfo stockInfo);

    void onReset();

    void  onQCStatusSelect(List<String> QCStatusList);
}
