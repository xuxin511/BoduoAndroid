package com.liansu.boduowms.modules.qualityInspection.randomInspection.scan;

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
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/28.
 */
public class QualityInspectionPresenter {
    private Context                mContext;
    private QualityInspectionModel mModel;
    private IQualityInspectionView mView;


    public QualityInspectionPresenter(Context context, IQualityInspectionView view, MyHandler<BaseActivity> handler, QualityHeaderInfo info) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new QualityInspectionModel(context, handler);
        mModel.setOrderHeaderInfo(info);

    }


    protected void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public QualityInspectionModel getModel() {
        return mModel;
    }

    /**
     * @desc: 获取单据表体信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/11 21:39
     */
    public void getOrderDetailInfo() {

        QualityHeaderInfo info = new QualityHeaderInfo();
        info.setId(mModel.getOrderHeaderInfo().getId());
        mModel.requestOrderDetailInfo(info, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_CHECK_QUALITY_DETAIL_LIST_SYNC, result);
                try {
                    BaseResultInfo<List<QualityHeaderInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<QualityHeaderInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        List<QualityHeaderInfo> list = returnMsgModel.getData();
                        if (list != null && list.size() > 0) {
                            mModel.setCurrentDetailInfo(list.get(0));
                            mView.setOrderInfo(mModel.getCurrentDetailInfo());
                            mView.onAreaNoFocus();
                        }

                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), 1);
                        mView.onErpVoucherNoFocus();
                    }
                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );
                    mView.onErpVoucherNoFocus();
                }


            }
        });
    }

    /**
     * @desc: 获取库位信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/11 21:55
     */
    public void getAreaInfo(String areaNo) {
        if (!areaNo.equals("")) {
            mModel.requestAreaInfo(areaNo, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    try {
                        LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GetAreaModelADF, result);
                        BaseResultInfo<AreaInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<AreaInfo>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == 1) {
                            AreaInfo areaInfo = returnMsgModel.getData();
                            if (areaInfo != null) {
                                mModel.setAreaInfo(areaInfo);
                                mView.onBarcodeFocus();
                            } else {
                                MessageBox.Show(mContext, "出现预期之外的异常,获取的库位信息为空" );
                                mView.onAreaNoFocus();
                                return;
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
    }


    /**
     * @desc: 扫描条码 外箱和散件
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
                    mView.onQtyFocus();
                } else {
                    MessageBox.Show(mContext, resultInfo.getMessage() );
                    mView.onBarcodeFocus();
                    return;
                }

            }
            if (scanQRCode != null) {
                mModel.requestPalletInfoFromStockQuery(scanQRCode.getBarcode(), new NetCallBackListener<String>() {
                    @Override
                    public void onCallBack(String result) {
                        try {
                            LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_CHECK_T_PALLET_BARCODE_SYNC, result);
                            BaseResultInfo<StockInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<StockInfo>>() {
                            }.getType());
                            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                StockInfo stockInfo = returnMsgModel.getData();
                                if (stockInfo != null) {
                                    BaseMultiResultInfo<Boolean, QualityHeaderInfo> resultInfo = mModel.findAndCheckMaterialInfo(stockInfo);
                                    if (resultInfo.getHeaderStatus()) {
                                        QualityHeaderInfo materialInfo = resultInfo.getInfo();
                                        if (materialInfo != null) {
                                            mModel.setCurrentBarcodeInfo(stockInfo);
                                            float qty = stockInfo.getQty();
                                            float scannedQty = mModel.getScannedQty();
                                            float remainWty = mModel.getCurrentDetailInfo().getRemainqty();
                                            float remainQty = ArithUtil.sub(remainWty, scannedQty);
                                            if (remainQty >= qty && qty <= remainWty) {
                                                mView.setQty(qty + "");
                                            }


                                        }
                                    } else {
                                        MessageBox.Show(mContext, resultInfo.getMessage() );
                                        mView.onBarcodeFocus();
                                        return;
                                    }
                                }


                            } else {
                                MessageBox.Show(mContext, returnMsgModel.getResultValue() );

                            }
                        } catch (Exception e) {
                            MessageBox.Show(mContext, "出现预期之外的异常:" + e.getMessage() );
                        }
                    }
                });

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


    /**
     * @desc: 提交质检信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/7 15:43
     */
    public void onOrderRefer() {
        if (mModel.getOrderHeaderInfo() == null) {
            MessageBox.Show(mContext, "单据信息不能为空" );
            mView.onErpVoucherNoFocus();
            return;
        }
        if (mModel.getCurrentBarcodeInfo() == null) {
            MessageBox.Show(mContext, "扫描标签信息为空,请先扫描托盘批次标签" );
            mView.onErpVoucherNoFocus();
            return;
        }

        float voucherQty = mModel.getCurrentDetailInfo().getVoucherqty();
        float qty = mModel.getScannedQty();
        if (qty > voucherQty) {
            MessageBox.Show(mContext, "抽检数量不能大于抽检单数量" );
            mView.onErpVoucherNoFocus();
            return;
        } else if (qty < voucherQty) {
            MessageBox.Show(mContext, "抽检数量必须等于抽检单数量" );
            mView.onErpVoucherNoFocus();
            return;
        }


        QualityHeaderInfo postInfo = mModel.getCurrentDetailInfo();
        postInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
        postInfo.setScanqty(qty);
        if (postInfo != null) {
            mModel.requestReferInfo(postInfo, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    try {
                        LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_POST_CHECK_QUALITY_SYNC, result);
                        BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == 1) {
                            MessageBox.Show(mContext, returnMsgModel.getResultValue(), 1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    mView.onActivityFinish();
                                }
                            });
                        }
                    } catch (Exception e) {
                        MessageBox.Show(mContext, "出现意料之外的异常:" + e.getMessage() );
                    }
                }
            });
        } else {

        }
    }


    /**
     * @desc: 添加扫描数量
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/21 17:11
     */
    public void setBarcodeQty() {
        float scanQty = mView.getQty();
        StockInfo scanBarcode = mModel.getCurrentBarcodeInfo();
        if (scanBarcode == null) {
            MessageBox.Show(mContext, "扫描标签信息为空,请先扫描托盘标签" );
            mView.onErpVoucherNoFocus();
            return;
        }
        BaseMultiResultInfo<Boolean, Void> result = mModel.setBarcodeInfo(scanBarcode, scanQty, mModel.getCurrentDetailInfo());
        if (result.getHeaderStatus()) {
            mView.setScannedQty(mModel.getScannedQty() + "/" + mModel.getCurrentDetailInfo().getVoucherqty());
        } else {
            MessageBox.Show(mContext, result.getMessage() );
        }

    }

}
