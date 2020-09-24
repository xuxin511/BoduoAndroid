package com.liansu.boduowms.bean.base;

/**
 * Created by GHOST on 2017/6/9.
 */

public class UrlInfo {

    private static UrlInfo instance;

    public static UrlInfo getUrl() {
        return new UrlInfo();
    }

    public static String  IPAdress                     = "172.19.106.230";//"wmstest.beukay.com";
    public static int     Port                         = 5001;//9000;
    public static int     mUpdatePort                  = 8022;//9000;  //版本更新端口号 如果能在一个端口下更新就不用这个字段 用Port
    public static String  LastContent                  = "api/";
    public static String  PrintIP                      = "10.2.32.192";
    public static String  ElecIP                       = "10.2.32.244";
    public static Boolean isWMS                        = true;
    public static boolean isSupplier                   = false;
    public static String  mBluetoothPrinterMacAddress  = "AC3FA45B9232"; //蓝牙打印地址
    public static String  mDesktopPrintAddress         = "";  //桌面打印机名称
    public static String  mLaserPrinterAddress         = "";  //激光打印机名称
    public static int     mInStockPrintType            = -1; //入库打印机选择
    public static int     mOutStockPrintType           = -1; //出库打印机选择
    public static int     mOutStockPackingBoxPrintType = -1; //出库拼箱打印机选择
    public static String  mInStockPrintName            = ""; //入库打印名称
    public static String  mOutStockPrintName           = ""; //出库打印名称
    public static String  mOutStockPackingBoxPrintName = ""; //出库拼箱打印名称


    String GetWCFAdress() {
        return "http://" + IPAdress + ":" + Port + "/" + LastContent;
    }

    //公共方法
    public String GetT_ScanBarcodeADFAsync         = GetWCFAdress() + "OutBarcode/GetT_ScanBarcodeADFAsync"; //获取扫描信息 单个条码
    public String GetT_AreaModel                   = GetWCFAdress() + "Area/GetT_AreaModel"; //获取库位信息
    public String SelectMaterial                   = GetWCFAdress() + "Material/SelectMaterial"; //获取物料信息
    public String GetT_StockList                   = GetWCFAdress() + "Stock/GetT_StockList"; //库存查询
    //模块方法
    //入库模块
    public String UserLogin                        = GetWCFAdress() + "user/userlogin"; //用户登录
    public String GetT_UserMenuListByRuleIdAsync   = GetWCFAdress() + "Menu/GetT_UserMenuListByRuleIdAsync"; //获取菜单明细
    public String GetT_PurchaseListADFasync        = GetWCFAdress() + "Purchase/GetT_PurchaseListADFasync"; //获取采购订单列表
    public String GetT_PurchaseOrderListADFAsync   = GetWCFAdress() + "Purchase/GetT_PurchaseOrderListADFAsync"; //获取采购订单明细
    public String SaveT_PurchaseDetailADFAsync     = GetWCFAdress() + "Purchase/SaveT_PurchaseDetailADFAsync"; //提交采购扫描条码
    public String PostT_PurchaseDetailADFAsync     = GetWCFAdress() + "Purchase/PostT_PurchaseDetailADFAsync"; //采购扫描条码过账
    public String GetT_QualityHeadListsync         = GetWCFAdress() + "Quality/GetT_QualityHeadListsync"; //质检列表
    public String GetT_QualityDetailListsync       = GetWCFAdress() + "Quality/GetT_QualityDetailListsync"; //质检合格表体
    public String PostT_QualityADFAsync            = GetWCFAdress() + "Quality/PostT_QualityADFAsync"; //质检合格提交
    public String PostT_QualityTransferTwoADFAsync = GetWCFAdress() + "Quality/PostT_QualityTransferTwoADFAsync"; //质检合格生成二阶段调拨提交

    public String GetT_CheckQualityHeadListsync     = GetWCFAdress() + "Quality/GetT_CheckQualityHeadListsync"; //抽检获取列表
    public String GetT_CheckQualityDetailListsync   = GetWCFAdress() + "Quality/GetT_CheckQualityDetailListsync"; //抽检表体获取
    public String GetT_PurchaseOrderListADF         = GetWCFAdress() + "Stock/GetT_PurchaseOrderListADF"; //取样扫描接口  2020-8-10 废弃
    public String GetT_ScanStockADFAsync            = GetWCFAdress() + "Stock/GetT_ScanStockADFAsync"; //取样扫描接口 新 2020-8-10
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
    public String GetT_ParameterList                = GetWCFAdress() + "Parameter/GetT_ParameterList"; //获取入库打印单据
    //调拨入库
    public String GetT_TransferInDetailListADFAsync = GetWCFAdress() + "Transferin/GetT_TransferInDetailListADFAsync"; //获取调拨入库单据
    public String SaveT_TransferInDetailADFAsync    = GetWCFAdress() + "Transferin/SaveT_TransferInDetailADFAsync"; //调拨入库实时提交
    public String PostT_TransferInDetailADFAsync    = GetWCFAdress() + "Transferin/PostT_TransferInDetailADFAsync"; //调拨入库过账
    //无源杂入
    public String Create_OtherInDetailADFasync      = GetWCFAdress() + "OtherIn/Create_OtherInDetailADFasync"; //无源杂入生单

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

    public String SalesOutstock_HeaderList = GetWCFAdress() + "InspecReturn/GetT_InspecReturnListADFAsync"; //采购验退获取列表

    //采购退货
    public String PurchaseReturn_GetT_InspecReturnListADFAsync        = GetWCFAdress() + "PurchaseReturn/GetT_InspecReturnListADFAsync"; //采购退货获取列表
    public String PurchaseReturn_GetT_OutStockDetailListADFAsync      = GetWCFAdress() + "PurchaseReturn/GetT_OutStockDetailListADFAsync"; //采购退货获取表体
    public String PurchaseReturn_SaveT_OutStockDetailADFAsync         = GetWCFAdress() + "PurchaseReturn/SaveT_OutStockDetailADFAsync"; //采购退货实时提交
    public String PurchaseReturn_GetT_CheckOutStockDetailListADFAsync = GetWCFAdress() + "PurchaseReturn/GetT_CheckOutStockDetailListADFAsync"; //采购退货复核获取单据明细
    public String PurchaseReturn_SubmitReviewScanADFAsync             = GetWCFAdress() + "PurchaseReturn/SubmitReviewScanADFAsync"; //采购退货复核扫描提交
    public String PurchaseReturn_PostT_OutStockDetailADFAsync         = GetWCFAdress() + "PurchaseReturn/PostT_OutStockDetailADFAsync"; //采购退货复核过账
    public String PurchaseReturn_PrintInspecReturn                    = GetWCFAdress() + "PurchaseReturn/PrintInspecReturn"; //采购退货打印


    // #regino x销售出库
    //销售出库
    public String SalesOutstock_ScanningNo     = GetWCFAdress() + "OutStock/GetT_OutStockDetailListADFAsync"; //销售出库订单扫描
    public String SalesOutstock_SacnningPallet = GetWCFAdress() + "OutStock/SaveT_OutStockDetailADFAsync"; //销售出库托盘提交
    public String SalesOutstock_PlatForm       = GetWCFAdress() + "OutStock/Package_PlatFormSubmitADFAsync"; //提交月台
    public String SalesOutstock_OrderColose    = GetWCFAdress() + "OutStock/Lock_Order"; //提交结案

//    public String SalesOutstock_ScanningNo        = GetWCFAdress() + "OutStock/GetT_OutStockDetailListADFAsync"; //销售出库订单扫描
//    public String SalesOutstock_SacnningPallet    = GetWCFAdress() + "OutStock/SaveT_OutStockDetailADFAsync"; //销售出库托盘提交
//    public String SalesOutstock_PlatForm          = GetWCFAdress() + "OutStock/Package_PlatFormSubmitADFAsync"; //提交月台
//    //拼箱

    public String SalesOutstock_Box_ScanningNo = GetWCFAdress() + "OutStock/GetT_CheckOutStockStatusADFAsync"; //拼箱订单判断是否下架
    public String SalesOutstock_Box_Submit     = GetWCFAdress() + "OutStock/Package_CartonScanADFAsync";//拼箱提交
    public String SalesOutstock_Box_Batchno    = GetWCFAdress() + "OutStock/CheckPackageScan";//验证69吗/箱号/物料是否多批次
    public String SalesOutstock_GetBoxList     = GetWCFAdress() + "OutStock/GetPackage_CartonScanADFAsync";//获取拼箱列表
    public String SalesOutstock_DelBox         = GetWCFAdress() + "OutStock/DelPackage_CartonScanADFAsync";//删除单个拼箱
    public String SalesOutstock_BoxList        = GetWCFAdress() + "OutStock/GetPackage_CartonDetail";//获取订单拼箱列表
    public String SalesOutstock_PrintBox       = GetWCFAdress() + "OutStock/PrintPackage_CartonScanADFAsync";//打印拼箱

    //复核
    public String SalesOutstock_Review_ScanningNo = GetWCFAdress() + "OutStock/GetT_CheckOutStockDetailListADFAsync";//获取复核单据
    public String SalesOutstock_JudgeStock        = GetWCFAdress() + "Stock/GetT_ScanStockADFAsync";//判断托盘库存
    public String SalesOutstock__SubmitBarcode    = GetWCFAdress() + "OutStock/SubmitReviewScanADFAsync"; //复核条码提交
    public String SalesOutstock__Review_Submit    = GetWCFAdress() + "OutStock/PostT_OutStockDetailADFAsync"; //复核过账

    public String SalesOutstock_Onereview                       = GetWCFAdress() + "OutStock/OnkeyReview"; //一键复核
    //托运单保存
    public String SalesOutstock__Review_configSaveOrder         = GetWCFAdress() + "WayBill/SubmitWayBillHeader"; //保存托运单
    public String SalesOutstock__Review_configSelectOrder       = GetWCFAdress() + "WayBill/GetWayBillNo"; //获取托运单表头
    public String SalesOutstock__Review_configSelectOrderDetial = GetWCFAdress() + "WayBill/Get_WayBillDetail"; //获取托运单明细
    public String SalesOutstock__Review_Printwaybill            = GetWCFAdress() + "WayBill/PrintWayBillOrder"; //打印托运单
    //endregion
    //二阶段回调订单访问
    public String SalesOutstock__Toutstock_Callback             = GetWCFAdress() + "TransferOut/Get_TransferoutInfoBack";
    public String SalesOutstock__Toutstock_Callback_Submit      = GetWCFAdress() + "TransferOut/Post_TransferoutInfoBackADFAsync";

    //入库，出库 条码回退
    public String GetT_DetailSubAsync    = "";//  获取暂存数据
    public String DeleteT_DetailSubAsync = ""; // 删除暂存数据

    public void InitUrl(int type) {
        switch (type) {
            case 22:
                GetT_DetailSubAsync = GetWCFAdress() + "Purchase/GetT_PurchaseDetailSubAsync"; //获取采购暂存数据
                DeleteT_DetailSubAsync = GetWCFAdress() + "Purchase/DeleteT_PurchaseDetailSubAsync"; //删除采购暂存数据
                break;
            case 24:
                GetT_DetailSubAsync = GetWCFAdress() + "Transferin/GetT_TransferInDetailSubAsync"; //获取二阶段调拨入库暂存数据
                DeleteT_DetailSubAsync = GetWCFAdress() + "Transferin/DeleteT_TransferInDetailSubAsync"; //删除二阶段调拨入库暂存数据
                break;
            case 26:
                GetT_DetailSubAsync = GetWCFAdress() + "SaleReturn/GetT_SaleReturnDetailSubAsync"; //获取有原销退明细
                DeleteT_DetailSubAsync = GetWCFAdress() + "SaleReturn/DeleteT_SaleReturnDetailSubAsync"; //删除暂存
                break;
            case 58:
                GetT_DetailSubAsync = GetWCFAdress() + "Transferin/GetT_TransferInDetailSubAsync"; //获取一阶段调拨入库暂存数据
                DeleteT_DetailSubAsync = GetWCFAdress() + "Transferin/DeleteT_TransferInDetailSubAsync"; //删除一阶段调拨入库暂存数据
                break;
            case 45:
                GetT_DetailSubAsync = GetWCFAdress() + "WorkOrder/GetT_WorkOrderDetailSubAsync"; //获取成品暂存数据
                DeleteT_DetailSubAsync = GetWCFAdress() + "WorkOrder/DeleteT_WorkOrderDetailSubAsync"; //删除成品暂存数据
                break;
            case 52:
                GetT_DetailSubAsync = GetWCFAdress() + "WorkOrderReturn/GetT_WorkOrderReturnDetailSubAsync"; //获取工单退料暂存数据
                DeleteT_DetailSubAsync = GetWCFAdress() + "WorkOrderReturn/DeleteT_WorkOrderReturnDetailSubAsync"; //删除工单退料暂存数据
                break;
            case 44:
                GetT_DetailSubAsync = GetWCFAdress() + "OtherIn/GetT_OtherInDetailSubAsync"; //获取有源杂入暂存数据
                DeleteT_DetailSubAsync = GetWCFAdress() + "OtherIn/DeleteT_OtherInDetailSubAsync"; //删除有源杂入暂存数据
                break;
            case 29://发货通知单
                GetT_DetailSubAsync = GetWCFAdress() + "OutStock/Return_PalletNoByErpno"; //获取采购暂存数据
                DeleteT_DetailSubAsync = GetWCFAdress() + "OutStock/Return_DelPalletNo"; //删除采购暂存数据
                SalesOutstock_ScanningNo = GetWCFAdress() + "OutStock/GetT_OutStockDetailListADFAsync"; //销售出库订单扫描
                SalesOutstock_SacnningPallet = GetWCFAdress() + "OutStock/SaveT_OutStockDetailADFAsync"; //销售出库托盘提交
                SalesOutstock_PlatForm = GetWCFAdress() + "OutStock/Package_PlatFormSubmitADFAsync"; //提交月台
                break;
            case 36://派车单
                // GetT_OutStockDetailListADFAsync
                GetT_DetailSubAsync = GetWCFAdress() + "OutStock/Return_PalletNoByErpno"; //获取采购暂存数据
                DeleteT_DetailSubAsync = GetWCFAdress() + "OutStock/Return_DelPalletNo"; //删除采购暂存数据

                SalesOutstock_Review_ScanningNo = GetWCFAdress() + "OutStock/GetT_CheckDispatchDetailListADFAsync";//获取复核单据
                SalesOutstock_ScanningNo = GetWCFAdress() + "OutStock/GetT_DispatchkDetailListADFAsync"; //下架获取订单数据
                SalesOutstock__Review_Submit = GetWCFAdress() + "OutStock/PostT_OutStockDetailADFAsync"; //复核过账
                break;
            case 46://领料/原材料发货
                SalesOutstock_HeaderList = GetWCFAdress() + "RawMaterialOut/GetT_OutStockHeaderADFAsync  "; //领料单获取订单列表
                GetT_DetailSubAsync = GetWCFAdress() + "RawMaterialOut/Return_PalletNoByErpno"; //获取采购暂存数据
                DeleteT_DetailSubAsync = GetWCFAdress() + "RawMaterialOut/Return_DelPalletNo"; //删除采购暂存数据
                SalesOutstock_ScanningNo = GetWCFAdress() + "RawMaterialOut/GetT_OutStockDetailListADFAsync"; //订单扫描
                SalesOutstock_SacnningPallet = GetWCFAdress() + "RawMaterialOut/SaveT_OutStockDetailADFAsync"; //托盘提交
                SalesOutstock__Review_Submit = GetWCFAdress() + "RawMaterialOut/PostT_OutStockDetailADFAsync"; //复核过账
                break;
            case 57://委外工单发料
                GetT_DetailSubAsync = GetWCFAdress() + "OutWork/Return_PalletNoByErpno"; //获取采购暂存数据
                DeleteT_DetailSubAsync = GetWCFAdress() + "OutWork/Return_DelPalletNo"; //删除采购暂存数据
                SalesOutstock_ScanningNo = GetWCFAdress() + "OutWork/GetT_OutStockDetailListADFAsync"; //订单扫描
                SalesOutstock_SacnningPallet = GetWCFAdress() + "OutWork/SaveT_OutStockDetailADFAsync"; //托盘提交
                SalesOutstock__Review_Submit = GetWCFAdress() + "OutWork/PostT_OutStockDetailADFAsync"; //复核过账
                break;
            case 25://一阶段调拨
                GetT_DetailSubAsync = GetWCFAdress() + "Transferout/Return_PalletNoByErpno"; //获取采购暂存数据
                DeleteT_DetailSubAsync = GetWCFAdress() + "Transferout/Return_DelPalletNo"; //删除采购暂存数据
                SalesOutstock_ScanningNo = GetWCFAdress() + "Transferout/GetT_OutStockDetailListADFAsync"; //订单扫描
                SalesOutstock_SacnningPallet = GetWCFAdress() + "Transferout/SaveT_OutStockDetailADFAsync"; //托盘提交
                SalesOutstock__Review_Submit = GetWCFAdress() + "Transferout/PostT_OutStockDetailADFAsync"; //复核过账
                break;
            case 30://二阶段调拨
                GetT_DetailSubAsync = GetWCFAdress() + "TransferTwoout/Return_PalletNoByErpno"; //获取采购暂存数据
                DeleteT_DetailSubAsync = GetWCFAdress() + "TransferTwoout/Return_DelPalletNo"; //删除采购暂存数据
                SalesOutstock_ScanningNo = GetWCFAdress() + "TransferTwoout/GetT_OutStockDetailListADFAsync"; //订单扫描
                SalesOutstock_SacnningPallet = GetWCFAdress() + "TransferTwoout/SaveT_OutStockDetailADFAsync"; //托盘提交
                SalesOutstock__Review_Submit = GetWCFAdress() + "TransferTwoout/PostT_OutStockDetailADFAsync"; //复核过账
                break;
            case 55://杂出
                GetT_DetailSubAsync = GetWCFAdress() + "OtherOut/Return_PalletNoByErpno"; //获取采购暂存数据
                DeleteT_DetailSubAsync = GetWCFAdress() + "OtherOut/Return_DelPalletNo"; //删除采购暂存数据
                SalesOutstock_ScanningNo = GetWCFAdress() + "OtherOut/GetT_OutStockDetailListADFAsync"; //订单扫描
                SalesOutstock_SacnningPallet = GetWCFAdress() + "OtherOut/SaveT_OutStockDetailADFAsync"; //托盘提交
                SalesOutstock__Review_Submit = GetWCFAdress() + "OtherOut/PostT_OutStockDetailADFAsync"; //复核过账
                break;
            case 27://仓退
                GetT_DetailSubAsync = GetWCFAdress() + "PurchaseReturn/Return_PalletNoByErpno"; //获取采购暂存数据
                DeleteT_DetailSubAsync = GetWCFAdress() + "PurchaseReturn/Return_DelPalletNo"; //删除采购暂存数据
                SalesOutstock_Review_ScanningNo = GetWCFAdress() + "PurchaseReturn/GetT_CheckOutStockDetailListADFAsync";//获取复核单据
                SalesOutstock__SubmitBarcode = GetWCFAdress() + "PurchaseReturn/SubmitReviewScanADFAsync"; //复核条码提交
                SalesOutstock_ScanningNo = GetWCFAdress() + "PurchaseReturn/GetT_OutStockDetailListADFAsync"; //订单扫描
                SalesOutstock_SacnningPallet = GetWCFAdress() + "PurchaseReturn/SaveT_OutStockDetailADFAsync"; //托盘提交
                SalesOutstock__Review_Submit = GetWCFAdress() + "PurchaseReturn/PostT_OutStockDetailADFAsync"; //复核过账
                break;
            case 28:
            case 61:
            case 62:
                //采购验退,成品 销售
                GetT_DetailSubAsync = GetWCFAdress() + "InspecReturn/Return_PalletNoByErpno"; //获取采购暂存数据
                DeleteT_DetailSubAsync = GetWCFAdress() + "InspecReturn/Return_DelPalletNo"; //删除采购暂存数据
                SalesOutstock_Review_ScanningNo = GetWCFAdress() + "InspecReturn/GetT_CheckOutStockDetailListADFAsync";//获取复核单据
                SalesOutstock__SubmitBarcode = GetWCFAdress() + "InspecReturn/SubmitReviewScanADFAsync"; //复核条码提交
                SalesOutstock_ScanningNo = GetWCFAdress() + "InspecReturn/GetT_OutStockDetailListADFAsync"; //订单扫描
                SalesOutstock_SacnningPallet = GetWCFAdress() + "InspecReturn/SaveT_OutStockDetailADFAsync"; //托盘提交
                SalesOutstock__Review_Submit = GetWCFAdress() + "InspecReturn/PostT_OutStockDetailADFAsync"; //复核过账
                SalesOutstock_HeaderList = GetWCFAdress() + "InspecReturn/GetT_InspecReturnListADFAsync"; //采购验退获取列表
                break;
            case 56://行政领用
                GetT_DetailSubAsync = GetWCFAdress() + "AdCollection/Return_PalletNoByErpno"; //获取采购暂存数据
                DeleteT_DetailSubAsync = GetWCFAdress() + "AdCollection/Return_DelPalletNo"; //删除采购暂存数据
                SalesOutstock_ScanningNo = GetWCFAdress() + "AdCollection/GetT_OutStockDetailListADFAsync"; //订单扫描
                SalesOutstock_SacnningPallet = GetWCFAdress() + "AdCollection/SaveT_OutStockDetailADFAsync"; //托盘提交
                SalesOutstock__Review_Submit = GetWCFAdress() + "AdCollection/PostT_OutStockDetailADFAsync"; //复核过账
                break;

        }

    }

    //盘点

    public String Inventory_Head_GetCheckList       = GetWCFAdress() + "Check/GetAndroidT_Check"; //获取盘点明细
    public String Inventory_Config_AreanobyCheckno  = GetWCFAdress() + "Check/GetT_AreanobyCheckno"; //获取库位信息
    public String Inventory_Config_GetScanInfo      = GetWCFAdress() + "Check/GetScanInfo"; //获取条码信息
    public String Project_GetParameter              = GetWCFAdress() + "Parameter/GetT_ParameterList"; //获取配置表信息
    public String Inventory_Detail_GetDetail        = GetWCFAdress() + "Check/Get_CheckDetail"; //获取盘点明细
    public String Inventory_Detail_Save_CheckDetail = GetWCFAdress() + "Check/Save_CheckDetail"; //保存盘点单
    public String Inventory_GetList  = GetWCFAdress() + "Check/Get_CheckStok"; //获取库位对应的所有托盘


//    public String SalesOutstock_Review_ScanningNo = GetWCFAdress() + "OutStock/GetT_CheckOutStockDetailListADFAsync";//获取复核单据
//    public String SalesOutstock_JudgeStock        = GetWCFAdress() + "Stock/GetT_ScanStockADFAsync";//判断托盘库存
//    public String SalesOutstock__SubmitBarcode    = GetWCFAdress() + "OutStock/SubmitReviewScanADFAsync"; //复核条码提交
//    public String SalesOutstock__Review_Submit    = GetWCFAdress() + "OutStock/PostT_OutStockDetailADFAsync"; //复核过账

    //批量打印
    public String CreateT_OutBarcodeADFAsync          = GetWCFAdress() + "OutBarcode/CreateT_OutBarcodeADFAsync"; //批量生成条码
    //库存调整
    public String AdjustStockGetT_ScanStockADFAsync   = GetWCFAdress() + "Stock/GetT_ScanStockADFAsync"; // 出库扫描获取托盘
    public String UpdateT_StockAdjust                 = GetWCFAdress() + "Stock/UpdateT_StockAdjust"; //库存调整提交
    //工单退料
    public String SaveT_WorkOrderReturnDetailADFAsync = GetWCFAdress() + "WorkOrderReturn/SaveT_WorkOrderReturnDetailADFAsync"; //工单退料实时提交
    public String PostT_WorkOrderReturnDetailADFAsync = GetWCFAdress() + "WorkOrderReturn/PostT_WorkOrderReturnDetailADFAsync"; //工单退料过账

    //移库
    public String UpdateT_StockListArea                = GetWCFAdress() + "Stock/UpdateT_StockListArea"; //移库提交
    //有源销售退货
    public String GetT_SaleReturnDetailListWMSADFAsync = GetWCFAdress() + "SaleReturn/GetT_SaleReturnDetailListWMSADFAsync";  //获取有原销退明细
    public String SaveT_SaleReturnDetailWMSADFAsync    = GetWCFAdress() + "SaleReturn/SaveT_SaleReturnDetailWMSADFAsync";  //有原数据提交
    public String PostT_SaleReturnDetailADFAsync       = GetWCFAdress() + "SaleReturn/PostT_SaleReturnDetailADFAsync";  //有原销退单过账


}
