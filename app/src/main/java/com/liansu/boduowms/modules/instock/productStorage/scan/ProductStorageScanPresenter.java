package com.liansu.boduowms.modules.instock.productStorage.scan;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
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
public class ProductStorageScanPresenter extends BaseOrderScanPresenter<IProductStoragerScanView, ProductStorageScanModel> {

    public ProductStorageScanPresenter(Context context, IProductStoragerScanView view, MyHandler<BaseActivity> handler, OrderHeaderInfo orderHeaderInfo, List<OutBarcodeInfo> barCodeInfos) {
        super(context, view, handler, orderHeaderInfo, barCodeInfos, new ProductStorageScanModel(context, handler));
    }

    @Override
    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    @Override
    protected String getTitle() {
        return mContext.getResources().getString(R.string.appbar_title_product_storage_bill);
    }


    /**
     * @desc: 获取采购订单明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 22:33
     */

    protected void getOrderDetailInfoList(OrderHeaderInfo orderHeaderInfo) {
        mModel.requestOrderDetail(orderHeaderInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC, result);
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
                                MessageBox.Show(mContext, "获取单据失败: " + returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onErpVoucherNoFocus();
                                    }
                                });
                            }
                        } else {
                            MessageBox.Show(mContext, "获取单据失败: " + returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onErpVoucherNoFocus();
                                }
                            });
                        }
                    } else {
                        MessageBox.Show(mContext, "获取单据失败: " + returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onErpVoucherNoFocus();
                            }
                        });
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取单据失败:出现预期之外的异常, " + ex.getMessage(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
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
        try {
            if (!mPrintModel.checkBluetoothSetting()) {
                return;
            }
            OutBarcodeInfo scanQRCode = null;
            if (scanBarcode.equals("")) return;
            if (scanBarcode.contains("%")) {
                BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(scanBarcode);
                if (resultInfo.getHeaderStatus()) {
                    scanQRCode = resultInfo.getInfo();
                } else {
                    MessageBox.Show(mContext, resultInfo.getMessage(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onBarcodeFocus();
                        }
                    });
                    return;
                }

            }
            if (scanQRCode != null) {
                mModel.requestBarcodeInfo(scanQRCode, new NetCallBackListener<String>() {
                    @Override
                    public void onCallBack(String result) {
                        LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, result);
                        try {
                            BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutBarcodeInfo>>() {
                            }.getType());
                            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                OutBarcodeInfo outBarcodeInfo = returnMsgModel.getData();
                                if (outBarcodeInfo != null) {
                                    onCombinePalletRefer(outBarcodeInfo);
                                } else {
                                    MessageBox.Show(mContext, "查询托盘码失败:获取的托盘数据为空," + returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.onBarcodeFocus();
                                        }
                                    });
                                }
                            } else {
                                MessageBox.Show(mContext, "查询托盘码失败:" + returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onBarcodeFocus();
                                    }
                                });
                            }

                        } catch (Exception ex) {
                            MessageBox.Show(mContext, "查询托盘码失败，出现预期之外的异常-" + ex.getMessage() + ",", MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onBarcodeFocus();
                                }
                            });
                        }
                    }
                });

            } else {
                MessageBox.Show(mContext, "解析条码失败，条码格式不正确" + scanBarcode, MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                    }
                });
                return;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "查询条码失败，出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onBarcodeFocus();
                }
            });
            return;
        }

    }

    @Override
    public void onCombinePalletRefer(final OutBarcodeInfo outBarcodeInfo) {
        outBarcodeInfo.setAreano(mView.getAreaNo());
        outBarcodeInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
        outBarcodeInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        if (outBarcodeInfo != null) {
            BaseMultiResultInfo<Boolean, OrderDetailInfo> detailResult = mModel.findMaterialInfo(outBarcodeInfo); //找到物料行
            if (detailResult.getHeaderStatus()) {
                final OrderDetailInfo orderDetailInfo = detailResult.getInfo();
                BaseMultiResultInfo<Boolean, Void> checkMaterialResult = mModel.checkMaterialInfo(orderDetailInfo, outBarcodeInfo, false); //校验条码是否匹配物料
                if (checkMaterialResult.getHeaderStatus()) {
                    orderDetailInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
                    orderDetailInfo.setUsername(BaseApplication.mCurrentUserInfo.getUsername());
                    mModel.requestCombineAndReferPallet(orderDetailInfo, new NetCallBackListener<String>() {
                        @Override
                        public void onCallBack(String result) {
                            try {
                                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_COMBINE_AND_REFER_PALLET_SUB, result);
                                BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                                }.getType());
                                if (returnMsgModel.getResult() == 1) {
                                    String barcode = returnMsgModel.getData();
                                    outBarcodeInfo.setBarcode(barcode);
                                    mModel.checkMaterialInfo(orderDetailInfo, outBarcodeInfo, true);
                                    mView.bindListView(mModel.getOrderDetailList());
                                    mView.onBarcodeFocus();
                                } else {
                                    MessageBox.Show(mContext, "提交条码信息失败:" + returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.onBarcodeFocus();
                                        }
                                    });

                                }
                            } catch (Exception e) {
                                MessageBox.Show(mContext, "提交条码信息失败:出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onBarcodeFocus();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    MessageBox.Show(mContext, checkMaterialResult.getMessage(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onBarcodeFocus();
                        }
                    });
                }
            } else {
                MessageBox.Show(mContext, detailResult.getMessage(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                    }
                });
            }
        } else {
            MessageBox.Show(mContext, "解析的外箱信息不能为空", MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onBarcodeFocus();
                }
            });
        }

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
            postInfo.setVouchertype(firstDetailInfo.getVouchertype());
            List<OrderDetailInfo> list = new ArrayList<>();
            list.add(postInfo);
            mModel.requestOrderRefer(list, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    LogUtil.WriteLog(ProductStorageScan.class, mModel.TAG_POST_T_WORK_ORDER_DETAIL_ADF_ASYNC, result);
                    try {
                        BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
//                        BaseResultInfo<OrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OrderHeaderInfo>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                            MessageBox.Show(mContext, returnMsgModel.getResultValue(), 1, new DialogInterface.OnClickListener() {
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
