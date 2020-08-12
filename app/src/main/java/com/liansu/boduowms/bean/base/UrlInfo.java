package com.liansu.boduowms.bean.base;

/**
 * Created by GHOST on 2017/6/9.
 */

public class UrlInfo {

    private static UrlInfo instance;

    public static UrlInfo getUrl() {
        return new UrlInfo();
    }

    public static String  IPAdress                    = "172.19.106.230";//"wmstest.beukay.com";
    public static int     Port                        = 5001;//9000;
    public static String  LastContent                 = "api/";
    public static String  PrintIP                     = "10.2.32.192";
    public static String  ElecIP                      = "10.2.32.244";
    public static Boolean isWMS                       = true;
    public static boolean isSupplier                  = false;
    public static String  mBluetoothPrinterMacAddress = "AC3FA45B9232"; //蓝牙打印地址
    public static String  mDesktopPrintAddress        = "";  //桌面打印机名称
    public static String  mLaserPrinterAddress        = "";  //激光打印机名称
    public static int     mInStockPrintType           = -1; //入库打印机选择
    public static int     mOutStockPrintType          = -1; //出库打印机选择
    public static String  mInStockPrintName           = ""; //入库打印名称
    public static String  mOutStockPrintName          = ""; //出库打印名称


    String GetWCFAdress() {
        return "http://" + IPAdress + ":" + Port + "/" + LastContent;
    }

    //公共方法
    public String GetT_ScanBarcodeADFAsync          = GetWCFAdress() + "OutBarcode/GetT_ScanBarcodeADFAsync"; //获取扫描信息
    public String GetT_AreaModel                    = GetWCFAdress() + "Area/GetT_AreaModel"; //获取库位信息
    public String SelectMaterial                    = GetWCFAdress() + "Material/SelectMaterial"; //获取物料信息
    //模块方法
    //入库模块
    public String UserLogin                         = GetWCFAdress() + "user/userlogin"; //用户登录
    public String GetT_UserMenuListByRuleIdAsync    = GetWCFAdress() + "Menu/GetT_UserMenuListByRuleIdAsync"; //获取菜单明细
    public String GetT_PurchaseListADFasync         = GetWCFAdress() + "Purchase/GetT_PurchaseListADFasync"; //获取采购订单列表
    public String GetT_PurchaseOrderListADFAsync    = GetWCFAdress() + "Purchase/GetT_PurchaseOrderListADFAsync"; //获取采购订单明细
    public String SaveT_PurchaseDetailADFAsync      = GetWCFAdress() + "Purchase/SaveT_PurchaseDetailADFAsync"; //提交采购扫描条码
    public String PostT_PurchaseDetailADFAsync      = GetWCFAdress() + "Purchase/PostT_PurchaseDetailADFAsync"; //采购扫描条码过账
    public String GetT_QualityHeadListsync          = GetWCFAdress() + "Quality/GetT_QualityHeadListsync"; //质检列表
    public String GetT_QualityDetailListsync        = GetWCFAdress() + "Quality/GetT_QualityDetailListsync"; //质检合格表体
    public String PostT_QualityADFAsync             = GetWCFAdress() + "Quality/PostT_QualityADFAsync"; //质检合格提交
    public String GetT_CheckQualityHeadListsync     = GetWCFAdress() + "Quality/GetT_CheckQualityHeadListsync"; //抽检获取列表
    public String GetT_CheckQualityDetailListsync   = GetWCFAdress() + "Quality/GetT_CheckQualityDetailListsync"; //抽检表体获取
    public String GetT_PurchaseOrderListADF         = GetWCFAdress() + "Stock/GetT_PurchaseOrderListADF"; //取样扫描接口  2020-8-10 废弃
    public String GetT_ScanStockADFAsync         = GetWCFAdress() + "Stock/GetT_ScanStockADFAsync"; //取样扫描接口 新 2020-8-10
    public String CheckT_PalletBarcodesync          = GetWCFAdress() + "Quality/CheckT_PalletBarcodesync"; //检验托盘号是否待检
    public String PostT_CheckQualitysync            = GetWCFAdress() + "Quality/PostT_CheckQualitysync"; //抽检表体提交
    public String GetT_WorkOrderHeadListADFAsync    = GetWCFAdress() + "WorkOrder/GetT_WorkOrderHeadListADFAsync"; //获取工单列表 产品入库
    public String Create_PalletNoADFAsync           = GetWCFAdress() + "WorkOrder/Create_PalletNoADFAsync"; //成品入库打印
    public String Save_InStockWorkOrderListToDB     = GetWCFAdress() + "WorkOrder/Save_InStockWorkOrderListToDB"; //成品入库实时提交
    public String PostT_WorkOrderDetailADFAsync     = GetWCFAdress() + "WorkOrder/PostT_WorkOrderDetailADFAsync"; //成品入库过账
    public String GetT_WorkOrderDetailListADFAsync  = GetWCFAdress() + "WorkOrder/GetT_WorkOrderDetailListADFAsync"; //成品入库获取明细
    public String GetT_OtherHeadListADFAsync        = GetWCFAdress() + "OtherIn/GetT_OtherHeadListADFAsync"; //有源杂入列表获取
    public String GetT_OtherDetailListADFAsync      = GetWCFAdress() + "OtherIn/GetT_OtherDetailListADFAsync"; //有源杂入扫描获取表体
    public String SaveT_OtherDetailADFAsync         = GetWCFAdress() + "OtherIn/SaveT_OtherDetailADFAsync"; //有源杂入扫描实时提交
    public String PostT_OtherDetailADFAsync         = GetWCFAdress() + "OtherIn/PostT_OtherDetailADFAsync"; //有源杂入 过账
    public String GetPrintNameList                  = GetWCFAdress() + "Print/GetPrintNameList"; //设置界面 获取打印机名称
    public String Post_SaleReturnDetailADFasync     = GetWCFAdress() + "SaleReturn/Post_SaleReturnDetailADFasync"; //销售退货提交
    public String GetT_SaleReturnDetailListADFAsync = GetWCFAdress() + "SaleReturn/GetT_SaleReturnDetailListADFAsync"; //销售退货打印获取物料批次
    public String Print_PalletNo                    = GetWCFAdress() + "OutBarcode/Print_PalletNo"; //销售退货打印托盘号


    //出库模块
    public String GetT_OutStockDetailListADFAsync = GetWCFAdress() + "OutStock/GetT_OutStockDetailListADFAsync"; //配货下架获取发货通知单

    //采购验退
    public String InspecReturn_GetT_InspecReturnListADFAsync        = GetWCFAdress() + "InspecReturn/GetT_InspecReturnListADFAsync"; //采购验退获取列表
    public String InspecReturn_GetT_OutStockDetailListADFAsync      = GetWCFAdress() + "InspecReturn/GetT_OutStockDetailListADFAsync"; //采购验退获取表体
    public String InspecReturn_SaveT_OutStockDetailADFAsync         = GetWCFAdress() + "InspecReturn/SaveT_OutStockDetailADFAsync"; //采购验退实时提交
    public String InspecReturn_GetT_CheckOutStockDetailListADFAsync = GetWCFAdress() + "InspecReturn/GetT_CheckOutStockDetailListADFAsync"; //采购验退复核获取单据明细
    public String InspecReturn_SubmitReviewScanADFAsync             = GetWCFAdress() + "InspecReturn/SubmitReviewScanADFAsync"; //采购验退复核扫描提交
    public String InspecReturn_PostT_OutStockDetailADFAsync         = GetWCFAdress() + "InspecReturn/PostT_OutStockDetailADFAsync"; //采购验退复核过账
    public String InspecReturn_PrintInspecReturn                    = GetWCFAdress() + "InspecReturn/PrintInspecReturn"; //采购验退打印


    //采购退货
    public String PurchaseReturn_GetT_InspecReturnListADFAsync        = GetWCFAdress() + "PurchaseReturn/GetT_InspecReturnListADFAsync"; //采购退货获取列表
    public String PurchaseReturn_GetT_OutStockDetailListADFAsync      = GetWCFAdress() + "PurchaseReturn/GetT_OutStockDetailListADFAsync"; //采购退货获取表体
    public String PurchaseReturn_SaveT_OutStockDetailADFAsync         = GetWCFAdress() + "PurchaseReturn/SaveT_OutStockDetailADFAsync"; //采购退货实时提交
    public String PurchaseReturn_GetT_CheckOutStockDetailListADFAsync = GetWCFAdress() + "PurchaseReturn/GetT_CheckOutStockDetailListADFAsync"; //采购退货复核获取单据明细
    public String PurchaseReturn_SubmitReviewScanADFAsync             = GetWCFAdress() + "PurchaseReturn/SubmitReviewScanADFAsync"; //采购退货复核扫描提交
    public String PurchaseReturn_PostT_OutStockDetailADFAsync         = GetWCFAdress() + "PurchaseReturn/PostT_OutStockDetailADFAsync"; //采购退货复核过账
    public String PurchaseReturn_PrintInspecReturn                    = GetWCFAdress() + "PurchaseReturn/PrintInspecReturn"; //采购退货打印


    //销售出库
    public String SalesOutstock_SacnningNo        = GetWCFAdress() + "OutStock/GetT_OutStockDetailListADFAsync"; //销售出库订单扫描
    public String SalesOutstock_SacnningPallet     = GetWCFAdress() + "OutStock/SaveT_OutStockDetailADFAsync"; //销售出库托盘提交



}
