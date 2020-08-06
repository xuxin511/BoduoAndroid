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

public class PalletItemAdapter extends BaseAdapter {
    private Context               context; // 运行上下文
    private List<OutBarcodeInfo> barCodeInfoList; // 信息集合
    private LayoutInflater        listContainer; // 视图容器


    public final class ListItemView { // 自定义控件集合
        public TextView txtBarcode;
        public TextView txtMaterialName;
        public TextView txtBatch;
        public TextView txtPalletNo;
        public TextView txtAreaNo;
        public TextView txtStrongHoldName;
        public TextView txtQty;
        public TextView txtMaterialNo;
    }

    public PalletItemAdapter(Context context, List<OutBarcodeInfo> barCodeInfoList) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.barCodeInfoList = barCodeInfoList;

    }


    @Override
    public int getCount() {
        return barCodeInfoList==null?0: barCodeInfoList.size();
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
            convertView = listContainer.inflate(R.layout.item_pallet_info_listview,null);
            listItemView.txtBarcode = (TextView) convertView.findViewById(R.id.item_Barcode);
            listItemView.txtMaterialName = (TextView) convertView.findViewById(R.id.item_MattterialName);
            listItemView.txtBatch = (TextView) convertView.findViewById(R.id.item_batchNo);
            listItemView.txtPalletNo = (TextView) convertView.findViewById(R.id.item_pallet_no);
            listItemView.txtAreaNo = (TextView) convertView.findViewById(R.id.item_area_info);
            listItemView.txtStrongHoldName = (TextView) convertView.findViewById(R.id.item_strong_hold_info);
            listItemView.txtMaterialNo = (TextView) convertView.findViewById(R.id.item_material_no);
            listItemView.txtQty = (TextView) convertView.findViewById(R.id.item_qty);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        OutBarcodeInfo barCodeInfo=barCodeInfoList.get(selectID);
        listItemView.txtBarcode.setText("序列号:"+barCodeInfo.getSerialno());
        listItemView.txtQty.setText("数量:"+barCodeInfo.getQty());
        listItemView.txtMaterialName.setText(barCodeInfo.getMaterialdesc());
        listItemView.txtBatch.setText("批次:"+barCodeInfo.getBatchno());
        listItemView.txtMaterialNo.setText("物料编码:"+barCodeInfo.getMaterialno());
//        listItemView.txtPalletNo.setText("托盘号:"+(barCodeInfo()==null?"":barCodeInfo.getPalletNo()));
//        if (barCodeInfo.getHouseNo()==null){
//            listItemView.txtAreaNo.setText("库位:"+barCodeInfo.getAreano());
//        }else {
//          listItemView.txtAreaNo.setText("库位:"+barCodeInfo.getAreano()+",库区:"+barCodeInfo.geth()+",仓库:"+barCodeInfo.getWarehouseNo());
//        }
        return convertView;
    }




}
