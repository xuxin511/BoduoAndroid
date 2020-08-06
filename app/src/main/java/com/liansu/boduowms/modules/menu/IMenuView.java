package com.liansu.boduowms.modules.menu;

import android.content.Intent;

import java.util.List;
import java.util.Map;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/6/25.
 */
public interface IMenuView {
    void bindMenuList(List<Map<String, Object>> menulist);
    void loadBusiness(Intent intent);
}
