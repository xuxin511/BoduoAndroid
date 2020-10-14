package com.liansu.boduowms.modules.outstock.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryModel;

import java.util.List;

public class OutStockDeleteReviewAdapter extends BaseAdapter {
    private Context context; // 运行上下文
    private List<OutStockDeleteReviewModel> modelList; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public OutStockDeleteReviewAdapter(Context mContext, List<OutStockDeleteReviewModel> stockInfoModels) {
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
        com.liansu.boduowms.modules.outstock.Model.OutStockDeleteReviewAdapter.ListItemView listItemView = null;
        if (convertView == null) {
            // 获取list_item布局文件的视图
            listItemView = new com.liansu.boduowms.modules.outstock.Model.OutStockDeleteReviewAdapter.ListItemView();
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
            listItemView = (com.liansu.boduowms.modules.outstock.Model.OutStockDeleteReviewAdapter.ListItemView) convertView.getTag();
        }
        final OutStockDeleteReviewModel mDetailInfo = modelList.get(selectID);
        if (mDetailInfo.getDeleteType() == 1) {
            //根据物料删除
            listItemView.txtOneLeft.setText(mDetailInfo.getMaterialno());
            // listItemView.txt_reference_standard.setText("已盘数量：" + mDetailInfo.ScannQty);
            listItemView.txtTwoCenter.setText("已装车量:" + mDetailInfo.getScanQty());
            listItemView.txtThreeCenter.setText("" + mDetailInfo.getMaterialdesc());
        } else {
            //根据拼箱删除
            listItemView.txtOneLeft.setText(mDetailInfo.getMaterialno());
            listItemView.txtOneRight.setText("箱号:" + mDetailInfo.getPackageSeq());
            listItemView.txtTwoCenter.setText("拼箱数量:" + mDetailInfo.Qty);
            listItemView.txtThreeCenter.setText("箱码"+mDetailInfo.getPackageCode());
        }
        if (mDetailInfo.isCheck()) {
            convertView.setBackgroundResource(R.color.springgreen);
        } else {
            convertView.setBackgroundResource(R.color.trans);
        }
        return convertView;
    }
}
