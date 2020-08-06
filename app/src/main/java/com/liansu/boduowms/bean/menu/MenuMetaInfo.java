package com.liansu.boduowms.bean.menu;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/9.
 */
public class MenuMetaInfo {
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
}
