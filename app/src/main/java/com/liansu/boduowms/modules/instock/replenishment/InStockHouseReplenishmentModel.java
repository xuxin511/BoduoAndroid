package com.liansu.boduowms.modules.instock.replenishment;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoice;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelListToJsonArray;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/10/10.
 */
public class InStockHouseReplenishmentModel extends BaseModel {
    public        String          TAG_GET_OUT_PALLET_INFO_QUERY            = "InStockHouseReplenishmentModel_TAG_GET_OUT_PALLET_INFO_QUERY"; // 获取移出托盘信息
    public        String          TAG_GET_IN_PALLET_INFO_QUERY             = "InStockHouseReplenishmentModel_TAG_GET_IN_PALLET_INFO_QUERY";//获取移入托盘信息
    public        String          TAG_GET_IN_PALLET_AREA_INFO_QUERY        = "InStockHouseReplenishmentModel_TAG_GET_IN_PALLET_AREA_INFO_QUERY";//获取移入托盘库位查询信息
    public        String          TAG_REPLENISHMENT_INFO_REFER             = "InStockHouseReplenishmentModel_TAG_GET_IN_PALLET_AREA_INFO_QUERY";//提交补货信息
    private final int             RESULT_TAG_GET_OUT_PALLET_INFO_QUERY     = 307501;
    private final int             RESULT_TAG_GET_IN_PALLET_INFO_QUERY      = 307502;
    private final int             RESULT_TAG_GET_IN_PALLET_AREA_INFO_QUERY = 307503;
    private final int             RESULT_TAG_REPLENISHMENT_INFO_REFER      = 307504;
    public        List<StockInfo> mInPalletInfoList                        = new ArrayList<>();  //移入托盘信息
    public        List<StockInfo> mOutPalletInfoList                       = new ArrayList<>(); //移出托盘信息
    public        AreaInfo        mAreaInfo                                = null; //库位信息
    public        StockInfo       mCurrentMaterialInfo;

    public InStockHouseReplenishmentModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    /**
     * @desc: 保存移入库信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/10/10 16:19
     */
    public void setInPalletInfoList(List<StockInfo> list) {
        mInPalletInfoList.clear();
        if (list != null && list.size() > 0) {
            mInPalletInfoList.addAll(list);
        }
    }

    public List<StockInfo> getInPalletInfoList() {
        return mInPalletInfoList;
    }

    /**
     * @desc: 保存移出库信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/10/10 16:19
     */
    public void setOutPalletInfoList(List<StockInfo> list) {
        mOutPalletInfoList.clear();
        if (list != null && list.size() > 0) {
            mOutPalletInfoList.addAll(list);
        }
    }

    public List<StockInfo> getOutPalletInfoList() {
        return mOutPalletInfoList;
    }

    /**
     * @desc: 保存当前选择的物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/10/10 17:31
     */
    public void setCurrentMaterialInfo(StockInfo stockInfo) {
        mCurrentMaterialInfo = stockInfo;
    }


    public StockInfo getCurrentMaterialInfo() {
        return mCurrentMaterialInfo;
    }


    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_OUT_PALLET_INFO_QUERY:
                listener = mNetMap.get("TAG_GET_OUT_PALLET_INFO_QUERY");
                break;
            case RESULT_TAG_GET_IN_PALLET_INFO_QUERY:
                listener = mNetMap.get("TAG_GET_IN_PALLET_INFO_QUERY");
                break;
            case RESULT_TAG_GET_IN_PALLET_AREA_INFO_QUERY:
                listener = mNetMap.get("TAG_GET_IN_PALLET_AREA_INFO_QUERY");
                break;
            case RESULT_TAG_REPLENISHMENT_INFO_REFER:
                listener = mNetMap.get("TAG_REPLENISHMENT_INFO_REFER");
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
     * @desc: 查询移出库条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/10/10 15:35
     */
    public void requestOutPalletInfoQuery(OutBarcodeInfo palletNo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_OUT_PALLET_INFO_QUERY", callBackListener);
        String ModelJson = GsonUtil.parseModelToJson(palletNo);
        LogUtil.WriteLog(BaseOrderBillChoice.class, TAG_GET_OUT_PALLET_INFO_QUERY, ModelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_OUT_PALLET_INFO_QUERY, mContext.getString(R.string.request_replenishment_move_out_tray_message), mContext, mHandler, RESULT_TAG_GET_OUT_PALLET_INFO_QUERY, null, UrlInfo.getUrl().GetT_TransferInDetailListADFAsync, ModelJson, null);

    }


    /**
     * @desc: 查询移入库条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/10/10 15:35
     */
    public void requestInPalletInfoQuery(OutBarcodeInfo palletNo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_IN_PALLET_INFO_QUERY", callBackListener);
        String ModelJson = GsonUtil.parseModelToJson(palletNo);
        LogUtil.WriteLog(BaseOrderBillChoice.class, TAG_GET_IN_PALLET_INFO_QUERY, ModelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_IN_PALLET_INFO_QUERY, mContext.getString(R.string.request_replenishment_move_in_tray_message), mContext, mHandler, RESULT_TAG_GET_IN_PALLET_INFO_QUERY, null, UrlInfo.getUrl().GetT_TransferInDetailListADFAsync, ModelJson, null);

    }


    /**
     * @desc: 获取库位信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestAreaNo(AreaInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_IN_PALLET_AREA_INFO_QUERY", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_GET_IN_PALLET_AREA_INFO_QUERY, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_IN_PALLET_AREA_INFO_QUERY, mContext.getString(R.string.request_replenishment_move_in_tray_area_message), mContext, mHandler, RESULT_TAG_GET_IN_PALLET_AREA_INFO_QUERY, null, UrlInfo.getUrl().GetT_AreaModel, modelJson, null);
    }

    /**
     * @desc: 提交补货信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 18:46
     */

    public void requestReplenishmentInfoRefer(List<StockInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_REPLENISHMENT_INFO_REFER", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_REPLENISHMENT_INFO_REFER, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_REPLENISHMENT_INFO_REFER, mContext.getString(R.string.request_replenishment_info_refer), mContext, mHandler, RESULT_TAG_REPLENISHMENT_INFO_REFER, null, UrlInfo.getUrl().PostT_WorkOrderDetailADFAsync, modelJson, null);

    }

    public void onReset() {
        setCurrentMaterialInfo(null);
        mOutPalletInfoList.clear();
        mInPalletInfoList.clear();
        mAreaInfo=null;
    }
}
