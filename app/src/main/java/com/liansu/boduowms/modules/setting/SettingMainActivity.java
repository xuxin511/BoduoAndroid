package com.liansu.boduowms.modules.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.AppManager;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.modules.setting.print.SettingPrintingFragment;
import com.liansu.boduowms.modules.setting.system.SettingSystemFragment;
import com.liansu.boduowms.modules.setting.user.IUserSettingView;
import com.liansu.boduowms.modules.setting.user.UserSettingPresenter;
import com.liansu.boduowms.ui.widget.MyFragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

public class SettingMainActivity extends FragmentActivity implements ISettingView, IUserSettingView {

    TabLayout            tabLayout;
    Toolbar              mToolBar;
    Context              mContext = SettingMainActivity.this;
    UserSettingPresenter mUserSettingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout_main);
        AppManager.getAppManager().addActivity(this); //添加当前Activity到avtivity管理类
        final List<String> tabTexts = new ArrayList<>();
        tabTexts.add(getString(R.string.setting_system_info));
        tabTexts.add(getString(R.string.setting_print_info));
        mToolBar = findViewById(R.id.widget_common_tool_bar);
        if (BaseApplication.mCurrentWareHouseInfo!=null){
            mToolBar.setTitle(mContext.getResources().getString(R.string.setting_title) + "-" +BaseApplication.mCurrentWareHouseInfo.getWarehouseno() );

        }else {
            mToolBar.setTitle(mContext.getResources().getString(R.string.setting_title)  );

        }
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
        mToolBar.inflateMenu(R.menu.menu_setting);
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //利用colorFilter动态更改图标颜色
//            menuItem.getIcon().setColorFilter(Color.parseColor("#223344"),PorterDuff.Mode.MULTIPLY);

                switch (menuItem.getItemId()) {
                    case R.id.user_setting_warehouse_select:
                        if (mUserSettingPresenter != null) {
                            SettingMainActivity.this.selectWareHouse(mUserSettingPresenter.getModel().getWareHouseNoList());
                        }
                        break;

                }
                return true;
            }
        });

        //        widget_common_tool_bar
        final List<Fragment> listFragments = new ArrayList<>();
        listFragments.add(new SettingSystemFragment());
        listFragments.add(new SettingPrintingFragment());

        // SETUP VIEWPAGER2
        final ViewPager2 viewPager2 = findViewById(R.id.pager);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                if (toast != null)
//                    toast.cancel()
//                toast = Toast.makeText(QualityInspectionMainActivity.this,
//                        listFragments.get(position).getClass().getSimpleName(), Toast.LENGTH_SHORT);
//                toast.show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        MyFragmentStateAdapter adapter = new MyFragmentStateAdapter(this, listFragments);
        viewPager2.setAdapter(adapter);

        // SETUP TAB LAYOUT
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(tabTexts.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(tabTexts.get(1)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setVisibility(View.VISIBLE);
        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabTexts.get(position));
                    }
                })
                .attach();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }

    @Override
    public void closeActivity() {
        AppManager.getAppManager().finishActivity();
        BaseApplication.isCloseActivity = true;
        if (AppManager.getAppManager().GetActivityCount() != 0)
            BaseApplication.context = AppManager.getAppManager().currentActivity();

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
        mToolBar.setTitle(mContext.getResources().getString(R.string.setting_title) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehouseno());

    }


    @Override
    protected void onResume() {
        super.onResume();
        mUserSettingPresenter = new UserSettingPresenter(mContext, this);
    }
}
