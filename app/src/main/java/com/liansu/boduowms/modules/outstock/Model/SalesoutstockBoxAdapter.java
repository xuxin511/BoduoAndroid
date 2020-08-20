package com.liansu.boduowms.modules.outstock.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.ui.adapter.outstock.packing.PackingScanAdapter;

import java.util.List;



    public class SalesoutstockBoxAdapter extends BaseAdapter {
        private Context mContext; // 运行上下文
        private List<SalesoutStcokboxRequery> stockInfoModels; // 信息集合
        private LayoutInflater listContainer; // 视图容器

        public final class ListItemView { // 自定义控件集合

            public TextView txtMaterialNo;
            public TextView txtMaterialDesc;
            public TextView txtQty;
            public TextView txtBatchNo;
            public TextView txtstrong;
        }

        public SalesoutstockBoxAdapter(Context mContext, List<SalesoutStcokboxRequery> stockInfoModels) {
            this.mContext = mContext;
            listContainer = LayoutInflater.from(mContext); // 创建视图容器并设置上下文
            this.stockInfoModels = stockInfoModels;

        }

        @Override
        public int getCount() {
            return stockInfoModels == null ? 0 : stockInfoModels.size();
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
            com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxAdapter.ListItemView listItemView = null;
            if (convertView == null) {
                listItemView = new com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxAdapter.ListItemView();

                // 获取list_item布局文件的视图
                convertView = listContainer.inflate(R.layout.item_quality_inspection_listview, null);
              //  listItemView.txtstrong = (TextView) convertView.findViewById(R.id.txt_strongcode);
                listItemView.txtMaterialNo = (TextView) convertView.findViewById(R.id.item_quality_inspection_material_no);
                listItemView.txtBatchNo = (TextView) convertView.findViewById(R.id.item_quality_inspection_batch_no);
                listItemView.txtMaterialDesc = (TextView) convertView.findViewById(R.id.item_quality_inspection_material_desc);
                listItemView.txtQty = (TextView) convertView.findViewById(R.id.item_quality_inspection_qty);
                convertView.setTag(listItemView);
            } else {
                listItemView = (com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxAdapter.ListItemView) convertView.getTag();
            }
            SalesoutStcokboxRequery stockInfoModel = stockInfoModels.get(selectID);
            listItemView.txtMaterialNo.setText(stockInfoModel.getMaterialno());
            listItemView.txtQty.setText(mContext.getString(R.string.packing_scan_qty) + stockInfoModel.getQty());
//        listItemView.txtQty.setText("取样数："+(stockInfoModel.getPickModel()==3?stockInfoModel.getAmountQty():stockInfoModel.getQty()));
            listItemView.txtMaterialDesc.setText(stockInfoModel.getMaterialdesc());
            listItemView.txtBatchNo.setText(stockInfoModel.getBatchno());
            return convertView;
        }


    }

