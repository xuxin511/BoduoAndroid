package com.liansu.boduowms.bean;

import com.liansu.boduowms.bean.barcode.OutBarcodeInfo;
import com.liansu.boduowms.bean.base.BaseMultiResultInfo;

/**
 * @desc:
 * @param:
 * @return:
 * @author: Nietzsche
 * @time 2020/6/30 16:27
 */

public class QRCodeFunc {
    public static final int BARCODE_TYPE_OUTER_BOX   = 1; //外箱
    public static final int BARCODE_TYPE_PALLET_NO   = 2;//托盘
    public static final int BARCODE_TYPE_SPARE_PARTS = 0; //散件

    public static BaseMultiResultInfo<Boolean, OutBarcodeInfo> getQrCode(String barcode) {
        BaseMultiResultInfo<Boolean, OutBarcodeInfo> resultInfo = new BaseMultiResultInfo();
        OutBarcodeInfo resultBarcode = new OutBarcodeInfo();
        try {
            String materialNo = null;
            String batchNo = null;
            float qty = 0;
            int barcodeType = -1;
            String serialNo = "";
            final String originalCode = barcode;
            //外箱码
            if (barcode.contains("%")) {
                String[] listCode = barcode.replace("", "").split("%");
                if (listCode.length == 5) {

                    // 物料编号%批次%数量%标签类型
                    materialNo = listCode[0];
                    batchNo = listCode[1];
                    qty = Float.parseFloat(listCode[2]);
                    serialNo = listCode[3];
                    barcodeType = Integer.parseInt(listCode[4]);
                } else if (listCode.length == 4) {

                    // 物料编号%批次%数量%标签类型
                    materialNo = listCode[0];
                    batchNo = listCode[1];
                    qty = Float.parseFloat(listCode[2]);
                    barcodeType = Integer.parseInt(listCode[3]);

                } else if (listCode.length == 2) {
                    //原料的原标签   物料编码和数量
                    materialNo = listCode[0];
                    qty = Integer.parseInt(listCode[1]);
                }
                resultBarcode.setSerialno(serialNo);
                resultBarcode.setBarcode(originalCode);
                resultBarcode.setBarcodetype(barcodeType);
                resultBarcode.setMaterialno(materialNo);
                resultBarcode.setBatchno(batchNo);
                resultBarcode.setQty(qty);
            } else { //散件  69 码或者物料编码
                resultBarcode.setBarcode(originalCode);
                resultBarcode.setMaterialno(originalCode);
            }
            if (resultBarcode != null) {
                resultInfo.setInfo(resultBarcode);
                resultInfo.setHeaderStatus(true);
            }
        } catch (Exception e) {
            resultInfo.setHeaderStatus(false);
            resultInfo.setMessage("解析条码失败:" + e.getMessage());
        }

        return resultInfo;
    }


    /**
     * @desc: 获取条码类型
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/4 12:10
     */
    public static String getBarcodeType(String barcode) {
        String barcodeType = "";
        if (barcode != null) {
            if (barcode.startsWith("A")) {
                barcodeType = "A";
            } else if (barcode.startsWith("B")) {
                barcodeType = "B";
            } else if (barcode.startsWith("C")) {
                barcodeType = "C";
            } else if (barcode.startsWith("D")) {
                barcodeType = "D";
            }
        }
        return barcodeType;
    }


}
