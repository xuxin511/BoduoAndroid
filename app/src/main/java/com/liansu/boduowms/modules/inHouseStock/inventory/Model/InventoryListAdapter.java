package com.liansu.boduowms.modules.inHouseStock.inventory.Model;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.utils.function.ArithUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//明盘列表
 public class InventoryListAdapter extends BaseAdapter {
    private Context mContext; // 运行上下文
    private List<InventoryModel> stockInfoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合
        public TextView txt_voucherNo; //单号
        public TextView txt_reference_standard;//推荐库位
        public TextView txtVoucherQty;//订单数量
        public TextView txtRemainQty;
        public TextView txtScanQty;
        public TextView txtMaterialDesc;//
        public TextView txt_recommended_location;
        public TextView txtstrong;
        public TextView txtbatchno;
    }

    public InventoryListAdapter(Context mContext, List<InventoryModel> stockInfoModels) {
        this.mContext = mContext;
        listContainer = LayoutInflater.from(mContext); // 创建视图容器并设置上下文
        this.stockInfoModels = stockInfoModels;
        //  List<InventoryModel>

        //盘点数量等于暂存数量沉底
        //大于 沉底之上
        //小于  大于之上
        Iterator<InventoryModel> it = this.stockInfoModels.iterator();
        List<InventoryModel> lvlist = new ArrayList<InventoryModel>();
        List<InventoryModel> hlist = new ArrayList<InventoryModel>();
        List<InventoryModel> huanglist = new ArrayList<InventoryModel>();
        while (it.hasNext()) {
            InventoryModel model = it.next();
            if (ArithUtil.sub(model.Qty, model.ScannQty) == 0 && ArithUtil.sub(0f, model.ScannQty)!=0) {
                lvlist.add(model);
                it.remove();
            } else if (ArithUtil.sub(model.Qty, model.ScannQty) > 0 && ArithUtil.sub(0f, model.ScannQty)!=0) {
                huanglist.add(model);
                it.remove();
            } else if (ArithUtil.sub(model.Qty, model.ScannQty) < 0 && ArithUtil.sub(0f, model.ScannQty)!=0) {
                hlist.add(model);
                it.remove();
            }
        }
        int i = stockInfoModels.size();
        for (InventoryModel item : hlist) {
            stockInfoModels.add(i, item);
            i++;
        }
        i = stockInfoModels.size();
        for (InventoryModel item : huanglist) {
            stockInfoModels.add(i, item);
            i++;
        }
        i = stockInfoModels.size();
        for (InventoryModel item : lvlist) {
            stockInfoModels.add(i, item);
            i++;
        }
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
        com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryListAdapter.ListItemView listItemView = null;
        if (convertView == null) {
            // 获取list_item布局文件的视图
            listItemView = new com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryListAdapter.ListItemView();
            convertView = listContainer.inflate(R.layout.item_inventory_configstyle, null);
            //   listItemView.txtstrong = (TextView) convertView.findViewById(R.id.txt_strongcode);
            listItemView.txt_voucherNo = (TextView) convertView.findViewById(R.id.txt_voucherNo);
            listItemView.txt_reference_standard = (TextView) convertView.findViewById(R.id.txt_reference_standard);
            listItemView.txtVoucherQty = (TextView) convertView.findViewById(R.id.txtVoucherQty);
            listItemView.txtMaterialDesc = (TextView) convertView.findViewById(R.id.txtMaterialDesc);
            listItemView.txtbatchno = (TextView) convertView.findViewById(R.id.txt_batch_no);
            listItemView.txt_recommended_location = (TextView) convertView.findViewById(R.id.txt_recommended_location);
            convertView.setTag(listItemView);
        } else {
            listItemView = (com.liansu.boduowms.modules.inHouseStock.inventory.Model.InventoryListAdapter.ListItemView) convertView.getTag();
        }
        final InventoryModel mDetailInfo = stockInfoModels.get(selectID);
        listItemView.txt_voucherNo.setText(mDetailInfo.getMaterialno());
        listItemView.txt_reference_standard.setText("已盘数量：" + mDetailInfo.ScannQty);
        listItemView.txtMaterialDesc.setText(mDetailInfo.getMaterialdesc());
        listItemView.txt_recommended_location.setText("批次:" + mDetailInfo.getBatchno());
        listItemView.txtVoucherQty.setText("数量:" + mDetailInfo.getQty() + mDetailInfo.getUnit());
        listItemView.txtbatchno.setText("序列号:" + mDetailInfo.Serialno);
        if (mDetailInfo.isCheck) {
            convertView.setBackgroundResource(R.color.gray_cc);
        } else if (ArithUtil.sub(mDetailInfo.Qty, mDetailInfo.ScannQty) == 0 && ArithUtil.sub(0f, mDetailInfo.ScannQty)!=0) {
            convertView.setBackgroundResource(R.color.springgreen);
//111
        } else if (ArithUtil.sub(mDetailInfo.Qty, mDetailInfo.ScannQty) > 0 && ArithUtil.sub(0f, mDetailInfo.ScannQty)!=0) {
            convertView.setBackgroundResource(R.color.khaki);
        } else if (ArithUtil.sub(mDetailInfo.Qty, mDetailInfo.ScannQty) < 0 && ArithUtil.sub(0f, mDetailInfo.ScannQty)!=0) {
            convertView.setBackgroundResource(R.color.pink);
        } else {
            convertView.setBackgroundResource(R.color.trans);
        }
        return convertView;
    }
}
