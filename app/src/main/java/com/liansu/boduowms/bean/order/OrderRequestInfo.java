package com.liansu.boduowms.bean.order;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ Des:  请求基类
 * @ Created by yangyiqing on 2020/7/30.
 */
public class OrderRequestInfo implements Parcelable {

    private int    Vouchertype;
    private String Towarehouseno;
    private String Materialno;
    private String Supplierno;
    //    private String Suppliername;
    private String Erpvoucherno;
    private String DateFrom;   //开始时间
    private String DateTo;   //结束时间
    private String Customerno; //客户编码
     private String Scanuserno; //
    private String Dirver;  //司机
    private String LogisticsCompany; //物流公司
    protected String                Strongholdcode;
    protected String                StrongholdName;
    public OrderRequestInfo() {

    }

    protected OrderRequestInfo(Parcel in) {

        Vouchertype = in.readInt();
        Towarehouseno = in.readString();
        Materialno = in.readString();
        Supplierno = in.readString();
//        Suppliername = in.readString();
        Erpvoucherno = in.readString();
        DateFrom = in.readString();
        DateTo = in.readString();
        Customerno=in.readString();
        Scanuserno=in.readString();
        Dirver=in.readString();
        LogisticsCompany=in.readString();
        Strongholdcode=in.readString();
        StrongholdName=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(Vouchertype);
        dest.writeString(Towarehouseno);
        dest.writeString(Materialno);
        dest.writeString(Supplierno);
//        dest.writeString(Suppliername);
        dest.writeString(Erpvoucherno);
        dest.writeString(DateFrom);
        dest.writeString(DateTo);
        dest.writeString(Customerno);
        dest.writeString(Scanuserno);
        dest.writeString(Dirver);
        dest.writeString(LogisticsCompany);
        dest.writeString(Strongholdcode);
        dest.writeString(StrongholdName);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderRequestInfo> CREATOR = new Creator<OrderRequestInfo>() {
        @Override
        public OrderRequestInfo createFromParcel(Parcel in) {
            return new OrderRequestInfo(in);
        }

        @Override
        public OrderRequestInfo[] newArray(int size) {
            return new OrderRequestInfo[size];
        }
    };


    public int getVouchertype() {
        return Vouchertype;
    }

    public void setVouchertype(int vouchertype) {
        Vouchertype = vouchertype;
    }


    public String getTowarehouseno() {
        return Towarehouseno;
    }

    public void setTowarehouseno(String towarehouseno) {
        Towarehouseno = towarehouseno;
    }

    public String getMaterialno() {
        return Materialno;
    }

    public void setMaterialno(String materialno) {
        Materialno = materialno;
    }

    public String getSupplierno() {
        return Supplierno;
    }

    public void setSupplierno(String supplierno) {
        Supplierno = supplierno;
    }

//    public String getSuppliername() {
//        return Suppliername;
//    }
//
//    public void setSuppliername(String suppliername) {
//        Suppliername = suppliername;
//    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String erpvoucherno) {
        Erpvoucherno = erpvoucherno;
    }

    public String getDateFrom() {
        return DateFrom;
    }

    public void setDateFrom(String dateFrom) {
        DateFrom = dateFrom;
    }

    public String getDateTo() {
        return DateTo;
    }

    public void setDateTo(String dateTo) {
        DateTo = dateTo;
    }

    public String getCustomerno() {
        return Customerno;
    }

    public void setCustomerno(String customerno) {
        Customerno = customerno;
    }

    public String getScanuserno() {
        return Scanuserno;
    }

    public void setScanuserno(String scanuserno) {
        Scanuserno = scanuserno;
    }

    public String getDirver() {
        return Dirver;
    }

    public void setDirver(String dirver) {
        Dirver = dirver;
    }

    public String getLogisticsCompany() {
        return LogisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        LogisticsCompany = logisticsCompany;
    }

    public String getStrongholdcode() {
        return Strongholdcode;
    }

    public void setStrongholdcode(String strongholdcode) {
        Strongholdcode = strongholdcode;
    }

    public String getStrongholdName() {
        return StrongholdName;
    }

    public void setStrongholdName(String strongholdName) {
        StrongholdName = strongholdName;
    }

    public static Creator<OrderRequestInfo> getCREATOR() {
        return CREATOR;
    }
}
