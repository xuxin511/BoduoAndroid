package com.liansu.boduowms.modules.instock.baseInstockReturnBusiness.scan;

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
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScanModel;
import com.liansu.boduowms.modules.instock.batchPrint.order.BaseOrderLabelPrintSelect;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.modules.print.linkos.PrintType;
import com.liansu.boduowms.utils.GUIDHelper;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelListToJsonArray;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class InStockReturnsStorageScanModel extends BaseOrderScanModel {
    public String TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC     = "InStockReturnsStorageScanModel_TAG_GET_T_WORK_ORDER_DETAIL_LIST_ADF_ASYNC";  // 获取工单明细
    public String TAG_SAVE_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC = "ProductionReturnsStorageScanModel_TAG_SaveT_WorkOrderReturnDetailADFAsync";
    protected   String TAG_POST_T_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC = "ProductionReturnsStorageScanModel_TAG_POST_T_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC";
    String TAG_SELECT_MATERIAL                           = "InStockReturnsStorageScanModel_SelectMaterial";  //获取物料信息
    private final       int            RESULT_TAG_SELECT_MATERIAL                           = 10001;
    protected final       int            RESULT_TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC     = 121;
    protected final       int            RESULT_TAG_SAVE_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC = 122;
    protected final       int            RESULT_TAG_POST_T_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC = 123;
    public final static int            IN_STOCK_RETURN_TYPE_NONE                            = -1; //没有选择退货类型
    public final static int            IN_STOCK_RETURN_TYPE_ACTIVE                          = 1; //有源退货
    public final static int            IN_STOCK_RETURN_TYPE_NO_SOURCE                       = 2;//无源退货
    public final static int            RETURN_PALLET_TYPE_NONE                              = -1;//未选择托盘类型
    public final static int            RETURN_PALLET_TYPE_OLD_PALLET                        = 1;//现有托盘
    public final static int            RETURN_PALLET_TYPE_NEW_PALLET                        = 2;//新托盘
    public final static String         RETURN_PALLET_TYPE_OLD_PALLET_NAME                   = "现有托盘";
    public final static String         RETURN_PALLET_TYPE_NEW_PALLET_NAME                   = "新托盘";
    protected           int            mBusinessType                                        = IN_STOCK_RETURN_TYPE_NONE;
    protected           OutBarcodeInfo mCurrentPalletInfo                                   = null;
    protected           OutBarcodeInfo mCurrentOuterBoxInfo                                 = null;

    public InStockReturnsStorageScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler, OrderType.IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE_VALUE);
    }

    public InStockReturnsStorageScanModel(Context context, MyHandler<BaseActivity> handler, int voucherType, int businessType) {
        super(context, handler, OrderType.IN_STOCK_ORDER_TYPE_PRODUCTION_RETURNS_STORAGE_VALUE);
        setVoucherType(voucherType);
        setBusinessType(businessType);
    }

    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_SELECT_MATERIAL:
                listener = mNetMap.get("TAG_SELECT_MATERIAL");
                break;
            case RESULT_TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC");
                break;
            case RESULT_TAG_SAVE_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC:
                listener = mNetMap.get("TAG_SAVE_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC");
                break;
            case RESULT_TAG_POST_T_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC:
                listener = mNetMap.get("TAG_POST_T_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC");
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
        super.onHandleMessage(msg);
    }

    /**
     * @desc: 设置入库类型
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/20 20:43
     */
    public void setBusinessType(int businessType) {
        mBusinessType = businessType;
    }

    public int getBusinessType() {
        return mBusinessType;
    }

    /**
     * @desc: 保存当前已扫描的托盘码条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/20 23:11
     */
    public void setCurrentScanInfo(OutBarcodeInfo info) {
        mCurrentPalletInfo = info;
    }

    public OutBarcodeInfo getCurrentScanInfo() {
        return mCurrentPalletInfo;
    }

    /**
     * @desc: 保存当前已扫描的外箱物料编码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/22 10:01
     */
    public void setCurrentOuterBoxInfo(OutBarcodeInfo info) {
        mCurrentOuterBoxInfo = info;
    }

    public OutBarcodeInfo getCurrentOuterBoxInfo() {
        return mCurrentOuterBoxInfo;
    }


    /**
     * @desc: 获取订单表体明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestOrderDetail(OrderRequestInfo orderRequestInfo, NetCallBackListener<String> callBackListener) {
//        mNetMap.put("TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC", callBackListener);
//        String modelJson = parseModelToJson(orderRequestInfo);
//        LogUtil.WriteLog(InStockReturnStorageScan.class, TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC, mContext.getString(R.string.message_request_production_return_detail), mContext, mHandler, RESULT_TAG_GET_T_SUB_ORDER_DETAIL_LIST_ADF_ASYNC, null, UrlInfo.getUrl().GetT_WorkOrderDetailListADFAsync, modelJson, null);
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


    /**
     * @desc: 组托并提交入库
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestCombineAndReferPallet(OrderDetailInfo info, NetCallBackListener<String> callBackListener) {
    }

    /**
     * @desc: 新托盘组托并提交入库  有源
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    @Override
    public void requestCombineAndReferPallet(List<OrderDetailInfo> list, NetCallBackListener<String> callBackListener) {
//        mNetMap.put("TAG_SAVE_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC", callBackListener);
//        String modelJson = parseModelListToJsonArray(list);
//        LogUtil.WriteLog(InStockReturnStorageScan.class, TAG_SAVE_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SAVE_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC, mContext.getString(R.string.message_request_refer_barcode_info), mContext, mHandler, RESULT_TAG_SAVE_SUB_ORDER_RETURN_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().SaveT_SaleReturnDetailWMSADFAsync, modelJson, null);
    }

    /**
     * @desc: 过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 18:46
     */
    public void requestOrderRefer(List<OrderDetailInfo> list, NetCallBackListener<String> callBackListener) {
//        mNetMap.put("TAG_POST_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC", callBackListener);
//        String modelJson = parseModelListToJsonArray(list);
//        LogUtil.WriteLog(InStockReturnStorageScan.class, TAG_POST_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_POST_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC, mContext.getString(R.string.Msg_order_refer), mContext, mHandler, RESULT_TAG_POST_T_WORK_ORDER_RETURN_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().PostT_WorkOrderReturnDetailADFAsync, modelJson, null);

    }

    /**
     * @desc: 过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 18:46
     */
    public void requestNoSourceOrderRefer(List<OutBarcodeInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC, mContext.getString(R.string.message_request_refer_barcode_info), mContext, mHandler, RESULT_TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().Post_SaleReturnDetailADFasync, modelJson, null);

    }

    @Override
    protected PrintInfo getPrintModel(OutBarcodeInfo outBarcodeInfo) {
        PrintInfo printInfo = null;
        if (outBarcodeInfo != null) {
            printInfo = new PrintInfo();
            String materialNo = outBarcodeInfo.getMaterialno() != null ? outBarcodeInfo.getMaterialno() : "";
            String batchNo = outBarcodeInfo.getBatchno() != null ? outBarcodeInfo.getBatchno() : "";
            int barcodeQty = (int) outBarcodeInfo.getQty();
            String QRBarcode = materialNo + "%" + batchNo + "%" + outBarcodeInfo.getPackqty() + "%" + 2;
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
            printInfo.setPrintType(PrintType.PRINT_TYPE_PALLET_STYLE);
        }
        return printInfo;


    }


    public void onReset(boolean isAllReset) {
        if (isAllReset){
            mHeaderInfo = null;
            mOrderDetailList.clear();
        }
        setOrderRequestInfo(null);
        setAreaInfo(null);
        mCurrentPalletInfo = null;
        mBarCodeInfos.clear();
//        if (mBusinessType==InStockReturnsStorageScanModel.IN_STOCK_RETURN_TYPE_ACTIVE){
//
//        }
    }

    /**
     * @desc: 获取扫描外箱码相对应的物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/1 14:16
     */
    public BaseMultiResultInfo<Boolean, Void> findOutBarcodeInfoFromMaterial(OutBarcodeInfo info, float qty) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        OrderDetailInfo sMaterialInfo = null;
        String barcodeMaterialNo = info.getMaterialno() != null ? info.getMaterialno() : "";
//        String barcodeBatchNo = info.getBatchno() != null ? info.getBatchno() : "";
        try {


            for (int i = 0; i < mOrderDetailList.size(); i++) {
                OrderDetailInfo materialInfo = mOrderDetailList.get(i);
                if (materialInfo != null) {
                    String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
                    if (materialNo.trim().equals(barcodeMaterialNo.trim())) {
                        sMaterialInfo = materialInfo;
                        break;
                    }
                }
            }

            if (sMaterialInfo != null) {
                resultInfo.setHeaderStatus(true);
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("物料号:[" + barcodeMaterialNo + "]不在订单：" + mHeaderInfo.getErpvoucherno() + "中,不能组托");
                return resultInfo;
            }


        } catch (Exception e) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("获取物料信息出现意料之外的异常:" + e.getMessage());
        }

        return resultInfo;

    }

    /**
     * @desc: 在有源单据中查询物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/22 10:35
     */
    public boolean hasMaterialInOrderDetail(OutBarcodeInfo info) {
        String barcodeMaterialNo = info.getMaterialno() != null ? info.getMaterialno() : "";
        boolean HAS_MATERIAL = false;
        for (int i = 0; i < mOrderDetailList.size(); i++) {
            OrderDetailInfo materialInfo = mOrderDetailList.get(i);
            if (materialInfo != null) {
                String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
                if (materialNo.trim().equals(barcodeMaterialNo.trim())) {
                    HAS_MATERIAL = true;
                    break;
                }
            }
        }
        return HAS_MATERIAL;
    }

    /**
     * @desc: 校验外箱码数量
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/22 11:14
     */
    public BaseMultiResultInfo<Boolean, Void> checkOuterBoxQty(OutBarcodeInfo materialInfo, float qty) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        resultInfo.setHeaderStatus(false);
        float sumQty = 0;
        boolean HAS_MATERIAL=false;
        String barcodeMaterialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
        if (qty <= 0) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("校验物料数量失败:数量必须大于0");
            return resultInfo;
        }
        if (mBusinessType==IN_STOCK_RETURN_TYPE_ACTIVE){
            for (int i = 0; i < mOrderDetailList.size(); i++) {
                OrderDetailInfo detailInfo = mOrderDetailList.get(i);
                if (detailInfo != null) {
                    String materialNo = detailInfo.getMaterialno() != null ? detailInfo.getMaterialno() : "";
                    if (materialNo.trim().equals(barcodeMaterialNo.trim())) {
                        sumQty = ArithUtil.add(sumQty, detailInfo.getRemainqty());
                        if (HAS_MATERIAL!=true){
                            HAS_MATERIAL=true;
                        }
                    }
                }
            }

            if (!HAS_MATERIAL){
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验物料数量失败:物料【"+barcodeMaterialNo+"】不在单据中");
                return resultInfo;
            }

            float subQty = ArithUtil.sub(sumQty, qty);
            if (subQty >= 0){
                resultInfo.setHeaderStatus(true);
            }else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验物料数量失败:物料【"+barcodeMaterialNo+"】的数量【"+qty+"】超出订单的剩余收货数量【"+sumQty+"】--"+Math.abs(subQty));
                return resultInfo;
            }
        }

            return resultInfo;
    }

    /**
     * @desc: 找到扫描条码的所在行
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 11:01
     */
    @Override
    public BaseMultiResultInfo<Boolean, List<OrderDetailInfo>> findMaterialInfoList(OutBarcodeInfo scanBarcode) {
        BaseMultiResultInfo<Boolean, List<OrderDetailInfo>> resultInfo = new BaseMultiResultInfo<>();
        List<OrderDetailInfo> sMaterialInfoList = new ArrayList<>();
        String barcodeMaterialNo = scanBarcode.getMaterialno() != null ? scanBarcode.getMaterialno() : "";
        String barcodeStrongHoldCode = scanBarcode.getStrongholdcode() != null ? scanBarcode.getStrongholdcode() : "";
        float barcodeQty = scanBarcode.getQty();
        try {

            if (barcodeMaterialNo == null || barcodeMaterialNo.equals("")) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的物料信息不能为空");
                return resultInfo;
            }

            if (barcodeQty < 0) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的数量必须大于等于0");
                return resultInfo;
            }
            //查找和条码的 物料和批次匹配的订单明细行的数据，只查第一个
            for (int i = 0; i < mOrderDetailList.size(); i++) {
                OrderDetailInfo materialInfo = mOrderDetailList.get(i);
                if (materialInfo != null) {
                    String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
                    if (materialNo.trim().equals(barcodeMaterialNo.trim())) {
                        List<OutBarcodeInfo> list = new ArrayList<>();
                        list.add(scanBarcode);
                        materialInfo.setLstBarCode(list);
                        sMaterialInfoList.add(materialInfo);

                    }
                }
            }

            if (sMaterialInfoList != null && sMaterialInfoList.size() > 0) {
                String strongHoldCode = sMaterialInfoList.get(0).getStrongholdcode() != null ? sMaterialInfoList.get(0).getStrongholdcode() : "";
                if (!strongHoldCode.equals(barcodeStrongHoldCode)) {
                    resultInfo.setHeaderStatus(false);
                    resultInfo.setMessage("校验条码失败:条码的组织编码[" + barcodeStrongHoldCode + "]和订单的组织编码[" + strongHoldCode + "]不一致");
                    return resultInfo;
                }

                resultInfo.setHeaderStatus(true);
                resultInfo.setInfo(sMaterialInfoList);
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

}
