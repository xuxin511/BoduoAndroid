package com.liansu.boduowms.modules.outstock.packingScan;

import android.content.Context;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_ACTION_CONTINUE;
import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/28.
 */
public class PackingScanPresenter {
    protected Context            mContext;
    protected PackingScanModel   mModel;
    protected IPackingScanView   mView;
    protected PrintBusinessModel mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public PackingScanPresenter(Context context, IPackingScanView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new PackingScanModel(context, handler);
        this.mPrintModel = new PrintBusinessModel(context, handler);

    }

    public PackingScanModel getModel() {
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
        OutStockOrderHeaderInfo info = new OutStockOrderHeaderInfo();
        mModel.requestOrderDetail(info, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
//                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GetT_PurchaseOrderListADFAsync, result);
                try {
                    BaseResultInfo<OrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OrderHeaderInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == 1) {
                        OrderHeaderInfo orderHeaderInfo = returnMsgModel.getData();
                        if (orderHeaderInfo != null) {
//                            mModel.setOrderDetailList(orderHeaderInfo.getDetail());
//                            if (mModel.getOrderDetailList().size() > 0) {
//                                mView.bindListView(mModel.getOrderDetailList());
//                            } else {
//                                MessageBox.Show(mContext, returnMsgModel.getResultValue());
//                            }
                        } else {
                            MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                        }
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );
                }


            }
        });
    }


    /**
     * @desc: 扫描 外箱,69码,内盒物料编码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/27 18:38
     */
    protected void onScan(String barcode) {
        try {
            mModel.setCurrentBarcodeInfo(null); //当前扫描物料要置空
            //解析条码
            OutBarcodeInfo scanQRCode = null;
            if (barcode.equals("")) return;
            BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(barcode);
            if (resultInfo.getHeaderStatus()) {
                scanQRCode = resultInfo.getInfo();
            } else {
                MessageBox.Show(mContext, "解析条码失败:" + resultInfo.getMessage() );
                return;
            }
            //带有%的是外箱，托盘不能扫
            if (barcode.contains("%")) {
                if (scanQRCode.getSerialno() != null && !scanQRCode.getSerialno().equals("") || scanQRCode.getBarcodetype() == 3) {
                    MessageBox.Show(mContext, "扫描条码失败:不能扫描托盘码" );
                    return;
                }
                onOuterBoxScan(scanQRCode);
            } else {
                //散件  69码和内盒  先输入数量
//                mView.onQtyFocus();
                onSparePartsScan(scanQRCode);
            }


        } catch (Exception e) {
            MessageBox.Show(mContext, "出现预期之外的异常" + e.getMessage() );
        }
    }


    /**
     * @desc: 扫描外箱条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/27 18:46
     */
    public void onOuterBoxScan(final OutBarcodeInfo outerBoxBarcode) {
        BaseMultiResultInfo<Boolean, Void> hasMaterialResult = mModel.isMaterialNoInOrderDetail(outerBoxBarcode.getMaterialno());
        if (hasMaterialResult.getHeaderStatus()) {
            mModel.requestReferBarcodeInfo(outerBoxBarcode, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    //                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GetT_PurchaseOrderListADFAsync, result);
                    try {
                        BaseResultInfo<OutStockOrderDetailInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderDetailInfo>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                            OutStockOrderDetailInfo orderDetailInfo = returnMsgModel.getData();
                            if (orderDetailInfo != null) {
                                //校验物料信息
                                BaseMultiResultInfo<Boolean, Void> checkMaterialResult = mModel.checkMaterialInfo(orderDetailInfo);
                                if (checkMaterialResult.getHeaderStatus()) {
                                    //更新物料信息
                                    BaseMultiResultInfo<Boolean, Void> updateMaterialResult = mModel.updateMaterialInfo(orderDetailInfo);
                                    if (updateMaterialResult.getHeaderStatus()) {
                                        mView.bindListView(mModel.getOrderDetailList());
                                        mView.onBarcodeNoFocus();
                                    } else {
                                        MessageBox.Show(mContext, updateMaterialResult.getMessage() );
                                        mView.onBarcodeNoFocus();
                                    }
                                } else {
                                    MessageBox.Show(mContext, checkMaterialResult.getMessage() );
                                    mView.onBarcodeNoFocus();
                                }

                            } else {
                                MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                                mView.onBarcodeNoFocus();
                            }
                        } else if (returnMsgModel.getResult() == RESULT_TYPE_ACTION_CONTINUE) {
                            mModel.setCurrentBarcodeInfo(outerBoxBarcode); //保存第一次发起的数据
                            OutStockOrderDetailInfo orderDetailInfo = returnMsgModel.getData();
                            List<String> batchNoList = new ArrayList<>();
                            mView.createBatchNoListDialog(batchNoList);
                        }
                        //返回数据集 ，再次发起请求


                    } catch (
                            Exception ex) {
                        MessageBox.Show(mContext, ex.getMessage() );
                        mView.onBarcodeNoFocus();
                    }


                }
            });
        }


    }

    /**
     * @desc: 扫描散件  69码或者内盒码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/27 18:48
     */
    public void onSparePartsScan(final OutBarcodeInfo sparePartsBarcode) {
        if (mView.getQty() == -1) return;
        sparePartsBarcode.setQty(mView.getQty());
        mModel.requestReferBarcodeInfo(sparePartsBarcode, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                //                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GetT_PurchaseOrderListADFAsync, result);
                try {
                    BaseResultInfo<OutStockOrderDetailInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderDetailInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        OutStockOrderDetailInfo orderDetailInfo = returnMsgModel.getData();
                        if (orderDetailInfo != null) {
                            //校验物料信息
                            BaseMultiResultInfo<Boolean, Void> checkMaterialResult = mModel.checkMaterialInfo(orderDetailInfo);
                            if (checkMaterialResult.getHeaderStatus()) {
                                //更新物料信息
                                BaseMultiResultInfo<Boolean, Void> updateMaterialResult = mModel.updateMaterialInfo(orderDetailInfo);
                                if (updateMaterialResult.getHeaderStatus()) {
                                    mView.bindListView(mModel.getOrderDetailList());
                                    mView.onBarcodeNoFocus();
                                } else {
                                    MessageBox.Show(mContext, updateMaterialResult.getMessage() );
                                    mView.onBarcodeNoFocus();
                                }
                            } else {
                                MessageBox.Show(mContext, checkMaterialResult.getMessage() );
                                mView.onBarcodeNoFocus();
                            }

                        } else {
                            MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                            mView.onBarcodeNoFocus();
                        }
                    } else if (returnMsgModel.getResult() == RESULT_TYPE_ACTION_CONTINUE) {
                        mModel.setCurrentBarcodeInfo(sparePartsBarcode); //保存第一次发起的数据
                        OutStockOrderDetailInfo orderDetailInfo = returnMsgModel.getData();
                        List<String> batchNoList = new ArrayList<>();
                        mView.createBatchNoListDialog(batchNoList);
                    }


                } catch (
                        Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );
                    mView.onBarcodeNoFocus();
                }


            }
        });

    }

    /**
     * @desc: 提交扫描的条码第二次请求发起时调用的方法
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/28 11:00
     */
    public void requestBarcodeInfo(OutBarcodeInfo outBarcodeInfo) {
        if (mModel.getCurrentBarcodeInfo() == null) {
            MessageBox.Show(mContext, "发起第二次扫描提交请求时,第一次保存的扫描数据不能为空" );
            return;
        }
        mModel.requestReferBarcodeInfo(outBarcodeInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                //LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GetT_PurchaseOrderListADFAsync, result);
                try {
                    BaseResultInfo<OutStockOrderDetailInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderDetailInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        OutStockOrderDetailInfo orderDetailInfo = returnMsgModel.getData();
                        if (orderDetailInfo != null) {
                            //校验物料信息
                            BaseMultiResultInfo<Boolean, Void> checkMaterialResult = mModel.checkMaterialInfo(orderDetailInfo);
                            if (checkMaterialResult.getHeaderStatus()) {
                                //更新物料信息
                                BaseMultiResultInfo<Boolean, Void> updateMaterialResult = mModel.updateMaterialInfo(orderDetailInfo);
                                if (updateMaterialResult.getHeaderStatus()) {
                                    mView.bindListView(mModel.getOrderDetailList());
                                    mView.onBarcodeNoFocus();
                                } else {
                                    MessageBox.Show(mContext, updateMaterialResult.getMessage() );
                                    mView.onBarcodeNoFocus();
                                }
                            } else {
                                MessageBox.Show(mContext, checkMaterialResult.getMessage() );
                                mView.onBarcodeNoFocus();
                            }

                        } else {
                            MessageBox.Show(mContext, "请求提交成功，但返回的数据为空" );
                            mView.onBarcodeNoFocus();
                        }
                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                        mView.onBarcodeNoFocus();
                    }


                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );
                    mView.onBarcodeNoFocus();
                }


            }
        });
    }

    /**
     * @desc: 非库存打印
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/28 13:51
     */
    public void unStockPackingPrint() {
        mModel.requestUnStockPackingPrintInfo(null, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                //LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GetT_PurchaseOrderListADFAsync, result);
                try {
                    BaseResultInfo<OutStockOrderDetailInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderDetailInfo>>() {
                    }.getType());
                    MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                    mView.onBarcodeNoFocus();

                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );
                    mView.onBarcodeNoFocus();
                }


            }
        });
    }

     /**
      * @desc: 标签打印
      * @param:
      * @return:
      * @author: Nietzsche
      * @time 2020/7/28 14:32
      */
    public void printLCLLabel(){
        mModel.requestPrintLCLLabel(null, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                //LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GetT_PurchaseOrderListADFAsync, result);
                try {
                    BaseResultInfo<OutStockOrderDetailInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderDetailInfo>>() {
                    }.getType());
                    MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                    mView.onBarcodeNoFocus();

                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );
                    mView.onBarcodeNoFocus();
                }


            }
        });
    }

    /**
     * @desc: 设置月台
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/28 14:21
     */
    public void setBalconyInfo(String orderNo, String balconyDesc) {

    }
}
