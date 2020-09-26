package com.liansu.boduowms.modules.inHouseStock.inventory;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryConfigAdapter;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryModel;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.T_Parameter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryConfig_GetBarcodeInfo;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryConfig_GetWarehouse;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryDetail_Save_CheckDetail;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_Project_GetParameter;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_InventoryConfig_GetBarcodeInfo;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_InventoryConfig_GetWarehouse;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_InventoryDetail_Save_CheckDetail;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_Project_GetParameter;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

@ContentView(R.layout.activity_inventory_scann)
public class InventoryScann extends BaseActivity {

    Context context = InventoryScann.this;

    //盘点数量
    @ViewInject(R.id.inventory_scann_num)
    EditText inventory_scann_num;

    //库位
    @ViewInject(R.id.inventory_scann_warehouse)
    EditText inventory_scann_warehouse;

    //条码
    @ViewInject(R.id.inventory_scann_barcode)
    EditText inventory_scann_barcode;

    //托盘过账
    @ViewInject(R.id.inventory_scann_post)
    Button inventory_scann_post;


    //列表
    @ViewInject(R.id.inventory_sacnn)
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
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        InventoryModel model = new InventoryModel();
        String arr = data.toString();
        model = GsonUtil.parseJsonToModel(arr, InventoryModel.class);
        BaseApplication.toolBarTitle = new ToolBarTitle(model.Areano+"-"+BaseApplication.mCurrentWareHouseInfo.Warehouseno, true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        mInventory = new InventoryModel();
        CurrAreano = "";
        inventory_scann_num.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus==true) {
                    inventory_scann_num.setSelectAllOnFocus(true);
                    // CommonUtil.setEditFocus(inventory__config_num);
                }
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InventoryModel model = listModel.get(i);
                //先给值然后再赋值
                try {
                    //循环找到当前model
                    for (InventoryModel item : listModel) {
                        if (item.isCheck) {//找到之前选中的 然后值
                            if (inventory_scann_num.getText().toString().trim().equals("")) {
                                item.setScannQty(0f);
                            } else {
                                Float aFloat = Float.parseFloat(inventory_scann_num.getText().toString().trim());
                                item.setScannQty(aFloat);
                            }
                        }
                    }
                    for (InventoryModel item : listModel) {
                        if (item == model) {
                            item.isCheck=true;
                            inventory_scann_num.setText(item.getScannQty().toString());
                        }else{
                            item.isCheck=false;
                        }
                    }
                } catch (Exception ex) {
                    CommonUtil.setEditFocus(inventory_scann_num);
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
    @Event(value = R.id.inventory_scann_barcode,type = EditText.OnKeyListener.class)
    private  boolean getBarcode(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == inventory_scann_barcode.getId()) {
            try {
                if (CurrAreano.equals("")) {
                    CommonUtil.setEditFocus(inventory_scann_warehouse);
                    MessageBox.Show(context, "请先扫描库位");
                    return true;
                }
                String barcode = inventory_scann_warehouse.getText().toString().trim();
                if (!barcode.split("%")[4].equals("2")) {
                    CommonUtil.setEditFocus(inventory_scann_warehouse);
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
                CommonUtil.setEditFocus(inventory_scann_barcode);
                MessageBox.Show(context, "请扫描正确托盘条码");
                return true;
            }
        }
        return false;
    }





    //获取货位信息
    @Event(value = R.id.inventory_scann_warehouse,type = EditText.OnKeyListener.class)
    private  boolean getWarehouse(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == inventory_scann_warehouse.getId()) {
            try {
                InventoryModel model = new InventoryModel();
                model.Warehouseid=BaseApplication.mCurrentWareHouseInfo.getId();
                model.Areano=inventory_scann_warehouse.getText().toString().trim();
                String modelJson = parseModelToJson(model);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_InventoryConfig_GetWarehouse, "获取库位信息",
                        context, mHandler, RESULT_InventoryConfig_GetWarehouse, null, UrlInfo.getUrl().Inventory_GetAreamodel, modelJson, null);

            } catch (Exception ex) {
                CommonUtil.setEditFocus(inventory_scann_warehouse);
                MessageBox.Show(context, ex.toString());
                return true;
            }
        }
        return false;
    }


    //托盘提交
    @Event(value =R.id.inventory_scann_post)
    private void  inventory_submit(View view) {
        if (CurrAreano.equals("")) {
            CommonUtil.setEditFocus(inventory_scann_warehouse);
            MessageBox.Show(context, "请先输入或扫描库位");
            return;
        }
        if (listModel.size() == 0) {
            CommonUtil.setEditFocus(inventory_scann_barcode);
            MessageBox.Show(context, "请输入或扫描条码");
            return;
        }
        try {
            for (InventoryModel item : listModel) {
                if (item.isCheck) {//给当前的数量赋值到对象
                    if (inventory_scann_num.getText().toString().trim().equals("")) {
                        item.setScannQty(0f);
                    } else {
                        Float aFloat = Float.parseFloat(inventory_scann_num.getText().toString().trim());
                        item.setScannQty(aFloat);
                    }
                }
                if (item.getScannQty() == null) {
                    item.setScannQty(0f);
                }
            }
        }catch (Exception ex) {
            CommonUtil.setEditFocus(inventory_scann_num);
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
        for (InventoryModel item : postModel) {
            item.setQty(item.ScannQty);
            item.setErpvoucherno(Currerpvoucherno);
            item.setCreater(BaseApplication.mCurrentUserInfo.getUserno());
            item.setAreaid(CurrAreaid);
            item.setAreano(CurrAreano);
        }
        modelJson = parseModelToJson(postModel);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_InventoryDetail_Save_CheckDetail, "获取库位信息",
                context, mHandler, RESULT_InventoryDetail_Save_CheckDetail, null, UrlInfo.getUrl().Inventory_Savelist, modelJson, null);
    }





    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_InventoryConfig_GetWarehouse:
                GetWarehouse((String) msg.obj);
                break;
            case RESULT_InventoryConfig_GetBarcodeInfo:
                GetBarcode((String) msg.obj);
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
            listModel = new ArrayList<InventoryModel>();
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(inventory_scann_barcode);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                mAdapter = new InventoryConfigAdapter(context, listModel);
                mList.setAdapter(mAdapter);
                return;
            } else {
                List<InventoryModel> listModel = new ArrayList<InventoryModel>();
                mAdapter = new InventoryConfigAdapter(context, listModel);
                mList.setAdapter(mAdapter);
                CommonUtil.setEditFocus(inventory_scann_barcode);
                Toast.makeText(context, returnMsgModel.getResultValue(), Toast.LENGTH_SHORT).show();
                //   MessageBox.Show(context, returnMsgModel.getResultValue(),2,null);
                return;
            }
        } catch (Exception ex) {
            CommonUtil.setEditFocus(inventory_scann_barcode);
            MessageBox.Show(context, ex.toString());
        }
    }



    //

    //获取库位信息
    public  void GetWarehouse(String result) {
        try {
            BaseResultInfo<AreaInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<AreaInfo>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(inventory_scann_warehouse);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            } else {

                CommonUtil.setEditFocus(inventory_scann_barcode);
            }
        } catch (Exception ex) {
            CommonUtil.setEditFocus(inventory_scann_warehouse);
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
                CommonUtil.setEditFocus(inventory_scann_barcode);
                MessageBox.Show(context, returnMsgModel.getResultValue());
            } else {
                listModel = returnMsgModel.getData();
                listModel.get(0).isCheck = true;
                //更新列表
                //更新下拉框

                if( listModel.size()>0) {
                    inventory_scann_num.setText(listModel.get(0).getQty().toString());
                }
            }
        } catch (Exception ex) {
            CommonUtil.setEditFocus(inventory_scann_barcode);
            MessageBox.Show(context, ex.toString());
        }
        mAdapter = new InventoryConfigAdapter(context, listModel);
        mList.setAdapter(mAdapter);
        CommonUtil.setEditFocus(inventory_scann_num);
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