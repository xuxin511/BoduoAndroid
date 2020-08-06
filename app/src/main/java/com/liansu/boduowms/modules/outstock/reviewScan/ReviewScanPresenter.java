package com.liansu.boduowms.modules.outstock.reviewScan;

import android.content.Context;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/28.
 */
public class ReviewScanPresenter {
    protected Context            mContext;
    protected ReviewScanModel   mModel;
    protected IReviewScanView   mView;
    protected PrintBusinessModel mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public ReviewScanPresenter(Context context, IReviewScanView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new ReviewScanModel(context, handler);
        this.mPrintModel = new PrintBusinessModel(context, handler);

    }

    public ReviewScanModel getModel() {
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
        OrderRequestInfo info = new OrderRequestInfo();
        mModel.requestOrderDetail(info, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_CHECK_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, result);
                try {
                    BaseResultInfo<OutStockOrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderHeaderInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
//                        OrderHeaderInfo orderHeaderInfo = returnMsgModel.getData();
//                        if (orderHeaderInfo != null) {
//                            mModel.setOrderDetailList(orderHeaderInfo.getDetail());
//                            if (mModel.getOrderDetailList().size() > 0) {
//                                mView.bindListView(mModel.getOrderDetailList());
//                            } else {
//                                MessageBox.Show(mContext, returnMsgModel.getResultValue());
//                            }
//                        } else {
//                            MessageBox.Show(mContext, returnMsgModel.getResultValue() );
//                        }
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );
                }


            }
        });
    }

}
