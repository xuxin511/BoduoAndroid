package com.liansu.boduowms.bean.stock;

import android.os.Parcel;
import android.os.Parcelable;

import com.liansu.boduowms.bean.base.BaseInfo;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/1.
 */
public class StockInfo extends BaseInfo implements Parcelable {


    private String Barcode;
    private String Serialno;
    private String Materialno;
    private String Materialdesc;
    private int    Towarehouseid;
    private int    Houseid;
    private int    Areaid;
    private float  Qty;
    private String Pickareano;
    private String Celareano;
    private int    Status;
    private int    Isdel;
    private String Batchno;
    private String Returnsupcode;
    private String Returnreson;
    private String Returnsupname;
    private int    Oldstockid;
    private int    Taskdetailesid;
    private int    Checkid;
    private int    Transferdetailsid;
    private String Unit;
    private String Unitname;
    private String Palletno;
    private int    Receivestatus;
    private int    Islimitstock;
    private int    Materialnoid;
    private String Edate;
    private String Supplierno;
    private String Suppliername;
    private int    Isretention;
    private String Specialstock;
    private String Cusmaterialno;
    private String Towarehouseno;
    private String Houseno;
    private String Areano;
    private String Prodate;
    private int    Vouchertype;
    private int    IsAmount;
    private String Rowno;
    private String WCustomerno;
    private float  Postqty;
    private String Scanuserno;
    private String Fromwarehouseno;
    private String Toareano;
    private String Fromareano;
    private String GUID;
    private String Erpvoucherno;
    /// 商品69码
    private String watercode;
    /// 合格数量
    private float  QualityQty;
    /// 不合格数量
    private float  UnQualityQty;
    /// 拣货数量
    private float  TaskQty;
    /// 锁定数量
    private float  LockQty;
    /// 质检单
    private String Qualityno;
    /// 到货单
    private String Arrvoucherno;


    public StockInfo() {
    }



    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getSerialno() {
        return Serialno;
    }

    public void setSerialno(String serialno) {
        Serialno = serialno;
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

    public int getTowarehouseid() {
        return Towarehouseid;
    }

    public void setTowarehouseid(int towarehouseid) {
        Towarehouseid = towarehouseid;
    }

    public int getHouseid() {
        return Houseid;
    }

    public void setHouseid(int houseid) {
        Houseid = houseid;
    }

    public int getAreaid() {
        return Areaid;
    }

    public void setAreaid(int areaid) {
        Areaid = areaid;
    }

    public float getQty() {
        return Qty;
    }

    public void setQty(float qty) {
        Qty = qty;
    }

    public String getPickareano() {
        return Pickareano;
    }

    public void setPickareano(String pickareano) {
        Pickareano = pickareano;
    }

    public String getCelareano() {
        return Celareano;
    }

    public void setCelareano(String celareano) {
        Celareano = celareano;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getIsdel() {
        return Isdel;
    }

    public void setIsdel(int isdel) {
        Isdel = isdel;
    }

    public String getBatchno() {
        return Batchno;
    }

    public void setBatchno(String batchno) {
        Batchno = batchno;
    }

    public String getReturnsupcode() {
        return Returnsupcode;
    }

    public void setReturnsupcode(String returnsupcode) {
        Returnsupcode = returnsupcode;
    }

    public String getReturnreson() {
        return Returnreson;
    }

    public void setReturnreson(String returnreson) {
        Returnreson = returnreson;
    }

    public String getReturnsupname() {
        return Returnsupname;
    }

    public void setReturnsupname(String returnsupname) {
        Returnsupname = returnsupname;
    }

    public int getOldstockid() {
        return Oldstockid;
    }

    public void setOldstockid(int oldstockid) {
        Oldstockid = oldstockid;
    }

    public int getTaskdetailesid() {
        return Taskdetailesid;
    }

    public void setTaskdetailesid(int taskdetailesid) {
        Taskdetailesid = taskdetailesid;
    }

    public int getCheckid() {
        return Checkid;
    }

    public void setCheckid(int checkid) {
        Checkid = checkid;
    }

    public int getTransferdetailsid() {
        return Transferdetailsid;
    }

    public void setTransferdetailsid(int transferdetailsid) {
        Transferdetailsid = transferdetailsid;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getUnitname() {
        return Unitname;
    }

    public void setUnitname(String unitname) {
        Unitname = unitname;
    }

    public String getPalletno() {
        return Palletno;
    }

    public void setPalletno(String palletno) {
        Palletno = palletno;
    }

    public int getReceivestatus() {
        return Receivestatus;
    }

    public void setReceivestatus(int receivestatus) {
        Receivestatus = receivestatus;
    }

    public int getIslimitstock() {
        return Islimitstock;
    }

    public void setIslimitstock(int islimitstock) {
        Islimitstock = islimitstock;
    }

    public int getMaterialnoid() {
        return Materialnoid;
    }

    public void setMaterialnoid(int materialnoid) {
        Materialnoid = materialnoid;
    }

    public String getEdate() {
        return Edate;
    }

    public void setEdate(String edate) {
        Edate = edate;
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

    public int getIsretention() {
        return Isretention;
    }

    public void setIsretention(int isretention) {
        Isretention = isretention;
    }

    public String getSpecialstock() {
        return Specialstock;
    }

    public void setSpecialstock(String specialstock) {
        Specialstock = specialstock;
    }

    public String getCusmaterialno() {
        return Cusmaterialno;
    }

    public void setCusmaterialno(String cusmaterialno) {
        Cusmaterialno = cusmaterialno;
    }

    public String getTowarehouseno() {
        return Towarehouseno;
    }

    public void setTowarehouseno(String towarehouseno) {
        Towarehouseno = towarehouseno;
    }

    public String getHouseno() {
        return Houseno;
    }

    public void setHouseno(String houseno) {
        Houseno = houseno;
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

    public int getVouchertype() {
        return Vouchertype;
    }

    public void setVouchertype(int vouchertype) {
        Vouchertype = vouchertype;
    }

    public int getIsAmount() {
        return IsAmount;
    }

    public void setIsAmount(int isAmount) {
        IsAmount = isAmount;
    }

    public String getRowno() {
        return Rowno;
    }

    public void setRowno(String rowno) {
        Rowno = rowno;
    }

    public String getWCustomerno() {
        return WCustomerno;
    }

    public void setWCustomerno(String WCustomerno) {
        this.WCustomerno = WCustomerno;
    }

    public float getPostqty() {
        return Postqty;
    }

    public void setPostqty(float postqty) {
        Postqty = postqty;
    }

    public String getScanuserno() {
        return Scanuserno;
    }

    public void setScanuserno(String scanuserno) {
        Scanuserno = scanuserno;
    }

    public String getFromwarehouseno() {
        return Fromwarehouseno;
    }

    public void setFromwarehouseno(String fromwarehouseno) {
        Fromwarehouseno = fromwarehouseno;
    }

    public String getToareano() {
        return Toareano;
    }

    public void setToareano(String toareano) {
        Toareano = toareano;
    }

    public String getFromareano() {
        return Fromareano;
    }

    public void setFromareano(String fromareano) {
        Fromareano = fromareano;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String erpvoucherno) {
        Erpvoucherno = erpvoucherno;
    }

    public String getWatercode() {
        return watercode;
    }

    public void setWatercode(String watercode) {
        this.watercode = watercode;
    }

    public float getQualityQty() {
        return QualityQty;
    }

    public void setQualityQty(float qualityQty) {
        QualityQty = qualityQty;
    }

    public float getUnQualityQty() {
        return UnQualityQty;
    }

    public void setUnQualityQty(float unQualityQty) {
        UnQualityQty = unQualityQty;
    }

    public float getTaskQty() {
        return TaskQty;
    }

    public void setTaskQty(float taskQty) {
        TaskQty = taskQty;
    }

    public float getLockQty() {
        return LockQty;
    }

    public void setLockQty(float lockQty) {
        LockQty = lockQty;
    }

    public String getQualityno() {
        return Qualityno;
    }

    public void setQualityno(String qualityno) {
        Qualityno = qualityno;
    }

    public String getArrvoucherno() {
        return Arrvoucherno;
    }

    public void setArrvoucherno(String arrvoucherno) {
        Arrvoucherno = arrvoucherno;
    }

    public static Creator<StockInfo> getCREATOR() {
        return CREATOR;
    }

    protected StockInfo(Parcel in) {
        Id = in.readInt();
        Barcode = in.readString();
        Serialno = in.readString();
        Materialno = in.readString();
        Materialdesc = in.readString();
        Towarehouseid = in.readInt();
        Houseid = in.readInt();
        Areaid = in.readInt();
        Qty = in.readFloat();
        Pickareano = in.readString();
        Celareano = in.readString();
        Status = in.readInt();
        Isdel = in.readInt();
        Batchno = in.readString();
        Returnsupcode = in.readString();
        Returnreson = in.readString();
        Returnsupname = in.readString();
        Oldstockid = in.readInt();
        Taskdetailesid = in.readInt();
        Checkid = in.readInt();
        Transferdetailsid = in.readInt();
        Unit = in.readString();
        Unitname = in.readString();
        Palletno = in.readString();
        Receivestatus = in.readInt();
        Islimitstock = in.readInt();
        Materialnoid = in.readInt();
        Edate = in.readString();
        Supplierno = in.readString();
        Suppliername = in.readString();
        Isretention = in.readInt();
        Specialstock = in.readString();
        Cusmaterialno = in.readString();
        Towarehouseno = in.readString();
        Houseno = in.readString();
        Areano = in.readString();
        Prodate = in.readString();
        Vouchertype = in.readInt();
        IsAmount = in.readInt();
        Rowno = in.readString();
        WCustomerno = in.readString();
        Postqty = in.readFloat();
        Scanuserno = in.readString();
        Fromwarehouseno = in.readString();
        Toareano = in.readString();
        Fromareano = in.readString();
        GUID = in.readString();
        Erpvoucherno = in.readString();
        watercode = in.readString();
        QualityQty = in.readFloat();
        UnQualityQty = in.readFloat();
        TaskQty = in.readFloat();
        LockQty = in.readFloat();
        Qualityno = in.readString();
        Arrvoucherno = in.readString();
    }

    public static final Creator<StockInfo> CREATOR = new Creator<StockInfo>() {
        @Override
        public StockInfo createFromParcel(Parcel in) {
            return new StockInfo(in);
        }

        @Override
        public StockInfo[] newArray(int size) {
            return new StockInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Barcode);
        dest.writeString(Serialno);
        dest.writeString(Materialno);
        dest.writeString(Materialdesc);
        dest.writeInt(Towarehouseid);
        dest.writeInt(Houseid);
        dest.writeInt(Areaid);
        dest.writeFloat(Qty);
        dest.writeString(Pickareano);
        dest.writeString(Celareano);
        dest.writeInt(Status);
        dest.writeInt(Isdel);
        dest.writeString(Batchno);
        dest.writeString(Returnsupcode);
        dest.writeString(Returnreson);
        dest.writeString(Returnsupname);
        dest.writeInt(Oldstockid);
        dest.writeInt(Taskdetailesid);
        dest.writeInt(Checkid);
        dest.writeInt(Transferdetailsid);
        dest.writeString(Unit);
        dest.writeString(Unitname);
        dest.writeString(Palletno);
        dest.writeInt(Receivestatus);
        dest.writeInt(Islimitstock);
        dest.writeInt(Materialnoid);
        dest.writeString(Edate);
        dest.writeString(Supplierno);
        dest.writeString(Suppliername);
        dest.writeInt(Isretention);
        dest.writeString(Specialstock);
        dest.writeString(Cusmaterialno);
        dest.writeString(Towarehouseno);
        dest.writeString(Houseno);
        dest.writeString(Areano);
        dest.writeString(Prodate);
        dest.writeInt(Vouchertype);
        dest.writeInt(IsAmount);
        dest.writeString(Rowno);
        dest.writeString(WCustomerno);
        dest.writeFloat(Postqty);
        dest.writeString(Scanuserno);
        dest.writeString(Fromwarehouseno);
        dest.writeString(Toareano);
        dest.writeString(Fromareano);
        dest.writeString(GUID);
        dest.writeString(Erpvoucherno);
        dest.writeString(watercode);
        dest.writeFloat(QualityQty);
        dest.writeFloat(UnQualityQty);
        dest.writeFloat(TaskQty);
        dest.writeFloat(LockQty);
        dest.writeString(Qualityno);
        dest.writeString(Arrvoucherno);
    }
}
