package com.liansu.boduowms.modules.print.LabelReprint;

import android.content.Context;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.modules.print.linkos.PrintType;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

/**
 * @desc: 无源杂入
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/7/18 19:24
 */
public class LabelReprintPresenter {

    protected Context            mContext;
    protected LabelReprintModel  mModel;
    protected ILabelReprintView  mView;
    protected PrintBusinessModel mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public LabelReprintPresenter(Context context, ILabelReprintView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new LabelReprintModel(context, handler);
        this.mPrintModel = new PrintBusinessModel(context, handler);
    }

    public LabelReprintModel getModel() {
        return mModel;
    }


    protected String getTitle() {
        return mContext.getResources().getString(R.string.appbar_title_no_source_scan);
    }


    /**
     * @desc: 获取订单明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 22:33
     */

    protected void getOrderDetailInfoList(String erpVoucherNo) {
        mModel.requestOrderDetail(erpVoucherNo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
//                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_GetT_PurchaseOrderListADFAsync, result);
                try {
                    BaseResultInfo<OrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OrderHeaderInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == 1) {
                        OrderHeaderInfo orderHeaderInfo = returnMsgModel.getData();
                        if (orderHeaderInfo != null) {
                            mModel.setOrderDetailList(orderHeaderInfo.getDetail());
                            if (mModel.getOrderDetailList().size() > 0) {
//                                mView.bindListView(mModel.getOrderDetailList());
                            } else {
                                MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                            }
                        } else {
                            MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                        }
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );
                }


            }
        });
    }

    /**
     * @desc: 扫描条码
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
                } else {
                    MessageBox.Show(mContext, resultInfo.getMessage() );
                    return;
                }

            }
            if (scanQRCode != null) {
                getMaterialInfo(scanQRCode);

            } else {
                MessageBox.Show(mContext, "解析条码失败，条码格式不正确" + scanBarcode );
                return;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, e.getMessage() );
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
        if (mModel.getMaterialList().size() == 0) {
            MessageBox.Show(mContext, "扫描的物料行为空" );
            return;

        }
        mModel.requestOrderRefer(mModel.getMaterialList(), new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_COMBINE_AND_REFER_PALLET_SUB, result);
                try {
                    BaseResultInfo<AreaInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<AreaInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == 1) {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                        onReset();
                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue() );

                    }
                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );

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


    public void getMaterialInfo(final OutBarcodeInfo barcode) {
        mModel.requestMaterialInfo(barcode, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_COMBINE_AND_REFER_PALLET_SUB, result);
                try {
                    BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutBarcodeInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == 1) {
                        mModel.setMaterialInfo(returnMsgModel.getData());
                        mView.setMaterialInfo(mModel.getMaterialInfo());
                        mModel.setBatchNoList(null);
                        mView.setBatchNoList(mModel.getBatchNoList());
                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue() );

                    }
                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );

                }
            }
        });
    }

    /**
     * @desc: 扫描当前托盘标签或外箱标签
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/18 20:18
     */
    public void onScan(OutBarcodeInfo outBarcodeInfo) {

        if (outBarcodeInfo != null) {
            outBarcodeInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
            outBarcodeInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());

        } else {
            MessageBox.Show(mContext, "外箱信息不能为空" );
        }
    }

    /**
     * @desc: 如果是成品要单独到物料界面去输入数量
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/19 0:25
     */
    public void getQty() {
        OutBarcodeInfo info = mModel.getMaterialInfo();
        if (info == null) {
            MessageBox.Show(mContext, "物料信息不能为空" );
            return;
        }
        if (mView.getPrintLabelType().equals(PrintType.PRINT_TYPE_FINISHED_PRODUCT_STYLE)) {
            mView.createDialog(info, PrintType.PRINT_LABEL_TYPE_FINISHED_PRODUCT);
        }

    }

    /**
     * @desc: 打印
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/19 0:29
     */
    public void onPrint(OutBarcodeInfo outBarcodeInfo, int printCount) {
        if (outBarcodeInfo == null) {
            MessageBox.Show(mContext, "打印信息不能为空" );
            return;
        }
        String printTypeSting = mView.getPrintLabelType();
        PrintInfo printInfo = mModel.getPrintModel(mModel.getMaterialInfo(), printTypeSting);
        if (printInfo != null) {
            for (int i = 0; i < printCount; i++)
                mPrintModel.onPrint(printInfo);
        }
    }

}
