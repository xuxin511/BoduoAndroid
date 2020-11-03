package com.liansu.boduowms.modules.outstock.SalesOutstock;


import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.deppon.android.bluetooth.print.util.ClsUtils;
import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.outstock.Model.MaterialResponseModel;
import com.liansu.boduowms.modules.outstock.Model.MenuOutStockModel;
import com.liansu.boduowms.modules.outstock.Model.Outbarcode_Requery;
import com.liansu.boduowms.modules.outstock.Model.SalesoustockReviewRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutStcokboxRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxListRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockreviewAdapter;
import com.liansu.boduowms.modules.outstock.purchaseReturn.offscan.PurchaseReturnOffScanModel;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.modules.stockRollBack.StockRollBack;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.log.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_box;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_none;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_pallet;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_parts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_ppallet;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_Box_Check_Box;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_DelBox;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_PlatForm;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_PostReview;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ReviewOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_SalesNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannBoxNo;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannPalletNo;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannParts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannParts_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_SubmitBarcode;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_barcodeisExist;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_Box_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_DelBox;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_PostReview;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_ReviewOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SelectNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitBarcode;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitPallet;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitParts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitParts_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_barcodeisExist;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

//复核   zl 2020-8-12
@ContentView(R.layout.activity_out_stock_sales_review)
public  class SalesOutReview extends BaseActivity {
    Context context = SalesOutReview.this;

    //region  控件
    //订单框
    @ViewInject(R.id.sales_outstock_revieworder)
    EditText sales_outstock_revieworder;

    //司机框
    @ViewInject(R.id.sales_outstock_driver)
    EditText sales_outstock_driver;

    //条码框
    @ViewInject(R.id.sales_outstock_reviewbarcode)
    EditText sales_outstock_reviewbarcode;


    //地址
    @ViewInject(R.id.sales_outstock_address)
    TextView sales_outstock_address;


    //收货人
    @ViewInject(R.id.out_stock_sales_reviewusername)
    TextView out_stock_sales_reviewusername;

    //整箱数
    @ViewInject(R.id.outstock_sales_boxnum)
    TextView outstock_sales_boxnum;

    //散件数
    @ViewInject(R.id.outstock_sales_reviewsan)
    TextView outstock_sales_reviewsan;

    //适配器
    SalesoutstockreviewAdapter mAdapter;
    //列表框
    @ViewInject(R.id.out_stock_sales_review_ListView)
    ListView mList;

    //散件类
    private MaterialResponseModel materialModle;


    //endregion

    int CurrvoucherType;


    String CurrOrderNO="";


    //据点集合
    Map<String, String> StrongholdcodeList = new HashMap<>();

    //存储类
    private PurchaseReturnOffScanModel mModel;
    UrlInfo info = new UrlInfo();

    MenuOutStockModel menuOutStockModel = new MenuOutStockModel();


    @Override
    protected void initViews() {
        super.initViews();
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        String arr=data.toString();
        menuOutStockModel = GsonUtil.parseJsonToModel(arr,MenuOutStockModel.class);
        int type=Integer.parseInt(menuOutStockModel.VoucherType);
        CurrvoucherType= type;
        info.InitUrl(type);
        BaseApplication.context=context;
        BaseApplication.toolBarTitle = new ToolBarTitle(menuOutStockModel.Title+"-"+BaseApplication.mCurrentWareHouseInfo.Warehouseno, true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
        CurrOrderNO = "";
        mModel = new PurchaseReturnOffScanModel(context, mHandler);
        materialModle = new MaterialResponseModel();
        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                MenuOutStockModel model=new MenuOutStockModel();
                model.ErpVoucherNo=CurrOrderNO;
                model.VoucherType=String.valueOf(CurrvoucherType);
                model.Title=menuOutStockModel.Title+"删除";
                String json=  GsonUtil.parseModelToJson(model);
                Uri data = Uri.parse(json);
                intent.setData(data);
                intent.setClass(context, OutstockDeleteReview.class);
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
    }


//    //当上一个界面返回后会触发这个方法
    @Override
    protected void onResume() {
        super.onResume();
        if (!CurrOrderNO.equals("")&& CurrOrderNO!=null) {
            SalesoutstockRequery model = new SalesoutstockRequery();
            model.Erpvoucherno = CurrOrderNO;
            model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
            String json = GsonUtil.parseModelToJson(model);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_ReviewOrder, "单号检验中",
                    context, mHandler, RESULT_Saleoutstock_ReviewOrder, null, info.SalesOutstock_Review_ScanningNo, json, null);
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
//
//        }
//        return false;
//    }
//
//    @Override
//    public void getToolTitle() {
//        getToolBarHelper().getToolBar().setTitle(menuOutStockModel.Title + "-" + BaseApplication.mCurrentWareHouseInfo.Warehouseno);
//        //清空列表//切换仓库后需要重新扫描
//        del();
//        CommonUtil.setEditFocus(sales_outstock_revieworder);
//    }

    //region 绑定事件

    //订单回车事件
    @Event(value = R.id.sales_outstock_revieworder, type = EditText.OnKeyListener.class)
    private boolean orderKeyDowm(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        //如果是扫描
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == sales_outstock_revieworder.getId()) {
            try {
                String order = sales_outstock_revieworder.getText().toString().trim();
                if (!order.equals("")) {
                    SalesoutstockRequery model = new SalesoutstockRequery();
                    model.Erpvoucherno = order;
                    model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
                    model.Vouchertype= CurrvoucherType;
                    String json = GsonUtil.parseModelToJson(model);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_ReviewOrder, "单号检验中",
                            context, mHandler, RESULT_Saleoutstock_ReviewOrder, null, info.SalesOutstock_Review_ScanningNo, json, null);
                    return true;
                }
            } catch (Exception ex) {
                CommonUtil.setEditFocus(sales_outstock_revieworder);
                MessageBox.Show(context, ex.getMessage());
                del();
                return true;
            }
        }
        return false;
    }


    //条码回车
    @Event(value = R.id.sales_outstock_reviewbarcode, type = EditText.OnKeyListener.class)
    private boolean barocodeKeyDowm(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        //如果是扫描
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == sales_outstock_reviewbarcode.getId()) {
            try {
                if (IsSacnningOrder()) {
                    if (IsScanningOver()) {
                        CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
                        MessageBox.Show(context, "已经全部复核完成");
                        return true;
                    }
                    String barcode = sales_outstock_reviewbarcode.getText().toString().trim();
                    String type = Analysis(barcode);
                    //无效
                    if (type.equals(OutStock_Submit_type_none)) {
                        CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
                        MessageBox.Show(context, "请扫描正确条码");
                        return true;
                    }
                    if (type.equals(OutStock_Submit_type_pallet)) {
                        //托盘
                        //先判断托盘是否存在
//                        Outbarcode_Requery model = new Outbarcode_Requery();
//                        model.Barcode = barcode;
//                        model.Vouchertype = CurrvoucherType;
//                        model.Towarehouseid = BaseApplication.mCurrentWareHouseInfo.getId();
//                        model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
//                        // model.Vouchertype=0;
//                        String json = GsonUtil.parseModelToJson(model);
//                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_barcodeisExist, "托盘提交中",
//                                context, mHandler, RESULT_Saleoutstock_barcodeisExist, null, info.SalesOutstock_JudgeStock, json, null);
                        //直接提交
                        //判断是否成功 直接提交
                        String palletno = sales_outstock_reviewbarcode.getText().toString().trim();
                        String[] strPallet = palletno.split("%");
                        SalesoutstockRequery model = new SalesoutstockRequery();
                        model.Erpvoucherno = CurrOrderNO;
                        model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                        model.Towarehouseno=BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
                        model.Vouchertype = CurrvoucherType;
                        model.MaterialNo = palletno;
                        model.ScanQty = Float.parseFloat(strPallet[2]);
                        String json = GsonUtil.parseModelToJson(model);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitBarcode, "托盘提交中",
                                context, mHandler, RESULT_Saleoutstock_SubmitBarcode, null, info.SalesOutstock__SubmitBarcode, json, null);

                        return true;
                    }
                    if (type.equals(OutStock_Submit_type_box)) {
                        //箱
                        //直接提交
                        //判断是否成功 直接提交
                        String[] strBox = barcode.split("%");
                        SalesoutstockRequery model = new SalesoutstockRequery();
                        model.Erpvoucherno = CurrOrderNO;
                        model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                        model.Towarehouseno=BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
                        model.Vouchertype = CurrvoucherType;
                        model.MaterialNo = barcode;
                        if (barcode.split("%").length >2) {
                            model.ScanQty = Float.parseFloat(strBox[2]);
                        }else{
                            model.ScanQty = Float.parseFloat(strBox[1]);
                        }
                        //仓退跟采购验退的时候输入数量
                        if(CurrvoucherType==27||CurrvoucherType==28){

                         //先找到当前行的数量判断是否大于当前物料
                            String materialno=strBox[0];
                            Float qty=0f;
                            for (OutStockOrderDetailInfo item: mModel.getOrderDetailList()){
                                 if(item.getMaterialno().equals(materialno)){
                                     qty= ArithUtil.sub(item.getRemainqty() ,item.getScanqty());
                                 }
                            }
                            if(qty==0) {
                                CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
                                MessageBox.Show(context, "该物料已经复核完毕或者不存在当前单号中，请确认");
                                return true;
                            }
                            inputBoxnumDialog("当前物料剩余复核数量为"+qty,qty,barcode);
                            return true;
                        }else{
                        String json = GsonUtil.parseModelToJson(model);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitBarcode, "箱号提交中",
                                context, mHandler, RESULT_Saleoutstock_SubmitBarcode, null, info.SalesOutstock__SubmitBarcode, json, null);
                        return true;
                        }
                    }
                    if (type.equals(OutStock_Submit_type_parts)) {
                        //散件
                        //先判断物料
                        String modelJson = parseModelToJson(barcode);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitParts, "检验是否存在",
                                context, mHandler, RESULT_Saleoutstock_ScannParts, null, info.SelectMaterial, modelJson, null);
                        return true;

                        //输入数量直接提交

                    }
                    if (type.equals(OutStock_Submit_type_ppallet)) {
                        CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
                        MessageBox.Show(context, "该托盘是拼托,请确认");
                        return true;
                    }
                }else{return  true;}
            } catch (Exception ex) {
                CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
                MessageBox.Show(context, "字符格式出错");
                return true;
            }
        }
        return false;
    }


    //提交
    @Event(value = R.id.outstock_sales_button_reviewsubmit)
    private void Sales_outstock_review_Submit(View view) {
        if (IsSacnningOrder()) {
            if (!IsScanningOver()) {
                //CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
                //  MessageBox.Show(context, "订单未全部复核完成");
                //部分复核
                ISSubmit("订单未全部复核完成，确认提交吗");
            } else {
                //全部复核
                ISSubmit("订单全部复核完成，确认提交吗");
            }
        }
    }


    //endregion
    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Saleoutstock_ReviewOrder:
                SacnnNo((String) msg.obj);
                break;
            case RESULT_Saleoutstock_barcodeisExist:
                PallExits((String) msg.obj);
                break;
            case RESULT_Saleoutstock_SubmitBarcode://条码提交
                BarcodeSubmit((String) msg.obj);
                break;
            case RESULT_Saleoutstock_ScannParts://检查物料69码是否存在
                ScannParts((String) msg.obj);
                break;
            case RESULT_Saleoutstock_PostReview://过账返回
                PostReview((String) msg.obj);
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

    public void PostReview(String result) {
        try {
            BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_revieworder);
                MessageBox.Show(context, returnMsgModel.getResultValue());
            }
            if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_OK) {
                //   CommonUtil.setEditFocus(sales_outstock_);
                // MessageBox.Show(context, returnMsgModel.getResultValue());
                //清空
                CommonUtil.setEditFocus(sales_outstock_revieworder);
                del();
                MessageBox.Show(context, returnMsgModel.getResultValue(),2,null);
                //this.closeActivity();
            }

        } catch (Exception EX) {
            CommonUtil.setEditFocus(sales_outstock_revieworder);
            MessageBox.Show(context, EX.toString());
        }

    }

    public void del() {
        sales_outstock_driver.setText("");
        sales_outstock_reviewbarcode.setText("");
        sales_outstock_address.setText("无");
        //sales_outstock_revieworder.setText("");
        outstock_sales_boxnum.setText("0");
        outstock_sales_reviewsan.setText("0");
        out_stock_sales_reviewusername.setText("无");
        mModel = new PurchaseReturnOffScanModel(context, mHandler);
        CurrOrderNO = "";
        materialModle = new MaterialResponseModel();
        mAdapter = new SalesoutstockreviewAdapter(context, mModel.getOrderDetailList());
        mList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    //扫描物料或者69码获取对象
    public void ScannParts(String result) {
        try {
            BaseResultInfo<MaterialResponseModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<MaterialResponseModel>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                if (returnMsgModel.getData() != null) {//旧外箱
                    MaterialResponseModel material = returnMsgModel.getData();
                    //箱号
                    //直接调用拼箱方法
                    SalesoutstockRequery model = new SalesoutstockRequery();
                    model.Batchno = "";
                    model.MaterialNo = material.Materialno;
                    model.Erpvoucherno = CurrOrderNO;
                    model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                    model.Towarehouseno=BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
                    model.ScanQty = material.OuterQty;
                    model.Vouchertype = CurrvoucherType;
                    if(CurrvoucherType==27||CurrvoucherType==28){
                        //先找到当前行的数量判断是否大于当前物料
                        String materialno=material.Materialno;
                        Float qty=0f;
                        for (OutStockOrderDetailInfo item: mModel.getOrderDetailList()){
                            if(item.getMaterialno().equals(materialno)){
                                qty= ArithUtil.sub(item.getRemainqty() ,item.getScanqty());
                            }
                        }
                        if(qty==0){
                            CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
                            MessageBox.Show(context, "该物料已经复核完毕或者不存在当前单号中，请确认");
                            return;
                        }
                        inputBoxnumDialog("当前物料剩余复核数量为"+qty,qty,model.MaterialNo );
                    }else{
                    String modelJson = parseModelToJson(model);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitBarcode, "箱号提交中",
                            context, mHandler, RESULT_Saleoutstock_SubmitBarcode, null, info.SalesOutstock__SubmitBarcode, modelJson, null);
                    return;
                    }
                } else {
                    CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
                    MessageBox.Show(context, returnMsgModel.getResultValue());
                    return;
                }
            }
            //直接提交一件
            materialModle = returnMsgModel.getData();
            if(CurrvoucherType==27||CurrvoucherType==28) {
                String materialno=materialModle.Materialno;
                Float qty=0f;
                for (OutStockOrderDetailInfo item: mModel.getOrderDetailList()){
                    if(item.getMaterialno().equals(materialno)){
                        qty= ArithUtil.sub(item.getRemainqty() ,item.getScanqty());
                    }
                }
                if(qty==0){
                    CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
                    MessageBox.Show(context, "该物料已经复核完毕或者不存在当前单号中，请确认");
                    return;
                }
                //库存输入散件
                inputTitleDialog("该物料当前剩余数量为"+qty,qty);
            }else {
                SalesoutstockRequery model = new SalesoutstockRequery();
                model.Erpvoucherno = CurrOrderNO;
                model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
                model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                model.Vouchertype = CurrvoucherType;
                model.MaterialNo = sales_outstock_reviewbarcode.getText().toString().trim();
                model.ScanQty = 1.0f;
                String json = GsonUtil.parseModelToJson(model);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitParts_Submit, "散件提交中",
                        context, mHandler, RESULT_Saleoutstock_SubmitBarcode, null, info.SalesOutstock__SubmitBarcode, json, null);
            }

        } catch (Exception EX) {
            CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
            MessageBox.Show(context, EX.toString());

        }
    }


    //判断托盘是否存在库存
    public void PallExits(String result) {
        try {
            BaseResultInfo<Outbarcode_Requery> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<Outbarcode_Requery>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            String palletno = sales_outstock_reviewbarcode.getText().toString().trim();
            if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_OK) {
                //判断是否成功 直接提交
                String[] strPallet = palletno.split("%");
                SalesoutstockRequery model = new SalesoutstockRequery();
                model.Erpvoucherno = CurrOrderNO;
                model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                model.Vouchertype = CurrvoucherType;
                model.MaterialNo = palletno;
                model.ScanQty = Float.parseFloat(strPallet[2]);
                String json = GsonUtil.parseModelToJson(model);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitBarcode, "托盘提交中",
                        context, mHandler, RESULT_Saleoutstock_SubmitBarcode, null, info.SalesOutstock__SubmitBarcode, json, null);
            }
        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
            MessageBox.Show(context, ex.toString());
        }
    }

    //扫描单号获取数据
    public void SacnnNo(String result) {
        try {
            BaseResultInfo<OutStockOrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderHeaderInfo>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_revieworder);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                del();
                return;
            }
            CurrOrderNO = sales_outstock_revieworder.getText().toString().trim();
            String address=returnMsgModel.getData().getAddress();
            if(returnMsgModel.getData().getAddress()!=null){
                if (address.length() > 15) {
                    address = address.substring(0, 15);
                }

            }
            sales_outstock_address.setText(address);
            out_stock_sales_reviewusername.setText(returnMsgModel.getData().getContacts());
            Float cartonnum = returnMsgModel.getData().getOrderCartonNum();
            outstock_sales_boxnum.setText(String.valueOf(cartonnum));
            Float OrderScanCartonNum = returnMsgModel.getData().getOrderScanCartonNum();
            outstock_sales_reviewsan.setText(String.valueOf(OrderScanCartonNum));
            //绑定
            mModel.setOrderHeaderInfo(returnMsgModel.getData());
            mModel.setOrderDetailList(returnMsgModel.getData().getDetail());
            mAdapter = new SalesoutstockreviewAdapter(context, mModel.getOrderDetailList());
            mList.setAdapter(mAdapter);
            CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_revieworder);
            del();
            MessageBox.Show(context, "数据解析报错");

        }
    }


    //提交返回值
    public void BarcodeSubmit(String result) {
        try {
            //返回
            LogUtil.WriteLog(SalesOutReview.class, "返回", result);
            BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            List<OutStockOrderDetailInfo> list = new ArrayList<OutStockOrderDetailInfo>();
            list = returnMsgModel.getData();
            //成功需要更新listView 怎么更新
            String msg = "";
            if (returnMsgModel.getData().size() > 0) {
                for (OutStockOrderDetailInfo oderdetail : list) {
                    BaseMultiResultInfo<Boolean, Void> checkResult = mModel.UpdateMaterialInfo(oderdetail);
                    mAdapter.notifyDataSetChanged();
                    if (!checkResult.getHeaderStatus()) {
                        msg = msg + "项次" + oderdetail.getRowno() + "项序" + oderdetail.getRownodel();
                    }
                }
            }
            CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
            if (!msg.equals("")) {
                CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
                MessageBox.Show(context, msg + "更新失败");
            }

        } catch (Exception EX) {
            CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
            MessageBox.Show(context, EX.toString());
        }

    }


    //散件输入数量
    private void inputTitleDialog(String name,Float qty) {
        final Float remaqty = qty;
        final EditText inputServer = new EditText(this);
        inputServer.setSingleLine(true);
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
                           // int packqty = Integer.parseInt(materialModle.getPackqty());
//                            if (inputValue >= packqty) {
//                                CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
//                                MessageBox.Show(context, "不能大于" + packqty + "包装量");
//                                return;
//                            }
                            if (inputValue > remaqty) {
                                CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
                                MessageBox.Show(context, "输入数量不能大于" + remaqty + "剩余量");
                                return;
                            }
                            //提交散件
                            SalesoutstockRequery model = new SalesoutstockRequery();
                            model.Erpvoucherno = CurrOrderNO;
                            model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
                            model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                            model.Vouchertype = CurrvoucherType;
                            model.MaterialNo = sales_outstock_reviewbarcode.getText().toString().trim();
                            model.ScanQty = inputValue;
                            String json = GsonUtil.parseModelToJson(model);
                            LogUtil.WriteLog(SalesOutReview.class, "散件提交", json);
                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitParts_Submit, "散件提交中",
                                    context, mHandler, RESULT_Saleoutstock_SubmitBarcode, null, info.SalesOutstock__SubmitBarcode, json, null);
                        } catch (Exception ex) {
                            CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
                            MessageBox.Show(context, "请输入正确的数量");
                        }
                    }
                });
        builder.show();
    }

    //箱号输入数量
    private void  inputBoxnumDialog(String name,Float qty,String material) {
        final   Float Remainqty=qty;
        final  String materialno=material;
        final EditText inputServer = new EditText(this);
        inputServer.setSingleLine(true);
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
                            SalesoutstockRequery model = new SalesoutstockRequery();
                            model.Erpvoucherno = CurrOrderNO;
                            model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                            model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
                            model.Vouchertype = CurrvoucherType;
                            model.MaterialNo = materialno;
                            if(inputValue>Remainqty){
                                CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
                                MessageBox.Show(context, "输入的数量不能大于当前剩余数量");
                                return;
                            }else {
                                model.ScanQty = inputValue;
                                String json = GsonUtil.parseModelToJson(model);
                                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitBarcode, "箱号提交中",
                                        context, mHandler, RESULT_Saleoutstock_SubmitBarcode, null, info.SalesOutstock__SubmitBarcode, json, null);
                            }
                        } catch (Exception ex) {
                            CommonUtil.setEditFocus(sales_outstock_reviewbarcode);
                            MessageBox.Show(context, "请输入正确的数量");
                            return;
                        }
                    }
                });
        builder.show();
    }


    //是否扫描过单号
    public boolean IsSacnningOrder() {
        if (CurrOrderNO.equals("")) {
            CommonUtil.setEditFocus(sales_outstock_revieworder);
            MessageBox.Show(context, "请先扫描单号");
            return false;
        }
        return true;
    }

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
    public String Analysis(String str) {
        String[] strarr = str.split("%");
        if (strarr.length == 5)
            if (strarr.length == 5) {
                if (strarr[4].equals(OutStock_Submit_type_pallet))
                    return OutStock_Submit_type_pallet;
                if (strarr[4].equals(OutStock_Submit_type_ppallet))//拼托
                    return OutStock_Submit_type_ppallet;
            }
        if (strarr.length == 4||strarr.length==3||strarr.length==2) {
            if (strarr.length == 4) {
                if (strarr[3].equals(OutStock_Submit_type_box))
                    return OutStock_Submit_type_box;
            } else {
                return OutStock_Submit_type_box;
            }
        }
        if (strarr.length == 2) {
            if (strarr[1].equals(OutStock_Submit_type_ppallet))
                return OutStock_Submit_type_box;
        }
        if (strarr.length == 1) {
            return OutStock_Submit_type_parts;
        }
        return OutStock_Submit_type_none;
    }


    //判断是否全部复核完成
    private boolean IsScanningOver() {
        boolean istrue = true;
        for (OutStockOrderDetailInfo item : mModel.getOrderDetailList()) {
            if (item.getRemainqty() != item.getScanqty()) {
                istrue = false;
            }
        }
        return istrue;
    }


    public void ISSubmit(String name) {
        new AlertDialog.Builder(this).setTitle(name)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定触发的事件
                        List<SalesoustockReviewRequery> list = new ArrayList<SalesoustockReviewRequery>();
                        SalesoustockReviewRequery model = new SalesoustockReviewRequery();
                        model.Erpvoucherno = CurrOrderNO;
                        model.Scanuserno = BaseApplication.mCurrentUserInfo.getUserno();
                        model.Vouchertype = CurrvoucherType;
                        model.Dirver = sales_outstock_driver.getText().toString().trim();
                        model.Printername= UrlInfo.mOutStockPrintName;
                        model.Printertype= UrlInfo.mOutStockPrintType;
                        model.Towarehouseno=BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
                        list.add(model);
                        String modelJson = parseModelToJson(list);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_PostReview, "复核过账提交中",
                                context, mHandler, RESULT_Saleoutstock_PostReview, null, info.SalesOutstock__Review_Submit, modelJson, null);
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
