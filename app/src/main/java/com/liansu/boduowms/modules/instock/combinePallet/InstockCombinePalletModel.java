package com.liansu.boduowms.modules.instock.combinePallet;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.CombinePalletInfo;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScanModel;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;


/**
 * @desc:
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/9/25 10:48
 */
public class InstockCombinePalletModel extends BaseOrderScanModel {
    public static final int             COMBINE_PALLET_TYPE_NONE                    = -1;
    public static final int             COMBINE_PALLET_TYPE_OLD_PALLET              = 1;
    public static final int             COMBINE_PALLET_TYPE_NEW_PALLET              = 2;
    public static final int             COMBINE_PALLET_TYPE_DIS_COMBINE_PALLET      = 3;
    public static final String          COMBINE_PALLET_TYPE_OLD_PALLET_NAME         = "原托盘";
    public static final String          COMBINE_PALLET_TYPE_NEW_PALLET_NAME         = "新托盘";
    public static final String          COMBINE_PALLET_TYPE_DIS_COMBINE_PALLET_NAME = "拆托";
    public static final int             PALLET_TYPE_FIRST_PALLET                    = 1;
    public static final int             PALLET_TYPE_SECOND_PALLET                   = 2;
    protected           List<StockInfo> mTargetPalletInfoList                       = new ArrayList<>(); //第一个托盘信息 作为参照
    protected           List<StockInfo> mAwaitPalletInfoList                        = new ArrayList<>(); //第一个托盘信息 作为参照
    protected           List<StockInfo> mList                                       = new ArrayList<>();//展示的托盘信息集合
    public static final String          TAG_COMBINE_PALLET_REFER                    = "InstockCombinePalletModel_TAG_COMBINE_PALLET_REFER";
    public static final String          TAG_DIS_COMBINE_PALLET_REFER                = "InstockCombinePalletModel_TAG_DIS_COMBINE_PALLET_REFER";
    public final        int             RESULT_TAG_COMBINE_PALLET_REFER             = 1001;
    public final        int             RESULT_TAG_DIS_COMBINE_PALLET_REFER         = 1002;


    public InstockCombinePalletModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler, -1);
    }

    public InstockCombinePalletModel(Context context, MyHandler<BaseActivity> handler, int voucherType) {
        super(context, handler, voucherType);
    }

    @Override
    public void onHandleMessage(Message msg) {
        super.onHandleMessage(msg);
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_COMBINE_PALLET_REFER:
                listener = mNetMap.get("TAG_COMBINE_PALLET_REFER");
                break;
            case RESULT_TAG_DIS_COMBINE_PALLET_REFER:
                listener = mNetMap.get("TAG_DIS_COMBINE_PALLET_REFER");
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    /**
     * @desc: 保存第一个托盘码信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/25 10:48
     */
    public void setTargetPalletInfoList(List<StockInfo> list) {
        mTargetPalletInfoList.clear();
        mList.clear();
        if (list != null && list.size() > 0) {
            mTargetPalletInfoList.addAll(0, list);
            mList.addAll(0, list);
        }
    }
   /**
    * @desc:  校验拼入托盘 的taskqty 是否大于0
    * @param:
    * @return:
    * @author: Nietzsche
    * @time 2020/11/2 21:47
    */
    public  BaseMultiResultInfo<Boolean, Void> checkTargetPalletInfoList(List<StockInfo> list){
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        boolean result=true;
        String  message="";
        if (list!=null && list.size()>0){
            for (StockInfo info:list){
                if (info!=null){
                    if (info.getTaskQty()>0){
                        result=false;
                        message="校验托盘失败：托盘:"+info.getBarcode()+"上的物料["+info.getMaterialno()+"]的任务数量大于0,不允许进行拼托或拆托操作";
                        break;
                    }
                }
            }
        }
        resultInfo.setHeaderStatus(result);
        resultInfo.setMessage(message);
        return  resultInfo;
    }
    public List<StockInfo> getTargetPalletInfoList() {
        return mTargetPalletInfoList;
    }


    public void setAwaitPalletInfoList(List<StockInfo> list) {
        mAwaitPalletInfoList.clear();
        if (list != null && list.size() > 0) {
            mAwaitPalletInfoList.addAll(list);
        }
    }

    public List<StockInfo> getAwaitPalletInfoList() {
        return mAwaitPalletInfoList;
    }

    /**
     * @desc: 获取展示列表
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/25 15:48
     */
    public List<StockInfo> getShowList() {
        return mList;
    }


    /**
     * @desc: 获取条码数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/24 15:32
     */
    @Override
    public void requestBarcodeInfo(OutBarcodeInfo outBarcodeInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_SCAN_BARCODE_ADF_ASYNC", callBackListener);
        String modelJson = GsonUtil.parseModelToJson(outBarcodeInfo);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, null, UrlInfo.getUrl().GetT_ScanStockADFAsync, modelJson, null);

    }

    @Override
    public void requestCombineAndReferPallet(OrderDetailInfo info, NetCallBackListener<String> callBackListener) {

    }

    /**
     * @desc: 拼托提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/25 16:19
     */
    public void requestOrderRefer(CombinePalletInfo postInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_COMBINE_PALLET_REFER", callBackListener);
        String modelJson = parseModelToJson(postInfo);
        LogUtil.WriteLog(InstockCombinePallet.class, TAG_COMBINE_PALLET_REFER, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_COMBINE_PALLET_REFER, mContext.getString(R.string.message_request_refer_barcode_info), mContext, mHandler, RESULT_TAG_COMBINE_PALLET_REFER, null, UrlInfo.getUrl().Save_StockSpell, modelJson, null);
    }

    /**
     * @desc: 拆托提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/25 16:22
     */
    public void requestDisCombineOrderRefer(CombinePalletInfo postInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_DIS_COMBINE_PALLET_REFER", callBackListener);
        String modelJson = parseModelToJson(postInfo);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_DIS_COMBINE_PALLET_REFER, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_DIS_COMBINE_PALLET_REFER, mContext.getString(R.string.message_request_refer_barcode_info), mContext, mHandler, RESULT_TAG_DIS_COMBINE_PALLET_REFER, null, UrlInfo.getUrl().Save_disassemblePallets, modelJson, null);
    }

    /**
     * @desc: 校验条码重复
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 16:01
     */

    public BaseMultiResultInfo<Boolean, Void> checkBarcode(List<StockInfo> palletInfoList) {
        boolean HAS_SERIAL_NO = false;
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        if (palletInfoList != null && palletInfoList.size() > 0) {
            StockInfo scanBarcode = palletInfoList.get(0);
            if (scanBarcode != null) {
                String serialNo = scanBarcode.getSerialno() != null ? scanBarcode.getSerialno() : "";
                String areaNo = scanBarcode.getAreano() != null ? scanBarcode.getAreano() : "";
                //校验条码重复
                for (int i = 0; i < mList.size(); i++) {
                    StockInfo info = mList.get(i);
                    if (info != null) {
                        String sSerialNo = info.getSerialno() != null ? info.getSerialno() : "";
                        if (sSerialNo.trim().equals(serialNo.trim())) {
                            HAS_SERIAL_NO = true;
                            break;
                        }
                    }
                }

                if (!HAS_SERIAL_NO) {
                    if (mTargetPalletInfoList.size() > 0) {
                        StockInfo info = mTargetPalletInfoList.get(0);
                        String sAreaNo = info.getAreano() != null ? info.getAreano() : "";
                        if (!sAreaNo.trim().equals(areaNo.trim())) {
                            resultInfo.setHeaderStatus(false);
                            resultInfo.setMessage("校验条码失败:待拼托盘[" + scanBarcode.getBarcode() + "]的库位[" + areaNo + "]和拼入托盘[" + info.getBarcode() + "]的库位[" + sAreaNo + "]不一致！不能拼托");
                            return resultInfo;
                        }

                    } else {
                        resultInfo.setHeaderStatus(false);
                        resultInfo.setMessage("校验条码失败:拼入托盘不能为空,请扫描拼入托盘");
                        return resultInfo;
                    }


                    mList.addAll(0, palletInfoList);
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


        } else {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("校验条码失败:条码信息不能为空");
            return resultInfo;
        }

        return resultInfo;
    }

    /**
     * @desc: 获取当前展示的集合中托盘数量
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/25 15:46
     */
    public int getSerialNoCount() {
        List<String> serialNoList = new ArrayList<>();
        for (StockInfo info : mList) {
            String serialNo = info.getSerialno() != null ? info.getSerialno() : "";
            if (!serialNo.equals("")) {
                if (!serialNoList.contains(serialNo)) {
                    serialNoList.add(serialNo);
                }
            }
        }
        return serialNoList.size();
    }

    @Override
    public void onReset() {
        mList.clear();
        mTargetPalletInfoList.clear();
        mAwaitPalletInfoList.clear();

    }
}
