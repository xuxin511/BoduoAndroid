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
import com.liansu.boduowms.bean.order.OrderType;
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

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;

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
        info.setStrongholdcode(BaseApplication.mCurrentWareHouseInfo.getStrongholdcode());
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
                            onReset();
                            mModel.setCurrentDetailInfo(list.get(0));
                            mView.setOrderInfo(mModel.getCurrentDetailInfo());
                            mView.onAreaNoFocus();
                        }

                    } else {
                        MessageBox.Show(mContext, "获取单据失败:" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onErpVoucherNoFocus();
                            }
                        });
                    }
                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取单据失败：出现预期之外的异常" + ex.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
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
                        if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                            AreaInfo areaInfo = returnMsgModel.getData();
                            if (areaInfo != null) {
                                mModel.setAreaInfo(areaInfo);
                                mView.onBarcodeFocus();
                            } else {
                                MessageBox.Show(mContext, "获取的库位信息为空", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onAreaNoFocus();
                                    }
                                });
                                return;
                            }
                        } else {
                            MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onAreaNoFocus();
                                }
                            });
                        }
                    } catch (Exception ex) {
                        MessageBox.Show(mContext, "获取库位信息出现预期之外的异常:" + ex.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onAreaNoFocus();
                            }
                        });
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
            mModel.setCurrentBarcodeInfo(null);
            OutBarcodeInfo scanQRCode = null;
            if (scanBarcode.equals("")) return;
            if (scanBarcode.contains("%")) {
                BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(scanBarcode);
                if (resultInfo.getHeaderStatus()) {
                    scanQRCode = resultInfo.getInfo();
                    mView.onQtyFocus();
                } else {
                    MessageBox.Show(mContext, resultInfo.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onBarcodeFocus();
                        }
                    });

                    return;
                }

            }

            if (scanQRCode != null) {
                OutBarcodeInfo postInfo = new OutBarcodeInfo();
                postInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
                postInfo.setBarcode(scanQRCode.getBarcode());
                postInfo.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_RANDOM_INSPECTION_STORAGE_VALUE);
                scanQRCode.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
                scanQRCode.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_RANDOM_INSPECTION_STORAGE_VALUE);
                mModel.requestPalletInfoFromStockQuery2(postInfo, new NetCallBackListener<String>() {
                    @Override
                    public void onCallBack(String result) {
                        try {
                            LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_CHECK_T_PALLET_BARCODE_SYNC, result);
                            BaseResultInfo<List<StockInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<StockInfo>>>() {
                            }.getType());
                            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                if (returnMsgModel.getData() != null && returnMsgModel.getData().size() == 1) {
                                    StockInfo stockInfo = returnMsgModel.getData().get(0);
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
                                            MessageBox.Show(mContext, resultInfo.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mView.onBarcodeFocus();
                                                }
                                            });
                                            return;
                                        }
                                    }
                                } else if (returnMsgModel.getData() != null && returnMsgModel.getData().size() >1) {
                                    MessageBox.Show(mContext, "查询条码失败:不能扫描混托码,请扫描单个托盘码", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.onBarcodeFocus();
                                        }
                                    });
                                } else {
                                    MessageBox.Show(mContext, "查询条码失败:获取条码信息为空" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.onBarcodeFocus();
                                        }
                                    });
                                }


                            } else {
                                MessageBox.Show(mContext, "查询条码失败:" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onBarcodeFocus();
                                    }
                                });

                            }
                        } catch (Exception e) {
                            MessageBox.Show(mContext, "查询条码失败,出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onBarcodeFocus();
                                }
                            });

                        }
                    }
                });

            } else {
                MessageBox.Show(mContext, "解析条码失败，条码格式不正确" + scanBarcode, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                    }
                });
                return;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "扫描条码出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onBarcodeFocus();
                }
            });
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
            MessageBox.Show(mContext, "单据信息不能为空", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onErpVoucherNoFocus();
                }
            });
            return;
        }
        if (mModel.getCurrentBarcodeInfo() == null) {
            MessageBox.Show(mContext, "扫描标签信息为空,请先扫描托盘批次标签", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onBarcodeFocus();
                }
            });
            return;
        }

        float voucherQty = mModel.getCurrentDetailInfo().getVoucherqty();
        float qty = mView.getQty();
        if (qty > voucherQty) {
            MessageBox.Show(mContext, "抽检数量不能大于抽检单数量", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onQtyFocus();
                }
            });
            return;
        }
//        else if (qty < voucherQty) {
//            MessageBox.Show(mContext, "抽检数量必须小于等于抽检单数量", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    mView.onQtyFocus();
//                }
//            });
//            return;
//        }

        if (qty > mModel.getCurrentBarcodeInfo().getQty()) {
            MessageBox.Show(mContext, "抽检数量不能大于库存数量", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onQtyFocus();
                }
            });
            return;
        }
        if (qty <= 0) {
            MessageBox.Show(mContext, "抽检数量必须大于0", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onQtyFocus();
                }
            });
            return;
        }
        float remainQty = ArithUtil.sub(mModel.getCurrentDetailInfo().getVoucherqty(), mModel.getCurrentDetailInfo().getSampqty());
        if (qty > remainQty) {
            MessageBox.Show(mContext, "抽检数量[" + qty + "]不能大于剩余抽检数量[" + remainQty + "]", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onQtyFocus();
                }
            });
            return;
        }

        final QualityHeaderInfo postInfo = mModel.getCurrentDetailInfo();
        postInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
        postInfo.setScanqty(qty);
        mModel.getCurrentBarcodeInfo().setQty(qty);
        List<StockInfo> list = new ArrayList<>();
        list.add(mModel.getCurrentBarcodeInfo());
        postInfo.setLstBarCode(list);
        if (postInfo != null) {
            MessageBox.Show2(mContext,"抽检数量："+qty+"，是否确认提交", MessageBox.MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mModel.requestReferInfo(postInfo, new NetCallBackListener<String>() {
                        @Override
                        public void onCallBack(String result) {
                            try {
                                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_POST_CHECK_QUALITY_SYNC, result);
                                BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                                }.getType());
                                if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                    MessageBox.Show(mContext, returnMsgModel.getResultValue(), 1, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            getOrderDetailInfo();
                                        }
                                    });
                                } else {
                                    MessageBox.Show(mContext, "提交质检信息失败:" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                }
                            } catch (Exception e) {
                                MessageBox.Show(mContext, "提交质检信息失败,出现意料之外的异常:" + e.getMessage(), MEDIA_MUSIC_ERROR);
                            }
                        }
                    });
                }
            });

        } else {

            MessageBox.Show(mContext, "提交质检信息失败,订单信息不能为空:", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onErpVoucherNoFocus();
                }
            });
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
            MessageBox.Show(mContext, "扫描标签信息为空,请先扫描托盘标签", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onBarcodeFocus();
                }
            });
            return;
        }
        onOrderRefer();


//        BaseMultiResultInfo<Boolean, Void> result = mModel.setBarcodeInfo(scanBarcode, scanQty, mModel.getCurrentDetailInfo());
//        if (result.getHeaderStatus()) {
//            mView.setScannedQty(mModel.getScannedQty() + "/" + mModel.getCurrentDetailInfo().getVoucherqty());
//        } else {
//            MessageBox.Show(mContext, result.getMessage(), MEDIA_MUSIC_ERROR);
//        }

    }

    public void onReset() {
        mModel.onReset();
        mView.onReset();
    }
}
