package com.liansu.boduowms.modules.print.LabelReprint;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScanModel;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class LabelReprintModel extends BaseOrderScanModel {
    String TAG_GetT_InStockDetailListByHeaderIDADF = "ReceiptionScan_GetT_InStockDetailListByHeaderIDADF";
    String TAG_GetT_PalletDetailByBarCodeADF       = "ReceiptionScan_GetT_PalletDetailByBarCodeADF";
    String TAG_SaveT_InStockDetailADF              = "ReceiptionScan_SaveT_InStockDetailADF";
    private final int                  RESULT_Msg_GetT_InStockDetailListByHeaderIDADF = 101;
    private final int                  RESULT_Msg_GetT_PalletDetailByBarCode          = 102;
    private final int                  RESULT_Msg_SaveT_InStockDetailADF              = 103;
    private final int                  RESULT_Msg_GetAreaModelADF                     = 104;
    private       List<OutBarcodeInfo> mMaterialList                                  = new ArrayList<>();
    private       OutBarcodeInfo       mCurrentBarcodeInfo;
    private     List<String>   mBatchNoList=new ArrayList<>();
    public LabelReprintModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_Msg_GetT_InStockDetailListByHeaderIDADF:
                listener = mNetMap.get("TAG_GetT_InStockDetailListByHeaderIDADF");
                break;
            case RESULT_Msg_GetT_PalletDetailByBarCode:

                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
        super.onHandleMessage(msg);
    }

    /**
     * @desc: 获取已扫描的物料集合
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/18 20:58
     */
    public List<OutBarcodeInfo> getMaterialList() {
        return mMaterialList;
    }


    public void setMaterialInfo(OutBarcodeInfo outBarcodeInfo) {
        mCurrentBarcodeInfo = outBarcodeInfo;
    }

    public OutBarcodeInfo getMaterialInfo() {
        return mCurrentBarcodeInfo;
    }


     public  void setBatchNoList(List<String> batchNoList){
        mBatchNoList.clear();
        if (batchNoList!=null && batchNoList.size()>0){
            mBatchNoList.addAll(batchNoList);
        }
     }

     public List<String>  getBatchNoList(){
        return  mBatchNoList;
     }
    /**
     * @desc: 组托并提交入库
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestCombineAndReferPallet(OrderDetailInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_PalletDetailByBarCodeADF", callBackListener);
//        final Map<String, String> params = new HashMap<String, String>();
//        params.put("BarCode", parseModelToJson(info));
////        params.put("UserJson", parseModelToJson(BaseApplication.mCurrentUserInfo));
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_PalletDetailByBarCodeADF, parseModelToJson(info));
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCodeADF, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_Msg_GetT_PalletDetailByBarCode, null, URLModel.GetURL().GetT_PalletDetailByBarCodeADF, params, null);

    }

    /**
     * @desc: 组托并提交入库
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestOrderRefer(List<OutBarcodeInfo> materialList, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_PalletDetailByBarCodeADF", callBackListener);
//        final Map<String, String> params = new HashMap<String, String>();
//        params.put("BarCode", parseModelToJson(info));
////        params.put("UserJson", parseModelToJson(BaseApplication.mCurrentUserInfo));
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_PalletDetailByBarCodeADF, parseModelToJson(info));
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCodeADF, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_Msg_GetT_PalletDetailByBarCode, null, URLModel.GetURL().GetT_PalletDetailByBarCodeADF, params, null);

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
        LogUtil.WriteLog(BaseOrderScan.class, TAG_COMBINE_AND_REFER_PALLET_SUB, areaNo);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetAreaModelADF, mContext.getString(R.string.Msg_GetAreaModelADF), mContext, mHandler, RESULT_Msg_GetAreaModelADF, null, URLModel.GetURL().GetAreaModelADF, params, null);

    }


    protected PrintInfo getPrintModel(OutBarcodeInfo outBarcodeInfo,String type) {
        PrintInfo printInfo = null;
        if (outBarcodeInfo != null) {
            printInfo = new PrintInfo();
            String materialNo = outBarcodeInfo.getMaterialno() != null ? outBarcodeInfo.getMaterialno() : "";
            String batchNo = outBarcodeInfo.getBatchno() != null ? outBarcodeInfo.getBatchno() : "";
            int barcodeQty = (int) outBarcodeInfo.getQty();
            String QRBarcode = materialNo + "%" + batchNo + "%" + barcodeQty + "%" + 2;
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
            printInfo.setPrintType(type);
        }
        return printInfo;
    }

    /**
     * @desc: 获取物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestMaterialInfo(OutBarcodeInfo barcode, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_PurchaseOrderListADFAsync", callBackListener);
//        String modelJson = parseModelToJson(receiptModel);
//        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PurchaseOrderListADFAsync, mContext.getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), mContext, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null, UrlInfo.getUrl().GetT_PurchaseOrderListADFAsync, modelJson, null);
    }

    /**
     * @desc: 获取订单表体明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestOrderDetail(String receiptModel, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_PurchaseOrderListADFAsync", callBackListener);
        String modelJson = parseModelToJson(receiptModel);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_GetT_InStockDetailListByHeaderIDADF, modelJson);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PurchaseOrderListADFAsync, mContext.getString(R.string.Msg_GetT_InStockDetailListByHeaderIDADF), mContext, mHandler, RESULT_Msg_GetT_InStockDetailListByHeaderIDADF, null, UrlInfo.getUrl().GetT_PurchaseOrderListADFAsync, modelJson, null);
    }

    /**
     * @desc: 找到扫描条码的所在行
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 11:01
     */

    public BaseMultiResultInfo<Boolean, OutBarcodeInfo> findScannedMaterialInfo(OutBarcodeInfo scanBarcode) {
        BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = new BaseMultiResultInfo<>();
        OutBarcodeInfo sMaterialInfo = null;
        String barcodeMaterialNo = scanBarcode.getMaterialno() != null ? scanBarcode.getMaterialno() : "";
        String barcodeBatchNo = scanBarcode.getBatchno() != null ? scanBarcode.getBatchno() : "";
        String barcodeAreaNo = scanBarcode.getAreano() != null ? scanBarcode.getAreano() : "";
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
            for (int i = 0; i < mMaterialList.size(); i++) {
                OutBarcodeInfo materialInfo = mMaterialList.get(i);
                if (materialInfo != null) {
                    String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
                    String batchNo = materialInfo.getBatchno() != null ? materialInfo.getBatchno() : "";
                    String areaNo = materialInfo.getAreano() != null ? materialInfo.getAreano() : "";
                    if (materialNo.trim().equals(barcodeMaterialNo.trim()) && batchNo.trim().equals(barcodeBatchNo.trim()) && areaNo.trim().equals(barcodeAreaNo.trim())) {
                        sMaterialInfo = materialInfo;
                        break;
                    }
                }
            }

            if (sMaterialInfo != null) {
                resultInfo.setHeaderStatus(true);
                resultInfo.setInfo(sMaterialInfo);
            } else {
                OutBarcodeInfo materialInfo = new OutBarcodeInfo();
                materialInfo.setMaterialno(barcodeMaterialNo);
                materialInfo.setBatchno(barcodeBatchNo);
                materialInfo.setQty(barcodeQty);
                materialInfo.setAreano(barcodeAreaNo);
                resultInfo.setHeaderStatus(true);
                resultInfo.setInfo(materialInfo);
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
    public BaseMultiResultInfo<Boolean, Void> checkMaterialInfo(OutBarcodeInfo detailInfo, OutBarcodeInfo scanBarcode, boolean isUpdate) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        String barcodeMaterialNo = scanBarcode.getMaterialno() != null ? scanBarcode.getMaterialno() : "";
        String barcodeBatchNo = scanBarcode.getBatchno() != null ? scanBarcode.getBatchno() : "";
        String barcodeAreaNo = scanBarcode.getAreano() != null ? scanBarcode.getAreano() : "";
        float barcodeScanQty = scanBarcode.getQty();
        if (detailInfo != null) {
            String materialNo = detailInfo.getMaterialno() != null ? detailInfo.getMaterialno() : "";
            String batchNo = detailInfo.getBatchno() != null ? detailInfo.getBatchno() : "";
            String areaNo = detailInfo.getAreano() != null ? detailInfo.getAreano() : "";
            float qty = detailInfo.getQty();
            if (materialNo.trim().equals(barcodeMaterialNo.trim()) && batchNo.trim().equals(barcodeBatchNo.trim()) && areaNo.trim().equals(barcodeAreaNo.trim())) {
                float sumQty = ArithUtil.add(qty, barcodeScanQty);
                if (sumQty > 0) {
                    resultInfo.setHeaderStatus(true);
                } else {
                    resultInfo.setMessage("物料行数量必须大于0");
                    resultInfo.setHeaderStatus(false);
                    return resultInfo;
                }
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("更新物料行失败:没有物料号为:[" + barcodeMaterialNo + "],批次为：[" + barcodeBatchNo + "],库位为:[" + barcodeAreaNo + "]的物料行,扫描的条码序列号为:" + scanBarcode.getBarcode());
                return resultInfo;
            }


        } else {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("绑定条码信息失败:没有找到能匹配条码的物料号:[" + barcodeMaterialNo + "],批次：[" + barcodeBatchNo + "]的物料行");
            return resultInfo;

        }
        return resultInfo;
    }

    /**
     * @desc:重置数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/18 21:07
     */
    public void onReset() {
        mBatchNoList.clear();
        mMaterialList.clear();
        mAreaInfo = null;
    }
}
