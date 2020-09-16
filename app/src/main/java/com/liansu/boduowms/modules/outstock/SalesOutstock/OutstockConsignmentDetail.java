package com.liansu.boduowms.modules.outstock.SalesOutstock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
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
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.outstock.Model.AwyBll;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockConsignRequery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockConsignmentAdapter;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.GsonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ConfigSaveOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ConfigSaveOrderDetail;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ConfigSelectOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ReviewOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_ConfigSaveOrderDetail;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_ConfigSelectOrder;

//托运单明细
@ContentView(R.layout.activity_outstock_consignment_detail)
public class OutstockConsignmentDetail extends BaseActivity {
    Context context = OutstockConsignmentDetail.this;
    //托运单
    @ViewInject(R.id.outstock_consignment_order)
    TextView outstock_consignment_order;
    //适配器
    SalesoutstockConsignmentAdapter mAdapter;
    @ViewInject(R.id.outstock_consignment_detailListView)
    ListView mList;
    private AwyBll awyBll;
    UrlInfo info=new UrlInfo();
    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context=context;
        BaseApplication.toolBarTitle = new ToolBarTitle("托运单明细", true);
        x.view().inject(this);
        BaseApplication.isCloseActivity=false;
        awyBll = new AwyBll();
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        String arr = data.toString();
        awyBll = GsonUtil.parseJsonToModel(arr, AwyBll.class);
        outstock_consignment_order.setText(awyBll.Erpvoucherno);
        info.InitUrl(awyBll.Vouchertype);
        String json = GsonUtil.parseModelToJson(awyBll);
        //直接加载托运单
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_ConfigSaveOrderDetail, "获取托运单信息",
                context, mHandler, RESULT_Saleoutstock_ConfigSaveOrderDetail, null, info.SalesOutstock__Review_configSelectOrderDetial.trim(), json, null);
    }




    @Override
    protected void initData() {
        super.initData();
    }



    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Saleoutstock_ConfigSaveOrderDetail:
                SacnnNo((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;
        }
    }


    //扫描单号方法
    public  void SacnnNo(String result) {
        try {
            BaseResultInfo<List<SalesoutstockConsignRequery>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<SalesoutstockConsignRequery>>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            //成功
            List<SalesoutstockConsignRequery> detailInfos = new ArrayList<SalesoutstockConsignRequery>();
            detailInfos = returnMsgModel.getData();
            if (detailInfos.size() > 0) {
                //绑定
                mAdapter = new SalesoutstockConsignmentAdapter(context, detailInfos);
                mList.setAdapter(mAdapter);
            }

        } catch (Exception ex) {
            MessageBox.Show(context, ex.toString());

        }
    }

}