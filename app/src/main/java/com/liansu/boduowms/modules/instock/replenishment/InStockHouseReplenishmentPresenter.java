package com.liansu.boduowms.modules.instock.replenishment;

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

import java.util.List;

import androidx.annotation.NonNull;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_NONE;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/10/10.
 */
public class InStockHouseReplenishmentPresenter {
    protected Context                        mContext;
    protected InStockHouseReplenishmentModel mModel;
    protected IInStockHouseReplenishmentView mView;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);

    }

    public InStockHouseReplenishmentPresenter(Context context, IInStockHouseReplenishmentView view, MyHandler<BaseActivity> handler,int voucherType) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new InStockHouseReplenishmentModel(mContext, handler,voucherType);

    }

    public InStockHouseReplenishmentModel getModel() {
        return mModel;
    }

    /**
     * @desc: 扫描移出托盘
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/10/10 15:32
     */
    public void onOutPalletScan(@NonNull String outPalletNo) {
        try {
            mModel.setCurrentMaterialInfo(null);
            mModel.setOutPalletInfoList(null);
            OutBarcodeInfo scanQRCode = null;
            if (outPalletNo.equals("")) return;
            if (outPalletNo.contains("%")) {
                BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(outPalletNo);
                if (resultInfo.getHeaderStatus()) {
                    scanQRCode = resultInfo.getInfo();
                    if (scanQRCode.getBarcodetype() != QRCodeFunc.BARCODE_TYPE_PALLET_NO) {
                        MessageBox.Show(mContext, "校验条码失败:托盘格式不正确,请扫描托盘码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onOutPalletNoFocus();
                            }
                        });
                        return;
                    }
                } else {
                    MessageBox.Show(mContext, resultInfo.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onOutPalletNoFocus();
                        }
                    });
                    return;
                }

            } else {
                MessageBox.Show(mContext, "校验条码失败:托盘格式不正确,请扫描托盘码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onOutPalletNoFocus();
                    }
                });
                return;
            }
            if (scanQRCode != null) {
                scanQRCode.setVouchertype(mModel.getVoucherType());
                scanQRCode.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
                scanQRCode.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
                mModel.requestOutPalletInfoQuery(scanQRCode, new NetCallBackListener<String>() {
                    @Override
                    public void onCallBack(String result) {
                        LogUtil.WriteLog(InStockHouseReplenishment.class, mModel.TAG_GET_OUT_PALLET_INFO_QUERY, result);
                        try {
                            BaseResultInfo<List<StockInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<StockInfo>>>() {
                            }.getType());
                            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                List<StockInfo> list = returnMsgModel.getData();
                                if (list != null && list.size() > 0) {
                                    mModel.setOutPalletInfoList(list);
                                    mView.bindListView(mModel.getOutPalletInfoList());
                                    mView.onOutPalletQtyFocus();
                                } else {
                                    MessageBox.Show(mContext, "查询移出托盘码信息失败:获取的托盘数据为空," + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.bindListView(mModel.getOutPalletInfoList());
                                            mView.onOutPalletNoFocus();
                                        }
                                    });
                                }
                            } else {
                                MessageBox.Show(mContext, "查询托盘码失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.bindListView(mModel.getOutPalletInfoList());
                                        mView.onOutPalletNoFocus();
                                    }
                                });
                            }

                        } catch (Exception ex) {
                            MessageBox.Show(mContext, "查询托盘码失败，出现预期之外的异常-" + ex.getMessage() + ",", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.bindListView(mModel.getOutPalletInfoList());
                                    mView.onOutPalletNoFocus();
                                }
                            });
                        }
                    }
                });

            } else {
                MessageBox.Show(mContext, "解析条码失败，条码格式不正确" + outPalletNo, MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.bindListView(mModel.getOutPalletInfoList());
                        mView.onOutPalletNoFocus();
                    }
                });
                return;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "查询移出条码失败，出现预期之外的异常:" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.bindListView(mModel.getOutPalletInfoList());
                    mView.onOutPalletNoFocus();
                }
            });
            return;
        }
    }


    /**
     * @desc: 校验移出托盘的选择的物料数量
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/10/10 16:52
     */
    public boolean checkOutPalletQty(float qty) {
        mModel.setCurrentMaterialInfo(null);
        StockInfo sCurrentMaterialInfo = null;
        if (qty == 0) {
            MessageBox.Show(mContext, "移出数量必须大于0", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onOutPalletQtyFocus();
                }
            });
            return false;
        }
        List<StockInfo> outPalletInfoList = mModel.getOutPalletInfoList();
        //如果出现混托的情况，就要校验是否选择物料
        if (outPalletInfoList.size() > 1) {
            List<StockInfo> selectedItemList = mView.getSelectedMaterialItems();
            if (selectedItemList == null || selectedItemList.size() == 0) {
                MessageBox.Show(mContext, "移出托盘存在多个物料,请选择一个物料后再输入数量", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onOutPalletQtyFocus();
                    }
                });
                return false;
            }
            if (selectedItemList != null && selectedItemList.size() > 1) {
                MessageBox.Show(mContext, "移出托盘存在多个物料,不能选择多个物料,请选择一个物料后再输入数量", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onOutPalletQtyFocus();
                    }
                });
                return false;
            }

            sCurrentMaterialInfo = selectedItemList.get(0);
        } else if (outPalletInfoList.size() == 1) {
            sCurrentMaterialInfo = outPalletInfoList.get(0);
        } else if (outPalletInfoList.size() == 0) {
            MessageBox.Show(mContext, "移出托盘信息不能为空,请扫描托盘码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onOutPalletQtyFocus();
                }
            });
            return false;
        }

        if (sCurrentMaterialInfo != null) {
            float sQty = sCurrentMaterialInfo.getQty();
            if (qty > sQty) {
                MessageBox.Show(mContext, "输入的数量[" + qty + "]不能大于当前选择的物料[" + sCurrentMaterialInfo.getMaterialno() + "]的库存数量[" + sQty + "]", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onOutPalletQtyFocus();
                    }
                });
                return false;
            }

        } else {
            MessageBox.Show(mContext, "移出托盘上选择的物料信息不能为空,请扫描托盘码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onOutPalletQtyFocus();
                }
            });
            return false;
        }


        mModel.setCurrentMaterialInfo(sCurrentMaterialInfo);
        return true;
    }


    /**
     * @desc: 扫描移入托盘
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/10/10 15:32
     */
    public void onInPalletScan(@NonNull String inPalletNo) {
        try {
            mModel.setInPalletInfoList(null);
            mModel.setAreaInfo(null);
            OutBarcodeInfo scanQRCode = null;
            if (inPalletNo.equals("")) return;
            if (inPalletNo.contains("%")) {
                BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(inPalletNo);
                if (resultInfo.getHeaderStatus()) {
                    scanQRCode = resultInfo.getInfo();
                    if (scanQRCode.getBarcodetype() != QRCodeFunc.BARCODE_TYPE_PALLET_NO) {
                        MessageBox.Show(mContext, "校验条码失败:托盘格式不正确,请扫描托盘码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onInPalletNoFocus();
                            }
                        });
                        return;
                    }
                } else {
                    MessageBox.Show(mContext, resultInfo.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onInPalletNoFocus();
                        }
                    });
                    return;
                }

            } else {
                MessageBox.Show(mContext, "校验条码失败:托盘格式不正确,请扫描托盘码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onInPalletNoFocus();
                    }
                });
                return;
            }
            if (scanQRCode != null) {
                mModel.requestInPalletInfoQuery(scanQRCode, new NetCallBackListener<String>() {
                    @Override
                    public void onCallBack(String result) {
                        LogUtil.WriteLog(InStockHouseReplenishment.class, mModel.TAG_GET_IN_PALLET_INFO_QUERY, result);
                        try {
                            BaseResultInfo<List<StockInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<StockInfo>>>() {
                            }.getType());
                            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                List<StockInfo> list = returnMsgModel.getData();
                                if (list != null && list.size() > 0) {
                                    BaseMultiResultInfo<Boolean, Void> checkMaterial = mModel.isMaterialInInPalletInfoList(mModel.getCurrentMaterialInfo(), list);
                                    if (!checkMaterial.getHeaderStatus()) {
                                        MessageBox.Show(mContext, checkMaterial.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mView.onInPalletNoFocus();
                                            }
                                        });
                                        return;
                                    }
                                    mModel.setInPalletInfoList(list);
                                    StockInfo stockInfo = list.get(0);
                                    String areaNo = stockInfo.getAreano();
                                    if (areaNo != null && !areaNo.trim().equals("")) {
                                        mView.setInPalletAreaNo(areaNo);
                                        mView.setInPalletAreaNoEnable(false);
                                    } else {
                                        mView.setInPalletAreaNoEnable(true);
                                        mView.onInPalletAreaNoFocus();
                                    }


                                } else {
                                    MessageBox.Show(mContext, "查询移入托盘码信息失败:获取的托盘数据为空," + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.onInPalletNoFocus();
                                        }
                                    });
                                }
                            } else {
                                MessageBox.Show(mContext, "查询托盘码失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onInPalletNoFocus();
                                    }
                                });
                            }

                        } catch (Exception ex) {
                            MessageBox.Show(mContext, "查询托盘码失败，出现预期之外的异常-" + ex.getMessage() + ",", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onInPalletNoFocus();
                                }
                            });
                        }
                    }
                });

            } else {
                MessageBox.Show(mContext, "解析条码失败，条码格式不正确" + inPalletNo, MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onInPalletNoFocus();
                    }
                });
                return;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "查询移入托盘条码失败，出现预期之外的异常:" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onInPalletNoFocus();
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
     * @time 2020/10/10 16:24
     */
    public void getAreaInfo(String areaNo) {
        if (mModel.getInPalletInfoList().size() == 0) {
            MessageBox.Show(mContext, "移入托盘信息为空,请先扫描移入托盘", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onInPalletNoFocus();
                }
            });
            return;
        }
        mModel.setAreaInfo(null);
        if (areaNo.equals("")) return;
        AreaInfo areaInfo = new AreaInfo();
        areaInfo.setAreano(areaNo);
        areaInfo.setWarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
        mModel.requestAreaNo(areaInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(InStockHouseReplenishment.class, mModel.TAG_GET_IN_PALLET_AREA_INFO_QUERY, result);
                try {
                    BaseResultInfo<AreaInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<AreaInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        AreaInfo data = returnMsgModel.getData();
                        if (data != null) {
                            mModel.setAreaInfo(data);
                        } else {
                            MessageBox.Show(mContext, "获取的库位信息为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onInPalletAreaNoFocus();
                                }
                            });

                        }
                    } else {
                        MessageBox.Show(mContext, "获取的库位信息失败，" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onInPalletAreaNoFocus();
                            }
                        });

                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取的库位信息出现预期之外的异常，" + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onInPalletAreaNoFocus();
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
     * @time 2020/10/11 15:03
     */
    public void onRefer() {
        //校验移出托盘信息和输入的数量，并获取选择的物料信息
        if (!checkOutPalletQty(mView.getInPalletQty())) return;
        //校验选择的物料信息是否为空
        if (mModel.getCurrentMaterialInfo() == null) {
            MessageBox.Show(mContext, "校验移出托盘信息失败,获取选择的移出托盘的物料信息不能为空,请扫描移出托盘码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onOutPalletQtyFocus();
                }
            });
            return;
        }
        //校验移入托盘的信息是否为空
        if (mModel.getInPalletInfoList().size() == 0) {
            MessageBox.Show(mContext, "校验移入托盘信息失败,移入托盘的信息不能为空,请先扫描移入托盘码", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onInPalletNoFocus();
                }
            });
            return;
        }
        // 校验移出托盘的物料是否和移入托盘的物料匹配
        BaseMultiResultInfo<Boolean, Void> checkMaterial = mModel.isMaterialInInPalletInfoList(mModel.getCurrentMaterialInfo(), mModel.getInPalletInfoList());
        if (!checkMaterial.getHeaderStatus()) {
            MessageBox.Show(mContext, checkMaterial.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onInPalletNoFocus();
                }
            });
            return;
        }
        StockInfo postInfo = new StockInfo();
        StockInfo moveInStockInfo = mModel.getInPalletInfoList().get(0);
        String areaNo = moveInStockInfo.getAreano();
        if (areaNo == null || areaNo.trim().equals("")) {
            if (mModel.getAreaInfo() == null || mModel.getAreaInfo().getAreano() == null || mModel.getAreaInfo().equals("")) {
                MessageBox.Show(mContext, "校验移入库位失败，移入库位不能为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onInPalletAreaNoFocus();
                    }
                });
                return;
            } else {
                postInfo.setTowarehouseno(mModel.getAreaInfo().getWarehouseno());
                postInfo.setTowarehouseid(mModel.getAreaInfo().getWarehouseid());
                postInfo.setHouseno(mModel.getAreaInfo().getHouseno());
                postInfo.setHouseid(mModel.getAreaInfo().getHouseid());
                postInfo.setAreano(mModel.getAreaInfo().getAreano());
                postInfo.setAreaid(mModel.getAreaInfo().getId());
            }
        } else {
            postInfo.setAreaid(moveInStockInfo.getAreaid());
            postInfo.setAreano(moveInStockInfo.getAreano());
        }

        postInfo.setOldbarcode(mModel.getCurrentMaterialInfo().getBarcode());
        postInfo.setMaterialno(mModel.getCurrentMaterialInfo().getMaterialno());
        postInfo.setMaterialnoid(mModel.getCurrentMaterialInfo().getMaterialnoid());
        postInfo.setBatchno(mModel.getCurrentMaterialInfo().getBatchno());
        postInfo.setQty(mView.getInPalletQty());
        postInfo.setNewbarcode(moveInStockInfo.getBarcode());
        mModel.requestReplenishmentInfoRefer(postInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_REPLENISHMENT_INFO_REFER, result);
                try {
                    BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onReset();
                            }
                        });

                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue());
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "提交补货信息出现预期之外的异常:" + ex.getMessage());
                }


            }
        });

    }

    /**
     * @desc: 重置数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/10/11 15:30
     */
    public void onReset() {
        mModel.onReset();
        mView.onReset();
    }
}
