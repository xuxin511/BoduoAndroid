package com.liansu.boduowms.modules.instock.salesReturn.print;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.instock.batchPrint.print.BaseOrderLabelPrint;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.DateUtil;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/17.
 */
public class SalesReturnPrintPresenter {
    protected Context               mContext;
    protected SaleReturnPrintModel  mModel;
    protected ISalesReturnPrintView mView;
    protected PrintBusinessModel    mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public SalesReturnPrintPresenter(Context context, ISalesReturnPrintView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new SaleReturnPrintModel(context, handler);
        this.mPrintModel = new PrintBusinessModel(context, handler);
    }

    public SaleReturnPrintModel getModel() {
        return mModel;
    }


    /**
     * @desc: 获取物料打印信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/2 10:10
     */
    public void getMaterialNoBatchList(final String materialNo, String startTime, String endTime, final String customerNo) {
        try {
            if (materialNo.equals("")) {
                MessageBox.Show(mContext, "物料信息不能为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onMaterialNoFocus();
                    }
                });
                return;
            }
            if (!DateUtil.isStartTimeBeforeAndEqualsEndTime(startTime, endTime)) {
                MessageBox.Show(mContext, "校验时间失败:开始时间[" + mView.getStartTime() + "]必须小于结束时间[" + mView.getEndTime() + "]", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onEndTimeFocus();
                    }
                });
                return;
            }
            OrderRequestInfo orderRequestInfo = new OrderRequestInfo();
            orderRequestInfo.setMaterialno(materialNo);
            orderRequestInfo.setDateFrom(startTime);
            orderRequestInfo.setDateTo(endTime);
            orderRequestInfo.setCustomerno(customerNo);
            orderRequestInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
            mModel.requestMaterialBatchNoListInfo(orderRequestInfo, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_SALE_RETURN_DETAIL_LIST_ADF_ASYNC, result);
                    try {
                        BaseResultInfo<OrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OrderHeaderInfo>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                            OrderHeaderInfo orderHeaderInfo = returnMsgModel.getData();
                            if (orderHeaderInfo != null) {
                                mModel.setCustomerCode(customerNo);
                                mModel.setMaterialDetailsList(orderHeaderInfo.getDetail());
                                BaseMultiResultInfo<Boolean, List<String>> checkResult = mModel.getMaterialBatchNoList(mModel.getMaterialDetailsList(), materialNo);
                                if (checkResult.getHeaderStatus()) {
                                    mView.setMaterialInfo(mModel.getMaterialDetailsList().get(0));
                                    mModel.setCurrentBatchNoList(checkResult.getInfo());
//                                    mView.setSpinnerData(checkResult.getInfo());
                                    mView.onBatchNoFocus();
                                } else {
                                    MessageBox.Show(mContext, checkResult.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.onMaterialNoFocus();
                                        }
                                    });
                                }

                            }
//                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), 1, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });

                        } else {
                            MessageBox.Show(mContext, "获取物料批次信息失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onMaterialNoFocus();
                                }
                            });
                        }

                    } catch (Exception ex) {
                        MessageBox.Show(mContext, "获取物料批次信息失败,出现预期之外的异常:" + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onMaterialNoFocus();
                            }
                        });
                    }


                }
            });
        } catch (ParseException e) {

        }


    }

    /**
     * @desc: 打印
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/17 7:30
     */
    public void onPrint(final String materialNo, final String materialName, final String batchNo, final float packQty, float packCount) {
        OrderDetailInfo orderDetailInfo = null;
        try {
            if (!mPrintModel.checkBluetoothSetting()) {
                return;
            }
            if (mModel.getMaterialDetailsList() != null && mModel.getMaterialDetailsList().size() > 0) {
                orderDetailInfo = mModel.getMaterialDetailsList().get(0);
            }

            if (mModel.getCustomerCode() == null) {
                MessageBox.Show(mContext, "客户编码不存在或为校验:请扫描客户编码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onCustomerNoFocus();
                    }
                });
                return;
            }
            if (orderDetailInfo == null) {
                MessageBox.Show(mContext, "物料数据类不能为空,请扫描物料编码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onMaterialNoFocus();
                    }
                });
                return;
            }

            if (materialNo.trim().equals("")) {
                MessageBox.Show(mContext, "物料编号不能为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onMaterialNoFocus();
                    }
                });
                return;
            }

            if (!DateUtil.isValidDate(batchNo.trim(), "yyyyMMdd") && !batchNo.trim().equals("")) {
                MessageBox.Show(mContext, "校验日期格式失败:" + "日期格式不正确", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       mView.onBatchNoFocus();
                    }
                });
                return;
            }

//            if (batchNo.equals("")) {
//                MessageBox.Show(mContext, "批次不能为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                return;
//            }
            if (!DateUtil.isStartTimeBeforeAndEqualsEndTime(mView.getStartTime(), mView.getEndTime())) {
                MessageBox.Show(mContext, "校验时间失败:开始时间[" + mView.getStartTime() + "]必须小于结束时间[" + mView.getEndTime() + "]");
                return;
            }

            if (packQty <= 0) {
                MessageBox.Show(mContext, "包装数量必须大于0", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onPackQtyFocus();
                    }
                });
                return;
            }
            if (packCount <= 0) {
                MessageBox.Show(mContext, "外箱的打印张数必须大于0", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onPrintCountFocus();
                    }
                });
                return;
            }


            List<PrintInfo> printInfoList = new ArrayList<>();
            for (int i = 0; i < packCount; i++) {
                PrintInfo info = new PrintInfo();
                info.setMaterialNo(materialNo);
                info.setMaterialDesc(materialName);
                info.setBatchNo(batchNo);
                info.setSpec(orderDetailInfo.getSpec());
                info.setPackQty(packQty);
                printInfoList.add(mModel.getPrintModel(info));

            }

            if (printInfoList.size() > 0) {
                mPrintModel.onPrint(printInfoList);
            }

            onPalletInfoBatchPrint(materialNo, batchNo, mView.getPalletRemainQty(), mView.getPalletQty());
        } catch (ParseException e) {
            MessageBox.Show(mContext, "校验日期失败:出现预期之外的异常," + e.getMessage());
            return;
        } catch (Exception e) {
            MessageBox.Show(mContext, "出现预期之外的异常," + e.getMessage());
            return;
        }


//        OutBarcodeInfo postBarcodeInfo = new OutBarcodeInfo();
//        postBarcodeInfo.setMaterialno(materialNo);
//        postBarcodeInfo.setMaterialdesc(materialName);
//        postBarcodeInfo.setBatchno(batchNo);
//        postBarcodeInfo.setSpec(mModel.getSpec(materialNo));
//        postBarcodeInfo.setPackqty(packCount);
//        postBarcodeInfo.setQty(packCount);
//        postBarcodeInfo.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE_VALUE);
//        postBarcodeInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
//        postBarcodeInfo.setStrongholdcode(orderDetailInfo.getStrongholdcode());
//        postBarcodeInfo.setStrongholdname(orderDetailInfo.getStrongholdname());
//        postBarcodeInfo.setCompanycode(orderDetailInfo.getCompanycode());
//        postBarcodeInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
//        postBarcodeInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
//        postBarcodeInfo.setPrintername(UrlInfo.mInStockPrintName);
//        postBarcodeInfo.setPrintertype(UrlInfo.mInStockPrintType);
//        postBarcodeInfo.setUsername(BaseApplication.mCurrentUserInfo.getUsername());
//        postBarcodeInfo.setCuscode(mModel.getCustomerCode());

//        float divValue = ArithUtil.div(sumPalletQty, packCount);
//        final double printCount = Math.ceil(divValue);   //向上取整
//        final float lastPackQty = sumPalletQty % packCount;  //最后一箱数量 模
//        mModel.requestPrint(postBarcodeInfo, new NetCallBackListener<String>() {
//            @Override
//            public void onCallBack(String result) {
//                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_SALE_RETURN_DETAIL_LIST_ADF_ASYNC, result);
//                try {
//                    BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
//                    }.getType());
//                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {  //托盘码打印成功后，打印外箱码
//                        List<PrintInfo> printInfoList = new ArrayList<>();
//                        for (int i = 0; i < printCount; i++) {
//                                PrintInfo info = new PrintInfo();
//                            info.setMaterialNo(materialNo);
//                            info.setMaterialDesc(materialName);
//                            info.setBatchNo(batchNo);
//                            info.setSpec(mModel.getSpec(materialNo));
//                            if (i == printCount - 1) {  //最后一箱数量 ，余数等于0 就是包装量 ,模大于零 就去余数
//                                if (lastPackQty == 0) {
//                                    info.setPackQty(packCount);
//                                } else {
//                                    info.setPackQty(lastPackQty);
//                                }
//                            } else {
//                                info.setPackQty(packCount);
//                            }
//
//                            printInfoList.add(mModel.getPrintModel(info));
//
//                        }
//
//                        if (printInfoList.size() > 0) {
//                            mPrintModel.onPrint(printInfoList);
//                        }

//                    } else {
//                        MessageBox.Show(mContext, returnMsgModel.getResultValue());
//                    }
//
//                } catch (Exception ex) {
//                    MessageBox.Show(mContext, ex.getMessage());
//                }
//            }
//        });


    }

    /**
     * @desc: 批量打印托盘条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 16:58
     */
    public void onPalletInfoBatchPrint(final String materialNo, final String batchNo, final float remainQty, final float palletQty) {
        OrderDetailInfo orderDetailInfo = null;
        if (mModel.getMaterialDetailsList() != null && mModel.getMaterialDetailsList().size() > 0) {
            orderDetailInfo = mModel.getMaterialDetailsList().get(0);
        }

        if (orderDetailInfo == null) {
            MessageBox.Show(mContext, "物料数据类不能为空,请扫描物料编码");
            return;
        }


        if (remainQty <= 0) {
            MessageBox.Show(mContext, "待收数量必须大于0");
            return;
        }
        if (palletQty <= 0) {
            MessageBox.Show(mContext, "整托数量必须大于0");
            return;
        }

        if (palletQty > remainQty) {
            MessageBox.Show(mContext, "待收数量必须大于托盘数量");
            return;
        }


        if (orderDetailInfo != null) {
            orderDetailInfo.setCustomerno(mModel.getCustomerCode());
            orderDetailInfo.setVoucherqty(remainQty);
            orderDetailInfo.setScanqty(palletQty);
            orderDetailInfo.setPrintqty(remainQty);
            orderDetailInfo.setBatchno(batchNo);
            orderDetailInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
            orderDetailInfo.setPrinterType(UrlInfo.mInStockPrintType);
            orderDetailInfo.setPrinterName(UrlInfo.mInStockPrintName);
            mModel.requestPrint(orderDetailInfo, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    try {
                        LogUtil.WriteLog(BaseOrderLabelPrint.class, mModel.TAG_PRINT_PALLET_NO, result);
                        BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                            onReset();

//                            MessageBox.Show(mContext, returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_NONE);

                        } else {
                            MessageBox.Show(mContext, "提交条码信息失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                        }
                    } catch (Exception e) {
                        MessageBox.Show(mContext, "提交条码信息失败,出现预期之外的异常:" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }
                }
            });
        } else {
            MessageBox.Show(mContext, "传入的打印数据为空");
        }

    }

    public String getTitle() {
        return mContext.getResources().getString(R.string.appbar_title_sales_return_storage_print) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
    }

    public void onReset() {
        mModel.onReset();
        mView.onReset();
    }
}
