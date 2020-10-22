package com.liansu.boduowms.modules.inHouseStock.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.widget.ListView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryConfigAdapter;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryDetailAdapter;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryModel;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.T_Parameter;
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

import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryConfig_GetBarcodeInfo;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryConfig_GetWarehouse;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryDetail_GetIDetail;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_Project_GetParameter;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_InventoryDetail_GetIDetail;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_Project_GetParameter;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

//盘点明细
@ContentView(R.layout.activity_inventory_detail)
public class InventoryDetail extends BaseActivity {
    Context context = InventoryDetail.this;
    //列表
    @ViewInject(R.id.inventory_detail_list)
    ListView mList;

    //适配器
    InventoryDetailAdapter mAdapter;



    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.toolBarTitle = new ToolBarTitle("已盘明细", true);
        x.view().inject(this);
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        InventoryModel model = new InventoryModel();
        String arr = data.toString();//明细只要传erpvhoucherno
        model = GsonUtil.parseJsonToModel(arr, InventoryModel.class);
        String modelJson = parseModelToJson(model);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_InventoryDetail_GetIDetail, "获取明细列表",
                context, mHandler, RESULT_InventoryDetail_GetIDetail, null, UrlInfo.getUrl().Inventory_Detail_GetDetail, modelJson, null);
    }

    @Override
    protected void initData() {
        super.initData();
    }


    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_InventoryDetail_GetIDetail:
                LoadList((String) msg.obj);
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


    //加载列表
    public  void  LoadList(String result) {
        try {
            BaseResultInfo<List<InventoryModel>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<InventoryModel>>>() {
            }.getType());
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            } else {
                //更新列表
                mAdapter = new InventoryDetailAdapter(context, returnMsgModel.getData());
                mList.setAdapter(mAdapter);
            }
        } catch (Exception ex) {

            MessageBox.Show(context, ex.toString());
        }

    }









}