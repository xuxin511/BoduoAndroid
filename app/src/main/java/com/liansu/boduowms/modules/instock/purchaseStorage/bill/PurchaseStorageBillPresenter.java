package com.liansu.boduowms.modules.instock.purchaseStorage.bill;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;
import com.liansu.boduowms.bean.order.OrderRequestInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoice;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.BaseOrderBillChoicePresenter;
import com.liansu.boduowms.modules.instock.baseOrderBusiness.bill.IBaseOrderBillChoiceView;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/13.
 */
public class PurchaseStorageBillPresenter extends BaseOrderBillChoicePresenter<IBaseOrderBillChoiceView, PurchaseStorageBillModel> {

    public PurchaseStorageBillPresenter(Context context, IBaseOrderBillChoiceView view, MyHandler<BaseActivity> handler, String businssType) {
        super(context, view, new PurchaseStorageBillModel(context, handler));
    }


    @Override
    protected String getTitle() {
        return mContext.getResources().getString(R.string.appbar_title_receipt_bill) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
    }

    @Override
    protected void loadDebugData() {
//        ArrayList<OrderHeaderInfo> list = DebugModuleData.loadReceiptHeaderList(OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE);
//        mView.bindListView(list);
    }

    @Override
    public void getOrderHeaderList(OrderRequestInfo orderHeaderInfo) {
        orderHeaderInfo.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE);
        orderHeaderInfo.setStrongholdcode(BaseApplication.mCurrentWareHouseInfo.getStrongholdcode());
        orderHeaderInfo.setStrongholdName(BaseApplication.mCurrentWareHouseInfo.getStrongholdname());
        mModel.requestInStockList(orderHeaderInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(BaseOrderBillChoice.class, mModel.TAG_GetT_InStockList, result);
                    BaseResultInfo<List<OrderHeaderInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OrderHeaderInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        mModel.setOrderInfoList(returnMsgModel.getData());
                        if (mModel.getOrderHeaderInfotList().size() != 0 && mModel.getBarCodeList().size() != 0) {
                        } else {
                            if (mModel.getOrderHeaderInfotList().size() == 1) {  //如果只有一条单据，直接跳转扫描界面
                                mView.StartScanIntent(mModel.getOrderHeaderInfotList().get(0), null);
                            } else {
                                mView.sumBillCount(mModel.getOrderHeaderInfotList().size());
                                mView.bindListView(mModel.getOrderHeaderInfotList());
                            }
                        }
                        mView.onFilterContentFocus();
                    } else {

                       MessageBox.Show(mContext, "获取单据列表失败:" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               mView.onFilterContentFocus();
                           }
                       });
                    }
                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取单据列表失败：出现意料之外的异常-" + ex.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mView.onFilterContentFocus();
                        }
                    });
                }finally {

                }

            }
        });
    }
    @Override
    public void getOrderHeaderList2(OrderRequestInfo orderHeaderInfo, final EditText editText) {
        orderHeaderInfo.setVouchertype(OrderType.IN_STOCK_ORDER_TYPE_PURCHASE_STORAGE_VALUE);
        orderHeaderInfo.setStrongholdcode(BaseApplication.mCurrentWareHouseInfo.getStrongholdcode());
        orderHeaderInfo.setStrongholdName(BaseApplication.mCurrentWareHouseInfo.getStrongholdname());
        mModel.requestInStockList(orderHeaderInfo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                try {
                    LogUtil.WriteLog(BaseOrderBillChoice.class, mModel.TAG_GetT_InStockList, result);
                    BaseResultInfo<List<OrderHeaderInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OrderHeaderInfo>>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        mModel.setOrderInfoList(returnMsgModel.getData());
                        if (mModel.getOrderHeaderInfotList().size() != 0 && mModel.getBarCodeList().size() != 0) {
                        } else {
                            if (mModel.getOrderHeaderInfotList().size() == 1) {  //如果只有一条单据，直接跳转扫描界面
                                mView.StartScanIntent(mModel.getOrderHeaderInfotList().get(0), null);
                            } else {
                                mView.sumBillCount(mModel.getOrderHeaderInfotList().size());
                                mView.bindListView(mModel.getOrderHeaderInfotList());
                            }
                        }

                    } else {

                        MessageBox.Show(mContext, "获取单据列表失败:" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }
                } catch (Exception ex) {
                    MessageBox.Show(mContext, "获取单据列表失败：出现意料之外的异常-" + ex.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }finally {
                    CommonUtil.setEditFocus(editText);
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
