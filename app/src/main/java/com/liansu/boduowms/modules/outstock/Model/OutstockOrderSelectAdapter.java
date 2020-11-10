package com.liansu.boduowms.modules.outstock.Model;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.modules.outstock.SalesOutstock.OutstockOrderSelect;

import java.util.List;

//订单查询适配器
public class OutstockOrderSelectAdapter extends BaseAdapter {

    private Context context; // 运行上下文
    private List<OutstockOrderSelectModel> modelList; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public OutstockOrderSelectAdapter(Context mContext, List<OutstockOrderSelectModel> stockInfoModels) {
        this.context = mContext;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.modelList = stockInfoModels;
    }


    public final class ListItemView { // 自定义控件集合
        public TextView txtOneLeft;
        public TextView txtTwoLeft;
        public TextView txtTwoRight;
        public TextView txtThreeLeft;
        public TextView txtThreeRight;
        public TextView txtFourRight;
        public LinearLayout linearLayout;
    }


    @Override
    public int getCount() {
        return modelList == null ? 0 : modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int selectID = position;
        com.liansu.boduowms.modules.outstock.Model.OutstockOrderSelectAdapter.ListItemView listItemView = null;
        if (convertView == null) {
            // 获取list_item布局文件的视图
            listItemView = new com.liansu.boduowms.modules.outstock.Model.OutstockOrderSelectAdapter.ListItemView();
            convertView = listContainer.inflate(R.layout.item_no_resource_detail_listview, null);
            //   listItemView.txtstrong = (TextView) convertView.findViewById(R.id.txt_strongcode);
            listItemView.txtOneLeft = (TextView) convertView.findViewById(R.id.item_barcode);
            listItemView.txtTwoLeft = (TextView) convertView.findViewById(R.id.item_material_no);
            listItemView.txtTwoRight = (TextView) convertView.findViewById(R.id.item_qty);
            listItemView.txtThreeLeft = (TextView) convertView.findViewById(R.id.item_batch_no);
            listItemView.txtThreeRight = (TextView) convertView.findViewById(R.id.item_area_no);
            listItemView.txtThreeLeft.setTextColor(Color.RED);
            listItemView.txtThreeRight.setTextColor(Color.RED);
            listItemView.txtFourRight = (TextView) convertView.findViewById(R.id.item_material_desc);
            convertView.setTag(listItemView);
        } else {
            listItemView = (com.liansu.boduowms.modules.outstock.Model.OutstockOrderSelectAdapter.ListItemView) convertView.getTag();
        }
        final OutstockOrderSelectModel mDetailInfo = modelList.get(selectID);
        listItemView.txtOneLeft.setText(mDetailInfo.getMaterialdesc());
        listItemView.txtTwoLeft.setText(mDetailInfo.getMaterialno());
        listItemView.txtTwoRight.setText("申请量:" + mDetailInfo.getVoucherqty());
        listItemView.txtThreeLeft.setText("已转出:" + mDetailInfo.Postqty);
        listItemView.txtThreeRight.setText("未转出:" + mDetailInfo.Notpostqty);
        if(mDetailInfo.Erpvoucherno==null)
            mDetailInfo.Erpvoucherno="无";
        if(mDetailInfo.Arrvoucherno==null)
            mDetailInfo.Arrvoucherno="无";
        listItemView.txtFourRight.setText("托运单:"+mDetailInfo.Erpvoucherno + "\n" + "发货单:"+mDetailInfo.Arrvoucherno);
        return convertView;
    }
}
