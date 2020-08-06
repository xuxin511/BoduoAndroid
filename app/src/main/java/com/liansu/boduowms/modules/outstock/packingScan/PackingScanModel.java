package com.liansu.boduowms.modules.outstock.packingScan;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelListToJsonArray;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/28.
 */
public class PackingScanModel extends BaseModel {
    public List<OutStockOrderDetailInfo> mOrderDetailList    = new ArrayList<>();
    public List<OutStockOrderDetailInfo> mScanDetailList     = new ArrayList<>();
    public OutBarcodeInfo                mCurrentBarcodeInfo = null;

    public PackingScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    protected void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
//            case RESULT_Msg_GetT_InStockDetailListByHeaderIDADF:
//                listener = mNetMap.get("TAG_GetT_InStockDetailListByHeaderIDADF");
//                break;
//            case RESULT_TAG_GET_T_SCAN_BARCODE_ADF_ASYNC:
//                listener = mNetMap.get("TAG_GET_T_SCAN_BARCODE_ADF_ASYNC");
//                break;
//            case RESULT_Msg_GetT_PalletDetailByBarCode:
//                break;
//            case RESULT_Msg_GetAreaModelADF:
//                listener = mNetMap.get("TAG_GetAreaModelADF");
//                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;

        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    /**
     * @desc: 获取订单列表
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/27 17:38
     */
    protected void setOrderDetailList(List<OutStockOrderDetailInfo> orderDetailList) {
        mOrderDetailList.clear();
        if (orderDetailList != null && orderDetailList.size() > 0) {
            mOrderDetailList.addAll(orderDetailList);
        }
    }

    protected List<OutStockOrderDetailInfo> getOrderDetailList() {
        return mOrderDetailList;
    }

    /**
     * @desc: 当前扫描信息类
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/28 10:57
     */
    public void setCurrentBarcodeInfo(OutBarcodeInfo outBarcodeInfo) {
        mCurrentBarcodeInfo = outBarcodeInfo;
    }

    public OutBarcodeInfo getCurrentBarcodeInfo(){
        return  mCurrentBarcodeInfo;
    }
    /**
     * @desc: 获取采购订单表体明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestOrderDetail(OutStockOrderHeaderInfo orderHeaderInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_PurchaseOrderListADFAsync", callBackListener);
        String modelJson = parseModelToJson(orderHeaderInfo);
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PurchaseOrderListADFAsync, mContext.getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), mContext, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null, UrlInfo.getUrl().GetT_PurchaseOrderListADFAsync, modelJson, null);
    }

    /**
     * @desc: 提交条码信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/27 18:45
     */
    public void requestReferBarcodeInfo(OutBarcodeInfo outBarcodeInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_PurchaseOrderListADFAsync", callBackListener);
        String modelJson = parseModelToJson(outBarcodeInfo);
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PurchaseOrderListADFAsync, mContext.getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), mContext, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null, UrlInfo.getUrl().GetT_PurchaseOrderListADFAsync, modelJson, null);
    }

    /**
     * @desc: 非库存打印
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/27 18:45
     */
    public void requestUnStockPackingPrintInfo(OutBarcodeInfo outBarcodeInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_PurchaseOrderListADFAsync", callBackListener);
        String modelJson = parseModelToJson(outBarcodeInfo);
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PurchaseOrderListADFAsync, mContext.getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), mContext, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null, UrlInfo.getUrl().GetT_PurchaseOrderListADFAsync, modelJson, null);
    }



    /**
     * @desc: 物流码打印
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/27 18:45
     */
    public void requestPrintLCLLabel(List<OutStockOrderDetailInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_PurchaseOrderListADFAsync", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PurchaseOrderListADFAsync, mContext.getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), mContext, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null, UrlInfo.getUrl().GetT_PurchaseOrderListADFAsync, modelJson, null);
    }
    /**
     * @desc: 校验物料是否在订单中  ,用于外箱校验
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/27 19:40
     */
    public BaseMultiResultInfo<Boolean, Void> isMaterialNoInOrderDetail(String materialNo) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        boolean HAS_MATERIAL = false;
        if (materialNo == null || materialNo.equals("")) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("校验条码失败:条码的物料信息不能为空");
            return resultInfo;
        }
        for (int i = 0; i < mOrderDetailList.size(); i++) {
            OutStockOrderDetailInfo materialInfo = mOrderDetailList.get(i);
            if (materialInfo != null) {
                String sMaterialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
                if (sMaterialNo.trim().equals(materialNo.trim())) {
                    HAS_MATERIAL = true;
                    break;
                }
            }
        }
        if (!HAS_MATERIAL) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("校验条码失败:条码的物料编号不在订单中");
            return resultInfo;
        }
        resultInfo.setHeaderStatus(true);
        return resultInfo;


    }


    /**
     * @desc: 校验物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/1 14:16
     */
    public BaseMultiResultInfo<Boolean, Void> checkMaterialInfo(OutStockOrderDetailInfo detailInfo) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        boolean HAS_MATERIAL = false;
        String barcodeMaterialNo = detailInfo.getMaterialno();
        String barcodeBatchNo = detailInfo.getBatchno() != null ? detailInfo.getBatchno() : "";
        float barcodeQty = detailInfo.getScanqty();
        try {
            //校验物料
            BaseMultiResultInfo<Boolean, Void> checkMaterialResult = isMaterialNoInOrderDetail(barcodeMaterialNo);
            if (!checkMaterialResult.getHeaderStatus()) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage(checkMaterialResult.getMessage());
                return resultInfo;
            }

            //校验数量
            if (detailInfo.getScanqty() < 0) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的数量必须大于0");
                return resultInfo;
            }

            //校验批次
            if (barcodeBatchNo.equals("")) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的批次不能为空");
                return resultInfo;
            }

        } catch (Exception e) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("获取物料信息出现意料之外的异常:" + e.getMessage());
        }

        return resultInfo;
    }


    /**
     * @desc: 更新物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/27 19:31
     */
    public BaseMultiResultInfo<Boolean, Void> updateMaterialInfo(OutStockOrderDetailInfo detailInfo) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        try {
            OutStockOrderDetailInfo sMaterialInfo = null;
            if (detailInfo != null) {
                String barcodeMaterialNo = detailInfo.getMaterialno() != null ? detailInfo.getMaterialno() : "";
                String barcodeMaterialDesc = detailInfo.getMaterialdesc() != null ? detailInfo.getMaterialdesc() : "";
                String barcodeBatchNo = detailInfo.getBatchno() != null ? detailInfo.getBatchno() : "";
                float barcodeQty = detailInfo.getScanqty();
                //查询扫描的物料批次是否在扫描明细中有，
                for (int i = 0; i < mScanDetailList.size(); i++) {
                    OutStockOrderDetailInfo materialInfo = mScanDetailList.get(i);
                    if (materialInfo != null) {
                        String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
                        String batchNo = materialInfo.getBatchno() != null ? materialInfo.getBatchno() : "";
                        if (materialNo.trim().equals(barcodeMaterialNo.trim()) && batchNo.trim().equals(barcodeBatchNo.trim())) {
                            sMaterialInfo = materialInfo;
                            break;
                        }
                    }
                }

                if (sMaterialInfo == null) {  //没有就创建并赋值
                    if (!barcodeBatchNo.equals("") && barcodeQty != 0) {
                        OutStockOrderDetailInfo createMaterialInfo = new OutStockOrderDetailInfo();
                        createMaterialInfo.setMaterialno(barcodeMaterialNo);
                        createMaterialInfo.setMaterialdesc(barcodeMaterialDesc);
                        createMaterialInfo.setBatchno(barcodeBatchNo);
                        createMaterialInfo.setScanqty(barcodeQty);
                        sMaterialInfo = createMaterialInfo;
                        mScanDetailList.add(createMaterialInfo);
                        resultInfo.setHeaderStatus(true);

                    } else {
                        resultInfo.setHeaderStatus(false);
                        resultInfo.setMessage("获取物料信息出现意料之外的异常：无法自动创建没有批次和数量的物料信息");
                        return resultInfo;
                    }
                } else {
                    //有就更新数量
                    float qty = sMaterialInfo.getScanqty();
                    sMaterialInfo.setScanqty(ArithUtil.add(qty, barcodeQty));
                    resultInfo.setHeaderStatus(true);

                }


            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("更新条码失败,传入的数据为空");
            }

        } catch (Exception e) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("绑定条码出现意料之外的异常：" + e.getMessage());
        }
        return resultInfo;
    }


}
