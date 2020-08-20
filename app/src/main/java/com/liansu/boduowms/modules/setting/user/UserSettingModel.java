package com.liansu.boduowms.modules.setting.user;

import android.content.Context;

import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.warehouse.WareHouseInfo;
import com.liansu.boduowms.utils.SharePreferUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Des: 用户和仓库设置类
 * @ Created by yangyiqing on 2020/8/19.
 */
public class UserSettingModel {
    Context             mContext;
    List<WareHouseInfo> mWareHouseList = new ArrayList<>();

    public UserSettingModel(Context context) {
        mContext = context;
        setWareHouseList();
    }


    /**
     * @desc: 获取当前用户的仓库列表
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/19 13:13
     */
    public void setWareHouseList() {
        mWareHouseList.clear();
        if (BaseApplication.mCurrentUserInfo != null) {
            List<WareHouseInfo> wareHouseInfos = BaseApplication.mCurrentUserInfo.getModelListWarehouse();
            if (wareHouseInfos != null && wareHouseInfos.size() > 0) {
                mWareHouseList.addAll(wareHouseInfos);
            }
        }
    }

    /**
     * @desc: 获取当前仓库信息类
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/9 18:38
     */
    public WareHouseInfo getWareHouseInfo(String name) {
        if (BaseApplication.mCurrentUserInfo != null && name != null) {
            List<WareHouseInfo> wareHouseInfos = BaseApplication.mCurrentUserInfo.getModelListWarehouse();
            if (wareHouseInfos != null && wareHouseInfos.size() > 0) {
                for (WareHouseInfo wareHouseInfo : wareHouseInfos) {
                    String wareHouseName = wareHouseInfo.getWarehousename() != null ? wareHouseInfo.getWarehousename() : "";
                    if (name.equals(wareHouseName)) {
                        return wareHouseInfo;
                    }

                }
            }
        }
        return null;
    }


    /**
     * @desc: 获取仓库名称集合
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/9 7:21
     */
    public List<String> getWareHouseNameList() {
        List<String> sWareHouseNameList = new ArrayList<String>();
        for (WareHouseInfo warehouse : mWareHouseList) {
            if (warehouse.getWarehousename() != null && !warehouse.getWarehousename().equals("")) {
                sWareHouseNameList.add(warehouse.getWarehousename());
            }
        }
        return sWareHouseNameList;
    }


    /**
     * @desc: 保存当前选择的仓库信息
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/19 13:27
     */
    public boolean saveCurrentWarehouseInfo(String wareHouseName) {
        boolean IS_SAVE = false;
        for (WareHouseInfo info : mWareHouseList) {
            String sWareHouseName = info.getWarehousename() != null ? info.getWarehousename() : "";
            if (sWareHouseName.trim().equals(wareHouseName.trim())) {
                BaseApplication.mCurrentWareHouseInfo = info;
                SharePreferUtil.SetWareHouseInfoShare(mContext, BaseApplication.mCurrentWareHouseInfo);
                IS_SAVE = true;
            }
        }
        return IS_SAVE;
    }


    public WareHouseInfo getCurrentWareHouseInfo() {
        return BaseApplication.mCurrentWareHouseInfo;
    }
}
