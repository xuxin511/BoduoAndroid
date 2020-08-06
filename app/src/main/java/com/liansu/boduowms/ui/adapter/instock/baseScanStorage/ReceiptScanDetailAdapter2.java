package com.liansu.boduowms.ui.adapter.instock.baseScanStorage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liansu.boduowms.bean.order.OrderHeaderInfo;

import java.util.List;


/**
 * Created by GHOST on 2017/1/13.
 */

public class ReceiptScanDetailAdapter2 extends BaseAdapter {
    private Context               context; // 运行上下文
    private List<OrderHeaderInfo> receiptDetailModels; // 信息集合
    private LayoutInflater        listContainer; // 视图容器
    private String                receiptType ="";

    public final class ListItemView { // 自定义控件集合

        public TextView txtbarcode;
        public TextView txtScanNum;
        public TextView txtRemainQty;
        public TextView txtMaterialDesc;
    }

    public ReceiptScanDetailAdapter2(Context context, String ReceiptType, List<OrderHeaderInfo> receiptDetailModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.receiptDetailModels = receiptDetailModels;
        receiptType =ReceiptType;
    }

    public ReceiptScanDetailAdapter2(Context context, List<OrderHeaderInfo> receiptDetailModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.receiptDetailModels = receiptDetailModels;
        receiptType ="";
    }

    @Override
    public int getCount() {
        return  receiptDetailModels==null?0:receiptDetailModels.size();
    }

    @Override
    public Object getItem(int position) {
        return receiptDetailModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int selectID = position;
        // 自定义视图
//        ListItemView listItemView = null;
//        if (convertView == null) {
//            listItemView = new ListItemView();
//            // 获取list_item布局文件的视图
//            convertView = listContainer.inflate(R.layout.item_receiptscandetail_listview,null);
//            listItemView.txtbarcode = (TextView) convertView.findViewById(R.id.txtbarcode);
//            listItemView.txtScanNum = (TextView) convertView.findViewById(R.id.txtScanNum);
//            listItemView.txtRemainQty = (TextView) convertView.findViewById(R.id.txtRemainQty);
//            listItemView.txtMaterialDesc = (TextView) convertView.findViewById(R.id.txtMaterialDesc);
//            convertView.setTag(listItemView);
//        } else {
//            listItemView = (ListItemView) convertView.getTag();
//        }
//        OrderHeaderInfo receiptDetailModel=receiptDetailModels.get(selectID);
////        listItemView.txtbarcode.setText(receiptDetailModel.getInvoiceNo()+"-"+receiptDetailModel.getMaterialNo()+"-"+receiptDetailModel.getToBatchNo());
//        listItemView.txtbarcode.setText(receiptDetailModel.get()+"   ("+receiptDetailModel.getTracNo()+")");
//        listItemView.txtScanNum.setText("已扫："+receiptDetailModel.getScanQty());
//        if(receiptDetailModel.getVoucherType()==22&&receiptType.equals("采购收货")){
//            listItemView.txtRemainQty.setText("待收："+(receiptDetailModel.getADVRECEIVEQTY()- receiptDetailModel.getReceiveQty()));
//        }else{
//            listItemView.txtRemainQty.setText("待收："+receiptDetailModel.getRemainQty());
//        }
//
//        listItemView.txtMaterialDesc.setText(receiptDetailModel.getMaterialDesc());
//        if (receiptDetailModel.getScanQty()!=0 &&
//                receiptDetailModel.getScanQty().compareTo( Math.abs(receiptDetailModel.getRemainQty()))<0) {
//            convertView.setBackgroundResource(R.color.khaki);
//        }
//        else if (receiptDetailModel.getScanQty().compareTo(Math.abs(receiptDetailModel.getRemainQty()))==0) {
//            convertView.setBackgroundResource(R.color.springgreen);
//        }else{
//            convertView.setBackgroundResource(R.color.trans);
//        }
        return convertView;
    }


}
