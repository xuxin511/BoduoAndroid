package com.liansu.boduowms.modules.inHouseStock.adjustStock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.log.LogUtil;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_adjust_stock)
public class AdjustStock extends BaseActivity implements  IAdjustStockView{

    String TAG_GetInfoBySerial = "AdjustStock_GetInfoBySerial";
    String TAG_SaveInfo        = "AdjustStock_SaveInfo";
    private final int RESULT_GetWareHouse    = 101;
    private final int RESULT_GetInfoBySerial = 102;
    private final int RESULT_SaveInfo        = 103;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_GetWareHouse:
             
                break;
            case RESULT_GetInfoBySerial:
                AnalysisGetInfoBySerialJson((String) msg.obj);
                break;
            case RESULT_SaveInfo:
                AnalysisSaveInfoJson((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;
        }
    }


    Context context = AdjustStock.this;
    @ViewInject(R.id.edt_AdjustScanBarcode)
    EditText mBarcode;
    @ViewInject(R.id.txt_material_no_desc)
    TextView mMaterialDesc;
    @ViewInject(R.id.txt_QCStatus)
    TextView mQCStatus;
    @ViewInject(R.id.txt_Warehouse)
    TextView mWareHouseName;
    @ViewInject(R.id.txt_StrongHold)
    TextView mStrongHoldName;
    @ViewInject(R.id.txt_area_no)
    TextView mAreaNo;
    @ViewInject(R.id.txt_changeEData)
    TextView mEDate;
    @ViewInject(R.id.edt_AdjustBatchNo)
    EditText mBatchNo;
    @ViewInject(R.id.edt_AdjustNum)
    EditText mQty;
    @ViewInject(R.id.btn_Delete)
    TextView btnDelete;
    @ViewInject(R.id.btn_Submit)
    TextView btnSubmit;

    StockInfo mCurrentStockInfo;
    String[]  QCStatus       = {"待检", "检验合格", "检验不合格"};
    int[]     QCStatusType   = {1, 3, 4};


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.app_bar_title_inventory_adjustment)+ "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename(), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        mCurrentStockInfo = null;
    }




    @Event(value = R.id.edt_AdjustScanBarcode, type = View.OnKeyListener.class)
    private boolean onBarcodeScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String barcode = mBarcode.getText().toString().trim();
            if (!barcode.equals("")) {
                final Map<String, String> params = new HashMap<String, String>();
                params.put("barcode", barcode);
                String para = (new JSONObject(params)).toString();
                LogUtil.WriteLog(AdjustStock.class, TAG_GetInfoBySerial, para);
//                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_GetInfoBySerial, getString(R.string.Msg_GetT_SerialNoByPalletADF), context, mHandler, RESULT_GetInfoBySerial, null, URLModel.GetURL().GetInfoBySerial, params, null);
            }
        }
        return false;
    }


    @Event(value = R.id.txt_QCStatus, type = View.OnClickListener.class)
    private void txtQCStatusClick(View view) {
//        if (barcodeModel != null) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("选择质检状态");
//            builder.setCancelable(false);
//            builder.setItems(QCStatus, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    txtQCStatus.setText(QCStatus[which]);
//                    barcodeModel.setSTATUS(QCStatusType[which]);
//                }
//            });
//            builder.show();
//        }
    }



    @Event(value = {R.id.btn_Delete, R.id.btn_Submit}, type = View.OnClickListener.class)
    private void btnDelete(View view) {
        boolean isBtnDelete = R.id.btn_Delete == view.getId();
        if (mCurrentStockInfo != null) {
            if (isBtnDelete) {
                //                barcodeModel.setAllIn("2");
            }

            String adjustBatchNo = mBatchNo.getText().toString();
            String adjustNum = mQty.getText().toString();
//            String adjustStock = edtAdjustStock.getText().toString();
            if (!isBtnDelete) {
                if (TextUtils.isEmpty(adjustBatchNo)) {
                    MessageBox.Show(context, getString(R.string.Error_BatchNoIsEmpty));
                    CommonUtil.setEditFocus(mBatchNo);
                    return;
                }
//                if (TextUtils.isEmpty(adjustStock)) {
//                    MessageBox.Show(context, getString(R.string.Error_StockIsEmpty));
//                    CommonUtil.setEditFocus(edtAdjustStock);
//                    return;
//                }
                if (!CommonUtil.isFloat(adjustNum)) {
                    MessageBox.Show(context, getString(R.string.Error_isnotnum));
                    CommonUtil.setEditFocus(mQty);
                    return;
                }
                if (Float.parseFloat(adjustNum) == 0f) {
                    MessageBox.Show(context, getString(R.string.Error_isnotzero));
                    CommonUtil.setEditFocus(mQty);
                    return;
                }
            }
            mCurrentStockInfo.setBatchno(adjustBatchNo);
//            mCurrentStockInfo.setAreano(adjustStock);
//            mCurrentStockInfo.setQty(Float.parseFloat(adjustNum));
//            mCurrentStockInfo.setEdate(CommonUtil.dateStrConvertDate(txtchangeEData.getText().toString()).toString());

            ArrayList<StockInfo> barcodeModels = new ArrayList<>();
            barcodeModels.add(mCurrentStockInfo);
            final Map<String, String> params = new HashMap<String, String>();
            String ModelJson = GsonUtil.parseModelToJson(barcodeModels);
            params.put("json", ModelJson);
            params.put("man", BaseApplication.mCurrentUserInfo.getUserno());
            String para = (new JSONObject(params)).toString();
            LogUtil.WriteLog(AdjustStock.class, TAG_SaveInfo, para);
            if (isBtnDelete) {
                new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除物料？\n" + mCurrentStockInfo.getBarcode())
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO 自动生成的方法
//                                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveInfo, getString(R.string.Msg_AdjustStockSubmit), context, mHandler, RESULT_SaveInfo, null, URLModel.GetURL().SaveInfo, params, null);
                            }
                        }).setNegativeButton("取消", null).show();
            } else {
//                RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_SaveInfo, getString(R.string.Msg_AdjustStockSubmit), context, mHandler, RESULT_SaveInfo, null, URLModel.GetURL().SaveInfo, params, null);

            }
        }

    }




    void AnalysisGetInfoBySerialJson(String result) {
        try {
//            LogUtil.WriteLog(AdjustStock.class, TAG_GetWareHouse, result);
//            ReturnMsgModelList<Barcode_Model> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<ReturnMsgModelList<Barcode_Model>>() {
//            }.getType());
//            if (returnMsgModel.getHeaderStatus().equals("S")) {
//                ArrayList<Barcode_Model> barcodeModels = returnMsgModel.getModelJson();
//                if (barcodeModels != null && barcodeModels.size() != 0) {
//                    barcodeModel = barcodeModels.get(0);
//                    txtCompany.setText(barcodeModel.getStrongHoldName());
//                    txtBatch.setText(barcodeModel.getBatchNo());
//                    txtStatus.setText("");
//                    txtEDate.setText(barcodeModel.getEds());
//                    txtMaterialName.setText(barcodeModel.getMaterialDesc());
//                    txtStrongHold.setText(barcodeModel.getStrongHoldName());
//                    edtAdjustBatchNo.setText(barcodeModel.getBatchNo());
//                    edtAdjustNum.setText(barcodeModel.getQty() + "");
//                    txtchangeEData.setText(barcodeModel.getEds());
//                    txtQCStatus.setText(getQCStrStatus(barcodeModel.getSTATUS()));
//                    txtWarehouse.setText(barcodeModel.getWarehousename());
//                    edtAdjustStock.setText(barcodeModel.getAreano());
//                    boolean isInsert = barcodeModel.getAllIn().equals("0");
//                    txtStrongHold.setEnabled(!isInsert);
//                    edtAdjustBatchNo.setEnabled(!isInsert);
//                    edtAdjustNum.setEnabled(!isInsert);
//                }
//            } else
//                MessageBox.Show(context, returnMsgModel.getMessage());
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
        CommonUtil.setEditFocus(mBarcode);
    }

    void AnalysisSaveInfoJson(String result) {
        try {
            LogUtil.WriteLog(AdjustStock.class, TAG_SaveInfo, result);
            BaseResultInfo<StockInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<StockInfo> >() {
            }.getType());
            if (returnMsgModel.getResult()==BaseResultInfo.RESULT_TYPE_OK) {
                MessageBox.Show(context, returnMsgModel.getResultValue());
                mCurrentStockInfo = null;
                mBarcode.setText("");
//                edtAdjustStock.setText("");
//                mBatchNo.setText("");
//                mQty.setText("");
//                txtCompany.setText("");
//                txtBatch.setText("");
//                txtStatus.setText("");
//                txtEDate.setText("");
//                txtchangeEData.setText("");
//                txtMaterialName.setText("");
                mQCStatus.setText("");
                mWareHouseName.setText("");
                mStrongHoldName.setText("");
                mStrongHoldName.setEnabled(true);
                mBatchNo.setEnabled(true);
                mQty.setEnabled(true);

            } else
                MessageBox.Show(context, returnMsgModel.getResultValue());
        } catch (Exception ex) {
            MessageBox.Show(context, ex.getMessage());
        }
        CommonUtil.setEditFocus(mBarcode);
    }

    String getQCStrStatus(int status) {
        String QCStaatus = "";
        switch (status) {
            case 1:
                QCStaatus = QCStatus[0];
                break;
            case 3:
                QCStaatus = QCStatus[1];
                break;
            case 4:
                QCStaatus = QCStatus[2];
                break;
        }
        return QCStaatus;
    }


    @Override
    public void onBarcodeFocus() {
        CommonUtil.setEditFocus(mBarcode);
    }

    @Override
    public void onBatchNoFocus() {
        CommonUtil.setEditFocus(mBatchNo);
    }

    @Override
    public void onQtyFocus() {
        CommonUtil.setEditFocus(mBarcode);
    }

    @Override
    public void setStockInfo(StockInfo stockInfo) {
       if (stockInfo!=null){
           mMaterialDesc.setText(stockInfo.getMaterialdesc());
           mQCStatus.setText(stockInfo.getStatus());
       }else {
           mMaterialDesc.setText("");
       }
    }

    @Override
    public void onReset() {

    }

    @Override
    public void onQCStatusSelect(List<String> QCStatusList) {

    }
}
