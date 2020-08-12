package com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.bill;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.qualityInspection.bill.QualityInspectionBill;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class PurchaseInspectionBillPresenter {
    private Context                     mContext;
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
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        mModel.setQualityInspectionInfoList(returnMsgModel.getData());
                        if (mModel.getQualityInspectionInfoList().size() != 0 && mModel.getBarCodeList().size() != 0) {
//
                        } else {
                            mView.sumBillCount(mModel.getQualityInspectionInfoList().size());
                            mView.bindListView(mModel.getQualityInspectionInfoList());
                        }

                    } else {
                        MessageBox.Show(mContext, "获取订单失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onFilterContentFocus();
                            }
                        });

                    }
                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取订单失败:" + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onFilterContentFocus();
                        }
                    });


                } finally {
                    mView.onFilterContentFocus();
                    mView.stopRefreshProgress();
                }

            }
        });


    }


    /**
     * @desc: 获取采购验退单明细 (筛选)
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/12 18:11
     */
    public void getQualityInspectionDetailList(String erpVoucherNo) {
        mView.startRefreshProgress();
        OrderRequestInfo info = new OrderRequestInfo();
        info.setErpvoucherno(erpVoucherNo);
        info.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        mModel.requestQualityInspectionDetail(info, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(QualityInspectionBill.class, mModel.TAG_GET_T_INSPEC_RETURN_LIST_ADF_ASYNC, result);
                    BaseResultInfo<OutStockOrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderHeaderInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        List<OutStockOrderHeaderInfo> list = new ArrayList<>();
                        if (returnMsgModel.getData() != null) {
                            list.add(returnMsgModel.getData());
                            mModel.setQualityInspectionInfoList(list);
                        }

                        if (mModel.getQualityInspectionInfoList().size() != 0 && mModel.getBarCodeList().size() != 0) {
                        } else {
                            mView.sumBillCount(mModel.getQualityInspectionInfoList().size());
                            mView.bindListView(mModel.getQualityInspectionInfoList());
                        }

                    } else {
                        MessageBox.Show(mContext, "获取订单失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mModel.setQualityInspectionInfoList(null);
                                mView.bindListView(mModel.getQualityInspectionInfoList());
                                mView.onFilterContentFocus();
                            }
                        });


                    }
                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取订单失败:" + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onFilterContentFocus();
                        }
                    });


                } finally {
                    mView.onFilterContentFocus();
                    mView.stopRefreshProgress();
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
