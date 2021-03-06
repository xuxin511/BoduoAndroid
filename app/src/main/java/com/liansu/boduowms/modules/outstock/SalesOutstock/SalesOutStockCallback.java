package com.liansu.boduowms.modules.outstock.SalesOutstock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.outstock.Model.MenuOutStockModel;
import com.liansu.boduowms.modules.outstock.Model.SaleoutstockcallbackAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockRequery;
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
import java.util.UUID;

import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESUL_Toutstock_Callback;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESUL_Toutstock_Callback_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_outstock_Callback;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_outstock_Callback_Submit;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;


//二阶段回调
@ContentView(R.layout.activity_outstock_callback)
public class SalesOutStockCallback extends BaseActivity {
    Context context = SalesOutStockCallback.this;
    //outstock_callback_submit

    //提交
    @ViewInject(R.id.outstock_callback_submit)
    Button outstock_callback_submit;

    //订单框
    @ViewInject(R.id.sales_outstock_callback_order)
    EditText sales_outstock_callback_order;

    //列表
    @ViewInject(R.id.sales_outstock_callback_list)
    ListView mList;
    SaleoutstockcallbackAdapter mAdapter;
    String CurrErpvoucherno;
    int CurrVoucherType;
    String  Strongholdcode="";

    UrlInfo info = new UrlInfo();
    MenuOutStockModel model = new MenuOutStockModel();
    String                       mUuid   = null;//每次进入界面只存在一个guiid
    boolean    Return;
    boolean isPost;
    @Override
    protected void initViews() {
        super.initViews();
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        String arr = data.toString();
        model = GsonUtil.parseJsonToModel(arr, MenuOutStockModel.class);
        int type = Integer.parseInt(model.VoucherType);
        info.InitUrl(type);
        CurrVoucherType = type;
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(model.Title+"-"+BaseApplication.mCurrentWareHouseInfo.getWarehouseno(), true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        CurrErpvoucherno = "";
        mUuid= UUID.randomUUID().toString();
        Return=true;
        isPost=false;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public  boolean  ReturnActivity(){
        if(!Return){
            CommonUtil.setEditFocus(sales_outstock_callback_order);
            MessageBox.Show(context, "过账异常不允许退出，请继续提交");
        }
        return Return;
    }
    //回车
    @Event(value = R.id.sales_outstock_callback_order,type = EditText.OnKeyListener.class)
    private  boolean callback_orderclick(View v, int keyCode, KeyEvent event) {
        View vFocus = v.findFocus();
        int etid = vFocus.getId();
        //如果是扫描
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && etid == sales_outstock_callback_order.getId()) {
            try {
                if(!Return){
                    MessageBox.Show(context,"过账异常不允许扫描，请先过账当前单号");
                    return true;
                }
                String erpvoucherno=sales_outstock_callback_order.getText().toString().trim();
                if(erpvoucherno.equals("")){
                    CommonUtil.setEditFocus(sales_outstock_callback_order);
                    MessageBox.Show(context, "请先扫描单号");
                    return true;
                }
                SalesoutstockRequery salesoutstockRequery = new SalesoutstockRequery();
                salesoutstockRequery.Erpvoucherno = erpvoucherno;
                salesoutstockRequery.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
                salesoutstockRequery.Creater = BaseApplication.mCurrentUserInfo.getUsername();
                salesoutstockRequery.setStrongholdcode(BaseApplication.mCurrentWareHouseInfo.getStrongholdcode());
                salesoutstockRequery.Vouchertype=CurrVoucherType;
                String json = GsonUtil.parseModelToJson(salesoutstockRequery);
                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_outstock_Callback, "获取单据信息中",
                        context, mHandler, RESUL_Toutstock_Callback, null, info.SalesOutstock__Toutstock_Callback, json, null);
            } catch (Exception ex) {
                CommonUtil.setEditFocus(sales_outstock_callback_order);
                MessageBox.Show(context, ex.toString());
                return  true
                        ;
            }
        }
        return  false;
    }



    //提交
    @Event(value =R.id.outstock_callback_submit)
    private void  Click_Submit(View view) {
        try {
            if (CurrErpvoucherno.equals("")) {
                CommonUtil.setEditFocus(sales_outstock_callback_order);
                MessageBox.Show(context, "请先扫描单号");
                return;
            }
            List<SalesoutstockRequery> Llist = new ArrayList<SalesoutstockRequery>();
            SalesoutstockRequery salesoutstockRequery = new SalesoutstockRequery();
            salesoutstockRequery.Erpvoucherno = CurrErpvoucherno;
            salesoutstockRequery.Scanuserno = BaseApplication.mCurrentUserInfo.getUserno();
            salesoutstockRequery.Vouchertype = CurrVoucherType;
            salesoutstockRequery.Strongholdcode = Strongholdcode;
            salesoutstockRequery.GUID=mUuid;
            isPost=true;
            Llist.add(salesoutstockRequery);
            String json = GsonUtil.parseModelToJson(Llist);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_outstock_Callback_Submit, "提交单据数据",
                    context, mHandler, RESUL_Toutstock_Callback_Submit, null, info.SalesOutstock__Toutstock_Callback_Submit, json, null);

        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_callback_order);
            MessageBox.Show(context, ex.toString());
        }
    }


    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESUL_Toutstock_Callback://订单获取
                ScannOrder((String) msg.obj);
                break;
            case RESUL_Toutstock_Callback_Submit://订单提交
                SubmitOrder((String) msg.obj);
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



    //扫描订单号
    public  void  ScannOrder(String result) {
        try {
            mUuid= UUID.randomUUID().toString();
            BaseResultInfo<OutStockOrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderHeaderInfo>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_callback_order);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            CurrErpvoucherno = sales_outstock_callback_order.getText().toString().trim();
            //成功
            List<OutStockOrderDetailInfo> detailInfos = new ArrayList<OutStockOrderDetailInfo>();
            Strongholdcode=returnMsgModel.getData().getStrongholdcode();
            detailInfos = returnMsgModel.getData().getDetail();
            //存储单号对象
            for (OutStockOrderDetailInfo item : returnMsgModel.getData().getDetail()) {
                item.setVouchertype(CurrVoucherType);
            }
            if (detailInfos.size() > 0) {
                //绑定
                mAdapter = new SaleoutstockcallbackAdapter(context, detailInfos);
                mList.setAdapter(mAdapter);
            }
            CommonUtil.setEditFocus(sales_outstock_callback_order);
        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_callback_order);
            MessageBox.Show(context, "数据解析报错");
        }
    }

    public  void SubmitOrder(String result) {
        try {
            isPost=false;
            BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
            }.getType());
            if (returnMsgModel.getResult() == returnMsgModel.RESULT_TYPE_OK) {
                mUuid=UUID.randomUUID().toString();
                Return=true;
                CurrErpvoucherno = "";
                //成功
                List<OutStockOrderDetailInfo> detailInfos = new ArrayList<OutStockOrderDetailInfo>();
                Strongholdcode = "";
                //绑定
                mAdapter = new SaleoutstockcallbackAdapter(context, detailInfos);
                mList.setAdapter(mAdapter);
                CommonUtil.setEditFocus(sales_outstock_callback_order);
                MessageBox.Show(context, returnMsgModel.getResultValue(),2,null);
            }else{
                CommonUtil.setEditFocus(sales_outstock_callback_order);
                if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_ERPPOSTERROR) {
                    Return=false;
                }else{
                    mUuid=UUID.randomUUID().toString();
                    Return=true;
                }
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }

        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_callback_order);
            MessageBox.Show(context, "数据解析报错");
            Return=false;
        }

    }





}