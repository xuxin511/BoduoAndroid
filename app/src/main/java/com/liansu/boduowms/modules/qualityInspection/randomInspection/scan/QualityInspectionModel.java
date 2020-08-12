package com.liansu.boduowms.modules.qualityInspection.randomInspection.scan;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoice;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/28.
 */
public class QualityInspectionModel extends BaseModel {
    public String TAG_GetAreaModelADF                    = "ReceiptionScan_GetAreaModelADF";
    public String TAG_GET_CHECK_QUALITY_DETAIL_LIST_SYNC = "QualityInspectionModel_GetT_CheckQualityDetailListsync"; //抽检表体
    public String TAG_POST_CHECK_QUALITY_SYNC            = "QualityInspectionModel_PostT_CheckQualitysync"; //抽检表体
    public String TAG_CHECK_T_PALLET_BARCODE_SYNC        = "QualityInspectionModel_CheckT_PalletBarcodesync"; //检验托盘号是否待检
    public String TAG_GET_T_PURCHASE_ORDER_LIST_ADF      = "QualityInspectionModel_GetT_PurchaseOrderListADF"; //取样扫描接口

    private final int               RESULT_AREA_NO_INFO                           = 107;
    private final int               RESULT_TAG_GET_CHECK_QUALITY_DETAIL_LIST_SYNC = 119;
    private final int               RESULT_TAG_POST_CHECK_QUALITY_SYNC            = 120;
    private final int               RESULT_TAG_CHECK_T_PALLET_BARCODE_SYNC        = 121;
    private final int               RESULT_TAG_GET_T_PURCHASE_ORDER_LIST_ADF      = 122;
    private       QualityHeaderInfo mOrderHeaderInfo;  //选择的检验单列表item 单号信息
    private       AreaInfo          mAreaInfo;
    private       QualityHeaderInfo mCurrentDetailInfo; //当前抽检单表体
    private       StockInfo         mCurrentBarcode;  //当前条码
    private       List<StockInfo>   mCurrentBarCodeList                           = new ArrayList<>();

    public QualityInspectionModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }


    @Override
    protected void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_AREA_NO_INFO:
                listener = mNetMap.get("TAG_BarCodeInfo_Stock_Query");
                break;
            case RESULT_TAG_GET_CHECK_QUALITY_DETAIL_LIST_SYNC:
                listener = mNetMap.get("TAG_GET_CHECK_QUALITY_DETAIL_LIST_SYNC");
                break;
            case RESULT_TAG_POST_CHECK_QUALITY_SYNC:
                listener = mNetMap.get("TAG_POST_CHECK_QUALITY_SYNC");
                break;
            case RESULT_TAG_CHECK_T_PALLET_BARCODE_SYNC:
                listener = mNetMap.get("TAG_CHECK_T_PALLET_BARCODE_SYNC");
                break;
            case RESULT_TAG_GET_T_PURCHASE_ORDER_LIST_ADF:
                listener = mNetMap.get("TAG_GET_T_PURCHASE_ORDER_LIST_ADF");
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    public void setOrderHeaderInfo(QualityHeaderInfo orderHeaderInfo) {
        mOrderHeaderInfo = orderHeaderInfo;
    }

    public QualityHeaderInfo getOrderHeaderInfo() {
        return mOrderHeaderInfo;
    }

    public AreaInfo getAreaInfo() {
        return mAreaInfo;
    }

    public void setAreaInfo(AreaInfo info) {
        mAreaInfo = info;
    }

    public void setCurrentDetailInfo(QualityHeaderInfo info) {
        mCurrentDetailInfo = info;
    }

    public QualityHeaderInfo getCurrentDetailInfo() {
        return mCurrentDetailInfo;
    }

    public void setCurrentBarcodeInfo(StockInfo info) {
        mCurrentBarcode = info;
    }

    public StockInfo getCurrentBarcodeInfo() {
        return mCurrentBarcode;
    }

    public List<StockInfo> getCurrentBarCodeList() {
        return mCurrentBarCodeList;
    }


    /**
     * @desc: 获取抽检表体数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 17:52
     */
    public void requestOrderDetailInfo(QualityHeaderInfo headerInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_CHECK_QUALITY_DETAIL_LIST_SYNC", callBackListener);
        String modelJson = GsonUtil.parseModelToJson(headerInfo);
        LogUtil.WriteLog(BaseOrderBillChoice.class, TAG_GET_CHECK_QUALITY_DETAIL_LIST_SYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_CHECK_QUALITY_DETAIL_LIST_SYNC, mContext.getString(R.string.Msg_GetT_InStockListADF), mContext, mHandler, RESULT_TAG_GET_CHECK_QUALITY_DETAIL_LIST_SYNC, null, UrlInfo.getUrl().GetT_CheckQualityDetailListsync, modelJson, null);

    }

    /**
     * @desc: 请求库位信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 15:42
     */
    public void requestAreaInfo(String areaNo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetAreaModelADF", callBackListener);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("AreaNo", areaNo);
//        params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.mCurrentUserInfo));
        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetAreaModelADF, areaNo);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetAreaModelADF, mContext.getString(R.string.Msg_GetAreaModelADF), mContext, mHandler, RESULT_AREA_NO_INFO, null, URLModel.GetURL().GetAreaModelADF, params, null);

    }

    /**
     * @desc: 查询条码信息带出托盘号 库存  新 2020-8-10
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/20 21:06
     */
    public void requestPalletInfoFromStockQuery2(OutBarcodeInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_PURCHASE_ORDER_LIST_ADF", callBackListener);
        String modelJson=parseModelToJson(info);
        LogUtil.WriteLog(QualityInspection.class, TAG_GET_T_PURCHASE_ORDER_LIST_ADF, parseModelToJson(info));
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_PURCHASE_ORDER_LIST_ADF, "正在查询托盘信息...", mContext, mHandler, RESULT_TAG_GET_T_PURCHASE_ORDER_LIST_ADF, null, UrlInfo.getUrl().GetT_ScanStockADFAsync, modelJson, null);

    }


    /**
     * @desc: 查询条码信息带出托盘号
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/20 21:06
     */
    public void requestPalletInfoFromStockQuery(String barcode, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_CHECK_T_PALLET_BARCODE_SYNC", callBackListener);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("barcode", barcode);  //条码
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_CHECK_T_PALLET_BARCODE_SYNC, "正在查询托盘信息...", mContext, mHandler, RESULT_TAG_CHECK_T_PALLET_BARCODE_SYNC, null, UrlInfo.getUrl().CheckT_PalletBarcodesync, params, null);

    }
    /**
     * @desc: 抽检提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestReferInfo(QualityHeaderInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_POST_CHECK_QUALITY_SYNC", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(QualityInspection.class, TAG_POST_CHECK_QUALITY_SYNC, parseModelToJson(info));
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_POST_CHECK_QUALITY_SYNC, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_TAG_POST_CHECK_QUALITY_SYNC, null, UrlInfo.getUrl().PostT_CheckQualitysync, modelJson, null);
    }


    /**
     * @desc: 找到扫描条码的所在行，条码的物料和信息必须在单据中
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 11:01
     */
    public BaseMultiResultInfo<Boolean, QualityHeaderInfo> findAndCheckMaterialInfo(StockInfo scanBarcode) {
        BaseMultiResultInfo<Boolean, QualityHeaderInfo> resultInfo = new BaseMultiResultInfo<>();
        QualityHeaderInfo sMaterialInfo = null;
        String barcodeMaterialNo = scanBarcode.getMaterialno() != null ? scanBarcode.getMaterialno() : "";
        String barcodeBatchNo = scanBarcode.getBatchno() != null ? scanBarcode.getBatchno() : "";
        String barcodeQualityNo=scanBarcode.getQualityno()!=null?scanBarcode.getQualityno():"";
        float barcodeQty = scanBarcode.getQty();
        String barcodeSerialNo = scanBarcode.getSerialno() != null ? scanBarcode.getSerialno() : "";
        String barcodeStrongHoldCode = scanBarcode.getStrongholdcode() != null ? scanBarcode.getStrongholdcode() : "";
        String barcodeToWareHouseNo = scanBarcode.getTowarehouseno() != null ? scanBarcode.getTowarehouseno() : "";
        try {

            if (barcodeMaterialNo == null || barcodeMaterialNo.equals("")) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的物料信息不能为空");
                return resultInfo;
            }
            if (barcodeBatchNo == null || barcodeBatchNo.equals("")) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的批次编号不能为空");
                return resultInfo;
            }
            if (barcodeQty < 0) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的数量必须大于0");
                return resultInfo;
            }
//            if (barcodeSerialNo.equals("")) {
//                resultInfo.setHeaderStatus(false);
//                resultInfo.setMessage("校验条码失败:托盘序列号不能为空");
//                return resultInfo;
//            }
            if (mOrderHeaderInfo != null) {
                String materialNo = mOrderHeaderInfo.getMaterialno() != null ? mOrderHeaderInfo.getMaterialno() : "";
                String batchNo = mOrderHeaderInfo.getBatchno() != null ? mOrderHeaderInfo.getBatchno() : "";
                if (materialNo.trim().equals(barcodeMaterialNo.trim()) && batchNo.trim().equals(barcodeBatchNo.trim())) {
                    sMaterialInfo = mOrderHeaderInfo;
                }

            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验单据表体信息失败:单据信息不能为空");
                return resultInfo;
            }


            if (sMaterialInfo != null) {
                String strongHoldCode = mOrderHeaderInfo.getStrongholdcode() != null ? mOrderHeaderInfo.getStrongholdcode() : "";
                String toWareHouseNo = mOrderHeaderInfo.getTowarehouseno() != null ? mOrderHeaderInfo.getTowarehouseno() : "";
                String qualityNo=mOrderHeaderInfo.getQualityno()!=null?mOrderHeaderInfo.getQualityno():"";
                if (!strongHoldCode.trim().equals(barcodeStrongHoldCode.trim())) {
                    resultInfo.setHeaderStatus(false);
                    resultInfo.setMessage("校验条码失败:条码的组织编码[" + barcodeStrongHoldCode + "]和单据的组织编号[" + strongHoldCode + "]不符");
                    return resultInfo;

                }
                if (!toWareHouseNo.trim().equals(barcodeToWareHouseNo)) {
                    resultInfo.setHeaderStatus(false);
                    resultInfo.setMessage("校验条码失败:条码的仓库编码[" + barcodeToWareHouseNo + "]和单据的仓库编号[" + toWareHouseNo + "]不符");
                    return resultInfo;

                }
                if (!qualityNo.trim().equals(barcodeQualityNo)) {
                    resultInfo.setHeaderStatus(false);
                    resultInfo.setMessage("校验条码失败:条码的之间单号编码[" + barcodeQualityNo + "]和单据的质检单号[" + qualityNo + "]不符");
                    return resultInfo;

                }
                resultInfo.setHeaderStatus(true);
                resultInfo.setInfo(sMaterialInfo);
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:单据中没有物料号为:[" + barcodeMaterialNo + "],批次为:[" + barcodeBatchNo + "]的物料行,扫描的条码序列号为:" + scanBarcode.getBarcode());
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
     * @desc: 添加扫描数量
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/21 17:13
     */
    public BaseMultiResultInfo<Boolean, Void> setBarcodeInfo(StockInfo scanBarcode, float scanQty, QualityHeaderInfo qualityHeaderInfo) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        String barcodeMaterialNo = scanBarcode.getMaterialno() != null ? scanBarcode.getMaterialno() : "";
        String barcodeBatchNo = scanBarcode.getBatchno() != null ? scanBarcode.getBatchno() : "";
        String barcodeSerialNo = scanBarcode.getSerialno() != null ? scanBarcode.getSerialno() : "";
        String barcodeQualityNo=scanBarcode.getQualityno()!=null?scanBarcode.getQualityno():"";
        float barcodeQty = scanBarcode.getQty();
        float sumScannedQty = 0;
//        float voucherQty = qualityHeaderInfo.getVoucherqty();
        float remainQty = qualityHeaderInfo.getRemainqty();
        StockInfo scanInfo = null;
        for (StockInfo info : mCurrentBarCodeList) {
            String materialNo = info.getMaterialno() != null ? info.getMaterialno() : "";
            String batchNo = info.getBatchno() != null ? info.getBatchno() : "";
            String serialNo = info.getSerialno() != null ? info.getSerialno() : "";
            String qualityNo =info.getQualityno()!=null?info.getQualityno():"";
            final float qty = info.getQty();
            if (materialNo.trim().equals(barcodeMaterialNo.trim()) && batchNo.trim().equals(barcodeBatchNo.trim()) && barcodeSerialNo.equals(serialNo)) {
                scanInfo = info;
                sumScannedQty = ArithUtil.add(sumScannedQty, qty);
            }
        }
        //校验条码数量不能大于订单数量
        float sumWillScannedQty = ArithUtil.add(sumScannedQty, scanQty);
        if (sumWillScannedQty > remainQty) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("校验条码失败:条码" + scanBarcode.getBarcode() + "扫描数量[" + scanQty + "]不能大于剩余扫描数量,[" + 0 + "]");
            return resultInfo;
        }
        // 扫描的标签条码已存在
        if (scanInfo != null) {
            float qty = scanInfo.getQty();
            float sumSameBatchNoQty = ArithUtil.add(qty, scanQty);
//            resultInfo.setHeaderStatus(true);
//            scanInfo.setQty(sumSameBatchNoQty);
//            return resultInfo;
            //校验条码不能超过最大外箱数量
            if (sumSameBatchNoQty > barcodeQty) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码" + scanBarcode.getBarcode() + "超出标签最大数量[" + barcodeQty + "],且已扫描数量为[" + qty + "]");
                return resultInfo;
            } else {
                resultInfo.setHeaderStatus(true);
                scanInfo.setQty(sumSameBatchNoQty);
                return resultInfo;
            }
        } else {
//            mCurrentBarCodeList.add(scanBarcode);
//            resultInfo.setHeaderStatus(true);
//            return resultInfo;
            if (scanQty > barcodeQty) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码" + scanQty + "超出标签最大数量[" + barcodeQty + "]");
                return resultInfo;
            } else {
                resultInfo.setHeaderStatus(true);
                mCurrentBarCodeList.add(scanBarcode);
                return resultInfo;
            }

        }


    }

    public float getScannedQty() {
        float sumQty = 0;
        for (StockInfo info : mCurrentBarCodeList) {
            sumQty = ArithUtil.add(sumQty, info.getQty());

        }
        return sumQty;
    }

    public void onReset(){
        mCurrentBarcode=null;
    }
}
