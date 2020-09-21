package com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
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
 * @ Created by yangyiqing on 2020/6/27.
 */
public class InStockReturnsStorageScanPresenter<V extends IInStockReturnStorageScanView, K extends InStockReturnsStorageScanModel> {
    protected Context            mContext;
    protected K                  mModel;
    protected V                  mView;
    protected PrintBusinessModel mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
        switch (msg.what) {
            case NetworkError.NET_ERROR_CUSTOM:
                MessageBox.Show(mContext, "获取请求失败_____" + msg.obj, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
        }
    }

    public InStockReturnsStorageScanPresenter(Context context, V view, MyHandler<BaseActivity> handler, K model) {
        this.mContext = context;
        this.mView = view;
        this.mModel = model;
        this.mPrintModel = new PrintBusinessModel(context, handler);
    }

    public K getModel() {
        return mModel;
    }


    /**
     * @desc: 获取库位信息 新托盘类型
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 15:31
     */
    public void getAreaInfo(String areaNo) {
        if (areaNo.equals("")) return;
        final int palletType = mView.getPalletType();
        AreaInfo areaInfo = new AreaInfo();
        areaInfo.setAreano(areaNo);
        areaInfo.setWarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
        mModel.requestAreaNo(areaInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_AREA_MODEL, result);
                try {
                    BaseResultInfo<AreaInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<AreaInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        AreaInfo data = returnMsgModel.getData();
                        if (data != null) {
                            mModel.setAreaInfo(data);
                            if (palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_NEW_PALLET) {
                                mView.onPalletNoFocus();
                            }
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
     * @desc: 获取订单明细  有源业务
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 22:33
     */
    protected void getOrderDetailInfoList(String erpVoucherNo) {
        onReset();
        OrderRequestInfo orderRequestInfo = new OrderRequestInfo();
        orderRequestInfo.setErpvoucherno(erpVoucherNo);
        orderRequestInfo.setVouchertype(mModel.getVoucherType());
        orderRequestInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        mModel.requestOrderDetail(orderRequestInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(InStockReturnStorageScan.class, mModel.TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC, result);
                try {
                    int palletType = mView.getPalletType();
                    BaseResultInfo<OrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OrderHeaderInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        OrderHeaderInfo orderHeaderInfo = returnMsgModel.getData();
                        if (orderHeaderInfo != null) {
                            mModel.setOrderDetailList(orderHeaderInfo.getDetail());
                            mModel.setOrderHeaderInfo(orderHeaderInfo);
                            if (mModel.getOrderDetailList().size() > 0) {
                                mView.setOrderHeaderInfo(orderHeaderInfo);
                                mView.bindListView(mModel.getOrderDetailList());
                                if (palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_OLD_PALLET) {
                                    mView.onPalletNoFocus();
                                } else if (palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_NEW_PALLET) {
                                    mView.onAreaNoFocus();
                                }
                            } else {
                                mView.onErpVoucherNoFocus();
                                MessageBox.Show(mContext, "获取单据失败:获取表体信息为空");
                            }
                        } else {
                            mView.onErpVoucherNoFocus();
                            MessageBox.Show(mContext, "获取单据失败:" + returnMsgModel.getResultValue());

                        }
                    } else {
                        mView.onErpVoucherNoFocus();
                        MessageBox.Show(mContext, "获取单据失败:" + returnMsgModel.getResultValue());

                    }

                } catch (Exception ex) {
                    mView.onErpVoucherNoFocus();
                    MessageBox.Show(mContext, "获取单据失败:出现预期之外的异常-" + ex.getMessage());

                }


            }
        });
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
            mModel.setCurrentScanInfo(null);
            final int palletType = mView.getPalletType();
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
                mModel.requestBarcodeInfo(scanQRCode, new NetCallBackListener<String>() {
                    @Override
                    public void onCallBack(String result) {
                        LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, result);
                        try {
                            BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutBarcodeInfo>>() {
                            }.getType());
                            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                OutBarcodeInfo outBarcodeInfo = returnMsgModel.getData();
                                if (outBarcodeInfo != null) {
                                    if (mModel.getOrderHeaderInfo() != null) {
                                        outBarcodeInfo.setSupplierno(mModel.getOrderHeaderInfo().getSupplierno());
                                        outBarcodeInfo.setSuppliername(mModel.getOrderHeaderInfo().getSuppliername());
                                    }
                                    if (palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_OLD_PALLET) {
                                        mModel.setCurrentScanInfo(outBarcodeInfo);
                                        mView.onBarcodeFocus();
                                    } else if (palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_NEW_PALLET) {
                                        onActiveCombinePalletRefer(outBarcodeInfo);
                                    }

                                } else {
                                    MessageBox.Show(mContext, "查询托盘码失败:获取的托盘数据为空," + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.onBarcodeFocus();
                                        }
                                    });
                                }
                            } else {
                                MessageBox.Show(mContext, "查询托盘码失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onBarcodeFocus();
                                    }
                                });
                            }

                        } catch (Exception ex) {
                            MessageBox.Show(mContext, "查询托盘码失败，出现预期之外的异常-" + ex.getMessage() + ",", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onBarcodeFocus();
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
     * @desc: 扫描外箱条码 无源类型
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/14 17:39
     */

    public void scanOuterBoxBarcode(String scanBarcode) {
        try {
            if (mModel.getCurrentScanInfo() == null) {
                MessageBox.Show(mContext, "校验托盘失败:获取托盘信息为空,请先扫描托盘号", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onPalletNoFocus();
                    }
                });
                return;
            }
            String areaNo = mModel.getCurrentScanInfo().getAreano();
            if (areaNo == null || areaNo.equals("")) {
                MessageBox.Show(mContext, "校验库位失败:获取库位信息为空,请先扫描托盘号", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onPalletNoFocus();
                    }
                });
                return;
            }
            OutBarcodeInfo scanQRCode = null;
            if (scanBarcode.equals("")) return;
            if (scanBarcode.contains("%")) {
                BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(scanBarcode);
                if (resultInfo.getHeaderStatus()) {
                    scanQRCode = resultInfo.getInfo();
                    if (scanQRCode.getBarcodetype() != QRCodeFunc.BARCODE_TYPE_OUTER_BOX) {
                        MessageBox.Show(mContext, "校验条码失败:外箱格式不正确,请扫描外箱码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
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
                MessageBox.Show(mContext, "校验条码失败:外箱格式不正确,请扫描外箱码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                    }
                });
                return;
            }
            if (scanQRCode != null) {
                mModel.requestBarcodeInfo(scanQRCode, new NetCallBackListener<String>() {
                    @Override
                    public void onCallBack(String result) {
                        LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, result);
                        try {
                            BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutBarcodeInfo>>() {
                            }.getType());
                            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                OutBarcodeInfo outBarcodeInfo = returnMsgModel.getData();
                                if (outBarcodeInfo != null) {
                                    outBarcodeInfo.setSupplierno(mModel.getOrderHeaderInfo().getSupplierno());
                                    outBarcodeInfo.setSuppliername(mModel.getOrderHeaderInfo().getSuppliername());
                                    String areaNo = outBarcodeInfo.getAreano();
                                    if (areaNo != null && !areaNo.equals("")) {
                                        onActiveCombinePalletRefer(outBarcodeInfo);
                                    }


                                } else {
                                    MessageBox.Show(mContext, "查询托盘码失败:获取的托盘数据为空," + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.onBarcodeFocus();
                                        }
                                    });
                                }
                            } else {
                                MessageBox.Show(mContext, "查询托盘码失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onBarcodeFocus();
                                    }
                                });
                            }

                        } catch (Exception ex) {
                            MessageBox.Show(mContext, "查询托盘码失败，出现预期之外的异常-" + ex.getMessage() + ",", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onBarcodeFocus();
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
     * @desc: 有源退货  新托盘提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/20 23:13
     */
    public void onActiveCombinePalletRefer(final OutBarcodeInfo palletInfo) {
        if (palletInfo != null) {
            palletInfo.setAreano(mView.getAreaNo());
            palletInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
            palletInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
            BaseMultiResultInfo<Boolean, List<OrderDetailInfo>> detailResult = mModel.findMaterialInfoList(palletInfo); //找到物料行
            if (detailResult.getHeaderStatus()) {
                List<OrderDetailInfo> postList = detailResult.getInfo();
                for (OrderDetailInfo orderDetailInfo : postList) {
                    orderDetailInfo.setSupplierno(mModel.getOrderHeaderInfo().getSupplierno());
                    orderDetailInfo.setSuppliername(mModel.getOrderHeaderInfo().getSuppliername());
                    orderDetailInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
                    orderDetailInfo.setUsername(BaseApplication.mCurrentUserInfo.getUsername());
                    orderDetailInfo.setDepartmentcode(mModel.getOrderHeaderInfo().getDepartmentcode());
                    orderDetailInfo.setDepartmentname(mModel.getOrderHeaderInfo().getDepartmentname());
                    orderDetailInfo.setOnwayWarehouse(mModel.getOrderHeaderInfo().getOnwayWarehouse());
                    if (UrlInfo.mInStockPrintType == PrintBusinessModel.PRINTER_TYPE_LASER) {
                        orderDetailInfo.setPrinterName(UrlInfo.mLaserPrinterAddress);
                    } else if (UrlInfo.mInStockPrintType == PrintBusinessModel.PRINTER_TYPE_DESKTOP) {
                        orderDetailInfo.setPrinterName(UrlInfo.mDesktopPrintAddress);
                    }

                    orderDetailInfo.setPrinterType(UrlInfo.mInStockPrintType);
                }


                mModel.requestCombineAndReferPallet(postList, new NetCallBackListener<String>() {
                    @Override
                    public void onCallBack(String result) {
                        try {
                            LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_COMBINE_AND_REFER_PALLET_SUB, result);
                            BaseResultInfo<List<OrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OrderDetailInfo>>>() {
                            }.getType());
                            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                List<OrderDetailInfo> resultList = returnMsgModel.getData();
                                //返回的是物料标签条码，现在要打外箱码
//                                    outBarcodeInfo.setBarcode(barcode);
                                for (OrderDetailInfo info : resultList) {
                                    mModel.updateMaterialInfo(info);
                                }
                                mModel.sortDetailList(resultList.get(0).getMaterialno());
                                mView.bindListView(mModel.getOrderDetailList());
                                Toast.makeText(mContext, returnMsgModel.getResultValue(), Toast.LENGTH_SHORT).show();
                                mView.onBarcodeFocus();
                            } else {
                                MessageBox.Show(mContext, "提交条码信息失败:" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onBarcodeFocus();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            MessageBox.Show(mContext, "提交条码信息失败,出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onBarcodeFocus();
                                }
                            });
                        }
                    }
                });
            }
        } else {
            MessageBox.Show(mContext, "托盘信息不能为空", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onBarcodeFocus();
                }
            });
        }
    }


    /**
     * @desc: 有源提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 22:37
     */

    protected void onActiveOrderRefer() {
        if (mModel.getOrderDetailList() == null) {
            MessageBox.Show(mContext, "校验单据信息失败:单据信息为空,请先扫描单据", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onErpVoucherNoFocus();
                }
            });
            return;
        }
        OrderDetailInfo firstDetailInfo = mModel.getOrderDetailList().get(0);
        if (firstDetailInfo != null) {
            OrderDetailInfo postInfo = new OrderDetailInfo();
            postInfo.setErpvoucherno(firstDetailInfo.getErpvoucherno());
            postInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
            postInfo.setUsername(BaseApplication.mCurrentUserInfo.getUsername());
            postInfo.setVouchertype(firstDetailInfo.getVouchertype());
            List<OrderDetailInfo> list = new ArrayList<>();
            list.add(postInfo);
            mModel.requestOrderRefer(list, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    LogUtil.WriteLog(InStockReturnStorageScan.class, mModel.TAG_POST_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC, result);
                    try {
                        BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == BaseResultInfo.RESULT_TYPE_OK) {
                            BaseMultiResultInfo<Boolean, Void> checkResult = mModel.isOrderScanFinished();
                            if (!checkResult.getHeaderStatus()) {
                                MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getOrderDetailInfoList(mView.getErpVoucherNo());
                                    }
                                });
                            } else {
                                MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onReset();
                                    }
                                });
//                                mView.onActivityFinish(checkResult.getMessage());
                            }
                        } else {
                            MessageBox.Show(mContext, returnMsgModel.getResultValue());
                        }

                    } catch (Exception ex) {
                        MessageBox.Show(mContext, ex.getMessage());
                    }


                }
            });
        }
    }

    /**
     * @desc: 提交数据 无源提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/2 11:10
     */
    protected void onNoSourceOrderRefer() {
        List<OutBarcodeInfo> list = mModel.getBarCodeInfos();
        if (list == null || list.size() == 0) {
            MessageBox.Show(mContext, "扫描数据为空!请先进行扫描操作");
            return;
        }
        mModel.requestNoSourceOrderRefer(list, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, " mModel.TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC", result);
                try {
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

                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage());
                }


            }
        });
    }

    /**
     * @desc: 同步activity 的生命周期中onResume()
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/20 20:37
     */
    public void onResume() {
        int palletType=mView.getPalletType();
//        if (palletType==)
        mView.onErpVoucherNoFocus();
    }

    /**
     * @desc: 选择不同的托盘方式
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/20 21:54
     */
    public void changePalletType() {
        int palletType = mView.getPalletType();
        mView.initPalletChangedViewStatus(palletType);


    }

    public void onReset() {
        mModel.onReset();
        mView.onReset();
    }
}
