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
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryModel;

import java.util.List;

public class SaleoutstockcallbackAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<OutStockOrderDetailInfo> outStockTaskDetailsInfoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合
        public TextView txtOneLeft;
        public TextView txtOneRight;
        public TextView txtTwoCenter;
        public TextView txtThreeCenter;


    }

    public SaleoutstockcallbackAdapter(Context mContext, List<OutStockOrderDetailInfo> stockInfoModels) {
        this.context = mContext;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.outStockTaskDetailsInfoModels = stockInfoModels;
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


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int selectID = position;
        // 自定义视图
        com.liansu.boduowms.modules.outstock.Model.SaleoutstockcallbackAdapter.ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new com.liansu.boduowms.modules.outstock.Model.SaleoutstockcallbackAdapter.ListItemView ();
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_inventory_headstyle, null);
            listItemView.txtOneLeft = (TextView) convertView.findViewById(R.id.inventory_txt_one_left);
            listItemView.txtOneRight = (TextView) convertView.findViewById(R.id.inventory_txt_one_right);
            listItemView.txtTwoCenter = (TextView) convertView.findViewById(R.id.inventory_txt_two_center);
            listItemView.txtThreeCenter = (TextView) convertView.findViewById(R.id.inventory_txt_three_center);
            //   listItemView.txtQty = (TextView) convertView.findViewById(R.id.item_quality_inspection_qty);
            convertView.setTag(listItemView);
        } else {
            listItemView = (com.liansu.boduowms.modules.outstock.Model.SaleoutstockcallbackAdapter.ListItemView) convertView.getTag();
        }
        OutStockOrderDetailInfo stockInfoModel = outStockTaskDetailsInfoModels.get(selectID);
        listItemView.txtOneLeft.setText("回调数量:"+stockInfoModel.getVoucherqty());
        listItemView.txtOneRight.setText("规格:"+stockInfoModel.getSpec());
        listItemView.txtTwoCenter.setText(stockInfoModel.getMaterialno());
        listItemView.txtThreeCenter.setText(stockInfoModel.getMaterialdesc());
        //     listItemView.txtOneLeft.setText("单号" + stockInfoModel.Erpvoucherno );
        //listItemView.txtOneLeft.setText("单号" + stockInfoModel.Erpvoucherno );
        return convertView;
    }


}
