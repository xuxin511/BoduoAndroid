package com.liansu.boduowms.modules.outstock.Model;

  public class OutstockOrderSelectModel {

    public  String Materialno;

    public  String Materialdesc;

    public  Float  Voucherqty;
    public  Float  Postqty;
    public  Float  Notpostqty;
    public  Float Outstockqty;
    public  Float ReviewQty;

      public Float getOutstockqty() {
          return Outstockqty;
      }

      public void setOutstockqty(Float outstockqty) {
          Outstockqty = outstockqty;
      }

      public Float getReviewQty() {
          return ReviewQty;
      }

      public void setReviewQty(Float reviewQty) {
          ReviewQty = reviewQty;
      }

      public  String Arrvoucherno;
    public  String Erpvoucherno;



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

    public Float getVoucherqty() {
        return Voucherqty;
    }

    public void setVoucherqty(Float voucherqty) {
        Voucherqty = voucherqty;
    }

    public Float getPostqty() {
        return Postqty;
    }

    public void setPostqty(Float postqty) {
        Postqty = postqty;
    }

    public Float getNotpostqty() {
        return Notpostqty;
    }

    public void setNotpostqty(Float notpostqty) {
        Notpostqty = notpostqty;
    }

    public String getArrvoucherno() {
        return Arrvoucherno;
    }

    public void setArrvoucherno(String arrvoucherno) {
        Arrvoucherno = arrvoucherno;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String erpvoucherno) {
        Erpvoucherno = erpvoucherno;
    }
}
