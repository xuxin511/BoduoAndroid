package com.liansu.boduowms.modules.outstock.Model;


import com.liansu.boduowms.bean.menu.MenuChildrenInfo;

import java.util.List;

//出库菜单类型
public class MenuOutStockModel {
    public String                 Title;
    public String                 VoucherType;
    public String                 ErpVoucherNo;
    public List<MenuChildrenInfo> secondMenuList ;

    public List<MenuChildrenInfo> getSecondMenuList() {
        return secondMenuList;
    }

    public void setSecondMenuList(List<MenuChildrenInfo> secondMenuList) {
        this.secondMenuList = secondMenuList;
    }

    public String getErpVoucherNo() {
        return ErpVoucherNo;
    }

    public void setErpVoucherNo(String erpVoucherNo) {
        ErpVoucherNo = erpVoucherNo;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getVoucherType() {
        return VoucherType;
    }

    public void setVoucherType(String voucherType) {
        VoucherType = voucherType;
    }
}
