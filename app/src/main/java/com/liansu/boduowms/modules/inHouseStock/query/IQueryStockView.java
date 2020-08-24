package com.liansu.boduowms.modules.inHouseStock.query;

import com.liansu.boduowms.bean.stock.StockInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/23.
 */
public interface IQueryStockView {
     void bindListView(List<StockInfo> list);
     void onContentFocus();
     int getQueryType();
     void onReset();
}
