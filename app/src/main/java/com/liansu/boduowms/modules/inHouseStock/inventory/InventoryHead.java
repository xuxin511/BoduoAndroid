package com.liansu.boduowms.modules.inHouseStock.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
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
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryHeadAdapter;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryModel;
import com.liansu.boduowms.modules.outstock.Model.Outbarcode_Requery;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockAdapter;
import com.liansu.boduowms.modules.outstock.SalesOutstock.SalesOutStockBoxList;
import com.liansu.boduowms.modules.outstock.SalesOutstock.SalesOutstock;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.GsonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryHead_SelectLit;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_InventoryHead_SelectLit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_PlatForm;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_SalesNO;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannBoxNo;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannPalletNo;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannParts;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ScannParts_Submit;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_barcodeisExist;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_SubmitParts;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

//盘点表头
@ContentView(R.layout.activity_inventory_head)
public class InventoryHead extends BaseActivity {
    Context context = InventoryHead.this;

    //托盘单号
    @ViewInject(R.id.inventory_Head_orderText)
    EditText inventory_Head_orderText;

    //列表
    @ViewInject(R.id.inventory_Head_ListView)
    ListView mList;

    //适配器
    InventoryHeadAdapter mAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.toolBarTitle = new ToolBarTitle("盘点单选择", true);
        x.view().inject(this);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InventoryModel model = (InventoryModel) mAdapter.getItem(i);
                //选择调页面
                Intent intent = new Intent();
                //intent.setData(data);
                //本地单号传过去
                String json = GsonUtil.parseModelToJson(model);
                Uri data = Uri.parse(json);
                intent.setData(data);
                intent.setClass(context, InventoryConfig.class);
                startActivity(intent);
            }
        });
        InventoryModel inventoryModel = new InventoryModel();
        String modelJson = parseModelToJson(inventoryModel);
        //加载访问所有盘点信息
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_InventoryHead_SelectLit, "获取盘点列表",
                context, mHandler, RESULT_InventoryHead_SelectLit, null, UrlInfo.getUrl().Inventory_Head_GetCheckList, modelJson, null);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Saleoutstock_SalesNO:
                SelectList((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;
        }
    }

    //绑定listview
    public void SelectList(String result) {
        try {
            BaseResultInfo<List<InventoryModel>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<InventoryModel>>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                CommonUtil.setEditFocus(inventory_Head_orderText);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            } else {
                mAdapter = new InventoryHeadAdapter(context, returnMsgModel.getData());
                mList.setAdapter(mAdapter);
            }
        } catch (Exception ex) {
            CommonUtil.setEditFocus(inventory_Head_orderText);
            MessageBox.Show(context, ex.toString());
        }
    }


}


