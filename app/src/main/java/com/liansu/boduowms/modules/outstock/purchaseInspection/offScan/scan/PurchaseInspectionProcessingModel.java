package com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.upshelf.scan.UpShelfScan;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class PurchaseInspectionProcessingModel extends BaseModel {
    String TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC = "PurchaseInspectionProcessingModel_InspecReturn_GetT_OutStockDetailListADFAsync";  //采购验退获取表体
    String TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC     = "PurchaseInspectionProcessingModel_InspecReturn_SaveT_OutStockDetailADFAsync";  //采购验退获取表体
    String TAG_SELECT_MATERIAL                       = "PurchaseInspectionProcessingModel_SelectMaterial";  //获取物料信息
    String TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC     = "PurchaseInspectionProcessingModel_PostT_OutStockDetailADFAsync"; //单据提交
    String TAG_INSPEC_RETURN_PRINT_INSPEC_RETURN     = "PurchaseInspectionProcessingModel_InspecReturn_PrintInspecReturn"; //单据打印
    public static final int SCAN_TYPE_PALLET_NO                              = 1;
    public static final int SCAN_TYPE_SUB_BARCODE                            = 2;
    private final       int RESULT_TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC = 221;
    private final       int RESULT_TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC     = 222;
    private final       int RESULT_TAG_SELECT_MATERIAL                       = 223;
    private final       int RESULT_TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC     = 224;
    private final       int RESULT_TAG_INSPEC_RETURN_PRINT_INSPEC_RETURN     = 225;

    public static final int BARCODE_TYPE_PALLET_NO   = 1;  //托盘
    public static final int BARCODE_TYPE_OUT_BARCODE = 2; //外箱
    public static final int BARCODE_TYPE_BULK        = 3; //散件

    List<OutStockOrderDetailInfo> mOrderDetailList = new ArrayList<>();
    List<OutBarcodeInfo>          mBarCodeInfos    = new ArrayList<>();
    private OutStockOrderHeaderInfo mOrderHeaderInfo = null;
    UUID mUuid = null;
    private             AreaInfo mAreaInfo;
    private             int      mOperationType           = -1;
    private             String   mQualityType             = "";
    public static final String   QUALITY_TYPE_QUALIFIED   = "QUALIFIED";
    public static final String   QUALITY_TYPE_UNQUALIFIED = "UNQUALIFIED";

    public PurchaseInspectionProcessingModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    protected void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;

        switch (msg.what) {
            case RESULT_TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC");
                break;
            case RESULT_TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC:
                listener = mNetMap.get("TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC");
                break;
            case RESULT_TAG_SELECT_MATERIAL:
                listener = mNetMap.get("TAG_SELECT_MATERIAL");
                break;
            case RESULT_TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC:
                listener = mNetMap.get("TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC");
                break;
            case RESULT_TAG_INSPEC_RETURN_PRINT_INSPEC_RETURN:
                listener = mNetMap.get("TAG_INSPEC_RETURN_PRINT_INSPEC_RETURN");
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;

        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    public OutStockOrderHeaderInfo getOrderHeaderInfo() {
        return mOrderHeaderInfo;
    }

    public void setOrderHeaderInfo(OutStockOrderHeaderInfo orderHeaderInfo) {
        mOrderHeaderInfo = orderHeaderInfo;
    }

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
     * @desc: 获取质检表体
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestQualityInspectionDetail(OrderRequestInfo headerInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(headerInfo);
        LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, mContext.getString(R.string.message_request_purchase_inspection_detail), mContext, mHandler, RESULT_TAG_GET_T_OUT_STOCK_DETAIL_LIST_ADF_ASYNC, null, UrlInfo.getUrl().InspecReturn_GetT_OutStockDetailListADFAsync, modelJson, null);
    }


    /**
     * @desc: 提交条码信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestBarcodeInfoRefer(OutStockOrderDetailInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC, mContext.getString(R.string.message_request_barcode_refer), mContext, mHandler, RESULT_TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().InspecReturn_SaveT_OutStockDetailADFAsync, modelJson, null);

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
        LogUtil.WriteLog(UpShelfScan.class, TAG_SELECT_MATERIAL, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SELECT_MATERIAL, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_TAG_SELECT_MATERIAL, null, UrlInfo.getUrl().SelectMaterial, modelJson, null);

    }

    /**
     * @desc:
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestRefer(OrderRequestInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC, parseModelToJson(info));
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC, mContext.getString(R.string.message_request_quality_un_inspection_refer), mContext, mHandler, RESULT_TAG_POST_T_OUT_STOCK_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().InspecReturn_PostT_OutStockDetailADFAsync, modelJson, null);

    }

    /**
     * @desc: 订单打印
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/6 11:01
     */
    public void requestOrderPrint(OutStockOrderDetailInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_INSPEC_RETURN_PRINT_INSPEC_RETURN", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(PurchaseInspectionProcessingScan.class, TAG_INSPEC_RETURN_PRINT_INSPEC_RETURN, parseModelToJson(info));
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_INSPEC_RETURN_PRINT_INSPEC_RETURN, mContext.getString(R.string.request_print_order_purchase_return), mContext, mHandler, RESULT_TAG_INSPEC_RETURN_PRINT_INSPEC_RETURN, null, UrlInfo.getUrl().InspecReturn_PrintInspecReturn, modelJson, null);
    }


    public List<OutBarcodeInfo> getBarCodeInfos() {
        return mBarCodeInfos;
    }

    public void setBarCodeInfos(List<OutBarcodeInfo> barCodeInfos) {
        mBarCodeInfos.clear();
        if (barCodeInfos != null && barCodeInfos.size() > 0) {
            mBarCodeInfos.addAll(barCodeInfos);
        }

    }


    public AreaInfo getAreaInfo() {
        return mAreaInfo;
    }

    public void setAreaInfo(AreaInfo info) {
        mAreaInfo = info;
    }

    public void setQualityType(String qualityType) {
        if (qualityType != null) {
            mQualityType = qualityType;
        }
    }

    public String getQualityType() {
        return mQualityType;
    }

    /**
     * @desc: 校验并更新物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 13:40
     */
    public BaseMultiResultInfo<Boolean, Void> checkAndUpdateMaterialInfo(OutStockOrderDetailInfo detailInfo, boolean isUpdate) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();

        if (detailInfo != null) {
            String materialNo = detailInfo.getMaterialno() != null ? detailInfo.getMaterialno() : "";
            String batchNo = detailInfo.getBatchno() != null ? detailInfo.getBatchno() : "";
            String rowNo = detailInfo.getRowno() != null ? detailInfo.getRowno() : "";
            String rowDel = detailInfo.getRownodel() != null ? detailInfo.getRownodel() : "";
            String erpVoucherNo = detailInfo.getErpvoucherno() != null ? detailInfo.getErpvoucherno() : "";
            String arrVoucherNo=detailInfo.getArrvoucherno()!=null? detailInfo.getArrvoucherno():"";
            for (OutStockOrderDetailInfo info : mOrderDetailList) {
                String sMaterialNo = info.getMaterialno() != null ? info.getMaterialno() : "";
                String sBatchNo = info.getBatchno() != null ? info.getBatchno() : "";
                String sRowNo = info.getRowno() != null ? info.getRowno() : "";
                String sRowDel = info.getRownodel() != null ? info.getRownodel() : "";
                String sErpVoucherNo = info.getErpvoucherno() != null ? info.getErpvoucherno() : "";
                String sArrVoucherNo=info.getArrvoucherno()!=null?info.getArrvoucherno():"";
                if (sMaterialNo.equals(materialNo)) {
//                    if (sRowNo.equals(rowNo) && sRowDel.equals(rowDel)) {
                    if (erpVoucherNo.equals(sErpVoucherNo) && sArrVoucherNo.equals(arrVoucherNo)) {
                        if (isUpdate) {
                            info.setScanqty(detailInfo.getScanqty());
                            info.setRemainqty(ArithUtil.sub(info.getVoucherqty(), detailInfo.getScanqty()));
                            resultInfo.setHeaderStatus(true);
                            break;
                        }
                    } else {
                        resultInfo.setMessage("校验物料行失败:条码的订单号[" + erpVoucherNo + "],到货单号["+arrVoucherNo+"]不订单在中");
                        resultInfo.setHeaderStatus(false);
                        return resultInfo;
                    }
//                    } else {
//                        resultInfo.setMessage("校验物料行失败:物料的项次[" + sRowNo + "]或项序[" + rowDel + "]+不订单在中");
//                        resultInfo.setHeaderStatus(false);
//                        return resultInfo;
//                    }
                } else {
                    resultInfo.setMessage("校验物料行失败:物料[" + materialNo + "]+不订单在中");
                    resultInfo.setHeaderStatus(false);
                    return resultInfo;
                }

            }

        } else {
            resultInfo.setMessage("更新物料行失败:更新数据不能为空");
            resultInfo.setHeaderStatus(false);
            return resultInfo;
        }
        return resultInfo;
    }

    /**
     * @desc: 校验条码重复
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 16:01
     */
    public BaseMultiResultInfo<Boolean, Void> checkBarcode(OrderDetailInfo orderDetailInfo, OutBarcodeInfo scanBarcode) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        if (orderDetailInfo != null) {
            //校验条码重复
            List<OutBarcodeInfo> barcodeList = orderDetailInfo.getLstBarCode();
            if (barcodeList == null) {
                barcodeList = new ArrayList<>();
            }
            int index = barcodeList.indexOf(scanBarcode);
            if (index == -1) {
                resultInfo.setHeaderStatus(true);
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码编号[" + scanBarcode.getBarcode() + "]已扫描,无需重复扫描");
                return resultInfo;
            }
        } else {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("校验条码失败:对比的物料行不能为空");
            return resultInfo;
        }

        return resultInfo;
    }


    /**
     * @desc: 获取当前行的物料明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/5 17:52
     */
    public BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> findMaterialInfo(OutBarcodeInfo outBarcodeInfo) {
        BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> resultInfo = new BaseMultiResultInfo<>();
        String materialNo = outBarcodeInfo.getMaterialno() != null ? outBarcodeInfo.getMaterialno() : "";
        OutStockOrderDetailInfo mMaterialInfo = null;
        for (OutStockOrderDetailInfo info : mOrderDetailList) {
            String sMaterialNo = info.getMaterialno() != null ? info.getMaterialno() : "";
            if (sMaterialNo.equals(materialNo)) {
                mMaterialInfo = info;
                break;
            }
        }
        if (mMaterialInfo != null) {
            if (mMaterialInfo.getStrongholdcode() == null) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("获取当前物料信息失败:和当前扫描的条码的物料编号:" + materialNo + "匹配的物料数据中据点为空!");
                return resultInfo;
            }
            resultInfo.setHeaderStatus(true);
            resultInfo.setInfo(mMaterialInfo);
        } else {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("获取当前物料号信息失败:扫描的条码的物料编号:" + materialNo + "不在单据中");
            return resultInfo;
        }

        return resultInfo;
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
        for (OutStockOrderDetailInfo info:mOrderDetailList){
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
