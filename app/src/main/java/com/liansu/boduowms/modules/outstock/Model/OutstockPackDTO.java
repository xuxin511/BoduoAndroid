package com.liansu.boduowms.modules.outstock.Model;

//发货通知单复核提交件数对象
public class OutstockPackDTO {
    public int Id;
    public String Companycode;
    public String Strongholdcode;
    public String Strongholdname;
    public String Erpvoucherno;
    public int Vouchertype;
    public String Departmentcode;
    public String Departmentname;
    public String WayBillNo;
    public int CartonNum;
    public int SystemCartonNum;
    public int SystemPackageCartonNum;
    public int ManualCartonNum;// 手工输入件数

}

