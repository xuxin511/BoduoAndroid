package com.liansu.boduowms.modules.inHouseStock.inventory.Model;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxListRequery;

import java.util.List;

//盘点表头信息
 public class InventoryHeadAdapter extends BaseAdapter {

    private Context mContext; // 运行上下文
    private List<InventoryModel> stockInfoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合
        public TextView txtOneLeft;
        public TextView txtOneRight;
        public TextView txtTwoCenter;
        public TextView txtThreeCenter;
    }

    public InventoryHeadAdapter(Context mContext, List<InventoryModel> stockInfoModels) {
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
        com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryHeadAdapter.ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryHeadAdapter.ListItemView();
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_inventory_headstyle, null);
            listItemView.txtOneLeft = (TextView)convertView.findViewById(R.id.inventory_txt_one_left);
            listItemView.txtOneRight = (TextView)convertView.findViewById(R.id.inventory_txt_one_right);
            listItemView.txtTwoCenter = (TextView) convertView.findViewById(R.id.inventory_txt_two_center);
            listItemView.txtThreeCenter = (TextView) convertView.findViewById(R.id.inventory_txt_three_center);
            //   listItemView.txtQty = (TextView) convertView.findViewById(R.id.item_quality_inspection_qty);
            convertView.setTag(listItemView);
        } else {
            listItemView = ( com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryHeadAdapter.ListItemView) convertView.getTag();
        }
        InventoryModel stockInfoModel = stockInfoModels.get(selectID);
        listItemView.txtOneLeft.setText(" 单号:" + stockInfoModel.Erpvoucherno);
        String status="";
        switch (stockInfoModel.Checkstatus){
            case 1:
                status="新建";
                break;
            case 2:
                status="开始";
                break;
            case 3:
                status="完成";
                break;
            case 4:
                status="终止";
                break;
        }
        listItemView.txtOneRight.setText("状态:" + status);
        listItemView.txtTwoCenter.setText("仓库:" + stockInfoModel. Warehouseno);
        if(stockInfoModel.Checkdesc==null)
            stockInfoModel.Checkdesc="无";
        listItemView.txtThreeCenter.setText("描述:" + stockInfoModel.Checkdesc);
   //     listItemView.txtOneLeft.setText("单号" + stockInfoModel.Erpvoucherno );
        //listItemView.txtOneLeft.setText("单号" + stockInfoModel.Erpvoucherno );
        return convertView;
    }



}
