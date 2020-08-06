package com.liansu.boduowms.modules.instock.salesReturn.scan;

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
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScanModel;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.modules.print.linkos.PrintType;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.GsonUtil;
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
public class SalesReturnStorageScanModel extends BaseOrderScanModel {

    public        String TAG_GET_T_AREA_MODEL                         = "SalesReturnStorageScanModel_GetT_AreaModel";  // 获取库位信息
    public        String TAG_GET_T_SCAN_BARCODE_ADF_ASYNC             = "SalesReturnStorageScanModel_GetT_ScanBarcodeADFAsync";  // 获取条码信息
    public        String TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC        = "SalesReturnStorageScanModel_Post_SaleReturnDetailADFasync";  // 销售退货提交
    public        String TAG_PRINT_PALLET_NO                          = "SalesReturnStorageScanModel_Print_PalletNo";  //打印
    private final int    RESULT_TAG_GET_T_AREA_MODEL                  = 121;
    private final int    RESULT_TAG_GET_T_SCAN_BARCODE_ADF_ASYNC      = 122;
    private final int    RESULT_TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC = 123;
    private final int    RESULT_TAG_PRINT_PALLET_NO                   = 124;
    AreaInfo             mAreaInfo;
    OutBarcodeInfo       mCurrentPalletInfo;
    List<OutBarcodeInfo> mList = new ArrayList<>();

    public SalesReturnStorageScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    protected void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_T_AREA_MODEL:
                listener = mNetMap.get("TAG_GET_T_AREA_MODEL");
                break;
            case RESULT_TAG_GET_T_SCAN_BARCODE_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_SCAN_BARCODE_ADF_ASYNC");
                break;
            case RESULT_TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC:
                listener = mNetMap.get("TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC");
                break;
            case RESULT_TAG_PRINT_PALLET_NO:
                listener = mNetMap.get("TAG_PRINT_PALLET_NO");
                break;

        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
        super.onHandleMessage(msg);
    }


    public void setAreaInfo(AreaInfo areaInfo) {
        if (areaInfo != null) {
            mAreaInfo = areaInfo;
        }
    }

    public AreaInfo getAreaInfo() {
        return mAreaInfo;
    }

    public void setCurrentPalletInfo(OutBarcodeInfo outBarcodeInfo) {
        mCurrentPalletInfo = outBarcodeInfo;
    }

    public OutBarcodeInfo getCurrentPalletInfo() {
        return mCurrentPalletInfo;
    }

    public List<OutBarcodeInfo> getList() {
        return mList;
    }

    /**
     * @desc: 获取库位信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestAreaNo(AreaInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_AREA_MODEL", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_GET_T_AREA_MODEL, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_AREA_MODEL, mContext.getString(R.string.Msg_GetAreaModelADF), mContext, mHandler, RESULT_TAG_GET_T_AREA_MODEL, null, UrlInfo.getUrl().GetT_AreaModel, modelJson, null);
    }

    /**
     * @desc: 获取条码数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/24 15:32
     */
    public void requestBarcodeInfo(OutBarcodeInfo outBarcodeInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_SCAN_BARCODE_ADF_ASYNC", callBackListener);
        String modelJson = GsonUtil.parseModelToJson(outBarcodeInfo);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, null, UrlInfo.getUrl().GetT_ScanBarcodeADFAsync, modelJson, null);

    }

    @Override
    public void requestCombineAndReferPallet(OrderDetailInfo info, NetCallBackListener<String> callBackListener) {

    }

    /**
     * @desc: 过账
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 18:46
     */
    public void requestOrderRefer(List<OutBarcodeInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC, mContext.getString(R.string.Msg_order_refer), mContext, mHandler, RESULT_TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().Post_SaleReturnDetailADFasync, modelJson, null);

    }

    /**
     * @desc: 校验条码重复
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 16:01
     */
    public BaseMultiResultInfo<Boolean, Void> checkBarcode(OutBarcodeInfo scanBarcode) {
        boolean HAS_SERIAL_NO = false;
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        if (scanBarcode != null) {
            String serialNo = scanBarcode.getSerialno();
            String materialNo = scanBarcode.getMaterialno();
            String batchNo = scanBarcode.getBatchno();
            float qty = scanBarcode.getQty();
            //校验物料编号
            if (materialNo == null) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:物料信息不能为空");
                return resultInfo;
            }
            //校验序列号
            if (serialNo == null) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:序列号不能为空");
                return resultInfo;
            }
            //校验序列号
            if (batchNo == null) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:批次不能为空");
                return resultInfo;
            }

            //校验数量
            if (qty <= 0) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:数量必须大于0");
                return resultInfo;
            }

            //校验条码重复
            for (int i = 0; i < mList.size(); i++) {
                OutBarcodeInfo info = mList.get(i);
                if (info != null) {
                    String sSerialNo = info.getSerialno() != null ? info.getSerialno() : "";
                    if (sSerialNo.trim().equals(serialNo.trim())) {
                        HAS_SERIAL_NO = true;
                        break;
                    }
                }
            }

            if (!HAS_SERIAL_NO) {
                mList.add(scanBarcode);
                resultInfo.setHeaderStatus(true);
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:序列号:" + serialNo + "已扫描!");
                return resultInfo;
            }


        } else {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("校验条码失败:条码信息不能为空");
            return resultInfo;
        }

        return resultInfo;
    }

    /**
     * @desc: 打印
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 18:46
     */
    public void requestPrint(List<OrderDetailInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_PRINT_PALLET_NO", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_PRINT_PALLET_NO, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_PRINT_PALLET_NO, mContext.getString(R.string.message_request_print), mContext, mHandler, RESULT_TAG_PRINT_PALLET_NO, null, UrlInfo.getUrl().Print_PalletNo, modelJson, null);

    }


    @Override
    protected PrintInfo getPrintModel(OutBarcodeInfo outBarcodeInfo) {
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
            printInfo.setPrintType(PrintType.PRINT_TYPE_PALLET_STYLE);
        }
        return printInfo;


    }


    public void onReset() {
        mList.clear();
        mAreaInfo = null;
        mCurrentPalletInfo = null;
    }
}
