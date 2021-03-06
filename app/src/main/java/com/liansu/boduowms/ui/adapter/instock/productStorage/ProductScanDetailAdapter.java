package com.liansu.boduowms.ui.adapter.instock.productStorage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.order.OrderDetailInfo;
import com.liansu.boduowms.ui.adapter.instock.baseScanStorage.BaseOrderLabelPrintDetailAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by GHOST on 2017/1/13.
 */

public class ProductScanDetailAdapter extends RecyclerView.Adapter<ProductScanDetailAdapter.ViewHolder> implements View.OnClickListener {
    private Context               context; // 运行上下文
    private List<OrderDetailInfo> receiptDetailModels; // 信息集合
    private LayoutInflater        listContainer; // 视图容器
    private String                receiptType = "";
    private RecyclerView          mRecyclerView;

    public interface OnItemClickListener {//也可以不在这个activity或者是fragment中来声明接口，可以在项目中单独创建一个interface，就改成static就OK

        //参数（父组件，当前单击的View,单击的View的位置，数据）
        void onItemClick(RecyclerView parent, View view, int position, OrderDetailInfo data);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    private BaseOrderLabelPrintDetailAdapter.OnItemClickListener mOnItemClickListener;//声明一下这个接口

    //提供setter方法
    public void setOnItemClickListener(BaseOrderLabelPrintDetailAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public ProductScanDetailAdapter(Context context, String ReceiptType, List<OrderDetailInfo> receiptDetailModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.receiptDetailModels = receiptDetailModels;
        receiptType = ReceiptType;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receiptscandetail_listview, parent, false);
        view.setOnClickListener(this);//设置监听器
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetailInfo receiptDetailModel = receiptDetailModels.get(position);
        holder.txtbarcode.setText(receiptDetailModel.getMaterialno());
//        holder.txtBatchNo.setText("批次:"+receiptDetailModel.getBatchno());
        holder.txtVoucherQty.setText("订单:" + receiptDetailModel.getVoucherqty());
        holder.txtScanNum.setText("已打:" + receiptDetailModel.getScanqty());
        holder.txtRemainQty.setText("待打:" + receiptDetailModel.getRemainqty());
        holder.txtMaterialDesc.setText(receiptDetailModel.getMaterialdesc());
        if (receiptDetailModel.getScanqty() != 0 && (
                receiptDetailModel.getScanqty() - (Math.abs(receiptDetailModel.getRemainqty())) < 0)) {
            holder.rootView.setBackgroundResource(R.color.khaki);
        } else if (receiptDetailModel.getScanqty() - (Math.abs(receiptDetailModel.getRemainqty())) == 0) {
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

    @Override
    public void onClick(View v) {
//程序执行到此，会去执行具体实现的onItemClick()方法
        if (mOnItemClickListener != null && mRecyclerView != null) {
            //根据RecyclerView获得当前View的位置
            int position = mRecyclerView.getChildAdapterPosition(v);
            mOnItemClickListener.onItemClick(mRecyclerView, v, position, receiptDetailModels.get(position));
        }
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
