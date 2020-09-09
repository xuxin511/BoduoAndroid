package com.liansu.boduowms.modules.instock.noSourceOtherStorage.scan;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.instock.salesReturn.scan.SalesReturnStorageScanModel;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelListToJsonArray;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class NoSourceOtherStorageScanModel extends SalesReturnStorageScanModel {

    public NoSourceOtherStorageScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    public void onHandleMessage(Message msg) {
        super.onHandleMessage(msg);
    }

    @Override
    public void requestOrderRefer(List<OutBarcodeInfo> list, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC", callBackListener);
        String modelJson = parseModelListToJsonArray(list);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC, mContext.getString(R.string.message_request_refer_barcode_info), mContext, mHandler, RESULT_TAG_POST_SALE_RETURN_DETAIL_ADF_ASYNC, null, UrlInfo.getUrl().Create_OtherInDetailADFasync, modelJson, null);
    }
}
