package com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan;

import android.content.Context;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan.PurchaseInspectionProcessingScan;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_ACTION_CONTINUE;
import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.BaseOutStockBusinessModel.OUT_STOCK_SCAN_TYPE_OUTER_BOX;
import static com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.BaseOutStockBusinessModel.OUT_STOCK_SCAN_TYPE_SPARE_PARTS;
import static com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.BaseOutStockBusinessModel.OUT_STOCK_SCAN_TYPE_TRAY;
import static com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan.PurchaseInspectionProcessingModel.BARCODE_TYPE_BULK;
import static com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan.PurchaseInspectionProcessingModel.BARCODE_TYPE_PALLET_NO;

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
     * @desc: 托盘扫描
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/26 15:00
     */
    public void onPalletScan(String palletBarcode, final int scanType) {
        mModel.setCurrentScanType(scanType);
        mModel.setCurrentFatherInfo(null);
        mModel.setCurrentSubInfo(null);
        mModel.setCurrentMaterialBatchNoList(null);
        //解析父级条码
        OutBarcodeInfo scanFatherQRCode = null;
        if (palletBarcode.equals("")) return;
        BaseMultiResultInfo<Boolean, OutBarcodeInfo> QRResultInfo = QRCodeFunc.getQrCode(palletBarcode);
        if (QRResultInfo.getHeaderStatus()) {
            scanFatherQRCode = QRResultInfo.getInfo();
            if (scanFatherQRCode.getBarcodetype() != QRCodeFunc.BARCODE_TYPE_PALLET_NO) {
                MessageBox.Show(mContext, "解析托盘码失败:托盘格式不正确");
                mView.onFatherBarcodeFocus();
                return;
            }
        } else {
            MessageBox.Show(mContext, "解析托盘码失败:" + QRResultInfo.getMessage());
            mView.onFatherBarcodeFocus();
            return;
        }
            //查询托盘信息
            mModel.requestPalletInfo(scanFatherQRCode, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, result);
                    try {
                        BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutBarcodeInfo>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                            OutBarcodeInfo outBarcodeInfo = returnMsgModel.getData();
                            if (outBarcodeInfo != null) {
                                mModel.setCurrentFatherInfo(outBarcodeInfo);
                                if (scanType == OUT_STOCK_SCAN_TYPE_TRAY) {
                                    onPalletInfoRefer(outBarcodeInfo);
                                } else if (scanType == OUT_STOCK_SCAN_TYPE_OUTER_BOX || scanType == OUT_STOCK_SCAN_TYPE_SPARE_PARTS) {
                                    mView.onSubBarcodeFocus();
                                }

                            } else {
                                MessageBox.Show(mContext, "查询托盘信息失败:获取托盘信息为空  " + returnMsgModel.getResultValue());
                                mView.onFatherBarcodeFocus();
                            }
                        } else {
                            MessageBox.Show(mContext, returnMsgModel.getResultValue());
                            mView.onFatherBarcodeFocus();
                        }

                    } catch (Exception ex) {
                        MessageBox.Show(mContext, ex.getMessage());
                        mView.onFatherBarcodeFocus();
                    }
                }
            });




    }

    /**
     * @desc: 外箱扫描
     * @param: barcode  外箱扫描  成品是有批次的
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 7:58
     */
    public void onOuterBoxScan(final String palletBarcode, String outerBoxBarcode, int scanType) {
        mModel.setCurrentScanType(scanType);
        mModel.setCurrentSubInfo(null);
        mModel.setCurrentMaterialBatchNoList(null);
        final OutBarcodeInfo fatherInfo = mModel.getCurrentFatherInfo();
        if (fatherInfo == null) {
            MessageBox.Show(mContext, "校验托盘条码信息失败:获取的条码信息为空,请扫描托盘码");
            mView.onFatherBarcodeFocus();
            return;
        }
        if (!palletBarcode.contains(fatherInfo.getSerialno())) {
            MessageBox.Show(mContext, "校验托盘条码信息失败:已保存的托盘序列号和当前界面扫描的托盘序列号不一致!请扫描托盘码");
            mView.onFatherBarcodeFocus();
            return;
        }
        //解析子级条码
        OutBarcodeInfo scanSubQRCode = null;
        if (outerBoxBarcode.equals("")) return;
        BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultSubInfo = QRCodeFunc.getQrCode(outerBoxBarcode);
        if (resultSubInfo.getHeaderStatus()) {
            scanSubQRCode = resultSubInfo.getInfo();
            if (scanSubQRCode.getBarcodetype() != QRCodeFunc.BARCODE_TYPE_OUTER_BOX) {
                MessageBox.Show(mContext, "解析外箱条码失败:外箱格式不正确");
                mView.onSubBarcodeFocus();
                return;
            }
        } else {
            MessageBox.Show(mContext, "解析外箱条码失败:" + resultSubInfo.getMessage());
            mView.onSubBarcodeFocus();
            return;
        }

        if (scanSubQRCode.getBatchno() == null || scanSubQRCode.getBatchno().equals("")) {
            MessageBox.Show(mContext, "解析外箱条码失败:批次不能为空");
            mView.onSubBarcodeFocus();
            return;
        }
        if (scanSubQRCode.getMaterialno() == null || scanSubQRCode.getMaterialno().equals("")) {
            MessageBox.Show(mContext, "解析外箱条码失败:物料不能为空");
            mView.onSubBarcodeFocus();
            return;
        }

        final OutBarcodeInfo finalScanSubQRCode = scanSubQRCode;
        mModel.requestMaterialInfoQuery(scanSubQRCode.getMaterialno(), new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, mModel.TAG_SELECT_MATERIAL, result);
                    BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutBarcodeInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        mModel.setCurrentSubInfo(finalScanSubQRCode);
                        onOuterBoxInfoRefer(fatherInfo, finalScanSubQRCode);

                    } else {
                        MessageBox.Show(mContext, "校验外箱码的物料信息失败:" + returnMsgModel.getResultValue(), 1);

                    }

                } catch (Exception e) {
                    MessageBox.Show(mContext, "出现意料之外的异常:" + e.getMessage());
                } finally {
                    mView.onSubBarcodeFocus();
                }
            }
        });

    }

    /**
     * @desc: 散件扫描
     * @param: barcode  69  物料编码   混托
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 7:48
     */
    public void onSparePartsScan(final String palletBarcode, String outerBoxBarcode, int scanType) {
        mModel.setCurrentScanType(scanType);
        mModel.setCurrentSubInfo(null);
        mModel.setCurrentMaterialBatchNoList(null);
        final OutBarcodeInfo fatherInfo = mModel.getCurrentFatherInfo();
        if (fatherInfo == null) {
            MessageBox.Show(mContext, "校验托盘条码信息失败:获取的条码信息为空,请扫描托盘码");
            mView.onFatherBarcodeFocus();
            return;
        }

        if (!palletBarcode.contains(fatherInfo.getSerialno())) {
            if (fatherInfo.getBatchno() != null && fatherInfo.getMaterialno() != null) { //如果是托盘码则校验和界面扫描的托盘码的序列号是否一致
                MessageBox.Show(mContext, "校验托盘条码信息失败:已保存的托盘序列号和当前界面扫描的托盘序列号不一致!请扫描托盘码");
                mView.onFatherBarcodeFocus();
                return;
            }


        }
        //解析子级条码
        OutBarcodeInfo scanSubQRCode = null;
        if (outerBoxBarcode.equals("")) return;
        BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultSubInfo = QRCodeFunc.getQrCode(outerBoxBarcode);
        if (resultSubInfo.getHeaderStatus()) {
            scanSubQRCode = resultSubInfo.getInfo();
            if (scanSubQRCode.getBarcodetype() != QRCodeFunc.BARCODE_TYPE_SPARE_PARTS) {
                MessageBox.Show(mContext, "解析散件条码失败:散件格式不正确,必须是物料编号或69码");
                mView.onSubBarcodeFocus();
                return;
            }
        } else {
            MessageBox.Show(mContext, "解析散件条码失败:" + resultSubInfo.getMessage());
            mView.onSubBarcodeFocus();
            return;
        }


        final OutBarcodeInfo finalScanSubQRCode = scanSubQRCode;
        mModel.requestMaterialInfoQuery(scanSubQRCode.getBarcode(), new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, mModel.TAG_SELECT_MATERIAL, result);
                    BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutBarcodeInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        mModel.setCurrentSubInfo(finalScanSubQRCode);
                        mView.onQtyFocus();


                    } else {
                        MessageBox.Show(mContext, "校验散件的物料信息失败:" + returnMsgModel.getResultValue(), 1);
                        mView.onSubBarcodeFocus();
                    }

                } catch (Exception e) {
                    MessageBox.Show(mContext, "出现意料之外的异常:" + e.getMessage());
                    mView.onSubBarcodeFocus();
                }
            }
        });

    }


    /**
     * @desc: 提交托盘条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 6:04
     */
    public void onPalletInfoRefer(final OutBarcodeInfo fatherBarcode) {
        OutStockOrderDetailInfo info = new OutStockOrderDetailInfo();
        info.setErpvoucherno(mModel.getOrderHeaderInfo().getErpvoucherno());
        info.setScanqty(fatherBarcode.getQty());
        info.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        info.setBarcodeType(BARCODE_TYPE_PALLET_NO);
        info.setPalletNo(fatherBarcode.getBarcode());
        info.setMaterialno(fatherBarcode.getMaterialno());
        info.setBatchno(fatherBarcode.getBatchno());
        info.setVouchertype(mModel.getOrderHeaderInfo().getVouchertype());
        info.setPostUserNo(BaseApplication.mCurrentUserInfo.getUserno());
        BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> checkResult = mModel.findMaterialInfo(fatherBarcode);
        if (checkResult.getHeaderStatus()) {
            OutStockOrderDetailInfo materialInfo = checkResult.getInfo();
            info.setStrongholdcode(materialInfo.getStrongholdcode());

        } else {
            if (fatherBarcode.getBatchno().equals("") && fatherBarcode.getMaterialno().equals("") && fatherBarcode.getBarcodetype() == QRCodeFunc.BARCODE_TYPE_PALLET_NO) {
                info.setStrongholdcode(mModel.getOrderHeaderInfo().getStrongholdcode());
            } else {
                MessageBox.Show(mContext, "获取和条码匹配的物料数据失败:" + checkResult.getMessage());
                mView.onFatherBarcodeFocus();
                return;
            }

        }

        mModel.requestBarcodeInfoRefer(info, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOutStockBusiness.class, "METHOD_REQUEST_BARCODE_INFO_REFER_SUB!", result);
                try {
                    BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        List<OutStockOrderDetailInfo> list = returnMsgModel.getData();
                        if (list != null) {
                            for (OutStockOrderDetailInfo orderDetailInfo : list) {
                                if (orderDetailInfo != null) {
                                    //更新数量
                                    BaseMultiResultInfo<Boolean, Void> checkResult = mModel.checkAndUpdateMaterialInfo(orderDetailInfo);
                                    if (!checkResult.getHeaderStatus()) {
                                        MessageBox.Show(mContext, "更新物料行失败:" + returnMsgModel.getResultValue());
                                        break;
                                    }
                                } else {
                                    MessageBox.Show(mContext, "实时提交托盘码获取的表体数据为空");
                                    break;
                                }
                            }
                            mView.onReset();
                            mView.bindListView(mModel.getOrderDetailList());
                            mView.onFatherBarcodeFocus();

                        }

                    } else {
                        MessageBox.Show(mContext, "提交条码失败:" + returnMsgModel.getResultValue());
                        mView.onFatherBarcodeFocus();
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "出现预期之外的异常" + ex.getMessage());
                    mView.onFatherBarcodeFocus();
                }

            }
        });

    }

    /**
     * @desc: 提交散件信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 7:10
     */
    public void onSparePartsInfoRefer(final String fatherBarcode, final String outerBoxBarcode, float scanQty) {
        final OutBarcodeInfo fatherInfo = mModel.getCurrentFatherInfo();
        final OutBarcodeInfo subInfo = mModel.getCurrentSubInfo();
        if (fatherInfo == null) {
            MessageBox.Show(mContext, "校验托盘条码信息失败:获取的条码信息为空,请扫描托盘码");
            mView.onFatherBarcodeFocus();
            return;
        }

        if (!fatherBarcode.contains(fatherInfo.getSerialno())) {
            if (fatherInfo.getBatchno() != null && fatherInfo.getMaterialno() != null) {  //如果是批次托盘就校验序列号
                MessageBox.Show(mContext, "校验托盘条码信息失败:已保存的托盘序列号和当前界面扫描的托盘序列号不一致!请扫描托盘码");
                mView.onFatherBarcodeFocus();
                return;
            }

        }

        if (subInfo == null) {
            MessageBox.Show(mContext, "校验散件条码信息失败:获取的条码信息为空,请扫描散件码");
            mView.onSubBarcodeFocus();
            return;
        }
        if (!outerBoxBarcode.contains(subInfo.getBarcode())) {
            MessageBox.Show(mContext, "校验散件条码信息失败:已保存的散件码和当前界面扫描的散件码不一致!请扫描托盘码");
            mView.onSubBarcodeFocus();
            return;
        }

        OutStockOrderDetailInfo info = new OutStockOrderDetailInfo();
        info.setErpvoucherno(mModel.getOrderHeaderInfo().getErpvoucherno());
        info.setScanqty(scanQty);
        info.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        info.setBarcodeType(BARCODE_TYPE_BULK);
        info.setMaterialno(subInfo.getBarcode());
        info.setPalletNo(fatherInfo.getBarcode());
        info.setBatchno(subInfo.getBatchno());
        info.setVouchertype(mModel.getOrderHeaderInfo().getVouchertype());
        BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> checkResult = mModel.findMaterialInfo(fatherInfo);
        if (checkResult.getHeaderStatus()) {
            OutStockOrderDetailInfo materialInfo = checkResult.getInfo();
            info.setStrongholdcode(materialInfo.getStrongholdcode());
        } else {
            if (fatherInfo.getBatchno().equals("") && fatherInfo.getMaterialno().equals("") && fatherInfo.getBarcodetype() == QRCodeFunc.BARCODE_TYPE_PALLET_NO) {
                info.setStrongholdcode(mModel.getOrderHeaderInfo().getStrongholdcode());
            } else {
                MessageBox.Show(mContext, "获取和托盘条码匹配的物料行数据失败:" + checkResult.getMessage());
                mView.onFatherBarcodeFocus();
                return;
            }

        }
        mModel.requestBarcodeInfoRefer(info, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(BaseOutStockBusiness.class, mModel.TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC_SUB, result);
                    BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
                    }.getType());

                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        List<OutStockOrderDetailInfo> list = returnMsgModel.getData();
                        if (list != null && list.size() > 0) {
                            for (OutStockOrderDetailInfo updateInfo : list) {
                                BaseMultiResultInfo<Boolean, Void> checkResult = mModel.checkAndUpdateMaterialInfo(updateInfo);
                                if (!checkResult.getHeaderStatus()) {
                                    MessageBox.Show(mContext, "更新物料行失败:" + returnMsgModel.getResultValue());
                                    mView.onQtyFocus();
                                    break;
                                }
                            }
                            mView.bindListView(mModel.getOrderDetailList());
                            mView.onReset();

                        }


                    } else if (returnMsgModel.getResult() == RESULT_TYPE_ACTION_CONTINUE) {
                        List<OutStockOrderDetailInfo> list = returnMsgModel.getData();
                        mModel.setCurrentMaterialBatchNoList(list);
                        BaseMultiResultInfo<Boolean, List<String>> resultInfo = mModel.getMaterialBatchNoList(mModel.getCurrentMaterialBatchNoList(), mModel.getCurrentSubInfo().getMaterialno());
                        if (resultInfo.getHeaderStatus()) {
                            mView.createMultipleBatchesSelectDialog(resultInfo.getInfo());
                        } else {
                            MessageBox.Show(mContext, returnMsgModel.getResultValue());
                            mView.onSubBarcodeFocus();
                        }

                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), 1);
                        mView.onQtyFocus();

                    }

                } catch (Exception e) {
                    MessageBox.Show(mContext, "出现意料之外的异常:" + e.getMessage());
                    mView.onQtyFocus();
                } finally {

                }
            }


        });
    }


    /**
     * @desc: 提交外箱码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 7:10
     */
    public void onOuterBoxInfoRefer(final OutBarcodeInfo fatherBarcodeInfo, final OutBarcodeInfo outerBoxBarcodeInfo) {
        OutStockOrderDetailInfo info = new OutStockOrderDetailInfo();
        info.setErpvoucherno(mModel.getOrderHeaderInfo().getErpvoucherno());
        info.setScanqty(outerBoxBarcodeInfo.getQty());
        info.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        info.setBarcodeType(BARCODE_TYPE_BULK);
        info.setMaterialno(outerBoxBarcodeInfo.getBarcode());
        info.setPalletNo(fatherBarcodeInfo.getBarcode());
        info.setBatchno(outerBoxBarcodeInfo.getBatchno());
        info.setVouchertype(mModel.getOrderHeaderInfo().getVouchertype());
        BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> checkResult = mModel.findMaterialInfo(fatherBarcodeInfo);
        if (checkResult.getHeaderStatus()) {
            OutStockOrderDetailInfo materialInfo = checkResult.getInfo();
            info.setStrongholdcode(materialInfo.getStrongholdcode());
        } else {
            if (fatherBarcodeInfo.getBatchno().equals("") && fatherBarcodeInfo.getMaterialno().equals("") && fatherBarcodeInfo.getBarcodetype() == QRCodeFunc.BARCODE_TYPE_PALLET_NO) {
                info.setStrongholdcode(mModel.getOrderHeaderInfo().getStrongholdcode());
            } else {
                MessageBox.Show(mContext, "获取和托盘条码匹配的物料行数据失败:" + checkResult.getMessage());
                mView.onSubBarcodeFocus();
                return;
            }

        }
        mModel.requestBarcodeInfoRefer(info, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(BaseOutStockBusiness.class, mModel.TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC_SUB, result);
                    BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        List<OutStockOrderDetailInfo> list = returnMsgModel.getData();
                        if (list != null && list.size() > 0) {
                            for (OutStockOrderDetailInfo updateInfo : list) {
                                BaseMultiResultInfo<Boolean, Void> checkResult = mModel.checkAndUpdateMaterialInfo(updateInfo);
                                if (!checkResult.getHeaderStatus()) {
                                    MessageBox.Show(mContext, "更新物料行失败:" + returnMsgModel.getResultValue());
                                    mView.onSubBarcodeFocus();
                                    break;
                                }
                            }

                            mView.onReset();
                            mView.bindListView(mModel.getOrderDetailList());
                        }


                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), 1);
                        mView.onSubBarcodeFocus();

                    }

                } catch (Exception e) {
                    MessageBox.Show(mContext, "出现意料之外的异常:" + e.getMessage());
                    mView.onSubBarcodeFocus();
                } finally {

                }
            }


        });
    }

    /**
     * @desc: 拼托扫描
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/26 15:04
     */
    protected void onCombineTrayScan(OutBarcodeInfo palletBarcode, int scanType) {

    }

    /**
     * @desc: 单据提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/20 15:38
     */
    public void onOrderRefer() {

    }
    /**
     * @desc: 单据打印
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 16:22
     */
    public void onOrderPrint(){}
    public void onReset() {
        mView.onReset();
        mModel.clearCurrentScanInfo();
    }

}
