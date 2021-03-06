package com.liansu.boduowms.modules.instock.productStorage.scan;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoice;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScanModel;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelListToJsonArray;

/**
 * @desc: 生产入库扫描
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/7/15 10:40
 */
public class ProductStorageScanModel extends BaseOrderScanModel {
    String TAG_SAVE_IN_STOCK_WORK_ORDER_LIST_TO_DB    = "ProductStorageScanModel_Save_InStockWorkOrderListToDB";  //实时提交
    String TAG_POST_T_WORK_ORDER_DETAIL_ADF_ASYNC     = "ProductStorageScanModel_TAG_PostT_WorkOrderDetailADFAsync";  //过账
    String TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC = "ProductStorageScanModel_GetT_WorkOrderDetailListADFAsync"; // 获取表体
    private final int RESULT_Msg_GetT_InStockDetailListByHeaderIDADF    = 112;
    private final int RESULT_Msg_SaveT_PurchaseDetailADFAsync           = 113;
    private final int RESULT_TAG_SAVE_IN_STOCK_WORK_ORDER_LIST_TO_DB    = 123;
    private final int RESULT_TAG_POST_T_WORK_ORDER_DETAIL_ADF_ASYNC     = 124;
    private final int RESULT_TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC = 125;

    public ProductStorageScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler, OrderType.IN_STOCK_ORDER_TYPE_PRODUCT_STORAGE_VALUE);
    }

    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC");
                break;
            case RESULT_Msg_SaveT_PurchaseDetailADFAsync:
                listener = mNetMap.get("TAG_SaveT_PurchaseDetailADFAsync");
                break;
            case RESULT_TAG_SAVE_IN_STOCK_WORK_ORDER_LIST_TO_DB:
                listener = mNetMap.get("TAG_SAVE_IN_STOCK_WORK_ORDER_LIST_TO_DB");
                break;
            case RESULT_TAG_POST_T_WORK_ORDER_DETAIL_ADF_ASYNC:
                listener = mNetMap.get("TAG_POST_T_WORK_ORDER_DETAIL_ADF_ASYNC");
                break;

        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
        super.onHandleMessage(msg);
    }

    @Override
    public void requestCombineAndReferPallet(OrderDetailInfo info, NetCallBackListener<String> callBackListener) {

    }

    /**
     * @desc: 获取报工单明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestOrderDetail(OrderHeaderInfo orderHeaderInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC", callBackListener);
        String ModelJson = GsonUtil.parseModelToJson(orderHeaderInfo);
        LogUtil.WriteLog(BaseOrderBillChoice.class, TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC, ModelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC, mContext.getString(R.string.MSG_GET_FINISHED_PRODUCT), mContext, mHandler, RESULT_TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC, null, UrlInfo.getUrl().GetT_WorkOrderDetailListADFAsync, ModelJson, null);

    }

    /**
     * @desc: 过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 18:46
     */

    public void requestOrderRefer(List<OrderDetailInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_POST_T_WORK_ORDER_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_POST_T_WORK_ORDER_DETAIL_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_POST_T_WORK_ORDER_DETAIL_ADF_ASYNC, mContext.getString(R.string.Msg_order_refer), mContext, mHandler, RESULT_TAG_POST_T_WORK_ORDER_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().PostT_WorkOrderDetailADFAsync, modelJson, null);
    }

    /**
     * @desc: 组托并提交入库
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */

    @Override
    public void requestCombineAndReferPallet(List<OrderDetailInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_SAVE_IN_STOCK_WORK_ORDER_LIST_TO_DB", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_SAVE_IN_STOCK_WORK_ORDER_LIST_TO_DB, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SAVE_IN_STOCK_WORK_ORDER_LIST_TO_DB, mContext.getString(R.string.message_request_barcode_refer), mContext, mHandler, RESULT_TAG_SAVE_IN_STOCK_WORK_ORDER_LIST_TO_DB, null, UrlInfo.getUrl().Save_InStockWorkOrderListToDB, modelJson, null);
    }

    /**
     * @desc: 找到扫描条码的所在行
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 11:01
     */
    @Override
    public BaseMultiResultInfo<Boolean, OrderDetailInfo> findMaterialInfo(OutBarcodeInfo scanBarcode) {
        BaseMultiResultInfo<Boolean, OrderDetailInfo> resultInfo = new BaseMultiResultInfo<>();
        OrderDetailInfo sMaterialInfo = null;
        String barcodeMaterialNo = scanBarcode.getMaterialno() != null ? scanBarcode.getMaterialno() : "";
        String barcodeStrongHoldCode = scanBarcode.getStrongholdcode() != null ? scanBarcode.getStrongholdcode() : "";
        String barcodeErpVoucherNo = scanBarcode.getErpvoucherno() != null ? scanBarcode.getErpvoucherno() : "";
        float barcodeQty = scanBarcode.getQty();
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
                OrderDetailInfo materialInfo = mOrderDetailList.get(i);
                if (materialInfo != null) {
                    String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
                    if (materialNo.trim().equals(barcodeMaterialNo.trim()) ) {
                        sMaterialInfo = materialInfo;
                        break;
                    }
                }
            }

            if (sMaterialInfo != null) {
                String strongHoldCode = sMaterialInfo.getStrongholdcode() != null ? sMaterialInfo.getStrongholdcode() : "";
                String erpVoucherNo = sMaterialInfo.getErpvoucherno() != null ? sMaterialInfo.getErpvoucherno() : "";
                if (!strongHoldCode.equals(barcodeStrongHoldCode)) {
                    resultInfo.setHeaderStatus(false);
                    resultInfo.setMessage("校验条码失败:条码的组织编码[" + barcodeStrongHoldCode + "]和订单的组织编码[" + strongHoldCode + "]不一致");
                    return resultInfo;
                }

                if (!erpVoucherNo.equals(barcodeErpVoucherNo)) {
                    resultInfo.setHeaderStatus(false);
                    resultInfo.setMessage("校验条码失败:条码的订单号[" + barcodeErpVoucherNo + "]不匹配当前订单[" + erpVoucherNo + "]");
                    return resultInfo;
                }
                resultInfo.setHeaderStatus(true);
                resultInfo.setInfo(sMaterialInfo);
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:订单中没有物料号为:[" + barcodeMaterialNo + "]的物料行,扫描的条码序列号为:" + scanBarcode.getBarcode());
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
     * @desc: 更新物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 13:40
     */
    @Override
    public BaseMultiResultInfo<Boolean, Void> checkMaterialInfo(OrderDetailInfo detailInfo, OutBarcodeInfo scanBarcode, boolean isUpdate) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        String barcodeMaterialNo = scanBarcode.getMaterialno() != null ? scanBarcode.getMaterialno() : "";
        String barcodeBatchNo = scanBarcode.getBatchno() != null ? scanBarcode.getBatchno() : "";
        float barcodeScanQty = scanBarcode.getQty();
        if (detailInfo != null) {
            String materialNo = detailInfo.getMaterialno() != null ? detailInfo.getMaterialno() : "";
            if (materialNo.trim().equals(barcodeMaterialNo.trim())) {
                float scanQty = detailInfo.getScanqty();
                float remainQty = detailInfo.getRemainqty(); //订单的剩余扫描数量　　
                float hasRemainQty = ArithUtil.sub(detailInfo.getRemainqty(), detailInfo.getScanqty()); // (订单减去已扫描条码的)剩余扫描数量
                if (remainQty > 0) {
                        if (ArithUtil.sub(remainQty, barcodeScanQty) >= 0) {
                            if (isUpdate) {
                                detailInfo.setScanqty(ArithUtil.add(scanQty, barcodeScanQty));
                                detailInfo.setRemainqty(ArithUtil.sub(remainQty, barcodeScanQty));
                                detailInfo.setReceiveqty(ArithUtil.sub(detailInfo.getVoucherqty(), detailInfo.getRemainqty()));
                            } else {
                                List<OutBarcodeInfo> list = new ArrayList<>();
                                list.add(scanBarcode);
                                detailInfo.setLstBarCode(list);
                            }
                            resultInfo.setHeaderStatus(true);

                        } else {
                            resultInfo.setMessage("校验物料行失败:物料号:[" + barcodeMaterialNo + "],批次：[" + barcodeBatchNo + "]的物料行的扫描数量不能大于订单剩余可扫描数量");
                            resultInfo.setHeaderStatus(false);
                            return resultInfo;
                        }

                } else if (remainQty == 0) {
                    resultInfo.setMessage("校验物料行失败:物料号:[" + barcodeMaterialNo + "],批次：[" + barcodeBatchNo + "]的物料行已扫完毕");
                    resultInfo.setHeaderStatus(false);
                    return resultInfo;
                } else {
                    resultInfo.setMessage("校验物料行出现异常:剩余扫描数量不能为负数!");
                    resultInfo.setHeaderStatus(false);
                    return resultInfo;
                }
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("更新物料行失败:订单中没有物料号为:[" + barcodeMaterialNo + "],批次为：[" + barcodeBatchNo + "]的物料行,扫描的条码序列号为:" + scanBarcode.getBarcode());
                return resultInfo;
            }


        } else {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("绑定条码信息失败:没有找到能匹配条码的物料号:[" + barcodeMaterialNo + "],批次：[" + barcodeBatchNo + "]的物料行");
            return resultInfo;

        }
        return resultInfo;
    }


}
