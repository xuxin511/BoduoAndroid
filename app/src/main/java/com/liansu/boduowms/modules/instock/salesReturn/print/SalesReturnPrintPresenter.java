package com.liansu.boduowms.modules.instock.salesReturn.print;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/17.
 */
public class SalesReturnPrintPresenter {
    protected Context               mContext;
    protected SaleReturnPrintModel  mModel;
    protected ISalesReturnPrintView mView;
    protected PrintBusinessModel    mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public SalesReturnPrintPresenter(Context context, ISalesReturnPrintView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new SaleReturnPrintModel(context, handler);
        this.mPrintModel = new PrintBusinessModel(context, handler);
    }

    public SaleReturnPrintModel getModel() {
        return mModel;
    }


    /**
     * @desc: 获取物料打印信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/2 10:10
     */
    public void getMaterialNoBatchList(final String materialNo, String startTime, String endTime, final String customerNo) {
        if (materialNo.equals("")) {
            MessageBox.Show(mContext, "物料信息不能为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onMaterialNoFocus();
                }
            });
            return;
        }
        OrderRequestInfo orderRequestInfo = new OrderRequestInfo();
        orderRequestInfo.setMaterialno(materialNo);
        orderRequestInfo.setDateFrom(startTime);
        orderRequestInfo.setDateTo(endTime);
        orderRequestInfo.setCustomerno(customerNo);
        orderRequestInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        mModel.requestMaterialBatchNoListInfo(orderRequestInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_SALE_RETURN_DETAIL_LIST_ADF_ASYNC, result);
                try {
                    BaseResultInfo<OrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OrderHeaderInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        OrderHeaderInfo orderHeaderInfo = returnMsgModel.getData();
                        if (orderHeaderInfo != null) {
                            mModel.setCustomerCode(customerNo);
                            mModel.setMaterialDetailsList(orderHeaderInfo.getDetail());
                            BaseMultiResultInfo<Boolean, List<String>> checkResult = mModel.getMaterialBatchNoList(mModel.getMaterialDetailsList(), materialNo);
                            if (checkResult.getHeaderStatus()) {
                                mView.setMaterialInfo(mModel.getMaterialDetailsList().get(0));
                                mView.setSpinnerData(checkResult.getInfo());
                            } else {
                                MessageBox.Show(mContext, checkResult.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mView.onMaterialNoFocus();
                                    }
                                });
                            }

                        }
//                        MessageBox.Show(mContext, returnMsgModel.getResultValue(), 1, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });

                    } else {
                        MessageBox.Show(mContext, "获取物料批次信息失败:"+returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onMaterialNoFocus();
                            }
                        });
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取物料批次信息失败,出现预期之外的异常:"+ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onMaterialNoFocus();
                        }
                    });
                }


            }
        });


    }

    /**
     * @desc: 打印
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/17 7:30
     */
    public void onPrint(final String materialNo, final String materialName, final String batchNo, final float packCount, float sumPalletQty) {
        OrderDetailInfo orderDetailInfo = null;
        if (!mPrintModel.checkBluetoothSetting()) {
            return;
        }
        if (mModel.getMaterialDetailsList() != null && mModel.getMaterialDetailsList().size() > 0) {
            orderDetailInfo = mModel.getMaterialDetailsList().get(0);
        }

        if (orderDetailInfo == null) {
            MessageBox.Show(mContext, "物料数据类不能为空,请扫描物料编码");
            return;
        }
        if (materialNo.equals("")) {
            MessageBox.Show(mContext, "物料编号不能为空");
            return;
        }
        if (batchNo.equals("")) {
            MessageBox.Show(mContext, "批次不能为空");
            return;
        }

        if (packCount <= 0) {
            MessageBox.Show(mContext, "包装数量必须大于0");
            return;
        }
        if (sumPalletQty <= packCount) {
            MessageBox.Show(mContext, "总数量必须大于等于包装量");
            return;
        }

        if (mModel.getCustomerCode() == null) {
            MessageBox.Show(mContext, "客户编码不存在或为校验:请扫描物料编号");
            return;
        }

        OutBarcodeInfo postBarcodeInfo = new OutBarcodeInfo();
        postBarcodeInfo.setMaterialno(materialNo);
        postBarcodeInfo.setMaterialdesc(materialName);
        postBarcodeInfo.setBatchno(batchNo);
        postBarcodeInfo.setSpec(mModel.getSpec(materialNo));
        postBarcodeInfo.setPackQty((int) packCount);
        postBarcodeInfo.setQty(sumPalletQty);
        postBarcodeInfo.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE_VALUE);
        postBarcodeInfo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
        postBarcodeInfo.setStrongholdcode(orderDetailInfo.getStrongholdcode());
        postBarcodeInfo.setStrongholdname(orderDetailInfo.getStrongholdname());
        postBarcodeInfo.setCompanycode(orderDetailInfo.getCompanycode());
        postBarcodeInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
        postBarcodeInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        postBarcodeInfo.setPrintername(UrlInfo.mInStockPrintName);
        postBarcodeInfo.setPrintertype(UrlInfo.mInStockPrintType);
        postBarcodeInfo.setUsername(BaseApplication.mCurrentUserInfo.getUsername());
        postBarcodeInfo.setCuscode(mModel.getCustomerCode());

        float divValue = ArithUtil.div(sumPalletQty, packCount);
        final double printCount = Math.ceil(divValue);   //向上取整
        final float lastPackQty = sumPalletQty % packCount;  //最后一箱数量 模
        mModel.requestPrint(postBarcodeInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GET_T_SALE_RETURN_DETAIL_LIST_ADF_ASYNC, result);
                try {
                    BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {  //托盘码打印成功后，打印外箱码
                        List<PrintInfo> printInfoList = new ArrayList<>();
                        for (int i = 0; i < printCount; i++) {
                            OutBarcodeInfo info = new OutBarcodeInfo();
                            info.setMaterialno(materialNo);
                            info.setMaterialdesc(materialName);
                            info.setBatchno(batchNo);
                            info.setSpec(mModel.getSpec(materialNo));
                            if (i == printCount - 1) {  //最后一箱数量 ，余数等于0 就是包装量 ,模大于零 就去余数
                                if (lastPackQty == 0) {
                                    info.setQty(packCount);
                                } else {
                                    info.setQty(lastPackQty);
                                }
                            } else {
                                info.setQty(packCount);
                            }

                            printInfoList.add(mModel.getPrintModel(info));

                        }

                        if (printInfoList.size() > 0) {
                            mPrintModel.onPrint(printInfoList);
                        }

                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue());
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage());
                }
            }
        });


    }

}
