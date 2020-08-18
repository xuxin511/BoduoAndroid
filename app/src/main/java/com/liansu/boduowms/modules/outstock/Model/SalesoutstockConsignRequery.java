package com.liansu.boduowms.modules.outstock.Model;

//托运明细展示类
public  class SalesoutstockConsignRequery {

    public String Materialdesc;


    public String Parametername;

    public String Qty;

    public String Rowno;

    public String Rownodel;

    public String Arrvoucherno;


    public String getMaterialdesc() {
        return Materialdesc;
    }

    public void setMaterialdesc(String materialdesc) {
        Materialdesc = materialdesc;
    }

    public String getParametername() {
        return Parametername;
    }

    public void setParametername(String parametername) {
        Parametername = parametername;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getRowno() {
        return Rowno;
    }

    public void setRowno(String rowno) {
        Rowno = rowno;
    }

    public String getRownodel() {
        return Rownodel;
    }

    public void setRownodel(String rownodel) {
        Rownodel = rownodel;
    }

    public String getArrvoucherno() {
        return Arrvoucherno;
    }

    public void setArrvoucherno(String arrvoucherno) {
        Arrvoucherno = arrvoucherno;
    }
}
