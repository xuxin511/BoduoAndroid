package com.liansu.boduowms.modules.inHouseStock.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.telephony.MbmsGroupCallSession;
import android.util.ArrayMap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxListRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockRequery;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.ArithUtil;
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

import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryConfig_GetBarcodeInfo;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryConfig_GetWarehouse;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryDetail_Save_CheckDetail;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryHead_SelectLit;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_Project_GetParameter;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_InventoryConfig_GetBarcodeInfo;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_InventoryConfig_GetWarehouse;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_InventoryDetail_Save_CheckDetail;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_InventoryHead_SelectLit;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_Project_GetParameter;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_parts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_SalesNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannParts_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitParts_Submit;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

//盘点配置页面
@ContentView(R.layout.activity_inventory_config)
public class InventoryConfig extends BaseActivity {
    Context context = InventoryConfig.this;

    //盘点单号
    @ViewInject(R.id.inventory__config_order)
    TextView  inventory__config_order;

    //盘点数量
    @ViewInject(R.id.inventory__config_num)
    EditText inventory__config_num;

    //库位
    @ViewInject(R.id.inventory__config_warehouse)
    EditText inventory__config_warehouse;

    //条码
    @ViewInject(R.id.inventory__config_barcode)
    EditText inventory__config_barcode;


    //托盘过账
    @ViewInject(R.id.inventory_config_post)
    Button inventory_config_post;

    //托盘明细
    @ViewInject(R.id.inventory_config_detail)
    Button inventory_config_detail;

    //下拉框
    @ViewInject(R.id.inventory__config_status)
    Spinner mSpinner;
    private ArrayAdapter<Pair> mShstatusAdapter;

    //列表
    @ViewInject(R.id.inventory__config_list)
    ListView mList;

    List<InventoryModel> listModel=new ArrayList<InventoryModel>();
    //适配器
    InventoryConfigAdapter mAdapter;

    //当前库位
    String CurrAreano="";
    int CurrAreaid;

    String Currerpvoucherno="";

    //下拉框列表
    Map downList=new HashMap<>();

    InventoryModel mInventory; //当前盘点对象

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle("盘点单扫描", true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        InventoryModel model = new InventoryModel();
        String arr = data.toString();
        model = GsonUtil.parseJsonToModel(arr, InventoryModel.class);
        inventory__config_order.setText(model.Erpvoucherno);
        Currerpvoucherno = model.Erpvoucherno;
        mInventory = new InventoryModel();
        CurrAreano = "";
        T_Parameter parameter = new T_Parameter();
        parameter.Groupname = "Stock_Status";
        String modelJson = parseModelToJson(parameter);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Project_GetParameter, "获取质检属性",
                context, mHandler, RESULT_Project_GetParameter, null, UrlInfo.getUrl().Project_GetParameter, modelJson, null);


//        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                InventoryModel inventoryModel = (InventoryModel) listModel.get(i);
//                if (inventoryModel != null) {
//                    //输入数量
//                    mInventory = inventoryModel;
//                    updateCheckNum(mInventory.getMaterialno() + "可盘数量为" +mInventory.getQty());
//                }
//                //   Toast.makeText(ListViewActivity.this,book.toString(),Toast.LENGTH_LONG).show();
//            }
//        });
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InventoryModel model = listModel.get(i);
                //先给值然后再赋值
                try {
                    for (InventoryModel item : listModel) {
                        if (item.isCheck) {
                            if (item != model) {
                                if (inventory__config_num.getText().toString().trim().equals("")) {
                                    item.setScannQty(0f);
                                } else {
                                    Float aFloat = Float.parseFloat(inventory__config_num.getText().toString().trim());
                                    item.setScannQty(aFloat);
                                }
                            }
                        }
                    }
                    for (InventoryModel item : listModel) {
                        if (item == model) {
                            inventory__config_num.setText(item.getScannQty().toString());
                        }
                    }
                } catch (Exception ex) {
                    CommonUtil.setEditFocus(inventory__config_num);
                    MessageBox.Show(context, "请输入正确的数量");
                    return;
                }
                mAdapter.notifyDataSetChanged();
            }
        });
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
                if (CurrAreano.equals("")) {
                    CommonUtil.setEditFocus(inventory__config_warehouse);
                    MessageBox.Show(context, "请先扫描库位");
                    return true;
                }
                String barcode = inventory__config_barcode.getText().toString().trim();
                if (!barcode.split("%")[4].equals("2")) {
                    CommonUtil.setEditFocus(inventory__config_barcode);
                    MessageBox.Show(context, "请扫描正确托盘条码");
                    return true;
                }
                OutBarcodeInfo model = new OutBarcodeInfo();
                model.setBarcode(barcode);
                model.setSerialno(barcode.split("%")[3]);
                model.setAreano(CurrAreano);
                model.setErpvoucherno(Currerpvoucherno);
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


    //托盘提交
    @Event(value =R.id.inventory_config_post)
    private void  inventory_submit(View view) {
        if (CurrAreano.equals("")) {
            CommonUtil.setEditFocus(inventory__config_warehouse);
            MessageBox.Show(context, "请先扫描库位");
            return;
        }
        if (listModel.size() == 0) {
            CommonUtil.setEditFocus(inventory__config_barcode);
            MessageBox.Show(context, "请先扫描扫描条码");
            return;
        }
        try {
            for (InventoryModel item : listModel) {
                if (item.isCheck) {//给当前的数量赋值到对象
                    if (inventory__config_num.getText().toString().trim().equals("")) {
                        item.setScannQty(0f);
                    } else {
                        Float aFloat = Float.parseFloat(inventory__config_num.getText().toString().trim());
                        item.setScannQty(aFloat);
                    }
                }
                if (item.getScannQty() == null) {
                    item.setScannQty(0f);
                }
            }
        }catch (Exception ex) {
            CommonUtil.setEditFocus(inventory__config_num);
            MessageBox.Show(context, "请输入正确的数量");
            return;
        }

//        if (!issacnning) {
//            CommonUtil.setEditFocus(inventory__config_barcode);
//            MessageBox.Show(context, "请先点击列表进行盘点");
//            return;
//        }

        List<InventoryModel> postModel = new ArrayList<InventoryModel>();
        String modelJson = parseModelToJson(listModel);
        postModel = GsonUtil.getGsonUtil().fromJson(modelJson, new TypeToken<List<InventoryModel>>() {
        }.getType());
        Pair ywpair = (Pair) mSpinner.getSelectedItem();
        for (InventoryModel item : postModel) {
            item.setQty(item.ScannQty);
            item.setErpvoucherno(Currerpvoucherno);
            item.setCreater(BaseApplication.mCurrentUserInfo.getUserno());
            item.setStatus(Integer.parseInt(ywpair.value));
            item.setAreaid(CurrAreaid);
            item.setAreano(CurrAreano);
        }
        modelJson = parseModelToJson(postModel);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_InventoryDetail_Save_CheckDetail, "获取库位信息",
                context, mHandler, RESULT_InventoryDetail_Save_CheckDetail, null, UrlInfo.getUrl().Inventory_Detail_Save_CheckDetail, modelJson, null);
    }


    //托盘明细
    @Event(value =R.id.inventory_config_detail)
    private void  inventory_detail(View view) {
        if (Currerpvoucherno.equals("")) {
            MessageBox.Show(context, "请先选择单号");
            return;
        }
        InventoryModel model = new InventoryModel();
        model.Erpvoucherno = Currerpvoucherno;
        //选择调页面
        Intent intent = new Intent();
        //intent.setData(data);
        //本地单号传过去
        String json = GsonUtil.parseModelToJson(model);
        Uri data = Uri.parse(json);
        intent.setData(data);
        intent.setClass(context, InventoryDetail.class);
        startActivity(intent);
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
            case RESULT_InventoryDetail_Save_CheckDetail:
                SaveDetail((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;
        }
    }


    //保存盘点信息
    public  void SaveDetail(String result) {
        try {
            BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
            }.getType());
            listModel=new ArrayList<InventoryModel>();
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(inventory__config_barcode);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                mAdapter = new InventoryConfigAdapter(context, listModel);
                mList.setAdapter(mAdapter);
                return;
            } else {
                List<InventoryModel> listModel = new ArrayList<InventoryModel>();
                mAdapter = new InventoryConfigAdapter(context, listModel);
                mList.setAdapter(mAdapter);
                CommonUtil.setEditFocus(inventory__config_barcode);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
        } catch (Exception ex) {
            CommonUtil.setEditFocus(inventory__config_barcode);
            MessageBox.Show(context, ex.toString());
        }
    }



    //加载下拉框
    public  void  LoadSpinner(String result) {
        downList=new HashMap();
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
                    downList.put(item.Parameterid, i);
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
                CurrAreaid=returnMsgModel.getData().get(0).getAreaid();
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
        listModel = new ArrayList<InventoryModel>();
        try {
            BaseResultInfo<List<InventoryModel>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<InventoryModel>>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(inventory__config_barcode);
                MessageBox.Show(context, returnMsgModel.getResultValue());
            } else {
                listModel = returnMsgModel.getData();
                listModel.get(0).isCheck = true;
                //更新列表
                //更新下拉框
                int type = returnMsgModel.getData().get(0).getStatus();
                int index = Integer.parseInt(downList.get(type).toString());
                mSpinner.setSelection(index - 1);
            }
        } catch (Exception ex) {
            CommonUtil.setEditFocus(inventory__config_barcode);
            MessageBox.Show(context, ex.toString());
        }
        mAdapter = new InventoryConfigAdapter(context, listModel);
        mList.setAdapter(mAdapter);
        inventory__config_num.setText("0");
        CommonUtil.setEditFocus(inventory__config_num);
    }



    //扫描或者输入数量
    private void updateCheckNum(String name) {
        final EditText inputServer = new EditText(this);
       inputServer.setMaxLines(1);
        inputServer.setSingleLine(true);
        inputServer.setFocusable(true);
        inputServer.setHint("请输入本次已盘数量");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //注册回车事件
        if(mInventory.ScannQty!=0 &&mInventory.ScannQty!=null) {
            inputServer.setText(mInventory.ScannQty.toString());
        }

        builder.setTitle(name).setIcon(
                null).setView(inputServer).setNegativeButton(
                "取消", null);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String Value = inputServer.getText().toString();
                            Float inputValue = Float.parseFloat(Value);
                            //盘赢
//                            if(ArithUtil.sub(mInventory.Qty,inputValue)<0) {
//                                MessageBox.Show(context, "盘点数量不能大于该物料可盘数量");
//                                return;
//                            }
                            for (InventoryModel item:  listModel){
                                if(item==mInventory) {
                                    item.ScannQty = inputValue;
                                }
                            }
                            mAdapter = new InventoryConfigAdapter(context, listModel);
                            mList.setAdapter(mAdapter);
                        } catch (Exception ex) {
                            MessageBox.Show(context, "请输入正确的数量");
                        }
                    }
                });
        builder.show();
        inputServer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                View vFocus = view.findFocus();
                int etid = vFocus.getId();
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == inputServer.getId()) {
                    return  true;
                  //  builder.
//                    try {
//                        builder.i
//                        String Value = inputServer.getText().toString().trim();
//                        Float inputValue = Float.parseFloat(Value);
//                        if(ArithUtil.sub(mInventory.Qty,inputValue)<0) {
//                            MessageBox.Show(context, "盘点数量不能大于该物料可盘数量");
//                            return true;
//                        }
//                        for (InventoryModel item:  listModel){
//                            if(item==mInventory) {
//                                item.ScannQty = inputValue;
//                            }
//                        }
//                        mAdapter = new InventoryConfigAdapter(context, listModel);
//                        mList.setAdapter(mAdapter);
//                        android.os.Process.killProcess(android.os.Process.myPid());
//                        System.exit(0);
//                    } catch (Exception ex) {
//                        MessageBox.Show(context, "请输入正确的数量");
//                        return true;
//                    }
                }
                return false;
            }
        });
    }





}