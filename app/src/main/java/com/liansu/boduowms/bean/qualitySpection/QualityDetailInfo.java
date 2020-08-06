package com.liansu.boduowms.bean.qualitySpection;

import android.os.Parcel;
import android.os.Parcelable;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.order.OrderDetailInfo;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * @ Des:质检明细类
 * @ Created by yangyiqing on 2020/7/9.
 */
public class QualityDetailInfo extends OrderDetailInfo implements Parcelable {

    public QualityDetailInfo() {
        super();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

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
        LstBarCode = in.createTypedArrayList(OutBarcodeInfo.CREATOR);
        Outstockqty = in.readFloat();
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }

    @Override
    public int getHeaderid() {
        return super.getHeaderid();
    }

    @Override
    public void setHeaderid(int headerid) {
        super.setHeaderid(headerid);
    }

    @Override
    public String getRowno() {
        return super.getRowno();
    }

    @Override
    public void setRowno(String rowno) {
        super.setRowno(rowno);
    }

    @Override
    public String getMaterialno() {
        return super.getMaterialno();
    }

    @Override
    public void setMaterialno(String materialno) {
        super.setMaterialno(materialno);
    }

    @Override
    public String getMaterialdesc() {
        return super.getMaterialdesc();
    }

    @Override
    public void setMaterialdesc(String materialdesc) {
        super.setMaterialdesc(materialdesc);
    }

    @Override
    public float getReceiveqty() {
        return super.getReceiveqty();
    }

    @Override
    public void setReceiveqty(float receiveqty) {
        super.setReceiveqty(receiveqty);
    }

    @Override
    public String getUnit() {
        return super.getUnit();
    }

    @Override
    public void setUnit(String unit) {
        super.setUnit(unit);
    }

    @Override
    public String getStorageloc() {
        return super.getStorageloc();
    }

    @Override
    public void setStorageloc(String storageloc) {
        super.setStorageloc(storageloc);
    }

    @Override
    public String getPlant() {
        return super.getPlant();
    }

    @Override
    public void setPlant(String plant) {
        super.setPlant(plant);
    }

    @Override
    public String getPlantname() {
        return super.getPlantname();
    }

    @Override
    public void setPlantname(String plantname) {
        super.setPlantname(plantname);
    }

    @Override
    public String getUnitname() {
        return super.getUnitname();
    }

    @Override
    public void setUnitname(String unitname) {
        super.setUnitname(unitname);
    }

    @Override
    public int getLinestatus() {
        return super.getLinestatus();
    }

    @Override
    public void setLinestatus(int linestatus) {
        super.setLinestatus(linestatus);
    }

    @Override
    public float getRemainqty() {
        return super.getRemainqty();
    }

    @Override
    public void setRemainqty(float remainqty) {
        super.setRemainqty(remainqty);
    }

    @Override
    public String getVoucherno() {
        return super.getVoucherno();
    }

    @Override
    public void setVoucherno(String voucherno) {
        super.setVoucherno(voucherno);
    }

    @Override
    public String getErpvoucherno() {
        return super.getErpvoucherno();
    }

    @Override
    public void setErpvoucherno(String erpvoucherno) {
        super.setErpvoucherno(erpvoucherno);
    }

    @Override
    public int getVouchertype() {
        return super.getVouchertype();
    }

    @Override
    public void setVouchertype(int vouchertype) {
        super.setVouchertype(vouchertype);
    }

    @Override
    public int getMaterialnoid() {
        return super.getMaterialnoid();
    }

    @Override
    public void setMaterialnoid(int materialnoid) {
        super.setMaterialnoid(materialnoid);
    }

    @Override
    public String getRownodel() {
        return super.getRownodel();
    }

    @Override
    public void setRownodel(String rownodel) {
        super.setRownodel(rownodel);
    }

    @Override
    public String getPurchaseqty() {
        return super.getPurchaseqty();
    }

    @Override
    public void setPurchaseqty(String purchaseqty) {
        super.setPurchaseqty(purchaseqty);
    }

    @Override
    public int getPostqty() {
        return super.getPostqty();
    }

    @Override
    public void setPostqty(int postqty) {
        super.setPostqty(postqty);
    }

    @Override
    public String getPostuser() {
        return super.getPostuser();
    }

    @Override
    public void setPostuser(String postuser) {
        super.setPostuser(postuser);
    }

    @Override
    public Object getPostdate() {
        return super.getPostdate();
    }

    @Override
    public void setPostdate(Object postdate) {
        super.setPostdate(postdate);
    }

    @Override
    public String getSpecialstock() {
        return super.getSpecialstock();
    }

    @Override
    public void setSpecialstock(String specialstock) {
        super.setSpecialstock(specialstock);
    }

    @Override
    public float getScanqty() {
        return super.getScanqty();
    }

    @Override
    public void setScanqty(float scanqty) {
        super.setScanqty(scanqty);
    }

    @Override
    public float getVoucherqty() {
        return super.getVoucherqty();
    }

    @Override
    public void setVoucherqty(float voucherqty) {
        super.setVoucherqty(voucherqty);
    }

    @Override
    public String getBatchno() {
        return super.getBatchno();
    }

    @Override
    public void setBatchno(String batchno) {
        super.setBatchno(batchno);
    }

    @Override
    public String getCusmaterialno() {
        return super.getCusmaterialno();
    }

    @Override
    public void setCusmaterialno(String cusmaterialno) {
        super.setCusmaterialno(cusmaterialno);
    }

    @Override
    public String getStrongholdcode() {
        return super.getStrongholdcode();
    }

    @Override
    public void setStrongholdcode(String strongholdcode) {
        super.setStrongholdcode(strongholdcode);
    }

    @Override
    public String getStrongholdname() {
        return super.getStrongholdname();
    }

    @Override
    public void setStrongholdname(String strongholdname) {
        super.setStrongholdname(strongholdname);
    }

    @Override
    public String getScanuserno() {
        return super.getScanuserno();
    }

    @Override
    public void setScanuserno(String scanuserno) {
        super.setScanuserno(scanuserno);
    }

    @Override
    public String getSupplierno() {
        return super.getSupplierno();
    }

    @Override
    public void setSupplierno(String supplierno) {
        super.setSupplierno(supplierno);
    }

    @Override
    public String getSuppliername() {
        return super.getSuppliername();
    }

    @Override
    public void setSuppliername(String suppliername) {
        super.setSuppliername(suppliername);
    }

    @Override
    public int getPackQty() {
        return super.getPackQty();
    }

    @Override
    public void setPackQty(int packQty) {
        super.setPackQty(packQty);
    }

    @Override
    public List<OutBarcodeInfo> getLstBarCode() {
        return super.getLstBarCode();
    }

    @Override
    public void setLstBarCode(List<OutBarcodeInfo> lstBarCode) {
        super.setLstBarCode(lstBarCode);
    }

    @Override
    public float getOutstockqty() {
        return super.getOutstockqty();
    }

    @Override
    public void setOutstockqty(float outstockqty) {
        super.setOutstockqty(outstockqty);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public int describeContents() {
        return super.describeContents();
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
}
