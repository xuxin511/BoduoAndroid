package com.liansu.boduowms.modules.inHouseStock.inventory.Model;

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

    public int Headerid ;

    public int Areaid ;

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
