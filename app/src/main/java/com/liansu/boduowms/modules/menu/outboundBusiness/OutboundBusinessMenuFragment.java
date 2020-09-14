package com.liansu.boduowms.modules.menu.outboundBusiness;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.BaseFragment;
import com.liansu.boduowms.bean.menu.MenuInfo;
import com.liansu.boduowms.modules.menu.IMenuView;
import com.liansu.boduowms.ui.adapter.GridViewItemAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Map;
@ContentView(R.layout.fragment_menu)
public class OutboundBusinessMenuFragment extends BaseFragment implements IMenuView {

    @ViewInject(R.id.gv_Function)
    GridView mGridView;
    GridViewItemAdapter mAdapter;
    Context             mContext = getActivity();
    private OutboundBusinessMenuPresenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        // 在界面onAttach之后就触发初始化Presenter
        mPresenter=new OutboundBusinessMenuPresenter(this,mContext);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter!=null){
            List<MenuInfo> menuInfos = BaseApplication.mCurrentMenuList;
            mPresenter.loadMenuList(menuInfos);
        }
    }

    @Override
    public void bindMenuList(List<Map<String, Object>> menulist) {
        if (menulist!=null && menulist.size()>0){
            mAdapter = new GridViewItemAdapter(mContext,menulist);
//            mAdapter.setBackgroundColor(R.drawable.green_background);
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
            mPresenter.loadBusiness(moduleName);
        }
    }
}
