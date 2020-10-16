package com.liansu.boduowms.modules.outstock.Model;

import java.util.List;

public class OutstockWaybillStatusModel {

    public List<OutstockWaybillStatusDtailModel> details ;

    public String Contacts ;
    public String Customerno ;

    public String Erpvoucherno ;

    public String Createtime ;
    public String Address ;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getContacts() {
        return Contacts;
    }

    public  boolean Ischeck;

    public boolean isIscheck() {
        return Ischeck;
    }

    public void setIscheck(boolean ischeck) {
        Ischeck = ischeck;
    }

    public void setContacts(String contacts) {
        Contacts = contacts;
    }

    public String getCustomerno() {
        return Customerno;
    }

    public void setCustomerno(String customerno) {
        Customerno = customerno;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String erpvoucherno) {
        Erpvoucherno = erpvoucherno;
    }

    public String getCreatetime() {
        return Createtime;
    }

    public void setCreatetime(String createtime) {
        Createtime = createtime;
    }

    public List<OutstockWaybillStatusDtailModel> getDetails() {
        return details;
    }

    public void setDetails(List<OutstockWaybillStatusDtailModel> details) {
        this.details = details;
    }
}


