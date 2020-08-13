package com.liansu.boduowms.modules.instock.purchaseStorage.scan;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
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
public class PurchaseStorageScanModel extends BaseOrderScanModel {

    String TAG_GetT_PurchaseOrderListADFAsync = "PurchaseStorageScanModel_TAG_GetT_PurchaseOrderListADFAsync";
    String TAG_GetT_PalletDetailByBarCodeADF  = "ReceiptionScan_GetT_PalletDetailByBarCodeADF";
    String TAG_SaveT_PurchaseDetailADFAsync   = "PurchaseStorageScanModel_TAG_SaveT_PurchaseDetailADFAsync";
    String TAG_PostT_PurchaseDetailADFAsync   = "PurchaseStorageScanModel_PurchaseDetailADFAsync";
    private final int RESULT_Msg_GetT_InStockDetailListByHeaderIDADF = 112;
    private final int RESULT_Msg_GetT_PalletDetailByBarCode          = 102;
    private final int RESULT_Msg_SaveT_PurchaseDetailADFAsync        = 113;
    private final int RESULT_Msg_GetAreaModelADF                     = 104;
    private final int RESULT_Msg_PostT_PurchaseDetailADFAsync        = 114;

    public PurchaseStorageScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    protected void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_Msg_GetT_InStockDetailListByHeaderIDADF:
                listener = mNetMap.get("TAG_GetT_PurchaseOrderListADFAsync");
                break;
            case RESULT_Msg_SaveT_PurchaseDetailADFAsync:
                listener = mNetMap.get("TAG_SaveT_PurchaseDetailADFAsync");
                break;
            case RESULT_Msg_PostT_PurchaseDetailADFAsync:
                listener = mNetMap.get("TAG_PostT_PurchaseDetailADFAsync");
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
        super.onHandleMessage(msg);
    }


    /**
     * @desc: 获取采购订单表体明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestReceiptDetail(OrderRequestInfo receiptModel, NetCallBackListener<String> callBackListener) {
        receiptModel.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE);
        mNetMap.put("TAG_GetT_PurchaseOrderListADFAsync", callBackListener);
        String modelJson = parseModelToJson(receiptModel);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PurchaseOrderListADFAsync, mContext.getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), mContext, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null, UrlInfo.getUrl().GetT_PurchaseOrderListADFAsync, modelJson, null);
    }


    /**
     * @desc: 组托并提交入库
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestCombineAndReferPallet(OrderDetailInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_SaveT_PurchaseDetailADFAsync", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_SaveT_PurchaseDetailADFAsync, parseModelToJson(info));
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_PurchaseDetailADFAsync, mContext.getString(R.string.message_request_refer_barcode_info), mContext, mHandler, RESULT_Msg_SaveT_PurchaseDetailADFAsync, null, UrlInfo.getUrl().SaveT_PurchaseDetailADFAsync, modelJson, null);

    }

    /**
     * @desc: 过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 18:46
     */
    public void requestOrderRefer(List<OrderDetailInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_PostT_PurchaseDetailADFAsync", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_PostT_PurchaseDetailADFAsync, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_PostT_PurchaseDetailADFAsync, mContext.getString(R.string.Msg_order_refer), mContext, mHandler, RESULT_Msg_PostT_PurchaseDetailADFAsync, null, UrlInfo.getUrl().PostT_PurchaseDetailADFAsync, modelJson, null);

    }

    @Override
    protected PrintInfo getPrintModel(OutBarcodeInfo outBarcodeInfo) {
        PrintInfo printInfo = null;
        if (outBarcodeInfo != null) {
            printInfo = new PrintInfo();
            String barcode=outBarcodeInfo.getBarcode()!=null?outBarcodeInfo.getBarcode() : "";
            String materialNo = outBarcodeInfo.getMaterialno() != null ? outBarcodeInfo.getMaterialno() : "";
            String batchNo = outBarcodeInfo.getBatchno() != null ? outBarcodeInfo.getBatchno() : "";
            int barcodeQty = (int) outBarcodeInfo.getQty();
            String QRBarcode = materialNo+"%"+batchNo+"%"+barcodeQty+"%"+outBarcodeInfo.getBarcodetype();
            printInfo.setMaterialNo(materialNo);
            printInfo.setMaterialDesc(outBarcodeInfo.getMaterialdesc());
            printInfo.setBatchNo(batchNo);
            printInfo.setQty(barcodeQty);
            printInfo.setPackQty(outBarcodeInfo.getPackQty());
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
    /**
     * @desc: 是否订单已扫描完毕
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/8 11:38
     */
    public   BaseMultiResultInfo<Boolean, Void>  isOrderScanFinished(){
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        boolean IS_ORDER_FINISHED=true;
        for (OrderDetailInfo info:mOrderDetailList){
            if (info!=null){
                if (info.getRemainqty()!=0){
                    IS_ORDER_FINISHED=false;
                    break;
                }
            }
        }
        if (IS_ORDER_FINISHED){
            resultInfo.setHeaderStatus(true);
            resultInfo.setMessage("订单已扫描完毕!");
        }else {
            resultInfo.setHeaderStatus(false);

        }
        return resultInfo;
    }
}
