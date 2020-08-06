package com.liansu.boduowms.modules.instock.combinePallet;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;


/**
 * @ Des:
 * @ Created by yangyiqing on 2019/11/14.
 */
public class InstockCombinePalletPresenter {
    private Context                   mContext;
    private InstockCombinePalletModel mModel;
    private IInstockCombinePalletView mView;
    private MyHandler<BaseActivity>   mHandler;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
        switch (msg.what) {

        }
    }

    public InstockCombinePalletPresenter(Context context, IInstockCombinePalletView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new InstockCombinePalletModel(context, handler);
        this.mHandler = handler;
    }

    /**
     * @desc: 设置订单数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/6 13:47
     */
    public void setOrderInfo(OrderHeaderInfo headerInfo, List<OrderDetailInfo> detailInfos, int instockType) {
        if (mModel != null) {
            mModel.setOrderInfo(headerInfo, detailInfos, instockType);
            if (headerInfo != null) {
                mView.setErpVoucherNo(headerInfo.getErpvoucherno());
            }

        }

    }


    /**
     * @desc: 获取新组托号
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/14 17:38
     */
    public void getNewCreatedPalletNo() {
        mModel.requestNewCreatedPalletNoQuery(null, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(mContext.getClass(), mModel.TAG_NewCreatedPalletNoQuery, result);
                try {
//                    ReturnMsgModel<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<String>>() {
//                    }.getType());
//
//                    if (returnMsgModel.getHeaderStatus().equals("S")) {
//                        String palletNo = returnMsgModel.getModelJson().toString();
//                        mView.setPalletNo(palletNo);
//                        mView.requestBarcodeFocus();
//                        mModel.setPalletNo(palletNo);
//                        mModel.setPalletInfo(null);
//
//                    } else {
//                        MessageBox.Show(mContext, returnMsgModel.getMessage());
//                    }

                } catch (Exception e) {
                    MessageBox.Show(mContext, e.getMessage() );
                }
            }
        });
    }

    /**
     * @desc: 扫描组托码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/2/13 19:50
     */
    public void scanPalletNo(String palletNo) {
        mModel.requestPalletInfoFromStockQuery(palletNo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(mContext.getClass(), mModel.TAG_PalletInfoFromStockQuery, result);
                    com.liansu.boduowms.bean.base.BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<com.liansu.boduowms.bean.base.BaseResultInfo<OutBarcodeInfo>>() {
                    }.getType());

                    if (returnMsgModel.getResult() == 1) {
                        mModel.setPalletInfo(null);
                        if (returnMsgModel != null) {
                            OutBarcodeInfo barcodeInfo = returnMsgModel.getData();
                            if (barcodeInfo != null) {
                                BaseMultiResultInfo<Boolean, Void> resultInfo = mModel.checkBarcode(barcodeInfo);
                                if (resultInfo.getHeaderStatus()) {
                                    mModel.setBarcodeInfo(barcodeInfo);
                                    mView.bindListView(mModel.getScannedList());
                                } else {
                                    MessageBox.Show(mContext, resultInfo.getMessage() );
                                    mView.requestPalletBarcodeFocus();
                                    return;
                                }
                                mView.requestBarcodeFocus();
                            } else {
                                MessageBox.Show(mContext, "查询到的条码信息为空！" );
                                mView.requestPalletBarcodeFocus();
                                return;
                            }
                        } else {
                            MessageBox.Show(mContext, "查询到的条码信息为空！" );
                            mView.requestPalletBarcodeFocus();
                            return;
                        }


                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                        mView.requestBarcodeFocus();
                        return;
                    }
                } catch (Exception e) {
                    MessageBox.Show(mContext, e.getMessage() );
                    mView.requestBarcodeFocus();
                    return;
                }
            }
        });
    }

    /**
     * @desc: 扫描托盘标签码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/14 17:39
     */
    public void scanBarcode(String scanBarcode) {
        try {
            OutBarcodeInfo scanQRCode = null;
            if (scanBarcode.equals("")) return;
            if (scanBarcode.contains("%")) {
                BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(scanBarcode);
                if (resultInfo.getHeaderStatus()) {
                    scanQRCode = resultInfo.getInfo();
                } else {
                    MessageBox.Show(mContext, resultInfo.getMessage() );
                    return;
                }

            }
            if (scanQRCode != null) {
                BaseMultiResultInfo<Boolean, Void> checkResult = mModel.setPalletBarcodeInfo(scanQRCode); //设置条码信息
                if (checkResult.getHeaderStatus()) {
                    mView.bindListView(mModel.getScannedList());
                    mView.setCurrentBarcodeInfo(scanQRCode);
                } else {
                    MessageBox.Show(mContext, checkResult.getMessage() );
                }

            } else {
                MessageBox.Show(mContext, "解析条码失败，条码格式不正确" + scanBarcode );
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, e.getMessage() );
        } finally {
            mView.requestBarcodeFocus();
        }
    }

    /**
     * @desc: 提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/21 10:59
     */
    public void onRefer() {
        List<OutBarcodeInfo> list = mModel.getScannedList();
        if (list == null || list.size() == 0) {
            MessageBox.Show(mContext, "组托的条码不能为空" );
            return;
        }
        mModel.requestPalletInfoSave(list, mView.getCombinPalletType(), new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(mContext.getClass(), mModel.TAG_PalletInfoSave, result);
                    BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                    }.getType());
                    MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                    if (returnMsgModel.getResult() == 1) {
                        onClear();
                    }
                } catch (Exception e) {
                    MessageBox.Show(mContext, e.getMessage() );
                    mView.requestBarcodeFocus();
                    return;
                }
            }
        });


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
        try {
            if (mModel.getScannedList() != null && mModel.getScannedList().size() > 0) {
                OutBarcodeInfo info = mModel.getScannedList().get(position);
                if (info != null) {
                    mModel.removeList(info);
//                    mView.bindListView(mModel.getList());
                    mView.setSumCountInfo(mModel.getSumNumbers());
                    mView.setCurrentBarcodeInfo(null);
                    mView.bindListView(mModel.getScannedList());
                }
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "删除条码出现预期之外的错误:" + e.getMessage() );
            return;
        }
    }

    /**
     * @desc: 切换模式
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/2/13 15:37
     */
    public void changeModuleType(final boolean check) {
        if (mModel.getScannedList() != null && mModel.getScannedList().size() > 0) {
            new android.app.AlertDialog.Builder(mContext).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage("存在已扫描的待组托条码，切换组托模式将清空数据，是否继续切换？")
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
