package com.liansu.boduowms.modules.instock.salesReturn.scan;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.GUIDHelper;
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
 * @ Created by yangyiqing on 2020/6/27.
 */
public class SalesReturnStorageScanPresenter {
    protected Context                     mContext;
    protected SalesReturnStorageScanModel mModel;
    protected ISalesReturnStorageScanView mView;
    protected GUIDHelper mGUIDHelper;
    public void onHandleMessage(Message msg)
    {
        if (msg.what == NetworkError.NET_ERROR_CUSTOM) {
            if (mGUIDHelper.isPost()) {
                //isPost=false;
                mGUIDHelper.setReturn(false);
            }
            MessageBox.Show(mContext, "获取请求失败_____" + msg.obj, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

        } else {
            mModel.onHandleMessage(msg);
        }
        //mModel.onHandleMessage(msg);
    }

    public GUIDHelper getGUIDHelper() {
        return mGUIDHelper;
    }

    public SalesReturnStorageScanPresenter(Context context, ISalesReturnStorageScanView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new SalesReturnStorageScanModel(context, handler, OrderType.IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE_VALUE);
        mGUIDHelper=new GUIDHelper();
    }

    public SalesReturnStorageScanModel getModel() {
        return mModel;
    }

    /**
     * @desc: 获取库位信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/1 23:15
     */
    public void getAreaNo(String areaNo) {
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
                            mModel.setAreaInfo(data);
                            mView.onPalletNoFocus();
                        } else {
                            MessageBox.Show(mContext, "获取的库位信息为空", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onAreaNoFocus();
                                }
                            });
                        }
                    } else {
                        MessageBox.Show(mContext, "获取的库位信息失败：" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onAreaNoFocus();
                            }
                        });

                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取的库位信息失败：出现预期之外的异常" + ex.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onAreaNoFocus();
                        }
                    });
                }


            }
        });
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
            if (mModel.getAreaInfo() == null) {
                MessageBox.Show(mContext, "请先扫描库位信息", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onAreaNoFocus();
                    }
                });
                return;
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
                        mView.onPalletNoFocus();
                    }
                });
                return;
            }
            if (scanQRCode != null) {
                if (scanQRCode.getSerialno() == null) {
                    MessageBox.Show(mContext, "条码解析失败:条码规则不正确,请扫描托盘条码", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onPalletNoFocus();
                        }
                    });
                    return;
                }

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
                                    outBarcodeInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
                                    outBarcodeInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
                                    outBarcodeInfo.setAreano(mModel.getAreaInfo().getAreano());
                                    outBarcodeInfo.setPostuser(BaseApplication.mCurrentUserInfo.getUserno());
                                    outBarcodeInfo.setUsername(BaseApplication.mCurrentUserInfo.getUsername());
                                    outBarcodeInfo.setVouchertype(mView.getVoucherType());
                                    BaseMultiResultInfo<Boolean, Void> checkResult = mModel.checkBarcode(outBarcodeInfo);
                                    if (checkResult.getHeaderStatus()) {
                                        mModel.setCurrentPalletInfo(outBarcodeInfo);

                                        mView.setPalletNoInfo(outBarcodeInfo);
                                        mView.bindListView(mModel.getList());
                                        mView.onPalletNoFocus();
                                    } else {
                                        MessageBox.Show(mContext, checkResult.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mView.onPalletNoFocus();
                                            }
                                        });
                                    }
                                } else {
                                    MessageBox.Show(mContext, "条码查询失败,获取的条码信息为空", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.onPalletNoFocus();
                                        }
                                    });
                                }
                            } else {
                                MessageBox.Show(mContext, "条码查询失败: " + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onPalletNoFocus();
                                    }
                                });
                            }

                        } catch (Exception ex) {
                            MessageBox.Show(mContext, "条码查询失败: " + ex.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onPalletNoFocus();
                                }
                            });
                        }
                    }
                });

            } else {
                MessageBox.Show(mContext, "解析条码失败，条码格式不正确" + scanBarcode, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onPalletNoFocus();
                    }
                });
                return;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onPalletNoFocus();
                }
            });
            return;
        }

    }

    /**
     * @desc: 提交数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/2 11:10
     */
    protected void onOrderRefer() {
        List<OutBarcodeInfo> list = mModel.getList();
        if (list == null ||list.size()==0) {
            MessageBox.Show(mContext, "扫描数据为空!请先进行扫描操作");
            return;
        }
        for (OutBarcodeInfo item:list){
            item.setGuid(mGUIDHelper.getmUuid());
        }
        mGUIDHelper.setPost(false);
        mModel.requestOrderRefer(list, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC, result);
                try {
                    BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                    }.getType());
                    mGUIDHelper.setPost(true);
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        mGUIDHelper.setReturn(true);
                        mGUIDHelper.createUUID();
                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onReset();
                            }
                        });
                    } else {
                        if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_ERPPOSTERROR) {
                            mGUIDHelper.setReturn(false);
                        } else {
                            mGUIDHelper.setReturn(true);
                            mGUIDHelper.createUUID();
                        }
                        MessageBox.Show(mContext, returnMsgModel.getResultValue());
                    }

                } catch (Exception ex) {
                    mGUIDHelper.setReturn(false);
                    MessageBox.Show(mContext, ex.getMessage());
                }


            }
        });
    }


    /**
     * @desc: 重置界面数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/2 11:28
     */
    public void onReset() {
        mModel.onReset();
        mView.onReset();

    }


    public String getTitle() {
        return mContext.getResources().getString(R.string.appbar_title_sales_return_storage_scan) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
    }
}
