package com.liansu.boduowms.modules.instock.baseOrderBusiness.upshelf.scan;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.utils.hander.MyHandler;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class UpShelfScanModel extends BaseModel {
    public UpShelfScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    protected void onHandleMessage(Message msg) {

    }
//    String TAG_GetT_InStockDetailListByHeaderIDADF = "ReceiptionScan_GetT_InStockDetailListByHeaderIDADF";
//    String TAG_GetT_PalletDetailByBarCodeADF       = "ReceiptionScan_GetT_PalletDetailByBarCodeADF";
//    String TAG_SaveT_InStockDetailADF              = "ReceiptionScan_SaveT_InStockDetailADF";
//    String TAG_GetAreaModelADF                     = "ReceiptionScan_GetAreaModelADF";
//    private final int RESULT_Msg_GetT_InStockDetailListByHeaderIDADF = 101;
//    private final int RESULT_Msg_GetT_PalletDetailByBarCode          = 102;
//    private final int RESULT_Msg_SaveT_InStockDetailADF              = 103;
//    private final int RESULT_Msg_GetAreaModelADF                     = 104;
//    ArrayList<OrderDetailInfo>     mReceiptDetailList        = new ArrayList<>();
//    ArrayList<OutBarcodeInfo>      mBarCodeInfos             = new ArrayList<>();
//    OrderHeaderInfo                mReceiptModel             = null;
//    UUID                           mUuid                     = null;
//    //  boolean isDel=false;//删除已扫条码
//    AreaInfo                       mAreaInfoModel            = null;//扫描库位
//    Float                          mSumReaminQty             = 0f; //当前拣货物料剩余拣货数量合计
//    ArrayList<ReceiptDetail_Model> mSameLineInStockTaskDetailsInfoModels; //相同行物料集合
//    int                            mCurrentPickMaterialIndex = -1;
//    private AreaInfo mAreaInfo;
//
//    public UpShelfScanModel(Context context, MyHandler<BaseActivity> handler) {
//        super(context, handler);
//    }
//
//    @Override
//    protected void onHandleMessage(Message msg) {
//        NetCallBackListener<String> listener = null;
//
//        switch (msg.what) {
//            case RESULT_Msg_GetT_InStockDetailListByHeaderIDADF:
//                listener = mNetMap.get("TAG_GetT_InStockDetailListByHeaderIDADF");
//                break;
//            case RESULT_Msg_GetT_PalletDetailByBarCode:
//
//                break;
////            case RESULT_Msg_SaveT_InStockDetailADF:
////
////                break;
//            case RESULT_Msg_GetAreaModelADF:
//                listener = mNetMap.get("TAG_GetAreaModelADF");
//                break;
//            case NetworkError.NET_ERROR_CUSTOM:
//                ToastUtil.show("获取请求失败_____" + msg.obj);
//                break;
//
//        }
//        if (listener != null) {
//            listener.onCallBack(msg.obj.toString());
//        }
//    }
//
//    /**
//     * @desc: 获取采购订单表体明细
//     * @param:
//     * @return:
//     * @author: Nietzsche
//     * @time 2020/6/27 21:37
//     */
//    public void requestReceiptDetail(OrderHeaderInfo receiptModel, NetCallBackListener<String> callBackListener) {
//        mNetMap.put("TAG_GetT_InStockDetailListByHeaderIDADF", callBackListener);
//        final ReceiptDetail_Model receiptDetailModel = new ReceiptDetail_Model();
//        receiptDetailModel.setHeaderID(receiptModel.getId());
//        receiptDetailModel.setErpVoucherNo(receiptModel.getErpvoucherno());
//        receiptDetailModel.setVoucherType(receiptModel.getVouchertype());
//        final Map<String, String> params = new HashMap<String, String>();
//        params.put("ModelDetailJson", parseModelToJson(receiptDetailModel));
//        String para = (new JSONObject(params)).toString();
//        LogUtil.WriteLog(UpShelfScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, para);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InStockDetailListByHeaderIDADF, mContext.getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), mContext, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null, URLModel.GetURL().GetT_InStockDetailListByHeaderIDADF, params, null);
//    }
//
//
//    /**
//     * @desc: 请求库位信息
//     * @param:
//     * @return:
//     * @author: Nietzsche
//     * @time 2020/7/3 15:42
//     */
//    public void requestAreaInfo(String areaNo, NetCallBackListener<String> callBackListener) {
//        mNetMap.put("TAG_GetAreaModelADF", callBackListener);
//        final Map<String, String> params = new HashMap<String, String>();
//        params.put("AreaNo", areaNo);
//        params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.mCurrentUserInfo));
//        LogUtil.WriteLog(UpShelfScan.class, TAG_GetAreaModelADF, areaNo);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetAreaModelADF, mContext.getString(R.string.Msg_GetAreaModelADF), mContext, mHandler, RESULT_Msg_GetAreaModelADF, null, URLModel.GetURL().GetAreaModelADF, params, null);
//
//    }
//
//    /**
//     * @desc: 获取托盘信息
//     * @param:
//     * @return:
//     * @author: Nietzsche
//     * @time 2020/7/3 16:47
//     */
//    public void requestBarcodeInfo(String barcode, NetCallBackListener<String> callBackListener) {
//        mNetMap.put("TAG_GetT_PalletDetailByBarCodeADF", callBackListener);
//        final Map<String, String> params = new HashMap<String, String>();
//        params.put("BarCode", barcode);
//        params.put("UserJson", parseModelToJson(BaseApplication.mCurrentUserInfo));
//        LogUtil.WriteLog(UpShelfScan.class, TAG_GetT_PalletDetailByBarCodeADF, barcode);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCodeADF, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_Msg_GetT_PalletDetailByBarCode, null, URLModel.GetURL().GetT_PalletDetailByBarCodeADF, params, null);
//
//    }
//
//
//    public ArrayList<OrderDetailInfo> getReceiptDetailModels() {
//        return mReceiptDetailList;
//    }
//
//    public void setReceiptDetailModels(ArrayList<OrderDetailInfo> receiptDetailModels) {
//        mReceiptDetailList.clear();
//        if (receiptDetailModels != null && receiptDetailModels.size() > 0) {
//            mReceiptDetailList.addAll(receiptDetailModels);
//        }
//
//    }
//
//    public ArrayList<OutBarcodeInfo> getBarCodeInfos() {
//        return mBarCodeInfos;
//    }
//
//    public void setBarCodeInfos(List<OutBarcodeInfo> barCodeInfos) {
//        mBarCodeInfos.clear();
//        if (barCodeInfos != null && barCodeInfos.size() > 0) {
//            mBarCodeInfos.addAll(barCodeInfos);
//        }
//
//    }
//
//    public OrderHeaderInfo getReceiptModel() {
//        return mReceiptModel;
//    }
//
//    public void setReceiptModel(OrderHeaderInfo receiptModel) {
//        this.mReceiptModel = receiptModel;
//    }
//
//    public AreaInfo getAreaInfo() {
//        return mAreaInfo;
//    }
//
//    public void setAreaInfo(AreaInfo info) {
//        mAreaInfo = info;
//    }
//
//
//    /**
//     * @desc: 更新物料信息
//     * @param:
//     * @return:
//     * @author: Nietzsche
//     * @time 2020/7/5 13:40
//     */
//    public BaseMultiResultInfo<Boolean, Void> updateMaterialInfo(OrderDetailInfo detailInfo, OutBarcodeInfo scanBarcode) {
//        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
//        String barcodeMaterialNo = scanBarcode.getMaterialno() != null ? scanBarcode.getMaterialno() : "";
//        String barcodeBatchNo = scanBarcode.getBatchno() != null ? scanBarcode.getBatchno() : "";
//        String barcodeRowNo = scanBarcode.getRowno() != null ? scanBarcode.getRowno() : "";
//        float barcodeScanQty = scanBarcode.getQty();
//        if (detailInfo != null) {
//            String materialNo = detailInfo.getMaterialno() != null ? detailInfo.getMaterialno() : "";
//            String batchNo = detailInfo.getBatchno() != null ? detailInfo.getBatchno() : "";
//            String rowNo = detailInfo.getRowno() != null ? detailInfo.getRowno() : "";
//            if (materialNo.trim().equals(barcodeMaterialNo.trim()) && batchNo.trim().equals(barcodeBatchNo.trim()) && rowNo.trim().equals(barcodeRowNo.trim())) {
//                float scanQty = detailInfo.getScanqty();
//                float remainQty = detailInfo.getRemainqty(); //订单的剩余扫描数量　　
//                float hasRemainQty = ArithUtil.sub(detailInfo.getRemainqty(), detailInfo.getScanqty()); // (订单减去已扫描条码的)剩余扫描数量
//                if (remainQty > 0) {
//                    if (hasRemainQty > 0) {
//                        if (ArithUtil.sub(hasRemainQty, barcodeScanQty) >= 0) {
//                            detailInfo.setScanqty(ArithUtil.add(scanQty, barcodeScanQty));
//                            resultInfo.setHeaderStatus(true);
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
//                resultInfo.setMessage("更新物料行失败:订单中没有物料号为:[" + barcodeMaterialNo + "],批次为：[" + barcodeBatchNo + "],行号为:[" + barcodeRowNo + "]的物料行,扫描的条码序列号为:" + scanBarcode.getBarcode());
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
//        return resultInfo;
//    }
//
//    /**
//     * @desc: 校验条码重复
//     * @param:
//     * @return:
//     * @author: Nietzsche
//     * @time 2020/7/5 16:01
//     */
//    public BaseMultiResultInfo<Boolean, Void> checkBarcode(OrderDetailInfo orderDetailInfo, OutBarcodeInfo scanBarcode) {
//        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
//        if (orderDetailInfo != null) {
//            //校验条码重复
//            List<OutBarcodeInfo> barcodeList = orderDetailInfo.getLstBarCode();
//            if (barcodeList == null) {
//                barcodeList = new ArrayList<>();
//            }
//            int index = barcodeList.indexOf(scanBarcode);
//            if (index == -1) {
//                resultInfo.setHeaderStatus(true);
//            } else {
//                resultInfo.setHeaderStatus(false);
//                resultInfo.setMessage("校验条码失败:条码编号[" + scanBarcode.getBarcode() + "]已扫描,无需重复扫描");
//                return resultInfo;
//            }
//        } else {
//            resultInfo.setHeaderStatus(false);
//            resultInfo.setMessage("校验条码失败:对比的物料行不能为空");
//            return resultInfo;
//        }
//
//        return resultInfo;
//    }
//
//
//    /**
//     * @desc: 找到扫描条码的所在行
//     * @param:
//     * @return:
//     * @author: Nietzsche
//     * @time 2020/7/5 11:01
//     */
//    public BaseMultiResultInfo<Boolean, OrderDetailInfo> findMaterialInfo(OutBarcodeInfo scanBarcode) {
//        BaseMultiResultInfo<Boolean, OrderDetailInfo> resultInfo = new BaseMultiResultInfo<>();
//        OrderDetailInfo sMaterialInfo = null;
//        String barcodeMaterialNo = scanBarcode.getMaterialno() != null ? scanBarcode.getMaterialno() : "";
//        String barcodeBatchNo = scanBarcode.getBatchno() != null ? scanBarcode.getBatchno() : "";
//        String barcodeRowNo = scanBarcode.getRowno() != null ? scanBarcode.getRowno() : "";
//        String barcodeStrongHoldCode = scanBarcode.getStrongholdcode() != null ? scanBarcode.getStrongholdcode() : "";
//        String barcodeErpVoucherNo = scanBarcode.getErpvoucherno() != null ? scanBarcode.getErpvoucherno() : "";
//        float barcodeQty = scanBarcode.getQty();
//        try {
//
//            if (barcodeMaterialNo == null || barcodeMaterialNo.equals("")) {
//                resultInfo.setHeaderStatus(false);
//                resultInfo.setMessage("校验条码失败:条码的物料信息不能为空");
//                return resultInfo;
//            }
//
//            if (barcodeQty < 0) {
//                resultInfo.setHeaderStatus(false);
//                resultInfo.setMessage("校验条码失败:条码的数量必须大于0");
//                return resultInfo;
//            }
//            //查找和条码的 物料和批次匹配的订单明细行的数据，只查第一个
//            for (int i = 0; i < mReceiptDetailList.size(); i++) {
//                OrderDetailInfo materialInfo = mReceiptDetailList.get(i);
//                if (materialInfo != null) {
//                    String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
//                    String batchNo = materialInfo.getBatchno() != null ? materialInfo.getBatchno() : "";
//                    String rowNo = materialInfo.getRowno() != null ? materialInfo.getRowno() : "";
//                    if (materialNo.trim().equals(barcodeMaterialNo.trim()) && batchNo.trim().equals(barcodeBatchNo.trim()) && rowNo.trim().equals(barcodeRowNo.trim())) {
//                        sMaterialInfo = materialInfo;
//                        break;
//                    }
//                }
//            }
//
//            if (sMaterialInfo != null) {
//                String strongHoldCode = sMaterialInfo.getStrongholdcode() != null ? sMaterialInfo.getStrongholdcode() : "";
//                String erpVoucherNo = sMaterialInfo.getErpvoucherno() != null ? sMaterialInfo.getErpvoucherno() : "";
//                if (!strongHoldCode.equals(barcodeStrongHoldCode)) {
//                    resultInfo.setHeaderStatus(false);
//                    resultInfo.setMessage("校验条码失败:条码的组织编码["+barcodeStrongHoldCode+"]和订单的组织编码["+strongHoldCode+"]不一致");
//                    return resultInfo;
//                }
//
//                if (!erpVoucherNo.equals(barcodeErpVoucherNo)) {
//                    resultInfo.setHeaderStatus(false);
//                    resultInfo.setMessage("校验条码失败:条码的订单号["+barcodeErpVoucherNo+"]不匹配当前订单["+erpVoucherNo+"]");
//                    return resultInfo;
//                }
//                resultInfo.setHeaderStatus(true);
//                resultInfo.setInfo(sMaterialInfo);
//            } else {
//                resultInfo.setHeaderStatus(false);
//                resultInfo.setMessage("校验条码失败:订单中没有物料号为:[" + barcodeMaterialNo + "],批次为：[" + barcodeBatchNo + "],行号为:[" + barcodeRowNo + "]的物料行,扫描的条码序列号为:" + scanBarcode.getBarcode());
//                return resultInfo;
//            }
//        } catch (Exception e) {
//            resultInfo.setHeaderStatus(false);
//            resultInfo.setMessage("校验条码失败:出现预期之外的错误:" + e.getMessage());
//            return resultInfo;
//        }
//
//        return resultInfo;
//    }


}
