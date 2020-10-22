package com.liansu.boduowms.modules.inHouseStock.inventoryMovement;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelListToJsonArray;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;


/**
 * @ Des:
 * @ Created by yangyiqing on 2019/11/14.
 */
public class InventoryMovementModel {
    MyHandler<BaseActivity> mHandler;
    Context                 mContext;
    public String TAG_GET_T_AREA_MODEL             = "InventoryMovementModel_GetT_AreaModel";  //库位
    public String TAG_GET_T_SCAN_BARCODE_ADF_ASYNC = "BaseOrderScanModel_GetT_ScanStockADFAsync";  //获取托盘码信息接口
    public String TAG_UPDATE_T_STOCK_LIST_AREA     = "InventoryMovementModel_UpdateT_StockListArea";  //移库提交

    private final       int                              RESULT_TAG_GET_T_AREA_MODEL             = 110;
    protected final     int                              RESULT_TAG_GET_T_SCAN_BARCODE_ADF_ASYNC = 105;
    protected final     int                              RESULT_TAG_UPDATE_T_STOCK_LIST_AREA     = 106;
    private             Map<String, NetCallBackListener> mNetMap                                 = new HashMap<>();
    private             List<StockInfo>                  mStockInfoList                          = new ArrayList<>();
    private             AreaInfo                         mMoveInAreaNo                           = null;
    private             AreaInfo                         mMoveOutAreaNo                          = null;
    public static final int                              MOVE_TYPE_IN_AREA_NO                    = 1;
    public static final int                              MOVE_TYPE_OUT_AREA_NO                   = 2;

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
            case RESULT_TAG_UPDATE_T_STOCK_LIST_AREA:
                listener = mNetMap.get("TAG_UPDATE_T_STOCK_LIST_AREA");
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                MessageBox.Show(mContext, "获取请求失败_____" + msg.obj, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    /**
     * public List<StockInfo> getStockInfoList(){
     * return mStockInfoList;
     * }
     * <p>
     * <p>
     * /**
     *
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
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, null, UrlInfo.getUrl().GetT_ScanStockADFAsync, modelJson, null);

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
    public void requestRefer(List<StockInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_UPDATE_T_STOCK_LIST_AREA", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_UPDATE_T_STOCK_LIST_AREA, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_UPDATE_T_STOCK_LIST_AREA, mContext.getString(R.string.message_request_refer_inventory_movement), mContext, mHandler, RESULT_TAG_UPDATE_T_STOCK_LIST_AREA, null, UrlInfo.getUrl().UpdateT_StockListArea, modelJson, null);
    }

    public void onClear() {
        mMoveInAreaNo = null;
        mStockInfoList.clear();

    }


    public void setMoveInAreaNo(AreaInfo info) {
        mMoveInAreaNo = info;
    }

    public AreaInfo getMoveInAreaNo() {
        return mMoveInAreaNo;
    }

    public List<StockInfo> getStockInfoList() {
        return mStockInfoList;
    }


    /**
     * @desc: 校验条码重复
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 16:01
     */
    public BaseMultiResultInfo<Boolean, Void> checkAndUpdateBarcodeList(List<StockInfo> list) {
        boolean HAS_SERIAL_NO = false;
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        if (list != null && list.size() > 0) {
            StockInfo info = list.get(0);
            if (info == null) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:查询到的数据为空");
                return resultInfo;
            }
                String serialNo = info.getSerialno();
                String wareHouseNo = info.getTowarehouseno() != null ? info.getTowarehouseno() : "";
                //校验序列号
                if (serialNo == null) {
                    resultInfo.setHeaderStatus(false);
                    resultInfo.setMessage("校验条码失败:序列号不能为空");
                    return resultInfo;
                }
                if (!wareHouseNo.trim().equals(BaseApplication.mCurrentWareHouseInfo.getWarehouseno().trim())) {
                    resultInfo.setHeaderStatus(false);
                    resultInfo.setMessage("校验条码失败:序列号[" + serialNo + "]的仓库编码[" + wareHouseNo + "]和当前仓库编码[" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno() + "]不一致！");
                    return resultInfo;

                }
                //校验条码重复
                for (int i = 0; i < mStockInfoList.size(); i++) {
                    StockInfo stockInfo = mStockInfoList.get(i);
                    if (stockInfo != null) {
                        String sSerialNo = stockInfo.getSerialno() != null ? stockInfo.getSerialno() : "";
                        if (sSerialNo.trim().equals(serialNo.trim())) {
                            HAS_SERIAL_NO = true;
                            break;
                        }
                    }
                }

                if (!HAS_SERIAL_NO) {
                    mStockInfoList.addAll(list);
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
}
