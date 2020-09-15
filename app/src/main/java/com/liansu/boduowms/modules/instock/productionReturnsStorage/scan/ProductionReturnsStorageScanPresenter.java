package com.liansu.boduowms.modules.instock.productionReturnsStorage.scan;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
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
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScanPresenter;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
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
public class ProductionReturnsStorageScanPresenter extends BaseOrderScanPresenter<IProductReturnStorageScanView, ProductionReturnsStorageScanModel> {

    public ProductionReturnsStorageScanPresenter(Context context, IProductReturnStorageScanView view, MyHandler<BaseActivity> handler, OrderHeaderInfo orderHeaderInfo, List<OutBarcodeInfo> barCodeInfos) {
        super(context, view, handler, orderHeaderInfo, barCodeInfos, new ProductionReturnsStorageScanModel(context, handler));
    }

    @Override
    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    @Override
    protected String getTitle() {
        return mContext.getResources().getString(R.string.appbar_title_production_returns_scan) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
    }


    /**
     * @desc: 获取库位信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 15:31
     */
    public void getAreaInfo(String areaNo) {
        if (areaNo.equals("")) return;
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
                            mView.onBarcodeFocus();
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
     * @desc: 获取订单明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 22:33
     */
    @Override
    protected void getOrderDetailInfoList(String erpVoucherNo) {
        onReset();
        OrderRequestInfo orderRequestInfo = new OrderRequestInfo();
        orderRequestInfo.setErpvoucherno(erpVoucherNo);
        orderRequestInfo.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE_VALUE);
        orderRequestInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        mModel.requestOrderDetail(orderRequestInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(ProductionReturnStorageScan.class, mModel.TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC, result);
                try {

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
                                mView.onAreaNoFocus();
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
            OutBarcodeInfo scanQRCode = null;
            if (scanBarcode.equals("")) return;
            if (scanBarcode.contains("%")) {
                BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(scanBarcode);
                if (resultInfo.getHeaderStatus()) {
                    scanQRCode = resultInfo.getInfo();
                    if (scanQRCode.getBarcodetype() != QRCodeFunc.BARCODE_TYPE_PALLET_NO) {
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
                                    outBarcodeInfo.setSupplierno(mModel.getOrderHeaderInfo().getSupplierno());
                                    outBarcodeInfo.setSuppliername(mModel.getOrderHeaderInfo().getSuppliername());
                                    onCombinePalletRefer(outBarcodeInfo);
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
     * @desc: 扫描外箱条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/14 17:39
     */

    public void scanOuterBoxBarcode(String scanBarcode) {
        try {
            if (mModel.getAreaInfo() == null) {
                MessageBox.Show(mContext, "校验库位失败:获取库位信息为空,请先扫描库位", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onAreaNoFocus();
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
                    if (scanQRCode.getBarcodetype() != QRCodeFunc.BARCODE_TYPE_PALLET_NO) {
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
                                    outBarcodeInfo.setSupplierno(mModel.getOrderHeaderInfo().getSupplierno());
                                    outBarcodeInfo.setSuppliername(mModel.getOrderHeaderInfo().getSuppliername());
                                    String areaNo=outBarcodeInfo.getAreano();
                                    if (areaNo!=null && !areaNo.equals("")){
                                        onCombinePalletRefer(outBarcodeInfo);
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

    @Override
    public void onCombinePalletRefer(final OutBarcodeInfo outBarcodeInfo) {
        if (outBarcodeInfo != null) {
            outBarcodeInfo.setAreano(mView.getAreaNo());
            outBarcodeInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
            outBarcodeInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
            BaseMultiResultInfo<Boolean, List<OrderDetailInfo>> detailResult = mModel.findMaterialInfoList(outBarcodeInfo); //找到物料行
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


//    @Override
//    public void onCombinePalletRefer(final OutBarcodeInfo outBarcodeInfo) {
//        if (outBarcodeInfo != null) {
//            outBarcodeInfo.setAreano(mView.getAreaNo());
//            outBarcodeInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
//            outBarcodeInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
//            BaseMultiResultInfo<Boolean, OrderDetailInfo> detailResult = mModel.findMaterialInfo(outBarcodeInfo); //找到物料行
//            if (detailResult.getHeaderStatus()) {
//                final OrderDetailInfo orderDetailInfo = detailResult.getInfo();
//                BaseMultiResultInfo<Boolean, Void> checkMaterialResult = mModel.checkMaterialInfo(orderDetailInfo, outBarcodeInfo, false); //校验条码是否匹配物料
//                if (checkMaterialResult.getHeaderStatus()) {
//                    orderDetailInfo.setSupplierno(mModel.getOrderHeaderInfo().getSupplierno());
//                    orderDetailInfo.setSuppliername(mModel.getOrderHeaderInfo().getSuppliername());
//                    orderDetailInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
//                    orderDetailInfo.setUsername(BaseApplication.mCurrentUserInfo.getUsername());
//                    if (UrlInfo.mInStockPrintType == PrintBusinessModel.PRINTER_TYPE_LASER) {
//                        orderDetailInfo.setPrinterName(UrlInfo.mLaserPrinterAddress);
//                    } else if (UrlInfo.mInStockPrintType == PrintBusinessModel.PRINTER_TYPE_DESKTOP) {
//                        orderDetailInfo.setPrinterName(UrlInfo.mDesktopPrintAddress);
//                    }
//
//                    orderDetailInfo.setPrinterType(UrlInfo.mInStockPrintType);
//                    mModel.requestCombineAndReferPallet(orderDetailInfo, new NetCallBackListener<String>() {
//                        @Override
//                        public void onCallBack(String result) {
//                            try {
//                                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_COMBINE_AND_REFER_PALLET_SUB, result);
//                                BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
//                                }.getType());
//                                if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
//                                    String barcode = returnMsgModel.getData();
//                                    //返回的是物料标签条码，现在要打外箱码
////                                    outBarcodeInfo.setBarcode(barcode);
//                                    mModel.checkMaterialInfo(orderDetailInfo, outBarcodeInfo, true);
//                                    mView.bindListView(mModel.getOrderDetailList());
//                                    PrintInfo printInfo = mModel.getPrintModel(outBarcodeInfo);
//                                    if (printInfo != null) {
//                                        mPrintModel.onPrint(printInfo);
//
//                                    }
//                                    mView.onBarcodeFocus();
//                                } else {
//                                    MessageBox.Show(mContext,"提交条码信息失败:"+ returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            mView.onBarcodeFocus();
//                                        }
//                                    });
//                                }
//                            } catch (Exception e) {
//                                MessageBox.Show(mContext, "提交条码信息失败,出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        mView.onBarcodeFocus();
//                                    }
//                                });
//                            }
//                        }
//                    });
//                } else {
//                    MessageBox.Show(mContext, checkMaterialResult.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            mView.onBarcodeFocus();
//                        }
//                    });
//                }
//            } else {
//                MessageBox.Show(mContext, detailResult.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mView.onBarcodeFocus();
//                    }
//                });
//            }
//        } else {
//            MessageBox.Show(mContext, "外箱信息不能为空", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    mView.onBarcodeFocus();
//                }
//            });
//        }
//
//    }

    /**
     * @desc: 生产退料过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 22:37
     */
    @Override
    protected void onOrderRefer() {
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
                    LogUtil.WriteLog(ProductionReturnStorageScan.class, mModel.TAG_POST_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC, result);
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

    @Override
    public void onResume() {
        super.onResume();
        mView.onErpVoucherNoFocus();
    }
}
