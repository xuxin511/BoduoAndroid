package com.liansu.boduowms.modules.login;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.user.UserInfo;
import com.liansu.boduowms.bean.warehouse.WareHouseInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.Network.NetworkError;
import com.liansu.boduowms.utils.Network.RequestHandler;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/8.
 */
public class LoginModel extends BaseModel {
    public               String TAG_GET_MENU_INFO        = "Login_GetT_UserMenuListByRuleIdAsync";
    public               String TAG_User_Login           = "Login_userlogin";
    private static final int    RESULT_USER_LOGIN        = 101;
    private static final int    RESULT_TAG_GET_MENU_INFO = 102;
    List<WareHouseInfo> mWareHouseList = new ArrayList<>();
    UserInfo            mUserInfo;

    public LoginModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    @Override
    public void onHandleMessage(Message msg) {
        NetCallBackListener<String> listener = null;
        switch (msg.what) {
            case RESULT_USER_LOGIN:
                listener = mNetMap.get("TAG_User_Login");
                break;
            case RESULT_TAG_GET_MENU_INFO:
                listener = mNetMap.get("TAG_GET_MENU_INFO");
                break;
            case NetworkError.NET_ERROR_CUSTOM:
                MessageBox.Show(mContext, "出现预期之外的异常:" + msg.obj);
//                ToastUtil.show("获取请求失败_____" + msg.obj);
                break;
        }
        if (listener != null) {
            listener.onCallBack(msg.obj.toString());
        }
    }


    /**
     * @desc: 登录接口
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/8 23:50
     */
    public void requestUserLogin(String userNo, String password, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_User_Login", callBackListener);
        final Map<String, String> params = new HashMap<String, String>();
        params.put("Userno", userNo);
        params.put("PassWord", password);
        LogUtil.WriteLog(Login.class, TAG_User_Login, params.toString());
        RequestHandler.addRequestWithDialog(Request.Method.POST, TAG_User_Login, mContext.getString(R.string.Msg_Login), mContext, mHandler, RESULT_USER_LOGIN, null, UrlInfo.getUrl().UserLogin, params, null);

    }

    /**
     * @desc: 登录接口
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/8 23:50
     */
    public void requestMenuInfo(String userNo, NetCallBackListener<String> callBackListener) {
        mNetMap.put("TAG_GET_MENU_INFO", callBackListener);
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("Userno", userNo);
        params.put("Pcorpda", 2);
        LogUtil.WriteLog(Login.class, TAG_GET_MENU_INFO, params.toString());
        RequestHandler.addObjectRequestWithDialog(Request.Method.POST, TAG_GET_MENU_INFO, mContext.getString(R.string.Msg_Verifying_Permission_information), mContext, mHandler, RESULT_TAG_GET_MENU_INFO, null, UrlInfo.getUrl().GetT_UserMenuListByRuleIdAsync, params, null);

    }


    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    public void setWareHouseList(List<WareHouseInfo> list) {
        mWareHouseList.clear();
        mWareHouseList.addAll(list);
    }

    public List<WareHouseInfo> getWareHouseList() {
        return mWareHouseList;
    }

    /**
     * @desc: 获取仓库编码
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/9 7:21
     */
    public List<String> getWareHouseNoList() {
        mWareHouseList.clear();
        List<String> sWareHouseNoList = new ArrayList<String>();
        if (BaseApplication.mCurrentUserInfo != null) {
            List<WareHouseInfo> wareHouseInfos = BaseApplication.mCurrentUserInfo.getModelListWarehouse();
            if (wareHouseInfos != null && wareHouseInfos.size() > 0) {
                mWareHouseList.addAll(wareHouseInfos);
                for (WareHouseInfo warehouse : wareHouseInfos) {
                    if (warehouse.getWarehouseno() != null && !warehouse.getWarehouseno().equals("")) {
                        sWareHouseNoList.add(warehouse.getWarehouseno());
                    }
                }

            }
        }

        return sWareHouseNoList;
    }

    /**
     * @desc: 获取当前仓库信息类
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/9 18:38
     */
    public WareHouseInfo getWareHouseInfo(String wareHouseNo) {
        if (BaseApplication.mCurrentUserInfo != null && wareHouseNo != null) {
            List<WareHouseInfo> wareHouseInfos = BaseApplication.mCurrentUserInfo.getModelListWarehouse();
            if (wareHouseInfos != null && wareHouseInfos.size() > 0) {
                for (WareHouseInfo wareHouseInfo : wareHouseInfos) {
                    String sWareHouseNo = wareHouseInfo.getWarehouseno() != null ? wareHouseInfo.getWarehouseno() : "";
                    if (wareHouseNo.equals(sWareHouseNo)) {
                        return wareHouseInfo;
                    }

                }
            }
        }
        return null;
    }
}
