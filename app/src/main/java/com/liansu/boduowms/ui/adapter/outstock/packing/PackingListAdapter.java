package com.liansu.boduowms.ui.adapter.outstock.packing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;

import java.util.List;


/**
 * Created by GHOST on 2017/1/13.
 */

public class PackingListAdapter extends BaseAdapter {
    private Context                       mContext; // 运行上下文
    private List<OutStockOrderDetailInfo> stockInfoModels; // 信息集合
    private LayoutInflater                listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtMaterialNo;
        public TextView txtMaterialDesc;
        public TextView txtQty;
        public TextView txtBatchNo;
        public TextView txtPackingNo;
    }

    public PackingListAdapter(Context mContext, List<OutStockOrderDetailInfo> stockInfoModels) {
        this.mContext = mContext;
        listContainer = LayoutInflater.from(mContext); // 创建视图容器并设置上下文
        this.stockInfoModels = stockInfoModels;

    }

    @Override
    public int getCount() {
        return  stockInfoModels==null?0:stockInfoModels.size();
    }

    @Override
    public Object getItem(int position) {
        return stockInfoModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int selectID = position;
        // 自定义视图
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();

            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_packing_listview,null);
            listItemView.txtPackingNo = (TextView) convertView.findViewById(R.id.packing_list_packing_no_desc);
            listItemView.txtMaterialNo = (TextView) convertView.findViewById(R.id.item_packing_list_material_no);
            listItemView.txtBatchNo = (TextView) convertView.findViewById(R.id.item_packing_list_batch_no);
            listItemView.txtMaterialDesc = (TextView) convertView.findViewById(R.id.item_packing_list_material_desc);
            listItemView.txtQty = (TextView) convertView.findViewById(R.id.item_packing_list_qty);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        OutStockOrderDetailInfo stockInfoModel=stockInfoModels.get(selectID);
        listItemView.txtMaterialNo.setText(stockInfoModel.getMaterialno());
        listItemView.txtQty.setText(mContext.getString(R.string.packing_scan_qty)+stockInfoModel.getScanqty());
        listItemView.txtPackingNo.setText(stockInfoModel.getErpvoucherno());
//        listItemView.txtQty.setText("取样数："+(stockInfoModel.getPickModel()==3?stockInfoModel.getAmountQty():stockInfoModel.getQty()));
        listItemView.txtMaterialDesc.setText(stockInfoModel.getMaterialdesc());
//        listItemView.txtBatchNo.setText(stockInfoModel.getBatchno());
        return convertView;
    }


}
