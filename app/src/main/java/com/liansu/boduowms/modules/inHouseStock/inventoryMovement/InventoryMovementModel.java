package com.liansu.boduowms.modules.inHouseStock.inventoryMovement;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.RequestHandler;
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
 * @ Created by yangyiqing on 2019/11/14.
 */
public class InventoryMovementModel {
    MyHandler<BaseActivity> mHandler;
    Context                 mContext;
    public  String                           TAG_GET_T_AREA_MODEL             = "InventoryMovementModel_GetT_AreaModel";  //库位
    public              String                           TAG_GET_T_SCAN_BARCODE_ADF_ASYNC = "BaseOrderScanModel_GetT_ScanBarcodeADFAsync";  //获取托盘码信息接口
    private final       int                              RESULT_TAG_GET_T_AREA_MODEL      = 110;
    protected final int                        RESULT_TAG_GET_T_SCAN_BARCODE_ADF_ASYNC        = 105;
    private Map<String, NetCallBackListener> mNetMap                          = new HashMap<>();
    private List<StockInfo>                  mStockInfoList=new ArrayList<>();
    private AreaInfo                         mMoveInAreaNo                    = null;
    private             AreaInfo                         mMoveOutAreaNo                   = null;
    public static final int                              MOVE_TYPE_IN_AREA_NO             = 1;
    public static final int                              MOVE_TYPE_OUT_AREA_NO            = 2;

    public InventoryMovementModel(Context context, MyHandler<BaseActivity> handler) {
        mContext = context;
        mHandler = handler;
    }


    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_T_AREA_MODEL:
                listener = mNetMap.get("TAG_GET_T_AREA_MODEL");
                break;
            case RESULT_TAG_GET_T_SCAN_BARCODE_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_SCAN_BARCODE_ADF_ASYNC");
                break;

        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    public void setStockInfo(List<StockInfo> stockInfoList) {
        mStockInfoList.clear();
        if (stockInfoList!=null && stockInfoList.size()>0){
            mStockInfoList.addAll(stockInfoList);
        }
    }

    public List<StockInfo> getStockList() {
        return mStockInfoList;
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
     * @desc: 移库提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestRefer(StockInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GetT_PalletDetailByBarCodeADF", callBackListener);
        final Map<String, String> params = new HashMap<String, String>();
//        params.put("BarCode", parseModelToJson(info));
//        params.put("UserJson", parseModelToJson(BaseApplication.mCurrentUserInfo));
//        LogUtil.WriteLog(ReceiptionScan.class, TAG_GetT_PalletDetailByBarCodeADF, parseModelToJson(info));
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_PalletDetailByBarCodeADF, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_Msg_GetT_PalletDetailByBarCode, null, URLModel.GetURL().GetT_PalletDetailByBarCodeADF, params, null);

    }

    public void onClear() {
        mStockInfoList.clear();
    }


    public void setMoveInAreaNo(AreaInfo info) {
        mMoveInAreaNo = info;
    }

    public AreaInfo getMoveInAreaNo() {
        return mMoveInAreaNo;
    }

    public void setMoveOutAreaNo(AreaInfo info) {
        mMoveOutAreaNo = info;
    }

    public AreaInfo getMoveOutAreaNo() {
        return mMoveOutAreaNo;
    }

}
