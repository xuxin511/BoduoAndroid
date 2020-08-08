package com.liansu.boduowms.modules.outstock.offScan;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan.BaseOutStockBusinessModel;
import com.liansu.boduowms.ui.dialog.ToastUtil;
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
 * @ Created by yangyiqing on 2020/6/28.
 */
public class DistributionOffShelfModel extends BaseOutStockBusinessModel {
    public               String TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC        = "DistributionOffShelfModel_GetT_OutStockDetailListADFAsync";
    private static final int    RESULT_TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC = 201;
    List<OutStockOrderDetailInfo> mOrderDetailList = new ArrayList<>();  //单据明细
    public DistributionOffShelfModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    protected void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC");
                break;

            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }


   @Override
    public void setOrderDetailList(List<OutStockOrderDetailInfo> list) {
        mOrderDetailList.clear();
        if (list != null && list.size() > 0) {
            mOrderDetailList.addAll(list);
        }
    }


    public List<OutStockOrderDetailInfo> getOrderDetailList() {
        return mOrderDetailList;
    }

    /**
     * @desc: 获取出库明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestOrderDetailInfo(OutStockOrderHeaderInfo receiptModel, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(receiptModel);
       LogUtil.WriteLog(DistributionOffShelf.class, TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, modelJson);
       RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, mContext.getString(R.string.message_request_get_order_detail), mContext, mHandler, RESULT_TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, null, UrlInfo.getUrl().GetT_PurchaseOrderListADFAsync, modelJson, null);
    }

    /**
     * @desc: 找到扫描条码的所在行
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 11:01
     */
    public BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> findMaterialInfo2(OutBarcodeInfo scanFatherBarcode) {
        BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> resultInfo = new BaseMultiResultInfo<>();
        OutStockOrderDetailInfo sMaterialInfo = null;
        String barcodeMaterialNo = scanFatherBarcode.getMaterialno() != null ? scanFatherBarcode.getMaterialno() : "";
        String barcodeBatchNo = scanFatherBarcode.getBatchno() != null ? scanFatherBarcode.getBatchno() : "";
        String barcodeRowNo = scanFatherBarcode.getRowno() != null ? scanFatherBarcode.getRowno() : "";
        String barcodeRowDel = scanFatherBarcode.getRownodel() != null ? scanFatherBarcode.getRownodel() : "";
//        String barcodeStrongHoldCode = scanBarcode.getStrongholdcode() != null ? scanBarcode.getStrongholdcode() : "";
//        String barcodeErpVoucherNo = scanBarcode.getErpvoucherno() != null ? scanBarcode.getErpvoucherno() : "";
        float barcodeQty = scanFatherBarcode.getQty();
        try {

            if (barcodeMaterialNo == null || barcodeMaterialNo.equals("")) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的物料信息不能为空");
                return resultInfo;
            }

            if (barcodeQty < 0) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的数量必须大于0");
                return resultInfo;
            }
            //查找和条码的 物料和批次匹配的订单明细行的数据，只查第一个
            for (int i = 0; i < mOrderDetailList.size(); i++) {
                OutStockOrderDetailInfo materialInfo = mOrderDetailList.get(i);
                if (materialInfo != null) {
                    String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
//                    String batchNo = materialInfo.getBatchno() != null ? materialInfo.getBatchno() : "";
                    String rowNo = materialInfo.getRowno() != null ? materialInfo.getRowno() : "";
                    if (materialNo.trim().equals(barcodeMaterialNo.trim()) && rowNo.trim().equals(barcodeRowNo.trim())) {
                        sMaterialInfo = materialInfo;
                        break;
                    }
                }
            }

            if (sMaterialInfo != null) {
//                String strongHoldCode = sMaterialInfo.getStrongholdcode() != null ? sMaterialInfo.getStrongholdcode() : "";
//                String erpVoucherNo = sMaterialInfo.getErpvoucherno() != null ? sMaterialInfo.getErpvoucherno() : "";
//                if (!strongHoldCode.equals(barcodeStrongHoldCode)) {
//                    resultInfo.setHeaderStatus(false);
//                    resultInfo.setMessage("校验条码失败:条码的组织编码[" + barcodeStrongHoldCode + "]和订单的组织编码[" + strongHoldCode + "]不一致");
//                    return resultInfo;
//                }
//
//                if (!erpVoucherNo.equals(barcodeErpVoucherNo)) {
//                    resultInfo.setHeaderStatus(false);
//                    resultInfo.setMessage("校验条码失败:条码的订单号[" + barcodeErpVoucherNo + "]不匹配当前订单[" + erpVoucherNo + "]");
//                    return resultInfo;
//                }
                resultInfo.setHeaderStatus(true);
                resultInfo.setInfo(sMaterialInfo);
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:订单中没有物料号为:[" + barcodeMaterialNo + "],行号为:[" + barcodeRowNo + "]的物料行,扫描的条码序列号为:" + scanFatherBarcode.getBarcode());
                return resultInfo;
            }
        } catch (Exception e) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("校验条码失败:出现预期之外的错误:" + e.getMessage());
            return resultInfo;
        }

        return resultInfo;
    }


    /**
     * @desc: 获取配货下架表体明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestOrderDetail(OrderRequestInfo orderRequestInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(orderRequestInfo);
        LogUtil.WriteLog(DistributionOffShelf.class, TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, mContext.getString(R.string.message_request_get_order_detail), mContext, mHandler, RESULT_TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, null, UrlInfo.getUrl().GetT_OutStockDetailListADFAsync, modelJson, null);
    }
}
