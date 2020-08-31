package com.liansu.boduowms.modules.outstock.SalesOutstock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PostProcessor;
import android.net.Uri;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
import com.liansu.boduowms.modules.outstock.Model.MaterialResponseModel;
import com.liansu.boduowms.modules.outstock.Model.MenuOutStockModel;
import com.liansu.boduowms.modules.outstock.Model.Outbarcode_Requery;
import com.liansu.boduowms.modules.outstock.Model.SalesoustockReviewRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutStcokboxRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_box;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_pallet;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_parts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_PostReview;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_SalesNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannPalletNo;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_barcodeisExist;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_PostReview;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SelectNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitPallet;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_barcodeisExist;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

@ContentView(R.layout.activity_outstock_rawmaterial)
public class OutstockRawmaterialActivity extends BaseActivity {
    Context context = OutstockRawmaterialActivity.this;
    //region  控件
    //司机

    //订单框
    @ViewInject(R.id.sales_outstock_rawmaterial_order)
    EditText sales_outstock_rawmaterial_order;

    //托盘框
    @ViewInject(R.id.sales_outstock_material_pallettext)
    EditText sales_outstock_material_pallettext;

    //列表框
    @ViewInject(R.id.outstock_rawmaterial_ListView)
    ListView mList;


    //过账按钮
    @ViewInject(R.id.outstock_rawmaterial_post)
    Button mButton;

    //listview    适配器
    SalesoutstockAdapter mAdapter;

    //endregion





    //当前单号
    private  String CurrOrderNO;

    //当前类型
    private int CurrVoucherType;


    //存储物料跟批次对象
    Map<String,OutStockOrderDetailInfo>  stockorderdetail=new HashMap<String,OutStockOrderDetailInfo>();

    //存储类
    private PurchaseReturnOffScanModel mModel;

    //散件类
    private MaterialResponseModel materialModle;

    UrlInfo info=new UrlInfo();
    //region 初始化
    @Override
    protected void initViews() {
        super.initViews();
        //不同类型的标题
        //  mBusinessType = getIntent().getStringExtra("BusinessType").toString();
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        MenuOutStockModel model = new MenuOutStockModel();
        String arr=data.toString();
        model = GsonUtil.parseJsonToModel(arr,MenuOutStockModel.class);
        int type=Integer.parseInt(model.VoucherType);
        info.InitUrl(type);
        BaseApplication.toolBarTitle = new ToolBarTitle(model.Title, true);
        x.view().inject(this);
        //默认是托盘提交  隐藏箱号框
        CurrOrderNO="";
        mModel= new PurchaseReturnOffScanModel(this,mHandler);
        CurrVoucherType= type; //
        if(CurrVoucherType==25 || CurrVoucherType==28) {
            mButton.setVisibility(View.INVISIBLE);
        }
        if(type==28)//验退需要立即查询单号
        {
            sales_outstock_rawmaterial_order.setText(model.ErpVoucherNo);
          //  CommonUtil.setEditFocus(sales_outstock_rawmaterial_order);
            SalesoutstockRequery salesoutstockRequery = new SalesoutstockRequery();
            salesoutstockRequery.Erpvoucherno = model.ErpVoucherNo;
            salesoutstockRequery.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
            salesoutstockRequery.Creater = BaseApplication.mCurrentUserInfo.getUsername();
            String json = GsonUtil.parseModelToJson(salesoutstockRequery);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SelectNO, "获取单据信息中",
                    context, mHandler, RESULT_Saleoutstock_SalesNO, null, info.SalesOutstock_ScanningNo, json, null);

        }
//        if(CurrVoucherType==46) {//领料 发料 派车单 自动过账  (开始隐藏按钮 失败后显示按钮)
//            //
//            mButton.setVisibility(View.INVISIBLE);
//        }
    }

    @Override
    protected void initData() {
        super.initData();

        //隐藏过账按钮  失败了放出来

    }

    //#region 事件

    //订单回车事件
    @Event(value = R.id.sales_outstock_rawmaterial_order,type = EditText.OnKeyListener.class)
    private  boolean orderKeyDowm(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        //如果是扫描
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == sales_outstock_rawmaterial_order.getId()) {
            try {
                String order = sales_outstock_rawmaterial_order.getText().toString().trim();
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
            } catch (Exception ex) {
                CommonUtil.setEditFocus(sales_outstock_rawmaterial_order);
                MessageBox.Show(context, ex.getMessage());
                return true;
            }
        }
        return false;
    }


    //托盘回车事件
    @Event(value = R.id.sales_outstock_material_pallettext,type = EditText.OnKeyListener.class)
    private  boolean palletKeyDowm(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        //如果是扫描
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == sales_outstock_material_pallettext.getId()) {
            try {
                if (IsSacnningOrder()) {
                    String palletno = sales_outstock_material_pallettext.getText().toString().trim();
                    if (!palletno.equals("")) {
                        if (!Analysis(palletno, OutStock_Submit_type_pallet)) {
                            CommonUtil.setEditFocus(sales_outstock_material_pallettext);
                            MessageBox.Show(context, "请输入或扫描正确托盘号");
                           // CommonUtil.setEditFocus(sales_outstock_material_pallettext);
                            return true;
                        } else {
                          //先判断托盘是否存在
                           //下架下的是原材料 所以可以先check所有的托盘物料是否已经超发过一次，或者

                            Outbarcode_Requery model = new Outbarcode_Requery();
                            model.Barcode = palletno;
                            model.Vouchertype = CurrVoucherType;
                            model.Towarehouseid = BaseApplication.mCurrentWareHouseInfo.getId();
                            model.Towarehouseno= BaseApplication.mCurrentWareHouseInfo.getWarehouseno();
                            // model.Vouchertype=0;
                            String json = GsonUtil.parseModelToJson(model);
                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_barcodeisExist, "托盘提交中",
                                    context, mHandler, RESULT_Saleoutstock_barcodeisExist, null, info.SalesOutstock_JudgeStock, json, null);
                            return true;
                        }
                    }
                }else{
                    return true;
                }
            } catch (Exception ex) {
                CommonUtil.setEditFocus(sales_outstock_material_pallettext);
                MessageBox.Show(context, ex.getMessage());
                return true;
            }
        }
        return false;
    }



    //过账点击
    @Event(value =R.id.outstock_rawmaterial_post)
    private void  Click_post(View view) {
        if (IsSacnningOrder()) {
            //过账接口
//            if (CurrVoucherType == 46) {//领料发料
//                //有这个按钮说明就是已经提交过一次失败了
//                //直接访问后台接口
//                List<SalesoustockReviewRequery> list = new ArrayList<SalesoustockReviewRequery>();
//                SalesoustockReviewRequery model = new SalesoustockReviewRequery();
//                model.Erpvoucherno = CurrOrderNO;
//                model.Scanuserno = BaseApplication.mCurrentUserInfo.getUserno();
//                model.Vouchertype = CurrVoucherType;
//                list.add(model);
//                String modelJson = parseModelToJson(list);
//                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_PostReview, "过账提交中",
//                        context, mHandler, RESULT_Saleoutstock_PostReview, null, info.SalesOutstock__Review_Submit, modelJson, null);
//            } else {//杂出
                if (IsScanningOver()) {
                    List<SalesoustockReviewRequery> list = new ArrayList<SalesoustockReviewRequery>();
                    SalesoustockReviewRequery model = new SalesoustockReviewRequery();
                    model.Erpvoucherno = CurrOrderNO;
                    model.Scanuserno = BaseApplication.mCurrentUserInfo.getUserno();
                    model.Vouchertype = CurrVoucherType;
                    list.add(model);
                    String modelJson = parseModelToJson(list);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_PostReview, "过账提交中",
                            context, mHandler, RESULT_Saleoutstock_PostReview, null, info.SalesOutstock__Review_Submit, modelJson, null);
                } else {
                    CommonUtil.setEditFocus(sales_outstock_material_pallettext);
                    MessageBox.Show(context, "需要全部下架完成才能复核提交");
                }
          //  }
        }
    }




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
                CommonUtil.setEditFocus(sales_outstock_material_pallettext);
                break;
            case  RESULT_Saleoutstock_barcodeisExist://判断托盘
                BarcodeisExist((String) msg.obj);
                break;
            case RESULT_Saleoutstock_PostReview://过账
                Post((String)msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
                break;
        }
    }
    //endregion


    //region 方法
    public  void Post(String result) {
        try {
            BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_material_pallettext);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_OK) {
                mModel.getOrderDetailList().clear();
                mAdapter.notifyDataSetChanged();
                sales_outstock_material_pallettext.setText("");
                CommonUtil.setEditFocus(sales_outstock_rawmaterial_order);
                MessageBox.Show(context, "单号" + CurrOrderNO + "过账成功");
                CurrOrderNO = "";
                //this.closeActivity();
                //跳到前一界面
                //ISReturn();
            }

        } catch (Exception EX) {
            MessageBox.Show(context, EX.toString());
        }
    }


    //扫描单号获取数据
    public  void SacnnNo(String result) {
        try {
            stockorderdetail=new HashMap<String,OutStockOrderDetailInfo>();
            BaseResultInfo<OutStockOrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderHeaderInfo>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_rawmaterial_order);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            CurrOrderNO = sales_outstock_rawmaterial_order.getText().toString().trim();
            //成功
            List<OutStockOrderDetailInfo> detailInfos = new ArrayList<OutStockOrderDetailInfo>();
            detailInfos = returnMsgModel.getData().getDetail();
             //存储单号对象
              for (OutStockOrderDetailInfo item:returnMsgModel.getData().getDetail()) {
                item.setVouchertype(CurrVoucherType);
              }
            if (detailInfos.size() > 0) {
                //绑定
                mModel.setOrderHeaderInfo(returnMsgModel.getData());
                mModel.setOrderDetailList(returnMsgModel.getData().getDetail());
                mAdapter = new SalesoutstockAdapter(context, mModel.getOrderDetailList());
                mList.setAdapter(mAdapter);
            }
            CommonUtil.setEditFocus(sales_outstock_material_pallettext);
        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_rawmaterial_order);
            MessageBox.Show(context, "数据解析报错");

        }
    }



    //更新列表
    public  void   SacnnPalletNo(String result) {
        try {
            BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
            }.getType());
            if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_POST_SUCCESS) {
                //过账完成 更新listview
                for (OutStockOrderDetailInfo item : mModel.getOrderDetailList()) {
                    item.setScanqty(ArithUtil.add(item.getScanqty(), item.getRemainqty()));
                    item.setRemainqty(0f);
                }
//                mModel.getOrderDetailList().clear();
//                mAdapter = new SalesoutstockAdapter(context, mModel.getOrderDetailList());
                  mModel.getOrderDetailList().clear();
                 mAdapter.notifyDataSetChanged();
                 sales_outstock_material_pallettext.setText("");
                 CommonUtil.setEditFocus(sales_outstock_rawmaterial_order);
                 MessageBox.Show(context, "单号"+CurrOrderNO+"过账成功");
                 CurrOrderNO = "";
                return;
            }if (returnMsgModel.getResult() ==returnMsgModel.RESULT_TYPE_POST_ERROR) {
                //过账失败 显示过账按钮
                mButton.setVisibility(View.VISIBLE);
            }
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_material_pallettext);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            } else {
                //实时更新
                List<OutStockOrderDetailInfo> list = new ArrayList<OutStockOrderDetailInfo>();
                list = returnMsgModel.getData();
                //成功需要更新listView 怎么更新
                String msg = "";
                if (returnMsgModel.getData().size() > 0) {
                    for (OutStockOrderDetailInfo oderdetail : list) {
                        //可以超发，判断逻辑限定在最开始输入的时候
                        BaseMultiResultInfo<Boolean, Void> checkResult = mModel.UpdateListViewItemcf(oderdetail);
                        mAdapter.notifyDataSetChanged();
                        if (!checkResult.getHeaderStatus()) {
                            msg = msg + "物料" + oderdetail.getMaterialno() + "批次" + oderdetail.getBatchno();
                        }
                    }
                }
                if (!msg.equals("")) {
                    CommonUtil.setEditFocus(sales_outstock_material_pallettext);
                    MessageBox.Show(context, msg + "更新失败");
                }
            }
        } catch (Exception EX) {
            CommonUtil.setEditFocus(sales_outstock_material_pallettext);
            MessageBox.Show(context, EX.toString());
        }
    }





    //先判断托盘是否存在   再处理逻辑
    public  void   BarcodeisExist(String result) {
        try {
            BaseResultInfo<Outbarcode_Requery> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<Outbarcode_Requery>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_material_pallettext);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            //check 订单数量
            //region   托盘回车
            //找到单据数量 传过去
            String palletno = sales_outstock_material_pallettext.getText().toString().trim();
            String[] palletarr = palletno.split("%");
            //找到该托盘物料
            OutStockOrderDetailInfo model = new OutStockOrderDetailInfo();
            for (OutStockOrderDetailInfo item : mModel.getOrderDetailList()) {
                if (item.getMaterialno().equals(palletarr[0])) {
                    model = item;
                }
            }
            //原材料可以混托下架吗
            // OutStockOrderDetailInfo model = stockorderdetail.get(palletarr[0] + palletarr[1]);
            if (model != null) {
                Float arr = ArithUtil.sub(model.getVoucherqty(), model.getScanqty());
                //  ArithUtil.mul()
                if (arr < 0) {
                    CommonUtil.setEditFocus(sales_outstock_material_pallettext);
                    MessageBox.Show(context, "当前物料已经超发" + arr + ",请确认");
                    return;
                } else {
                    if(arr==0) {
                        CommonUtil.setEditFocus(sales_outstock_material_pallettext);
                        MessageBox.Show(context, palletarr[0]+"该物料已经扫描完成,请确认");
                        return;
                    }
                    //判断数量是否大于当前行
//                    if(model.getRemainqty()>palletarr[0]){
//                    }

                    inputTitleDialog("该物料行剩余数量为:" + model.getRemainqty());
                }
            } else {
                MessageBox.Show(context, "当前订单不存在该托盘物料，请确认");
                //  inputTitleDialog("托盘数量");
            }
            //endregion
        } catch (Exception EX) {
            CommonUtil.setEditFocus(sales_outstock_material_pallettext);
            MessageBox.Show(context, EX.toString());
            return;
        }
    }









    //endregion

    //判断是否全部复核完成
    private boolean IsScanningOver() {
        boolean istrue = true;
        for (OutStockOrderDetailInfo item : mModel.getOrderDetailList()) {
            if (item.getRemainqty()!= 0) {
                istrue = false;
            }
        }
        return istrue;
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
        if (type.equals( OutStock_Submit_type_box)) {
            if (strarr.length == 4) {
                if (strarr[3].equals(OutStock_Submit_type_box))
                    bool = true;
            }
        }
        if (type.equals(OutStock_Submit_type_parts)) {
            if(strarr.length<2){
                bool = true;
            }
        }
        return bool;
    }



    //是否扫描过单号
    public  boolean IsSacnningOrder() {
        if (CurrOrderNO.equals("")) {
            CommonUtil.setEditFocus(sales_outstock_rawmaterial_order);
            MessageBox.Show(context, "请先扫描单号");
            return false;
        }
        return true;
    }


    private   Float inputNum;

    //扫描或者输入数量
    private void inputTitleDialog(String name) {

        final String palletno = sales_outstock_material_pallettext.getText().toString().trim();
        if(!IsSacnningOrder()){
            return;
        }
        if(palletno.equals("")){
            CommonUtil.setEditFocus(sales_outstock_material_pallettext);
            MessageBox.Show(context, "托盘号不能为空");

            return;
        }

        final EditText inputServer = new EditText(this);
        inputServer.setHint("请输入托盘数量");
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
                            inputNum = inputValue;
                            String palletno = sales_outstock_material_pallettext.getText().toString().trim();
                            String[] strPallet = palletno.split("%");
                            SalesoutstockRequery model = new SalesoutstockRequery();
                            model.Erpvoucherno = CurrOrderNO;
                            model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
                            model.PostUserNo = BaseApplication.mCurrentUserInfo.getUserno();
                            model.PalletNo = palletno;
                            model.Vouchertype=CurrVoucherType;
                            model.MaterialNo= strPallet[0];
                            model.Batchno = strPallet[1];
                            model.BarcodeType = 3;
                            model.ScanQty = inputNum;
                            String json = GsonUtil.parseModelToJson(model);
                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitPallet, "托盘提交中",
                                    context, mHandler, RESULT_Saleoutstock_ScannPalletNo, null, info.SalesOutstock_SacnningPallet, json, null);
                            //}


                        } catch (Exception ex) {
                            CommonUtil.setEditFocus(sales_outstock_material_pallettext);
                            MessageBox.Show(context, "请输入正确的数量");
                            inputNum=Float.parseFloat("0");
                        }
                    }
                });
        builder.show();
    }








}