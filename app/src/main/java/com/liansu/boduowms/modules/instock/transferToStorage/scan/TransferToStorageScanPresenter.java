package com.liansu.boduowms.modules.instock.transferToStorage.scan;

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
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScanPresenter;
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
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class TransferToStorageScanPresenter extends BaseOrderScanPresenter<TransferToStorageScanView, TransferToStorageScanModel> {

    public TransferToStorageScanPresenter(Context context, TransferToStorageScanView view, MyHandler<BaseActivity> handler, OrderHeaderInfo orderHeaderInfo, List<OutBarcodeInfo> barCodeInfos, int voucherType) {
        super(context, view, handler, orderHeaderInfo, barCodeInfos, new TransferToStorageScanModel(context, handler, voucherType));
    }

    @Override
    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    @Override
    protected String getTitle() {
        String title = "";
        if (mModel.getVoucherType() == OrderType.IN_STOCK_ORDER_TYPE_TWO_STAGE_TRANSFER_TO_STORAGE_VALUE) {
            title = mContext.getResources().getString(R.string.main_menu_item_two_stage_transfer_to_storage) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename();
        } else if (mModel.getVoucherType() == OrderType.IN_STOCK_ORDER_TYPE_ONE_STAGE_TRANSFER_TO_STORAGE_VALUE) {
            title = mContext.getResources().getString(R.string.main_menu_item_one_stage_transfer_to_storage) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename();
        }
        return title;
    }


    /**
     * @desc: 获取采购订单明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 22:33
     */

    protected void getOrderDetailInfoList(OrderRequestInfo orderHeaderInfo, int voucherType) {
        orderHeaderInfo.setVouchertype(voucherType);
        mModel.requestOrderDetail(orderHeaderInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_TRANSFER_IN_DETAIL_LIST_ADF_ASYNC, result);
                try {
                    BaseResultInfo<OrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OrderHeaderInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        OrderHeaderInfo orderHeaderInfo = returnMsgModel.getData();
                        if (orderHeaderInfo != null) {
                            mModel.setOrderHeaderInfo(orderHeaderInfo);
                            mModel.setOrderDetailList(orderHeaderInfo.getDetail());
                            if (mModel.getOrderDetailList().size() > 0) {
                                mView.bindListView(mModel.getOrderDetailList());
                                mView.onAreaNoFocus();
                            } else {
                                MessageBox.Show(mContext, "获取单据失败: " + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onErpVoucherNoFocus();
                                    }
                                });
                            }
                        } else {
                            MessageBox.Show(mContext, "获取单据失败: " + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onErpVoucherNoFocus();
                                }
                            });
                        }
                    } else {
                        MessageBox.Show(mContext, "获取单据失败: " + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onErpVoucherNoFocus();
                            }
                        });
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取单据失败:出现预期之外的异常, " + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onErpVoucherNoFocus();
                        }
                    });
                }


            }
        });
    }

    /**
     * @desc: 扫描外箱条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/14 17:39
     */
    @Override
    public void scanBarcode(String scanBarcode) {
        super.scanBarcode(scanBarcode);

    }

    @Override
    public void onCombinePalletRefer(final OutBarcodeInfo outBarcodeInfo) {
        super.onCombinePalletRefer(outBarcodeInfo);

    }


    /**
     * @desc: 产品过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/16 11:45
     */
    @Override
    protected void onOrderRefer() {
        OrderDetailInfo firstDetailInfo = mModel.getOrderDetailList().get(0);
        if (firstDetailInfo != null) {
            OrderDetailInfo postInfo = new OrderDetailInfo();
            postInfo.setErpvoucherno(firstDetailInfo.getErpvoucherno());
            postInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
            postInfo.setUsername(BaseApplication.mCurrentUserInfo.getUsername());
            postInfo.setVouchertype(firstDetailInfo.getVouchertype());
            List<OrderDetailInfo> list = new ArrayList<>();
            list.add(postInfo);
            mModel.requestOrderRefer(list, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    LogUtil.WriteLog(TransferToStorageScan.class, mModel.TAG_POST_T_TRANSFER_IN_DETAIL_ADF_ASYNC, result);
                    try {
                        BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
//                        BaseResultInfo<OrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OrderHeaderInfo>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                            MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                        } else {
                            MessageBox.Show(mContext, "提交单据信息失败:" + returnMsgModel.getResultValue());
                        }

                    } catch (Exception ex) {
                        MessageBox.Show(mContext, "提交单据信息失败:出现预期之外的异常," + ex.getMessage());
                    }


                }
            });
        }
    }

    /**
     * @desc: 调拨接口
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/16 16:11
     */
    public void onTransferSubmissionRefer() {

    }


}
