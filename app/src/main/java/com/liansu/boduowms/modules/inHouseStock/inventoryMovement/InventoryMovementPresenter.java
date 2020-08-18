package com.liansu.boduowms.modules.inHouseStock.inventoryMovement;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.hander.MyHandler;


/**
 * @ Des:
 * @ Created by yangyiqing on 2019/11/14.
 */
public class InventoryMovementPresenter {
    private Context                 mContext;
    private InventoryMovementModel  mModel;
    private IInventoryMovementView  mView;
    private MyHandler<BaseActivity> mHandler;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
        switch (msg.what) {

        }
    }

    public InventoryMovementPresenter(Context context, IInventoryMovementView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new InventoryMovementModel(context, handler);
        this.mHandler = handler;
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
//            OutBarcodeInfo scanQRCode = null;
            if (scanBarcode == null || scanBarcode.equals("")) return;
//            if (scanBarcode.contains("%")) {
//                BaseResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(scanBarcode);
//                if (resultInfo.getHeaderStatus()) {
//                    scanQRCode = resultInfo.getInfo();
//                } else {
//                    MessageBox.Show(mContext, resultInfo.getMessage());
//                    return;
//                }
//
//            }

            mModel.requestBarcodeInfo(scanBarcode, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    try {
////                            LogUtil.WriteLog(ReceiptionScan.class, mModel.TAG_GetAreaModelADF, result);
//                        ReturnMsgModel<StockInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<StockInfo>>() {
//                        }.getType());
//                        if (returnMsgModel.getHeaderStatus().equals("S")) {
//                            StockInfo stockInfo = returnMsgModel.getModelJson();
//                            if (stockInfo != null) {
//                                mModel.setStockInfo(stockInfo);
//                                mView.setBarcodeInfo(stockInfo);
//                            } else {
//                                MessageBox.Show(mContext, "出现预期之外的异常,获取条码信息为空");
//                                return;
//                            }
//                        } else {
//                            MessageBox.Show(mContext, returnMsgModel.getMessage());
//                            mView.requestBarcodeFocus();
//                        }
                    } catch (Exception ex) {
                        MessageBox.Show(mContext, ex.getMessage() );
                        mView.requestBarcodeFocus();
                    }

                }
            });

        } catch (Exception e) {
            MessageBox.Show(mContext, e.getMessage() );
            return;
        }

    }

    /**
     * @desc: 获取库位信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 15:31
     */
    public void scanMoveInAreaInfo(String areaNo) {
        if (mModel.getStockInfo() == null) {
            MessageBox.Show(mContext, "请先扫描条码" );
            return;
        }
        if (!areaNo.equals("")) {
            mModel.requestAreaInfo(areaNo, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    try {
////                        LogUtil.WriteLog(ReceiptionScan.class, mModel.TAG_GetAreaModelADF, result);
//                        ReturnMsgModel<AreaInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<AreaInfo>>() {
//                        }.getType());
//                        if (returnMsgModel.getHeaderStatus().equals("S")) {
//                            AreaInfo areaInfo = returnMsgModel.getModelJson();
//                            if (areaInfo != null) {
//                                mModel.setMoveInAreaNo(areaInfo);
//                                mView.requestMoveOutAreaNoFocus();
//                            } else {
//                                MessageBox.Show(mContext, "出现预期之外的异常,获取的库位信息为空");
//                                return;
//                            }
//                        } else {
//                            MessageBox.Show(mContext, returnMsgModel.getMessage());
//                            mView.requestMoveInAreaNoFocus();
//                        }
                    } catch (Exception ex) {
                        MessageBox.Show(mContext, ex.getMessage() );
                        mView.requestMoveInAreaNoFocus();
                    }

                }
            });
        }
    }


    /**
     * @desc: 获取库位信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 15:31
     */
    public void scanMoveOutAreaInfo(String areaNo) {
        if (mModel.getMoveInAreaNo() == null) {
            MessageBox.Show(mContext, "请先扫描或输入移入库位" );
            return;
        }
        if (!areaNo.equals("")) {
            mModel.requestAreaInfo(areaNo, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    try {
////                        LogUtil.WriteLog(ReceiptionScan.class, mModel.TAG_GetAreaModelADF, result);
//                        ReturnMsgModel<AreaInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<AreaInfo>>() {
//                        }.getType());
//                        if (returnMsgModel.getHeaderStatus().equals("S")) {
//                            AreaInfo areaInfo = returnMsgModel.getModelJson();
//                            if (areaInfo != null) {
//                                mModel.setMoveOutAreaNo(areaInfo);
//                                mView.requestQtyFocus();
//                            } else {
//                                MessageBox.Show(mContext, "出现预期之外的异常,获取的库位信息为空");
//                                return;
//                            }
//                        } else {
//                            MessageBox.Show(mContext, returnMsgModel.getMessage());
//                            mView.requestMoveOutAreaNoFocus();
//                        }
                    } catch (Exception ex) {
                        MessageBox.Show(mContext, ex.getMessage() );
                        mView.requestMoveOutAreaNoFocus();
                    }

                }
            });
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
        String inMoveAreaNo = mView.getMoveInAreaNo();
        String outMoveAreaNo = mView.getMoveOutAreaNo();
        float qty = mView.getQty();
        if (inMoveAreaNo == null || inMoveAreaNo.equals("") ||mModel.getMoveInAreaNo()==null) {
            MessageBox.Show(mContext, "请输入或扫描移入库位" );
            return;
        }
        if (outMoveAreaNo == null || outMoveAreaNo.equals("") ||mModel.getMoveOutAreaNo()==null) {
            MessageBox.Show(mContext, "请输入或扫描移出库位" );
            return;
        }
        if (qty <= 0) {
            MessageBox.Show(mContext, "输入的数量必须大于零" );
            return;
        }
        if (mModel.getStockInfo()==null){
            MessageBox.Show(mContext, "请扫描条码信息" );
            return;
        }

        StockInfo stockInfo=new StockInfo();
        mModel.requestRefer(stockInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
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
            }
        });


    }


    public void onClear() {
        mView.onClear();
        mModel.onClear();


    }


}
