package com.liansu.boduowms.modules.setting.newSystem;

import android.view.View;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/29.
 */
 public interface INewSettingSystemView {
  void onOfficialEnvironmentIpAddressFocus();
  void onTestEnvironmentIpAddressFocus();
  void onTimeOutFocus();
  int getEnvironmentType();

}
