package com.liansu.boduowms.modules.instock.transferToStorage.scan;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScanModel;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.HashMap;
import java.util.Map;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des: 调拨入库扫描
 * @ Created by yangyiqing on 2020/6/27.
 */
public class TransferToStorageScanModel extends BaseOrderScanModel {
    String TAG_GetT_InStockDetailListByHeaderIDADF = "ReceiptionScan_GetT_InStockDetailListByHeaderIDADF";
    String TAG_GetT_PalletDetailByBarCodeADF       = "ReceiptionScan_GetT_PalletDetailByBarCodeADF";
    String TAG_SaveT_InStockDetailADF              = "ReceiptionScan_SaveT_InStockDetailADF";
    String TAG_GetAreaModelADF                     = "ReceiptionScan_GetAreaModelADF";
    private final int RESULT_Msg_GetT_InStockDetailListByHeaderIDADF = 101;
    private final int RESULT_Msg_GetT_PalletDetailByBarCode          = 102;
    private final int RESULT_Msg_SaveT_InStockDetailADF              = 103;
    private final int RESULT_Msg_GetAreaModelADF                     = 104;

    public TransferToStorageScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    protected void onHandleMessage(Message msg) {

        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_Msg_GetT_InStockDetailListByHeaderIDADF:
                listener = mNetMap.get("TAG_GetT_InStockDetailListByHeaderIDADF");
                break;
            case RESULT_Msg_GetT_PalletDetailByBarCode:

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
    public void requestReceiptDetail(OrderHeaderInfo receiptModel, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_InStockDetailListByHeaderIDADF", callBackListener);
//        final ReceiptDetail_Model receiptDetailModel = new ReceiptDetail_Model();
//        receiptDetailModel.setHeaderID(receiptModel.getId());
//        receiptDetailModel.setErpVoucherNo(receiptModel.getErpvoucherno());
//        receiptDetailModel.setVoucherType(receiptModel.getVouchertype());
//        final Map<String, String> params = new HashMap<String, String>();
//        params.put("ModelDetailJson", parseModelToJson(receiptDetailModel));
//        String para = (new JSONObject(params)).toString();
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, para);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InStockDetailListByHeaderIDADF, mContext.getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), mContext, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null, URLModel.GetURL().GetT_InStockDetailListByHeaderIDADF, params, null);
    }


    /**
     * @desc: 组托并提交入库
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestCombineAndReferPallet(OrderDetailInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_PalletDetailByBarCodeADF", callBackListener);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("BarCode", parseModelToJson(info));
//        params.put("UserJson", parseModelToJson(BaseApplication.mCurrentUserInfo));
        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_PalletDetailByBarCodeADF, parseModelToJson(info));
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCodeADF, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_Msg_GetT_PalletDetailByBarCode, null, URLModel.GetURL().GetT_PalletDetailByBarCodeADF, params, null);

    }

    @Override
    protected PrintInfo getPrintModel(OutBarcodeInfo outBarcodeInfo) {
        return null;
    }

    /**
     * @desc: 过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 18:42
     */
    public void requestOrderRefer(OrderDetailInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_PalletDetailByBarCodeADF", callBackListener);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("BarCode", parseModelToJson(info));
//        params.put("UserJson", parseModelToJson(BaseApplication.mCurrentUserInfo));
        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_PalletDetailByBarCodeADF, parseModelToJson(info));
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCodeADF, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_Msg_GetT_PalletDetailByBarCode, null, UrlInfo.getUrl().SaveT_PurchaseDetailADFAsync, params, null);

    }


}
