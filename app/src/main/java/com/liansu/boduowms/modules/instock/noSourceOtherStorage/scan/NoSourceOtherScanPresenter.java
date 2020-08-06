package com.liansu.boduowms.modules.instock.noSourceOtherStorage.scan;

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
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
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
public class NoSourceOtherScanPresenter   {

    protected Context            mContext;
    protected NoSourceOtherStorageScanModel                  mModel;
    protected INoSourceOtherScanView                  mView;
    protected PrintBusinessModel mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public NoSourceOtherScanPresenter(Context context, INoSourceOtherScanView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new NoSourceOtherStorageScanModel(context,handler);
        this.mPrintModel = new PrintBusinessModel(context, handler);
    }

    public NoSourceOtherStorageScanModel getModel() {
        return mModel;
    }


    protected String getTitle() {
        return mContext.getResources().getString(R.string.appbar_title_no_source_scan);
    }

    /**
     * @desc: 获取库位
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/18 20:20
     */
    public void getAreaInfo(String areaNo) {
        if (areaNo.equals("")) {
            MessageBox.Show(mContext, "库位信息不能为空" );
            return;
        }
        mModel.requestAreaInfo(areaNo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(BaseOrderScan.class, mModel.TAG_COMBINE_AND_REFER_PALLET_SUB, result);
                try {
                    BaseResultInfo<AreaInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<AreaInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == 1) {
                        mModel.setAreaInfo(returnMsgModel.getData());
                        mView.onBarcodeFocus();
                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                        mView.onAreaNoFocus();
                    }
                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );
                    mView.onAreaNoFocus();
                }


            }
        });
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

    /**
     * @desc: 扫描当前托盘标签或外箱标签
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/18 20:18
     */
    public void onScan(OutBarcodeInfo outBarcodeInfo) {
        AreaInfo areaInfo = mModel.getAreaInfo();
        if (areaInfo == null || areaInfo.getAreano() == null || areaInfo.getAreano().equals("")) {
            MessageBox.Show(mContext, "库位不能为空!" );
            mView.onAreaNoFocus();
        }
        if (outBarcodeInfo != null) {
            outBarcodeInfo.setAreano(areaInfo.getAreano());
            outBarcodeInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
            outBarcodeInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
            BaseMultiResultInfo<Boolean, OutBarcodeInfo> detailResult = mModel.findScannedMaterialInfo(outBarcodeInfo); //找到物料行
            if (detailResult.getHeaderStatus()) {
                final OutBarcodeInfo materialNo = detailResult.getInfo();
                BaseMultiResultInfo<Boolean, Void> checkMaterialResult = mModel.checkMaterialInfo(materialNo, outBarcodeInfo, true); //校验条码是否匹配物料
                if (checkMaterialResult.getHeaderStatus()) {
                    materialNo.setSupplierno(mModel.getOrderHeaderInfo().getSupplierno());
                    materialNo.setSuppliername(mModel.getOrderHeaderInfo().getSuppliername());
                    materialNo.setScanuserno(BaseApplication.mCurrentUserInfo.getUserno());
                    mView.bindListView(mModel.getMaterialList());
                } else {
                    MessageBox.Show(mContext, checkMaterialResult.getMessage() );
                }
            } else {
                MessageBox.Show(mContext, detailResult.getMessage() );
            }
        } else {
            MessageBox.Show(mContext, "外箱信息不能为空" );
        }
    }

}
