package com.liansu.boduowms.modules.qualityInspection.qualityInspectionProcessing;

import android.content.Context;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.modules.qualityInspection.qualityInspectionProcessing.QualityInspectionProcessingModel.QUALITY_TYPE_QUALIFIED;
import static com.liansu.boduowms.modules.qualityInspection.qualityInspectionProcessing.QualityInspectionProcessingModel.QUALITY_TYPE_UNQUALIFIED;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class QualityInspectionProcessingPresenter {
    private Context                          mContext;
    private QualityInspectionProcessingModel mModel;
    private IQualityInspectionProcessingView mView;
    private PrintBusinessModel               mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public QualityInspectionProcessingPresenter(Context context, IQualityInspectionProcessingView view, MyHandler<BaseActivity> handler, QualityHeaderInfo headerInfo, String qualityType) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new QualityInspectionProcessingModel(context, handler);
        this.mModel.setQualityHeaderInfo(headerInfo);
        this.mView.setOrderInfo(headerInfo);
        this.mModel.setQualityType(qualityType);
        this.mPrintModel = new PrintBusinessModel(context, handler);
    }

    public QualityInspectionProcessingModel getModel() {
        return mModel;
    }


    /**
     * @desc: 获取质检合格明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/12 18:11
     */
    public void getQualityInspectionDetailList() {
        mModel.requestQualityInspectionDetail(mModel.getQualityHeaderInfo(), new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(QualityInspectionProcessingScan.class, mModel.TAG_GetT_InStockDetailListByHeaderIDADF, result);
                try {
                    BaseResultInfo<List<QualityHeaderInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<QualityHeaderInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == 1) {
                        List<QualityHeaderInfo> list = returnMsgModel.getData();
                        if (list != null && list.size() > 0) {
                            mModel.setQualityInspectionDetailList(list);
                            mView.setOrderDetailInfo(mModel.getQualityInspectionDetailList().get(0));
                        } else {
                            MessageBox.Show(mContext, "获取质检明细为空" );
                            mView.onAreaNoFocus();
                        }

                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                        mView.onAreaNoFocus();
                    }
                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );
                    mView.onAreaNoFocus();
                }


            }
        });
    }

    /**
     * @desc: 获取质检明细列表
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/12 18:11
     */
    public void getUnQualityInspectionDetailList() {
        mModel.requestQualityInspectionDetail(mModel.getQualityHeaderInfo(), new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(QualityInspectionProcessingScan.class, mModel.TAG_GetT_InStockDetailListByHeaderIDADF, result);
                try {
                    BaseResultInfo<List<QualityHeaderInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<QualityHeaderInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == 1) {
                        List<QualityHeaderInfo> list = returnMsgModel.getData();
                        if (list != null && list.size() > 0) {
                            mModel.setQualityInspectionDetailList(list);
                            mView.setOrderDetailInfo(mModel.getQualityInspectionDetailList().get(0));
                        } else {
                            MessageBox.Show(mContext, "获取质检明细为空" );
                            mView.onAreaNoFocus();
                        }

                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                        mView.onAreaNoFocus();
                    }
                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );
                    mView.onAreaNoFocus();
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
    public void onBarCodeScan(String scanBarcode, int operationType) {
        if (operationType == 1) {
            if (scanBarcode.equals("")) return;
            if (scanBarcode.contains("%")) {  //外箱码
//                scanBarcode(scanBarcode);
                scanPalletNo(scanBarcode);  //托盘码
            } else {
//                scanPalletNo(scanBarcode);  //托盘码
            }
        } else {

        }

    }

    /**
     * @desc: 扫描托盘码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/12 18:43
     */

    private void scanPalletNo(String scanBarcode) {

    }

    /**
     * @desc: 扫描外箱条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/14 17:39
     */
    public void scanBarcode(String scanBarcode) {
        try {
            OutBarcodeInfo scanQRCode = null;
            if (scanBarcode.equals("")) return;
            if (scanBarcode.contains("%")) {
                BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(scanBarcode);
                if (resultInfo.getHeaderStatus()) {
                    scanQRCode = resultInfo.getInfo();
                } else {
                    MessageBox.Show(mContext, resultInfo.getMessage() );
                    return;
                }

            }
            if (scanQRCode != null) {
                //如果外箱码不为空，就弹出物料输入框
                mModel.findOutBarcodeInfoFromMaterial(scanQRCode); //找到物料行,如果找到给外箱码赋值
//                mView.createDialog(scanQRCode);
                mView.onQtyFocus();
            } else {
                MessageBox.Show(mContext, "解析条码失败，条码格式不正确" + scanBarcode );
                mView.onBarcodeFocus();
                return;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, e.getMessage() );
            mView.onBarcodeFocus();
            return;
        }

    }

    public void onRefer() {
        if (mModel.getQualityType().equals(QUALITY_TYPE_QUALIFIED)) {
            onQualifiedOrderRefer();
        } else if (mModel.getQualityType().equals(QUALITY_TYPE_UNQUALIFIED)) {
            onUnQualifiedOrderRefer();
        }
    }


    /**
     * @desc: 质检合格提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/20 15:38
     */
    public void onQualifiedOrderRefer() {
        QualityHeaderInfo info = mModel.getQualityHeaderInfo();
        info.setScanqty(info.getQualityqty());
        info.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
//        List<OutBarcodeInfo> list = new ArrayList<>();
//        OutBarcodeInfo outBarcodeInfo = new OutBarcodeInfo();
//        outBarcodeInfo.setVouchertype(OUT_STOCK_ORDER_TYPE_QUALITY_INSPECTION_VALUE);
//        outBarcodeInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
//        list.add(outBarcodeInfo);
//        info.setLstBarCode(list);
        mModel.requestQualifiedRefer(info, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(QualityInspectionProcessingScan.class, mModel.TAG_PostT_QualityADFAsync, result);
                    BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        mView.onActivityFinish(returnMsgModel.getResultValue());
                    }else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue(),1);
                    }
                } catch (Exception e) {
                    MessageBox.Show(mContext, "出现意料之外的异常:" + e.getMessage() );
                }
            }
        });
    }


    /**
     * @desc: 质检不合格提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/20 15:38
     */
    public void onUnQualifiedOrderRefer() {
        String areaNo = mView.getAreaNo();
        if (areaNo == null || areaNo.equals("")) {
            MessageBox.Show(mContext, "库位号不能为空" );
            return;
        }
        QualityHeaderInfo info = new QualityHeaderInfo();
        List<OutBarcodeInfo> list = new ArrayList<>();
        OutBarcodeInfo outBarcodeInfo = new OutBarcodeInfo();
        info.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
        info.setScanqty(99999999);
        outBarcodeInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
        list.add(outBarcodeInfo);
        info.setLstBarCode(list);
        mModel.requestQualifiedRefer(info, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(QualityInspectionProcessingScan.class, mModel.TAG_PostT_QualityADFAsync, result);
                    BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == 1) {
//                        mModel.checkMaterialInfo(orderDetailInfo, outBarcodeInfo, true);
//                        mView.bindListView(mModel.getQualityInspectionDetailList());
//                        mView.onBarcodeFocus();
                        MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                    }
                } catch (Exception e) {
                    MessageBox.Show(mContext, "出现意料之外的异常:" + e.getMessage() );
                }
            }
        });


    }

    /**
     * @desc:
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/20 15:39
     */
    public void onBarcodeRefer(final OutBarcodeInfo outBarcodeInfo) {
        if (outBarcodeInfo != null) {
            BaseMultiResultInfo<Boolean, QualityHeaderInfo> detailResult = mModel.findMaterialInfo(outBarcodeInfo); //找到物料行
            if (detailResult.getHeaderStatus()) {
                final QualityHeaderInfo orderDetailInfo = detailResult.getInfo();
                BaseMultiResultInfo<Boolean, Void> checkMaterialResult = mModel.checkMaterialInfo(orderDetailInfo, outBarcodeInfo, false); //校验条码是否匹配物料
                if (checkMaterialResult.getHeaderStatus()) {
                    mModel.requestRefer(orderDetailInfo, new NetCallBackListener<String>() {
                        @Override
                        public void onCallBack(String result) {
                            try {
                                LogUtil.WriteLog(QualityInspectionProcessingScan.class, mModel.TAG_GetAreaModelADF, result);
                                BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                                }.getType());
                                if (returnMsgModel.getResult() == 1) {
                                    mModel.checkMaterialInfo(orderDetailInfo, outBarcodeInfo, true);
                                    mView.bindListView(mModel.getQualityInspectionDetailList());
                                    mView.onBarcodeFocus();

                                }
                            } catch (Exception e) {
                                MessageBox.Show(mContext, "出现意料之外的异常:" + e.getMessage() );
                            }
                        }
                    });
                } else {
                    MessageBox.Show(mContext, checkMaterialResult.getMessage() );
                }
            } else {
                MessageBox.Show(mContext, detailResult.getMessage() );
            }
        } else {
            MessageBox.Show(mContext, "外箱信息不能为空" );
        }
    }


}
