

package com.liansu.boduowms.bean.barcode;

import android.os.Parcel;
import android.os.Parcelable;

import com.liansu.boduowms.bean.base.BaseInfo;

import androidx.annotation.NonNull;

public class OutBarcodeInfo extends BaseInfo implements Parcelable, Cloneable {

    /**
     * Voucherno :
     * Rowno : 10
     * Erpvoucherno : 4500000010
     * Vouchertype : 22
     * Materialno : YL001
     * Materialdesc : ABC0200箱 YL001
     * Cuscode :
     * Cusname :
     * Supplierno :
     * Suppliername :
     * Qty : 100
     * Barcode : 6311738-01,20011201,12010,4500307491,20011251263,5,B
     * Barcodetype : 0
     * Serialno :
     * Materialnoid : 0
     * Edate :
     * Batchno :
     * Unit :
     */

    private   String Voucherno;
    private   String Rowno;
    protected String Rownodel;
    private   String Erpvoucherno;
    private   int    Vouchertype;
    private   String Materialno;
    private   String Materialdesc;
    private   String Cuscode;
    private   String Cusname;
    private   String Supplierno;
    private   String Suppliername;
    private   float  Qty;
    private   String Barcode;
    private   int    Barcodetype;
    private   String Serialno;
    private   int    Materialnoid;
    private   String Edate;
    private   String Batchno;
    private   String Unit;
    private   String Towarehouseno;
    private   int    Towarehouseid;
    private   String Areano;
    private   String Prodate;
    private   String CusMaterialNo;
    private   String Scanuserno;
    private   String Specialstock;
    private   int Headeridsub;//关联表体ID
    private   float Packqty; //包装量
//    private   int    PackQty; //包装量
    /**
     * 1-不拆零 2-拆零
     */
    private   int    IsAmount;
    private   String WCustomerno;
    protected String Spec;
    protected String Printername; //打印机名称
    protected int    Printertype; //打印机类型  1 激光打印机 2 台式打印机 3.蓝牙
    protected String Postuser;//过账人
    protected String Username;//登录人
    protected String Unitname;
    protected float  Printqty;   //托盘打印功能 剩余数量
    protected String Watercode;
    protected String Customerno;
    protected int  Areaid;
    protected  int Scantype; //1-原托盘入库 0-新托盘入库
    protected  String WBarcode;//外箱码
    protected  String WBatchno;//外箱批次

    public OutBarcodeInfo() {
    }

    protected OutBarcodeInfo(Parcel in) {
        Voucherno = in.readString();
        Rowno = in.readString();
        Erpvoucherno = in.readString();
        Vouchertype = in.readInt();
        Materialno = in.readString();
        Materialdesc = in.readString();
        Cuscode = in.readString();
        Cusname = in.readString();
        Supplierno = in.readString();
        Suppliername = in.readString();
        Qty = in.readFloat();
        Barcode = in.readString();
        Barcodetype = in.readInt();
        Serialno = in.readString();
        Materialnoid = in.readInt();
        Edate = in.readString();
        Batchno = in.readString();
        Unit = in.readString();
        Towarehouseno = in.readString();
        Towarehouseid = in.readInt();
        Areano = in.readString();
        Strongholdcode = in.readString();
        Strongholdname = in.readString();
        Prodate = in.readString();
        CusMaterialNo = in.readString();
        Scanuserno = in.readString();
        Specialstock = in.readString();
        Headeridsub = in.readInt();
        IsAmount = in.readInt();
        WCustomerno = in.readString();
        Packqty = in.readFloat();
        Rownodel = in.readString();
        Spec = in.readString();
        Printername = in.readString();
        Printertype = in.readInt();
        Postuser = in.readString();
        Username=in.readString();
        Unitname=in.readString();
        Printqty=in.readFloat();
        Watercode=in.readString();
        Customerno=in.readString();
        Areaid=in.readInt();
        Scantype=in.readInt();
        WBarcode=in.readString();
        WBatchno=in.readString();
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        OutBarcodeInfo outBarcodeInfo = (OutBarcodeInfo) super.clone();
        return outBarcodeInfo;

    }

    public static final Creator<OutBarcodeInfo> CREATOR = new Creator<OutBarcodeInfo>() {
        @Override
        public OutBarcodeInfo createFromParcel(Parcel in) {
            return new OutBarcodeInfo(in);
        }

        @Override
        public OutBarcodeInfo[] newArray(int size) {
            return new OutBarcodeInfo[size];
        }
    };

    public String getVoucherno() {
        return Voucherno;
    }

    public void setVoucherno(String Voucherno) {
        this.Voucherno = Voucherno;
    }

    public String getRowno() {
        return Rowno;
    }

    public void setRowno(String Rowno) {
        this.Rowno = Rowno;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String Erpvoucherno) {
        this.Erpvoucherno = Erpvoucherno;
    }

    public int getVouchertype() {
        return Vouchertype;
    }

    public void setVouchertype(int Vouchertype) {
        this.Vouchertype = Vouchertype;
    }

    public String getMaterialno() {
        return Materialno;
    }

    public void setMaterialno(String Materialno) {
        this.Materialno = Materialno;
    }

    public String getMaterialdesc() {
        return Materialdesc;
    }

    public void setMaterialdesc(String Materialdesc) {
        this.Materialdesc = Materialdesc;
    }

    public String getCuscode() {
        return Cuscode;
    }

    public void setCuscode(String Cuscode) {
        this.Cuscode = Cuscode;
    }

    public String getCusname() {
        return Cusname;
    }

    public void setCusname(String Cusname) {
        this.Cusname = Cusname;
    }

    public String getSupplierno() {
        return Supplierno;
    }

    public void setSupplierno(String Supplierno) {
        this.Supplierno = Supplierno;
    }

    public String getSuppliername() {
        return Suppliername;
    }

    public void setSuppliername(String Suppliername) {
        this.Suppliername = Suppliername;
    }

    public float getQty() {
        return Qty;
    }

    public void setQty(float Qty) {
        this.Qty = Qty;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String Barcode) {
        this.Barcode = Barcode;
    }

    public int getBarcodetype() {
        return Barcodetype;
    }

    public void setBarcodetype(int Barcodetype) {
        this.Barcodetype = Barcodetype;
    }

    public String getSerialno() {
        return Serialno;
    }

    public void setSerialno(String Serialno) {
        this.Serialno = Serialno;
    }

    public int getMaterialnoid() {
        return Materialnoid;
    }

    public void setMaterialnoid(int Materialnoid) {
        this.Materialnoid = Materialnoid;
    }

    public String getEdate() {
        return Edate;
    }

    public void setEdate(String Edate) {
        this.Edate = Edate;
    }

    public String getBatchno() {
        return Batchno;
    }

    public void setBatchno(String Batchno) {
        this.Batchno = Batchno;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String Unit) {
        this.Unit = Unit;
    }

    public String getTowarehouseno() {
        return Towarehouseno;
    }

    public void setTowarehouseno(String towarehouseno) {
        Towarehouseno = towarehouseno;
    }

    public int getTowarehouseid() {
        return Towarehouseid;
    }

    public void setTowarehouseid(int towarehouseid) {
        Towarehouseid = towarehouseid;
    }

    public String getAreano() {
        return Areano;
    }

    public void setAreano(String areano) {
        Areano = areano;
    }


    public String getProdate() {
        return Prodate;
    }

    public void setProdate(String prodate) {
        Prodate = prodate;
    }

    public String getStrongholdcode() {
        return Strongholdcode;
    }

    public void setStrongholdcode(String strongholdcode) {
        Strongholdcode = strongholdcode;
    }

    public String getCusMaterialNo() {
        return CusMaterialNo;
    }

    public void setCusMaterialNo(String cusMaterialNo) {
        CusMaterialNo = cusMaterialNo;
    }

    public int getIsAmount() {
        return IsAmount;
    }

    public void setIsAmount(int isAmount) {
        IsAmount = isAmount;
    }

    public String getWCustomerno() {
        return WCustomerno;
    }

    public void setWCustomerno(String WCustomerno) {
        this.WCustomerno = WCustomerno;
    }

    public String getScanuserno() {
        return Scanuserno;
    }

    public void setScanuserno(String scanuserno) {
        Scanuserno = scanuserno;
    }

    public String getSpecialstock() {
        return Specialstock;
    }

    public void setSpecialstock(String specialstock) {
        Specialstock = specialstock;
    }

    public int getHeaderidsub() {
        return Headeridsub;
    }

    public void setHeaderidsub(int headeridsub) {
        Headeridsub = headeridsub;
    }

    public float getPackqty() {
        return Packqty;
    }

    public void setPackqty(float packqty) {
        Packqty = packqty;
    }

    public String getRownodel() {
        return Rownodel;
    }

    public void setRownodel(String rownodel) {
        Rownodel = rownodel;
    }

    public String getSpec() {
        return Spec;
    }

    public void setSpec(String spec) {
        Spec = spec;
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

    public String getPostuser() {
        return Postuser;
    }

    public void setPostuser(String postuser) {
        Postuser = postuser;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUnitname() {
        return Unitname;
    }

    public void setUnitname(String unitname) {
        Unitname = unitname;
    }

    public float getPrintqty() {
        return Printqty;
    }

    public void setPrintqty(float printqty) {
        Printqty = printqty;
    }

    public String getWatercode() {
        return Watercode;
    }

    public void setWatercode(String watercode) {
        Watercode = watercode;
    }

    public String getCustomerno() {
        return Customerno;
    }

    public void setCustomerno(String customerno) {
        Customerno = customerno;
    }

    public int getAreaid() {
        return Areaid;
    }

    public void setAreaid(int areaid) {
        Areaid = areaid;
    }

    public int getScantype() {
        return Scantype;
    }

    public void setScantype(int scantype) {
        Scantype = scantype;
    }

    public String getWBarcode() {
        return WBarcode;
    }

    public void setWBarcode(String WBarcode) {
        this.WBarcode = WBarcode;
    }

    public String getWBatchno() {
        return WBatchno;
    }

    public void setWBatchno(String WBatchno) {
        this.WBatchno = WBatchno;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Voucherno);
        dest.writeString(Rowno);
        dest.writeString(Erpvoucherno);
        dest.writeInt(Vouchertype);
        dest.writeString(Materialno);
        dest.writeString(Materialdesc);
        dest.writeString(Cuscode);
        dest.writeString(Cusname);
        dest.writeString(Supplierno);
        dest.writeString(Suppliername);
        dest.writeFloat(Qty);
        dest.writeString(Barcode);
        dest.writeInt(Barcodetype);
        dest.writeString(Serialno);
        dest.writeInt(Materialnoid);
        dest.writeString(Edate);
        dest.writeString(Batchno);
        dest.writeString(Unit);
        dest.writeString(Towarehouseno);
        dest.writeInt(Towarehouseid);
        dest.writeString(Areano);
        dest.writeString(Strongholdcode);
        dest.writeString(Strongholdname);
        dest.writeString(Prodate);
        dest.writeString(CusMaterialNo);
        dest.writeString(Scanuserno);
        dest.writeString(Specialstock);
        dest.writeInt(Headeridsub);
        dest.writeInt(IsAmount);
        dest.writeString(WCustomerno);
        dest.writeFloat(Packqty);
        dest.writeString(Rownodel);
        dest.writeString(Spec);
        dest.writeString(Printername);
        dest.writeInt(Printertype);
        dest.writeString(Postuser);
        dest.writeString(Username);
        dest.writeString(Unitname);
        dest.writeString(Watercode);
        dest.writeString(Customerno);
        dest.writeInt(Areaid);
        dest.writeInt(Scantype);
        dest.writeString(WBarcode);
        dest.writeString(WBatchno);
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(Barcode, Serialno);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OutBarcodeInfo)) return false;
        OutBarcodeInfo that = (OutBarcodeInfo) o;
        return Barcode.equals(that.Barcode);
    }


}
