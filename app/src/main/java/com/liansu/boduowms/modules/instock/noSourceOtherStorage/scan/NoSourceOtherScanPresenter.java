package com.liansu.boduowms.modules.instock.noSourceOtherStorage.scan;

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
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_NONE;

/**
 * @desc: 无源杂入
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/7/18 19:24
 */
public class NoSourceOtherScanPresenter {

    protected Context                       mContext;
    protected NoSourceOtherStorageScanModel mModel;
    protected INoSourceOtherScanView        mView;
    protected PrintBusinessModel            mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public NoSourceOtherScanPresenter(Context context, INoSourceOtherScanView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new NoSourceOtherStorageScanModel(context, handler);
        this.mPrintModel = new PrintBusinessModel(context, handler);
    }

    public NoSourceOtherStorageScanModel getModel() {
        return mModel;
    }


    protected String getTitle() {
        return mContext.getResources().getString(R.string.appbar_title_no_source_scan) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename();
    }

    /**
     * @desc: 获取库位
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/18 20:20
     */
    public void getAreaInfo(String areaNo) {
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
                            mView.onBarcodeFocus();
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
                        mView.onBarcodeFocus();
                    }
                });
                return;
            }
            if (scanQRCode != null) {
                if (scanQRCode.getSerialno() == null) {
                    MessageBox.Show(mContext, "条码解析失败:条码规则不正确,请扫描托盘条码", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onBarcodeFocus();
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
                                    BaseMultiResultInfo<Boolean, Void> checkResult = mModel.checkBarcode(outBarcodeInfo);
                                    if (checkResult.getHeaderStatus()) {
                                        mView.bindListView(mModel.getList());
                                        mView.onBarcodeFocus();
                                    } else {
                                        MessageBox.Show(mContext, checkResult.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mView.onBarcodeFocus();
                                            }
                                        });
                                    }
                                } else {
                                    MessageBox.Show(mContext, "条码查询失败,获取的条码信息为空", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.onBarcodeFocus();
                                        }
                                    });
                                }
                            } else {
                                MessageBox.Show(mContext, "条码查询失败: " + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onBarcodeFocus();
                                    }
                                });
                            }

                        } catch (Exception ex) {
                            MessageBox.Show(mContext, "条码查询失败: " + ex.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onBarcodeFocus();
                                }
                            });
                        }
                    }
                });

            } else {
                MessageBox.Show(mContext, "解析条码失败，条码格式不正确" + scanBarcode, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                    }
                });
                return;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onBarcodeFocus();
                }
            });
            return;
        }

    }

    /**
     * @desc: 提交信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 22:37
     */

    protected void onOrderRefer() {
        List<OutBarcodeInfo> list = mModel.getList();
        if (list == null || list.size() == 0) {
            MessageBox.Show(mContext, "扫描数据为空!请先进行扫描操作");
            return;
        }
        mModel.requestOrderRefer(list, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC, result);
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
     * @desc: 重置界面
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/18 21:03
     */
    private void onReset() {
        mView.onReset();
        mModel.onReset();
    }


}
