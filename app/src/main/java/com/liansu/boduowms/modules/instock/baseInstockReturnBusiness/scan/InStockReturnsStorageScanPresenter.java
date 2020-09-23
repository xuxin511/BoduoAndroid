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
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan.PurchaseInspectionProcessingScan;
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
 * @ Des:  入库退货基类
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
                LogUtil.WriteLog(InStockReturnStorageScan.class, mModel.TAG_GET_T_AREA_MODEL, result);
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
        onReset(false);
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
            final int palletType = mView.getPalletType();
            if (mModel.getBusinessType() == InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_ACTIVE) {
                if (mModel.getOrderHeaderInfo() == null) {
                    mView.onErpVoucherNoFocus();
                    MessageBox.Show(mContext, "获取单据失败:获取单据信息为空,请扫描单据");
                    return;
                }
            }
            if (mModel.getAreaInfo() == null && palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_NEW_PALLET) {
                MessageBox.Show(mContext, "获取的库位信息为空,请扫描库位", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onAreaNoFocus();
                    }
                });
                return;
            }

            //扫描新托盘码前 清空旧数据
            mModel.setCurrentScanInfo(null);
            mModel.setCurrentOuterBoxInfo(null);

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
                if (palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_OLD_PALLET) {
                    scanQRCode.setScantype(1);
                }
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
                                        mView.setAreaNo(outBarcodeInfo.getAreano());
                                        mModel.setCurrentScanInfo(outBarcodeInfo);
                                        mView.onBarcodeFocus();
                                    } else if (palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_NEW_PALLET) {
                                        //库位信息
                                        outBarcodeInfo.setAreano(mModel.getAreaInfo().getAreano());
                                        outBarcodeInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
                                        outBarcodeInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
                                        onActiveCombinePalletRefer(outBarcodeInfo);
                                    }

                                } else {
                                    MessageBox.Show(mContext, "查询托盘码失败:获取的托盘数据为空," + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.onPalletNoFocus();
                                        }
                                    });
                                }
                            } else {
                                MessageBox.Show(mContext, "查询托盘码失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onPalletNoFocus();
                                    }
                                });
                            }

                        } catch (Exception ex) {
                            MessageBox.Show(mContext, "查询托盘码失败，出现预期之外的异常-" + ex.getMessage() + ",", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onPalletNoFocus();
                                }
                            });
                        }
                    }
                });

            } else {
                MessageBox.Show(mContext, "解析条码失败，条码格式不正确" + scanBarcode, MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onPalletNoFocus();
                    }
                });
                return;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "查询条码失败，出现预期之外的异常:" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onPalletNoFocus();
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
        //扫描新物料前 清空旧数据
        mModel.setCurrentOuterBoxInfo(null);
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
                MessageBox.Show(mContext, "校验库位失败:获取托盘上的库位信息为空,请先扫描托盘号", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
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
                    if (scanQRCode != null) {
                        if (scanQRCode.getBarcodetype() != QRCodeFunc.BARCODE_TYPE_OUTER_BOX) {
                            MessageBox.Show(mContext, "校验条码失败:外箱条码格式不正确", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onBarcodeFocus();
                                }
                            });
                            return;
                        }
                        if (scanQRCode.getMaterialno() == null || scanQRCode.getMaterialno().equals("")) {
                            MessageBox.Show(mContext, "校验条码失败:获取的物料编码不能为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
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

            } else {
                MessageBox.Show(mContext, "校验条码失败:条码不正确", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
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
     * @desc: 根据物料编码查询物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 15:40
     */
    public void onMaterialInfoQuery(final OutBarcodeInfo scanQRCode) {
        final int palletType = mView.getPalletType();
        mModel.requestMaterialInfoQuery(scanQRCode.getMaterialno(), new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, mModel.TAG_SELECT_MATERIAL, result);
                    BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutBarcodeInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        OutBarcodeInfo materialInfo = returnMsgModel.getData();
                        if (materialInfo != null) {
                            scanQRCode.setMaterialdesc(materialInfo.getMaterialdesc());
                            scanQRCode.setPackqty(materialInfo.getPackqty());
                            scanQRCode.setSpec(materialInfo.getSpec());
                            scanQRCode.setUnit(materialInfo.getUnit());
                            scanQRCode.setUnitname(materialInfo.getUnitname());
                            if (palletType == InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_ACTIVE) {
                                if (!mModel.hasMaterialInOrderDetail(scanQRCode)) {
                                    MessageBox.Show(mContext, "查询物料失败: 物料【" + scanQRCode.getMaterialno() + "】不在订单中", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.onBarcodeFocus();
                                        }
                                    });
                                    return;
                                }
                            }
                            mModel.setCurrentOuterBoxInfo(scanQRCode);
                            mView.onOuterBoxQtyFocus();
                        }


                    } else {
                        MessageBox.Show(mContext, "查询物料失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onBarcodeFocus();
                            }
                        });
                        return;
                    }
                } catch (Exception e) {
                    MessageBox.Show(mContext, "查询物料失败:出现预期之外的异常-" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onBarcodeFocus();
                        }
                    });

                }
            }


        });
    }

    /**
     * @desc: 有源退货  新托盘提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/20 23:13
     */
    public void onActiveCombinePalletRefer(final OutBarcodeInfo palletInfo) {
        final int palletType = mView.getPalletType();
        if (palletInfo != null) {
            if (palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_OLD_PALLET) {
                palletInfo.setScantype(1);
            } else if (palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_NEW_PALLET) {
                palletInfo.setScantype(0);
            }
            BaseMultiResultInfo<Boolean, List<OrderDetailInfo>> detailResult = mModel.findMaterialInfoList(palletInfo); //找到物料行
            if (detailResult.getHeaderStatus()) {
                List<OrderDetailInfo> postList = detailResult.getInfo();
                for (OrderDetailInfo orderDetailInfo : postList) {
                    orderDetailInfo.setCustomerno(mModel.getOrderHeaderInfo().getCustomerno());
                    orderDetailInfo.setCustomername(mModel.getOrderHeaderInfo().getCustomername());
                    orderDetailInfo.setVouchertype(mModel.getOrderHeaderInfo().getVouchertype());
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
                                if (palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_OLD_PALLET) {
                                    mView.onOuterBoxQtyFocus();
                                } else if (palletType == InStockReturnsStorageScanModel.RETURN_PALLET_TYPE_NEW_PALLET) {
                                    mView.onPalletNoFocus();
                                }

                            } else {
                                MessageBox.Show(mContext, "提交条码信息失败:" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onPalletNoFocus();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            MessageBox.Show(mContext, "提交条码信息失败,出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onPalletNoFocus();
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
                    mView.onPalletNoFocus();
                }
            });
        }
    }

    /**
     * @desc: 有源退货  原有托盘提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/20 23:13
     */
    public void onActiveCombineOldPalletRefer(float qty) {
        OutBarcodeInfo materialInfo = mModel.getCurrentOuterBoxInfo();
        OutBarcodeInfo palletInfo = mModel.getCurrentScanInfo();
        if (palletInfo == null) {
            MessageBox.Show(mContext, "校验条码失败:托盘码信息为空,请扫描托盘码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onPalletNoFocus();
                }
            });
            return;
        }
        if (materialInfo == null) {
            MessageBox.Show(mContext, "校验条码失败:物料信息为空,请扫描外箱码或物料号", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onBarcodeFocus();
                }
            });
            return;
        }

        BaseMultiResultInfo<Boolean, Void> checkResult = mModel.checkOuterBoxQty(materialInfo, qty);
        if (!checkResult.getHeaderStatus()) {
            MessageBox.Show(mContext, checkResult.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onOuterBoxQtyFocus();
                }
            });
            return;
        }

        if (palletInfo != null) {
            palletInfo.setWBarcode(materialInfo.getBarcode());
            palletInfo.setQty(qty);
            onActiveCombinePalletRefer(palletInfo);
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
        if (mModel.getOrderDetailList() == null || mModel.getOrderDetailList().size() == 0) {
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
            postInfo.setVouchertype(mModel.getVoucherType());
            List<OrderDetailInfo> list = new ArrayList<>();
            list.add(postInfo);
            BaseMultiResultInfo<Boolean, Void> isOrderFinished = mModel.isOrderScanFinished();
            if (mModel.getVoucherType() == OrderType.IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE_VALUE) {
                //销退只能过一次账
                if (isOrderFinished.getHeaderStatus()) {
                    onActiveOrderRefer(list);
                } else {
                    MessageBox.Show(mContext, "校验单据信息失败:单据未全部扫描完毕,请扫描完成后再提交", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onBarcodeFocus();
                        }
                    });
                    return;
                }
                //工单退可以多次过账
            } else if (mModel.getVoucherType() == OrderType.IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE_VALUE) {
                onActiveOrderRefer(list);
            }


        }
    }

    /**
     * @desc: 提交过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/23 10:48
     */
    protected void onActiveOrderRefer(List<OrderDetailInfo> list) {
        mModel.requestOrderRefer(list, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(InStockReturnStorageScan.class, mModel.TAG_POST_T_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC, result);
                try {
                    BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == BaseResultInfo.RESULT_TYPE_OK) {
                        BaseMultiResultInfo<Boolean, Void> checkResult = mModel.isOrderScanFinished();
                        if (!checkResult.getHeaderStatus()) {
                            if (mModel.getVoucherType() == OrderType.IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE_VALUE) {
                                MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getOrderDetailInfoList(mView.getErpVoucherNo());
                                    }
                                });
                            }
                        } else {
                            MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onReset(true);
                                    mView.onPalletNoFocus();
                                }
                            });
//                                mView.onActivityFinish(checkResult.getMessage());
                        }
                    } else {
                        MessageBox.Show(mContext, "提交单据失败:" + returnMsgModel.getResultValue());
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "提交单据出现预期之外的异常:" + ex.getMessage());
                }


            }
        });
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
                                onReset(true);
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
        int palletType = mView.getPalletType();
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
        String erpVoucherNo = mView.getErpVoucherNo();
        if (erpVoucherNo == null || erpVoucherNo.equals("")) {
            onReset(true);
        } else {
            onReset(false);
        }


    }

    public void onReset(boolean isAllReset) {
        mModel.onReset(isAllReset);
        mView.onReset(isAllReset);
    }
}
