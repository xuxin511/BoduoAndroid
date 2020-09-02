package com.liansu.boduowms.modules.outstock.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.order.OutStockOrderDetailInfo;
import com.liansu.boduowms.utils.function.GsonUtil;

import java.util.ArrayList;
import java.util.List;

public class SalesoutstockAdapter extends BaseAdapter {
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
        public TextView txtstrong;
        public TextView txtbatchno;


    }

    public SalesoutstockAdapter(Context context, List<OutStockOrderDetailInfo> outStockTaskDetailsInfoModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.outStockTaskDetailsInfoModels = outStockTaskDetailsInfoModels;
      //  List<OutStockOrderDetailInfo> list=new ArrayList<OutStockOrderDetailInfo>();
       // String json = GsonUtil.parseModelToJson(outStockTaskDetailsInfoModels);
      //  list= GsonUtil.parseJsonToModel(json,OutStockOrderDetailInfo.class);
//        for (OutStockOrderDetailInfo item:outStockTaskDetailsInfoModels) {
//            if (item.getRemainqty() == 0) {
//                list.add(item);
//                outStockTaskDetailsInfoModels.remove(item);
//            }
//        }
//        int i=outStockTaskDetailsInfoModels.size();
//        for (OutStockOrderDetailInfo item:list) {
//            outStockTaskDetailsInfoModels.add(i, item);
//            i++;
//        }

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
        com.liansu.boduowms.modules.outstock.Model.SalesoutstockAdapter.ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new com.liansu.boduowms.modules.outstock.Model.SalesoutstockAdapter.ListItemView();

            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_offshelfscandetail_listview, null);
            listItemView.txtstrong = (TextView) convertView.findViewById(R.id.txt_strongcode);
            listItemView.txt_voucherNo = (TextView) convertView.findViewById(R.id.txt_voucherNo);
            listItemView.txt_reference_standard = (TextView) convertView.findViewById(R.id.txt_reference_standard);

            listItemView.txtVoucherQty = (TextView) convertView.findViewById(R.id.txtVoucherQty);
            listItemView.txtRemainQty = (TextView) convertView.findViewById(R.id.txtRemainQty);
            listItemView.txtScanQty = (TextView) convertView.findViewById(R.id.txtScanQty);
            listItemView.txtMaterialDesc = (TextView) convertView.findViewById(R.id.txtMaterialDesc);
            listItemView.txtbatchno = (TextView) convertView.findViewById(R.id.txt_batch_no);

            listItemView.txt_recommended_location = (TextView) convertView.findViewById(R.id.txt_recommended_location);
            convertView.setTag(listItemView);
        } else {
            listItemView = (com.liansu.boduowms.modules.outstock.Model.SalesoutstockAdapter.ListItemView) convertView.getTag();
        }
        final OutStockOrderDetailInfo mDetailInfo = outStockTaskDetailsInfoModels.get(selectID);
        listItemView.txt_voucherNo.setText(mDetailInfo.getMaterialno());
        if(mDetailInfo.getVouchertype()!=46 && mDetailInfo.getVouchertype()!=30 & mDetailInfo.getVouchertype()!=25& mDetailInfo.getVouchertype()!=55) { //不等于领料委外单的情况下显示  零头散件 包装量
            if (mDetailInfo.getMaterialCartonNum() != 0 || mDetailInfo.getMaterialPartNum() != 0) {
                listItemView.txt_reference_standard.setText("整件:" + mDetailInfo.getMaterialCartonNum() + "/零头:" + mDetailInfo.getMaterialPartNum());
            }
            listItemView.txtstrong.setText("包装:" + String.valueOf(mDetailInfo.getPackQty()));
        }else {
            listItemView.txt_reference_standard.setText("规格型号:" + mDetailInfo.getSpec());
        }
        String batchno="";
        if(mDetailInfo.getBatchno()!=null)
            batchno=mDetailInfo.getBatchno();
        listItemView.txtbatchno.setText("批次:" +batchno);
        listItemView.txtVoucherQty.setText("订单：" + mDetailInfo.getVoucherqty());
        listItemView.txtRemainQty.setText("剩余:" + mDetailInfo.getRemainqty());
        listItemView.txtScanQty.setText("已扫：" + mDetailInfo.getScanqty());
        listItemView.txt_recommended_location.setText("推荐库位:"+mDetailInfo.getAreano());
        listItemView.txtMaterialDesc.setText(mDetailInfo.getMaterialdesc());


//             listItemView.txt_voucherNo.setText("12345678901");
//        if (mDetailInfo.getMaterialCartonNum() != 0 || mDetailInfo.getMaterialPartNum() != 0) {
//            listItemView.txt_reference_standard.setText("整件:1111" + "/零头:1111");
//        }
//        listItemView.txtstrong.setText(String.valueOf("包装:"+mDetailInfo.getPackQty()));
//        listItemView.txtVoucherQty.setText("订单：" + mDetailInfo.getVoucherqty());
//        listItemView.txtRemainQty.setText("剩余:" + mDetailInfo.getRemainqty());
//        listItemView.txtScanQty.setText("已扫：" + mDetailInfo.getScanqty());
//        listItemView.txt_recommended_location.setText("推荐库位:"+mDetailInfo.getAreano());
//        listItemView.txtMaterialDesc.setText("物料名称:" + mDetailInfo.getMaterialdesc());


        if (mDetailInfo.getRemainqty() > 0 && mDetailInfo.getRemainqty() < mDetailInfo.getVoucherqty()) {
            convertView.setBackgroundResource(R.color.khaki);
        } else if (mDetailInfo.getRemainqty() == 0) {
            convertView.setBackgroundResource(R.color.springgreen);
        } else {
            convertView.setBackgroundResource(R.color.trans);
        }
        return convertView;
    }


}
