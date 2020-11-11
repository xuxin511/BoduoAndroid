package com.liansu.boduowms.modules.outstock.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryModel;
import com.liansu.boduowms.utils.function.ArithUtil;
import com.liansu.boduowms.utils.function.GsonUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.qos.logback.core.joran.spi.ElementSelector;

public class SalesoutstockAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<OutStockOrderDetailInfo> outStockTaskDetailsInfoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txt_voucherNo; //单号
        public TextView txt_reference_standard;//推荐库位
        public TextView txtVoucherQty;//订单数量
        public TextView txtRemainQty;
        public TextView txtScanQty;
        public TextView txtMaterialDesc;//
        public TextView txt_recommended_location;
        public TextView txtstrong;
        public TextView txtbatchno;
        public TextView unitno;


    }

    public SalesoutstockAdapter(Context context, List<OutStockOrderDetailInfo> outStockTaskDetailsInfoModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.outStockTaskDetailsInfoModels = outStockTaskDetailsInfoModels;
        Iterator<OutStockOrderDetailInfo> it = this.outStockTaskDetailsInfoModels.iterator();
        List<OutStockOrderDetailInfo> list = new ArrayList<OutStockOrderDetailInfo>();
        List<OutStockOrderDetailInfo> REDlist = new ArrayList<OutStockOrderDetailInfo>();
        while (it.hasNext()) {
            OutStockOrderDetailInfo model = it.next();
            if (model.getRemainqty() == 0) {
                list.add(model);
                it.remove();
            } else if (model.getRemainqty() > 0 && model.getRemainqty() < model.getVoucherqty()) {
                REDlist.add(model);
                it.remove();
            }
        }
        int i = outStockTaskDetailsInfoModels.size();
        for (OutStockOrderDetailInfo item : list) {
            outStockTaskDetailsInfoModels.add(i, item);
            i++;
        }
        for (OutStockOrderDetailInfo item : REDlist) {
            outStockTaskDetailsInfoModels.add(0, item);
        }

    }

    @Override
    public int getCount() {
        return outStockTaskDetailsInfoModels == null ? 0 : outStockTaskDetailsInfoModels.size();
    }

    @Override
    public Object getItem(int position) {
        return outStockTaskDetailsInfoModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int selectID = position;
        // 自定义视图
        com.liansu.boduowms.modules.outstock.Model.SalesoutstockAdapter.ListItemView listItemView = null;
        final OutStockOrderDetailInfo mDetailInfo = outStockTaskDetailsInfoModels.get(selectID);
        if (convertView == null) {
            listItemView = new com.liansu.boduowms.modules.outstock.Model.SalesoutstockAdapter.ListItemView();
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_offshelfscandetail_listview, null);
            listItemView.txtstrong = (TextView) convertView.findViewById(R.id.txt_strongcode);
            listItemView.txt_voucherNo = (TextView) convertView.findViewById(R.id.txt_voucherNo);
            listItemView.txt_reference_standard = (TextView) convertView.findViewById(R.id.txt_reference_standard);
            listItemView.txtVoucherQty = (TextView) convertView.findViewById(R.id.txtVoucherQty);
            listItemView.txtRemainQty = (TextView) convertView.findViewById(R.id.txtRemainQty);
            listItemView.txtScanQty = (TextView) convertView.findViewById(R.id.txtScanQty);
            listItemView.txtMaterialDesc = (TextView) convertView.findViewById(R.id.txtMaterialDesc);
            listItemView.txtbatchno = (TextView) convertView.findViewById(R.id.txt_batch_no);
            listItemView.unitno = (TextView) convertView.findViewById(R.id.txt_unitno);
            listItemView.txt_recommended_location = (TextView) convertView.findViewById(R.id.txt_recommended_location);
            if(mDetailInfo.getVouchertype()==36){
                listItemView.txt_recommended_location.setVisibility(View.GONE);
                listItemView.txtMaterialDesc.setVisibility(View.GONE);
                listItemView.unitno.setVisibility(View.GONE);
                listItemView.txtbatchno.setVisibility(View.GONE);
                listItemView.txtstrong.setVisibility(View.GONE);
            }
            convertView.setTag(listItemView);
        } else {
            listItemView = (com.liansu.boduowms.modules.outstock.Model.SalesoutstockAdapter.ListItemView) convertView.getTag();
        }
        listItemView.txt_voucherNo.setText(mDetailInfo.getMaterialno());
        if(mDetailInfo.getVouchertype()!=46 && mDetailInfo.getVouchertype()!=30 & mDetailInfo.getVouchertype()!=25& mDetailInfo.getVouchertype()!=55) { //不等于领料委外单的情况下显示  零头散件 包装量
            if (mDetailInfo.getMaterialCartonNum() != 0 || mDetailInfo.getMaterialPartNum() != 0) {
                if(mDetailInfo.getVouchertype()==36){
                    listItemView.txt_reference_standard.setText(mDetailInfo.getMaterialdesc());
                }else
                {
                    listItemView.txt_reference_standard.setText("整件:" + mDetailInfo.getMaterialCartonNum() + "/零头:" + mDetailInfo.getMaterialPartNum());
                }
            }
            listItemView.txtstrong.setText("包装量:" + String.valueOf(mDetailInfo.getPackQty()));

        }else {
            listItemView.txt_reference_standard.setText("规格型号:" + mDetailInfo.getSpec());
        }
        String batchno="";
        if(mDetailInfo.getBatchno()!=null)
            batchno=mDetailInfo.getBatchno();
        listItemView.txtbatchno.setText("批次:" +batchno);

        //listItemView.txtVoucherQty.setText("下架量:" + "11111111"+mDetailInfo.getUnit());
        listItemView.txtRemainQty.setText("剩余量:" + mDetailInfo.getRemainqty());
        if(mDetailInfo.getVoucherqty()<0&& mDetailInfo.getOutstockqty()==0&&mDetailInfo.getRemainqty()==0) {
            listItemView.txtVoucherQty.setText("下架量:0");
            listItemView.txtScanQty.setText("已超领:" + ArithUtil.sub(0f, mDetailInfo.getVoucherqty()));
        }else {
            listItemView.txtVoucherQty.setText("下架量:" + mDetailInfo.getVoucherqty());
            listItemView.txtScanQty.setText("已下架:" + mDetailInfo.getScanqty());
        }
        listItemView.txt_recommended_location.setText("推荐库位:"+mDetailInfo.getAreano());
        listItemView.txtMaterialDesc.setText(mDetailInfo.getMaterialdesc());
        listItemView.unitno.setText("单位:"+mDetailInfo.getUnit());

//             listItemView.txt_voucherNo.setText("12345678901");
//        if (mDetailInfo.getMaterialCartonNum() != 0 || mDetailInfo.getMaterialPartNum() != 0) {
//            listItemView.txt_reference_standard.setText("整件:1111" + "/零头:1111");
//        }
//        listItemView.txtstrong.setText(String.valueOf("包装:"+mDetailInfo.getPackQty()));
//        listItemView.txtVoucherQty.setText("订单：" + mDetailInfo.getVoucherqty());
//        listItemView.txtRemainQty.setText("剩余:" + mDetailInfo.getRemainqty());
//        listItemView.txtScanQty.setText("已扫：" + mDetailInfo.getScanqty());
//        listItemView.txt_recommended_location.setText("推荐库位:"+mDetailInfo.getAreano());
//        listItemView.txtMaterialDesc.setText("物料名称:" + mDetailInfo.getMaterialdesc());

        if (mDetailInfo.getRemainqty() > 0 && mDetailInfo.getRemainqty() < mDetailInfo.getVoucherqty()) {
            convertView.setBackgroundResource(R.color.khaki);
        } else if (mDetailInfo.getRemainqty() == 0) {
            convertView.setBackgroundResource(R.color.springgreen);
        } else {
            convertView.setBackgroundResource(R.color.trans);
        }
//        if(mDetailInfo.getVoucherqty()<0&& mDetailInfo.getOutstockqty()==0&&mDetailInfo.getRemainqty()==0){
//            convertView.setBackgroundResource(R.color.colorAccent);
//        }
        return convertView;
    }


}
