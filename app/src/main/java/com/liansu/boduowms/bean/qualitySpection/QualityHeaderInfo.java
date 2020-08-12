package com.liansu.boduowms.bean.qualitySpection;

import android.os.Parcel;
import android.os.Parcelable;

import com.liansu.boduowms.bean.stock.StockInfo;

import java.util.Date;

/**
 * @ Des:质检明细类
 * @ Created by yangyiqing on 2020/7/9.
 */
public class QualityHeaderInfo extends QualityDetailInfo implements Parcelable {

    /// 合格数量
    public float  Qualityqty;
    public float  Unqualityqty;
    /// 单据数量
    public String Erpstatuscode;
    public Date   erpqualitydatetime;
    public String Qualityno;
    // public string Arrvoucherno ;
    public String Orowno;
    public String Orownodel;
    public String Ylinestatus;
    public String  Areano;
   private  float  Sampqty;
   private  String Erpvoucherdesc;
   private String Erpstatuscodedesc;


    public QualityHeaderInfo() {
    }

    protected QualityHeaderInfo(Parcel in) {
        Qualityqty = in.readFloat();
        Unqualityqty = in.readFloat();
        Erpstatuscode = in.readString();
        Qualityno = in.readString();
        Orowno = in.readString();
        Orownodel = in.readString();
        Ylinestatus = in.readString();
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
        Areano=in.readString();
        Towarehouseno=in.readString();
        Companycode=in.readString();
        Arrvoucherno =in.readString();
        Sampqty =in.readFloat();
        Erpvoucherdesc=in.readString();
        Erpstatuscodedesc=in.readString();
    }

    public static final Creator<QualityHeaderInfo> CREATOR = new Creator<QualityHeaderInfo>() {
        @Override
        public QualityHeaderInfo createFromParcel(Parcel in) {
            return new QualityHeaderInfo(in);
        }

        @Override
        public QualityHeaderInfo[] newArray(int size) {
            return new QualityHeaderInfo[size];
        }
    };

    public float getQualityqty() {
        return Qualityqty;
    }

    public void setQualityqty(float qualityqty) {
        Qualityqty = qualityqty;
    }

    public float getUnqualityqty() {
        return Unqualityqty;
    }

    public void setUnqualityqty(float unqualityqty) {
        Unqualityqty = unqualityqty;
    }

    public String getErpstatuscode() {
        return Erpstatuscode;
    }

    public void setErpstatuscode(String erpstatuscode) {
        Erpstatuscode = erpstatuscode;
    }

    public Date getErpqualitydatetime() {
        return erpqualitydatetime;
    }

    public void setErpqualitydatetime(Date erpqualitydatetime) {
        this.erpqualitydatetime = erpqualitydatetime;
    }

    public String getQualityno() {
        return Qualityno;
    }

    public void setQualityno(String qualityno) {
        Qualityno = qualityno;
    }

    public String getOrowno() {
        return Orowno;
    }

    public void setOrowno(String orowno) {
        Orowno = orowno;
    }

    public String getOrownodel() {
        return Orownodel;
    }

    public void setOrownodel(String orownodel) {
        Orownodel = orownodel;
    }

    public String getYlinestatus() {
        return Ylinestatus;
    }

    public void setYlinestatus(String ylinestatus) {
        Ylinestatus = ylinestatus;
    }

    public String getAreano() {
        return Areano;
    }

    public void setAreano(String areano) {
        Areano = areano;
    }

    public String getTowarehouseno() {
        return Towarehouseno;
    }

    public void setTowarehouseno(String towarehouseno) {
        Towarehouseno = towarehouseno;
    }

    public float getSampqty() {
        return Sampqty;
    }

    public void setSampqty(float sampqty) {
        this.Sampqty = sampqty;
    }

    public String getErpvoucherdesc() {
        return Erpvoucherdesc;
    }

    public void setErpvoucherdesc(String erpvoucherdesc) {
        Erpvoucherdesc = erpvoucherdesc;
    }

    public String getErpstatuscodedesc() {
        return Erpstatuscodedesc;
    }

    public void setErpstatuscodedesc(String erpstatuscodedesc) {
        Erpstatuscodedesc = erpstatuscodedesc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(Qualityqty);
        dest.writeFloat(Unqualityqty);
        dest.writeString(Erpstatuscode);
        dest.writeString(Qualityno);
        dest.writeString(Orowno);
        dest.writeString(Orownodel);
        dest.writeString(Ylinestatus);
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
        dest.writeString(Areano);
        dest.writeString(Towarehouseno);
        dest.writeString(Companycode);
        dest.writeString(Arrvoucherno);
        dest.writeFloat(Sampqty);
        dest.writeString(Erpvoucherdesc);
        dest.writeString(Erpstatuscodedesc);

    }
}
