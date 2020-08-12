package com.liansu.boduowms.modules.outstock.SalesOutstock;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.liansu.boduowms.modules.outstock.Model.MaterialResponseModel;
import com.liansu.boduowms.modules.outstock.Model.SalesoutStcokboxRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockRequery;
import com.liansu.boduowms.ui.adapter.outstock.packing.PackingScanAdapter;
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

import ch.qos.logback.core.joran.spi.ElementSelector;

import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_box;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_parts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_Box_Check_Box;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_Box_Check_waterCode;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_Box_SelectNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_Box_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_Box_Submit_Box;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_PlatForm;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_SalesNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannParts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannParts_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_barcodeisExist;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_Box_SelectNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_Box_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_PlatForm;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitParts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitParts_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_barcodeisExist;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

//拼箱   每次需要注册
@ContentView(R.layout.activity_outstock_sales_box)
public  class SalesOutStockBox   extends BaseActivity {
    Context context = SalesOutStockBox.this;

    //region
    //当前单号
    private String CurrOrder;

    //订单框
    @ViewInject(R.id.sales_outstock_box_order)
    EditText sales_outstock_box_order;

    //69码 物料号 箱号
    @ViewInject(R.id.sales_outstock_box_watercode)
    EditText sales_outstock_box_watercode;

    //适配器
    protected SalesoutstockBoxAdapter mAdapter;

    //列表框
    @ViewInject(R.id.out_stock_sales_box_ListView)
    ListView mList;


    //物料存储类 判断是否存在
    Map<String,String> modelIsExits;

    //提交对象
    List<SalesoutStcokboxRequery> stockInfoModels;

    //散件类
    private  MaterialResponseModel   materialModle;


    //多批次对象
     private List<OutStockOrderDetailInfo> responseList;

    private  int CurrVoucherType;

    private  int Scanningtype;

    //endregion
    protected void initViews() {
        super.initViews();
        BaseApplication.toolBarTitle = new ToolBarTitle("拼箱", true);
        x.view().inject(this);
        CurrOrder="";
        materialModle=new  MaterialResponseModel();
        stockInfoModels=new ArrayList<SalesoutStcokboxRequery>();
        CurrVoucherType=29;
        modelIsExits=new HashMap<String, String>();
        Scanningtype=0;
        responseList=new ArrayList<OutStockOrderDetailInfo>();
    }
    protected void initData() {
        super.initData();


    }

    //region 事件
    //订单回车事件
    @Event(value = R.id.sales_outstock_box_order,type = EditText.OnKeyListener.class)
    private  boolean orderKeyDowm(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        //如果是扫描
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == sales_outstock_box_order.getId()) {
            try {
                String order =sales_outstock_box_order.getText().toString().trim();
                if (!order.equals("")) {
                    SalesoutStcokboxRequery model = new SalesoutStcokboxRequery();
                    model.Erpvoucherno =order;
                    model.Vouchertype=CurrVoucherType;
                    String modelJson = parseModelToJson(model);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_Box_SelectNO, "订单提交中",
                            context, mHandler, RESULT_Saleoutstock_Box_SelectNO, null, UrlInfo.getUrl().SalesOutstock_Box_ScanningNo, modelJson, null);
                    return true;
                }
            } catch (Exception ex) {
                CommonUtil.setEditFocus(sales_outstock_box_order);
                MessageBox.Show(context, ex.toString());
                return false;
            }
        }
        return false;
    }


    //月台提交
     @Event(value =R.id.outstock_sales_buttonyue)
      private void  yuetai_buttonp_Submit(View view) {
             inputYUETAIDialog("输入月台");
        }


        //69码/物料回车事件
        @Event(value = R.id.sales_outstock_box_watercode,type = EditText.OnKeyListener.class)
        private  boolean waterCodeKeyDeowm(View v,int keyCode,KeyEvent event) {
            View vFocus = v.findFocus();
            int etid = vFocus.getId();
            //如果是扫描
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == sales_outstock_box_watercode.getId()) {
                try {
                    if (!IsSacnningOrder()) {
                        return true;
                    }
                    //可能是箱号 69码，物料号
                    String barcode = sales_outstock_box_watercode.getText().toString().trim();
                    String[] arr = barcode.split("%");
                    //不是箱号
                    if (arr.length == 1) {
                        Scanningtype=2;
                        //查询该物料是否存在
                        String modelJson = parseModelToJson(barcode);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitParts, "检验是否存在",
                                context, mHandler, RESULT_Saleoutstock_ScannParts, null, UrlInfo.getUrl().SelectMaterial, modelJson, null);
                        return true;
                    }
                    if (arr.length == 4) {
                        if (arr[3].equals("1")) {
                            Scanningtype=1;
                            //箱号
                            //直接调用拼箱方法
                            SalesoutStcokboxRequery model = new SalesoutStcokboxRequery();
                            model.Batchno = arr[1];
                            model.Materialno = barcode;
                            model.Erpvoucherno = CurrOrder;
                            model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                            model.Qty = Float.parseFloat(arr[2]);
                            model.Vouchertype = CurrVoucherType;
                            String modelJson = parseModelToJson(model);
                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_Box_Submit, "提交物料中",
                                    context, mHandler, RESULT_Saleoutstock_Box_Check_Box, null, UrlInfo.getUrl().SalesOutstock_Box_Batchno, modelJson, null);
                            return true;
                        }
                    }
                } catch (Exception ex) {
                    MessageBox.Show(context, ex.toString());
                }
            }
            //   MessageBox.Show(context, "请扫描69码/物料/外箱标签");
            return false;
        }

     //拼箱
    @Event(value =R.id.outstock_sales_buttonp)
    private void  Click_pinBox(View view) {
        try {
            if (IsSacnningOrder()) {
                if (stockInfoModels.size() > 0) {
                    String modelJson = parseModelToJson(stockInfoModels);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_Box_Submit, "拼箱提交中",
                            context, mHandler, RESULT_Saleoutstock_Box_Submit_Box, null, UrlInfo.getUrl().SalesOutstock_Box_Submit, modelJson, null);
                } else {
                    MessageBox.Show(context, "列表中没有需要提交的信息，请先扫描69码/物料");
                }
            }
        } catch (Exception EX) {
            MessageBox.Show(context, EX.toString());
        }

    }

    //删除
    @Event(value =R.id.outstock_sales_deletebuttonp)
    private void  Click_DelBox(View view) {
        if (IsSacnningOrder()) {
            if (stockInfoModels.size() > 0) {
                materialModle = new MaterialResponseModel();
                stockInfoModels = new ArrayList<SalesoutStcokboxRequery>();
                mAdapter = new SalesoutstockBoxAdapter(context, stockInfoModels);
                responseList=new ArrayList<OutStockOrderDetailInfo>();
                modelIsExits = new HashMap<String, String>();
                mList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                CommonUtil.setEditFocus(sales_outstock_box_watercode);
                MessageBox.Show(context, "删除成功");

            } else {
                CommonUtil.setEditFocus(sales_outstock_box_watercode);
                MessageBox.Show(context, "当前无数据可删除");
            }
        }
    }



    //拼箱列表
    @Event(value =R.id.outstock_sales_buttoList)
    private void  Click_showBoxList(View view){
        Intent intent = new Intent();
        //intent.setData(data);
        intent.setClass(context, SalesOutStockBoxList.class);
        startActivity(intent);
    }


    //endregion


    //region回调
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Saleoutstock_Box_SelectNO:
                SacnnNo((String) msg.obj);
                break;
            case RESULT_Saleoutstock_ScannParts://检查物料69码是否存在
                ScannParts((String) msg.obj);
                break;
            case  RESULT_Saleoutstock_Box_Check_Box://检查是否事多批次
                CheckBox((String) msg.obj,0);
                break;
            case RESULT_Saleoutstock_Box_Check_waterCode://散件提交
                CheckBox((String) msg.obj,1);
                break;
            case  RESULT_Saleoutstock_PlatForm://月台
                ResultPlatForm((String) msg.obj);
                break;
            case  RESULT_Saleoutstock_Box_Submit_Box://提交
                   SubmitBox((String) msg.obj);
            break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;
        }
    }
    //endregion



    //检验
    public  void   CheckBox(String result,int type) {
        try {
            responseList=new ArrayList<OutStockOrderDetailInfo>();
            BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
            }.getType());
            if (type == 0) {
                //多批次
                if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_ACTION_CONTINUE) {
                    responseList=returnMsgModel.getData();
                    if(responseList.size()>0){
                        //选择批次
                        SelectBatchno(returnMsgModel.getData());
                    }else{
                        CommonUtil.setEditFocus(sales_outstock_box_watercode);
                            MessageBox.Show(context,"没有批次数据");
                        return;
                    }

                } else {
                    if(returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                        MessageBox.Show(context, returnMsgModel.getResultValue());
                        return;
                    }
                    List<OutStockOrderDetailInfo>list= returnMsgModel.getData();
                    //直接提交
                    String[] arr = sales_outstock_box_watercode.getText().toString().trim().split("%");
                  List<SalesoutStcokboxRequery> listmodel=new ArrayList<SalesoutStcokboxRequery>();
                    SalesoutStcokboxRequery model = new SalesoutStcokboxRequery();
                    model.Batchno = list.get(0).getBatchno();
                    model.Materialno = arr[0];
                    model.Erpvoucherno = CurrOrder;
                    model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                    model.Qty = Float.parseFloat(arr[2]);
                    model.Vouchertype = CurrVoucherType;
                    listmodel.add(model);
                    String modelJson = parseModelToJson(list);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_Box_Submit, "提交物料中",
                            context, mHandler, RESULT_Saleoutstock_Box_Submit_Box, null, UrlInfo.getUrl().SalesOutstock_Box_Submit, modelJson, null);
//                    CommonUtil.setEditFocus(sales_outstock_box_watercode);
//                    MessageBox.Show(context, returnMsgModel.getResultValue());
                }
            } else {
                //提交列表拼箱
                if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_ACTION_CONTINUE) {
                    responseList=returnMsgModel.getData();
                    if(responseList.size()>0){
                        SelectBatchno(returnMsgModel.getData());
                    }else{
                        CommonUtil.setEditFocus(sales_outstock_box_watercode);
                        MessageBox.Show(context,"没有批次数据");
                        return;
                    }
                  //  MessageBox.Show(context, returnMsgModel.getResultValue());
                }
                if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_OK) {
                    List<OutStockOrderDetailInfo>list= returnMsgModel.getData();
                  //只有一个批次
                  //直接刷新listView
                    String name = modelIsExits.get(materialModle.Materialno+list.get(0).getBatchno());
                    if (name==null||name.equals("")) {
                        //不存在
                        //添加到ListView
                        SalesoutStcokboxRequery model = new SalesoutStcokboxRequery();
                        model.Materialno=materialModle.Materialno;
                        model.Materialdesc=materialModle.Materialdesc;
                        model.Batchno=list.get(0).getBatchno();
                        model.Erpvoucherno=CurrOrder;
                        model.PostUserNo=BaseApplication.mCurrentUserInfo.getUserno();
                        model.Qty=num;
                        model.Vouchertype=CurrVoucherType;
                        modelIsExits.put(materialModle.Materialno+list.get(0).getBatchno(),materialModle.Materialno);
                        stockInfoModels.add(model);
                    } else {
                        //存在
                        for (SalesoutStcokboxRequery infos : stockInfoModels) {
                            if (infos.getMaterialno().equals(materialModle.Materialno) && infos.getBatchno().equals(list.get(0).getBatchno())) {
                                Float value =ArithUtil.add(infos.Qty ,num);
                                infos.setQty(value);
                            }
                        }
                    }
                    mAdapter = new SalesoutstockBoxAdapter(context, stockInfoModels);
                    mList.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
                 if(returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_DEFAULT){
                     CommonUtil.setEditFocus(sales_outstock_box_watercode);
                     MessageBox.Show(context, returnMsgModel.getResultValue());
                 }
            }
            return;
        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_box_watercode);
            MessageBox.Show(context, ex.toString());
        }
    }


    //提交
    public  void   SubmitBox(String result) {
        try {
            BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
            }.getType());
            CommonUtil.setEditFocus(sales_outstock_box_order);
            if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_OK) {
                materialModle = new MaterialResponseModel();
                stockInfoModels = new ArrayList<SalesoutStcokboxRequery>();
                mAdapter = new SalesoutstockBoxAdapter(context, stockInfoModels);
                responseList = new ArrayList<OutStockOrderDetailInfo>();
                modelIsExits = new HashMap<String, String>();
                mList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                CommonUtil.setEditFocus(sales_outstock_box_watercode);
            }
            MessageBox.Show(context, returnMsgModel.getResultValue());
        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_box_order);
            MessageBox.Show(context, ex.toString());
        }
    }

    //订单扫描事件
    public  void SacnnNo(String result) {
        BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
        }.getType());
        if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
            CommonUtil.setEditFocus(sales_outstock_box_order);
            MessageBox.Show(context, returnMsgModel.getResultValue());
            return;
        }
        CommonUtil.setEditFocus(sales_outstock_box_watercode);
        CurrOrder = sales_outstock_box_order.getText().toString().trim();
    }

    //月台返回
    public  void    ResultPlatForm(String result) {
        BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
        }.getType());
        if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
            MessageBox.Show(context, returnMsgModel.getResultValue());
            return;
        }
        MessageBox.Show(context, "提交成功");
    }


    //扫描物料或者69码获取对象
    public  void   ScannParts(String  result) {
        try {
            BaseResultInfo<MaterialResponseModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<MaterialResponseModel>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_box_watercode);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            materialModle=returnMsgModel.getData();
            inputTitleDialog("输入散件数量");

        } catch (Exception EX) {
            CommonUtil.setEditFocus(sales_outstock_box_watercode);
            MessageBox.Show(context, EX.toString());

        }
    }

    //是否扫描过单号
    public  boolean IsSacnningOrder() {
        if (CurrOrder.equals("")) {
            CommonUtil.setEditFocus(sales_outstock_box_order);
            MessageBox.Show(context, "请先扫描单号");
            return false;
        }
        return true;
    }
    private   String inputYuetai;
    private void inputYUETAIDialog(String name) {
        try {
            if(IsSacnningOrder()){
                final EditText inputServer = new EditText(this);
                inputServer.setFocusable(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(name).setIcon(
                        null).setView(inputServer).setNegativeButton(
                        "取消", null);
                builder.setPositiveButton("确认提交月台",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                inputYuetai = inputServer.getText().toString();
                                Platform model=new Platform();
                                model.Erpvoucherno=CurrOrder;
                                model.Platform=inputYuetai;
                                String json = GsonUtil.parseModelToJson(model);
                                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_PlatForm, "月台提交",
                                        context, mHandler, RESULT_Saleoutstock_PlatForm, null, UrlInfo.getUrl().SalesOutstock_PlatForm, json, null);
                            }
                        });
                builder.show();
            }
        }catch (Exception ex) {
            MessageBox.Show(context, ex.toString());
        }
    }





    //选择多批次
    private void SelectBatchno(List<OutStockOrderDetailInfo> list)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("选择一个批次");
        //    指定下拉列表的显示数据
        final String[] cities = new String[list.size()];
        int i=0;
        for (OutStockOrderDetailInfo  model:list) {
            cities[i] = model.getBatchno();
            i++;
        }
        //    设置一个下拉的列表选择项
        builder.setItems(cities, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //  Toast.makeText(getActivity(), "选择的城市为：" + cities[which], Toast.LENGTH_SHORT).show();
                //再次提交
                try {
                    //判断是箱号还是散件
                    if(Scanningtype==1){
                        String boxno=sales_outstock_box_watercode.getText().toString().trim();
                        //先找到返回的对象
                        OutStockOrderDetailInfo detailinfo=new OutStockOrderDetailInfo();
                        for (OutStockOrderDetailInfo item:responseList) {
                            if (item.getMaterialno().equals(boxno.split("%")[0]) && item.getBatchno().equals(cities[which])) {
                                detailinfo = item;
                            }
                        }
                        if( ArithUtil.sub(detailinfo.getQty(),Float.parseFloat(boxno.split("%")[2]))<0){
                            MessageBox.Show(context, "该箱号的数量已经大于下架数"+detailinfo.getQty());
                            return;
                        }
                        //提交
                        List<SalesoutStcokboxRequery> lista = new ArrayList<SalesoutStcokboxRequery>();
                        SalesoutStcokboxRequery model = new SalesoutStcokboxRequery();
                        model.Batchno = cities[which];
                        model.Materialno = boxno.split("%")[0];
                        model.Erpvoucherno = CurrOrder;
                        model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                        model.Qty = Float.parseFloat(boxno.split("%")[2]);
                        model.Vouchertype = CurrVoucherType;
                        lista.add(model);
                        String modelJson = parseModelToJson(lista);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_Box_Submit, "拼箱提交中",
                                context, mHandler, RESULT_Saleoutstock_Box_Submit_Box, null, UrlInfo.getUrl().SalesOutstock_Box_Submit, modelJson, null);
                    }else
                    {
                        //先找到返回的对象
                        OutStockOrderDetailInfo detailinfo=new OutStockOrderDetailInfo();
                        for (OutStockOrderDetailInfo item:responseList) {
                            if (item.getMaterialno().equals(materialModle.Materialno) && item.getBatchno().equals(cities[which])) {
                                detailinfo = item;
                            }
                        }
                        //再找到存在的对象
                        for (SalesoutStcokboxRequery item:stockInfoModels) {
                            if (item.Materialno.equals(materialModle.Materialno) && item.Batchno.equals(cities[which])) {
                                //判断是否大于下架量/包装量
                                Float value = ArithUtil.add(item.Qty, num);
                                if (ArithUtil.sub(detailinfo.getQty(), value) < 0) {
                                    MessageBox.Show(context, "该散件的数量已经大于下架数" + detailinfo.getQty());
                                    return;
                                }
                            }
                        }
                        //散件  刷新界面
                        String name = modelIsExits.get(materialModle.Materialno+ cities[which]);
                        if (name==null||name.equals("")) {
                            //不存在
                            //添加到ListView
                            SalesoutStcokboxRequery model = new SalesoutStcokboxRequery();
                            model.Materialno=materialModle.Materialno;
                            model.Materialdesc=materialModle.Materialdesc;
                            model.Batchno=cities[which];
                            model.Erpvoucherno=CurrOrder;
                            model.PostUserNo=BaseApplication.mCurrentUserInfo.getUserno();
                            model.Qty=num;
                            model.Vouchertype=CurrVoucherType;
                            modelIsExits.put(materialModle.Materialno+cities[which],materialModle.Materialno);
                            stockInfoModels.add(model);
                        } else {
                            //存在
                            for (SalesoutStcokboxRequery infos : stockInfoModels) {
                                if (infos.getMaterialno().equals(materialModle.Materialno) && infos.getBatchno().equals(cities[which])) {
                                    Float value =ArithUtil.add(infos.Qty , num);
                                    infos.setQty(value);
                                }
                            }
                        }
                        mAdapter = new SalesoutstockBoxAdapter(context, stockInfoModels);
                        mList.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                }catch (Exception ex){

                    MessageBox.Show(context, "系统出现异常请重新扫描");

                }
            }
        });
        builder.show();
    }


    private  Float num;

    //散件数量  多次输入 多批次怎么办
    private void inputTitleDialog(String name) {
        String boxNo = sales_outstock_box_watercode.getText().toString().trim();
        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(name).setIcon(
                null).setView(inputServer).setNegativeButton(
                "取消", null);
        builder.setPositiveButton("确认提交",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String Value = inputServer.getText().toString();
                        try {
                            Float inputValue = Float.parseFloat(Value);
                            Float packqty = Float.parseFloat(materialModle.getPackqty());
                            if (inputValue >= packqty) {
                                CommonUtil.setEditFocus(sales_outstock_box_watercode);
                                MessageBox.Show(context, "不能大于" + packqty + "包装量");
                                return;
                            }
                            if (materialModle == null) {
                                CommonUtil.setEditFocus(sales_outstock_box_watercode);
                                MessageBox.Show(context, "该69码/物料不存在");
                                return;
                            }
                            //先找是不是存在同一个物料存在列表中
                            //选择批次
                            num=inputValue;
                            SalesoutStcokboxRequery model = new SalesoutStcokboxRequery();
                            model.Batchno = "";
                            model.Materialno = materialModle.Materialno;
                            model.Erpvoucherno = CurrOrder;
                            model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                            model.Qty =inputValue;
                            model.Vouchertype = CurrVoucherType;
                            String modelJson = parseModelToJson(model);
                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_Box_Submit, "提交物料中",
                                    context, mHandler, RESULT_Saleoutstock_Box_Check_waterCode, null, UrlInfo.getUrl().SalesOutstock_Box_Batchno, modelJson, null);

                        } catch (Exception ex) {
                            CommonUtil.setEditFocus(sales_outstock_box_watercode);
                            MessageBox.Show(context, "请输入正确的数量");
                        }
                    }

                });
        builder.show();
    }




    //月台类
    public  class   Platform{

        public  String  Platform;

        public  String  Erpvoucherno;

        public String getPlatform() {
            return Platform;
        }

        public void setPlatform(String platform) {
            Platform = platform;
        }

        public String getErpvoucherno() {
            return Erpvoucherno;
        }

        public void setErpvoucherno(String erpvoucherno) {
            Erpvoucherno = erpvoucherno;
        }
    }





}
