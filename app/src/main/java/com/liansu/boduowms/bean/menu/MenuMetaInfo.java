package com.liansu.boduowms.bean.menu;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/9.
 */
public class MenuMetaInfo implements Parcelable {
    /**
     * Id : 13
     * keepalive : false
     * internalOrExternal : false
     * title : PDA入库
     * icon : null
     */

    private int     Id;
    private boolean keepalive;
    private boolean internalOrExternal;
    private String  title;
    private Object  icon;
    public  MenuMetaInfo(){}

    protected MenuMetaInfo(Parcel in) {
        Id = in.readInt();
        keepalive = in.readByte() != 0;
        internalOrExternal = in.readByte() != 0;
        title = in.readString();
    }

    public static final Creator<MenuMetaInfo> CREATOR = new Creator<MenuMetaInfo>() {
        @Override
        public MenuMetaInfo createFromParcel(Parcel in) {
            return new MenuMetaInfo(in);
        }

        @Override
        public MenuMetaInfo[] newArray(int size) {
            return new MenuMetaInfo[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public boolean isKeepalive() {
        return keepalive;
    }

    public void setKeepalive(boolean keepalive) {
        this.keepalive = keepalive;
    }

    public boolean isInternalOrExternal() {
        return internalOrExternal;
    }

    public void setInternalOrExternal(boolean internalOrExternal) {
        this.internalOrExternal = internalOrExternal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getIcon() {
        return icon;
    }

    public void setIcon(Object icon) {
        this.icon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeByte((byte) (keepalive ? 1 : 0));
        dest.writeByte((byte) (internalOrExternal ? 1 : 0));
        dest.writeString(title);
    }
}
