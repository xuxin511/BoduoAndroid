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
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.modules.outstock.Model.Outbarcode_Requery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutStcokboxRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutsotckBxoListAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxListRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockRequery;
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
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_SalesNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannParts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESUL_Saleoutstock_PrintBox;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_Box_SelectNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_DelBox;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_GetBoxList;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_PrintBox;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SelectNO;
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

    Outbarcode_Requery modelpbox = new Outbarcode_Requery();

    int index;
    UrlInfo info = new UrlInfo();


    @Override
    protected void initData() {
        super.initData();
        //重写路径
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();

        String arr = data.toString();
        modelpbox = GsonUtil.parseJsonToModel(arr, Outbarcode_Requery.class);
        //初始化加载列表
        info.InitUrl(modelpbox.Vouchertype);
        sales_outstock_boxlist_order.setText(modelpbox.Barcode);
        if (!sales_outstock_boxlist_order.getText().toString().trim().equals("")) {
            final Map<String, String> map = new HashMap<String, String>();
            map.put("Erpvoucherno", modelpbox.Barcode);
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
        delModel = new SalesoutstockBoxListRequery();
//        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                index = position;
//                ISdel();
//                return false;
//            }
//        });
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SalesoutstockBoxListRequery model = salesoutstockBoxListRequeries.get(i);
                for (SalesoutstockBoxListRequery item : salesoutstockBoxListRequeries) {
                    if (item == model) {
                        delModel = model;
                        item.isSelected = true;
                    } else {
                        item.isSelected = false;
                    }
                }
                mAdapter.notifyDataSetChanged();
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

    //删除拼箱
    @Event(value = R.id.outstock_plist_del)
    private void del_box(View view) {
        if (delModel != null) {
            if (delModel.PackageCode!=null) {
                ISdel();
            }else
            {
                CommonUtil.setEditFocus(sales_outstock_boxlist_order);
                MessageBox.Show(context, "请先选择列表箱号进行删除");
            }
        } else {
            CommonUtil.setEditFocus(sales_outstock_boxlist_order);
            MessageBox.Show(context, "请先选择列表箱号进行删除");
        }
    }

    //打印拼箱
    @Event(value = R.id.outstock_plist_print)
    private void print_box(View view) {
        if (delModel != null) {
            if (delModel.PackageCode!=null) {
                ISprint();
            }else
            {
                CommonUtil.setEditFocus(sales_outstock_boxlist_order);
                MessageBox.Show(context, "请先选择列表箱号进行打印");
            }
        } else {
            CommonUtil.setEditFocus(sales_outstock_boxlist_order);
            MessageBox.Show(context, "请先选择列表箱号进行打印");
        }
    }


    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Saleoutstock_GetBoxList:
                SacnnNo((String) msg.obj);
                break;
            case RESULT_Saleoutstock_DelBox:
                DelBox((String) msg.obj);
                break;
            case   RESUL_Saleoutstock_PrintBox:
                PrintBox((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;
        }
    }

    public void DelBox(String result) {
        BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
        }.getType());
        if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
            CommonUtil.setEditFocus(sales_outstock_boxlist_order);
            MessageBox.Show(context, returnMsgModel.getResultValue());
            return;
        }
        MessageBox.Show(context, "删除成功");

    //   mAdapter.notifyDataSetChanged();
        final Map<String, String> map = new HashMap<String, String>();
        map.put("Erpvoucherno", CurrOrder);
        //先清空列表
        RequestHandler.addRequestWithDialog(Request.Method.GET, TAG_Saleoutstock_GetBoxList, "订单提交中",
                context, mHandler, RESULT_Saleoutstock_GetBoxList, null, info.SalesOutstock_GetBoxList, map, null);
    }

    public  void PrintBox(String result){
        BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
        }.getType());
        if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
            CommonUtil.setEditFocus(sales_outstock_boxlist_order);
            MessageBox.Show(context, returnMsgModel.getResultValue());
            return;
        }
        MessageBox.Show(context, "打印成功");

    }


    //订单扫描事件
    public void SacnnNo(String result) {
        BaseResultInfo<List<SalesoutstockBoxListRequery>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<SalesoutstockBoxListRequery>>>() {
        }.getType());
        if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
            delModel = new SalesoutstockBoxListRequery();
            salesoutstockBoxListRequeries = new ArrayList<SalesoutstockBoxListRequery>();
            mAdapter = new SalesoutsotckBxoListAdapter(context, salesoutstockBoxListRequeries);
            mList.setAdapter(mAdapter);
            CommonUtil.setEditFocus(sales_outstock_boxlist_order);
            MessageBox.Show(context, returnMsgModel.getResultValue());
            return;
        }
        delModel = new SalesoutstockBoxListRequery();
        salesoutstockBoxListRequeries = returnMsgModel.getData();
        mAdapter = new SalesoutsotckBxoListAdapter(context, salesoutstockBoxListRequeries);
        mList.setAdapter(mAdapter);
        CommonUtil.setEditFocus(sales_outstock_boxlist_order);
        CurrOrder = sales_outstock_boxlist_order.getText().toString().trim();
    }


    public void ISdel() {
        new AlertDialog.Builder(this).setTitle("确定删除箱号" + delModel.PackageSeq + "吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定触发的事件
                     SalesoutstockBoxListRequery model = delModel;
                       // delModel = model;
                        model.Erpvoucherno = CurrOrder;
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
    public void ISprint() {
        new AlertDialog.Builder(this).setTitle("确定打印箱号" + delModel.PackageSeq + "吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定触发的事件
                        OutStockOrderDetailInfo model = new OutStockOrderDetailInfo();
                        model.setBatchno(delModel.PackageCode);
                        model.setPrintername(UrlInfo.mOutStockPackingBoxPrintName);
                        model.setPrintertype(UrlInfo.mOutStockPackingBoxPrintType);
                        String json = parseModelToJson(model);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_PrintBox, "打印中...",
                                context, mHandler, RESUL_Saleoutstock_PrintBox, null, info.SalesOutstock_PrintBox, json, null);
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
