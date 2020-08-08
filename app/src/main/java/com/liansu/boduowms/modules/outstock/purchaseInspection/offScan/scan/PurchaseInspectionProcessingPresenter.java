package com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan;

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
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.outstock.offScan.DistributionOffShelf;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan.PurchaseInspectionProcessingModel.BARCODE_TYPE_BULK;
import static com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan.PurchaseInspectionProcessingModel.BARCODE_TYPE_OUT_BARCODE;
import static com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan.PurchaseInspectionProcessingModel.BARCODE_TYPE_PALLET_NO;
import static com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan.PurchaseInspectionProcessingModel.SCAN_TYPE_PALLET_NO;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_NONE;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class PurchaseInspectionProcessingPresenter {
    private Context                           mContext;
    private PurchaseInspectionProcessingModel mModel;
    private IPurchaseInspectionProcessingView mView;
    private PrintBusinessModel                mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public PurchaseInspectionProcessingPresenter(Context context, IPurchaseInspectionProcessingView view, MyHandler<BaseActivity> handler, OutStockOrderHeaderInfo headerInfo, String qualityType) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new PurchaseInspectionProcessingModel(context, handler);
        this.mModel.setOrderHeaderInfo(headerInfo);
//        this.mView.setOrderInfo(headerInfo);
        this.mModel.setQualityType(qualityType);
        this.mPrintModel = new PrintBusinessModel(context, handler);
    }

    public PurchaseInspectionProcessingModel getModel() {
        return mModel;
    }


    /**
     * @desc: 获取采购验退单明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/12 18:11
     */
    public void getQualityInspectionDetailList(String erpVoucherNo) {
        OrderRequestInfo info = new OrderRequestInfo();
        info.setErpvoucherno(erpVoucherNo);
        info.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        mModel.requestQualityInspectionDetail(info, new NetCallBackListener<String>() {
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
                            mView.setOrderInfo(orderHeaderInfo);
                            if (mModel.getOrderDetailList().size() > 0) {
                                mView.setOrderDetailInfo(mModel.getOrderDetailList().get(0));
                                mView.onPalletFocus();
                            } else {
                                MessageBox.Show(mContext, "获取表体信息为空");
//                                mView.onErpVoucherNoFocus();
                            }
                        } else {
                            MessageBox.Show(mContext, "获取订单明细失败: " + returnMsgModel.getResultValue());
//                            mView.onErpVoucherNoFocus();
                        }
                    } else {
                        MessageBox.Show(mContext, "获取订单明细失败: " + returnMsgModel.getResultValue());
//                        mView.onErpVoucherNoFocus();
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取订单明细失败: " + ex.getMessage());
//                    mView.onErpVoucherNoFocus();
                }


            }
        });

    }


    /**
     * @desc: 扫描条码   外箱码，托盘码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/11 23:23
     */
    public void onBarCodeScan(String fatherBarcode, String subBarcode, int operationType, float scanQty) {
        try {


            //解析父级条码
            OutBarcodeInfo scanFatherQRCode = null;
            if (fatherBarcode.equals("")) return;
            BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(fatherBarcode);
            if (resultInfo.getHeaderStatus()) {
                scanFatherQRCode = resultInfo.getInfo();
                if (scanFatherQRCode.getSerialno() == null || scanFatherQRCode.getSerialno().equals("")) {
                    MessageBox.Show(mContext, "解析父级条码失败:父级的序列号不能为空");
                    mView.onPalletFocus();
                }
            } else {
                MessageBox.Show(mContext, "解析父级条码失败:" + resultInfo.getMessage());
                mView.onPalletFocus();
                return;
            }

            if (operationType == PurchaseInspectionProcessingModel.SCAN_TYPE_SUB_BARCODE) {
                //解析子级条码
                OutBarcodeInfo scanSubQRCode = null;
                if (subBarcode.equals("")) {
                    mView.onBarcodeFocus();
                    return;
                }
                BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultSubInfo = QRCodeFunc.getQrCode(subBarcode);
                if (resultSubInfo.getHeaderStatus()) {
                    scanSubQRCode = resultSubInfo.getInfo();
                } else {
                    MessageBox.Show(mContext, "解析子级条码失败:" + resultSubInfo.getMessage());
                    return;
                }
                if (scanSubQRCode.getBarcode().contains("%")) {
                    //外箱条码
                    if (scanSubQRCode.getSerialno() == null || scanSubQRCode.getSerialno().equals("")) {
                        mView.setQtyViewStatus(false);
                        scanOuterBarcode(scanFatherQRCode, scanSubQRCode);
                    }

                } else {
                    mView.setQtyViewStatus(true);
                    mView.onQtyFocus();

                }

            } else if (operationType == SCAN_TYPE_PALLET_NO) {
                scanPalletNo(scanFatherQRCode);
            }

        } catch (Exception e) {
            MessageBox.Show(mContext, "扫描条码出现预期之外的异常:" + e.getMessage());
        }
    }

    /**
     * @desc: 扫描托盘码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/12 18:43
     */

    private void scanPalletNo(OutBarcodeInfo palletInfo) {
        OutStockOrderDetailInfo info = new OutStockOrderDetailInfo();
        info.setErpvoucherno(mModel.getOrderHeaderInfo().getErpvoucherno());
        info.setScanqty(palletInfo.getQty());
        info.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        info.setBarcodeType(BARCODE_TYPE_PALLET_NO);
        info.setPalletNo(palletInfo.getBarcode());
        info.setMaterialno(palletInfo.getMaterialno());
        info.setVouchertype(mModel.getOrderHeaderInfo().getVouchertype());
        info.setPostUserNo(BaseApplication.mCurrentUserInfo.getUserno());
//        info.setStrongholdcode(mModel.getOrderHeaderInfo().getStrongholdcode());
        BaseMultiResultInfo<Boolean,OutStockOrderDetailInfo>  checkResult=mModel.findMaterialInfo(palletInfo);
        if (checkResult.getHeaderStatus()){
            OutStockOrderDetailInfo materialInfo=checkResult.getInfo();
            info.setStrongholdcode(materialInfo.getStrongholdcode());
        }else {
            MessageBox.Show(mContext,"获取和条码匹配的物料数据失败:"+checkResult.getMessage());
            mView.onPalletFocus();
            return;
        }
        mModel.requestBarcodeInfoRefer(info, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, mModel.TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC, result);
                    BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        BaseMultiResultInfo<Boolean, Void> checkResult = mModel.checkAndUpdateMaterialInfo(returnMsgModel.getData().get(0), true);
                        if (checkResult.getHeaderStatus()) {
                            mView.setOrderDetailInfo(mModel.getOrderDetailList().get(0));
                        } else
                            MessageBox.Show(mContext, returnMsgModel.getResultValue(), 1);
                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), 1);

                    }

                } catch (Exception e) {
                    MessageBox.Show(mContext, "出现意料之外的异常:" + e.getMessage());
                } finally {
                    mView.onPalletFocus();
                }
            }


        });
    }

    /**
     * @desc: 拆零
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/14 17:39
     */
    public void scanBulkBarcode(final String fatherBarcode, final String scanBarcode, final float scanQty) {
        //解析父级条码
        OutBarcodeInfo scanFatherQRCode = null;
        if (fatherBarcode.equals("")) return;
        BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(fatherBarcode);
        if (resultInfo.getHeaderStatus()) {
            scanFatherQRCode = resultInfo.getInfo();
            if (scanFatherQRCode.getSerialno() == null || scanFatherQRCode.getSerialno().equals("")) {
                MessageBox.Show(mContext, "解析父级条码失败:父级的序列号不能为空");
                mView.onPalletFocus();
            }
        } else {
            MessageBox.Show(mContext, "解析父级条码失败:" + resultInfo.getMessage());
            mView.onPalletFocus();
            return;
        }

        //解析子级条码
        OutBarcodeInfo scanSubQRCode = null;
        if (scanBarcode.equals("")) {
            mView.onBarcodeFocus();
            return;
        }
        BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultSubInfo = QRCodeFunc.getQrCode(scanBarcode);
        if (resultSubInfo.getHeaderStatus()) {
            scanSubQRCode = resultSubInfo.getInfo();
        } else {
            MessageBox.Show(mContext, "解析子级条码失败:" + resultSubInfo.getMessage());
            return;
        }
        final OutBarcodeInfo finalScanSubQRCode = scanSubQRCode;
        final OutBarcodeInfo finalScanFatherQRCode = scanFatherQRCode;
        mModel.requestMaterialInfoQuery(finalScanSubQRCode.getBarcode(), new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, mModel.TAG_SELECT_MATERIAL, result);
                    BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutBarcodeInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        OutStockOrderDetailInfo info = new OutStockOrderDetailInfo();
                        info.setErpvoucherno(mModel.getOrderHeaderInfo().getErpvoucherno());
                        info.setScanqty(scanQty);
                        info.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
                        info.setBarcodeType(BARCODE_TYPE_BULK);
                        info.setMaterialno(finalScanSubQRCode.getBarcode());
                        info.setPalletNo(finalScanFatherQRCode.getBarcode());
                        info.setVouchertype(mModel.getOrderHeaderInfo().getVouchertype());
//                        info.setStrongholdcode(mModel.getOrderHeaderInfo().getStrongholdcode());
                        BaseMultiResultInfo<Boolean,OutStockOrderDetailInfo>  checkResult=mModel.findMaterialInfo(finalScanFatherQRCode);
                        if (checkResult.getHeaderStatus()){
                            OutStockOrderDetailInfo materialInfo=checkResult.getInfo();
                            info.setStrongholdcode(materialInfo.getStrongholdcode());
                        }else {
                            MessageBox.Show(mContext,"获取和条码匹配的物料数据失败:"+checkResult.getMessage());
                            mView.onPalletFocus();
                            return;
                        }
                        mModel.requestBarcodeInfoRefer(info, new NetCallBackListener<String>() {
                            @Override
                            public void onCallBack(String result) {
                                try {
                                    LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, mModel.TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC, result);
                                    BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
                                    }.getType());
                                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                        BaseMultiResultInfo<Boolean, Void> checkResult = mModel.checkAndUpdateMaterialInfo(returnMsgModel.getData().get(0), true);
                                        if (checkResult.getHeaderStatus()) {
                                            mView.setOrderDetailInfo(mModel.getOrderDetailList().get(0));
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
                    } else {
                        MessageBox.Show(mContext, "扫描失败:" + returnMsgModel.getResultValue(), 1);

                    }

                } catch (Exception e) {
                    MessageBox.Show(mContext, "出现意料之外的异常:" + e.getMessage());
                } finally {
                    mView.onPalletFocus();
                }
            }
        });


    }

    /**
     * @desc: 扫描外箱, 整箱出
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/14 17:39
     */
    public void scanOuterBarcode(final OutBarcodeInfo fatherBarcode, final OutBarcodeInfo scanBarcode) {
        mModel.requestMaterialInfoQuery(fatherBarcode.getMaterialno(), new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, mModel.TAG_SELECT_MATERIAL, result);
                    BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutBarcodeInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        OutStockOrderDetailInfo info = new OutStockOrderDetailInfo();
                        info.setErpvoucherno(mModel.getOrderHeaderInfo().getErpvoucherno());
                        info.setScanqty(scanBarcode.getQty());
                        info.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
                        if (scanBarcode.getBarcode() != null && scanBarcode.getBarcode().contains("%")) {
                            info.setBarcodeType(BARCODE_TYPE_OUT_BARCODE);
                        } else {
                            info.setBarcodeType(BARCODE_TYPE_BULK);
                        }
                        info.setMaterialno(scanBarcode.getBarcode());
                        info.setPalletNo(fatherBarcode.getBarcode());
                        info.setVouchertype(mModel.getOrderHeaderInfo().getVouchertype());
//                        info.setStrongholdcode(mModel.getOrderHeaderInfo().getStrongholdcode());
                        BaseMultiResultInfo<Boolean,OutStockOrderDetailInfo>  checkResult=mModel.findMaterialInfo(fatherBarcode);
                        if (checkResult.getHeaderStatus()){
                            OutStockOrderDetailInfo materialInfo=checkResult.getInfo();
                            info.setStrongholdcode(materialInfo.getStrongholdcode());
                        }else {
                            MessageBox.Show(mContext,"获取和条码匹配的物料数据失败:"+checkResult.getMessage());
                            mView.onPalletFocus();
                            return;
                        }
                        mModel.requestBarcodeInfoRefer(info, new NetCallBackListener<String>() {
                            @Override
                            public void onCallBack(String result) {
                                try {
                                    LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, mModel.TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC, result);
                                    BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
                                    }.getType());
                                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                        BaseMultiResultInfo<Boolean, Void> checkResult = mModel.checkAndUpdateMaterialInfo(returnMsgModel.getData().get(0), true);
                                        if (checkResult.getHeaderStatus()) {
                                            mView.setOrderDetailInfo(mModel.getOrderDetailList().get(0));
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
                    } else {
                        MessageBox.Show(mContext, "扫描失败:" + returnMsgModel.getResultValue(), 1);

                    }

                } catch (Exception e) {
                    MessageBox.Show(mContext, "出现意料之外的异常:" + e.getMessage());
                } finally {
                    mView.onPalletFocus();
                }
            }
        });


    }

    /**
     * @desc: 验退提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/20 15:38
     */
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
                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onActivityFinish();
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
            MessageBox.Show(mContext, "请扫描单据编号");
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
                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

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

}
