package com.liansu.boduowms.modules.instock.productionReturnsStorage.print;

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
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan.PurchaseInspectionProcessingScan;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/17.
 */
public class ProductionReturnsPresenter {
    protected Context                       mContext;
    protected ProductionReturnsModel        mModel;
    protected IProductionReturnsStorageView mView;
    protected PrintBusinessModel            mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public ProductionReturnsPresenter(Context context, IProductionReturnsStorageView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new ProductionReturnsModel(context, handler);
        this.mPrintModel = new PrintBusinessModel(context, handler);
    }

    public ProductionReturnsModel getModel() {
        return mModel;
    }


    /**
     * @desc: 获取订单明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 22:33
     */

    protected void getOrderDetailInfoList(String erpVoucherNo) {
        OrderRequestInfo postInfo = new OrderRequestInfo();
        postInfo.setErpvoucherno(erpVoucherNo);
        mModel.requestOrderDetail(postInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
//                    LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_ORDER_DETAIL_INFO_LIST_SUB, result);
                try {

                    BaseResultInfo<OrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OrderHeaderInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        OrderHeaderInfo orderHeaderInfo = returnMsgModel.getData();
                        if (orderHeaderInfo != null) {
                            mModel.setOrderDetailsList(orderHeaderInfo.getDetail());
                            mModel.setOrderHeaderInfo(orderHeaderInfo);
                            if (mModel.getOrderDetailsList().size() > 0) {
                                mView.bindListView(mModel.getOrderDetailsList());
                                mView.onBarcodeFocus();
                            } else {
                                MessageBox.Show(mContext, "获取单据失败:获取表体信息为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onErpVoucherNo();
                                    }
                                });

                            }
                        } else {
                            MessageBox.Show(mContext, "获取单据失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onErpVoucherNo();
                                }
                            });

                        }
                    } else {
                        MessageBox.Show(mContext, "获取单据失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onErpVoucherNo();
                            }
                        });

                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取单据失败:出现预期之外的异常-" + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onErpVoucherNo();
                        }
                    });

                }


            }
        });
//
    }


    /**
     * @desc: 扫描物料编号获取物料信息  打印外箱方式
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 15:08
     */
    public void onScan(String materialNo) {
        try {

            OutBarcodeInfo scanQRCode = null;
            if (materialNo.equals("")) return;
            BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(materialNo);
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
                onMaterialInfoQuery(scanQRCode);
            } else {
                MessageBox.Show(mContext, "解析物料失败，条码格式不正确" + materialNo, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onBarcodeFocus();
                    }
                });
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "解析物料失败,出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onBarcodeFocus();
                }
            });

            return;
        }
    }

    /**
     * @desc: 根据物料编码查询物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 15:40
     */
    public void onMaterialInfoQuery(final OutBarcodeInfo scanQRCode) {
        mModel.requestMaterialInfoQuery(scanQRCode.getMaterialno(), new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, mModel.TAG_SELECT_MATERIAL, result);
                    BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutBarcodeInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        OutBarcodeInfo materialInfo = returnMsgModel.getData();
                        if (materialInfo != null) {
                            scanQRCode.setMaterialdesc(materialInfo.getMaterialdesc());
                            scanQRCode.setPackqty(materialInfo.getPackqty());
                            scanQRCode.setSpec(materialInfo.getSpec());
                            scanQRCode.setUnit(materialInfo.getUnit());
                            scanQRCode.setUnitname(materialInfo.getUnitname());
                        }
                        onScanByOuterBoxPrintType(scanQRCode);

                    } else {
                        MessageBox.Show(mContext, "查询物料失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onBarcodeFocus();
                            }
                        });
                        return;
                    }
                } catch (Exception e) {
                    MessageBox.Show(mContext, "查询物料失败:出现预期之外的异常-" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onBarcodeFocus();
                        }
                    });

                }
            }


        });
    }

    /**
     * @desc: 打印外箱类型方式时 的扫描方式
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 15:29
     */
    public void onScanByOuterBoxPrintType(OutBarcodeInfo scanQRCode) {
        OrderDetailInfo materialInfo = new OrderDetailInfo();
        materialInfo.setMaterialno(scanQRCode.getMaterialno());
        materialInfo.setMaterialdesc(scanQRCode.getMaterialdesc());
        materialInfo.setBatchno(scanQRCode.getBatchno());
        materialInfo.setPackQty(scanQRCode.getPackqty());
        materialInfo.setSpec(scanQRCode.getSpec());
        materialInfo.setUnit(scanQRCode.getUnit());
        materialInfo.setUnitname(scanQRCode.getUnitname());
        mView.createDialog(materialInfo);

    }

    public String getTitle() {
       return mContext.getResources().getString(R.string.appbar_title_production_returns_print) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename();
    }

    public void onReset(){
        mModel.onReset();
        mView.onReset();
    }
}
