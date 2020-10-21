package com.liansu.boduowms.modules.setting.newSystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseFragment;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;
import com.liansu.boduowms.bean.setting.SystemSettingInfo;
import com.liansu.boduowms.modules.setting.print.SettingModel;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.SharePreferUtil;
import com.liansu.boduowms.utils.hander.MyHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

import static com.liansu.boduowms.ui.dialog.MessageBox.MEDIA_MUSIC_ERROR;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/27.
 */
public class NewSettingSystemPresenter {
    private             Context               mContext;
    private             SettingModel          mModel;
    private             INewSettingSystemView mView;
    public static final int                   URL_TYPE_OFFICIAL_ENVIRONMENT = 1; //正式区
    public static final int                   URL_TYPE_TEST_ENVIRONMENT     = 2; //测试区

    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);
    }

    public NewSettingSystemPresenter(Context context, INewSettingSystemView view, MyHandler<BaseFragment> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new SettingModel(context, handler);
    }


    public BaseMultiResultInfo<Boolean, SystemSettingInfo> checkSystemSettingInfo(@NonNull String url, int urlType) {
        BaseMultiResultInfo<Boolean, SystemSettingInfo> resultInfo = new BaseMultiResultInfo<>();
        SystemSettingInfo info = null;
        String ipAddress = "";
        String lastContent="";
        int port = -1;
        String environmentName = "未定义区";
        if (urlType == URL_TYPE_OFFICIAL_ENVIRONMENT) {
            environmentName = "正式区";
        } else if (urlType == URL_TYPE_TEST_ENVIRONMENT) {
            environmentName = "测试区";
        }
        try {

            if (url != null && !url.equals("")) {
                Pattern p = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)\\:(\\d+)");
                Matcher m = p.matcher(url);
                //将符合规则的提取出来
                while (m.find()) {
                    ipAddress = m.group(1);
                    port = Integer.parseInt(m.group(2));
                }

                if (ipAddress==null ||ipAddress.equals("")){
                    resultInfo.setHeaderStatus(false);
                    resultInfo.setMessage("校验" + environmentName + "的路径["+url+"]失败:IP地址不正确");
                    return resultInfo;
                }
                String splitString=":"+port+"/";
                int index=url.lastIndexOf(splitString)+splitString.length();
                lastContent = url.substring(index,url.length());


            }
            info=new SystemSettingInfo();
            info.setIPAddress(ipAddress);
            info.setPort(port);
            info.setLastContent(lastContent);
            info.setUrl(url);
            resultInfo.setHeaderStatus(true);
            resultInfo.setInfo(info);


        } catch (Exception e) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("校验" + environmentName + "的路径出现预期之外的异常:" + e.getMessage());
            return resultInfo;

        }

        return resultInfo;
    }


    public void  onSave(String officialEnvironmentIpAddress,String testEnvironmentIpAddress,Integer timeOut){
        SystemSettingInfo mSettingInfo=null;
        BaseMultiResultInfo<Boolean, SystemSettingInfo>   checkOfficialResult=checkSystemSettingInfo(officialEnvironmentIpAddress,NewSettingSystemPresenter.URL_TYPE_OFFICIAL_ENVIRONMENT);
        if (!checkOfficialResult.getHeaderStatus()){
            MessageBox.Show(mContext, checkOfficialResult.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onOfficialEnvironmentIpAddressFocus();
                }
            });
            return;
        }
        BaseMultiResultInfo<Boolean, SystemSettingInfo>   checkTestResult=checkSystemSettingInfo(testEnvironmentIpAddress,NewSettingSystemPresenter.URL_TYPE_TEST_ENVIRONMENT);
        if (!checkTestResult.getHeaderStatus()){
            MessageBox.Show(mContext, checkTestResult.getMessage(), MEDIA_MUSIC_ERROR, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.onTestEnvironmentIpAddressFocus();
                }
            });
            return;
        }

        int type=mView.getEnvironmentType();
        if (type==NewSettingSystemPresenter.URL_TYPE_OFFICIAL_ENVIRONMENT){
            mSettingInfo=checkOfficialResult.getInfo();
        }else if(type==NewSettingSystemPresenter.URL_TYPE_TEST_ENVIRONMENT){
            mSettingInfo=checkTestResult.getInfo();
        }
        if (mSettingInfo!=null){
            SharePreferUtil.setSystemSettingShare(mContext,mSettingInfo.getIPAddress(),mSettingInfo.getPort(),mSettingInfo.getLastContent(),timeOut,officialEnvironmentIpAddress,testEnvironmentIpAddress,type);
        }else {
            SharePreferUtil.setSystemSettingShare(mContext,"",-1,"",timeOut,officialEnvironmentIpAddress,testEnvironmentIpAddress,type);
        }

//        SharePreferUtil.SetShare(mContext, IPAdress, "", "", Port, TimeOut, true);
        new AlertDialog.Builder(mContext).setTitle("提示").setCancelable(false).setMessage(mContext.getResources().getString(R.string.SaveSuccess)).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                mISettingView.closeActivity();
            }
        }).show();
    }
}
