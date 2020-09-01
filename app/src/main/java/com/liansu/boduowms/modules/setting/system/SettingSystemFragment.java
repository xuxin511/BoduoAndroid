package com.liansu.boduowms.modules.setting.system;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseFragment;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.modules.setting.ISettingView;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.SharePreferUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * @ Des:  系统设置
 * @ Created by yangyiqing on 2020/7/19.
 */
@ContentView(R.layout.activity_setting_system)
public class SettingSystemFragment extends BaseFragment implements ISettingSystemView {
    Context mContext;
    @ViewInject(R.id.edt_IPAdress)
    EditText edtIPAdress;
    @ViewInject(R.id.edt_Port)
    EditText edtPort;
    @ViewInject(R.id.edt_TimeOut)
    EditText edtTimeOut;
    SettingSystemPresenter mPresenter;
    ISettingView           mISettingView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mISettingView = (ISettingView) context;
        // 在界面onAttach之后就触发初始化Presenter
        mPresenter = new SettingSystemPresenter(context, this, mHandler);

    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        closeKeyBoard(edtIPAdress,edtPort,edtTimeOut);
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
        String IPAdress = edtIPAdress.getText().toString().trim();
        Integer Port = Integer.parseInt(edtPort.getText().toString().trim());
        Integer TimeOut = Integer.parseInt(edtTimeOut.getText().toString().trim()) * 1000;
        SharePreferUtil.SetShare(mContext, IPAdress, "", "", Port, TimeOut, true);
        new AlertDialog.Builder(mContext).setTitle("提示").setCancelable(false).setMessage(getResources().getString(R.string.SaveSuccess)).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                mISettingView.closeActivity();
            }
        }).show();

    }

    public  void  initData(){
        SharePreferUtil.ReadShare(mContext);
        edtIPAdress.setText(UrlInfo.IPAdress);
        edtPort.setText(UrlInfo.Port + "");
        edtTimeOut.setText(RequestHandler.SOCKET_TIMEOUT / 1000 + "");
    }
}
