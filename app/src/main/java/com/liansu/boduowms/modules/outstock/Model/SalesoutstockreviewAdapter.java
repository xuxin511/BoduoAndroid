package com.liansu.boduowms.modules.outstock.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.utils.function.ArithUtil;

import java.util.ArrayList;
import java.util.List;

public class SalesoutstockreviewAdapter extends BaseAdapter {

    private Context context; // 运行上下文
    private List<OutStockOrderDetailInfo> outStockTaskDetailsInfoModels; // 信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合

        public TextView txt_voucherNo; //单号
        public TextView txt_reference_standard;//推荐库位
        public TextView txtVoucherQty;//订单数量
        public TextView txtRemainQty;
        public TextView txtScanQty;
        public TextView txtMaterialDesc;//
        public TextView txt_recommended_location;

    }

    public SalesoutstockreviewAdapter(Context context, List<OutStockOrderDetailInfo> outStockTaskDetailsInfoModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
//        List<OutStockOrderDetailInfo> list=new ArrayList<OutStockOrderDetailInfo>();
//        list=outStockTaskDetailsInfoModels;
//        for (OutStockOrderDetailInfo item:outStockTaskDetailsInfoModels){
//             if(item.getRemainqty()==0){
//                 list.remove(item);
//                 list.add(outStockTaskDetailsInfoModels.size(),item);
//             }
//        }
        this.outStockTaskDetailsInfoModels = outStockTaskDetailsInfoModels;

    }

    @Override
    public int getCount() {
        return outStockTaskDetailsInfoModels == null ? 0 : outStockTaskDetailsInfoModels.size();
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
        com.liansu.boduowms.modules.outstock.Model.SalesoutstockreviewAdapter.ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new com.liansu.boduowms.modules.outstock.Model.SalesoutstockreviewAdapter.ListItemView();

            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_offshelfscandetail_listview, null);
            listItemView.txt_voucherNo = (TextView) convertView.findViewById(R.id.txt_voucherNo);
            //   listItemView.txt_reference_standard = (TextView) convertView.findViewById(R.id.txt_reference_standard);
            listItemView.txtVoucherQty = (TextView) convertView.findViewById(R.id.txtVoucherQty);
            listItemView.txtRemainQty = (TextView) convertView.findViewById(R.id.txtRemainQty);
            listItemView.txtScanQty = (TextView) convertView.findViewById(R.id.txtScanQty);
            listItemView.txtMaterialDesc = (TextView) convertView.findViewById(R.id.txtMaterialDesc);
            //     listItemView.txt_recommended_location = (TextView) convertView.findViewById(R.id.txt_recommended_location);
            convertView.setTag(listItemView);
        } else {
            listItemView = (com.liansu.boduowms.modules.outstock.Model.SalesoutstockreviewAdapter.ListItemView) convertView.getTag();
        }
        final OutStockOrderDetailInfo mDetailInfo = outStockTaskDetailsInfoModels.get(selectID);
        listItemView.txt_voucherNo.setText(mDetailInfo.getMaterialno()); //单号
//        if (mDetailInfo.getMaterialCartonNum() != 0 || mDetailInfo.getMaterialPartNum() != 0) {
//            listItemView.txt_reference_standard.setText("外箱:" + mDetailInfo.getMaterialCartonNum() + "/零头:" + mDetailInfo.getMaterialPartNum());
//        }
        Float QTY=ArithUtil.sub(mDetailInfo.getRemainqty(),mDetailInfo.getScanqty());
        listItemView.txtVoucherQty.setText("复核数量:" + mDetailInfo.getRemainqty());
        listItemView.txtRemainQty.setText("剩余数量:"+ ArithUtil.sub(mDetailInfo.getRemainqty(),mDetailInfo.getScanqty()));
        listItemView.txtScanQty.setText("已复核：" + mDetailInfo.getScanqty());
//        listItemView.txt_recommended_location.setText("推荐库位:"+mDetailInfo.getAreano());
        listItemView.txtMaterialDesc.setText("物料名称:" + mDetailInfo.getMaterialdesc());
        if (QTY>0&&QTY<mDetailInfo.getRemainqty()) {//已扫数量
            convertView.setBackgroundResource(R.color.khaki);
        } else if ( ArithUtil.sub( mDetailInfo.getRemainqty() ,mDetailInfo.getScanqty())==0) {
            convertView.setBackgroundResource(R.color.springgreen);
        } else {
            convertView.setBackgroundResource(R.color.trans);
        }
        return convertView;
    }

}
