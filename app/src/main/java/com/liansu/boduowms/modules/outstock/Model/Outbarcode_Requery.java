package com.liansu.boduowms.modules.outstock.Model;

public class Outbarcode_Requery {
   public  String  Barcode;
    public  int        Vouchertype;
    public  int    Towarehouseid;
    public  String Towarehouseno;
    public   Float Qty;

    public  String Materialno;

    public String  Batchno;




    public String getMaterialno() {
        return Materialno;
    }

    public void setMaterialno(String materialno) {
        Materialno = materialno;
    }

    public String getBatchno() {
        return Batchno;
    }

    public void setBatchno(String batchno) {
        Batchno = batchno;
    }

/// <summary>
    /// 到货单
    /// </summary>

    public String Arrvoucherno ;

    /// <summary>
    /// 质检单
    /// </summary>
    public String Qualityno ;

    public Float getQty() {
        return Qty;
    }

    public void setQty(Float qty) {
        Qty = qty;
    }

    public String getArrvoucherno() {
        return Arrvoucherno;
    }

    public String getQualityno() {
        return Qualityno;
    }

    public void setQualityno(String qualityno) {
        Qualityno = qualityno;
    }

    public void setArrvoucherno(String arrvoucherno) {
        Arrvoucherno = arrvoucherno;
    }

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
