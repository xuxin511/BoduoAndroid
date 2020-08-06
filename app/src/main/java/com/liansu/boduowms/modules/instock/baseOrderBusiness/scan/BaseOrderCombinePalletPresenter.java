package com.liansu.boduowms.modules.instock.baseOrderBusiness.scan;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.QRCodeFunc;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;


/**
 * @ Des:
 * @ Created by yangyiqing on 2019/11/14.
 */
public class BaseOrderCombinePalletPresenter {
    private Context                     mContext;
    private BaseOrderCombinePalletModel mModel;
    private PrintBusinessModel          mPrintModel;
    private IBaseOrderScanView          mView;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
        switch (msg.what) {

        }
    }

    public BaseOrderCombinePalletPresenter(Context context, IBaseOrderScanView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new BaseOrderCombinePalletModel(context, handler);
        this.mPrintModel = new PrintBusinessModel(context, handler);
    }

    /**
     * @desc: 设置订单数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/6 13:47
     */
    public void setOrderInfo(OrderHeaderInfo headerInfo, List<OrderDetailInfo> detailInfos, int instockType) {
        if (mModel != null) {
            mModel.setOrderInfo(headerInfo, detailInfos, instockType);
        }

    }


    /**
     * @desc: 扫描外箱条码
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
                //如果外箱码不为空，就弹出物料输入框
                mView.createDialog(scanQRCode);
            } else {
                MessageBox.Show(mContext, "解析条码失败，条码格式不正确" + scanBarcode );
                return;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, e.getMessage() );
            return;
        }

    }

    public void onClear() {
//        mView.onClear();
        mModel.onClear();


    }


//    /**
//     * @desc: 切换模式
//     * @param:
//     * @return:
//     * @author: Nietzsche
//     * @time 2020/2/13 15:37
//     */
//    public void changeModuleType(final boolean check) {
//        if (mModel.getList() != null && mModel.getList().size() > 0) {
//            new android.app.AlertDialog.Builder(mContext).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage("存在已扫描的待组托条码，切换组托模式将清空数据，是否继续切换？")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            onClear();
//                            mView.showPalletScan(check);
//                        }
//                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    mView.setSwitchButton(!check);
//                }
//            }).show();
//
//        } else {
//            onClear();
//            mView.showPalletScan(check);
//        }
//    }



    /**
     * @desc: 刷新订单明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/6 14:02
     */
    public void onOrderDetailsRefresh() {
        if (mModel.getOrderDetailList().size() > 0) {
            mView.bindListView(mModel.getOrderDetailList());
        }
    }

    /**
     * @desc: 根据外箱码信息生成新的托盘码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/7 13:09
     */
    public void createPalletNo(OrderDetailInfo orderDetailInfo) {
        mModel.requestNewCreatedPalletNoQuery(orderDetailInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_NewCreatedPalletNoQuery, result);
                try {
//                    ReturnMsgModel<PalletDetailInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<ReceiptDetail_Model>>() {
//                    }.getType());
//                    if (returnMsgModel.getHeaderStatus().equals("S")) {
//                        PalletDetailInfo palletDetailInfo = returnMsgModel.getModelJson();
//                        if (palletDetailInfo != null) {
////                            onPrint(palletDetailInfo);
//                        }
//                    } else {
//                        MessageBox.Show(mContext, returnMsgModel.getMessage());
//                    }
                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );
                }


            }
        });
    }


}
