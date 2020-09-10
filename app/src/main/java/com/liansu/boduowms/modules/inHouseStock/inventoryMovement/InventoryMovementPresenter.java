package com.liansu.boduowms.modules.inHouseStock.inventoryMovement;

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
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_NONE;


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
     * @desc: 扫描托盘条码
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
                    if (scanQRCode.getBarcodetype() != QRCodeFunc.BARCODE_TYPE_PALLET_NO) {
                        MessageBox.Show(mContext, "校验条码失败:托盘格式不正确,请扫描托盘码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.requestBarcodeFocus();
                            }
                        });
                        return;
                    }
                } else {
                    MessageBox.Show(mContext, resultInfo.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.requestBarcodeFocus();
                        }
                    });
                    return;
                }

            } else {
                MessageBox.Show(mContext, "校验条码失败:托盘格式不正确,请扫描托盘码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.requestBarcodeFocus();
                    }
                });
                return;
            }
            if (scanQRCode != null) {
                mModel.requestBarcodeInfo(scanQRCode, new NetCallBackListener<String>() {
                    @Override
                    public void onCallBack(String result) {
                        LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, result);
                        try {
                            BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutBarcodeInfo>>() {
                            }.getType());
                            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                OutBarcodeInfo outBarcodeInfo = returnMsgModel.getData();
                                if (outBarcodeInfo != null) {
//                                    mModel.setStockInfo();
//                                    mView.
                                } else {
                                    MessageBox.Show(mContext, "查询托盘码失败:获取的托盘数据为空," + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            mView.onBarcodeFocus();
                                        }
                                    });
                                }
                            } else {
                                MessageBox.Show(mContext, "查询托盘码失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        mView.onBarcodeFocus();
                                    }
                                });
                            }

                        } catch (Exception ex) {
                            MessageBox.Show(mContext, "查询托盘码失败，出现预期之外的异常-" + ex.getMessage() + ",", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.requestBarcodeFocus();
                                }
                            });
                        }
                    }
                });

            } else {
                MessageBox.Show(mContext, "解析条码失败，条码格式不正确" + scanBarcode, MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.requestBarcodeFocus();
                    }
                });
                return;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "查询条码失败，出现预期之外的异常:" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.requestBarcodeFocus();
                }
            });
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
        if (mModel.getStockList() == null) {
            MessageBox.Show(mContext, "请先扫描条码");
            return;
        }
        if (!areaNo.equals("")) {
            getAreaInfo(areaNo, InventoryMovementModel.MOVE_TYPE_IN_AREA_NO);
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
            MessageBox.Show(mContext, "请先扫描或输入移入库位");
            return;
        }
        if (!areaNo.equals("")) {
            getAreaInfo(areaNo, InventoryMovementModel.MOVE_TYPE_OUT_AREA_NO);
        }
    }

    /**
     * @desc: 获取库位信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 15:31
     */
    public void getAreaInfo(String areaNo, final int areaType) {
        if (areaNo.equals("")) return;
        AreaInfo areaInfo = new AreaInfo();
        areaInfo.setAreano(areaNo);
        areaInfo.setWarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
        mModel.requestAreaNo(areaInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_AREA_MODEL, result);
                try {
                    BaseResultInfo<AreaInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<AreaInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        AreaInfo data = returnMsgModel.getData();
                        if (data != null) {
                            if (areaType == InventoryMovementModel.MOVE_TYPE_IN_AREA_NO) {
                                mModel.setMoveInAreaNo(data);
                                mView.requestMoveOutAreaNoFocus();
                            } else if (areaType == InventoryMovementModel.MOVE_TYPE_OUT_AREA_NO) {
                                mModel.setMoveOutAreaNo(data);
                                mView.requestMoveOutAreaNoFocus();
                            }

                        } else {
                            MessageBox.Show(mContext, "获取的库位信息为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (areaType == InventoryMovementModel.MOVE_TYPE_IN_AREA_NO) {
                                        mView.requestMoveInAreaNoFocus();
                                    } else if (areaType == InventoryMovementModel.MOVE_TYPE_OUT_AREA_NO) {
                                        mView.requestMoveOutAreaNoFocus();
                                    }
                                }
                            });

                        }
                    } else {
                        MessageBox.Show(mContext, "获取的库位信息失败，" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (areaType == InventoryMovementModel.MOVE_TYPE_IN_AREA_NO) {
                                    mView.requestMoveInAreaNoFocus();
                                } else if (areaType == InventoryMovementModel.MOVE_TYPE_OUT_AREA_NO) {
                                    mView.requestMoveOutAreaNoFocus();
                                }
                            }
                        });

                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取的库位信息出现预期之外的异常，" + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (areaType == InventoryMovementModel.MOVE_TYPE_IN_AREA_NO) {
                                mView.requestMoveInAreaNoFocus();
                            } else if (areaType == InventoryMovementModel.MOVE_TYPE_OUT_AREA_NO) {
                                mView.requestMoveOutAreaNoFocus();
                            }
                        }
                    });

                }


            }
        });

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
        if (inMoveAreaNo == null || inMoveAreaNo.equals("") || mModel.getMoveInAreaNo() == null) {
            MessageBox.Show(mContext, "请输入或扫描移入库位");
            return;
        }
        if (outMoveAreaNo == null || outMoveAreaNo.equals("") || mModel.getMoveOutAreaNo() == null) {
            MessageBox.Show(mContext, "请输入或扫描移出库位");
            return;
        }
//        if (qty <= 0) {
//            MessageBox.Show(mContext, "输入的数量必须大于零");
//            return;
//        }
        if (mModel.getStockList() == null) {
            MessageBox.Show(mContext, "请扫描条码信息");
            return;
        }

        StockInfo stockInfo = new StockInfo();
        mModel.requestRefer(stockInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
//                  LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC, result);
                try {
                    BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                onReset();
                            }
                        });
                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue());
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage());
                }

            }

        });


    }


    public void onClear() {
        mView.onClear();
        mModel.onClear();


    }


}
