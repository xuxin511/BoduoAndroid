package com.liansu.boduowms.bean.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * @ Des: 数据基类
 * @ Created by yangyiqing on 2020/7/1.
 */
public class BaseInfo implements Parcelable {
    protected int    Id ;
    protected String Companycode ;
    protected String Strongholdcode ;
    protected String Strongholdname ;
    protected String Creater ;
//    protected Date   Createtime ;
    protected String Modifyer ;
    protected Date Modifytime ;
   public  BaseInfo(){}
    protected BaseInfo(Parcel in) {
        Id = in.readInt();
        Companycode = in.readString();
        Strongholdcode = in.readString();
        Strongholdname = in.readString();
        Creater = in.readString();
        Modifyer = in.readString();
    }

    public static final Creator<BaseInfo> CREATOR = new Creator<BaseInfo>() {
        @Override
        public BaseInfo createFromParcel(Parcel in) {
            return new BaseInfo(in);
        }

        @Override
        public BaseInfo[] newArray(int size) {
            return new BaseInfo[size];
        }
    };

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

//    public Date getCreatetime() {
//        return Createtime;
//    }
//
//    public void setCreatetime(Date createtime) {
//        Createtime = createtime;
//    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Companycode);
        dest.writeString(Strongholdcode);
        dest.writeString(Strongholdname);
        dest.writeString(Creater);
        dest.writeString(Modifyer);
    }
}
