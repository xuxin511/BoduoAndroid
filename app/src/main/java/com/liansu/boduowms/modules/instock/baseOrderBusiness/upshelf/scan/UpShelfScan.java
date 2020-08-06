package com.liansu.boduowms.modules.instock.baseOrderBusiness.upshelf.scan;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;

/**
 * @desc:  上架
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/7/5 22:36
 */
@ContentView(R.layout.activity_up_shelf_scan)
public class UpShelfScan extends BaseActivity implements IUpShelfScanView {
    @Override
    public void bindListView(ArrayList<OrderDetailInfo> receiptDetailModels) {

    }

    @Override
    public void onReset() {

    }

    @Override
    public void onBarcodeFocus() {

    }

    @Override
    public void onAreaNoFocus() {

    }

    @Override
    public void setReceiptHeaderInfo(OrderHeaderInfo receipt_model) {

    }
//    Context context = UpShelfScan.this;
//    @ViewInject(R.id.lsv_UpShelfScan)
//    ListView mListView;
//    @ViewInject(R.id.txt_VoucherNo)
//    TextView txtVoucherNo;
//    @ViewInject(R.id.txt_Company)
//    TextView txtCompany;
//    @ViewInject(R.id.txt_Batch)
//    TextView txtBatch;
//    @ViewInject(R.id.txt_Status)
//    TextView txtStatus;
//    @ViewInject(R.id.txt_EDate)
//    TextView txtEDate;
//    @ViewInject(R.id.txt_MaterialName)
//    TextView txtMaterialName;
//    @ViewInject(R.id.txt_UpShelfNum)
//    TextView txtUpShelfNum;
//    @ViewInject(R.id.txt_UpShelfScanNum)
//    TextView txtUpShelfScanNum;
//    @ViewInject(R.id.edt_UpShelfScanBarcode)
//    EditText mBarcode;
//    @ViewInject(R.id.edt_StockScan)
//    EditText mAreaNo;
//
//    @ViewInject(R.id.edt_up_scanqty)
//    EditText edtUpScanQty;
//
//    UpShelfScanDetailAdapter mAdapter;
//    UpShelfScanPresenter     mPresenter;
//
//    @Override
//    protected void initViews() {
//        super.initViews();
//        BaseApplication.context = context;
//        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.UpShelfscan_subtitle) , true);
//        x.view().inject(this);
//        BaseApplication.isCloseActivity = false;
//
//
//    }
//
//    @Override
//    protected void initData() {
//        super.initData();
//        OrderHeaderInfo headerInfo = getIntent().getParcelableExtra("orderHeaderInfo");
//        List<OutBarcodeInfo> barCodeInfos = getIntent().getParcelableArrayListExtra("barCodeInfo");
//        if (mPresenter == null) {
//            mPresenter = new UpShelfScanPresenter(context, this, mHandler, headerInfo, barCodeInfos);
//        }
//        if (DebugModuleData.isDebugDataStatusOffline()) {
//            ArrayList<OrderDetailInfo> list = DebugModuleData.loadUpshelfDetailList();
//            bindListView(list);
////            txtVoucherNo.setText(inStockTaskInfoModel.getErpVoucherNo());
//            return;
//        }
//        mPresenter.getReceiptDetailList(headerInfo);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//    }
//
//    @Event(value = R.id.edt_area_no, type = View.OnKeyListener.class)
//    private boolean edtStockScanClick(View v, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
//        {
//            String areaNo = mAreaNo.getText().toString().trim();
//            mBarcode.setText("");
//            if (TextUtils.isEmpty(areaNo)) {
//                CommonUtil.setEditFocus(mAreaNo);
//                return true;
//            }
//            mPresenter.getAreaInfo(areaNo);
//        }
//        return false;
//    }
//
//
//    @Event(value = R.id.edt_RecScanBarcode, type = View.OnKeyListener.class)
//    private boolean edtRecScanBarcode(View v, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
//        {
//            keyBoardCancle();
//            String barcode = mBarcode.getText().toString().trim();
//            if (mPresenter != null) {
//                mPresenter.onScan(barcode);
//            }
//        }
//
//        return false;
//    }
//    @Event(R.id.btn_ReceiptDetail)
//    private void btnLoginClick(View view) {
//        Intent intent=new Intent();
//        intent.setClass(UpShelfScan.this, InstockCombinePallet.class);
//        Bundle bundle = new Bundle();
//        bundle.putInt("inStockType", COMBINE_PALLET_TYPE_RECEIPTION);
//        bundle.putParcelable("orderHeader",mPresenter.getModel().getReceiptModel());
//        bundle.putParcelableArrayList("orderDetailList", mPresenter.getModel().getReceiptDetailModels());
//        intent.putExtras(bundle);
//        startActivityLeft(intent);
//    }
//
//
//    @Event(value = R.id.lsv_ReceiptScan, type = AdapterView.OnItemClickListener.class)
//    private boolean lsv_ReceiptScanItemClick(AdapterView<?> parent, View view, int position,
//                                             long id) {
////        if (id >= 0) {
////            ReceiptDetail_Model receiptDetailModel = receiptDetailModels.get(position);
////            try {
////                if (receiptDetailModel.getLstBarCode() != null && receiptDetailModel.getLstBarCode().size() != 0) {
//////                        Intent intent = new Intent(context, ReceiptionBillDetail.class);
//////                        Bundle bundle = new Bundle();
//////                        bundle.putParcelable("receiptDetailModel", receiptDetailModel);
//////                        intent.putExtras(bundle);
//////                        startActivityLeft(intent);
////                }
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        }
//        return true;
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_filter) {
//            if (DoubleClickCheck.isFastDoubleClick(context)) {
//                return false;
//            }
//        }
//
//        return false;
//    }
//
//
//    @Override
//    public void bindListView(ArrayList<OrderDetailInfo> receiptDetailModels) {
//        mAdapter = new UpShelfScanDetailAdapter(context, receiptDetailModels);
//        mListView.setAdapter(mAdapter);
//
//    }
//
//    @Override
//    public void onReset() {
//
//    }
//
//    @Override
//    public void onBarcodeFocus() {
//        CommonUtil.setEditFocus(mBarcode);
//    }
//
//    @Override
//    public void onAreaNoFocus() {
//        CommonUtil.setEditFocus(mAreaNo);
//    }
//
//    @Override
//    public void setReceiptHeaderInfo(OrderHeaderInfo receipt_model) {
//        if (receipt_model != null) {
//            txtVoucherNo.setText(receipt_model.getErpvoucherno());
////            mSupplierName.setText(receipt_model.getSuppliername());
//        }
//    }
}
