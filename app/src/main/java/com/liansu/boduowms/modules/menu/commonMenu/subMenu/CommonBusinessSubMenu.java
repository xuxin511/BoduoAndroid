package com.liansu.boduowms.modules.menu.commonMenu.subMenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.bean.menu.MenuChildrenInfo;
import com.liansu.boduowms.modules.menu.commonMenu.ICommonMenuView;
import com.liansu.boduowms.modules.outstock.Model.MenuOutStockModel;
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.ui.adapter.menu.MenuItemAdapter;
import com.liansu.boduowms.utils.function.GsonUtil;

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
public class CommonBusinessSubMenu extends BaseActivity implements ICommonMenuView , IUserSettingView {

    @ViewInject(R.id.gv_Function)
    GridView mGridView;
    MenuItemAdapter              mAdapter;
    Context                          mContext = CommonBusinessSubMenu.this;
    CommonBusinessSubMenuPresenter mPresenter;
    protected UserSettingPresenter mUserSettingPresenter;
    MenuOutStockModel menuOutStockModel=new  MenuOutStockModel();
    @Override
    protected void initViews() {
        super.initViews();
        BaseApplication.context = mContext;
        Intent intentMain = getIntent();
        Uri data = intentMain.getData();
        String arr = data.toString();
        menuOutStockModel = GsonUtil.parseJsonToModel(arr, MenuOutStockModel.class);
        List<MenuChildrenInfo> list=menuOutStockModel.secondMenuList;
        BaseApplication.toolBarTitle = new ToolBarTitle(menuOutStockModel.Title+"-"+BaseApplication.mCurrentWareHouseInfo.getWarehouseno(), false);
        mPresenter = new CommonBusinessSubMenuPresenter(mContext,this,list);
        x.view().inject(this);
        mUserSettingPresenter = new UserSettingPresenter(CommonBusinessSubMenu.this, this);
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
    @Override
    public void selectWareHouse(List<String> list) {
        if (list != null && list.size() > 0) {
            final String[] items = list.toArray(new String[0]);
            new AlertDialog.Builder(mContext).setTitle(getResources().getString(R.string.activity_login_WareHousChoice))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setCancelable(true)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO 自动生成的方法存根
                            String select_item = items[which].toString();
                            if (mUserSettingPresenter != null) {
                                mUserSettingPresenter.saveCurrentWareHouse(select_item);
                            }

                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public void setTitle() {
        getToolBarHelper().getToolBar().setTitle(menuOutStockModel.Title + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_setting_warehouse_select) {
            selectWareHouse(mUserSettingPresenter.getModel().getWareHouseNoList());
        }
        return false;
    }


}
