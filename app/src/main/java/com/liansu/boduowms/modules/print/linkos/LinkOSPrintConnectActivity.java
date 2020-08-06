package com.liansu.boduowms.modules.print.linkos;

import android.content.Context;
import android.os.Looper;

import com.liansu.boduowms.base.BaseActivity;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.modules.print.PrintCallBackListener;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.function.CommonCheckUtil;
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

import static com.liansu.boduowms.utils.SharePreferUtil.readBluetoothPrinterMacAddressShare;

/**
 * @ Des:
 * @ Created by yangyiqing on 2019/5/30.
 */
public abstract class LinkOSPrintConnectActivity extends BaseActivity {
    public  String                mMac = "";
    private PrintCallBackListener mPrintListener;


    public void setPrintCallBackListener(PrintCallBackListener listener) {
        mPrintListener = listener;
    }

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

    public void sendFile(PrintInfo info) {
        try {
            readBluetoothPrinterMacAddressShare(LinkOSPrintConnectActivity.this);
            mMac = UrlInfo.mBluetoothPrinterMacAddress;
            Connection connection = null;
            if (CommonCheckUtil.isEmptyOrNull(mMac)) {
                MessageBox.Show(LinkOSPrintConnectActivity.this, "蓝牙地址不存在，是否已保存?" );
                return;
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
            MessageBox.Show(LinkOSPrintConnectActivity.this, e.getMessage() );
        } catch (ZebraPrinterLanguageUnknownException e) {
            MessageBox.Show(LinkOSPrintConnectActivity.this, e.getMessage() );


        }
    }

    private void sendFile(ZebraPrinter printer, PrintInfo info) {
        try {
            File filepath = getFileStreamPath("TEST.LBL");
            createFile(printer, "TEST.LBL", info);
            printer.sendFileContents(filepath.getAbsolutePath());
            SettingsHelper.saveBluetoothAddress(this, mMac);
        } catch (ConnectionException e1) {
            MessageBox.Show(LinkOSPrintConnectActivity.this, "Error creating file" );

        } catch (IOException e) {
            MessageBox.Show(LinkOSPrintConnectActivity.this, "Error creating file" );

        }
    }

    private void createFile(ZebraPrinter printer, String fileName, PrintInfo info) throws IOException {
        FileOutputStream os = this.openFileOutput(fileName, Context.MODE_PRIVATE);
        byte[] configLabel = null;
        PrinterLanguage pl = printer.getPrinterControlLanguage();
        String cpclConfigLabel = getPrintStyle(info);
        String command = cpclConfigLabel;
//        configLabel = command.getBytes();
        configLabel = command.getBytes("GB2312");
        ;
        os.write(configLabel);
        os.flush();
        os.close();
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
        command = command.replace("X_QR_CODE", QRCode).replace("  X_MATERIAL_NO", materialNo)
                .replace("X_QTY", qty).replace("X_BATCH_NO", batchNo)
                .replace("X_ARRIVAL_TIME", arrivalTime).replace("X_SIGNATORY", signatory);
        return command;
    }


    private void createDemoFile(ZebraPrinter printer, String fileName) throws IOException {

        FileOutputStream os = this.openFileOutput(fileName, Context.MODE_PRIVATE);

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

}
