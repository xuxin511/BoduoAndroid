package com.liansu.boduowms.modules.outstock.baseOutStockBusiness.baseReviewScan;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.utils.hander.MyHandler;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/28.
 */
public class BaseReviewScanPresenter<T extends IBaseReviewScanView, V extends BaseReviewScanModel> {
    protected Context            mContext;
    protected V                  mModel;
    protected T                  mView;
    protected PrintBusinessModel mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public BaseReviewScanPresenter(Context context, T view, MyHandler<BaseActivity> handler, V model) {
        this.mContext = context;
        this.mView = view;
        this.mModel = model;
        this.mPrintModel = new PrintBusinessModel(context, handler);

    }

    public BaseReviewScanModel getModel() {
        return mModel;
    }


    /**
     * @desc: 获取订单表体明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/27 18:18
     */
    protected void getOrderDetailInfoList(String erpVoucherNo) {

    }

    /**
     * @desc: 扫描条码  外箱码，69码，物料编码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/5 6:29
     */
    protected void onScan(String barcode) {
    }

    /**
     * @desc: 扫描外箱条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/5 6:33
     */
    protected void onOuterBarcodeScan(OutBarcodeInfo outBarcodeInfo) {

    }

    /**
     * @desc: 散件条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/5 6:34
     */
    protected void onSparePartsBarcodeScan(OutBarcodeInfo outBarcodeInfo) {

    }

    /**
     * @desc: 托盘条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/5 9:42
     */
    protected void onPalletNoBarcodeScan(OutBarcodeInfo outBarcodeInfo) {
    }

    /**
     * @desc: 条码复核提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/5 7:17
     */
    protected void onBarcodeRefer(OutStockOrderDetailInfo outBarcodeInfo) {
    }

    /**
     * @desc: 单据过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/5 7:30
     */
    protected void onOrderRefer() {
    }

    protected void  onResume(){}
}
