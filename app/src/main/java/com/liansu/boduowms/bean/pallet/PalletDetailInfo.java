package com.liansu.boduowms.bean.pallet;

import android.os.Parcel;
import android.os.Parcelable;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseInfo;
import com.liansu.boduowms.bean.stock.StockInfo;


import java.util.ArrayList;

/**
 * @ Des:
 * @ Created by yangyiqing on 2020/7/3.
 */
public class PalletDetailInfo extends BaseInfo implements Parcelable {
    private String                    TaskNo;
    private String                    VoucherNo;
    private String                    RowNo;
    private String                    PalletNo;
    private String                    MaterialNo;
    private String                    MaterialDesc;
    private int                       IsSerial;
    private String                    PartNo;
    private String                    BatchNo;
    private String                    SupPrdBatch;
    private int                       AreaID;
    public  String                    SupplierNo;
    public  String                    SupplierName;
    private ArrayList<OutBarcodeInfo> lstBarCode;
    private ArrayList<StockInfo>      lstStockInfo;
    private String                    BarCode;
    private int                       PalletType;
    private String                    PrintIPAdress;
    private String                    PrintIPName;


    public PalletDetailInfo() {
    }

    protected PalletDetailInfo(Parcel in) {
        TaskNo = in.readString();
        VoucherNo = in.readString();
        RowNo = in.readString();
        PalletNo = in.readString();
        MaterialNo = in.readString();
        MaterialDesc = in.readString();
        IsSerial = in.readInt();
        PartNo = in.readString();
        BatchNo = in.readString();
        SupPrdBatch = in.readString();
        AreaID = in.readInt();
        SupplierNo = in.readString();
        SupplierName = in.readString();
        lstBarCode = in.createTypedArrayList(OutBarcodeInfo.CREATOR);
        lstStockInfo = in.createTypedArrayList(StockInfo.CREATOR);
        BarCode = in.readString();
        PalletType = in.readInt();
        PrintIPAdress = in.readString();
        PrintIPName = in.readString();
    }

    public static final Creator<PalletDetailInfo> CREATOR = new Creator<PalletDetailInfo>() {
        @Override
        public PalletDetailInfo createFromParcel(Parcel in) {
            return new PalletDetailInfo(in);
        }

        @Override
        public PalletDetailInfo[] newArray(int size) {
            return new PalletDetailInfo[size];
        }
    };

    public String getTaskNo() {
        return TaskNo;
    }

    public void setTaskNo(String taskNo) {
        TaskNo = taskNo;
    }

    public String getVoucherNo() {
        return VoucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        VoucherNo = voucherNo;
    }

    public String getRowNo() {
        return RowNo;
    }

    public void setRowNo(String rowNo) {
        RowNo = rowNo;
    }

    public String getPalletNo() {
        return PalletNo;
    }

    public void setPalletNo(String palletNo) {
        PalletNo = palletNo;
    }

    public String getMaterialNo() {
        return MaterialNo;
    }

    public void setMaterialNo(String materialNo) {
        MaterialNo = materialNo;
    }

    public String getMaterialDesc() {
        return MaterialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        MaterialDesc = materialDesc;
    }

    public int getIsSerial() {
        return IsSerial;
    }

    public void setIsSerial(int isSerial) {
        IsSerial = isSerial;
    }

    public String getPartNo() {
        return PartNo;
    }

    public void setPartNo(String partNo) {
        PartNo = partNo;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    public String getSupPrdBatch() {
        return SupPrdBatch;
    }

    public void setSupPrdBatch(String supPrdBatch) {
        SupPrdBatch = supPrdBatch;
    }

    public int getAreaID() {
        return AreaID;
    }

    public void setAreaID(int areaID) {
        AreaID = areaID;
    }

    public String getSupplierNo() {
        return SupplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        SupplierNo = supplierNo;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public ArrayList<OutBarcodeInfo> getLstBarCode() {
        return lstBarCode;
    }

    public void setLstBarCode(ArrayList<OutBarcodeInfo> lstBarCode) {
        this.lstBarCode = lstBarCode;
    }

    public ArrayList<StockInfo> getLstStockInfo() {
        return lstStockInfo;
    }

    public void setLstStockInfo(ArrayList<StockInfo> lstStockInfo) {
        this.lstStockInfo = lstStockInfo;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public int getPalletType() {
        return PalletType;
    }

    public void setPalletType(int palletType) {
        PalletType = palletType;
    }

    public String getPrintIPAdress() {
        return PrintIPAdress;
    }

    public void setPrintIPAdress(String printIPAdress) {
        PrintIPAdress = printIPAdress;
    }

    public String getPrintIPName() {
        return PrintIPName;
    }

    public void setPrintIPName(String printIPName) {
        PrintIPName = printIPName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(TaskNo);
        dest.writeString(VoucherNo);
        dest.writeString(RowNo);
        dest.writeString(PalletNo);
        dest.writeString(MaterialNo);
        dest.writeString(MaterialDesc);
        dest.writeInt(IsSerial);
        dest.writeString(PartNo);
        dest.writeString(BatchNo);
        dest.writeString(SupPrdBatch);
        dest.writeInt(AreaID);
        dest.writeString(SupplierNo);
        dest.writeString(SupplierName);
        dest.writeTypedList(lstBarCode);
        dest.writeTypedList(lstStockInfo);
        dest.writeString(BarCode);
        dest.writeInt(PalletType);
        dest.writeString(PrintIPAdress);
        dest.writeString(PrintIPName);
    }
}
