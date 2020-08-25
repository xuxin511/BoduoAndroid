package com.liansu.boduowms.modules.qualityInspection.bill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.AppManager;
import com.liansu.boduowms.base.BaseApplication;
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

public class QualityInspectionMainActivity extends FragmentActivity implements IUserSettingView {

    TabLayout             tabLayout;
    Toolbar               mToolBar;
    Context               mContext = QualityInspectionMainActivity.this;
    QualifiedFragmentBill mQualifiedFragmentBill;
    protected UserSettingPresenter mUserSettingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout_main);
        AppManager.getAppManager().addActivity(this); //添加当前Activity到avtivity管理类
        final List<String> tabTexts = new ArrayList<>();
        tabTexts.add(getString(R.string.qualified_title));
//        tabTexts.add(getString(R.string.unqualified_title));
        mToolBar = findViewById(R.id.widget_common_tool_bar);
        mToolBar.setTitle(mContext.getResources().getString(R.string.quality_inspection_processing_scan_title) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename());
        final List<Fragment> listFragments = new ArrayList<>();
        mQualifiedFragmentBill = new QualifiedFragmentBill();
        listFragments.add(mQualifiedFragmentBill);
//        listFragments.add(new UnQualifiedFragmentBill());
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
                            selectWareHouse(mUserSettingPresenter.getModel().getWareHouseNameList());
                        }
                        break;
                }
                return true;
            }
        });
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
//                    toast.cancel();
//
//                toast = Toast.makeText(QualityInspectionMainActivity.this,
//                        listFragments.get(position).getClass().getSimpleName(), Toast.LENGTH_SHORT);
//                toast.show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        mUserSettingPresenter = new UserSettingPresenter(mContext, this);
        MyFragmentStateAdapter adapter = new MyFragmentStateAdapter(this, listFragments);
        viewPager2.setAdapter(adapter);

        // SETUP TAB LAYOUT
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(tabTexts.get(0)));
//        tabLayout.addTab(tabLayout.newTab().setText(tabTexts.get(1)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
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

    private void closeActivity() {
        AppManager.getAppManager().finishActivity();
        BaseApplication.isCloseActivity = true;
        if (AppManager.getAppManager().GetActivityCount() != 0)
            BaseApplication.context = AppManager.getAppManager().currentActivity();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mQualifiedFragmentBill != null) {
            return mQualifiedFragmentBill.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
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
        mToolBar.setTitle(mContext.getResources().getString(R.string.quality_inspection_processing_scan_title) + "-" + BaseApplication.mCurrentWareHouseInfo.getWarehousename());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            BackAlter();

        }
        return true;
    }


    public void BackAlter() {
        new AlertDialog.Builder(QualityInspectionMainActivity.this).setTitle("提示").setCancelable(false).setIcon(android.R.drawable.ic_dialog_info).setMessage("是否返回上一页面？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       closeActivity();
                    }
                }).setNegativeButton("取消", null).show();
    }
}
