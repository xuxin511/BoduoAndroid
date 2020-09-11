package com.liansu.boduowms.modules.outstock.Model;

//拼箱展示数据类
public class SalesoutstockBoxListRequery {
public String PackageCode;
// 当前单据箱号
public int PackageSeq;

public  String Materialdesc;


    public  String Materialno;

    public  String spec;
    public  String Strongholdname;
    public  Float Qty;
    public  String Unit;
// 是否非库存拼箱 1:非库存拼箱 0：库存拼箱

public int IsStockCombine;

    public boolean isSelected;


public  String Erpvoucherno;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getStrongholdname() {
        return Strongholdname;
    }

    public void setStrongholdname(String strongholdname) {
        Strongholdname = strongholdname;
    }

    public Float getQty() {
        return Qty;
    }

    public void setQty(Float qty) {
        Qty = qty;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String erpvoucherno) {
        Erpvoucherno = erpvoucherno;
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

    public int getIsStockCombine() {
        return IsStockCombine;
    }

    public void setIsStockCombine(int isStockCombine) {
        IsStockCombine = isStockCombine;
    }
}
