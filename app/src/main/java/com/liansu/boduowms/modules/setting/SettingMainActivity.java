package com.liansu.boduowms.modules.setting;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.AppManager;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.modules.setting.print.SettingPrintingFragment;
import com.liansu.boduowms.modules.setting.system.SettingSystemFragment;
import com.liansu.boduowms.ui.widget.MyFragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

public class SettingMainActivity extends FragmentActivity implements ISettingView {

    TabLayout tabLayout;
    Toolbar  mToolBar;
    Context mContext= SettingMainActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout_main);
        AppManager.getAppManager().addActivity(this); //添加当前Activity到avtivity管理类
        final List<String> tabTexts = new ArrayList<>();
        tabTexts.add(getString(R.string.setting_system_info));
        tabTexts.add(getString(R.string.setting_print_info));
        mToolBar=findViewById(R.id.widget_common_tool_bar);
        mToolBar.setTitle(mContext.getResources().getString(R.string.setting_title));
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              closeActivity();
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
}
