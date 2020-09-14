package com.liansu.boduowms.modules.instock.combinePallet;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
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
 * @ Created by yangyiqing on 2019/11/14.
 */
public class InstockCombinePalletModel extends SalesReturnStorageScanModel {

    public InstockCombinePalletModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler,-1);
    }

    public InstockCombinePalletModel(Context context, MyHandler<BaseActivity> handler, int voucherType) {
        super(context, handler, voucherType);
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
    /**
     * @desc: 校验条码重复
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 16:01
     */
    @Override
    public BaseMultiResultInfo<Boolean, Void> checkBarcode(OutBarcodeInfo scanBarcode) {
        boolean HAS_SERIAL_NO = false;
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        if (scanBarcode != null) {
            String serialNo = scanBarcode.getSerialno();
            String materialNo = scanBarcode.getMaterialno();
            String batchNo = scanBarcode.getBatchno();
            String customerCode=scanBarcode.getCustomerno()!=null?scanBarcode.getCustomerno():"";
            float qty = scanBarcode.getQty();
            //校验物料编号
            if (materialNo == null) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:物料信息不能为空");
                return resultInfo;
            }
            //校验序列号
            if (serialNo == null) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:序列号不能为空");
                return resultInfo;
            }
            //校验序列号
            if (batchNo == null) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:批次不能为空");
                return resultInfo;
            }

            //校验数量
            if (qty <= 0) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:数量必须大于0");
                return resultInfo;
            }

            //校验条码重复
            for (int i = 0; i < mList.size(); i++) {
                OutBarcodeInfo info = mList.get(i);
                if (info != null) {
                    String sSerialNo = info.getSerialno() != null ? info.getSerialno() : "";
                    if (sSerialNo.trim().equals(serialNo.trim())) {
                        HAS_SERIAL_NO = true;
                        break;
                    }
                }
            }

            if (!HAS_SERIAL_NO) {
                mList.add(scanBarcode);
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
