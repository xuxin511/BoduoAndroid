package com.liansu.boduowms.bean.menu;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/9.
 */
public class MenuChildrenInfo implements Parcelable {
    /**
     * Id : 14
     * path : 22
     * component : 1
     * name : null
     * icon : null
     * title : 到货入库
     * meta : {"Id":14,"keepalive":false,"internalOrExternal":false,"title":"到货入库","icon":null}
     */

    private int                    Id;
    private String                 path;
    private String                 component;
    private Object                 name;
    private Object                 icon;
    private String                 title;
    private MenuMetaInfo           meta;
    private List<MenuChildrenInfo> children;

    public  MenuChildrenInfo(){}
    protected MenuChildrenInfo(Parcel in) {
        Id = in.readInt();
        path = in.readString();
        component = in.readString();
        title = in.readString();
        meta = in.readParcelable(MenuMetaInfo.class.getClassLoader());
        children = in.createTypedArrayList(MenuChildrenInfo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(path);
        dest.writeString(component);
        dest.writeString(title);
        dest.writeParcelable(meta, flags);
        dest.writeTypedList(children);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MenuChildrenInfo> CREATOR = new Creator<MenuChildrenInfo>() {
        @Override
        public MenuChildrenInfo createFromParcel(Parcel in) {
            return new MenuChildrenInfo(in);
        }

        @Override
        public MenuChildrenInfo[] newArray(int size) {
            return new MenuChildrenInfo[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getIcon() {
        return icon;
    }

    public void setIcon(Object icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MenuMetaInfo getMeta() {
        return meta;
    }

    public void setMeta(MenuMetaInfo meta) {
        this.meta = meta;
    }



    public List<MenuChildrenInfo> getChildren() {
        return children;
    }

    public void setChildren(List<MenuChildrenInfo> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "MenuChildrenInfo{" +
                "Id=" + Id +
                ", path='" + path + '\'' +
                ", component='" + component + '\'' +
                ", name=" + name +
                ", icon=" + icon +
                ", title='" + title + '\'' +
                ", meta=" + meta +
                ", children=" + children +
                '}';
    }
}
