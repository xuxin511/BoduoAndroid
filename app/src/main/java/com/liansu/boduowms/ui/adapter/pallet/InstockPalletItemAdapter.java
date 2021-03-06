package com.liansu.boduowms.ui.adapter.pallet;

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

public class InstockPalletItemAdapter extends BaseAdapter {
    private Context               context; // 运行上下文
    private List<OutBarcodeInfo> barCodeInfoList; // 信息集合
    private LayoutInflater        listContainer; // 视图容器


    public final class ListItemView { // 自定义控件集合
        public TextView txtBatchNo;
        public TextView txtMaterialNo;
        public TextView txtQty;
    }

    public InstockPalletItemAdapter(Context context, List<OutBarcodeInfo> barCodeInfoList) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.barCodeInfoList = barCodeInfoList;

    }


    @Override
    public int getCount() {
        return barCodeInfoList == null ? 0 : barCodeInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return barCodeInfoList.get(position);
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
            convertView = listContainer.inflate(R.layout.item_instock_pallet_listview, null);
            listItemView.txtMaterialNo = (TextView) convertView.findViewById(R.id.txt_materialNo);
            listItemView.txtBatchNo = (TextView) convertView.findViewById(R.id.txt_batch);
            listItemView.txtQty = (TextView) convertView.findViewById(R.id.txt_qty);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        OutBarcodeInfo outBarcodeInfo = barCodeInfoList.get(selectID);
        listItemView.txtMaterialNo.setText("物料编码:"+outBarcodeInfo.getMaterialno());
        listItemView.txtBatchNo.setText("批次：" + outBarcodeInfo.getBatchno());
        listItemView.txtQty.setText("数量：" + outBarcodeInfo.getQty());
        return convertView;
    }


}
