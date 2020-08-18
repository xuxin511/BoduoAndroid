package com.liansu.boduowms.bean.order;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ Des: 单据类型类
 * @ Created by yangyiqing on 2020/8/17.
 */
public class VoucherTypeInfo implements Parcelable {


    /**
     * Id : 1071
     * Parameterid : 22
     * Parametername : 采购订单
     * Groupname : VoucherRecPrt_Name
     */

    private int    Id;
    private int    Parameterid;
    private String Parametername;
    private String Groupname;

    public VoucherTypeInfo() {
    }

    protected VoucherTypeInfo(Parcel in) {
        Id = in.readInt();
        Parameterid = in.readInt();
        Parametername = in.readString();
        Groupname = in.readString();
    }

    public static final Creator<VoucherTypeInfo> CREATOR = new Creator<VoucherTypeInfo>() {
        @Override
        public VoucherTypeInfo createFromParcel(Parcel in) {
            return new VoucherTypeInfo(in);
        }

        @Override
        public VoucherTypeInfo[] newArray(int size) {
            return new VoucherTypeInfo[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getParameterid() {
        return Parameterid;
    }

    public void setParameterid(int Parameterid) {
        this.Parameterid = Parameterid;
    }

    public String getParametername() {
        return Parametername;
    }

    public void setParametername(String Parametername) {
        this.Parametername = Parametername;
    }

    public String getGroupname() {
        return Groupname;
    }

    public void setGroupname(String Groupname) {
        this.Groupname = Groupname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeInt(Parameterid);
        dest.writeString(Parametername);
        dest.writeString(Groupname);
    }
}
