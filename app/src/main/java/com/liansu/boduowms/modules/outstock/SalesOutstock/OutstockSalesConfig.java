package com.liansu.boduowms.modules.outstock.SalesOutstock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.liansu.boduowms.modules.outstock.Model.AwyBll;
import com.liansu.boduowms.modules.outstock.Model.MenuOutStockModel;
import com.liansu.boduowms.modules.outstock.Model.Pair;
import com.liansu.boduowms.modules.outstock.Model.PairAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutStcokboxRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockRequery;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
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

import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_Box_Submit_Box;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ConfigSaveOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ConfigSelectOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ReviewOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_SalesNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannPalletNo;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_barcodeisExist;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESUL_Saleoutstock_WaybillPrint;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_Box_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_ConfigSaveOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_ConfigSelectOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_ReviewOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SelectNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_WaybillPrint;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

//运输配置界面
@ContentView(R.layout.activity_outstock_sales_config)
public class OutstockSalesConfig extends BaseActivity {
    Context context = OutstockSalesConfig.this;
    //发货物流
    @ViewInject(R.id.outstock_down_fahuo)
    TextView mfhSpinner;
    // private ArrayAdapter<Pair> mFahuoArrayAdapter;

    //送货方式
    @ViewInject(R.id.outstock_down_shstatus)
    Spinner mshSpinner;
    private ArrayAdapter<Pair> mShstatusAdapter;

    //业务类型
    @ViewInject(R.id.outstock_config_ywtype)
    Spinner mywSpinner;
    private ArrayAdapter<Pair> mYwtypeAdapter;

    //结算方式
    @ViewInject(R.id.outstock_config_jstype)
    Spinner mjsSpinner;
    private ArrayAdapter<Pair> mJstypeAdapter;

    //费用计算
    @ViewInject(R.id.outstock_config_fytype)
    Spinner mfySpinner;
    private ArrayAdapter<Pair> mFytypeAdapter;

    //托运单号
    @ViewInject(R.id.outstock_sales_config_order)
    TextView outstock_sales_config_order;

    //物流单号
    @ViewInject(R.id.sales_outstock_wlOrder)
    EditText sales_outstock_wlOrder;


    //扫描单号
    @ViewInject(R.id.sales_outstock_configOrder)
    EditText sales_outstock_configOrder;


    //上门费用
    @ViewInject(R.id.sales_outstock_configsm_text)
    EditText sales_outstock_configsm_text;


    //报价费用
    @ViewInject(R.id.sales_outstock_configbj_text)
    EditText sales_outstock_configbj_text;

    //单价
    @ViewInject(R.id.sales_outstock_configdj_text)
    EditText sales_outstock_configdj_text;

    //送货人
    @ViewInject(R.id.outstock_config_creater)
    TextView outstock_config_creater;

    //送货地址
    @ViewInject(R.id.outstock_config_address)
    TextView outstock_config_address;

    //当前托运单集合
    private List<AwyBll> awyBllList = new ArrayList<AwyBll>();

    //当前单号对象
    private AwyBll awyBll;

    private int CurrVoucherType;

    private  UrlInfo info = new UrlInfo();

    private  String CurrVoucherNo="";//当前单号
    MenuOutStockModel menuOutStockModel = new MenuOutStockModel();

      private  int isload=1;//用来区分是否第一次加载
    private  int fytype=1;//计算费用类型
    //做一个费用计算的标记   每个定义的变量都需要定义
    private  int feiyongType=0;

    @Override
    protected void initViews() {
        super.initViews();
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        String arr = data.toString();
        feiyongType=0;
        menuOutStockModel = new MenuOutStockModel();
        menuOutStockModel = GsonUtil.parseJsonToModel(arr, MenuOutStockModel.class);
       int type = Integer.parseInt(menuOutStockModel.VoucherType);
        //int type = Integer.parseInt(arr);
        CurrVoucherType = type;
        info.InitUrl(type);
        CurrVoucherNo="";
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(menuOutStockModel.Title+"-"+BaseApplication.mCurrentWareHouseInfo.Warehouseno, true);
     //    BaseApplication.toolBarTitle = new ToolBarTitle(menuOutStockModel.Title + "-" + BaseApplication.mCurrentWareHouseInfo.Warehouseno, true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        //送货方式
        mshSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(
        ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {//自提
                    sales_outstock_configsm_text.setText("0");
                    sales_outstock_configsm_text.setVisibility(View.INVISIBLE);//隐藏
                    CommonUtil.setEditFocus(sales_outstock_configbj_text);
                } else {//送货上门
                    sales_outstock_configsm_text.setVisibility(View.VISIBLE);//显示
                    CommonUtil.setEditFocus(sales_outstock_configsm_text);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //费用计算
        mfySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(
        ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sales_outstock_configdj_text.setText("0");
                if (position == 0) {//重量
                    if(fytype==1) {
                        if (isload == 1) {//加载选择发货通知单
                            isload = 3;
                            CommonUtil.setEditFocus(sales_outstock_configOrder);
                        }else{
                            CommonUtil.setEditFocus(sales_outstock_configbj_text);
                        }
                        sales_outstock_configdj_text.setVisibility(View.INVISIBLE);// 隐藏

                    }else{
                        sales_outstock_configdj_text.setVisibility(View.VISIBLE);// 显示
                         CommonUtil.setEditFocus(sales_outstock_configdj_text);
                    }
                } else {
                    sales_outstock_configdj_text.setVisibility(View.VISIBLE);//显示
                    CommonUtil.setEditFocus(sales_outstock_configdj_text);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        //结算方式
//        mjsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(
//        ) {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                PairAdapter fydapter = new PairAdapter();
//                sales_outstock_configdj_text.setText("0");
//                if (position == 0) {//月结 到付三种
//                    fytype=1;
//                    fydapter.pairs = new Pair[3];
//                    fydapter.addPairs(1, "1", "重量", fydapter.pairs);
//                    fydapter.addPairs(2, "2", "件数", fydapter.pairs);
//                    fydapter.addPairs(3, "3", "体积", fydapter.pairs);
//                    fydapter.bindAdapter(mFytypeAdapter, mfySpinner, fydapter.pairs, context);
//                    if(feiyongType!=0){
//                        mfySpinner.setSelection(feiyongType-1);
//                        feiyongType=0;
//                    }
//                } else {//到付   只能是新店和整车
//                    fytype=2;
//                    fydapter.pairs = new Pair[2];
//                    fydapter.addPairs(1, "4", "整车", fydapter.pairs);
//                    fydapter.addPairs(2, "5", "新店", fydapter.pairs);
//                    fydapter.bindAdapter(mFytypeAdapter, mfySpinner, fydapter.pairs, context);
//                    if(feiyongType!=0){
//                        mfySpinner.setSelection(feiyongType-3);
//                        feiyongType=0;
//                    }
//                }
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
        CommonUtil.setEditFocus(sales_outstock_configOrder);


    }


    @Override
    protected void initData() {
        super.initData();
        //加载接口/下拉框数据
        awyBll = new AwyBll();
//        Intent intentMain = getIntent();
//        Uri data = intentMain.getData();
//        int type = Integer.parseInt(data.toString());
//        info.InitUrl(type);
//        CurrVoucherType = type;
        //送货方式
        PairAdapter shadapter = new PairAdapter();
        shadapter.pairs = new Pair[2];
        shadapter.addPairs(1, "1", "自提", shadapter.pairs);
        shadapter.addPairs(2, "2", "送货上门", shadapter.pairs);
    //    shadapter.addPairs(3, "3", "物流点自提", shadapter.pairs);
        shadapter.bindAdapter(mShstatusAdapter, mshSpinner, shadapter.pairs, context);
        //业务类型
        PairAdapter ywadapter = new PairAdapter();
        ywadapter.pairs = new Pair[2];
        ywadapter.addPairs(1, "1", "加盟", ywadapter.pairs);
        ywadapter.addPairs(2, "2", "销售", ywadapter.pairs);
        ywadapter.bindAdapter(mYwtypeAdapter, mywSpinner, ywadapter.pairs, context);
        //结算方式
//        PairAdapter jsdapter = new PairAdapter();
//        jsdapter.pairs = new Pair[2];
//        jsdapter.addPairs(1, "1", "月结", jsdapter.pairs);
//        jsdapter.addPairs(2, "2", "到付", jsdapter.pairs);
//        jsdapter.bindAdapter(mJstypeAdapter, mjsSpinner, jsdapter.pairs, context);
//        //费用计算方式
//        PairAdapter fydapter = new PairAdapter();
//        fydapter.pairs = new Pair[5];
//        fydapter.addPairs(1, "1", "重量", fydapter.pairs);
//        fydapter.addPairs(2, "2", "件数", fydapter.pairs);
//        fydapter.addPairs(3, "3", "体积", fydapter.pairs);
//        fydapter.addPairs(4, "4", "整车", fydapter.pairs);
//        fydapter.addPairs(5, "5", "新店", fydapter.pairs);
//        fydapter.bindAdapter(mFytypeAdapter, mfySpinner, fydapter.pairs, context);
        CommonUtil.setEditFocus(sales_outstock_wlOrder);
    }

    protected void onResume() {
        super.onResume();
        String order = outstock_sales_config_order.getText().toString().trim();
        if (!order.equals("无") && !order.equals("")) {
            new AlertDialog.Builder(this).setTitle("是否打印托运单")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AwyBll way = new AwyBll();
                            way.setErpvoucherno(outstock_sales_config_order.getText().toString().trim());
                            way.Printername = UrlInfo.mOutStockPrintName;
                            way.Printertype = UrlInfo.mOutStockPrintType;
                            //托运单号
                            String json = GsonUtil.parseModelToJson(way);
                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_WaybillPrint, "打印托运单号",
                                    context, mHandler, RESUL_Saleoutstock_WaybillPrint, null, info.SalesOutstock__Review_Printwaybill, json, null);
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_setting, menu);
//        return true;
//    }
//
//    protected UserSettingPresenter mUserSettingPresenter;
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.user_setting_warehouse_select) {
//            selectWareHouse(this);
//        }
//        return false;
//    }
//
//    @Override
//    public void getToolTitle() {
//        getToolBarHelper().getToolBar().setTitle(menuOutStockModel.Title + "-" + BaseApplication.mCurrentWareHouseInfo.Warehouseno);
//        //清空列表//切换仓库后需要重新扫描
//        awyBll = new AwyBll();
//        awyBllList = new ArrayList<AwyBll>();
//        CurrVoucherNo = "";
//        outstock_sales_config_order.setText("");
//        sales_outstock_wlOrder.setText("");
//        outstock_config_address.setText("");
//        outstock_config_creater.setText("");
//        mfhSpinner.setText("");
//        sales_outstock_configbj_text.setText("");
//        sales_outstock_configsm_text.setText("");
//        sales_outstock_configdj_text.setText("");
//        sales_outstock_configOrder.setText("");
//        CommonUtil.setEditFocus(sales_outstock_configOrder);
//    }
//



    //确认按钮
    @Event(value = R.id.sales_outstock_button_configsubmit)
    private void Click_submit(View view) {
        try {
            if (awyBll.Erpvoucherno == null) {
                CommonUtil.setEditFocus(sales_outstock_configOrder);
                MessageBox.Show(context, "请先输入或扫描发货通知单号");
                return;
            }
            if (!awyBll.Erpvoucherno.equals("")) {
                if(!awyBll.getLinkVoucherNo().equals(sales_outstock_configOrder.getText().toString().trim())) {
                    //重新选择托运单
                    SalesoutstockRequery model = new SalesoutstockRequery();
                    model.Erpvoucherno = sales_outstock_configOrder.getText().toString().trim();
                    model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
                    model.Vouchertype = CurrVoucherType;
                    String json = GsonUtil.parseModelToJson(model);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_ReviewOrder, "单号检验中",
                            context, mHandler, RESULT_Saleoutstock_ReviewOrder, null, info.SalesOutstock_Review_ScanningNo, json, null);
                    return;
                }
//                if(sales_outstock_wlOrder.getText().toString().trim().equals("")){
//                    CommonUtil.setEditFocus(sales_outstock_wlOrder);
//                    MessageBox.Show(context, "物流单号不能为空");
//                    return;
//                }

                Pair pair = (Pair) mshSpinner.getSelectedItem();
                Pair fypair = (Pair) mfySpinner.getSelectedItem();
                if(pair.value.equals("2")&&sales_outstock_configsm_text.getText().toString().trim().equals("")) {
                    CommonUtil.setEditFocus(sales_outstock_configsm_text);
                    MessageBox.Show(context, "送货上门时,上门费用不能为空");
                    return;
                }
                if(sales_outstock_configbj_text.getText().toString().trim().equals("")){
                    CommonUtil.setEditFocus(sales_outstock_configbj_text);
                    MessageBox.Show(context, "请填写物流保价费用");
                    return;
                }
                if(!fypair.value.equals("1")&&sales_outstock_configdj_text.getText().toString().trim().equals("")){
                    CommonUtil.setEditFocus(sales_outstock_configdj_text);
                    MessageBox.Show(context, "费用计算不为重量单价时，请填写单价");
                    return;
                }
                //先提交在跳转
                Pair ywpair = (Pair) mywSpinner.getSelectedItem();
                Pair jspair = (Pair) mjsSpinner.getSelectedItem();
                awyBll.SendMethod=Integer.parseInt(pair.value);
                awyBll.SettlementMethod=Integer.parseInt(jspair.value);
                awyBll.BusinessType=Integer.parseInt(ywpair.value);
                awyBll.CostCalMethod=Integer.parseInt(fypair.value);
                awyBll.Vouchertype=CurrVoucherType;
                awyBll.WayBillNo=outstock_sales_config_order.getText().toString().trim();
                awyBll.Trackingnumber=sales_outstock_wlOrder.getText().toString().trim();
                awyBll.Address= outstock_config_address.getText().toString().trim();
              //  awyBll.SendAddress= outstock_config_address.getText().toString().trim();
                awyBll.Contacts=outstock_config_creater.getText().toString().trim();
                awyBll.Creater=BaseApplication.mCurrentUserInfo.getUsername(); //当前登录的人
                //awyBll.Customerno= outstock_config_creater.getText().toString().trim();
                awyBll.InsuranceCost= Float.parseFloat(sales_outstock_configbj_text.getText().toString().trim());
                awyBll.LogisticsCompany= mfhSpinner.getText().toString().trim();
                awyBll.Towarehouseno=BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
                if(pair.value.equals("2")) {
                    awyBll.OutCostTotal= Float.parseFloat(sales_outstock_configsm_text.getText().toString().trim());
                }
                if(fypair.value.equals("4")||fypair.value.equals("5")){
                    awyBll.CostTotal=Float.parseFloat(sales_outstock_configdj_text.getText().toString().trim());
                }else {
                    if (!fypair.value.equals("1")) {
                        awyBll.PrePrice = Float.parseFloat(sales_outstock_configdj_text.getText().toString().trim());
                    } else {
                        awyBll.PrePrice = 0f;
                    }
                }
                String json = GsonUtil.parseModelToJson(awyBll);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_ConfigSaveOrder, "保存托运单号",
                        context, mHandler, RESULT_Saleoutstock_ConfigSaveOrder, null, info.SalesOutstock__Review_configSaveOrder, json, null);
            } else {
                CommonUtil.setEditFocus(sales_outstock_configOrder);
                MessageBox.Show(context, "请先输入或扫描发货通知单号");
                return;
            }
        }catch (Exception EX){
            MessageBox.Show(context,"值不能为空");
            return ;
        }
    }


    //托运单打印
    @Event(value = R.id.sales_outstock_button_configprint)
    private  void Click_Print(View view) {
        try {
            String order=outstock_sales_config_order.getText().toString().trim();
            if(order.equals("无")||order.equals("")) {
                MessageBox.Show(context, "请先选择托运单");
            }
            AwyBll way=new AwyBll();
            way.setErpvoucherno(order);
            way.Printername= UrlInfo.mOutStockPrintName;
            way.Printertype= UrlInfo.mOutStockPrintType;
            //托运单号
            String json = GsonUtil.parseModelToJson(way);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_WaybillPrint, "打印托运单号",
                    context, mHandler, RESUL_Saleoutstock_WaybillPrint, null, info.SalesOutstock__Review_Printwaybill, json, null);
        } catch (Exception ex) {
        }
    }


    //托运单明细
//    @Event(value = R.id.sales_outstock_button_configdetail)
//    private void Click_showdetail(View view) {
//        try {
//            if (awyBll.Erpvoucherno.equals("")) {
//                MessageBox.Show(context, "请先选择或者生成托运单");
//                return;
//            }
//            String json = GsonUtil.parseModelToJson(awyBll);
//            Intent intent = new Intent();
//            //界面传值
//            Uri data = Uri.parse(json);
//            intent.setData(data);
//            intent.setClass(this, OutstockConsignmentDetail.class);
//            startActivity(intent);
//        } catch (Exception ex) {
//            MessageBox.Show(context, "请先选择或者生成托运单");
//            return;
//        }
//    }


    //单号扫描事件
    @Event(value = R.id.sales_outstock_configOrder, type = EditText.OnKeyListener.class)
    private boolean orderKeyDowm(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        //如果是扫描
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == sales_outstock_configOrder.getId()) {
            try {
                String order = sales_outstock_configOrder.getText().toString().trim();
                if (order.equals("")) {
                    CommonUtil.setEditFocus(sales_outstock_configOrder);
                    MessageBox.Show(context, "请先输入或扫描发货通知单号");
                    return true;
                }
                SalesoutstockRequery model = new SalesoutstockRequery();
                model.Erpvoucherno = order;
                model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
                model.Vouchertype=CurrVoucherType;
                String json = GsonUtil.parseModelToJson(model);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_ReviewOrder, "单号检验中",
                        context, mHandler, RESULT_Saleoutstock_ReviewOrder, null, info.SalesOutstock_Review_ScanningNo, json, null);
                return true;
            } catch (Exception ex) {
                CommonUtil.setEditFocus(sales_outstock_configOrder);
                MessageBox.Show(context, ex.toString());
                return true;
            }
        }
        return false;
    }


    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Saleoutstock_ReviewOrder:
                SacnnNo((String) msg.obj);
                break;
            case RESULT_Saleoutstock_ConfigSelectOrder:
                TYorder((String) msg.obj);
                break;
            case  RESULT_Saleoutstock_ConfigSaveOrder:
                SaveOrder((String) msg.obj);
                break;
            case  RESUL_Saleoutstock_WaybillPrint:
                PrintWaybill((String) msg.obj);
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


    //打印托运单
    public  void PrintWaybill(String result) {
        try {
            BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                MessageBox.Show(context, "打印托运单失败，请检查该托运单是否存在可打印的通知单");
                return;
            }
            MessageBox.Show(context, "打印托运单成功");
        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_configOrder);
            MessageBox.Show(context, ex.toString());
        }
    }

    //托运单保存
    public  void  SaveOrder(String result) {
        try {
            BaseResultInfo<OutStockOrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderHeaderInfo>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_configOrder);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            String json = GsonUtil.parseModelToJson(awyBll);
            Intent intent = new Intent();
            //界面传值
            Uri data = Uri.parse(json);
            intent.setData(data);
            intent.setClass(this, OutstockConfigreview.class);
            startActivity(intent);
        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_configOrder);
            MessageBox.Show(context, ex.toString());
        }
    }


    //扫描单号
    public void SacnnNo(String result) {
        try {
            BaseResultInfo<OutStockOrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderHeaderInfo>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_configOrder);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            //得到数据赋值地址
            //   awyBll=returnMsgModel.getData();
            PairAdapter fydapter = new PairAdapter();//费用计算
            PairAdapter jsdapter = new PairAdapter();//计算方式
            jsdapter.pairs = new Pair[1];
            if (returnMsgModel.getData().getSettlementMethod() == 2) {
                //到付   只能是新店和整车
                jsdapter.addPairs(1, "2", "到付", jsdapter.pairs);
                fytype = 2;
                fydapter.pairs = new Pair[2];
                fydapter.addPairs(1, "4", "整车", fydapter.pairs);
                fydapter.addPairs(2, "5", "新店", fydapter.pairs);
                fydapter.bindAdapter(mFytypeAdapter, mfySpinner, fydapter.pairs, context);
                if (feiyongType != 0) {
                    mfySpinner.setSelection(feiyongType - 3);
                    feiyongType = 0;
                }
            } else {
                jsdapter.addPairs(1, "1", "月结", jsdapter.pairs);
                fytype = 1;
                fydapter.pairs = new Pair[3];
                fydapter.addPairs(1, "1", "重量", fydapter.pairs);
                fydapter.addPairs(2, "2", "件数", fydapter.pairs);
                fydapter.addPairs(3, "3", "体积", fydapter.pairs);
                fydapter.bindAdapter(mFytypeAdapter, mfySpinner, fydapter.pairs, context);
                if (feiyongType != 0) {
                    mfySpinner.setSelection(feiyongType - 1);
                    feiyongType = 0;
                }
            }
            jsdapter.bindAdapter(mJstypeAdapter, mjsSpinner, jsdapter.pairs, context);
            CurrVoucherNo = returnMsgModel.getData().getErpvoucherno();
            outstock_config_address.setText(returnMsgModel.getData().getAddress());
            outstock_config_creater.setText(returnMsgModel.getData().getContacts());
            if (returnMsgModel.getData().getOutCostTotal() != 0) {
                mshSpinner.setSelection(1);
            } else {
                mshSpinner.setSelection(0);
            }
            //送货方式
            mfhSpinner.setText(returnMsgModel.getData().getLogisticsCompany());
            //根据地址跟客户获取托运单号
            AwyBll model = new AwyBll();
            awyBll.LinkVoucherNo = returnMsgModel.getData().getErpvoucherno();
            awyBll.Customerno = returnMsgModel.getData().getCustomerno();
            awyBll.Address = returnMsgModel.getData().getAddress();
            awyBll.Customername = returnMsgModel.getData().getCustomername();
            awyBll.Tel = returnMsgModel.getData().getTel();
            awyBll.LogisticsCompany = returnMsgModel.getData().getLogisticsCompany();
            awyBll.WeightTotal = returnMsgModel.getData().getWeightTotal();
            awyBll.OutCostTotal = returnMsgModel.getData().getOutCostTotal();
            awyBll.CostTotal = returnMsgModel.getData().getCostTotal();
            // awyBll.IsStockCombine=returnMsgModel.getData().
            //赋值给
            model.Address = returnMsgModel.getData().getAddress();
            model.Customerno = returnMsgModel.getData().getCustomerno();
            String json = GsonUtil.parseModelToJson(model);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_ConfigSelectOrder, "获取托运单信息",
                    context, mHandler, RESULT_Saleoutstock_ConfigSelectOrder, null, info.SalesOutstock__Review_configSelectOrder.trim(), json, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.toString());
            CommonUtil.setEditFocus(sales_outstock_configOrder);
        }
    }


    //获取托运单号
    public void TYorder(String result) {
        try {
            BaseResultInfo<List<AwyBll>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<AwyBll>>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_configOrder);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            //如果存在一个就是新的
            List<AwyBll> model = new ArrayList<AwyBll>();
            model = returnMsgModel.getData();
            awyBllList = model;
            if (model.size() == 1) {
                awyBll.Erpvoucherno=model.get(0).Erpvoucherno;
                outstock_sales_config_order.setText(model.get(0).Erpvoucherno);
            }
            if (model.size() > 1) {
                //第二次
                SelectOrder();
               // AAA();
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.toString());
            CommonUtil.setEditFocus(sales_outstock_configOrder);

        }
    }


    //选择托运单
    private void SelectOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("选择一个托运单号");
        //    指定下拉列表的显示数据
        final String[] cities = new String[awyBllList.size()];
        cities[0] = "新的托运单号";
        int i = 0;
        for (AwyBll model : awyBllList) {
            if (i > 0) {
                cities[i] = model.Erpvoucherno;
            }
            i++;
        }
        //    设置一个下拉的列表选择项
        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                //再次提交
                try {
                    String order = cities[which];
                    for (AwyBll model : awyBllList) {
                        if (model.Erpvoucherno.equals(order)) {
                            awyBll = model;
                            mshSpinner.setSelection(awyBll.SendMethod-1);
                          //  mjsSpinner.setSelection(awyBll.SettlementMethod-1);
                            mywSpinner.setSelection(awyBll.BusinessType-1);
                            feiyongType=awyBll.CostCalMethod;
//                             if(fytype==1){
//                                 mfySpinner.setSelection(awyBll.CostCalMethod-3);
//                             }else {
//                                 mfySpinner.setSelection(awyBll.CostCalMethod - 1);
//                             }
                            sales_outstock_configbj_text.setText(awyBll.InsuranceCost.toString());
                            if(awyBll.SendMethod==2) {//等于上门
                               sales_outstock_configsm_text.setText(awyBll.OutCostTotal.toString());
                            }
                            if(awyBll.CostCalMethod!=1){//不等于重量
                              sales_outstock_configdj_text.setText(awyBll.PrePrice.toString());
                            }
                        }
                    }
                    if (order.equals("新的托运单号")) {
                        awyBll.Erpvoucherno = awyBllList.get(0).getErpvoucherno();
                    }
                    awyBll.LinkVoucherNo=CurrVoucherNo;
                    outstock_sales_config_order.setText(awyBll.Erpvoucherno);
                } catch (Exception ex) {
                    MessageBox.Show(context, "系统出现异常请重新扫描");
                }
            }
        });
        builder.show();
    }


}