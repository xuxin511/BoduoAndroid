package com.liansu.boduowms.modules.outstock.packingScan.packingList;

import android.content.Context;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.modules.print.PrintBusinessModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/28.
 */
public class PackingListPresenter {

    protected Context            mContext;
    protected PackingListModel   mModel;
    protected IPackingListView   mView;
    protected PrintBusinessModel mPrintModel;

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public PackingListPresenter(Context context, IPackingListView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new PackingListModel(context, handler);
        this.mPrintModel = new PrintBusinessModel(context, handler);

    }

    public PackingListModel getModel() {
        return mModel;
    }

    /**
     * @desc: 获取拼箱列表
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/12 18:11
     */
    public void getOrderDetailList() {
        mModel.requestPackingList(mModel.getOrderInfo(), new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
//    LogUtil.WriteLog(QualityInspectionProcessingScan.class, mModel.TAG_GetT_InStockDetailListByHeaderIDADF, result);
                try {
                    BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == BaseResultInfo.RESULT_TYPE_OK) {
                        List<OutStockOrderDetailInfo> list = returnMsgModel.getData();
                        if (list != null && list.size() > 0) {
                            mModel.setPackingList(list);
                            mView.bindListView(mModel.getPackingList());
                        } else {
                            MessageBox.Show(mContext, "获取到的拼箱列表为空" );
                        }

                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                    }
                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );
                }


            }
        });
    }

    /**
     * @desc: 拆箱
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/28 16:30
     */
    public void deletePackingNoInfo(OutStockOrderDetailInfo detailInfo) {
        mModel.requestDeletePackingInfo(detailInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                //    LogUtil.WriteLog(QualityInspectionProcessingScan.class, mModel.TAG_GetT_InStockDetailListByHeaderIDADF, result);
                try {
                    BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == BaseResultInfo.RESULT_TYPE_OK) {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                        getOrderDetailList();
                    } else {
                        MessageBox.Show(mContext, returnMsgModel.getResultValue() );
                    }

                } catch (Exception ex) {
                    MessageBox.Show(mContext, ex.getMessage() );
                }


            }
        });
    }

}


