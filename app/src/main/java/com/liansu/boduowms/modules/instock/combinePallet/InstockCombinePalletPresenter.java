package com.liansu.boduowms.modules.instock.combinePallet;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.CombinePalletInfo;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan.InStockReturnStorageScan;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_NONE;


/**
 * @ Des:
 * @ Created by yangyiqing on 2019/11/14.
 */
public class InstockCombinePalletPresenter {
    private Context                   mContext;
    private InstockCombinePalletModel mModel;
    private IInstockCombinePalletView mView;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
        switch (msg.what) {
            case NetworkError.NET_ERROR_CUSTOM:
                MessageBox.Show(mContext, "获取请求失败_____" + msg.obj, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
        }
    }

    public InstockCombinePalletPresenter(Context context, IInstockCombinePalletView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new InstockCombinePalletModel(context, handler);
    }


    public InstockCombinePalletModel getModel() {
        return mModel;
    }


    protected String getTitle() {
        return mContext.getResources().getString(R.string.in_stock_combine_pallet_title) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
    }

    /**
     * /**
     *
     * @desc: 扫描托盘条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/14 17:39
     */


    public void scanPalletInfo(String scanBarcode, final int palletType) {
        try {
            if (palletType == InstockCombinePalletModel.PALLET_TYPE_SECOND_PALLET) {
                if (mModel.getTargetPalletInfoList().size() == 0) {
                    MessageBox.Show(mContext, "请先扫描第一个托盘编码", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.requestPalletOneFocus();
                        }
                    });
                    return;
                }
            } else if (palletType == InstockCombinePalletModel.PALLET_TYPE_FIRST_PALLET) {
                mModel.setTargetPalletInfoList(null);
            }

            OutBarcodeInfo scanQRCode = null;
            if (scanBarcode.equals("")) return;
            BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(scanBarcode);
            if (resultInfo.getHeaderStatus()) {
                scanQRCode = resultInfo.getInfo();
            } else {
                MessageBox.Show(mContext, resultInfo.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.requestPalletFocus(palletType);
                    }
                });
                return;
            }
            if (scanQRCode != null) {
                if (!(scanQRCode.getBarcodetype() == QRCodeFunc.BARCODE_TYPE_PALLET_NO || scanQRCode.getBarcodetype() == QRCodeFunc.BARCODE_TYPE_MIXING_PALLET_NO)) {
                    MessageBox.Show(mContext, "条码解析失败:条码规则不正确,请扫描托盘条码", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.requestPalletFocus(palletType);
                        }
                    });
                    return;
                }
                scanQRCode.setVouchertype(OrderType.IN_HOUSE_STOCK_ORDER_TYPE_COMBINE_PALLET_VALUE);
                scanQRCode.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
                scanQRCode.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
                mModel.requestBarcodeInfo(scanQRCode, new NetCallBackListener<String>() {
                    @Override
                    public void onCallBack(String result) {
                        LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, result);
                        try {
                            BaseResultInfo<List<StockInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<StockInfo>>>() {
                            }.getType());
                            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                List<StockInfo> list = returnMsgModel.getData();
                                if (list != null) {
                                    if (palletType == InstockCombinePalletModel.PALLET_TYPE_FIRST_PALLET) {
                                        BaseMultiResultInfo<Boolean, Void> checkResult = mModel.checkTargetPalletInfoList(list);
                                        if (checkResult.getHeaderStatus()) {
                                            mModel.setTargetPalletInfoList(list);
                                        } else {
                                            MessageBox.Show(mContext, checkResult.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mView.requestPalletFocus(palletType);
                                                }
                                            });
                                            return;
                                        }

                                    } else if (palletType == InstockCombinePalletModel.PALLET_TYPE_SECOND_PALLET) {
                                        int count = mModel.getSerialNoCount();
                                        if (count > 2) {
                                            MessageBox.Show(mContext, "校验条码失败,拼托的托盘码不能超过2个", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mView.requestPalletFocus(palletType);
                                                }
                                            });
                                            return;
                                        }
                                        BaseMultiResultInfo<Boolean, Void> checkResult = mModel.checkBarcode(list);
                                        if (checkResult.getHeaderStatus()) {
                                            mModel.setAwaitPalletInfoList(list);
                                        } else {
                                            MessageBox.Show(mContext, checkResult.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mView.requestPalletFocus(palletType);
                                                }
                                            });
                                        }
                                    }
                                    mView.bindListView(mModel.getShowList());
                                    mView.requestPalletTwoFocus();
                                } else {
                                    MessageBox.Show(mContext, "条码查询失败,获取的条码信息为空", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.requestPalletFocus(palletType);
                                        }
                                    });
                                }
                            } else {
                                MessageBox.Show(mContext, "条码查询失败: " + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.requestPalletFocus(palletType);
                                    }
                                });
                            }

                        } catch (Exception ex) {
                            MessageBox.Show(mContext, "条码查询失败: " + ex.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.requestPalletFocus(palletType);
                                }
                            });
                        }
                    }
                });

            } else {
                MessageBox.Show(mContext, "解析条码失败，条码格式不正确" + scanBarcode, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.requestPalletFocus(palletType);
                    }
                });
                return;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.requestPalletFocus(palletType);
                }
            });
            return;
        }

    }


    /**
     * @desc: 拆托条码提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 22:37
     */
    protected void onDisCombinePalletListRefer(List<StockInfo> selectedList) {
        List<StockInfo> list = mModel.getShowList();
        if (list == null || list.size() == 0) {
            MessageBox.Show(mContext, "扫描数据为空!请先进行扫描操作");
            return;
        }
        if (selectedList == null || selectedList.size() == 0) {
            MessageBox.Show(mContext, "没有选中的数据!请先选择要删除的托盘物料行");
            return;
        }
        if (selectedList.size() == list.size()) {
            MessageBox.Show(mContext, "不能全部删除该托盘码上的物料!至少保留一个物料");
            return;
        }

        CombinePalletInfo postInfo = new CombinePalletInfo();
        postInfo.setTargetPalletNo(mModel.getTargetPalletInfoList().get(0).getBarcode());
        postInfo.setCombinePalletType(mView.getCombinePalletType());
        postInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
        postInfo.setPrintertype(UrlInfo.mInStockPrintType);
        postInfo.setPrintername(UrlInfo.mInStockPrintName);
        postInfo.setStockList(selectedList);
        mModel.requestDisCombineOrderRefer(postInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(InStockReturnStorageScan.class, mModel.TAG_DIS_COMBINE_PALLET_REFER, result);
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
                    MessageBox.Show(mContext, ex.getMessage());
                }

            }

        });
    }

    /**
     * @desc: 组托条码提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/25 15:52
     */
    protected void onCombinePalletListRefer() {
        try {
            if (mModel.getTargetPalletInfoList().size() == 0) {
                MessageBox.Show(mContext, "拼入托盘数据为空,请扫描拼入托盘", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.requestPalletOneFocus();
                    }
                });
                return;
            }
            if (mModel.getAwaitPalletInfoList().size() == 0) {
                MessageBox.Show(mContext, "待拼托盘数据为空,请扫描待拼托盘", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.requestPalletTwoFocus();
                    }
                });
                return;
            }
            CombinePalletInfo postInfo = new CombinePalletInfo();
            postInfo.setTargetPalletNo(mModel.getTargetPalletInfoList().get(0).getBarcode());
            postInfo.setAwaitPalletNo(mModel.getAwaitPalletInfoList().get(0).getBarcode());
            postInfo.setCombinePalletType(mView.getCombinePalletType());
            postInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
            postInfo.setPrintertype(UrlInfo.mInStockPrintType);
            postInfo.setPrintername(UrlInfo.mInStockPrintName);
            mModel.requestOrderRefer(postInfo, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_COMBINE_PALLET_REFER, result);
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
                        MessageBox.Show(mContext, ex.getMessage());
                    }

                }

            });
        } catch (Exception e) {
            MessageBox.Show(mContext, "出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

    }

    /**
     * @desc: 重置界面
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/18 21:03
     */
    public void onReset() {
        mModel.onReset();
        mView.onReset();

    }


}
