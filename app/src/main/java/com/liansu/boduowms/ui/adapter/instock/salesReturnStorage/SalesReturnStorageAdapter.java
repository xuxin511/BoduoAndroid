package com.liansu.boduowms.ui.adapter.instock.salesReturnStorage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;

import java.util.List;


/**
 * Created by GHOST on 2017/1/13.
 */

public class SalesReturnStorageAdapter  extends BaseAdapter {
    private Context              context; // 运行上下文
    private List<OutBarcodeInfo> mList; // 信息集合
    private LayoutInflater       listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txt_material_no;
        public TextView txt_batch_no;
        public TextView txt_qty;
        public TextView txt_material_desc;
        public TextView txt_serial_no;


    }

    public SalesReturnStorageAdapter(Context context, List<OutBarcodeInfo> outBarcodeInfos) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.mList = outBarcodeInfos;

    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int selectID = position;
        // 自定义视图
        SalesReturnStorageAdapter.ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new SalesReturnStorageAdapter.ListItemView();

            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_sales_return_listview, null);
            listItemView.txt_material_no = (TextView) convertView.findViewById(R.id.txt_material_no);
            listItemView.txt_batch_no = (TextView) convertView.findViewById(R.id.txt_batch_no);
            listItemView.txt_qty = (TextView) convertView.findViewById(R.id.txt_qty);
            listItemView.txt_material_desc = (TextView) convertView.findViewById(R.id.txt_material_desc);
            listItemView.txt_serial_no = (TextView) convertView.findViewById(R.id.txt_serial_no);
            convertView.setTag(listItemView);
        } else {
            listItemView = (SalesReturnStorageAdapter.ListItemView) convertView.getTag();
        }
        final OutBarcodeInfo mDetailInfo = mList.get(selectID);
        listItemView.txt_material_no.setText(mDetailInfo.getMaterialno());
        listItemView.txt_serial_no.setText(mDetailInfo.getSerialno());
        listItemView.txt_batch_no.setText(mDetailInfo.getBatchno());
        listItemView.txt_qty.setText(mDetailInfo.getQty()+"");
        listItemView.txt_material_desc.setText(mDetailInfo.getMaterialdesc());
        return convertView;
    }

}
