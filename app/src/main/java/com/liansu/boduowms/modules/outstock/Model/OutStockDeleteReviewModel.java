package com.liansu.boduowms.modules.outstock.Model;

import android.provider.ContactsContract;

import com.liansu.boduowms.modules.outstock.purchaseInspection.bill.PurchaseInspectionBill;

//用来展示复核删除的对象
public  class OutStockDeleteReviewModel {

    /// <summary>
    /// 物流标签
    /// </summary>
    /// <value></value>
    public String PackageCode;

    /// <summary>
    /// 当前单据箱号
    /// </summary>
    /// <value></value>
    public int PackageSeq ;

    public String Contacts ;

    public String Materialno ;

    public String Materialdesc ;

    //已经复核数量
    public Float ScanQty ;

    //拼箱数量
    public Float  PackageNum ;

    //删除类型
    public int DeleteType;

    public  boolean IsCheck;

    public  String Erpvoucherno;

    public int Vouchertype;

    public  Float Qty;

    public Float getQty() {
        return Qty;
    }

    public void setQty(Float qty) {
        Qty = qty;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String erpvoucherno) {
        Erpvoucherno = erpvoucherno;
    }

    public int getVouchertpe() {
        return Vouchertype;
    }

    public void setVouchertpe(int vouchertpe) {
        Vouchertype = vouchertpe;
    }

    public boolean isCheck() {
        return IsCheck;
    }

    public void setCheck(boolean check) {
        IsCheck = check;
    }

    public String getPackageCode() {
        return PackageCode;
    }

    public void setPackageCode(String packageCode) {
        PackageCode = packageCode;
    }

    public int getPackageSeq() {
        return PackageSeq;
    }

    public void setPackageSeq(int packageSeq) {
        PackageSeq = packageSeq;
    }

    public String getContacts() {
        return Contacts;
    }

    public void setContacts(String contacts) {
        Contacts = contacts;
    }

    public String getMaterialno() {
        return Materialno;
    }

    public void setMaterialno(String materialno) {
        Materialno = materialno;
    }

    public String getMaterialdesc() {
        return Materialdesc;
    }

    public void setMaterialdesc(String materialdesc) {
        Materialdesc = materialdesc;
    }

    public Float getScanQty() {
        return ScanQty;
    }

    public void setScanQty(Float scanQty) {
        ScanQty = scanQty;
    }

    public Float getPackageNum() {
        return PackageNum;
    }

    public void setPackageNum(Float packageNum) {
        PackageNum = packageNum;
    }

    public int getDeleteType() {
        return DeleteType;
    }

    public void setDeleteType(int deleteType) {
        DeleteType = deleteType;
    }
}
