package com.liansu.boduowms.modules.instock.productStorage.printPalletScan;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoice;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.modules.print.linkos.PrintType;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.function.ListUtils;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @desc: 生产入库扫描
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/7/15 10:40
 */
public class PrintPalletScanModel extends BaseModel {
    public        String                TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC        = "PrintPalletScanModel_GetT_WorkOrderHeadListADFAsync";
    public        String                TAG_CREATE_PALLET_NO_ADF_ASYNC                  = "PrintPalletScanModel_Create_PalletNoADFAsync";
    private final int                   RESULT_TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC = 122;
    private final int                   RESULT_TAG_CREATE_PALLET_NO_ADF_ASYNC           = 123;
    protected     String                mBusinessType                                   = "";
    protected     OrderHeaderInfo       mOrderHeaderInfo                                = null;
    protected     OrderHeaderInfo       mProductInfo                                    = null;
    protected     OrderDetailInfo       mDetailInfo                                     = null;
    protected     List<OrderDetailInfo> mOrderDetailList                                = new ArrayList<>();

    public PrintPalletScanModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }


    public void setBusinessType(String businessType) {
        mBusinessType = businessType;
    }

    public String getBusinessType() {
        return mBusinessType;
    }

    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_CREATE_PALLET_NO_ADF_ASYNC:
                listener = mNetMap.get("TAG_CREATE_PALLET_NO_ADF_ASYNC");
                break;
            case RESULT_TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC");
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }

    }

    public OrderHeaderInfo getOrderHeaderInfo() {
        return mOrderHeaderInfo;
    }

    public void setOrderHeaderInfo(OrderHeaderInfo mOrderHeaderInfo) {
        this.mOrderHeaderInfo = mOrderHeaderInfo;
    }

    public OrderHeaderInfo getProductInfo() {
        return mProductInfo;
    }

    public void setProductInfo(OrderHeaderInfo productInfo) {
        this.mProductInfo = productInfo;
        setOrderHeaderInfo(productInfo);
        mOrderDetailList.clear();
        if (mProductInfo != null && mProductInfo.getDetail() != null && mProductInfo.getDetail().size() > 0) {
            mOrderDetailList.addAll(mProductInfo.getDetail());
        }
    }

    public OrderDetailInfo getDetailInfo() {
        return mDetailInfo;
    }

    public void setDetailInfo(OrderDetailInfo mDetailInfo) {
        this.mDetailInfo = mDetailInfo;
    }


    public List<OrderDetailInfo> getOrderDetailList() {
        return mOrderDetailList;
    }

    /**
     * @desc: 获取报工单打印明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */

    public void requestOrderDetail(OrderHeaderInfo orderHeaderInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC", callBackListener);
        String ModelJson = GsonUtil.parseModelToJson(orderHeaderInfo);
        LogUtil.WriteLog(BaseOrderBillChoice.class, TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC, ModelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC, mContext.getString(R.string.MSG_GET_FINISHED_PRODUCT), mContext, mHandler, RESULT_TAG_GET_T_WORK_ORDER_HEAD_LIST_ADF_ASYNC, null, UrlInfo.getUrl().GetT_WorkOrderHeadListADFAsync, ModelJson, null);
    }


    /**
     * @desc: 实时外箱提交组托
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestCombineAndReferPallet(OrderDetailInfo info, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_CREATE_PALLET_NO_ADF_ASYNC", callBackListener);
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(PrintPalletScan.class, TAG_CREATE_PALLET_NO_ADF_ASYNC, parseModelToJson(info));
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_CREATE_PALLET_NO_ADF_ASYNC, mContext.getString(R.string.product_storage_print_request_barcode_info), mContext, mHandler, RESULT_TAG_CREATE_PALLET_NO_ADF_ASYNC, null, UrlInfo.getUrl().Create_PalletNoADFAsync, modelJson, null);
    }


    protected PrintInfo getPrintModel(OutBarcodeInfo outBarcodeInfo) {
        PrintInfo printInfo = null;
        if (outBarcodeInfo != null) {
            printInfo = new PrintInfo();
            String barcode = outBarcodeInfo.getBarcode() != null ? outBarcodeInfo.getBarcode() : "";
            String materialNo = outBarcodeInfo.getMaterialno() != null ? outBarcodeInfo.getMaterialno() : "";
            String batchNo = outBarcodeInfo.getBatchno() != null ? outBarcodeInfo.getBatchno() : "";
            int barcodeQty = (int) outBarcodeInfo.getQty();
//            String QRBarcode = materialNo+"%"+batchNo+"%"+barcodeQty+"%"+outBarcodeInfo.getBarcodetype();
            String QRBarcode = outBarcodeInfo.getBarcode();
            printInfo.setMaterialNo(materialNo);
            printInfo.setMaterialDesc(outBarcodeInfo.getMaterialdesc());
            printInfo.setBatchNo(batchNo);
            printInfo.setQty(barcodeQty);
            printInfo.setQRCode(QRBarcode);
            printInfo.setSignatory(BaseApplication.mCurrentUserInfo.getUsername());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            //获取当前时间
            Date date = new Date(System.currentTimeMillis());
            String arrivalTime = simpleDateFormat.format(date);
            printInfo.setArrivalTime(arrivalTime);
            printInfo.setSpec(outBarcodeInfo.getSpec());
            printInfo.setPrintType(PrintType.PRINT_TYPE_RAW_MATERIAL_STYLE);
        }
        return printInfo;
    }

    /**
     * @desc: 获取扫描外箱码相对应的物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/1 14:16
     */
    public BaseMultiResultInfo<Boolean, List<OrderDetailInfo>> findOutBarcodeInfoFromMaterial(OutBarcodeInfo info) {
        BaseMultiResultInfo<Boolean, List<OrderDetailInfo>> resultInfo = new BaseMultiResultInfo<>();
        List<OrderDetailInfo> sMaterialInfoList = new ArrayList<>();
        String barcodeMaterialNo = info.getMaterialno() != null ? info.getMaterialno() : "";
        String barcodeBatchNo = info.getBatchno() != null ? info.getBatchno() : "";
        try {


            for (int i = 0; i < mOrderDetailList.size(); i++) {
                OrderDetailInfo materialInfo = mOrderDetailList.get(i);
                if (materialInfo != null) {
                    String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
                    if (materialNo.trim().equals(barcodeMaterialNo.trim())) {
                        if (materialInfo.getRemainqty() != 0) {
                            sMaterialInfoList.add(materialInfo);
//                            info.setUnit(sMaterialInfo.getUnit());
//                            info.setMaterialno(sMaterialInfo.getMaterialno());
//                            info.setMaterialdesc(sMaterialInfo.getMaterialdesc());
//                            info.setRowno(sMaterialInfo.getRowno());
//                            info.setRownodel(sMaterialInfo.getRownodel());
//                            info.setErpvoucherno(sMaterialInfo.getErpvoucherno());
//                            info.setStrongholdcode(sMaterialInfo.getStrongholdcode());
//                            info.setStrongholdname(sMaterialInfo.getStrongholdname());
//                            info.setPackQty(sMaterialInfo.getPackQty());
//                            info.setSpec(sMaterialInfo.getSpec());

                        }


                    }
                }
            }

            if (sMaterialInfoList != null && sMaterialInfoList.size() > 0) {
                resultInfo.setHeaderStatus(true);
                resultInfo.setInfo(sMaterialInfoList);
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("物料号:[" + barcodeMaterialNo + "]不在订单：" + mOrderHeaderInfo.getErpvoucherno() + "中,不能组托");
                return resultInfo;
            }


        } catch (Exception e) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("获取物料信息出现意料之外的异常:" + e.getMessage());
        }

        return resultInfo;

    }

    /**
     * @desc: 找到扫描条码的所在行
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 11:01
     */
    public BaseMultiResultInfo<Boolean, OrderDetailInfo> findMaterialInfo(OutBarcodeInfo scanBarcode) {
        BaseMultiResultInfo<Boolean, OrderDetailInfo> resultInfo = new BaseMultiResultInfo<>();
        OrderDetailInfo sMaterialInfo = null;
        String barcodeMaterialNo = scanBarcode.getMaterialno() != null ? scanBarcode.getMaterialno() : "";
//        String barcodeBatchNo = scanBarcode.getBatchno() != null ? scanBarcode.getBatchno() : "";
        String barcodeRowNo = scanBarcode.getRowno() != null ? scanBarcode.getRowno() : "";
        String barcodeRowDel = scanBarcode.getRownodel() != null ? scanBarcode.getRownodel() : "";
//        String barcodeStrongHoldCode = scanBarcode.getStrongholdcode() != null ? scanBarcode.getStrongholdcode() : "";
//        String barcodeErpVoucherNo = scanBarcode.getErpvoucherno() != null ? scanBarcode.getErpvoucherno() : "";
        float barcodeQty = scanBarcode.getQty();
        try {

            if (barcodeMaterialNo == null || barcodeMaterialNo.equals("")) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的物料信息不能为空");
                return resultInfo;
            }

            if (barcodeQty < 0) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的数量必须大于0");
                return resultInfo;
            }
            //查找和条码的 物料和批次匹配的订单明细行的数据，只查第一个
            for (int i = 0; i < mOrderDetailList.size(); i++) {
                OrderDetailInfo materialInfo = mOrderDetailList.get(i);
                if (materialInfo != null) {
                    String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
//                    String batchNo = materialInfo.getBatchno() != null ? materialInfo.getBatchno() : "";
                    if (materialNo.trim().equals(barcodeMaterialNo.trim())) {
                        sMaterialInfo = materialInfo;
                        break;
                    }
                }
            }

            if (sMaterialInfo != null) {
                resultInfo.setHeaderStatus(true);
                resultInfo.setInfo(sMaterialInfo);
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:订单中没有物料号为:[" + barcodeMaterialNo + "],行号为:[" + barcodeRowNo + "]的物料行,扫描的条码序列号为:" + scanBarcode.getBarcode());
                return resultInfo;
            }
        } catch (Exception e) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("校验条码失败:出现预期之外的错误:" + e.getMessage());
            return resultInfo;
        }

        return resultInfo;
    }


    /**
     * @desc: 更新物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 13:40
     */
    public BaseMultiResultInfo<Boolean, Void> checkMaterialInfo(OrderDetailInfo detailInfo, OutBarcodeInfo scanBarcode, boolean isUpdate) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        String barcodeMaterialNo = scanBarcode.getMaterialno() != null ? scanBarcode.getMaterialno() : "";
        String barcodeBatchNo = scanBarcode.getBatchno() != null ? scanBarcode.getBatchno() : "";
        String barcodeRowNo = scanBarcode.getRowno() != null ? scanBarcode.getRowno() : "";
        String barcodeRowDel = scanBarcode.getRownodel() != null ? scanBarcode.getRownodel() : "";
        float barcodeScanQty = scanBarcode.getQty();
        if (detailInfo != null) {
            String materialNo = detailInfo.getMaterialno() != null ? detailInfo.getMaterialno() : "";
//            String batchNo = detailInfo.getBatchno() != null ? detailInfo.getBatchno() : "";
            String rowNo = detailInfo.getRowno() != null ? detailInfo.getRowno() : "";
            String rowDel = detailInfo.getRownodel() != null ? detailInfo.getRownodel() : "";
            if (materialNo.trim().equals(barcodeMaterialNo.trim())) {
                float scanQty = detailInfo.getScanqty();
                float remainQty = detailInfo.getRemainqty(); //订单的剩余扫描数量　　
                float hasRemainQty = ArithUtil.sub(detailInfo.getRemainqty(), detailInfo.getScanqty()); // (订单减去已扫描条码的)剩余扫描数量
                if (remainQty > 0) {
                    if (hasRemainQty > 0) {
                        if (ArithUtil.sub(hasRemainQty, barcodeScanQty) >= 0) {
                            if (isUpdate) {
                                detailInfo.setScanqty(ArithUtil.add(scanQty, barcodeScanQty));
                                detailInfo.setRemainqty(ArithUtil.sub(remainQty, barcodeScanQty));
                                detailInfo.setReceiveqty(ArithUtil.sub(detailInfo.getVoucherqty(), detailInfo.getRemainqty()));
                            } else {
                                List<OutBarcodeInfo> list = new ArrayList<>();
                                list.add(scanBarcode);
                                detailInfo.setLstBarCode(list);
                            }
                            resultInfo.setHeaderStatus(true);

                        } else {
                            resultInfo.setMessage("校验物料行失败:物料号:[" + barcodeMaterialNo + "],批次：[" + barcodeBatchNo + "],行号:[" + rowNo + "]的物料行的扫描数量不能大于订单剩余可扫描数量");
                            resultInfo.setHeaderStatus(false);
                            return resultInfo;
                        }
                    } else {
                        if (hasRemainQty == 0) {
                            resultInfo.setMessage("校验物料行失败:物料号:[" + barcodeMaterialNo + "],批次：[" + barcodeBatchNo + "],行号:[" + rowNo + "]的物料行已扫完毕");
                        } else {
                            resultInfo.setMessage("校验物料行出现异常:剩余扫描数量不能大于扫描数量!剩余数量[" + detailInfo.getRemainqty() + "],已扫描数量：[" + detailInfo.getScanqty() + "]");
                        }
                        resultInfo.setHeaderStatus(false);
                        return resultInfo;
                    }
                } else if (remainQty == 0) {
                    resultInfo.setMessage("校验物料行失败:物料号:[" + barcodeMaterialNo + "],批次：[" + barcodeBatchNo + "],行号:[" + rowNo + "]的物料行已扫完毕");
                    resultInfo.setHeaderStatus(false);
                    return resultInfo;
                } else {
                    resultInfo.setMessage("校验物料行出现异常:剩余扫描数量不能为负数!");
                    resultInfo.setHeaderStatus(false);
                    return resultInfo;
                }
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("更新物料行失败:订单中没有物料号为:[" + barcodeMaterialNo + "],批次为：[" + barcodeBatchNo + "],行号为:[" + barcodeRowNo + "]的物料行,扫描的条码序列号为:" + scanBarcode.getBarcode());
                return resultInfo;
            }


        } else {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("绑定条码信息失败:没有找到能匹配条码的物料号:[" + barcodeMaterialNo + "],批次：[" + barcodeBatchNo + "]的物料行");
            return resultInfo;

        }
        return resultInfo;
    }

    /**
     * @desc: 排序
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/18 16:09
     */
    public void sortDetailList(String materialNo) {
        String[] sortNameArr = {"Materialno", "Remainqty"};
        boolean[] isAscArr = {true, true};
        ListUtils.sort(mOrderDetailList, sortNameArr, isAscArr);
        List<OrderDetailInfo> taskDoneList = new ArrayList<>();
        List<OrderDetailInfo> currentMaterialList = new ArrayList<>();
        Iterator<OrderDetailInfo> it = mOrderDetailList.iterator();
        while (it.hasNext()) {
            OrderDetailInfo item = it.next();
            String sMaterialNo = item.getMaterialno() != null ? item.getMaterialno() : "";
            if (materialNo != null && materialNo.equals(sMaterialNo)) {
                if (item.getRemainqty() > 0 && ArithUtil.sub(item.getVoucherqty(), item.getRemainqty()) > 0) {
                    it.remove();
                    currentMaterialList.add(item);
                    continue;
                }
            }
            //变绿的沉底
            if (item.getRemainqty() == 0) {
                it.remove();
                taskDoneList.add(item);

            }

        }
        //扫描完毕的沉底
        if (taskDoneList.size() > 0) {
            mOrderDetailList.addAll(taskDoneList);

        }
        //正在扫描的物料放在最前面
        if (currentMaterialList.size() > 0) {
            mOrderDetailList.addAll(0, currentMaterialList);
        }

    }
}
