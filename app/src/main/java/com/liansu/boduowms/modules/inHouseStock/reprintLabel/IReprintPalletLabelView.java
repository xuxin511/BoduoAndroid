package com.liansu.boduowms.modules.inHouseStock.reprintLabel;

import com.liansu.boduowms.bean.stock.StockInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/23.
 */
public interface IReprintPalletLabelView {
     void bindListView(List<StockInfo> list);
     void onContentFocus();
     int getQueryType();
     void onReset();
}
