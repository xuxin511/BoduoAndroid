package com.liansu.boduowms.ui.adapter.instock.baseScanStorage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.utils.function.ArithUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by GHOST on 2017/1/13.
 */

public class BaseScanDetailAdapter extends RecyclerView.Adapter<BaseScanDetailAdapter.ViewHolder> {
    private Context               context; // 运行上下文
    private List<OrderDetailInfo> receiptDetailModels; // 信息集合
    private LayoutInflater        listContainer; // 视图容器
    private String                receiptType = "";


    public BaseScanDetailAdapter(Context context, String ReceiptType, List<OrderDetailInfo> receiptDetailModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.receiptDetailModels = receiptDetailModels;
        receiptType = ReceiptType;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receiptscandetail_listview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetailInfo receiptDetailModel = receiptDetailModels.get(position);
        holder.txtbarcode.setText(receiptDetailModel.getMaterialno());
//        holder.txtBatchNo.setText("批次:" + receiptDetailModel.getBatchno());
        holder.txtVoucherQty.setText("订单数:" + receiptDetailModel.getVoucherqty());
        holder.txtScanNum.setText("已扫数：" + receiptDetailModel.getScanqty());
        holder.txtRemainQty.setText("待收数：" + receiptDetailModel.getRemainqty());
        holder.txtMaterialDesc.setText(receiptDetailModel.getMaterialdesc());
        if (receiptDetailModel.getRemainqty() != 0 && ArithUtil.sub(receiptDetailModel.getVoucherqty(), receiptDetailModel.getRemainqty()) > 0) {
            holder.rootView.setBackgroundResource(R.color.khaki);
        } else if (receiptDetailModel.getRemainqty() == 0) {
            holder.rootView.setBackgroundResource(R.color.springgreen);
        } else {
            holder.rootView.setBackgroundResource(R.color.trans);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return receiptDetailModels == null ? 0 : receiptDetailModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtbarcode;
        public TextView txtScanNum;
        public TextView txtRemainQty;
        public TextView txtMaterialDesc;
        public TextView txtBatchNo;
        public TextView txtVoucherQty;
        public View     rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtbarcode = (TextView) itemView.findViewById(R.id.txtbarcode);
            txtScanNum = (TextView) itemView.findViewById(R.id.txtScanNum);
            txtRemainQty = (TextView) itemView.findViewById(R.id.txtRemainQty);
            txtMaterialDesc = (TextView) itemView.findViewById(R.id.txtMaterialDesc);
            txtBatchNo = (TextView) itemView.findViewById(R.id.txtbatchno);
            txtVoucherQty = (TextView) itemView.findViewById(R.id.txtVoucherQty);
            rootView = itemView;
        }
    }

}
