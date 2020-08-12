package com.liansu.boduowms.modules.instock.productionReturnsStorage.print;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.modules.print.linkos.PrintType;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/17.
 */
public class ProductionReturnsModel extends BaseModel {
    public        String TAG_GET_T_SALE_RETURN_DETAIL_LIST_ADF_ASYNC        = "SaleReturnPrintModel_Post_GetT_SaleReturnDetailListADFAsync";  // 获取物料批次
    private final int    RESULT_TAG_GET_T_SALE_RETURN_DETAIL_LIST_ADF_ASYNC = 121;
    public        String TAG_PRINT_PALLET_NO                                = "SalesReturnStorageScanModel_Print_PalletNo";  //销售退货打印获取托盘
    private final int    RESULT_TAG_PRINT_PALLET_NO                         = 124;
    List<OrderDetailInfo> mOrderDetailsList = new ArrayList<>();
    private OrderHeaderInfo mOrderHeaderInfo;

    public ProductionReturnsModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    protected void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_T_SALE_RETURN_DETAIL_LIST_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_SALE_RETURN_DETAIL_LIST_ADF_ASYNC");
                break;
            case RESULT_TAG_PRINT_PALLET_NO:
                listener = mNetMap.get("TAG_PRINT_PALLET_NO");
                break;

            case NetworkError.NET_ERROR_CUSTOM:
                MessageBox.Show(mContext, "获取请求失败_____" + msg.obj);
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }

    }


    /**
     * @desc: 保存单据明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/11 16:53
     */
    void setOrderDetailsList(List<OrderDetailInfo> list) {
        mOrderDetailsList.clear();
        if (list != null && list.size() > 0) {
            mOrderDetailsList.addAll(list);
        }
    }

    List<OrderDetailInfo> getOrderDetailsList() {
        return mOrderDetailsList;
    }

    void setOrderHeaderInfo(OrderHeaderInfo orderHeaderInfo) {
        mOrderHeaderInfo = orderHeaderInfo;
    }

    OrderHeaderInfo getOrderHeaderInfo() {
        return mOrderHeaderInfo;
    }


     //物料编号（ERP获取）
     //物料名称（ERP获取）
     //物料规格（ERP获取）
     //采购批次或供应商批次（手工录入）
     //物料数量（手工录入）
    protected PrintInfo getPrintModel(OutBarcodeInfo outBarcodeInfo) {
        PrintInfo printInfo = null;
        if (outBarcodeInfo != null) {
            printInfo = new PrintInfo();
            String materialNo = outBarcodeInfo.getMaterialno() != null ? outBarcodeInfo.getMaterialno() : "";
            String materialDesc = outBarcodeInfo.getMaterialdesc() != null ? outBarcodeInfo.getMaterialdesc() : "";
            String batchNo = outBarcodeInfo.getBatchno() != null ? outBarcodeInfo.getBatchno() : "";
            String spec = outBarcodeInfo.getSpec() != null ? outBarcodeInfo.getSpec() : "";
            int barcodeQty = (int) outBarcodeInfo.getQty();
            String QRBarcode = materialNo + "%" + batchNo + "%" + barcodeQty + "%" + PrintType.PRINT_LABEL_TYPE_RAW_MATERIAL;
            printInfo.setMaterialNo(materialNo);
            printInfo.setMaterialDesc(outBarcodeInfo.getMaterialdesc());
            printInfo.setBatchNo(batchNo);
            printInfo.setQty(barcodeQty);
            printInfo.setQRCode(QRBarcode);
            printInfo.setSpec(spec);
            printInfo.setPrintType(PrintType.PRINT_TYPE_RAW_MATERIAL_STYLE);
        }
        return printInfo;


    }


    /**
     * @desc: 获取生产退料表体明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestOrderDetail(OrderRequestInfo orderRequestInfo, NetCallBackListener<String> callBackListener) {
        orderRequestInfo.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE);
        mNetMap.put("TAG_GetT_PurchaseOrderListADFAsync", callBackListener);
        String modelJson = parseModelToJson(orderRequestInfo);
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PurchaseOrderListADFAsync, mContext.getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), mContext, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null, UrlInfo.getUrl().GetT_PurchaseOrderListADFAsync, modelJson, null);
    }


}
