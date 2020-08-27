package com.liansu.boduowms.modules.instock.batchPrint.order;

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
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.bean.order.VoucherTypeInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan.PurchaseInspectionProcessingScan;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/14.
 */
public class BaseOrderLabelPrintSelectPresenter {
    protected Context                        mContext;
    protected BaseOrderLabelPrintSelectModel mModel;
    protected IBaseOrderLabelPrintSelectView mView;
    protected PrintBusinessModel             mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);

    }

    public BaseOrderLabelPrintSelectPresenter(Context context, IBaseOrderLabelPrintSelectView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new BaseOrderLabelPrintSelectModel(mContext, handler);
        this.mPrintModel = new PrintBusinessModel(context, handler);
    }

    public BaseOrderLabelPrintSelectModel getModel() {
        return mModel;
    }

    public String getTitle() {
        return mContext.getResources().getString(R.string.main_menu_item_batch_printing) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename();
    }

    /**
     * @desc: 获取订单类型数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 14:05
     */
    public void getVoucherTypeList() {
        mModel.requestVoucherInfoQuery(new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderLabelPrintSelect.class, mModel.TAG_GET_T_PARAMETER_LIST, result);
                try {
                    BaseResultInfo<List<VoucherTypeInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<VoucherTypeInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        List<VoucherTypeInfo> list = returnMsgModel.getData();
                        if (list != null && list.size() > 0) {
                            mModel.setVoucherTypeList(list);
                            List<String> voucherTypeNameList = mModel.getVoucherTypeNameList();
                            if (voucherTypeNameList != null && voucherTypeNameList.size() > 0) {
                                mView.setSpinnerData(voucherTypeNameList);
                            }
                        } else {
                            MessageBox.Show(mContext, "获取单据类型失败:获取的单据类型集合为空" + returnMsgModel.getResultValue());

                        }
                    } else {
                        MessageBox.Show(mContext, "获取单据类型失败:获取的单据类型集合为空" + returnMsgModel.getResultValue());

                    }


                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取单据类型失败:出现预期之外的异常-" + ex.getMessage());

                }

            }
        });

    }

    /**
     * @desc: 获取订单信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/16 16:16
     */
    public void getOrderDetailInfoList(String voucherTypeName, String erpVoucherNo) {
        if (voucherTypeName.contains("请选择")) {
            MessageBox.Show(mContext, "请选择单据类型", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onErpVoucherNoFocus();
                }
            });
            return;
        }
        int voucherType = mModel.getVoucherType(voucherTypeName);
        OrderRequestInfo postInfo = new OrderRequestInfo();
        postInfo.setErpvoucherno(erpVoucherNo);
        if (voucherType == OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_VALUE) {
            getProductionOrderDetailInfoList(voucherTypeName, erpVoucherNo);
        } else {
            mModel.getOrderDetailInfoList(voucherType, postInfo, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_ORDER_DETAIL_INFO_LIST_SUB, result);
                    try {

                        BaseResultInfo<OrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OrderHeaderInfo>>() {
                        }.getType());
                        if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                            OrderHeaderInfo orderHeaderInfo = returnMsgModel.getData();
                            if (orderHeaderInfo != null) {
                                mModel.setOrderDetailList(orderHeaderInfo.getDetail());
                                mModel.setOrderHeaderInfo(orderHeaderInfo);
                                if (mModel.getOrderDetailList().size() > 0) {
                                    mView.bindListView(mModel.getOrderDetailList());
                                    mView.onMaterialFocus();
                                } else {
                                    mView.onErpVoucherNoFocus();
                                    MessageBox.Show(mContext, "获取单据失败:获取表体信息为空");

                                }
                            } else {
                                mView.onErpVoucherNoFocus();
                                MessageBox.Show(mContext, "获取单据失败:" + returnMsgModel.getResultValue());

                            }
                        } else {
                            mView.onErpVoucherNoFocus();
                            MessageBox.Show(mContext, "获取单据失败:" + returnMsgModel.getResultValue());

                        }

                    } catch (Exception ex) {
                        mView.onErpVoucherNoFocus();
                        MessageBox.Show(mContext, "获取单据失败:出现预期之外的异常-" + ex.getMessage());

                    }


                }
            });
        }


    }

    /**
     * @desc: 获取报工单明细接口
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/23 16:24
     */
    protected void getProductionOrderDetailInfoList(String voucherTypeName, String erpVoucherNo) {
        int voucherType = mModel.getVoucherType(voucherTypeName);
        OrderRequestInfo postInfo = new OrderRequestInfo();
        postInfo.setErpvoucherno(erpVoucherNo);
        mModel.setOrderHeaderInfo(null);
        mModel.setOrderDetailList(null);
        mModel.getOrderDetailInfoList(voucherType, postInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_ORDER_DETAIL_INFO_LIST_SUB, result);
                try {
                    BaseResultInfo<List<OrderHeaderInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OrderHeaderInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        List<OrderHeaderInfo> list = returnMsgModel.getData();
                        OrderHeaderInfo orderHeaderInfo = list.get(0);
                        if (list != null && list.size() > 0) {
                            mModel.setOrderHeaderInfo(orderHeaderInfo);
                            mModel.setOrderDetailList(orderHeaderInfo.getDetail());
                            if (mModel.getOrderDetailList().size() > 0) {
                                mModel.sortDetailList(null);
                                mView.bindListView(mModel.getOrderDetailList());
                                mView.onMaterialFocus();
                            } else {
                                MessageBox.Show(mContext, "获取单据列表失败：" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onErpVoucherNoFocus();
                                    }
                                });

                            }
                        } else {
                            MessageBox.Show(mContext, "获取单据列表失败：" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onErpVoucherNoFocus();
                                }
                            });
                        }
                    } else {
                        mView.onErpVoucherNoFocus();
                        MessageBox.Show(mContext, "获取单据失败:" + returnMsgModel.getResultValue());

                    }


                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取单据列表失败：" + ex.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onErpVoucherNoFocus();
                        }
                    });
                }


            }
        });
    }


    /**
     * @desc: 扫描物料编号获取物料信息   如果是打印托盘方式  根据单据明细取,如果是打印外箱方式
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 15:08
     */
    public void onScan(String materialNo, int printType) {
        try {
            if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO) {
                if (mModel.getOrderDetailList().size() == 0) {
                    MessageBox.Show(mContext, "校验单据信息失败：获取的单据信息为空，请先扫描单据号", MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onErpVoucherNoFocus();
                        }
                    });
                    return;
                }
            }
            OutBarcodeInfo scanQRCode = null;
            if (materialNo.equals("")) return;
            BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(materialNo);
            if (resultInfo.getHeaderStatus()) {
                scanQRCode = resultInfo.getInfo();
            } else {
                MessageBox.Show(mContext, resultInfo.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onMaterialFocus();
                    }
                });
                return;
            }


            if (scanQRCode != null) {
                onMaterialInfoQuery(scanQRCode, printType);

            } else {
                MessageBox.Show(mContext, "解析物料失败，条码格式不正确" + materialNo, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.onMaterialFocus();
                    }
                });
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "解析物料失败,出现预期之外的异常:" + e.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onMaterialFocus();
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
    public void onMaterialInfoQuery(final OutBarcodeInfo scanQRCode, final int printType) {
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
                        if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_OUTER_BOX) {
                            onScanByOuterBoxPrintType(scanQRCode);
                        } else if (printType == PrintBusinessModel.PRINTER_LABEL_TYPE_PALLET_NO) {
                            onScanByPalletNoPrintType(scanQRCode);
                        }

                    } else {
                        MessageBox.Show(mContext, "查询物料失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onMaterialFocus();
                            }
                        });
                        return;
                    }
                } catch (Exception e) {
                    MessageBox.Show(mContext, "查询物料失败:出现预期之外的异常-" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onMaterialFocus();
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
        mView.StartScanIntent(materialInfo);

    }

    /**
     * @desc: 打印托盘方式的扫描
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 15:36
     */
    public void onScanByPalletNoPrintType(OutBarcodeInfo scanQRCode) {
        BaseMultiResultInfo<Boolean, List<OrderDetailInfo>> resultInfo = mModel.findOutBarcodeInfoFromMaterial(scanQRCode); //找到物料行,如果找到给外箱码赋值
        if (resultInfo.getHeaderStatus()) {
            mView.onMaterialFocus();
            List<OrderDetailInfo> list = resultInfo.getInfo();
            if (list != null && list.size() == 1) {
                list.get(0).setBatchno(scanQRCode.getBatchno());
                mView.StartScanIntent(list.get(0));
            } else if (list != null && list.size() > 1) {
                if (mModel.getOrderDetailList().size() > 0) {
                    //订单无批次,用外箱的
                    for (OrderDetailInfo info:list){
                        info.setBatchno(scanQRCode.getBatchno());
                    }

                    mModel.sortDetailList(list.get(0).getMaterialno());
                    mView.bindListView(mModel.getOrderDetailList());
                }


            }

        } else {
            MessageBox.Show(mContext, "获取订单的物料信息失败:" + resultInfo.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onMaterialFocus();
                }
            });
        }
    }


    /**
     * @desc: 重置数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/16 15:19
     */
    public void onReset(boolean isReset) {
        mModel.onReset(isReset);
        mView.onReset(isReset);

    }
}
