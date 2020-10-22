package com.liansu.boduowms.modules.pallet.combinePallet;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;


/**
 * @ Des:
 * @ Created by yangyiqing on 2019/11/14.
 */
public class CombinPalletModel {
    MyHandler<BaseActivity> mHandler;
    Context                 mContext;
    public        String                           TAG_NewCreatedPalletNoQuery          = "CombinPallet_NewCreatedPalletNoQuery";
    public        String                           TAG_BarCodeInfo_Stock_Query          = "CombinPallet_BarCodeInfo_Stock_Query";
    public        String                           TAG_PalletInfoFromStockQuery         = "CombinPallet_PalletInfoFromStockQuery";
    public        String                           TAG_PalletInfoSave                   = "CombinPallet_PalletInfoSave";
    private final int                              Result_NewCreatedPalletNoQuery       = 106;
    private final int                              Result_BarCodeInfo_Stock_Query       = 107;
    private final int                              Result_PalletInfoFromStockQuery      = 108;
    private final int                              Result_PalletInfoSave                = 109;
    private       Map<String, NetCallBackListener> mNetMap                              = new HashMap<>();
    private       List<OutBarcodeInfo>             mList                                = new ArrayList<>();
    private       String                           mPalletNo;
    private       OutBarcodeInfo                        mPalletInfo;
    public static int                              COMBINE_PALLET_TYPE_RAW_MATERIAL     =1;  //原料
    public static int                              COMBINE_PALLET_TYPE_FINISHED_PRODUCT =2;  //成品
    public CombinPalletModel(Context context, MyHandler<BaseActivity> handler) {
        mContext = context;
        mHandler = handler;
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
                MessageBox.Show(mContext, "获取请求失败_____" + msg.obj, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }

    /**
     * @desc: 获取新生成的托盘码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/20 21:05
     */
    public void requestNewCreatedPalletNoQuery(NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_NewCreatedPalletNoQuery", callBackListener);
        final Map<String, String> params = new HashMap<String, String>();
//        LogUtil.WriteLog(ReceiptionScan.class, TAG_NewCreatedPalletNoQuery, "");
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_NewCreatedPalletNoQuery, "正在获取托盘信息...", mContext, mHandler, Result_NewCreatedPalletNoQuery, null, URLModel.GetURL().getPALLETNO, params, null);
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
    public void requestPalletInfoSave(List<OutBarcodeInfo> list, String palletNo, int type, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_PalletInfoSave", callBackListener);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("barcodeListJson", GsonUtil.parseModelListToJsonArray(list));  //条码
        params.put("type", type + "");  //1 新建  2. 更新
        params.put("palletNo", palletNo);  //托盘码
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_PalletInfoFromStockQuery, "正在提交组托信息...", mContext, mHandler, Result_PalletInfoSave, null, URLModel.GetURL().saveCombinePalletInfos, params, null);
    }


    /**
     * @desc: 保存条码信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2019/11/20 11:01
     */
    public void setBarcodeInfo(@NonNull OutBarcodeInfo barcodeInfo) {
        mList.add(0, barcodeInfo);
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

    public List<OutBarcodeInfo> getList() {
        return mList;
    }

    /**
     * @desc:  删除集合中的条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/2 23:19
     */
    public void  removeList(OutBarcodeInfo info){
        String deleteMaterialNo=info.getMaterialno();
        String deleteBatchNo=info.getBatchno();
        Iterator<OutBarcodeInfo> iterator = mList.iterator();
        while (iterator.hasNext()) {
            OutBarcodeInfo item = iterator.next();//必须在remove()之前，显式调用next()
            if (item!=null){
                String materialNo=item.getMaterialno();
                String batchNo=item.getBatchno();
                if (materialNo.equals(deleteMaterialNo) && deleteBatchNo.equals(deleteBatchNo)){
                    iterator.remove();
                    break;
                }
            }


        }
    }
    public void onClear() {
        mPalletNo = null;
        mList.clear();
        mPalletInfo = null;
    }


    public void setPalletNo(String palletNo) {
        mPalletNo = palletNo;
    }

    public String getPalletNo() {
        return mPalletNo;
    }

    public String getSumNumbers() {
        if (mList == null) mList = new ArrayList<>();
        int count = mList.size();
        int sumQty = 0;
        for (OutBarcodeInfo info : mList) {
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
    public BaseMultiResultInfo<Boolean, Void> updateMaterialInfo(OutBarcodeInfo info) {
        BaseMultiResultInfo<Boolean, Void> resultInfo = new BaseMultiResultInfo<>();
        try {
            if (info != null) {
                BaseMultiResultInfo<Boolean, OutBarcodeInfo> isFindMaterialInfo = findMaterialInfo(info);
                if (isFindMaterialInfo.getHeaderStatus()) {
                    OutBarcodeInfo updateInfo = isFindMaterialInfo.getInfo();
                    float qty = updateInfo.getQty();
                    qty += info.getQty();
                    updateInfo.setQty(qty);
                    resultInfo.setHeaderStatus(true);
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
     * @desc: 获取扫描外箱码相对应的物料信息，如果没有就创建一个新的
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/1 14:16
     */
    public BaseMultiResultInfo<Boolean, OutBarcodeInfo> findMaterialInfo(OutBarcodeInfo info) {
        BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = new BaseMultiResultInfo<>();
        OutBarcodeInfo sMaterialInfo = null;
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
            for (int i = 0; i < mList.size(); i++) {
                OutBarcodeInfo materialInfo = mList.get(i);
                if (materialInfo != null) {
                    String materialNo = materialInfo.getMaterialno() != null ? materialInfo.getMaterialno() : "";
                    String batchNo = materialInfo.getBatchno() != null ? materialInfo.getBatchno() : "";
                    if (materialNo.trim().equals(barcodeMaterialNo.trim()) && batchNo.trim().equals(barcodeBatchNo.trim())) {
                        sMaterialInfo = materialInfo;
                        break;
                    }
                }
            }

            if (sMaterialInfo == null) {  //找不到物料就新建
                if (!barcodeBatchNo.equals("") && barcodeQty != 0) {
                    OutBarcodeInfo createMaterialInfo = new OutBarcodeInfo();
                    createMaterialInfo.setMaterialno(info.getMaterialno());
                    createMaterialInfo.setMaterialdesc(info.getMaterialdesc());
                    createMaterialInfo.setBatchno(info.getBatchno());
                    createMaterialInfo.setQty(0);
                    sMaterialInfo = createMaterialInfo;
                    mList.add(createMaterialInfo);
                    resultInfo.setHeaderStatus(true);
                    resultInfo.setInfo(sMaterialInfo);
                } else {
                    resultInfo.setHeaderStatus(false);
                    resultInfo.setMessage("获取物料信息出现意料之外的异常：无法自动创建没有批次和数量的物料信息");
                    return resultInfo;
                }
            } else {
                resultInfo.setHeaderStatus(true);
                resultInfo.setInfo(sMaterialInfo);
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


}
