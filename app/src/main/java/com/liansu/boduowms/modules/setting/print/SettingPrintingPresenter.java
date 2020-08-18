package com.liansu.boduowms.modules.setting.print;

import android.content.Context;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseFragment;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.debug.DebugModuleData;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.modules.qualityInspection.randomInspection.bill.RandomInspectionBill;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_NONE;
import static com.liansu.boduowms.utils.SharePreferUtil.ReadPrintSettingShare;
import static com.liansu.boduowms.utils.SharePreferUtil.setBluetoothPrinterMacAddressShare;
import static com.liansu.boduowms.utils.SharePreferUtil.setBusinessPrinterType;
import static com.liansu.boduowms.utils.SharePreferUtil.setPrinterAddressShare;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class SettingPrintingPresenter {
    private Context            mContext;
    private SettingModel       mModel;
    private ISettingPrintView  mView;
    private PrintBusinessModel mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public SettingPrintingPresenter(Context context, ISettingPrintView view, MyHandler<BaseFragment> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new SettingModel(context, handler);
        this.mPrintModel = new PrintBusinessModel(context, null);
        onReadData();
    }

    public SettingModel getModel() {
        return mModel;
    }

    /**
     * @desc: 测试蓝牙打印
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/30 13:18
     */
    public void onLabelTest() {
        try {
//            mPrintModel.onPrint(DebugModuleData.getRawMaterialLabel());
//        mPrintModel.onPrint( DebugModuleData.getRawMaterialLabel());
            mPrintModel.onPrint(DebugModuleData.getRawMaterialLabelList());
        } catch (Exception ex) {
            MessageBox.Show(mContext, ex.toString());
        }
    }


    /**
     * @desc: 保存
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/30 13:33
     */
    public void onSave() {
        try {
            String laserPrinterAddress = mView.getLaserPrinterAddress();
            String desktopPrintAddress = mView.getDesktopPrintAddress();
            String inStockPrintAddress = "";
            String outStockPrintAddress = "";
            String bluetoothPrinterMacAddress = mView.getBluetoothPrinterMacAddress();
            int inStockPrintType = mView.getInStockPrintType();
            int outStockPrintType = mView.getOutStockPrintType();
            if (inStockPrintType == PrintBusinessModel.PRINTER_TYPE_LASER) {
                inStockPrintAddress = laserPrinterAddress;
            } else if (inStockPrintType == PrintBusinessModel.PRINTER_TYPE_DESKTOP) {
                inStockPrintAddress = desktopPrintAddress;
            }
            if (outStockPrintType == PrintBusinessModel.PRINTER_TYPE_LASER) {
                outStockPrintAddress = laserPrinterAddress;
            } else if (outStockPrintType == PrintBusinessModel.PRINTER_TYPE_DESKTOP) {
                outStockPrintAddress = desktopPrintAddress;
            }

            setPrinterAddressShare(mContext, laserPrinterAddress, desktopPrintAddress);
            setBluetoothPrinterMacAddressShare(mContext, bluetoothPrinterMacAddress);
            setBusinessPrinterType(mContext, inStockPrintType, inStockPrintAddress, outStockPrintType, outStockPrintAddress);
            MessageBox.Show(mContext, "保存成功", MEDIA_MUSIC_NONE);
        } catch (Exception e) {
            MessageBox.Show(mContext, "保存打印设置出现预期之外的异常:" + e.getMessage());
        }
    }

    /**
     * @desc: 读取数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/30 14:50
     */
    public void onReadData() {
        ReadPrintSettingShare(mContext);

    }


    /**
     * @desc:
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/30 14:44
     */
    public void getPrinterAddressList() {
        mModel.requestPrinterAddressList(new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(RandomInspectionBill.class, mModel.TAG_GET_PRINT_NAME_LIST, result);
                    BaseResultInfo<List<String>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<String>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        List<String> list = returnMsgModel.getData();
                        if (list != null && list.size() > 0) {
                            mModel.setDesktopPrinterAddressList(list);
                            mModel.setLaserPrinterAddressList(list);
                        }

                    }

                } catch (Exception ex) {
                    ToastUtil.show(ex.getMessage());
                }

            }
        });
    }


}
