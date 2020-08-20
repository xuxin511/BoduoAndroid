package com.liansu.boduowms.modules.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.warehouse.WareHouseInfo;
import com.liansu.boduowms.debug.DebugModuleData;
import com.liansu.boduowms.modules.menu.MainActivity;
import com.liansu.boduowms.modules.setting.SettingMainActivity;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.SharePreferUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import static com.liansu.boduowms.debug.DebugModuleData.DEBUG_DATA_STATUS_OFFLINE;
import static com.liansu.boduowms.utils.SharePreferUtil.ReadPrintSettingShare;


@ContentView(R.layout.activity_login)
public class Login extends BaseActivity implements ILoginView {

    @ViewInject(R.id.txt_Verion)
    TextView  txtVersion;
    @ViewInject(R.id.txt_WareHousName)
    TextView  mWareHouseName;
    @ViewInject(R.id.edt_UserName)
    EditText  mUserNo;
    @ViewInject(R.id.edt_Password)
    EditText  mPassword;
    @ViewInject(R.id.imageView2)
    ImageView mLogo;
    LoginPresenter mPresenter;
    Context        mContext = Login.this;

    @Override
    public void onHandleMessage(Message msg) {
        if (mPresenter != null) {
            mPresenter.getModel().onHandleMessage(msg);
        }
    }


    @Override
    protected void initViews() {
        BaseApplication.context = mContext;
        x.view().inject(this);
        txtVersion.setText(getString(R.string.login_Version) + (updateVersionService.getVersionCode(mContext)));
        DebugModuleData.setDebugDataStatus(mContext, DEBUG_DATA_STATUS_OFFLINE);
        super.initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharePreferUtil.ReadShare(mContext);
        SharePreferUtil.ReadUserShare(mContext);
        SharePreferUtil.ReadWareHouseInfoShare(mContext);
        ReadPrintSettingShare(mContext);
        if (BaseApplication.mCurrentUserInfo != null) {
            mUserNo.setText(BaseApplication.mCurrentUserInfo.getUserno());
//            edtPassword.setText(DESUtil.decode(BaseApplication.mCurrentUserInfo.getPassWord()));
        }
        if (BaseApplication.mCurrentWareHouseInfo != null) {
            mWareHouseName.setText(BaseApplication.mCurrentWareHouseInfo.getWarehousename());
        }
        if (mPresenter == null) {
            mPresenter = new LoginPresenter(mContext, this, mHandler);
        }
        mPassword.setText("");
    }

    @Event(value = R.id.edt_UserName, type = View.OnKeyListener.class)
    private boolean edtUserNameOnKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)// 如果为Enter键
        {
            keyBoardCancle();
//            GetWareHouse();
        }
        return false;
    }

    @Event(value = R.id.txt_WareHousName, type = View.OnClickListener.class)
    private void txtWareHousNameOnClick(View v) {
        if (mPresenter != null) {
            mPresenter.selectWareHouseName();
        }


    }


    @Event(R.id.btn_Login)
    private void btnLoginClick(View view) {

        String userNo = mUserNo.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if (mPresenter != null) {
            mPresenter.onLogin(userNo, password);
        }

    }

    @Event(R.id.btn_Setting)
    private void btnSetting(View view) {
        startActivityLeft(new Intent(mContext, SettingMainActivity.class));
    }


    @Override
    public void jumpToNextActivity() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivityLeft(intent);
    }

    @Override
    public void selectWareHouse(List<String> list, final boolean isJump) {
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
                            String userNo = mUserNo.getText().toString().trim();
                            if (!userNo.equals( BaseApplication.mCurrentUserInfo.getUserno())){
                                MessageBox.Show(mContext,"正在登陆的用户:["+userNo+"]和已登陆的用户:["+BaseApplication.mCurrentUserInfo.getUserno()+"]不一致,请重新登录再选择仓库");
                                dialog.dismiss();
                                return;
                            }
                            WareHouseInfo wareHouseInfo = BaseApplication.mCurrentUserInfo.getModelListWarehouse().get(which);
                            mWareHouseName.setText(select_item);
                            BaseApplication.mCurrentWareHouseInfo = wareHouseInfo;
                            SharePreferUtil.SetWareHouseInfoShare(mContext, BaseApplication.mCurrentWareHouseInfo);
                            if (isJump) {
                                jumpToNextActivity();
                            }

                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public String getCurrentWareHouseName() {
        return mWareHouseName.getText().toString().trim();
    }

    @Override
    public void setCurrentWareHouseName(String wareHouseName) {
        mWareHouseName.setText(wareHouseName);
    }


}
