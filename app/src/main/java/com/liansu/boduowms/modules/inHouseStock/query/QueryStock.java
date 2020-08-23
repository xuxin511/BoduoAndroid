package com.liansu.boduowms.modules.inHouseStock.query;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.ui.adapter.inHouseStock.QueryStockDetailAdapter;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetworkError;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ Des:  库存查询
 * @ Created by yangyiqing on 2020/8/23.
 */
@ContentView(R.layout.activity_query_stock)
public class QueryStock extends BaseActivity implements IQueryStockView {
    Context mContext = QueryStock.this;
    @ViewInject(R.id.activity_query_stock_radio_group)
    RadioGroup   mRadioGroup;
    @ViewInject(R.id.activity_query_stock_material)
    RadioButton  mMaterialQuery;
    @ViewInject(R.id.activity_query_stock_batch_no)
    RadioButton  mBatchNo;
    @ViewInject(R.id.activity_query_stock_area_no)
    RadioButton  mAreaNo;
    @ViewInject(R.id.activity_query_stock_content)
    EditText     mContent;
    @ViewInject(R.id.activity_query_stock_recyclerView)
    RecyclerView mRecyclerView;
    QueryStockDetailAdapter mAdapter;
    String                  TAG_GetWareHouse    = "AdjustStock_GetWareHouse";
    String                  TAG_GetInfoBySerial = "AdjustStock_GetInfoBySerial";
    String                  TAG_SaveInfo        = "AdjustStock_SaveInfo";
    private final int RESULT_GetWareHouse    = 101;
    private final int RESULT_GetInfoBySerial = 102;
    private final int RESULT_SaveInfo        = 103;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_GetWareHouse:
//                AnalysisGetWareHouseJson((String) msg.obj);
                break;
            case RESULT_GetInfoBySerial:
//                AnalysisGetInfoBySerialJson((String) msg.obj);
                break;
            case RESULT_SaveInfo:
//                AnalysisSaveInfoJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;
        }
    }


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(mContext.getResources().getString(R.string.app_bar_title_inventory_stock_query) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename(), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;

    }

    @Override
    public void bindListView(List<StockInfo> list) {

        if (mAdapter == null) {
            mAdapter = new QueryStockDetailAdapter(mContext, list);
            mAdapter.setRecyclerView(mRecyclerView);
            mAdapter.setOnItemClickListener(new QueryStockDetailAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView parent, View view, int position, StockInfo data) {
                    if (data != null) {
                        OutBarcodeInfo scanQRCode = null;
//
                    }

                }
            });
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onContentFocus() {

    }

    @Override
    public int getQueryType() {
        return 0;
    }


}
