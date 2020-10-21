package com.liansu.boduowms.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liansu.boduowms.base.BaseApplication;
import com.liansu.boduowms.bean.base.UrlInfo;
import com.liansu.boduowms.bean.user.UserInfo;
import com.liansu.boduowms.bean.warehouse.WareHouseInfo;
import com.liansu.boduowms.modules.setting.newSystem.NewSettingSystemPresenter;
import com.liansu.boduowms.utils.Network.RequestHandler;

import java.lang.reflect.Type;


/**
 * Created by GHOST on 2017/2/3.
 */

public class SharePreferUtil {

    public static void ReadShare(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            UrlInfo.ElecIP = sharedPreferences.getString("ElecIP", "1.1.1.1");
            UrlInfo.isWMS = sharedPreferences.getBoolean("isWMS", true);
            RequestHandler.SOCKET_TIMEOUT = sharedPreferences.getInt("TimeOut", 20000);
            UrlInfo.mOfficialEnvironmentIpAddress=sharedPreferences.getString("OfficialEnvironmentIpAddress","http://172.19.106.190:7001/api/");
            UrlInfo.mTestEnvironmentIpAddress=sharedPreferences.getString("TestEnvironmentIpAddress","http://172.19.106.230:5001/api/");
            UrlInfo.IPAdress = sharedPreferences.getString("IPAddress", "");
            UrlInfo.Port = sharedPreferences.getInt("Port", -1);
            UrlInfo.LastContent=sharedPreferences.getString("LastContent","");
            UrlInfo.PrintIP = sharedPreferences.getString("PrintIP", "1.1.1.1");
            UrlInfo.mEnvironmentType=sharedPreferences.getInt(   "EnvironmentType", NewSettingSystemPresenter.URL_TYPE_OFFICIAL_ENVIRONMENT);
        }
    }

    public static void SetShare(Context context, String IPAdress, String PrintIP, String ElecIP, Integer Port, Integer TimeOut, Boolean isWMS) {
        ElecIP = "1.1.1.1";
        PrintIP = ElecIP;
        SharedPreferences sharedPreferences = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("IPAddress", IPAdress);
        edit.putString("PrintIP", PrintIP);
        edit.putString("ElecIP", ElecIP);
        edit.putInt("Port", Port);
        edit.putInt("TimeOut", TimeOut);
        edit.putBoolean("isWMS", isWMS);
        edit.commit();
        UrlInfo.IPAdress = IPAdress;
        UrlInfo.PrintIP = PrintIP;
        UrlInfo.ElecIP = ElecIP;
        UrlInfo.Port = Port;
        UrlInfo.isWMS = isWMS;
        RequestHandler.SOCKET_TIMEOUT = TimeOut;
    }

    public static void setSystemSettingShare(Context context,String IPAddress,int port,String lastContent,int timeOut,String officialEnvironmentIpAddress,String testEnvironmentIpAddress,int environmentType){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("IPAddress", IPAddress);
        edit.putInt("Port", port);
        edit.putString("LastContent",lastContent);
        edit.putInt("TimeOut", timeOut);
        edit.putString("OfficialEnvironmentIpAddress",officialEnvironmentIpAddress);
        edit.putString("TestEnvironmentIpAddress",testEnvironmentIpAddress);
        edit.putInt("EnvironmentType",environmentType);
        edit.commit();
        UrlInfo.IPAdress = IPAddress;
        UrlInfo.Port = port;
        UrlInfo.LastContent=lastContent;
        UrlInfo.mOfficialEnvironmentIpAddress=officialEnvironmentIpAddress;
        UrlInfo.mTestEnvironmentIpAddress=testEnvironmentIpAddress;
        UrlInfo.mEnvironmentType=environmentType;
        RequestHandler.SOCKET_TIMEOUT = timeOut;
    }

    public static void SetSupplierShare(Context context, boolean isSupplier) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SupplierSetting", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean("isSupplier", isSupplier);
        edit.commit();
        UrlInfo.isSupplier = isSupplier;
    }

    public static void ReadSupplierShare(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SupplierSetting", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            UrlInfo.isSupplier = sharedPreferences.getBoolean("isSupplier", false);
        }
    }

    public static void ReadUserShare(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<UserInfo>() {
            }.getType();
            BaseApplication.mCurrentUserInfo = gson.fromJson(sharedPreferences.getString("User", ""), type);
        }
    }

    public static void SetUserShare(Context context, UserInfo user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Gson gson = new Gson();
        Type type = new TypeToken<UserInfo>() {
        }.getType();
        edit.putString("User", gson.toJson(user, type));
        edit.commit();
        BaseApplication.mCurrentUserInfo = user;
    }

    public static void SetWareHouseInfoShare(Context context, WareHouseInfo wareHouseInfo) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("WareHouse", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Gson gson = new Gson();
        Type type = new TypeToken<WareHouseInfo>() {
        }.getType();
        edit.putString("WareHouse", gson.toJson(wareHouseInfo, type));
        edit.commit();
        BaseApplication.mCurrentWareHouseInfo = wareHouseInfo;
    }

    public static void ReadWareHouseInfoShare(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("WareHouse", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<WareHouseInfo>() {
            }.getType();
            BaseApplication.mCurrentWareHouseInfo = gson.fromJson(sharedPreferences.getString("WareHouse", ""), type);
        }
    }

    /**
     * @desc: 保存激光打印机和台式打印机地址
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/30 15:02
     */

    public static void setPrinterAddressShare(Context context, String laserPrinterAddress, String desktopPrintAddress) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("LaserPrinterAddress", laserPrinterAddress);
        edit.putString("DesktopPrintAddress", desktopPrintAddress);
        edit.commit();
        UrlInfo.mLaserPrinterAddress = laserPrinterAddress;
        UrlInfo.mDesktopPrintAddress = desktopPrintAddress;
    }

    /**
     * @desc: 保存出入库打印方式
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/30 15:01
     */
    public static void setBusinessPrinterType(Context context, int inStockPrintType, String inStockPrintAddress, int outStockPrintType, String outStockPrintAddress,int outStockPackingBoxPrintType, String outStockPackingBoxPrintAddress) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("InStockPrintType", inStockPrintType);
        edit.putInt("OutStockPrintType", outStockPrintType);
        edit.putInt("OutStockPackingBoxPrintType", outStockPackingBoxPrintType);
        edit.putString("InStockPrintAddress", inStockPrintAddress);
        edit.putString("OutStockPrintAddress", outStockPrintAddress);
        edit.putString("OutStockPackingBoxPrintAddress", outStockPackingBoxPrintAddress);
        edit.commit();
        UrlInfo.mInStockPrintType = inStockPrintType;
        UrlInfo.mOutStockPrintType = outStockPrintType;
        UrlInfo.mOutStockPackingBoxPrintType = outStockPackingBoxPrintType;
        UrlInfo.mInStockPrintName = inStockPrintAddress;
        UrlInfo.mOutStockPrintName = outStockPrintAddress;
        UrlInfo.mOutStockPackingBoxPrintName=outStockPackingBoxPrintAddress;
    }

    public static void setBluetoothPrinterMacAddressShare(Context context, String macAddress) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("MacAddress", macAddress);
        edit.commit();
        UrlInfo.mBluetoothPrinterMacAddress = macAddress;
    }


    public static void readBluetoothPrinterMacAddressShare(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            UrlInfo.mBluetoothPrinterMacAddress = sharedPreferences.getString("MacAddress", "1.1.1.1");
        }
    }

    /**
     * @desc: 读取打印设置的数据
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/7/30 15:06
     */
    public static void ReadPrintSettingShare(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            UrlInfo.mBluetoothPrinterMacAddress = sharedPreferences.getString("MacAddress", "1.1.1.1");
            UrlInfo.mLaserPrinterAddress = sharedPreferences.getString("LaserPrinterAddress", "1.1.1.1");
            UrlInfo.mDesktopPrintAddress = sharedPreferences.getString("DesktopPrintAddress", "1.1.1.1");
            UrlInfo.mInStockPrintType = sharedPreferences.getInt("InStockPrintType", -1);
            UrlInfo.mOutStockPrintType = sharedPreferences.getInt("OutStockPrintType", -1);
            UrlInfo.mOutStockPackingBoxPrintType = sharedPreferences.getInt("OutStockPackingBoxPrintType", -1);
            UrlInfo.mInStockPrintName=sharedPreferences.getString("InStockPrintAddress", "");
            UrlInfo.mOutStockPrintName=sharedPreferences.getString("OutStockPrintAddress", "");
            UrlInfo.mOutStockPackingBoxPrintName=sharedPreferences.getString("OutStockPackingBoxPrintAddress", "");

        }
    }

    /**
     * @desc:在Debug时数据离线的状态
     * @param:
     * @return:
     * @author: Nietzsche
     * @time 2020/6/25 19:33
     */
    public static void SetDebugDataStatusShare(Context context, int dataLevel) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("DataLevel", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("DataLevel", dataLevel);
        edit.commit();
        BaseApplication.mDebugDataStatus = dataLevel;
    }

}
