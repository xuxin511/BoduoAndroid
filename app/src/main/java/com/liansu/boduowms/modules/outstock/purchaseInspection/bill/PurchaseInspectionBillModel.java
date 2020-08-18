package com.liansu.boduowms.modules.outstock.purchaseInspection.bill;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.qualityInspection.bill.QualityInspectionMainActivity;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class PurchaseInspectionBillModel extends BaseModel {
    public        String TAG_GET_T_INSPEC_RETURN_LIST_ADF_ASYNC = "PurchaseInspectionBillModel_GetT_InspecReturnListADFAsync";  //采购验退
    private final int    RESULT_TAG_GET_T_INSPEC_RETURN_LIST_ADF_ASYNC      = 301;
    ArrayList<OutStockOrderHeaderInfo> mQualityOrderList = new ArrayList<>();//单据信息
    ArrayList<OutBarcodeInfo>    mBarCodeInfos     = new ArrayList<>();

    public PurchaseInspectionBillModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }


    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_T_INSPEC_RETURN_LIST_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_INSPEC_RETURN_LIST_ADF_ASYNC");
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
     * @desc: 获取质检订单合格列表数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 17:52
     */
    public void requestQualityInspectionBillInfoList(OutStockOrderHeaderInfo headerInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_INSPEC_RETURN_LIST_ADF_ASYNC", callBackListener);
        String ModelJson = GsonUtil.parseModelToJson(headerInfo);
        LogUtil.WriteLog(QualityInspectionMainActivity.class, TAG_GET_T_INSPEC_RETURN_LIST_ADF_ASYNC, ModelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_INSPEC_RETURN_LIST_ADF_ASYNC, mContext.getString(R.string.quality_inspection_list_query), mContext, mHandler, RESULT_TAG_GET_T_INSPEC_RETURN_LIST_ADF_ASYNC, null, UrlInfo.getUrl().InspecReturn_GetT_InspecReturnListADFAsync, ModelJson, null);
    }

    /**
     * @desc: 临时存放质检订单列表数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 18:10
     */
    public void setQualityInspectionInfoList(List<OutStockOrderHeaderInfo> list) {
        mQualityOrderList.clear();
        if (list != null && list.size() != 0) {
            mQualityOrderList.addAll(list);
        }
    }

    public List<OutStockOrderHeaderInfo> getQualityInspectionInfoList() {
        return mQualityOrderList;
    }


    public List<OutBarcodeInfo> getBarCodeList() {
        return mBarCodeInfos;
    }

    public void onReset() {
        mBarCodeInfos.clear();
        mQualityOrderList.clear();
    }
}
