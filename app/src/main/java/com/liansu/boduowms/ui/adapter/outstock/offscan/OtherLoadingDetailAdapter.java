package com.liansu.boduowms.ui.adapter.outstock.offscan;

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
 * 其他装车
 * Created by GHOST on 2017/1/13.
 */

public class OtherLoadingDetailAdapter extends BaseAdapter {
    private Context                            context; // 运行上下文
    private List<OutStockOrderDetailInfo> outStockTaskDetailsInfoModels; // 信息集合
    private LayoutInflater                     listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txt_material_no;
        public TextView txt_batch_no;
        public TextView txt_remain_qty;
        public TextView txt_out_stock_qty;
        public TextView txt_scan_qty;
        public TextView txt_material_desc;

    }

    public OtherLoadingDetailAdapter(Context context, List<OutStockOrderDetailInfo> outStockTaskDetailsInfoModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.outStockTaskDetailsInfoModels = outStockTaskDetailsInfoModels;

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
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();

            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_other_loading_detail_listview, null);
            listItemView.txt_material_no = (TextView) convertView.findViewById(R.id.txt_material_no);
            listItemView.txt_batch_no = (TextView) convertView.findViewById(R.id.txt_batch_no);
            listItemView.txt_remain_qty = (TextView) convertView.findViewById(R.id.txt_remain_qty);
            listItemView.txt_out_stock_qty = (TextView) convertView.findViewById(R.id.txt_out_stock_qty);
            listItemView.txt_scan_qty = (TextView) convertView.findViewById(R.id.txt_scan_qty);
            listItemView.txt_material_desc = (TextView) convertView.findViewById(R.id.txt_material_desc);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        final OutStockOrderDetailInfo detailInfo = outStockTaskDetailsInfoModels.get(selectID);
        listItemView.txt_material_no.setText(detailInfo.getMaterialno());
        if (detailInfo.getBatchno() != null && !detailInfo.getBatchno().equals("")) {
            listItemView.txt_batch_no.setText(detailInfo.getBatchno());
        }

        listItemView.txt_remain_qty.setText("下架:" + detailInfo.getOutstockqty());
        listItemView.txt_out_stock_qty.setText("已复核:" + detailInfo.getReviewQty());
        listItemView.txt_scan_qty.setText("可复核：" + detailInfo.getRemainqty());
        listItemView.txt_material_desc.setText(detailInfo.getMaterialdesc());

        if (detailInfo.getRemainqty() != 0 &&
                detailInfo.getOutstockqty() > 0) {
            convertView.setBackgroundResource(R.color.khaki);
        } else if (detailInfo.getRemainqty() == 0) {
            convertView.setBackgroundResource(R.color.springgreen);
        } else if (detailInfo.getOutstockqty() == 0) {
            convertView.setBackgroundResource(R.color.trans);
        }

        return convertView;
    }


}
