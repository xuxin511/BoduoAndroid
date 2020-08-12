package com.liansu.boduowms.bean.order;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/20.
 */
public class OutStockOrderDetailInfo implements Parcelable {

    /**
     * Unit : BOT
     * Spec : 8瓶*2升
     * Unitname : 瓶、罐、桶
     * Erpnote :
     * MaterialCartonNum : 1
     * MaterialPartNum : 0
     * ReviewQty : 0
     * Headerid : 15
     * Id : 27
     * Companycode : 10
     * Strongholdcode : 2206
     * Strongholdname : 密巢销售有限公司
     * Erpvoucherno : 2102-B520-2007200001
     * Materialno : 130100001
     * Materialdesc : 百香果风味饮料（博多家园）1
     * Rowno : 1
     * Rownodel : 1
     * Scanqty : 0
     * Outstockqty : 0
     * PackQty : 1
     * Voucherqty : 1
     * Remainqty : 1
     * Areano : 81102,20200727
     */

    private String Unit;
    private String Spec;
    private String Unitname;
    private String Erpnote;
    private int    MaterialCartonNum;
    private int    MaterialPartNum;
    private float    ReviewQty;
    private int    Headerid;
    private int    Id;
    private String Companycode;
    private String Strongholdcode;
    private String Strongholdname;
    private String Erpvoucherno;
    private String Materialno;
    private String Materialdesc;
    private String Rowno;
    private String Rownodel;
    private float  Scanqty;
    private float  Outstockqty;
    private float  PackQty;
    private float  Voucherqty;
    private float  Remainqty;
    private String Areano;
    private String Batchno;
    private  String PalletNo;
    private  int BarcodeType;
    private int Vouchertype;
    private  String PostUserNo;
    private String Towarehouseno;
    private String Printername;
    private int  Printertype;
    private  Float Qty;//下架数量
    private  String Arrvoucherno;

    public String getArrvoucherNO() {
        return Arrvoucherno;
    }

    public void setArrvoucherNO(String Arrvoucherno) {
        Arrvoucherno = Arrvoucherno;
    }

    public Float getQty() {
        return Qty;
    }

    public void setQTY(Float Qty) {
        this.Qty = Qty;
    }

    public OutStockOrderDetailInfo() {
    }

    protected OutStockOrderDetailInfo(Parcel in) {
        Unit = in.readString();
        Spec = in.readString();
        Unitname = in.readString();
        Erpnote = in.readString();
        MaterialCartonNum = in.readInt();
        MaterialPartNum = in.readInt();
        ReviewQty = in.readFloat();
        Headerid = in.readInt();
        Id = in.readInt();
        Companycode = in.readString();
        Strongholdcode = in.readString();
        Strongholdname = in.readString();
        Erpvoucherno = in.readString();
        Materialno = in.readString();
        Materialdesc = in.readString();
        Rowno = in.readString();
        Rownodel = in.readString();
        Scanqty = in.readFloat();
        Outstockqty = in.readFloat();
        PackQty = in.readFloat();
        Voucherqty = in.readFloat();
        Remainqty = in.readFloat();
        Areano = in.readString();
        Batchno = in.readString();
        PalletNo=in.readString();
        BarcodeType=in.readInt();
        Vouchertype=in.readInt();
        PostUserNo=in.readString();
        Towarehouseno=in.readString();
        Printername=in.readString();
        Printertype=in.readInt();
        Qty=in.readFloat();
        Arrvoucherno=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Unit);
        dest.writeString(Spec);
        dest.writeString(Unitname);
        dest.writeString(Erpnote);
        dest.writeInt(MaterialCartonNum);
        dest.writeInt(MaterialPartNum);
        dest.writeFloat(ReviewQty);
        dest.writeInt(Headerid);
        dest.writeInt(Id);
        dest.writeString(Companycode);
        dest.writeString(Strongholdcode);
        dest.writeString(Strongholdname);
        dest.writeString(Erpvoucherno);
        dest.writeString(Materialno);
        dest.writeString(Materialdesc);
        dest.writeString(Rowno);
        dest.writeString(Rownodel);
        dest.writeFloat(Scanqty);
        dest.writeFloat(Outstockqty);
        dest.writeFloat(PackQty);
        dest.writeFloat(Voucherqty);
        dest.writeFloat(Remainqty);
        dest.writeString(Areano);
        dest.writeString(Batchno);
        dest.writeString(PalletNo);
        dest.writeInt(BarcodeType);
        dest.writeInt(Vouchertype);
        dest.writeString(PostUserNo);
        dest.writeString(Towarehouseno);
        dest.writeString(Printername);
        dest.writeInt(Printertype);
        dest.writeFloat(Qty);
        dest.writeString(Arrvoucherno);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OutStockOrderDetailInfo> CREATOR = new Creator<OutStockOrderDetailInfo>() {
        @Override
        public OutStockOrderDetailInfo createFromParcel(Parcel in) {
            return new OutStockOrderDetailInfo(in);
        }

        @Override
        public OutStockOrderDetailInfo[] newArray(int size) {
            return new OutStockOrderDetailInfo[size];
        }
    };

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getSpec() {
        return Spec;
    }

    public void setSpec(String spec) {
        Spec = spec;
    }

    public String getUnitname() {
        return Unitname;
    }

    public void setUnitname(String unitname) {
        Unitname = unitname;
    }

    public String getErpnote() {
        return Erpnote;
    }

    public void setErpnote(String erpnote) {
        Erpnote = erpnote;
    }

    public int getMaterialCartonNum() {
        return MaterialCartonNum;
    }

    public void setMaterialCartonNum(int materialCartonNum) {
        MaterialCartonNum = materialCartonNum;
    }

    public int getMaterialPartNum() {
        return MaterialPartNum;
    }

    public void setMaterialPartNum(int materialPartNum) {
        MaterialPartNum = materialPartNum;
    }

    public float getReviewQty() {
        return ReviewQty;
    }

    public void setReviewQty(float reviewQty) {
        ReviewQty = reviewQty;
    }

    public int getHeaderid() {
        return Headerid;
    }

    public void setHeaderid(int headerid) {
        Headerid = headerid;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCompanycode() {
        return Companycode;
    }

    public void setCompanycode(String companycode) {
        Companycode = companycode;
    }

    public String getStrongholdcode() {
        return Strongholdcode;
    }

    public void setStrongholdcode(String strongholdcode) {
        Strongholdcode = strongholdcode;
    }

    public String getStrongholdname() {
        return Strongholdname;
    }

    public void setStrongholdname(String strongholdname) {
        Strongholdname = strongholdname;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String erpvoucherno) {
        Erpvoucherno = erpvoucherno;
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

    public float getScanqty() {
        return Scanqty;
    }

    public void setScanqty(float scanqty) {
        Scanqty = scanqty;
    }

    public float getOutstockqty() {
        return Outstockqty;
    }

    public void setOutstockqty(float outstockqty) {
        Outstockqty = outstockqty;
    }

    public float getPackQty() {
        return PackQty;
    }

    public void setPackQty(float packQty) {
        PackQty = packQty;
    }

    public float getVoucherqty() {
        return Voucherqty;
    }

    public void setVoucherqty(float voucherqty) {
        Voucherqty = voucherqty;
    }

    public float getRemainqty() {
        return Remainqty;
    }

    public void setRemainqty(float remainqty) {
        Remainqty = remainqty;
    }

    public String getAreano() {
        return Areano;
    }

    public void setAreano(String areano) {
        Areano = areano;
    }

    public String getBatchno() {
        return Batchno;
    }

    public void setBatchno(String batchno) {
        Batchno = batchno;
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

    public String getPostUserNo() {
        return PostUserNo;
    }

    public void setPostUserNo(String postUserNo) {
        PostUserNo = postUserNo;
    }

    public String getTowarehouseno() {
        return Towarehouseno;
    }

    public void setTowarehouseno(String towarehouseno) {
        Towarehouseno = towarehouseno;
    }

    public String getPrintername() {
        return Printername;
    }

    public void setPrintername(String printername) {
        Printername = printername;
    }

    public int getPrintertype() {
        return Printertype;
    }

    public void setPrintertype(int printertype) {
        Printertype = printertype;
    }
}
