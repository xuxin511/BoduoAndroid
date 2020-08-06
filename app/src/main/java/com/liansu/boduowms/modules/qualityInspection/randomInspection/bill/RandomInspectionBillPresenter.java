package com.liansu.boduowms.modules.qualityInspection.randomInspection.bill;

import android.content.Context;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class RandomInspectionBillPresenter {
    private Context                   mContext;
    private RandomInspectionBillModel mModel;
    private IRandomInspectionBillView mView;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public RandomInspectionBillPresenter(Context context, IRandomInspectionBillView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new RandomInspectionBillModel(context, handler);

    }

    /**
     * @desc: 获取抽检列表
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 17:44
     */
    public void getQualityInsHeaderList(final QualityHeaderInfo headerInfo) {
        headerInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        mModel.requestRandomInspectionBillInfoList(headerInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(RandomInspectionBill.class, mModel.TAG_GET_CHECK_QUALITY_HEAD_LIST_SYNC, result);
                    BaseResultInfo<List<QualityHeaderInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<QualityHeaderInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == 1) {
                        mModel.setQualityInspectionInfoList(returnMsgModel.getData());
//
                        if (mModel.getQualityInspectionInfoList().size() != 0) {
                            mView.sumBillCount(mModel.getQualityInspectionInfoList().size());
                            mView.bindListView(mModel.getQualityInspectionInfoList());
                        } else {

                        }

                    } else {
                        ToastUtil.show(returnMsgModel.getResultValue());
                    }
                } catch (Exception ex) {
                    ToastUtil.show(ex.getMessage());
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
