package com.liansu.boduowms.modules.instock.combinePallet;

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
        switch (msg.what){
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
            BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(scanBarcode);
            if (resultInfo.getHeaderStatus()) {
                scanQRCode = resultInfo.getInfo();
            } else {
                MessageBox.Show(mContext, resultInfo.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.requestBarcodeFocus();
                    }
                });
                return;
            }
            if (scanQRCode != null) {
                if (scanQRCode.getSerialno() == null) {
                    MessageBox.Show(mContext, "条码解析失败:条码规则不正确,请扫描托盘条码", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.requestBarcodeFocus();
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
                                    outBarcodeInfo.setStrongholdname(BaseApplication.mCurrentWareHouseInfo.getStrongholdname());
                                    outBarcodeInfo.setStrongholdcode(BaseApplication.mCurrentWareHouseInfo.getStrongholdcode());
//                                    outBarcodeInfo.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_NO_SOURCE_OTHER_STORAGE_VALUE);
                                    outBarcodeInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
                                    outBarcodeInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
//                                    outBarcodeInfo.setAreano(mModel.getAreaInfo().getAreano());
                                    outBarcodeInfo.setPostuser(BaseApplication.mCurrentUserInfo.getUserno());
                                    outBarcodeInfo.setUsername(BaseApplication.mCurrentUserInfo.getUsername());
                                    BaseMultiResultInfo<Boolean, Void> checkResult = mModel.checkBarcode(outBarcodeInfo);
                                    if (checkResult.getHeaderStatus()) {
                                        mView.bindListView(mModel.getList());
                                        mView.requestBarcodeFocus();
                                    } else {
                                        MessageBox.Show(mContext, checkResult.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mView.requestBarcodeFocus();
                                            }
                                        });
                                    }
                                } else {
                                    MessageBox.Show(mContext, "条码查询失败,获取的条码信息为空", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.requestBarcodeFocus();
                                        }
                                    });
                                }
                            } else {
                                MessageBox.Show(mContext, "条码查询失败: " + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.requestBarcodeFocus();
                                    }
                                });
                            }

                        } catch (Exception ex) {
                            MessageBox.Show(mContext, "条码查询失败: " + ex.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.requestBarcodeFocus();
                                }
                            });
                        }
                    }
                });

            } else {
                MessageBox.Show(mContext, "解析条码失败，条码格式不正确" + scanBarcode, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.requestBarcodeFocus();
                    }
                });
                return;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.requestBarcodeFocus();
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

    protected void onPalletListRefer() {
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
        mView.onClear();
        mModel.onReset();
    }



}
