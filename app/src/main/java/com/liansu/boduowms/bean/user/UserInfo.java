package com.liansu.boduowms.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.liansu.boduowms.bean.warehouse.WareHouseInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/8.
 */
public class UserInfo implements Parcelable {

    /**
     * Userno : admin
     * Password : 119A904D54290623
     * RePassword : null
     * Username : 管理员
     * modelListWarehouse : [{"Id":9,"Warehouseno":"4001","Warehousename":"扬州仓库","StrFifo":null,"WCustomerno":"KH001"},{"Id":40,"Warehouseno":"4002","Warehousename":"扬州仓库2","StrFifo":null,"WCustomerno":"HH002"}]
     */

    private String              Userno;
    private String              Password;
    private String              RePassword;
    private String              Username;
    private List<WareHouseInfo> modelListWarehouse;
     public UserInfo(){}
    protected UserInfo(Parcel in) {
        Userno = in.readString();
        Password = in.readString();
        RePassword = in.readString();
        Username = in.readString();
        modelListWarehouse = in.createTypedArrayList(WareHouseInfo.CREATOR);
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getUserno() {
        return Userno;
    }

    public void setUserno(String userno) {
        Userno = userno;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRePassword() {
        return RePassword;
    }

    public void setRePassword(String rePassword) {
        RePassword = rePassword;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public List<WareHouseInfo> getModelListWarehouse() {
        return modelListWarehouse;
    }

    public void setModelListWarehouse(List<WareHouseInfo> modelListWarehouse) {
        this.modelListWarehouse = modelListWarehouse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Userno);
        dest.writeString(Password);
        dest.writeString(RePassword);
        dest.writeString(Username);
        dest.writeTypedList(modelListWarehouse);
    }
}
