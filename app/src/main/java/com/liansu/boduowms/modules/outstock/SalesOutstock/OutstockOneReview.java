package com.liansu.boduowms.modules.outstock.SalesOutstock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.outstock.Model.MenuOutStockModel;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockRequery;
import com.liansu.boduowms.modules.outstock.purchaseReturn.offscan.PurchaseReturnOffScanModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.GsonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_SalesNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESUL_Saleoutstock_OneReview;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESUL_Toutstock_Ordercolose_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_OneReview;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SelectNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_outstock_Ordercolose_Submit;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

@ContentView(R.layout.activity_outstock_one_review)
public class OutstockOneReview extends BaseActivity {
    Context context = OutstockOneReview.this;

    //订单号
    @ViewInject(R.id.outstock_onereview_order)
    EditText outstock_onereview_order;

    //列表框
    @ViewInject(R.id.outstock_onreview_listview)
    ListView mList;


    //listview    适配器
    SalesoutstockAdapter mAdapter;

    //当前单号
    private  String CurrOrderNO="";

    //存储类
    private PurchaseReturnOffScanModel mModel;

    String strongcode;

    //当前类型
    private int CurrVoucherType;
    UrlInfo info=new UrlInfo();
    MenuOutStockModel menuOutStockModel = new MenuOutStockModel();

    @Override
    protected void initViews() {
        super.initViews();
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        CurrOrderNO="";
        String arr = data.toString();
        menuOutStockModel = GsonUtil.parseJsonToModel(arr, MenuOutStockModel.class);
        int type = Integer.parseInt(menuOutStockModel.VoucherType);
        info.InitUrl(type);
        CurrVoucherType = type;
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(menuOutStockModel.Title + "-" + BaseApplication.mCurrentWareHouseInfo.Warehouseno, true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        mModel= new PurchaseReturnOffScanModel(context, mHandler);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    //订单回车
    @Event(value = R.id.outstock_onereview_order, type = EditText.OnKeyListener.class)
    private boolean palletKeyDowm(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        //如果是扫描
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == outstock_onereview_order.getId()) {
            try {
                String order = outstock_onereview_order.getText().toString().trim();
                if (order.equals("")) {
                    MessageBox.Show(context, "青先输入或扫描单号");
                    return true
                            ;
                }
                SalesoutstockRequery model = new SalesoutstockRequery();
                model.Erpvoucherno = order;
                model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
                model.Creater = BaseApplication.mCurrentUserInfo.getUsername();
                String json = GsonUtil.parseModelToJson(model);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SelectNO, "获取单据信息中",
                        context, mHandler, RESULT_Saleoutstock_SalesNO, null, info.SalesOutstock_ScanningNo, json, null);
                return true;

            } catch (Exception ex) {
                MessageBox.Show(context, ex.toString());
                CommonUtil.setEditFocus(outstock_onereview_order);
                return true;
            }
        }
        return false;
    }


    //一件复核
    @Event(value =R.id.outstock_onereview_button)
    private void  outstock_ordercolse_Submit(View view) {
        if (CurrOrderNO.equals("")) {
            CommonUtil.setEditFocus(outstock_onereview_order);
            MessageBox.Show(context, "请先输入或扫描单号");
            return;
        }
        IReview();
    }


    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Saleoutstock_SalesNO:
                SacnnNo((String) msg.obj);
                break;
            case RESUL_Saleoutstock_OneReview:
                Submit((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                MessageBox.Show(context, "获取请求失败_____" + msg.obj, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
        }
    }

    //扫描单号
    private  void SacnnNo(String result) {
        try {
            BaseResultInfo<OutStockOrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderHeaderInfo>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(outstock_onereview_order);
                //清空所有数据
                //据点集合
                //存储类
                mModel = new PurchaseReturnOffScanModel(context, mHandler);
                mAdapter = new SalesoutstockAdapter(context, mModel.getOrderDetailList());
                mList.setAdapter(mAdapter);
                //散件类
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            CurrOrderNO = outstock_onereview_order.getText().toString().trim();
            //成功
            List<OutStockOrderDetailInfo> detailInfos = new ArrayList<OutStockOrderDetailInfo>();
            strongcode=returnMsgModel.getData().getStrongholdcode();
            detailInfos = returnMsgModel.getData().getDetail();
            for (OutStockOrderDetailInfo item : returnMsgModel.getData().getDetail()) {
                item.setVouchertype(CurrVoucherType);
            }
            if (detailInfos.size() > 0) {
                //绑定
                mModel.setOrderHeaderInfo(returnMsgModel.getData());
                mModel.setOrderDetailList(returnMsgModel.getData().getDetail());
                mAdapter = new SalesoutstockAdapter(context, mModel.getOrderDetailList());
                mList.setAdapter(mAdapter);
            }
            CommonUtil.setEditFocus(outstock_onereview_order);
        } catch (Exception ex) {
            CommonUtil.setEditFocus(outstock_onereview_order);
            MessageBox.Show(context, ex.toString());
        }
    }

    //提交
    private  void Submit (String result) {
        try {
            BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(outstock_onereview_order);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            //成功后提示
            MessageBox.Show(context, returnMsgModel.getResultValue());
            SalesoutstockRequery model = new SalesoutstockRequery();
            model.Erpvoucherno = CurrOrderNO;
            model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
            model.Creater = BaseApplication.mCurrentUserInfo.getUsername();
            String json = GsonUtil.parseModelToJson(model);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SelectNO, "获取单据信息中",
                    context, mHandler, RESULT_Saleoutstock_SalesNO, null, info.SalesOutstock_ScanningNo, json, null);
        } catch (Exception ex) {
            CommonUtil.setEditFocus(outstock_onereview_order);
            MessageBox.Show(context, ex.toString());
        }
    }

    public void IReview() {
        new AlertDialog.Builder(this).setTitle("确定一键复核吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定触发的事件
                        SalesoutstockRequery model = new SalesoutstockRequery();
                        model.Erpvoucherno = CurrOrderNO;
                        model.Vouchertype = CurrVoucherType;
                        model.Creater=BaseApplication.mCurrentUserInfo.getUserno();
                        model.Towarehouseno=BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
                        String modelJson = parseModelToJson(model);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_OneReview, "提交复核中",
                                context, mHandler, RESUL_Saleoutstock_OneReview, null, UrlInfo.getUrl().SalesOutstock_Onereview, modelJson, null);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击取消触发的事件
                    }
                }).show();
    }

}