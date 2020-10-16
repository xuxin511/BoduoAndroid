package com.liansu.boduowms.modules.login;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.base.BaseResultInfo;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.menu.MenuInfo;
import com.liansu.boduowms.bean.user.UserInfo;
import com.liansu.boduowms.bean.warehouse.WareHouseInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.ui.dialog.ToastUtil;
import com.liansu.boduowms.utils.Network.NetCallBackListener;
import com.liansu.boduowms.utils.SharePreferUtil;
import com.liansu.boduowms.utils.function.GsonUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.liansu.boduowms.utils.log.LogUtil;

import java.util.List;

import static com.liansu.boduowms.bean.base.BaseResultInfo.RESULT_TYPE_OK;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/8.
 */
public class LoginPresenter {
    private Context    mContext;
    private LoginModel mModel;
    private ILoginView mView;


    public void onHandleMessage(Message msg) {
        mModel.onHandleMessage(msg);

    }

    public LoginPresenter(Context context, ILoginView view, MyHandler<BaseActivity> handler) {
        this.mContext = context;
        this.mView = view;
        this.mModel = new LoginModel(context, handler);
    }

    public LoginModel getModel() {
        return mModel;
    }

    /**
     * @desc: 登录
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/8 23:43
     */
    public void onLogin(final String userNo, String password) {
        try {
            if (TextUtils.isEmpty(UrlInfo.PrintIP)) {
                MessageBox.Show(mContext, mContext.getString(R.string.Error_PrintIPNotSet));
                return;
            }
            if (!UrlInfo.isWMS && TextUtils.isEmpty(UrlInfo.ElecIP)) {
                MessageBox.Show(mContext, mContext.getString(R.string.Error_PrintIPNotSet));
                return;
            }

            if (userNo.equals("")) {
                MessageBox.Show(mContext, "请输入用户名");
                return;
            }
            if (password.equals("")) {
                MessageBox.Show(mContext, "请输入密码");
                return;
            }

            mModel.requestUserLogin(userNo, password, new NetCallBackListener<String>() {
                @Override
                public void onCallBack(String result) {
                    LogUtil.WriteLog(Login.class, mModel.TAG_User_Login, result);
                    BaseResultInfo<UserInfo> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<UserInfo>>() {
                    }.getType());
                    if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                        UserInfo info = returnMsgModel.getData();
                        if (info != null) {
                            BaseApplication.mCurrentUserInfo = info;
                            SharePreferUtil.SetUserShare(mContext, BaseApplication.mCurrentUserInfo);
                            getMenu(userNo);
                        } else {
                            MessageBox.Show(mContext, "校验用户失败:获取" + userNo + "的用户信息为空");
                            return;
                        }
                    } else {
                        ToastUtil.show(returnMsgModel.getResultValue());
                    }
                }
            });
        } catch (Exception e) {
            MessageBox.Show(mContext, "校验用户失败:出现预期之外的异常," + e.getMessage());
            return;
        }


    }

    /**
     * @desc: 获取用户的权限和菜单信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/9 11:09
     */
    public void getMenu(final String userNo) {
        mModel.requestMenuInfo(userNo, new NetCallBackListener<String>() {
            @Override
            public void onCallBack(String result) {
                LogUtil.WriteLog(Login.class, mModel.TAG_User_Login, result);
                BaseResultInfo<List<MenuInfo>> returnMsgModel = GsonUtil.getGsonUtil().fromJson(result, new TypeToken<BaseResultInfo<List<MenuInfo>>>() {
                }.getType());
                if (returnMsgModel.getResult() == RESULT_TYPE_OK) {
                    List<MenuInfo> menuList = returnMsgModel.getData();
                    if (menuList != null && menuList.size() > 0) {
                        BaseApplication.mCurrentMenuList = menuList;
                        List<String> wareHouseNoList = mModel.getWareHouseNoList();
                        if (wareHouseNoList.size() == 1) {
                            BaseApplication.mCurrentWareHouseInfo = mModel.getWareHouseList().get(0);
                            mView.setCurrentWareHouseNo(wareHouseNoList.get(0));
                            SharePreferUtil.SetWareHouseInfoShare(mContext, BaseApplication.mCurrentWareHouseInfo);
                            mView.jumpToNextActivity();
                        } else if (wareHouseNoList.size() > 1) {
                            String currentWareHouseNo = mView.getCurrentWareHouseNo();
                            if (currentWareHouseNo != null && !currentWareHouseNo.equals("") && !currentWareHouseNo.equals("仓库")) {
                                WareHouseInfo wareHouseInfo = mModel.getWareHouseInfo(currentWareHouseNo);
                                if (wareHouseInfo != null) {
                                    BaseApplication.mCurrentWareHouseInfo = wareHouseInfo;
                                    SharePreferUtil.SetWareHouseInfoShare(mContext, BaseApplication.mCurrentWareHouseInfo);
                                    mView.jumpToNextActivity();
                                } else {
                                    mView.selectWareHouse(wareHouseNoList, true);
                                }


                            } else {
                                mView.selectWareHouse(wareHouseNoList, true);
                            }

                        } else {
                            MessageBox.Show(mContext, "校验仓库失败,用户:" + userNo + "没有仓库的操作权限,请联系管理员");

                        }

                    } else {
                        MessageBox.Show(mContext, "校验权限失败,用户:" + userNo + "没有业务操作权限,请联系管理员");
                        return;
                    }
                } else {
                    ToastUtil.show(returnMsgModel.getResultValue());
                }
            }
        });
    }

    /**
     * @desc: 选择当前
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/9 17:53
     */
    public void selectWareHouseName() {
        UserInfo userInfo = BaseApplication.mCurrentUserInfo;
        if (userInfo != null) {
            List<String> wareHouseNameList = mModel.getWareHouseNoList();
            mView.selectWareHouse(wareHouseNameList, false);

        }
    }
}
