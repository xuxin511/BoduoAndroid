package com.liansu.boduowms.modules.pallet.combinePallet;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;


/**
 * @ Des:
 * @ Created by yangyiqing on 2019/11/14.
 */
public class CombinPalletPresenter {
    private Context                 mContext;
    private CombinPalletModel       mModel;
    private ICombinPalletView       mView;
    private MyHandler<BaseActivity> mHandler;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);

    }

    public CombinPalletPresenter(Context context, ICombinPalletView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new CombinPalletModel(context, handler);
        this.mHandler = handler;
    }

    /**
     * @desc: 获取新组托号
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/14 17:38
     */
    public void getNewCreatedPalletNo() {
//        mModel.requestNewCreatedPalletNoQuery(new NetCallBackListener<String>() {
//            @Override
//            public void onCallBack(String result) {
//                LogUtil.WriteLog(mContext.getClass(), mModel.TAG_NewCreatedPalletNoQuery, result);
//                try {
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
//
//                } catch (Exception e) {
//                    MessageBox.Show(mContext, e.getMessage());
//                }
//            }
//        });
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
//                    ReturnMsgModel<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<OutBarcodeInfo7>>() {
//                    }.getType());
//
//                    if (returnMsgModel.getHeaderStatus().equals("S")) {
//                        mModel.setPalletInfo(null);
//                        if (returnMsgModel != null) {
//                            OutBarcodeInfo barcodeInfo = returnMsgModel.getModelJson();
//                            if (barcodeInfo != null) {
//                                BaseMultiResultInfo<Boolean, Void> resultInfo = checkBarcode(barcodeInfo, 1);
//                                if (resultInfo.getHeaderStatus() == false) {
//                                    MessageBox.Show(mContext, resultInfo.getMessage());
//                                    mView.requestPalletBarcodeFocus();
//                                    return;
//                                }
//
////                                mModel.setPalletNo(barcodeInfo.getPalletNo());
////                                mModel.setPalletInfo(barcodeInfo);
////                                mView.setPalletNo(barcodeInfo.getPalletNo());
//                                mView.requestBarcodeFocus();
//                            } else {
//                                MessageBox.Show(mContext, "查询到的条码信息为空！");
//                                mView.requestPalletBarcodeFocus();
//                                return;
//                            }
//                        } else {
//                            MessageBox.Show(mContext, "查询到的条码信息为空！");
//                            mView.requestPalletBarcodeFocus();
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
     * @desc: 扫描条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/14 17:39
     */
    public void scanBarcode(String scanBarcode) {
        try {
//            if (mModel.getPalletInfo()==null){
//                MessageBox.Show(mContext,"组托码不能为空,请先扫描或生成");
//                return;
//            }
            OutBarcodeInfo scanQRCode = null;
            if (scanBarcode.equals("")) return;
            if (scanBarcode.contains("%")) {
                BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(scanBarcode);
                if (resultInfo.getHeaderStatus()) {
                    scanQRCode = resultInfo.getInfo();
                } else {
                    MessageBox.Show(mContext, resultInfo.getMessage() );
                }

            }


            if (scanQRCode != null) {
                bindBarcodeData(scanQRCode);
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, e.getMessage() );
        }

//        QRCode scanQRCode = null;

//        if (scanBarcode.contains("@")) {
//            scanQRCode = QRCodeFunc.getQrCode(scanBarcode);
//        } else {    //内箱或本地
//            scanQRCode = new QRCode();
//            scanQRCode.setBarcodeType(QRCode.BARCODE_TYPE_NONE);
//            scanQRCode.setOriginalCode(scanBarcode);
//        }
//
//        if (mModel.getPalletNo() == null) {
//            MessageBox.Show(mContext, "请先获取托盘号");
//            return;
//        }
//
//        final String barcode = scanQRCode.getOriginalCode();
//        mModel.requestBarcodeInfoFromStockQuery(barcode, new NetCallBackListener<String>() {
//            @Override
//            public void onCallBack(String result) {
//                try {
//                    LogUtil.WriteLog(mContext.getClass(), mModel.TAG_BarCodeInfo_Stock_Query, result);
//                    ReturnMsgModel<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<OutBarcodeInfo7>>() {
//                    }.getType());
//
//                    if (returnMsgModel.getHeaderStatus().equals("S")) {
//                        if (returnMsgModel != null) {
//                            OutBarcodeInfo barcodeInfo = returnMsgModel.getModelJson();
//                            if (barcodeInfo != null) {
//                                BaseResultInfo<Boolean, Void> resultInfo = checkBarcode(barcodeInfo, 2);
//                                if (resultInfo.getHeaderStatus() == false) {
//                                    MessageBox.Show(mContext, resultInfo.getMessage());
//                                    mView.requestBarcodeFocus();
//                                    return;
//                                }
//
//                                mModel.setBarcodeInfo(barcodeInfo);
//                                mView.setCurrentBarcodeInfo(barcodeInfo);
//                                mView.setSumCountInfo(mModel.getSumNumbers());
//                                mView.bindListView(mModel.getList());
//                                mView.requestBarcodeFocus();
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
//                } catch (Exception e) {
//                    MessageBox.Show(mContext, e.getMessage());
//                    mView.requestBarcodeFocus();
//                    return;
//                }
//            }
//        });
    }


    /**
     * @desc: 提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/21 10:59
     */
    public void onRefer() {
//        String palletNo = mModel.getPalletNo();
//        List<StockInfo> list = mModel.getList();
//
//        if (palletNo == null || palletNo.equals("")) {
//            MessageBox.Show(mContext, "请获取托盘号");
//            return;
//        }
//
//        if (list == null || list.size() == 0) {
//            MessageBox.Show(mContext, "组托的条码不能为空");
//            return;
//        }
//        mModel.requestPalletInfoSave(mModel.getList(), mModel.getPalletNo(), mView.getCombinPalletType(), new NetCallBackListener<String>() {
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
     * @param: type  1  代表是扫描组托号的校验   2 是扫描条码的校验
     * @return:
     * @author: Nietzsche
     * @time 2020/2/13 17:14
     */
    BaseMultiResultInfo<Boolean, Void> checkBarcode(OutBarcodeInfo barcodeInfo, int type) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        resultInfo.setHeaderStatus(true);
        try {
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
//            if (barcodeInfo.getTaskDetailesID() != 0) {
//                resultInfo.setHeaderStatus(false);
//                resultInfo.setMessage("条码:" + barcodeInfo.getSerialNo() + "已被任务扫描绑定!");
//                return resultInfo;
//            }
//            if (type == 1) {
//                if (barcodeInfo.getPalletNo() == null || barcodeInfo.getPalletNo().equals("")) {
//                    resultInfo.setHeaderStatus(false);
//                    resultInfo.setMessage("条码:" + barcodeInfo.getSerialNo() + "的没有托盘号!");
//                    return resultInfo;
//
//                }
//                if (barcodeInfo.getMaterialNo() == null || barcodeInfo.getMaterialNo().equals("")) {
//                    resultInfo.setHeaderStatus(false);
//                    if (mView.getCombinPalletType() == 2){
//                        resultInfo.setMessage("带出托盘的条码的物料号不能为空!");
//                    }
//
//                    return resultInfo;
//
//                }
//            }
            if (type == 2) {

//                if (barcodeInfo.getMaterialNo()==null || barcodeInfo.getMaterialNo().equals("")){
//                    resultInfo.setHeaderStatus(false);
//                    resultInfo.setMessage("条码:" + barcodeInfo.getSerialNo() + "的物料号不能为空!");
//                    return resultInfo;
//                }
//
//
//                if (mModel.getPalletNo() != null) {
//                    if (mView.getCombinPalletType() == 1) { //创建新托
//                        if (mModel.getPalletInfo()==null){
//                            mModel.setPalletInfo(barcodeInfo);
//                        }
//                        if (barcodeInfo.getPalletNo() != null && !barcodeInfo.getPalletNo().equals("")) {
////                        if (mModel.getPalletNo().trim().equals(barcodeInfo.getPalletNo())) {
//                            resultInfo.setHeaderStatus(false);
//                            resultInfo.setMessage("条码:" + barcodeInfo.getSerialNo() + "已组托,托盘号为" + mModel.getPalletNo());
//                            return resultInfo;
//                        }
////                    }
//                    } else if (mView.getCombinPalletType() == 2) {
//                        if (barcodeInfo.getPalletNo() != null && !barcodeInfo.getPalletNo().equals("")) {
//                            if (mModel.getPalletNo().trim().equals(barcodeInfo.getPalletNo())) {
//                                resultInfo.setHeaderStatus(false);
//                                resultInfo.setMessage("条码:" + barcodeInfo.getSerialNo() + "已在托盘号为" + mModel.getPalletNo() + "的托盘中");
//                                return resultInfo;
//                            }
//                        }
//
//                    }

//                    if (mModel.getPalletInfo()!=null){
//                        if(!mModel.getPalletInfo().getMaterialNo().equals(barcodeInfo.getMaterialNo())){
//                            if (mView.getCombinPalletType()==1){
//                                resultInfo.setHeaderStatus(false);
//                                resultInfo.setMessage("条码:" + barcodeInfo.getSerialNo() + "的物料号(" + barcodeInfo.getMaterialNo() + ")必须和首个条码的物料号"+mModel.getPalletInfo().getMaterialNo()+"一致,物料名称应为:"+mModel.getPalletInfo().getMaterialDesc());
//                                return resultInfo;
//                            }else if (mView.getCombinPalletType()==2){
//                                resultInfo.setHeaderStatus(false);
//                                resultInfo.setMessage("条码:" + barcodeInfo.getSerialno() + "的物料号(" + barcodeInfo.getMaterialNo() + ")必须和带出托盘号的条码的物料号"+mModel.getPalletInfo().getMaterialNo()+"一致,物料名称应为:"+mModel.getPalletInfo().getMaterialDesc());
//                                return resultInfo;
//                            }
//
//                        }
//                    }
                if (mModel.getList().contains(barcodeInfo)) {  //检查是否重复
                    resultInfo.setHeaderStatus(false);
                    resultInfo.setMessage("条码:" + barcodeInfo.getSerialno() + "不能重复扫描");
                    return resultInfo;
                }
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("托盘号不能为空");
                return resultInfo;
            }
//            }
        } catch (Exception e) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("验证条码的信息出现异常:" + e.getMessage());
            return resultInfo;
        }
//
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
        try {
            if (mModel.getList() != null && mModel.getList().size() > 0) {
                OutBarcodeInfo info = mModel.getList().get(position);
                if (info != null) {
                    mModel.removeList(info);
                    mView.bindListView(mModel.getList());
                    mView.setSumCountInfo(mModel.getSumNumbers());
                    mView.setCurrentBarcodeInfo(null);
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
        if (mModel.getList() != null && mModel.getList().size() > 0) {
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

    /**
     * @desc: 绑定条码数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time \ 12:13
     */
    public void bindBarcodeData(OutBarcodeInfo barcode) {
        boolean isCreateManuallyMaterialInfo = mModel.isCreateManuallyNewMaterialInfo(barcode);
        if (isCreateManuallyMaterialInfo) {
            mView.createDialog(barcode);
        } else {
            BaseMultiResultInfo<Boolean, Void> resultInfo = mModel.updateMaterialInfo(barcode);
            if (resultInfo.getHeaderStatus()) {
                mView.bindListView(mModel.getList());
            } else {
                MessageBox.Show(mContext, resultInfo.getMessage() );

            }

        }

    }

}
