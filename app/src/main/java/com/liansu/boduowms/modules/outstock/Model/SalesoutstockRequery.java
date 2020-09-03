package com.liansu.boduowms.modules.outstock.Model;

//销售出库请求类
public class SalesoutstockRequery {

     public String  Erpvoucherno;

    public String  Towarehouseno;
    public String  Creater;
    public String  Scanuserno;


    public String getCreater() {
        return Creater;
    }

    public void setCreater(String creater) {
        Creater = creater;
    }

    public String getStrongholdcode() {
        return Strongholdcode;
    }

    public void setStrongholdcode(String strongholdcode) {
        Strongholdcode = strongholdcode;
    }

    public  String Strongholdcode;

    public String  MaterialNo;
    public String  Batchno;
    public String  PostUserNo;
    public Float  ScanQty;
    public String  PalletNo;
    public int  BarcodeType;
    public int  Vouchertype;

    public String getScanuserno() {
        return Scanuserno;
    }

    public void setScanuserno(String scanuserno) {
        Scanuserno = scanuserno;
    }

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

    public Float getScanQty() {
        return ScanQty;
    }

    public void setScanQty(Float scanQty) {
        ScanQty = scanQty;
    }

    public String getPalletNo() {
        return PalletNo;
    }

    public void setPalletNo(String palletNo) {
        PalletNo = palletNo;
    }

    public int getBarcodeType() {
        return BarcodeType;
    }

    public void setBarcodeType(int barcodeType) {
        BarcodeType = barcodeType;
    }

    public int getVouchertype() {
        return Vouchertype;
    }

    public void setVouchertype(int vouchertype) {
        Vouchertype = vouchertype;
    }
}
