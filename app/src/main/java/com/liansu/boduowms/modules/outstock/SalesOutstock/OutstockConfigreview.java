package com.liansu.boduowms.modules.outstock.SalesOutstock;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
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
import com.liansu.boduowms.modules.outstock.Model.AwyBll;
import com.liansu.boduowms.modules.outstock.Model.MaterialResponseModel;
import com.liansu.boduowms.modules.outstock.Model.Outbarcode_Requery;
import com.liansu.boduowms.modules.outstock.Model.SalesoustockReviewRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockreviewAdapter;
import com.liansu.boduowms.modules.outstock.purchaseReturn.offscan.PurchaseReturnOffScanModel;
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
import java.util.List;

import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_box;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_none;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_pallet;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_parts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_ppallet;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_PostReview;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ReviewOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannParts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_SubmitBarcode;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_barcodeisExist;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_PostReview;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_ReviewOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitBarcode;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitParts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitParts_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_barcodeisExist;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

//复核
@ContentView(R.layout.activity_outstock_configreview)
public class OutstockConfigreview extends BaseActivity {
    Context context = OutstockConfigreview.this;
    //总箱数
    @ViewInject(R.id.outstock_sales_boxcount)
    TextView outstock_sales_boxcount;

    //总箱数已扫
    @ViewInject(R.id.outstock_sales_boxscanning)
    TextView outstock_sales_boxscanning;

//    //总件数
//    @ViewInject(R.id.outstock_sales_jiancount)
//    TextView outstock_sales_jiancount;
//
//
//    //总件数已扫
//    @ViewInject(R.id.outstock_sales_jianscanning)
//    TextView outstock_sales_jianscanning;



    //托运单号
    @ViewInject(R.id.sales_outstock_tyorder)
    TextView sales_outstock_tyorder;

    //订单号
    @ViewInject(R.id.sales_outstock_order)
    TextView sales_outstock_order;

    //条码
    @ViewInject(R.id.sales_outstock_config_reviewbarcode)
    EditText sales_outstock_config_reviewbarcode;

    //适配器
    SalesoutstockreviewAdapter mAdapter;
    //列表框
    @ViewInject(R.id.out_stock_sales_configreview_ListView)
    ListView mList;

    //储存类
    private PurchaseReturnOffScanModel mModel;

    private   int CurrvoucherType;


    //散件类
    private MaterialResponseModel materialModle;

    UrlInfo info=new UrlInfo();

    private AwyBll awyBll;


  String  CurrOrderNO;
    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context=context;
        BaseApplication.toolBarTitle = new ToolBarTitle("物流装车扫描", true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
        awyBll = new AwyBll();
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        String arr = data.toString();
        awyBll = GsonUtil.parseJsonToModel(arr, AwyBll.class);
        info.InitUrl(awyBll.Vouchertype);
        CurrvoucherType = awyBll.Vouchertype;//单据类型
        CurrOrderNO = awyBll.LinkVoucherNo;//单号
        sales_outstock_tyorder.setText(awyBll.Erpvoucherno);
        sales_outstock_order.setText(awyBll.LinkVoucherNo);
        mModel = new PurchaseReturnOffScanModel(context, mHandler);
        materialModle = new MaterialResponseModel();
        //直接访问订单信息
        SalesoutstockRequery model = new SalesoutstockRequery();
        model.Erpvoucherno = CurrOrderNO;
        model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
        model.Vouchertype = CurrvoucherType;
        String json = GsonUtil.parseModelToJson(model);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_ReviewOrder, "单号检验中",
                context, mHandler, RESULT_Saleoutstock_ReviewOrder, null, info.SalesOutstock_Review_ScanningNo, json, null);
    }

    @Override
    protected void initData() {
        super.initData();

    }




    //提交过账
    @Event(value =R.id.outstock_sales_configbutton_reviewsubmit)
    private void   Sales_outstock_review_Submit(View view) {
      //  if (IsScanning()) {
            //部分复核
            //  CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
            // MessageBox.Show(context, "订单未全部复核完成");
       //     MessageBox.Show(context, "订单未扫描,不能过账");

      //  } else {
           if(!IsScanningOver()){
               //全部复核
               ISSubmit("订单未全部复核完成，确认提交吗");
           }else
           {
               //全部复核
               ISSubmit("订单全部复核完成，确认提交吗");

           }

      // }
    }


     //条码回车
    @Event(value = R.id.sales_outstock_config_reviewbarcode,type = EditText.OnKeyListener.class)
    private  boolean palletKeyDowm(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        //如果是扫描
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == sales_outstock_config_reviewbarcode.getId()) {
            try {
                if(!IsScanningOver()){
                    if(CurrOrderNO.equals("")) {
                        MessageBox.Show(context, "请先扫描有效单号");
                        return true;
                    }
                    String barcode = sales_outstock_config_reviewbarcode.getText().toString().trim();
                    String type = Analysis(barcode);
                    //无效
                    if (type.equals(OutStock_Submit_type_none)) {
                        CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
                        MessageBox.Show(context, "请扫描正确条码");
                        return true;
                    }
                    if (type.equals(OutStock_Submit_type_pallet)) {
                        //托盘
                        //先判断托盘是否存在
                        Outbarcode_Requery model = new Outbarcode_Requery();
                        model.Barcode = barcode;
                        model.Vouchertype = CurrvoucherType;
                        model.Towarehouseid = BaseApplication.mCurrentWareHouseInfo.getId();
                        model.Towarehouseno= BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
                        // model.Vouchertype=0;
                        String json = GsonUtil.parseModelToJson(model);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_barcodeisExist, "托盘提交中",
                                context, mHandler, RESULT_Saleoutstock_barcodeisExist, null, info.SalesOutstock_JudgeStock, json, null);
                        return true;
                    }
                    if (type.equals(OutStock_Submit_type_box)) {
                        //箱
                        //直接提交
                        //判断是否成功 直接提交
                        String[] strPallet = barcode.split("%");
                        SalesoutstockRequery model = new SalesoutstockRequery();
                        model.Erpvoucherno = CurrOrderNO;
                        model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                        model.Vouchertype = CurrvoucherType;
                        model.MaterialNo = barcode;
                        if( barcode.split("%").length!=2){
                            model.ScanQty = Float.parseFloat(strPallet[2]);
                        }
                        String json = GsonUtil.parseModelToJson(model);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitBarcode, "箱号提交中",
                                context, mHandler, RESULT_Saleoutstock_SubmitBarcode, null, info.SalesOutstock__SubmitBarcode, json, null);
                    }
                    if (type.equals(OutStock_Submit_type_parts)) {
                        //散件
                        //先判断物料
                        String modelJson = parseModelToJson(barcode);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitParts, "检验是否存在",
                                context, mHandler, RESULT_Saleoutstock_ScannParts, null, info.SelectMaterial, modelJson, null);
                        return true;

                    }
                    if (type.equals(OutStock_Submit_type_ppallet)) {
                        CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
                        MessageBox.Show(context, "该托盘是拼托,请确认");
                        return true;
                    }

                }else{
                    MessageBox.Show(context, "已经全部复核完成");
                    CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
                    return true;
                }
            } catch (Exception ex) {
                MessageBox.Show(context, ex.toString());
                CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
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
            case RESULT_Saleoutstock_ReviewOrder:
                SacnnNo((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;

        }
    }

    public  void SacnnNo(String result) {
        try {
            BaseResultInfo<OutStockOrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderHeaderInfo>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                /// 失败之后直接返回
                MessageBox.Show(context, returnMsgModel.getResultValue());
               // closeActivity();
                return;
            }
            //   CurrOrderNO = awyBll.LinkVoucherNo;
            Float cartonnum = returnMsgModel.getData().getOrderCartonNum();//原材料不需要总箱数跟已扫描数
            outstock_sales_boxcount.setText(String.valueOf(cartonnum));
            Float OrderScanCartonNum = returnMsgModel.getData().getOrderScanCartonNum();
            outstock_sales_boxscanning.setText(String.valueOf(OrderScanCartonNum));
            //绑定
            mModel.setOrderHeaderInfo(returnMsgModel.getData());
            mModel.setOrderDetailList(returnMsgModel.getData().getDetail());
            mAdapter = new SalesoutstockreviewAdapter(context, mModel.getOrderDetailList());
            mList.setAdapter(mAdapter);
            CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
            MessageBox.Show(context, "数据解析报错");
        }
    }
    public  void   PostReview(String  result) {
        try {
            BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_OK) {
                //   CommonUtil.setEditFocus(sales_outstock_);
                // MessageBox.Show(context, returnMsgModel.getResultValue());
                //清空
              //  MessageBox.Show(context, returnMsgModel.getResultValue());

//                outstock_sales_jiancount.setText("0");
//                outstock_sales_jianscanning.setText("0");
                //清空
//                outstock_sales_boxcount.setText("0");
//                outstock_sales_boxscanning.setText("0");
//                mModel = new PurchaseReturnOffScanModel(context, mHandler);
//                CurrOrderNO = "";
//                materialModle = new MaterialResponseModel();
//                mAdapter = new SalesoutstockreviewAdapter(context, mModel.getOrderDetailList());
//                mList.setAdapter(mAdapter);
//                mAdapter.notifyDataSetChanged();
                //this.closeActivity();
                //跳到前一界面
                ISReturn();
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
                CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            materialModle = returnMsgModel.getData();
            //库存
           // inputTitleDialog("输入散件数量");
            //直接打印 +1
            SalesoutstockRequery model = new SalesoutstockRequery();
            model.Erpvoucherno = CurrOrderNO;
            model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
            model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
            model.Vouchertype = CurrvoucherType;
            model.MaterialNo = sales_outstock_config_reviewbarcode.getText().toString().trim();
            model.ScanQty = 1f;
            String json = GsonUtil.parseModelToJson(model);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitParts_Submit, "散件提交中",
                    context, mHandler, RESULT_Saleoutstock_SubmitBarcode, null, info.SalesOutstock__SubmitBarcode, json, null);
        } catch (Exception EX) {
            CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
            MessageBox.Show(context, EX.toString());

        }
    }


    //判断托盘是否存在库存
    public  void  PallExits(String result) {
        try {
            BaseResultInfo<Outbarcode_Requery> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<Outbarcode_Requery>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            String palletno = sales_outstock_config_reviewbarcode.getText().toString().trim();
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
            CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
            MessageBox.Show(context, ex.toString());
        }
    }

    //箱号/托盘 提交返回值
    public  void  BarcodeSubmit(String result) {
        try {
            BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
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
            if (!msg.equals("")) {
                CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
                MessageBox.Show(context, msg + "更新失败");

            }
        } catch (Exception EX) {
            CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
            MessageBox.Show(context, EX.toString());
        }
        CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
    }

     //散件输入
    private void inputTitleDialog(String name) {
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
                            Float packqty = Float.parseFloat(materialModle.Packqty);
                            if(ArithUtil.sub(inputValue,packqty)>=0) {
                                CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
                                MessageBox.Show(context, "不能大于" + packqty + "包装量");
                                return;
                            }
                            //提交散件
                            SalesoutstockRequery model = new SalesoutstockRequery();
                            model.Erpvoucherno = CurrOrderNO;
                            model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
                            model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                            model.Vouchertype = CurrvoucherType;
                            model.MaterialNo = sales_outstock_config_reviewbarcode.getText().toString().trim();
                            model.ScanQty = inputValue;
                            String json = GsonUtil.parseModelToJson(model);
                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitParts_Submit, "散件提交中",
                                    context, mHandler, RESULT_Saleoutstock_SubmitBarcode, null, info.SalesOutstock__SubmitBarcode, json, null);
                        } catch (Exception ex) {
                            CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
                            MessageBox.Show(context, "请输入正确的数量");
                        }
                    }
                });
        builder.show();
    }


    //是否返回上一级菜单
    public void ISReturn() {
        new AlertDialog.Builder(this).setTitle("数据保存成功，是否返回上一页")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定触发的事件
                        closeActivity();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击取消触发的事件
                        //再次访问订单信息
                        SalesoutstockRequery model = new SalesoutstockRequery();
                        model.Erpvoucherno = CurrOrderNO;
                        model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
                        model.Vouchertype = CurrvoucherType;
                        String json = GsonUtil.parseModelToJson(model);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_ReviewOrder, "单号检验中",
                                context, mHandler, RESULT_Saleoutstock_ReviewOrder, null, info.SalesOutstock_Review_ScanningNo, json, null);
                    }
                }).show();
    }



    public void ISSubmit(String name) {
        new AlertDialog.Builder(this).setTitle(name)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定触发的事件
                        List<SalesoustockReviewRequery> list=new ArrayList<SalesoustockReviewRequery>();
                        SalesoustockReviewRequery model=new SalesoustockReviewRequery();
                        model.Erpvoucherno=CurrOrderNO;
                        model.Scanuserno=BaseApplication.mCurrentUserInfo.getUserno();
                        model.Vouchertype=CurrvoucherType;
                        model.WayBillNo=sales_outstock_tyorder.getText().toString().trim();
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




    //判断是否全部复核完成
    private boolean IsScanningOver() {
        boolean istrue = true;
        for (OutStockOrderDetailInfo item : mModel.getOrderDetailList()) {
            if (ArithUtil.sub(item.getRemainqty(),item.getScanqty())!=0) {
                istrue = false;
            }
        }
        return istrue;
    }
    //判断是否全部复核完成
    private boolean IsScanning() {
        boolean istrue = true;
        for (OutStockOrderDetailInfo item : mModel.getOrderDetailList()) {
            if (item.getScanqty()>0){
                istrue = false;
            }
        }
        return istrue;
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
        if (strarr.length == 4) {
            if (strarr[3].equals(OutStock_Submit_type_box))//拼箱
                return OutStock_Submit_type_box;
        }
        if(strarr.length==2){
            if (strarr[1].equals(OutStock_Submit_type_ppallet))
                return OutStock_Submit_type_box;
        }
        if (strarr.length == 1) {
            return OutStock_Submit_type_parts;
        }
        return OutStock_Submit_type_none;
    }

}