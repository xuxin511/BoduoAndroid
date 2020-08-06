package com.liansu.boduowms.modules.instock.transferToStorage.scan;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScanPresenter;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.IBaseOrderScanView;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.List;

/**
 * @desc: 调拨入库扫描
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/7/13 23:50
 */
public class TransferToStorageScanPresenter extends BaseOrderScanPresenter<IBaseOrderScanView, TransferToStorageScanModel> {

    public TransferToStorageScanPresenter(Context context, IBaseOrderScanView view, MyHandler<BaseActivity> handler, OrderHeaderInfo orderHeaderInfo, List<OutBarcodeInfo> barCodeInfos) {
        super(context, view, handler, orderHeaderInfo, barCodeInfos, new TransferToStorageScanModel(context, handler));
    }

    @Override
    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    @Override
    protected String getTitle() {
        return mContext.getResources().getString(R.string.appbar_title_transfer_to_storage_scan);
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
//        mModel.requestReceiptDetail(mModel.getOrderHeaderInfo(), new NetCallBackListener<String>() {
//            @Override
//            public void onCallBack(String result) {
//                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GetT_InStockDetailListByHeaderIDADF, result);
//                try {
//                    ReturnMsgModelList<OrderDetailInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<ReceiptDetail_Model>>() {
//                    }.getType());
//                    if (returnMsgModel.getHeaderStatus().equals("S")) {
//                        mModel.setOrderDetailList(returnMsgModel.getModelJson());
//                        if (mModel.getOrderDetailList().size() > 0) {
//                            mView.bindListView(mModel.getOrderDetailList());
//                        } else {
//                            MessageBox.Show(mContext, returnMsgModel.getMessage());
//                        }
//                    }
//                } catch (Exception ex) {
//                    MessageBox.Show(mContext, ex.getMessage());
//                }
//
//
//            }
//        });
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

    }


}
