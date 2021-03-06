package com.liansu.boduowms.ui.adapter.instock.baseScanStorage.upshelf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.bean.stock.AreaInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by GHOST on 2017/1/13.
 */

public class UpShelfScanDetailAdapter extends BaseAdapter {
    private Context               context; // 运行上下文
    private List<OrderDetailInfo> inStockTaskDetailsInfoModels; // 信息集合
    private LayoutInflater        listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txtbarcode;
        public TextView txtScanNum;
        public TextView txtRemainQty;
        public TextView txtMaterialDesc;
        public TextView txtreferStock;
    }

    public UpShelfScanDetailAdapter(Context context, List<OrderDetailInfo> inStockTaskDetailsInfoModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.inStockTaskDetailsInfoModels = inStockTaskDetailsInfoModels;

    }

    @Override
    public int getCount() {
        return inStockTaskDetailsInfoModels == null ? 0 : inStockTaskDetailsInfoModels.size();
    }

    @Override
    public Object getItem(int position) {
        return inStockTaskDetailsInfoModels.get(position);
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
            convertView = listContainer.inflate(R.layout.item_uploadscandetail_listview, null);
            listItemView.txtbarcode = (TextView) convertView.findViewById(R.id.txtbarcode);
            listItemView.txtScanNum = (TextView) convertView.findViewById(R.id.txtScanNum);
            listItemView.txtreferStock = (TextView) convertView.findViewById(R.id.txtreferStock);
            listItemView.txtRemainQty = (TextView) convertView.findViewById(R.id.txtRemainQty);
            listItemView.txtMaterialDesc = (TextView) convertView.findViewById(R.id.txtMaterialDesc);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        final OrderDetailInfo inStockTaskDetailsInfoModel = inStockTaskDetailsInfoModels.get(selectID);
            listItemView.txtbarcode.setText(inStockTaskDetailsInfoModel.getMaterialno());

        listItemView.txtScanNum.setText("扫描数："+inStockTaskDetailsInfoModel.getReceiveqty());//"扫描数："+inStockTaskDetailsInfoModel.getScanQty()
//        listItemView.txtRemainQty.setText("待上架："+inStockTaskDetailsInfoModel.getRemainQty());
        listItemView.txtRemainQty.setText("待上架：" + inStockTaskDetailsInfoModel.getRemainqty());
        if (inStockTaskDetailsInfoModel != null) {
//            listItemView.txtreferStock.setText("推荐库位：" + GetReferStock(inStockTaskDetailsInfoModel.getLstArea()));
            listItemView.txtreferStock.setText("推荐库位：");
        } else {
            listItemView.txtreferStock.setText("推荐库位：");
        }
        listItemView.txtMaterialDesc.setText(inStockTaskDetailsInfoModel.getMaterialdesc());
        if (inStockTaskDetailsInfoModel.getRemainqty() >0 &&  inStockTaskDetailsInfoModel.getReceiveqty()>0) {
            convertView.setBackgroundResource(R.color.khaki);
        }
//        else if (inStockTaskDetailsInfoModel.getScanQty().compareTo(inStockTaskDetailsInfoModel.getRemainQty())==0) {
        else if (inStockTaskDetailsInfoModel.getRemainqty() == 0) {
            convertView.setBackgroundResource(R.color.springgreen);
        } else {
            convertView.setBackgroundResource(R.color.trans);
        }
        return convertView;
    }


    String GetReferStock(ArrayList<AreaInfo> areaInfoModels) {
        StringBuffer Area = new StringBuffer();
        String[] referStocks = new String[areaInfoModels.size()];
        if (areaInfoModels != null) {
            int i = 0;
            for (AreaInfo areaInfoModel : areaInfoModels) {
                Area.append(areaInfoModel.getAreano() + ",");
                referStocks[i++] = areaInfoModel.getAreano();
            }
        }
        return areaInfoModels == null || areaInfoModels.size() == 0 ? "" : Area.substring(0, Area.length() - 1);
    }


}
