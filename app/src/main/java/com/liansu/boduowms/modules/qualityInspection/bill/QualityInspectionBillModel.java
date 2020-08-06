package com.liansu.boduowms.modules.qualityInspection.bill;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseFragment;
import com.liansu.boduowms.base.BaseFragmentModel;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
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
public class QualityInspectionBillModel extends BaseFragmentModel {
    public        String TAG_GetT_InStockList              = "ReceiptBillChoice_GetT_InStockList";
    public        String TAG_GetT_PalletDetailByBarCode    = "ReceiptBillChoice_GetT_PalletDetailByBarCode";
    public        String TAG_GetErpVoucherNo               = "ReceiptBillChoice_GetErpVoucherNo";
    public        String TAG_GET_QUALITY_HEAD_LIST_SYNC    = "QualityInspectionBillModel_GetT_QualityHeadListsync";
    private final int    RESULT_GetT_InStockList           = 101;
    private final int    RESULT_GetT_PalletDetailByBarCode = 102;
    private final int    RESULT_GetErpVoucherNo            = 116;
    private final int    RESULT_GET_QUALITY_HEAD_LIST_SYNC = 117;
    ArrayList<QualityHeaderInfo> mQualityOrderList = new ArrayList<>();//单据信息
    ArrayList<OutBarcodeInfo>    mBarCodeInfos     = new ArrayList<>();

    public QualityInspectionBillModel(Context context, MyHandler<BaseFragment> handler) {
        super(context, handler);
    }

    @Override
    protected void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_GetT_InStockList:
                listener = mNetMap.get("TAG_GetT_InStockList");
                break;
            case RESULT_GetT_PalletDetailByBarCode:

                break;
            case RESULT_GET_QUALITY_HEAD_LIST_SYNC:
                listener = mNetMap.get("TAG_GET_QUALITY_HEAD_LIST_SYNC");
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
    public void requestQualityInspectionBillInfoList(QualityHeaderInfo headerInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_QUALITY_HEAD_LIST_SYNC", callBackListener);
        String ModelJson = GsonUtil.parseModelToJson(headerInfo);
        LogUtil.WriteLog(QualityInspectionMainActivity.class, TAG_GET_QUALITY_HEAD_LIST_SYNC, ModelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_QUALITY_HEAD_LIST_SYNC, mContext.getString(R.string.quality_inspection_list_query), mContext, mHandler, RESULT_GET_QUALITY_HEAD_LIST_SYNC, null, UrlInfo.getUrl().GetT_QualityHeadListsync, ModelJson, null);
    }

    /**
     * @desc: 临时存放质检订单列表数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 18:10
     */
    public void setQualityInspectionInfoList(List<QualityHeaderInfo> list) {
        mQualityOrderList.clear();
        if (list != null && list.size() != 0) {
            mQualityOrderList.addAll(list);
        }
    }

    public List<QualityHeaderInfo> getQualityInspectionInfoList() {
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
