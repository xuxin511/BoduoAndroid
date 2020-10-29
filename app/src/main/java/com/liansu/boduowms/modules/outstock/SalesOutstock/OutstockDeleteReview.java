package com.liansu.boduowms.modules.outstock.SalesOutstock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;
import com.liansu.boduowms.modules.outstock.Model.MenuOutStockModel;
import com.liansu.boduowms.modules.outstock.Model.OutStockDeleteReviewAdapter;
import com.liansu.boduowms.modules.outstock.Model.OutStockDeleteReviewModel;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockRequery;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.function.GsonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_GETBOXlISTl;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESULT_Saleoutstock_ReviewOrder;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.RESUL_Outstock_DeleteMaterial;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Outstock_DeleteMaterial;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_GETBOXlIST;
import static com.liansu.boduowms.modules.outstock.Model.OutStock_Tag.TAG_Saleoutstock_ReviewOrder;
import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;

//删除复核记录
@ContentView(R.layout.activity_outstock_delete_review)
public class OutstockDeleteReview extends BaseActivity {
    Context context = OutstockDeleteReview.this;

    //列表
    @ViewInject(R.id.outstock_deletereview_ListView)
    ListView mList;

    //单选框
    @ViewInject(R.id.outstock_deletereview_radiogroup)
    RadioGroup radioGroup;

    //复核单号
    @ViewInject(R.id.outstock_deletereivew_order)
    TextView orderText;

    //  适配器
    OutStockDeleteReviewAdapter mAdapter;


    //存储类型
    List<OutStockDeleteReviewModel> modelList = new ArrayList<OutStockDeleteReviewModel>();

    //当前选中的列表
    OutStockDeleteReviewModel mModel = new OutStockDeleteReviewModel();

    //当前删除类型
    private int DeleteType;

    private String CurrOrder="";

    private int CurrvoucherType;

    UrlInfo info = new UrlInfo();
    MenuOutStockModel model = new MenuOutStockModel();

    @Override
    protected void initViews() {
        super.initViews();
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        String arr=data.toString();
        model = GsonUtil.parseJsonToModel(arr, MenuOutStockModel.class);

        BaseApplication.context = context;
        BaseApplication.toolBarTitle = new ToolBarTitle(model.Title, true);
        x.view().inject(this);
        BaseApplication.isCloseActivity = false;


        //加载数据

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //托盘框获取焦点
                switch (checkedId) {
                    case R.id.radio_deletereview_materialno://选择物料删除
                        DeleteType = 1;
                        break;
                    case R.id.radio_deletereview_packagecarton://选择拼箱码删除
                        DeleteType = 2;
                        break;

                }
                InitOrder(CurrOrder, DeleteType);
            }
        });

        //选择listview
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OutStockDeleteReviewModel model = modelList.get(i);
                mModel = model;
                for (OutStockDeleteReviewModel item : modelList) {
                    if (item == model) {
                        item.IsCheck = true;

                    } else {
                        item.IsCheck = false;
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                OutStockDeleteReviewModel model = modelList.get(position);
                mModel = model;
                IsDel(model);
                return true;
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        int type = Integer.parseInt(model.VoucherType);
        info.InitUrl(type);
        CurrvoucherType = type;
        modelList = new ArrayList<OutStockDeleteReviewModel>();
        CurrOrder = model.ErpVoucherNo;
        orderText.setText(model.ErpVoucherNo);
        DeleteType = 1;
        if (type != 29) {
            radioGroup.setVisibility(View.INVISIBLE);
        }
        InitOrder(CurrOrder, DeleteType);
    }


    //删除
    @Event(value =R.id.outstock_deletereview_button)
    private void  Click_delete(View view) {
        if (mModel == null) {
            MessageBox.Show(context, "请选中列表行删除");
            return;
        }
        IsDel(mModel);
    }




    @Override
    public void onHandleMessage(Message msg) {
        switch (msg.what) {
            case RESULT_Saleoutstock_ReviewOrder://获取复核数据
                LoadReviewData((String) msg.obj);
                break;
            case RESULT_Saleoutstock_GETBOXlISTl://获取拼箱复核数据
                LoadPackagecartonData((String) msg.obj);
                break;
            case RESUL_Outstock_DeleteMaterial://获取拼箱复核数据
                DeleteMaterial((String) msg.obj);
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


    //加载复核物料数据
    public void LoadReviewData(String result) {
        try {
            BaseResultInfo<OutStockOrderHeaderInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<OutStockOrderHeaderInfo>>() {
            }.getType());
            modelList = new ArrayList<OutStockDeleteReviewModel>();
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                mAdapter = new OutStockDeleteReviewAdapter(context, modelList);
                mList.setAdapter(mAdapter);
                //散件类
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            int i = 0;
            for (OutStockOrderDetailInfo item : returnMsgModel.getData().getDetail()) {
                if (item.getScanqty() != 0) {
                    OutStockDeleteReviewModel model = new OutStockDeleteReviewModel();
                    model.Materialno = item.getMaterialno();
                    model.Materialdesc = item.getMaterialdesc();
                    model.ScanQty = item.getScanqty();
                    model.DeleteType = 1;
                    if (i == 0) {
                        model.IsCheck = true;
                        mModel =model;
                    }
                    modelList.add(model);
                    i++;
                }
            }
            //绑定
            mAdapter = new OutStockDeleteReviewAdapter(context, modelList);
            mList.setAdapter(mAdapter);
        } catch (Exception ex) {
            MessageBox.Show(context, "数据解析报错");
        }
    }

    //加载拼箱复核数据
    public void LoadPackagecartonData(String result) {
        try {
            BaseResultInfo<List<OutStockDeleteReviewModel>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<OutStockDeleteReviewModel>>>() {
            }.getType());
            modelList = new ArrayList<OutStockDeleteReviewModel>();
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                mAdapter = new OutStockDeleteReviewAdapter(context, modelList);
                mList.setAdapter(mAdapter);
                MessageBox.Show(context, returnMsgModel.getResultValue());
                return;
            }
            int i = 0;
            for (OutStockDeleteReviewModel item : returnMsgModel.getData()) {
                item.DeleteType = 2;
                if (i == 0) {
                    item.IsCheck = true;
                    mModel = item;
                }
                modelList.add(item);
                i++;
            }
            mAdapter = new OutStockDeleteReviewAdapter(context, modelList);
            mList.setAdapter(mAdapter);
        } catch (Exception ex) {
            MessageBox.Show(context, "数据解析报错");
        }
    }

    public void DeleteMaterial(String result) {
        try {
            BaseResultInfo<String> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<String>>() {
            }.getType());
            modelList = new ArrayList<OutStockDeleteReviewModel>();
            if (returnMsgModel.getResult() != returnMsgModel.RESULT_TYPE_OK) {
                MessageBox.Show(context, returnMsgModel.getResultValue());
            }
            mModel = new OutStockDeleteReviewModel();
          //  MessageBox.Show(context, "删除成功");
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            //重新获取信息
            InitOrder(CurrOrder, DeleteType);
        } catch (Exception ex) {
            MessageBox.Show(context, "数据解析报错");
        }
    }


    private void InitOrder(String order, int type) {
        if (type == 1) {//查询物料
            SalesoutstockRequery model = new SalesoutstockRequery();
            model.Erpvoucherno = CurrOrder;
            model.Towarehouseno = BaseApplication.mCurrentWareHouseInfo.Warehouseno;
            model.Vouchertype = CurrvoucherType;
            String json = GsonUtil.parseModelToJson(model);
            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_ReviewOrder, "获取物料复核列表",
                    context, mHandler, RESULT_Saleoutstock_ReviewOrder, null, info.SalesOutstock_Review_ScanningNo, json, null);
        } else {//查询拼箱
            Map<String, String> map = new HashMap<>();
            map.put("ErpVoucherNo", CurrOrder);
            map.put("Towarehouseno", BaseApplication.mCurrentWareHouseInfo.Warehouseno);
            RequestHandler.addRequestWithDialog(Request.Method.GET, TAG_Saleoutstock_GETBOXlIST, "获取拼箱复核列表",
                    context, mHandler, RESULT_Saleoutstock_GETBOXlISTl, null, info.Outstock_GetpackagecartonList, map, null);
        }
    }

    public void IsDel(OutStockDeleteReviewModel model) {
        final OutStockDeleteReviewModel deleteReviewModel = model;
        String title = "";
        if (model.DeleteType == 1) {
            title = "确定删除物料" + model.getMaterialno() + "吗？";
        } else {
            title = "确定拼箱号" + model.getPackageSeq() + "吗？";
        }
        new AlertDialog.Builder(this).setTitle(title)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定触发的事件
                        if (deleteReviewModel.DeleteType == 1) {
                            //根据物料来删除
                            deleteReviewModel.setVouchertpe(CurrvoucherType);
                            deleteReviewModel.setErpvoucherno(CurrOrder);
                            String json = GsonUtil.parseModelToJson(deleteReviewModel);
                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Outstock_DeleteMaterial, "删除物料中",
                                    context, mHandler, RESUL_Outstock_DeleteMaterial, null, info.Outstock_DelReviewByMaterial, json, null);
                        } else {
                            deleteReviewModel.setErpvoucherno(CurrOrder);
                            String json = GsonUtil.parseModelToJson(deleteReviewModel);
                            //根据拼箱码来删除
                            RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_Saleoutstock_ReviewOrder, "删除拼箱中",
                                    context, mHandler, RESUL_Outstock_DeleteMaterial, null, info.Outstock_DelReviewBypackageCarton, json, null);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();

    }
}
