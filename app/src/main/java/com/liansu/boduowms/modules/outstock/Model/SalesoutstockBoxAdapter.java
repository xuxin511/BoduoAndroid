package com.liansu.boduowms.modules.outstock.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.ui.adapter.outstock.packing.PackingScanAdapter;
import com.liansu.boduowms.utils.function.ArithUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class SalesoutstockBoxAdapter extends BaseAdapter {
    private Context mContext; // 运行上下文
    private List<OutStockOrderDetailInfo> stockInfoModels; // 信息集合
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
        public TextView txt_batch_no;
        public LinearLayout linearLayout;
    }

    public SalesoutstockBoxAdapter(Context mContext, List<OutStockOrderDetailInfo> stockInfoModels) {
        this.mContext = mContext;
        listContainer = LayoutInflater.from(mContext); // 创建视图容器并设置上下文
        this.stockInfoModels = stockInfoModels;

        Iterator<OutStockOrderDetailInfo> it = this.stockInfoModels.iterator();
        List<OutStockOrderDetailInfo> list = new ArrayList<OutStockOrderDetailInfo>();
        while (it.hasNext()) {
            OutStockOrderDetailInfo model = it.next();
            if (model.getRemainqty() == 0) {
                list.add(model);
                it.remove();
            }
        }
        int i = stockInfoModels.size();
        for (OutStockOrderDetailInfo item : list) {
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
        com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxAdapter.ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxAdapter.ListItemView();

            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_offshelfscandetail_listview, null);
            listItemView.txtstrong = (TextView) convertView.findViewById(R.id.txt_strongcode);
            listItemView.txt_voucherNo = (TextView) convertView.findViewById(R.id.txt_voucherNo);
            listItemView.txt_reference_standard = (TextView) convertView.findViewById(R.id.txt_reference_standard);
            listItemView.txtVoucherQty = (TextView) convertView.findViewById(R.id.txtVoucherQty);
            listItemView.txtRemainQty = (TextView) convertView.findViewById(R.id.txtRemainQty);
            listItemView.txtScanQty = (TextView) convertView.findViewById(R.id.txtScanQty);
            listItemView.txtMaterialDesc = (TextView) convertView.findViewById(R.id.txtMaterialDesc);
            listItemView.txt_recommended_location = (TextView) convertView.findViewById(R.id.txt_recommended_location);
            listItemView.txt_batch_no = (TextView) convertView.findViewById(R.id.txt_batch_no);
            listItemView.txt_recommended_location.setVisibility(View.GONE);
            listItemView.txt_batch_no.setVisibility(View.GONE);
            listItemView.linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout2);
            listItemView.linearLayout.setVisibility(View.GONE);
            convertView.setTag(listItemView);
        } else {
            listItemView = (com.liansu.boduowms.modules.outstock.Model.SalesoutstockBoxAdapter.ListItemView) convertView.getTag();
        }
        OutStockOrderDetailInfo mDetailInfo = stockInfoModels.get(selectID);
        //  listItemView.txt_voucherNo.setText(mDetailInfo.getErpvoucherno());
//            if (mDetailInfo.getVouchertype() != 46) { //不等于领料委外单的情况下显示  零头散件 包装量
//                if (mDetailInfo.getMaterialCartonNum() != 0 || mDetailInfo.getMaterialPartNum() != 0) {
//                    listItemView.txt_reference_standard.setText("整件:" + mDetailInfo.getMaterialCartonNum() + "/零头:" + mDetailInfo.getMaterialPartNum());
//                }
//                listItemView.txtstrong.setText("包装:" + String.valueOf(mDetailInfo.getPackQty()));
//            }
        listItemView.txt_voucherNo.setText(mDetailInfo.getMaterialno());
        listItemView.txtMaterialDesc.setText(mDetailInfo.getMaterialdesc());
        if (mDetailInfo.getIsStockCombine() == 1) {
            listItemView.txt_reference_standard.setText("");
            listItemView.txtRemainQty.setText("");
            listItemView.txtVoucherQty.setText("");
            listItemView.txtScanQty.setText("拼箱数量：" + mDetailInfo.getQty());
            listItemView.txt_recommended_location.setText("批次:无");
        } else {
            listItemView.txt_reference_standard.setText("下架数量:" + mDetailInfo.getQty());
            listItemView.txtVoucherQty.setText("散件数:" + mDetailInfo.getPackageNum());
            listItemView.txtRemainQty.setText("已拼数:" + mDetailInfo.getReviewQty());
            listItemView.txtScanQty.setText("未拼数：" + ArithUtil.sub(mDetailInfo.getPackageNum(), mDetailInfo.getReviewQty()));
            //  listItemView.txt_recommended_location.setText("批次:" + mDetailInfo.getBatchno());
        }
        listItemView.txtMaterialDesc.setText(mDetailInfo.getMaterialdesc());
        if (mDetailInfo.getReviewQty() > 0 && mDetailInfo.getRemainqty() != 0) {
            convertView.setBackgroundResource(R.color.khaki);
        } else if (mDetailInfo.getRemainqty() == 0) {
            convertView.setBackgroundResource(R.color.springgreen);
        } else {
            convertView.setBackgroundResource(R.color.trans);
        }
        return convertView;
    }
}


