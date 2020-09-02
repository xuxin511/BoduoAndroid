package com.liansu.boduowms.bean.update;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ Des: apk更新类
 * @ Created by yangyiqing on 2020/9/2.
 */
public class ApkInfo implements Parcelable {

    /**
     * ApkSize : 0
     * Code : 0
     * DownloadUrl : http://172.19.106.230:5001/AppData/BODUO.apk
     * ModifyContent : 1、修复部分已知问题。
     * Msg :
     * UpdateStatus : 2
     * UploadTime : 2020-06-14 08:00:00
     * VersionCode : 1.00652
     * VersionName : 1.2
     */

    private int    ApkSize;
    private int    Code;
    private String DownloadUrl;
    private String ModifyContent;
    private String Msg;
    private int    UpdateStatus;
    private String UploadTime;
    private double VersionCode;
    private String VersionName;
    private String FileName;
    protected ApkInfo(Parcel in) {
        ApkSize = in.readInt();
        Code = in.readInt();
        DownloadUrl = in.readString();
        ModifyContent = in.readString();
        Msg = in.readString();
        UpdateStatus = in.readInt();
        UploadTime = in.readString();
        VersionCode = in.readDouble();
        VersionName = in.readString();
        FileName=in.readString();
    }

    public static final Creator<ApkInfo> CREATOR = new Creator<ApkInfo>() {
        @Override
        public ApkInfo createFromParcel(Parcel in) {
            return new ApkInfo(in);
        }

        @Override
        public ApkInfo[] newArray(int size) {
            return new ApkInfo[size];
        }
    };

    public int getApkSize() {
        return ApkSize;
    }

    public void setApkSize(int ApkSize) {
        this.ApkSize = ApkSize;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getDownloadUrl() {
        return DownloadUrl;
    }

    public void setDownloadUrl(String DownloadUrl) {
        this.DownloadUrl = DownloadUrl;
    }

    public String getModifyContent() {
        return ModifyContent;
    }

    public void setModifyContent(String ModifyContent) {
        this.ModifyContent = ModifyContent;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public int getUpdateStatus() {
        return UpdateStatus;
    }

    public void setUpdateStatus(int UpdateStatus) {
        this.UpdateStatus = UpdateStatus;
    }

    public String getUploadTime() {
        return UploadTime;
    }

    public void setUploadTime(String UploadTime) {
        this.UploadTime = UploadTime;
    }

    public double getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(double VersionCode) {
        this.VersionCode = VersionCode;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String VersionName) {
        this.VersionName = VersionName;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ApkSize);
        dest.writeInt(Code);
        dest.writeString(DownloadUrl);
        dest.writeString(ModifyContent);
        dest.writeString(Msg);
        dest.writeInt(UpdateStatus);
        dest.writeString(UploadTime);
        dest.writeDouble(VersionCode);
        dest.writeString(VersionName);
        dest.writeString(FileName);
    }
}
