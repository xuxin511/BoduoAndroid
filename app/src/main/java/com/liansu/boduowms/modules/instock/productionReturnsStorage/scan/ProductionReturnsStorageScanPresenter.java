package com.liansu.boduowms.modules.instock.productionReturnsStorage.scan;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScanPresenter;
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
        return mContext.getResources().getString(R.string.appbar_title_production_returns_scan) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename();
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

    @Override
    public void onCombinePalletRefer(final OutBarcodeInfo outBarcodeInfo) {
        super.onCombinePalletRefer(outBarcodeInfo);
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
