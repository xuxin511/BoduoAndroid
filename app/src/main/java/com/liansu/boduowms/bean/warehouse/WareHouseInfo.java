package com.liansu.boduowms.bean.warehouse;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/8.
 */
public class WareHouseInfo implements Parcelable {
    public int    Id;
    public String Warehouseno;
    public String Warehousename;
    public String StrFifo;
    public String WCustomerno;
    public String Areano;

    public WareHouseInfo() {
    }

    protected WareHouseInfo(Parcel in) {
        Id = in.readInt();
        Warehouseno = in.readString();
        Warehousename = in.readString();
        StrFifo = in.readString();
        WCustomerno = in.readString();
        Areano = in.readString();
    }

    public static final Creator<WareHouseInfo> CREATOR = new Creator<WareHouseInfo>() {
        @Override
        public WareHouseInfo createFromParcel(Parcel in) {
            return new WareHouseInfo(in);
        }

        @Override
        public WareHouseInfo[] newArray(int size) {
            return new WareHouseInfo[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getWarehouseno() {
        return Warehouseno;
    }

    public void setWarehouseno(String warehouseno) {
        Warehouseno = warehouseno;
    }

    public String getWarehousename() {
        return Warehousename;
    }

    public void setWarehousename(String warehousename) {
        Warehousename = warehousename;
    }

    public String getStrFifo() {
        return StrFifo;
    }

    public void setStrFifo(String strFifo) {
        StrFifo = strFifo;
    }

    public String getWCustomerno() {
        return WCustomerno;
    }

    public void setWCustomerno(String WCustomerno) {
        this.WCustomerno = WCustomerno;
    }

    public String getAreano() {
        return Areano;
    }

    public void setAreano(String areano) {
        Areano = areano;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Warehouseno);
        dest.writeString(Warehousename);
        dest.writeString(StrFifo);
        dest.writeString(WCustomerno);
        dest.writeString(Areano);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WareHouseInfo)) return false;
        WareHouseInfo that = (WareHouseInfo) o;
        return Warehouseno.equals(that.Warehouseno) &&
                Warehousename.equals(that.Warehousename);
    }


//    @Override
//    public int hashCode() {
//        return Objects.hash(Warehousename);
//    }
}
