package com.liansu.boduowms.modules.outstock.SalesOutstock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.util.ArrayMap;
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
import com.liansu.boduowms.modules.outstock.Model.Outbarcode_Requery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutStcokboxRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutsotckBxoListAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxListRequery;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_Box_Check_Box;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_Box_Check_waterCode;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_Box_SelectNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_Box_Submit_Box;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_DelBox;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_GetBoxList;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_PlatForm;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannParts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_Box_SelectNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_DelBox;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_GetBoxList;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

//拼箱列表
@ContentView(R.layout.activity_outstock_sales_boxlist)
public class SalesOutStockBoxList extends BaseActivity {
    Context context = SalesOutStockBoxList.this;

    private String CurrOrder;

    //订单框
    @ViewInject(R.id.sales_outstock_boxlist_order)
    EditText sales_outstock_boxlist_order;

    //列表框
    @ViewInject(R.id.out_stock_sales_box_ListView)
    ListView mList;

    List<SalesoutstockBoxListRequery> salesoutstockBoxListRequeries;

    SalesoutsotckBxoListAdapter mAdapter;

    SalesoutstockBoxListRequery delModel;

    int index;
    UrlInfo info = new UrlInfo();
    @Override
    protected void initData() {
        super.initData();
        //重写路径
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        Outbarcode_Requery model = new Outbarcode_Requery();
        String arr=data.toString();
        model = GsonUtil.parseJsonToModel(arr,Outbarcode_Requery.class);
        //初始化加载列表
        info.InitUrl(model.Vouchertype);
        sales_outstock_boxlist_order.setText(model.Barcode);
        if (!sales_outstock_boxlist_order.getText().toString().trim().equals("")) {
            final Map<String, String> map = new HashMap<String, String>();
            map.put("Erpvoucherno", model.Barcode);
            RequestHandler.addRequestWithDialog(Request.Method.GET, TAG_Saleoutstock_GetBoxList, "订单提交中",
                    context, mHandler, RESULT_Saleoutstock_GetBoxList, null, info.SalesOutstock_GetBoxList, map, null);
        }

    }


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.toolBarTitle = new ToolBarTitle("拼箱列表", true);
        x.view().inject(this);
        salesoutstockBoxListRequeries = new ArrayList<SalesoutstockBoxListRequery>();
        delModel=new SalesoutstockBoxListRequery();
        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                ISdel();
                return false;
            }
        });
    }

    @Event(value = R.id.sales_outstock_boxlist_order, type = EditText.OnKeyListener.class)
    private boolean orderKeyDowm(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        //如果是扫描
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == sales_outstock_boxlist_order.getId()) {
            try {
                String order = sales_outstock_boxlist_order.getText().toString().trim();
                if (!order.equals("")) {
                    SalesoutStcokboxRequery model = new SalesoutStcokboxRequery();
                    model.Erpvoucherno = order;
                    String modelJson = parseModelToJson(model);
                    final Map<String, String> map = new HashMap<String, String>();
                    map.put("Erpvoucherno", order);
                    RequestHandler.addRequestWithDialog(Request.Method.GET, TAG_Saleoutstock_GetBoxList, "订单提交中",
                            context, mHandler, RESULT_Saleoutstock_GetBoxList, null, info.SalesOutstock_GetBoxList, map, null);
                    return true;
                }
            } catch (Exception ex) {
                CommonUtil.setEditFocus(sales_outstock_boxlist_order);
                MessageBox.Show(context, ex.toString());
                return false;
            }
        }
        return false;
    }


    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Saleoutstock_GetBoxList:
                SacnnNo((String) msg.obj);
                break;
            case RESULT_Saleoutstock_DelBox:
                DelBox((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;
        }
    }

    public  void DelBox(String result) {
        BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
        }.getType());
        if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
            CommonUtil.setEditFocus(sales_outstock_boxlist_order);
            MessageBox.Show(context, returnMsgModel.getResultValue());
            return;
        }
        MessageBox.Show(context, "删除成功");
        if (delModel != null) {
            List<SalesoutstockBoxListRequery> list = new ArrayList<SalesoutstockBoxListRequery>();
            for (SalesoutstockBoxListRequery item : salesoutstockBoxListRequeries) {
                if (delModel.PackageSeq != item.PackageSeq) {
                    list.add(item);
                }
            }
            salesoutstockBoxListRequeries = new ArrayList<SalesoutstockBoxListRequery>();
            salesoutstockBoxListRequeries = list;
        }
        mAdapter = new SalesoutsotckBxoListAdapter(context, salesoutstockBoxListRequeries);
        mList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        CommonUtil.setEditFocus(sales_outstock_boxlist_order);
        CurrOrder = sales_outstock_boxlist_order.getText().toString().trim();
    }


    //订单扫描事件
    public void SacnnNo(String result) {
        BaseResultInfo<List<SalesoutstockBoxListRequery>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<SalesoutstockBoxListRequery>>>() {
        }.getType());
        if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
            CommonUtil.setEditFocus(sales_outstock_boxlist_order);
            MessageBox.Show(context, returnMsgModel.getResultValue());
            return;
        }
        salesoutstockBoxListRequeries=returnMsgModel.getData();
        mAdapter = new SalesoutsotckBxoListAdapter(context, salesoutstockBoxListRequeries);
        mList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        CommonUtil.setEditFocus(sales_outstock_boxlist_order);
        CurrOrder = sales_outstock_boxlist_order.getText().toString().trim();
    }


    public void ISdel() {
        new AlertDialog.Builder(this).setTitle("确定删除改行吗")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定触发的事件
                        SalesoutstockBoxListRequery model = (SalesoutstockBoxListRequery) mAdapter.getItem(index);
                        delModel=model;
                        model.Erpvoucherno=CurrOrder;
                        String modelJson = parseModelToJson(model);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_DelBox, "删除中",
                                context, mHandler, RESULT_Saleoutstock_DelBox, null, UrlInfo.getUrl().SalesOutstock_DelBox, modelJson, null);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击取消触发的事件
                        index = 0;
                    }
                }).show();
    }
}
