package com.liansu.boduowms.modules.qualityInspection.qualityInspectionProcessing;

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
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.upshelf.scan.UpShelfScan;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class QualityInspectionProcessingModel extends BaseModel {
    String TAG_GetT_InStockDetailListByHeaderIDADF = "ReceiptionScan_GetT_InStockDetailListByHeaderIDADF";
    String TAG_GetT_PalletDetailByBarCodeADF       = "ReceiptionScan_GetT_PalletDetailByBarCodeADF";
    String TAG_SaveT_InStockDetailADF              = "ReceiptionScan_SaveT_InStockDetailADF";
    String TAG_GetAreaModelADF                     = "ReceiptionScan_GetAreaModelADF";
    String TAG_GetT_QualityDetailListsync          = "QualityInspectionProcessingModel_GetT_QualityDetailListsync";
    String TAG_PostT_QualityADFAsync               = "QualityInspectionProcessingModel_PostT_QualityADFAsync"; //质检合格提交
    private final int RESULT_Msg_GetT_InStockDetailListByHeaderIDADF = 101;
    private final int RESULT_Msg_GetT_PalletDetailByBarCode          = 102;
    private final int RESULT_Msg_SaveT_InStockDetailADF              = 103;
    private final int RESULT_Msg_GetAreaModelADF                     = 104;
    private final int RESULT_MSG_GET_QUALITY_DETAIL_LIST_SYNC        = 117;
    private final int RESULT_MSG_TAG_POST_QUALITY_ADF_ASYNC         = 118;
    List<QualityHeaderInfo> mQualityInspectionDetailList = new ArrayList<>();
    List<OutBarcodeInfo>    mBarCodeInfos                = new ArrayList<>();
    QualityHeaderInfo       mQualityHeaderInfo           = null;
    UUID                    mUuid                        = null;
    private      AreaInfo mAreaInfo;
    private      int      mOperationType           = -1;
    private      String   mQualityType             = "";
    public static final String   QUALITY_TYPE_QUALIFIED   = "QUALIFIED";
    public static final String   QUALITY_TYPE_UNQUALIFIED = "UNQUALIFIED";

    public QualityInspectionProcessingModel(Context context, MyHandler<BaseActivity> handler) {
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
            case RESULT_MSG_GET_QUALITY_DETAIL_LIST_SYNC:
                listener = mNetMap.get("TAG_GetT_QualityDetailListsync");
                break;
            case RESULT_MSG_TAG_POST_QUALITY_ADF_ASYNC:
                listener = mNetMap.get("TAG_PostT_QualityADFAsync");
                break;

            case RESULT_Msg_GetAreaModelADF:
                listener = mNetMap.get("TAG_GetAreaModelADF");
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
     * @desc: 获取质检表体
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestQualityInspectionDetail(QualityHeaderInfo headerInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_QualityDetailListsync", callBackListener);
        String modelJson = "{\"id\":" + headerInfo.getId() + "}";
        LogUtil.WriteLog(QualityInspectionProcessingScan.class, TAG_GetT_QualityDetailListsync, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_QualityDetailListsync, mContext.getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), mContext, mHandler, RESULT_MSG_GET_QUALITY_DETAIL_LIST_SYNC, null, UrlInfo.getUrl().GetT_QualityDetailListsync, modelJson, null);
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
        LogUtil.WriteLog(QualityInspectionProcessingScan.class, TAG_GetAreaModelADF, areaNo);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetAreaModelADF, mContext.getString(R.string.Msg_GetAreaModelADF), mContext, mHandler, RESULT_Msg_GetAreaModelADF, null, URLModel.GetURL().GetAreaModelADF, params, null);

    }

    /**
     * @desc: 获取托盘信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestBarcodeInfo(String barcode, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_PalletDetailByBarCodeADF", callBackListener);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("BarCode", barcode);
//        params.put("UserJson", parseModelToJson(BaseApplication.mCurrentUserInfo));
        LogUtil.WriteLog(UpShelfScan.class, TAG_GetT_PalletDetailByBarCodeADF, barcode);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCodeADF, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_Msg_GetT_PalletDetailByBarCode, null, URLModel.GetURL().GetT_PalletDetailByBarCodeADF, params, null);

    }

    /**
     * @desc: 组托并提交入库
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestRefer(QualityHeaderInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_PalletDetailByBarCodeADF", callBackListener);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("BarCode", parseModelToJson(info));
//        params.put("UserJson", parseModelToJson(BaseApplication.mCurrentUserInfo));
        LogUtil.WriteLog(QualityInspectionProcessingScan.class, TAG_GetT_PalletDetailByBarCodeADF, parseModelToJson(info));
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCodeADF, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_Msg_GetT_PalletDetailByBarCode, null, URLModel.GetURL().GetT_PalletDetailByBarCodeADF, params, null);

    }

    /**
     * @desc: 质检合格提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/20 15:11
     */
    public void requestQualifiedRefer(QualityHeaderInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_PostT_QualityADFAsync", callBackListener);
        String modelJson= parseModelToJson(info);
        LogUtil.WriteLog(QualityInspectionProcessingScan.class, TAG_PostT_QualityADFAsync, parseModelToJson(info));
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_PostT_QualityADFAsync, mContext.getString(R.string.message_request_quality_inspection_refer), mContext, mHandler, RESULT_MSG_TAG_POST_QUALITY_ADF_ASYNC, null, UrlInfo.getUrl().PostT_QualityADFAsync, modelJson, null);

    }

    public List<QualityHeaderInfo> getQualityInspectionDetailList() {
        return mQualityInspectionDetailList;
    }

    public void setQualityInspectionDetailList(List<QualityHeaderInfo> receiptDetailModels) {
        mQualityInspectionDetailList.clear();
        if (receiptDetailModels != null && receiptDetailModels.size() > 0) {
            mQualityInspectionDetailList.addAll(receiptDetailModels);
        }

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

    public QualityHeaderInfo getQualityHeaderInfo() {
        return mQualityHeaderInfo;
    }

    public void setQualityHeaderInfo(QualityHeaderInfo qualityHeaderInfo) {
        this.mQualityHeaderInfo = qualityHeaderInfo;
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
     * @desc: 更新物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 13:40
     */
    public BaseMultiResultInfo<Boolean, Void> checkMaterialInfo(QualityHeaderInfo detailInfo, OutBarcodeInfo scanBarcode, boolean isUpdate) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
//        String barcodeMaterialNo = scanBarcode.getMaterialno() != null ? scanBarcode.getMaterialno() : "";
//        String barcodeBatchNo = scanBarcode.getBatchno() != null ? scanBarcode.getBatchno() : "";
//        float barcodeScanQty = scanBarcode.getQty();
//        if (detailInfo != null) {
//            String materialNo = detailInfo.getMaterialno() != null ? detailInfo.getMaterialno() : "";
//            String batchNo = detailInfo.getBatchno() != null ? detailInfo.getBatchno() : "";
//            String rowNo = detailInfo.getRowno() != null ? detailInfo.getRowno() : "";
//            if (materialNo.trim().equals(barcodeMaterialNo.trim()) && batchNo.trim().equals(barcodeBatchNo.trim())) {
//                float scanQty = detailInfo.getScanqty();
//                float remainQty = detailInfo.getRemainqty(); //订单的剩余扫描数量　　
//                float hasRemainQty = ArithUtil.sub(detailInfo.getRemainqty(), detailInfo.getScanqty()); // (订单减去已扫描条码的)剩余扫描数量
//                if (remainQty > 0) {
//                    if (hasRemainQty > 0) {
//                        if (ArithUtil.sub(hasRemainQty, barcodeScanQty) >= 0) {
//                            if (isUpdate) {
//                                detailInfo.setScanqty(ArithUtil.add(scanQty, barcodeScanQty));
//                                detailInfo.setRemainqty(ArithUtil.sub(remainQty, barcodeScanQty));
//                                detailInfo.setReceiveqty(ArithUtil.sub(detailInfo.getVoucherqty(), detailInfo.getRemainqty()));
//                            } else {
//                                List<OutBarcodeInfo> list = new ArrayList<>();
//                                list.add(scanBarcode);
//                                detailInfo.setLstBarCode(list);
//                            }
//                            resultInfo.setHeaderStatus(true);
//
//                        } else {
//                            resultInfo.setMessage("校验物料行失败:物料号:[" + barcodeMaterialNo + "],批次：[" + barcodeBatchNo + "],行号:[" + rowNo + "]的物料行的收货数量不能大于剩余收货数量");
//                            resultInfo.setHeaderStatus(false);
//                            return resultInfo;
//                        }
//                    } else {
//                        if (hasRemainQty == 0) {
//                            resultInfo.setMessage("校验物料行失败:物料号:[" + barcodeMaterialNo + "],批次：[" + barcodeBatchNo + "],行号:[" + rowNo + "]的物料行已扫完毕");
//                        } else {
//                            resultInfo.setMessage("校验物料行出现异常:剩余扫描数量不能大于扫描数量!剩余数量[" + detailInfo.getRemainqty() + "],已扫描数量：[" + detailInfo.getScanqty() + "]");
//                        }
//                        resultInfo.setHeaderStatus(false);
//                        return resultInfo;
//                    }
//                } else if (remainQty == 0) {
//                    resultInfo.setMessage("校验物料行失败:物料号:[" + barcodeMaterialNo + "],批次：[" + barcodeBatchNo + "],行号:[" + rowNo + "]的物料行已扫完毕");
//                    resultInfo.setHeaderStatus(false);
//                    return resultInfo;
//                } else {
//                    resultInfo.setMessage("校验物料行出现异常:剩余扫描数量不能为负数!");
//                    resultInfo.setHeaderStatus(false);
//                    return resultInfo;
//                }
//            } else {
//                resultInfo.setHeaderStatus(false);
//                resultInfo.setMessage("更新物料行失败:订单中没有物料号为:[" + barcodeMaterialNo + "],批次为：[" + barcodeBatchNo + "]的物料行,扫描的条码序列号为:" + scanBarcode.getBarcode());
//                return resultInfo;
//            }
//
//
//        } else {
//            resultInfo.setHeaderStatus(false);
//            resultInfo.setMessage("绑定条码信息失败:没有找到能匹配条码的物料号:[" + barcodeMaterialNo + "],批次：[" + barcodeBatchNo + "]的物料行");
//            return resultInfo;
//
//        }
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
     * @desc: 找到扫描条码的所在行
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 11:01
     */
    public BaseMultiResultInfo<Boolean, QualityHeaderInfo> findMaterialInfo(OutBarcodeInfo scanBarcode) {
        BaseMultiResultInfo<Boolean, QualityHeaderInfo> resultInfo = new BaseMultiResultInfo<>();
        QualityHeaderInfo sMaterialInfo = null;
        String barcodeMaterialNo = scanBarcode.getMaterialno() != null ? scanBarcode.getMaterialno() : "";
//        String barcodeBatchNo = scanBarcode.getBatchno() != null ? scanBarcode.getBatchno() : "";
//        String barcodeRowNo = scanBarcode.getRowno() != null ? scanBarcode.getRowno() : "";
//        String barcodeStrongHoldCode = scanBarcode.getStrongholdcode() != null ? scanBarcode.getStrongholdcode() : "";
//        String barcodeErpVoucherNo = scanBarcode.getErpvoucherno() != null ? scanBarcode.getErpvoucherno() : "";
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
            for (int i = 0; i < mQualityInspectionDetailList.size(); i++) {
                QualityHeaderInfo materialInfo = mQualityInspectionDetailList.get(i);
                if (materialInfo != null) {
                    String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
                    String batchNo = materialInfo.getBatchno() != null ? materialInfo.getBatchno() : "";
                    if (materialNo.trim().equals(barcodeMaterialNo.trim())) {
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
//                resultInfo.setMessage("校验条码失败:订单中没有物料号为:[" + barcodeMaterialNo + "],批次为：[" + barcodeBatchNo + "],行号为:[" + barcodeRowNo + "]的物料行,扫描的条码序列号为:" + scanBarcode.getBarcode());
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
     * @desc: 获取扫描外箱码相对应的物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/1 14:16
     */
    public BaseMultiResultInfo<Boolean, Void> findOutBarcodeInfoFromMaterial(OutBarcodeInfo info) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
//        OrderDetailInfo sMaterialInfo = null;
//        String barcodeMaterialNo = info.getMaterialno() != null ? info.getMaterialno() : "";
//        String barcodeBatchNo = info.getBatchno() != null ? info.getBatchno() : "";
//        try {
//
//
//            for (int i = 0; i < mQualityInspectionDetailList.size(); i++) {
//                OrderDetailInfo materialInfo = mQualityInspectionDetailList.get(i);
//                if (materialInfo != null) {
//                    String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
//                    String batchNo = materialInfo.getBatchno() != null ? materialInfo.getBatchno() : "";
//                    if (materialNo.trim().equals(barcodeMaterialNo.trim()) && batchNo.trim().equals(barcodeBatchNo.trim())) {
//                        if (materialInfo.getRemainqty() != 0) {
//                            sMaterialInfo = materialInfo;
//                            info.setUnit(sMaterialInfo.getUnit());
//                            info.setMaterialno(sMaterialInfo.getMaterialno());
//                            info.setMaterialdesc(sMaterialInfo.getMaterialdesc());
//                            info.setErpvoucherno(sMaterialInfo.getErpvoucherno());
//                            info.setStrongholdcode(sMaterialInfo.getStrongholdcode());
//                            info.setStrongholdname(sMaterialInfo.getStrongholdname());
//                            info.setPackQty(sMaterialInfo.getPackQty());
//                            break;
//                        }
//
//
//                    }
//                }
//            }
//
//            if (sMaterialInfo != null) {
//                resultInfo.setHeaderStatus(true);
//            } else {
//                resultInfo.setHeaderStatus(false);
//                resultInfo.setMessage("物料号:[" + barcodeMaterialNo + "],批次:[" + barcodeBatchNo + "]不在订单：" + mQualityHeaderInfo.getErpvoucherno() + "中,不能组托");
//                return resultInfo;
//            }
//
//
//        } catch (Exception e) {
//            resultInfo.setHeaderStatus(false);
//            resultInfo.setMessage("获取物料信息出现意料之外的异常:" + e.getMessage());
//        }

        return resultInfo;
    }

}
