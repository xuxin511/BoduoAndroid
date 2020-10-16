package com.liansu.boduowms.modules.outstock.Model;

public class OutstockWaybillStatusDtailModel {
    public int Id;
    public String Creater ;
    public String Createtime ;
    public String Modifyer ;
    public String Materialno ;
    public String Materialdesc;
    public String Batchno;
    public String Barcode ;
    public Float Qty ;
    public String Erpvoucherno ;
    public String Arrvoucherno ;
    public String Towarehouseno ;
    public String Companycode ;
    public String Strongholdcode;
    public String Strongholdname ;

    public String Unit ;
    public int Vouchertype ;

    public String platform ;

    public String watercode ;


    public String Serialno ;
    public String WayBillno ;
    public int Areaid;


    public  boolean IsCheck;


    /// <summary>
    /// 费用计算方式 1：重量  2：件数  3：体积
    /// </summary>
    /// <value></value>
    public int CostCalMethod ;


    /// <summary>
    /// 业务类型 1：加盟 2：销售
    /// </summary>
    /// <value></value>
    public int BusinessType;


    /// <summary>
    /// 送货方式 1：自提  2：送货上门
    /// </summary>
    /// <value></value>
    public int SendMethod ;



    /// <summary>
    /// 总重量
    /// </summary>
    public Float WeightTotal ;

    /// <summary>
    /// 体积
    /// </summary>
    /// <value></value>
    public Float Volume;

    /// <summary>
    /// 单价
    /// </summary>
    public Float PrePrice ;


    /// <summary>
    /// 保价费用
    /// </summary>
    public Float InsuranceCost ;


    /// <summary>
    /// 总费用
    /// </summary>
    public Float Price;


    ///送货上门费
    public Float OutCostTotal ;



    ///件数
    public int PackageNum ;


    public String Erpnote;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCreater() {
        return Creater;
    }

    public void setCreater(String creater) {
        Creater = creater;
    }

    public String getCreatetime() {
        return Createtime;
    }

    public void setCreatetime(String createtime) {
        Createtime = createtime;
    }

    public String getModifyer() {
        return Modifyer;
    }

    public void setModifyer(String modifyer) {
        Modifyer = modifyer;
    }

    public String getMaterialno() {
        return Materialno;
    }

    public void setMaterialno(String materialno) {
        Materialno = materialno;
    }

    public String getMaterialdesc() {
        return Materialdesc;
    }

    public void setMaterialdesc(String materialdesc) {
        Materialdesc = materialdesc;
    }

    public String getBatchno() {
        return Batchno;
    }

    public void setBatchno(String batchno) {
        Batchno = batchno;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public Float getQty() {
        return Qty;
    }

    public void setQty(Float qty) {
        Qty = qty;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String erpvoucherno) {
        Erpvoucherno = erpvoucherno;
    }

    public String getArrvoucherno() {
        return Arrvoucherno;
    }

    public void setArrvoucherno(String arrvoucherno) {
        Arrvoucherno = arrvoucherno;
    }

    public String getTowarehouseno() {
        return Towarehouseno;
    }

    public void setTowarehouseno(String towarehouseno) {
        Towarehouseno = towarehouseno;
    }

    public String getCompanycode() {
        return Companycode;
    }

    public void setCompanycode(String companycode) {
        Companycode = companycode;
    }

    public String getStrongholdcode() {
        return Strongholdcode;
    }

    public void setStrongholdcode(String strongholdcode) {
        Strongholdcode = strongholdcode;
    }

    public String getStrongholdname() {
        return Strongholdname;
    }

    public void setStrongholdname(String strongholdname) {
        Strongholdname = strongholdname;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public int getVouchertype() {
        return Vouchertype;
    }

    public void setVouchertype(int vouchertype) {
        Vouchertype = vouchertype;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getWatercode() {
        return watercode;
    }

    public void setWatercode(String watercode) {
        this.watercode = watercode;
    }

    public String getSerialno() {
        return Serialno;
    }

    public void setSerialno(String serialno) {
        Serialno = serialno;
    }

    public String getWayBillno() {
        return WayBillno;
    }

    public void setWayBillno(String wayBillno) {
        WayBillno = wayBillno;
    }

    public int getAreaid() {
        return Areaid;
    }

    public void setAreaid(int areaid) {
        Areaid = areaid;
    }

    public boolean isCheck() {
        return IsCheck;
    }

    public void setCheck(boolean check) {
        IsCheck = check;
    }

    public int getCostCalMethod() {
        return CostCalMethod;
    }

    public void setCostCalMethod(int costCalMethod) {
        CostCalMethod = costCalMethod;
    }

    public int getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(int businessType) {
        BusinessType = businessType;
    }

    public int getSendMethod() {
        return SendMethod;
    }

    public void setSendMethod(int sendMethod) {
        SendMethod = sendMethod;
    }

    public Float getWeightTotal() {
        return WeightTotal;
    }

    public void setWeightTotal(Float weightTotal) {
        WeightTotal = weightTotal;
    }

    public Float getVolume() {
        return Volume;
    }

    public void setVolume(Float volume) {
        Volume = volume;
    }

    public Float getPrePrice() {
        return PrePrice;
    }

    public void setPrePrice(Float prePrice) {
        PrePrice = prePrice;
    }

    public Float getInsuranceCost() {
        return InsuranceCost;
    }

    public void setInsuranceCost(Float insuranceCost) {
        InsuranceCost = insuranceCost;
    }

    public Float getPrice() {
        return Price;
    }

    public void setPrice(Float price) {
        Price = price;
    }

    public Float getOutCostTotal() {
        return OutCostTotal;
    }

    public void setOutCostTotal(Float outCostTotal) {
        OutCostTotal = outCostTotal;
    }

    public int getPackageNum() {
        return PackageNum;
    }

    public void setPackageNum(int packageNum) {
        PackageNum = packageNum;
    }

    public String getErpnote() {
        return Erpnote;
    }

    public void setErpnote(String erpnote) {
        Erpnote = erpnote;
    }
}
