package com.liansu.boduowms.ui.adapter.instock.productReturn;

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


/**  生成退
 * Created by GHOST on 2017/1/13.
 */

public class ProductReturnAdapter extends RecyclerView.Adapter<ProductReturnAdapter.ViewHolder>  {
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    private Context               context; // 运行上下文
    private List<OrderDetailInfo> receiptDetailModels; // 信息集合
    private LayoutInflater        listContainer; // 视图容器
    private String                receiptType = "";

    //私有属性
    OnItemClickListener mOnItemClickListener;

    public ProductReturnAdapter(Context context,List<OrderDetailInfo> receiptDetailModels) {
        this.context = context;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
        this.receiptDetailModels = receiptDetailModels;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_return_listview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        OrderDetailInfo orderDetailInfo = receiptDetailModels.get(position);
        holder.txt_material_no.setText(orderDetailInfo.getMaterialno());
        holder.txt_batch_no.setText(orderDetailInfo.getBatchno());
        holder.txt_material_desc.setText(orderDetailInfo.getMaterialdesc());
        holder.txt_voucher_qty.setText("订单数：" + orderDetailInfo.getVoucherqty());
        if (orderDetailInfo.getRemainqty() != 0 && ArithUtil.sub(orderDetailInfo.getVoucherqty(), orderDetailInfo.getRemainqty()) > 0) {
            holder.rootView.setBackgroundResource(R.color.khaki);
        } else if (orderDetailInfo.getRemainqty() == 0) {
            holder.rootView.setBackgroundResource(R.color.springgreen);
        } else {
            holder.rootView.setBackgroundResource(R.color.trans);
        }
        //实现点击效果
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
//                    mOnItemClickListener.onItemClick(view, note, position);
                }
            }
        });
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

        public TextView txt_material_no;
        public TextView txt_batch_no;
        public TextView txt_material_desc;
        public TextView txt_voucher_qty;
        public View     rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_material_no = (TextView) itemView.findViewById(R.id.txt_material_no);
            txt_batch_no = (TextView) itemView.findViewById(R.id.txt_batch_no);
            txt_material_desc = (TextView) itemView.findViewById(R.id.txt_material_desc);
            txt_voucher_qty = (TextView) itemView.findViewById(R.id.txt_voucher_qty);
            rootView = itemView;
        }
    }

}
