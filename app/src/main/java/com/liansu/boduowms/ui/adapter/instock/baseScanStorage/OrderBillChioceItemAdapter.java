package com.liansu.boduowms.ui.adapter.instock.baseScanStorage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.order.OrderHeaderInfo;

import java.util.List;

/**
 * Created by GHOST on 2017/1/13.
 */

public class OrderBillChioceItemAdapter extends BaseAdapter {
    private Context             context; // 运行上下文
    private List<OrderHeaderInfo> receiptModels; // 信息集合
    private LayoutInflater      listContainer; // 视图容器
    private int                 selectItem = -1;


    public final class ListItemView { // 自定义控件集合

        public TextView txtTaskNo;
        public TextView txt_department;
        public TextView txt_Supplier_Name;
        public TextView txtStrongHoldName;
    }

    public OrderBillChioceItemAdapter(Context context, List<OrderHeaderInfo> receiptModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.receiptModels = receiptModels;

    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        return receiptModels==null?0: receiptModels.size();
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
            convertView = listContainer.inflate(R.layout.item_billchoice_listview,null);
            listItemView.txtTaskNo = (TextView) convertView.findViewById(R.id.txtTaskNo);
            listItemView.txt_department = (TextView) convertView.findViewById(R.id.txt_department);
            listItemView.txtStrongHoldName = (TextView) convertView.findViewById(R.id.txtStrongHoldName);
            listItemView.txt_Supplier_Name = (TextView) convertView.findViewById(R.id.txt_Supplier_Name);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }

        OrderHeaderInfo receiptModel=receiptModels.get(selectID);

        listItemView.txtTaskNo.setText(receiptModel.getErpvoucherno());
        listItemView.txtStrongHoldName.setText(receiptModel.getStrongholdName());
        if (receiptModel.getStrongholdName()!=null && !receiptModel.getStrongholdName().equals("")){
            listItemView.txtStrongHoldName.setVisibility(View.VISIBLE);
        }else {
            listItemView.txtStrongHoldName.setVisibility(View.GONE);
        }
        listItemView.txt_department.setText(receiptModel.getDepartmentname());
//        String supplierCode=receiptModel.getSuppliername()+"("+receiptModel.getSupplierno()+")";
        String supplierName=receiptModel.getSuppliername();
        listItemView.txt_Supplier_Name.setText(supplierName);
        if (selectItem == position) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.mediumseagreen));
        }else{
            convertView.setBackgroundResource(R.color.trans);
        }
        return convertView;
    }

}
