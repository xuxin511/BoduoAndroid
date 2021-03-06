package com.liansu.boduowms.bean.qualitySpection;

import android.os.Parcel;
import android.os.Parcelable;

import com.liansu.boduowms.bean.stock.StockInfo;

import java.util.List;

/**
 * @ Des:质检明细类
 * @ Created by yangyiqing on 2020/7/9.
 */
public class QualityDetailInfo  implements Parcelable {

    public QualityDetailInfo() {

    }

    protected int                  Id;
    protected int                  Headerid;
    protected String               Rowno;
    protected String               Materialno;
    protected String               Materialdesc;
    protected float                Receiveqty;
    protected String               Unit;
    protected String               Storageloc;
    protected String               Plant;
    protected String               Plantname;
    protected String               Unitname;
    protected int                  Linestatus;
    protected float                Remainqty;
    protected String               Voucherno;
    protected String               Erpvoucherno;
    protected int                  Vouchertype;
    protected int                  Materialnoid;
    protected String               Rownodel;
    protected String               Purchaseqty;
    protected int                  Postqty;
    protected String               Postuser;
    protected Object               Postdate;
    protected String               Specialstock;//寄售库存
    protected float                Scanqty;
    protected float                Voucherqty;
    protected String               Batchno;
    protected String               Cusmaterialno;
    protected String               Strongholdcode;
    protected String               Strongholdname;
    protected String               Scanuserno;
    protected String               Supplierno;
    protected String               Suppliername;
    protected int             PackQty;   /// 包装规格
    protected List<StockInfo> LstBarCode;
    protected float           Outstockqty;
    /// <summary>
    /// 规格
    /// </summary>
    protected String               Spec;
    /// 合格数量
    protected int                  Isquality;
    protected String               Companycode;
    protected String               Arrvoucherno; //到货单号
    protected String               Towarehouseno;
    protected String               Printername; //打印机名称
    protected int                  Printertype; //打印机类型  1 激光打印机 2 台式打印机 3.蓝牙
    protected String               Username;
    private int    Towarehouseid;
    protected QualityDetailInfo(Parcel in) {
        Id = in.readInt();
        Headerid = in.readInt();
        Rowno = in.readString();
        Materialno = in.readString();
        Materialdesc = in.readString();
        Receiveqty = in.readFloat();
        Unit = in.readString();
        Storageloc = in.readString();
        Plant = in.readString();
        Plantname = in.readString();
        Unitname = in.readString();
        Linestatus = in.readInt();
        Remainqty = in.readFloat();
        Voucherno = in.readString();
        Erpvoucherno = in.readString();
        Vouchertype = in.readInt();
        Materialnoid = in.readInt();
        Rownodel = in.readString();
        Purchaseqty = in.readString();
        Postqty = in.readInt();
        Postuser = in.readString();
        Specialstock = in.readString();
        Scanqty = in.readFloat();
        Voucherqty = in.readFloat();
        Batchno = in.readString();
        Cusmaterialno = in.readString();
        Strongholdcode = in.readString();
        Strongholdname = in.readString();
        Scanuserno = in.readString();
        Supplierno = in.readString();
        Suppliername = in.readString();
        PackQty = in.readInt();
        LstBarCode = in.createTypedArrayList(StockInfo.CREATOR);
        Outstockqty = in.readFloat();
        Spec = in.readString();
        Isquality = in.readInt();
        Companycode = in.readString();
        Arrvoucherno = in.readString();
        Towarehouseno = in.readString();
        Printername = in.readString();
        Printertype = in.readInt();
        Username = in.readString();
        Towarehouseid=in.readInt();
    }

    public static final Creator<QualityDetailInfo> CREATOR = new Creator<QualityDetailInfo>() {
        @Override
        public QualityDetailInfo createFromParcel(Parcel in) {
            return new QualityDetailInfo(in);
        }

        @Override
        public QualityDetailInfo[] newArray(int size) {
            return new QualityDetailInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeInt(Headerid);
        dest.writeString(Rowno);
        dest.writeString(Materialno);
        dest.writeString(Materialdesc);
        dest.writeFloat(Receiveqty);
        dest.writeString(Unit);
        dest.writeString(Storageloc);
        dest.writeString(Plant);
        dest.writeString(Plantname);
        dest.writeString(Unitname);
        dest.writeInt(Linestatus);
        dest.writeFloat(Remainqty);
        dest.writeString(Voucherno);
        dest.writeString(Erpvoucherno);
        dest.writeInt(Vouchertype);
        dest.writeInt(Materialnoid);
        dest.writeString(Rownodel);
        dest.writeString(Purchaseqty);
        dest.writeInt(Postqty);
        dest.writeString(Postuser);
        dest.writeString(Specialstock);
        dest.writeFloat(Scanqty);
        dest.writeFloat(Voucherqty);
        dest.writeString(Batchno);
        dest.writeString(Cusmaterialno);
        dest.writeString(Strongholdcode);
        dest.writeString(Strongholdname);
        dest.writeString(Scanuserno);
        dest.writeString(Supplierno);
        dest.writeString(Suppliername);
        dest.writeInt(PackQty);
        dest.writeTypedList(LstBarCode);
        dest.writeFloat(Outstockqty);
        dest.writeString(Spec);
        dest.writeInt(Isquality);
        dest.writeString(Companycode);
        dest.writeString(Arrvoucherno);
        dest.writeString(Towarehouseno);
        dest.writeString(Printername);
        dest.writeInt(Printertype);
        dest.writeString(Username);
        dest.writeInt(Towarehouseid);
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getHeaderid() {
        return Headerid;
    }

    public void setHeaderid(int headerid) {
        Headerid = headerid;
    }

    public String getRowno() {
        return Rowno;
    }

    public void setRowno(String rowno) {
        Rowno = rowno;
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

    public float getReceiveqty() {
        return Receiveqty;
    }

    public void setReceiveqty(float receiveqty) {
        Receiveqty = receiveqty;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getStorageloc() {
        return Storageloc;
    }

    public void setStorageloc(String storageloc) {
        Storageloc = storageloc;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getPlantname() {
        return Plantname;
    }

    public void setPlantname(String plantname) {
        Plantname = plantname;
    }

    public String getUnitname() {
        return Unitname;
    }

    public void setUnitname(String unitname) {
        Unitname = unitname;
    }

    public int getLinestatus() {
        return Linestatus;
    }

    public void setLinestatus(int linestatus) {
        Linestatus = linestatus;
    }

    public float getRemainqty() {
        return Remainqty;
    }

    public void setRemainqty(float remainqty) {
        Remainqty = remainqty;
    }

    public String getVoucherno() {
        return Voucherno;
    }

    public void setVoucherno(String voucherno) {
        Voucherno = voucherno;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String erpvoucherno) {
        Erpvoucherno = erpvoucherno;
    }

    public int getVouchertype() {
        return Vouchertype;
    }

    public void setVouchertype(int vouchertype) {
        Vouchertype = vouchertype;
    }

    public int getMaterialnoid() {
        return Materialnoid;
    }

    public void setMaterialnoid(int materialnoid) {
        Materialnoid = materialnoid;
    }

    public String getRownodel() {
        return Rownodel;
    }

    public void setRownodel(String rownodel) {
        Rownodel = rownodel;
    }

    public String getPurchaseqty() {
        return Purchaseqty;
    }

    public void setPurchaseqty(String purchaseqty) {
        Purchaseqty = purchaseqty;
    }

    public int getPostqty() {
        return Postqty;
    }

    public void setPostqty(int postqty) {
        Postqty = postqty;
    }

    public String getPostuser() {
        return Postuser;
    }

    public void setPostuser(String postuser) {
        Postuser = postuser;
    }

    public Object getPostdate() {
        return Postdate;
    }

    public void setPostdate(Object postdate) {
        Postdate = postdate;
    }

    public String getSpecialstock() {
        return Specialstock;
    }

    public void setSpecialstock(String specialstock) {
        Specialstock = specialstock;
    }

    public float getScanqty() {
        return Scanqty;
    }

    public void setScanqty(float scanqty) {
        Scanqty = scanqty;
    }

    public float getVoucherqty() {
        return Voucherqty;
    }

    public void setVoucherqty(float voucherqty) {
        Voucherqty = voucherqty;
    }

    public String getBatchno() {
        return Batchno;
    }

    public void setBatchno(String batchno) {
        Batchno = batchno;
    }

    public String getCusmaterialno() {
        return Cusmaterialno;
    }

    public void setCusmaterialno(String cusmaterialno) {
        Cusmaterialno = cusmaterialno;
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

    public String getScanuserno() {
        return Scanuserno;
    }

    public void setScanuserno(String scanuserno) {
        Scanuserno = scanuserno;
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

    public int getPackQty() {
        return PackQty;
    }

    public void setPackQty(int packQty) {
        PackQty = packQty;
    }

    public List<StockInfo> getLstBarCode() {
        return LstBarCode;
    }

    public void setLstBarCode(List<StockInfo> lstBarCode) {
        LstBarCode = lstBarCode;
    }

    public float getOutstockqty() {
        return Outstockqty;
    }

    public void setOutstockqty(float outstockqty) {
        Outstockqty = outstockqty;
    }

    public String getSpec() {
        return Spec;
    }

    public void setSpec(String spec) {
        Spec = spec;
    }

    public int getIsquality() {
        return Isquality;
    }

    public void setIsquality(int isquality) {
        Isquality = isquality;
    }

    public String getCompanycode() {
        return Companycode;
    }

    public void setCompanycode(String companycode) {
        Companycode = companycode;
    }

    public String getArrvoucherno() {
        return Arrvoucherno;
    }

    public void setArrvoucherno(String arrvoucherno) {
        Arrvoucherno = arrvoucherno;
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

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public static Creator<QualityDetailInfo> getCREATOR() {
        return CREATOR;
    }

    public int getTowarehouseid() {
        return Towarehouseid;
    }

    public void setTowarehouseid(int towarehouseid) {
        Towarehouseid = towarehouseid;
    }
}
