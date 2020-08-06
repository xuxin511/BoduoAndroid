package com.liansu.boduowms.modules.instock.baseOrderBusiness.upshelf.scan;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;

import org.xutils.view.annotation.ContentView;


@ContentView(R.layout.activity_up_shelf_scan)
public class UpShelfScanActivity2 extends BaseActivity {
//
//    String TAG_GetT_InTaskDetailListByHeaderIDADF = "UpShelfScanActivity_GetT_InTaskDetailListByHeaderIDADF";
//    String TAG_GetT_ScanInStockModelADF           = "UpShelfScanActivity_GetT_ScanInStockModelADF";
//    String TAG_GetAreaModelADF                    = "UpShelfScanActivity_GetAreaModelADF";
//    String TAG_SaveT_InStockTaskDetailADF         = "UpShelfBillChoice_SaveT_InStockTaskDetailADF";
//
//    private final int RESULT_Msg_GetT_InTaskDetailListByHeaderIDADF = 101;
//    private final int RESULT_Msg_GetT_ScanInStockModelADF           = 102;
//    private final int RESULT_Msg_SaveT_InStockTaskDetailADF         = 103;
//    private final int RESULT_Msg_GetAreaModelADF                    = 104;
//
//    @Override
//    public void onHandleMessage(Message msg) {
//        switch (msg.what) {
//            case RESULT_Msg_GetT_InTaskDetailListByHeaderIDADF:
//                AnalysisGetT_InTaskDetailListByHeaderIDADFJson((String) msg.obj);
//                break;
//            case RESULT_Msg_GetT_ScanInStockModelADF:
//                AnalysisetT_PalletDetailByBarCodeJson((String) msg.obj);
//                break;
//            case RESULT_Msg_SaveT_InStockTaskDetailADF:
//                AnalysisSaveT_InStockTaskDetailADFJson((String) msg.obj);
//                break;
//            case RESULT_Msg_GetAreaModelADF:
//                AnalysisGetAreaModelADFJson((String) msg.obj);
//                break;
//            case NetworkError.NET_ERROR_CUSTOM:
//                ToastUtil.show("获取请求失败_____" + msg.obj);
//                CommonUtil.setEditFocus(edtUpShelfScanBarcode);
//                break;
//        }
//    }
//
//    Context context = UpShelfScanActivity2.this;
//    @ViewInject(R.id.lsv_UpShelfScan)
//    ListView lsvUpShelfScan;
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
//    EditText edtUpShelfScanBarcode;
//    @ViewInject(R.id.edt_StockScan)
//    EditText edtStockScan;
//
//    @ViewInject(R.id.edt_up_scanqty)
//    EditText edtUpScanQty;
//    @ViewInject(R.id.txtAll)
//    TextView txtAll1;
//
//    ArrayList<InStockTaskDetailsInfo_Model> inStockTaskDetailsInfoModels;
//    InStockTaskInfo_Model                   inStockTaskInfoModel     = null;
//    ArrayList<OutBarcodeInfo7>              stockInfoModels          = new ArrayList<OutBarcodeInfo7>();
//    AreaInfo                                areaInfoModel            = null;//扫描库位
//    UpShelfScanDetailAdapter                upShelfScanDetailAdapter;
//    float                                   barcodeQty               = 0;//扫描条码的数量
//    boolean                                 isInStock                = false;//录入数量是否已写入
//    Float                                   mSumReaminQty            = 0f; //当前拣货物料剩余拣货数量合计
//    ArrayList<InStockTaskDetailsInfo_Model> mSameLineInStockTaskDetailsInfoModels; //相同行物料集合
//    int                                     currentPickMaterialIndex = -1;
//
//    @Override
//    protected void initViews() {
//        super.initViews();
//        BaseApplication.context = context;
//        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.UpShelfscan_subtitle) , true);
////        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.UpShelfscan_subtitle) + "-" + BaseApplication.mCurrentUserInfo.getWarehouseName(), true);
//        x.view().inject(this);
//        BaseApplication.isCloseActivity = false;
//    }
//
//    @Override
//    protected void initData() {
//        super.initData();
//        inStockTaskInfoModel = getIntent().getParcelableExtra("inStockTaskInfoModel");
//        //  stockInfoModels=getIntent().getParcelableArrayListExtra("stockInfoModels");
//        CommonUtil.setEditFocus(edtStockScan);
//        if (DebugModuleData.isDebugDataStatusOffline()) {
////            ArrayList<InStockTaskDetailsInfo_Model> list = DebugModuleData.loadUpshelfDetailList();
////            BindListVIew(list);
////            txtVoucherNo.setText(inStockTaskInfoModel.getErpVoucherNo());
//            return;
//        }
//        GetInStockTaskDetail(inStockTaskInfoModel);
//
//        txtCompany.setText("");
//        txtBatch.setText("");
//        txtEDate.setText("");
//        txtStatus.setText("");
//        txtMaterialName.setText("");
//    }
//
//
//    @Event(value = R.id.edt_UpShelfScanBarcode, type = View.OnKeyListener.class)
//    private boolean edtUpShelfScanBarcodeClick(View v, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
//        {
//            String code = edtUpShelfScanBarcode.getText().toString().trim();
//            if (TextUtils.isEmpty(code)) {
//                CommonUtil.setEditFocus(edtUpShelfScanBarcode);
//                return true;
//            }
//            if (areaInfoModel == null) {
//                CommonUtil.setEditFocus(edtStockScan);
//                return true;
//            }
//            edtStockScan.setText(areaInfoModel.getAreaNo());
//            ScanBarcode(code, areaInfoModel.getAreaNo());
//
//        }
//        return false;
//    }
//
//    @Event(value = R.id.edt_StockScan, type = View.OnKeyListener.class)
//    private boolean edtStockScanClick(View v, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
//        {
//            String StockCode = edtStockScan.getText().toString().trim();
//            edtUpShelfScanBarcode.setText("");
//            edtUpScanQty.setText("");
//            stockInfoModels = null;
//            if (TextUtils.isEmpty(StockCode)) {
//                CommonUtil.setEditFocus(edtStockScan);
//                return true;
//            }
//            final Map<String, String> params = new HashMap<String, String>();
//            params.put("AreaNo", StockCode);
//            params.put("UserJson", parseModelToJson(BaseApplication.mCurrentUserInfo));
//            LogUtil.WriteLog(UpShelfScanActivity2.class, TAG_GetAreaModelADF, StockCode);
//            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetAreaModelADF, getString(R.string.Msg_GetAreaModelADF), context, mHandler, RESULT_Msg_GetAreaModelADF, null, URLModel.GetURL().GetAreaModelADF, params, null);
//        }
//        return false;
//    }
//
//    @Event(value = R.id.edt_up_scanqty, type = View.OnKeyListener.class)
//    private boolean edtUpScanQtyClick(View v, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
//        {
//
//            try {
//                String value = edtUpScanQty.getText().toString();
//                Float scanQty = Float.valueOf(edtUpScanQty.getText().toString());
//                if (scanQty <= 0) {
//                    MessageBox.Show(context, "请输入正确的数量信息");
//                    CommonUtil.setEditFocus(edtUpScanQty);
//                    return true;
//                }
//                if (barcodeQty < scanQty) {
//                    MessageBox.Show(context, "上架数量不能大于条码剩余数量");
//                    CommonUtil.setEditFocus(edtUpScanQty);
//                    return true;
//                }
//                if (stockInfoModels != null && stockInfoModels.size() != 0) {
//                    for (OutBarcodeInfo7 stockInfoModel : stockInfoModels) {
//
//                        if (!isInStock) {
//                            if (!CheckBarcode(stockInfoModel, stockInfoModel.getQty())) {
//                                isInStock = true;
//                            }
//
//                        }
//                    }
//                }
//
//                CommonUtil.setEditFocus(edtUpScanQty);
//
//            } catch (Exception e) {
//                MessageBox.Show(context, "异常" + e.toString());
//                CommonUtil.setEditFocus(edtUpScanQty);
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_receiptbilldetail, menu);
//        menu.findItem(R.id.action_filter).setVisible(true);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_filter) {
//            if (DoubleClickCheck.isFastDoubleClick(context)) {
//                return false;
//            }
//            if (areaInfoModel != null && stockInfoModels != null) {
//                //筛选已经扫描的行
////                ArrayList<InStockTaskDetailsInfo_Model> listInStock = new ArrayList<InStockTaskDetailsInfo_Model>();
////                for (InStockTaskDetailsInfo_Model stockTask : inStockTaskDetailsInfoModels) {
////                    if (stockTask.getLstStockInfo() != null && stockTask.getLstStockInfo().size() > 0) {
////                        listInStock.add(stockTask);
////                    }
////                }
//                final Map<String, String> params = new HashMap<String, String>();
//                String ModelJson = parseModelToJson(inStockTaskDetailsInfoModels);
//                params.put("UserJson", parseModelToJson(BaseApplication.mCurrentUserInfo));
//                params.put("ModelJson", ModelJson);
//                LogUtil.WriteLog(UpShelfScanActivity2.class, TAG_SaveT_InStockTaskDetailADF, ModelJson);
//                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveT_InStockTaskDetailADF, getString(R.string.Msg_SaveT_InStockTaskDetailADF), context, mHandler, RESULT_Msg_SaveT_InStockTaskDetailADF, null, URLModel.GetURL().SaveT_InStockTaskDetailADF, params, null);
//
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Event(value = R.id.lsv_UpShelfScan, type = AdapterView.OnItemClickListener.class)
//    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        InStockTaskDetailsInfo_Model inStockTaskDetailsInfoModel = (InStockTaskDetailsInfo_Model) upShelfScanDetailAdapter.getItem(position);
//        String[] referStocks = GetReferStockArray(inStockTaskDetailsInfoModel.getLstArea());
//        if (referStocks != null && referStocks.length != 0) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("推荐库位：");
//            builder.setCancelable(true);
//            builder.setItems(referStocks, null);
//            builder.show();
//        }
//    }
//
//    /*
//   处理收货明细
//    */
//    void AnalysisGetT_InTaskDetailListByHeaderIDADFJson(String result) {
//        try {
//            LogUtil.WriteLog(UpShelfScanActivity2.class, TAG_GetT_InTaskDetailListByHeaderIDADF, result);
//            ReturnMsgModelList<InStockTaskDetailsInfo_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<InStockTaskDetailsInfo_Model>>() {
//            }.getType());
//            if (returnMsgModel.getHeaderStatus().equals("S")) {
//                inStockTaskDetailsInfoModels = returnMsgModel.getModelJson();
//                boolean isUpFull = true;
//                for (InStockTaskDetailsInfo_Model model : inStockTaskDetailsInfoModels) {
//                    if (model.getTaskQty1() > 0) {
//                        isUpFull = false;
//                        break;
//                    }
//                }
//                if (isUpFull) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("提示");
//                    builder.setMessage("当前任务已全部上架完成");
//                    builder.setPositiveButton("返回重选任务", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            closeActiviry();
//                        }
//                    });
//                    builder.setNeutralButton("留在本任务", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    });
//                    builder.show();
//                }
//                if (inStockTaskDetailsInfoModels != null) {
//                    Collections.sort(inStockTaskDetailsInfoModels, new InStockTaskDetailsInfo_Model());
//                    BindListVIew(inStockTaskDetailsInfoModels);
//                } else {
//
//                }
//
//            } else {
//                if (returnMsgModel.getMessage().contains("获取上架任务表体数据列表为空")) {
//                    new AlertDialog.Builder(context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage("当前任务已全部上架完成,返回重选任务")
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // TODO 自动生成的方法
//                                    closeActiviry();
//                                }
//                            }).show();
//                } else {
//                    MessageBox.Show(context, returnMsgModel.getMessage());
//                }
//
//
//            }
//        } catch (Exception ex) {
//            MessageBox.Show(context, ex.getMessage());
//        }
//        CommonUtil.setEditFocus(edtStockScan);
//    }
//
//
//    void ScanBarcode(String code, String StockCode) {
//        final Map<String, String> params = new HashMap<String, String>();
//        params.put("SerialNo", code);
//        params.put("ERPVoucherNo", inStockTaskInfoModel.getErpVoucherNo());
//        params.put("TaskNo", inStockTaskInfoModel.getTaskNo());
//        params.put("AreaNo", StockCode);
////        params.put("WareHouseID", BaseApplication.mCurrentUserInfo.get() + "");
//        LogUtil.WriteLog(UpShelfScanActivity2.class, TAG_GetT_ScanInStockModelADF, code);
//        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_ScanInStockModelADF, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_Msg_GetT_ScanInStockModelADF, null, URLModel.GetURL().GetT_ScanInStockModelADF, params, null);
//    }
//
//    /*
//    获取收货明细
//     */
//    void GetInStockTaskDetail(InStockTaskInfo_Model inStockTaskInfoModel) {
//        if (inStockTaskInfoModel != null) {
//            txtVoucherNo.setText(inStockTaskInfoModel.getErpVoucherNo());
//            InStockTaskDetailsInfo_Model inStockTaskDetailsInfoModel = new InStockTaskDetailsInfo_Model();
//            inStockTaskDetailsInfoModel.setHeaderID(inStockTaskInfoModel.getID());
//            inStockTaskDetailsInfoModel.setTaskNo(inStockTaskInfoModel.getTaskNo());
//            inStockTaskDetailsInfoModel.setErpVoucherNo(inStockTaskInfoModel.getErpVoucherNo());
//            inStockTaskDetailsInfoModel.setVoucherType(inStockTaskInfoModel.getVoucherType());
//            final Map<String, String> params = new HashMap<String, String>();
//            params.put("ModelDetailJson", parseModelToJson(inStockTaskDetailsInfoModel));
//            String para = (new JSONObject(params)).toString();
//            LogUtil.WriteLog(UpShelfScanActivity2.class, TAG_GetT_InTaskDetailListByHeaderIDADF, para);
//            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetT_InTaskDetailListByHeaderIDADF, getString(R.string.Msg_GetT_InTaskDetailListByHeaderIDADF), context, mHandler, RESULT_Msg_GetT_InTaskDetailListByHeaderIDADF, null, URLModel.GetURL().GetT_InTaskDetailListByHeaderIDADF, params, null);
//        }
//    }
//
//    /**
//     * @desc: 条码扫描
//     * @param:
//     * @return:
//     * @author: Nietzsche
//     * @time 2020/5/14 13:17
//     */
//    void AnalysisetT_PalletDetailByBarCodeJson(String result) {
//        LogUtil.WriteLog(UpShelfScanActivity2.class, TAG_GetT_ScanInStockModelADF, result);
//        ReturnMsgModelList<OutBarcodeInfo7> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<OutBarcodeInfo7>>() {
//        }.getType());
//        if (returnMsgModel.getHeaderStatus().equals("S")) {
//            stockInfoModels = returnMsgModel.getModelJson();
//            ;
//            if (stockInfoModels != null && stockInfoModels.size() != 0) {
//                barcodeQty = stockInfoModels.get(0).getQty();
//                edtUpScanQty.setText(stockInfoModels.get(0).getQty().toString());
//                txtCompany.setText(stockInfoModels.get(0).getMaterialNo());
//                txtBatch.setText(stockInfoModels.get(0).getBatchNo());
//                txtEDate.setText(CommonUtil.DateToString(stockInfoModels.get(0).getEDate()));
//                txtMaterialName.setText(stockInfoModels.get(0).getMaterialDesc());
////                if (areaInfoModel.getHouseProp().equals("2")) {
////                    CommonUtil.setEditFocus(edtUpScanQty);
////                } else {
////                    for (StockInfo_Model model:stockInfoModels){
////                        if (CheckBarcode(model)){
////                            AddSameLineMaterialNum(model);
////                        }
////                    }
////
////
////                }
//                for (OutBarcodeInfo7 model : stockInfoModels) {
//                    if (CheckBarcode(model)) {
//                        AddSameLineMaterialNum(model);
//                    }
//                }
//
//                Collections.sort(inStockTaskDetailsInfoModels, new InStockTaskDetailsInfo_Model());
//                BindListVIew(inStockTaskDetailsInfoModels);
//                CommonUtil.setEditFocus(edtUpShelfScanBarcode);
//            } else {
//                MessageBox.Show(context, "未获取到条码信息");
//                CommonUtil.setEditFocus(edtUpShelfScanBarcode);
//            }
//        } else {
//            MessageBox.Show(context, returnMsgModel.getMessage());
//            CommonUtil.setEditFocus(edtUpShelfScanBarcode);
//        }
//    }
//
//
//    /*
//    扫描库位
//     */
//    void AnalysisGetAreaModelADFJson(String result) {
//        try {
//            LogUtil.WriteLog(UpShelfScanActivity2.class, TAG_GetAreaModelADF, result);
//            ReturnMsgModel<AreaInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<AreaInfo>>() {
//            }.getType());
//            if (returnMsgModel.getHeaderStatus().equals("S")) {
//                areaInfoModel = returnMsgModel.getModelJson();
//                edtUpShelfScanBarcode.setText("");
//                edtUpScanQty.setText("");
//                stockInfoModels = null;
//                CommonUtil.setEditFocus(edtUpShelfScanBarcode);
//            } else {
//                MessageBox.Show(context, returnMsgModel.getMessage());
//                CommonUtil.setEditFocus(edtStockScan);
//            }
//        } catch (Exception ex) {
//            MessageBox.Show(context, ex.getMessage());
//            CommonUtil.setEditFocus(edtStockScan);
//        }
//    }
//
//
//    /*
//   提交收货
//    */
//    void AnalysisSaveT_InStockTaskDetailADFJson(String result) {
//        try {
//            LogUtil.WriteLog(UpShelfScanActivity2.class, TAG_SaveT_InStockTaskDetailADF, result);
//            ReturnMsgModel<Base_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModel<Base_Model>>() {
//            }.getType());
//            if (returnMsgModel.getHeaderStatus().equals("S")) {
//                new AlertDialog.Builder(context).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage(returnMsgModel.getMessage())
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // TODO 自动生成的方法
//                                ClearFrm();
//                                GetInStockTaskDetail(inStockTaskInfoModel);
//                            }
//                        }).show();
//
//
//            } else {
//                MessageBox.Show(context, returnMsgModel.getMessage());
//            }
//
//        } catch (Exception ex) {
//            MessageBox.Show(context, ex.getMessage());
//
//        }
//    }
//
//    void InitFrm(OutBarcodeInfo7 stockInfoModel) {
//        try {
//            if (stockInfoModel != null) {
//                txtCompany.setText(stockInfoModel.getStrongHoldName());
//                txtBatch.setText(stockInfoModel.getBatchNo());
//                txtStatus.setText("");
//                txtMaterialName.setText(stockInfoModel.getMaterialDesc());
//                txtEDate.setText(CommonUtil.DateToString(stockInfoModel.getEDate()));
//
//            }
//        } catch (Exception ex) {
//            MessageBox.Show(context, ex.getMessage());
//            CommonUtil.setEditFocus(edtUpShelfScanBarcode);
//        }
//    }
//
//
//    boolean CheckBarcode(OutBarcodeInfo7 StockInfo_Model, float upQty) {
//        if (StockInfo_Model != null && inStockTaskDetailsInfoModels != null) {
//
//            InStockTaskDetailsInfo_Model inStockTaskDetailsInfoModel = new InStockTaskDetailsInfo_Model(StockInfo_Model.getMaterialNo(), upQty);
//            int index = -1;
//            try {
//                index = inStockTaskDetailsInfoModels.indexOf(inStockTaskDetailsInfoModel);
//            } catch (Exception ex) {
//                String value = ex.toString();
//            }
//            try {
//                if (index != -1) {
//                    if (areaInfoModel != null) {
//                        inStockTaskDetailsInfoModels.get(index).setAreaID(areaInfoModel.getID());
//                        inStockTaskDetailsInfoModels.get(index).setHouseID(areaInfoModel.getHouseID());
//                        inStockTaskDetailsInfoModels.get(index).setWarehouseID(areaInfoModel.getWarehouseID());
//                        inStockTaskDetailsInfoModels.get(index).setWareHouseNo(areaInfoModel.getWarehouseNo());
//                        inStockTaskDetailsInfoModels.get(index).setToErpAreaNo(areaInfoModel.getAreaNo());
//                        inStockTaskDetailsInfoModels.get(index).setAreaNo(areaInfoModel.getAreaNo());
//                        inStockTaskDetailsInfoModels.get(index).setToErpWarehouse(areaInfoModel.getWarehouseNo());
//                    }
//                    if (inStockTaskDetailsInfoModels.get(index).getLstStockInfo() == null)
//                        inStockTaskDetailsInfoModels.get(index).setLstStockInfo(new ArrayList<OutBarcodeInfo7>());
//                    if (!inStockTaskDetailsInfoModels.get(index).getLstStockInfo().contains(StockInfo_Model)) {
//                        Float qty = ArithUtil.add(inStockTaskDetailsInfoModels.get(index).getScanQty(), upQty);
////                        if (qty <= inStockTaskDetailsInfoModels.get(index).getRemainQty()) {
//                        if (qty <= inStockTaskDetailsInfoModels.get(index).getTaskQty1()) {
//                            if (inStockTaskDetailsInfoModels.get(index).getFromErpWarehouse().equals(inStockTaskDetailsInfoModels.get(index).getToErpWarehouse())) {//不同仓库需要触发调拨业务员
////                                inStockTaskDetailsInfoModels.get(index).setVoucherType(999);//不生成调拨
//                            } else {
////                                inStockTaskDetailsInfoModels.get(index).setVoucherType(9996);//生成调拨单   9996
//                                inStockTaskDetailsInfoModels.get(index).setErpVoucherType("DB6");
//                                inStockTaskDetailsInfoModels.get(index).setFromErpAreaNo("");
//                                inStockTaskDetailsInfoModels.get(index).setToErpAreaNo("");
//                                inStockTaskDetailsInfoModels.get(index).setFromBatchNo(StockInfo_Model.getBatchNo());
//                            }
//
//                            inStockTaskDetailsInfoModels.get(index).setScanQty(qty);
//                            txtUpShelfNum.setText(inStockTaskDetailsInfoModels.get(index).getTaskQty1() + "");
//                            txtUpShelfScanNum.setText(inStockTaskDetailsInfoModels.get(index).getScanQty() + "");
//                            // edtUpShelfScanBarcode.setText(StockInfo_Model.getBarcode()+"");
//                            //StockInfo_Model.setAreaNo(edtStockScan.getText().toString().trim());
//                            inStockTaskDetailsInfoModels.get(index).getLstStockInfo().add(0, StockInfo_Model);
//                        } else {
//                            MessageBox.Show(context, getString(R.string.Error_UpshelfQtyBiger));
//                            return false;
//                        }
//                    } else {
//                        MessageBox.Show(context, getString(R.string.Error_BarcodeScaned) + "|" + StockInfo_Model.getSerialNo());
//                        return false;
//                    }
//                } else {
//                    MessageBox.Show(context, getString(R.string.Error_BarcodeNotInList) + "|" + StockInfo_Model.getSerialNo());
//                    CommonUtil.setEditFocus(edtUpShelfScanBarcode);
//                    return true;
//                }
//            } catch (Exception ex) {
//                MessageBox.Show(context, ex.toString());
//                return true;
//            }
//
//        }
//        return true;
//    }
//
//
//    private void BindListVIew(ArrayList<InStockTaskDetailsInfo_Model> inStockTaskDetailsInfoModels) {
////        upShelfScanDetailAdapter = new UpShelfScanDetailAdapter(context, inStockTaskDetailsInfoModels);
////        lsvUpShelfScan.setAdapter(upShelfScanDetailAdapter);
//    }
//
//
//    void ClearFrm() {
//
//        stockInfoModels = new ArrayList<>();
//        areaInfoModel = null;
//        inStockTaskDetailsInfoModels = new ArrayList<InStockTaskDetailsInfo_Model>();
//        edtStockScan.setText("");
//        edtUpShelfScanBarcode.setText("");
//        edtUpScanQty.setText("");
//        barcodeQty = 0;
//        isInStock = false;
//        CommonUtil.setEditFocus(edtStockScan);
//    }
//
//    String[] GetReferStockArray(ArrayList<AreaInfo> areaInfoModels) {
//        String[] referStocks = new String[areaInfoModels.size()];
//        if (areaInfoModels != null) {
//            int i = 0;
//            for (AreaInfo areaInfoModel : areaInfoModels) {
//                referStocks[i++] = areaInfoModel.getAreaNo();
//            }
//        }
//        return referStocks;
//    }
//
//    /**
//     * @desc: 统计相同行物料剩余拣货数量
//     * @param:
//     * @return:
//     * @author: Nietzsche
//     * @time 2020/5/14 11:09
//     */
//    void FindSumQtyByMaterialNo(String MaterialNo, String traceNo) {
//        if (traceNo == null) traceNo = "";
//        mSumReaminQty = 0.0f;
//        mSameLineInStockTaskDetailsInfoModels = new ArrayList<>();
//        for (int i = 0; i < inStockTaskDetailsInfoModels.size(); i++) {
//            InStockTaskDetailsInfo_Model model = inStockTaskDetailsInfoModels.get(i);
//            if (model != null) {
//                String sMaterialNo = model.getMaterialNo() != null ? model.getMaterialNo() : "";
//                String sTraceNo = model.getTracNo() != null ? model.getTracNo() : "";
//                String sStrongHoldNo = model.getStrongHoldCode() != null ? model.getStrongHoldCode() : "";
//                if (sStrongHoldNo.contains("SHJC")) {
//                    if (sMaterialNo.equals(MaterialNo) && sTraceNo.equals(traceNo)) {
//                        mSameLineInStockTaskDetailsInfoModels.add(model);
//                        mSumReaminQty = ArithUtil.add(mSumReaminQty, ArithUtil.sub(model.getTaskQty1(), model.getScanQty()));
//                    }
//                } else {
//                    if (sMaterialNo.equals(MaterialNo)) {
//                        mSameLineInStockTaskDetailsInfoModels.add(model);
//                        mSumReaminQty = ArithUtil.add(mSumReaminQty, ArithUtil.sub(model.getTaskQty1(), model.getScanQty()));
//                    }
//                }
//
//            }
//
//        }
//    }
//
//    /**
//     * @desc: 自动行分配
//     * @param:
//     * @return:
//     * @author:
//     * @time 2020/5/14 11:11
//     */
//    void AddSameLineMaterialNum(OutBarcodeInfo7 barCodeInfo) {
//        try {
//            boolean hasInsertBarcodeInfo = false;
//            float ScanReaminQty = barCodeInfo.getQty();
//            for (int i = 0; i < mSameLineInStockTaskDetailsInfoModels.size(); i++) {
//                if (ScanReaminQty == 0f) break;
//                InStockTaskDetailsInfo_Model model = mSameLineInStockTaskDetailsInfoModels.get(i);
//                if (model != null) {
//                    if (areaInfoModel != null && model.getAreaID() != areaInfoModel.getID() && model.getHouseID() != areaInfoModel.getHouseID()) {
//                        model.setAreaID(areaInfoModel.getID());
//                        model.setHouseID(areaInfoModel.getHouseID());
//                        model.setWarehouseID(areaInfoModel.getWarehouseID());
//                        model.setWareHouseNo(areaInfoModel.getWarehouseNo());
//                        model.setToErpAreaNo(areaInfoModel.getAreaNo());
//                        model.setAreaNo(areaInfoModel.getAreaNo());
//                        model.setToErpWarehouse(areaInfoModel.getWarehouseNo());
//                    }
//                    if (model.getLstStockInfo() == null) {
//                        model.setLstStockInfo(new ArrayList<OutBarcodeInfo7>());
//                    }
//
//                    Float remainQty = ArithUtil.sub(model.getTaskQty1(), model.getScanQty());
//                    Float addQty = remainQty < ScanReaminQty ? remainQty : ScanReaminQty;
//                    model.setScanQty(ArithUtil.add(model.getScanQty(), addQty));
//                    ScanReaminQty = ArithUtil.sub(ScanReaminQty, addQty);
//                    if (!hasInsertBarcodeInfo) {
//                        model.getLstStockInfo().add(0, barCodeInfo);
//                        hasInsertBarcodeInfo = true;
//
//                    }
//                    txtUpShelfNum.setText(model.getTaskQty1() + "");
//                    txtUpShelfScanNum.setText(model.getScanQty().intValue() + "");
//                    currentPickMaterialIndex = i;
//                }
//            }
//        } catch (Exception e) {
//            MessageBox.Show(context, "分配物料行出现预期之外的异常:" + e.getMessage());
//        }
//
//    }
//
//    /**
//     * @desc: 校验条码
//     * @param:
//     * @return:
//     * @author: Nietzsche
//     * @time 2020/5/14 11:41
//     */
//    boolean CheckBarcode(OutBarcodeInfo7 barCodeInfo) {
//        boolean isChecked = true;
//        FindSumQtyByMaterialNo(barCodeInfo.getMaterialNo(), "");
//        //校验条码的物料号和需求跟踪号是否在物料行中
//        if (mSameLineInStockTaskDetailsInfoModels == null || mSameLineInStockTaskDetailsInfoModels.size() == 0) {
//            MessageBox.Show(context, "校验条码失败：没有找到 物料编号:" + barCodeInfo.getMaterialNo() + "  ,需求跟踪号:[" + "]的物料行");
//            return false;
//        }
//        //校验条码对应的物料行是否已经拣货完成
//        if (mSumReaminQty == 0) {
//            MessageBox.Show(context, "校验条码失败： 物料编号:" + barCodeInfo.getMaterialNo() + "  ,需求跟踪号:[" + "]的物料行已扫描完毕！");
//            return false;
//        }
//        //校验条码是否超出物料行的待收货数量
//        if (mSumReaminQty < barCodeInfo.getQty()) {
//            MessageBox.Show(context, "校验条码失败： 物料编号:" + barCodeInfo.getMaterialNo() + "  ,需求跟踪号:[" + "]的物料行的待上架数量为:" + mSumReaminQty + ",条码数量为:" + barCodeInfo.getQty());
//            return false;
//        }
//        // 校验条码是否重复
//        for (InStockTaskDetailsInfo_Model model : mSameLineInStockTaskDetailsInfoModels) {
//            if (model.getLstStockInfo() == null)
//                model.setLstStockInfo(new ArrayList<OutBarcodeInfo7>());
//            final int barIndex = model.getLstStockInfo().indexOf(barCodeInfo);
//            if (barIndex != -1) {
//                MessageBox.Show(context, "校验条码失败：序列号:" + barCodeInfo.getSerialNo() + "已扫描");
//                return false;
//            }
//        }
//        return isChecked;
//    }
}
