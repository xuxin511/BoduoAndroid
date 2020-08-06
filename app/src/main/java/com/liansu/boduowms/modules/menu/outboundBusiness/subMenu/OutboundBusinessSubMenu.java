package com.liansu.boduowms.modules.menu.outboundBusiness.subMenu;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.modules.menu.IMenuView;
import com.liansu.boduowms.ui.adapter.GridViewItemAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.Map;

/**
 * @ Des:  出库二级菜单
 * @ Created by yangyiqing on 2020/8/4.
 */
@ContentView(R.layout.fragment_menu)
public class OutboundBusinessSubMenu extends BaseActivity implements IMenuView {

    @ViewInject(R.id.gv_Function)
    GridView mGridView;
    GridViewItemAdapter              mAdapter;
    Context                          mContext = OutboundBusinessSubMenu.this;
    String                           mBusinessType;
    OutboundBusinessSubMenuPresenter mPresenter;

    @Override
    protected void initViews() {
        BaseApplication.context = mContext;
        mBusinessType = getIntent().getStringExtra("BusinessType").toString();
        if (mBusinessType != null) {
            mPresenter = new OutboundBusinessSubMenuPresenter(mContext,this);
            BaseApplication.toolBarTitle = new ToolBarTitle(mBusinessType + " 二级菜单-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename(), false);
        }
        x.view().inject(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter!=null){
            mPresenter.loadMenuList(mBusinessType);
        }
    }

    @Override
    public void bindMenuList(List<Map<String, Object>> menuList) {
        if (menuList!=null && menuList.size()>0){
            mAdapter = new GridViewItemAdapter(mContext,menuList);
            mGridView.setAdapter(mAdapter);
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
        LinearLayout linearLayout = (LinearLayout) mGridView.getAdapter().getView(position, view, null);
        TextView item = ((TextView) linearLayout.getChildAt(1));
        String moduleName = null;
        if (item != null) {
            moduleName=item.getText().toString().trim();
        }
        if (mPresenter != null && moduleName!=null) {
            mPresenter.loadBusiness(moduleName,mBusinessType);
        }
    }
}
