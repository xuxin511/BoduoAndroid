package com.liansu.boduowms.modules.outstock.SalesOutstock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.ArrayMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.bean.warehouse.WareHouseInfo;
import com.liansu.boduowms.modules.outstock.Model.MaterialResponseModel;
import com.liansu.boduowms.modules.outstock.Model.MenuOutStockModel;
import com.liansu.boduowms.modules.outstock.Model.Outbarcode_Requery;
import com.liansu.boduowms.modules.outstock.Model.SalesoustockReviewRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockRequery;
import com.liansu.boduowms.modules.outstock.purchaseInspection.scan.PurchaseInspectionProcessingModel;
import com.liansu.boduowms.modules.outstock.purchaseReturn.offscan.PurchaseReturnOffScanModel;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.modules.stockRollBack.StockRollBack;
import com.liansu.boduowms.ui.adapter.outstock.offscan.BaseOffShelfScanDetailAdapter;
import com.liansu.boduowms.ui.adapter.outstock.offscan.OffShelfScanDetailAdapter;
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

import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_box;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_pallet;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_parts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_AreaPlatForm;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_PlatForm;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_PostReview;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_SalesNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannBoxNo;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannPalletNo;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannParts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannParts_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_barcodeisExist;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_AreaPlatForm;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_PlatForm;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_PostReview;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SelectNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitBox;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitPallet;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitParts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitParts_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_barcodeisExist;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

//销售出库   zl  2020-8-6
@ContentView(R.layout.activity_outstock_sales)
public class SalesOutstock  extends BaseActivity  {
    Context context = SalesOutstock.this;

    //region  控件

    //司机
    @ViewInject(R.id.sales_outstock_driver)
    EditText sales_outstock_driver;


    //订单框
    @ViewInject(R.id.sales_outstock_order)
    EditText sales_outstock_order;

    //托盘框
    @ViewInject(R.id.sales_outstock_pallettext)
    EditText sales_outstock_pallettext;


    //箱（69码 物料）框
    @ViewInject(R.id.sales_outstock_boxtext)
    EditText sales_outstock_boxtext;

    //列表框
    @ViewInject(R.id.out_stock_sales_ListView)
    ListView mList;

    //地址
    @ViewInject(R.id.sales_outstock_address)
    TextView sales_outstock_address;


    //整箱
    @ViewInject(R.id.outstock_sales_boxnum)
    TextView outstock_sales_boxnum;

    //已下架
    @ViewInject(R.id.outstock_sales_shelf)
    TextView outstock_sales_shelf;

    //整托
    @ViewInject(R.id.sales_outstock_rediobutton_pallet)
    RadioButton radioGroup_pallet;

    //整箱
    @ViewInject(R.id.sales_outstock_rediobutton_box)
    RadioButton radioGroup_box;

    //散件
    @ViewInject(R.id.sales_outstock_rediobutton_san)
    RadioButton radioGroup_san;

    //散件
    @ViewInject(R.id.sales_outstock_radiogroup)
    RadioGroup radioGroup;

    //listview    适配器
    SalesoutstockAdapter mAdapter;

    //endregion


    //全局属性扫描类型
    private  String OutStock_Type;


    //当前单号
    private  String CurrOrderNO="";

    //当前类型
    private int CurrVoucherType;

    //当前托盘  因为可以输入序列号导致不能用文本框中的托盘号
    private  String Currpalletno="";


    //据点集合
    Map<String,String> StrongholdcodeList=new HashMap<>();

    //存储类
    private PurchaseReturnOffScanModel mModel;

    //散件类
    private  MaterialResponseModel   materialModle;

    //托盘是否可用
    boolean  palletIsTrue=false;

    MenuOutStockModel  menuOutStockModel = new MenuOutStockModel();
    //region 初始化
    @Override
    protected void initViews() {
        super.initViews();
        //不同类型的标题
        //  mBusinessType = getIntent().getStringExtra("BusinessType").toString();
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        String arr=data.toString();
        menuOutStockModel = GsonUtil.parseJsonToModel(arr,MenuOutStockModel.class);
        int type=Integer.parseInt(menuOutStockModel.VoucherType);
        info.InitUrl(type);
        BaseApplication.context=context;
        BaseApplication.toolBarTitle = new ToolBarTitle(menuOutStockModel.Title+"-"+BaseApplication.mCurrentWareHouseInfo.Warehouseno, true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
        //注册单选按钮事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //托盘框获取焦点
                switch (checkedId) {
                    case R.id.sales_outstock_rediobutton_pallet:
                        CommonUtil.setEditFocus(sales_outstock_pallettext);
                        sales_outstock_boxtext.setVisibility(View.INVISIBLE);
                        sales_outstock_boxtext.setText("");
                        OutStock_Type=OutStock_Submit_type_pallet;
                        materialModle=new  MaterialResponseModel();
                        break;
                    case R.id.sales_outstock_rediobutton_box:
                        sales_outstock_boxtext.setVisibility(View.VISIBLE);
                        OutStock_Type=OutStock_Submit_type_box;
                        sales_outstock_boxtext.setHint("扫描箱号");
                        sales_outstock_boxtext.setText("");
                        materialModle=new  MaterialResponseModel();
                        if(palletIsTrue){
                            CommonUtil.setEditFocus(sales_outstock_boxtext);
                        }
                        break;
                    case R.id.sales_outstock_rediobutton_san:
                        sales_outstock_boxtext.setHint("扫描69码/物料");
                        OutStock_Type=OutStock_Submit_type_parts;
                        sales_outstock_boxtext.setText("");
                        sales_outstock_boxtext.setVisibility(View.VISIBLE);
                        materialModle=new  MaterialResponseModel();
                        if(palletIsTrue){
                            CommonUtil.setEditFocus(sales_outstock_boxtext);
                        }
                        break;
                }
            }
        });
        //默认是托盘提交  隐藏箱号框
        sales_outstock_boxtext.setVisibility(View.INVISIBLE);
        OutStock_Type=OutStock_Submit_type_pallet;
        CurrOrderNO="";
        mModel= new PurchaseReturnOffScanModel(context, mHandler);
        materialModle=new  MaterialResponseModel();
         CurrVoucherType= type;

         mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, StockRollBack.class);
                Bundle bundle = new Bundle();
                intent.putExtra("ErpVoucherNo", CurrOrderNO);
                intent.putExtra("VoucherType", CurrVoucherType);
                intent.putExtra("Title", menuOutStockModel.Title+"删除");
                intent.putExtras(bundle);
                startActivityLeft(intent);
                return false;
            }
        });
    }

    UrlInfo info=new UrlInfo();
    String  url;
    @Override
    protected void initData() {
        super.initData();
        //重写路径

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
        CommonUtil.setEditFocus(sales_outstock_order);
        //据点集合
        StrongholdcodeList = new HashMap<>();
        //存储类
        mModel = new PurchaseReturnOffScanModel(context, mHandler);
        mAdapter = new SalesoutstockAdapter(context, mModel.getOrderDetailList());
        mList.setAdapter(mAdapter);
        //散件类
        materialModle = new MaterialResponseModel();
        CurrOrderNO = "";
        sales_outstock_address.setText("无");
        outstock_sales_shelf.setText("0");
        outstock_sales_boxnum.setText("0");
    }

    //当上一个界面返回后会触发这个方法
    @Override
    protected void onResume() {
        super.onResume();
        if (CurrOrderNO != "") {
            SalesoutstockRequery model = new SalesoutstockRequery();
            model.Erpvoucherno = CurrOrderNO;
            model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
            model.Creater = BaseApplication.mCurrentUserInfo.getUsername();
            String json = GsonUtil.parseModelToJson(model);
            RequestHandler.addRequest(Request.Method.POST, TAG_Saleoutstock_SelectNO, mHandler, RESULT_Saleoutstock_SalesNO,
                    null, info.SalesOutstock_ScanningNo, json, null);
        }
    }

    //#region 事件


    //订单回车事件
    @Event(value = R.id.sales_outstock_order,type = EditText.OnKeyListener.class)
    private  boolean orderKeyDowm(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        //如果是扫描
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == sales_outstock_order.getId()) {
            try {
                String order = sales_outstock_order.getText().toString().trim();
                if (!order.equals("")) {
                    SalesoutstockRequery model = new SalesoutstockRequery();
                    model.Erpvoucherno = order;
                    model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
                    model.Creater=BaseApplication.mCurrentUserInfo.getUsername();
                    String json = GsonUtil.parseModelToJson(model);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SelectNO, "获取单据信息中",
                            context, mHandler, RESULT_Saleoutstock_SalesNO, null, info.SalesOutstock_ScanningNo, json, null);
                    return true;
                }
            }catch (Exception ex) {
                CommonUtil.setEditFocus(sales_outstock_order);
                //据点集合
                StrongholdcodeList=new HashMap<>();
                //存储类
                mModel= new PurchaseReturnOffScanModel(context, mHandler);
                mAdapter = new SalesoutstockAdapter(context, mModel.getOrderDetailList());
                mList.setAdapter(mAdapter);
                //散件类
                materialModle=new MaterialResponseModel();
                CurrOrderNO="";
                sales_outstock_address.setText("无");
                outstock_sales_shelf.setText("0");
                outstock_sales_boxnum.setText("0");
                MessageBox.Show(context, ex.getMessage());
                return true;
            }
        }
        return false;
    }

    @Event(value = R.id.sales_outstock_driver,type = EditText.OnKeyListener.class)
    private  boolean  driverKeyDowm(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        //如果是扫描
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == sales_outstock_driver.getId()) {
            CommonUtil.setEditFocus(sales_outstock_driver);
        }
        return false;
    }

    //托盘回车事件
    @Event(value = R.id.sales_outstock_pallettext,type = EditText.OnKeyListener.class)
    private  boolean palletKeyDowm(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        //如果是扫描
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == sales_outstock_pallettext.getId()) {
            try {
                if (IsSacnningOrder()) {
                    String palletno = sales_outstock_pallettext.getText().toString().trim();
                    if (!palletno.equals("")) {
//                        if (!Analysis(palletno, OutStock_Submit_type_pallet)) {
//                            CommonUtil.setEditFocus(sales_outstock_pallettext);
//                            MessageBox.Show(context, "请输入或扫描正确托盘号");
//                            CommonUtil.setEditFocus(sales_outstock_pallettext);
//                            palletIsTrue=false;
//                            return true;
//                        } else {
                            //先判断托盘是否存在
                            Outbarcode_Requery model = new Outbarcode_Requery();
                            model.Barcode = palletno;
                            model.Vouchertype = CurrVoucherType;
                            model.Towarehouseid = BaseApplication.mCurrentWareHouseInfo.getId();
                            model.Towarehouseno= BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
                            // model.Vouchertype=0;
                            String json = GsonUtil.parseModelToJson(model);
//                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_barcodeisExist, "托盘提交中",
//                                    context, mHandler, RESULT_Saleoutstock_barcodeisExist, null, info.SalesOutstock_JudgeStock, json, null);
                               RequestHandler.addRequest(Request.Method.POST, TAG_Saleoutstock_barcodeisExist, mHandler, RESULT_Saleoutstock_barcodeisExist,
                                null, info.SalesOutstock_JudgeStock, json, null);
                            return true;
                       // }
                    }
                }else
                {
                    return true;
                }
            } catch (Exception ex) {
                CommonUtil.setEditFocus(sales_outstock_pallettext);
                palletIsTrue=false;
                MessageBox.Show(context, ex.getMessage());
                return true;
            }
        }
        return false;
    }

    //混托用到外箱数量用来做记录
    private   Float outwareQty=0f;

    //箱号回车事件
    @Event(value = R.id.sales_outstock_boxtext,type = EditText.OnKeyListener.class)
    private  boolean boxKeyDowm(View v, int keyCode, KeyEvent event) {

        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == sales_outstock_boxtext.getId()) {
            try {
                if (IsSacnningOrder()) {
                    String boxNo = sales_outstock_boxtext.getText().toString().trim();
                    String palletno = Currpalletno;
                    if (!boxNo.equals("")) {
                        if(palletno.equals("")){
                            CommonUtil.setEditFocus(sales_outstock_pallettext);
                            MessageBox.Show(context, "请先输入或扫描托盘号");
                            return true;
                        }
                        if(palletList.size()==0){
                            CommonUtil.setEditFocus(sales_outstock_pallettext);
                            MessageBox.Show(context, "请输入或扫描正确托盘号");
                            return true;
                        }
                        //判断当前模式是箱还是散件
                        if (OutStock_Type.equals(OutStock_Submit_type_box)) {
                            if (!Analysis(boxNo, OutStock_Submit_type_box)) {
                                CommonUtil.setEditFocus(sales_outstock_boxtext);
                                MessageBox.Show(context, "请输入或扫描正确的箱号");
                                return true;
                            } else {
                                //  MessageBox.Show(context, "是箱号");
                                //提交
                                String[] strBox = boxNo.split("%");
                                String Strongholdcode = GetStrongholdcode(strBox[0]);
                                String[] strPallet = palletno.split("%");
                                SalesoutstockRequery model = new SalesoutstockRequery();
                                if(strBox.length>1) {//是否是旧的箱号69码
//                                    if (!strBox[0].equals(strPallet[0])) {
//                                        CommonUtil.setEditFocus(sales_outstock_boxtext);
//                                        MessageBox.Show(context, "扫描的外箱条码" + strBox[0] + "和托盘条码物料不一致");
//                                        return true;
//                                    } else {
                                        if (strBox.length < 3) {
                                            model.ScanQty = Float.parseFloat(strBox[1]);
                                        } else {
                                            model.Batchno = strBox[1];
                                            model.ScanQty = Float.parseFloat(strBox[2]);
                                        }
                                    //}
                                }
                                outwareQty=model.ScanQty;
                                model.Erpvoucherno = CurrOrderNO;
                                model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
                                model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                                model.PalletNo = palletno;
                                model.Strongholdcode = Strongholdcode;
                                model.Vouchertype=CurrVoucherType;
                                model.BarcodeType = 2;
                                model.MaterialNo = boxNo;
                                if(strBox.length==2) {
                                    model.MaterialNo = strBox[0];
                                    model.BarcodeType = 3;
                                }
                                String json = GsonUtil.parseModelToJson(model);
                                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitBox, "箱号提交中",
                                        context, mHandler, RESULT_Saleoutstock_ScannBoxNo, null, info.SalesOutstock_SacnningPallet, json, null);
                                return true;
                            }
                        }
                        if (OutStock_Type.equals(OutStock_Submit_type_parts)) {
                            if (!Analysis(boxNo, OutStock_Submit_type_parts)) {
                                CommonUtil.setEditFocus(sales_outstock_boxtext);
                                MessageBox.Show(context, "请输入或扫描正确69码或者物料号");
                                return true;
                            } else {
                                //检验是否存在
                                String modelJson = parseModelToJson(boxNo);
                                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitParts, "检验是否存在",
                                        context, mHandler, RESULT_Saleoutstock_ScannParts, null, info.SelectMaterial, modelJson, null);
                                return true;
                            }
                        }

                    }
                }else{
                    return true;
                }
            } catch (Exception ex) {
                CommonUtil.setEditFocus(sales_outstock_boxtext);
                MessageBox.Show(context, ex.getMessage());
                return true;
            }
        }
        return false;
    }


//    //散件提交
//    @Event(value =R.id.outstock_sales_buttonp)
//    private void  sales_buttonp_Submit(View view) {
//        inputTitleDialog("输入散件数量");
//    }
    @Event(value =R.id.outstock_sales_buttonyue)
    private void  yuetai_buttonp_Submit(View view) {
        //先检验是否全部下架完成
        boolean isallover = true;
        for (OutStockOrderDetailInfo item : mModel.getOrderDetailList()) {
            if (item.getRemainqty() != 0) {
                isallover = false;
            }
        }
        if (!isallover) {
            new AlertDialog.Builder(this).setTitle("未全部下架完成,确认提交吗")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            inputYUETAIDialog("输入月台");
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //点击取消触发的事件
                        }
                    }).show();
        } else {
            inputYUETAIDialog("输入月台");
        }
    }





//outstock_sales_buttonp
    //为甚么没作用
//    @Event(value =R.id.sales_outstock_radiogroup,type = RadioGroup.OnCheckedChangeListener.class)
//    private void onCheckedChanged(RadioGroup group, int checkedId) {
//        //托盘框获取焦点
//        switch (checkedId) {
//            case R.id.sales_outstock_rediobutton_pallet:
//                sales_outstock_boxtext.setVisibility(View.INVISIBLE);
//                break;
//            case R.id.sales_outstock_rediobutton_box:
//            case R.id.sales_outstock_rediobutton_san:
//                sales_outstock_boxtext.setVisibility(View.VISIBLE);
//                break;
//        }
//    }


    //#endregion


    //region 回调事件
    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Saleoutstock_SalesNO:
                SacnnNo((String) msg.obj);
                break;
            case RESULT_Saleoutstock_ScannPalletNo://托盘提交 更新列表
                SacnnPalletNo((String) msg.obj);
                CommonUtil.setEditFocus(sales_outstock_pallettext);
                break;
            case RESULT_Saleoutstock_ScannBoxNo://箱号提交 更新列表
            case  RESULT_Saleoutstock_ScannParts_Submit:
                SacnnPalletNo((String) msg.obj);
                CommonUtil.setEditFocus(sales_outstock_boxtext);
                break;
            case RESULT_Saleoutstock_ScannParts://检查物料69码是否存在
                ScannParts((String) msg.obj);
                break;
            case  RESULT_Saleoutstock_barcodeisExist://判断托盘
                BarcodeisExist((String) msg.obj);
                break;
            case  RESULT_Saleoutstock_PlatForm://月台
                 ResultPlatForm((String) msg.obj);
                break;
            case    RESULT_Saleoutstock_AreaPlatForm:
                AreaPlat((String) msg.obj);
                break;
//            case RESULT_Saleoutstock_ScannParts_Submit://散件提交
//                SacnnPalletNo((String) msg.obj);
//                CommonUtil.setEditFocus(sales_outstock_boxtext);
//                break;
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



    //region 方法
    //扫描单号获取数据
    public  void SacnnNo(String result) {
        try {
            BaseResultInfo<OutStockOrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderHeaderInfo>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_order);
                //清空所有数据
                //据点集合
                StrongholdcodeList=new HashMap<>();
                //存储类
                mModel= new PurchaseReturnOffScanModel(context, mHandler);
                mAdapter = new SalesoutstockAdapter(context, mModel.getOrderDetailList());
                mList.setAdapter(mAdapter);
                //散件类
                materialModle=new MaterialResponseModel();
                CurrOrderNO="";
                sales_outstock_address.setText("无");
                outstock_sales_shelf.setText("0");
                outstock_sales_boxnum.setText("0");
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            CurrOrderNO = sales_outstock_order.getText().toString().trim();
            sales_outstock_address.setText(returnMsgModel.getData().getAddress());
            Float cartonnum = returnMsgModel.getData().getOrderCartonNum();
            Float OrderScanCartonNum = returnMsgModel.getData().getOrderScanCartonNum();
            outstock_sales_shelf.setText(String.valueOf(OrderScanCartonNum));
            outstock_sales_boxnum.setText(String.valueOf(cartonnum));
            //成功
            List<OutStockOrderDetailInfo> detailInfos = new ArrayList<OutStockOrderDetailInfo>();
            detailInfos = returnMsgModel.getData().getDetail();
            StrongholdcodeList.put("1", returnMsgModel.getData().getStrongholdcode());
            for (OutStockOrderDetailInfo item:returnMsgModel.getData().getDetail()) {
                item.setVouchertype(CurrVoucherType);
            }
            for (OutStockOrderDetailInfo orderDetailInfo:detailInfos) {
                //存储据点
                StrongholdcodeList.put(orderDetailInfo.getMaterialno(), orderDetailInfo.getStrongholdcode());
            }
            if (detailInfos.size() > 0) {
                //绑定
                mModel.setOrderHeaderInfo(returnMsgModel.getData());
                mModel.setOrderDetailList(returnMsgModel.getData().getDetail());
                mAdapter = new SalesoutstockAdapter(context, mModel.getOrderDetailList());
                mList.setAdapter(mAdapter);
            }
            CommonUtil.setEditFocus(sales_outstock_pallettext);
            return;
        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_order);
            MessageBox.Show(context, "数据解析报错");
        }
    }



    //更新列表
    public  void   SacnnPalletNo(String result) {
        try {
            BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
            }.getType());
            if(returnMsgModel.getResult()==returnMsgModel.RESULT_TYPE_ACTION_CONTINUE){
                //多批次的情况 选择批次
                if(returnMsgModel.getData().size()>0){
                    SelectBatchno(returnMsgModel.getData());
                    return;
                }else
                {
                    MessageBox.Show(context, "批次集合数据为空");
                }
            }
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            materialModle=new MaterialResponseModel();
            inputNum=Float.parseFloat("0");
            List<OutStockOrderDetailInfo> list = new ArrayList<OutStockOrderDetailInfo>();
            list = returnMsgModel.getData();
            //成功需要更新listView 怎么更新
            String msg = "";
            if (returnMsgModel.getData().size() > 0) {
                for (OutStockOrderDetailInfo oderdetail : list) {
                    BaseMultiResultInfo<Boolean, Void> checkResult = mModel.UpdateListViewItem(oderdetail);
                    mAdapter.notifyDataSetChanged();
                    if (!checkResult.getHeaderStatus()) {
                        msg = msg + "物料" + oderdetail.getMaterialno() + "批次" + oderdetail.getBatchno();
                    }
                }
            }
            if (!msg.equals("")) {
                MessageBox.Show(context, msg + "更新失败");
            }

        } catch (Exception EX) {
            MessageBox.Show(context, EX.toString());
        }
    }


    //扫描物料或者69码获取对象
    public  void   ScannParts(String  result) {
        try {
            BaseResultInfo<MaterialResponseModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<MaterialResponseModel>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_boxtext);
                if(returnMsgModel.getResultValue()=="") {
                    MessageBox.Show(context, "请输入或扫描正确的物料号或69码");
                    return;
                }
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            boolean ispalletexits=false;
            if(palletList.size()==0){
                CommonUtil.setEditFocus(sales_outstock_pallettext);
                MessageBox.Show(context, "请先输入或扫描有效托盘号");
                return;
            }
            for (Outbarcode_Requery item:palletList) {
                if (item.getMaterialno().equals(returnMsgModel.getData().Materialno)) {
                    ispalletexits=true;
                }
            }
            if(!ispalletexits) {
                CommonUtil.setEditFocus(sales_outstock_boxtext);
                MessageBox.Show(context, "扫描的散件或者物料不在该托盘下");
                return;
            }

            materialModle = returnMsgModel.getData();
            //如果包装量等于1
            Float packqty = Float.parseFloat(materialModle.PackQty);
            if( ArithUtil.sub(packqty,1f)==0) {
                CommonUtil.setEditFocus(sales_outstock_boxtext);
                MessageBox.Show(context, "包装量等于1不允许下架");
                return;
            }
            //输入数量
            inputTitleDialog("请输入散件数量");

        } catch (Exception EX) {
            CommonUtil.setEditFocus(sales_outstock_boxtext);
            MessageBox.Show(context, EX.toString());

        }
    }


    List<Outbarcode_Requery> palletList=new ArrayList<Outbarcode_Requery>();

    //先判断托盘是否存在   再处理逻辑
    public  void   BarcodeisExist(String result){
        try {
            palletList=    new ArrayList<Outbarcode_Requery>();
            BaseResultInfo<List<Outbarcode_Requery>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<Outbarcode_Requery>>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_pallettext);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                palletIsTrue=false;
                return;
            }
            if(returnMsgModel.getData().size()>1) {
                if (OutStock_Type.equals(OutStock_Submit_type_pallet)) {
                    CommonUtil.setEditFocus(sales_outstock_pallettext);
                    MessageBox.Show(context, "该托盘为混托，请选择其它模式下架");
                    palletIsTrue = false;
                    return;
                }
            }
            palletList=returnMsgModel.getData();
            Currpalletno=returnMsgModel.getData().get(0).getBarcode();
            Outbarcode_Requery palletmodel=returnMsgModel.getData().get(0);
            palletIsTrue=true;
            //region   托盘回车
            //判断是否是提交托盘还是箱号
            String palletno =palletmodel.Barcode;
            if (OutStock_Type.equals(OutStock_Submit_type_pallet)) {
                //调用方法
                //  MessageBox.Show(context, "是托盘");
                String[] strPallet = palletno.split("%");
                String  Strongholdcode=GetStrongholdcode(strPallet[0]);
                //拼托不知道据点
//                                if(Strongholdcode.equals("")){
//                                    MessageBox.Show(context,"该托盘的据点不存在");
//                                }else
//                                {
                SalesoutstockRequery model = new SalesoutstockRequery();
                model.Erpvoucherno = CurrOrderNO;
                model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
                model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                model.PalletNo = palletno;
                model.Vouchertype=CurrVoucherType;
                model.Strongholdcode =Strongholdcode;
                model.MaterialNo=strPallet[0];
                model.Batchno = strPallet[1];
                model.BarcodeType = 1;
                model.ScanQty = Float.parseFloat(strPallet[2]);
                String json = GsonUtil.parseModelToJson(model);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitPallet, "托盘提交中",
                        context, mHandler, RESULT_Saleoutstock_ScannPalletNo, null, info.SalesOutstock_SacnningPallet, json, null);
                //}
            } else {
                CommonUtil.setEditFocus(sales_outstock_boxtext);
            }
            //endregion
        } catch (Exception EX) {
            CommonUtil.setEditFocus(sales_outstock_pallettext);
            MessageBox.Show(context, EX.toString());
            palletIsTrue=false;
            return;
        }
        return;
    }




    //月台返回
    public  void    ResultPlatForm(String result) {
        BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
        }.getType());
        if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
            CommonUtil.setEditFocus(sales_outstock_pallettext);
            MessageBox.Show(context, returnMsgModel.getResultValue());
            return;
        }
        CommonUtil.setEditFocus(sales_outstock_pallettext);
        MessageBox.Show(context, "提交成功",2,null);
    }


    public  void AreaPlat(String result) {
        BaseResultInfo<AreaInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<AreaInfo>>() {
        }.getType());
        if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
            CommonUtil.setEditFocus(sales_outstock_pallettext);
            MessageBox.Show(context, returnMsgModel.getResultValue());
            return;
        }
//        CommonUtil.setEditFocus(sales_outstock_pallettext);
//        MessageBox.Show(context, "提交成功", 2, null);
        Platform model = new Platform();
        model.Erpvoucherno = CurrOrderNO;
        model.Platform = returnMsgModel.getData().getAreano();
        String json = GsonUtil.parseModelToJson(model);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_PlatForm, "月台提交",
                context, mHandler, RESULT_Saleoutstock_PlatForm, null, info.SalesOutstock_PlatForm, json, null);

    }




    //endregion


    //根据物料获取据点
    public String GetStrongholdcode(String materialno) {
        String Strongholdcode = "";
        try {
            Strongholdcode = StrongholdcodeList.get(materialno);
            if (Strongholdcode.equals("")) {
                Strongholdcode = StrongholdcodeList.get("1");
            }
        } catch (Exception ex) {
            Strongholdcode = StrongholdcodeList.get("1");
        }
        return Strongholdcode;
    }


    //解析传入的格式是否符号当前扫描的类型
    public boolean Analysis(String str,String type) {
        boolean bool = false;
        String[] strarr = str.split("%");
        if (type.equals(OutStock_Submit_type_pallet)) {
            if (strarr.length == 5) {
                if (strarr[4].equals(OutStock_Submit_type_pallet))
                    bool = true;
            }
        }
        if (type.equals(OutStock_Submit_type_box)) {
            if (strarr.length == 4 || strarr.length == 3 || strarr.length == 1||strarr.length==2) {//包容老条码
                if (strarr.length > 3) {
                    if (strarr[3].equals(OutStock_Submit_type_box))
                        bool = true;
                } else {
                    bool = true;
                }
            }
        }
        if (type.equals(OutStock_Submit_type_parts)) {
            if (strarr.length < 2) {
                bool = true;
            }
        }
        return bool;
    }



    //是否扫描过单号
    public  boolean IsSacnningOrder() {
        if (CurrOrderNO.equals("")) {
            CommonUtil.setEditFocus(sales_outstock_order);
            MessageBox.Show(context, "请先输入或扫描单号");
            return false;
        }
        return true;
    }


   private   Float inputNum;

    //扫描或者输入数量
    private void inputTitleDialog(String name) {
        String boxNo = sales_outstock_boxtext.getText().toString().trim();
        final String palletno =Currpalletno;
         if(!OutStock_Type.equals(OutStock_Submit_type_parts)) {
             CommonUtil.setEditFocus(sales_outstock_pallettext);
             MessageBox.Show(context, "请先选择散件模式");
             return;
         }
         if(!IsSacnningOrder()){
            return;
        }
        if(palletno.equals("")){
            CommonUtil.setEditFocus(sales_outstock_pallettext);
            MessageBox.Show(context, "托盘号不能为空");

            return;
        }
        if( materialModle.getMaterialno().equals("")) {
            CommonUtil.setEditFocus(sales_outstock_boxtext);
            MessageBox.Show(context, "请先输入或扫描散件");
            return;
        }
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
                            inputNum = inputValue;
                            Float packqty = Float.parseFloat(materialModle.PackQty);
                            if(ArithUtil.sub(inputNum,packqty)>=0) {
                                CommonUtil.setEditFocus(sales_outstock_boxtext);
                                MessageBox.Show(context, "输入的数量需要小于包装量:" + packqty );
                                return;
                            }
                            //提交散件
                            SalesoutstockRequery model = new SalesoutstockRequery();
                                model.Erpvoucherno = CurrOrderNO;
                                model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
                                model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                                model.Strongholdcode=GetStrongholdcode(materialModle.getMaterialno());
                                model.Batchno =palletno.split("%")[1];
                                model.PalletNo = palletno;
                                model.Vouchertype=CurrVoucherType;
                                model.MaterialNo = materialModle.getMaterialno();
                                 model.BarcodeType = 3;
                                model.ScanQty =inputValue;
                                String json = GsonUtil.parseModelToJson(model);
                                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitParts_Submit, "散件提交中",
                                        context, mHandler, RESULT_Saleoutstock_ScannParts_Submit, null, info.SalesOutstock_SacnningPallet, json, null);


                        } catch (Exception ex) {
                            CommonUtil.setEditFocus(sales_outstock_boxtext);
                            MessageBox.Show(context, "请输入正确的数量");

                            inputNum=Float.parseFloat("0");
                        }
                    }
                });
        builder.show();
    }

    private   String inputYuetai;

    //扫描或者输入月台
    private void inputYUETAIDialog(String name) {
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
                            MessageBox.Show(context, "请确认,月台不能为空");
                            return;
                        }
                        Platform model = new Platform();
                        model.Erpvoucherno = CurrOrderNO;
                        model.Platform = inputYuetai;
                        String json = GsonUtil.parseModelToJson(model);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_PlatForm, "月台提交",
                                context, mHandler, RESULT_Saleoutstock_PlatForm, null, info.SalesOutstock_PlatForm, json, null);

//                        AreaInfo model=new AreaInfo();
//                        model.setWarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
//                        model.setAreano(inputYuetai);
//                        String json = GsonUtil.parseModelToJson(model);
//                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_AreaPlatForm, "检验月台",
//                                context, mHandler, RESULT_Saleoutstock_AreaPlatForm, null, info.GetT_AreaModel, json, null);

                    }
                });
        builder.show();
        }
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

    //选择多批次
    private void SelectBatchno( List<OutStockOrderDetailInfo> list)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setIcon(R.drawable.ic_launcher);
        final Float  batchQty=outwareQty;
        final   List<OutStockOrderDetailInfo> selectList=list;
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
                    SalesoutstockRequery model = new SalesoutstockRequery();
                    //如果当前是扫描箱号模式 截取字符
                    int submit=0;
                    if(OutStock_Type.equals(OutStock_Submit_type_box)){
                        submit=2;
                        for (OutStockOrderDetailInfo item:selectList){
                            if(cities[which].equals(item.getBatchno())){
                                if(batchQty==null){
                                    model.ScanQty =item.getOutWaterQty();
                                }else{
                                    model.ScanQty=batchQty;
                                }
                            }
                        }
                    }else {
                        submit = 3;
                    }
                    model.Erpvoucherno = CurrOrderNO;
                    model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
                    model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                    model.PalletNo = Currpalletno;
                    model.Vouchertype=CurrVoucherType;
                    model.Strongholdcode=GetStrongholdcode(materialModle.getMaterialno());
                    model.MaterialNo= sales_outstock_boxtext.getText().toString().trim();
                    model.Batchno = cities[which];
                    model.BarcodeType = submit;
                    String json = GsonUtil.parseModelToJson(model);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitParts_Submit, "箱号提交中",
                            context, mHandler, RESULT_Saleoutstock_ScannParts_Submit, null, info.SalesOutstock_SacnningPallet, json, null);
                }catch (Exception ex){
                    CommonUtil.setEditFocus(sales_outstock_boxtext);
                    MessageBox.Show(context,ex.toString());

                }
            }
        });
        builder.show();
    }



}
