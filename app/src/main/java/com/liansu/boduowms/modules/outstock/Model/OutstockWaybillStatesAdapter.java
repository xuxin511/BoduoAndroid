package com.liansu.boduowms.modules.outstock.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liansu.boduowms.R;

import java.util.List;

//托运单适配器
 public class OutstockWaybillStatesAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<OutstockWaybillStatusModel> modelList; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public OutstockWaybillStatesAdapter(Context mContext, List<OutstockWaybillStatusModel> stockInfoModels) {
        this.context = mContext;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.modelList = stockInfoModels;
    }


    public final class ListItemView { // 自定义控件集合
        public TextView txtOneLeft;
        public TextView txtOneRight;
        public TextView txtTwoCenter;
        public TextView txtThreeCenter;
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
        com.liansu.boduowms.modules.outstock.Model.OutstockWaybillStatesAdapter.ListItemView listItemView = null;
        if (convertView == null) {
            // 获取list_item布局文件的视图
            listItemView = new com.liansu.boduowms.modules.outstock.Model.OutstockWaybillStatesAdapter.ListItemView();
            convertView = listContainer.inflate(R.layout.item_inventory_configstyle, null);
            //   listItemView.txtstrong = (TextView) convertView.findViewById(R.id.txt_strongcode);
            listItemView.txtOneLeft = (TextView) convertView.findViewById(R.id.txt_voucherNo);
            listItemView.txtOneRight = (TextView) convertView.findViewById(R.id.txt_reference_standard);
            listItemView.txtTwoCenter = (TextView) convertView.findViewById(R.id.txtVoucherQty);
            listItemView.txtThreeCenter = (TextView) convertView.findViewById(R.id.txtMaterialDesc);
            listItemView.linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout2);
            listItemView.linearLayout.setVisibility(View.GONE);//隐藏
            convertView.setTag(listItemView);
        } else {
            listItemView = (com.liansu.boduowms.modules.outstock.Model.OutstockWaybillStatesAdapter.ListItemView) convertView.getTag();
        }
        final OutstockWaybillStatusModel mDetailInfo = modelList.get(selectID);
            //根据物料删除
            listItemView.txtOneLeft.setText( mDetailInfo.Contacts);
            // listItemView.txt_reference_standard.setText("已盘数量：" + mDetailInfo.ScannQty);
            listItemView.txtTwoCenter.setText(mDetailInfo.Erpvoucherno  );
            listItemView.txtThreeCenter.setText("" + mDetailInfo.Address);
        if (mDetailInfo.isIscheck()) {
            convertView.setBackgroundResource(R.color.springgreen);
        } else {
            convertView.setBackgroundResource(R.color.trans);
        }
        return convertView;
    }

}
