package com.liansu.boduowms.modules.instock.activeOtherStorage.scan;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScanModel;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.modules.print.linkos.PrintType;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelListToJsonArray;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class ActiveOtherScanModel extends BaseOrderScanModel {
    String TAG_GET_T_OTHER_DETAIL_LIST_ADF_ASYNC = "ActiveOtherScanModel_GetT_OtherDetailListADFAsync";  //有源杂入获取表体明细
    String TAG_SAVE_T_OTHER_DETAIL_ADF_ASYNC     = "ActiveOtherScanModel_SaveT_OtherDetailADFAsync";  //有源杂入实时提交
    String TAG_POST_T_OTHER_DETAIL_ADF_ASYNC     = "ActiveOtherScanModel_PostT_OtherDetailADFAsync";  //有源杂入过账

    private final int RESULT_TAG_GET_T_OTHER_DETAIL_LIST_ADF_ASYNC = 110;
    private final int RESULT_TAG_SAVE_T_OTHER_DETAIL_ADF_ASYNC     = 111;
    private final int RESULT_TAG_POST_T_OTHER_DETAIL_ADF_ASYNC     = 112;

    public ActiveOtherScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler,OrderType.IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE_VALUE);
    }

    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_T_OTHER_DETAIL_LIST_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_OTHER_DETAIL_LIST_ADF_ASYNC");
                break;
            case RESULT_TAG_SAVE_T_OTHER_DETAIL_ADF_ASYNC:
                listener = mNetMap.get("TAG_SAVE_T_OTHER_DETAIL_ADF_ASYNC");
                break;
            case RESULT_TAG_POST_T_OTHER_DETAIL_ADF_ASYNC:
                listener = mNetMap.get("TAG_POST_T_OTHER_DETAIL_ADF_ASYNC");
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
        super.onHandleMessage(msg);
    }

    /**
     * @desc: 获取有源杂入表体明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestActiveOtherDetail(OrderRequestInfo orderRequestInfo, NetCallBackListener<String> callBackListener) {
        orderRequestInfo.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE_VALUE);
        mNetMap.put("TAG_GET_T_OTHER_DETAIL_LIST_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(orderRequestInfo);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_GET_T_OTHER_DETAIL_LIST_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_OTHER_DETAIL_LIST_ADF_ASYNC, mContext.getString(R.string.message_request_get_order_detail), mContext, mHandler, RESULT_TAG_GET_T_OTHER_DETAIL_LIST_ADF_ASYNC, null, UrlInfo.getUrl().GetT_OtherDetailListADFAsync, modelJson, null);
    }


    /**
     * @desc: 组托并提交入库
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    @Override
    public void requestCombineAndReferPallet(OrderDetailInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_SAVE_T_OTHER_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_SAVE_T_OTHER_DETAIL_ADF_ASYNC, parseModelToJson(info));
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SAVE_T_OTHER_DETAIL_ADF_ASYNC, mContext.getString(R.string.message_request_refer_barcode_info), mContext, mHandler, RESULT_TAG_SAVE_T_OTHER_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().SaveT_OtherDetailADFAsync, modelJson, null);

    }


    @Override
    public void requestCombineAndReferPallet(List<OrderDetailInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_SAVE_T_OTHER_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_SAVE_T_OTHER_DETAIL_ADF_ASYNC, parseModelToJson(modelJson));
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SAVE_T_OTHER_DETAIL_ADF_ASYNC, mContext.getString(R.string.message_request_refer_barcode_info), mContext, mHandler, RESULT_TAG_SAVE_T_OTHER_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().SaveT_OtherDetailADFAsync, modelJson, null);
    }

    /**
     * @desc: 过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 18:46
     */
    public void requestOrderRefer(List<OrderDetailInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_POST_T_OTHER_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_POST_T_OTHER_DETAIL_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_POST_T_OTHER_DETAIL_ADF_ASYNC, mContext.getString(R.string.message_request_system_refer), mContext, mHandler, RESULT_TAG_POST_T_OTHER_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().PostT_OtherDetailADFAsync, modelJson, null);

    }



    @Override
    protected PrintInfo getPrintModel(OutBarcodeInfo outBarcodeInfo) {
        PrintInfo printInfo = null;
        if (outBarcodeInfo != null) {
            printInfo = new PrintInfo();
            String barcode = outBarcodeInfo.getBarcode() != null ? outBarcodeInfo.getBarcode() : "";
            String materialNo = outBarcodeInfo.getMaterialno() != null ? outBarcodeInfo.getMaterialno() : "";
            String batchNo = outBarcodeInfo.getBatchno() != null ? outBarcodeInfo.getBatchno() : "";
            int barcodeQty = (int) outBarcodeInfo.getQty();
            String QRBarcode = materialNo+"%"+batchNo+"%"+barcodeQty+"%"+outBarcodeInfo.getBarcodetype();
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
            printInfo.setSpec(outBarcodeInfo.getSpec());
            printInfo.setPrintType(PrintType.PRINT_TYPE_RAW_MATERIAL_STYLE);
        }
        return printInfo;


    }

}
