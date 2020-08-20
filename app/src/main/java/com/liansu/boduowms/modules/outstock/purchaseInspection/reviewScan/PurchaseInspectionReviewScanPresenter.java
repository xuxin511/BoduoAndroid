package com.liansu.boduowms.modules.outstock.purchaseInspection.reviewScan;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.baseReviewScan.BaseReviewScanPresenter;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.baseReviewScan.IBaseReviewScanView;
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
 * @ Des:
 * @ Created by yangyiqing on 2020/6/28.
 */
public class PurchaseInspectionReviewScanPresenter extends BaseReviewScanPresenter<IBaseReviewScanView, PurchaseInspectionReviewScanModel> {


    public PurchaseInspectionReviewScanPresenter(Context context, IBaseReviewScanView view, MyHandler<BaseActivity> handler) {
        super(context, view, handler, new PurchaseInspectionReviewScanModel(context, handler));
    }

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }


    public PurchaseInspectionReviewScanModel getModel() {
        return mModel;
    }


    /**
     * @desc: 获取订单表体明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/27 18:18
     */
    @Override
    protected void getOrderDetailInfoList(String erpVoucherNo) {
        OrderRequestInfo info = new OrderRequestInfo();
        info.setErpvoucherno(erpVoucherNo);
        info.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        mModel.requestOrderDetail(info, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_CHECK_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, result);
                try {
                    BaseResultInfo<OutStockOrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderHeaderInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        OutStockOrderHeaderInfo orderHeaderInfo = returnMsgModel.getData();
                        if (orderHeaderInfo != null) {
                            mModel.setOrderHeaderInfo(orderHeaderInfo);
                            mModel.setOrderDetailList(orderHeaderInfo.getDetail());
                            if (mModel.getOrderDetailList().size() > 0) {
                                BaseMultiResultInfo<Boolean, Void> checkResult = mModel.isOrderScanFinished();
                                if (!checkResult.getHeaderStatus()) {
                                    mView.bindListView(mModel.getOrderDetailList());
                                    mView.setErpVoucherNoInfo(mModel.getOrderHeaderInfo());
                                    mView.setSumScanQty(mModel.getOrderDetailList().get(0).getMaterialCartonNum(), mModel.getOrderDetailList().get(0).getMaterialPartNum());

                                } else {
                                    mView.onActivityFinish("单号已扫描完毕," + checkResult.getMessage());
                                }
                            } else {
                                MessageBox.Show(mContext, "获取单据明细失败::获取单据明细为空" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onErpVoucherFocus();
                                    }
                                });

                            }
                        } else {
                            MessageBox.Show(mContext, "获取单据明细失败:获取单据明细为空" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onErpVoucherFocus();
                                }
                            });
                        }
                    } else {
                        MessageBox.Show(mContext, "获取单据明细失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onErpVoucherFocus();
                            }
                        });
                    }


                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取单据明细出现预期之外的异常:" + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onErpVoucherFocus();
                        }
                    });
                }


            }
        });
    }


    @Override
    protected void onScan(String barcode) {
        try {
            OutBarcodeInfo scanQRCode = null;
            if (barcode.equals("")) return;
            BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(barcode);
            if (resultInfo.getHeaderStatus()) {
                scanQRCode = resultInfo.getInfo();
            } else {
                MessageBox.Show(mContext, "解析条码失败:" + resultInfo.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                    }
                });

                return;
            }
            String materialNo = "";
            if (scanQRCode != null) {  //查询物料条码是否存在
                if (scanQRCode.getBarcode().contains("%") && scanQRCode.getSerialno() == null) {  //外箱
                    materialNo = scanQRCode.getMaterialno();
                } else if (!scanQRCode.getBarcode().contains("%")) {  //物料
                    materialNo = scanQRCode.getBarcode();
                } else if (scanQRCode.getBarcode().contains("%") && scanQRCode.getSerialno() != null) {
                    materialNo = scanQRCode.getMaterialno();
                }
                final OutBarcodeInfo finalScanQRCode = scanQRCode;
                mModel.requestBarcodeInfoQuery(materialNo, new NetCallBackListener<String>() {
                    @Override
                    public void onCallBack(String result) {
                        try {
                            LogUtil.WriteLog(PurchaseInspectionReviewScan.class, mModel.TAG_SELECT_MATERIAL, result);
                            BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutBarcodeInfo>>() {
                            }.getType());
                            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                if (finalScanQRCode.getBarcode().contains("%")) {
                                    mView.setQtyViewStatus(false);
                                    if (finalScanQRCode.getBarcodetype() == QRCodeFunc.BARCODE_TYPE_PALLET_NO) {
                                        onPalletNoBarcodeScan(finalScanQRCode);
                                    } else if (finalScanQRCode.getBarcodetype() == QRCodeFunc.BARCODE_TYPE_OUTER_BOX) {
                                        onOuterBarcodeScan(finalScanQRCode);
                                    }

                                } else {
                                    OutBarcodeInfo info = returnMsgModel.getData();
                                    if (info != null) {
                                        finalScanQRCode.setMaterialno(info.getMaterialno());
                                        mView.setQtyViewStatus(true);
                                        mView.onQtyFocus();
                                    }

                                }
                            } else {
                                MessageBox.Show(mContext, "查询物料信息失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onBarcodeFocus();
                                    }
                                });

                            }

                        } catch (Exception e) {
                            MessageBox.Show(mContext, "出现意料之外的异常:" + e.getMessage());
                            mView.onBarcodeFocus();
                        } finally {

                        }
                    }
                });
            }

        } catch (Exception e) {
            MessageBox.Show(mContext, "扫描条码出现预期之外的异常:" + e.getMessage());
        }
    }


    @Override
    protected void onOuterBarcodeScan(OutBarcodeInfo outBarcodeInfo) {
        OutStockOrderDetailInfo info = new OutStockOrderDetailInfo();
        info.setMaterialno(outBarcodeInfo.getBarcode());
        info.setErpvoucherno(mModel.getOrderHeaderInfo().getErpvoucherno());
        info.setVouchertype(mModel.getOrderHeaderInfo().getVouchertype());
        info.setScanqty(outBarcodeInfo.getQty());
        info.setPostUserNo(BaseApplication.mCurrentUserInfo.getUserno());
        info.setStrongholdcode(mModel.getOrderHeaderInfo().getStrongholdcode());
        onBarcodeRefer(info);
    }


    @Override
    protected void onSparePartsBarcodeScan(OutBarcodeInfo outBarcodeInfo) {
        OutStockOrderDetailInfo info = new OutStockOrderDetailInfo();
        info.setMaterialno(outBarcodeInfo.getBarcode());
        info.setErpvoucherno(mModel.getOrderHeaderInfo().getErpvoucherno());
        info.setVouchertype(mModel.getOrderHeaderInfo().getVouchertype());
        info.setScanqty(outBarcodeInfo.getQty());
        info.setPostUserNo(BaseApplication.mCurrentUserInfo.getUserno());
        info.setStrongholdcode(mModel.getOrderHeaderInfo().getStrongholdcode());
        onBarcodeRefer(info);
    }

    @Override
    protected void onPalletNoBarcodeScan(OutBarcodeInfo outBarcodeInfo) {
        OutStockOrderDetailInfo info = new OutStockOrderDetailInfo();
        info.setMaterialno(outBarcodeInfo.getBarcode());
        info.setErpvoucherno(mModel.getOrderHeaderInfo().getErpvoucherno());
        info.setVouchertype(mModel.getOrderHeaderInfo().getVouchertype());
        info.setScanqty(outBarcodeInfo.getQty());
        info.setPostUserNo(BaseApplication.mCurrentUserInfo.getUserno());
        info.setStrongholdcode(mModel.getOrderHeaderInfo().getStrongholdcode());
        onBarcodeRefer(info);
    }

    @Override
    protected void onBarcodeRefer(OutStockOrderDetailInfo outBarcodeInfo) {
        mModel.requestBarcodeInfoRefer(outBarcodeInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(PurchaseInspectionReviewScan.class, mModel.TAG_SUBMIT_REVIEW_SCAN_ADF_ASYNC, result);
                    BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> checkResult = mModel.checkAndUpdateMaterialInfo(returnMsgModel.getData().get(0), true);
                        if (checkResult.getHeaderStatus()) {
                            OutStockOrderDetailInfo info = checkResult.getInfo();
                            if (info != null) {
                                mView.bindListView(mModel.getOrderDetailList());
                                mView.setSumScanQty(info.getMaterialCartonNum(), info.getMaterialPartNum());

                            }
                        } else
                            MessageBox.Show(mContext, returnMsgModel.getResultValue(), 1);
                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), 1);

                    }

                } catch (Exception e) {
                    MessageBox.Show(mContext, "出现意料之外的异常:" + e.getMessage());
                } finally {
                    mView.onBarcodeFocus();
                }
            }


        });
    }


    @Override
    protected void onOrderRefer() {
        if (mModel.getOrderHeaderInfo() == null) {
            MessageBox.Show(mContext, "获取的订单信息为空,请先扫描单据号", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onErpVoucherFocus();
                }
            });
            return;
        }
        OrderRequestInfo info = new OrderRequestInfo();
        info.setErpvoucherno(mModel.getOrderHeaderInfo().getErpvoucherno());
        info.setVouchertype(mModel.getOrderHeaderInfo().getVouchertype());
        info.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
        info.setDirver(mView.getDriverInfo());
        info.setLogisticsCompany(mView.getLogisticsCompany());
        List<OrderRequestInfo> list = new ArrayList<>();
        list.add(info);
        mModel.requestOrderRefer(list, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(PurchaseInspectionReviewScan.class, mModel.TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC, result);
                    BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onReset();
                            }
                        });
                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue());
                    }
                } catch (Exception e) {
                    MessageBox.Show(mContext, "出现意料之外的异常:" + e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        mView.setReceiverStatus(false);
        mView.setLogisticsCompanyStatus(true);
    }

    /**
     * @desc: 重置数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/12 15:25
     */
    void onReset() {
        mView.onReset();
        mModel.onReset();
    }

}
