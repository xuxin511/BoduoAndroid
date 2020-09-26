package com.liansu.boduowms.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.liansu.boduowms.bean.stock.StockInfo;

import java.util.List;

/**
 * @ Des: 组托信息类
 * @ Created by yangyiqing on 2020/9/25.
 */
public class CombinePalletInfo implements Parcelable {
    private String targetPalletNo;
    private String awaitPalletNo;
    private int    combinePalletType; //组托方式
    protected String               Scanuserno;
    protected String               Printername; //打印机名称
    protected int                  Printertype; //打印机类型  1 激光打印机 2 台式打印机 3.蓝牙
    List<StockInfo>    StockList;
   public CombinePalletInfo(){}
    public String getTargetPalletNo() {
        return targetPalletNo;
    }

    public void setTargetPalletNo(String targetPalletNo) {
        this.targetPalletNo = targetPalletNo;
    }

    public String getAwaitPalletNo() {
        return awaitPalletNo;
    }

    public void setAwaitPalletNo(String awaitPalletNo) {
        this.awaitPalletNo = awaitPalletNo;
    }

    public int getCombinePalletType() {
        return combinePalletType;
    }

    public void setCombinePalletType(int combinePalletType) {
        this.combinePalletType = combinePalletType;
    }

    public String getScanuserno() {
        return Scanuserno;
    }

    public void setScanuserno(String scanuserno) {
        Scanuserno = scanuserno;
    }

    public String getPrintername() {
        return Printername;
    }

    public void setPrintername(String printername) {
        Printername = printername;
    }

    public int getPrintertype() {
        return Printertype;
    }

    public void setPrintertype(int printertype) {
        Printertype = printertype;
    }

    public List<StockInfo> getStockList() {
        return StockList;
    }

    public void setStockList(List<StockInfo> stockList) {
        StockList = stockList;
    }

    protected CombinePalletInfo(Parcel in) {
        targetPalletNo = in.readString();
        awaitPalletNo = in.readString();
        combinePalletType = in.readInt();
        Scanuserno = in.readString();
        Printername = in.readString();
        Printertype = in.readInt();
        StockList = in.createTypedArrayList(StockInfo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(targetPalletNo);
        dest.writeString(awaitPalletNo);
        dest.writeInt(combinePalletType);
        dest.writeString(Scanuserno);
        dest.writeString(Printername);
        dest.writeInt(Printertype);
        dest.writeTypedList(StockList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CombinePalletInfo> CREATOR = new Creator<CombinePalletInfo>() {
        @Override
        public CombinePalletInfo createFromParcel(Parcel in) {
            return new CombinePalletInfo(in);
        }

        @Override
        public CombinePalletInfo[] newArray(int size) {
            return new CombinePalletInfo[size];
        }
    };
}
