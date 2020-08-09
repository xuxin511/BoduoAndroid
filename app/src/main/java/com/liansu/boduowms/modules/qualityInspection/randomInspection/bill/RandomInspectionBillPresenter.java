package com.liansu.boduowms.modules.qualityInspection.randomInspection.bill;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_NONE;

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
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        mModel.setQualityInspectionInfoList(returnMsgModel.getData());
//
                        if (mModel.getQualityInspectionInfoList().size() != 0) {
                            mView.sumBillCount(mModel.getQualityInspectionInfoList().size());
                            mView.bindListView(mModel.getQualityInspectionInfoList());
                        } else {
                            MessageBox.Show(mContext,"获取单据失败", MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onFilterContentFocus();
                                }
                            });
                        }

                    } else {
                        MessageBox.Show(mContext,"获取单据失败:"+returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onFilterContentFocus();
                            }
                        });
                    }
                } catch (Exception ex) {
                    MessageBox.Show(mContext,"获取单据列表失败：出现预期之外的异常，"+ex.getMessage(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onFilterContentFocus();
                        }
                    });

            }}
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
