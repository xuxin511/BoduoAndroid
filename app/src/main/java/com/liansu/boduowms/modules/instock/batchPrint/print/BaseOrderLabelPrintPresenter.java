package com.liansu.boduowms.modules.instock.batchPrint.print;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.modules.print.PrintCallBackListener;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.ArithUtil;
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
    protected Context                  mContext;
    protected BaseOrderLabelPrintModel mModel;
    protected IBaseOrderLabelPrintView mView;
    protected PrintBusinessModel       mPrintModel;
    PrintCallBackListener mPrintCallBackListener = new PrintCallBackListener() {
        @Override
        public void afterPrint() {
            mView.onReset();
            MessageBox.Show(mContext, "打印成功!");
        }
    };

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public BaseOrderLabelPrintPresenter(Context context, IBaseOrderLabelPrintView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new BaseOrderLabelPrintModel(mContext, handler);
        this.mPrintModel = new PrintBusinessModel(context, handler);
        this.mPrintModel.setPrintCallBackListener(mPrintCallBackListener);
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
        } else if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO) {
            onPalletInfoBatchPrint();
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
        if (mPrintModel.checkBluetoothSetting() == false) return;
        OrderDetailInfo printInfo = mModel.getCurrentPrintInfo();
        if (printInfo != null) {
            String materialNo = printInfo.getMaterialno();
            String materialDesc = printInfo.getMaterialdesc();
            String spec = printInfo.getSpec();
            String batchNo = mView.getBatchNo();
            float remainQty = mView.getRemainQty();
            float packQty = mView.getPackQty();
            if (materialNo.equals("")) {
                MessageBox.Show(mContext, "物料编号不能为空");
                return;
            }
            if (batchNo.equals("")) {
                MessageBox.Show(mContext, "批次不能为空");
                return;
            }

            if (packQty <= 0) {
                MessageBox.Show(mContext, "包装数量必须大于0");
                return;
            }
            if (remainQty <= packQty) {
                MessageBox.Show(mContext, "总数量必须大于等于包装量");
                return;
            }
            float divValue = ArithUtil.div(remainQty, packQty);
            final double printCount = Math.ceil(divValue);   //向上取整
            final float lastPackQty = remainQty % packQty;  //最后一箱数量 模
            List<PrintInfo> printInfoList = new ArrayList<>();
            for (int i = 0; i < printCount; i++) {
                PrintInfo info = new PrintInfo();
                info.setMaterialNo(materialNo);
                info.setMaterialDesc(materialDesc);
                info.setBatchNo(batchNo);
                info.setSpec(spec);
                if (i == printCount - 1) {  //最后一箱数量 ，余数等于0 就是包装量 ,模大于零 就去余数
                    if (lastPackQty == 0) {
                        info.setPackQty(packQty);
                    } else {
                        info.setPackQty(lastPackQty);
                    }
                } else {
                    info.setPackQty(packQty);
                }

                printInfoList.add(mModel.getPrintModel(info));

            }

            if (printInfoList.size() > 0) {
                mPrintModel.onPrint(printInfoList);
            }

        } else {
            MessageBox.Show(mContext, "传入的打印数据为空");
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
        if (mPrintModel.checkBluetoothSetting() == false) return;
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
                MessageBox.Show(mContext, "批次不能为空");
                return;
            }

            if (palletQty <= 0) {
                MessageBox.Show(mContext, "整托数量必须大于0");
                return;
            }
            if (remainQty < palletQty) {
                MessageBox.Show(mContext, "总数量必须大于等于可打印数");
                return;
            }
            OrderDetailInfo orderDetailInfo =  mModel.getCurrentPrintInfo();
            orderDetailInfo.setScanqty(palletQty);
            orderDetailInfo.setPrintqty(remainQty);
            orderDetailInfo.setBatchno(batchNo);
            orderDetailInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
            orderDetailInfo.setPrinterType(UrlInfo.mInStockPrintType);
            orderDetailInfo.setPrinterName(UrlInfo.mInStockPrintName);
            mModel.requestCreateBatchPalletInfo(orderDetailInfo, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    try {
                        LogUtil.WriteLog(BaseOrderLabelPrint.class, mModel.TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC, result);
                        BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == RESULT_TYPE_OK) {

                           MessageBox.Show(mContext,returnMsgModel.getResultValue(),MessageBox.MEDIA_MUSIC_NONE);

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


}
