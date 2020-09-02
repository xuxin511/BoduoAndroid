package com.liansu.boduowms.modules.inHouseStock.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.telephony.MbmsGroupCallSession;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryConfigAdapter;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryHeadAdapter;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryModel;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.T_Parameter;
import com.liansu.boduowms.modules.outstock.Model.Outbarcode_Requery;
import com.liansu.boduowms.modules.outstock.Model.Pair;
import com.liansu.boduowms.modules.outstock.Model.PairAdapter;
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

import java.util.List;

import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryConfig_GetBarcodeInfo;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryConfig_GetWarehouse;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryHead_SelectLit;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_Project_GetParameter;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_InventoryConfig_GetBarcodeInfo;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_InventoryConfig_GetWarehouse;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_InventoryHead_SelectLit;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_Project_GetParameter;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_SalesNO;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

//盘点配置页面
@ContentView(R.layout.activity_inventory_config)
public class InventoryConfig extends BaseActivity {
    Context context = InventoryConfig.this;

    //盘点单号
    @ViewInject(R.id.inventory__config_order)
    TextView  inventory__config_order;

    //库位
    @ViewInject(R.id.inventory__config_warehouse)
    EditText inventory__config_warehouse;

    //条码
    @ViewInject(R.id.inventory__config_barcode)
    EditText inventory__config_barcode;

    //下拉框
    @ViewInject(R.id.inventory__config_status)
    Spinner mSpinner;
    private ArrayAdapter<Pair> mShstatusAdapter;

    //列表
    @ViewInject(R.id.inventory__config_list)
    ListView mList;

    //适配器
    InventoryConfigAdapter mAdapter;

    //当前库位
    String CurrAreano;


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.toolBarTitle = new ToolBarTitle("盘点单扫描", true);
        x.view().inject(this);
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        InventoryModel model = new InventoryModel();
        String arr = data.toString();
        model = GsonUtil.parseJsonToModel(arr, InventoryModel.class);
        inventory__config_order.setText(model.Erpvoucherno);
        T_Parameter parameter=new T_Parameter();
        parameter.Groupname="Stock_Status";
        String modelJson = parseModelToJson(parameter);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Project_GetParameter, "获取质检属性",
                context, mHandler, RESULT_Project_GetParameter, null, UrlInfo.getUrl().Project_GetParameter, modelJson, null);

    }

    @Override
    protected void initData() {
        super.initData();
    }


    //获取条码信息
    @Event(value = R.id.inventory__config_barcode,type = EditText.OnKeyListener.class)
    private  boolean getBarcode(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == inventory__config_barcode.getId()) {
            try {
                if(CurrAreano.equals("")) {
                    CommonUtil.setEditFocus(inventory__config_warehouse);
                    MessageBox.Show(context, "请先扫描库位");
                }
                String barcode=inventory__config_barcode.getText().toString().trim();
                if(!barcode.split("%")[4].equals("4")){
                    CommonUtil.setEditFocus(inventory__config_barcode);
                    MessageBox.Show(context, "请扫描正确托盘条码");
                }
                OutBarcodeInfo model=new OutBarcodeInfo();
                model.setBarcode(barcode);
                model.setSerialno(barcode.split("%")[3]);
                String modelJson = parseModelToJson(model);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_InventoryConfig_GetBarcodeInfo, "获取条码信息",
                context, mHandler, RESULT_InventoryConfig_GetBarcodeInfo, null, UrlInfo.getUrl().Inventory_Config_GetScanInfo, modelJson, null);

            } catch (Exception ex) {
                CommonUtil.setEditFocus(inventory__config_barcode);
                MessageBox.Show(context, "请扫描正确托盘条码");
                return true;
            }
        }
        return false;
    }

    //获取货位信息
    @Event(value = R.id.inventory__config_warehouse,type = EditText.OnKeyListener.class)
    private  boolean getWarehouse(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == inventory__config_warehouse.getId()) {
            try {
                InventoryModel model = new InventoryModel();
                model.Erpvoucherno=inventory__config_order.getText().toString().trim();
                model.Areano=inventory__config_warehouse.getText().toString().trim();
                String modelJson = parseModelToJson(model);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_InventoryConfig_GetWarehouse, "获取库位信息",
                        context, mHandler, RESULT_InventoryConfig_GetWarehouse, null, UrlInfo.getUrl().Inventory_Config_AreanobyCheckno, modelJson, null);

            } catch (Exception ex) {
                CommonUtil.setEditFocus(inventory__config_warehouse);
                MessageBox.Show(context, ex.toString());
                return true;
            }
        }
        return false;
    }




    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_InventoryConfig_GetWarehouse:
                GetWarehouse((String) msg.obj);
                break;
            case RESULT_InventoryConfig_GetBarcodeInfo:
                GetBarcode((String) msg.obj);
                break;
            case RESULT_Project_GetParameter:
                LoadSpinner((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;
        }
    }


    //加载下拉框
    public  void  LoadSpinner(String result) {
        try {
            BaseResultInfo<List<T_Parameter>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<T_Parameter>>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            } else {
                //初始化下拉框
                PairAdapter fydapter = new PairAdapter();
                fydapter.pairs = new Pair[returnMsgModel.getData().size()];
                int i = 1;
                for (T_Parameter item : returnMsgModel.getData()) {
                    fydapter.addPairs(i, String.valueOf(item.Parameterid), item.Parametername, fydapter.pairs);
                    i++;
                }
                fydapter.bindAdapter(mShstatusAdapter, mSpinner, fydapter.pairs, context);
              //  mSpinner.setcolor(Color.parseColor("#333333"));
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.toString());
        }
    }


    //获取库位信息
    public  void GetWarehouse(String result) {
        try {
            BaseResultInfo<List<InventoryModel>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<InventoryModel>>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(inventory__config_warehouse);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            } else {
                CurrAreano = inventory__config_warehouse.getText().toString().trim();
//                mAdapter = new InventoryHeadAdapter(context, returnMsgModel.getData());
//                mList.setAdapter(mAdapter);
                CommonUtil.setEditFocus(inventory__config_barcode);
            }
        } catch (Exception ex) {
            CommonUtil.setEditFocus(inventory__config_warehouse);
            MessageBox.Show(context, ex.toString());
        }
    }


    //获取条码信息
    public  void GetBarcode(String result) {
        try {
            BaseResultInfo<List<StockInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<StockInfo>>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(inventory__config_barcode);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            } else {
                //更新列表
                mAdapter = new InventoryConfigAdapter(context, returnMsgModel.getData());
                mList.setAdapter(mAdapter);
                //更新下拉框
                mSpinner.setSelection(returnMsgModel.getData().get(0).getStatus());
            }
        } catch (Exception ex) {
            CommonUtil.setEditFocus(inventory__config_barcode);
            MessageBox.Show(context, ex.toString());
        }
    }






}