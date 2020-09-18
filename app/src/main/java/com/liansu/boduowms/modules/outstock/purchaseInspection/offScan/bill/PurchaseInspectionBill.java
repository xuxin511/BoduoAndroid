package com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.bill;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.outstock.Model.MaterialResponseModel;
import com.liansu.boduowms.modules.outstock.Model.MenuOutStockModel;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockAdapter;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockRequery;
import com.liansu.boduowms.modules.outstock.SalesOutstock.OutstockRawmaterialActivity;
import com.liansu.boduowms.modules.outstock.purchaseInspection.offScan.scan.PurchaseInspectionProcessingScan;
import com.liansu.boduowms.modules.outstock.purchaseReturn.offscan.PurchaseReturnOffScanModel;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.ui.adapter.outstock.purchaseInspection.PurchaseInspectionBillItemAdapter;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.GsonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @desc: 采购验退列表
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/7/19 15:40
 */
@ContentView(R.layout.activity_quality_inspection_bill_choice)
public class PurchaseInspectionBill extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, IPurchaseInspectionBillView {
    /*业务类型 */
    String                 businesType = "";
    Context                context     = PurchaseInspectionBill.this;
    PurchaseInspectionBillPresenter mPresenter;

    @Override
    public void onHandleMessage(Message msg) {
        mSwipeLayout.setRefreshing(false);
        if (mPresenter != null) {
            mPresenter.onHandleMessage(msg);
        }
    }

    @ViewInject(R.id.lsvChoiceReceipt)
    ListView           mListView;
    @ViewInject(R.id.inspection_bill_swipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @ViewInject(R.id.edt_filterContent)
    EditText           mEdtfilterContent;
    @ViewInject(R.id.txt_receipt_sumrow)
    TextView           mSumBillCount;
    PurchaseInspectionBillItemAdapter mAdapter;

    public  int Currvouchertype;
    MenuOutStockModel menuOutStockModel = new MenuOutStockModel();
    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();

        String arr=data.toString();
        menuOutStockModel = GsonUtil.parseJsonToModel(arr,MenuOutStockModel.class);
        Currvouchertype=Integer.parseInt(menuOutStockModel.VoucherType);
        //info.InitUrl(type);
    //    initTitle();
        BaseApplication.toolBarTitle = new ToolBarTitle(menuOutStockModel.Title+"-"+BaseApplication.mCurrentWareHouseInfo.Warehouseno, true);
        //BaseApplication.toolBarTitle = new ToolBarTitle(model.getTitle()+"-"+BaseApplication.mCurrentWareHouseInfo.getWarehouseno(), true);
        x.view().inject(this);
        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    protected UserSettingPresenter mUserSettingPresenter;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_setting_warehouse_select) {
            selectWareHouse(this);
        }
        return false;
    }

    @Override
    public void getToolTitle() {
        getToolBarHelper().getToolBar().setTitle(menuOutStockModel.Title + "-" + BaseApplication.mCurrentWareHouseInfo.Warehouseno);
        //清空列表//切换仓库后需要重新扫描
        //存储类
        if (mPresenter == null) {
            mPresenter = new PurchaseInspectionBillPresenter(context, this, mHandler,Currvouchertype);
        }
        onRefresh();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter == null) {
            mPresenter = new PurchaseInspectionBillPresenter(context, this, mHandler,Currvouchertype);
        }
        onRefresh();


    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void initData() {
        super.initData();
        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
    }




    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            mPresenter.onReset();
            OutStockOrderHeaderInfo qualityHeaderInfo = new OutStockOrderHeaderInfo();
            qualityHeaderInfo.setVouchertype(Currvouchertype);
            qualityHeaderInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
            mPresenter.getQualityInsHeaderList(qualityHeaderInfo);
        }
    }

    public  String loadString(){
        String name="";
        switch(Currvouchertype)
        {
            case 28:
                name="采购验退下架";
                    break;
            case 61:
                name="成品验退下架";
                break;
            case 62:
                name="销售验退下架";
                break;
        }
        return name;
    }


    /**
     * Listview item点击事件
     */
    @Event(value = R.id.lsvChoiceReceipt, type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OutStockOrderHeaderInfo receiptModel = (OutStockOrderHeaderInfo) mAdapter.getItem(position);
        try {
            Intent intent = new Intent();
            MenuOutStockModel model=new MenuOutStockModel();
            model.Title=getTitleString();
            model.VoucherType=String.valueOf(Currvouchertype);
            model.ErpVoucherNo=receiptModel.getErpvoucherno();
            String json = GsonUtil.parseModelToJson(model);
            Uri data = Uri.parse(json);
            intent.setData(data);
            intent.setClass(this, OutstockRawmaterialActivity.class);
            startActivity(intent);
          //  StartScanIntent(receiptModel, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Event(value = R.id.edt_filterContent, type = View.OnKeyListener.class)
    private boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            String code = mEdtfilterContent.getText().toString().trim();
            if (code.equals("")) {
                return true;
            }
            if (code.length() < 25) {
                if (mPresenter != null) {
                    mPresenter.getQualityInspectionDetailList(code, Currvouchertype);
                }

            } else {
                MessageBox.Show(context,"校验订单的长度失败:请扫描订单号");
            }

        }
        return false;
    }


    void StartScanIntent(OutStockOrderHeaderInfo headerInfo, ArrayList<OutBarcodeInfo> barCodeInfo) {
        Intent intent = new Intent();
        intent.setClass(PurchaseInspectionBill.this, PurchaseInspectionProcessingScan.class);
        String json = GsonUtil.parseModelToJson(headerInfo);
        Uri data = Uri.parse(json);
        intent.setData(data);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("QUALITY_INSPECTION", headerInfo);
//        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void sumBillCount(int billCount) {
        mSumBillCount.setText("合计:" + billCount);
    }

    @Override
    public void onFilterContentFocus() {
        CommonUtil.setEditFocus(mEdtfilterContent);
    }

    @Override
    public void bindListView(List<OutStockOrderHeaderInfo> receiptModels) {
        if (mAdapter==null){
            mAdapter = new PurchaseInspectionBillItemAdapter(context, receiptModels);
            mAdapter.notifyDataSetChanged();
            mListView.setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onReset() {
        mEdtfilterContent.setText("");
    }

    @Override
    public void stopRefreshProgress() {
        //处理完业务后记得关闭，这里也得用post
        mSwipeLayout.post(new Runnable() {//刷新完成
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void startRefreshProgress() {
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });
    }

    public void initTitle(){
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.purchase_inspection_processing_title), false);
    }
}
