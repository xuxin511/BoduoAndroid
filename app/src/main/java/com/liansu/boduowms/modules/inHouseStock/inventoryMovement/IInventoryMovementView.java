package com.liansu.boduowms.modules.inHouseStock.inventoryMovement;

import com.liansu.boduowms.bean.stock.StockInfo;

import java.util.List;

/**
 * @desc: 移库
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/7/7 22:57
 */
public interface IInventoryMovementView {
    void requestBarcodeFocus();
    void requestMoveInAreaNoFocus();
    void requestMoveOutAreaNoFocus();
    void requestQtyFocus();
    String  getMoveInAreaNo();
    String  getMoveOutAreaNo();
    float getQty();
    void onClear();
    void bindListView(List<StockInfo> itemList);
}
