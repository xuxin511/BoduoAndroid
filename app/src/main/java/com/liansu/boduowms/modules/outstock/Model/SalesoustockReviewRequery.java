package com.liansu.boduowms.modules.outstock.Model;

//复核提交类
public class SalesoustockReviewRequery {

    public  String  Erpvoucherno;
    public  int  Vouchertype;
    public  String  WayBillNo;
    public  String  Scanuserno;

    public  String  Dirver;//司机
    public  String  LogisticsCompany;//物料公司


    public String getDirver() {
        return Dirver;
    }

    public void setDirver(String dirver) {
        Dirver = dirver;
    }

    public String getLogisticsCompany() {
        return LogisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        LogisticsCompany = logisticsCompany;
    }

    public String getScanuserno() {
        return Scanuserno;
    }

    public void setScanuserno(String scanuserno) {
        Scanuserno = scanuserno;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String erpvoucherno) {
        Erpvoucherno = erpvoucherno;
    }

    public int getVouchertype() {
        return Vouchertype;
    }

    public void setVouchertype(int vouchertype) {
        Vouchertype = vouchertype;
    }

    public String getWayBillNo() {
        return WayBillNo;
    }

    public void setWayBillNo(String wayBillNo) {
        WayBillNo = wayBillNo;
    }
}
