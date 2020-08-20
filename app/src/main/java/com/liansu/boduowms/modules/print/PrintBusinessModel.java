package com.liansu.boduowms.modules.print;

import android.content.Context;
import android.os.Looper;
import android.os.Message;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.base.BaseModel;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.modules.print.linkos.PrintInfo;
import com.liansu.boduowms.modules.print.linkos.PrintType;
import com.liansu.boduowms.modules.print.linkos.SettingsHelper;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonCheckUtil;
import com.liansu.boduowms.utils.hander.MyHandler;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.liansu.boduowms.utils.SharePreferUtil.readBluetoothPrinterMacAddressShare;

/**
 * @ Des:  打印业务类
 * @ Created by yangyiqing on 2020/7/7.
 */
public class PrintBusinessModel extends BaseModel {
    public              String                mMac                         = "";
    private             PrintCallBackListener mPrintListener;
    //打印机类型  1 激光打印机 2 台式打印机 3.蓝牙 -1 未选择
    public static final int                   PRINTER_TYPE_LASER           = 1;
    public static final int                   PRINTER_TYPE_DESKTOP         = 2;
    public static final int                   PRINTER_TYPE_BLUETOOTH       = 3;
    public static final int                   PRINTER_TYPE_NONE            = -1;
    public static final int                   PRINTER_LABEL_TYPE_OUTER_BOX = 1;
    public static final int                   PRINTER_LABEL_TYPE_PALLET_NO = 2;
    public static final int                   PRINTER_LABEL_TYPE_NONE      = -1;
    public static final String                PRINTER_LABEL_NAME_PALLET_NO = "托盘标签";
    public static final String                PRINTER_LABEL_NAME_OUTER_BOX = "外箱标签";

    @Override
    public void onHandleMessage(Message msg) {

    }

    public void setPrintCallBackListener(PrintCallBackListener listener) {
        mPrintListener = listener;
    }

    public PrintBusinessModel(Context context, MyHandler<BaseActivity> handler) {
        super(context, handler);
    }

    /**
     * @desc: 打印主方法
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/16 9:09
     */
    public void onPrint(final PrintInfo info) {

        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                sendFile(info);
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();
    }

    /**
     * @desc: 批量打印条码数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/3 11:12
     */
    public void onPrint(final List<PrintInfo> list) {

        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                sendFileByList(list);
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();
    }

    public void sendFileByList(List<PrintInfo> list) {
        if (list != null && list.size() > 0) {
            sendFile(list);
//            for (PrintInfo info : list) {
//                if (!sendFile(info)) {
//                    break;
//                }
//            }
        }
    }

    public boolean sendFile(PrintInfo info) {
        try {
            readBluetoothPrinterMacAddressShare(mContext);
            mMac = UrlInfo.mBluetoothPrinterMacAddress;
            Connection connection = null;
            if (CommonCheckUtil.isEmptyOrNull(mMac)) {
                MessageBox.Show(mContext, "蓝牙地址不存在，是否已保存?");
                return false;
            }
            if (info == null) {
                MessageBox.Show(mContext, "打印的数据信息不能为空");
                return false;
            }
            if (info.getPrintType() == null || info.getPrintType().equals("")) {
                MessageBox.Show(mContext, "打印的模板类型不能为空");
                return false;
            }
            connection = new BluetoothConnection(mMac);
            connection.open();
            ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
            sendFile(printer, info);
            connection.close();
            if (mPrintListener != null) {
                mPrintListener.afterPrint();
            }
        } catch (ConnectionException e) {
            MessageBox.Show(mContext, e.getMessage());
            return false;
        } catch (ZebraPrinterLanguageUnknownException e) {
            MessageBox.Show(mContext, e.getMessage());
            return false;


        }
        return true;
    }

    /**
     * @desc: 批量打印数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/8/14 17:22
     */
    public boolean sendFile(List<PrintInfo> list) {
        try {
            readBluetoothPrinterMacAddressShare(mContext);
            mMac = UrlInfo.mBluetoothPrinterMacAddress;
            Connection connection = null;
            if (CommonCheckUtil.isEmptyOrNull(mMac)) {
                MessageBox.Show(mContext, "蓝牙地址不存在，是否已保存?");
                return false;
            }
            if (list == null ||list.size()==0) {
                MessageBox.Show(mContext, "打印的数据信息不能为空");
                return false;
            }
            if (list.get(0).getPrintType() == null || list.get(0).getPrintType().equals("")) {
                MessageBox.Show(mContext, "打印的模板类型不能为空");
                return false;
            }
            connection = new BluetoothConnection(mMac);
            connection.open();
            ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
            for (PrintInfo printInfo : list) {
                sendFile(printer, printInfo);
                Thread.sleep(200);
            }
            connection.close();
            if (mPrintListener != null) {
                mPrintListener.afterPrint();
            }
        } catch (ConnectionException e) {
            MessageBox.Show(mContext, e.getMessage());
            return false;
        } catch (ZebraPrinterLanguageUnknownException e) {
            MessageBox.Show(mContext, e.getMessage());
            return false;


        } catch (InterruptedException e) {
            MessageBox.Show(mContext, e.getMessage());
            return false;

        }
        return true;
    }

    private void sendFile(ZebraPrinter printer, PrintInfo info) {
        try {
            File filepath = mContext.getFileStreamPath("TEST.LBL");
            createFile(printer, "TEST.LBL", info);
            printer.sendFileContents(filepath.getAbsolutePath());
            SettingsHelper.saveBluetoothAddress(mContext, mMac);
        } catch (ConnectionException e1) {
            MessageBox.Show(mContext, "Error creating file");

        } catch (IOException e) {
            MessageBox.Show(mContext, "Error creating file");

        }
    }

    private void createFile(ZebraPrinter printer, String fileName, PrintInfo info) throws IOException {
        FileOutputStream os = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
        byte[] configLabel = null;
        PrinterLanguage pl = printer.getPrinterControlLanguage();
        String command = getPrintStyle(info);
//        configLabel = command.getBytes();
        if (command != null) {
            configLabel = command.getBytes("GB2312");
            os.write(configLabel);
            os.flush();
            os.close();
        }

    }

    /**
     * @desc: 托盘标签样式
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 21:53
     */
    public String getPalletLabelStyle(PrintInfo info) {
        String materialNo = info.getMaterialNo() != null ? info.getMaterialNo() : "";
        ;   //
        String materialDesc = info.getMaterialDesc() != null ? info.getMaterialDesc() : "";
        float infoQty = info.getQty();
        String qty = Math.round(infoQty) + "";
        String arrivalTime = info.getArrivalTime() != null ? info.getArrivalTime() : "";
        String batchNo = info.getBatchNo() != null ? info.getBatchNo() : "";
        String signatory = info.getSignatory() != null ? info.getSignatory() : "";
        String QRCode = info.getQRCode() != null ? info.getQRCode() : "";
        String command = "! 0 200 200 400 1\r\n" +
                "ENCODING GB18030\r\n" +
                "B QR 400 20 M 2 U 4\r\n" +
                "MM,B0050X_QR_CODE\r\n" +
                "ENDQR\r\n" +
                "T GBUNSG24.CPF 0 70 15 物料编号:X_MATERIAL_NO\r\n" +
                "T GBUNSG24.CPF 0 70 60 数    量:X_QTY\r\n" +
                "T GBUNSG24.CPF 0 70 105 批    次:X_BATCH_NO\r\n" +
                "T GBUNSG24.CPF 0 70 150 到货时间:X_ARRIVAL_TIME\r\n" +
                "T GBUNSG24.CPF 0 70 195 签收人:X_SIGNATORY\r\n" +
                "PRINT\r\n";
        command = command.replace("X_QR_CODE", QRCode).replace("X_MATERIAL_NO", materialNo)
                .replace("X_QTY", qty).replace("X_BATCH_NO", batchNo)
                .replace("X_ARRIVAL_TIME", arrivalTime).replace("X_SIGNATORY", signatory);
        return command;
    }

    /**
     * @desc: 原料标签样式
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 21:53
     */
    public String getRawMaterialLabelStyle(PrintInfo info) {
        int maxSingleLength = 10;
        String materialNo = info.getMaterialNo() != null ? info.getMaterialNo() : "";
        String materialDesc = info.getMaterialDesc() != null ? info.getMaterialDesc() : "";
        String batchNo = info.getBatchNo() != null ? info.getBatchNo() : "";
        String spec = info.getSpec() != null ? info.getSpec() : "";
        int barcodeQty = (int) info.getQty();
        float packQty = info.getPackQty();

        String QRCode = info.getQRCode() != null ? info.getQRCode() : "";
        String materialDesc1 = "";
        String materialDesc2 = "";
        if (!materialDesc.equals("")) {

            if (materialDesc.length() > maxSingleLength) {
                materialDesc1 = materialDesc.substring(0, maxSingleLength);
                materialDesc2 = materialDesc.substring(maxSingleLength, materialDesc.length());
            } else {
                materialDesc1 = materialDesc;
            }
        }
        String part1 = "! 0 200 200 400 1\r\n" +
                "ENCODING GB18030\r\n" +
                "B QR 400 20 M 2 U 4\r\n" +
                "MM,B0050X_QR_CODE\r\n" +
                "ENDQR\r\n" +
                "T GBUNSG24.CPF 0 70 15 料号:X_MATERIAL_NO\r\n";

        String part2 = "";
        if (materialDesc2.equals("")) {

            part2 = "T GBUNSG24.CPF 0 70 60 品名:X_MATERIAL_DESC\r\n" +
                    "T GBUNSG24.CPF 0 70 105 规格:X_SPEC\r\n";
        } else {
            part2 = "T GBUNSG24.CPF 0 70 60 品名:X_MATERIAL_DESC\r\n" +
                    "T GBUNSG24.CPF 0 120 90  SECOND_MATERIAL_DESC \r\n" +
                    "T GBUNSG24.CPF 0 70 115 规格:X_SPEC\r\n";
        }

        String part3 = "T GBUNSG24.CPF 0 70 150 批次:X_BATCH_NO\r\n" +
                "T GBUNSG24.CPF 0 70 195 数量:X_QTY\r\n" +
                "PRINT\r\n";
        String command = part1 + part2 + part3;


        command = command.replace("X_MATERIAL_NO", materialNo).replace("X_MATERIAL_DESC", materialDesc1).replace("SECOND_MATERIAL_DESC", materialDesc2)
                .replace("X_SPEC", spec).replace("X_BATCH_NO", batchNo)
                .replace("X_QTY", packQty + "").replace("X_QR_CODE", QRCode);
        return command;
    }


    private void createDemoFile(ZebraPrinter printer, String fileName) throws IOException {

        FileOutputStream os = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);

        byte[] configLabel = null;

        PrinterLanguage pl = printer.getPrinterControlLanguage();
        if (pl == PrinterLanguage.ZPL) {
            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
        } else if (pl == PrinterLanguage.CPCL) {
            String cpclConfigLabel = "! 0 200 200 406 1\r\n" + "ON-FEED IGNORE\r\n" + "BOX 20 20 380 380 8\r\n" + "T 0 6 137 177 TEST\r\n" + "PRINT\r\n";
            configLabel = cpclConfigLabel.getBytes();
        }
        os.write(configLabel);
        os.flush();
        os.close();
    }

    /**
     * @desc: 更根据不同的打印类型获取指令
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/15 22:04
     */
    public String getPrintStyle(PrintInfo printInfo) {
        String command = null;
        if (printInfo != null) {
            String printType = printInfo.getPrintType();
            if (printType != null) {
                if (printType.equals(PrintType.PRINT_TYPE_PALLET_STYLE)) {
                    command = getPalletLabelStyle(printInfo);
                } else if (printType.equals(PrintType.PRINT_TYPE_RAW_MATERIAL_STYLE)) {
                    command = getRawMaterialLabelStyle(printInfo);
                }
            }
        }
        return command;
    }

    public void EZ320DEMO() {
//        "T GBUNSG24.CPF 0 70 15 物料编号:130100002\r\n" +
//                "T GBUNSG24.CPF 0 70 15 物料编号:130100002\r\n" +
//                "T GBUNSG16.CPF 0 288 101 签名:\r\n" +

        String cpclConfigLabel = "! 0 200 200 400 1\r\n" +
                "ENCODING GB18030\r\n" +
                "B QR 400 20 M 2 U 4\r\n" +
                "MM,B00502020010013%20200703%130%2\r\n" +
                "ENDQR\r\n" +
                "T GBUNSG24.CPF 0 70 15 物料编号:130100002\r\n" +
                "T GBUNSG24.CPF 0 70 60 数    量:30\r\n" +
                "T GBUNSG24.CPF 0 70 105 批    次:20200715\r\n" +
                "T GBUNSG24.CPF 0 70 150 到货时间:2020-7-15\r\n" +
                "T GBUNSG24.CPF 0 70 195 签收人:博多工贸有限公司\r\n" +
                "PRINT\r\n";
    }

    /**
     * @desc: 打印前校验打印设置
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/16 10:32
     */
    public boolean checkBluetoothSetting() {
        boolean isAccess = true;
        readBluetoothPrinterMacAddressShare(mContext);
        mMac = UrlInfo.mBluetoothPrinterMacAddress;
        if (CommonCheckUtil.isEmptyOrNull(mMac)) {
            MessageBox.Show(mContext, "蓝牙地址不存在，请到设置页面设置?");
            isAccess = false;
        }

        return isAccess;
    }

}
