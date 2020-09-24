package com.liansu.boduowms.modules.stockRollBack;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.stock.VoucherDetailSubInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelListToJsonArray;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/8/26.
 */
public class StockRollBackModel extends BaseModel {
    int    mVoucherType  = -1;
    String mTitle        = "回退";
    String mErpVoucherNo = null;
    public          String TAG_GET_T_DETAIL_SUB_ASYNC           = "StockRollBackModel_Get_T_Detail_Sub_Async";  //获取暂存数据
    public          String TAG_DELETE_T_DETAIL_SUB_ASYNC        = "StockRollBackModel_DeleteT_DetailSubAsync"; //删除暂存数据
    protected final int    RESULT_TAG_GET_T_DETAIL_SUB_ASYNC    = 101;
    protected final int    RESULT_TAG_DELETE_T_DETAIL_SUB_ASYNC = 102;
    UrlInfo mUrlInfo = new UrlInfo();
    private List<VoucherDetailSubInfo> mTemporaryList = new ArrayList<>();

    public StockRollBackModel(Context context, MyHandler<BaseActivity> handler, String erpVoucherNo, int voucherType, String title) {
        super(context, handler);
        mVoucherType = voucherType;
        mErpVoucherNo = erpVoucherNo;
        mTitle = title;
        mUrlInfo.InitUrl(voucherType);
    }


    public int getVoucherType() {
        return mVoucherType;
    }

    public String getErpVoucherNo() {
        return mErpVoucherNo;
    }

    public String getTitle() {
        return mTitle;
    }

    /**
     * @desc: 保存暂存数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/27 14:53
     */
    public void setTemporaryList(List<VoucherDetailSubInfo> list) {
        mTemporaryList.clear();
        if (list != null && list.size() > 0) {
            mTemporaryList.addAll(list);
        }
    }

    /**
     * @desc: 获取暂存数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/27 14:53
     */
    public List<VoucherDetailSubInfo> getTemporaryList() {
        return mTemporaryList;
    }

    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_T_DETAIL_SUB_ASYNC:
                listener = mNetMap.get("TAG_GET_T_DETAIL_SUB_ASYNC");
                break;
            case RESULT_TAG_DELETE_T_DETAIL_SUB_ASYNC:
                listener = mNetMap.get("TAG_DELETE_T_DETAIL_SUB_ASYNC");
                break;

        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    /**
     * @desc: 获取暂存数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestTemporaryDetailList(OrderRequestInfo orderRequestInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_DETAIL_SUB_ASYNC", callBackListener);
//        String modelJson = "{\"Erpvoucherno\":\"" + erpVoucherNo + "\"}";
        String modelJson = parseModelToJson(orderRequestInfo);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_GET_T_DETAIL_SUB_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_DETAIL_SUB_ASYNC, mContext.getString(R.string.stock_roll_back_request_temporary_data), mContext, mHandler, RESULT_TAG_GET_T_DETAIL_SUB_ASYNC, null,mUrlInfo.GetT_DetailSubAsync, modelJson, null);
    }

    /**
     * @desc: 删除暂存信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */

    public void requestDeleteTemporaryDetail(List<VoucherDetailSubInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_DELETE_T_DETAIL_SUB_ASYNC", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_DELETE_T_DETAIL_SUB_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_DELETE_T_DETAIL_SUB_ASYNC, mContext.getString(R.string.stock_roll_back_request_delete_temporary_data), mContext, mHandler, RESULT_TAG_DELETE_T_DETAIL_SUB_ASYNC, null, mUrlInfo.DeleteT_DetailSubAsync, modelJson, null);
    }

     /**
      * @desc: 获取当前扫描的条码信息 如果在列表中就返回 数据
      * @param:
      * @return:
      * @author: Nietzsche
      * @time 2020/8/27 16:04
      */
    public  List<VoucherDetailSubInfo> getPalletInfoList(String barcode) {
        List<VoucherDetailSubInfo> deleteList=new ArrayList<>();
        if (barcode == null) barcode = "";
        for (VoucherDetailSubInfo info : mTemporaryList) {
            String sBarcode = info.getBarcode() != null ? info.getBarcode() : "";
            if (info != null && sBarcode.equals(barcode)) {
                deleteList.add(info);
                break;
            }
        }
        return deleteList;
    }

}
