package com.liansu.boduowms.modules.instock.activeOtherStorage.scan;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OrderType;
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
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_NONE;

/**
 * @ Des: 有源杂入扫描
 * @ Created by yangyiqing on 2020/6/27.
 */
public class ActiveOtherScanPresenter extends BaseOrderScanPresenter<IBaseOrderScanView, ActiveOtherScanModel> {

    public ActiveOtherScanPresenter(Context context, IBaseOrderScanView view, MyHandler<BaseActivity> handler, OrderHeaderInfo orderHeaderInfo, List<OutBarcodeInfo> barCodeInfos) {
        super(context, view, handler, orderHeaderInfo, barCodeInfos, new ActiveOtherScanModel(context, handler));
        mView.setSecondLineInfo(null, null, false);
    }

    @Override
    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    @Override
    protected String getTitle() {
        return mContext.getResources().getString(R.string.appbar_title_active_other_bill_scan) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
    }


    @Override
    public void getAreaInfo(String areaNo) {
        super.getAreaInfo(areaNo);
    }

    @Override
    public void scanBarcode(String scanBarcode) {
        super.scanBarcode(scanBarcode);
    }

    @Override
    protected void getOrderDetailInfoList(String erpVoucherNo) {
        onReset();
        OrderRequestInfo postInfo = new OrderRequestInfo();
        postInfo.setErpvoucherno(erpVoucherNo);
        postInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        postInfo.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE_VALUE);
        mModel.setOrderRequestInfo(postInfo);
        mModel.requestActiveOtherDetail(mModel.getOrderRequestInfo(), new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_OTHER_DETAIL_LIST_ADF_ASYNC, result);
                try {
                    BaseResultInfo<OrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OrderHeaderInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        OrderHeaderInfo orderHeaderInfo = returnMsgModel.getData();
                        if (orderHeaderInfo != null) {
                            mModel.setOrderHeaderInfo(orderHeaderInfo);
                            mModel.setOrderDetailList(orderHeaderInfo.getDetail());
                            if (mModel.getOrderDetailList().size() > 0) {
                                mView.setOrderHeaderInfo(orderHeaderInfo);
                                mView.bindListView(mModel.getOrderDetailList());
                                mView.onAreaNoFocus();
                            } else {
                                MessageBox.Show(mContext, "获取单据失败:获取的表体数据为空", MEDIA_MUSIC_ERROR);
                            }
                        } else {
                            MessageBox.Show(mContext, "获取单据失败:获取的表体数据为空", MEDIA_MUSIC_ERROR);
                        }
                    } else {
                        MessageBox.Show(mContext, "获取单据失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取单据失败:出现预期之外的异常," + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
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
        if (mModel.getOrderDetailList() == null) {
            MessageBox.Show(mContext, "校验单据信息失败:单据信息为空,请先扫描单据", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onErpVoucherNoFocus();
                }
            });
            return;
        }
        OrderDetailInfo firstDetailInfo = mModel.getOrderDetailList().get(0);
        if (firstDetailInfo != null) {
            OrderDetailInfo postInfo = new OrderDetailInfo();
            postInfo.setErpvoucherno(firstDetailInfo.getErpvoucherno());
            postInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
            postInfo.setVouchertype(firstDetailInfo.getVouchertype());
            postInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
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
                            BaseMultiResultInfo<Boolean, Void> checkResult = mModel.isOrderScanFinished();
                            if (!checkResult.getHeaderStatus()) {
                                MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getOrderDetailInfoList(mView.getErpVoucherNo());
                                    }
                                });
                            } else {
                                MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onReset();
                                    }
                                });

//                                mView.onActivityFinish(checkResult.getMessage());
                            }
                        } else {
                            MessageBox.Show(mContext, "提交订单失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR);
                        }

                    } catch (Exception ex) {
                        MessageBox.Show(mContext, "提交订单失败:出现预期之外的异常" + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR);
                    }


                }
            });
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mView.onErpVoucherNoFocus();
        final String erpVoucherNo = mView.getErpVoucherNo();
        if (erpVoucherNo != null && !erpVoucherNo.equals("")) {
            getOrderDetailInfoList(erpVoucherNo);
        }
    }
}
