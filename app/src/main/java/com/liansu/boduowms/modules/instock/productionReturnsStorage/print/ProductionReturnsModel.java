package com.liansu.boduowms.modules.instock.productionReturnsStorage.print;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.instock.batchPrint.order.BaseOrderLabelPrintSelect;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.modules.print.linkos.PrintType;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/17.
 */
public class ProductionReturnsModel extends BaseModel {
    public String TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC = "ProductionReturnsModel_TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC";  // 获取工单明细
    public String TAG_PRINT_PALLET_NO = "SalesReturnStorageScanModel_Print_PalletNo";  //销售退货打印获取托盘
    String TAG_SELECT_MATERIAL = "ProductionReturnsModel_SelectMaterial";  //获取物料信息
    private final int                   RESULT_TAG_SELECT_MATERIAL                         = 10001;
    private final int             RESULT_TAG_PRINT_PALLET_NO                        = 124;
    private final int             RESULT_TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC = 121;
    protected     OrderHeaderInfo mOrderHeaderInfo                                  = null;
    protected     List<OrderDetailInfo> mOrderDetailList                                   = new ArrayList<>();

    public ProductionReturnsModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_SELECT_MATERIAL:
                listener = mNetMap.get("TAG_SELECT_MATERIAL");
                break;
            case RESULT_TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC");
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
        mOrderDetailList.clear();
        if (list != null && list.size() > 0) {
            mOrderDetailList.addAll(list);
        }
    }

    List<OrderDetailInfo> getOrderDetailsList() {
        return mOrderDetailList;
    }

    void setOrderHeaderInfo(OrderHeaderInfo orderHeaderInfo) {
        mOrderHeaderInfo = orderHeaderInfo;
    }

    public OrderHeaderInfo getOrderHeaderInfo() {
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
        mNetMap.put("TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(orderRequestInfo);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC, mContext.getString(R.string.message_request_production_return_detail), mContext, mHandler, RESULT_TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC, null, UrlInfo.getUrl().GetT_WorkOrderDetailListADFAsync, modelJson, null);
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


    public void onReset(){
        mOrderHeaderInfo=null;
        mOrderDetailList.clear();
    }

}
