package com.liansu.boduowms.modules.outstock.Model;

//拼箱展示数据类
public class SalesoutstockBoxListRequery {
public String PackageCode;


// 当前单据箱号
public int PackageSeq;

// 是否非库存拼箱 1:非库存拼箱 0：库存拼箱

public int IsStockCombine;

public  String Erpvoucherno;

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
