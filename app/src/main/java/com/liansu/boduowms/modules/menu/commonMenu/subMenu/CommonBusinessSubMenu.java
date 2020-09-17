package com.liansu.boduowms.modules.menu.commonMenu.subMenu;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.menu.MenuChildrenInfo;
import com.liansu.boduowms.modules.menu.commonMenu.ICommonMenuView;
import com.liansu.boduowms.ui.adapter.menu.MenuItemAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * @ Des:  出库二级菜单
 * @ Created by yangyiqing on 2020/8/4.
 */
@ContentView(R.layout.fragment_menu)
public class CommonBusinessSubMenu extends BaseActivity implements ICommonMenuView {

    @ViewInject(R.id.gv_Function)
    GridView mGridView;
    MenuItemAdapter              mAdapter;
    Context                          mContext = CommonBusinessSubMenu.this;
    String                         mBusinessType;
    CommonBusinessSubMenuPresenter mPresenter;

    @Override
    protected void initViews() {
        BaseApplication.context = mContext;
        List<MenuChildrenInfo> list=getIntent().getParcelableArrayListExtra("MENU_CHILDREN_INFO");
        mBusinessType = getIntent().getStringExtra("BusinessType").toString();
        if (mBusinessType != null) {
            mPresenter = new CommonBusinessSubMenuPresenter(mContext,this,list);
            BaseApplication.toolBarTitle = new ToolBarTitle(mBusinessType + " 二级菜单-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno(), false);
        }
        x.view().inject(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter!=null){
            mPresenter.loadMenuList();
        }
    }


    @Override
    public void bindMenuList(List<MenuChildrenInfo> menuList) {
        if (menuList != null ) {
            if (mAdapter==null){
                mAdapter = new MenuItemAdapter(mContext, menuList);
                mGridView.setAdapter(mAdapter);
            }
            mAdapter.notifyDataSetChanged();

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
