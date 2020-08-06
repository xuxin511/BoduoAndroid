package com.liansu.boduowms.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.liansu.boduowms.bean.warehouse.WareHouseInfo;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/8.
 */
public class LoginUserInfo implements Parcelable {
    public String              Userno ;
    public String              Password ;
    public String              RePassword ;
    public String              Username ;
    public List<WareHouseInfo> modelListWarehouse ;

     public LoginUserInfo(){}
    protected LoginUserInfo(Parcel in) {
        Userno = in.readString();
        Password = in.readString();
        RePassword = in.readString();
        Username = in.readString();
        modelListWarehouse = in.createTypedArrayList(WareHouseInfo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Userno);
        dest.writeString(Password);
        dest.writeString(RePassword);
        dest.writeString(Username);
        dest.writeTypedList(modelListWarehouse);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoginUserInfo> CREATOR = new Creator<LoginUserInfo>() {
        @Override
        public LoginUserInfo createFromParcel(Parcel in) {
            return new LoginUserInfo(in);
        }

        @Override
        public LoginUserInfo[] newArray(int size) {
            return new LoginUserInfo[size];
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
}
