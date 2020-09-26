package com.liansu.boduowms.modules.inHouseStock.inventory.Model;

import java.sql.Time;

public class InventoryModel {
    public  int Id ;
    public String Erpvoucherno;//单号

    public String Checktype ;

    public String Checkdesc ;

    public int Checkstatus ;
    public String   Areano;

    public String Remarks ;

    public String Warehouseno ; //仓库编号

    public String Warehousename ;

    public  int Warehouseid;

    public int Headerid ;
    public int Areaid ;
    public  Float Qty;
    public String Materialdesc ;
    public String Materialno ;
    public String Strongholdcode ;

    public String Serialno;

    public int Materialid;

    public int Status ;

    public String Barcode ;
    public String Batchno ;

    public String Unit ;

    public String Supplierno ;
    public String Suppliername;
    public String Watercode ;

    public String Companycode ;

    public String Strongholdname ;

    public String Creater;

    public String Modifyer;

    public Time Modifytime;


    public  Float  ScannQty;

    public  boolean isCheck;

     public  int isProfit;
     // 1 盈利 2平  3亏
    public int getIsProfit() {
        return isProfit;
    }

    public void setIsProfit(int isProfit) {
        this.isProfit = isProfit;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public Float getScannQty() {
        return ScannQty;
    }

    public void setScannQty(Float scannQty) {
        ScannQty = scannQty;
    }

    public String getSerialno() {
        return Serialno;
    }

    public void setSerialno(String serialno) {
        Serialno = serialno;
    }

    public int getMaterialid() {
        return Materialid;
    }

    public void setMaterialid(int materialid) {
        Materialid = materialid;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getBatchno() {
        return Batchno;
    }

    public void setBatchno(String batchno) {
        Batchno = batchno;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getSupplierno() {
        return Supplierno;
    }

    public void setSupplierno(String supplierno) {
        Supplierno = supplierno;
    }

    public String getSuppliername() {
        return Suppliername;
    }

    public void setSuppliername(String suppliername) {
        Suppliername = suppliername;
    }

    public String getWatercode() {
        return Watercode;
    }

    public void setWatercode(String watercode) {
        Watercode = watercode;
    }

    public String getCompanycode() {
        return Companycode;
    }

    public void setCompanycode(String companycode) {
        Companycode = companycode;
    }

    public String getStrongholdname() {
        return Strongholdname;
    }

    public void setStrongholdname(String strongholdname) {
        Strongholdname = strongholdname;
    }

    public String getCreater() {
        return Creater;
    }

    public void setCreater(String creater) {
        Creater = creater;
    }

    public String getModifyer() {
        return Modifyer;
    }

    public void setModifyer(String modifyer) {
        Modifyer = modifyer;
    }

    public Time getModifytime() {
        return Modifytime;
    }

    public void setModifytime(Time modifytime) {
        Modifytime = modifytime;
    }

    public Float getQty() {
        return Qty;
    }

    public void setQty(Float qty) {
        Qty = qty;
    }

    public String getMaterialdesc() {
        return Materialdesc;
    }

    public void setMaterialdesc(String materialdesc) {
        Materialdesc = materialdesc;
    }

    public String getMaterialno() {
        return Materialno;
    }

    public void setMaterialno(String materialno) {
        Materialno = materialno;
    }

    public String getStrongholdcode() {
        return Strongholdcode;
    }

    public void setStrongholdcode(String strongholdcode) {
        Strongholdcode = strongholdcode;
    }

    public int getHeaderid() {
        return Headerid;
    }

    public void setHeaderid(int headerid) {
        Headerid = headerid;
    }

    public int getAreaid() {
        return Areaid;
    }

    public void setAreaid(int areaid) {
        Areaid = areaid;
    }

    public String getAreano() {
        return Areano;
    }

    public void setAreano(String areano) {
        Areano = areano;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String erpvoucherno) {
        Erpvoucherno = erpvoucherno;
    }

    public String getChecktype() {
        return Checktype;
    }

    public void setChecktype(String checktype) {
        Checktype = checktype;
    }

    public String getCheckdesc() {
        return Checkdesc;
    }

    public void setCheckdesc(String checkdesc) {
        Checkdesc = checkdesc;
    }

    public int getCheckstatus() {
        return Checkstatus;
    }

    public void setCheckstatus(int checkstatus) {
        Checkstatus = checkstatus;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getWarehouseno() {
        return Warehouseno;
    }

    public void setWarehouseno(String warehouseno) {
        Warehouseno = warehouseno;
    }

    public String getWarehousename() {
        return Warehousename;
    }

    public void setWarehousename(String warehousename) {
        Warehousename = warehousename;
    }
}
