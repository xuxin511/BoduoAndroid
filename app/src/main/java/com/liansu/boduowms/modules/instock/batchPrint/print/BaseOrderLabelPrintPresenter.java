package com.liansu.boduowms.modules.instock.batchPrint.print;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.modules.print.PrintCallBackListener;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.function.DateUtil;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/14.
 */
public class BaseOrderLabelPrintPresenter {
    protected           Context                  mContext;
    protected           BaseOrderLabelPrintModel mModel;
    protected           IBaseOrderLabelPrintView mView;
    protected           PrintBusinessModel       mPrintModel;
    protected           MyHandler<BaseActivity>  mHandler;
    protected final     int                      PRINT_OUTER_BOX           = 10003;
    public static final int                      CHECK_BATCH_NO_TYPE_NONE  = 0;
    public static final int                      CHECK_BATCH_NO_TYPE_PRINT = 1;

    PrintCallBackListener mPrintCallBackListener = new PrintCallBackListener() {
        @Override
        public void afterPrint() {
            Message message = new Message();
            message.what = PRINT_OUTER_BOX;
            mHandler.sendMessage(message);
        }
    };

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
        switch (msg.what) {
            case PRINT_OUTER_BOX:
                mView.onReset();
                break;
        }
    }

    public BaseOrderLabelPrintPresenter(Context context, IBaseOrderLabelPrintView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new BaseOrderLabelPrintModel(mContext, handler);
        this.mPrintModel = new PrintBusinessModel(context, handler);
        this.mPrintModel.setPrintCallBackListener(mPrintCallBackListener);
        this.mHandler = handler;
    }

    public BaseOrderLabelPrintModel getModel() {
        return mModel;
    }

    /**
     * @desc: 打印方式  外箱打印和托盘打印
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/16 20:27
     */
    public void onPrint() {
        int printType = mModel.getPrintType();
        if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_OUTER_BOX) {
            onOuterBoxInfoBatchPrint();
        } else if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO || printType == PrintBusinessModel.PRINTER_LABEL_TYPE_NO_SOURCE_PALLET_NO) {
//            onPalletInfoBatchPrint();
            checkBatchNoOfPallet(CHECK_BATCH_NO_TYPE_PRINT);
        }
    }


    /**
     * @desc: 批量打印外箱条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 16:53
     */
    public void onOuterBoxInfoBatchPrint() {
        try {

            if (mPrintModel.checkBluetoothSetting() == false) return;
            OrderDetailInfo printInfo = mModel.getCurrentPrintInfo();
            if (printInfo != null) {
                String erpVoucherNo = printInfo.getErpvoucherno() != null ? printInfo.getErpvoucherno() : "";
                String materialNo = printInfo.getMaterialno();
                String materialDesc = printInfo.getMaterialdesc();
                String spec = printInfo.getSpec();
                String batchNo = mView.getBatchNo();
                float printCount = mView.getPrintCount();
                float packQty = mView.getPackQty();
                if (materialNo.equals("")) {
                    MessageBox.Show(mContext, "物料编号不能为空");
                    return;
                }
                if (batchNo.equals("")) {
                    MessageBox.Show(mContext, "批次不能为空");
                    return;
                }

                if (mView.checkBatchNo(batchNo) == false) {
                    return;
                }

                if (packQty <= 0) {
                    MessageBox.Show(mContext, "包装数量必须大于0");
                    return;
                }
                if (printCount <= 0) {
                    MessageBox.Show(mContext, "打印张数必须大于0");
                    return;
                }
                List<PrintInfo> printInfoList = new ArrayList<>();
                for (int i = 0; i < printCount; i++) {
                    PrintInfo info = new PrintInfo();
                    info.setErpVoucherNo(erpVoucherNo);
                    info.setMaterialNo(materialNo);
                    info.setMaterialDesc(materialDesc);
                    info.setBatchNo(batchNo);
                    info.setSpec(spec);
                    info.setPackQty(packQty);
                    printInfoList.add(mModel.getPrintModel(info));

                }

                if (printInfoList.size() > 0) {
                    mPrintModel.onPrint(printInfoList);
                }

            } else {
                MessageBox.Show(mContext, "传入的打印数据为空");
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "打印出现预期之外的异常:" + e.getMessage());
        }
    }


    /**
     * @desc: 批量打印托盘条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 16:58
     */
    public void onPalletInfoBatchPrint() {
        try {
            OrderDetailInfo printInfo = mModel.getCurrentPrintInfo();
            if (printInfo != null) {
                String materialNo = printInfo.getMaterialno();
                String materialDesc = printInfo.getMaterialdesc();
                String spec = printInfo.getSpec();
                String batchNo = mView.getBatchNo();
                float remainQty = mView.getRemainQty();
                float palletQty = mView.getPalletQty();
                if (materialNo.equals("")) {
                    MessageBox.Show(mContext, "物料编号不能为空");
                    return;
                }
                if (batchNo.equals("")) {
                    MessageBox.Show(mContext, "批次不能为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onBatchNoFocus();
                        }
                    });
                    return;
                }
                if (mView.checkBatchNo(batchNo) == false) {
                    return;
                }
                if (palletQty <= 0) {
                    MessageBox.Show(mContext, "整托数量必须大于0", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onPalletQtyFocus();
                        }
                    });
                    return;
                }
//                if (mModel.getCurrentPrintInfo()!=null && mModel.getCurrentPrintInfo().getVouchertype()== OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE){
//                    if (mView.getOriginalRemainQty()>0){
//                        if (ArithUtil.sub(mView.getOriginalRemainQty(),remainQty)<0){
//                            MessageBox.Show(mContext, "修改的剩余打印数量不能大于订单的剩余打印数量", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    mView.onOrderRemainQtyFocus();
//                                }
//                            });
//                            return;
//                        }
//                    }
//                }
                if (!mView.checkRemainQty()) {
                    return;
                }

                if (remainQty < palletQty) {
                    MessageBox.Show(mContext, "总数量必须大于等于可打印数", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onOrderRemainQtyFocus();
                        }
                    });
                    return;
                }
                float divValue = ArithUtil.div(remainQty, palletQty);
                final double printCount = Math.ceil(divValue);   //向上取整
                final OrderDetailInfo orderDetailInfo = mModel.getCurrentPrintInfo();
                orderDetailInfo.setScanqty(palletQty);
                orderDetailInfo.setPrintqty(remainQty);
                orderDetailInfo.setBatchno(batchNo);
                orderDetailInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
                orderDetailInfo.setPrinterType(UrlInfo.mInStockPrintType);
                orderDetailInfo.setPrinterName(UrlInfo.mInStockPrintName);
                //如果是无订单 托盘打印,订单数量赋值成待收数量
                if (orderDetailInfo.getErpvoucherno() == null || orderDetailInfo.getErpvoucherno().equals("")) {
                    orderDetailInfo.setVoucherqty(remainQty);
                }
                MessageBox.Show2(mContext, "是否确定打印托盘码,打印张数：" + Math.round(printCount), MessageBox.MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mModel.requestCreateBatchPalletInfo(orderDetailInfo, new NetCallBackListener<String>() {
                            @Override
                            public void onCallBack(String result) {
                                try {
                                    LogUtil.WriteLog(BaseOrderLabelPrint.class, mModel.TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC, result);
                                    BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                                    }.getType());
                                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                        ToastUtil.show(returnMsgModel.getResultValue());
                                        mView.onReset();
//                                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//
//                                            }
//                                        });

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
                    }
                });

            } else {
                MessageBox.Show(mContext, "传入的打印数据为空");
            }

        } catch (Exception e) {
            MessageBox.Show(mContext, "校验打印数据出现预期之外的异常:请检查输入的打印信息是否正确," + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, null);
        }

    }

    /**
     * @desc: 校验托盘的批次信息
     * @param: checkType 0
     * @return:
     * @author: Nietzsche
     * @time 2021/1/21 10:22
     */
    ;

    public void checkBatchNoOfPallet(final int checkType) {
        final String batchNo = mView.getBatchNo();
        if (mView.checkBatchNo(batchNo) == false) {
            return;
        }
        OrderDetailInfo printInfo = mModel.getCurrentPrintInfo();
        if (printInfo!=null){
            final String materialNo = printInfo.getMaterialno();
            if (materialNo==null ||materialNo.equals("")) {
                MessageBox.Show(mContext, "物料编号不能为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                return;
            }
            OrderDetailInfo  orderDetailInfo=new OrderDetailInfo();
            orderDetailInfo.setBatchno(batchNo);
            orderDetailInfo.setMaterialno(materialNo);
            orderDetailInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());

            mModel.requestCheckBatchNoOfPallet(orderDetailInfo, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    try {
                        LogUtil.WriteLog(BaseOrderLabelPrint.class, mModel.TAG_CHECK_BATCH_DATE, result);
                        BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                              String maxStockBatchNo=returnMsgModel.getData();
                              // 如果库存条码是客户以前的批次 不是标准的批次格式（yyyyMMdd）或者这个物料没有入过库存，最新批次不存在（为null 或空），则不去比较当前批次和库存批次，直接打印
                              if (maxStockBatchNo!=null && !maxStockBatchNo.equals("") &&DateUtil.isValidDate(maxStockBatchNo.trim(), "yyyyMMdd")){
                                  BaseMultiResultInfo<Boolean, Void> checkResult=mModel.compareDate(batchNo,maxStockBatchNo,"yyyyMMdd",materialNo);
                                  if (!checkResult.getHeaderStatus()){
                                      MessageBox.Show2(mContext, checkResult.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialog, int which) {
                                              if (checkType == CHECK_BATCH_NO_TYPE_PRINT) {
                                                  onPalletInfoBatchPrint();
                                              }

                                          }
                                      });
                                  }else {
                                      if (checkType == CHECK_BATCH_NO_TYPE_PRINT) {
                                          onPalletInfoBatchPrint();
                                      }
                                  }
                              }else {
                                  if (checkType == CHECK_BATCH_NO_TYPE_PRINT) {
                                      onPalletInfoBatchPrint();
                                  }
                              }

                        } else {
                            MessageBox.Show(mContext, "校验批次信息失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onBatchNoFocus();
                                }
                            });
                        }
                    } catch (Exception e) {
                        MessageBox.Show(mContext, "校验批次信息失败,出现预期之外的异常:" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onBatchNoFocus();
                            }
                        });
                    }
                }
            });
        }

    }

}
