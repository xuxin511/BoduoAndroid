package com.liansu.boduowms.modules.menu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.ToolBarTitle;
import com.liansu.boduowms.modules.menu.inHouseManagementMenu.InHouseManagementMenuFragment;
import com.liansu.boduowms.modules.menu.outboundBusiness.OutboundBusinessMenuFragment;
import com.liansu.boduowms.modules.menu.storageBusinessMenu.StorageBusinessMenuFragment;
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.ui.widget.NavHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import androidx.annotation.NonNull;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener<Integer>, IUserSettingView {

    @ViewInject(R.id.lay_container)
    FrameLayout          mContainer;
    @ViewInject(R.id.navigation)
    BottomNavigationView mNavigation;
    private   NavHelper<Integer>   mNavHelper;
    protected UserSettingPresenter mUserSettingPresenter;
    Context mContext = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.context = MainActivity.this;
        BaseApplication.toolBarTitle = new ToolBarTitle(getString(R.string.main_menu_title) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename(), true);
        x.view().inject(this);
        mUserSettingPresenter = new UserSettingPresenter(MainActivity.this, this);
        // 初始化底部辅助工具类
        mNavHelper = new NavHelper<>(this, R.id.lay_container,
                getSupportFragmentManager(), this);
        mNavHelper.add(R.id.navigation_storage_business, new NavHelper.Tab<>(StorageBusinessMenuFragment.class, R.string.title_storage_business))
                .add(R.id.navigation_in_housemanagemen, new NavHelper.Tab<>(InHouseManagementMenuFragment.class, R.string.title_in_house_management))
                .add(R.id.navigation_outbound_business, new NavHelper.Tab<>(OutboundBusinessMenuFragment.class, R.string.title_outbound_business));

        // 添加对底部按钮点击的监听
        mNavigation.setOnNavigationItemSelectedListener(this);
        Menu menu = mNavigation.getMenu();
        // 触发首次选中Home
        menu.performIdentifierAction(R.id.navigation_storage_business, 0);
    }

    /**
     * 当我们的底部导航被点击的时候触发
     *
     * @param item MenuItem
     * @return True 代表我们能够处理这个点击
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // 转接事件流到工具类中
        return mNavHelper.performClickMenu(item.getItemId());
    }

    /**
     * NavHelper 处理后回调的方法
     *
     * @param newTab 新的Tab
     * @param oldTab 就的Tab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        // 从额外字段中取出我们的Title资源Id
//        mTitle.setText(newTab.extra);


    }

    @Override
    public void selectWareHouse(List<String> list) {
        if (list != null && list.size() > 0) {
            final String[] items = list.toArray(new String[0]);
            new AlertDialog.Builder(mContext).setTitle(getResources().getString(R.string.activity_login_WareHousChoice))// 设置对话框标题
                    .setIcon(android.R.drawable.ic_dialog_info)// 设置对话框图
                    .setCancelable(false)
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
        getToolBarHelper().getToolBar().setTitle(getString(R.string.main_menu_title) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_setting_warehouse_select) {
            selectWareHouse(mUserSettingPresenter.getModel().getWareHouseNameList());
        }
        return false;
    }
}
