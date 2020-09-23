package com.liansu.boduowms.modules.menu.commonMenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseFragment;
import com.liansu.boduowms.bean.menu.MenuChildrenInfo;
import com.liansu.boduowms.bean.menu.MenuType;
import com.liansu.boduowms.ui.adapter.menu.MenuItemAdapter;
import com.liansu.boduowms.ui.widget.LineGridView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @desc: 二级菜单
 * @author: Nietzsche
 * @time 2020/6/25 16:11
 */
@ContentView(R.layout.fragment_menu)
public class CommonBusinessMenuFragment extends BaseFragment implements ICommonMenuView {
    @ViewInject(R.id.gv_Function)
    LineGridView mGridView;
    MenuItemAdapter mAdapter;
    Context         mContext = getActivity();
    private CommonBusinessMenuPresenter mPresenter;
    int mMenuType= MenuType.MENU_TYPE_NONE;
   public  CommonBusinessMenuFragment (){
       mMenuType=MenuType.MENU_TYPE_IN_STOCK;
   }
    public CommonBusinessMenuFragment(int menuType){
       super();
        mMenuType=menuType;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        // 在界面onAttach之后就触发初始化Presenter

        mPresenter = new CommonBusinessMenuPresenter(this, mContext,mMenuType);

    }


    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPresenter!=null){
            mPresenter.loadMenuList();
        }
    }


    @Override
    public void bindMenuList(List<MenuChildrenInfo> menuList) {
        if (menuList != null ) {
//            if (mAdapter==null){
                mAdapter = new MenuItemAdapter(mContext, menuList);
                mGridView.setAdapter(mAdapter);
//            }
//            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void loadBusiness(Intent intent) {
        if (intent != null && intent.resolveActivity(mContext.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Event(value = R.id.gv_Function, type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MenuChildrenInfo item= (MenuChildrenInfo) mAdapter.getItem(position);
        if (mPresenter != null && item!=null) {
            mPresenter.loadBusiness(item);
        }
    }
}
