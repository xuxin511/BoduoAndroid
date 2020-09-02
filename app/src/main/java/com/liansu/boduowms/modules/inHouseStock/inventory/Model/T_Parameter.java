package com.liansu.boduowms.modules.inHouseStock.inventory.Model;


//参数类
public class T_Parameter {

        public int Id;
        public int Parameterid ;
        public String Parametername ;
        public String Groupname ;
        public String Groupdesc ;
        public String Parameteridn ;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getParameterid() {
        return Parameterid;
    }

    public void setParameterid(int parameterid) {
        Parameterid = parameterid;
    }

    public String getParametername() {
        return Parametername;
    }

    public void setParametername(String parametername) {
        Parametername = parametername;
    }

    public String getGroupname() {
        return Groupname;
    }

    public void setGroupname(String groupname) {
        Groupname = groupname;
    }

    public String getGroupdesc() {
        return Groupdesc;
    }

    public void setGroupdesc(String groupdesc) {
        Groupdesc = groupdesc;
    }

    public String getParameteridn() {
        return Parameteridn;
    }

    public void setParameteridn(String parameteridn) {
        Parameteridn = parameteridn;
    }
}
