package com.liansu.boduowms.modules.instock.productStorage.printPalletScan;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.bean.paroductStorage.ProductDetailInfo;
import com.liansu.boduowms.bean.paroductStorage.ProductInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
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
public class PrintPalletScanPresenter {
    protected Context              mContext;
    protected PrintPalletScanModel mModel;
    protected IPrintPalletScanView mView;
    protected PrintBusinessModel   mPrintModel;

    public PrintPalletScanPresenter(Context context, IPrintPalletScanView view, MyHandler<BaseActivity> handler, OrderHeaderInfo orderHeaderInfo, String businessType) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new PrintPalletScanModel(context, handler);
        this.mModel.setOrderHeaderInfo(orderHeaderInfo);
        this.mPrintModel = new PrintBusinessModel(context, handler);
        this.mModel.setBusinessType(businessType);
    }

    public PrintPalletScanModel getModel() {
        return mModel;
    }

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }


    protected String getTitle() {
        return mContext.getResources().getString(R.string.appbar_title_product_storage_pallet_label_print);
    }


    /**
     * @desc: 获取报工单明细接口
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/23 16:24
     */
    protected void getOrderDetailInfoList(OrderHeaderInfo info) {
        info.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_VALUE);
        mModel.requestOrderDetail(info, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC, result);
                try {
                    BaseResultInfo<List<ProductInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<ProductInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        ProductInfo productInfo = returnMsgModel.getData().get(0);
                        if (productInfo != null) {
                            mModel.setProductInfo(productInfo);
                            if (mModel.getOrderDetailList().size() > 0) {
                                mView.bindListView(mModel.getOrderDetailList());
                                mView.setErpVoucherNo(mModel.getDetailInfo().getErpvoucherno());
                                mView.onBarcodeFocus();
                            } else {
                                MessageBox.Show(mContext, "获取单据列表失败：" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onErpVoucherNo();
                                    }
                                });

                            }
                        } else {
                            MessageBox.Show(mContext, "获取单据列表失败：" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onErpVoucherNo();
                                }
                            });
                        }
                    } else {
                        MessageBox.Show(mContext, "获取单据列表失败：" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onErpVoucherNo();
                            }
                        });
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取单据列表失败：" + ex.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onErpVoucherNo();
                        }
                    });
                }


            }
        });
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
            if (!mPrintModel.checkBluetoothSetting()) {
                return;
            }
            OutBarcodeInfo scanQRCode = null;
            if (scanBarcode.equals("")) return;
            BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(scanBarcode);
            if (resultInfo.getHeaderStatus()) {
                scanQRCode = resultInfo.getInfo();
            } else {
                MessageBox.Show(mContext, "解析条码失败: " + resultInfo.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                    }
                });

                return;
            }


            if (scanQRCode != null) {
                //如果外箱码不为空，就弹出物料输入框
                BaseMultiResultInfo<Boolean, Void> result = mModel.findOutBarcodeInfoFromMaterial(scanQRCode); //找到物料行,如果找到给外箱码赋值
                if (result.getHeaderStatus()) {
                    mView.createDialog(scanQRCode);
                } else {
                    MessageBox.Show(mContext, "解析条码失败: " + result.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onBarcodeFocus();
                        }
                    });

                }

            } else {
                MessageBox.Show(mContext, "解析条码失败: 条码格式不正确" + scanBarcode, MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                    }
                });
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "解析条码失败: " + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onBarcodeFocus();
                }
            });


        }

    }


    /**
     * @desc: 外箱码扫描提交后生成托盘批次编码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/23 16:30
     */

    public void onCombinePalletRefer(final OutBarcodeInfo outBarcodeInfo) {
        if (outBarcodeInfo != null) {
            outBarcodeInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
            outBarcodeInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
            outBarcodeInfo.setUsername(BaseApplication.mCurrentUserInfo.getUsername());
            BaseMultiResultInfo<Boolean, ProductDetailInfo> detailResult = mModel.findMaterialInfo(outBarcodeInfo); //找到物料行
            if (detailResult.getHeaderStatus()) {
                final ProductDetailInfo orderDetailInfo = detailResult.getInfo();
                BaseMultiResultInfo<Boolean, Void> checkMaterialResult = mModel.checkMaterialInfo(orderDetailInfo, outBarcodeInfo, false); //校验条码是否匹配物料
                if (checkMaterialResult.getHeaderStatus()) {
                    if (UrlInfo.mInStockPrintType == PrintBusinessModel.PRINTER_TYPE_LASER) {
                        orderDetailInfo.setPrintername(UrlInfo.mLaserPrinterAddress);
                    } else if (UrlInfo.mInStockPrintType == PrintBusinessModel.PRINTER_TYPE_DESKTOP) {
                        orderDetailInfo.setPrintername(UrlInfo.mDesktopPrintAddress);
                    }
                    orderDetailInfo.setPrintertype(UrlInfo.mInStockPrintType);
                    mModel.requestCombineAndReferPallet(orderDetailInfo, new NetCallBackListener<String>() {
                        @Override
                        public void onCallBack(String result) {
                            try {
                                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_CREATE_PALLET_NO_ADF_ASYNC, result);
                                BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                                }.getType());
                                if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                    String barcode = returnMsgModel.getData();
                                    outBarcodeInfo.setBarcode(barcode);
                                    mModel.checkMaterialInfo(orderDetailInfo, outBarcodeInfo, true);
                                    mView.bindListView(mModel.getOrderDetailList());
//                                    PrintInfo printInfo = mModel.getPrintModel(outBarcodeInfo);
//                                    if (printInfo != null) {
//                                        mPrintModel.onPrint(printInfo);
//                                    }
                                    mView.onBarcodeFocus();
                                } else {
                                    MessageBox.Show(mContext,"提交条码信息失败:"+ returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.onBarcodeFocus();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                MessageBox.Show(mContext, "提交条码信息失败,出现预期之外的异常:" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onBarcodeFocus();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    MessageBox.Show(mContext, checkMaterialResult.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onBarcodeFocus();
                        }
                    });
                }
            } else {
                MessageBox.Show(mContext, detailResult.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                    }
                });
            }
        } else {
            MessageBox.Show(mContext, "外箱信息不能为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onBarcodeFocus();
                }
            });
        }
    }


}
