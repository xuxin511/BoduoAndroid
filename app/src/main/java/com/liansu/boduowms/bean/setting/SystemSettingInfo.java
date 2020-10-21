package com.liansu.boduowms.bean.setting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/10/20.
 */
public class SystemSettingInfo implements Parcelable {

    private String url; //完整的ip地址
    private String IPAddress; //ip
    private int port; //端口号
    private String lastContent; //末尾文件
    private int urlType;//url 类型   1.正式区 2.测试区



    protected SystemSettingInfo(Parcel in) {
        url = in.readString();
        IPAddress = in.readString();
        port = in.readInt();
        lastContent = in.readString();
        urlType=in.readInt();
    }

    public SystemSettingInfo(){}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getLastContent() {
        return lastContent;
    }

    public void setLastContent(String lastContent) {
        this.lastContent = lastContent;
    }

    public int getUrlType() {
        return urlType;
    }

    public void setUrlType(int urlType) {
        this.urlType = urlType;
    }

    public static final Creator<SystemSettingInfo> CREATOR = new Creator<SystemSettingInfo>() {
        @Override
        public SystemSettingInfo createFromParcel(Parcel in) {
            return new SystemSettingInfo(in);
        }

        @Override
        public SystemSettingInfo[] newArray(int size) {
            return new SystemSettingInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(IPAddress);
        dest.writeInt(port);
        dest.writeString(lastContent);
        dest.writeInt(urlType);
    }
}
