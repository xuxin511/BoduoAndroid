package com.liansu.boduowms.modules.instock.baseOrderBusiness.bill;

import android.content.Context;
import android.os.Message;

import com.liansu.boduowms.bean.order.OrderRequestInfo;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public abstract class BaseOrderBillChoicePresenter<V extends  IBaseOrderBillChoiceView,K extends BaseOrderBillChoiceModel> {
    protected Context            mContext;
    protected K                  mModel;
    protected V                  mView;
    private   String             mBusinessType = "";

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);

    }

    public BaseOrderBillChoicePresenter(Context context, V view,K model) {
        this.mContext = context;
        this.mView = view;
        this.mModel = model;

    }

    /**
     * @desc: 获取功能名称显示在 toolbar上
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 21:34
     */
    protected abstract String getTitle();

    /**
     * @desc: 载入静态数据，没有后台数据时测试用
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/13 23:30
     */
    protected  void loadDebugData(){};
    /**
     * @desc: 获取采购订单
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 17:44
     */
    public abstract  void getOrderHeaderList(final OrderRequestInfo orderHeaderInfo);

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

    public void onResume(){};
}
