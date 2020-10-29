package com.liansu.boduowms.modules.inHouseStock.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.bluetooth.le.ScanSettings;
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
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryConfigAdapter;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryListAdapter;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryModel;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.T_Parameter;
import com.liansu.boduowms.modules.outstock.Model.Pair;
import com.liansu.boduowms.modules.outstock.Model.PairAdapter;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryConfig_GetBarcodeInfo;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryConfig_GetWarehouse;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryDetail_Save_CheckDetail;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryList_GetWarehouse;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_Project_GetParameter;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_InventoryConfig_GetBarcodeInfo;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_InventoryConfig_GetWarehouse;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_InventoryDetail_Save_CheckDetail;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_Project_GetParameter;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_DelBox;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESUL_Saleoutstock_PrintBox;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_DelBox;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_PrintBox;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;


//明盘操作
@ContentView(R.layout.activity_inventory_list)
public class InventoryList extends BaseActivity {
    Context context = InventoryList.this;
    //盘点单号
    @ViewInject(R.id.inventory_list_order)
    TextView inventory_list_order;

    //盘点数量
    @ViewInject(R.id.inventory_list_num)
    EditText inventory_list_num;

    //库位
    @ViewInject(R.id.inventory_list_warehouse)
    EditText inventory_list_warehouse;

    //条码
    @ViewInject(R.id.inventory_list_barcode)
    EditText inventory_list_barcode;


    //托盘过账
    @ViewInject(R.id.inventory_list_post)
    Button inventory_config_post;

    //下拉框
    @ViewInject(R.id.inventory_list_status)
    Spinner mSpinner;
    private ArrayAdapter<Pair> mShstatusAdapter;

    //列表
    @ViewInject(R.id.inventory_list)
    ListView mList;

    //盘点列表
    List<InventoryModel> listModel=new ArrayList<InventoryModel>();

    //适配器
    InventoryListAdapter mAdapter;

    //当前库位
    String CurrAreano="";
    int CurrAreaid;

    String Currerpvoucherno="";

    //下拉框列表
    Map downList=new HashMap<>();

    InventoryModel CurrSelectmodel=new InventoryModel();

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
        inventory_list_order.setText(model.Erpvoucherno);
        Currerpvoucherno = model.Erpvoucherno;
        CurrAreano = "";
        listModel=new ArrayList<InventoryModel>();
        T_Parameter parameter = new T_Parameter();

        parameter.Groupname = "Stock_Status";
        String modelJson = parseModelToJson(parameter);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Project_GetParameter, "获取质检属性",
                context, mHandler, RESULT_Project_GetParameter, null, UrlInfo.getUrl().Project_GetParameter, modelJson, null);

        inventory_list_num.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus==true) {
                    inventory_list_num.setSelectAllOnFocus(true);
                    // CommonUtil.setEditFocus(inventory__config_num);
                }
            }
        });

        //选择listview
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InventoryModel model = listModel.get(i);
                CurrSelectmodel=model;
                //先给值然后再赋值
                try {
                    Pair ywpair = (Pair) mSpinner.getSelectedItem();
                    //循环找到当前model
                    for (InventoryModel item : listModel) {
                        if (item.isCheck) {//找到之前选中的值赋值
                            if (inventory_list_num.getText().toString().trim().equals("")) {
                                item.setScannQty(0f);
                            } else {
                                Float aFloat = Float.parseFloat(inventory_list_num.getText().toString().trim());
                                item.setScannQty(aFloat);
                            }
                            int status =Integer.parseInt(ywpair.value);
                            item.Status = status;
                        }
                    }
                    for (InventoryModel item : listModel) {
                        if (item == model) {
                                item.isCheck=true;
                                inventory_list_num.setText(String.valueOf( item.getScannQty()));
                                int type =item.getStatus();
                                if(type==0){
                                    type=1;
                                }
                                int index = Integer.parseInt(String.valueOf(downList.get(type)));
                                mSpinner.setSelection(index-1);
                        }else{
                            item.isCheck=false;
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    inventory_list_num.setSelectAllOnFocus(true);
                    CommonUtil.setEditFocus(inventory_list_num);

                } catch (Exception ex) {
                    CommonUtil.setEditFocus(inventory_list_num);
                    MessageBox.Show(context, "请输入正确的数量");
                    return;
                }
            }
        });


        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                InventoryModel model = listModel.get(position);
                delModel = model;
                IsDel();
                return true;
            }
        });

    }


    @Override
    protected void initData() {
        super.initData();
        disableShowSoftInput(inventory_list_num);
        disableShowSoftInput(inventory_list_barcode);
        disableShowSoftInput(inventory_list_warehouse);
    }


    //托盘提交
    @Event(value =R.id.inventory_list_post)
    private void  inventory_submit(View view) {
        if (listModel.size() == 0) {
            CommonUtil.setEditFocus(inventory_list_warehouse);
            MessageBox.Show(context, "请先输入或扫描库位");
            return;
        }
        try {
            for (InventoryModel item : listModel) {
                if (item.isCheck) {//给当前的数量赋值到对象
                    if (inventory_list_num.getText().toString().trim().equals("")) {
                        item.setScannQty(0f);
                    } else {
                        Float aFloat = Float.parseFloat(inventory_list_num.getText().toString().trim());
                        item.setScannQty(aFloat);
                    }
                }
                if (item.getScannQty() == null) {
                    item.setScannQty(0f);
                }
            }
        }catch (Exception ex) {
            CommonUtil.setEditFocus(inventory_list_num);
            MessageBox.Show(context, "请输入正确的数量");
            return;
        }
        List<InventoryModel> postModel = new ArrayList<InventoryModel>();
        Pair ywpair = (Pair) mSpinner.getSelectedItem();
        for (InventoryModel item : listModel) {
            if (item.Barcode.equals(CurrSelectmodel.Barcode)) {
                Float aFloat = Float.parseFloat(inventory_list_num.getText().toString().trim());
                item.setQty(aFloat);
                item.setStatus(Integer.parseInt(ywpair.value));
                item.setErpvoucherno(Currerpvoucherno);
                item.setCreater(BaseApplication.mCurrentUserInfo.getUserno());
                item.setAreaid(CurrAreaid);
                item.setAreano(CurrAreano);
            }else{
                item.setQty(item.ScannQty);
                item.setErpvoucherno(Currerpvoucherno);
                item.setCreater(BaseApplication.mCurrentUserInfo.getUserno());
                item.setAreaid(CurrAreaid);
                item.setAreano(CurrAreano);
            }

        }
        String modelJson = parseModelToJson(listModel);
         postModel = GsonUtil.getGsonUtil().fromJson(modelJson, new TypeToken<List<InventoryModel>>() { }.getType());
        modelJson = parseModelToJson(postModel);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_InventoryDetail_Save_CheckDetail, "获取库位信息",
                context, mHandler, RESULT_InventoryDetail_Save_CheckDetail, null, UrlInfo.getUrl().Inventory_Detail_Save_CheckDetail, modelJson, null);
    }


    //数量回车事件
    @Event(value = R.id.inventory_list_num,type = EditText.OnKeyListener.class)
    private  boolean kedowm(View v, int keyCode, KeyEvent event){
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == inventory_list_num.getId()) {
            try {
                if (CurrAreano.equals("")) {
                    CommonUtil.setEditFocus(inventory_list_warehouse);
                    MessageBox.Show(context, "请先扫描库位");
                    return true;
                }
                if(listModel.size()==0){
                    CommonUtil.setEditFocus(inventory_list_warehouse);
                    MessageBox.Show(context, "当前列表不存在数据，请确认");
                    return true;
                }
                Pair ywpair = (Pair) mSpinner.getSelectedItem();
                //循环找到当前model
                for (InventoryModel item : listModel) {
                    if (item.isCheck) {//找到之前选中的值赋值
                        if (inventory_list_num.getText().toString().trim().equals("")) {
                            item.setScannQty(0f);
                        } else {
                            Float aFloat = Float.parseFloat(inventory_list_num.getText().toString().trim());
                            item.setScannQty(aFloat);
                        }
                        int status =Integer.parseInt(ywpair.value);
                        item.Status = status;
                    }
                }
                mAdapter.notifyDataSetChanged();
                return true;

            } catch (Exception ex){
                inventory_list_num.setSelectAllOnFocus(true);
                CommonUtil.setEditFocus(inventory_list_num);
                MessageBox.Show(context, "请输入正确数量");
                return true;
            }
        }
        return  false;
    }



    //获取条码信息
    @Event(value = R.id.inventory_list_barcode,type = EditText.OnKeyListener.class)
    private  boolean getBarcode(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == inventory_list_barcode.getId()) {
            try {
                if (CurrAreano.equals("")) {
                    CommonUtil.setEditFocus(inventory_list_warehouse);
                    MessageBox.Show(context, "请先扫描库位");
                    return true;
                }
                String barcode = inventory_list_barcode.getText().toString().trim();
//                if (!barcode.split("%")[4].equals("2")) {
//                    CommonUtil.setEditFocus(inventory_list_barcode);
//                    MessageBox.Show(context, "请扫描正确托盘条码");
//                    return true;
//                }
                if(barcode.equals("")){
                    CommonUtil.setEditFocus(inventory_list_barcode);
                    MessageBox.Show(context, "请扫描正确托盘条码");
                    return true;
                }
                boolean  istrue=false;
                int index=0;
                int count=0;
                for (InventoryModel item : listModel) {
                    if (item.Barcode.equals(barcode)) {
                        istrue = true;
                        count = index;
                    }
                    index++;
                }
                if(!istrue) {
                    IsaddPalletno();
                }else {
                    Pair ywpair = (Pair) mSpinner.getSelectedItem();
                    for (InventoryModel item : listModel) {
                        if (item.isCheck) {//找到之前选中的值赋值
                            if (inventory_list_num.getText().toString().trim().equals("")) {
                                item.setScannQty(0f);
                            } else {
                                Float aFloat = Float.parseFloat(inventory_list_num.getText().toString().trim());
                                item.setScannQty(aFloat);
                            }
                            int status =Integer.parseInt(ywpair.value);
                            item.Status = status;
                        }
                    }
                    for (InventoryModel item : listModel) {
                        if (item.getBarcode().equals(barcode)) {
                            item.isCheck = true;
                            inventory_list_num.setText(item.getScannQty().toString());
                            int type = item.getStatus();
                            if(type==0){
                                type=1;
                            }
                            int aindex = Integer.parseInt(downList.get(type).toString());
                            mSpinner.setSelection(aindex - 1);
                            CurrSelectmodel = item;
                        } else {
                            item.isCheck = false;
                        }
                    }
                    mList.smoothScrollToPosition(count);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(context, "已经定位到该托盘", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {
                CommonUtil.setEditFocus(inventory_list_barcode);
                MessageBox.Show(context, "请扫描正确托盘条码");
                return true;
            }
        }
        return false;
    }


    //获取货位信息
    @Event(value = R.id.inventory_list_warehouse,type = EditText.OnKeyListener.class)
    private  boolean getWarehouse(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == inventory_list_warehouse.getId()) {
            try {
                InventoryModel model = new InventoryModel();
                model.Erpvoucherno=inventory_list_order.getText().toString().trim();
                model.Areano=inventory_list_warehouse.getText().toString().trim();
                String modelJson = parseModelToJson(model);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_InventoryDetail_Save_CheckDetail, "获取库位信息",
                        context, mHandler, RESULT_InventoryList_GetWarehouse, null, UrlInfo.getUrl().Inventory_GetList, modelJson, null);

            } catch (Exception ex) {
                CommonUtil.setEditFocus(inventory_list_warehouse);
                MessageBox.Show(context, ex.toString());
                return true;
            }
        }
        return false;
    }



    //禁止软键盘
    public static void disableShowSoftInput(EditText editText) {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {
            // TODO: 2018/8/27 处理错误
        }
    }

    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_InventoryList_GetWarehouse:
                GetInventoryList((String) msg.obj);
                break;
            case RESULT_InventoryConfig_GetBarcodeInfo:
                Addmodel((String)msg.obj);
                break;
            case RESULT_InventoryDetail_Save_CheckDetail:
                SaveDetail((String)msg.obj);
                break;
            case RESULT_Project_GetParameter:
                LoadSpinner((String) msg.obj);
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


    //保存盘点信息
    public  void SaveDetail(String result) {
        try {
            BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
            }.getType());
            listModel = new ArrayList<InventoryModel>();
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(inventory_list_warehouse);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            } else {
                CurrSelectmodel=new InventoryModel();
                List<InventoryModel> listModel = new ArrayList<InventoryModel>();
                mAdapter = new InventoryListAdapter(context, listModel);
                mList.setAdapter(mAdapter);
                CommonUtil.setEditFocus(inventory_list_warehouse);
                Toast.makeText(context, returnMsgModel.getResultValue(), Toast.LENGTH_SHORT).show();
                //   MessageBox.Show(context, returnMsgModel.getResultValue(),2,null);
                return;
            }
        } catch (Exception ex) {
            CommonUtil.setEditFocus(inventory_list_warehouse);
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


    //根据库位获取盘点列表
    public  void   GetInventoryList(String result) {
        listModel = new ArrayList<InventoryModel>();
        try {
            BaseResultInfo<List<InventoryModel>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<InventoryModel>>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_POST_ISDEL) {
                    final  List<InventoryModel> listmodel=returnMsgModel.getData();
                    //已经扫盘点过一次的库位
                    new AlertDialog.Builder(this).setTitle(returnMsgModel.getResultValue())
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    InventoryModel model = new InventoryModel();
                                    model.Erpvoucherno = inventory_list_order.getText().toString().trim();
                                    model.Areano = inventory_list_warehouse.getText().toString().trim();
                                    model.setId(5);
                                    String modelJson = parseModelToJson(model);
                                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_InventoryDetail_Save_CheckDetail, "获取库位信息",
                                            context, mHandler, RESULT_InventoryList_GetWarehouse, null, UrlInfo.getUrl().Inventory_GetList, modelJson, null);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (listmodel.size() == 0) {
                                        CommonUtil.setEditFocus(inventory_list_warehouse);
                                        MessageBox.Show(context, "该库位下没有对应的托盘请确认");
                                        return;
                                    }
                                    listModel = listmodel;
                                    for (InventoryModel item : listModel) {
                                        item.setId(0);
                                        item.isProfit = 2;
                                        //  item.ScannQty = item.getQty();
                                    }
                                    listModel.get(0).isCheck = true;
                                    CurrAreaid = listModel.get(0).Areaid;
                                    CurrAreano = listModel.get(0).Areano;
                                    //更新列表
                                    //更新下拉框
                                    int type = listModel.get(0).getStatus();
                                    int index = Integer.parseInt(downList.get(type).toString());
                                    mSpinner.setSelection(index - 1);
                                    if (listModel.size() > 0) {
                                        inventory_list_num.setText(listModel.get(0).getScannQty().toString());
                                    }
                                    mAdapter = new InventoryListAdapter(context, listModel);
                                    mList.setAdapter(mAdapter);
                                    inventory_list_num.setSelectAllOnFocus(true);
                                    CommonUtil.setEditFocus(inventory_list_num);
                                }
                            }).show();
                } else {
                    CommonUtil.setEditFocus(inventory_list_warehouse);
                    MessageBox.Show(context, returnMsgModel.getResultValue());
                    return;
                }
            } else {
                if (returnMsgModel.getData().size() == 0) {
                    CommonUtil.setEditFocus(inventory_list_warehouse);
                    MessageBox.Show(context, "该库位下没有对应的托盘请确认");
                    return;
                }
                listModel = returnMsgModel.getData();
                for (InventoryModel item : listModel) {
                    item.setId(0);
                    item.isProfit = 2;
                  //  item.ScannQty = item.getQty();
                }
                listModel.get(0).isCheck = true;
                CurrAreaid = listModel.get(0).Areaid;
                CurrAreano = listModel.get(0).Areano;
                //更新列表
                //更新下拉框
                int type = listModel.get(0).getStatus();
                int index = Integer.parseInt(downList.get(type).toString());
                mSpinner.setSelection(index - 1);
                if (listModel.size() > 0) {
                    inventory_list_num.setText(listModel.get(0).getScannQty().toString());
                }

            }
        } catch (Exception ex) {
            CommonUtil.setEditFocus(inventory_list_barcode);
            MessageBox.Show(context, ex.toString());
            return;
        }
        mAdapter = new InventoryListAdapter(context, listModel);
        mList.setAdapter(mAdapter);
        inventory_list_num.setSelectAllOnFocus(true);
        CommonUtil.setEditFocus(inventory_list_num);


    }


    //获取条码信息
    public  void Addmodel(String result) {
        //添加的托盘
        List<InventoryModel> list = new ArrayList<InventoryModel>();
        try {
            BaseResultInfo<List<InventoryModel>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<InventoryModel>>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(inventory_list_barcode);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            } else {
                //添加到现有托盘中
                list = returnMsgModel.getData();
                if (list.size() == 0) {
                    CommonUtil.setEditFocus(inventory_list_barcode);
                    MessageBox.Show(context, "未能获取到该托盘信息,请确认");
                    return;
                }
                for (InventoryModel item : list) {
                    item.setId(0);
                    item.isProfit = 1;
                    item.ScannQty = 0f;
                }
                listModel.addAll(0, list);
                int i = 0;
                Pair ywpair = (Pair) mSpinner.getSelectedItem();
                for (InventoryModel item : listModel) {
                    if (item.isCheck) {//找到之前选中的值赋值
                        if (inventory_list_num.getText().toString().trim().equals("")) {
                            item.setScannQty(0f);
                        } else {
                            Float aFloat = Float.parseFloat(inventory_list_num.getText().toString().trim());
                            item.setScannQty(aFloat);
                        }
                        int status =Integer.parseInt(ywpair.value);
                        item.Status = status;
                    }
                }
                for (InventoryModel item : listModel) {
                    if (item == listModel.get(0)) {
                        item.isCheck=true;
                        inventory_list_num.setText(item.getScannQty().toString());
                        int type =item.getStatus();
                        if(type==0){
                            type=1;
                        }
                        int index = Integer.parseInt(downList.get(type).toString());
                        mSpinner.setSelection(index-1);
                        CurrSelectmodel=item;
                    }else{
                        item.isCheck=false;
                    }
                }

            }
        } catch (Exception ex) {
            CommonUtil.setEditFocus(inventory_list_barcode);
            MessageBox.Show(context, ex.toString());
        }
        mAdapter.notifyDataSetChanged();
      //   mList.setSelection(0);
//        mAdapter = new InventoryListAdapter(context, listModel);
//        mList.setAdapter(mAdapter);

    }

    public void IsaddPalletno() {
        new AlertDialog.Builder(this).setTitle("该托盘不在当前库位中是否盘赢？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String barcode = inventory_list_barcode.getText().toString().trim();
                        OutBarcodeInfo model = new OutBarcodeInfo();
                        model.setBarcode(barcode);
                    //    model.setSerialno(barcode.split("%")[3]);
                        model.setAreano(CurrAreano);
                        model.setErpvoucherno(Currerpvoucherno);
                        String modelJson = parseModelToJson(model);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_InventoryConfig_GetBarcodeInfo, "获取条码信息",
                                context, mHandler, RESULT_InventoryConfig_GetBarcodeInfo, null, UrlInfo.getUrl().Inventory_Config_GetScanInfo, modelJson, null);

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    InventoryModel delModel=new InventoryModel();

    public  void IsDel() {
        new AlertDialog.Builder(this).setTitle("确定删除序列为" + delModel.Serialno + "的托盘吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定触发的事件
                        Pair ywpair = (Pair) mSpinner.getSelectedItem();
                        for (InventoryModel item : listModel) {
                            if (item.isCheck) {//找到之前选中的值赋值
                                if (inventory_list_num.getText().toString().trim().equals("")) {
                                    item.setScannQty(0f);
                                } else {
                                    Float aFloat = Float.parseFloat(inventory_list_num.getText().toString().trim());
                                    item.setScannQty(aFloat);
                                }
                                int status = Integer.parseInt(ywpair.value);
                                item.Status = status;
                            }
                        }
                        //删除
//                        for (InventoryModel item : listModel) {
//                            if (item.Serialno.equals(delModel.Serialno)) {
//                                //删除选中行
//                                listModel.remove(item);
//
//                            }
//                        }

                        Iterator<InventoryModel> it=listModel.iterator();
                        while(it.hasNext()){
                            InventoryModel item=it.next();
                            if(item.Serialno.equals(delModel.Serialno)){
                                it.remove();
                            }
                        }

                        //选中第一行
                        for (InventoryModel item : listModel) {
                            if (item == listModel.get(0)) {
                                item.isCheck = true;
                                inventory_list_num.setText(item.getScannQty().toString());
                                int type = item.getStatus();
                                if(type==0){
                                    type=1;
                                }
                                int index = Integer.parseInt(downList.get(type).toString());
                                mSpinner.setSelection(index - 1);
                                CurrSelectmodel = item;
                            } else {
                                item.isCheck = false;
                            }
                        }
                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();

    }



}