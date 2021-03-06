package com.liansu.boduowms.modules.outstock.SalesOutstock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.liansu.boduowms.modules.outstock.Model.MenuOutStockModel;
import com.liansu.boduowms.modules.outstock.Model.Outbarcode_Requery;
import com.liansu.boduowms.modules.outstock.Model.OutstockPackDTO;
import com.liansu.boduowms.modules.outstock.Model.SalesoustockReviewRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockreviewAdapter;
import com.liansu.boduowms.modules.outstock.purchaseReturn.offscan.PurchaseReturnOffScanModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
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
import java.util.UUID;

import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_box;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_none;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_pallet;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_parts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_ppallet;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Get_PackCartonCountADFAsynce;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_PostReview;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ReviewOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannParts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_SubmitBarcode;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_barcodeisExist;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_SaveManualPackageNumADFAsync;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Get_PackCartonCountADFAsync;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_PostReview;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_ReviewOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitBarcode;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitParts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitParts_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_SaveManualPackageNumADFAsync;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;
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

    private int CurrvoucherType;


    //散件类
    private MaterialResponseModel materialModle;

    UrlInfo info = new UrlInfo();

    private AwyBll awyBll;

    OutstockPackDTO outstockPackDTO = new OutstockPackDTO();

    String CurrOrderNO = "";
    String                       mUuid   = null;//每次进入界面只存在一个guiid

    boolean Return;
    boolean isPost;//是否点击过过账按钮   该参数重置于返回方法里面，用来判断过账是否连接超时

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle("物流装车扫描", true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
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
        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                MenuOutStockModel model = new MenuOutStockModel();
                model.ErpVoucherNo = CurrOrderNO;
                model.VoucherType = String.valueOf(CurrvoucherType);
                model.Title = "物流装车删除";
                String json = GsonUtil.parseModelToJson(model);
                Uri data = Uri.parse(json);
                intent.setData(data);
                intent.setClass(context, OutstockDeleteReview.class);
                startActivity(intent);
                return false;
            }
        });
        mUuid= UUID.randomUUID().toString();
        Return=true;
        isPost=false;
        //直接访问订单信息
//        SalesoutstockRequery model = new SalesoutstockRequery();
//        model.Erpvoucherno = CurrOrderNO;
//        model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
//        model.Vouchertype = CurrvoucherType;
//        String json = GsonUtil.parseModelToJson(model);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_ReviewOrder, "单号检验中",
//                context, mHandler, RESULT_Saleoutstock_ReviewOrder, null, info.SalesOutstock_Review_ScanningNo, json, null);
    }

    @Override
    protected void initData() {
        super.initData();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_nav_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            if(Return){
                onResume();
            }else{
                MessageBox.Show(context,"过账异常不允许刷新，请先过账当前单号");
            }
        }
        return false;
    }


    //当上一个界面返回后会触发这个方法
    @Override
    protected void onResume() {
        super.onResume();
        BaseApplication.isCloseActivity = false;//关闭界面 再次提示
        if (!CurrOrderNO.equals("") && CurrOrderNO != null) {
            SalesoutstockRequery model = new SalesoutstockRequery();
            model.Erpvoucherno = CurrOrderNO;
            model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
            model.Vouchertype = CurrvoucherType;
            String json = GsonUtil.parseModelToJson(model);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_ReviewOrder, "单号检验中",
                    context, mHandler, RESULT_Saleoutstock_ReviewOrder, null, info.SalesOutstock_Review_ScanningNo, json, null);

        }
    }

    int isReviewOver;//是否全部复核完成

    //提交过账FF
    @Event(value = R.id.outstock_sales_configbutton_reviewsubmit)
    private void Sales_outstock_review_Submit(View view) {
        //  if (IsScanning()) {
        //部分复核
        //  CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
        // MessageBox.Show(context, "订单未全部复核完成");
        //     MessageBox.Show(context, "订单未扫描,不能过账");

        //  } else {

        if (!IsScanningOver()) {
            //全部复核
            isReviewOver = 1;
            ISSubmit("订单未全部复核完成，确认提交吗");
        } else {
            isReviewOver = 2;
            //全部复核
            ISSubmit("订单全部复核完成，确认提交吗");

        }

        // }
    }

    //当前扫描类型
    private String CurrScannType;

    //条码回车
    @Event(value = R.id.sales_outstock_config_reviewbarcode, type = EditText.OnKeyListener.class)
    private boolean palletKeyDowm(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        //如果是扫描
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == sales_outstock_config_reviewbarcode.getId()) {
            try {
                if (!IsScanningOver()) {
                    if (CurrOrderNO.equals("")) {
                        MessageBox.Show(context, "请先扫描有效单号");
                        return true;
                    }
                    String barcode = sales_outstock_config_reviewbarcode.getText().toString().trim();
                    if (barcode.equals("")) {
                        MessageBox.Show(context, "请输入或扫描条码");
                        return true;
                    }
                    String type = Analysis(barcode);
                    CurrScannType = type;
                    //无效
                    if (type.equals(OutStock_Submit_type_none)) {
                        CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
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
//                        model.Towarehouseno= BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
//                        // model.Vouchertype=0;
//                        String json = GsonUtil.parseModelToJson(model);
//                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_barcodeisExist, "托盘提交中",
//                                context, mHandler, RESULT_Saleoutstock_barcodeisExist, null, info.SalesOutstock_JudgeStock, json, null);
                        //直接提交
                        String palletno = sales_outstock_config_reviewbarcode.getText().toString().trim();
                        //判断是否成功 直接提交
                        String[] strPallet = palletno.split("%");
                        SalesoutstockRequery model = new SalesoutstockRequery();
                        model.Erpvoucherno = CurrOrderNO;
                        model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                        model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
                        model.Vouchertype = CurrvoucherType;
                        model.MaterialNo = palletno;
                        model.WayBillNo = sales_outstock_tyorder.getText().toString().trim();
                        model.ScanQty = Float.parseFloat(strPallet[2]);
                        String json = GsonUtil.parseModelToJson(model);
                        LogUtil.WriteLog(OutstockConfigreview.class, "复核装车托盘提交", json);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitBarcode, "托盘提交中",
                                context, mHandler, RESULT_Saleoutstock_SubmitBarcode, null, info.SalesOutstock__SubmitBarcode, json, null);

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
                        model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
                        model.WayBillNo = sales_outstock_tyorder.getText().toString().trim();
                        model.MaterialNo = barcode;
                        if (barcode.split("%").length != 2) {
                            model.ScanQty = Float.parseFloat(strPallet[2]);
                        } else {
                            model.ScanQty = Float.parseFloat(strPallet[1]);
                        }
                        String json = GsonUtil.parseModelToJson(model);
//                        int judge=0;
//                        for (OutStockOrderDetailInfo item: mModel.getOrderDetailList()) {
//                            if (strPallet[0].equals(item.getMaterialno())) {
//                                judge=1;
//                                if(ArithUtil.sub(item.getRemainqty(),ArithUtil.add(item.getScanqty(),model.ScanQty))<0){
//                                    judge=2;
//                                }
//                            }
//                        }
//                        if(judge==2) {
//                            CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
//                            MessageBox.Show(context, "该物料扫描数量超过剩余复核数量");
//                            return true;
//                        }
//                        if(judge==0) {
//                            CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
//                            MessageBox.Show(context, "该物料不存在列表中");
//                            return true;
//                        }
                        LogUtil.WriteLog(OutstockConfigreview.class, "复核装车箱号提交", json);
                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitBarcode, "箱号提交中",
                                context, mHandler, RESULT_Saleoutstock_SubmitBarcode, null, info.SalesOutstock__SubmitBarcode, json, null);
                        // sales_outstock_config_reviewbarcode.setText("");
                        return true;
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

                } else {
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
            case RESULT_Get_PackCartonCountADFAsynce:
                Getpackcartoncount((String) msg.obj);
                break;
            case RESULT_SaveManualPackageNumADFAsync:
                Savepackagenum((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                if(isPost){
                    //isPost=false;
                    Return=false;
                }
                MessageBox.Show(context, "获取请求失败_____" + msg.obj, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;

        }
    }


    //保存件数
    public void Savepackagenum(String result) {
        try {
            BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                /// 失败之后直接返回
                MessageBox.Show(context, returnMsgModel.getResultValue());
                // closeActivity();
                ISReturn("复核完成，件数保存失败 是否返回上一层");
                return;
            } else {
                ISReturn("复核完成，件数保存成功 是否返回上一层");
            }
        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
            MessageBox.Show(context, "数据解析报错");
        }

    }


    //获取件数
    public void Getpackcartoncount(String result) {

        try {
            BaseResultInfo<OutstockPackDTO> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutstockPackDTO>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                /// 失败之后直接返回
                MessageBox.Show(context, returnMsgModel.getResultValue());
                // closeActivity();
                return;
            }
            outstockPackDTO = returnMsgModel.getData();
            MessageBox.Show2(context, "复核成功,总件数为" + outstockPackDTO.CartonNum + "是否需要修改件数?", MessageBox.MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //输入
                    String title = "总件数为" + outstockPackDTO.CartonNum + "请输入实际件数";
                    inputjian1(title);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ISReturn("复核完成，是否返回上一层");
                }
            });

        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
            MessageBox.Show(context, "数据解析报错");
        }
    }


    public void SacnnNo(String result) {
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
            Float cartonnum = returnMsgModel.getData().getCartonNum();//原材料不需要总箱数跟已扫描数
            outstock_sales_boxcount.setText(String.valueOf(cartonnum));
            Float OrderScanCartonNum = returnMsgModel.getData().getPackageNum();
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


    @Override
    public  boolean  ReturnActivity(){
        if(!Return){
            CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
            MessageBox.Show(context, "过账异常不允许退出，请继续提交");
        }
        return Return;
    }

    public void PostReview(String result) {
        try {
            BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
            }.getType());
            isPost=false;//重置，表明提交过账之后有返回
            if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_OK) {
                Return=true;
                //   CommonUtil.setEditFocus(sales_outstock_);
//                MessageBox.Show(context, returnMsgModel.getResultValue());
                //跳到前一界面
                MessageBox.Show(context, returnMsgModel.getResultValue() , MessageBox.MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //请求获取对象
                        Map<String, String> map = new HashMap<>();
                        map.put("ErpVoucherNo", awyBll.LinkVoucherNo);
                        String json = GsonUtil.parseModelToJson(map);
                        LogUtil.WriteLog(OutstockConfigreview.class, "获取件数", json);
                        RequestHandler.addRequestWithDialog(Request.Method.GET, TAG_Get_PackCartonCountADFAsync, "获取件数中",
                                context, mHandler, RESULT_Get_PackCartonCountADFAsynce, null, info.Outstock_Get_PackCartonCountADFAsync, map, null);

                    }
                });
                mUuid=UUID.randomUUID().toString();
                //
            }else{
                CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_ERPPOSTERROR) {
                    Return=false;
                }else{
                    Return=true;
                    mUuid=UUID.randomUUID().toString();
                }
            }
        } catch (Exception EX) {
            MessageBox.Show(context, EX.toString());
            Return=false;
        }

    }


    //扫描物料或者69码获取对象
    public void ScannParts(String result) {
        try {
            BaseResultInfo<MaterialResponseModel> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<MaterialResponseModel>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                if (returnMsgModel.getData() != null) {//旧外箱
                    MaterialResponseModel material = returnMsgModel.getData();
                    int judge = 0;
                    for (OutStockOrderDetailInfo item : mModel.getOrderDetailList()) {
                        if (material.getMaterialno().equals(item.getMaterialno())) {
                            judge = 1;
                            if (ArithUtil.sub(item.getRemainqty(), ArithUtil.add(item.getScanqty(), material.OuterQty)) < 0) {
                                judge = 2;
                            }
                        }
                    }
                    if (judge == 2) {
                        CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
                        MessageBox.Show(context, "该物料包装量超过了当前剩余数量");
                        return;
                    }
                    //箱号
                    //直接调用拼箱方法
                    SalesoutstockRequery model = new SalesoutstockRequery();
                    model.Batchno = "";
                    model.MaterialNo = material.Materialno;
                    model.Erpvoucherno = CurrOrderNO;
                    model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                    model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
                    model.ScanQty = material.OuterQty;
                    model.Vouchertype = CurrvoucherType;
                    model.WayBillNo = sales_outstock_tyorder.getText().toString().trim();
                    String modelJson = parseModelToJson(model);
                    LogUtil.WriteLog(OutstockConfigreview.class, "复核装车箱号提交", modelJson);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitBarcode, "箱号提交中",
                            context, mHandler, RESULT_Saleoutstock_SubmitBarcode, null, info.SalesOutstock__SubmitBarcode, modelJson, null);
                    //   sales_outstock_config_reviewbarcode.setText("");
                    return;
                } else {
                    CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
                    MessageBox.Show(context, returnMsgModel.getResultValue());
                    return;
                }
            }
            materialModle = returnMsgModel.getData();
            int judge = 0;
            Float qty = 0f;
            Boolean isreviewover = true;//该物料是否复核完成
            for (OutStockOrderDetailInfo item : mModel.getOrderDetailList()) {
                if (materialModle.getMaterialno().equals(item.getMaterialno())) {
                    judge = 1;
                    if (ArithUtil.sub(item.getRemainqty(), item.getScanqty()) != 0) {
                        isreviewover = false;
                    }
                    if (ArithUtil.sub(item.getRemainqty(), item.getScanqty()) < 1 && ArithUtil.sub(item.getRemainqty(), item.getScanqty()) > 0) {//未装车数量小于1的情况
                        qty = ArithUtil.sub(item.getRemainqty(), item.getScanqty());//给小数
                    } else {
                        qty = 1f;
                    }
                }
            }
            if (isreviewover) {
                CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
                MessageBox.Show(context, "该物料已经全部复核完成,请确认");
                return;
            }
            if (judge != 1) {
                CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
                MessageBox.Show(context, "物料不存在当前复核数据中");
                return;
            }
            //库存
            // inputTitleDialog("输入散件数量");
            //直接打印 +1
            SalesoutstockRequery model = new SalesoutstockRequery();
            model.Erpvoucherno = CurrOrderNO;
            model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
            model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
            model.Vouchertype = CurrvoucherType;
            model.MaterialNo = materialModle.getMaterialno();
            model.WayBillNo = sales_outstock_tyorder.getText().toString().trim();
            model.ScanQty = qty;
            String json = GsonUtil.parseModelToJson(model);
            LogUtil.WriteLog(OutstockConfigreview.class, "复核装车散件提交", json);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitParts_Submit, "散件提交中",
                    context, mHandler, RESULT_Saleoutstock_SubmitBarcode, null, info.SalesOutstock__SubmitBarcode, json, null);
        } catch (Exception EX) {
            CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
            MessageBox.Show(context, EX.toString());

        }
    }


    //判断托盘是否存在库存
    public void PallExits(String result) {
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
                model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
                model.Vouchertype = CurrvoucherType;
                model.MaterialNo = palletno;
                model.WayBillNo = sales_outstock_tyorder.getText().toString().trim();
                model.ScanQty = Float.parseFloat(strPallet[2]);
                String json = GsonUtil.parseModelToJson(model);
                LogUtil.WriteLog(OutstockConfigreview.class, "复核装车托盘提交", json);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitBarcode, "托盘提交中",
                        context, mHandler, RESULT_Saleoutstock_SubmitBarcode, null, info.SalesOutstock__SubmitBarcode, json, null);
            }
        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
            MessageBox.Show(context, ex.toString());
        }
    }

    //箱号/托盘 提交返回值
    public void BarcodeSubmit(String result) {
        try {
            LogUtil.WriteLog(OutstockConfigreview.class, "复核装车返回数据", result);
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

            if (IsScanningOver()) {
                //全部复核
                isReviewOver = 2;
                ISSubmit("订单已全部复核完成，确认提交吗");
            }
        } catch (Exception EX) {
            CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
            MessageBox.Show(context, EX.toString());
        }
        CommonUtil.setEditFocus(sales_outstock_config_reviewbarcode);
        return;
    }

    //散件输入
    private void inputTitleDialog(String name) {
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
                            Float packqty = Float.parseFloat(materialModle.PackQty);
                            if (ArithUtil.sub(inputValue, packqty) >= 0) {
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
                            model.WayBillNo = sales_outstock_tyorder.getText().toString().trim();
                            model.ScanQty = inputValue;
                            String json = GsonUtil.parseModelToJson(model);
                            LogUtil.WriteLog(OutstockConfigreview.class, "复核装车散件提交", json);
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
    public void ISReturn(String title) {
        new AlertDialog.Builder(this).setTitle(title)
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
//                        SalesoutstockRequery model = new SalesoutstockRequery();
//                        model.Erpvoucherno = CurrOrderNO;
//                        model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
//                        model.Vouchertype = CurrvoucherType;
//                        String json = GsonUtil.parseModelToJson(model);
//                        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_ReviewOrder, "单号检验中",
//                                context, mHandler, RESULT_Saleoutstock_ReviewOrder, null, info.SalesOutstock_Review_ScanningNo, json, null);
                    }
                }).show();
    }


    private void inputjian(String name) {
        final EditText inputServer = new EditText(this);
        inputServer.setRawInputType(InputType.TYPE_CLASS_NUMBER);//设置bai进入du的时zhi候显dao示为zhuannumber模式shu
        inputServer.setFocusable(true);
        inputServer.setSingleLine(true);
        //    numeric="integer"
        // inputServer.android
        inputServer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    String a = inputServer.getText().toString();
                    if (inputServer.getText().toString().equals("0")) {
                        MessageBox.Show(context, "不能为零");
                    }
                    return true;
                }
                return false;
            }
        });

        inputServer.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {//失去焦点 等于零 就获取焦点
                    if (inputServer.getText().toString().equals("0")) {
                        inputServer.setSelectAllOnFocus(true);
                        inputServer.setFocusable(true);
                        inputServer.requestFocus();
                    }
                }
            }
        });

        inputServer.setRawInputType(InputType.TYPE_CLASS_NUMBER);//设置bai进入du的时zhi候显dao示为zhuannumber模式shu
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
                            if (Value.equals("0")) {
                                return;
                            }
                            int inputValue = Integer.parseInt(Value);
                            outstockPackDTO.ManualCartonNum = inputValue;
                            outstockPackDTO.WayBillNo = awyBll.Erpvoucherno;
                            String json = GsonUtil.parseModelToJson(outstockPackDTO);
                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveManualPackageNumADFAsync, "件数提交中",
                                    context, mHandler, RESULT_SaveManualPackageNumADFAsync, null, info.Outstock_SaveManualPackageNumADFAsync, json, null);
                        } catch (Exception ex) {
                            MessageBox.Show(context, "请输入正确的数量");
                        }
                    }
                });
        builder.show();
    }

    //自己控制关闭弹出层
    private void inputjian1(String name) {
        final EditText inputServer = new EditText(this);
        inputServer.setRawInputType(InputType.TYPE_CLASS_NUMBER);//设置b进入的时候显示为number模式
        inputServer.setFocusable(true);
        inputServer.setSingleLine(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(name).setIcon(
                null).setView(inputServer);
        builder.setPositiveButton("确认", null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);//只能点击确认才能关闭
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int inputValue = Integer.parseInt(inputServer.getText().toString());
                    outstockPackDTO.ManualCartonNum = inputValue;
                    outstockPackDTO.WayBillNo = awyBll.Erpvoucherno;
                    String json = GsonUtil.parseModelToJson(outstockPackDTO);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveManualPackageNumADFAsync, "件数提交中",
                            context, mHandler, RESULT_SaveManualPackageNumADFAsync, null, info.Outstock_SaveManualPackageNumADFAsync, json, null);
                    alertDialog.dismiss();//自己控制窗口关闭
                } catch (Exception ex) {
                    Toast.makeText(context, "请输入正确数字", Toast.LENGTH_SHORT).show();
                    inputServer.setSelectAllOnFocus(true);
                    inputServer.setFocusable(true);
                    inputServer.requestFocus();
                }
            }
        });
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
                        model.WayBillNo = sales_outstock_tyorder.getText().toString().trim();
                        model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
                        model.GUID= mUuid;
                        list.add(model);
                        String modelJson = parseModelToJson(list);
                        isPost=true;
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
            if (ArithUtil.sub(item.getVoucherqty(), item.getScanqty()) != 0) {
                istrue = false;
            }
        }
        return istrue;
    }

    //判断是否全部复核完成
    private boolean IsScanning() {
        boolean istrue = true;
        for (OutStockOrderDetailInfo item : mModel.getOrderDetailList()) {
            if (item.getScanqty() > 0) {
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
        if (strarr.length == 4 || strarr.length == 3 || strarr.length == 2) {
            if (strarr.length == 4) {
                if (strarr[3].equals(OutStock_Submit_type_box))
                    return OutStock_Submit_type_box;
            } else {
                return OutStock_Submit_type_box;
            }
        }
        if (strarr.length == 1) {
            return OutStock_Submit_type_parts;
        }
        return OutStock_Submit_type_none;
    }

}