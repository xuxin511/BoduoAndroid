package com.liansu.boduowms.ui.adapter.instock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.stock.StockInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by GHOST on 2017/1/13.
 */

public class InventoryMovementScanDetailAdapter extends RecyclerView.Adapter<InventoryMovementScanDetailAdapter.ViewHolder> {
    private Context              context; // 运行上下文
    private List<StockInfo> mOrderDetailList; // 信息集合
    private LayoutInflater       listContainer; // 视图容器

    public InventoryMovementScanDetailAdapter(Context context, List<StockInfo> mOrderDetailList) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.mOrderDetailList = mOrderDetailList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_resource_detail_listview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StockInfo barcodeInfo = mOrderDetailList.get(position);
        holder.mMaterialNo.setText("料号:"+barcodeInfo.getMaterialno());
        holder.mBatchNo.setText("批次:"+barcodeInfo.getBatchno());
        holder.mAreaNo.setText("库位:"+barcodeInfo.getAreano());
        holder.mQty.setText("数量:"+ barcodeInfo.getQty());
        holder.mMaterialDesc.setText("品名:"+ barcodeInfo.getMaterialdesc());
        holder.item_barcode.setText(""+barcodeInfo.getBarcode());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mOrderDetailList == null ? 0 : mOrderDetailList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mMaterialNo;
        public TextView mQty;
        public TextView mBatchNo;
        public TextView mAreaNo;
        public TextView mMaterialDesc;
        public TextView item_barcode;
        public View     rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            mMaterialNo = (TextView) itemView.findViewById(R.id.item_material_no);
            mQty = (TextView) itemView.findViewById(R.id.item_qty);
            mBatchNo = (TextView) itemView.findViewById(R.id.item_batch_no);
            mAreaNo = (TextView) itemView.findViewById(R.id.item_area_no);
            mMaterialDesc = (TextView) itemView.findViewById(R.id.item_material_desc);
            item_barcode = (TextView) itemView.findViewById(R.id.item_barcode);
            rootView = itemView;
        }
    }

}
