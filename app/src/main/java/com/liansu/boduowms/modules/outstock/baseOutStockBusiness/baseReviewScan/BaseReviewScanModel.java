package com.liansu.boduowms.modules.outstock.baseOutStockBusiness.baseReviewScan;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Des: 发货装车扫描
 * @ Created by yangyiqing on 2020/6/28.
 */
public class BaseReviewScanModel extends BaseModel {
    public final  String                        TAG_GET_T_CHECK_OUT_STOCK_DETAIL_LIST_ADF_ASYNC        = "ReviewScanModel_GetT_CheckOutStockDetailListADFAsync";
    protected     List<OutStockOrderDetailInfo> mOrderDetailList                                       = new ArrayList<>(); //订单表体
    protected     OutStockOrderHeaderInfo       mOrderHeaderInfo;
    private final int                           RESULT_TAG_GET_T_CHECK_OUT_STOCK_DETAIL_LIST_ADF_ASYNC = 301;

    public BaseReviewScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    protected void onHandleMessage(Message msg) {

    }

    /**
     * @desc: 保存表体明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/30 18:09
     */
    public void setOrderDetailList(List<OutStockOrderDetailInfo> list) {
        mOrderDetailList.clear();
        if (list != null && list.size() > 0) {
            mOrderDetailList.addAll(list);
        }
    }

    public List<OutStockOrderDetailInfo> getOrderDetailList() {
        return mOrderDetailList;
    }
    /**
     * @desc: 保存表头信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/30 18:09
     */
    public void setOrderHeaderInfo(OutStockOrderHeaderInfo orderHeaderInfo) {
        mOrderHeaderInfo = orderHeaderInfo;
    }

    public OutStockOrderHeaderInfo getOrderHeaderInfo() {
        return mOrderHeaderInfo;
    }

    /**
     * @desc: 获取单据明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestOrderDetail(OrderRequestInfo receiptModel, NetCallBackListener<String> callBackListener) {

    }

    /**
     * @desc: 校验并更新物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 13:40
     */
    public BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> checkAndUpdateMaterialInfo(OutStockOrderDetailInfo detailInfo, boolean isUpdate) {
        BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> resultInfo = new BaseMultiResultInfo<>();

        if (detailInfo != null) {
            String materialNo = detailInfo.getMaterialno() != null ? detailInfo.getMaterialno() : "";
//            String batchNo = detailInfo.getBatchno() != null ? detailInfo.getBatchno() : "";
            String rowNo = detailInfo.getRowno() != null ? detailInfo.getRowno() : "";
            String rowDel = detailInfo.getRownodel() != null ? detailInfo.getRownodel() : "";
            String erpVoucherNo = detailInfo.getErpvoucherno() != null ? detailInfo.getErpvoucherno() : "";

            for (OutStockOrderDetailInfo info : mOrderDetailList) {
                String sMaterialNo = detailInfo.getMaterialno() != null ? detailInfo.getMaterialno() : "";
                String sRowNo = detailInfo.getRowno() != null ? detailInfo.getRowno() : "";
                String sRowDel = detailInfo.getRownodel() != null ? detailInfo.getRownodel() : "";
                String sErpVoucherNo = detailInfo.getErpvoucherno() != null ? detailInfo.getErpvoucherno() : "";
                if (sMaterialNo.equals(materialNo)) {
                    if (sRowNo.equals(rowNo) && sRowDel.equals(rowDel)) {
                        if (erpVoucherNo.equals(sErpVoucherNo)) {
                            if (isUpdate) {
                                info.setRemainqty(detailInfo.getRemainqty());
                                info.setScanqty(ArithUtil.sub(detailInfo.getVoucherqty(), detailInfo.getRemainqty()));
                                resultInfo.setHeaderStatus(true);
                                break;
                            }
                        } else {
                            resultInfo.setMessage("校验物料行失败:条码的订单号[" + erpVoucherNo + "]不订单在中");
                            resultInfo.setHeaderStatus(false);
                            return resultInfo;
                        }
                    } else {
                        resultInfo.setMessage("校验物料行失败:物料号:[" + materialNo + "]的项次[" + sRowNo + "]或项序[" + rowDel + "]+不订单在中");
                        resultInfo.setHeaderStatus(false);
                        return resultInfo;
                    }
                } else {
                    resultInfo.setMessage("校验物料行失败:物料号:[" + materialNo + "]不订单在中");
                    resultInfo.setHeaderStatus(false);
                    return resultInfo;
                }
            }


        }
        return resultInfo;
    }

}
