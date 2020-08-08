package com.liansu.boduowms.modules.outstock.SalesOutstock;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
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
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockRequery;
import com.liansu.boduowms.ui.adapter.outstock.offscan.BaseOffShelfScanDetailAdapter;
import com.liansu.boduowms.ui.adapter.outstock.offscan.OffShelfScanDetailAdapter;
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
import java.util.List;

import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_box;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_pallet;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.OutStock_Submit_type_parts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_SalesNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannPalletNo;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SelectNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitPallet;

//销售出库   zl  2020-8-6
@ContentView(R.layout.activity_outstock_sales)
public class SalesOutstock  extends BaseActivity  {
    Context context = SalesOutstock.this;

    //region  控件
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
    private  String CurrOrderNO;

    //region 初始化
    @Override
    protected void initViews() {
        super.initViews();
        //不同类型的标题
        //  mBusinessType = getIntent().getStringExtra("BusinessType").toString();
        BaseApplication.toolBarTitle = new ToolBarTitle("销售出库", true);
        x.view().inject(this);
        //注册单选按钮事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //托盘框获取焦点
                CommonUtil.setEditFocus(sales_outstock_pallettext);
                switch (checkedId) {
                    case R.id.sales_outstock_rediobutton_pallet:
                        sales_outstock_boxtext.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.sales_outstock_rediobutton_box:
                        sales_outstock_boxtext.setVisibility(View.VISIBLE);
                        sales_outstock_boxtext.setHint("扫描箱号");
                        break;
                    case R.id.sales_outstock_rediobutton_san:
                        sales_outstock_boxtext.setHint("扫描69码/物料");
                        sales_outstock_boxtext.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        //默认是托盘提交  隐藏箱号框
        sales_outstock_boxtext.setVisibility(View.INVISIBLE);
        OutStock_Type=OutStock_Submit_type_pallet;
        CurrOrderNO="";

    }

    @Override
    protected void initData() {
        super.initData();
       // radioGroup_pallet.setOnClickListener(this);
//        radioGroup.setOnClickListener(onCheckedChanged(radioGroup,radioGroup_pallet.getId()));
//        radioGroup_box.setOnClickListener(this);
//        radioGroup_san.setOnClickListener(this);
//        Intent intentMain = getIntent();
//        Uri data = intentMain.getData();
        //userModel= GsonUtil.parseJsonToModel(data.toString(),UserModel.class);
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
                    String json = GsonUtil.parseModelToJson(model);
                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SelectNO, "扫描中",
                            context, mHandler, RESULT_Saleoutstock_SalesNO, null, UrlInfo.getUrl().SalesOutstock_SacnningNo, json, null);
                    return true;
                }
            } catch (Exception ex) {
                MessageBox.Show(context, ex.getMessage());
            }
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
                        if (!Analysis(palletno, OutStock_Submit_type_pallet)) {
                            MessageBox.Show(context, "请输入或扫描正确托盘号");
                        } else {
                            //判断是否是提交托盘还是箱号
                            if (OutStock_Type == OutStock_Submit_type_pallet) {
                                //调用方法
                              //  MessageBox.Show(context, "是托盘");
                                String[] strPallet=  palletno.split("%");
                                SalesoutstockRequery model = new SalesoutstockRequery();
                                model.Erpvoucherno = CurrOrderNO;
                                model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
                                model.PostUserNo=BaseApplication.mCurrentUserInfo.getUserno();
                                model.PalletNo=palletno;
                                model.MaterialNo= strPallet[0];
                                model.Batchno= strPallet[1];
                                model.BarcodeType= strPallet[3];
                                model.ScanQty= Integer.parseInt(strPallet[2]);
                                String json = GsonUtil.parseModelToJson(model);
                                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_SubmitPallet, "托盘提交中",
                                        context, mHandler, RESULT_Saleoutstock_ScannPalletNo, null, UrlInfo.getUrl().SalesOutstock_SacnningPallet, json, null);
                                return true;
                            } else {
                                CommonUtil.setEditFocus(sales_outstock_boxtext);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                MessageBox.Show(context, ex.getMessage());
            }
        }
        return false;
    }


    //箱号回车事件
    @Event(value = R.id.sales_outstock_boxtext,type = EditText.OnKeyListener.class)
    private  boolean boxKeyDowm(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == sales_outstock_boxtext.getId()) {
            try {
                if (IsSacnningOrder()) {
                    String palletno = sales_outstock_boxtext.getText().toString().trim();
                    if (!palletno.equals("")) {
                        //判断当前模式是箱还是散件
                        if (OutStock_Type == OutStock_Submit_type_box) {
                            if (!Analysis(palletno, OutStock_Submit_type_box)) {
                                MessageBox.Show(context, "请输入或扫描正确的箱号");
                            } else {
                                MessageBox.Show(context, "是箱号");
                                //提交
                            }
                        }
                        if (OutStock_Type == OutStock_Submit_type_parts) {
                            if (!Analysis(palletno, OutStock_Submit_type_box)) {
                                MessageBox.Show(context, "请输入或扫描正确69码或者物料号");
                            } else {
                                //提交
                                MessageBox.Show(context, "是69码或者物料");
                            }

                        }

                    }
                }
            } catch (Exception ex) {
                MessageBox.Show(context, ex.getMessage());
            }
        }
        return false;
    }


    //为甚么没作用
//    @Event(value =R.id.sales_outstock_radiogroup,type = RadioGroup.OnCheckedChangeListener.class)
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        //托盘框获取焦点
//
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
            case RESULT_Saleoutstock_ScannPalletNo:
                SacnnPalletNo((String) msg.obj);
                break;

            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____"+ msg.obj);
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
            if(returnMsgModel.getResult()!=returnMsgModel.RESULT_TYPE_OK) {
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            CurrOrderNO=sales_outstock_order.getText().toString().trim();
            sales_outstock_address.setText(returnMsgModel.getData().getAddress());
            int cartonnum=returnMsgModel.getData().getOrderCartonNum();
            outstock_sales_shelf.setText(String.valueOf(cartonnum));
            outstock_sales_boxnum.setText(String.valueOf(returnMsgModel.getData().getOrderScanCartonNum()));
           //成功
            List<OutStockOrderDetailInfo> detailInfos=  new ArrayList<OutStockOrderDetailInfo>();
            detailInfos=returnMsgModel.getData().getDetail();
            if(detailInfos.size()>0) {
                //绑定
                mAdapter = new SalesoutstockAdapter(context, detailInfos);
                mList.setAdapter(mAdapter);
            }
        } catch (Exception ex) {
            MessageBox.Show(context, "数据解析报错");
        }
    }



    //扫描托盘提交
    public  void   SacnnPalletNo(String result) {
        try {
            BaseResultInfo<List<OutStockOrderDetailInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockOrderDetailInfo>>>() {
            }.getType());
            if(returnMsgModel.getResult()!=returnMsgModel.RESULT_TYPE_OK) {
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            //成功需要更新listView 怎么更新




        } catch (Exception EX) {
            MessageBox.Show(context, EX.toString());

        }
    }

    //endregion



    //解析传入的格式是否符号当前扫描的类型
    public boolean Analysis(String str,String type) {
        boolean bool = false;
        String[] strarr = str.split("%");
        if (type == OutStock_Submit_type_pallet) {
            if (strarr.length == 4) {
                if (strarr[3].equals(OutStock_Submit_type_pallet))
                    bool = true;
            }
        }
        if (type == OutStock_Submit_type_box) {
            if (strarr.length == 4) {
                if (strarr[2].equals(OutStock_Submit_type_box))
                    bool = true;
            }
        }
        if (type == OutStock_Submit_type_parts) {
            if(strarr.length<2){
                bool = true;
            }
        }
        return bool;
    }



    //是否扫描过单号
    public  boolean IsSacnningOrder() {
        if (CurrOrderNO.equals("")) {
            MessageBox.Show(context, "请选扫描单号");
            return false;
        }
        return true;
    }




}
