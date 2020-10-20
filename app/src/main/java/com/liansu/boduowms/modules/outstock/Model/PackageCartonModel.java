package com.liansu.boduowms.modules.outstock.Model;

//拼箱数据类
public   class PackageCartonModel {

    public  int Id;
    public  String Serialno ;

    public  Float PackageNum;

    public  int IsReview ;

    public Float Qty ;

    /// <summary>
    /// 复核数量
    /// </summary>
    /// <value></value>
    public  Float ReviewQty;

    /// <summary>
    /// 库存ID
    /// </summary>
    /// <value></value>
    public  int StockId ;

    /// <summary>
    /// 工单项次
    /// </summary>
    /// <value></value>
    public  String WORowno ;
    /// <summary>
    /// 工单项序
    /// </summary>
    /// <value></value>
    public  String WORownodel;

    /// <summary>
    /// 派车单-发货通知单项次
    /// </summary>
    /// <value></value>
    public  String SendRowno ;

    /// <summary>
    /// 拨入据点
    /// </summary>
    /// <value></value>
    public  String ToCompanyCode ;
    /// <summary>
    /// 拨出据点
    /// </summary>
    /// <value></value>
    public  String FromCompanyCode;

    /// <summary>
    /// 重量单价
    /// </summary>
    /// <value></value>
    public  Float WeightPrice ;

    /// <summary>
    /// 物料理论重量
    /// </summary>
    /// <value></value>

    public  Float PreWeight ;

    public Float getQty() {
        return Qty;
    }

    public void setQty(Float qty) {
        Qty = qty;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getSerialno() {
        return Serialno;
    }

    public void setSerialno(String serialno) {
        Serialno = serialno;
    }

    public Float getPackageNum() {
        return PackageNum;
    }

    public void setPackageNum(Float packageNum) {
        PackageNum = packageNum;
    }

    public int getIsReview() {
        return IsReview;
    }

    public void setIsReview(int isReview) {
        IsReview = isReview;
    }

    public Float getReviewQty() {
        return ReviewQty;
    }

    public void setReviewQty(Float reviewQty) {
        ReviewQty = reviewQty;
    }

    public int getStockId() {
        return StockId;
    }

    public void setStockId(int stockId) {
        StockId = stockId;
    }

    public String getWORowno() {
        return WORowno;
    }

    public void setWORowno(String WORowno) {
        this.WORowno = WORowno;
    }

    public String getWORownodel() {
        return WORownodel;
    }

    public void setWORownodel(String WORownodel) {
        this.WORownodel = WORownodel;
    }

    public String getSendRowno() {
        return SendRowno;
    }

    public void setSendRowno(String sendRowno) {
        SendRowno = sendRowno;
    }

    public String getToCompanyCode() {
        return ToCompanyCode;
    }

    public void setToCompanyCode(String toCompanyCode) {
        ToCompanyCode = toCompanyCode;
    }

    public String getFromCompanyCode() {
        return FromCompanyCode;
    }

    public void setFromCompanyCode(String fromCompanyCode) {
        FromCompanyCode = fromCompanyCode;
    }

    public Float getWeightPrice() {
        return WeightPrice;
    }

    public void setWeightPrice(Float weightPrice) {
        WeightPrice = weightPrice;
    }

    public Float getPreWeight() {
        return PreWeight;
    }

    public void setPreWeight(Float preWeight) {
        PreWeight = preWeight;
    }
}
