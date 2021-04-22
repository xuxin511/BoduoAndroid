package com.liansu.boduowms.modules.instock.baseOrderBusiness.scan;

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
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.GUIDHelper;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public abstract class BaseOrderScanPresenter<V extends IBaseOrderScanView, K extends BaseOrderScanModel> {
    protected Context            mContext;
    protected K                  mModel;
    protected V                  mView;
    protected PrintBusinessModel mPrintModel;

   //protected  GUIDHelper mGUIDHelper;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public BaseOrderScanPresenter(Context context, V view, MyHandler<BaseActivity> handler, OrderHeaderInfo orderHeaderInfo, List<OutBarcodeInfo> barCodeInfos, K model) {
        this.mContext = context;
        this.mView = view;
        this.mModel = model;
        this.mModel.setOrderHeaderInfo(orderHeaderInfo);
        this.mModel.setBarCodeInfos(barCodeInfos);
        this.mPrintModel = new PrintBusinessModel(context, handler);
        setHeaderInfo();
    //    mGUIDHelper=new GUIDHelper();
    }
     public GUIDHelper getGUIDHelper(){
      // return mGUIDHelper;
        return  null;
     }
    public K getModel() {
        return mModel;
    }

    protected abstract String getTitle();

    protected void setHeaderInfo() {
        mView.setOrderHeaderInfo(mModel.getOrderHeaderInfo());
    }

    /**
     * @desc: 获取采购订单表体明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:44
     */
    protected void getOrderDetailInfoList() {
    }

    ;

    protected void getOrderDetailInfoList(String erpVoucherNo) {
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
     * @desc: 单据过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 22:37
     */
    protected  void onOrderRefer(){}

//    /**
//     * @desc: 扫描外箱条码
//     * @param:
//     * @return:
//     * @author: Nietzsche
//     * @time 2019/11/14 17:39
//     */
//    public void scanBarcode(String scanBarcode) {
//        try {
////            if (!mPrintModel.checkBluetoothSetting()) {
////                return;
////            }
//            OutBarcodeInfo scanQRCode = null;
//            if (scanBarcode.equals("")) return;
//            if (scanBarcode.contains("%")) {
//                BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(scanBarcode);
//                if (resultInfo.getHeaderStatus()) {
//                    scanQRCode = resultInfo.getInfo();
//                } else {
//                    MessageBox.Show(mContext, resultInfo.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            mView.onBarcodeFocus();
//                        }
//                    });
//                    return;
//                }
//
//            }
//            if (scanQRCode != null) {
//                //如果外箱码不为空，就弹出物料输入框
//                BaseMultiResultInfo<Boolean, Void> resultInfo = mModel.findOutBarcodeInfoFromMaterial(scanQRCode); //找到物料行,如果找到给外箱码赋值
//                if (resultInfo.getHeaderStatus()) {
//                    mView.createDialog(scanQRCode);
//                    mView.onBarcodeFocus();
//                } else {
//                    MessageBox.Show(mContext, resultInfo.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            mView.onBarcodeFocus();
//                        }
//                    });
//                }
//
//            } else {
//                MessageBox.Show(mContext, "解析条码失败，条码格式不正确" + scanBarcode, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mView.onBarcodeFocus();
//                    }
//                });
//
//
//            }
//        } catch (Exception e) {
//            MessageBox.Show(mContext, "解析条码失败,出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    mView.onBarcodeFocus();
//                }
//            });
//
//
//            return;
//        }
//
//    }

    /**
     * @desc: 扫描外箱条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/14 17:39
     */

    public void scanBarcode(String scanBarcode) {
        try {
            if (mModel.getAreaInfo()==null){
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
                scanQRCode.setVouchertype(mModel.getVoucherType());
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
     * @desc: 提交组托信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/7 15:43
     */
    public void onCombinePalletRefer(final OutBarcodeInfo outBarcodeInfo) {
        if (outBarcodeInfo != null) {
//            outBarcodeInfo.setVouchertype(mModel.getVoucherType());
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
            }else {
                MessageBox.Show(mContext,detailResult.getMessage() , MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
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
     * @desc: 重置界面
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/24 15:43
     */
    public void onReset() {
        mModel.onReset();
        mView.onReset();
    }


    public void onResume(){}

//    /**
//     * @desc: 提交组托信息
//     * @param:
//     * @return:
//     * @author: Nietzsche
//     * @time 2020/7/7 15:43
//     */
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
//                                    MessageBox.Show(mContext, "提交条码信息失败:" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
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
//    }
}
