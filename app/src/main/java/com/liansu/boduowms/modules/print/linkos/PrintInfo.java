package com.liansu.boduowms.modules.print.linkos;

/**
 * @desc: 打印类
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/7/15 19:50
 */
public class PrintInfo {
    private String printType; // 打印类型
    private String orderType;   //订单类型
    private String materialNo;   //
    private String materialDesc;  //物料描述
    private float  qty;    //数量
    private String batchNo; //批次
    private String arrivalTime;  //到货时间
    private String signatory;  //签收人
    private String QRCode;  //二维码
    private String spec;//规格

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getSignatory() {
        return signatory;
    }

    public void setSignatory(String signatory) {
        this.signatory = signatory;
    }

    public String getQRCode() {
        return QRCode;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
}
