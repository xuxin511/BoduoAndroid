package com.liansu.boduowms.modules.instock.baseOrderBusiness.upshelf.bill;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;

import org.xutils.view.annotation.ContentView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


@ContentView(R.layout.activity_bill_choice)
public class UpShelfBillChoice extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Override
    public void onRefresh() {

    }
//
//
//    String TAG_GetT_InTaskListADF       = "UpShelfBillChoice_GetT_InTaskListADF";
//    String TAG_GetT_ScanInStockModelADF = "UpShelfBillChoice_GetT_ScanInStockModelADF";
//    private final int RESULT_GetT_InTaskListADF = 101;
//    private final int RESULT_GetT_ScanInStockModelADF = 102;
//    boolean isScanOrder = false;
//
//    @Override
//    public void onHandleMessage(Message msg) {
//        mSwipeLayout.setRefreshing(false);
//        switch (msg.what) {
//            case RESULT_GetT_InTaskListADF:
//                AnalysisGetT_InTaskListADFJson((String) msg.obj);
//                break;
//            case RESULT_GetT_ScanInStockModelADF:
//                AnalysisGetT_ScanInStockModelADFJson((String) msg.obj);
//                break;
//            case NetworkError.NET_ERROR_CUSTOM:
//                ToastUtil.show("获取请求失败_____" + msg.obj);
//                CommonUtil.setEditFocus(edtfilterContent);
//                break;
//        }
//    }
//
//    @ViewInject(R.id.lsvChoice)
//    ListView           lsvChoice;
//    @ViewInject(R.id.mSwipeLayout)
//    SwipeRefreshLayout mSwipeLayout;
//    @ViewInject(R.id.edt_filterContent)
//    EditText           edtfilterContent;
//    @ViewInject(R.id.txt_billchoice_sumrow)
//    TextView           txtChoiceSumRow;
//
//
//    Context                      context = UpShelfBillChoice.this;
//    UpshelfBillChioceItemAdapter upshelfBillChioceItemAdapter;
//    ArrayList<OrderHeaderInfo>   inStockTaskInfoModels;
//    ArrayList<OutBarcodeInfo>    stockInfoModels;
//
//    @Override
//    protected void initViews() {
//        super.initViews();
//        BaseApplication.context = context;
////        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.UpShelf_title) + "-" + BaseApplication.mCurrentUserInfo.getWarehouseName(), false);
//        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.UpShelf_title) , false);
//        x.view().inject(this);
//    }
//
//    @Override
//    protected void initData() {
//        super.initData();
//        mSwipeLayout.setOnRefreshListener(this); //下拉刷新
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (DebugModuleData.isDebugDataStatusOffline()){
//            ArrayList<OrderHeaderInfo> inStockTaskInfoModels=DebugModuleData.loadUpshelfHeaderList();
//            BindListVIew(inStockTaskInfoModels);
//            return;
//        }
//        InitListView();
//        edtfilterContent.setText("");
//        CommonUtil.setEditFocus(edtfilterContent);
//    }
//
//    @Override
//    public void onRefresh() {
//        inStockTaskInfoModels = new ArrayList<>();
//        edtfilterContent.setText("");
//        if (DebugModuleData.isDebugDataStatusOffline()){
//            ArrayList<OrderHeaderInfo> inStockTaskInfoModels=DebugModuleData.loadUpshelfHeaderList();
//            BindListVIew(inStockTaskInfoModels);
//            return;
//        }
//        InitListView();
//    }
//
//    /**
//     * Listview item点击事件
//     */
//    @Event(value = R.id.lsvChoice, type = AdapterView.OnItemClickListener.class)
//    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        OrderHeaderInfo inStockTaskInfoModel = (OrderHeaderInfo) upshelfBillChioceItemAdapter.getItem(position);
//        StartScanIntent(inStockTaskInfoModel, null);
//    }
//
//    @Event(value = R.id.edt_filterContent, type = View.OnKeyListener.class)
//    private boolean onKey(View v, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
//        {
//            int index = -1;
//            String code = edtfilterContent.getText().toString().trim();
//            if (inStockTaskInfoModels != null && inStockTaskInfoModels.size() > 0) {
//                //扫描单据号、检查单据列表
//                InStockTaskInfo_Model inStockTaskInfoModel = new InStockTaskInfo_Model(code);
//                index = inStockTaskInfoModels.indexOf(inStockTaskInfoModel);
//            }
//            if (index != -1) {
//                StartScanIntent(inStockTaskInfoModels.get(index), null);
//                return false;
//            } else {
//
//                if (code.contains("SHJC") || code.contains("JSJC") || code.contains("SHSY") || code.length()<10) {
//                    InStockTaskInfo_Model inStockTaskInfoModel = new InStockTaskInfo_Model();
//                    inStockTaskInfoModel.setStatus(1);
////                    inStockTaskInfoModel.setWareHouseID(BaseApplication.mCurrentUserInfo.getWarehouseID());
//                    inStockTaskInfoModel.setErpVoucherNo(code);
//                    GetT_InStockTaskInfoList(inStockTaskInfoModel);
//                }else {
//                    final Map<String, String> params = new HashMap<String, String>();
//                    params.put("SerialNo", code);
//                    params.put("ERPVoucherNo", "");
//                    params.put("TaskNo", "");
//                    params.put("AreaNo", "");
////                    params.put("WareHouseID", BaseApplication.mCurrentUserInfo.getWarehouseID() + "");
//                    isScanOrder = true;
//                    LogUtil.WriteLog(UpShelfBillChoice.class, TAG_GetT_ScanInStockModelADF, code);
//                    RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_ScanInStockModelADF, getString(R.string.Msg_GetT_InStockListADF), context, mHandler, RESULT_GetT_ScanInStockModelADF, null, URLModel.GetURL().GetT_ScanInStockModelADF, params, null);
//
//                }
//                //扫描箱条码
//
//            }
//
//            // StartScanIntent(null,null);
//            CommonUtil.setEditFocus(edtfilterContent);
//            return false;
//        }
//        return false;
//    }
//
//
//    /**
//     * 初始化加载listview
//     */
//    private void InitListView() {
//        InStockTaskInfo_Model inStockTaskInfoModel = new InStockTaskInfo_Model();
//        inStockTaskInfoModel.setStatus(1);
//        inStockTaskInfoModel.setWareHouseID(BaseApplication.mCurrentWareHouseInfo.getId());
//        GetT_InStockTaskInfoList(inStockTaskInfoModel);
//    }
//
//    void GetT_InStockTaskInfoList(InStockTaskInfo_Model inStockTaskInfoModel) {
//        try {
//            inStockTaskInfoModel.setPcOrPda("0");
//            String ModelJson = GsonUtil.parseModelToJson(inStockTaskInfoModel);
//            Map<String, String> params = new HashMap<>();
////            params.put("UserJson", GsonUtil.parseModelToJson(BaseApplication.mCurrentUserInfo));
//            params.put("ModelJson", ModelJson);
//            // params.put("WareHouseID",BaseApplication.mCurrentUserInfo.getWarehouseID()+"");
//            LogUtil.WriteLog(UpShelfBillChoice.class, TAG_GetT_InTaskListADF, ModelJson);
//            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InTaskListADF, getString(R.string.Msg_GetT_InStockListADF), context, mHandler, RESULT_GetT_InTaskListADF, null, URLModel.GetURL().GetT_InTaskListADF, params, null);
//        } catch (Exception ex) {
//            mSwipeLayout.setRefreshing(false);
//            MessageBox.Show(context, ex.getMessage());
//            CommonUtil.setEditFocus(edtfilterContent);
//        }
//    }
//
//    void AnalysisGetT_InTaskListADFJson(String result) {
//        LogUtil.WriteLog(UpShelfBillChoice.class, TAG_GetT_InTaskListADF, result);
//
//        ReturnMsgModelList<OrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<InStockTaskInfo_Model>>() {
//        }.getType());
//        if (returnMsgModel.getHeaderStatus().equals("S")) {
//            inStockTaskInfoModels = returnMsgModel.getModelJson();
//            if (inStockTaskInfoModels != null && inStockTaskInfoModels.size() == 1 && stockInfoModels != null && stockInfoModels.size() != 0) {
//                if (isScanOrder) {
//                    isScanOrder = false;
//                    StartScanIntent(inStockTaskInfoModels.get(0), stockInfoModels);
//                }
//            } else {
//                txtChoiceSumRow.setText("合计:" + inStockTaskInfoModels.size());
//                BindListVIew(inStockTaskInfoModels);
//            }
//
//        } else {
//            MessageBox.Show(context, returnMsgModel.getMessage());
//            CommonUtil.setEditFocus(edtfilterContent);
//        }
//    }
//
//    void AnalysisGetT_ScanInStockModelADFJson(String result) {
//        LogUtil.WriteLog(UpShelfBillChoice.class, TAG_GetT_ScanInStockModelADF, result);
//        ReturnMsgModelList<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<OutBarcodeInfo7>>() {
//        }.getType());
//        if (returnMsgModel.getHeaderStatus().equals("S")) {
//            stockInfoModels = returnMsgModel.getModelJson();
//            if (stockInfoModels != null) {
//                // Receipt_Model receiptModel = new Receipt_Model(barCodeInfo.getBarCode());
//                //  int index = receiptModels.indexOf(receiptModel);
//                //  if (index != -1) {
//                //调用GetT_InStockList 赋值ERP订单号字段，获取Receipt_Model列表，跳转到扫描界面
//                InStockTaskInfo_Model inStockTaskInfoModel = new InStockTaskInfo_Model();
//                inStockTaskInfoModel.setStatus(1);
//                inStockTaskInfoModel.setTaskNo(stockInfoModels.get(0).getVoucherno());
//                GetT_InStockTaskInfoList(inStockTaskInfoModel);
//                //   } else {
//                //     MessageBox.Show(context, R.string.Error_BarcodeNotInList);
//                // }
//            }
//        } else {
//            MessageBox.Show(context, returnMsgModel.getMessage());
//            CommonUtil.setEditFocus(edtfilterContent);
//        }
//    }
//
//    void StartScanIntent(OrderHeaderInfo inStockTaskInfoModel, ArrayList<OutBarcodeInfo> stockInfoModels) {
//        Intent intent = new Intent(context, UpShelfScan.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("orderHeaderInfo", inStockTaskInfoModel);
////        bundle.putParcelableArrayList("stockInfoModels", stockInfoModels);
//        intent.putExtras(bundle);
//        startActivityLeft(intent);
//    }
//
//    private void BindListVIew(ArrayList<OrderHeaderInfo> inStockTaskInfoModels) {
//        upshelfBillChioceItemAdapter = new UpshelfBillChioceItemAdapter(context, inStockTaskInfoModels);
//        lsvChoice.setAdapter(upshelfBillChioceItemAdapter);
//    }

}
