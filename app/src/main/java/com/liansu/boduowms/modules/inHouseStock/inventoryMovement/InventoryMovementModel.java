package com.liansu.boduowms.modules.inHouseStock.inventoryMovement;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.HashMap;
import java.util.Map;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;


/**
 * @ Des:
 * @ Created by yangyiqing on 2019/11/14.
 */
public class InventoryMovementModel {
    MyHandler<BaseActivity> mHandler;
    Context                 mContext;
    public        String                           TAG_NewCreatedPalletNoQuery     = "CombinPallet_NewCreatedPalletNoQuery";
    public        String                           TAG_BarCodeInfo_Stock_Query     = "CombinPallet_BarCodeInfo_Stock_Query";
    public        String                           TAG_PalletInfoFromStockQuery    = "CombinPallet_PalletInfoFromStockQuery";
    public        String                           TAG_PalletInfoSave              = "CombinPallet_PalletInfoSave";
    private final int                              Result_NewCreatedPalletNoQuery  = 106;
    private final int                              Result_BarCodeInfo_Stock_Query  = 107;
    private final int                              Result_PalletInfoFromStockQuery = 108;
    private final int                              Result_PalletInfoSave           = 109;
    private       Map<String, NetCallBackListener> mNetMap                         = new HashMap<>();
    private       StockInfo                        mStockInfo;
    private  AreaInfo  mMoveInAreaNo=null;
    private  AreaInfo  mMoveOutAreaNo=null;
    public  static  final  int MOVE_IN_AREA_NO=1;
    public  static  final  int MOVE_OUT_AREA_NO=2;
    public InventoryMovementModel(Context context, MyHandler<BaseActivity> handler) {
        mContext = context;
        mHandler = handler;
    }


    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case Result_NewCreatedPalletNoQuery:
                listener = mNetMap.get("TAG_NewCreatedPalletNoQuery");
                break;
            case Result_BarCodeInfo_Stock_Query:
                listener = mNetMap.get("TAG_BarCodeInfo_Stock_Query");
                break;
            case Result_PalletInfoFromStockQuery:
                listener = mNetMap.get("TAG_PalletInfoFromStockQuery");
                break;
            case Result_PalletInfoSave:
                listener = mNetMap.get("TAG_PalletInfoSave");
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    public void setStockInfo(StockInfo stockInfo) {
        mStockInfo = stockInfo;
    }

    public StockInfo getStockInfo() {
        return mStockInfo;
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
        params.put("UserJson", parseModelToJson(BaseApplication.mCurrentUserInfo));
//        LogUtil.WriteLog(InventoryMovementScan.class, TAG_GetT_PalletDetailByBarCodeADF, barcode);
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
//        LogUtil.WriteLog(ReceiptionScan.class, TAG_GetAreaModelADF, areaNo);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetAreaModelADF, mContext.getString(R.string.Msg_GetAreaModelADF), mContext, mHandler, RESULT_Msg_GetAreaModelADF, null, URLModel.GetURL().GetAreaModelADF, params, null);

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
        mStockInfo = null;
    }


    public void setMoveInAreaNo(AreaInfo info) {
        mMoveInAreaNo=info;
    }

    public AreaInfo   getMoveInAreaNo(){
        return  mMoveInAreaNo;
    }

    public void setMoveOutAreaNo(AreaInfo info){
        mMoveOutAreaNo=info;
    }

    public AreaInfo  getMoveOutAreaNo(){
       return mMoveOutAreaNo;
    }

}
