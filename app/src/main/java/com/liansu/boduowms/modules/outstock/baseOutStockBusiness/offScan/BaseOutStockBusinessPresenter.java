package com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan;

import android.content.Context;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.BaseOutStockBusinessModel.OUT_STOCK_SCAN_TYPE_COMBINE_TRAYS;
import static com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.BaseOutStockBusinessModel.OUT_STOCK_SCAN_TYPE_OUTER_BOX;
import static com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.BaseOutStockBusinessModel.OUT_STOCK_SCAN_TYPE_SPARE_PARTS;
import static com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.BaseOutStockBusinessModel.OUT_STOCK_SCAN_TYPE_TRAY;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/28.
 */
public abstract class BaseOutStockBusinessPresenter<V extends IBaseOutStockBusinessView, K extends BaseOutStockBusinessModel> {

    protected Context            mContext;
    protected K                  mModel;
    protected V                  mView;
    protected PrintBusinessModel mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public BaseOutStockBusinessPresenter(Context context, V view, MyHandler<BaseActivity> handler, K model) {
        this.mContext = context;
        this.mView = view;
        this.mModel = model;
        this.mPrintModel = new PrintBusinessModel(context, handler);

    }

    public K getModel() {
        return mModel;
    }

    protected String getTitle() {
        return null;
    }

    /**
     * @desc: 获取订单明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/22 9:53
     */
    public void getOrderDetailInfoList(String erpVoucherNo) {
    }


    /**
     * @desc:
     * @param: barcode  根据不同的扫描方式 可以是 托盘码，拼托码，外箱码
     * @param：bulkBarcode 散件条码  69码或内盒物料编码
     * @param：scanType  扫描类型
     * @return:
     * @author: Nietzsche
     * @time 2020/7/22 10:13
     */
    public void onScan(String fatherBarcode, String subBarcode, int scanType) {
        try {


            //解析父级条码
            OutBarcodeInfo scanFatherQRCode = null;
            if (fatherBarcode.equals("")) return;
            BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(fatherBarcode);
            if (resultInfo.getHeaderStatus()) {
                scanFatherQRCode = resultInfo.getInfo();
            } else {
                MessageBox.Show(mContext, "解析父级条码失败:"+resultInfo.getMessage() );
                return;
            }


            //解析子级条码
            OutBarcodeInfo scanSubQRCode = null;
            if (subBarcode.equals("")) return;
            BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultSubInfo = QRCodeFunc.getQrCode(subBarcode);
            if (resultSubInfo.getHeaderStatus()) {
                scanSubQRCode = resultSubInfo.getInfo();
            } else {
                MessageBox.Show(mContext, "解析子级条码失败:"+resultSubInfo.getMessage() );
                return;
            }

            //扫描条码
            if (scanType == OUT_STOCK_SCAN_TYPE_TRAY) {
                onPalletScan(scanFatherQRCode);
            } else if (scanType == OUT_STOCK_SCAN_TYPE_OUTER_BOX) {
                onOuterBoxScan(scanFatherQRCode,scanSubQRCode,scanType);
            } else if (scanType == OUT_STOCK_SCAN_TYPE_SPARE_PARTS) {
                onSparePartsScan(scanFatherQRCode, scanSubQRCode,mView.getScanQty(),scanType);
            } else if (scanType == OUT_STOCK_SCAN_TYPE_COMBINE_TRAYS) {
                onCombineTrayScan(scanFatherQRCode,scanType);
            }

        } catch (Exception e) {
            MessageBox.Show(mContext, "出现预期之外的异常"+e.getMessage() );
        }
    }

    /**
     * @desc: 托盘扫描
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/26 15:00
     */
    protected void onPalletScan(final OutBarcodeInfo palletBarcode) {
        //校验条码的物料和批次
        BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> resultInfo = mModel.findMaterialInfo(palletBarcode);
        if (resultInfo.getHeaderStatus()) {
            OutStockOrderDetailInfo info=resultInfo.getInfo();
             mModel.requestBarcodeInfoRefer(info, new NetCallBackListener<String>() {
                 @Override
                 public void onCallBack(String result) {
//                     LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC, result);
                     try {
                         BaseResultInfo<OutStockOrderDetailInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderDetailInfo>>() {
                         }.getType());
                         if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                             OutStockOrderDetailInfo orderDetailInfo = returnMsgModel.getData();
                             if (orderDetailInfo != null) {
                                 palletBarcode.setQty(orderDetailInfo.getScanqty());
                                 //更新数量
                                 mModel.updateMaterialInfo(orderDetailInfo,palletBarcode,true);
                                 if (mModel.getOrderDetailList().size() > 0) {
                                     mView.bindListView(mModel.getOrderDetailList());
                                     mView.onFatherBarcodeFocus();
                                 } else {
                                     MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                                     mView.onFatherBarcodeFocus();
                                 }
                             } else {
                                 MessageBox.Show(mContext, "实时提交获取的表体数据为空" );
                                 mView.onFatherBarcodeFocus();
                             }
                         }else {
                             MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                             mView.onFatherBarcodeFocus();
                         }

                     } catch (Exception ex) {
                         MessageBox.Show(mContext, "出现预期之外的异常"+ex.getMessage() );
                         mView.onFatherBarcodeFocus();
                     }

                 }
             });
        }
    }

    /**
     * @desc: 外箱扫描
     * @param: barcode  外箱扫描  成品是有批次的
     * @return:
     * @author: Nietzsche
     * @time 2020/7/22 10:34
     */
    protected void onOuterBoxScan(final OutBarcodeInfo palletBarcode, OutBarcodeInfo outerBoxBarcode, int scanType) {

        BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> resultInfo = mModel.findMaterialInfo(palletBarcode,outerBoxBarcode,scanType);
        if (resultInfo.getHeaderStatus()) {
            OutStockOrderDetailInfo info=resultInfo.getInfo();
            mModel.requestBarcodeInfoRefer(info, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
//                     LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC, result);
                    try {
                        BaseResultInfo<OutStockOrderDetailInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderDetailInfo>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                            OutStockOrderDetailInfo orderDetailInfo = returnMsgModel.getData();
                            if (orderDetailInfo != null) {
                                palletBarcode.setQty(orderDetailInfo.getScanqty());
                                //更新数量
                                mModel.updateMaterialInfo(orderDetailInfo,palletBarcode,true);
                                if (mModel.getOrderDetailList().size() > 0) {
                                    mView.bindListView(mModel.getOrderDetailList());
                                    mView.onFatherBarcodeFocus();
                                } else {
                                    MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                                    mView.onFatherBarcodeFocus();
                                }
                            } else {
                                MessageBox.Show(mContext, "实时提交获取的表体数据为空" );
                                mView.onFatherBarcodeFocus();
                            }
                        }else {
                            MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                            mView.onFatherBarcodeFocus();
                        }

                    } catch (Exception ex) {
                        MessageBox.Show(mContext, "出现预期之外的异常"+ex.getMessage() );
                        mView.onFatherBarcodeFocus();
                    }

                }
            });
        }
    }


    /**
     * @desc: 散件扫描
     * @param: barcode  父级托盘条码   bulkBarcode  散件条码
     * @return:
     * @author: Nietzsche
     * @time 2020/7/22 10:36
     */
    protected void onSparePartsScan(final OutBarcodeInfo palletBarcode, OutBarcodeInfo sparePartsBarcode, float subQty,int scanType) {
       if (subQty==-1) return;
        sparePartsBarcode.setQty(subQty);
        BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> resultInfo = mModel.findMaterialInfo(palletBarcode,sparePartsBarcode,scanType);
        if (resultInfo.getHeaderStatus()) {
            OutStockOrderDetailInfo info=resultInfo.getInfo();
            mModel.requestBarcodeInfoRefer(info, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
//                     LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC, result);
                    try {
                        BaseResultInfo<OutStockOrderDetailInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderDetailInfo>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                            OutStockOrderDetailInfo orderDetailInfo = returnMsgModel.getData();
                            if (orderDetailInfo != null) {
                                palletBarcode.setQty(orderDetailInfo.getScanqty());

                                //更新数量
                                mModel.updateMaterialInfo(orderDetailInfo,palletBarcode,true);
                                if (mModel.getOrderDetailList().size() > 0) {
                                    mView.bindListView(mModel.getOrderDetailList());
                                    mView.onFatherBarcodeFocus();
                                } else {
                                    MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                                    mView.onFatherBarcodeFocus();
                                }
                            } else {
                                MessageBox.Show(mContext, "实时提交获取的表体数据为空" );
                                mView.onFatherBarcodeFocus();
                            }
                        }else {
                            MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                            mView.onFatherBarcodeFocus();
                        }

                    } catch (Exception ex) {
                        MessageBox.Show(mContext, "出现预期之外的异常"+ex.getMessage() );
                        mView.onFatherBarcodeFocus();
                    }

                }
            });
        }
    }


    /**
     * @desc: 拼托扫描
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/26 15:04
     */
    protected void onCombineTrayScan(OutBarcodeInfo palletBarcode,int scanType) {

    }
}
