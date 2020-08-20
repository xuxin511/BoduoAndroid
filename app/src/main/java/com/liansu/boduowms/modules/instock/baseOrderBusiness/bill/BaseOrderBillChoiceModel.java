package com.liansu.boduowms.modules.instock.baseOrderBusiness.bill;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;

import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class BaseOrderBillChoiceModel extends BaseModel {
    public        String TAG_GetT_InStockList              = "ReceiptBillChoice_GetT_InStockList";
    public        String TAG_GetT_PalletDetailByBarCode    = "ReceiptBillChoice_GetT_PalletDetailByBarCode";
    public        String TAG_GetErpVoucherNo               = "ReceiptBillChoice_GetErpVoucherNo";
    private final int    RESULT_GetT_InStockList           = 101;
    private final int    RESULT_GetT_PalletDetailByBarCode = 102;
    private final int    RESULT_GetErpVoucherNo            = 103;
    ArrayList<OrderHeaderInfo> mOrderList    = new ArrayList<>();//单据信息
    ArrayList<OutBarcodeInfo>  mBarCodeInfos = new ArrayList<>();

    public BaseOrderBillChoiceModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    public void onHandleMessage(Message msg) {

    }






    /**
     * @desc: 临时存放采购订单列表数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 18:10
     */
    public void setOrderInfoList(List<OrderHeaderInfo> receiptList) {
        mOrderList.clear();
        if (receiptList!=null && receiptList.size()!=0){
            mOrderList.addAll(receiptList);
        }

    }

    public ArrayList<OrderHeaderInfo> getOrderHeaderInfotList() {
        return mOrderList;
    }

    public void setBarCodeList(ArrayList<OutBarcodeInfo> barCodeInfos) {
        mBarCodeInfos.clear();
        if (barCodeInfos!=null && barCodeInfos.size()!=0){
            mBarCodeInfos.addAll(barCodeInfos);
        }

    }

    public ArrayList<OutBarcodeInfo> getBarCodeList() {
        return mBarCodeInfos;
    }

   public void  onReset(){
       mBarCodeInfos.clear();
       mOrderList.clear();
   }
}
