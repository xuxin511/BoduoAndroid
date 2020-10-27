package com.liansu.boduowms.modules.inHouseStock.print;

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
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan.PurchaseInspectionProcessingScan;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.modules.print.PrintCallBackListener;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/14.
 */
public class BaseInStockHouseLabelPrintPresenter {
    protected       Context                         mContext;
    protected       BaseInStockHouseLabelPrintModel mModel;
    protected       BaseInStockHouseLabelPrintView  mView;
    protected       PrintBusinessModel              mPrintModel;
    protected       MyHandler<BaseActivity>         mHandler;
    protected final int                             PRINT_OUTER_BOX = 10003;


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
                MessageBox.Show(mContext, "托盘打印成功!", MessageBox.MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onReset();
                    }
                });

                break;
        }
    }

    public BaseInStockHouseLabelPrintPresenter(Context context, BaseInStockHouseLabelPrintView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new BaseInStockHouseLabelPrintModel(mContext, handler);
        this.mPrintModel = new PrintBusinessModel(context, handler);
        this.mPrintModel.setPrintCallBackListener(mPrintCallBackListener);
        this.mHandler = handler;
    }

    public BaseInStockHouseLabelPrintModel getModel() {
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

        } else if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO || printType == PrintBusinessModel.PRINTER_LABEL_TYPE_NO_SOURCE_PALLET_NO) {

        } else if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO_BY_BLUETOOTH) {
            onPalletInfoBatchPrintByBluetooth();
        }
    }


    /**
     * @desc: 用蓝牙批量打印托盘条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 16:58
     */
    public void onPalletInfoBatchPrintByBluetooth() {
        try {

            String batchNo = mView.getBatchNo();
            float remainQty = mView.getRemainQty();
            float palletQty = mView.getPalletQty();
            if (mModel.getMaterialInfo() == null) {
                MessageBox.Show(mContext, "物料编号不能为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onMaterialFocus();
                    }
                });
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
            if (remainQty < palletQty) {
                MessageBox.Show(mContext, "总数量必须大于等于可打印数", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onOrderRemainQtyFocus();
                    }
                });
                return;
            }

            OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
            orderDetailInfo.setMaterialno(mModel.getMaterialInfo().getMaterialno());
            orderDetailInfo.setMaterialdesc(mModel.getMaterialInfo().getMaterialdesc());
            orderDetailInfo.setSpec(mModel.getMaterialInfo().getSpec());
            orderDetailInfo.setUnit(mModel.getMaterialInfo().getUnit());
            orderDetailInfo.setUnitname(mModel.getMaterialInfo().getUnitname());
            orderDetailInfo.setScanqty(palletQty);
            orderDetailInfo.setPrintqty(remainQty);
            orderDetailInfo.setBatchno(batchNo);
            orderDetailInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
            orderDetailInfo.setMaterialno(mModel.getMaterialInfo().getMaterialno());
            orderDetailInfo.setVouchertype(mModel.getVoucherType());
            orderDetailInfo.setStrongholdcode(BaseApplication.mCurrentWareHouseInfo.getStrongholdcode());
            orderDetailInfo.setStrongholdname(BaseApplication.mCurrentWareHouseInfo.getStrongholdname());
            //如果是无订单 托盘打印,订单数量赋值成待收数量
            if (orderDetailInfo.getErpvoucherno() == null || orderDetailInfo.getErpvoucherno().equals("")) {
                orderDetailInfo.setVoucherqty(remainQty);
            }
            mModel.requestCreateBatchPalletInfo(orderDetailInfo, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    try {
                        LogUtil.WriteLog(BaseInStockHouseLabelPrint.class, mModel.TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC, result);
                        BaseResultInfo<List<OutBarcodeInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutBarcodeInfo>>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
//                                MessageBox.Show(mContext, returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                });
                            List<OutBarcodeInfo> list = returnMsgModel.getData();
                            if (list != null && list.size() > 0) {
                                List<PrintInfo> printInfoList = new ArrayList<>();
                                for (int i = 0; i < list.size(); i++) {
                                    OutBarcodeInfo outBarcodeInfo = list.get(i);
                                    if (outBarcodeInfo != null) {
                                        PrintInfo printInfo = mModel.getPrintModel(outBarcodeInfo);
                                        if (printInfo != null) {
                                            printInfoList.add(printInfo);
                                            String command=mPrintModel.getPalletLabelStyle(printInfo);
                                            if (command!=null){

                                            }
                                        }
                                    }

                                }

                                if (printInfoList.size() > 0) {
                                    mPrintModel.onPrint(printInfoList);
                                }
                            } else {
                                MessageBox.Show(mContext, "获取条码返回信息为空:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                            }


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

        } catch (Exception e) {
            MessageBox.Show(mContext, "校验打印数据出现预期之外的异常:请检查输入的打印信息是否正确," + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, null);
        }

    }

    /**
     * @desc: 扫描物料编号获取物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 15:08
     */
    public void onScan(String materialNo, int printType) {
        try {
            mModel.setMaterialNo(null);
            OutBarcodeInfo scanQRCode = null;
            if (materialNo.equals("")) return;
            BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(materialNo);
            if (resultInfo.getHeaderStatus()) {
                scanQRCode = resultInfo.getInfo();
            } else {
                MessageBox.Show(mContext, resultInfo.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onMaterialFocus();
                        mView.setMaterialDesc("");
                    }
                });
                return;
            }


            if (scanQRCode != null) {
                onMaterialInfoQuery(scanQRCode, printType);

            } else {
                MessageBox.Show(mContext, "解析物料失败，条码格式不正确" + materialNo, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onMaterialFocus();
                        mView.setMaterialDesc("");
                    }
                });
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "解析物料失败,出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onMaterialFocus();
                    mView.setMaterialDesc("");
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
    public void onMaterialInfoQuery(final OutBarcodeInfo scanQRCode, final int printType) {
        mModel.requestMaterialInfoQuery(scanQRCode.getMaterialno(), new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, mModel.TAG_SELECT_MATERIAL, result);
                    BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutBarcodeInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK||returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK && returnMsgModel.getData()!=null) {
                        OutBarcodeInfo materialInfo = returnMsgModel.getData();
                        if (materialInfo != null) {
                            scanQRCode.setMaterialdesc(materialInfo.getMaterialdesc());
                            scanQRCode.setPackqty(materialInfo.getPackqty());
                            scanQRCode.setSpec(materialInfo.getSpec());
                            scanQRCode.setUnit(materialInfo.getUnit());
                            scanQRCode.setUnitname(materialInfo.getUnitname());
                            mModel.setMaterialNo(materialInfo);
                        }
                        //方式是蓝牙托盘打印
                        if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO_BY_BLUETOOTH) {
                            onScanByPalletPrintType(scanQRCode);
                        }

                    } else {
                        MessageBox.Show(mContext, "查询物料失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onMaterialFocus();
                                mView.setMaterialDesc("");
                            }
                        });
                        return;
                    }
                } catch (Exception e) {
                    MessageBox.Show(mContext, "查询物料失败:出现预期之外的异常-" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onMaterialFocus();
                            mView.setMaterialDesc("");
                        }
                    });

                }
            }


        });
    }


    /**
     * @desc: 打印托盘类型方式时 的扫描方式
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 15:29
     */
    public void onScanByPalletPrintType(OutBarcodeInfo scanQRCode) {
        OrderDetailInfo materialInfo = new OrderDetailInfo();
        materialInfo.setMaterialno(scanQRCode.getMaterialno());
        materialInfo.setMaterialdesc(scanQRCode.getMaterialdesc());
        materialInfo.setBatchno(scanQRCode.getBatchno());
        materialInfo.setPackQty(scanQRCode.getPackqty());
        materialInfo.setSpec(scanQRCode.getSpec());
        materialInfo.setUnit(scanQRCode.getUnit());
        materialInfo.setUnitname(scanQRCode.getUnitname());
        mView.setPrintInfoData(materialInfo, mView.getPrintType());
    }


    public void onReset() {
        mModel.onReset();
        mView.onReset();
    }


    public void onStop(){
        if (mPrintModel!=null){
            mPrintModel.onClose();
        }
    }
}
