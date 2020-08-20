package com.liansu.boduowms.modules.instock.batchPrint.print;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.modules.instock.productStorage.printPalletScan.PrintPalletScan;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.modules.print.linkos.PrintType;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/14.
 */
public class BaseOrderLabelPrintModel extends BaseModel {
    private       int             mPrintType;
    private       OrderDetailInfo mCurrentPrintInfo                         = null;
    public final  String          TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC        = "BaseOrderLabelPrintModel_CreateT_OutBarcodeADFAsync";
    private final int             RESULT_TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC = 10004;

    public BaseOrderLabelPrintModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC:
                listener = mNetMap.get("TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC");
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


    //物料编号（ERP获取）
//物料名称（ERP获取）
//物料规格（ERP获取）
//采购批次或供应商批次（手工录入）
//物料数量（手工录入）
    protected PrintInfo getPrintModel(PrintInfo printInfo) {
        if (printInfo != null) {
            String materialNo = printInfo.getMaterialNo() != null ? printInfo.getMaterialNo() : "";
            String materialDesc = printInfo.getMaterialDesc() != null ? printInfo.getMaterialDesc() : "";
            String batchNo = printInfo.getBatchNo() != null ? printInfo.getBatchNo() : "";
            String spec = printInfo.getSpec() != null ? printInfo.getSpec() : "";
            int barcodeQty = (int) printInfo.getQty();
            String QRBarcode = materialNo + "%" + batchNo + "%" + printInfo.getPackQty() + "%" + PrintType.PRINT_LABEL_TYPE_RAW_MATERIAL;
            printInfo.setMaterialNo(materialNo);
            printInfo.setMaterialDesc(materialDesc);
            printInfo.setBatchNo(batchNo);
            printInfo.setQty(barcodeQty);
            printInfo.setQRCode(QRBarcode);
            printInfo.setSpec(spec);
            printInfo.setPrintType(PrintType.PRINT_TYPE_RAW_MATERIAL_STYLE);
        }
        return printInfo;


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
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC, mContext.getString(R.string.product_storage_print_request_barcode_info), mContext, mHandler, RESULT_TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC, null, UrlInfo.getUrl().CreateT_OutBarcodeADFAsync, modelJson, null);
    }
}
