package com.liansu.boduowms.modules.instock.activeOtherStorage.bill;

import android.content.Context;
import android.content.DialogInterface;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoice;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoicePresenter;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.IBaseOrderBillChoiceView;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/13.
 */
public class ActiveOtherBillPresenter extends BaseOrderBillChoicePresenter<IBaseOrderBillChoiceView, ActiveOtherBillModel> {

    public ActiveOtherBillPresenter(Context context, IBaseOrderBillChoiceView view, MyHandler<BaseActivity> handler, String businssType) {
        super(context, view, new ActiveOtherBillModel(context, handler));

    }


    @Override
    protected String getTitle() {
        return mContext.getResources().getString(R.string.appbar_title_active_other_bill) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename();
    }

    @Override
    protected void loadDebugData() {
//        ArrayList<OrderHeaderInfo> list = DebugModuleData.loadReceiptHeaderList(OrderType.IN_STOCK_ORDER_TYPE_ACTIVE_OTHER_STORAGE);
//        mView.bindListView(list);
    }

    @Override
    public void getOrderHeaderList(OrderRequestInfo orderHeaderInfo) {
        mModel.requestInStockList(orderHeaderInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(BaseOrderBillChoice.class, mModel.TAG_GET_T_OTHER_HEAD_LIST_ADF_ASYNC, result);
                    BaseResultInfo<List<OrderHeaderInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OrderHeaderInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        mModel.setOrderInfoList(returnMsgModel.getData());
                        if (mModel.getOrderHeaderInfotList().size() == 1) {  //如果只有一条单据，直接跳转扫描界面
                            mView.sumBillCount(mModel.getOrderHeaderInfotList().size());
                            mView.StartScanIntent(mModel.getOrderHeaderInfotList().get(0), null);
                        }else if (mModel.getOrderHeaderInfotList().size() >1 ) {
                            mView.sumBillCount(mModel.getOrderHeaderInfotList().size());
                            mView.bindListView(mModel.getOrderHeaderInfotList());
                            mView.onFilterContentFocus();
                        }


                    } else {
                        MessageBox.Show(mContext,"获取单据失败:"+returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mView.onFilterContentFocus();
                            }
                        });
                    }
                } catch (Exception ex) {
                    MessageBox.Show(mContext,"获取单据失败:出现预期之外的异常，"+ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onFilterContentFocus();
                        }
                    });
                }

            }
        });
    }

    @Override
    public void onResume() {
        mView.setViewStatus(mView.getEditSupplierName(),false);
    }
}
