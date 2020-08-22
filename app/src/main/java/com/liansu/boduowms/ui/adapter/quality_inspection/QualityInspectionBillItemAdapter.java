package com.liansu.boduowms.ui.adapter.quality_inspection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.qualitySpection.QualityHeaderInfo;

import java.util.List;

/**
 * Created by GHOST on 2017/1/13.
 */

public class QualityInspectionBillItemAdapter extends BaseAdapter {
    private Context                 context; // 运行上下文
    private List<QualityHeaderInfo> receiptModels; // 信息集合
    private LayoutInflater          listContainer; // 视图容器
    private int                     selectItem = -1;


    public final class ListItemView { // 自定义控件集合


        public TextView txt_erpVoucherNo;
        public TextView txtCompany;
        public TextView txt_quality_no;
        public TextView txt_materialNo;
        public TextView txt_voucherNo;     //单据类型
        public TextView txt_arrVoucherNo;  //到货单号
        public TextView txt_voucher_qty;  //订单数量
        public TextView txt_status;  //质检状态
        public TextView txt_material_desc;  //物料名称
    }

    public QualityInspectionBillItemAdapter(Context context, List<QualityHeaderInfo> receiptModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.receiptModels = receiptModels;

    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        return receiptModels == null ? 0 : receiptModels.size();
    }

    @Override
    public Object getItem(int position) {
        return receiptModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int selectID = position;
        // 自定义视图
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();

            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.item_quality_inspection_choice_listview, null);
            listItemView.txt_erpVoucherNo = (TextView) convertView.findViewById(R.id.txt_erpVoucherNo);
            listItemView.txt_materialNo = (TextView) convertView.findViewById(R.id.txt_materialNo);
            listItemView.txt_quality_no = (TextView) convertView.findViewById(R.id.txt_quality_no);
            listItemView.txt_voucherNo = (TextView) convertView.findViewById(R.id.txt_voucherNo);
            listItemView.txt_arrVoucherNo = (TextView) convertView.findViewById(R.id.txt_arrVoucherNo);
            listItemView.txt_voucher_qty = (TextView) convertView.findViewById(R.id.txt_voucher_qty);
            listItemView.txt_status= (TextView) convertView.findViewById(R.id.txt_status);
            listItemView.txt_material_desc=(TextView) convertView.findViewById(R.id.txt_material_desc);
//            LinearLayout linearLayout=(LinearLayout) convertView.findViewById(R.id.fifth_layout);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        QualityHeaderInfo headerInfo = receiptModels.get(selectID);
//                int voucherType = headerInfo.getVouchertype();
//        if (voucherType == 47) {
//            listItemView.txt_erpVoucherNo.setText("采购单号:" + headerInfo.getErpvoucherno());
//        } else if (voucherType == 48) {
//            listItemView.txt_erpVoucherNo.setText("工单号" + headerInfo.getErpvoucherno());
//        } else {
//            listItemView.txt_erpVoucherNo.setText("ERP单号:" + headerInfo.getErpvoucherno());
//        }
        listItemView.txt_quality_no.setText(headerInfo.getQualityno());
        listItemView.txt_erpVoucherNo.setText(headerInfo.getErpvoucherno());
        listItemView.txt_materialNo.setText(headerInfo.getMaterialno());
        listItemView.txt_voucher_qty.setText(headerInfo.getMaterialdesc());
        listItemView.txt_status.setText(headerInfo.getErpvoucherdesc());
        listItemView.txt_material_desc.setText(headerInfo.getMaterialdesc());
        String arrVoucherNo = headerInfo.getArrvoucherno() != null ? headerInfo.getArrvoucherno() : "";
        listItemView.txt_arrVoucherNo.setText(arrVoucherNo);
//        listItemView.txt_voucher_qty.setText("订单数量:" + headerInfo.getVoucherqty());
        return convertView;
    }

}
