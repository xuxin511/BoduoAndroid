package com.liansu.boduowms.modules.outstock.purchaseInspection.reviewScan;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.upshelf.scan.UpShelfScan;
import com.liansu.boduowms.modules.outstock.baseOutStockBusiness.baseReviewScan.BaseReviewScanModel;
import com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan.PurchaseInspectionProcessingScan;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelListToJsonArray;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des: 采购退发货装车扫描
 * @ Created by yangyiqing on 2020/6/28.
 */
public class PurchaseInspectionReviewScanModel extends BaseReviewScanModel {
    public final String TAG_GET_T_CHECK_OUT_STOCK_DETAIL_LIST_ADF_ASYNC = "ReviewScanModel_GetT_CheckOutStockDetailListADFAsync";
    public final String TAG_SELECT_MATERIAL                             = "PurchaseInspectionProcessingModel_SelectMaterial";  //获取物料信息
    public final String TAG_SUBMIT_REVIEW_SCAN_ADF_ASYNC                = "PurchaseInspectionProcessingModel_SelectMaterial";  //复核扫描提交
    String TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC = "PurchaseReturnReviewScanModel_PostT_OutStockDetailADFAsync"; //单据提交
    private final int RESULT_TAG_GET_T_CHECK_OUT_STOCK_DETAIL_LIST_ADF_ASYNC = 301;
    private final int RESULT_TAG_SELECT_MATERIAL                             = 302;
    private final int RESULT_TAG_SUBMIT_REVIEW_SCAN_ADF_ASYNC                = 303;
    private final int RESULT_TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC           = 304;

    public PurchaseInspectionReviewScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    protected void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_T_CHECK_OUT_STOCK_DETAIL_LIST_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_CHECK_OUT_STOCK_DETAIL_LIST_ADF_ASYNC");
                break;
            case RESULT_TAG_SELECT_MATERIAL:
                listener = mNetMap.get("TAG_SELECT_MATERIAL");
                break;
            case RESULT_TAG_SUBMIT_REVIEW_SCAN_ADF_ASYNC:
                listener = mNetMap.get("TAG_SUBMIT_REVIEW_SCAN_ADF_ASYNC");
                break;
            case RESULT_TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC:
                listener = mNetMap.get("TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC");
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    /**
     * @desc: 保存表体明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/30 18:09
     */
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
     * @desc: 保存表头信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/30 18:09
     */
    public void setOrderHeaderInfo(OutStockOrderHeaderInfo orderHeaderInfo) {
        mOrderHeaderInfo = orderHeaderInfo;
    }

    public OutStockOrderHeaderInfo getOrderHeaderInfo() {
        return mOrderHeaderInfo;
    }

    /**
     * @desc: 获取单据明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    @Override
    public void requestOrderDetail(OrderRequestInfo receiptModel, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_CHECK_OUT_STOCK_DETAIL_LIST_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(receiptModel);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_GET_T_CHECK_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_CHECK_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, mContext.getString(R.string.message_request_get_order_detail), mContext, mHandler, RESULT_TAG_GET_T_CHECK_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, null, UrlInfo.getUrl().InspecReturn_GetT_CheckOutStockDetailListADFAsync, modelJson, null);
    }

    /**
     * @desc:
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestOrderRefer(List<OrderRequestInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
        LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC, mContext.getString(R.string.message_request_quality_un_inspection_refer), mContext, mHandler, RESULT_TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().InspecReturn_PostT_OutStockDetailADFAsync, modelJson, null);

    }

    /**
     * @desc: 查询条码信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestBarcodeInfoQuery(String material, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_SELECT_MATERIAL", callBackListener);
        String modelJson = parseModelToJson(material);
        LogUtil.WriteLog(UpShelfScan.class, TAG_SELECT_MATERIAL, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SELECT_MATERIAL, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_TAG_SELECT_MATERIAL, null, UrlInfo.getUrl().SelectMaterial, modelJson, null);

    }

    /**
     * @desc: 提交条码信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestBarcodeInfoRefer(OutStockOrderDetailInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_SUBMIT_REVIEW_SCAN_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(UpShelfScan.class, TAG_SUBMIT_REVIEW_SCAN_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SUBMIT_REVIEW_SCAN_ADF_ASYNC, mContext.getString(R.string.message_request_barcode_refer), mContext, mHandler, RESULT_TAG_SUBMIT_REVIEW_SCAN_ADF_ASYNC, null, UrlInfo.getUrl().InspecReturn_SubmitReviewScanADFAsync, modelJson, null);
    }

    /**
     * @desc: 校验并更新物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 13:40
     */
    @Override
    public BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> checkAndUpdateMaterialInfo(OutStockOrderDetailInfo detailInfo, boolean isUpdate) {
        BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> resultInfo = new BaseMultiResultInfo<>();

        if (detailInfo != null) {
//            String materialNo = detailInfo.getMaterialno() != null ? detailInfo.getMaterialno() : "";
//            String batchNo = detailInfo.getBatchno() != null ? detailInfo.getBatchno() : "";
            String rowNo = detailInfo.getRowno() != null ? detailInfo.getRowno() : "";
            String rowDel = detailInfo.getRownodel() != null ? detailInfo.getRownodel() : "";

            for (OutStockOrderDetailInfo info : mOrderDetailList) {
//                String sMaterialNo = detailInfo.getMaterialno() != null ? detailInfo.getMaterialno() : "";
                String sRowNo = detailInfo.getRowno() != null ? detailInfo.getRowno() : "";
                String sRowDel = detailInfo.getRownodel() != null ? detailInfo.getRownodel() : "";
                if (sRowNo.equals(rowNo) && sRowDel.equals(rowDel)) {
                    if (isUpdate) {
                        float sScanQty = detailInfo.getScanqty();
                        info.setRemainqty(ArithUtil.sub(info.getRemainqty(), sScanQty));
                        info.setReviewQty(ArithUtil.add(sScanQty, info.getReviewQty()));
                        resultInfo.setHeaderStatus(true);
                        resultInfo.setInfo(info);
                        break;
                    }
                } else {
                    resultInfo.setMessage("校验物料行失败:项次[" + sRowNo + "]或项序[" + rowDel + "]+不订单在中");
                    resultInfo.setHeaderStatus(false);
                    return resultInfo;
                }


            }

        }
        return resultInfo;
    }

}
