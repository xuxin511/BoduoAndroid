package com.liansu.boduowms.utils.updateversion;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.liansu.boduowms.BuildConfig;
import com.liansu.boduowms.R;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.ui.dialog.MessageBox;
import com.liansu.boduowms.utils.ParseXmlService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import androidx.core.content.FileProvider;


/**
 * 检测安装更新文件的助手类
 *
 * @author Administrator
 */
public class UpdateVersionService {
    private static final int                     ERROR_MESSAGE = -1;// 报错
    private static final int                     DOWN          = 1;// 用于区分正在下载
    private static final int                     DOWN_FINISH   = 0;// 用于区分下载完成
    private              HashMap<String, String> hashMap;// 存储跟心版本的xml信息
    private              String                  fileSavePath;// 下载新apk的厨房地点
    // private static final String UPDATEVERSIONXMLPATH = UserConfigModel.UPDATEURL+"version.xml";
//    public final static  String                  LastContent = "/AppData/version.xml";
    public final static  String                  LastContent   = "/AppData/version.json";
    public final static  String                  APK_PATH      = "/AppData/BODUO.apk";

    public static String UP_DATE_VERSION_XML_PATH() {
        return "http://" + UrlInfo.IPAdress + ":" + UrlInfo.Port + "/" + LastContent;
    }

    public static String UP_DATE_DOWN_LOAD_PATH() {
        return "http://" + UrlInfo.IPAdress + ":" + UrlInfo.Port + "/" + APK_PATH;
    }

    private boolean  cancelUpdate = false;// 是否取消下载
    //private Context context;
    private TextView txtVersion;
    private Dialog   downLoadDialog;
    private Handler  handler      = new Handler() {// 跟心ui

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case DOWN:
                    //  progressBar.setProgress(progress);
                    break;
                case DOWN_FINISH:
                    Toast.makeText(BaseApplication.context, "文件下载完成,正在安装更新", Toast.LENGTH_SHORT).show();
                    installAPK();
                    break;
                case ERROR_MESSAGE:
                    MessageBox.Show(BaseApplication.context,  msg.obj.toString());
                    if (downLoadDialog != null && downLoadDialog.isShowing()) {
                        downLoadDialog.dismiss();
                    }
                default:
                    break;
            }
        }

    };

    /**
     * 构造方法
     *
     * @param context 比较版本的xml文件地址(服务器上的)
     * @param context 上下文
     */
    public UpdateVersionService(Context context) {
        super();
        // this.context = context;
    }


    /**
     * 检测是否可更新
     *
     * @return
     */
    public boolean checkUpdate() {
        if (isUpdate()) {
            showUpdateVersionDialog();// 显示提示对话框
            showDownloadDialog();
            return true;
        } else {
            Toast.makeText(BaseApplication.context, "已经是新版本", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * 更新提示框
     */
    private void showUpdateVersionDialog() {
        // 构造对话框
        Builder builder = new Builder(BaseApplication.context);
        builder.setTitle("软件更新");
        builder.setMessage("检测到新版本,是否下载更新");
        // 更新
        builder.setPositiveButton("更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton("稍后更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 下载的提示框
     */
    public void showDownloadDialog() {
        {
            // 构造软件下载对话框
            Builder builder = new Builder(BaseApplication.context);
            builder.setTitle("存在新版本，正在更新");
            // 给下载对话框增加进度条
            final LayoutInflater inflater = LayoutInflater.from(BaseApplication.context);
            View v = inflater.inflate(R.layout.downloaddialog, null);
            txtVersion = (TextView) v.findViewById(R.id.txtVersion);
            txtVersion.setText("当前版本：" + getVersionCode(BaseApplication.context));
            builder.setView(v);
            // 取消更新
            builder.setNegativeButton("取消", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    // 设置取消状态
                    cancelUpdate = true;
                }
            });
            downLoadDialog = builder.create();
            downLoadDialog.show();
            // 现在文件
            downloadApk();
        }

    }

    /**
     * 下载apk,不能占用主线程.所以另开的线程
     */
    private void downloadApk() {
        new downloadApkThread().start();

    }

    /**
     * 判断是否可更新
     *
     * @return
     */
    public boolean isUpdate() {
        double versionCode = getVersionCode(BaseApplication.context);
        try {
            // 把version.xml放到网络上，然后获取文件信息
            URL url = new URL(UP_DATE_VERSION_XML_PATH());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setRequestMethod("GET");// 必须要大写
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "GB2312"));
            String strRead = null;
            StringBuffer sbf = new StringBuffer();
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            String result = sbf.toString();
            // 解析XML文件。
            ParseXmlService service = new ParseXmlService();
            hashMap = service.parseJson(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != hashMap) {
            double serverCode = Double.valueOf(hashMap.get("versionCode"));
            // 版本判断
            if (serverCode > versionCode) {
                //   Toast.makeText(context, "新版本是: " + serverCode, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;

    }

    /**
     * 获取当前版本和服务器版本.如果服务器版本高于本地安装的版本.就更新
     *
     * @param context2
     * @return
     */
    public Double getVersionCode(Context context2) {
        double versionCode = 0.0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
//            versionCode = Double.parseDouble(context.getPackageManager().getPackageInfo("com.xx.chinetek.cywms", 0).versionName);
            versionCode = Double.parseDouble(BuildConfig.VERSION_NAME);
            //Toast.makeText(context, "当前版本是: " + versionCode, Toast.LENGTH_SHORT).show();
//        } catch (NameNotFoundException e) {
//            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;

    }

//    /**
//     * 安装apk文件
//     */
//    private void installAPK() {
//        File apkfile = new File(fileSavePath, hashMap.get("fileName") + ".apk");
//        if (!apkfile.exists()) {
//            return;
//        }
//        // 通过Intent安装APK文件
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        System.out.println("filepath=" + apkfile.toString() + "  " + apkfile.getPath());
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
//        context.startActivity(i);
//        android.os.Process.killProcess(android.os.Process.myPid());// 如果不加上这句的话在apk安装完成之后点击单开会崩溃
//
//    }

    /**
     * 安装apk文件
     */
    private void installAPK() {
        try {
            File apkfile = new File(fileSavePath, hashMap.get("fileName") + ".apk");
            if (!apkfile.exists()) {
                return;
            }

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri url;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // Android7.0及以上版本
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                url = FileProvider.getUriForFile(BaseApplication.context, BaseApplication.context.getPackageName() + ".fileProvider", apkfile);
            } else {
                url = Uri.fromFile(apkfile);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            }
            intent.setDataAndType(url, "application/vnd.android.package-archive");
            BaseApplication.context.startActivity(intent);
            //最后杀死进程
            if (handler != null) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }, 200);
            }
//        context.startActivity(intent);
//            android.os.Process.killProcess(android.os.Process.myPid());// 如果不加上这句的话在apk安装完成之后点击单开会崩溃
        } catch (Exception e) {
            MessageBox.Show(BaseApplication.context, "安装APK出现预期之外的异常:" + e.getMessage());
        }
    }


    /**
     * 卸载应用程序(没有用到)
     */
    public void uninstallAPK() {
        Uri packageURI = Uri.parse("package:com.example.updateversion");
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        BaseApplication.context.startActivity(uninstallIntent);

    }

    /**
     * 下载apk的方法
     *
     * @author rongsheng
     */
    public class downloadApkThread extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            try {

                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    fileSavePath = sdpath + "download";
                    URL url = new URL(UP_DATE_DOWN_LOAD_PATH());
//                    URL url1 = new URL(hashMap.get("loadUrl"));
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(5 * 1000);// 设置超时时间
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Charser", "GBK,utf-8;q=0.7,*;q=0.3");
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(fileSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(fileSavePath, hashMap.get("fileName") + ".apk");
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        // 更新进度
                        Message message = new Message();
                        message.what = DOWN;
                        handler.sendMessage(message);
                        if (numread <= 0) {
                            // 下载完成
                            // 取消下载对话框显示
                            downLoadDialog.dismiss();
                            Message message2 = new Message();
                            message2.what = DOWN_FINISH;
                            handler.sendMessage(message2);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                Message message = new Message();
                message.what = ERROR_MESSAGE;
                message.obj = "更新地址不正确!请到设置页面进行设置。message:" + e.getMessage();
                handler.sendMessage(message);
                e.printStackTrace();
            } catch (IOException e) {
                Message message = new Message();
                message.what = ERROR_MESSAGE;
                message.obj = "更新地址不正确!请到设置页面进行设置。message:" + e.getMessage();
                handler.sendMessage(message);
                e.printStackTrace();
            }

        }

    }


}  