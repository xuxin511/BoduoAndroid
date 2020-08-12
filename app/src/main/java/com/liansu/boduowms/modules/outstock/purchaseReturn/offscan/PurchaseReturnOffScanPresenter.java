package com.liansu.boduowms.modules.outstock.purchaseReturn.offscan;

import android.content.Context;
import android.content.DialogInterface;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.BaseOutStockBusinessPresenter;
import com.liansu.boduowms.modules.outstock.offScan.DistributionOffShelf;
import com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan.PurchaseInspectionProcessingScan;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/6.
 */
public class PurchaseReturnOffScanPresenter extends BaseOutStockBusinessPresenter<IPurchaseReturnOffScanView, PurchaseReturnOffScanModel> {
    public PurchaseReturnOffScanPresenter(Context context, IPurchaseReturnOffScanView view, MyHandler handler) {
        super(context, view, handler, new PurchaseReturnOffScanModel(context, handler));
    }


    /**
     * @desc: 获取仓退明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/6 15:40
     */
    @Override
    public void getOrderDetailInfoList(String erpVoucherNo) {
        OrderRequestInfo info = new OrderRequestInfo();
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
                            mView.setOrderHeaderInfo(orderHeaderInfo);
                            if (mModel.getOrderDetailList().size() > 0) {
                                mView.bindListView(mModel.getOrderDetailList());
                                mView.onFatherBarcodeFocus();
                            } else {
                                MessageBox.Show(mContext, "获取订单明细失败:获取表体信息为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onErpVoucherNoFocus();
                                    }
                                });
                            }
                        } else {
                            MessageBox.Show(mContext, "获取订单明细失败: " + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onErpVoucherNoFocus();
                                }
                            });
                        }
                    } else {
                        MessageBox.Show(mContext, "获取订单明细失败: " + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onErpVoucherNoFocus();
                            }
                        });
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取订单明细失败: " + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onErpVoucherNoFocus();
                        }
                    });
                }


            }
        });

    }


    @Override
    public void onPalletScan(String palletBarcode, int scanType) {
        super.onPalletScan(palletBarcode, scanType);
    }

    @Override
    public void onOuterBoxScan(String palletBarcode, String outerBoxBarcode, int scanType) {
        super.onOuterBoxScan(palletBarcode, outerBoxBarcode, scanType);
    }

    @Override
    public void onSparePartsScan(String palletBarcode, String outerBoxBarcode, int scanType) {
        super.onSparePartsScan(palletBarcode, outerBoxBarcode, scanType);
    }

    @Override
    protected void onCombineTrayScan(OutBarcodeInfo palletBarcode, int scanType) {
        super.onCombineTrayScan(palletBarcode, scanType);
    }

    @Override
    public void onPalletInfoRefer(OutBarcodeInfo fatherBarcode) {
        super.onPalletInfoRefer(fatherBarcode);
    }

    @Override
    public void onOuterBoxInfoRefer(OutBarcodeInfo fatherBarcodeInfo, OutBarcodeInfo outerBoxBarcodeInfo) {
        super.onOuterBoxInfoRefer(fatherBarcodeInfo, outerBoxBarcodeInfo);
    }

    @Override
    public void onSparePartsInfoRefer(String fatherBarcode, String outerBoxBarcode, float scanQty) {
        super.onSparePartsInfoRefer(fatherBarcode, outerBoxBarcode, scanQty);
    }

    @Override
    public void onOrderRefer() {
        OrderRequestInfo info = new OrderRequestInfo();
        if (mModel.getOrderHeaderInfo() == null) {
            MessageBox.Show(mContext, "请扫描单据编号");
        }
        info.setErpvoucherno(mModel.getOrderHeaderInfo().getErpvoucherno());
        info.setVouchertype(mModel.getOrderHeaderInfo().getVouchertype());
        info.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
        mModel.requestRefer(info, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, mModel.TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC, result);
                    BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    } else {
                        MessageBox.Show(mContext,"提交订单失败:"+ returnMsgModel.getResultValue());
                    }
                } catch (Exception e) {
                    MessageBox.Show(mContext, "提交订单失败,出现意料之外的异常:" + e.getMessage());
                }
            }
        });


    }

    /**
     * @desc: 打印单据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/6 11:14
     */
    public void  onOrderPrint(){
        OutStockOrderDetailInfo info = new OutStockOrderDetailInfo();
        if (mModel.getOrderHeaderInfo() == null) {
            MessageBox.Show(mContext, "请扫描单据编号", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onErpVoucherNoFocus();
                }
            });
            return;
        }
        info.setErpvoucherno(mModel.getOrderHeaderInfo().getErpvoucherno());
        info.setPrintername(UrlInfo.mOutStockPrintName);
        info.setPrintertype(UrlInfo.mOutStockPrintType);
        mModel.requestOrderPrint(info, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, mModel.TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC, result);
                    BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    } else {
                        MessageBox.Show(mContext, "打印单据失败:"+returnMsgModel.getResultValue());
                    }
                } catch (Exception e) {
                    MessageBox.Show(mContext, "打印单据失败,出现意料之外的异常:" + e.getMessage());
                }
            }
        });


    }

}
