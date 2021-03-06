package com.liansu.boduowms.ui.adapter.outstock.offscan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.order.OrderDetailInfo;

import java.util.ArrayList;


/**
 * Created by GHOST on 2017/1/13.
 */

public class OffShelfScanDetailAdapter extends BaseAdapter {
    private Context                                  context; // 运行上下文
    private ArrayList<OrderDetailInfo> outStockTaskDetailsInfoModels; // 信息集合
    private LayoutInflater                           listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtbarcode;
        public TextView txtScanNum;
        public TextView txtRemainQty;
        public TextView txtMaterialDesc;
        public TextView txtreferStock;
        public TextView txtERPVoucherNo;
        public TextView txtbatch;
    }

    public OffShelfScanDetailAdapter(Context context, ArrayList<OrderDetailInfo> outStockTaskDetailsInfoModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.outStockTaskDetailsInfoModels = outStockTaskDetailsInfoModels;

    }

    @Override
    public int getCount() {
        return  outStockTaskDetailsInfoModels==null?0:outStockTaskDetailsInfoModels.size();
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
            convertView = listContainer.inflate(R.layout.item_offshelfscandetail_listview,null);
            listItemView.txtbarcode = (TextView) convertView.findViewById(R.id.txtbarcode);
            listItemView.txtScanNum = (TextView) convertView.findViewById(R.id.txtScanNum);
            listItemView.txtERPVoucherNo = (TextView) convertView.findViewById(R.id.txtERPVoucherNo);
            listItemView.txtreferStock = (TextView) convertView.findViewById(R.id.txtreferStock);
            listItemView.txtRemainQty = (TextView) convertView.findViewById(R.id.txtRemainQty);
            listItemView.txtMaterialDesc = (TextView) convertView.findViewById(R.id.txtMaterialDesc);
//            listItemView.txtbatch = (TextView) convertView.findViewById(R.id.txtbatch);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
//       final OutStockTaskDetailsInfo_Model outStockTaskDetailsInfoModel=outStockTaskDetailsInfoModels.get(selectID);
//        listItemView.txtbarcode.setText("物料:"+outStockTaskDetailsInfoModel.getMaterialNo());
//        listItemView.txtScanNum.setText("扫描数："+outStockTaskDetailsInfoModel.getScanQty());
//        listItemView.txtRemainQty.setText("可拣:"+outStockTaskDetailsInfoModel.getRemainQty());
//        listItemView.txtreferStock.setText("批次："+outStockTaskDetailsInfoModel.getBatchNo());
//        listItemView.txtERPVoucherNo.setText("推荐库位:"+outStockTaskDetailsInfoModel.getAreaNo());
//        listItemView.txtMaterialDesc.setText(outStockTaskDetailsInfoModel.getMaterialDesc());
//        listItemView.txtbatch.setText((outStockTaskDetailsInfoModel.getEAN()==null?"":outStockTaskDetailsInfoModel.getEAN()));
//        if (outStockTaskDetailsInfoModel.getScanQty()!=0 &&
//                outStockTaskDetailsInfoModel.getScanQty().compareTo(outStockTaskDetailsInfoModel.getRemainQty())<0) {
//            convertView.setBackgroundResource(R.color.khaki);
//        }
//        else if (outStockTaskDetailsInfoModel.getPickFinish()) {
//            convertView.setBackgroundResource(R.color.springgreen);
//        }else if(outStockTaskDetailsInfoModel.getOutOfstock()){
//            convertView.setBackgroundResource(R.color.pink);
//        }
//        else{
//            convertView.setBackgroundResource(R.color.trans);
//        }
        return convertView;
    }





}
