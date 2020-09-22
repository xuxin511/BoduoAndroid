package com.liansu.boduowms.modules.outstock.Model;

public class OutStock_Tag {


    public static String TAG_Saleoutstock_SelectNO = "Sales_outstock_ScannNo";    // 销售出库单号扫描
    public static final int RESULT_Saleoutstock_SalesNO = 123;

    public static String TAG_Saleoutstock_SubmitPallet = "Sales_outstock_ScannPalletNo";    // 销售扫描托盘提交
    public static final int RESULT_Saleoutstock_ScannPalletNo = 124;


    public static String TAG_Saleoutstock_SubmitParts = "Sales_outstock_ScannParts";    // 散件扫描托盘提交
    public static final int RESULT_Saleoutstock_ScannParts = 125;


    public static String TAG_Saleoutstock_SubmitBox = "Sales_outstock_ScannBoxNo";    // 箱号
    public static final int RESULT_Saleoutstock_ScannBoxNo = 126;


    public static String TAG_Saleoutstock_barcodeisExist = "Sales_outstock_barcodeisExist";    // 判断托盘是否存在
    public static final int RESULT_Saleoutstock_barcodeisExist = 127;

    public static String TAG_Saleoutstock_PlatForm = "Sales_outstock_PlatForm ";    // 提交月台是否存在
    public static final int RESULT_Saleoutstock_PlatForm = 128;

    public static String TAG_Saleoutstock_SubmitParts_Submit = "Sales_outstock_ScannParts_Submit";    // 散件扫描托盘提交
    public static final int RESULT_Saleoutstock_ScannParts_Submit = 129;


    //#region 出库下架模式/复核barcode扫描类型
    public static String OutStock_Submit_type_parts = "0";//散件
    public static String OutStock_Submit_type_box = "1";//箱
    public static String OutStock_Submit_type_pallet = "2"; //托盘
    public static String OutStock_Submit_type_none = "3"; //无效条码
    public static String OutStock_Submit_type_ppallet = "5"; //拼托
    public static String OutStock_Submit_type_pbox = "4"; //拼箱
    //#endregion


    public static String TAG_Saleoutstock_Box_SelectNO = "Sales_outstock_Box_SelectNO";    // 拼箱查询单号
    public static final int RESULT_Saleoutstock_Box_SelectNO = 130;

    public static String TAG_Saleoutstock_Box_Submit = "Sales_outstock_Box_Submit";    // 拼箱查询单号
    public static final int RESULT_Saleoutstock_Box_Submit = 131;//提交整个扫描列表
    public static final int RESULT_Saleoutstock_Box_Submit_Box = 132;//提交箱
    public static final int RESULT_Saleoutstock_Box_Check_Box = 133;//检查是否多批次
    public static final int RESULT_Saleoutstock_Box_Check_waterCode = 134;//提交散件

    public static String TAG_Saleoutstock_GetBoxList = "Sales_outstock_GetBoxList";    // 拼箱查询单号
    public static final int RESULT_Saleoutstock_GetBoxList = 135;

    public static String TAG_Saleoutstock_DelBox = "Sales_outstock_DelBox";    // 删除箱号
    public static final int RESULT_Saleoutstock_DelBox = 136;

    public static String TAG_Saleoutstock_ReviewOrder = "Sales_outstock_ReviewOrder";    // 复核判断单号是否存在库存
    public static final int RESULT_Saleoutstock_ReviewOrder = 137;

    public static String TAG_Saleoutstock_SubmitBarcode = "Sales_outstock_ReviewOrder";    // 提交代码
    public static final int RESULT_Saleoutstock_SubmitBarcode = 138;


    public static String TAG_Saleoutstock_PostReview = "Sales_outstock_PostReview";    // 过账
    public static final int RESULT_Saleoutstock_PostReview = 139;

    public static String TAG_Saleoutstock_ConfigSelectOrder = "Sales_outstock_ConfigSelectOrder";    // 获取托运单信息
    public static final int RESULT_Saleoutstock_ConfigSelectOrder = 140;

    public static String TAG_Saleoutstock_ConfigSaveOrder = "Sales_outstock_ConfigSaveOrder";    // 保存托运单号
    public static final int RESULT_Saleoutstock_ConfigSaveOrder = 141;
    public static String TAG_Saleoutstock_ConfigSaveOrderDetail = "Sales_outstock_ConfigSaveOrderDetail";    // 获取托运单明细
    public static final int RESULT_Saleoutstock_ConfigSaveOrderDetail = 142;

    public static String TAG_Saleoutstock_GETBOXlIST = "Sales_outstock_GetBoxList";    // 获取托运单明细
    public static final int RESULT_Saleoutstock_GETBOXlISTl = 143;

    public static String TAG_outstock_Callback = "Sales_outstock_Callback";    // 二阶段挑拨订单访问
    public static final int RESUL_Toutstock_Callback = 144;

    public static String TAG_outstock_Callback_Submit = "Sales_outstock_Callback_Submit";    // 二阶段挑拨订单访问
    public static final int RESUL_Toutstock_Callback_Submit = 145;


    public static String TAG_outstock_Ordercolose_Submit = "Sales_outstock_Ordercolose_Submit";    // 提交结案数据
    public static final int RESUL_Toutstock_Ordercolose_Submit = 146;

    public static String TAG_Saleoutstock_PrintBox = "Sales_Saleoutstock_PrintBox";    // 打印拼箱标签
    public static final int RESUL_Saleoutstock_PrintBox = 147;


    public static String TAG_Saleoutstock_OneReview = "Sales_Saleoutstock_OneReview";    // 打印拼箱标签
    public static final int RESUL_Saleoutstock_OneReview = 148;



    public static String TAG_Saleoutstock_WaybillPrint = "Sales_Saleoutstock_WaybillPrint";    // 打印托运单
    public static final int RESUL_Saleoutstock_WaybillPrint = 149;
}
