package com.liansu.boduowms.modules.inHouseStock.print;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.modules.instock.batchPrint.order.BaseOrderLabelPrintSelect;
import com.liansu.boduowms.modules.instock.productStorage.printPalletScan.PrintPalletScan;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.modules.print.linkos.PrintType;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/14.
 */
public class BaseInStockHouseLabelPrintModel extends BaseModel {
    private       int             mPrintType;
    private       OrderDetailInfo mCurrentPrintInfo                         = null;
    public final  String          TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC        = "BaseOrderLabelPrintModel_CreateT_OutBarcodeADFAsync";
    private final int             RESULT_TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC = 10004;
    String TAG_SELECT_MATERIAL = "BaseInStockHouseLabelPrintModel_SelectMaterial";  //获取物料信息
    private final int            RESULT_TAG_SELECT_MATERIAL = 10001;
    private       OutBarcodeInfo mMaterialInfo              = null;

    public BaseInStockHouseLabelPrintModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC:
                listener = mNetMap.get("TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC");
                break;
            case RESULT_TAG_SELECT_MATERIAL:
                listener = mNetMap.get("TAG_SELECT_MATERIAL");
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                MessageBox.Show(mContext, "获取请求失败_____" + msg.obj);
                break;

        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    public int getPrintType() {
        return mPrintType;
    }

    public void setPrintType(int mPrintType) {
        this.mPrintType = mPrintType;
    }

    public OrderDetailInfo getCurrentPrintInfo() {
        return mCurrentPrintInfo;
    }

    public void setCurrentPrintInfo(OrderDetailInfo mCurrentPrintInfo) {
        this.mCurrentPrintInfo = mCurrentPrintInfo;
    }

    public void setMaterialNo(OutBarcodeInfo info) {
        mMaterialInfo = info;
    }

    public OutBarcodeInfo getMaterialInfo() {
        return mMaterialInfo;
    }


    /**
     * @desc: 实时外箱提交组托
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestCreateBatchPalletInfo(OrderDetailInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(PrintPalletScan.class, TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC, parseModelToJson(info));
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC, mContext.getString(R.string.product_storage_print_request_barcode_info), mContext, mHandler, RESULT_TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC, null, UrlInfo.getUrl().CreateT_OutBarcodeBlueADFAsync, modelJson, null);
    }

    /**
     * @desc: 查询物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestMaterialInfoQuery(String material, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_SELECT_MATERIAL", callBackListener);
        String modelJson = parseModelToJson(material);
        LogUtil.WriteLog(BaseOrderLabelPrintSelect.class, TAG_SELECT_MATERIAL, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SELECT_MATERIAL, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_TAG_SELECT_MATERIAL, null, UrlInfo.getUrl().SelectMaterial, modelJson, null);

    }

    /**
     * @desc: 获取托盘标签数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/10/9 15:50
     */
    protected PrintInfo getPrintModel(OutBarcodeInfo outBarcodeInfo) {
        PrintInfo printInfo = null;
        if (outBarcodeInfo != null) {
            printInfo = new PrintInfo();
            String materialNo = outBarcodeInfo.getMaterialno() != null ? outBarcodeInfo.getMaterialno() : "";
            String batchNo = outBarcodeInfo.getBatchno() != null ? outBarcodeInfo.getBatchno() : "";
            int barcodeQty = (int) outBarcodeInfo.getQty();
            String QRBarcode = outBarcodeInfo.getBarcode();
            printInfo.setMaterialNo(materialNo);
            printInfo.setMaterialDesc(outBarcodeInfo.getMaterialdesc());
            printInfo.setBatchNo(batchNo);
            printInfo.setQty(barcodeQty);
            printInfo.setQRCode(QRBarcode);
            printInfo.setSignatory(BaseApplication.mCurrentUserInfo.getUsername());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            //获取当前时间
            Date date = new Date(System.currentTimeMillis());
            String arrivalTime = simpleDateFormat.format(date);
            printInfo.setArrivalTime(arrivalTime);
            printInfo.setPrintType(PrintType.PRINT_TYPE_PALLET_STYLE);
        }
        return printInfo;


    }

    /**
     * @desc: 重置物料
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/10/9 16:14
     */
    public void onReset() {
        mMaterialInfo = null;
    }
}
