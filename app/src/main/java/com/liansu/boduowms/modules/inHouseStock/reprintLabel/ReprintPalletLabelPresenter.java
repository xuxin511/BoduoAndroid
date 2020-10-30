package com.liansu.boduowms.modules.inHouseStock.reprintLabel;

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
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan.InStockReturnStorageScan;
import com.liansu.boduowms.modules.instock.batchPrint.print.BaseOrderLabelPrint;
import com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan.PurchaseInspectionProcessingScan;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import androidx.annotation.NonNull;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;


/**
 * @ Des:
 * @ Created by yangyiqing on 2019/11/14.
 */
public class ReprintPalletLabelPresenter {
    private Context                 mContext;
    private ReprintPalletLabelModel mModel;
    private IReprintPalletLabelView mView;
    private MyHandler<BaseActivity> mHandler;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);

    }

    public ReprintPalletLabelPresenter(Context context, IReprintPalletLabelView view, MyHandler<BaseActivity> handler,int voucherType) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new ReprintPalletLabelModel(context, handler);
        this.mModel.setVoucherType(voucherType);
        this.mHandler = handler;
    }


    public ReprintPalletLabelModel getModel() {
        return mModel;
    }


    /**
     * @desc: 扫描托盘码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/11 16:21
     */
    public void scanPalletBarcode(String scanBarcode) {
        try {
            OutBarcodeInfo scanQRCode = null;
            if (scanBarcode.equals("")) return;
            if (scanBarcode.contains("%")) {
                BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(scanBarcode);
                if (resultInfo.getHeaderStatus()) {
                    scanQRCode = resultInfo.getInfo();
                    if (!(scanQRCode.getBarcodetype() == QRCodeFunc.BARCODE_TYPE_PALLET_NO || scanQRCode.getBarcodetype() == QRCodeFunc.BARCODE_TYPE_MIXING_PALLET_NO)) {
                        MessageBox.Show(mContext, "校验条码失败:托盘格式不正确,请扫描托盘码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onBarcodeFocus();
                            }
                        });
                        return;
                    }
                } else {
                    MessageBox.Show(mContext, resultInfo.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onBarcodeFocus();
                        }
                    });
                    return;
                }

            } else {
                MessageBox.Show(mContext, "校验条码失败:托盘格式不正确,请扫描托盘码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                    }
                });
                return;
            }
            if (scanQRCode != null) {
                StockInfo postInfo = new StockInfo();
                postInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
                postInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
                postInfo.setBarcode(scanQRCode.getBarcode());
                postInfo.setSerialno(scanQRCode.getSerialno());
                mModel.requestStockInfoListQuery(postInfo, new NetCallBackListener<String>() {
                    @Override
                    public void onCallBack(String result) {
                        LogUtil.WriteLog(ReprintPalletLabelPresenter.class, mModel.TAG_STOCK_INFO_LIST_QUERY, result);
                        try {
                            BaseResultInfo<List<StockInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<StockInfo>>>() {
                            }.getType());
                            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                final List<StockInfo> data = returnMsgModel.getData();
                                if (data != null) {
                                    mModel.setStockInfoList(data);
                                    mView.bindListView(mModel.getStockInfoList());
                                    mView.onBarcodeFocus();
                                } else {
                                    MessageBox.Show(mContext, "查询的库存信息为空:", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mModel.setStockInfoList(null);
                                            mView.bindListView(mModel.getStockInfoList());
                                            mView.onBarcodeFocus();
                                        }
                                    });

                                }
                            } else {
                                MessageBox.Show(mContext, "查询的库存信息失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mModel.setStockInfoList(null);
                                        mView.bindListView(mModel.getStockInfoList());
                                        mView.onBarcodeFocus();
                                    }
                                });

                            }

                        } catch (Exception ex) {
                            MessageBox.Show(mContext, "查询的库存信息失败,出现预期之外的异常，" + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mModel.setStockInfoList(null);
                                    mView.bindListView(mModel.getStockInfoList());
                                    mView.onBarcodeFocus();
                                    ;
                                }
                            });

                        }
                    }
                });

            } else {
                MessageBox.Show(mContext, "解析条码失败，条码格式不正确" + scanBarcode, MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                        ;
                    }
                });
                return;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "查询条码失败，出现预期之外的异常:" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onBarcodeFocus();
                }
            });
            return;
        }

    }


    /**
     * @desc: 扫描物料
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/14 17:39
     */

    public void scanMaterialNo(String scanBarcode) {
        //扫描新物料前 清空旧数据
        mModel.setMaterialInfo(null);
        try {
            OutBarcodeInfo scanQRCode = null;
            if (scanBarcode.equals("")) return;
            BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(scanBarcode);
            if (resultInfo.getHeaderStatus()) {
                scanQRCode = resultInfo.getInfo();
                if (scanQRCode != null) {
                    if (scanQRCode.getMaterialno() == null || scanQRCode.getMaterialno().equals("")) {
                        MessageBox.Show(mContext, "校验物料失败:获取的物料编码不能为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onBarcodeFocus();
                            }
                        });
                        return;
                    }

                }
            } else {
                MessageBox.Show(mContext, resultInfo.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                    }
                });
                return;
            }
            if (scanQRCode != null) {
                //校验条码是否存在
                onMaterialInfoQuery(scanQRCode);
            } else {
                MessageBox.Show(mContext, "解析物料失败，物料格式不正确" + scanBarcode, MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                    }
                });
                return;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "查询物料失败，出现预期之外的异常:" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onBarcodeFocus();
                }
            });
            return;
        }

    }

    /**
     * @desc: 根据物料编码查询物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 15:40
     */
    public void onMaterialInfoQuery(final OutBarcodeInfo scanQRCode) {
        mModel.requestMaterialInfoQuery(scanQRCode.getMaterialno(), new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, mModel.TAG_SELECT_MATERIAL, result);
                    BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutBarcodeInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK || returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK && returnMsgModel.getData() != null) {
                        OutBarcodeInfo materialInfo = returnMsgModel.getData();
                        if (materialInfo != null) {
                            scanQRCode.setMaterialno(materialInfo.getMaterialno());
                            scanQRCode.setMaterialdesc(materialInfo.getMaterialdesc());
                            scanQRCode.setPackqty(materialInfo.getPackqty());
                            scanQRCode.setSpec(materialInfo.getSpec());
                            scanQRCode.setUnit(materialInfo.getUnit());
                            scanQRCode.setUnitname(materialInfo.getUnitname());
                            mModel.setMaterialInfo(scanQRCode);
                            String batchNo = scanQRCode.getBatchno();
                            if (batchNo != null && !batchNo.equals("")) {
                                mView.setBatchNo(batchNo);
                                mView.onAreaNoFocus();
                            } else {
                                mView.onBatchNoFocus();
                            }

                        }


                    } else {
                        MessageBox.Show(mContext, "查询物料失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onMaterialNoFocus();
                            }
                        });
                        return;
                    }
                } catch (Exception e) {
                    MessageBox.Show(mContext, "查询物料失败:出现预期之外的异常-" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onMaterialNoFocus();
                        }
                    });

                }
            }


        });
    }


    /**
     * @desc: 获取库位信息 新托盘类型
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 15:31
     */
    public void scanAreaInfo(String areaNo) {
        if (areaNo.equals("")) return;
        AreaInfo areaInfo = new AreaInfo();
        areaInfo.setAreano(areaNo);
        areaInfo.setWarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
        mModel.requestAreaNo(areaInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(InStockReturnStorageScan.class, mModel.TAG_GET_T_AREA_MODEL, result);
                try {
                    BaseResultInfo<AreaInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<AreaInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        AreaInfo data = returnMsgModel.getData();
                        if (data != null) {
                            mModel.setAreaNo(data);
                        } else {
                            MessageBox.Show(mContext, "获取的库位信息为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onAreaNoFocus();
                                }
                            });

                        }
                    } else {
                        MessageBox.Show(mContext, "获取的库位信息失败，" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onAreaNoFocus();
                            }
                        });

                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取的库位信息出现预期之外的异常，" + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onAreaNoFocus();
                        }
                    });

                }


            }
        });

    }

    /**
     * @desc: 库存补打查询
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/21 10:59
     */
    public void onQueryInfoRefer() {
        String barcode = mView.getBarcode();
        OutBarcodeInfo materialInfo = mModel.getMaterialInfo();
        String batchNo = mView.getBatchNo();
        AreaInfo areaInfo = mModel.getAreaNo();
        if (barcode.equals("") && materialInfo == null && batchNo.equals("") && areaInfo == null) {
            MessageBox.Show(mContext, "校验库存补打查询失败：提交的查询信息为空,请先进行扫描操作", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onBarcodeFocus();
                }
            });
            return;
        }


        StockInfo postInfo = new StockInfo();
        if (barcode != null && !barcode.equals("")) {
            postInfo.setBarcode(barcode);
        } else {
            if (materialInfo != null && materialInfo.getMaterialno() != null) {
                postInfo.setMaterialno(materialInfo.getMaterialno());
            }

            if (!batchNo.equals("")) {
                if (!mView.checkBatchNo(batchNo)) {
                    return;
                } else {
                    postInfo.setBatchno(batchNo);
                }
            }

            if (areaInfo != null) {
                postInfo.setAreano(areaInfo.getAreano());
            }
        }


        postInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
        postInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        mModel.requestStockInfoListQuery(postInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(ReprintPalletLabelPresenter.class, mModel.TAG_STOCK_INFO_LIST_QUERY, result);
                try {
                    BaseResultInfo<List<StockInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<StockInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        onClear();
                        final List<StockInfo> data = returnMsgModel.getData();
                        if (data != null) {
                            mModel.setStockInfoList(data);
                            mView.bindListView(mModel.getStockInfoList());
                            mView.onMaterialNoFocus();
                        } else {
                            MessageBox.Show(mContext, "查询的库存信息为空:", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mModel.setStockInfoList(null);
                                    mView.bindListView(mModel.getStockInfoList());
                                    mView.onMaterialNoFocus();
                                }
                            });

                        }
                    } else {
                        MessageBox.Show(mContext, "查询的库存信息失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mModel.setStockInfoList(null);
                                mView.bindListView(mModel.getStockInfoList());
                                mView.onMaterialNoFocus();
                            }
                        });

                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "查询的库存信息失败,出现预期之外的异常，" + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mModel.setStockInfoList(null);
                            mView.bindListView(mModel.getStockInfoList());
                            mView.onMaterialNoFocus();
                            ;
                        }
                    });

                }
            }
        });
    }

    /**
     * @desc: 打印条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/10/22 11:12
     */
    public void onPrint(StockInfo stockInfo) {
        try {
            if (stockInfo == null) {
                MessageBox.Show(mContext, "校验托盘补打信息失败:打印信息为空,请选择托盘码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                return;
            }
            if (stockInfo != null) {
                final OutBarcodeInfo postInfo = new OutBarcodeInfo();
                postInfo.setQty(stockInfo.getQty());
                postInfo.setBarcode(stockInfo.getBarcode());
                postInfo.setSerialno(stockInfo.getSerialno());
                postInfo.setBatchno(stockInfo.getBatchno());
                postInfo.setMaterialno(stockInfo.getMaterialno());
                postInfo.setUsername(BaseApplication.mCurrentUserInfo.getUsername());
                postInfo.setPrintertype(UrlInfo.mInStockPrintType);
                postInfo.setPrintername(UrlInfo.mInStockPrintName);
                postInfo.setVouchertype(mModel.getVoucherType());
                MessageBox.Show2(mContext, "是否确定打印托盘码,打印张数：1", MessageBox.MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestReprintPalletInfo(postInfo);
                    }
                });
            }

        } catch (Exception e) {
            MessageBox.Show(mContext, "提交托盘补打信息失败,出现预期之外的异常:" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

    }

    /**
     * @desc: 提交补打信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/10/29 13:47
     */
    private void requestReprintPalletInfo(@NonNull OutBarcodeInfo postInfo) {
        if (postInfo != null) {
            mModel.requestReprintPalletInfo(postInfo, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    try {
                        LogUtil.WriteLog(BaseOrderLabelPrint.class, mModel.TAG_REPRINT_PALLET_LABEL, result);
                        BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                            ToastUtil.show("托盘补打成功!");
                        } else {
                            MessageBox.Show(mContext, "提交托盘补打信息失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    listViewClear();
                                }
                            });
                        }
                    } catch (Exception e) {
                        MessageBox.Show(mContext, "提交托盘补打信息失败,出现预期之外的异常:" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listViewClear();
                            }
                        });
                    }


                }
            });
        }
    }

    public void onClear() {
        mModel.onClear();
        mView.onReset();

    }


    public void listViewClear() {
        mModel.setStockInfoList(null);
        mView.bindListView(mModel.getStockInfoList());
    }


}
