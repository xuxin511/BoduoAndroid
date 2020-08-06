package com.liansu.boduowms.bean.menu;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/9.
 */
public class MenuInfo {
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


}
