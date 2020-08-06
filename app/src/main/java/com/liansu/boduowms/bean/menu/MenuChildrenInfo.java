package com.liansu.boduowms.bean.menu;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/9.
 */
public class MenuChildrenInfo {
    /**
     * Id : 14
     * path : 22
     * component : 1
     * name : null
     * icon : null
     * title : 到货入库
     * meta : {"Id":14,"keepalive":false,"internalOrExternal":false,"title":"到货入库","icon":null}
     */

    private int       Id;
    private String    path;
    private String    component;
    private Object    name;
    private Object    icon;
    private String    title;
    private MetaBeanX meta;

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

    public MetaBeanX getMeta() {
        return meta;
    }

    public void setMeta(MetaBeanX meta) {
        this.meta = meta;
    }

    public static class MetaBeanX {
        /**
         * Id : 14
         * keepalive : false
         * internalOrExternal : false
         * title : 到货入库
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
}
