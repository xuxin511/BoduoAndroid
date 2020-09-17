package com.liansu.boduowms.ui.adapter.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liansu.boduowms.R;
import com.liansu.boduowms.bean.menu.MenuChildrenInfo;

import java.util.List;

import androidx.annotation.DrawableRes;

/**
 * @desc: 菜单GridView适配器
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/9/16 13:10
 */
public class MenuItemAdapter extends BaseAdapter {

    private Context                mContext;
    private List<MenuChildrenInfo> mMenuChildrenInfoList;
    private int                    mBackgroundColor = -1;

    public MenuItemAdapter(Context context, List<MenuChildrenInfo> childrenInfoList) {
        this.mContext = context;
        this.mMenuChildrenInfoList = childrenInfoList;
    }

    public void setBackgroundColor(@DrawableRes int id) {
        mBackgroundColor = id;
    }

    @Override
    public int getCount() {
        return mMenuChildrenInfoList == null ? 0 : mMenuChildrenInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMenuChildrenInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_main_gridview, null);
        }

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.ItemImage);
        TextView textView = (TextView) convertView.findViewById(R.id.ItemText);
        ImageView backImage = (ImageView) convertView.findViewById(R.id.item_background);
        MenuChildrenInfo info = mMenuChildrenInfoList.get(position);
        int iconId=R.drawable.app_logo;
        if (info.getIcon()!=null){
             iconId = (Integer) info.getIcon();
        }
        Glide.with(mContext)
                .asDrawable()
                .load(mContext.getResources().getDrawable(iconId))
                .fitCenter()
//                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);

        textView.setText(info.getTitle());
        if (mBackgroundColor != -1) {
            backImage.setImageDrawable(mContext.getResources().getDrawable(mBackgroundColor));
        } else {
            backImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.blue_background));

        }
        return convertView;
    }
}

