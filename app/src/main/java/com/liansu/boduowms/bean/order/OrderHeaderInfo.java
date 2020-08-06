/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.liansu.boduowms.bean.order;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class OrderHeaderInfo implements Parcelable {

    /**
     * Voucherno :
     * Erpvoucherno : 4500000010
     * Supplierno : 11019
     * Suppliername : 银邦金属复合材料股份有限公司
     * Vouchertype : 0
     * Plant :
     * Plantname :
     * Movetype : 101
     * Departmentcode :
     * Departmentname :
     * Erpnote :
     * Operator : 0
     */
    protected int                   Id;
    protected String                Voucherno;
    protected String                Erpvoucherno;
    protected String                Erpvouchertype;
    protected String                Supplierno;
    protected String                Suppliername;
    protected int                   Vouchertype;
    protected String                Plant;
    protected String                Plantname;
    protected String                Movetype;
    protected String                Departmentcode;
    protected String                Departmentname;
    protected String                Erpnote;
    protected int                   Operator;
    protected String                Scanuserno;
    protected String                Customerno;
    protected String                Customername;
    protected String                Towarehouseno;
    protected String                Fromwarehouseno;
    protected String                Strongholdcode;
    protected String                StrongholdName;
    protected List<OrderDetailInfo> Detail;

    public OrderHeaderInfo() {
    }

    protected OrderHeaderInfo(Parcel in) {
        Id = in.readInt();
        Voucherno = in.readString();
        Erpvoucherno = in.readString();
        Erpvouchertype = in.readString();
        Supplierno = in.readString();
        Suppliername = in.readString();
        Vouchertype = in.readInt();
        Plant = in.readString();
        Plantname = in.readString();
        Movetype = in.readString();
        Departmentcode = in.readString();
        Departmentname = in.readString();
        Erpnote = in.readString();
        Operator = in.readInt();
        Scanuserno = in.readString();
        Customerno = in.readString();
        Customername = in.readString();
        Towarehouseno = in.readString();
        Fromwarehouseno = in.readString();
        Strongholdcode = in.readString();
        StrongholdName = in.readString();
        Detail = in.createTypedArrayList(OrderDetailInfo.CREATOR);

    }

    public static final Creator<OrderHeaderInfo> CREATOR = new Creator<OrderHeaderInfo>() {
        @Override
        public OrderHeaderInfo createFromParcel(Parcel in) {
            return new OrderHeaderInfo(in);
        }

        @Override
        public OrderHeaderInfo[] newArray(int size) {
            return new OrderHeaderInfo[size];
        }
    };

    public String getVoucherno() {
        return Voucherno;
    }

    public void setVoucherno(String Voucherno) {
        this.Voucherno = Voucherno;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String Erpvoucherno) {
        this.Erpvoucherno = Erpvoucherno;
    }

    public String getSupplierno() {
        return Supplierno;
    }

    public void setSupplierno(String Supplierno) {
        this.Supplierno = Supplierno;
    }

    public String getSuppliername() {
        return Suppliername;
    }

    public void setSuppliername(String Suppliername) {
        this.Suppliername = Suppliername;
    }

    public int getVouchertype() {
        return Vouchertype;
    }

    public void setVouchertype(int Vouchertype) {
        this.Vouchertype = Vouchertype;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String Plant) {
        this.Plant = Plant;
    }

    public String getPlantname() {
        return Plantname;
    }

    public void setPlantname(String Plantname) {
        this.Plantname = Plantname;
    }

    public String getMovetype() {
        return Movetype;
    }

    public void setMovetype(String Movetype) {
        this.Movetype = Movetype;
    }

    public String getDepartmentcode() {
        return Departmentcode;
    }

    public void setDepartmentcode(String Departmentcode) {
        this.Departmentcode = Departmentcode;
    }

    public String getDepartmentname() {
        return Departmentname;
    }

    public void setDepartmentname(String Departmentname) {
        this.Departmentname = Departmentname;
    }

    public String getErpnote() {
        return Erpnote;
    }

    public void setErpnote(String Erpnote) {
        this.Erpnote = Erpnote;
    }

    public int getOperator() {
        return Operator;
    }

    public void setOperator(int Operator) {
        this.Operator = Operator;
    }

    public List<OrderDetailInfo> getDetail() {
        return Detail;
    }

    public void setDetail(List<OrderDetailInfo> detail) {
        this.Detail = detail;
    }

    public String getScanuserno() {
        return Scanuserno;
    }

    public void setScanuserno(String scanuserno) {
        Scanuserno = scanuserno;
    }

    public String getCustomerno() {
        return Customerno;
    }

    public void setCustomerno(String customerno) {
        Customerno = customerno;
    }

    public String getCustomername() {
        return Customername;
    }

    public void setCustomername(String customername) {
        Customername = customername;
    }

    public String getTowarehouseno() {
        return Towarehouseno;
    }

    public void setTowarehouseno(String towarehouseno) {
        Towarehouseno = towarehouseno;
    }

    public String getStrongholdcode() {
        return Strongholdcode;
    }

    public void setStrongholdcode(String strongholdcode) {
        Strongholdcode = strongholdcode;
    }

    public String getStrongholdName() {
        return StrongholdName;
    }

    public void setStrongholdName(String strongholdName) {
        StrongholdName = strongholdName;
    }

    public String getErpvouchertype() {
        return Erpvouchertype;
    }

    public void setErpvouchertype(String erpvouchertype) {
        Erpvouchertype = erpvouchertype;
    }

    public String getFromwarehouseno() {
        return Fromwarehouseno;
    }

    public void setFromwarehouseno(String fromwarehouseno) {
        Fromwarehouseno = fromwarehouseno;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Voucherno);
        dest.writeString(Erpvoucherno);
        dest.writeString(Erpvouchertype);
        dest.writeString(Supplierno);
        dest.writeString(Suppliername);
        dest.writeInt(Vouchertype);
        dest.writeString(Plant);
        dest.writeString(Plantname);
        dest.writeString(Movetype);
        dest.writeString(Departmentcode);
        dest.writeString(Departmentname);
        dest.writeString(Erpnote);
        dest.writeInt(Operator);
        dest.writeString(Scanuserno);
        dest.writeString(Customerno);
        dest.writeString(Customername);
        dest.writeString(Towarehouseno);
        dest.writeString(Fromwarehouseno);
        dest.writeString(Strongholdcode);
        dest.writeString(StrongholdName);
        dest.writeTypedList(Detail);
    }
}
