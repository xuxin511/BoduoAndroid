package com.liansu.boduowms.modules.instock.productionReturnsStorage.scan;

import android.content.Context;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScanPresenter;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.IBaseOrderScanView;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class ProductionReturnsStorageScanPresenter extends BaseOrderScanPresenter<IBaseOrderScanView, ProductionReturnsStorageScanModel> {

    public ProductionReturnsStorageScanPresenter(Context context, IBaseOrderScanView view, MyHandler<BaseActivity> handler, OrderHeaderInfo orderHeaderInfo, List<OutBarcodeInfo> barCodeInfos) {
        super(context, view, handler, orderHeaderInfo, barCodeInfos, new ProductionReturnsStorageScanModel(context, handler));
    }

    @Override
    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    @Override
    protected String getTitle() {
        return mContext.getResources().getString(R.string.appbar_title_production_returns_scan);
    }


    /**
     * @desc: 获取采购订单明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 22:33
     */
    @Override
    protected void getOrderDetailInfoList() {
        mModel.requestReceiptDetail(mModel.getOrderHeaderInfo(), new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GetT_PurchaseOrderListADFAsync, result);
                try {
                    BaseResultInfo<OrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<  BaseResultInfo<OrderHeaderInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult()==1) {
                        OrderHeaderInfo orderHeaderInfo=returnMsgModel.getData();
                        if (orderHeaderInfo!=null){
                            mModel.setOrderDetailList(orderHeaderInfo.getDetail());
                            if (mModel.getOrderDetailList().size() > 0) {
                                mView.bindListView(mModel.getOrderDetailList());
                            } else {
                                MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                            }
                        }else {
                            MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                        }
                        }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );
                }


            }
        });
    }

    @Override
    public void onCombinePalletRefer(OutBarcodeInfo outBarcodeInfo) {
        super.onCombinePalletRefer(outBarcodeInfo);
    }

    /**
     * @desc: 采购订单过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 22:37
     */
    @Override
    protected void onOrderRefer() {
        List<OrderDetailInfo>  list=mModel.getOrderDetailList();
        if (list!=null){
            for (int i=0;i<list.size();i++){
                OrderDetailInfo info=list.get(i);
                info.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
                info.setRownodel(i+"");
            }
        }
        mModel.requestOrderRefer(list, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_PostT_PurchaseDetailADFAsync, result);
                try {
                    BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<  BaseResultInfo<String>>() {
                    }.getType());
                    if (returnMsgModel.getResult()==1) {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                    }else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );
                }


            }
        });
    }


}
