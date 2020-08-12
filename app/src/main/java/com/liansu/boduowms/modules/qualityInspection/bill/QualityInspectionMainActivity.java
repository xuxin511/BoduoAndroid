package com.liansu.boduowms.modules.qualityInspection.bill;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.AppManager;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.ui.widget.MyFragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

public class QualityInspectionMainActivity extends FragmentActivity {

    TabLayout             tabLayout;
    Toolbar               mToolBar;
    Context               mContext = QualityInspectionMainActivity.this;
    QualifiedFragmentBill mQualifiedFragmentBill;

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
}
