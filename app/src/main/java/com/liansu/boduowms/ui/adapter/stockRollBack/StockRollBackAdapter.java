package com.liansu.boduowms.ui.adapter.stockRollBack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.stock.VoucherDetailSubInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by GHOST on 2017/1/13.
 */

public class StockRollBackAdapter extends RecyclerView.Adapter<StockRollBackAdapter.ViewHolder> implements View.OnClickListener {
    private Context                    context; // 运行上下文
    private List<VoucherDetailSubInfo> mStockList; // 信息集合
    private LayoutInflater             listContainer; // 视图容器
    private RecyclerView    mRecyclerView;

    public interface OnItemClickListener {//也可以不在这个activity或者是fragment中来声明接口，可以在项目中单独创建一个interface，就改成static就OK

        //参数（父组件，当前单击的View,单击的View的位置，数据）
        void onItemClick(RecyclerView parent, View view, int position, VoucherDetailSubInfo data);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    private OnItemClickListener mOnItemClickListener;//声明一下这个接口

    //提供setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    public StockRollBackAdapter(Context context, List<VoucherDetailSubInfo> list) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.mStockList = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock_roll_stock, parent, false);
        view.setOnClickListener(this);//设置监听器
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VoucherDetailSubInfo info = mStockList.get(position);
        holder.txt_barcode.setText("条码:"+info.getBarcode());
        holder.txt_material_no.setText("料号:" + info.getMaterialno());
        holder.txt_batch_no.setText("批次:" + info.getBatchno());
        holder.txt_qty.setText("库存数量:" + info.getQty());
        holder.txt_material_desc.setText("品名:" + info.getMaterialdesc());

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mStockList == null ? 0 : mStockList.size();
    }

    @Override
    public void onClick(View v) {
        //程序执行到此，会去执行具体实现的onItemClick()方法
        if (mOnItemClickListener != null && mRecyclerView != null) {
            //根据RecyclerView获得当前View的位置
            int position = mRecyclerView.getChildAdapterPosition(v);
            mOnItemClickListener.onItemClick(mRecyclerView, v, position, mStockList.get(position));
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_barcode;
        public TextView txt_material_no;
        public TextView txt_batch_no;
        public TextView txt_qty;
        public TextView txt_area_no;
        public TextView txt_material_desc;
        public TextView txt_task_qty;
        public View     rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_barcode = (TextView) itemView.findViewById(R.id.txt_barcode);
            txt_material_no = (TextView) itemView.findViewById(R.id.txt_material_no);
            txt_batch_no = (TextView) itemView.findViewById(R.id.txt_batch_no);
            txt_qty = (TextView) itemView.findViewById(R.id.txt_qty);
            txt_area_no = (TextView) itemView.findViewById(R.id.txt_area_no);
            txt_material_desc = (TextView) itemView.findViewById(R.id.txt_material_desc);
            txt_task_qty = (TextView) itemView.findViewById(R.id.txt_task_qty);
            rootView = itemView;
        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.mRecyclerView = null;
    }

}
