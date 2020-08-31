package com.liansu.boduowms.modules.inHouseStock.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryModel;
import com.liansu.boduowms.modules.outstock.Model.Outbarcode_Requery;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.GsonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.RESULT_InventoryHead_SelectLit;
import static com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryTag.TAG_InventoryHead_SelectLit;

//盘点配置页面
@ContentView(R.layout.activity_inventory_config)
public class InventoryConfig extends BaseActivity {
    Context context = InventoryConfig.this;
    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.toolBarTitle = new ToolBarTitle("盘点单扫描", true);
        x.view().inject(this);
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        InventoryModel model = new InventoryModel();
        String arr = data.toString();
        model = GsonUtil.parseJsonToModel(arr, InventoryModel.class);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_InventoryHead_SelectLit, "获取盘点列表",
//                context, mHandler, RESULT_InventoryHead_SelectLit, null, UrlInfo.getUrl().Inventory_Head_GetCheckList, "", null);

    }

    @Override
    protected void initData() {
        super.initData();
    }



}