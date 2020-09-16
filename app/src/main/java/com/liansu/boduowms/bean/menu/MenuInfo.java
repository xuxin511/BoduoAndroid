package com.liansu.boduowms.bean.menu;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/9.
 */
public class MenuInfo implements Parcelable {
    /**
     * Id : 13
     * Menuno : 1013
     * title : PDA入库
     * Menutype : 1
     * path : 1
     * component : 1
     * icon : null
     * Nodelevel : 1
     * Nodesort : 1
     * Parentid : 13
     * name : null
     * children : [{"Id":14,"path":"22","component":"1","name":null,"icon":null,"title":"到货入库","meta":{"Id":14,"keepalive":false,"internalOrExternal":false,"title":"到货入库","icon":null}},{"Id":15,"path":"26","component":"1","name":null,"icon":null,"title":"寄售退回","meta":{"Id":15,"keepalive":false,"internalOrExternal":false,"title":"寄售退回","icon":null}},{"Id":16,"path":"24","component":"1","name":null,"icon":null,"title":"调拨入库","meta":{"Id":16,"keepalive":false,"internalOrExternal":false,"title":"调拨入库","icon":null}}]
     * meta : {"Id":13,"keepalive":false,"internalOrExternal":false,"title":"PDA入库","icon":null}
     */

    private int                    Id;
    private String                 Menuno;
    private String                 title;
    private int                    Menutype;
    private String                 path;
    private String                 component;
    private Object                 icon;
    private String                 Nodelevel;
    private String                 Nodesort;
    private int                    Parentid;
    private Object                 name;
    private MenuMetaInfo           meta;
    private List<MenuChildrenInfo> children;


    protected MenuInfo(Parcel in) {
        Id = in.readInt();
        Menuno = in.readString();
        title = in.readString();
        Menutype = in.readInt();
        path = in.readString();
        component = in.readString();
        Nodelevel = in.readString();
        Nodesort = in.readString();
        Parentid = in.readInt();
        meta = in.readParcelable(MenuMetaInfo.class.getClassLoader());
        children = in.createTypedArrayList(MenuChildrenInfo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Menuno);
        dest.writeString(title);
        dest.writeInt(Menutype);
        dest.writeString(path);
        dest.writeString(component);
        dest.writeString(Nodelevel);
        dest.writeString(Nodesort);
        dest.writeInt(Parentid);
        dest.writeParcelable(meta, flags);
        dest.writeTypedList(children);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MenuInfo> CREATOR = new Creator<MenuInfo>() {
        @Override
        public MenuInfo createFromParcel(Parcel in) {
            return new MenuInfo(in);
        }

        @Override
        public MenuInfo[] newArray(int size) {
            return new MenuInfo[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getMenuno() {
        return Menuno;
    }

    public void setMenuno(String Menuno) {
        this.Menuno = Menuno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMenutype() {
        return Menutype;
    }

    public void setMenutype(int Menutype) {
        this.Menutype = Menutype;
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

    public Object getIcon() {
        return icon;
    }

    public void setIcon(Object icon) {
        this.icon = icon;
    }

    public String getNodelevel() {
        return Nodelevel;
    }

    public void setNodelevel(String Nodelevel) {
        this.Nodelevel = Nodelevel;
    }

    public String getNodesort() {
        return Nodesort;
    }

    public void setNodesort(String Nodesort) {
        this.Nodesort = Nodesort;
    }

    public int getParentid() {
        return Parentid;
    }

    public void setParentid(int Parentid) {
        this.Parentid = Parentid;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
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
        return "MenuInfo{" +
                "Id=" + Id +
                ", Menuno='" + Menuno + '\'' +
                ", title='" + title + '\'' +
                ", Menutype=" + Menutype +
                ", path='" + path + '\'' +
                ", component='" + component + '\'' +
                ", icon=" + icon +
                ", Nodelevel='" + Nodelevel + '\'' +
                ", Nodesort='" + Nodesort + '\'' +
                ", Parentid=" + Parentid +
                ", name=" + name +
                ", meta=" + meta +
                ", children=" + children +
                '}';
    }
}
