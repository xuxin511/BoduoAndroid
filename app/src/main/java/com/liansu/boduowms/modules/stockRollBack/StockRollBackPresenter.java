package com.liansu.boduowms.modules.stockRollBack;

import android.app.AlertDialog;
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
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.stock.VoucherDetailSubInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_NONE;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/26.
 */
public class StockRollBackPresenter {
    protected Context            mContext;
    protected StockRollBackModel mModel;
    protected IStockRollBackView mView;

    public StockRollBackPresenter(Context context, IStockRollBackView view, MyHandler<BaseActivity> handler, String erpVoucherNo, int voucherType, String title) {
        mContext = context;
        mView = view;
        mModel = new StockRollBackModel(context, handler, erpVoucherNo, voucherType, title);

    }

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
        switch (msg.what) {
            case NetworkError.NET_ERROR_CUSTOM:
                MessageBox.Show(mContext, "获取请求失败_____" + msg.obj, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                    }
                });
                break;
        }
    }


    protected String getTitle() {
        return mModel.getTitle() ;
    }

    /**
     * @desc: 扫描托盘条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/27 14:45
     */
    public void onScan(String scanBarcode) {
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
                            mView.onBarcodeFocus();
                        }
                    });
                    return;
                }
            } else {
                MessageBox.Show(mContext, resultInfo.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                    }
                });
                return;
            }

        } else {
            MessageBox.Show(mContext, "校验条码失败:托盘格式不正确,请扫描托盘码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onBarcodeFocus();
                }
            });
            return;
        }

        if (scanQRCode != null) {
            VoucherDetailSubInfo scanInfo = mModel.getPalletInfo(scanQRCode.getBarcode());
            if (scanInfo != null) {
                onDeleteTemporaryDataRefer(scanInfo);
            } else {
                MessageBox.Show(mContext, "校验条码失败:条码:[" + scanQRCode.getBarcode() + "]不在暂存列表中", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                    }
                });
                return;
            }
        }
    }


    /**
     * @desc: 获取单据暂存数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/27 14:46
     */
    public void getTemporaryDetailList() {
        if (mModel.getErpVoucherNo() == null || mModel.equals("")) {
            MessageBox.Show(mContext, "获取订单暂存数据失败：从上页面传入的订单号不能为空", MEDIA_MUSIC_ERROR);
            return;
        }
        OrderRequestInfo postInfo=new OrderRequestInfo();
        postInfo.setVouchertype(mModel.getVoucherType());
        postInfo.setErpvoucherno(mModel.getErpVoucherNo());
        mModel.requestTemporaryDetailList(postInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_DETAIL_SUB_ASYNC, result);
                try {
                    BaseResultInfo<List<VoucherDetailSubInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<VoucherDetailSubInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        List<VoucherDetailSubInfo> list = returnMsgModel.getData();
                        if (list != null && list.size() > 0) {
                            mModel.setTemporaryList(list);
                            mView.bindRecycleView(mModel.getTemporaryList());
                            mView.setErpVoucherNo(mModel.getErpVoucherNo());
                            mView.onBarcodeFocus();
                        } else {
                            mModel.getTemporaryList().clear();
                            mView.bindRecycleView(mModel.getTemporaryList());
                            mView.onBarcodeFocus();
//                            MessageBox.Show(mContext, "获取订单号[" + mModel.getErpVoucherNo() + "]的暂存信息为空", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    mModel.getTemporaryList().clear();
//                                    mView.bindRecycleView(mModel.getTemporaryList());
//                                    mView.onBarcodeFocus();
//                                }
//                            });
                        }
                    } else {
                        MessageBox.Show(mContext, "获取订单号[" + mModel.getErpVoucherNo() + "]的暂存信息失败:" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mModel.getTemporaryList().clear();
                                mView.bindRecycleView(mModel.getTemporaryList());
                                mView.onBarcodeFocus();
                            }
                        });
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取订单号[" + mModel.getErpVoucherNo() + "]的暂存信息失败:" + ex.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mModel.getTemporaryList().clear();
                            mView.bindRecycleView(mModel.getTemporaryList());
                            mView.onBarcodeFocus();
                        }
                    });
                }


            }
        });

    }

    /**
     * @desc: 删除暂存数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/27 15:09
     */
    public void onDeleteTemporaryDataRefer(final VoucherDetailSubInfo deleteInfo) {
        if (deleteInfo == null) {
            MessageBox.Show(mContext, "校验回退数据失败:提交的回退条码数据不能为空", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onBarcodeFocus();
                }
            });
        }

        new AlertDialog.Builder(BaseApplication.context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除托盘:"+deleteInfo.getBarcode()+"?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<VoucherDetailSubInfo> postList = new ArrayList<>();
                        postList.add(deleteInfo);
                        mModel.requestDeleteTemporaryDetail(postList, new NetCallBackListener<String>() {
                            @Override
                            public void onCallBack(String result) {
                                try {
                                    LogUtil.WriteLog(StockRollBack.class, mModel.TAG_DELETE_T_DETAIL_SUB_ASYNC, result);
                                    BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                                    }.getType());
                                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                getTemporaryDetailList();
                                            }
                                        });
                                    } else {
                                        MessageBox.Show(mContext, "条码回退失败:" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mView.onBarcodeFocus();
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    MessageBox.Show(mContext, "条码回退失败,出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.onBarcodeFocus();
                                        }
                                    });
                                }
                            }
                        });

                    }
                }).setNegativeButton("取消", null).show();

    }

}
