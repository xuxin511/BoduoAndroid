package com.liansu.boduowms.bean.stock;

import android.os.Parcel;
import android.os.Parcelable;

import com.liansu.boduowms.bean.base.BaseInfo;

/**
 * @ Des: 收货 发货回退
 * @ Created by yangyiqing on 2020/8/25.
 */
public class VoucherDetailSubInfo extends BaseInfo implements Parcelable {
    private int    Headeridsub;
    private String Rowno;
    private String Rownodel;
    private String Materialno;
    private String Materialdesc;
    private float  Linestatussub;
    private String Batchno;
    private String Barcode;
    private float  Qty;
    private String Erpvoucherno;
    private String Towarehouseno;
    private String Unit;
    private int    Vouchertype;
    private String GUID;
    private String Cusmaterialno;
    private String Serialno;
    private String Fromwarehouseno;
    private int    IsAmount;
    private String Customerno;
    private String Customername;
    private String Specialstock;
    private String WCustomerno;
    private int    Areaid;
    /// <summary>
    /// 月台
    /// </summary>
    /// <value></value>
    private String platform;
    /// <summary>
    /// 商品69码
    /// </summary>
    /// <value></value>
    private String watercode;
    /// <summary>
    /// 发货门店：到货通知单；仓退：到货单号
    /// </summary>
    /// <value></value>
    private String Arrvoucherno;


    /// <summary>
    /// 拼箱数量
    /// </summary>
    /// <value></value>
    private float PackageNum;

    /// <summary>
    /// 是否复核
    /// </summary>
    /// <value></value>
    private int IsReview;

    /// <summary>
    /// 复核数量
    /// </summary>
    /// <value></value>
    private float ReviewQty;


    /// <summary>
    /// 库存ID
    /// </summary>
    /// <value></value>
    private int StockId;


    /// <summary>
    /// 工单项次
    /// </summary>
    /// <value></value>
    ///
    private String WORowno;


    /// <summary>
    /// 工单项序
    /// </summary>
    /// <value></value>
    private String WORownodel;

    /// <summary>
    /// 拨入据点
    /// </summary>
    /// <value></value>
    private String FromCompanyCode;
    protected  int Scantype; //1-原托盘入库 0-新托盘入库
    protected  String WBarcode;//外箱码
    private String Areano;  //库位编码
    protected VoucherDetailSubInfo(Parcel in) {

        Headeridsub = in.readInt();
        Rowno = in.readString();
        Rownodel = in.readString();
        Materialno = in.readString();
        Materialdesc = in.readString();
        Linestatussub = in.readFloat();
        Batchno = in.readString();
        Barcode = in.readString();
        Qty = in.readFloat();
        Erpvoucherno = in.readString();
        Towarehouseno = in.readString();
        Unit = in.readString();
        Vouchertype = in.readInt();
        GUID = in.readString();
        Cusmaterialno = in.readString();
        Serialno = in.readString();
        Fromwarehouseno = in.readString();
        IsAmount = in.readInt();
        Customerno = in.readString();
        Customername = in.readString();
        Specialstock = in.readString();
        WCustomerno = in.readString();
        Areaid = in.readInt();
        platform = in.readString();
        watercode = in.readString();
        Arrvoucherno = in.readString();
        PackageNum = in.readFloat();
        IsReview = in.readInt();
        ReviewQty = in.readFloat();
        StockId = in.readInt();
        WORowno = in.readString();
        WORownodel = in.readString();
        FromCompanyCode = in.readString();
        Scantype=in.readInt();
        WBarcode=in.readString();
        Areano=in.readString();

    }

    public static final Creator<VoucherDetailSubInfo> CREATOR = new Creator<VoucherDetailSubInfo>() {
        @Override
        public VoucherDetailSubInfo createFromParcel(Parcel in) {
            return new VoucherDetailSubInfo(in);
        }

        @Override
        public VoucherDetailSubInfo[] newArray(int size) {
            return new VoucherDetailSubInfo[size];
        }
    };

    public int getHeaderidsub() {
        return Headeridsub;
    }

    public void setHeaderidsub(int headeridsub) {
        Headeridsub = headeridsub;
    }

    public String getRowno() {
        return Rowno;
    }

    public void setRowno(String rowno) {
        Rowno = rowno;
    }

    public String getRownodel() {
        return Rownodel;
    }

    public void setRownodel(String rownodel) {
        Rownodel = rownodel;
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

    public float getLinestatussub() {
        return Linestatussub;
    }

    public void setLinestatussub(float linestatussub) {
        Linestatussub = linestatussub;
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

    public float getQty() {
        return Qty;
    }

    public void setQty(float qty) {
        Qty = qty;
    }

    public String getErpvoucherno() {
        return Erpvoucherno;
    }

    public void setErpvoucherno(String erpvoucherno) {
        Erpvoucherno = erpvoucherno;
    }

    public String getTowarehouseno() {
        return Towarehouseno;
    }

    public void setTowarehouseno(String towarehouseno) {
        Towarehouseno = towarehouseno;
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

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public String getCusmaterialno() {
        return Cusmaterialno;
    }

    public void setCusmaterialno(String cusmaterialno) {
        Cusmaterialno = cusmaterialno;
    }

    public String getSerialno() {
        return Serialno;
    }

    public void setSerialno(String serialno) {
        Serialno = serialno;
    }

    public String getFromwarehouseno() {
        return Fromwarehouseno;
    }

    public void setFromwarehouseno(String fromwarehouseno) {
        Fromwarehouseno = fromwarehouseno;
    }

    public int getIsAmount() {
        return IsAmount;
    }

    public void setIsAmount(int isAmount) {
        IsAmount = isAmount;
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

    public String getSpecialstock() {
        return Specialstock;
    }

    public void setSpecialstock(String specialstock) {
        Specialstock = specialstock;
    }

    public String getWCustomerno() {
        return WCustomerno;
    }

    public void setWCustomerno(String WCustomerno) {
        this.WCustomerno = WCustomerno;
    }

    public int getAreaid() {
        return Areaid;
    }

    public void setAreaid(int areaid) {
        Areaid = areaid;
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

    public String getArrvoucherno() {
        return Arrvoucherno;
    }

    public void setArrvoucherno(String arrvoucherno) {
        Arrvoucherno = arrvoucherno;
    }

    public float getPackageNum() {
        return PackageNum;
    }

    public void setPackageNum(float packageNum) {
        PackageNum = packageNum;
    }

    public int getIsReview() {
        return IsReview;
    }

    public void setIsReview(int isReview) {
        IsReview = isReview;
    }

    public float getReviewQty() {
        return ReviewQty;
    }

    public void setReviewQty(float reviewQty) {
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

    public String getFromCompanyCode() {
        return FromCompanyCode;
    }

    public void setFromCompanyCode(String fromCompanyCode) {
        FromCompanyCode = fromCompanyCode;
    }

    public int getScantype() {
        return Scantype;
    }

    public void setScantype(int scantype) {
        Scantype = scantype;
    }

    public String getWBarcode() {
        return WBarcode;
    }

    public void setWBarcode(String WBarcode) {
        this.WBarcode = WBarcode;
    }

    public String getAreano() {
        return Areano;
    }

    public void setAreano(String areano) {
        Areano = areano;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Headeridsub);
        dest.writeString(Rowno);
        dest.writeString(Rownodel);
        dest.writeString(Materialno);
        dest.writeString(Materialdesc);
        dest.writeFloat(Linestatussub);
        dest.writeString(Batchno);
        dest.writeString(Barcode);
        dest.writeFloat(Qty);
        dest.writeString(Erpvoucherno);
        dest.writeString(Towarehouseno);
        dest.writeString(Unit);
        dest.writeInt(Vouchertype);
        dest.writeString(GUID);
        dest.writeString(Cusmaterialno);
        dest.writeString(Serialno);
        dest.writeString(Fromwarehouseno);
        dest.writeInt(IsAmount);
        dest.writeString(Customerno);
        dest.writeString(Customername);
        dest.writeString(Specialstock);
        dest.writeString(WCustomerno);
        dest.writeInt(Areaid);
        dest.writeString(platform);
        dest.writeString(watercode);
        dest.writeString(Arrvoucherno);
        dest.writeFloat(PackageNum);
        dest.writeInt(IsReview);
        dest.writeFloat(ReviewQty);
        dest.writeInt(StockId);
        dest.writeString(WORowno);
        dest.writeString(WORownodel);
        dest.writeString(FromCompanyCode);
        dest.writeInt(Scantype);
        dest.writeString(WBarcode);
        dest.writeString(Areano);

    }
}
