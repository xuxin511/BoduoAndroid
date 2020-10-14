package com.liansu.boduowms.modules.outstock.Model;

import com.liansu.boduowms.modules.outstock.purchaseInspection.bill.PurchaseInspectionBill;

//拼箱提交类
public  class SalesoutStcokboxRequery {
    public  String Erpvoucherno;
    public  Float Qty;
    public  String Batchno;
    public  String Materialno;
    public  int Vouchertype;
    public  String PostUser;
    public  int IsStockCombine;
    public  int Barcodetype; //2箱号 3散件

    public int Boxtype;

    public int getBoxtype() {
        return Boxtype;
    }

    public void setBoxtype(int boxtype) {
        Boxtype = boxtype;
    }

    public  boolean       IsPrint;

    public boolean isPrint() {
        return IsPrint;
    }

    public void setPrint(boolean print) {
        IsPrint = print;
    }

    public int getBarcodetype() {
        return Barcodetype;
    }

    public void setBarcodetype(int barcodetype) {
        Barcodetype = barcodetype;
    }

    public int getIsStockCombine() {
        return IsStockCombine;
    }

    public void setIsStockCombine(int isStockCombine) {
        IsStockCombine = isStockCombine;
    }

    public  String Printername;
    public  int  Printertype;

    public String getPrintname() {
        return Printername;
    }

    public void setPrintname(String printname) {
        Printername = printname;
    }

    public int getPrinttype() {
        return Printertype;
    }

    public void setPrinttype(int printtype) {
        Printertype = printtype;
    }

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
        return PostUser;
    }

    public void setPostUserNo(String PostUser) {
        PostUser = PostUser;
    }
}
