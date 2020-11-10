package com.liansu.boduowms.modules.outstock.SalesOutstock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.lang.UProperty;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
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
import com.liansu.boduowms.modules.outstock.Model.MenuOutStockModel;
import com.liansu.boduowms.modules.outstock.Model.OutstockOrderSelectAdapter;
import com.liansu.boduowms.modules.outstock.Model.OutstockOrderSelectModel;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockAdapter;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.GsonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_OrderSelect;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_PostReview;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_SalesNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannPalletNo;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_barcodeisExist;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_OrderSelect;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_barcodeisExist;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;

//发货通知单查询
@ContentView(R.layout.activity_outstock_order_select)
public class OutstockOrderSelect extends BaseActivity {
    Context context = OutstockOrderSelect.this;
    //托运单号
    @ViewInject(R.id.sales_outstock_select_wabillorder)
    EditText sales_outstock_select_wabillorder;

    //发货通知单号
    @ViewInject(R.id.sales_outstock_select_order)
    EditText sales_outstock_select_order;


    @ViewInject(R.id.outstock_orderselect_ListView)
    ListView mList;

    //listview    适配器
    OutstockOrderSelectAdapter mAdapter;


    UrlInfo info = new UrlInfo();
    MenuOutStockModel model = new MenuOutStockModel();

    List<OutstockOrderSelectModel> mModelList=new ArrayList<>();

    @Override
    protected void initViews() {
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        String arr = data.toString();
        model = GsonUtil.parseJsonToModel(arr, MenuOutStockModel.class);
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(model.Title + "-" + BaseApplication.mCurrentWareHouseInfo.Warehouseno, true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_setting_warehouse_select) {
            selectWareHouse(this);

        }
        return false;
    }

    @Override
    public void getToolTitle() {
        getToolBarHelper().getToolBar().setTitle(model.Title + "-" + BaseApplication.mCurrentWareHouseInfo.Warehouseno);
        // mList.not
    }

    //点击查询
    @Event(value = R.id.outstock_selectorder_button)
    private void Click_Select(View view) {
        if (sales_outstock_select_wabillorder.getText().toString().trim().equals("") && sales_outstock_select_order.getText().toString().trim().equals("")) {
            CommonUtil.setEditFocus(sales_outstock_select_wabillorder);
            MessageBox.Show(context, "请输入或扫描单号");
        } else {
            //都不为空的情况
            OutstockOrderSelectModel model = new OutstockOrderSelectModel();
            model.Erpvoucherno = sales_outstock_select_wabillorder.getText().toString().trim();
            model.Arrvoucherno = sales_outstock_select_order.getText().toString().trim();
            String json = GsonUtil.parseModelToJson(model);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_OrderSelect, "查询单号信息中",
                    context, mHandler, RESULT_Saleoutstock_OrderSelect, null, info.OutStock_OrderSelect, json, null);
        }
    }


    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Saleoutstock_OrderSelect:
                SacnnNo((String) msg.obj);
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

    public  void  SacnnNo(String result){
        try {
            mModelList=new ArrayList<>();
            BaseResultInfo<List<OutstockOrderSelectModel>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo< List<OutstockOrderSelectModel>>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(sales_outstock_select_order);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            mModelList=returnMsgModel.getData();
            if (mModelList.size() > 0) {
                //绑定
                mAdapter = new OutstockOrderSelectAdapter(context, mModelList);
                mList.setAdapter(mAdapter);
            }else{
                Toast.makeText(context, "该单号对应的数据为空", Toast.LENGTH_SHORT).show();
            }
            CommonUtil.setEditFocus(sales_outstock_select_order);
            return;
        } catch (Exception ex) {
            CommonUtil.setEditFocus(sales_outstock_select_order);
            MessageBox.Show(context, "数据解析报错");
            return;
        }


    }



}
