package com.liansu.boduowms.modules.outstock.Model;

public class Outbarcode_Requery {
   public  String  Barcode;
    public  int        Vouchertype;
    public  int    Towarehouseid;
    public  String Towarehouseno;

    public String getTowarehouseno() {
        return Towarehouseno;
    }

    public void setTowarehouseno(String towarehouseno) {
        Towarehouseno = towarehouseno;
    }

    public int getTowarehouseid() {
        return Towarehouseid;
    }

    public void setTowarehouseid(int towarehouseid) {
        Towarehouseid = towarehouseid;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public int getVouchertype() {
        return Vouchertype;
    }

    public void setVouchertype(int vouchertype) {
        Vouchertype = vouchertype;
    }
}
