package com.liansu.boduowms.modules.outstock.Model;

//销售出库请求类
public class SalesoutstockRequery {

     public String  Erpvoucherno;

    public String  Towarehouseno;


    public String  MaterialNo;
    public String  Batchno;
    public String  PostUserNo;
    public int  ScanQty;
    public String  PalletNo;
    public String  BarcodeType;
    public int  Vouchertype;

    public String getTowarehouseno() {
        return Towarehouseno;
    }

    public void setTowarehouseno(String towarehouseno) {
        Towarehouseno = towarehouseno;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String erpvoucherno) {
        Erpvoucherno = erpvoucherno;
    }

    public String getMaterialNo() {
        return MaterialNo;
    }

    public void setMaterialNo(String materialNo) {
        MaterialNo = materialNo;
    }

    public String getBatchno() {
        return Batchno;
    }

    public void setBatchno(String batchno) {
        Batchno = batchno;
    }

    public String getPostUserNo() {
        return PostUserNo;
    }

    public void setPostUserNo(String postUserNo) {
        PostUserNo = postUserNo;
    }

    public int getScanQty() {
        return ScanQty;
    }

    public void setScanQty(int scanQty) {
        ScanQty = scanQty;
    }

    public String getPalletNo() {
        return PalletNo;
    }

    public void setPalletNo(String palletNo) {
        PalletNo = palletNo;
    }

    public String getBarcodeType() {
        return BarcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        BarcodeType = barcodeType;
    }

    public int getVouchertype() {
        return Vouchertype;
    }

    public void setVouchertype(int vouchertype) {
        Vouchertype = vouchertype;
    }
}
