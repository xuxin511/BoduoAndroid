package com.liansu.boduowms.modules.pallet.disCombinePallet;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;


/**
 * @ Des:
 * @ Created by yangyiqing on 2019/11/14.
 */
public class DismantlePalletModel {
    MyHandler<BaseActivity> mHandler;
    Context                 mContext;

    public        String                           TAG_BarCodeInfo_Stock_Query     = "CombinPallet_BarCodeInfo_Stock_Query";
    public        String                           TAG_PalletInfoFromStockQuery    = "CombinPallet_PalletInfoFromStockQuery";
    public        String                           TAG_PalletInfoSave              = "CombinPallet_PalletInfoSave";
    private final int                              Result_NewCreatedPalletNoQuery  = 106;
    private final int                              Result_BarCodeInfo_Stock_Query  = 107;
    private final int                              Result_PalletInfoFromStockQuery = 108;
    private final int                              Result_PalletInfoSave           = 109;
    private       Map<String, NetCallBackListener> mNetMap                         = new HashMap<>();
    private       List<OutBarcodeInfo>            mList                           = new ArrayList<>();


    public DismantlePalletModel(Context context, MyHandler<BaseActivity> handler) {
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




    /**
     * @desc: 查询条码信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/20 21:06
     */
    public void requestBarcodeInfoFromStockQuery(String barcode, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_BarCodeInfo_Stock_Query", callBackListener);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("barcode", barcode);  //条码
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_BarCodeInfo_Stock_Query, "正在查询子级条码信息...", mContext, mHandler, Result_BarCodeInfo_Stock_Query, null, URLModel.GetURL().getStockBarcode, params, null);

    }



    /**
     * @desc: 提交组织信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/2/14 10:14
     */
    public void requestPalletInfoSave(OutBarcodeInfo info, int type, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_PalletInfoSave", callBackListener);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("barcodeJson", GsonUtil.parseModelToJson(info));  //条码
        params.put("type", type + "");  //1 单个拆托  2. 关不拆托
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_PalletInfoFromStockQuery, "正在提交拆托信息...", mContext, mHandler, Result_PalletInfoSave, null, URLModel.GetURL().deleteCombinePalletInfos, params, null);
    }


    /**
     * @desc: 保存条码信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/20 11:01
     */
    public void setBarcodeInfo(@NonNull OutBarcodeInfo barcodeInfo) {
        mList.add(0, barcodeInfo);
    }

    public List<OutBarcodeInfo> getList() {
        return mList;
    }

    public void onClear() {
        mList.clear();
    }



}
