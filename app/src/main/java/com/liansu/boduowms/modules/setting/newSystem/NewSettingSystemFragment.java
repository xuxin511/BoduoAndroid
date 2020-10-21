package com.liansu.boduowms.modules.setting.newSystem;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseFragment;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.modules.setting.ISettingView;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.SharePreferUtil;
import com.liansu.boduowms.utils.function.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * @ Des:  系统设置 新
 * @ Created by yangyiqing on 2020/7/19.
 */
@ContentView(R.layout.activity_setting_system2)
public class NewSettingSystemFragment extends BaseFragment implements INewSettingSystemView{
    Context mContext;
    @ViewInject(R.id.setting_system_official_environment_ip_address_button)
    RadioButton mOfficialButton;
    @ViewInject(R.id.setting_system_test_environment_ip_address_button)
    RadioButton mTestButton;
    @ViewInject(R.id.setting_system_official_environment_ip_address)
    EditText mOfficialEnvironmentIpAddress;
    @ViewInject(R.id.setting_test_official_environment_ip_address)
    EditText mTestEnvironmentIpAddress;
    @ViewInject(R.id.edt_TimeOut)
    EditText edtTimeOut;
    NewSettingSystemPresenter mPresenter;
    ISettingView              mISettingView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mISettingView = (ISettingView) context;
        // 在界面onAttach之后就触发初始化Presenter
        mPresenter = new NewSettingSystemPresenter(context, this, mHandler);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        closeKeyBoard(mOfficialEnvironmentIpAddress,mTestEnvironmentIpAddress,edtTimeOut);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOfficialButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mTestButton.setChecked(false);
                }
          onOfficialEnvironmentIpAddressFocus();
            }
        });
        mTestButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mOfficialButton.setChecked(false);
                }
                onTestEnvironmentIpAddressFocus();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onHandleMessage(Message message) {
        mPresenter.onHandleMessage(message);
    }

    @Event(R.id.btn_SaveSetting)
    private void onSaveClick(View view) {
        String sOfficialEnvironmentIpAddress = mOfficialEnvironmentIpAddress.getText().toString().trim();
        String sTestEnvironmentIpAddress=mTestEnvironmentIpAddress.getText().toString().trim();
        Integer sTimeOut = Integer.parseInt(edtTimeOut.getText().toString().trim()) * 1000;
        if (mPresenter!=null){
            mPresenter.onSave(sOfficialEnvironmentIpAddress,sTestEnvironmentIpAddress,sTimeOut);
        }
    }

    public  void  initData(){
        SharePreferUtil.ReadShare(mContext);
        int type=UrlInfo.mEnvironmentType;
        if (type== NewSettingSystemPresenter.URL_TYPE_OFFICIAL_ENVIRONMENT){
            mOfficialButton.setChecked(true);
            mTestButton.setChecked(false);
            onOfficialEnvironmentIpAddressFocus();
        }else if (type==NewSettingSystemPresenter.URL_TYPE_TEST_ENVIRONMENT){
            mOfficialButton.setChecked(false);
            mTestButton.setChecked(true);
            onTestEnvironmentIpAddressFocus();
        }
        mOfficialEnvironmentIpAddress.setText(UrlInfo.mOfficialEnvironmentIpAddress);
        mTestEnvironmentIpAddress.setText(UrlInfo.mTestEnvironmentIpAddress);
        edtTimeOut.setText(RequestHandler.SOCKET_TIMEOUT / 1000 + "");
    }

    @Override
    public void onOfficialEnvironmentIpAddressFocus() {
        CommonUtil.setEditFocus(mOfficialEnvironmentIpAddress);
    }

    @Override
    public void onTestEnvironmentIpAddressFocus() {
        CommonUtil.setEditFocus(mTestEnvironmentIpAddress);
    }

    @Override
    public void onTimeOutFocus() {
        CommonUtil.setEditFocus(edtTimeOut);
    }



    @Override
    public int getEnvironmentType() {
        int type=-1;
        if (mOfficialButton.isChecked()){
            type=NewSettingSystemPresenter.URL_TYPE_OFFICIAL_ENVIRONMENT;
        }else if (mTestButton.isChecked()){
            type=NewSettingSystemPresenter.URL_TYPE_TEST_ENVIRONMENT;
        }
        return type;
    }



}
