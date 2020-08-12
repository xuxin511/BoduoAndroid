package com.liansu.boduowms.modules.qualityInspection.bill;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseFragment;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
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
public class QualifiedBillPresenter {
    private Context                    mContext;
    private QualityInspectionBillModel mModel;
    private IQualityInspectionBillView mView;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public QualifiedBillPresenter(Context context, IQualityInspectionBillView view, MyHandler<BaseFragment> handler) {
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
//    mView.startRefreshProgress();
        mModel.requestQualityInspectionBillInfoList(headerInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(QualityInspectionBill.class, mModel.TAG_GET_QUALITY_HEAD_LIST_SYNC, result);
                    BaseResultInfo<List<QualityHeaderInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<QualityHeaderInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() ==RESULT_TYPE_OK ) {
                        mModel.setQualityInspectionInfoList(returnMsgModel.getData());
                        if (mModel.getQualityInspectionInfoList().size() != 0 ) {
                            mView.sumBillCount(mModel.getQualityInspectionInfoList().size());
                            mView.bindListView(mModel.getQualityInspectionInfoList());
                            mView.onFilterContentFocus();
                        } else {
                            MessageBox.Show(mContext, "获取单据信息失败:获取的列表数据为空" , MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mView.onFilterContentFocus();
                                }
                            });
                        }

                    } else {
                        MessageBox.Show(mContext, "获取单据信息失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onFilterContentFocus();
                            }
                        });

                    }
                } catch (Exception ex) {
                    ToastUtil.show(ex.getMessage());
                }finally {
                    mView.onFilterContentFocus();
//                    mView.stopRefreshProgress();
                }

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
