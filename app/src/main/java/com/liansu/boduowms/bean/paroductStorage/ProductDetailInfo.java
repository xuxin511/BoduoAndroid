package com.liansu.boduowms.bean.paroductStorage;

import android.os.Parcel;
import android.os.Parcelable;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;

import java.util.Date;
import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/24.
 */
public class ProductDetailInfo implements Parcelable {
    /**
     * Headerid : 0
     * Rowno : null
     * Rownodel : null
     * Materialno : null
     * Materialdesc : null
     * Outstockqty : 0
     * Unit : null
     * Remainqty : 99
     * Erpvoucherno : null
     * Voucherno : null
     * Towarehouseno : null
     * Fromwarehouseno : null
     * Cusmaterialno : null
     * Linestatus : 0
     * Scanqty : 0
     * Receiveqty : 0
     * Batchno : null
     * Vouchertype : 0
     * Isquality : 0
     * Voucherqty : null
     * Specialstock : null
     * PostStatus : 0
     * Spec : null
     * PackQty : 0
     * QualityQty : 0
     * UnQualityQty : 0
     * MaterialCartonNum : 0
     * MaterialPartNum : 0
     * watercode : null
     * ArrVoucherNo : null
     * Id : 0
     * Companycode : null
     * Strongholdcode : null
     * Strongholdname : null
     * Creater : null
     * Createtime : null
     * Modifyer : null
     * Modifytime : null
     */

    private   int                  Headerid;
    private String Rowno;
    private String Rownodel;
    private String Materialno;
    private String Materialdesc;
    private float    Outstockqty;
    private String Unit;
    private float    Remainqty;
    private String Erpvoucherno;
    private String Voucherno;
    private String Towarehouseno;
    private String Fromwarehouseno;
    private String Cusmaterialno;
    private int    Linestatus;
    private float    Scanqty;
    private float    Receiveqty;
    private String Batchno;
    private int    Vouchertype;
    private int    Isquality;
    private float Voucherqty;
    private String Specialstock;
    private int    PostStatus;
    private String Spec;
    private int    PackQty;
    private int    QualityQty;
    private int    UnQualityQty;
    private int    MaterialCartonNum;
    private int    MaterialPartNum;
    private String watercode;
    private String ArrVoucherNo;
    private int    Id;
    private String Companycode;
    private String Strongholdcode;
    private String Strongholdname;
    private String Creater;
    private Date   Createtime;
    private String Modifyer;
    private   Date                 Modifytime;
    protected List<OutBarcodeInfo> LstBarCode;
    protected String               Printername; //打印机名称
    protected int                  Printertype; //打印机类型  1 激光打印机 2 台式打印机 3.蓝牙

    protected ProductDetailInfo(Parcel in) {
        Headerid = in.readInt();
        Rowno = in.readString();
        Rownodel = in.readString();
        Materialno = in.readString();
        Materialdesc = in.readString();
        Outstockqty = in.readInt();
        Unit = in.readString();
        Remainqty = in.readInt();
        Erpvoucherno = in.readString();
        Voucherno = in.readString();
        Towarehouseno = in.readString();
        Fromwarehouseno = in.readString();
        Cusmaterialno = in.readString();
        Linestatus = in.readInt();
        Scanqty = in.readFloat();
        Receiveqty = in.readFloat();
        Batchno = in.readString();
        Vouchertype = in.readInt();
        Isquality = in.readInt();
        Voucherqty = in.readFloat();
        Specialstock = in.readString();
        PostStatus = in.readInt();
        Spec = in.readString();
        PackQty = in.readInt();
        QualityQty = in.readInt();
        UnQualityQty = in.readInt();
        MaterialCartonNum = in.readInt();
        MaterialPartNum = in.readInt();
        watercode = in.readString();
        ArrVoucherNo = in.readString();
        Id = in.readInt();
        Companycode = in.readString();
        Strongholdcode = in.readString();
        Strongholdname = in.readString();
        Creater = in.readString();
        Modifyer = in.readString();
        LstBarCode = in.createTypedArrayList(OutBarcodeInfo.CREATOR);
        Printername = in.readString();
        Printertype = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Headerid);
        dest.writeString(Rowno);
        dest.writeString(Rownodel);
        dest.writeString(Materialno);
        dest.writeString(Materialdesc);
        dest.writeFloat(Outstockqty);
        dest.writeString(Unit);
        dest.writeFloat(Remainqty);
        dest.writeString(Erpvoucherno);
        dest.writeString(Voucherno);
        dest.writeString(Towarehouseno);
        dest.writeString(Fromwarehouseno);
        dest.writeString(Cusmaterialno);
        dest.writeInt(Linestatus);
        dest.writeFloat(Scanqty);
        dest.writeFloat(Receiveqty);
        dest.writeString(Batchno);
        dest.writeInt(Vouchertype);
        dest.writeInt(Isquality);
        dest.writeFloat(Voucherqty);
        dest.writeString(Specialstock);
        dest.writeInt(PostStatus);
        dest.writeString(Spec);
        dest.writeInt(PackQty);
        dest.writeInt(QualityQty);
        dest.writeInt(UnQualityQty);
        dest.writeInt(MaterialCartonNum);
        dest.writeInt(MaterialPartNum);
        dest.writeString(watercode);
        dest.writeString(ArrVoucherNo);
        dest.writeInt(Id);
        dest.writeString(Companycode);
        dest.writeString(Strongholdcode);
        dest.writeString(Strongholdname);
        dest.writeString(Creater);
        dest.writeString(Modifyer);
        dest.writeTypedList(LstBarCode);
        dest.writeString(Printername);
        dest.writeInt(Printertype);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductDetailInfo> CREATOR = new Creator<ProductDetailInfo>() {
        @Override
        public ProductDetailInfo createFromParcel(Parcel in) {
            return new ProductDetailInfo(in);
        }

        @Override
        public ProductDetailInfo[] newArray(int size) {
            return new ProductDetailInfo[size];
        }
    };

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

    public String getRownodel() {
        return Rownodel;
    }

    public void setRownodel(String rownodel) {
        Rownodel = rownodel;
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

    public float getOutstockqty() {
        return Outstockqty;
    }

    public void setOutstockqty(int outstockqty) {
        Outstockqty = outstockqty;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public float getRemainqty() {
        return Remainqty;
    }

    public void setRemainqty(float remainqty) {
        Remainqty = remainqty;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String erpvoucherno) {
        Erpvoucherno = erpvoucherno;
    }

    public String getVoucherno() {
        return Voucherno;
    }

    public void setVoucherno(String voucherno) {
        Voucherno = voucherno;
    }

    public String getTowarehouseno() {
        return Towarehouseno;
    }

    public void setTowarehouseno(String towarehouseno) {
        Towarehouseno = towarehouseno;
    }

    public String getFromwarehouseno() {
        return Fromwarehouseno;
    }

    public void setFromwarehouseno(String fromwarehouseno) {
        Fromwarehouseno = fromwarehouseno;
    }

    public String getCusmaterialno() {
        return Cusmaterialno;
    }

    public void setCusmaterialno(String cusmaterialno) {
        Cusmaterialno = cusmaterialno;
    }

    public int getLinestatus() {
        return Linestatus;
    }

    public void setLinestatus(int linestatus) {
        Linestatus = linestatus;
    }

    public float getScanqty() {
        return Scanqty;
    }

    public void setScanqty(float scanqty) {
        Scanqty = scanqty;
    }

    public float getReceiveqty() {
        return Receiveqty;
    }

    public void setReceiveqty(float receiveqty) {
        Receiveqty = receiveqty;
    }

    public String getBatchno() {
        return Batchno;
    }

    public void setBatchno(String batchno) {
        Batchno = batchno;
    }

    public int getVouchertype() {
        return Vouchertype;
    }

    public void setVouchertype(int vouchertype) {
        Vouchertype = vouchertype;
    }

    public int getIsquality() {
        return Isquality;
    }

    public void setIsquality(int isquality) {
        Isquality = isquality;
    }

    public float getVoucherqty() {
        return Voucherqty;
    }

    public void setVoucherqty(float voucherqty) {
        Voucherqty = voucherqty;
    }

    public String getSpecialstock() {
        return Specialstock;
    }

    public void setSpecialstock(String specialstock) {
        Specialstock = specialstock;
    }

    public int getPostStatus() {
        return PostStatus;
    }

    public void setPostStatus(int postStatus) {
        PostStatus = postStatus;
    }

    public String getSpec() {
        return Spec;
    }

    public void setSpec(String spec) {
        Spec = spec;
    }

    public int getPackQty() {
        return PackQty;
    }

    public void setPackQty(int packQty) {
        PackQty = packQty;
    }

    public int getQualityQty() {
        return QualityQty;
    }

    public void setQualityQty(int qualityQty) {
        QualityQty = qualityQty;
    }

    public int getUnQualityQty() {
        return UnQualityQty;
    }

    public void setUnQualityQty(int unQualityQty) {
        UnQualityQty = unQualityQty;
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

    public String getWatercode() {
        return watercode;
    }

    public void setWatercode(String watercode) {
        this.watercode = watercode;
    }

    public String getArrVoucherNo() {
        return ArrVoucherNo;
    }

    public void setArrVoucherNo(String arrVoucherNo) {
        ArrVoucherNo = arrVoucherNo;
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

    public String getCreater() {
        return Creater;
    }

    public void setCreater(String creater) {
        Creater = creater;
    }

    public Date getCreatetime() {
        return Createtime;
    }

    public void setCreatetime(Date createtime) {
        Createtime = createtime;
    }

    public String getModifyer() {
        return Modifyer;
    }

    public void setModifyer(String modifyer) {
        Modifyer = modifyer;
    }

    public Date getModifytime() {
        return Modifytime;
    }

    public void setModifytime(Date modifytime) {
        Modifytime = modifytime;
    }

    public List<OutBarcodeInfo> getLstBarCode() {
        return LstBarCode;
    }

    public void setLstBarCode(List<OutBarcodeInfo> lstBarCode) {
        LstBarCode = lstBarCode;
    }

    public static Creator<ProductDetailInfo> getCREATOR() {
        return CREATOR;
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
