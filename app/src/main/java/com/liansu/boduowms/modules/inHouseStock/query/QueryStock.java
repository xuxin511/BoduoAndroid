package com.liansu.boduowms.modules.inHouseStock.query;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.VoucherTypeInfo;
import com.liansu.boduowms.bean.stock.AreaInfo;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.modules.instock.batchPrint.order.BaseOrderLabelPrintSelect;
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.ui.adapter.inHouseStock.QueryStockDetailAdapter;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.log.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

/**
 * @ Des:  库存查询
 * @ Created by yangyiqing on 2020/8/23.
 */
@ContentView(R.layout.activity_query_stock)
public class QueryStock extends BaseActivity implements IQueryStockView, RadioGroup.OnCheckedChangeListener, IUserSettingView {
    Context mContext = QueryStock.this;
    @ViewInject(R.id.activity_query_stock_radio_group)
    RadioGroup   mRadioGroup;
    @ViewInject(R.id.activity_query_stock_material)
    RadioButton  mMaterialQuery;
    @ViewInject(R.id.activity_query_stock_batch_no)
    RadioButton  mBatchNoQuery;
    @ViewInject(R.id.activity_query_stock_area_no)
    RadioButton  mAreaNoQuery;
    @ViewInject(R.id.activity_query_stock_content)
    EditText     mContent;
    @ViewInject(R.id.activity_query_stock_recyclerView)
    RecyclerView mRecyclerView;
    @ViewInject(R.id.query_stock_select_qr_status_spinner)
    Spinner      mQRStatusTypeNameSpinner;
    ArrayAdapter            mQRStatusTypeNameArrayAdapter;
    QueryStockDetailAdapter mAdapter;
    public String TAG_SELECT_MATERIAL      = "QueryStock_SelectMaterial";  //获取物料信息
    public String TAG_GET_T_AREA_MODEL     = "QueryStock_GetT_AreaModel";  //获取库位信息
    public String TAG_GET_STOCK_INFO_LIST  = "QueryStock_GetT_AreaModel";  //获取库位信息
    public String TAG_GetT_StockList       = "QueryStock_TAG_GetT_StockList";  //库存查询
    public String TAG_GET_T_PARAMETER_LIST = "QueryStock_TAG_GetT_ParameterList";  //获取质检状态

    private final       int    RESULT_TAG_SELECT_MATERIAL      = 101;
    private final       int    RESULT_TAG_GET_T_AREA_MODEL     = 102;
    private final       int    RESULT_TAG_GET_STOCK_INFO_LIST  = 103;
    private final       int    RESULT_TAG_GET_T_STOCK_LIST     = 104;
    private final       int    RESULT_TAG_GET_T_PARAMETER_LIST = 105;
    public static final int    QUERY_TYPE_NONE                 = -1;
    public static final int    QUERY_TYPE_PALLET               = 1;
    public static final int    QUERY_TYPE_MATERIAL             = 2;
    public static final int    QUERY_TYPE_BATCH_NO             = 3;
    public static final int    QUERY_TYPE_AREA_NO              = 4;
    public static final String QUERY_TYPE_PALLET_NAME          = "托盘";
    public static final String QUERY_TYPE_MATERIAL_NAME        = "物料";
    public static final String QUERY_TYPE_BATCH_NO_NAME        = "批次";
    public static final String QUERY_TYPE_AREA_NO_NAME         = "库位";
    List<StockInfo>      mStockList       = new ArrayList<>();
    Map<String, Integer> mQRStatusTypeMap = new HashMap<>();
    private   List<VoucherTypeInfo> mQRStatusTypeList = new ArrayList<>();
    protected UserSettingPresenter  mUserSettingPresenter;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_TAG_SELECT_MATERIAL:
                resultMaterialInfoQuery((String) msg.obj);
                break;
            case RESULT_TAG_GET_T_AREA_MODEL:
                resultAreaInfoQuery((String) msg.obj);
                break;
            case RESULT_TAG_GET_STOCK_INFO_LIST:
                resultQueryStockInfo((String) msg.obj);
                break;
            case RESULT_TAG_GET_T_STOCK_LIST:
                resultQueryStockInfo((String) msg.obj);
                break;
            case RESULT_TAG_GET_T_PARAMETER_LIST:
                resultQRStatusInfo((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                MessageBox.Show(mContext, "获取请求失败_____" + msg.obj);
                break;

        }
    }


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(mContext.getResources().getString(R.string.app_bar_title_inventory_stock_query) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno(), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        mUserSettingPresenter = new UserSettingPresenter(mContext, this);
    }

    @Override
    public void bindListView(List<StockInfo> list) {
        if (mAdapter == null) {
            mAdapter = new QueryStockDetailAdapter(mContext, list);
            mAdapter.setRecyclerView(mRecyclerView);
            mAdapter.setOnItemClickListener(new QueryStockDetailAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView parent, View view, int position, StockInfo data) {

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
        CommonUtil.setEditFocus(mContent);
    }

    @Override
    public int getQueryType() {
        String value = "";
        int type = QUERY_TYPE_NONE;
        int count = mRadioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton rb = (RadioButton) mRadioGroup.getChildAt(i);
            if (rb.isChecked()) {
                value = rb.getText().toString().trim();
                break;
            }
        }
        if (value.equals(QUERY_TYPE_PALLET_NAME)) {
            type = QUERY_TYPE_PALLET;
        } else if (value.equals(QUERY_TYPE_MATERIAL_NAME)) {
            type = QUERY_TYPE_MATERIAL;
        } else if (value.equals(QUERY_TYPE_BATCH_NO_NAME)) {
            type = QUERY_TYPE_BATCH_NO;
        } else if (value.equals(QUERY_TYPE_AREA_NO_NAME)) {
            type = QUERY_TYPE_AREA_NO;
        }

        return type;
    }


    /**
     * @desc: 重置方法
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/23 22:23
     */
    @Override
    public void onReset() {
        mContent.setText("");
        onContentFocus();
        mStockList.clear();
        bindListView(mStockList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestQRStatusQuery();
    }

    /**
     * @desc: 订单号
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/7 12:45
     */
    @Event(value = {R.id.activity_query_stock_content}, type = View.OnKeyListener.class)
    private boolean onScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {

            switch (v.getId()) {
                case R.id.activity_query_stock_content:
                    onScan(mContent.getText().toString().trim(), getQueryType());
                    break;
            }

        }

        return false;
    }


    /**
     * @desc: 选择不同的查询方式  现在的接口不用自己解析，直接传接口就行了，这个方法暂时用不到。
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/23 22:07
     */
//    public void onScan(String content, int queryType) {
//        if (content.equals("")) return;
//        OutBarcodeInfo scanQRCode = null;
//        if (queryType == QUERY_TYPE_AREA_NO) {
//            AreaInfo areaInfo = new AreaInfo();
//            areaInfo.setAreano(content);
//            areaInfo.setWarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
//            requestAreaNo(areaInfo);
//        } else {
//
//            if (content.contains("%")) {
//                BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = QRCodeFunc.getQrCode(content);
//                if (resultInfo.getHeaderStatus()) {
//                    scanQRCode = resultInfo.getInfo();
//                } else {
//                    MessageBox.Show(mContext, "解析条码失败:" + resultInfo.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            onContentFocus();
//                        }
//                    });
//                    return;
//                }
//
//            }
//            if (queryType == QUERY_TYPE_ALL) {
//                String barcode = "";
//                if (scanQRCode != null) {
//                    barcode = scanQRCode.getBarcode() != null ? scanQRCode.getBarcode() : "";
//                } else {
//                    barcode = content;
//                }
//                StockInfo postInfo = new StockInfo();
//                requestQueryStockInfo(postInfo);
//            } else if (queryType == QUERY_TYPE_BATCH_NO) {
//                String batchNo = "";
//                if (scanQRCode != null) {
//                    batchNo = scanQRCode.getBatchno() != null ? scanQRCode.getBatchno() : "";
//                } else {
//                    batchNo = content;
//                }
//                if (!DateUtil.isValidDate(batchNo, "yyyyMMdd")) {
//                    MessageBox.Show(mContext, "校验日期格式失败:" + "日期格式不正确", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            onContentFocus();
//                        }
//                    });
//                    return;
//                }
//                StockInfo postInfo = new StockInfo();
//                requestQueryStockInfo(postInfo);
//            } else if (queryType == QUERY_TYPE_MATERIAL) {
//                String materialNo = "";
//                if (scanQRCode != null) {
//                    materialNo = scanQRCode.getMaterialno() != null ? scanQRCode.getMaterialno() : "";
//                } else {
//                    materialNo = content;
//                }
//                requestMaterialInfo(materialNo);
//            }
//        }
//
//    }


    /**
     * @desc: 选择不同的方式
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/23 22:07
     */
    public void onScan(String content, int queryType) {
        if (content.equals("")) return;
        StockInfo stockInfo = new StockInfo();
        stockInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
        stockInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        if (queryType == QUERY_TYPE_PALLET) {
            stockInfo.setBarcode(content);
        } else if (queryType == QUERY_TYPE_MATERIAL) {
            stockInfo.setMaterialno(content);
        } else if (queryType == QUERY_TYPE_BATCH_NO) {
            stockInfo.setBatchno(content);
        } else if (queryType == QUERY_TYPE_AREA_NO) {
            stockInfo.setAreano(content);
        }
        String statusName=mQRStatusTypeNameSpinner.getSelectedItem().toString().trim();
        if (statusName!=null && !statusName.equals("")){
            stockInfo.setStatus(mQRStatusTypeMap.get(statusName));
        }
        requestQueryStockInfo(stockInfo);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        onReset();
    }


    /**
     * @desc: 获取库位信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/27 21:37
     */
    public void requestAreaNo(AreaInfo info) {
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(QueryStock.class, TAG_GET_T_AREA_MODEL, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_AREA_MODEL, mContext.getString(R.string.Msg_GetAreaModelADF), mContext, mHandler, RESULT_TAG_GET_T_AREA_MODEL, null, UrlInfo.getUrl().GetT_AreaModel, modelJson, null);
    }


    /**
     * @desc: 库位查询返回结果
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/23 22:18
     */
    private void resultAreaInfoQuery(String result) {
        LogUtil.WriteLog(QueryStock.class, TAG_GET_T_AREA_MODEL, result);
        try {
            BaseResultInfo<AreaInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<AreaInfo>>() {
            }.getType());
            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                AreaInfo data = returnMsgModel.getData();
                if (data != null) {
                    StockInfo postInfo = new StockInfo();
                    requestQueryStockInfo(postInfo);
                } else {
                    MessageBox.Show(mContext, "获取的库位信息为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onContentFocus();
                        }
                    });

                }
            } else {
                MessageBox.Show(mContext, "获取的库位信息失败，" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onContentFocus();
                    }
                });

            }

        } catch (Exception ex) {
            MessageBox.Show(mContext, "获取的库位信息出现预期之外的异常，" + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onContentFocus();
                }
            });

        }
    }

    /**
     * @desc: 查询物料信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/23 22:41
     */
    public void requestMaterialInfo(String material) {
        String modelJson = parseModelToJson(material);
        LogUtil.WriteLog(QueryStock.class, TAG_SELECT_MATERIAL, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SELECT_MATERIAL, mContext.getString(R.string.Msg_GetT_SerialNoByPalletADF), mContext, mHandler, RESULT_TAG_SELECT_MATERIAL, null, UrlInfo.getUrl().SelectMaterial, modelJson, null);
    }


    /**
     * @desc: 正在获取质检类型
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/17 13:38
     */
    public void requestQRStatusQuery() {
        String modelJson = "{\"Groupname\":\"StockQuery_Status\"}";
        LogUtil.WriteLog(BaseOrderLabelPrintSelect.class, TAG_GET_T_PARAMETER_LIST, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_T_PARAMETER_LIST, mContext.getString(R.string.message_request_qr_status_info_query), mContext, mHandler, RESULT_TAG_GET_T_PARAMETER_LIST, null, UrlInfo.getUrl().GetT_ParameterList, modelJson, null);
    }

    /**
     * @desc: 质检类型返回结果
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/9/7 18:27
     */
    public void resultQRStatusInfo(String result) {
        LogUtil.WriteLog(BaseOrderLabelPrintSelect.class, TAG_GET_T_PARAMETER_LIST, result);
        try {
            BaseResultInfo<List<VoucherTypeInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<VoucherTypeInfo>>>() {
            }.getType());
            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                List<VoucherTypeInfo> list = returnMsgModel.getData();
                if (list != null && list.size() > 0) {
                    setQRStatusTypeList(list);
                    List<String> QRStatusNameList = getQRStatusNameList();
                    if (QRStatusNameList != null && QRStatusNameList.size() > 0) {
                        setSpinnerData(QRStatusNameList);
                    }
                } else {
                    MessageBox.Show(mContext, "获取单据类型失败:获取的单据类型集合为空" + returnMsgModel.getResultValue());

                }
            } else {
                MessageBox.Show(mContext, "获取单据类型失败:获取的单据类型集合为空" + returnMsgModel.getResultValue());

            }


        } catch (Exception ex) {
            MessageBox.Show(mContext, "获取单据类型失败:出现预期之外的异常-" + ex.getMessage());

        }
    }

    /**
     * @desc: 物料查询返回结果
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/23 22:13
     */
    private void resultMaterialInfoQuery(String result) {
        try {
            LogUtil.WriteLog(QueryStock.class, TAG_SELECT_MATERIAL, result);
            BaseResultInfo<OutBarcodeInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutBarcodeInfo>>() {
            }.getType());
            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                OutBarcodeInfo materialInfo = returnMsgModel.getData();
                if (materialInfo != null) {
                    StockInfo stockInfo = new StockInfo();
                    requestQueryStockInfo(stockInfo);
                }
            } else {
                MessageBox.Show(mContext, "查询物料失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onContentFocus();
                    }
                });
                return;
            }
        } catch (Exception e) {
            MessageBox.Show(mContext, "查询物料失败:出现预期之外的异常-" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onContentFocus();
                }
            });

        }
    }

    /**
     * @desc: 查询库存信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/23 22:37
     */
    public void requestQueryStockInfo(StockInfo info) {
        String modelJson = parseModelToJson(info);
        LogUtil.WriteLog(QueryStock.class, TAG_SELECT_MATERIAL, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GET_STOCK_INFO_LIST, mContext.getString(R.string.query_stock_request_stock_info_list), mContext, mHandler, RESULT_TAG_GET_STOCK_INFO_LIST, null, UrlInfo.getUrl().GetT_StockList, modelJson, null);
    }

    /**
     * @desc: 查询库存返回结果
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/23 22:38
     */
    public void resultQueryStockInfo(String result) {
        LogUtil.WriteLog(QueryStock.class, TAG_GET_STOCK_INFO_LIST, result);
        try {
            BaseResultInfo<List<StockInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<StockInfo>>>() {
            }.getType());
            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                List<StockInfo> data = returnMsgModel.getData();
                if (data != null) {
                    mStockList.clear();
                    mStockList.addAll(data);
                    bindListView(mStockList);
                    onContentFocus();
                } else {
                    MessageBox.Show(mContext, "查询的库存信息为空:", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onContentFocus();
                            mStockList.clear();
                            bindListView(mStockList);
                        }
                    });

                }
            } else {
                MessageBox.Show(mContext, "查询的库存信息失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onContentFocus();
                        mStockList.clear();
                        bindListView(mStockList);
                    }
                });

            }

        } catch (Exception ex) {
            MessageBox.Show(mContext, "查询的库存信息失败,出现预期之外的异常，" + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onContentFocus();
                    mStockList.clear();
                    bindListView(mStockList);
                }
            });

        }


    }

    @Override
    public void selectWareHouse(List<String> list) {
        if (list != null && list.size() > 0) {
            final String[] items = list.toArray(new String[0]);
            new AlertDialog.Builder(mContext).setTitle(getResources().getString(R.string.activity_login_WareHousChoice))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setCancelable(false)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法存根
                            String select_item = items[which].toString();
                            if (mUserSettingPresenter != null) {
                                mUserSettingPresenter.saveCurrentWareHouse(select_item);
                            }

                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public void setTitle() {
        getToolBarHelper().getToolBar().setTitle(mContext.getResources().getString(R.string.app_bar_title_inventory_stock_query) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_setting_warehouse_select) {
            selectWareHouse(mUserSettingPresenter.getModel().getWareHouseNoList());
        }
        return false;
    }


    public void setQRStatusTypeList(List<VoucherTypeInfo> voucherTypeList) {
        mQRStatusTypeList.clear();
        mQRStatusTypeMap.clear();
        if (voucherTypeList != null && voucherTypeList.size() > 0) {
            mQRStatusTypeList.addAll(voucherTypeList);
            setQRStatusTypeMap(mQRStatusTypeList);
        }

    }

    public void setQRStatusTypeMap(List<VoucherTypeInfo> voucherTypeList) {
        for (VoucherTypeInfo info : voucherTypeList) {
            mQRStatusTypeMap.put(info.getParametername(), info.getParameterid());
        }

    }

    /**
     * @desc: 获取托盘名称
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 13:59
     */
    public List<String> getQRStatusNameList() {
        List<String> list = new ArrayList<>();
//        list.add(ORDER_TYPE_NONE);
        for (String key : mQRStatusTypeMap.keySet()) {
            if (!list.contains(key)) {
                if (key.contains("所有")){
                    list.add(0,key);
                }else {
                    list.add(key);
                }


            }

        }
        return list;
    }

    public void setSpinnerData(List<String> list) {
        if (list == null || list.size() == 0) {
            if (mQRStatusTypeNameSpinner.getVisibility() != View.GONE) {
                mQRStatusTypeNameSpinner.setVisibility(View.GONE);
            }
            return;
        } else {
            if (mQRStatusTypeNameSpinner.getVisibility() != View.VISIBLE) {
                mQRStatusTypeNameSpinner.setVisibility(View.VISIBLE);
            }

            mQRStatusTypeNameArrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            mQRStatusTypeNameArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉风格
            mQRStatusTypeNameSpinner.setAdapter(mQRStatusTypeNameArrayAdapter); // 将adapter 添加到spinner中
            mQRStatusTypeNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(
            ) {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });// 添加监听
        }

    }

    /**
     * @desc: 获取订单类型
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 10:51
     */
    public int getQRStatusType(String statusName) {
        return mQRStatusTypeMap.get(statusName);
    }
}
