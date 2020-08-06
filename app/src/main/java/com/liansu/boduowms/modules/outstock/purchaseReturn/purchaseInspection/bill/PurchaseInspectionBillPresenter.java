package com.liansu.boduowms.modules.outstock.purchaseReturn.purchaseInspection.bill;

import android.content.Context;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.qualityInspection.bill.QualityInspectionBill;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class PurchaseInspectionBillPresenter {
    private Context                    mContext;
    private PurchaseInspectionBillModel mModel;
    private IPurchaseInspectionBillView mView;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public PurchaseInspectionBillPresenter(Context context, IPurchaseInspectionBillView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new PurchaseInspectionBillModel(context, handler);

    }

    /**
     * @desc: 获取质检列表
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 17:44
     */
    public void getQualityInsHeaderList(final OutStockOrderHeaderInfo headerInfo) {
        mView.startRefreshProgress();
        mModel.requestQualityInspectionBillInfoList(headerInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(QualityInspectionBill.class, mModel.TAG_GET_T_INSPEC_RETURN_LIST_ADF_ASYNC, result);
                    BaseResultInfo<List<OutStockOrderHeaderInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderHeaderInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult()==RESULT_TYPE_OK) {
                        mModel.setQualityInspectionInfoList(returnMsgModel.getData());
                        if (mModel.getQualityInspectionInfoList().size() != 0 && mModel.getBarCodeList().size() != 0) {
//
                        } else {
                            mView.sumBillCount(mModel.getQualityInspectionInfoList().size());
                            mView.bindListView(mModel.getQualityInspectionInfoList());
                        }

                    } else {
                        ToastUtil.show(returnMsgModel.getResultValue());

                    }
                } catch (Exception ex) {
                    ToastUtil.show(ex.getMessage());
                }finally {
                    mView.onFilterContentFocus();
                    mView.stopRefreshProgress();
                }
                mView.onFilterContentFocus();
            }
        });


    }

    /**
     * @desc: 重置界面和数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 18:32
     */
    public void onReset() {
        mView.onReset();
        mModel.onReset();
    }
}
