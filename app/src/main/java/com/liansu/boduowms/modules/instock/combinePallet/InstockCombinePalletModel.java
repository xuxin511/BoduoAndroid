package com.liansu.boduowms.modules.instock.combinePallet;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;


/**
 * @ Des:
 * @ Created by yangyiqing on 2019/11/14.
 */
public class InstockCombinePalletModel extends BaseModel {

    public        String                           TAG_NewCreatedPalletNoQuery          = "CombinPallet_NewCreatedPalletNoQuery";
    public        String                           TAG_BarCodeInfo_Stock_Query          = "CombinPallet_BarCodeInfo_Stock_Query";
    public        String                           TAG_PalletInfoFromStockQuery         = "CombinPallet_PalletInfoFromStockQuery";
    public        String                           TAG_PalletInfoSave                   = "CombinPallet_PalletInfoSave";
    private final int                              Result_NewCreatedPalletNoQuery       = 106;
    private final int                              Result_BarCodeInfo_Stock_Query       = 107;
    private final int                              Result_PalletInfoFromStockQuery      = 108;
    private final int                              Result_PalletInfoSave                = 109;
    private       Map<String, NetCallBackListener> mNetMap                              = new HashMap<>();
    private       String                           mPalletNo;
    private       OutBarcodeInfo                   mPalletInfo;
    public static int                              COMBINE_PALLET_TYPE_RECEIPTION       = 1;  //入库
    public static int                              COMBINE_PALLET_TYPE_FINISHED_PRODUCT = 2;  //成品
    public static int                              COMBINE_PALLET_TYPE_NONE             = -1;  //异常
    private       OrderHeaderInfo                  mOrderInfo;
    private       List<OrderDetailInfo>            mOrderDetailList                     = new ArrayList<>();
    private       List<OutBarcodeInfo>             mScannedList                         = new ArrayList<>(); // 扫描托盘码的集合；
    private       int                              mCombinePalletType                   = -1;

    public InstockCombinePalletModel(Context context, MyHandler<BaseActivity> handler) {
       super(context,handler);
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

    public void setOrderInfo(OrderHeaderInfo orderInfo, List<OrderDetailInfo> detailInfos, int instockType) {
        mOrderInfo = orderInfo;
        mOrderDetailList.clear();
        mOrderDetailList.addAll(detailInfos);
        mCombinePalletType = instockType;
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
     * @desc: 查询条码信息带出托盘号
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/20 21:06
     */
    public void requestPalletInfoFromStockQuery(String barcode, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_PalletInfoFromStockQuery", callBackListener);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("barcode", barcode);  //条码
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_PalletInfoFromStockQuery, "正在查询托盘信息...", mContext, mHandler, Result_PalletInfoFromStockQuery, null, URLModel.GetURL().getStockBarcode, params, null);

    }

    /**
     * @desc: 提交组织信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/2/14 10:14
     */
    public void requestPalletInfoSave(List<OutBarcodeInfo> list, int type, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_PalletInfoSave", callBackListener);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("barcodeListJson", GsonUtil.parseModelListToJsonArray(list));  //条码
        params.put("type", type + "");  //1 新建  2. 更新
//        params.put("palletNo", palletNo);  //托盘码
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_PalletInfoFromStockQuery, "正在提交组托信息...", mContext, mHandler, Result_PalletInfoSave, null, URLModel.GetURL().saveCombinePalletInfos, params, null);
    }

    /**
     * @desc: 获取新生成的托盘码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/20 21:05
     */
    public void requestNewCreatedPalletNoQuery(OrderDetailInfo orderDetailInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_NewCreatedPalletNoQuery", callBackListener);
        final Map<String, String> params = new HashMap<String, String>();
//        LogUtil.WriteLog(ReceiptionScan.class, TAG_NewCreatedPalletNoQuery, "");
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_NewCreatedPalletNoQuery, "正在获取托盘信息...", mContext, mHandler, Result_NewCreatedPalletNoQuery, null, URLModel.GetURL().getPALLETNO, params, null);
    }

    /**
     * @desc: 保存条码信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/20 11:01
     */
    public void setBarcodeInfo(@NonNull OutBarcodeInfo barcodeInfo) {
        mScannedList.add(0, barcodeInfo);
    }

    /**
     * @desc: 保存组托信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/2 22:42
     */
    public void setPalletInfo(OutBarcodeInfo barcodeInfo) {
        mPalletInfo = barcodeInfo;
    }

    public OutBarcodeInfo getPalletInfo() {
        return mPalletInfo;
    }

    public List<OutBarcodeInfo> getScannedList() {
        return mScannedList;
    }

    public List<OrderDetailInfo> getOrderDetailList() {
        return mOrderDetailList;
    }

    public void setOrderDetailList() {
    }

    ;

    /**
     * @desc: 删除集合中的条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/2 23:19
     */
    public void removeList(OutBarcodeInfo info) {
        String deleteMaterialNo = info.getMaterialno()!=null?info.getMaterialno():"";
        String deleteBatchNo = info.getBatchno()!=null?info.getBatchno():"";
        Iterator<OutBarcodeInfo> iterator = mScannedList.iterator();
        while (iterator.hasNext()) {
            OutBarcodeInfo item = iterator.next();//必须在remove()之前，显式调用next()
            if (item != null) {
                String materialNo = item.getMaterialno()!=null?item.getMaterialno():"";
                String batchNo = item.getBatchno()!=null?item.getBatchno():"";
                if (materialNo.equals(deleteMaterialNo) && batchNo.equals(deleteBatchNo)) {
                    iterator.remove();
                    break;
                }
            }


        }
    }

    public void onClear() {
        mPalletNo = null;
        mScannedList.clear();
        mPalletInfo = null;
    }


    public void setPalletNo(String palletNo) {
        mPalletNo = palletNo;
    }

    public String getPalletNo() {
        return mPalletNo;
    }

    public String getSumNumbers() {
        if (mScannedList == null) mScannedList = new ArrayList<>();
        int count = mScannedList.size();
        int sumQty = 0;
        for (OutBarcodeInfo info : mScannedList) {
            sumQty += info.getQty();
        }
        return count + "/" + sumQty;
    }

    /**
     * @desc: 更新物料信息
     * @param: info 传入的条码信息
     * @return:
     * @author: Nietzsche
     * @time 2020/7/1 16:37
     */
    public BaseMultiResultInfo<Boolean, OrderDetailInfo> updateMaterialInfo(OutBarcodeInfo info) {
        BaseMultiResultInfo<Boolean, OrderDetailInfo> resultInfo = new BaseMultiResultInfo<>();
        try {
            if (info != null) {
                BaseMultiResultInfo<Boolean, OrderDetailInfo> isFindMaterialInfo = findMaterialInfo(info);
                if (isFindMaterialInfo.getHeaderStatus()) {
                    OrderDetailInfo updateInfo = isFindMaterialInfo.getInfo();
                    float qty = updateInfo.getScanqty();
                    qty += info.getQty();
                    updateInfo.setScanqty(qty);
                    List<OutBarcodeInfo> list = new ArrayList<>();
                    list.add(info);
                    updateInfo.setLstBarCode(list);
                    resultInfo.setHeaderStatus(true);
                    resultInfo.setInfo(updateInfo);
                } else {
                    resultInfo.setHeaderStatus(false);
                    resultInfo.setMessage(isFindMaterialInfo.getMessage());
                }
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("扫描的条码不能为空");
            }

        } catch (Exception e) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("绑定条码出现意料之外的异常：" + e.getMessage());
        }
        return resultInfo;
    }

    /**
     * @desc: 获取扫描外箱码相对应的物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/1 14:16
     */
    public BaseMultiResultInfo<Boolean, OrderDetailInfo> findMaterialInfo(OutBarcodeInfo info) {
        BaseMultiResultInfo<Boolean, OrderDetailInfo> resultInfo = new BaseMultiResultInfo<>();
        OrderDetailInfo sMaterialInfo = null;
        String barcodeMaterialNo = info.getMaterialno();
        String barcodeBatchNo = info.getBatchno() != null ? info.getBatchno() : "";
        float barcodeQty = info.getQty();
        try {

            if (barcodeMaterialNo == null || barcodeMaterialNo.equals("")) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的物料信息不能为空");
                return resultInfo;
            }

            if (info.getQty() < 0) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的数量必须大于0");
                return resultInfo;
            }
            for (int i = 0; i < mOrderDetailList.size(); i++) {
                OrderDetailInfo materialInfo = mOrderDetailList.get(i);
                if (materialInfo != null) {
                    String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
                    String batchNo = materialInfo.getBatchno() != null ? materialInfo.getBatchno() : "";
                    if (materialNo.trim().equals(barcodeMaterialNo.trim()) && batchNo.trim().equals(barcodeBatchNo.trim())) {
                        sMaterialInfo = materialInfo;
                        info.setUnit(sMaterialInfo.getUnit());
                        info.setMaterialdesc(sMaterialInfo.getMaterialdesc());
                        info.setRowno(sMaterialInfo.getRowno());
                        info.setErpvoucherno(sMaterialInfo.getErpvoucherno());
                        info.setStrongholdcode(sMaterialInfo.getStrongholdcode());
                        info.setStrongholdname(sMaterialInfo.getStrongholdname());
                        break;
                    }
                }
            }

            if (sMaterialInfo != null) {
                resultInfo.setHeaderStatus(true);
                resultInfo.setInfo(sMaterialInfo);
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("物料号:[" + barcodeMaterialNo + "],批次:[" + barcodeBatchNo + "]不在订单：" + mOrderInfo.getErpvoucherno() + "中,不能组托");
                return resultInfo;
            }


        } catch (Exception e) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("获取物料信息出现意料之外的异常:" + e.getMessage());
        }

        return resultInfo;
    }

    /**
     * @desc: 校验并且添加扫描的托盘标签
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 10:10
     */
    public BaseMultiResultInfo<Boolean, Void> setPalletBarcodeInfo(OutBarcodeInfo outBarcodeInfo) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        String barcodeMaterialNo = outBarcodeInfo.getMaterialno() != null ? outBarcodeInfo.getMaterialno() : "";
        String barcodeBatchNo = outBarcodeInfo.getBatchno() != null ? outBarcodeInfo.getBatchno() : "";
        float barcodeQty = outBarcodeInfo.getQty();
        boolean HAS_ORDER_MATERIAL = false;
        boolean HAS_SCANNED_MATERIAL = false;
        try {

            if (barcodeMaterialNo == null || barcodeMaterialNo.equals("")) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:托盘标签的物料信息不能为空");
                return resultInfo;
            }
            if (barcodeBatchNo == null || barcodeBatchNo.equals("")) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:托盘标签的物料信息不能为空");
                return resultInfo;
            }
            if (barcodeQty <= 0) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:托盘标签的数量必须大于0");
                return resultInfo;
            }

            //检验物料是否在订单中
            for (int i = 0; i < mOrderDetailList.size(); i++) {
                OrderDetailInfo materialInfo = mOrderDetailList.get(i);
                String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
                if (materialNo.trim().equals(barcodeMaterialNo.trim())) {
                    HAS_ORDER_MATERIAL = true;
                    break;
                }

            }
            if (!HAS_ORDER_MATERIAL) {
                if (barcodeBatchNo == null || barcodeBatchNo.equals("")) {
                    resultInfo.setHeaderStatus(false);
                    resultInfo.setMessage("校验条码失败:托盘标签的物料编码不在订单" + mOrderInfo.getErpvoucherno() + "中");
                    return resultInfo;
                }
            }

            //如果有就更新数量 ,如果没有就添加进集合
            for (int i = 0; i < mScannedList.size(); i++) {
                OutBarcodeInfo palletInfo = mScannedList.get(i);
                if (palletInfo != null) {
                    String materialNo = palletInfo.getMaterialno() != null ? palletInfo.getMaterialno() : "";
                    String batchNo = palletInfo.getBatchno() != null ? palletInfo.getBatchno() : "";
                    float qty = palletInfo.getQty();
                    if (materialNo.trim().equals(barcodeMaterialNo.trim()) && batchNo.trim().equals(barcodeBatchNo.trim())) {
                        float sumQty = ArithUtil.add(qty, barcodeQty);
                        palletInfo.setQty(sumQty);
                        HAS_SCANNED_MATERIAL = true;
                        break;
                    }
                }
            }

            if (HAS_SCANNED_MATERIAL == false) {
                mScannedList.add(outBarcodeInfo);
            }

            resultInfo.setHeaderStatus(true);
        } catch (Exception e) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("获取物料信息出现意料之外的异常:" + e.getMessage());
        }

        return resultInfo;
    }

    public BaseMultiResultInfo<Boolean, OrderDetailInfo> updateMaterialInfo(OrderDetailInfo orderDetailInfo, OutBarcodeInfo info) {
        BaseMultiResultInfo<Boolean, OrderDetailInfo> resultInfo = new BaseMultiResultInfo<>();
        OrderDetailInfo sMaterialInfo = null;
        String barcodeMaterialNo = info.getMaterialno();
        String barcodeBatchNo = info.getBatchno() != null ? info.getBatchno() : "";
        float barcodeQty = info.getQty();
        try {

            if (barcodeMaterialNo == null || barcodeMaterialNo.equals("")) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的物料信息不能为空");
                return resultInfo;
            }

            if (info.getQty() < 0) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的数量必须大于0");
                return resultInfo;
            }
            for (int i = 0; i < mOrderDetailList.size(); i++) {
                OrderDetailInfo materialInfo = mOrderDetailList.get(i);
                if (materialInfo != null) {
                    String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
                    String batchNo = materialInfo.getBatchno() != null ? materialInfo.getBatchno() : "";
                    if (materialNo.trim().equals(barcodeMaterialNo.trim()) && batchNo.trim().equals(barcodeBatchNo.trim())) {
                        sMaterialInfo = materialInfo;
                        break;
                    }
                }
            }

            if (sMaterialInfo != null) {  //找不到物料就新建
                resultInfo.setHeaderStatus(true);
                resultInfo.setInfo(sMaterialInfo);
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("物料号:[" + barcodeMaterialNo + "],批次:[" + barcodeBatchNo + "]不在订单：" + mOrderInfo.getErpvoucherno() + "中,不能组托");
                return resultInfo;
            }


        } catch (Exception e) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("获取物料信息出现意料之外的异常:" + e.getMessage());
        }

        return resultInfo;
    }

    /**
     * @desc: 是否手动创建新的物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/1 15:17
     */
    public boolean isCreateManuallyNewMaterialInfo(OutBarcodeInfo info) {
        if (info != null) {
            String barcodeMaterialNo = info.getMaterialno();
            String barcodeBatchNo = info.getBatchno() != null ? info.getBatchno() : "";
            float barcodeQty = info.getQty();
            if (barcodeMaterialNo != null && !barcodeMaterialNo.equals("")) {
                if (barcodeBatchNo.equals("") || barcodeQty == 0) {
                    return true;
                }
            }
        }

        return false;

    }

    /**
     * @desc: 校验条码重复
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 16:01
     */
    public BaseMultiResultInfo<Boolean, Void> checkBarcode(OutBarcodeInfo scanBarcode) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        if (scanBarcode != null) {
            //只有相同的
            String erpVoucherNo = mOrderInfo.getErpvoucherno() != null ? mOrderInfo.getErpvoucherno() : "";
            String barcodeErpVoucherNo = scanBarcode.getErpvoucherno() != null ? scanBarcode.getErpvoucherno() : "";
            if (!erpVoucherNo.equals(barcodeErpVoucherNo) && !barcodeErpVoucherNo.equals("")) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码编号[" + scanBarcode.getBarcode() + "]的订单号:[" + barcodeErpVoucherNo + "]和当前入库单号[" + erpVoucherNo + "]不符");
                return resultInfo;
            }
            //校验条码重复
            int index = mScannedList.indexOf(scanBarcode);
            if (index == -1) {
                resultInfo.setHeaderStatus(true);
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码编号[" + scanBarcode.getBarcode() + "]已扫描,无需重复扫描");
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
