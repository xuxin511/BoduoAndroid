package com.liansu.boduowms.modules.instock.batchPrint.print;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/14.
 */
public class BaseOrderLabelPrintModel extends BaseModel {
    private       int             mPrintType;
    private       OrderDetailInfo mCurrentPrintInfo                         = null;
    public final  String          TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC        = "BaseOrderLabelPrintModel_CreateT_OutBarcodeADFAsync";
    public final  String          TAG_CHECK_BATCH_DATE                      = "BaseOrderLabelPrintModel_TAG_CHECK_BATCH_DATE";
    private final int             RESULT_TAG_CREATE_T_OUT_BARCODE_ADF_ASYNC = 10004;
    private final int             RESULT_TAG_CHECK_BATCH_DATE               = 10005;  //校验批次接口


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
            case RESULT_TAG_CHECK_BATCH_DATE:
                listener = mNetMap.get("TAG_CHECK_BATCH_DATE");
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
            printInfo.setErpVoucherNo(printInfo.getErpVoucherNo());
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


    /**
     * @desc: 校验托盘的批次日期
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestCheckBatchNoOfPallet(final OrderDetailInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_CHECK_BATCH_DATE", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(PrintPalletScan.class, TAG_CHECK_BATCH_DATE, parseModelToJson(info));
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_CHECK_BATCH_DATE, mContext.getString(R.string.request_label_print_check_batch_no), mContext, mHandler, RESULT_TAG_CHECK_BATCH_DATE, null, UrlInfo.getUrl().Get_MaxStockBatchADFAsync, modelJson, null);
    }


    /**
     * @desc: 比较日期大小  是否第一个日期大于第二个日期   是 返回true  否返回 false
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2021/1/22 16:21
     */
    public static BaseMultiResultInfo<Boolean, Void> compareDate(String printBatchNo, String maxStockBatchNo, String pattern, String materialNo) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        resultInfo.setHeaderStatus(false);
        try {
            // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            format.setLenient(false);
            if (format.parse(printBatchNo).getTime() >= format.parse(maxStockBatchNo).getTime()) {//转成long类型比较
                resultInfo.setHeaderStatus(true);
                resultInfo.setMessage("物料[" + materialNo + "]的当前打印批次[" + printBatchNo + "]大于等于该物料的库存批次[" + maxStockBatchNo + "]");
            } else if (format.parse(printBatchNo).getTime() < format.parse(maxStockBatchNo).getTime()) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("物料[" + materialNo + "]的当前打印批次[" + printBatchNo + "]小于该物料的库存批次[" + maxStockBatchNo + "]");
            }
        } catch (ParseException e) {
            resultInfo.setHeaderStatus(false);
            StringBuilder builder = new StringBuilder();
            builder.append("校验物料[" + materialNo + "]的批次出现预期之外的异常:批次格式不正确@" + e.getMessage() + "\n");
            builder.append("物料[" + materialNo + "]的当前打印批次[" + printBatchNo + "],该物料库存批次[" + maxStockBatchNo + "]");
            resultInfo.setMessage(builder.toString());

        } catch (Exception e) {
            resultInfo.setHeaderStatus(false);
            StringBuilder builder = new StringBuilder();
            builder.append("校验物料[" + materialNo + "]的批次出现预期之外的异常:" + e.getMessage() + "\n");
            builder.append("物料[" + materialNo + "]的当前打印批次[" + printBatchNo + "],该物料库存批次[" + maxStockBatchNo + "]");
            resultInfo.setMessage(builder.toString());
        }

        return resultInfo;
    }
}
