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
import android.widget.AdapterView;
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
import com.liansu.boduowms.modules.outstock.Model.OutStockDeleteReviewModel;
import com.liansu.boduowms.modules.outstock.Model.OutstockWaybillStatesAdapter;
import com.liansu.boduowms.modules.outstock.Model.OutstockWaybillStatusModel;
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

import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_GETBOXlISTl;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ReviewOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_SalesNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESUL_OTAG_OutStock_WayBill_ChangeOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESUL_OutStock_WayBill_GetOrderList;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESUL_Outstock_DeleteMaterial;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESUL_Saleoutstock_OneReview;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_OutStock_WayBill_ChangeOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_OutStock_WayBill_GetOrderList;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_OneReview;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SelectNO;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

//托运加单
@ContentView(R.layout.activity_outstock_waybill_states)
public class OutstockWaybillStates extends BaseActivity {
    Context context = OutstockWaybillStates.this;

    //订单号
    @ViewInject(R.id.outstock_waybill_order)
    EditText outstock_waybill_order;

    //列表框
    @ViewInject(R.id.outstock_waybill_listview)
    ListView mList;


    //listview    适配器
    OutstockWaybillStatesAdapter mAdapter;

    //当前单号
    private  String CurrOrderNO;

    List<OutstockWaybillStatusModel> mModel=new ArrayList<OutstockWaybillStatusModel>();


    OutstockWaybillStatusModel Currmodel=new OutstockWaybillStatusModel();
    //当前类型
    private int CurrVoucherType;
    UrlInfo info=new UrlInfo();
    MenuOutStockModel model = new MenuOutStockModel();

    @Override
    protected void initViews() {
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        String arr = data.toString();
        model = GsonUtil.parseJsonToModel(arr, MenuOutStockModel.class);
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(model.Title, true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        Currmodel=new OutstockWaybillStatusModel();
        //选择listview
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OutstockWaybillStatusModel model = mModel.get(i);
                Currmodel = model;
                for (OutstockWaybillStatusModel item : mModel) {
                    if (item == model) {
                        item.Ischeck = true;

                    } else {
                        item.Ischeck = false;
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mModel=new ArrayList<OutstockWaybillStatusModel>();
    }

    //订单回车
    @Event(value = R.id.outstock_waybill_order, type = EditText.OnKeyListener.class)
    private boolean palletKeyDowm(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        //如果是扫描
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == outstock_waybill_order.getId()) {
            try {
                String order = outstock_waybill_order.getText().toString().trim();
                if (order.equals("")) {
                    MessageBox.Show(context, "请先输入或扫描单号");
                    return true;
                }
                SalesoutstockRequery model = new SalesoutstockRequery();
                model.Erpvoucherno = order;
                String json = GsonUtil.parseModelToJson(model);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_OutStock_WayBill_GetOrderList, "获取单据信息中",
                        context, mHandler, RESUL_OutStock_WayBill_GetOrderList, null, info.OutStock_WayBill_GetOrderList, json, null);
                return true;

            } catch (Exception ex) {
                MessageBox.Show(context, ex.toString());
                CommonUtil.setEditFocus(outstock_waybill_order);
                return true;
            }
        }
        return false;
    }


    @Event(value =R.id.outstock_waybill_button)
    private void  outstock_waybill_Submit(View view) {
        if (CurrOrderNO.equals("")) {
            CommonUtil.setEditFocus(outstock_waybill_order);
            MessageBox.Show(context, "请先输入或扫描单号");
            return;
        }
        if (Currmodel == null) {
            CommonUtil.setEditFocus(outstock_waybill_order);
            MessageBox.Show(context, "请先选中托运单号");
            return;
        }
        IReview();
    }


    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESUL_OutStock_WayBill_GetOrderList://获取发货通知单对应的数据
                LoadOrderData((String) msg.obj);
                break;
            case RESUL_OTAG_OutStock_WayBill_ChangeOrder://回退托运单
                ReturnMsg((String) msg.obj);
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

    public  void  ReturnMsg(String result) {
        try {
            BaseResultInfo<List<OutstockWaybillStatusModel>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutstockWaybillStatusModel>>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            MessageBox.Show(context, "操作成功",2,null);
            //MessageBox.Show(context, "操作成功");
            SalesoutstockRequery model = new SalesoutstockRequery();
            model.Erpvoucherno = CurrOrderNO;
            String json = GsonUtil.parseModelToJson(model);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_OutStock_WayBill_GetOrderList, "获取单据信息中",
                    context, mHandler, RESUL_OutStock_WayBill_GetOrderList, null, info.OutStock_WayBill_GetOrderList, json, null);
            return;

        } catch (Exception ex) {
            CommonUtil.setEditFocus(outstock_waybill_order);
            MessageBox.Show(context, ex.toString());

        }
    }


    public  void  LoadOrderData(String result) {
        try {
            BaseResultInfo<OutstockWaybillStatusModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutstockWaybillStatusModel>>() {
            }.getType());
            mModel = new ArrayList<OutstockWaybillStatusModel>();
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(outstock_waybill_order);
                CurrOrderNO = "";
                Currmodel = new OutstockWaybillStatusModel();

                mAdapter = new OutstockWaybillStatesAdapter(context, mModel);
                mList.setAdapter(mAdapter);
                //散件类
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            CurrOrderNO = outstock_waybill_order.getText().toString().trim();
            //成功
            mModel.add( returnMsgModel.getData());
            if (mModel.size() > 0) {
                //绑定
                mModel.get(0).setIscheck(true);
                Currmodel = mModel.get(0);
                mAdapter = new OutstockWaybillStatesAdapter(context, mModel);
                mList.setAdapter(mAdapter);
            }
            CommonUtil.setEditFocus(outstock_waybill_order);
        } catch (Exception ex) {
            CommonUtil.setEditFocus(outstock_waybill_order);
            MessageBox.Show(context, ex.toString());
        }

    }

    public void IReview() {
        new AlertDialog.Builder(this).setTitle("确定回退托运单" + Currmodel.Erpvoucherno + "吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定触发的事件
                        SalesoutstockRequery model = new SalesoutstockRequery();
                        model.Erpvoucherno = Currmodel.Erpvoucherno;
                        String modelJson = parseModelToJson(model);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_OutStock_WayBill_ChangeOrder, "修改托运单状态中",
                                context, mHandler, RESUL_OTAG_OutStock_WayBill_ChangeOrder, null, UrlInfo.getUrl().OutStock_WayBill_CancelPrintWayBill, modelJson, null);
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