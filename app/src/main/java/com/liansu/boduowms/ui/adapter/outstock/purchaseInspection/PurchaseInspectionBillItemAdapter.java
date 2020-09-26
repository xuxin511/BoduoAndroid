package com.liansu.boduowms.ui.adapter.outstock.purchaseInspection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.order.OutStockOrderHeaderInfo;

import java.util.List;

/**
 * Created by GHOST on 2017/1/13.
 */

public class PurchaseInspectionBillItemAdapter extends BaseAdapter {
    private Context                       context; // 运行上下文
    private List<OutStockOrderHeaderInfo> receiptModels; // 信息集合
    private LayoutInflater                listContainer; // 视图容器
    private int                           selectItem = -1;


    public final class ListItemView { // 自定义控件集合


        public TextView txt_erpVoucherNo;
        public TextView txtCompany;
        public TextView txt_quality_no;
        public TextView txt_materialNo;
        public TextView txt_voucherNo;     //单据类型
        public TextView txt_arrVoucherNo;  //到货单号
        public TextView txt_voucher_qty;  //订单数量
        public TextView txt_material_desc;
    }

    public PurchaseInspectionBillItemAdapter(Context context, List<OutStockOrderHeaderInfo> receiptModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.receiptModels = receiptModels;

    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        return receiptModels == null ? 0 : receiptModels.size();
    }

    @Override
    public Object getItem(int position) {
        return receiptModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int selectID = position;
        // 自定义视图
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();

            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_quality_inspection_choice_listview, null);
            listItemView.txt_erpVoucherNo = (TextView) convertView.findViewById(R.id.txt_erpVoucherNo);
            listItemView.txt_materialNo = (TextView) convertView.findViewById(R.id.txt_materialNo);
            listItemView.txt_quality_no = (TextView) convertView.findViewById(R.id.txt_quality_no);
            listItemView.txt_voucherNo = (TextView) convertView.findViewById(R.id.txt_voucherNo);
            listItemView.txt_arrVoucherNo = (TextView) convertView.findViewById(R.id.txt_arrVoucherNo);
            listItemView.txt_voucher_qty = (TextView) convertView.findViewById(R.id.txt_voucher_qty);
            LinearLayout forth_layout = (LinearLayout) convertView.findViewById(R.id.forth_layout);
            LinearLayout second_layout = (LinearLayout) convertView.findViewById(R.id.second_layout);
            listItemView.txt_material_desc = (TextView) convertView.findViewById(R.id.txt_material_desc);
            forth_layout.setVisibility(View.GONE);
            listItemView.txt_material_desc.setVisibility(View.GONE);
//            second_layout.setVisibility(View.GONE);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        OutStockOrderHeaderInfo headerInfo = receiptModels.get(selectID);
        listItemView.txt_quality_no.setText(headerInfo.getQualityno()+"");
//        listItemView.txt_materialNo.setText(headerInfo.getm);
        listItemView.txt_erpVoucherNo.setText(headerInfo.getPurchaseno()+"");
        listItemView.txt_arrVoucherNo.setText(headerInfo.getArrvoucherno()+"");
        return convertView;
    }

}
