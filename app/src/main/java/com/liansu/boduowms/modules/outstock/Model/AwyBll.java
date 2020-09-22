package com.liansu.boduowms.modules.outstock.Model;

public  class AwyBll {

    public  int Id ;
    public String Creater ;

     public  String WayBillNo;

    public String Modifyer ;
    public String Trackingnumber;
    public String Voucherno ;
    public String Erpvoucherno ;
    public int Vouchertype ;
    public int Status ;

     public  int IsStockCombine;

     public  int Printertype;
     public  String Printername;

    public int getPrintertype() {
        return Printertype;
    }

    public void setPrintertype(int printertype) {
        Printertype = printertype;
    }

    public String getPrintername() {
        return Printername;
    }

    public void setPrintername(String printername) {
        Printername = printername;
    }

    public int getIsStockCombine() {
        return IsStockCombine;
    }

    public void setIsStockCombine(int isStockCombine) {
        IsStockCombine = isStockCombine;
    }

    public String getWayBillNo() {
        return WayBillNo;
    }

    public void setWayBillNo(String wayBillNo) {
        WayBillNo = wayBillNo;
    }

    /// <summary>
    /// 体积
    /// </summary>
    /// <value></value>
    public Float Volume ;
    public String Note ;
    public String Address ;

    public String Tel ;

    public Float getVolume() {
        return Volume;
    }

    public void setVolume(Float volume) {
        Volume = volume;
    }

    public String getTrackingnumber() {
        return Trackingnumber;
    }

    public void setTrackingnumber(String trackingnumber) {
        Trackingnumber = trackingnumber;
    }

    public String Contacts ;
    public String Customerno ;
    public String Customername ;
    /// <summary>
    /// 送货方式 1：自提  2：送货上门
    /// </summary>
    /// <value></value>
    public int SendMethod ;

    /// <summary>
    /// 业务类型 1：加盟 2：销售
    /// </summary>
    /// <value></value>
    public int BusinessType ;

    /// <summary>
    /// 结算方式  1：月结    2：到付
    /// </summary>
    /// <value></value>
    public int SettlementMethod ;
    /// <summary>
    /// 费用计算方式 1：重量  2：件数  3：体积
    /// </summary>
    /// <value></value>
    public int CostCalMethod ;

    /// <summary>
    /// 单价
    /// </summary>
    public Float PrePrice ;

    /// <summary>
    /// 物流公司
    /// </summary>
    /// <value></value>
    public String LogisticsCompany ;

    /// <summary>
    /// 发货地址 取仓库代码
    /// </summary>
    /// <value></value>
    public String SendAddress ;

    /// <summary>
    /// 保价费用
    /// </summary>
    public Float InsuranceCost ;

    /// <summary>
    /// 总重量
    /// </summary>
    public Float WeightTotal ;

    /// <summary>
    /// 总运费
    /// </summary>
    public Float CostTotal;

    ///送货上门费
    public Float OutCostTotal ;

    /// <summary>
    /// 关联单号
    /// </summary>
    /// <value></value>

    public String LinkVoucherNo ;

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

    public String getModifyer() {
        return Modifyer;
    }

    public void setModifyer(String modifyer) {
        Modifyer = modifyer;
    }

    public String getVoucherno() {
        return Voucherno;
    }

    public void setVoucherno(String voucherno) {
        Voucherno = voucherno;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String erpvoucherno) {
        Erpvoucherno = erpvoucherno;
    }

    public int getVouchertype() {
        return Vouchertype;
    }

    public void setVouchertype(int vouchertype) {
        Vouchertype = vouchertype;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getContacts() {
        return Contacts;
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

    public String getCustomername() {
        return Customername;
    }

    public void setCustomername(String customername) {
        Customername = customername;
    }

    public int getSendMethod() {
        return SendMethod;
    }

    public void setSendMethod(int sendMethod) {
        SendMethod = sendMethod;
    }

    public int getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(int businessType) {
        BusinessType = businessType;
    }

    public int getSettlementMethod() {
        return SettlementMethod;
    }

    public void setSettlementMethod(int settlementMethod) {
        SettlementMethod = settlementMethod;
    }

    public int getCostCalMethod() {
        return CostCalMethod;
    }

    public void setCostCalMethod(int costCalMethod) {
        CostCalMethod = costCalMethod;
    }

    public Float getPrePrice() {
        return PrePrice;
    }

    public void setPrePrice(Float prePrice) {
        PrePrice = prePrice;
    }

    public String getLogisticsCompany() {
        return LogisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        LogisticsCompany = logisticsCompany;
    }

    public String getSendAddress() {
        return SendAddress;
    }

    public void setSendAddress(String sendAddress) {
        SendAddress = sendAddress;
    }

    public Float getInsuranceCost() {
        return InsuranceCost;
    }

    public void setInsuranceCost(Float insuranceCost) {
        InsuranceCost = insuranceCost;
    }

    public Float getWeightTotal() {
        return WeightTotal;
    }

    public void setWeightTotal(Float weightTotal) {
        WeightTotal = weightTotal;
    }

    public Float getCostTotal() {
        return CostTotal;
    }

    public void setCostTotal(Float costTotal) {
        CostTotal = costTotal;
    }

    public Float getOutCostTotal() {
        return OutCostTotal;
    }

    public void setOutCostTotal(Float outCostTotal) {
        OutCostTotal = outCostTotal;
    }

    public String getLinkVoucherNo() {
        return LinkVoucherNo;
    }

    public void setLinkVoucherNo(String linkVoucherNo) {
        LinkVoucherNo = linkVoucherNo;
    }
}
