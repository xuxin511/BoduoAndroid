package com.liansu.boduowms.modules.outstock.baseOutStockBusiness.offScan;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.bean.QRCodeFunc.BARCODE_TYPE_PALLET_NO;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/28.
 */
public class BaseOutStockBusinessModel extends BaseModel {
    public static final int                           OUT_STOCK_SCAN_TYPE_TRAY                  = 1; //整托
    public static final int                           OUT_STOCK_SCAN_TYPE_OUTER_BOX             = 2; //外箱
    public static final int                           OUT_STOCK_SCAN_TYPE_SPARE_PARTS           = 3; //散件
    public static final int                           OUT_STOCK_SCAN_TYPE_COMBINE_TRAYS         = 4; //拼托
    public static final int                           OUT_STOCK_SCAN_TYPE_NONE                  = -1; //没有选择扫描模式
    private             List<OutStockOrderDetailInfo> mOrderDetailList                          = new ArrayList<>();  //单据明细
    private             OutStockOrderHeaderInfo       mOrderHeaderInfo;
    private             OutBarcodeInfo                mCurrentFatherInfo;  // 父级条码信息  托盘码或混托码
    private             OutBarcodeInfo                mCurrentSubInfo;    // 子级条码信息   外箱码或散件（69码或物料编码）
    private             List<OutStockOrderDetailInfo> mCurrentMaterialBatchNoList               = new ArrayList<>(); //散件获取多批次
    protected           int                           mScanType                                 = OUT_STOCK_SCAN_TYPE_NONE;
    public              String                        TAG_GET_T_SCAN_BARCODE_ADF_ASYNC          = "BaseOutStockBusinessModel_GetT_ScanBarcodeADFAsync";  // 获取条码信息
    public              String                        TAG_SELECT_MATERIAL                       = "BaseOutStockBusinessModel_SelectMaterial";  //获取物料信息
    public              String                        TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC_SUB = "TAG_SAVE_T_OUT_STOCK_DETAIL_ADF_ASYNC_SUB";  //提交条码信息
    private final       int                           RESULT_TAG_GET_T_SCAN_BARCODE_ADF_ASYNC   = 240;
    private final       int                           RESULT_TAG_SELECT_MATERIAL                = 241;

    public BaseOutStockBusinessModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_TAG_GET_T_SCAN_BARCODE_ADF_ASYNC:
                listener = mNetMap.get("TAG_GET_T_SCAN_BARCODE_ADF_ASYNC");
                break;
            case RESULT_TAG_SELECT_MATERIAL:
                listener = mNetMap.get("TAG_SELECT_MATERIAL");
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    /**
     * @desc: 保存单据表头信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 0:10
     */
    public void setOrderHeaderInfo(OutStockOrderHeaderInfo info) {
        mOrderHeaderInfo = info;
    }


    public OutStockOrderHeaderInfo getOrderHeaderInfo() {
        return mOrderHeaderInfo;
    }

    /**
     * @desc: 保存单据表体信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 0:10
     */
    public void setOrderDetailList(List<OutStockOrderDetailInfo> list) {
        mOrderDetailList.clear();
        if (list != null && list.size() > 0) {
            mOrderDetailList.addAll(list);
        }
    }

    /**
     * @desc: 保存父级条码信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 0:08
     */
    public void setCurrentFatherInfo(OutBarcodeInfo fatherInfo) {
        mCurrentFatherInfo = fatherInfo;
    }

    /**
     * @desc: 保存子级条码信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 0:08
     */
    public void setCurrentSubInfo(OutBarcodeInfo subInfo) {
        mCurrentSubInfo = subInfo;
    }

    /**
     * @desc: 获取父级条码信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 0:08
     */
    public OutBarcodeInfo getCurrentFatherInfo() {
        return mCurrentFatherInfo;
    }


    /**
     * @desc: 获取子级条码信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 0:09
     */
    public OutBarcodeInfo getCurrentSubInfo() {
        return mCurrentSubInfo;
    }


    /**
     * @desc: 保存当前操作类型
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 0:09
     */
    public void setCurrentScanType(int scanType) {
        mScanType = scanType;
    }

    /**
     * @desc: 获取当前操作类型
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 0:10
     */
    public int getCurrentScanType() {
        return mScanType;
    }

    /**
     * @desc: 报存当前批次集合
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 11:25
     */
    public void setCurrentMaterialBatchNoList(List<OutStockOrderDetailInfo> list) {
        mCurrentMaterialBatchNoList.clear();
        if (list != null && list.size() > 0) {
            mCurrentMaterialBatchNoList.addAll(list);
        }
    }

    public List<OutStockOrderDetailInfo> getCurrentMaterialBatchNoList() {
        return mCurrentMaterialBatchNoList;
    }


    /**
     * @desc: 获取出库明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestOrderDetailInfo(OutStockOrderHeaderInfo receiptModel, NetCallBackListener<String> callBackListener) {
    }


    /**
     * @desc: 实时提交扫描
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestBarcodeInfoRefer(OutStockOrderDetailInfo detailInfo, NetCallBackListener<String> callBackListener) {
    }

    /**
     * @desc: 查询物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestMaterialInfoQuery(String material, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_SELECT_MATERIAL", callBackListener);
        String modelJson = parseModelToJson(material);
        LogUtil.WriteLog(BaseOutStockBusiness.class, TAG_SELECT_MATERIAL, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SELECT_MATERIAL, mContext.getString(R.string.Msg_request_material_info), mContext, mHandler, RESULT_TAG_SELECT_MATERIAL, null, UrlInfo.getUrl().SelectMaterial, modelJson, null);

    }

    /**
     * @desc: 获取条码数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/24 15:32
     */
    public void requestPalletInfo(OutBarcodeInfo outBarcodeInfo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_T_SCAN_BARCODE_ADF_ASYNC", callBackListener);
        String modelJson = GsonUtil.parseModelToJson(outBarcodeInfo);
        LogUtil.WriteLog(BaseOutStockBusiness.class, TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_TAG_GET_T_SCAN_BARCODE_ADF_ASYNC, null, UrlInfo.getUrl().GetT_ScanBarcodeADFAsync, modelJson, null);

    }

    public List<OutStockOrderDetailInfo> getOrderDetailList() {
        return mOrderDetailList;
    }

    public List<String> getAddressList() {
        List<String> list = new ArrayList<>();
        return list;
    }

    /**
     * @desc: 设置扫描模式
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/26 15:13
     */
    public void setScanType(int scanType) {
        mScanType = scanType;
    }

    public int getScanType() {
        return mScanType;
    }

    /**
     * @desc: 找到扫描条码的所在行
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 11:01
     */
    public BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> findMaterialInfo2(OutBarcodeInfo scanFatherBarcode) {
        BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> resultInfo = new BaseMultiResultInfo<>();
        OutStockOrderDetailInfo sMaterialInfo = null;
        String barcodeMaterialNo = scanFatherBarcode.getMaterialno() != null ? scanFatherBarcode.getMaterialno() : "";
        String barcodeBatchNo = scanFatherBarcode.getBatchno() != null ? scanFatherBarcode.getBatchno() : "";
        float barcodeQty = scanFatherBarcode.getQty();
        try {

            if (barcodeMaterialNo == null || barcodeMaterialNo.equals("")) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的物料信息不能为空");
                return resultInfo;
            }
            if (barcodeBatchNo == null || barcodeBatchNo.equals("")) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的批次不能为空");
                return resultInfo;
            }
            if (barcodeQty < 0) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:条码的数量必须大于0");
                return resultInfo;
            }

            //查找和条码的 物料和批次匹配的订单明细行的数据，只查第一个
            for (int i = 0; i < mOrderDetailList.size(); i++) {
                OutStockOrderDetailInfo materialInfo = mOrderDetailList.get(i);
                if (materialInfo != null) {
                    String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
//                    String batchNo = materialInfo.get() != null ? materialInfo.getBatchno() : "";
                    if (!barcodeBatchNo.equals("")) {
                        if (materialNo.trim().equals(barcodeMaterialNo.trim())) {
                            sMaterialInfo = materialInfo;
                            break;
                        }
                    }

                }
            }

            if (sMaterialInfo != null) {
                resultInfo.setHeaderStatus(true);
                resultInfo.setInfo(sMaterialInfo);
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:订单中没有物料号为:[" + barcodeMaterialNo + "],批次为:[" + barcodeBatchNo + "]的物料行,扫描的条码为:" + scanFatherBarcode.getBarcode());
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
     * @desc: 校验 父级和子级条码  并 找到扫描条码的所在行
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 11:01
     */
    public BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> findMaterialInfo2(OutBarcodeInfo scanFatherBarcode, OutBarcodeInfo scanSubBarcode, int scanType) {
        BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> resultInfo = new BaseMultiResultInfo<>();
        OutStockOrderDetailInfo sMaterialInfo = null;
        String barcodeMaterialNo = scanFatherBarcode.getMaterialno() != null ? scanFatherBarcode.getMaterialno() : "";
        String barcodeBatchNo = scanFatherBarcode.getBatchno() != null ? scanFatherBarcode.getBatchno() : "";
        float barcodeQty = scanFatherBarcode.getQty();
        String barcodeSubMaterialNo = scanSubBarcode.getMaterialno() != null ? scanSubBarcode.getMaterialno() : "";
        String barcodeSubBatchNo = scanSubBarcode.getBatchno() != null ? scanSubBarcode.getBatchno() : "";
        float barcodeSubQty = scanSubBarcode.getQty();
        try {

            if (barcodeMaterialNo == null || barcodeMaterialNo.equals("")) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:父级条码的物料信息不能为空");
                return resultInfo;
            }
            if (barcodeBatchNo == null || barcodeBatchNo.equals("")) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:父级条码的批次不能为空");
                return resultInfo;
            }
            if (barcodeQty < 0) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:父级条码的数量必须大于0");
                return resultInfo;
            }

            if (scanType == OUT_STOCK_SCAN_TYPE_OUTER_BOX) {
                if (!barcodeMaterialNo.equals(barcodeSubMaterialNo) && !barcodeSubMaterialNo.equals("")) {
                    resultInfo.setHeaderStatus(false);
                    resultInfo.setMessage("校验条码失败:父级条码的物料编码[" + barcodeMaterialNo + "]和子级条码的物料编码[" + barcodeSubMaterialNo + "]不一致");
                    return resultInfo;
                }

                if (!barcodeBatchNo.equals(barcodeSubBatchNo) && !barcodeBatchNo.equals("")) {
                    resultInfo.setHeaderStatus(false);
                    resultInfo.setMessage("校验条码失败:父级条码的批次[" + barcodeBatchNo + "]和子级条码的批次[" + barcodeSubBatchNo + "]不一致");
                    return resultInfo;
                }
            }


            //查找和条码的 物料和批次匹配的订单明细行的数据，只查第一个
            for (int i = 0; i < mOrderDetailList.size(); i++) {
                OutStockOrderDetailInfo materialInfo = mOrderDetailList.get(i);
                if (materialInfo != null) {
                    String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
//                    String batchNo = materialInfo.getBatchno() != null ? materialInfo.getBatchno() : "";
                    if (!barcodeBatchNo.equals("")) {
                        if (materialNo.trim().equals(barcodeMaterialNo.trim())) {
                            sMaterialInfo = materialInfo;
                            break;
                        }
                    }

                }
            }

            if (sMaterialInfo != null) {
                if (scanType == OUT_STOCK_SCAN_TYPE_SPARE_PARTS) {
                    float packQty = sMaterialInfo.getPackQty();
                    if (ArithUtil.sub(packQty, barcodeSubQty) < 0) {
                        resultInfo.setHeaderStatus(false);
                        resultInfo.setMessage("校验条码失败:子级条码的数量[" + barcodeSubQty + "]和不能大于订单的外箱包装量[" + packQty + "]");
                        return resultInfo;
                    }
                }
                resultInfo.setHeaderStatus(true);
                resultInfo.setInfo(sMaterialInfo);
            } else {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("校验条码失败:订单中没有物料号为:[" + barcodeMaterialNo + "],批次为:[" + barcodeBatchNo + "]的物料行,扫描的条码为:" + scanFatherBarcode.getBarcode());
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
    /**
     * @desc: 校验并更新物料信息
     * @param: rowNo, rowDel, erpVoucherNo  校验项次，项序和单据号
     * @return:
     * @author: Nietzsche
     * @time 2020/7/5 13:40
     */
    public BaseMultiResultInfo<Boolean, Void> checkAndUpdateMaterialInfo(OutStockOrderDetailInfo detailInfo) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        if (detailInfo != null) {
            String rowNo = detailInfo.getRowno() != null ? detailInfo.getRowno() : "";
            String rowDel = detailInfo.getRownodel() != null ? detailInfo.getRownodel() : "";
            String erpVoucherNo = detailInfo.getErpvoucherno() != null ? detailInfo.getErpvoucherno() : "";
            boolean IS_EXIST = false;
            for (OutStockOrderDetailInfo info : mOrderDetailList) {
                String sRowNo = info.getRowno() != null ? info.getRowno() : "";
                String sRowDel = info.getRownodel() != null ? info.getRownodel() : "";
                String sErpVoucherNo = info.getErpvoucherno() != null ? info.getErpvoucherno() : "";
                if (sRowNo.equals(rowNo) && sRowDel.equals(rowDel)) {
                    if (erpVoucherNo.equals(sErpVoucherNo)) {
                        IS_EXIST = true;
                        info.setScanqty(detailInfo.getScanqty());
                        info.setRemainqty(ArithUtil.sub(info.getVoucherqty(), detailInfo.getScanqty()));
                        resultInfo.setHeaderStatus(true);
                        break;
                    } else {
                        resultInfo.setMessage("校验物料行失败:条码的订单号[" + erpVoucherNo + "]不订单在中");
                        resultInfo.setHeaderStatus(false);
                        return resultInfo;
                    }
                }
//                else {
//                    resultInfo.setMessage("校验物料行失败:物料的项次[" + rowNo + "]或项序[" + rowDel + "]+不订单在中");
//                    resultInfo.setHeaderStatus(false);
//                    return resultInfo;
//                }
            }
            if (!IS_EXIST) {
                resultInfo.setMessage("校验物料行失败:物料的项次[" + rowNo + "]或项序[" + rowDel + "]+不订单在中");
                resultInfo.setHeaderStatus(false);
                return resultInfo;
            }
        } else {
            resultInfo.setMessage("更新物料行失败:更新数据不能为空");
            resultInfo.setHeaderStatus(false);
            return resultInfo;
        }
        return resultInfo;
    }


    // 更新item
    public BaseMultiResultInfo<Boolean, Void> UpdateMaterialInfo(OutStockOrderDetailInfo detailInfo) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        if (detailInfo != null) {
            String rowNo = detailInfo.getRowno() != null ? detailInfo.getRowno() : "";
            String rowDel = detailInfo.getRownodel() != null ? detailInfo.getRownodel() : "";
            String erpVoucherNo = detailInfo.getErpvoucherno() != null ? detailInfo.getErpvoucherno() : "";
            boolean isexits = false;
            for (OutStockOrderDetailInfo info : mOrderDetailList) {
                String sRowNo = info.getRowno() != null ? info.getRowno() : "";
                String sRowDel = info.getRownodel() != null ? info.getRownodel() : "";
                String sErpVoucherNo = info.getErpvoucherno() != null ? info.getErpvoucherno() : "";
                if (sRowNo.equals(rowNo) && sRowDel.equals(rowDel)) {
                    isexits = true;
                    //复核没有单号
//                    if (erpVoucherNo.equals(sErpVoucherNo)) {
                    info.setScanqty(ArithUtil.add(info.getScanqty(), detailInfo.getScanqty()));
                    Float arr = ArithUtil.sub(info.getRemainqty(), detailInfo.getScanqty());
                    if (arr < 0) {
                        resultInfo.setHeaderStatus(false);
                        return resultInfo;
                    } else {
                        //info.setRemainqty(arr);
                    }
                    resultInfo.setHeaderStatus(true);
                    break;
                }
//                else {
//                    resultInfo.setMessage("校验物料行失败:物料的项次[" + sRowNo + "]或项序[" + rowDel + "]+不订单在中");
//                resultInfo.setHeaderStatus(false);
//                return resultInfo;
//                }
            }
            if (!isexits) {
                //   resultInfo.setMessage("校验物料行失败:物料的项次[" + rowNo + "]或项序[" + rowDel + "]+不订单在中");
                resultInfo.setHeaderStatus(false);
                return resultInfo;
            }
        } else {
//            resultInfo.setMessage("更新物料行失败:更新数据不能为空");
            resultInfo.setHeaderStatus(false);
            return resultInfo;
        }
        return resultInfo;
    }


    List<OutStockOrderDetailInfo> sortList=new ArrayList<OutStockOrderDetailInfo>();

    //更新item
    public  BaseMultiResultInfo<Boolean,Void> UpdateListViewItem(OutStockOrderDetailInfo detailInfo){
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        if(detailInfo!=null) {
            sortList = new ArrayList<OutStockOrderDetailInfo>();
            String materialno = detailInfo.getMaterialno() != null ? detailInfo.getMaterialno() : "";
            String batchno = detailInfo.getBatchno() != null ? detailInfo.getBatchno() : "";
            String erpVoucherNo = detailInfo.getErpvoucherno() != null ? detailInfo.getErpvoucherno() : "";
            String arrvoucherno = detailInfo.getArrvoucherNO() != null ? detailInfo.getArrvoucherNO() : "";
            sortList = mOrderDetailList;
            for (OutStockOrderDetailInfo info : mOrderDetailList) {
                String smaterialno = info.getMaterialno() != null ? info.getMaterialno() : "";
                String sbatchno = info.getBatchno() != null ? info.getBatchno() : "";
                String sErpVoucherNo = info.getErpvoucherno() != null ? info.getErpvoucherno() : "";
                String sarrvoucherno = info.getArrvoucherNO() != null ? info.getArrvoucherNO() : "";
                if (smaterialno.equals(materialno) && batchno.equals(sbatchno) && sErpVoucherNo.equals(erpVoucherNo) && sarrvoucherno.equals(arrvoucherno)) {
                    sortList.remove(info);
                    if (erpVoucherNo.equals(sErpVoucherNo)) {
                        info.setScanqty(detailInfo.getScanqty());
                        Float arr = ArithUtil.sub(info.getVoucherqty(), detailInfo.getScanqty());
                        if (arr < 0) {
                            resultInfo.setHeaderStatus(false);
                            return resultInfo;
                        } else {
                            info.setRemainqty(arr);
                        }
                        sortList.add(0, info);
                        resultInfo.setHeaderStatus(true);
                        return resultInfo;
                    }
                }
            }
        }
        resultInfo.setHeaderStatus(false);
        return resultInfo;
    }





    /**
     * @desc: 获取当前行的物料明细
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/5 17:52
     */
    public BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> findMaterialInfo(OutBarcodeInfo outBarcodeInfo) {
        BaseMultiResultInfo<Boolean, OutStockOrderDetailInfo> resultInfo = new BaseMultiResultInfo<>();
        String materialNo = outBarcodeInfo.getMaterialno() != null ? outBarcodeInfo.getMaterialno() : "";
        String batchNo = outBarcodeInfo.getBatchno() != null ? outBarcodeInfo.getBatchno() : "";
        OutStockOrderDetailInfo mMaterialInfo = null;
        for (OutStockOrderDetailInfo info : mOrderDetailList) {
            String sMaterialNo = info.getMaterialno() != null ? info.getMaterialno() : "";
            String sBatchNo = info.getBatchno() != null ? info.getBatchno() : "";
            if (!materialNo.equals("") && !batchNo.equals("")) { //批次托盘或  混托获取批次后再次请求方法
                if (sMaterialNo.equals(materialNo) && sBatchNo.equals(batchNo)) {
                    mMaterialInfo = info;
                    break;
                }
            } else {
                if (materialNo.equals("") && batchNo.equals("") && outBarcodeInfo.getBarcodetype() == BARCODE_TYPE_PALLET_NO) { //混托的情况不校验
                    mMaterialInfo = info;
                    break;
                }

            }
        }
        if (mMaterialInfo != null) {
            if (mMaterialInfo.getStrongholdcode() == null) {
                resultInfo.setHeaderStatus(false);
                resultInfo.setMessage("获取当前物料信息失败:和当前扫描的条码的物料编号[" + materialNo + "],批次[" + batchNo + " ]匹配的物料数据中据点为空!");
                return resultInfo;
            }
            resultInfo.setHeaderStatus(true);
            resultInfo.setInfo(mMaterialInfo);
        } else {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("获取当前物料号信息失败:扫描的条码的物料编号[" + materialNo + "],批次[" + batchNo + " ]不在单据中");
            return resultInfo;
        }

        return resultInfo;

    }

    /**
     * @desc: 下架提交
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/3 16:47
     */
    public void requestRefer(OrderRequestInfo info, NetCallBackListener<String> callBackListener) {
    }

    /**
     * @desc: 提交数据后, 清除当前扫描信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 0:11
     */
    public void clearCurrentScanInfo() {
        mCurrentFatherInfo = null;
        mCurrentSubInfo = null;
        mScanType = OUT_STOCK_SCAN_TYPE_NONE;
        mCurrentMaterialBatchNoList.clear();
    }

    /**
     * @desc: 获取物料批次
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/7 11:29
     */
    public BaseMultiResultInfo<Boolean, List<String>> getMaterialBatchNoList
    (List<OutStockOrderDetailInfo> orderDetailInfos, String materialNo) {
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
        for (OutStockOrderDetailInfo orderDetailInfo : orderDetailInfos) {
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
