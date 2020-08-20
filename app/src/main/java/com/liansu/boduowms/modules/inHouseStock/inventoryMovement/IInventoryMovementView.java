package com.liansu.boduowms.modules.inHouseStock.inventoryMovement;

import com.liansu.boduowms.bean.stock.StockInfo;

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

    void setBarcodeInfo(StockInfo stockInfo);
    String  getMoveInAreaNo();
    String  getMoveOutAreaNo();
    float getQty();

    void onClear();
}
