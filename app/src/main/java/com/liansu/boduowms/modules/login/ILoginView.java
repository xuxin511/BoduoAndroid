package com.liansu.boduowms.modules.login;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/8.
 */
public interface ILoginView {
    void jumpToNextActivity();
    void selectWareHouse(List<String> list,boolean isJump);
    String getCurrentWareHouseName();
    void setCurrentWareHouseName(String wareHouseName);
}
