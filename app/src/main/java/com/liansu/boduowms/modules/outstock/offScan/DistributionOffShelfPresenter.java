package com.liansu.boduowms.modules.outstock.offScan;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.BaseOutStockBusinessPresenter;
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
public class DistributionOffShelfPresenter extends BaseOutStockBusinessPresenter<IDistributionOffShelfView, DistributionOffShelfModel> {


    public DistributionOffShelfPresenter(Context context, IDistributionOffShelfView view, MyHandler<BaseActivity> handler) {
        super(context, view, handler, new DistributionOffShelfModel(context, handler));
    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    public void onScan(String fatherBarcode, String subBarcode, int scanType) {
        super.onScan(fatherBarcode, subBarcode, scanType);
    }

    @Override
    public void getOrderDetailInfoList(String erpVoucherNo) {
        OrderRequestInfo info=new OrderRequestInfo();
        info.setErpvoucherno(erpVoucherNo);
        info.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        mModel.requestOrderDetail(info, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(DistributionOffShelf.class, mModel.TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, result);
                try {
                    BaseResultInfo<OutStockOrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderHeaderInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        OutStockOrderHeaderInfo orderHeaderInfo = returnMsgModel.getData();
                        if (orderHeaderInfo != null) {
                            mModel.setOrderHeaderInfo(orderHeaderInfo);
                            mModel.setOrderDetailList(orderHeaderInfo.getDetail());
                            if (mModel.getOrderDetailList().size() > 0) {
                                mView.bindListView(mModel.getOrderDetailList());
                                mView.onFatherBarcodeFocus();
                            } else {
                                MessageBox.Show(mContext, "获取表体信息为空" );
                                mView.onErpVoucherNoFocus();
                            }
                        } else {
                            MessageBox.Show(mContext, "获取订单明细失败: "+returnMsgModel.getResultValue() );
                            mView.onErpVoucherNoFocus();
                        }
                    } else {
                        MessageBox.Show(mContext,"获取订单明细失败: "+ returnMsgModel.getResultValue() );
                        mView.onErpVoucherNoFocus();
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取订单明细失败: "+ex.getMessage() );
                    mView.onErpVoucherNoFocus();
                }


            }
        });


    }

    /**
     * @desc: 设置月台信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/28 14:26
     */
    protected void setBalconyInfo(String orderNo, String balconyNo) {

    }


}
