package com.liansu.boduowms.modules.instock.productionReturnsStorage.print;

import android.content.Context;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/17.
 */
public class ProductionReturnsPresenter {
    protected Context                       mContext;
    protected ProductionReturnsModel        mModel;
    protected IProductionReturnsStorageView mView;
    protected PrintBusinessModel            mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public ProductionReturnsPresenter(Context context, IProductionReturnsStorageView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new ProductionReturnsModel(context, handler);
        this.mPrintModel = new PrintBusinessModel(context, handler);
    }

    public ProductionReturnsModel getModel() {
        return mModel;
    }


    /**
     * @desc: 获取订单明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 22:33
     */

    protected void getOrderDetailInfoList(String erpVoucherNo) {
        OrderRequestInfo info = new OrderRequestInfo();
        info.setErpvoucherno(erpVoucherNo);
        mModel.requestOrderDetail(info, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
//                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GetT_PurchaseOrderListADFAsync, result);
                try {
                    BaseResultInfo<OrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OrderHeaderInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        OrderHeaderInfo orderHeaderInfo = returnMsgModel.getData();
                        if (orderHeaderInfo != null) {
                            mModel.setOrderDetailsList(orderHeaderInfo.getDetail());
                            if (mModel.getOrderDetailsList().size() > 0) {
                                mView.bindListView(mModel.getOrderDetailsList());
                            } else {
                                mView.onErpVoucherNoFocus();
                                MessageBox.Show(mContext, "获取订单失败:获取表体信息为空");

                            }
                        } else {
                            mView.onErpVoucherNoFocus();
                            MessageBox.Show(mContext, "获取订单失败:获取表体信息为空" + returnMsgModel.getResultValue());

                        }
                    } else {
                        mView.onErpVoucherNoFocus();
                        MessageBox.Show(mContext, "获取订单失败:" + returnMsgModel.getResultValue());

                    }

                } catch (Exception ex) {
                    mView.onErpVoucherNoFocus();
                    MessageBox.Show(mContext, ex.getMessage());

                }


            }
        });
    }

    /**
     * @desc: 打印
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/17 7:30
     */
    public void onPrint(final OutBarcodeInfo outBarcodeInfo) {
        if (mPrintModel.checkBluetoothSetting()){
            PrintInfo printInfo = mModel.getPrintModel(outBarcodeInfo);
            if (printInfo != null) {
                mPrintModel.onPrint(printInfo);
            }
        }


    }

}
