package com.liansu.boduowms.modules.outstock.SalesOutstock;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.modules.outstock.Model.MaterialResponseModel;
import com.liansu.boduowms.modules.outstock.Model.MenuOutStockModel;
import com.liansu.boduowms.modules.outstock.Model.Outbarcode_Requery;
import com.liansu.boduowms.modules.outstock.Model.PackageCartonModel;
import com.liansu.boduowms.modules.outstock.Model.SalesoutStcokboxRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockRequery;
import com.liansu.boduowms.modules.outstock.purchaseReturn.offscan.PurchaseReturnOffScanModel;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.ui.dialog.MessageBox;
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

import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_AreaPlatForm;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_Box_Check_Box;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_Box_Check_waterCode;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_Box_SelectNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_Box_Submit_Box;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_GETBOXlISTl;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_PlatForm;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannParts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESUL_OTAG_OutStock_GetOrderPackageNum;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_OutStock_GetOrderPackageNum;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_Box_SelectNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_Box_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_GETBOXlIST;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_PlatForm;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitParts;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

//拼箱   每次需要注册
@ContentView(R.layout.activity_outstock_sales_box)
public  class SalesOutStockBox   extends BaseActivity {
    Context context = SalesOutStockBox.this;

    //region
    //当前单号
    private String CurrOrder="";

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

    //单选按钮
    @ViewInject(R.id.outstock_box_isCheck)
    CheckBox checkBox;

    //总箱数
    @ViewInject(R.id.outstock_package_boxcount)
    TextView outstock_package_boxcount;

    //总箱数已扫
    @ViewInject(R.id.outstock_package_boxscanning)
    TextView outstock_package_boxscanning;


    //物料存储类 判断是否存在
    Map<String,String> modelIsExits;

    //提交对象
    List<OutStockOrderDetailInfo> stockInfoModels;

    //存储列表对象
    //存储类
    private PurchaseReturnOffScanModel mModel;


    //散件类
    private  MaterialResponseModel   materialModle;


    //多批次对象
     private List<OutStockOrderDetailInfo> responseList;

    private  int CurrVoucherType;

    private  int Scanningtype;

    private   UrlInfo info=new UrlInfo();

    private   MenuOutStockModel menuOutStockModel = new MenuOutStockModel();
    //endregion

    @Override
    protected void initViews() {
        super.initViews();
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        String arr = data.toString();
        menuOutStockModel = GsonUtil.parseJsonToModel(arr, MenuOutStockModel.class);
        int type = Integer.parseInt(menuOutStockModel.VoucherType);
        info.InitUrl(type);
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(menuOutStockModel.Title + "-" + BaseApplication.mCurrentWareHouseInfo.Warehouseno, true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        CurrOrder = "";
        materialModle = new MaterialResponseModel();
        stockInfoModels = new ArrayList<OutStockOrderDetailInfo>();
        //重写路径
        CurrVoucherType = Integer.parseInt(menuOutStockModel.getVoucherType());
        modelIsExits = new HashMap<String, String>();
        Scanningtype = 0;
        responseList = new ArrayList<OutStockOrderDetailInfo>();
        mModel = new PurchaseReturnOffScanModel(context, mHandler);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    // editText1.setText(buttonView.getText()+"选中");
                    OutStockOrderDetailInfo outStockOrderDetailInfo = new OutStockOrderDetailInfo();
                    outStockOrderDetailInfo.setMaterialdesc("非库存拼箱");
                    outStockOrderDetailInfo.setBatchno("");
                    outStockOrderDetailInfo.setErpvoucherno(CurrOrder);
                    outStockOrderDetailInfo.setPostUser(BaseApplication.mCurrentUserInfo.getUserno());
                    outStockOrderDetailInfo.setQTY(1f);
                    outStockOrderDetailInfo.setIsStockCombine(1);
                    outStockOrderDetailInfo.setVouchertype(CurrVoucherType);
                    outStockOrderDetailInfo.setPrintername(UrlInfo.mOutStockPackingBoxPrintName);
                    outStockOrderDetailInfo.setPrintertype(UrlInfo.mOutStockPackingBoxPrintType);
                    boolean isprint = BaseApplication.mCurrentWareHouseInfo.getIsPrint() == 2 ? true : false;
                    outStockOrderDetailInfo.setPrint(isprint);
                    stockInfoModels.add(outStockOrderDetailInfo);
                    mModel.getOrderDetailList().add(outStockOrderDetailInfo);
                } else {
                    for (OutStockOrderDetailInfo infos : stockInfoModels) {
                        if (infos.getIsStockCombine() == 1) {
                            stockInfoModels.remove(infos);
                            mModel.getOrderDetailList().remove(infos);
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
                // mAdapter = new SalesoutstockBoxAdapter(context, mModel.getOrderDetailList());
                // mList.setAdapter(mAdapter);
                //
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    protected UserSettingPresenter mUserSettingPresenter;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_setting_warehouse_select) {
            selectWareHouse(this);

        }
        return false;
    }

    @Override
    public void getToolTitle() {
        getToolBarHelper().getToolBar().setTitle(menuOutStockModel.Title + "-" + BaseApplication.mCurrentWareHouseInfo.Warehouseno);
        //清空列表//切换仓库后需要重新扫描
        mModel=new PurchaseReturnOffScanModel(context, mHandler);
        checkBox.setChecked(false);
        //stockInfoModels = new ArrayList<OutStockOrderDetailInfo>();
        del();
    }

    //当上一个界面返回后会触发这个方法
    @Override
    protected void onResume() {
        super.onResume();
        if (!CurrOrder.equals("")&& CurrOrder !=null) {
            SalesoutStcokboxRequery model = new SalesoutStcokboxRequery();
            model.Erpvoucherno = CurrOrder;
            model.Vouchertype = CurrVoucherType;
            String modelJson = parseModelToJson(model);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_Box_SelectNO, "订单提交中",
                    context, mHandler, RESULT_Saleoutstock_Box_SelectNO, null, info.SalesOutstock_Box_ScanningNo, modelJson, null);
        }
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
                            context, mHandler, RESULT_Saleoutstock_Box_SelectNO, null, info.SalesOutstock_Box_ScanningNo, modelJson, null);
                    return true;
                }else{
                    CommonUtil.setEditFocus(sales_outstock_box_order);
                    MessageBox.Show(context, "请输入或扫描单号");
                    return true;
                }
            } catch (Exception ex) {
                CommonUtil.setEditFocus(sales_outstock_box_order);
                MessageBox.Show(context, ex.toString());
                return true;
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
                    String barcode = sales_outstock_box_watercode.getText().toString().trim();
                    if(barcode.equals("")) {
                        CommonUtil.setEditFocus(sales_outstock_box_watercode);
                        MessageBox.Show(context, "请先输入或扫描条码");
                        return true;
                    }
                    //可能是箱号 69码，物料号
                    String[] arr = barcode.split("%");
                    //不是箱号
                    if (arr.length == 1) {
                        Scanningtype = 2;
                        //查询该物料是否存在
                        String modelJson = parseModelToJson(barcode);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitParts, "检验是否存在",
                                context, mHandler, RESULT_Saleoutstock_ScannParts, null, info.SelectMaterial, modelJson, null);
                        return true;
                    }
                   else  if (arr.length == 4||arr.length==3||arr.length==2) {
                        if(BaseApplication.mCurrentWareHouseInfo.getIsPrint()!=2) {
                            MessageBox.Show(context, "该仓库不能打印外箱拼箱");
                            CommonUtil.setEditFocus(sales_outstock_box_watercode);
                            return true;
                        }
                            //先判断这个物料有没有扫描满
                            Scanningtype=1;
                            //箱号
                            //直接调用拼箱方法
                            SalesoutStcokboxRequery model = new SalesoutStcokboxRequery();
                            if(arr.length>2){
                                model.Batchno = arr[1];
                                model.Qty = Float.parseFloat(arr[2]);
                            }else{
                                model.Qty = Float.parseFloat(arr[1]);
                            }
                           // model.BoxType=BaseApplication.mCurrentWareHouseInfo.getIsPrint();
                            model.Materialno = barcode;
                            model.Erpvoucherno = CurrOrder;
                            model.PostUser = BaseApplication.mCurrentUserInfo.getUserno();
                            model.Vouchertype = CurrVoucherType;
                            String modelJson = parseModelToJson(model);
                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_Box_Submit, "提交物料中",
                                    context, mHandler, RESULT_Saleoutstock_Box_Check_Box, null, info.SalesOutstock_Box_Batchno, modelJson, null);
                            return true;
                    }else{
                       MessageBox.Show(context,"请扫描正确的箱号跟物料码");
                        CommonUtil.setEditFocus(sales_outstock_box_watercode);
                        return true;
                    }
                } catch (Exception ex) {
                    MessageBox.Show(context, ex.toString());
                    CommonUtil.setEditFocus(sales_outstock_box_watercode);
                    return true;
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
                            context, mHandler, RESULT_Saleoutstock_Box_Submit_Box, null, info.SalesOutstock_Box_Submit, modelJson, null);
                } else {
                    CommonUtil.setEditFocus(sales_outstock_box_watercode);
                    MessageBox.Show(context, "列表中不存在可提交的物料,请先扫描69码/物料");
                }
            }
        } catch (Exception EX) {
            CommonUtil.setEditFocus(sales_outstock_box_watercode);
            MessageBox.Show(context, EX.toString());
        }

    }

    //删除
    @Event(value =R.id.outstock_sales_deletebuttonp)
    private void  Click_DelBox(View view) {
        if (IsSacnningOrder()) {
            if (stockInfoModels.size() > 0) {
                del();
                MessageBox.Show(context, "删除成功");

            } else {
                CommonUtil.setEditFocus(sales_outstock_box_watercode);
                MessageBox.Show(context, "当前无数据可删除");
            }
        }
    }

    public  void del() {
        sales_outstock_box_watercode.setText("");
        materialModle = new MaterialResponseModel();
        stockInfoModels = new ArrayList<OutStockOrderDetailInfo>();
        mAdapter = new SalesoutstockBoxAdapter(context, stockInfoModels);
        responseList = new ArrayList<OutStockOrderDetailInfo>();
        modelIsExits = new HashMap<String, String>();
        mList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        CommonUtil.setEditFocus(sales_outstock_box_order);
    }



    //拼箱列表
    @Event(value =R.id.outstock_sales_buttoList)
    private void  Click_showBoxList(View view){
        Intent intent = new Intent();
        //intent.setData(data);
        //本地单号传过去
        Outbarcode_Requery model=new Outbarcode_Requery();
        model.Barcode=CurrOrder;
        model.Vouchertype=CurrVoucherType;
        String json=  GsonUtil.parseModelToJson(model);
        Uri data = Uri.parse(json);
        intent.setData(data);
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
            case  RESULT_Saleoutstock_GETBOXlISTl:
                 LoadBoxList((String)msg.obj);
                break;
            case   RESUL_OTAG_OutStock_GetOrderPackageNum:
                 LoadPackageCarton((String)msg.obj);
                break;
            case    RESULT_Saleoutstock_AreaPlatForm:
                AreaPlat((String) msg.obj);
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
    //endregion

    public  void LoadPackageCarton(String result) {
        try {
            BaseResultInfo<PackageCartonModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<PackageCartonModel>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_box_order);
                del();
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            outstock_package_boxcount.setText(String.valueOf(returnMsgModel.getData().Qty));
            outstock_package_boxscanning.setText(String.valueOf(returnMsgModel.getData().PackageNum));
            boolean isScannover=true;
            for(OutStockOrderDetailInfo item:mModel.getOrderDetailList()){
                 if(item.getRemainqty()!=0){
                     isScannover=false;
                 }
            }
            if(isScannover) {
                Float count = ArithUtil.add(returnMsgModel.getData().Qty, returnMsgModel.getData().PackageNum);
                MessageBox.Show(context, "请确认，拼箱已经全部完成 总件数为" + count);
                return;
            }
        } catch (Exception ex) {
            MessageBox.Show(context, ex.toString());
        }
    }



    //展示拼箱列表
    public  void LoadBoxList(String result) {
        try {
            BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_box_order);
                del();
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            //先判断是否有非库存拼箱 如果有加入到扫描的集合中
            stockInfoModels = new ArrayList<OutStockOrderDetailInfo>();
            modelIsExits = new HashMap<String, String>();
            mModel.setOrderDetailList(returnMsgModel.getData());
            mAdapter = new SalesoutstockBoxAdapter(context, mModel.getOrderDetailList());
            mList.setAdapter(mAdapter);
            CommonUtil.setEditFocus(sales_outstock_box_watercode);
            CurrOrder = sales_outstock_box_order.getText().toString().trim();
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
            }
            //查询拼箱箱数
            SalesoutstockRequery model=new SalesoutstockRequery();
            model.Erpvoucherno=CurrOrder;
            model.Vouchertype=CurrVoucherType;
            String modelJson = parseModelToJson(model);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_OutStock_GetOrderPackageNum, "查询箱号数据中",
                    context, mHandler, RESUL_OTAG_OutStock_GetOrderPackageNum, null, info.GetOrderPackageNum, modelJson, null);
        } catch (Exception ex) {
            MessageBox.Show(context, ex.toString());
        }
    }



    //检验
    public  void   CheckBox(String result,int type) {
        try {
            responseList = new ArrayList<OutStockOrderDetailInfo>();
            BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
            }.getType());
            if (type == 0) { // 箱
                //多批次
                if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_ACTION_CONTINUE) {
                    responseList = returnMsgModel.getData();
                    if (responseList.size() > 0) {
                        //选择批次
                        // SelectBatchno(returnMsgModel.getData());
                        List<SalesoutStcokboxRequery> listmodel = new ArrayList<SalesoutStcokboxRequery>();
                        SalesoutStcokboxRequery model = new SalesoutStcokboxRequery();
                        model.Batchno = "";
                        model.Materialno = returnMsgModel.getData().get(0).getMaterialno();
                        model.Erpvoucherno = CurrOrder;
                        if (BaseApplication.mCurrentWareHouseInfo.getIsPrint() == 2) {
                            model.IsPrint = true;
                        } else {
                            model.IsPrint = false;
                        }
                        model.Barcodetype = 2;
                        model.PostUser = BaseApplication.mCurrentUserInfo.getUsername();
                       // model.Qty = returnMsgModel.getData().get(0).getOutstockqty();
                        // model.Qty = reOuterQty;
                        model.Vouchertype = CurrVoucherType;
                        model.Printername = UrlInfo.mOutStockPackingBoxPrintName;
                        model.Printertype = UrlInfo.mOutStockPackingBoxPrintType;

                        listmodel.add(model);
                        String modelJson = parseModelToJson(listmodel);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_Box_Submit, "提交物料中",
                                context, mHandler, RESULT_Saleoutstock_Box_Submit_Box, null, info.SalesOutstock_Box_Submit, modelJson, null);
                    } else {
                        CommonUtil.setEditFocus(sales_outstock_box_watercode);
                        MessageBox.Show(context, "没有批次数据");
                        return;
                    }

                } else {
                    if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                        CommonUtil.setEditFocus(sales_outstock_box_watercode);
                        MessageBox.Show(context, returnMsgModel.getResultValue());
                        return;
                    }
                    //更新列表刷新 然后提交
                    List<OutStockOrderDetailInfo> list = returnMsgModel.getData();
                    //直接提交
                    String[] arr = sales_outstock_box_watercode.getText().toString().trim().split("%");
                    List<SalesoutStcokboxRequery> listmodel = new ArrayList<SalesoutStcokboxRequery>();
                    SalesoutStcokboxRequery model = new SalesoutStcokboxRequery();
                    model.Batchno = list.get(0).getBatchno();
                    model.Materialno = returnMsgModel.getData().get(0).getMaterialno();
                    ;
                    model.Barcodetype = 2;
                    model.Erpvoucherno = CurrOrder;
                    model.PostUser = BaseApplication.mCurrentUserInfo.getUserno();
                    //   model.Qty = materialModle.OuterQty;
                    model.Vouchertype = CurrVoucherType;
                    if (BaseApplication.mCurrentWareHouseInfo.getIsPrint() == 2) {
                        model.IsPrint = true;
                    } else {
                        model.IsPrint = false;
                    }
                    model.Printername = UrlInfo.mOutStockPackingBoxPrintName;
                    model.Printertype = UrlInfo.mOutStockPackingBoxPrintType;
                    listmodel.add(model);
                    String modelJson = parseModelToJson(listmodel);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_Box_Submit, "提交物料中",
                            context, mHandler, RESULT_Saleoutstock_Box_Submit_Box, null, info.SalesOutstock_Box_Submit, modelJson, null);
                }
            } else {
                //提交列表拼箱
                if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_ACTION_CONTINUE) {
                    responseList = returnMsgModel.getData();

                    if (responseList.size() > 0) {
                        //  SelectBatchno(returnMsgModel.getData());
                        List<OutStockOrderDetailInfo> list = returnMsgModel.getData();
                        Float scanQty = 0f;
                        for (OutStockOrderDetailInfo item : mModel.getOrderDetailList()) {
                            if (item.getMaterialno() != null) {//勾选非库存拼箱会存在这种情况
                                if (item.getMaterialno().equals(materialModle.Materialno)) {
                                    if (ArithUtil.sub(item.getRemainqty(), 1f) < 0) {
                                        scanQty = item.getRemainqty();
                                    } else {
                                        scanQty = 1f;
                                    }
                                }
                            }
                        }
                        //批次给空
                        String name = modelIsExits.get(returnMsgModel.getData().get(0).getMaterialno() + "");
                        if (name == null || name.equals("")) {
                            //不存在
                            //添加到ListView
                            OutStockOrderDetailInfo model = new OutStockOrderDetailInfo();
                            model.setMaterialno(list.get(0).getMaterialno());
                            model.setMaterialdesc(list.get(0).getMaterialdesc());
                            model.setBatchno("");
                            model.setErpvoucherno(CurrOrder);
                            model.setPostUser(BaseApplication.mCurrentUserInfo.getUsername());
                            model.setQTY(scanQty);
                            if (model.getMaterialdesc() == null) {
                                model.setMaterialdesc("");
                            }
                            boolean isprint = BaseApplication.mCurrentWareHouseInfo.getIsPrint() == 2 ? true : false;
                            model.setPrint(isprint);
                            model.setReviewQty(scanQty);
                            model.setBarcodeType(3);
                            model.setVouchertype(CurrVoucherType);
                            model.setPrintername(UrlInfo.mOutStockPackingBoxPrintName);
                            model.setPrintertype(UrlInfo.mOutStockPackingBoxPrintType);
                            modelIsExits.put(list.get(0).getMaterialno() + "", list.get(0).getMaterialno());
                            stockInfoModels.add(model);
                            //加到listview
                            mModel.UpdateMaterialItem(model);
                            //      mList.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            //存在
                            for (OutStockOrderDetailInfo infos : stockInfoModels) {
                                if (infos.getMaterialno().equals(returnMsgModel.getData().get(0).getMaterialno()) && infos.getBatchno().equals("")) {
                                    Float reviewqty = ArithUtil.add(infos.getReviewQty(), scanQty);//已拼
                                    Float remainqty = ArithUtil.sub(reviewqty, scanQty);//未拼
                                    infos.setQTY(ArithUtil.add(infos.getQty(), 1f));
                                    infos.setReviewQty(scanQty);
                                    mModel.UpdateMaterialItem(infos);
                                    mList.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();
                                    infos.setReviewQty(reviewqty);
                                    infos.setRemainqty(remainqty);
                                }
                            }
                        }
                    } else {
                        CommonUtil.setEditFocus(sales_outstock_box_watercode);
                        MessageBox.Show(context, "没有批次数据");
                        return;
                    }
                    sales_outstock_box_watercode.setText("");
                    //  MessageBox.Show(context, returnMsgModel.getResultValue());
                }
                if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_OK) {
                    List<OutStockOrderDetailInfo> list = returnMsgModel.getData();
                    Float scanQty = 0f;
                    Float remainQty=0F;
                    for (OutStockOrderDetailInfo item : mModel.getOrderDetailList()) {
                        if (item.getMaterialno() != null) {//勾选非库存拼箱会存在这种情况
                            if (item.getMaterialno().equals(materialModle.Materialno)) {
                                remainQty=item.getRemainqty();;
                                if (ArithUtil.sub(item.getRemainqty(), 1f) < 0) {
                                    scanQty = item.getRemainqty();
                                } else {
                                    scanQty = 1f;
                                }
                            }
                        }
                    }
                    //只有一个批次
                    //直接刷新listView
                    String name = modelIsExits.get(list.get(0).getMaterialno() + list.get(0).getBatchno());
                    if (name == null || name.equals("")) {
                        //不存在
                        //添加到ListView
                        OutStockOrderDetailInfo model = new OutStockOrderDetailInfo();
                        model.setMaterialno(list.get(0).getMaterialno());
                        model.setMaterialdesc(list.get(0).getMaterialdesc());
                        model.setBatchno(list.get(0).getBatchno());
                        model.setErpvoucherno(CurrOrder);
                        model.setPostUser(BaseApplication.mCurrentUserInfo.getUsername());
                        model.setQTY(scanQty);
                        boolean isprint = BaseApplication.mCurrentWareHouseInfo.getIsPrint() == 2 ? true : false;
                        model.setPrint(isprint);
                        model.setReviewQty(scanQty);
                        if (model.getMaterialdesc() == null) {
                            model.setMaterialdesc("");
                        }
                        model.setMaterialdesc("");
                        model.setVouchertype(CurrVoucherType);
                        model.setPrintername(UrlInfo.mOutStockPackingBoxPrintName);
                        model.setPrintertype(UrlInfo.mOutStockPackingBoxPrintType);
                        modelIsExits.put(list.get(0).getMaterialno() + list.get(0).getBatchno(), list.get(0).getMaterialno());
                        stockInfoModels.add(model);
                        //加到listview
                        mModel.UpdateMaterialItem(model);
                        //      mList.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        //存在
                        for (OutStockOrderDetailInfo infos : stockInfoModels) {
                            if (!infos.getMaterialdesc().equals("非库存拼箱")) {
                                if (infos.getMaterialno().equals(returnMsgModel.getData().get(0).getMaterialno()) && infos.getBatchno().equals(returnMsgModel.getData().get(0).getBatchno())) {
                                    Float reviewqty = ArithUtil.add(infos.getReviewQty(), scanQty);//已拼
                                    Float remainqty = ArithUtil.sub(remainQty, scanQty);//未拼
                                    infos.setQTY(ArithUtil.add(infos.getQty(), scanQty));
                                    infos.setReviewQty(scanQty);
                                    mModel.UpdateMaterialItem(infos);
                                    mList.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();
                                    infos.setReviewQty(reviewqty);
                                    infos.setRemainqty(remainqty);
                                }
                            }
                        }
                    }
                    sales_outstock_box_watercode.setText("");
                    CommonUtil.setEditFocus(sales_outstock_box_watercode);
                }
                if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_DEFAULT) {
                    CommonUtil.setEditFocus(sales_outstock_box_watercode);
                    MessageBox.Show(context, returnMsgModel.getResultValue());
                }
            }
        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_box_watercode);
            MessageBox.Show(context, ex.toString());
        }
    }


    //提交
    public  void   SubmitBox(String result) {
        try {
            BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
            }.getType());
            if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_OK) {
                String order=CurrOrder;
                Map<String,String> map=new HashMap<>();
                map.put("ErpVoucherNo",order);
                map.put("Towarehouseno",BaseApplication.mCurrentWareHouseInfo.Warehouseno);
                RequestHandler.addRequestWithDialog(Request.Method.GET, TAG_Saleoutstock_GETBOXlIST, "获取拼箱列表",
                        context, mHandler, RESULT_Saleoutstock_GETBOXlISTl, null, info.SalesOutstock_BoxList, map, null);
            }
            CommonUtil.setEditFocus(sales_outstock_box_watercode);
            MessageBox.Show(context, returnMsgModel.getResultValue(),2,null);
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
            del();
            MessageBox.Show(context, returnMsgModel.getResultValue());
            return;
        }
        //成功访问拼箱所有订单
        String order=sales_outstock_box_order.getText().toString().trim();
        Map<String,String> map=new HashMap<>();
        map.put("ErpVoucherNo",order);
        map.put("Towarehouseno",BaseApplication.mCurrentWareHouseInfo.Warehouseno);
        RequestHandler.addRequestWithDialog(Request.Method.GET, TAG_Saleoutstock_GETBOXlIST, "获取拼箱列表",
                context, mHandler, RESULT_Saleoutstock_GETBOXlISTl, null, info.SalesOutstock_BoxList, map, null);
       // CommonUtil.setEditFocus(sales_outstock_box_watercode);
       // CurrOrder = sales_outstock_box_order.getText().toString().trim();
    }

    //月台返回
    public  void    ResultPlatForm(String result) {
        BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
        }.getType());
        if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
            MessageBox.Show(context, returnMsgModel.getResultValue());
            return;
        }
        MessageBox.Show(context, "提交成功",2,null);
    }

    public  void AreaPlat(String result) {
        BaseResultInfo<AreaInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<AreaInfo>>() {
        }.getType());
        if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
            CommonUtil.setEditFocus(sales_outstock_box_order);
            MessageBox.Show(context, returnMsgModel.getResultValue());
            return;
        }
        Platform model = new Platform();
        model.Erpvoucherno = CurrOrder;
        model.Platform = returnMsgModel.getData().getAreano();
        String json = GsonUtil.parseModelToJson(model);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_PlatForm, "月台提交",
                context, mHandler, RESULT_Saleoutstock_PlatForm, null, info.SalesOutstock_PlatForm, json, null);

    }




    //扫描物料或者69码获取对象
    public  void   ScannParts(String  result) {
        try {
            BaseResultInfo<MaterialResponseModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<MaterialResponseModel>>() {
            }.getType());
            //先判断是否存在列表中剩余是否等于小数
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                if(returnMsgModel.getData()!=null){//旧外箱
                    if(BaseApplication.mCurrentWareHouseInfo.getIsPrint()!=2) {
                        MessageBox.Show(context, "该仓库不能打印外箱拼箱");
                        CommonUtil.setEditFocus(sales_outstock_box_watercode);
                        return;
                    }
                    MaterialResponseModel material=returnMsgModel.getData();
                    materialModle=returnMsgModel.getData();
                    //查找是否存在这个东西
                    int judge=0;
                    Float  qty=0f;
                    for(OutStockOrderDetailInfo item:mModel.getOrderDetailList()) {
                        if (item.getMaterialno() == materialModle.getMaterialno()) {
                            judge=1;
                            if (ArithUtil.sub(item.getRemainqty(), material.OuterQty) < 0) {
                                qty=item.getRemainqty();
                            }else{
                                qty=material.OuterQty;
                            }
                        }
                    }
                        if (judge!=1) {
                            MessageBox.Show(context, "列表中未找到该物料信息");
                            CommonUtil.setEditFocus(sales_outstock_box_watercode);
                            return;
                        }
                    Scanningtype=1;
                    //箱号
                    //直接调用拼箱方法
                    SalesoutStcokboxRequery model = new SalesoutStcokboxRequery();
                    model.IsPrint=true;
                    model.Batchno ="";
                    model.Materialno = material.Materialno;
                    model.Erpvoucherno = CurrOrder;
                    model.PostUser = BaseApplication.mCurrentUserInfo.getUserno();
                    model.Qty = qty;
                    model.Vouchertype = CurrVoucherType;
                    String modelJson = parseModelToJson(model);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_Box_Submit, "提交物料中",
                            context, mHandler, RESULT_Saleoutstock_Box_Check_Box, null, info.SalesOutstock_Box_Batchno, modelJson, null);
                         return;
                }else{
                    CommonUtil.setEditFocus(sales_outstock_box_watercode);
                    MessageBox.Show(context, returnMsgModel.getResultValue());
                    return;
                }
            }
            materialModle=returnMsgModel.getData();
            int type=0;
            Float sanjianQty=0f;
            for (OutStockOrderDetailInfo item:mModel.getOrderDetailList()) {
                if (item.getMaterialno() != null) {//勾选非库存拼箱会存在这种情况
                    if (item.getMaterialno().equals(materialModle.Materialno)) {
                        type = 1;
                        if (item.getRemainqty() == 0) {
                            //不能再扫描
                            type = 2;
                        }
                        if(ArithUtil.sub(item.getRemainqty(),1f)<0){
                            sanjianQty=item.getRemainqty();
                        }else{
                            sanjianQty=1f;
                        }
                    }
                }
            }
            //没有该物料
             if(type==0){
                 MessageBox.Show(context,"物料:"+ materialModle.Materialno+"不存在单号:"+CurrOrder+"中");
                 CommonUtil.setEditFocus(sales_outstock_box_watercode);
                 return;
             }
             if(type==2) {
                 MessageBox.Show(context, "物料：" + materialModle.Materialno + "已经全部扫描完成");
                 CommonUtil.setEditFocus(sales_outstock_box_watercode);
                 return;
             }
          //  num=1f;
            SalesoutStcokboxRequery model = new SalesoutStcokboxRequery();
            model.Batchno = "";
            model.Materialno = materialModle.Materialno;
            model.Erpvoucherno = CurrOrder;
            model.PostUser = BaseApplication.mCurrentUserInfo.getUserno();
            model.Qty =sanjianQty;
            model.IsPrint=BaseApplication.mCurrentWareHouseInfo.getIsPrint()==2?true:false;
            model.Vouchertype = CurrVoucherType;
            String modelJson = parseModelToJson(model);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_Box_Submit, "提交物料中",
                    context, mHandler, RESULT_Saleoutstock_Box_Check_waterCode, null, info.SalesOutstock_Box_Batchno, modelJson, null);
           // inputTitleDialog("输入散件数量");
        } catch (Exception EX) {
            CommonUtil.setEditFocus(sales_outstock_box_watercode);
            MessageBox.Show(context, EX.toString());

        }
    }

    //是否扫描过单号
    public  boolean IsSacnningOrder() {
        if (CurrOrder.equals("")) {
            CommonUtil.setEditFocus(sales_outstock_box_order);
            MessageBox.Show(context, "请先输入或扫描单号");
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
                inputServer.setSingleLine(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(name).setIcon(
                        null).setView(inputServer).setNegativeButton(
                        "取消", null);
                builder.setPositiveButton("确认提交月台",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                inputYuetai = inputServer.getText().toString();
                                if(inputYuetai.equals("")){
                                    MessageBox.Show(context, "月台不能为空");
                                  return;
                                }
                                Platform model = new Platform();
                                model.Erpvoucherno = CurrOrder;
                                model.Platform = inputYuetai;
                                String json = GsonUtil.parseModelToJson(model);
                                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_PlatForm, "月台提交",
                                        context, mHandler, RESULT_Saleoutstock_PlatForm, null, info.SalesOutstock_PlatForm, json, null);

//                                AreaInfo model=new AreaInfo();
//                                model.setWarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
//                                model.setAreano(inputYuetai);
//                                String json = GsonUtil.parseModelToJson(model);
//                                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_AreaPlatForm, "检验月台",
//                                        context, mHandler, RESULT_Saleoutstock_AreaPlatForm, null, info.GetT_AreaModel, json, null);

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
                        model.PostUser = BaseApplication.mCurrentUserInfo.getUserno();
                        model.Qty = Float.parseFloat(boxno.split("%")[2]);
                        model.Vouchertype = CurrVoucherType;
                        model.Printername=UrlInfo.mOutStockPackingBoxPrintName;
                        model.Printertype=UrlInfo.mOutStockPackingBoxPrintType;
                        lista.add(model);
                        String modelJson = parseModelToJson(lista);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_Box_Submit, "拼箱提交中",
                                context, mHandler, RESULT_Saleoutstock_Box_Submit_Box, null, info.SalesOutstock_Box_Submit, modelJson, null);
                    }else
                    {
//                        //先找到返回的对象
//                        OutStockOrderDetailInfo detailinfo=new OutStockOrderDetailInfo();
//                        for (OutStockOrderDetailInfo item:responseList) {
//                            if (item.getMaterialno().equals(materialModle.Materialno) && item.getBatchno().equals(cities[which])) {
//                                detailinfo = item;
//                            }
//                        }
//                        //再找到存在的对象
//                        for (OutStockOrderDetailInfo item:stockInfoModels) {
//                            if (item.getMaterialno().equals(materialModle.Materialno) && item.getBatchno().equals(cities[which])) {
//                                //判断是否大于下架量/包装量
//                                Float value = ArithUtil.add(item.getQty(), 1f);
//                                if (ArithUtil.sub(detailinfo.getQty(), value) < 0) {
//                                    MessageBox.Show(context, "该散件的数量已经大于下架数" + detailinfo.getQty());
//                                    return;
//                                }
//                            }
//                        }
//                        List<OutStockOrderDetailInfo> listModel=new ArrayList<OutStockOrderDetailInfo>();
//                        String detailinfoModel = parseModelToJson(stockInfoModels);
//                        listModel=  GsonUtil.getGsonUtil().fromJson(detailinfoModel, new TypeToken<List<OutStockOrderDetailInfo>>() {
//                        }.getType());
//                        //散件  刷新界面
//                        String name = modelIsExits.get(materialModle.Materialno+ cities[which]);

                        String name = modelIsExits.get(materialModle.Materialno+cities[which]);
                         //判断这个有没有
                        if (name==null||name.equals("")) {
                            //不存在
                            //添加到ListView
                            OutStockOrderDetailInfo model = new OutStockOrderDetailInfo();
                            model.setMaterialno(materialModle.Materialno);
                            model.setMaterialdesc(materialModle.Materialdesc);
                            model.setBatchno(cities[which]);
                            model.setErpvoucherno(CurrOrder);
                            model.setPostUser(BaseApplication.mCurrentUserInfo.getUserno());
                            model.setQTY(1f);
                            model.setReviewQty(1f);
                            model.setVouchertype(CurrVoucherType);
                            model.setPrintername(UrlInfo.mOutStockPackingBoxPrintName);
                            model.setPrintertype(UrlInfo.mOutStockPackingBoxPrintType);
                            modelIsExits.put(materialModle.Materialno+cities[which],materialModle.Materialno);
                            stockInfoModels.add(model);
                            //加到listview
                            mModel.UpdateMaterialItem(model);
                            //      mList.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            //存在
                            for (OutStockOrderDetailInfo infos : stockInfoModels) {
                                if (infos.getMaterialno().equals(materialModle.Materialno)&&infos.getBatchno().equals(cities[which])  ) {
                                    Float reviewqty =ArithUtil.add(infos.getReviewQty() ,1f);//已拼
                                    Float remainqty =ArithUtil.sub( reviewqty,1f);//未拼
                                    mModel.UpdateMaterialItem(infos);
                                    mList.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();
                                    infos.setQTY(ArithUtil.add(infos.getQty(),1f));
                                    infos.setReviewQty(reviewqty);
                                    infos.setReviewQty(remainqty);
                                }
                            }
                        }
//                        //先找到返回的对象
//                        OutStockOrderDetailInfo detailinfo=new OutStockOrderDetailInfo();
//                        for (OutStockOrderDetailInfo item:responseList) {
//                            if (item.getMaterialno().equals(materialModle.Materialno) && item.getBatchno().equals(cities[which])) {
//                                detailinfo = item;
//                            }
//                        }
//                        //再找到存在的对象
//                        for (OutStockOrderDetailInfo item:stockInfoModels) {
//                            if (item.getMaterialno().equals(materialModle.Materialno) && item.getBatchno().equals(cities[which])) {
//                                //判断是否大于下架量/包装量
//                                Float value = ArithUtil.add(item.getQty(), 1f);
//                                if (ArithUtil.sub(detailinfo.getQty(), value) < 0) {
//                                    MessageBox.Show(context, "该散件的数量已经大于下架数" + detailinfo.getQty());
//                                    return;
//                                }
//                            }
//                        }
//                        List<OutStockOrderDetailInfo> listModel=new ArrayList<OutStockOrderDetailInfo>();
//                        String detailinfoModel = parseModelToJson(stockInfoModels);
//                        listModel=  GsonUtil.getGsonUtil().fromJson(detailinfoModel, new TypeToken<List<OutStockOrderDetailInfo>>() {
//                        }.getType());
//                        //散件  刷新界面
//                        String name = modelIsExits.get(materialModle.Materialno+ cities[which]);
//                        if (name==null||name.equals("")) {
//                            //不存在
//                            //添加到ListView
//                            OutStockOrderDetailInfo model = new OutStockOrderDetailInfo();
//                            model.setMaterialno(materialModle.Materialno);
//                            model.setMaterialdesc(materialModle.Materialdesc);
//                            model.setBatchno(cities[which]);
//                            model.setErpvoucherno(CurrOrder);
//                            model.setPostUser(BaseApplication.mCurrentUserInfo.getUserno());
//                            model.setQTY(1f);
//                            model.setPrintername(UrlInfo.mOutStockPrintName);
//                            model.setPrintertype(UrlInfo.mOutStockPrintType);
//                            model.setVouchertype(CurrVoucherType);
//                            modelIsExits.put(materialModle.Materialno+cities[which],materialModle.Materialno);
//                            listModel.add(0,model);
//                        } else {
//                        //    listModel=stockInfoModels;
//                            //存在
//                            for (OutStockOrderDetailInfo infos : listModel) {
//                                if (infos.getMaterialno().equals(materialModle.Materialno) && infos.getBatchno().equals(cities[which])) {
//                                    listModel.remove(infos);
//                                    Float value =ArithUtil.add(infos.getQty() , 1f);
//                                    infos.setQTY(value);
//                                    listModel.add(0,infos);
//                                }
//                            }
//                        }
//                        stockInfoModels=listModel;
//                        mAdapter = new SalesoutstockBoxAdapter(context, listModel);
//                        mList.setAdapter(mAdapter);
//                        mAdapter.notifyDataSetChanged();
                    }
                }catch (Exception ex) {
                    MessageBox.Show(context, ex.toString());
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
        inputServer.setSingleLine(true);
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
                            model.PostUser = BaseApplication.mCurrentUserInfo.getUserno();
                            model.Qty =inputValue;
                            model.Vouchertype = CurrVoucherType;
                            String modelJson = parseModelToJson(model);
                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_Box_Submit, "提交物料中",
                                    context, mHandler, RESULT_Saleoutstock_Box_Check_waterCode, null, info.SalesOutstock_Box_Batchno, modelJson, null);

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
