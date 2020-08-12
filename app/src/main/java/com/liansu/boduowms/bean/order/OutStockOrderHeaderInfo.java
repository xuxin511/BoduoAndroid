package com.liansu.boduowms.bean.order;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @ Des: 出库表头基类
 * @ Created by yangyiqing on 2020/7/20.
 */
public class OutStockOrderHeaderInfo implements Parcelable {


    /**
     * Customerno : 10000057
     * Customername : 义乌市宏裕食品商行
     * Address : 义乌市城北路上洪村东区1栋1号
     * Tel : 18657975666
     * Contacts : 曹宝贤
     * Erpstatus : Y
     * Erpnote : testwmsnxx
     * WeightTotal : 270.9
     * CostTotal : 100
     * OutCostTotal : 150
     * Detail : [{"Unit":"BOT","Spec":"8瓶*2升","Unitname":"瓶、罐、桶","Erpnote":"","MaterialCartonNum":1,"MaterialPartNum":0,"ReviewQty":0,"Headerid":15,"Id":27,"Companycode":"10","Strongholdcode":"2206","Strongholdname":"密巢销售有限公司","Erpvoucherno":"2102-B520-2007200001","Materialno":"130100001","Materialdesc":"百香果风味饮料（博多家园）1","Rowno":"1","Rownodel":"1","Scanqty":0,"Outstockqty":0,"PackQty":1,"Voucherqty":1,"Remainqty":1,"Areano":"81102,20200727"},{"Unit":"BOT","Spec":"8瓶*2升","Unitname":"瓶、罐、桶","Erpnote":"","MaterialCartonNum":100,"MaterialPartNum":0,"ReviewQty":0,"Headerid":15,"Id":28,"Companycode":"10","Strongholdcode":"2102","Strongholdname":"杭州密巢食品贸易有限公司","Erpvoucherno":"2102-B520-2007200001","Materialno":"130100002","Materialdesc":"菠萝风味饮料（博多家园）","Rowno":"2","Rownodel":"1","Scanqty":0,"Outstockqty":0,"PackQty":1,"Voucherqty":100,"Remainqty":100,"Areano":"81102,20200716|81101,20200716|81102,20200724|81102,20200727|81102,20200728|81101,20200729|81101,20200730|81102,20200731|81101,20200803|81102,20200803|81102,20200804"}]
     * OrderCartonNum : 101
     * OrderScanCartonNum : 0
     * Id : 0
     * Companycode : 10
     * Strongholdcode : 2102
     * Strongholdname : 杭州密巢食品贸易有限公司
     * Erpvoucherno : 2102-B520-2007200001
     * Vouchertype : 29
     */

    private String                        Customerno;
    private String                        Customername;
    private String                        Address;
    private String                        Tel;
    private String                        Contacts;
    private String                        Erpstatus;
    private String                        Erpnote;
    private double                        WeightTotal;
    private int                           CostTotal;
    private int                           OutCostTotal;
    private int                           OrderCartonNum;
    private int                           OrderScanCartonNum;
    private int                           Id;
    private String                        Companycode;
    private String                        Strongholdcode;
    private String                        Strongholdname;
    private String                        Erpvoucherno;
    private int                           Vouchertype;
    private List<OutStockOrderDetailInfo> Detail;
    private String                        Arrvoucherno;
    private String                        Purchaseno;
    private String      Qualityno;
    public OutStockOrderHeaderInfo() {
    }

    protected OutStockOrderHeaderInfo(Parcel in) {
        Customerno = in.readString();
        Customername = in.readString();
        Address = in.readString();
        Tel = in.readString();
        Contacts = in.readString();
        Erpstatus = in.readString();
        Erpnote = in.readString();
        WeightTotal = in.readDouble();
        CostTotal = in.readInt();
        OutCostTotal = in.readInt();
        OrderCartonNum = in.readInt();
        OrderScanCartonNum = in.readInt();
        Id = in.readInt();
        Companycode = in.readString();
        Strongholdcode = in.readString();
        Strongholdname = in.readString();
        Erpvoucherno = in.readString();
        Vouchertype = in.readInt();
        Detail = in.createTypedArrayList(OutStockOrderDetailInfo.CREATOR);
        Arrvoucherno = in.readString();
        Purchaseno = in.readString();
        Qualityno=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Customerno);
        dest.writeString(Customername);
        dest.writeString(Address);
        dest.writeString(Tel);
        dest.writeString(Contacts);
        dest.writeString(Erpstatus);
        dest.writeString(Erpnote);
        dest.writeDouble(WeightTotal);
        dest.writeInt(CostTotal);
        dest.writeInt(OutCostTotal);
        dest.writeInt(OrderCartonNum);
        dest.writeInt(OrderScanCartonNum);
        dest.writeInt(Id);
        dest.writeString(Companycode);
        dest.writeString(Strongholdcode);
        dest.writeString(Strongholdname);
        dest.writeString(Erpvoucherno);
        dest.writeInt(Vouchertype);
        dest.writeTypedList(Detail);
        dest.writeString(Arrvoucherno);
        dest.writeString(Purchaseno);
        dest.writeString(Qualityno);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OutStockOrderHeaderInfo> CREATOR = new Creator<OutStockOrderHeaderInfo>() {
        @Override
        public OutStockOrderHeaderInfo createFromParcel(Parcel in) {
            return new OutStockOrderHeaderInfo(in);
        }

        @Override
        public OutStockOrderHeaderInfo[] newArray(int size) {
            return new OutStockOrderHeaderInfo[size];
        }
    };

    public String getCustomerno() {
        return Customerno;
    }

    public void setCustomerno(String Customerno) {
        this.Customerno = Customerno;
    }

    public String getCustomername() {
        return Customername;
    }

    public void setCustomername(String Customername) {
        this.Customername = Customername;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String Tel) {
        this.Tel = Tel;
    }

    public String getContacts() {
        return Contacts;
    }

    public void setContacts(String Contacts) {
        this.Contacts = Contacts;
    }

    public String getErpstatus() {
        return Erpstatus;
    }

    public void setErpstatus(String Erpstatus) {
        this.Erpstatus = Erpstatus;
    }

    public String getErpnote() {
        return Erpnote;
    }

    public void setErpnote(String Erpnote) {
        this.Erpnote = Erpnote;
    }

    public double getWeightTotal() {
        return WeightTotal;
    }

    public void setWeightTotal(double WeightTotal) {
        this.WeightTotal = WeightTotal;
    }

    public int getCostTotal() {
        return CostTotal;
    }

    public void setCostTotal(int CostTotal) {
        this.CostTotal = CostTotal;
    }

    public int getOutCostTotal() {
        return OutCostTotal;
    }

    public void setOutCostTotal(int OutCostTotal) {
        this.OutCostTotal = OutCostTotal;
    }

    public int getOrderCartonNum() {
        return OrderCartonNum;
    }

    public void setOrderCartonNum(int OrderCartonNum) {
        this.OrderCartonNum = OrderCartonNum;
    }

    public int getOrderScanCartonNum() {
        return OrderScanCartonNum;
    }

    public void setOrderScanCartonNum(int OrderScanCartonNum) {
        this.OrderScanCartonNum = OrderScanCartonNum;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getCompanycode() {
        return Companycode;
    }

    public void setCompanycode(String Companycode) {
        this.Companycode = Companycode;
    }

    public String getStrongholdcode() {
        return Strongholdcode;
    }

    public void setStrongholdcode(String Strongholdcode) {
        this.Strongholdcode = Strongholdcode;
    }

    public String getStrongholdname() {
        return Strongholdname;
    }

    public void setStrongholdname(String Strongholdname) {
        this.Strongholdname = Strongholdname;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String Erpvoucherno) {
        this.Erpvoucherno = Erpvoucherno;
    }

    public int getVouchertype() {
        return Vouchertype;
    }

    public void setVouchertype(int Vouchertype) {
        this.Vouchertype = Vouchertype;
    }

    public List<OutStockOrderDetailInfo> getDetail() {
        return Detail;
    }

    public void setDetail(List<OutStockOrderDetailInfo> Detail) {
        this.Detail = Detail;
    }

    public String getArrvoucherno() {
        return Arrvoucherno;
    }

    public void setArrvoucherno(String arrvoucherno) {
        Arrvoucherno = arrvoucherno;
    }

    public String getPurchaseno() {
        return Purchaseno;
    }

    public void setPurchaseno(String purchaseno) {
        Purchaseno = purchaseno;
    }

    public String getQualityno() {
        return Qualityno;
    }

    public void setQualityno(String qualityno) {
        Qualityno = qualityno;
    }
}
