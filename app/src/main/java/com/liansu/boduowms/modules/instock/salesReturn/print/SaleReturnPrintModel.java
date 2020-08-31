package com.liansu.boduowms.modules.instock.salesReturn.print;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.scan.BaseOrderScan;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.modules.print.linkos.PrintType;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/17.
 */
public class SaleReturnPrintModel extends BaseModel {
    public        String TAG_GET_T_SALE_RETURN_DETAIL_LIST_ADF_ASYNC        = "SaleReturnPrintModel_Post_GetT_SaleReturnDetailListADFAsync";  // 获取物料批次
    private final int    RESULT_TAG_GET_T_SALE_RETURN_DETAIL_LIST_ADF_ASYNC = 121;
    public        String TAG_PRINT_PALLET_NO                                = "SalesReturnStorageScanModel_Print_PalletNo";  //销售退货打印获取托盘
    private final int    RESULT_TAG_PRINT_PALLET_NO                         = 124;
    List<OrderDetailInfo> mOrderDetailsList           = new ArrayList<>();
    List<OrderDetailInfo> mCurrentMaterialDetailsList = new ArrayList<>();
    OutBarcodeInfo        mCurrentMaterialInfo;
    String                mCustomerCode               = null;

    public SaleReturnPrintModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_T_SALE_RETURN_DETAIL_LIST_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_SALE_RETURN_DETAIL_LIST_ADF_ASYNC");
                break;
            case RESULT_TAG_PRINT_PALLET_NO:
                listener = mNetMap.get("TAG_PRINT_PALLET_NO");
                break;

            case NetworkError.NET_ERROR_CUSTOM:
                MessageBox.Show(mContext, "获取请求失败_____" + msg.obj);
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }

    }

    /**
     * @desc: 获取客户编码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/6 11:25
     */
    public void setCustomerCode(String customerCode) {
        mCustomerCode = customerCode;
    }

    public String getCustomerCode() {
        return mCustomerCode;
    }

    public void setOrderDetailsList(List<OrderDetailInfo> list) {
        mOrderDetailsList.clear();
        if (list != null && list.size() > 0) {
            mOrderDetailsList.addAll(list);
        }
    }

    /**
     * @desc: 清空
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/31 16:48
     */
    public void onReset() {
        mCustomerCode = null;
        mOrderDetailsList.clear();
        mCurrentMaterialDetailsList.clear();
    }

    /**
     * @desc: 保存当前物料的批次信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/2 16:31
     */
    public void setMaterialDetailsList(List<OrderDetailInfo> list) {
        mCurrentMaterialDetailsList.clear();
        if (list != null && list.size() > 0) {
            mCurrentMaterialDetailsList.addAll(list);


        }
    }

    public List<OrderDetailInfo> getMaterialDetailsList() {
        return mCurrentMaterialDetailsList;
    }

    //物料编号（ERP获取）
//物料名称（ERP获取）
//物料规格（ERP获取）
//采购批次或供应商批次（手工录入）
//物料数量（手工录入）
    protected PrintInfo getPrintModel(PrintInfo info) {
        if (info != null) {
            String materialNo = info.getMaterialNo() != null ? info.getMaterialNo() : "";
            String materialDesc = info.getMaterialDesc() != null ? info.getMaterialDesc() : "";
            String batchNo = info.getBatchNo() != null ? info.getBatchNo() : "";
            String spec = info.getSpec() != null ? info.getSpec() : "";
            int barcodeQty = (int) info.getQty();
            String QRBarcode = materialNo + "%" + batchNo + "%" + barcodeQty + "%" + PrintType.PRINT_LABEL_TYPE_RAW_MATERIAL;
            info.setMaterialNo(materialNo);
            info.setMaterialDesc(info.getMaterialDesc());
            info.setBatchNo(batchNo);
            info.setQty(barcodeQty);
            info.setQRCode(QRBarcode);
            info.setSpec(spec);
            info.setPrintType(PrintType.PRINT_TYPE_RAW_MATERIAL_STYLE);
        }
        return info;


    }


    /**
     * @desc: 根据物料编码获取规格
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/17 10:10
     */
    public String getSpec(String materialNo) {
        String spec = "";
        if (materialNo == null) materialNo = "";
        for (OrderDetailInfo info : mOrderDetailsList) {
            if (info != null) {
                String sMaterialNo = info.getMaterialno() != null ? info.getMaterialno() : "";
                if (sMaterialNo.trim().equals(materialNo.trim())) {
                    spec = info.getSpec() != null ? info.getSpec() : "";
                }

            }
        }

        return spec;
    }

    /**
     * @desc: 获取物料批次信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestMaterialBatchNoListInfo(OrderRequestInfo outBarcodeInfo, NetCallBackListener<String> callBackListener) {
        outBarcodeInfo.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_SALES_RETURN_STORAGE_VALUE);
        mNetMap.put("TAG_GET_T_SALE_RETURN_DETAIL_LIST_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(outBarcodeInfo);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_GET_T_SALE_RETURN_DETAIL_LIST_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_SALE_RETURN_DETAIL_LIST_ADF_ASYNC, mContext.getString(R.string.message_request_material_info), mContext, mHandler, RESULT_TAG_GET_T_SALE_RETURN_DETAIL_LIST_ADF_ASYNC, null, UrlInfo.getUrl().GetT_SaleReturnDetailListADFAsync, modelJson, null);
    }

    /**
     * @desc: 打印
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 18:46
     */
    public void requestPrint(OrderDetailInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_PRINT_PALLET_NO", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(BaseOrderScan.class, TAG_PRINT_PALLET_NO, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_PRINT_PALLET_NO, mContext.getString(R.string.message_request_print), mContext, mHandler, RESULT_TAG_PRINT_PALLET_NO, null, UrlInfo.getUrl().CreateT_OutBarcodeADFAsync, modelJson, null);

    }


    public BaseMultiResultInfo<Boolean, List<String>> getMaterialBatchNoList(List<OrderDetailInfo> orderDetailInfos, String materialNo) {
        BaseMultiResultInfo<Boolean, List<String>> resultInfo = new BaseMultiResultInfo<>();
        List<String> list = null;
        if (orderDetailInfos == null || orderDetailInfos.size() == 0) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("获取物料批次列表失败:传入的物料批次集合信息为空");
            return resultInfo;
        }
        if (materialNo == null) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("获取物料批次列表失败:传入的物料编码为空");
            return resultInfo;
        }


        list = new ArrayList<>();
        for (OrderDetailInfo orderDetailInfo : orderDetailInfos) {
            if (orderDetailInfo != null) {
                String sMaterialNo = orderDetailInfo.getMaterialno() != null ? orderDetailInfo.getMaterialno() : "";
                String sBatchNo = orderDetailInfo.getBatchno() != null ? orderDetailInfo.getBatchno() : "";
                if (sMaterialNo.trim().equals(materialNo) && !sBatchNo.equals("")) {
                    if (!list.contains(sBatchNo)) {
                        list.add(sBatchNo);
                    }
                }
            }
        }


        if (list == null || list.size() == 0) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("获取物料批次列表失败:没有在集合中找到和条码的物料编号:" + materialNo + "相匹配的批次");
            return resultInfo;
        } else {
            resultInfo.setHeaderStatus(true);
            resultInfo.setInfo(list);
        }

        return resultInfo;
    }

}
