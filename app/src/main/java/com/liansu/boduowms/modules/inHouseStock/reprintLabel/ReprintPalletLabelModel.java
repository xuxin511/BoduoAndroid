package com.liansu.boduowms.modules.inHouseStock.reprintLabel;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.modules.instock.batchPrint.order.BaseOrderLabelPrintSelect;
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
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;


/**
 * @ Des:
 * @ Created by yangyiqing on 2019/11/14.
 */
public class ReprintPalletLabelModel {
    MyHandler<BaseActivity> mHandler;
    Context                 mContext;
    private             Map<String, NetCallBackListener> mNetMap                                 = new HashMap<>();
    private             List<StockInfo> mStockInfoList = new ArrayList<>();
    private             AreaInfo        mAreaInfo      = null;
    private             OutBarcodeInfo  mMaterialInfo=null;
    public        String TAG_SELECT_MATERIAL              = "ReprintPalletLabelModel_SelectMaterial";  //获取物料信息
    public        String TAG_GET_T_AREA_MODEL             = "ReprintPalletLabelModel_GetT_AreaModel";  //获取库位信息
    public        String TAG_STOCK_INFO_LIST_QUERY        = "ReprintPalletLabelModel_GetT_StockDetailList";  //库存查询
    public        String TAG_REPRINT_PALLET_LABEL         = "ReprintPalletLabelModel_TAG_TAG_REPRINT_PALLET_LABEL";  //重新打印托盘标签
    private final int    RESULT_TAG_SELECT_MATERIAL       = 101;
    private final int    RESULT_TAG_GET_T_AREA_MODEL      = 102;
    private final int    RESULT_TAG_STOCK_INFO_LIST_QUERY = 103;
    private final int    RESULT_TAG_REPRINT_PALLET_LABEL  = 104;
    protected int mVoucherType=-1;
    public ReprintPalletLabelModel(Context context, MyHandler<BaseActivity> handler) {
        mContext = context;
        mHandler = handler;
    }
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_SELECT_MATERIAL:
                listener = mNetMap.get("TAG_SELECT_MATERIAL");
                break;
            case RESULT_TAG_GET_T_AREA_MODEL:
                listener = mNetMap.get("TAG_GET_T_AREA_MODEL");
                break;
            case RESULT_TAG_STOCK_INFO_LIST_QUERY:
                listener = mNetMap.get("TAG_STOCK_INFO_LIST_QUERY");
                break;
            case RESULT_TAG_REPRINT_PALLET_LABEL:
                listener = mNetMap.get("TAG_REPRINT_PALLET_LABEL");
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

    public void setVoucherType(int voucherType){
        mVoucherType=voucherType;
    }
    /**
     * @desc: 获取单据类型
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/10 17:58
     */
    public int  getVoucherType(){
        return mVoucherType;
    }


   public void  setStockInfoList(List<StockInfo> list){
        mStockInfoList.clear();
        if (list!=null && list.size()>0){
            mStockInfoList.addAll(list);
        }
   }

   public  List<StockInfo> getStockInfoList(){
        return  mStockInfoList;
   }
    public void setAreaNo(AreaInfo info) {
        mAreaInfo = info;
    }

    public AreaInfo getAreaNo() {
        return mAreaInfo;
    }

    public void setMaterialInfo(OutBarcodeInfo materialInfo){
        mMaterialInfo=materialInfo;
    }

    public OutBarcodeInfo  getMaterialInfo(){
        return mMaterialInfo;
    }
    /**
     * @desc: 查询物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestMaterialInfoQuery(String material, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_SELECT_MATERIAL", callBackListener);
        String modelJson = parseModelToJson(material);
        LogUtil.WriteLog(BaseOrderLabelPrintSelect.class, TAG_SELECT_MATERIAL, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SELECT_MATERIAL, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_TAG_SELECT_MATERIAL, null, UrlInfo.getUrl().SelectMaterial, modelJson, null);

    }
    /**
     *
     * @desc: 获取库存条码数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/24 15:32
     */
    public void requestStockInfoListQuery(StockInfo stockInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_STOCK_INFO_LIST_QUERY", callBackListener);
        String modelJson = GsonUtil.parseModelToJson(stockInfo);
        LogUtil.WriteLog(ReprintPalletLabelModel.class, TAG_STOCK_INFO_LIST_QUERY, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_STOCK_INFO_LIST_QUERY, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_TAG_STOCK_INFO_LIST_QUERY, null, UrlInfo.getUrl().GetT_StockDetailList, modelJson, null);
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
        LogUtil.WriteLog(ReprintPalletLabelModel.class, TAG_GET_T_AREA_MODEL, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_AREA_MODEL, mContext.getString(R.string.Msg_GetAreaModelADF), mContext, mHandler, RESULT_TAG_GET_T_AREA_MODEL, null, UrlInfo.getUrl().GetT_AreaModel, modelJson, null);
    }


    /**
     * @desc: 请求托盘标签补打
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/10/22 12:43
     */
    public void requestReprintPalletInfo(OutBarcodeInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_REPRINT_PALLET_LABEL", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(ReprintPalletLabelModel.class, TAG_REPRINT_PALLET_LABEL, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_REPRINT_PALLET_LABEL, mContext.getString(R.string.query_stock_request_stock_info_list), mContext, mHandler, RESULT_TAG_REPRINT_PALLET_LABEL, null, UrlInfo.getUrl().PrintPalletno, modelJson, null);

    }


    public void onClear() {
        mAreaInfo = null;
        mStockInfoList.clear();
        mMaterialInfo=null;
    }
}
