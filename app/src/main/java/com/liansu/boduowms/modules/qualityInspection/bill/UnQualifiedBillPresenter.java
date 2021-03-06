package com.liansu.boduowms.modules.qualityInspection.bill;

import android.content.Context;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseFragment;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
import com.liansu.boduowms.debug.DebugModuleData;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class UnQualifiedBillPresenter {
    private Context                    mContext;
    private QualityInspectionBillModel mModel;
    private IQualityInspectionBillView mView;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public UnQualifiedBillPresenter(Context context, IQualityInspectionBillView view, MyHandler<BaseFragment> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new QualityInspectionBillModel(context, handler);

    }

    /**
     * @desc: 获取质检列表
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 17:44
     */
    public void getQualityInsHeaderList(final QualityHeaderInfo headerInfo) {
//        if (DebugModuleData.isDebugDataStatusOffline()) {
//        if (true) {
//            mModel.setQualityInspectionInfoList(DebugModuleData.loadQualityHeaderList());
//            mView.bindListView(mModel.getQualityInspectionInfoList());
//            return;
//        }
        mView.startRefreshProgress();
        mModel.requestQualityInspectionBillInfoList(headerInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
//                    LogUtil.WriteLog(QualityInspectionBill.class, mModel.TAG_GET_QUALITY_HEAD_LIST_SYNC, result);
                    BaseResultInfo<List<QualityHeaderInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<QualityHeaderInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult()==1) {
                        mModel.setQualityInspectionInfoList(returnMsgModel.getData());
                        if (DebugModuleData.isDebugDataStatusOffline()) {
                            mModel.setQualityInspectionInfoList(DebugModuleData.loadQualityHeaderList());
                        }
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
