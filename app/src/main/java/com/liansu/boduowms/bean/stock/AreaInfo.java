package com.liansu.boduowms.bean.stock;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 库位信息
 */

public class AreaInfo implements Parcelable {


    /**
     * Id : 457
     * Areano : 81101
     * Areatype : 1
     * Areastatus : 2
     * Houseid : 0
     * Warehouseid : 4
     * Isdefault : 1
     * Strongholdcode : YL
     * Creater : 管理员
     * Createtime : 2020-07-14T00:00:00
     */

    private int    Id;
    private String Areano;
    private int    Areatype;
    private int    Areastatus;
    private int    Houseid;
    private int    Warehouseid;
    private int    Isdefault;
    private String Strongholdcode;
    private String Creater;
    private String Createtime;
    private String Companycode;
    public AreaInfo (){};
    protected AreaInfo(Parcel in) {
        Id = in.readInt();
        Areano = in.readString();
        Areatype = in.readInt();
        Areastatus = in.readInt();
        Houseid = in.readInt();
        Warehouseid = in.readInt();
        Isdefault = in.readInt();
        Strongholdcode = in.readString();
        Creater = in.readString();
        Createtime = in.readString();
        Companycode=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Areano);
        dest.writeInt(Areatype);
        dest.writeInt(Areastatus);
        dest.writeInt(Houseid);
        dest.writeInt(Warehouseid);
        dest.writeInt(Isdefault);
        dest.writeString(Strongholdcode);
        dest.writeString(Creater);
        dest.writeString(Createtime);
        dest.writeString(Companycode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AreaInfo> CREATOR = new Creator<AreaInfo>() {
        @Override
        public AreaInfo createFromParcel(Parcel in) {
            return new AreaInfo(in);
        }

        @Override
        public AreaInfo[] newArray(int size) {
            return new AreaInfo[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getAreano() {
        return Areano;
    }

    public void setAreano(String Areano) {
        this.Areano = Areano;
    }

    public int getAreatype() {
        return Areatype;
    }

    public void setAreatype(int Areatype) {
        this.Areatype = Areatype;
    }

    public int getAreastatus() {
        return Areastatus;
    }

    public void setAreastatus(int Areastatus) {
        this.Areastatus = Areastatus;
    }

    public int getHouseid() {
        return Houseid;
    }

    public void setHouseid(int Houseid) {
        this.Houseid = Houseid;
    }

    public int getWarehouseid() {
        return Warehouseid;
    }

    public void setWarehouseid(int Warehouseid) {
        this.Warehouseid = Warehouseid;
    }

    public int getIsdefault() {
        return Isdefault;
    }

    public void setIsdefault(int Isdefault) {
        this.Isdefault = Isdefault;
    }

    public String getStrongholdcode() {
        return Strongholdcode;
    }

    public void setStrongholdcode(String Strongholdcode) {
        this.Strongholdcode = Strongholdcode;
    }

    public String getCreater() {
        return Creater;
    }

    public void setCreater(String Creater) {
        this.Creater = Creater;
    }

    public String getCreatetime() {
        return Createtime;
    }

    public void setCreatetime(String Createtime) {
        this.Createtime = Createtime;
    }

    public String getCompanycode() {
        return Companycode;
    }

    public void setCompanycode(String companycode) {
        Companycode = companycode;
    }
}
