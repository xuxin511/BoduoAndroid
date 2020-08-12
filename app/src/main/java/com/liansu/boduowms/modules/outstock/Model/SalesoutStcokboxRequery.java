package com.liansu.boduowms.modules.outstock.Model;

//拼箱提交类
public  class SalesoutStcokboxRequery {
    public  String Erpvoucherno;
    public  Float Qty;
    public  String Batchno;
    public  String Materialno;
    public  int Vouchertype;
    public  String PostUserNo;

    public  String Materialdesc;
    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String erpvoucherno) {
        Erpvoucherno = erpvoucherno;
    }

    public String getMaterialdesc() {
        return Materialdesc;
    }

    public void setMaterialdesc(String materialdesc) {
        Materialdesc = materialdesc;
    }

    public Float getQty() {
        return Qty;
    }

    public void setQty(Float qty) {
        Qty = qty;
    }

    public String getBatchno() {
        return Batchno;
    }

    public void setBatchno(String batchno) {
        Batchno = batchno;
    }

    public String getMaterialno() {
        return Materialno;
    }

    public void setMaterialno(String materialno) {
        Materialno = materialno;
    }

    public int getVouchertype() {
        return Vouchertype;
    }

    public void setVouchertype(int vouchertype) {
        Vouchertype = vouchertype;
    }

    public String getPostUserNo() {
        return PostUserNo;
    }

    public void setPostUserNo(String postUserNo) {
        PostUserNo = postUserNo;
    }
}
