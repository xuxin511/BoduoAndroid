package com.liansu.boduowms.modules.inHouseStock.adjustStock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OrderType;
import com.liansu.boduowms.bean.stock.StockInfo;
import com.liansu.boduowms.modules.inHouseStock.query.QueryStock;
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.CommonUtil;
import com.liansu.boduowms.utils.function.DateUtil;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.log.LogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Arrays;
import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_NONE;
import static com.liansu.boduowms.utils.function.GsonUtil.parseModelToJson;

@ContentView(R.layout.activity_adjust_stock)
public class AdjustStock extends BaseActivity implements IAdjustStockView, IUserSettingView {

    String TAG_UPDATE_T_STOCK_ADJUST                   = "AdjustStock_UpdateT_StockAdjust"; // 库存调整提交
    String TAG_ADJUST_STOCK_GET_T_SCAN_STOCK_ADF_ASYNC = "AdjustStock_GetT_ScanStockADFAsync"; //库存调整托盘扫描
    private final int RESULT_ADJUST_STOCK_GET_T_SCAN_STOCK_ADF_ASYNC = 101;
    private final int RESULT_UPDATE_T_STOCK_ADJUST                   = 102;

    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_ADJUST_STOCK_GET_T_SCAN_STOCK_ADF_ASYNC:
                resultQueryPalletInfo((String) msg.obj);
                break;
            case RESULT_UPDATE_T_STOCK_ADJUST:
                resultUpdateStockInfo((String) msg.obj);
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                MessageBox.Show(mContext, "获取请求失败_____" + msg.obj, MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
        }
    }

    protected UserSettingPresenter mUserSettingPresenter;
    Context mContext = AdjustStock.this;
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
    String[]  QCStatus = {"待检", "合格"};
    public final static int QC_STATUS_TYPE_PENDING_QUALITY_INSPECTION = 1;
    public final static int QC_STATUS_TYPE_QUALIFIED                  = 3;


    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        BaseApplication.toolBarTitle = new ToolBarTitle(getToolBarTitle(), false);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;
        mCurrentStockInfo = null;
        onReset();
        mUserSettingPresenter = new UserSettingPresenter(mContext, this);

    }


    @Event(value = R.id.edt_AdjustScanBarcode, type = View.OnKeyListener.class)
    private boolean onBarcodeScan(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
            String barcode = mBarcode.getText().toString().trim();
            if (!barcode.equals("")) {
                requestQueryPalletInfo(barcode);
            }
        }
        return false;
    }


    @Event(value = R.id.txt_QCStatus, type = View.OnClickListener.class)
    private void onQCStatusClick(View view) {
        if (mCurrentStockInfo != null) {
            onQCStatusSelect(Arrays.asList(QCStatus));
        }

    }


    @Event(value = {R.id.btn_Delete, R.id.btn_Submit}, type = View.OnClickListener.class)
    private void onRefer(View view) {
        boolean isBtnDelete = R.id.btn_Delete == view.getId();
        final float qty = Float.parseFloat(mQty.getText().toString());
        final String batchNo = mBatchNo.getText().toString().trim();
        final String QCStatus = mQCStatus.getText().toString();
        int QCStatusType = -1;
        if (QCStatus.contains("待检")) {
            QCStatusType = QC_STATUS_TYPE_PENDING_QUALITY_INSPECTION;
        } else if (QCStatus.contains("合格")) {
            QCStatusType = QC_STATUS_TYPE_QUALIFIED;
        }
        StockInfo checkInfo = new StockInfo();
        checkInfo.setBatchno(batchNo);
        checkInfo.setQty(qty);
        //校验数据     成功给条码赋值,不成功报错
        if (!checkBarcodeInfoRefer(checkInfo)) {
            return;
        } else {
            if (isBtnDelete) {
                mCurrentStockInfo.setQty(0);
            } else {
                mCurrentStockInfo.setQty(qty);
            }
            mCurrentStockInfo.setBatchno(batchNo);
            mCurrentStockInfo.setStatus(QCStatusType);
            mCurrentStockInfo.setVouchertype(OrderType.IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY_ADJUSTMENT_VALUE);
        }
//        mCurrentStockInfo.setModifytime(null);
        if (isBtnDelete) {
            new AlertDialog.Builder(mContext).setCancelable(false).setTitle("提示").setIcon(android.R.drawable.ic_dialog_info).setMessage("是否删除库存？\n" + mCurrentStockInfo.getBarcode())
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestUpdateStockInfo(mCurrentStockInfo);
                        }
                    }).setNegativeButton("取消", null).show();
        } else {
            requestUpdateStockInfo(mCurrentStockInfo);
        }
    }


    /**
     * @desc: 获取质检状态名称
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/25 14:13
     */
    String getQCStrStatus(int status) {
        String QCStaatus = "";
        switch (status) {
            case 1:
                QCStaatus = QCStatus[0];
                break;
            case 3:
                QCStaatus = QCStatus[1];
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
        CommonUtil.setEditFocus(mQty);
    }

    @Override
    public void setStockInfo(StockInfo stockInfo) {
        if (stockInfo != null) {
            mMaterialDesc.setText(stockInfo.getMaterialdesc());
            mQCStatus.setText(getQCStrStatus(stockInfo.getStatus()));
            mWareHouseName.setText(stockInfo.getTowarehouseno());
            mStrongHoldName.setText(stockInfo.getStrongholdname() + "(" + stockInfo.getStrongholdcode() + ")");
            mAreaNo.setText(stockInfo.getAreano());
            mEDate.setText(stockInfo.getEdate());
            mBatchNo.setText(stockInfo.getBatchno());
            mQty.setText(stockInfo.getQty() + "");
        } else {
            mMaterialDesc.setText("");
            mQCStatus.setText("");
            mWareHouseName.setText("");
            mStrongHoldName.setText("");
            mAreaNo.setText("");
            mEDate.setText("");
            mBatchNo.setText("");
            mQty.setText("0");
        }
    }

    @Override
    public void onReset() {
        mBarcode.setText("");
        mCurrentStockInfo=null;
        setStockInfo(null);
        onBarcodeFocus();
    }

    @Override
    public void onQCStatusSelect(List<String> QCStatusList) {
        if (QCStatusList != null && QCStatusList.size() > 0) {
            final String[] items = QCStatusList.toArray(new String[0]);
            new AlertDialog.Builder(mContext).setTitle(getResources().getString(R.string.activity_adjust_select_qc_status))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setCancelable(false)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String select_item = items[which].toString();
                            mQCStatus.setText(select_item);
                            dialog.dismiss();
                        }
                    }).show();
        }
    }


    /**
     * @desc: 查询托盘条码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/25 10:09
     */
    public void requestQueryPalletInfo(String barcode) {
        StockInfo stockInfo = new StockInfo();
        stockInfo.setBarcode(barcode);
        stockInfo.setVouchertype(OrderType.IN_HOUSE_STOCK_ORDER_TYPE_INVENTORY_ADJUSTMENT_VALUE);
        stockInfo.setTowarehouseno(BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
        stockInfo.setTowarehouseid(BaseApplication.mCurrentWareHouseInfo.getId());
        String modelJson = parseModelToJson(stockInfo);
        LogUtil.WriteLog(AdjustStock.class, TAG_ADJUST_STOCK_GET_T_SCAN_STOCK_ADF_ASYNC, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_ADJUST_STOCK_GET_T_SCAN_STOCK_ADF_ASYNC, mContext.getString(R.string.activity_adjust_request_pallet_info), mContext, mHandler, RESULT_ADJUST_STOCK_GET_T_SCAN_STOCK_ADF_ASYNC, null, UrlInfo.getUrl().AdjustStockGetT_ScanStockADFAsync, modelJson, null);

    }

    /**
     * @desc: 查询托盘条码返回结果
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/25 10:23
     */
    public void resultQueryPalletInfo(String result) {
        LogUtil.WriteLog(QueryStock.class, TAG_ADJUST_STOCK_GET_T_SCAN_STOCK_ADF_ASYNC, result);
        try {
            BaseResultInfo<List<StockInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<StockInfo>>>() {
            }.getType());
            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                List<StockInfo> data = returnMsgModel.getData();
                if (data != null && data.size()>0) {
                    if (data.size()==1){
                        mCurrentStockInfo = data.get(0);
                        setStockInfo(mCurrentStockInfo);
                        onBatchNoFocus();
                    }else if (data.size()>1){
                        MessageBox.Show(mContext, "不能对混托的托盘进行库存调整:", MessageBox.MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBarcodeFocus();
                            }
                        });
                    }


                } else {
                    MessageBox.Show(mContext, "查询的库存信息为空:", MessageBox.MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBarcodeFocus();
                        }
                    });

                }
            } else {
                MessageBox.Show(mContext, "查询的库存信息失败:" + returnMsgModel.getResultValue(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBarcodeFocus();
                    }
                });

            }

        } catch (Exception ex) {
            MessageBox.Show(mContext, "查询的库存信息失败,出现预期之外的异常，" + ex.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBarcodeFocus();
                }
            });

        }
    }

    /**
     * @desc: 保存库存信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/25 15:18
     */
    public void requestUpdateStockInfo(StockInfo stockInfo) {
        String modelJson = parseModelToJson(stockInfo);
        LogUtil.WriteLog(AdjustStock.class, TAG_UPDATE_T_STOCK_ADJUST, modelJson);
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_UPDATE_T_STOCK_ADJUST, mContext.getString(R.string.activity_adjust_request_save_pallet_info), mContext, mHandler, RESULT_UPDATE_T_STOCK_ADJUST, null, UrlInfo.getUrl().UpdateT_StockAdjust, modelJson, null);

    }

    /**
     * @desc: 保存库存信息返回结果
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/25 15:26
     */
    public void resultUpdateStockInfo(String result) {
        LogUtil.WriteLog(AdjustStock.class, TAG_UPDATE_T_STOCK_ADJUST, result);
        try {
            BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
            }.getType());
            if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                MessageBox.Show(mContext, returnMsgModel.getResultValue(), MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onReset();
                    }
                });

            } else {
                MessageBox.Show(mContext, "保存库存信息失败:" + returnMsgModel.getResultValue(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBarcodeFocus();
                    }
                });
            }

        } catch (Exception ex) {
            MessageBox.Show(mContext, "保存库存信息失败:出现预期之外的异常," + ex.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBarcodeFocus();
                }
            });
        }


    }

    /**
     * @desc: 提交库存调整时校验托盘信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/25 14:45
     */
    public boolean checkBarcodeInfoRefer(StockInfo stockInfo) {
        try {
            if (mCurrentStockInfo == null) {
                MessageBox.Show(mContext, "校验托盘信息失败:托盘数据不能为空,请扫描托盘码", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBarcodeFocus();
                    }
                });
                return false;
            }
            String batchNo = stockInfo.getBatchno();
            if (batchNo.equals("")) {
                MessageBox.Show(mContext, "校验托盘信息失败:批次不能为空", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBarcodeFocus();
                    }
                });
            }

            if (!DateUtil.isBeforeOrCompareToday(batchNo.trim(), "yyyyMMdd")) {
                MessageBox.Show(mContext, "校验日期格式失败:" + "日期格式不正确或日期大于今天", MessageBox.MEDIA_MUSIC_NONE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBatchNoFocus();
                    }
                });
                return false;
            }
            float qty = stockInfo.getQty();
            if (qty < 0) {
                MessageBox.Show(mContext, "数量必须大于等于0", MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onQtyFocus();
                    }
                });
                return false;
            }


        } catch (Exception e) {
            MessageBox.Show(mContext, "校验托盘信息出现预期之外的异常:" + e.getMessage(), MessageBox.MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBarcodeFocus();
                }
            });
            return false;
        }


        return true;

    }

    @Override
    public void selectWareHouse(List<String> list) {
        if (list != null && list.size() > 0) {
            final String[] items = list.toArray(new String[0]);
            new AlertDialog.Builder(mContext).setTitle(getResources().getString(R.string.activity_login_WareHousChoice))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setCancelable(true)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法存根
                            String select_item = items[which].toString();
                            if (mUserSettingPresenter != null) {
                                mUserSettingPresenter.saveCurrentWareHouse(select_item);
                            }
                            onReset();
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public void setTitle() {
        getToolBarHelper().getToolBar().setTitle(mContext.getString(R.string.app_bar_title_inventory_adjustment) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno());

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
}
