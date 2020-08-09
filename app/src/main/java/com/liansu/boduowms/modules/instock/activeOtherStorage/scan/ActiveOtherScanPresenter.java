package com.liansu.boduowms.modules.instock.activeOtherStorage.scan;

import android.content.Context;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_NONE;

/**
 * @ Des: 有源杂入扫描
 * @ Created by yangyiqing on 2020/6/27.
 */
public class ActiveOtherScanPresenter extends BaseOrderScanPresenter<IBaseOrderScanView, ActiveOtherScanModel> {

    public ActiveOtherScanPresenter(Context context, IBaseOrderScanView view, MyHandler<BaseActivity> handler, OrderHeaderInfo orderHeaderInfo, List<OutBarcodeInfo> barCodeInfos) {
        super(context, view, handler, orderHeaderInfo, barCodeInfos, new ActiveOtherScanModel(context, handler));
        mView.setSecondLineInfo(null,null,false);
    }

    @Override
    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    @Override
    protected String getTitle() {
        return mContext.getResources().getString(R.string.appbar_title_active_other_bill_scan)+"-"+BaseApplication.mCurrentWareHouseInfo.getWarehousename();
    }


    /**
     * @desc: 获取有源杂入明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 22:33
     */
    @Override
    protected void getOrderDetailInfoList() {
        mModel.requestActiveOtherDetail(mModel.getOrderHeaderInfo(), new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_OTHER_DETAIL_LIST_ADF_ASYNC, result);
                try {
                    BaseResultInfo<OrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OrderHeaderInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        OrderHeaderInfo orderHeaderInfo = returnMsgModel.getData();
                        if (orderHeaderInfo != null) {
                            mModel.setOrderDetailList(orderHeaderInfo.getDetail());
                            if (mModel.getOrderDetailList().size() > 0) {
                                mView.bindListView(mModel.getOrderDetailList());
                                mView.onAreaNoFocus();
                            } else {
                                MessageBox.Show(mContext, "获取单据失败:获取的表体数据为空",MEDIA_MUSIC_NONE);
                            }
                        } else {
                            MessageBox.Show(mContext, "获取单据失败:获取的表体数据为空",MEDIA_MUSIC_NONE);
                        }
                    }else {
                        MessageBox.Show(mContext,"获取单据失败:"+returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext,"获取单据失败:出现预期之外的异常,"+ex.getMessage(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                }


            }
        });
    }

    /**
     * @desc: 实时扫描提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/29 14:08
     */
    @Override
    public void onCombinePalletRefer(OutBarcodeInfo outBarcodeInfo) {
        super.onCombinePalletRefer(outBarcodeInfo);
    }

    /**
     * @desc: 有源杂入过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 22:37
     */
    @Override
    protected void onOrderRefer() {
        OrderDetailInfo firstDetailInfo = mModel.getOrderDetailList().get(0);
        if (firstDetailInfo != null) {
            OrderDetailInfo postInfo = new OrderDetailInfo();
            postInfo.setErpvoucherno(firstDetailInfo.getErpvoucherno());
            postInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
            postInfo.setVouchertype(firstDetailInfo.getVouchertype());
            List<OrderDetailInfo> list = new ArrayList<>();
            list.add(postInfo);
            mModel.requestOrderRefer(list, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_POST_T_OTHER_DETAIL_ADF_ASYNC, result);
                    try {
                        BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                            MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE);
                        } else {
                            MessageBox.Show(mContext,"提交订单失败:"+ returnMsgModel.getResultValue(),MEDIA_MUSIC_NONE );
                        }

                    } catch (Exception ex) {
                        MessageBox.Show(mContext, "提交订单失败:出现预期之外的异常"+ex.getMessage(),MEDIA_MUSIC_NONE );
                    }


                }
            });
        }


    }


}
