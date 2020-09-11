package com.liansu.boduowms.modules.outstock.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liansu.boduowms.R;

import java.util.List;

public class SalesoutsotckBxoListAdapter extends BaseAdapter {
    private Context mContext; // 运行上下文
    private List<SalesoutstockBoxListRequery> stockInfoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合
        public TextView one;
        public TextView oneright;
        public TextView er;
        public TextView erright;
        public TextView san;
        public TextView si;
        public TextView wu;
    }

    public SalesoutsotckBxoListAdapter(Context mContext, List<SalesoutstockBoxListRequery> stockInfoModels) {
        this.mContext = mContext;
        listContainer = LayoutInflater.from(mContext); // 创建视图容器并设置上下文
        this.stockInfoModels = stockInfoModels;

    }

    @Override
    public int getCount() {
        return stockInfoModels == null ? 0 : stockInfoModels.size();
    }

    @Override
    public Object getItem(int position) {
        return stockInfoModels.get(position);
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
        com.liansu.boduowms.modules.outstock.Model.SalesoutsotckBxoListAdapter.ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new com.liansu.boduowms.modules.outstock.Model.SalesoutsotckBxoListAdapter.ListItemView();
            // 获取list_item布局文件的视图
           convertView = listContainer.inflate(R.layout.item_outstock_pboxlist, null);
            listItemView.one = (TextView)convertView.findViewById(R.id.outstock_pboxlist_yi);
            listItemView.oneright = (TextView)convertView.findViewById(R.id.outstock_pboxlist_yi_right);
            listItemView.er = (TextView)convertView.findViewById(R.id.outstock_pboxlist_er);
            listItemView.erright = (TextView)convertView.findViewById(R.id.outstock_pboxlist_er_right);
            listItemView.san = (TextView)convertView.findViewById(R.id.outstock_pboxlist_san);
            listItemView.si = (TextView)convertView.findViewById(R.id.outstock_pboxlist_si);
            listItemView.wu = (TextView)convertView.findViewById(R.id.outstock_pboxlist_wu);
            convertView.setTag(listItemView);
        } else {
            listItemView = (com.liansu.boduowms.modules.outstock.Model.SalesoutsotckBxoListAdapter.ListItemView) convertView.getTag();
        }
        SalesoutstockBoxListRequery stockInfoModel = stockInfoModels.get(selectID);
        listItemView.one.setText( "箱号:"+stockInfoModel.PackageSeq );
        listItemView.oneright.setText("数量:"+ stockInfoModel.getQty() );
        listItemView.er.setText( stockInfoModel.Materialno );
        listItemView.erright.setText("规格:"+ stockInfoModel.spec);
        listItemView.san.setText( stockInfoModel.getMaterialdesc());
        listItemView.si.setText("据点:"+ stockInfoModel.Strongholdname);
        listItemView.wu.setText("单位:"+ stockInfoModel.getUnit());
        if (stockInfoModel.isSelected) {
            convertView.setBackgroundResource(R.color.springgreen);
        } else {
            convertView.setBackgroundResource(R.color.trans);
        }
        return convertView;
    }
}
