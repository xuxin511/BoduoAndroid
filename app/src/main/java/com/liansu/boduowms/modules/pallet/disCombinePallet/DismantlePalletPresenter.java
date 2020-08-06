package com.liansu.boduowms.modules.pallet.disCombinePallet;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.QRCode;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.hander.MyHandler;
/**
 * @ Des:
 * @ Created by yangyiqing on 2019/11/14.
 */
public class DismantlePalletPresenter {
    private Context                 mContext;
    private DismantlePalletModel    mModel;
    private DismantlePalletView     mView;
    private MyHandler<BaseActivity> mHandler;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
        switch (msg.what) {
        }
    }

    public DismantlePalletPresenter(Context context, DismantlePalletView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new DismantlePalletModel(context, handler);
        this.mHandler = handler;
    }


    /**
     * @desc: 扫描条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/14 17:39
     */
    public void scanBarcode(String scanBarcode) {
        QRCode scanQRCode = null;
        if (scanBarcode.equals("")) return;
        if (scanBarcode.contains("@")) {
//            scanQRCode = QRCodeFunc.getQrCode(scanBarcode);
        } else {    //内箱或本地
            scanQRCode = new QRCode();
            scanQRCode.setBarcode(scanBarcode);
        }

        final String barcode = scanQRCode.getBarcode();
        mModel.requestBarcodeInfoFromStockQuery(barcode, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
//                    LogUtil.WriteLog(mContext.getClass(), mModel.TAG_BarCodeInfo_Stock_Query, result);
//                    ReturnMsgModel<OutBarcodeInfo7> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<OutBarcodeInfo7>>() {
//                    }.getType());
//
//                    if (returnMsgModel.getHeaderStatus().equals("S")) {
//                        if (returnMsgModel != null) {
//                            OutBarcodeInfo7 barcodeInfo = returnMsgModel.getModelJson();
//                            if (barcodeInfo != null) {
//                                BaseMultiResultInfo<Boolean, Void> resultInfo = checkBarcode(barcodeInfo);
//                                if (resultInfo.getHeaderStatus() == false) {
//                                    MessageBox.Show(mContext, resultInfo.getMessage());
//                                    mView.requestBarcodeFocus();
//                                    return;
//                                }
//
////                                mModel.setBarcodeInfo(barcodeInfo);
////                                mView.setCurrentBarcodeInfo(barcodeInfo);
////                                mView.setPalletNo(barcodeInfo.getPalletNo());
////                                mView.bindListView(mModel.getList());
////                                mView.requestBarcodeFocus();
//                            } else {
//                                MessageBox.Show(mContext, "查询到的条码信息为空！");
//                                mView.requestBarcodeFocus();
//                                return;
//                            }
//                        } else {
//                            MessageBox.Show(mContext, "查询到的条码信息为空！");
//                            mView.requestBarcodeFocus();
//                            return;
//                        }
//
//
//                    } else {
//                        MessageBox.Show(mContext, returnMsgModel.getMessage());
//                        mView.requestBarcodeFocus();
//                        return;
//                    }
                } catch (Exception e) {
                    MessageBox.Show(mContext, e.getMessage() );
                    mView.requestBarcodeFocus();
                    return;
                }
            }
        });
    }


    /**
     * @desc: 提交 拆零数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/21 10:59
     */
    public void onRefer() {
//        List<OutBarcodeInfo7> list = mModel.getList();
//        if (list == null || list.size() == 0 || list.get(0)==null) {
//            MessageBox.Show(mContext, "拆托的条码不能为空");
//            return;
//        }
//        OutBarcodeInfo7 info=list.get(0);
//        if (info.getPalletNo() == null || info.getPalletNo().equals("")) {
//            MessageBox.Show(mContext, "条码:"+info.getSerialNo()+"的托盘号不能为空");
//            return;
//        }
//
//
//        mModel.requestPalletInfoSave(info, mView.getCombinPalletType(), new NetCallBackListener<String>() {
//            @Override
//            public void onCallBack(String result) {
//                try {
//                    LogUtil.WriteLog(mContext.getClass(), mModel.TAG_PalletInfoSave, result);
//                    ReturnMsgModel<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<String>>() {
//                    }.getType());
//                    MessageBox.Show(mContext, returnMsgModel.getMessage());
//                    if (returnMsgModel.getHeaderStatus().equals("Success")) {
//                        onClear();
//                    }
//                } catch (Exception e) {
//                    MessageBox.Show(mContext, e.getMessage());
//                    mView.requestBarcodeFocus();
//                    return;
//                }
//            }
//        });

    }

    /**
     * @desc: 验证条码信息
     * @param: type  2 是扫描条码的校验
     * @return:
     * @author: Nietzsche
     * @time 2020/2/13 17:14
     */
    BaseMultiResultInfo<Boolean, Void> checkBarcode(OutBarcodeInfo barcodeInfo) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        resultInfo.setHeaderStatus(true);
//        try {
//            if (barcodeInfo.getReceiveStatus() == 1) {
//                resultInfo.setHeaderStatus(false);
//                resultInfo.setMessage("条码:" + barcodeInfo.getSerialNo() + "为待收货状态!");
//                return resultInfo;
//            }
//            if (barcodeInfo.getIsLimitStock() != 2) {
//                resultInfo.setHeaderStatus(false);
//                resultInfo.setMessage("条码:" + barcodeInfo.getSerialNo() + "为非库存状态!");
//                return resultInfo;
//
//            }
//
////            if (barcodeInfo.getTaskDetailesID() != 0) {
////                resultInfo.setHeaderStatus(false);
////                resultInfo.setMessage("条码:" + barcodeInfo.getSerialNo() + "已被任务扫描绑定!");
////                return resultInfo;
////            }
//            if (barcodeInfo.getPalletNo() == null || barcodeInfo.getPalletNo().equals("")) {
//                resultInfo.setHeaderStatus(false);
//                resultInfo.setMessage("托盘号不能为空");
//                return resultInfo;
//            }
//
//            if (mModel.getList().contains(barcodeInfo)) {  //检查是否重复
//                resultInfo.setHeaderStatus(false);
//                resultInfo.setMessage("条码:" + barcodeInfo.getSerialNo() + "不能重复扫描");
//                return resultInfo;
//            }
//
//            if (mModel.getList().size()>1){
//                resultInfo.setHeaderStatus(false);
//                resultInfo.setMessage("请先完成当前条码的拆托操作");
//                return resultInfo;
//            }
//
//        } catch (Exception e) {
//            resultInfo.setHeaderStatus(false);
//            resultInfo.setMessage("验证条码的信息出现异常:" + e.getMessage());
//            return resultInfo;
//        }

        return resultInfo;
    }

    public void onClear() {
        mView.onClear();
        mModel.onClear();


    }

    /**
     * @desc: 删除条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/2/14 11:03
     */
    public void onDelete(int position) {
//        try {
//            if (mModel.getList() != null && mModel.getList().size() > 0) {
//                OutBarcodeInfo7 info = mModel.getList().get(position);
//                if (info != null) {
//                    mModel.getList().remove(info);
////                    mView.bindListView(mModel.getList());
//                    mView.setCurrentBarcodeInfo(null);
//                }
//            }
//        } catch (Exception e) {
//            MessageBox.Show(mContext, "删除条码出现预期之外的错误:" + e.getMessage());
//            return;
//        }
    }

    /**
     * @desc: 切换模式
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/2/13 15:37
     */
    public void changeModuleType(final boolean check) {
        if (mModel.getList() != null && mModel.getList().size() > 0) {
            new android.app.AlertDialog.Builder(mContext).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage("存在已扫描的待拆托条码，切换拆托模式将清空数据，是否继续切换？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onClear();
                            mView.showPalletScan(check);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.setSwitchButton(!check);
                }
            }).show();

        } else {
            onClear();
            mView.showPalletScan(check);
        }
    }

}
