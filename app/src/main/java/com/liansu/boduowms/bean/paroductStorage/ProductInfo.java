package com.liansu.boduowms.bean.paroductStorage;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * @ Des: 产品方法
 * @ Created by yangyiqing on 2020/7/23.
 */
public class ProductInfo  implements Parcelable {
    private String                  Erpvoucherno;
    private int                     Vouchertype;
    private String                  Departmentcode;
    private String                  Departmentname;
    private String                  Towarehouseno;
    private String                  Fromwarehouseno;
    private String           Customerno;
    private String           Customername;
    private String           Supplierno;
    private String           Suppliername;
    private int              Status;
    private String           Erpvouchertype;
    private int              PostStatus;
    private int              OrderCartonNum;
    private int              OrderScanCartonNum;
    private int              Weight;
    private int              Netweight;
    private int              Qualityday;
    private int              Qualitymon;
    private int              Grossweight;
    private int              ImpDay;
    private String           Materialno;
    private String           Materialdesc;
    private String           Unit;
    private String           Unitname;
    private String           Spec;
    private float              Voucherqty;
    private int              Id;
    private String           Companycode;
    private String           Strongholdcode;
    private String           Strongholdname;
    private String Creater;
    private Date   Createtime;
    private String Modifyer;
    private Date                  Modifytime;
    private List<ProductDetailInfo> Detail;

    protected ProductInfo(Parcel in) {
        Erpvoucherno = in.readString();
        Vouchertype = in.readInt();
        Departmentcode = in.readString();
        Departmentname = in.readString();
        Towarehouseno = in.readString();
        Fromwarehouseno = in.readString();
        Customerno = in.readString();
        Customername = in.readString();
        Supplierno = in.readString();
        Suppliername = in.readString();
        Status = in.readInt();
        Erpvouchertype = in.readString();
        PostStatus = in.readInt();
        OrderCartonNum = in.readInt();
        OrderScanCartonNum = in.readInt();
        Weight = in.readInt();
        Netweight = in.readInt();
        Qualityday = in.readInt();
        Qualitymon = in.readInt();
        Grossweight = in.readInt();
        ImpDay = in.readInt();
        Materialno = in.readString();
        Materialdesc = in.readString();
        Unit = in.readString();
        Unitname = in.readString();
        Spec = in.readString();
        Voucherqty = in.readInt();
        Id = in.readInt();
        Companycode = in.readString();
        Strongholdcode = in.readString();
        Strongholdname = in.readString();
        Creater = in.readString();
        Modifyer = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Erpvoucherno);
        dest.writeInt(Vouchertype);
        dest.writeString(Departmentcode);
        dest.writeString(Departmentname);
        dest.writeString(Towarehouseno);
        dest.writeString(Fromwarehouseno);
        dest.writeString(Customerno);
        dest.writeString(Customername);
        dest.writeString(Supplierno);
        dest.writeString(Suppliername);
        dest.writeInt(Status);
        dest.writeString(Erpvouchertype);
        dest.writeInt(PostStatus);
        dest.writeInt(OrderCartonNum);
        dest.writeInt(OrderScanCartonNum);
        dest.writeInt(Weight);
        dest.writeInt(Netweight);
        dest.writeInt(Qualityday);
        dest.writeInt(Qualitymon);
        dest.writeInt(Grossweight);
        dest.writeInt(ImpDay);
        dest.writeString(Materialno);
        dest.writeString(Materialdesc);
        dest.writeString(Unit);
        dest.writeString(Unitname);
        dest.writeString(Spec);
        dest.writeFloat(Voucherqty);
        dest.writeInt(Id);
        dest.writeString(Companycode);
        dest.writeString(Strongholdcode);
        dest.writeString(Strongholdname);
        dest.writeString(Creater);
        dest.writeString(Modifyer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductInfo> CREATOR = new Creator<ProductInfo>() {
        @Override
        public ProductInfo createFromParcel(Parcel in) {
            return new ProductInfo(in);
        }

        @Override
        public ProductInfo[] newArray(int size) {
            return new ProductInfo[size];
        }
    };

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

    public String getDepartmentcode() {
        return Departmentcode;
    }

    public void setDepartmentcode(String departmentcode) {
        Departmentcode = departmentcode;
    }

    public String getDepartmentname() {
        return Departmentname;
    }

    public void setDepartmentname(String departmentname) {
        Departmentname = departmentname;
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

    public String getCustomerno() {
        return Customerno;
    }

    public void setCustomerno(String customerno) {
        Customerno = customerno;
    }

    public String getCustomername() {
        return Customername;
    }

    public void setCustomername(String customername) {
        Customername = customername;
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

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getErpvouchertype() {
        return Erpvouchertype;
    }

    public void setErpvouchertype(String erpvouchertype) {
        Erpvouchertype = erpvouchertype;
    }

    public int getPostStatus() {
        return PostStatus;
    }

    public void setPostStatus(int postStatus) {
        PostStatus = postStatus;
    }

    public int getOrderCartonNum() {
        return OrderCartonNum;
    }

    public void setOrderCartonNum(int orderCartonNum) {
        OrderCartonNum = orderCartonNum;
    }

    public int getOrderScanCartonNum() {
        return OrderScanCartonNum;
    }

    public void setOrderScanCartonNum(int orderScanCartonNum) {
        OrderScanCartonNum = orderScanCartonNum;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public int getNetweight() {
        return Netweight;
    }

    public void setNetweight(int netweight) {
        Netweight = netweight;
    }

    public int getQualityday() {
        return Qualityday;
    }

    public void setQualityday(int qualityday) {
        Qualityday = qualityday;
    }

    public int getQualitymon() {
        return Qualitymon;
    }

    public void setQualitymon(int qualitymon) {
        Qualitymon = qualitymon;
    }

    public int getGrossweight() {
        return Grossweight;
    }

    public void setGrossweight(int grossweight) {
        Grossweight = grossweight;
    }

    public int getImpDay() {
        return ImpDay;
    }

    public void setImpDay(int impDay) {
        ImpDay = impDay;
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

    public String getSpec() {
        return Spec;
    }

    public void setSpec(String spec) {
        Spec = spec;
    }

    public float getVoucherqty() {
        return Voucherqty;
    }

    public void setVoucherqty(int voucherqty) {
        Voucherqty = voucherqty;
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

    public List<ProductDetailInfo> getDetail() {
        return Detail;
    }

    public void setDetail(List<ProductDetailInfo> detail) {
        Detail = detail;
    }

    public static Creator<ProductInfo> getCREATOR() {
        return CREATOR;
    }
}

