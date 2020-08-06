package com.liansu.boduowms.bean;

/**
 * Created by 86988 on 2019-03-22.
 */

public class QRCode {
    public static  final  int BARCODE_TYPE_OUTERBOX  =1;  //外箱
    public static  final  int BARCODE_TYPE_PALLET_NO =2;  //托盘
    public static  final  int BARCODE_TYPE_EAN       =3;       //EAN 69码
    public static  final  int BARCODE_TYPE_NONE      =0;       //
    public static  final  int BARCODE_TYPE_ERROR     =-1;       //异常情况
    private String            originalCode;  //原始数据
    private String            materialNo;
    private String            materialDesc;
    private int               qty;
    private String            barcode;
    private int               barcodeType;
    private String            serialNo;
    private int               materialNoId;
    private String            batchNo;
    private String            message;

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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(int barcodeType) {
        this.barcodeType = barcodeType;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public int getMaterialNoId() {
        return materialNoId;
    }

    public void setMaterialNoId(int materialNoId) {
        this.materialNoId = materialNoId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOriginalCode() {
        return originalCode;
    }

    public void setOriginalCode(String originalCode) {
        this.originalCode = originalCode;
    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof QRCode)) return false;
//        QRCode qrCode = (QRCode) o;
//        return randomCode.equals(qrCode.randomCode);
//    }


}
